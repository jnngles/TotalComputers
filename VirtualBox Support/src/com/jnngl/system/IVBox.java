package com.jnngl.system;

import java.nio.ByteBuffer;

public interface IVBox {

    public String[] getMachineNames(ByteBuffer vb);
    public ByteBuffer launchVM(ByteBuffer vb, String name);
    public void closeVM(ByteBuffer vb, ByteBuffer vm);
    public byte[] getScreen(ByteBuffer vb, ByteBuffer vm, int[] width, int[] height);
    public ByteBuffer init();

}
