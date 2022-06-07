package com.jnngl.server;

import io.netty.channel.*;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Injector {

    public interface AbstractInjector { void inject(Channel channel); }
    private class InitInjector extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg)
                throws Exception {
            Channel channel = (Channel) msg;
            channel.pipeline().addLast(new ChannelInitializer<>() {
                @Override
                protected void initChannel(@NotNull Channel channel) {
                    channel.pipeline().addLast(new ChannelDuplexHandler() {
                        private boolean injected = false;
                        @Override
                        public void channelRead(@NotNull ChannelHandlerContext ctx, @NotNull Object msg)
                                throws Exception {
                            if(!injected) {
                                ctx.pipeline().remove(this);
                                inject(ctx.channel());
                                injected = true;
                            }
                            super.channelRead(ctx, msg);
                        }
                        @Override
                        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                                throws Exception {
                            if(!injected) {
                                ctx.pipeline().remove(this);
                                inject(ctx.channel());
                                injected = true;
                            }
                            super.write(ctx, msg, promise);
                        }
                        public void inject(Channel channel) {
                            for(AbstractInjector injector : injectorList) {
                                injector.inject(channel);
                            }
                        }
                    });
                }
            });
            super.channelRead(ctx, msg);
        }
    }

    private InitInjector initInjector;
    private final List<AbstractInjector> injectorList = new LinkedList<>();
    private List<ChannelFuture> channels;

    public Injector(Plugin plugin) {
        Bukkit.getServicesManager().register(Injector.class, this, plugin, ServicePriority.Highest);
    }

    @SuppressWarnings("unchecked")
    public void inject() throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, NoSuchFieldException {
        Server bs = Bukkit.getServer();
        Field console = bs.getClass().getDeclaredField("console");
        console.setAccessible(true);
        Object ms = console.get(bs);
        Method gcon = ms.getClass().getMethod("getServerConnection");
        Object con = gcon.invoke(ms);
        Field fchannels = null;
        for(Field field : con.getClass().getDeclaredFields()) {
            if(!(field.getGenericType() instanceof ParameterizedType type)) continue;
            if(type.getRawType() != List.class) continue;
            Type firstParam = type.getActualTypeArguments()[0];
            if(!firstParam.getTypeName().endsWith("ChannelFuture")) continue;
            fchannels = field;
            break;
        }
        if(fchannels == null) {
            System.out.println("Unable to access open connections field");
            return;
        }
        fchannels.setAccessible(true);
        channels = (List<ChannelFuture>) fchannels.get(con);
        initInjector = new InitInjector();
        for(ChannelFuture future : channels) {
            future.channel().pipeline().addFirst(initInjector);
            System.out.println("Injected -> "+future);
        }
        List<ChannelFuture> newChannels = new ListenableList<>(channels);
        ((ListenableList<ChannelFuture>)newChannels)
                .addListener(new ListenableList.Listener<>() {
                    @Override
                    public void onAdd(ChannelFuture futureChannel) {
                        futureChannel.channel().pipeline().addFirst(initInjector);
                        System.out.println("Injected -> "+futureChannel);
                    }
                });
        newChannels = Collections.synchronizedList(newChannels);
        channels = newChannels;
        fchannels.set(con, newChannels);
    }

    public void removeInjection() {
        for(ChannelFuture openChannel : channels) {
            openChannel.channel().pipeline().remove(initInjector);
        }
    }

    public List<AbstractInjector> injectors() {
        return injectorList;
    }

}
