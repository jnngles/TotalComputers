package com.jnngl.packet;

import io.netty.channel.*;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketListener {

    private PacketHandler handler;

    private static Method CraftPlayer$getHandle;
    private static String version;

    public interface PacketHandler {

        public boolean read(Object packet);
        public boolean write(Object packet);

    }

    public PacketListener(PacketHandler handler) {
        this.handler = handler;
    }

    private static void init() {
        if(CraftPlayer$getHandle == null) {
            try {
                for (Package pkg : Package.getPackages()) {
                    if (pkg.getName().startsWith("org.bukkit.craftbukkit.v")) {
                        version = pkg.getName().replace("org.bukkit.craftbukkit.", "").split("\\.")[0];
                        break;
                    }
                }

                if (version == null) {
                    System.err.println("Failed to get version");
                    return;
                }

                Class<?> CraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                CraftPlayer$getHandle = CraftPlayer.getMethod("getHandle");
            } catch (Throwable e) {
                System.err.println(" -> "+e.getMessage());
            }
        }
    }

    private static Channel getPlayerChannel(Object entityPlayer) {
        try {
            Object playerConnection;
            try {
                playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            } catch (Throwable e) {
                playerConnection = entityPlayer.getClass().getField("b").get(entityPlayer);
            }
            Object networkManager;
            try {
                networkManager = playerConnection.getClass().getField("networkManager").get(playerConnection);
            } catch (Throwable e) {
                networkManager = playerConnection.getClass().getField("a").get(playerConnection);
            }
            try {
                return (Channel) networkManager.getClass().getField("channel").get(networkManager);
            } catch (Throwable e) {
                return (Channel) networkManager.getClass().getField("m").get(networkManager);
            }
        } catch (Throwable e) {
            System.err.println(" -> "+e.getMessage());
        }
        return null;
    }

    public static void removePlayer(Player player) {
        init();
        try {
            final Channel channel = getPlayerChannel(CraftPlayer$getHandle.invoke(player));
            channel.eventLoop().submit(() -> {
                channel.pipeline().remove(player.getName());
                return null;
            });
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("Failed to access channel -> "+e.getMessage());
        }
    }

    public static void addPlayer(Player player, PacketListener listener) {
        init();
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if(!listener.handler.read(msg)) return;
                super.channelRead(ctx, msg);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                if(!listener.handler.write(msg)) return;
                super.write(ctx, msg, promise);
            }
        };

        try {
            ChannelPipeline pipeline = getPlayerChannel(CraftPlayer$getHandle.invoke(player)).pipeline();
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (Throwable e) {
            System.err.println("Failed to access channel -> "+e.getMessage());
        }
    }

}
