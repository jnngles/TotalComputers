package com.jnngl.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PacketDecryptor extends MessageToMessageDecoder<ByteBuf> {

    private final Encryption encryption;

    public PacketDecryptor(Encryption encryption) {
        this.encryption = encryption;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);
        byte[] decrypted = encryption.decryptAES(data);
        out.add(ctx.alloc().heapBuffer(decrypted.length).writeBytes(decrypted));
    }

}
