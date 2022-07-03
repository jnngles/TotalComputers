package com.jnngl.server.protocol;

import com.jnngl.server.BufUtils;
import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundCreationRequestPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB7;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeShort(width);
        buf.writeShort(height);
        buf.writeShort(id);
        BufUtils.writeString(buf, name);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 9) throw new TooSmallPacketException(length, 9);
        width = buf.readShort();
        height = buf.readShort();
        id = buf.readShort();
        name = BufUtils.readString(buf);
    }

    @Override
    public int getLength() {
        return 6 + BufUtils.sizeofString(name);
    }

    public short width;
    public short height;
    public short id;
    public String name;

}
