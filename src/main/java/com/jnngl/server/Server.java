package com.jnngl.server;

import com.jnngl.server.exception.InvalidTokenException;
import com.jnngl.server.exception.PacketAlreadyExistsException;
import com.jnngl.server.protocol.ClientboundDisconnectPacket;
import com.jnngl.server.protocol.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private record BoundToken(Player player, String token, Channel channel) {}

    private final Map<String, Player> unboundRegisteredTokens = new HashMap<>();
    private final Map<String, BoundToken> boundRegisteredTokens = new HashMap<>();
    private final Map<Channel, String> channel2token = new HashMap<>();

    private final EventLoopGroup group = new NioEventLoopGroup();
    private final Server server = this;

    public static int protocolVersion() {
        return 1;
    }

    public void start(String ip, int port) {
        new Thread(() -> {
            System.out.println("Registering packets...");
            try {
                Protocol.registerPackets();
            } catch (PacketAlreadyExistsException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Initializing server...");
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(new InetSocketAddress(ip, port));
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(@NotNull SocketChannel ch) {
                    System.out.println("Connected "+ch.remoteAddress().toString());
                    ch.pipeline().addLast("decoder", new PacketDecoder());
                    ch.pipeline().addLast("encoder", new PacketEncoder());
                    ch.pipeline().addLast("packet_handler", new PacketHandler(server));
                    ch.pipeline().addLast("exception_handler", new ExceptionHandler());
                }
            });
            try {
                ChannelFuture channelFuture = bootstrap.bind();
                System.out.println("Server is listening on "+ip+":"+port);
                channelFuture = channelFuture.sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void shutdown() {
        try {
            group.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void unregisterToken(String token) {
        unboundRegisteredTokens.remove(token);
        if(boundRegisteredTokens.containsKey(token)) {
            ClientboundDisconnectPacket s2c_disconnect = new ClientboundDisconnectPacket();
            s2c_disconnect.reason = "Token was reset";
            boundRegisteredTokens.get(token).channel.writeAndFlush(s2c_disconnect);
            boundRegisteredTokens.get(token).channel.disconnect();
            boundRegisteredTokens.remove(token);
        }
    }

    public String tokenFromChannel(Channel channel) {
        return channel2token.getOrDefault(channel, null);
    }

    public void unboundToken(String token) {
        BoundToken bt = boundRegisteredTokens.getOrDefault(token, null);
        if(bt == null) return;
        Player player = bt.player;
        unregisterToken(token);
        registerToken(token, player);
    }

    public void bindToken(String token, Channel channel) throws InvalidTokenException {
        if(!unboundRegisteredTokens.containsKey(token)) throw new InvalidTokenException();
        Player player = unboundRegisteredTokens.get(token);
        unboundRegisteredTokens.remove(token);
        boundRegisteredTokens.put(token, new BoundToken(player, token, channel));
        channel2token.put(channel, token);
    }

    public void registerToken(String token, Player player) {
        unboundRegisteredTokens.put(token, player);
    }

}
