package com.jnngl.totalcomputers.system.desktop;

import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.image.BufferedImage;

abstract class Application {

    protected BufferedImage icon;
    protected final TotalOS os;
    protected String name;

    public Application(TotalOS os, String name) {
        this.os = os;
        icon = os.fs.getResourceImage("default-icon");
        this.name = name;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    protected void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    protected void close() {
        onClose();
    }

    protected void start() {
        onStart();
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected abstract void onStart();
    protected abstract boolean onClose();

}
