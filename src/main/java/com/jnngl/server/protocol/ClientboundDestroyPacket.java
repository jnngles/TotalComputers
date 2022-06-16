package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundDestroyPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB9;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeShort(id);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 2) throw new TooSmallPacketException(length, 2);
        id = buf.readShort();
    }

    @Override
    public int getLength() {
        return 2;
    }

    public short id;

}
