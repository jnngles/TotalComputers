package com.jnngl.totalcomputers.system;

import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.InflaterInputStream;
import com.jnngl.server.Server;
import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.protocol.ClientboundCreationRequestPacket;
import com.jnngl.server.protocol.ClientboundDestroyPacket;
import com.jnngl.server.protocol.ClientboundTouchPacket;
import com.jnngl.server.protocol.ServerboundCreationStatusPacket;
import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.exception.AlreadyClientboundException;
import com.jnngl.totalcomputers.system.exception.AlreadyRequestedException;
import com.jnngl.totalcomputers.system.exception.TimedOutException;
import io.netty.channel.Channel;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class RemoteOS {

    private int width, height;
    private String name;
    private short id;
    private Channel connection;
    private byte[] indexedBuffer;
    private String token;
    private boolean destroyed = false;


    public Channel getConnection() {
        return connection;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    public short getId() {
        return id;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private static final Map<String, RemoteOS> requested = new HashMap<>();

    private static final Map<String, Short> name2id = new HashMap<>();
    private static final Map<Short, RemoteOS> id2os = new HashMap<>();

    public RemoteOS() {}

    public static Set<String> allNames() {
        return name2id.keySet();
    }

    public static Set<RemoteOS> fromToken(String token) {
        Set<RemoteOS> remotes = new HashSet<>();
        for(RemoteOS remote : id2os.values()) {
            if(remote.token.equals(token))
                remotes.add(remote);
        }
        return remotes;
    }

    public static RemoteOS fromId(short id) {
        return id2os.get(id);
    }

    public static RemoteOS fromName(String name) {
        return fromId(name2id.getOrDefault(name, (short)-1));
    }

    public static RemoteOS requestCreation(Server server, String token, String name, int width, int height)
            throws AlreadyRequestedException, TimedOutException, InvalidTokenException, AlreadyClientboundException {
        Server.BoundToken boundToken = server.getBoundToken(token);
        if(boundToken == null) throw new InvalidTokenException();
        if(name2id.containsKey(name)) throw new AlreadyClientboundException();
        RemoteOS remote = new RemoteOS();
        synchronized(requested) {
            if (requested.containsKey(token))
                throw new AlreadyRequestedException();
            requested.put(token, remote);
        }
        Channel channel = boundToken.channel();
        ClientboundCreationRequestPacket s2c_request = new ClientboundCreationRequestPacket();
        s2c_request.width = (short)width;
        s2c_request.height = (short)height;
        s2c_request.name = name;
        channel.writeAndFlush(s2c_request);
        Timer timeOutTimer = new Timer();
        final boolean[] isTimedOut = { false };
        timeOutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTimedOut[0] = true;
                synchronized(requested) {
                    requested.remove(token);
                }
            }
        }, 10000);
        while(true) {
            synchronized(requested) {
                if(!requested.containsKey(token)) break;
            }
        }
        if(isTimedOut[0])
            throw new TimedOutException();
        timeOutTimer.cancel();
        remote.name = name;
        remote.width = width;
        remote.height = height;
        remote.connection = channel;
        remote.createBuffer();
        name2id.put(remote.name, remote.id);
        id2os.put(remote.id, remote);
        return remote;
    }

    private void createBuffer() {
        indexedBuffer = new byte[width*height];
    }

    public static void handleResponse(String token, ServerboundCreationStatusPacket s2c_status) {
        if(token == null) return;
        if(s2c_status.status == ServerboundCreationStatusPacket.STATUS_ERR) return;
        synchronized(requested) {
            if (!requested.containsKey(token)) return;
            requested.get(token).id = s2c_status.id;
            requested.get(token).token = token;
            requested.remove(token);
        }
    }

    public void destroy() {
        ClientboundDestroyPacket s2c_destroy = new ClientboundDestroyPacket();
        s2c_destroy.id = id;
        connection.writeAndFlush(s2c_destroy);
        name2id.remove(name);
        id2os.remove(id);
        id = 0;
        name = "";
        width = 0;
        height = 0;
        destroyed = true;
        connection = null;
    }

    public byte[] getBuffer() {
        return indexedBuffer;
    }

    public void handleBuffer(byte[] compressed) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(new InflaterInputStream(new ByteArrayInputStream(compressed), new Inflater()), out);
        byte[] data = out.toByteArray();
        System.arraycopy(data, 0, indexedBuffer,
                0, Math.min(data.length, indexedBuffer.length));
    }

    public void sendTouchEvent(int x, int y, TotalComputers.InputInfo.InteractType type, boolean admin) {
        ClientboundTouchPacket s2c_touch = new ClientboundTouchPacket();
        s2c_touch.id = id;
        s2c_touch.x = (short)x;
        s2c_touch.y = (short)y;
        s2c_touch.type = (type == TotalComputers.InputInfo.InteractType.LEFT_CLICK?
                            ClientboundTouchPacket.LEFT_CLICK : ClientboundTouchPacket.RIGHT_CLICK);
        s2c_touch.admin = admin;
        connection.writeAndFlush(s2c_touch);
    }

}
