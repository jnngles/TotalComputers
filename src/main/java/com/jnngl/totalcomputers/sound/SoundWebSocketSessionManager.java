package com.jnngl.totalcomputers.sound;

import java.util.ArrayList;
import java.util.List;

public class SoundWebSocketSessionManager {
    private static SoundWebSocketSessionManager sessionManager;

    private final List<SoundWebSocketSession> sessions = new ArrayList<>();

    public static SoundWebSocketSessionManager getSessionManager() {
        if(sessionManager == null)
            sessionManager = new SoundWebSocketSessionManager();
        return sessionManager;
    }

    public List<SoundWebSocketSession> getSessions() {
        return sessions;
    }

    public void openSession(String host) {
        sessions.add(new SoundWebSocketSession(host));
        System.out.println("[TotalComputers: SoundWebSocketSessionManager] <-> Opened session: "
                +getSessionByHost(host));
    }

    public void endSession(String host) {
        sessions.remove(getSessionByHost(host));
    }

    public void endSessionByName(String name) {
        sessions.remove(getSessionByName(name));
    }

    public SoundWebSocketSession getSessionByHost(String host) {
        for(SoundWebSocketSession s : sessions) {
            if(s.getHost().equalsIgnoreCase(host))
                return s;
        }
        return null;
    }

    public SoundWebSocketSession getSessionByName(String name) {
        for(SoundWebSocketSession s : sessions) {
            String a = s.getName();
            if(a == null) {
                System.err.println("Warning: "+s+".name == null");
                continue;
            }
            if(a.equalsIgnoreCase(name))
                return s;
        }
        return null;
    }

    public void addSessionUsername(String host, String name) {
        getSessionByHost(host).setName(name);
    }


}
