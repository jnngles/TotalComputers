package com.jnngl.server.exception;

public class TooSmallPacketException extends Exception {

    public TooSmallPacketException(int readableBytes, int expectedMinSize) {
        super("Packet was too small ("+readableBytes+" < "+expectedMinSize+")");
    }

}
