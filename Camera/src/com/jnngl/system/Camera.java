package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.GLWindow;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;

import java.awt.*;

public class Camera extends GLWindow {

    public static void main(String[] args) {
        ApplicationHandler.open(Camera.class, args[0]);
    }

    public Camera(TotalOS os, String title, int width, int height, String path) {
        super(os, title, width, height, path);
    }

    @Override
    protected void renderGL() {

    }

    @Override
    protected void updateGL() {

    }

    @Override
    protected void onStart() {

    }

    @Override
    protected boolean onClose() {
        return false;
    }

    @Override
    protected void render(Graphics2D g) {

    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }
}
