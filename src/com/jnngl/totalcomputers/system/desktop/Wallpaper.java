package com.jnngl.totalcomputers.system.desktop;

import com.jnngl.totalcomputers.system.states.Desktop;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wallpaper {

    public enum ResizeType {
        NATIVE_SIZE,
        STRETCH,
        FIT_WIDTH,
        FIT_HEIGHT,
        AUTO_FULL,
        AUTO_CROP
    }

    private BufferedImage wallpaper;
    private final Desktop desktop;
    private ResizeType resizeType;

    private int x, y, width, height;

    public Wallpaper(Desktop desktop) {
        this.desktop = desktop;
        resizeType = ResizeType.AUTO_CROP;
        loadWallpaper();
    }

    public void render(Graphics2D g) {
        g.drawImage(wallpaper, x, y, width, height, null);
    }

    public void updateBounds() {
        if(resizeType == ResizeType.NATIVE_SIZE) {
            x = desktop.getOS().screenWidth/2-wallpaper.getWidth()/2;
            y = desktop.getOS().screenHeight/2-wallpaper.getHeight()/2;
            width = wallpaper.getWidth();
            height = wallpaper.getHeight();
        } else if(resizeType == ResizeType.STRETCH) {
            x = 0;
            y = 0;
            width = desktop.getOS().screenWidth;
            height = desktop.getOS().screenHeight;
        } else if(resizeType == ResizeType.FIT_WIDTH) {
            x = 0;
            width = desktop.getOS().screenWidth;
            float size = (float)width/wallpaper.getWidth();
            height = (int)(wallpaper.getHeight()*size);
            y = desktop.getOS().screenHeight/2-height/2;
        } else if(resizeType == ResizeType.FIT_HEIGHT) {
            y = 0;
            height = desktop.getOS().screenHeight;
            float size = (float)height/wallpaper.getHeight();
            width = (int)(wallpaper.getWidth()*size);
            x = desktop.getOS().screenWidth/2-width/2;
        } else if(resizeType == ResizeType.AUTO_FULL) {
            if((float)wallpaper.getWidth()/wallpaper.getHeight() <= (float)desktop.getOS().screenWidth/desktop.getOS().screenHeight) {
                y = 0;
                height = desktop.getOS().screenHeight;
                float size = (float)height/wallpaper.getHeight();
                width = (int)(wallpaper.getWidth()*size);
                x = desktop.getOS().screenWidth/2-width/2;
            } else {
                x = 0;
                width = desktop.getOS().screenWidth;
                float size = (float)width/wallpaper.getWidth();
                height = (int)(wallpaper.getHeight()*size);
                y = desktop.getOS().screenHeight/2-height/2;
            }
        } else if(resizeType == ResizeType.AUTO_CROP) {
            if((float)wallpaper.getWidth()/wallpaper.getHeight() > (float)desktop.getOS().screenWidth/desktop.getOS().screenHeight) {
                y = 0;
                height = desktop.getOS().screenHeight;
                float size = (float)height/wallpaper.getHeight();
                width = (int)(wallpaper.getWidth()*size);
                x = desktop.getOS().screenWidth/2-width/2;
            } else {
                x = 0;
                width = desktop.getOS().screenWidth;
                float size = (float)width/wallpaper.getWidth();
                height = (int)(wallpaper.getHeight()*size);
                y = desktop.getOS().screenHeight/2-height/2;
            }
        }
    }

    public void loadWallpaper() {
        wallpaper = desktop.getOS().fs.loadImage(desktop.getOS().fs.readFile("/sys/wallpaper"));
        updateBounds();
    }

    public void changeWallpaper(String path) {
        desktop.getOS().fs.writeWallpaper(path);
        loadWallpaper();
    }

    public void setWallpaper(BufferedImage wallpaper) {
        this.wallpaper = wallpaper;
        updateBounds();
    }

    public void setResizeType(ResizeType type) {
        this.resizeType = type;
        updateBounds();
    }

    public ResizeType getResizeType() {
        return resizeType;
    }

    public BufferedImage getWallpaper() {
        return wallpaper;
    }
}
