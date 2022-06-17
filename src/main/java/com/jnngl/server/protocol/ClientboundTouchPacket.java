package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundTouchPacket extends Packet {

    public static final byte LEFT_CLICK = 0;
    public static final byte RIGHT_CLICK = 1;

    @Override
    public byte getPacketID() {
        return (byte)0xC2;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeShort(id);
        buf.writeShort(x);
        buf.writeShort(y);
        buf.writeByte(type);
        buf.writeBoolean(admin);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length != 8) throw new TooSmallPacketException(length, 8);
        id = buf.readShort();
        x = buf.readShort();
        y = buf.readShort();
        type = buf.readByte();
        admin = buf.readBoolean();
    }

    @Override
    public int getLength() {
        return 8;
    }

    public short id;
    public short x;
    public short y;
    public byte type;
    public boolean admin;

}
