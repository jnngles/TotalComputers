package com.jnngl.server.protocol;

import com.jnngl.server.exception.TooSmallPacketException;
import io.netty.buffer.ByteBuf;

public class ServerboundEncryptionPacket extends Packet {

    @Override
    public byte getPacketID() {
        return (byte)0xC4;
    }

    @Override
    public void writeData(ByteBuf buf) {
        buf.writeBytes(secret);
    }

    @Override
    public void readData(ByteBuf buf, int length) throws Exception {
        if(buf.readableBytes() != length)
            throw new TooSmallPacketException(buf.readableBytes(), length);
        secret = new byte[buf.readableBytes()];
        buf.readBytes(secret, 0, secret.length);
    }

    @Override
    public int getLength() {
        return secret.length;
    }

    public byte[] secret;

}
