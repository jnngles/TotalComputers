package com.jnngl.totalcomputers.sound.discord;

import com.jnngl.totalcomputers.Localization;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.nio.ByteBuffer;

@RequiresAPI(apiLevel = 6)
public class DiscordBot extends ListenerAdapter implements AudioSendHandler {

    public JDA jda;

    public final AudioPlayerManager playerManager;
    public static AudioPlayer global_player;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame = new MutableAudioFrame();


    private static boolean isPlaying = false;
    public static boolean canPlay = false;
    private static boolean isPaused = false;

    public DiscordBot() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        global_player = playerManager.createPlayer();
        global_player.addListener(new AudioEventAdapter() {
            @Override
            public void onPlayerPause(AudioPlayer player) {
                if(player != global_player) return;
                isPaused = true;
            }

            @Override
            public void onPlayerResume(AudioPlayer player) {
                if(player != global_player) return;
                isPaused = false;
            }

            @Override
            public void onTrackStart(AudioPlayer player, AudioTrack track) {
                if(player != global_player) return;
                isPlaying = true;
            }

            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                if(player != global_player) return;
                isPlaying = false;
            }
        });
        buffer = ByteBuffer.allocate(1024);
        frame.setBuffer(buffer);
    }

    public static DiscordBot start(String token) {
        final DiscordBot bot = new DiscordBot();
        new Thread(() -> {
            try {
                bot.jda = JDABuilder.createDefault(token).addEventListeners(bot).build();
            } catch (LoginException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return bot;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        Message message = event.getMessage();
        TextChannel channel = message.getTextChannel();
        String messageText = message.getContentRaw().toLowerCase();
        if(messageText.equals("`join")) {
            if(!event.getGuild().getSelfMember().hasPermission(channel, Permission.VOICE_CONNECT)) {
                message.addReaction("❌").queue();
                channel.sendMessage(Localization.get(148)).queue();
                return;
            }

            VoiceChannel voiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
            if(voiceChannel == null) {
                message.addReaction("❌").queue();
                channel.sendMessage(Localization.get(149)).queue();
                return;
            }

            AudioManager audioManager = event.getGuild().getAudioManager();
            if(audioManager.isConnected()) {
                message.addReaction("❌").queue();
                channel.sendMessage(Localization.get(150)).queue();
                return;
            }

            audioManager.setSendingHandler(this);
            audioManager.openAudioConnection(voiceChannel);
            canPlay = true;
            message.addReaction("✅").queue();
            return;
        } else if(messageText.equals("`leave")) {
            VoiceChannel connected = (VoiceChannel) event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(connected == null) {
                message.addReaction("❌").queue();
                channel.sendMessage(Localization.get(151)).queue();
                return;
            }

            event.getGuild().getAudioManager().closeAudioConnection();
            canPlay = false;
            message.addReaction("✅").queue();
            return;
        }
//        else if(messageText.startsWith("`play")) {
//            String[] parts = messageText.split(" ");
//            if(parts.length > 1) {
//                loadMusic(parts[1], new AudioLoadResultHandler() {
//                    @Override
//                    public void trackLoaded(AudioTrack track) {
//                        play(track);
//                        message.addReaction("✅").queue();
//                        return;
//                    }
//
//                    @Override
//                    public void playlistLoaded(AudioPlaylist playlist) {
//                        message.addReaction("❌").queue();
//                        channel.sendMessage("Playlists are not supported yet!").queue();
//                        return;
//                    }
//
//                    @Override
//                    public void noMatches() {
//                        message.addReaction("❌").queue();
//                        channel.sendMessage("Nothing found!").queue();
//                        return;
//                    }
//
//                    @Override
//                    public void loadFailed(FriendlyException exception) {
//                        message.addReaction("❌").queue();
//                        channel.sendMessage("Failed to load! \n"+exception.toString()).queue();
//                        return;
//                    }
//                });
//            } else {
//                message.addReaction("❌").queue();
//                channel.sendMessage("What should I play?").queue();
//                return;
//            }
//        } else if(messageText.equals("`pause")) {
//            if(!isPlaying()) {
//                message.addReaction("❌").queue();
//                channel.sendMessage("Nothing is playing").queue();
//                return;
//            } else if(isPaused()) {
//                message.addReaction("❌").queue();
//                channel.sendMessage("Already paused").queue();
//                return;
//            } else {
//                pause();
//                message.addReaction("✅").queue();
//                return;
//            }
//        } else if(messageText.equals("`resume")) {
//            if(!isPaused()) {
//                message.addReaction("❌").queue();
//                channel.sendMessage("Already playing").queue();
//            } else {
//                resume();
//                message.addReaction("✅").queue();
//            }
//        } else if(messageText.equals("`stop")) {
//            if(!isPlaying()) {
//                message.addReaction("❌").queue();
//                channel.sendMessage("Nothing is playing").queue();
//            } else {
//                stop();
//                message.addReaction("✅").queue();
//            }
//        }
    }

    public void loadMusic(String location, AudioLoadResultHandler callback) {
        playerManager.loadItem(location, callback);
    }

    public void play(AudioTrack track) {
        if(!canPlay) return;
        if(track == null) return;
        if(isPlaying) return;
        global_player.playTrack(track);
    }

    public void pause() {
        global_player.setPaused(true);
    }

    public void stop() {
        global_player.stopTrack();
    }

    public boolean isPaused() {
        return isPaused || !isPlaying();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setVolume(int volume) {
        global_player.setVolume(volume);
    }

    public int getVolume() {
        return global_player.getVolume();
    }

    public void resume() {
        global_player.setPaused(false);
    }



    @Override
    public boolean canProvide() {
        boolean didProvide = global_player.provide(frame);
        if(didProvide) buffer.flip();
        return didProvide;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
