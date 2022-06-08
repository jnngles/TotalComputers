package com.jnngl.server.exception;

public class InvalidPacketIdException extends InvalidPacketException {

    public InvalidPacketIdException(int id) {
        super("Invalid packet ID: "+id);
    }

}
