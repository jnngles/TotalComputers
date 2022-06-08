package com.jnngl.server;

import com.jnngl.server.exception.PacketAlreadyExistsException;
import com.jnngl.server.protocol.Protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

public class Server {

    private final EventLoopGroup group = new NioEventLoopGroup();

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
                    System.out.println("Server is listening on "+ip+":"+port);
                    ch.pipeline().addLast("decoder", new PacketDecoder());
                    ch.pipeline().addLast("packet_handler", new PacketHandler());
                }
            });
            try {
                ChannelFuture channelFuture = bootstrap.bind().sync();
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

}
