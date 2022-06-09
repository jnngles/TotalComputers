package com.jnngl.server;

import com.jnngl.server.protocol.ClientboundHandshakePacket;
import com.jnngl.server.protocol.ServerboundHandshakePacket;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class PacketHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        if(msg instanceof ServerboundHandshakePacket) {
            ClientboundHandshakePacket s2c_handshake = new ClientboundHandshakePacket();
            s2c_handshake.serverName = Bukkit.getServer().getName();
            ctx.channel().writeAndFlush(s2c_handshake);
        }
        super.channelRead(ctx, msg);
    }

}
