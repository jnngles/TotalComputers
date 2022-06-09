package com.jnngl.server.protocol;

import com.jnngl.server.BufUtils;
import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundHandshakePacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xB1;
    }

    @Override
    public void writeData(ByteBuf buf) {
        BufUtils.writeString(buf, serverName);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(length < 4) throw new TooSmallPacketException(length, 4);
        serverName = BufUtils.readString(buf);
    }

    @Override
    public int getLength() {
        return BufUtils.sizeofString(serverName);
    }

    public String serverName;

}
