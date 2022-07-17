package com.jnngl.packet;

import com.jnngl.mapcolor.ColorMatcher;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MapPacketSender_R1_8 extends PacketSender implements MapPacketSender {

    private final Constructor<?> packet;
    private Field data;

    public MapPacketSender_R1_8() throws ReflectiveOperationException {
        super();

        Class<?> packetClass = Class.forName(pkg+".PacketPlayOutMap");
        packet = packetClass.getConstructor(int.class, byte.class, Collection.class, byte[].class, int.class, int.class, int.class, int.class);
        for(Field f : packetClass.getDeclaredFields()) {
            if(f.getType().equals(byte[].class)) {
                data = f;
                data.setAccessible(true);
                return;
            }
        }
        System.err.println("Failed to access data field");
    }

    @Override
    public void sendMap(Player player, int mapId, ThreadLocal<ColorMatcher> matcher, BufferedImage data) {
        try {
            Object packet = createPacket(mapId, matcher, data);
            sendPacket(player, packet);
        } catch (Throwable e) {
            System.err.println("Failed to create/send packet");
            System.err.println(" -> "+e.getMessage());
        }
    }

    @Override
    public Object createPacket(int mapId, ThreadLocal<ColorMatcher> matcher, BufferedImage data) throws ReflectiveOperationException {
        return packet
                .newInstance(mapId, (byte)0, new ArrayList<>(), matcher.get().matchImage(data), 0, 0, 128, 128);
    }

    @Override
    public void modifyPacket(Object packet, byte[] raw) throws ReflectiveOperationException {
        data.set(packet, raw);
    }

}
