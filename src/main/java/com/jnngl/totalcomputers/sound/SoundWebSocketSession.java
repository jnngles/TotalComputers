package com.jnngl.totalcomputers.sound;

@Deprecated
public class SoundWebSocketSession {
    @Deprecated
    private String host, name;

    @Deprecated
    public SoundWebSocketSession(String host) {
        this.host = host;
    }

    @Deprecated
    public String getName() {
        return name;
    }

    @Deprecated
    public String getHost() {
        return host;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    @Override
    public String toString() {
        return "[SoundWebSocketSession: host="+host+", name="+name+"]";
    }

}
