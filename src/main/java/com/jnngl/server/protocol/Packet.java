package com.jnngl.server.protocol;

import com.jnngl.server.exception.InvalidPacketException;
import com.jnngl.server.exception.InvalidPacketIdException;
import com.jnngl.server.exception.PacketAlreadyExistsException;
import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Packet {

    private static final Set<Byte> registeredIDs = new HashSet<>();
    private static final Map<Byte, Constructor<? extends Packet>> registeredPackets = new HashMap<>();

    public static void registerPacket(Packet packet) throws PacketAlreadyExistsException, NoSuchMethodException {
        if(registeredIDs.contains(packet.getPacketID()))
            throw new PacketAlreadyExistsException(packet.getPacketID());
        registeredIDs.add(packet.getPacketID());
        registeredPackets.put(packet.getPacketID(), packet.getClass().getConstructor());
    }

    public static Packet read(ByteBuf buf) throws Exception {
        if(buf.readableBytes() < 7) throw new TooSmallPacketException(buf.readableBytes(), 7);
        if(buf.readByte() != 0x0A) throw new InvalidPacketException("Invalid magic value");
        if(buf.readByte() != 0x1F) throw new InvalidPacketException("Invalid magic value");
        byte packetId = buf.readByte();
        Packet packet = getPacketInstance(packetId);
        int length = buf.readInt();
        packet.readData(buf, length);
        return packet;
    }

    public static Packet getPacketInstance(byte id)
            throws InvocationTargetException, InstantiationException,
                    IllegalAccessException, InvalidPacketIdException {
        if(!registeredIDs.contains(id)) throw new InvalidPacketIdException(id);
        return registeredPackets.get(id).newInstance();
    }

    public abstract byte getPacketID();
    public abstract void writeData(ByteBuf buf);
    public abstract void readData(ByteBuf buf, int length) throws Exception;
    public abstract int getLength();

    public void write(ByteBuf buf) {
        buf.writeByte(0x0A);
        buf.writeByte(0x1F); // *magic*
        buf.writeByte(getPacketID());
        buf.writeInt(getLength());
        writeData(buf);
    }

}
