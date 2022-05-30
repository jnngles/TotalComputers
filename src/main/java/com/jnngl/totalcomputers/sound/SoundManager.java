package com.jnngl.totalcomputers.sound;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Deprecated
public class SoundManager {

    @Deprecated
    public static float getDuration(String name) {
        return Float.parseFloat(SoundWebSocketServer.getDuration(name));
    }

    @Deprecated
    public static void play(Player p, String data) {
        SoundWebSocketSession session = SoundWebSocketSessionManager.getSessionManager().getSessionByName(p.getName());
        if(session != null) {
            SoundWebSocketServer.server.sendData(session, data);
        }
    }

    @Deprecated
    public static void play(String data) {
        for(Player p : Bukkit.getOnlinePlayers())
            play(p, data);
    }

}
