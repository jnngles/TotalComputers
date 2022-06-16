package com.jnngl.packet;

import com.jnngl.totalcomputers.MapColor;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MapPacketSender_R1_17 extends PacketSender implements MapPacketSender {

    private final Constructor<?> imageData;
    private final Constructor<?> packet;
    private Field mapData, data;

    public MapPacketSender_R1_17() throws ReflectiveOperationException {
        super();

        final Class<?> imageDataClass = resolveClass("WorldMap$b", "world.level.saveddata.maps");
        final Class<?> packetClass = resolveClass("PacketPlayOutMap", "network.protocol.game");
        imageData = imageDataClass
                .getConstructor(int.class, int.class, int.class, int.class, byte[].class);
        packet = packetClass
                .getConstructor(int.class, byte.class, boolean.class, Collection.class, imageDataClass);
        try {
            data = imageDataClass.getDeclaredField("e");
        } catch (Throwable e) {
            data = imageDataClass.getDeclaredField("data");
        }
        data.setAccessible(true);

        for(Field f : packetClass.getDeclaredFields()) {
            if(f.getType().equals(imageDataClass)) {
                mapData = f;
                mapData.setAccessible(true);
                return;
            }
        }
        System.err.println("Failed to find image data field");
    }

    @Override
    public void sendMap(Player player, int mapId, BufferedImage data) {
        try {
            Object packet = createPacket(mapId, data);
            sendPacket(player, packet);
        } catch (Throwable e) {
            System.err.println("Failed to create/send packet");
            System.err.println(" -> "+e.getMessage());
        }
    }

    @Override
    public Object createPacket(int mapId, BufferedImage data) throws ReflectiveOperationException {
        final Object image = imageData
                .newInstance(0, 0, 128, 128, MapColor.toByteArray(data));
        return packet
                .newInstance(mapId, (byte) 0, false, new ArrayList<>(), image);
    }

    @Override
    public void modifyPacket(Object packet, byte[] raw) throws ReflectiveOperationException {
        data.set(mapData.get(packet), raw);
    }

}
