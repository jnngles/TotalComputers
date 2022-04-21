package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;

public class CitraApplication extends WindowApplication {

    public static void main(String[] args) {
        ApplicationHandler.open(CitraApplication.class, args[0]);
    }

    public CitraApplication(TotalOS os, String path) {
        super(os, "Citra", os.screenWidth, os.screenHeight, path);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void update() {

    }

    @Override
    protected void render(Graphics2D g) {

    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }
}
