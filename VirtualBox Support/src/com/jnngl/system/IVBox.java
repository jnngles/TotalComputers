package com.jnngl.system;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public interface IVBox {

    public String[] getMachineNames(ByteBuffer vb);
    public ByteBuffer launchVM(ByteBuffer vb, String name);
    public void closeVM(ByteBuffer vb, ByteBuffer vm);
    public void getScreenPixels(ByteBuffer vb, ByteBuffer vm, BufferedImage dst);
    public int getWidth();
    public int getHeight();
    public ByteBuffer init();
    public void touch(ByteBuffer vm, int x, int y, boolean isLeft);
    public void keyType(ByteBuffer vm, int[] scancodes);

}
