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

package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ImageViewer extends WindowApplication {

    private int x, y, width, height;
    private float zoom;

    public ImageViewer(TotalOS os, String path, String[] args) {
        super(os, "Image Viewer", os.screenWidth/3*2, os.screenHeight/3*2, path);
        zoom = 0;
        if(args.length > 1) {
            loadImage(args[1]);
        }
        renderCanvas();
        addResizeEvent(new ResizeEvent() {
            @Override
            public void onResize(int width, int height) {
                updateLayout();
                renderCanvas();
            }

            @Override
            public void onMaximize(int width, int height) {
                updateLayout();
                renderCanvas();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                updateLayout();
                renderCanvas();
            }
        });
    }

    private void loadImage(String path) {
        image = os.fs.loadImage(path);
        updateLayout();
    }

    private void updateLayout() {
        int xOffset = 0, yOffset = 0;
        int screenWidth  = getWidth() - xOffset;
        int screenHeight = getHeight() - yOffset;
        if((float)image.getWidth()/image.getHeight() <= (float)screenWidth/screenHeight) {
            y = yOffset;
            height = screenHeight;
            float size = (float)height/image.getHeight();
            width = (int)(image.getWidth()*size);
            x = screenWidth/2-width/2+xOffset;
        } else {
            x = xOffset;
            width = screenWidth;
            float size = (float)width/image.getWidth();
            height = (int)(image.getHeight()*size);
            y = screenHeight/2-height/2+yOffset;
        }
        x -= zoom*width;
        y -= zoom*height;
        width += zoom*width*2;
        height += zoom*height*2;
        updateDithering();
    }

    public void updateDithering() {
        dithered_image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dithered_image.createGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        new Thread(() -> {
            Utils.floydSteinbergDithering(dithered_image);
            renderCanvas();
        }).start();
    }

    public static void main(String[] args) {
        ApplicationHandler.open(ImageViewer.class, args[0], args);
    }

    private BufferedImage image;
    private BufferedImage dithered_image;

    @Override
    protected void onStart() {
        setIcon(os.fs.loadImage(applicationPath+"/icon.png"));
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(dithered_image == null? image : dithered_image, x, y, width, height, null);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        zoom += (type == TotalComputers.InputInfo.InteractType.LEFT_CLICK? 1 : -1) * (1.f / Math.exp(Math.abs(zoom)));
        updateLayout();
        renderCanvas();
    }

}
