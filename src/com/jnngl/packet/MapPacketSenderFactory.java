package com.jnngl.packet;

public class MapPacketSenderFactory {

    public static MapPacketSender createMapPacketSender(String v) throws ReflectiveOperationException {
        if(v.contains("1.8")) return new MapPacketSender_R1_8();
        if(v.contains("1.9") || v.contains("1.10") || v.contains("1.11") || v.contains("1.12") || v.contains("1.13"))
            return new MapPacketSender_R1_9();
        if(v.contains("1.14") || v.contains("1.15") || v.contains("1.16")) return new MapPacketSender_R1_14();
        return new MapPacketSender_R1_17();
    }

}
