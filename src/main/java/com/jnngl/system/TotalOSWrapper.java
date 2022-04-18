package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;

public class TotalOSWrapper extends WindowApplication {

    private TotalOS childOS;

    public TotalOSWrapper(TotalOS os, String path) {
        super(os, "TotalOS inside TotalOS", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    public static void main(String[] args) {
        ApplicationHandler.open(TotalOSWrapper.class, args[0]);
    }

    @Override
    protected void onStart() {
        childOS = new TotalOS(getWidth(), getHeight(), "test");
        childOS.turnOn();
    }

    @Override
    protected boolean onClose() {
        childOS.turnOff();
        return true;
    }

    @Override
    public void update() {
        renderCanvas();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(childOS.getScreen(), 0, 0, null);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        childOS.processTouch(x, y, type, os.requestAdminRights());
    }
}
