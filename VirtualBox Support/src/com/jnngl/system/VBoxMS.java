package com.jnngl.system;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

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

    private static final Set<Long> threads = new HashSet<>();
    @Override
    public void getScreenPixels(ByteBuffer vb, ByteBuffer vm, BufferedImage dst) {
//        if(!threads.isEmpty()) return;
//        long tid = Thread.currentThread().getId();
//        threads.add(tid);
        getScreen(vb, vm, width, height, dst);
//        threads.remove(tid);
    }

    public native void getScreen(ByteBuffer vb, ByteBuffer vm, int[] width, int[] height, BufferedImage dst);

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
