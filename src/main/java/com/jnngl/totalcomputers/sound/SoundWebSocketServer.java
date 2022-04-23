package com.jnngl.totalcomputers.sound;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SoundWebSocketServer extends WebSocketServer {
    public static SoundWebSocketServer server;

    private static Map<String, String> durations = new HashMap<>();

    static String getDuration(String name) {
        return durations.getOrDefault(name, "-1");
    }

    public SoundWebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public SoundWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String hostAddr = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        SoundWebSocketSessionManager.getSessionManager().openSession(hostAddr);
        System.out.println("[TotalComputers: Sound WebSocketServer] <-- " + hostAddr + " has connected");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String hostAddr = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        SoundWebSocketSessionManager.getSessionManager().endSession(hostAddr);
        System.out.println("[TotalComputers: Sound WebSocketServer] --> " + hostAddr + " has disconnected");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("[TotalComputers: Sound WebSocketServer] <-- Received packet from "
                +conn.getRemoteSocketAddress().getAddress().getHostAddress()+": "+message);
        if(message.split(":")[0].equalsIgnoreCase("name")) {
            SoundWebSocketSessionManager.getSessionManager().addSessionUsername
                    (conn.getRemoteSocketAddress().getAddress().getHostAddress(), message.split(":")[1]);
        }
        else if(message.split(":")[0].equalsIgnoreCase("duration")) {
            durations.put(message.split(":")[1].split("=")[0],
                    message.split(":")[1].split("=")[1]);
        }
    }

    public static void runServer() throws UnknownHostException {
        final int port = 7255;
        server = new SoundWebSocketServer(port);
        server.start();
        System.out.println("[TotalComputers: Sound WebSocketServer] <-> Started on port: "+server.getPort());
    }

    public static void shutdown() throws InterruptedException {
        server.stop();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("[TotalComputers: Sound WebSocketServer] <-- Oops! "+ex.getMessage());
    }

    @Override
    public void onStart() {

    }

    public void sendToAll(String data) {
        Collection<WebSocket> cons = getConnections();
        synchronized (cons) {
            for(WebSocket con : cons) {
                con.send(data);
            }
        }
    }

    public void sendData(SoundWebSocketSession session, String data) {
        Collection<WebSocket> cons = getConnections();
        synchronized (cons) {
            for(WebSocket con : cons) {
                if(con.getRemoteSocketAddress().getAddress().getHostAddress().equalsIgnoreCase(session.getHost())) {
                    System.out.println("[TotalComputers: Sound WebSocketServer] --> Send packet: "+data);
                    con.send(data);
                }
            }
        }
    }

}
