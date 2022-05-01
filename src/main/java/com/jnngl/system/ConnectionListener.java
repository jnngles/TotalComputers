package com.jnngl.system;

public interface ConnectionListener {
    
    public void onConnect(Server.Connection con);
    public void onDisconnect(Server.Connection con);
    public void onMessage(Server.Connection con, String data);
    
}
