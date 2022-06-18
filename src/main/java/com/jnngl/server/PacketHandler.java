package com.jnngl.server;

import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.protocol.*;
import com.jnngl.totalcomputers.MapColor;
import com.jnngl.totalcomputers.system.RemoteOS;
import com.jnngl.totalcomputers.system.TotalOS;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PacketHandler extends ChannelDuplexHandler {

    private short unhandledPings;
    private boolean connected = false;
    private ChannelHandlerContext ctx;
    private final Server server;

    private final Map<Channel, Encryption> pendingEncryptionRequests = new HashMap<>();

    public PacketHandler(Server server) {
        this.server = server;
    }


    @Override
    public void channelActive(@NotNull ChannelHandlerContext ctx) throws Exception {
        connected = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(!ctx.channel().isActive()) {
                    cancel();
                    return;
                }
                if(unhandledPings >= 2) {
                    ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
                    s2c_disconnect.reason = "Timed out";
                    ctx.channel().writeAndFlush(s2c_disconnect);
                    ctx.channel().disconnect();
                    cancel();
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
        new Thread(() -> {
            try {
                if (server.enableEncryption) {
                    Encryption encryption = new Encryption();
                    encryption.generateRSA();
                    pendingEncryptionRequests.put(ctx.channel(), encryption);
                    ClientboundEncryptionPacket s2c_encryption = new ClientboundEncryptionPacket();
                    s2c_encryption.publicKey = encryption.rsa.getPublic().getEncoded();
                    ctx.channel().writeAndFlush(s2c_encryption);
                    long lastTime = System.currentTimeMillis();
                    while (pendingEncryptionRequests.get(ctx.channel()).aes == null) {
                        if (System.currentTimeMillis() - lastTime >= 5000) {
                            ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
                            s2c_disconnect.reason = "Timed out: Encryption";
                            ctx.channel().writeAndFlush(s2c_disconnect);
                            ctx.channel().disconnect();
                            return;
                        }
                    }
                    encryption = pendingEncryptionRequests.get(ctx.channel());
                    pendingEncryptionRequests.remove(ctx.channel());
                    ctx.pipeline().addBefore("decoder", "decrypt", new PacketDecryptor(encryption));
                    ctx.pipeline().addBefore("encoder", "encrypt", new PacketEncryptor(encryption));
                }
                if (c2s_handshake.protocolVersion != Server.protocolVersion() ||
                        c2s_handshake.apiVersion != TotalOS.getApiVersion()) {
                    ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
                    s2c_disconnect.reason = c2s_handshake.protocolVersion != Server.protocolVersion() ?
                            "Incompatible protocol version: " + c2s_handshake.protocolVersion :
                            "Incompatible API version: " + c2s_handshake.apiVersion;
                    ctx.channel().writeAndFlush(s2c_disconnect);
                    ctx.channel().disconnect();
                    return;
                }
                ClientboundHandshakePacket s2c_handshake = new ClientboundHandshakePacket();
                s2c_handshake.serverName = server.name;
                if (s2c_handshake.serverName == null) s2c_handshake.serverName = "unknown";
                ctx.channel().writeAndFlush(s2c_handshake);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handleConnectC2S(ServerboundConnectPacket c2s_connect) throws InvalidTokenException {
        Server.BoundToken token = server.bindToken(c2s_connect.token, ctx.channel());
        ClientboundConnectionSuccessPacket s2c_connectionSuccess = new ClientboundConnectionSuccessPacket();
        Player player = token.player();
        s2c_connectionSuccess.name = player.getName();
        player.sendMessage(ChatColor.GOLD+"[TotalComputers] "+
                ChatColor.GREEN+"Connected "+ctx.channel().remoteAddress());
        ctx.channel().writeAndFlush(s2c_connectionSuccess);
        ClientboundPalettePacket s2c_palette = new ClientboundPalettePacket();
        s2c_palette.palette = new int[MapColor.colors.length];
        for(int i = 0; i < s2c_palette.palette.length; i++)
            s2c_palette.palette[i] = MapColor.colors[i].getRGB();
        ctx.channel().writeAndFlush(s2c_palette);
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

    public void handleFrameC2S(ServerboundFramePacket c2s_frame) throws IOException {
        RemoteOS remote = RemoteOS.fromId(c2s_frame.id);
        if(remote == null) return;
        if(c2s_frame.compressedData == null) return;
        remote.handleBuffer(c2s_frame.compressedData);
    }

    public void handleEncryptionC2S(ServerboundEncryptionPacket c2s_encryption)
            throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        if(!pendingEncryptionRequests.containsKey(ctx.channel())) return;
        Encryption encryption = pendingEncryptionRequests.get(ctx.channel());
        encryption.initAES(encryption.decryptRSA(c2s_encryption.secret));
    }

    @Override
    public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg) throws Exception {
        this.ctx = ctx;
        if(msg instanceof ServerboundHandshakePacket c2s_handshake) handleHandshakeC2S(c2s_handshake);
        else if(msg instanceof ServerboundConnectPacket c2s_connect) handleConnectC2S(c2s_connect);
        else if(msg instanceof ServerboundPongPacket c2s_pong) handlePongC2S(c2s_pong);
        else if(msg instanceof ServerboundCreationStatusPacket c2s_status) handleCreationStatusC2S(c2s_status);
        else if(msg instanceof ServerboundFramePacket c2s_frame) handleFrameC2S(c2s_frame);
        else if(msg instanceof ServerboundEncryptionPacket c2s_encryption) handleEncryptionC2S(c2s_encryption);
        super.channelRead(ctx, msg);
    }

    private void handleDisconnect() {
        connected = false;
        String token = server.tokenFromChannel(ctx.channel());
        if(token != null) {
            server.getBoundToken(server.tokenFromChannel(ctx.channel())).player()
                    .sendMessage(ChatColor.GOLD+"[TotalComputers] "+
                            ChatColor.RED+"Disconnected "+ctx.channel().remoteAddress());
            server.unboundToken(token);
        }
        RemoteOS.fromToken(token).forEach(RemoteOS::destroy);
        pendingEncryptionRequests.remove(ctx.channel());
        ctx.close();
        System.out.println("Closed "+ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        handleDisconnect();
        super.channelInactive(ctx);
    }

}
