package com.jnngl.packet;

import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public interface MapPacketSender {

    public void sendMap(Player player, int mapId, BufferedImage data);
    public void sendPacket(Player player, Object packet) throws ReflectiveOperationException;
    public Object createPacket(int mapId, BufferedImage data) throws ReflectiveOperationException;
    public void modifyPacket(Object packet, BufferedImage tile) throws ReflectiveOperationException;

}
