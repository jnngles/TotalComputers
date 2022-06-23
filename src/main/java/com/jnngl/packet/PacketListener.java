package com.jnngl.packet;

import io.netty.channel.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
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
        Object playerConnection = null;
        for(Field f : entityPlayer.getClass().getDeclaredFields()) {
            try {
                if (f.getType().getSimpleName().equals("PlayerConnection")) {
                    f.setAccessible(true);
                    playerConnection = f.get(entityPlayer);
                    break;
                }
            } catch (Throwable e2) {
                System.err.println(" -> " + e2.getMessage());
            }
        }
        if(playerConnection == null) {
            System.err.println("Unable to find player connection");
            return null;
        }
        Object networkManager = null;
        for(Field f : playerConnection.getClass().getDeclaredFields()) {
            if(f.getType().getSimpleName().contains("NetworkManager")) {
                try {
                    f.setAccessible(true);
                    networkManager = f.get(playerConnection);
                    break;
                } catch (Throwable e2) {
                    System.err.println(" -> "+e2.getMessage());
                }
            }
        }
        if(networkManager == null) {
            System.err.println("Unable to find network manager");
            return null;
        }
        for(Field field : networkManager.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(networkManager) instanceof Channel
                        || (field.getType().getName().contains("netty") && field.getType().getName().contains("Channel")))
                    return (Channel) field.get(networkManager);
            } catch (Throwable e) {
                System.err.println(" -> "+e.getMessage());
            }
        }
        System.err.println("Unable to find player channel");
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
            Object entityPlayer = CraftPlayer$getHandle.invoke(player);
            ChannelPipeline pipeline = getPlayerChannel(entityPlayer).pipeline();
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (Throwable e) {
            System.err.println("Failed to access channel -> "+e.getMessage());
        }
    }

}
