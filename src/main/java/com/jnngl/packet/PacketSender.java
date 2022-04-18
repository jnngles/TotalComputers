package com.jnngl.packet;

import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PacketSender {

    private Method sendPackage;
    private Field connection;

    public String pkg;

    protected Class<?> resolveClass(String name, String p) {
        try {
            return Class.forName(pkg+"."+p+"."+name);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(p+"."+name);
            } catch (ClassNotFoundException ex) {
                try {
                    return Class.forName(pkg+"."+name);
                } catch (ClassNotFoundException exc) {
                    try {
                        return Class.forName("net.minecraft."+p+"."+name);
                    } catch (ClassNotFoundException classNotFoundException) {
                        try {
                            return Class.forName("net.minecraft.server."+p+"."+name);
                        } catch (ClassNotFoundException i) {
                            return null;
                        }
                    }
                }
            }
        }
    }

    private String getPackages(String prefix) throws ReflectiveOperationException {
        for(Package p : Package.getPackages()) {
            if(p.getName().startsWith(prefix)) return p.getName();
        }
        throw new ReflectiveOperationException("Failed to enumerate package");
    }

    public PacketSender() throws ReflectiveOperationException {
        pkg = getPackages("net.minecraft.server");
        Class<?> entityPlayer = resolveClass("EntityPlayer", "server.level");
        Class<?> playerConnection = resolveClass("PlayerConnection", "server.network");
        Class<?> packet = resolveClass("Packet", "network.protocol");
        try {
            sendPackage = playerConnection.getMethod("sendPacket", packet);
        } catch (Throwable e) {
            sendPackage = playerConnection.getMethod("a", packet);
        }
        for(Field field : entityPlayer.getDeclaredFields()) {
            if(field.getType().equals(playerConnection)) {
                connection = field;
                break;
            }
        }
    }

    public void sendPacket(Player player, Object packet) throws ReflectiveOperationException {
        Method getHandle = player.getClass().getDeclaredMethod("getHandle");
        getHandle.setAccessible(true);
        Object handle = getHandle.invoke(player);
        final Object connection = this.connection.get(handle);
        sendPackage.invoke(connection, packet);
    }

}
