package com.jnngl.server;

import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.protocol.*;
import com.jnngl.totalcomputers.system.TotalOS;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        Server.BoundToken token = server.bindToken(c2s_connect.token, ctx.channel());
        ClientboundConnectionSuccessPacket s2c_connectionSuccess = new ClientboundConnectionSuccessPacket();
        Player player = token.player();
        s2c_connectionSuccess.name = player.getName();
        player.sendMessage(ChatColor.GOLD+"[TotalComputers] "+
                ChatColor.GREEN+"Connected "+ctx.channel().remoteAddress());
        ctx.channel().writeAndFlush(s2c_connectionSuccess);
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

    private void handleDisconnect() {
        String token = server.tokenFromChannel(ctx.channel());
        if(token != null) {
            server.getBoundToken(server.tokenFromChannel(ctx.channel())).player()
                    .sendMessage(ChatColor.GOLD+"[TotalComputers] "+
                            ChatColor.RED+"Disconnected "+ctx.channel().remoteAddress());
            server.unboundToken(token);
        }
        ctx.close();
        System.out.println("Closed "+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        handleDisconnect();
        super.channelInactive(ctx);
    }

}
