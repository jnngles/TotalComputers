package com.jnngl.server.protocol;

import com.jnngl.server.BufUtils;
import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundConnectionSuccessPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB4;
    }

    @Override
    public void writeData(ByteBuf buf) {
        BufUtils.writeString(buf, name);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 4) throw new TooSmallPacketException(length, 4);
        name = BufUtils.readString(buf);
    }

    @Override
    public int getLength() {
        return BufUtils.sizeofString(name);
    }

    public String name;

}
