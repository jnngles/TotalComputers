package com.jnngl.packet;

import com.jnngl.mapcolor.ColorMatcher;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public interface MapPacketSender {

    public void sendMap(Player player, int mapId, ThreadLocal<ColorMatcher> matcher, BufferedImage data);
    public void sendPacket(Player player, Object packet) throws ReflectiveOperationException;
    public Object createPacket(int mapId, ThreadLocal<ColorMatcher> matcher, BufferedImage data) throws ReflectiveOperationException;
    default public void modifyPacket(Object packet, ThreadLocal<ColorMatcher> matcher, BufferedImage tile) throws ReflectiveOperationException {
        modifyPacket(packet, matcher.get().matchImage(tile));
    };
    public void modifyPacket(Object packet, byte[] raw) throws ReflectiveOperationException;

}
