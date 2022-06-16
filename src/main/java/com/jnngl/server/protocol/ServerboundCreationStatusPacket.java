package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ServerboundCreationStatusPacket extends Packet {

    public static final byte STATUS_OK = 0;
    public static final byte STATUS_ERR = 1;

    @Override
    public byte getPacketID() {
        return (byte)0xB8;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeByte(status);
        buf.writeShort(id);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 3) throw new TooSmallPacketException(length, 3);
        status = buf.readByte();
        id = buf.readShort();
    }

    @Override
    public int getLength() {
        return 3;
    }

    public byte status;
    public short id;

}
