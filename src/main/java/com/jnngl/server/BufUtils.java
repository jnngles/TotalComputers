package com.jnngl.server;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class BufUtils {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static int readVarInt(ByteBuf buf) {
        int value = 0;
        int position = 0;
        byte current;
        while(true) {
            current = buf.readByte();
            value |= (current & SEGMENT_BITS) << position;
            if((current & CONTINUE_BIT) == 0) break;
            position += 7;
            if(position >= 32)
                throw new RuntimeException("VarInt is too big");
        }
        return value;
    }

    public static long readVarLong(ByteBuf buf) {
        long value = 0;
        int position = 0;
        byte current;
        while(true) {
            current = buf.readByte();
            value |= (long)(current & SEGMENT_BITS) << position;
            if((current & CONTINUE_BIT) == 0) break;
            position += 7;
            if(position >= 64)
                throw new RuntimeException("VarLong is too big");
        }
        return value;
    }

    public static void writeVarInt(ByteBuf buf, int value) {
        while(true) {
            if((value & ~SEGMENT_BITS) == 0) {
                buf.writeByte(value);
                return;
            }
            buf.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);
            value >>>= 7;
        }
    }

    public static void writeVarLong(ByteBuf buf, long value) {
        while(true) {
            if((value & ~((long)SEGMENT_BITS)) == 0) {
                buf.writeByte((int)value);
                return;
            }
            buf.writeByte((int)((value & SEGMENT_BITS) | CONTINUE_BIT));
            value >>>= 7;
        }
    }

    public static void writeString(ByteBuf buf, String str) {
        buf.writeInt(str.length());
        buf.writeBytes(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes, 0, length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static int sizeofVarInt(int value) {
        int length = 0;
        while(true) {
            if((value & ~SEGMENT_BITS) == 0) {
                return length + 1;
            }
            length++;
            value >>>= 7;
        }
    }

    public static int sizeofVarLong(long value) {
        int length = 0;
        while(true) {
            if((value & ~((long)SEGMENT_BITS)) == 0) {
                return length + 1;
            }
            length++;
            value >>>= 7;
        }
    }

    public static int sizeofString(String str) {
        return 4 + str.length();
    }

}
