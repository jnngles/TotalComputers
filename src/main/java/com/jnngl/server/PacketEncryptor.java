package com.jnngl.server;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncryptor extends MessageToByteEncoder<ByteBuf> {

    private final Encryption encryption;

    public PacketEncryptor(Encryption encryption) {
        this.encryption = encryption;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);
        out.writeBytes(encryption.encryptAES(data));
    }
}
