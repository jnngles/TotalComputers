package com.jnngl.server;

import com.jnngl.server.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        if(Server.DEBUG) {
            System.out.println("S -> C: " + msg.getClass().getSimpleName()
                    + " (0x" + String.format("%x", msg.getPacketID()) + ")");
        }
        msg.write(out);
    }

}
