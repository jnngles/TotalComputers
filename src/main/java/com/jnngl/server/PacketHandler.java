package com.jnngl.server;

import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.protocol.*;
import com.jnngl.totalcomputers.system.RemoteOS;
import com.jnngl.totalcomputers.system.TotalOS;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

public class PacketHandler extends ChannelDuplexHandler {

    private short unhandledPings;
    private boolean connected = false;
    private ChannelHandlerContext ctx;
    private final Server server;

    public PacketHandler(Server server) {
        this.server = server;
    }

    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        connected = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(unhandledPings >= 2) {
                    ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
                    s2c_disconnect.reason = "Timed out";
                    ctx.channel().writeAndFlush(s2c_disconnect);
                    ctx.channel().disconnect();
                    return;
                }
                unhandledPings++;
                ClientboundPingPacket s2c_ping = new ClientboundPingPacket();
                s2c_ping.payload = System.currentTimeMillis();
                if(!connected) cancel();
                else ctx.channel().writeAndFlush(s2c_ping);
            }
        }, 15000, 15000);
        super.channelActive(ctx);
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
        s2c_handshake.serverName = server.name;
        if(s2c_handshake.serverName == null) s2c_handshake.serverName = "unknown";
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

    public void handlePongC2S(ServerboundPongPacket c2s_pong) {
        if(System.currentTimeMillis() - c2s_pong.payload > 1000) {
            ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
            s2c_disconnect.reason = "Ping is greater than 1000";
            ctx.channel().writeAndFlush(s2c_disconnect);
            ctx.disconnect();
            return;
        }
        unhandledPings--;
    }

    public void handleCreationStatusC2S(ServerboundCreationStatusPacket c2s_status) {
        RemoteOS.handleResponse(server.tokenFromChannel(ctx.channel()), c2s_status);
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        this.ctx = ctx;
        if(msg instanceof ServerboundHandshakePacket c2s_handshake) handleHandshakeC2S(c2s_handshake);
        else if(msg instanceof ServerboundConnectPacket c2s_connect) handleConnectC2S(c2s_connect);
        else if(msg instanceof ServerboundPongPacket c2s_pong) handlePongC2S(c2s_pong);
        else if(msg instanceof ServerboundCreationStatusPacket c2s_status) handleCreationStatusC2S(c2s_status);
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
        connected = false;
        ctx.close();
        System.out.println("Closed "+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        handleDisconnect();
        super.channelInactive(ctx);
    }

}
