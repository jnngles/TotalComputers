package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundPingPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB5;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeLong(payload);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 8) throw new TooSmallPacketException(length, 8);
        payload = buf.readLong();
    }

    @Override
    public int getLength() {
        return 8;
    }

    public long payload;

}
