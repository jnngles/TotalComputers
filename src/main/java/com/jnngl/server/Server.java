package com.jnngl.server;

import io.netty.channel.ChannelHandlerContext;
import org.bukkit.plugin.Plugin;

public class Server {

    private final PacketInjector injector;

    public Server(Plugin plugin) {
        injector = new PacketInjector(plugin);
        injector.listeners().add(new PacketInjector.PacketListener() {
            @Override
            public boolean onRead(ChannelHandlerContext ctx, Object msg) {
                System.out.println("Read "+msg.getClass().getName());
                return true;
            }

            @Override
            public boolean onWrite(ChannelHandlerContext ctx, Object msg) {
                System.out.println("Write "+msg.getClass().getName());
                return true;
            }
        });
    }

    public PacketInjector injector() {
        return injector;
    }

    public void inject() throws ReflectiveOperationException {
        injector.inject();
    }

    public void removeInjection() {
        injector.getInjector().removeInjection();
    }

}
