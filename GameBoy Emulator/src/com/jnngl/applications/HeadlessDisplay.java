package com.jnngl.applications;

import eu.rekawek.coffeegb.gpu.Display;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeadlessDisplay implements Display {

    public static final float FPS_CAP = 60;
    public static final int DISPLAY_WIDTH = 160;
    public static final int DISPLAY_HEIGHT = 144;
    private final BufferedImage img;
    public static final int[] COLORS = new int[]{0xe6f8da, 0x99c886, 0x437969, 0x051f2a};
    private final int[] rgb;
    private int i;

    private boolean enabled;
    private long lastUpdate = 0;

    public HeadlessDisplay() {
        super();
        img = new BufferedImage(DISPLAY_WIDTH, DISPLAY_HEIGHT, BufferedImage.TYPE_INT_RGB);
        rgb = new int[DISPLAY_WIDTH * DISPLAY_HEIGHT];
    }

    @Override
    public void putDmgPixel(int color) {
        rgb[i++] = COLORS[color];
        i %= rgb.length;
    }

    @Override
    public void putColorPixel(int gbcRgb) {
        if(i >= rgb.length) return;
        rgb[i++] = translateGbcRgb(gbcRgb);
    }

    public static int translateGbcRgb(int gbcRgb) {
        int r = (gbcRgb >> 0) & 0x1f;
        int g = (gbcRgb >> 5) & 0x1f;
        int b = (gbcRgb >> 10) & 0x1f;
        int result = (r * 8) << 16;
        result |= (g * 8) << 8;
        result |= (b * 8) << 0;
        return result;
    }

    @Override
    public synchronized void requestRefresh() {
        i = 0;
        while(System.currentTimeMillis() - lastUpdate < 1000/FPS_CAP);
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public synchronized void waitForRefresh() {

    }

    @Override
    public void enableLcd() {
        enabled = true;
    }

    @Override
    public void disableLcd() {
        enabled = false;
    }

    protected BufferedImage render() {
        if(!enabled) img.setRGB(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, new int[DISPLAY_WIDTH*DISPLAY_HEIGHT], 0, DISPLAY_WIDTH);
        else img.setRGB(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, rgb, 0, DISPLAY_WIDTH);
        return img;
    }

}
