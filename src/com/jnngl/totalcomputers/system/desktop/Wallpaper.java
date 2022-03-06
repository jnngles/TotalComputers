/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
