package com.jnngl.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Disconnected "+ctx.channel().remoteAddress()+": "+cause.getMessage());
        ctx.channel().disconnect();
    }
}
