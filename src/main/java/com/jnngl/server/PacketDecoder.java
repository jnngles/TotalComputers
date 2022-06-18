package com.jnngl.server;

import com.jnngl.server.protocol.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            out.add(Packet.read(in));
        } catch (Exception e) {
            if(Server.DEBUG)
                System.out.println("Bad packet: "+e.getMessage());
        }

        if(Server.DEBUG) {
            for (Object msg : out) {
                System.out.println("C -> S: " + msg.getClass().getSimpleName() +
                        " (0x" + String.format("%x", ((Packet) msg).getPacketID()) + ")");
            }
        }
    }
}
