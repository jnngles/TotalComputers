package com.jnngl.totalcomputers.sound;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SoundWebSocketSessionManager {
    @Deprecated
    private static SoundWebSocketSessionManager sessionManager;

    @Deprecated
    private final List<SoundWebSocketSession> sessions = new ArrayList<>();

    @Deprecated
    public static SoundWebSocketSessionManager getSessionManager() {
        if(sessionManager == null)
            sessionManager = new SoundWebSocketSessionManager();
        return sessionManager;
    }

    @Deprecated
    public List<SoundWebSocketSession> getSessions() {
        return sessions;
    }

    @Deprecated
    public void openSession(String host) {
        sessions.add(new SoundWebSocketSession(host));
        System.out.println("[TotalComputers: SoundWebSocketSessionManager] <-> Opened session: "
                +getSessionByHost(host));
    }

    @Deprecated
    public void endSession(String host) {
        sessions.remove(getSessionByHost(host));
    }

    @Deprecated
    public void endSessionByName(String name) {
        sessions.remove(getSessionByName(name));
    }

    @Deprecated
    public SoundWebSocketSession getSessionByHost(String host) {
        for(SoundWebSocketSession s : sessions) {
            if(s.getHost().equalsIgnoreCase(host))
                return s;
        }
        return null;
    }

    @Deprecated
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

    @Deprecated
    public void addSessionUsername(String host, String name) {
        getSessionByHost(host).setName(name);
    }


}
