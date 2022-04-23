package com.jnngl.totalcomputers.sound;

public class SoundWebSocketSession {
    private String host, name;

    public SoundWebSocketSession(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[SoundWebSocketSession: host="+host+", name="+name+"]";
    }

}
