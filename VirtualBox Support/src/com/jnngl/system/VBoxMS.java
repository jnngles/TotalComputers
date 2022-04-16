package com.jnngl.system;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

class VBoxMS implements IVBox {

    private final int[] width = { 0 };
    private final int[] height = { 0 };

    @Override
    public native String[] getMachineNames(ByteBuffer vb);

    @Override
    public native ByteBuffer launchVM(ByteBuffer vb, String name);

    @Override
    public native void closeVM(ByteBuffer vb, ByteBuffer vm);

    @Override
    public int getWidth() { return width[0]; }

    @Override
    public int getHeight() { return height[0]; }

    @Override
    public void getScreenPixels(ByteBuffer vb, ByteBuffer vm, BufferedImage dst) {
        ByteBuffer pixels = getScreen(vb.duplicate(), vm.duplicate(), width, height);
        if(pixels == null) return;

        pixels.clear();

        for(int y = 0; y < dst.getHeight(); y++) {
            for(int x = 0; x < dst.getWidth(); x++) {
                if(x >= dst.getWidth() || y >= dst.getHeight()) return;
                int r = (pixels.get() & 0xff);
                int g = (pixels.get() & 0xff);
                int b = (pixels.get() & 0xff);
                int a = (pixels.get() & 0xff);
                int argb = (a << 24) | (r << 16) | (g << 8) | b;

                dst.setRGB(x, y, argb);
            }
        }

    }

    public native ByteBuffer getScreen(ByteBuffer vb, ByteBuffer vm, int[] width, int[] height);

    @Override
    public native ByteBuffer init();

    public native void click(ByteBuffer vm, int x, int y, boolean isLeft);

    @Override
    public void touch(ByteBuffer vm, int x, int y, boolean isLeft) {
        click(vm.duplicate(), x, y, isLeft);
    }

    public native void key(ByteBuffer vm, int[] scancodes);

    @Override
    public void keyType(ByteBuffer vm, int[] scancodes) {
        key(vm.duplicate(), scancodes);
    }
}
