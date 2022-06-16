package com.jnngl.totalcomputers.system;

import com.jnngl.server.Server;
import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.protocol.ClientboundCreationRequestPacket;
import com.jnngl.server.protocol.ClientboundDestroyPacket;
import com.jnngl.server.protocol.ClientboundDisconnectPacket;
import com.jnngl.server.protocol.ServerboundCreationStatusPacket;
import com.jnngl.totalcomputers.system.exception.AlreadyClientboundException;
import com.jnngl.totalcomputers.system.exception.AlreadyRequestedException;
import com.jnngl.totalcomputers.system.exception.TimedOutException;
import io.netty.channel.Channel;

import java.util.*;

public class RemoteOS {

    private int width, height;
    private String name;
    private short id;
    private Channel connection;

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

    private static final Map<String, RemoteOS> requested = new HashMap<>();

    private static final Map<String, Short> name2id = new HashMap<>();
    private static final Map<Short, RemoteOS> id2os = new HashMap<>();

    public RemoteOS() {}

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
        name2id.put(remote.name, remote.id);
        id2os.put(remote.id, remote);
        return remote;
    }

    public static void handleResponse(String token, ServerboundCreationStatusPacket s2c_status) {
        if(token == null) return;
        if(s2c_status.status == ServerboundCreationStatusPacket.STATUS_ERR) return;
        synchronized(requested) {
            if (!requested.containsKey(token)) return;
            requested.get(token).id = s2c_status.id;
            requested.remove(token);
        }
    }

    public void destroy() {
        ClientboundDestroyPacket s2c_destroy = new ClientboundDestroyPacket();
        s2c_destroy.id = id;
        connection.writeAndFlush(s2c_destroy);
        name2id.remove(name);
        id2os.remove(id);
        id = -1;
        name = "";
        width = 0;
        height = 0;
        connection = null;
    }

}
