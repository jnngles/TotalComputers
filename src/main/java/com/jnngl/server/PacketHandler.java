package com.jnngl.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

public class PacketHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        System.out.println("Received custom packet "+msg.getClass().toString());
        super.channelRead(ctx, msg);
    }

}
