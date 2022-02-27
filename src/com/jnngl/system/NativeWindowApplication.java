package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;
import java.io.File;

public class NativeWindowApplication extends WindowApplication {

    public native void _Init(String path, String[] args);
    public native boolean _OnClose();
    public native void _Update();
    public native void _Render(Graphics2D g);
    public native void _ProcessInput(int x, int y, boolean type);

    public static void main(String[] args) {
        System.load(new File(args[0]).getAbsolutePath()+"/application.dll");
        ApplicationHandler.open(NativeWindowApplication.class, new File(args[0]).getAbsolutePath(), args);
    }

    public NativeWindowApplication(TotalOS os, String path, String[] args) {
        super(path, os, "Unknown Native Application", 0, 0, 100, 100);
        ApplicationHandler.registerApplication(this);
        _Init(path, args);
        renderCanvas();
    }

    boolean ExtOnClose() {
        return _OnClose();
    }

    void ExtUpdate() {
        _Update();
    }

    void ExtRender(Graphics2D g) {
        _Render(g);
    }

    void ExtProcessInput(int x, int y, boolean type) {
        _ProcessInput(x, y, type);
    }

    @Override
    protected void onStart() {}

    @Override
    protected boolean onClose() {
        return ExtOnClose();
    }

    @Override
    public void update() {
        ExtUpdate();
    }

    @Override
    protected void render(Graphics2D g) {
        ExtRender(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        ExtProcessInput(x, y, type == TotalComputers.InputInfo.InteractType.RIGHT_CLICK);
    }

}
