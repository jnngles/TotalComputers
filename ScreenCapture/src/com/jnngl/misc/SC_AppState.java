package com.jnngl.misc;

import com.jnngl.totalcomputers.TotalComputers;

import java.awt.*;

abstract class SC_AppState {

    protected ScreenCapture application;

    public SC_AppState(ScreenCapture application) {
        this.application = application;
    }

    public abstract void render(Graphics2D g);
    public abstract void processInput(int x, int y, TotalComputers.InputInfo.InteractType type);

    public void onResize() {}

}
