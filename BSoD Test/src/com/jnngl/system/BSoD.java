package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;

@RequiresAPI(apiLevel = 5)
public class BSoD extends WindowApplication {

    public static void main(String[] args) {
        ApplicationHandler.open(BSoD.class, args[0]);
    }

    public BSoD(TotalOS os, String path) {
        super(os, "", 1, 1, path);
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
        (new int[0])[0] = 0;
    }

    @Override
    protected void render(Graphics2D g) {

    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }
}
