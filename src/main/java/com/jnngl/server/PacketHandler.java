package com.jnngl.server;

import com.jnngl.server.protocol.ClientboundDisconnectPacket;
import com.jnngl.server.protocol.ClientboundHandshakePacket;
import com.jnngl.server.protocol.ServerboundHandshakePacket;
import com.jnngl.totalcomputers.system.TotalOS;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class PacketHandler extends ChannelDuplexHandler {

    private ChannelHandlerContext ctx;

    public void handleHandshakeC2S(ServerboundHandshakePacket c2s_handshake) {
        if(c2s_handshake.protocolVersion != Server.protocolVersion() ||
                c2s_handshake.apiVersion != TotalOS.getApiVersion()) {
            ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
            s2c_disconnect.reason = c2s_handshake.protocolVersion != Server.protocolVersion()?
                    "Incompatible protocol version: "+c2s_handshake.protocolVersion :
                    "Incompatible API version: "+c2s_handshake.apiVersion;
            ctx.channel().writeAndFlush(s2c_disconnect);
            ctx.channel().disconnect();
            return;
        }
        ClientboundHandshakePacket s2c_handshake = new ClientboundHandshakePacket();
        s2c_handshake.serverName = Bukkit.getServer().getName();
        ctx.channel().writeAndFlush(s2c_handshake);
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        this.ctx = ctx;
        if(msg instanceof ServerboundHandshakePacket c2s_handshake)
            handleHandshakeC2S(c2s_handshake);
        super.channelRead(ctx, msg);
    }

}
