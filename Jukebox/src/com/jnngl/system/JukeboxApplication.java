package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.sound.SoundManager;
import com.jnngl.totalcomputers.sound.SoundWebServer;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ElementList;

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

@RequiresAPI(apiLevel = 4)
public class JukeboxApplication extends WindowApplication {

    private ElementList music;
    private Button play, pause;
    private String current;
    private boolean playing;
    private Thread timer;

    public static final String[] SUPPORTED_FORMATS = {
            "mp3", "mpeg", "opus", "ogg", "oga", "wav", "aac", "caf", "m4a", "mp4", "weba", "webm", "dolby", "flac"
    };

    public static void main(String[] args) {
        ApplicationHandler.open(JukeboxApplication.class, args[0], args);
    }

    private void start(File file) {
        current = System.currentTimeMillis()+file.getName();
        try {
            Files.copy(file.toPath(), new File(SoundWebServer.HTTDOCS, "sounds/"+current).toPath());
        } catch (IOException e) {
            System.err.println("Failed to copy file");
        }
        resume();
        play.setText("Stop");
        play.setLocked(true);
        pause.setLocked(true);
        new Thread(() -> {
            int elapsed = 0;
            long start = System.currentTimeMillis();
            long lastTime = System.currentTimeMillis();
            float duration = 0;
            while(elapsed < 10) {
                SoundManager.play(current);
                duration = SoundManager.getDuration(current);
                elapsed = (int) ((System.currentTimeMillis() - start) / 1000);
                if (duration > 0) break;
                while (System.currentTimeMillis() - lastTime < 500);
                lastTime = System.currentTimeMillis();
            }
            if(duration <= 0) {
                super.close();
                return;
            }
            duration--;
            play.setLocked(false);
            pause.setLocked(false);
            System.out.println("Remaining: "+duration);
            resume();

            lastTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - lastTime < duration*1000);
            current = null;
            playing = false;
            play.setText("Play");
            play.setLocked(true);
            pause.setLocked(true);
            lastTime = System.currentTimeMillis();
            while(System.currentTimeMillis() - lastTime < 2500);
            play.setLocked(false);
            if(!new File(SoundWebServer.HTTDOCS, "sounds/"+current).delete())
                new File(SoundWebServer.HTTDOCS, "sounds/"+current).deleteOnExit();
        }).start();
    }

    private void pause() {
        if(!playing) return;
        SoundManager.play(current);
        playing = false;
        pause.setText("Resume");
        pause.setLocked(false);
    }

    private void resume() {
        if(playing) return;
        SoundManager.play(current);
        playing = true;
        pause.setText("Pause");
        pause.setLocked(false);
    }

    private void stop() {
        pause();
        current = null;
        play.setText("Play");
        pause.setLocked(true);
        if(!new File(SoundWebServer.HTTDOCS, "sounds/"+current).delete())
            new File(SoundWebServer.HTTDOCS, "sounds/"+current).deleteOnExit();
    }

    public JukeboxApplication(TotalOS os, String path, String[] args) {
        super(os, "Jukebox", os.screenWidth/3*2, os.screenHeight/3*2, path);
        if(args.length == 2) {
            start(os.fs.toFile(args[1]));
        }
    }

    @Override
    protected void onStart() {
        Font font = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        play = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(),
                Utils.getFontMetrics(font).getHeight(), font, "Play");
        pause = new Button(Button.ButtonColor.BLUE, 0, play.getHeight(), getWidth(),
                play.getHeight(), font, "Pause");
        pause.setLocked(true);
        music = new ElementList(0, pause.getHeight()*2, getWidth(),
                getHeight()-pause.getHeight()*2, font);

        pause.registerClickEvent(() -> {
            if(pause.getText().equalsIgnoreCase("resume")) resume();
            else pause();
        });

        play.registerClickEvent(() -> {
            if(play.getText().equalsIgnoreCase("play")) {
                if(music.getSelectedIndex() < 0) return;
                start(os.fs.toFile(music.getSelectedElement().startsWith("Desktop: ")?
                        "/usr/Desktop/"+music.getSelectedElement().substring(9) :
                        "/usr/Applications/Jukebox.app/"+music.getSelectedElement().substring(5)));
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
        stop();
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
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        music.processInput(x, y, type);
        play.processInput(x, y, type);
        pause.processInput(x, y, type);
    }
}
