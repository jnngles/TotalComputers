package com.jnngl.server;

import com.jnngl.server.protocol.ClientboundDisconnectPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
        s2c_disconnect.reason = cause.getMessage();
        ctx.channel().writeAndFlush(s2c_disconnect);
        System.out.println("Disconnected "+ctx.channel().remoteAddress()+": "+cause.getMessage());
        ctx.channel().disconnect();
    }
}
