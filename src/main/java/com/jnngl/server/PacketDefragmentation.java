package com.jnngl.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * tcp fragmentation
 * @since Protocol 2
 */
public class PacketDefragmentation extends ByteToMessageDecoder {

    private ByteBuf buf = null;
    private int remaining = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while(in.readableBytes() > 0) {
            if(buf == null) {
                buf = Unpooled.buffer();
                remaining = BufUtils.readVarInt(in);
            }
            int toWrite = Math.min(remaining, in.readableBytes());
            buf.writeBytes(in, toWrite);
            remaining -= toWrite;
            if(remaining == 0) {
                out.add(buf);
                buf = null;
            }
        }
    }

}
