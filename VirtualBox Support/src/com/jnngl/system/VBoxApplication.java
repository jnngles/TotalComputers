package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class VBoxApplication extends WindowApplication {

    private IVBox iface;
    private ByteBuffer virtualBox;
    private ByteBuffer session;

    private int[] width = { 0 };
    private int[] height = { 0 };

    private BufferedImage buffer;

    public static void main(String[] args) {
        ApplicationHandler.open(VBoxApplication.class, args[0]);
    }

    public VBoxApplication(TotalOS os, String path) {
        super(os, "Virtual Box", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        System.load(applicationPath+"/vbox_native.dll");
        iface = new VBoxMS();
        virtualBox = iface.init();
        String[] names = iface.getMachineNames(virtualBox);
        session = iface.launchVM(virtualBox, names[0]);
    }

    @Override
    protected boolean onClose() {
        iface.closeVM(virtualBox, session);
        virtualBox = null;
        session = null;
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D graphics) {
        if(virtualBox == null || session == null) return;
        byte[] buffer = iface.getScreen(virtualBox, session, width, height);
        if(buffer == null || buffer.length < 4) return;

        if(this.buffer == null || this.buffer.getWidth() != width[0] || this.buffer.getHeight() != height[0])
            this.buffer = new BufferedImage(width[0], height[0], BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < this.buffer.getWidth(); x++) {
            for(int y = 0; y < this.buffer.getHeight(); y++) {
                int idx = (y*width[0]+x)*4;
                int r = (buffer[idx++] & 0xff);
                int g = (buffer[idx++] & 0xff);
                int b = (buffer[idx++] & 0xff);
                int a = (buffer[idx  ] & 0xff);
                int argb = (a << 24) | (r << 16) | (g << 8) | b;

                this.buffer.setRGB(x, y, argb);
            }
        }

        graphics.drawImage(this.buffer, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }
}
