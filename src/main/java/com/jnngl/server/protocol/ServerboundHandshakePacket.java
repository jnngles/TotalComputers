package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ServerboundHandshakePacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB0;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeShort(protocolVersion);
        buf.writeShort(apiVersion);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws TooSmallPacketException {
        if(length < 4) throw new TooSmallPacketException(length, 4);
        protocolVersion = buf.readShort();
        apiVersion = buf.readShort();
    }

    @Override
    public int getLength() {
        return 4;
    }

    public short protocolVersion;
    public short apiVersion;

}
