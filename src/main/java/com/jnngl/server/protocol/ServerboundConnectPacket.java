package com.jnngl.server.protocol;

import com.jnngl.server.BufUtils;
import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ServerboundConnectPacket extends Packet {
    @Override
    public byte getPacketID() {
        return (byte)0xB3;
    }

    @Override
    public void writeData(ByteBuf buf) {
        BufUtils.writeString(buf, token);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 4) throw new TooSmallPacketException(length, 4);
        token = BufUtils.readString(buf);
    }

    @Override
    public int getLength() {
        return BufUtils.sizeofString(token);
    }

    public String token;

}
