package com.jnngl.system;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;
import java.io.Serializable;

public class SerializableImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int[] data;
    private int width;
    private int height;

    public static SerializableImage fromBufferedImage(BufferedImage source) {
        SerializableImage dst = new SerializableImage();
        dst.data = ((DataBufferInt)source.getRaster().getDataBuffer()).getData();
        dst.width = source.getWidth();
        dst.height = source.getHeight();
        return dst;
    }

    public BufferedImage toBufferedImage() {
        BufferedImage dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.arraycopy(data, 0,
                ((DataBufferInt)dst.getRaster().getDataBuffer()).getData(), 0, data.length);
        return dst;
    }

}
