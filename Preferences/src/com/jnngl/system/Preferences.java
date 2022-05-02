package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;

public class Preferences extends WindowApplication {

    private AppState state;

    public static void main(String[] args) {
        ApplicationHandler.open(Preferences.class, args[0]);
    }

    public Preferences(TotalOS os, String path) {
        super(os, "Preferences", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    public void switchState(AppState state) {
        this.state = new SwitchState(this, state);
    }

    @Override
    protected void onStart() {
        state = new BaseState(this);
        addResizeEvent(new ResizeEvent() {
            @Override
            public void onResize(int width, int height) {
                state.onResize();
            }

            @Override
            public void onMaximize(int width, int height) {
                state.onResize();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                state.onResize();
            }
        });
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        state.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        state.processInput(x, y, type);
    }

    TotalOS os() {
        return os;
    }

    public void setState(AppState state) {
        this.state = state;
    }

}
