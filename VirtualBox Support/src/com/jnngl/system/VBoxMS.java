package com.jnngl.system;

import java.nio.ByteBuffer;

class VBoxMS implements IVBox {

    @Override
    public native String[] getMachineNames(ByteBuffer vb);

    @Override
    public native ByteBuffer launchVM(ByteBuffer vb, String name);

    @Override
    public native void closeVM(ByteBuffer vb, ByteBuffer vm);

    @Override
    public native byte[] getScreen(ByteBuffer vb, ByteBuffer vm, int[] width, int[] height);

    @Override
    public native ByteBuffer init();

}
