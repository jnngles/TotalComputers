package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.sound.SoundManager;
import com.jnngl.totalcomputers.sound.SoundWebServer;
import com.jnngl.totalcomputers.sound.discord.DiscordBot;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ElementList;
import com.jnngl.totalcomputers.system.ui.Field;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@RequiresAPI(apiLevel = 6)
public class JukeboxApplication extends WindowApplication {

    private ElementList music;
    private Button play, pause;
    private Field url;

    private boolean isPlayingByThis = false;

    public static final String[] SUPPORTED_FORMATS = {
            "mp3", "flac", "wav", "aac", "opus", "vorbis", "mp4", "m4a", "ogg", "aac"
    };

    public static void main(String[] args) {
        ApplicationHandler.open(JukeboxApplication.class, args[0], args);
    }

    public JukeboxApplication(TotalOS os, String path, String[] args) {
        super(os, "Jukebox", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    public void pause() {
        if(isPlayingByThis && !TotalOS.audio.isPaused()) TotalOS.audio.pause();
    }

    public void resume() {
        if(isPlayingByThis && TotalOS.audio.isPaused() && TotalOS.audio.isPlaying()) TotalOS.audio.resume();
    }

    public void stop() {
        if(isPlayingByThis && TotalOS.audio.isPlaying()) TotalOS.audio.stop();
    }

    @Override
    protected void onStart() {
        if(!TotalOS.isDiscordBotAvailable()) {
            os.information.displayMessage(Information.Type.ERROR, "Discord bot is disabled on this server",
                    this::close);
            return;
        }

        Font font = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        play = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(),
                Utils.getFontMetrics(font).getHeight(), font, "Play/Stop");
        pause = new Button(Button.ButtonColor.BLUE, 0, play.getHeight(), getWidth(),
                play.getHeight(), font, "Pause/Resume");
        url = new Field(0, pause.getHeight()*2, getWidth(), play.getHeight(), font, "", "URL (Optional)", os.keyboard);
        music = new ElementList(0, pause.getHeight()*3, getWidth(),
                getHeight()-pause.getHeight()*2, font);

        pause.registerClickEvent(() -> {
            if(TotalOS.audio.isPlaying() && !isPlayingByThis) return;
            if(TotalOS.audio.isPaused()) resume();
            else pause();
        });

        DiscordBot.global_player.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                isPlayingByThis = false;
            }
        });

        play.registerClickEvent(() -> {
            if(TotalOS.audio.isPlaying() && !isPlayingByThis) return;
            if(!TotalOS.audio.isPlaying()) {
                if(music.getSelectedIndex() < 0) return;
                TotalOS.audio.loadMusic((!url.isEmpty())? url.getText() : os.fs.toFile(music.getSelectedElement().startsWith("Desktop: ") ?
                                "/usr/Desktop/" + music.getSelectedElement().substring(9) :
                                "/usr/Applications/Jukebox.app/" + music.getSelectedElement().substring(5)).getPath(),
                        new AudioLoadResultHandler() {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                if(!TotalOS.audio.isPlaying()) {
                                    TotalOS.audio.play(track);
                                    isPlayingByThis = true;
                                }
                            }

                            @Override
                            public void playlistLoaded(AudioPlaylist playlist) {

                            }

                            @Override
                            public void noMatches() {

                            }

                            @Override
                            public void loadFailed(FriendlyException exception) {

                            }
                        });
            } else stop();
        });

        for(String file : os.fs.toFile("/usr/Desktop").list()) {
            for(String ext : SUPPORTED_FORMATS) {
                if(file.endsWith("."+ext)) {
                    music.addEntry("Desktop: "+file);
                    break;
                }
            }
        }

        for(String file : os.fs.toFile("/usr/Applications/Jukebox.app").list()) {
            for(String ext : SUPPORTED_FORMATS) {
                if(file.endsWith("."+ext)) {
                    music.addEntry("App: "+file);
                    break;
                }
            }
        }

        addResizeEvent(new ResizeEvent() {
            private void handleResize() {
                play.setWidth(getWidth());
                url.setWidth(getWidth());
                pause.setWidth(getWidth());
                music.setWidth(getWidth());
                music.setHeight(getHeight()-play.getHeight()*2);
            }

            @Override
            public void onResize(int width, int height) {
                handleResize();
            }

            @Override
            public void onMaximize(int width, int height) {
                handleResize();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                handleResize();
            }
        });
    }

    @Override
    protected boolean onClose() {
        if(isPlayingByThis)
            TotalOS.audio.stop();
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        music.render(g);
        play.render(g);
        pause.render(g);
        url.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        music.processInput(x, y, type);
        play.processInput(x, y, type);
        pause.processInput(x, y, type);
        url.processInput(x, y, type);
    }
}
