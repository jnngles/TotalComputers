package com.jnngl.server.protocol;

import com.jnngl.server.exception.PacketAlreadyExistsException;

public class Protocol {

    public static void registerPackets() throws PacketAlreadyExistsException, NoSuchMethodException {
        Packet.registerPacket(new ServerboundHandshakePacket());
        Packet.registerPacket(new ClientboundHandshakePacket());
    }

}
