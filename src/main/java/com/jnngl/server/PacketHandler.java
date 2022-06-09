package com.jnngl.server;

import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.protocol.ClientboundDisconnectPacket;
import com.jnngl.server.protocol.ClientboundHandshakePacket;
import com.jnngl.server.protocol.ServerboundConnectPacket;
import com.jnngl.server.protocol.ServerboundHandshakePacket;
import com.jnngl.totalcomputers.system.TotalOS;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class PacketHandler extends ChannelDuplexHandler {

    private ChannelHandlerContext ctx;
    private final Server server;

    public PacketHandler(Server server) {
        this.server = server;
    }

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

    public void handleConnectC2S(ServerboundConnectPacket c2s_connect) throws InvalidTokenException {
        server.bindToken(c2s_connect.token, ctx.channel());
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        this.ctx = ctx;
        if(msg instanceof ServerboundHandshakePacket c2s_handshake)
            handleHandshakeC2S(c2s_handshake);
        else if(msg instanceof ServerboundConnectPacket c2s_connect)
            handleConnectC2S(c2s_connect);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        String token = server.tokenFromChannel(ctx.channel());
        if(token != null)
            server.unboundToken(token);
        ctx.close();
        System.out.println("Closed "+ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

}
