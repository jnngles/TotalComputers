package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;

import java.awt.*;

abstract class AppState {

    protected Preferences application;

    public AppState(Preferences preferences) {
        this.application = preferences;
    }

    public abstract void render(Graphics2D g);
    public abstract void processInput(int x, int y, TotalComputers.InputInfo.InteractType type);

    public void onResize() {}

}
