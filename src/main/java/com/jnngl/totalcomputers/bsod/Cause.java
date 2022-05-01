package com.jnngl.totalcomputers.bsod;

public enum Cause {
    UNKNOWN(0x00),
    UNCAUGHT_EXCEPTION(0x01),
    PURPOSEFUL(0x02),
    OUT_OF_MEMORY(0x03);

    int code;
    Cause(int code) {
        this.code = code;
    }

    public static Cause fromCode(int code) {
        for(Cause cause : values()) {
            if(cause.code == code) return cause;
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

}
