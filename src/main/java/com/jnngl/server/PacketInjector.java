package com.jnngl.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class PacketInjector {

    interface PacketListener {
        default boolean onRead(ChannelHandlerContext ctx, Object msg) { return true; }
        default boolean onWrite(ChannelHandlerContext ctx, Object msg) { return true; }
    }

    private final List<PacketListener> listeners = new LinkedList<>();
    private final Injector injector;

    public PacketInjector(Plugin plugin) {
        injector = new Injector(plugin);
        injector.injectors().add(
            channel -> channel.pipeline().addFirst(new ChannelDuplexHandler() {
                @Override
                public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg)
                        throws Exception{
                    for(PacketListener listener : listeners) {
                        if(!listener.onRead(ctx, msg)) return;
                    }
                    super.channelRead(ctx, msg);
                }

                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                        throws Exception {
                    for(PacketListener listener : listeners) {
                        if(!listener.onWrite(ctx, msg)) return;
                    }
                    super.write(ctx, msg, promise);
                }
            }));
    }

    public List<PacketListener> listeners() {
        return listeners;
    }

    public void inject() throws ReflectiveOperationException {
        injector.inject();
    }

    public Injector getInjector() {
        return injector;
    }

}
