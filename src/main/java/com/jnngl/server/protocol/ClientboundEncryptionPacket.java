package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ClientboundEncryptionPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xC3;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeBytes(publicKey);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(buf.readableBytes() != length)
            throw new TooSmallPacketException(buf.readableBytes(), length);
        publicKey = new byte[buf.readableBytes()];
        buf.readBytes(publicKey, 0, publicKey.length);
    }

    @Override
    public int getLength() {
        return publicKey.length;
    }

    public byte[] publicKey;

}
