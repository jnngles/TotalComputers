package com.jnngl.server.protocol;

import com.jnngl.server.BufUtils;
import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundDisconnectPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB2;
    }

    @Override
    public void writeData(ByteBuf buf) {
        BufUtils.writeString(buf, reason);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 4) throw new TooSmallPacketException(length, 4);
        reason = BufUtils.readString(buf);
    }

    @Override
    public int getLength() {
        return BufUtils.sizeofString(reason);
    }

    public String reason;

}
