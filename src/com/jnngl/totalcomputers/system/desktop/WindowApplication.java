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

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class WindowApplication extends Application {

    public interface ResizeEvent {

        void onResize(int width, int height);
        void onMaximize(int width, int height);
        void onUnmaximize(int width, int height);

    }

    public interface MoveEvent {

        void onMove(int x, int y);

    }

    public interface MinimizeEvent {

        void onMinimize();
        void onUnminimize();

    }

    protected BufferedImage canvas;
    private int x, y, width, height;

    private boolean maximized;
    private boolean minimized;
    private int minX, minY, minW, minH;

    private int minWidth, minHeight;
    private int maxWidth, maxHeight;
    private boolean resizable;

    private final List<ResizeEvent> resizeEvents;
    private final List<MoveEvent> moveEvents;
    private final List<MinimizeEvent> minimizeEvents;

    protected String applicationPath;

    public WindowApplication(TotalOS os, String title, int width, int height, String path) {
        this(os, title, os.screenWidth/2-width/2, os.screenHeight/2-height/2, width, height, path);
    }

    public WindowApplication(TotalOS os, String title, int x, int y, int width, int height, String path) {
        this(path, os, title, x, y, width, height);
        start();
        renderCanvas();
    }

    protected WindowApplication(String path, TotalOS os, String title, int x, int y, int width, int height) {
        super(os, title);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        resizable = true;
        minWidth = os.screenWidth/16;
        minHeight = os.screenHeight/32;
        maxWidth = os.screenWidth;
        maxHeight = os.screenHeight;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        resizeEvents = new ArrayList<>();
        moveEvents = new ArrayList<>();
        minimizeEvents = new ArrayList<>();
        applicationPath = path;
    }

    @Override
    protected void start() {
        ApplicationHandler.registerApplication(this);
        onStart();
    }

    @Override
    public boolean close() {
        if(onClose()) {
            ApplicationHandler.unregisterApplication(this);
            return true;
        }
        return false;
    }

    public abstract void update();
    protected abstract void render(Graphics2D g);
    public abstract void processInput(int x, int y, TotalComputers.InputInfo.InteractType type);

    public void move(int x, int y) {
        this.x = x;
        this.y = y;

        for(MoveEvent moveEvent : moveEvents) moveEvent.onMove(x, y);
    }

    public void resize(int width, int height) {
        if(!resizable) return;
        this.width = width;
        this.height = height;
        updateCanvas();
        for(ResizeEvent resizeEvent : resizeEvents) resizeEvent.onResize(width, height);
    }

    public void maximize(int titleBarHeight) {
        if(!resizable) return;
        if(maximized) return;
        maximized = true;
        minX = x;
        minY = y;
        minW = width;
        minH = height;
        x = 0;
        y = titleBarHeight;
        width = os.screenWidth;
        height = os.screenHeight-titleBarHeight;
        updateCanvas();
        for(ResizeEvent resizeEvent : resizeEvents) resizeEvent.onMaximize(width, height);
    }

    private void updateCanvas() {
        if(width < minWidth) width = minWidth;
        if(height < minHeight) height = minHeight;
        if(width > maxWidth) width = maxWidth;
        if(height > maxHeight) height = maxHeight;
        BufferedImage image = canvas;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = canvas.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    public void unmaximize() {
        if(!resizable) return;
        if(!maximized) return;
        maximized = false;
        x = minX;
        y = minY;
        width = minW;
        height = minH;
        updateCanvas();
        for(ResizeEvent resizeEvent : resizeEvents) resizeEvent.onUnmaximize(width, height);
    }

    public void minimize() {
        minimized = true;
        for(MinimizeEvent minimizeEvent : minimizeEvents) minimizeEvent.onMinimize();
    }

    public void unminimize() {
        minimized = false;
        for(MinimizeEvent minimizeEvent : minimizeEvents) minimizeEvent.onUnminimize();
    }

    public boolean isMinimized() {
        return minimized;
    }

    public void renderCanvas() {
        Graphics2D graphics = canvas.createGraphics();
        render(graphics);
        graphics.dispose();
    }

    public BufferedImage getCanvas() {
        return canvas;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        for(MoveEvent moveEvent : moveEvents) moveEvent.onMove(x, y);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        for(MoveEvent moveEvent : moveEvents) moveEvent.onMove(x, y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(ResizeEvent resizeEvent : resizeEvents) resizeEvent.onUnmaximize(width, height);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(ResizeEvent resizeEvent : resizeEvents) resizeEvent.onUnmaximize(width, height);
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void addResizeEvent(ResizeEvent event) {
        resizeEvents.add(event);
    }

    public void removeResizeEvent(ResizeEvent event) {
        resizeEvents.remove(event);
    }

    public void addMoveEvent(MoveEvent event) {
        moveEvents.add(event);
    }

    public void removeMoveEvent(MoveEvent event) {
        moveEvents.remove(event);
    }

    public void addMinimizeEvent(MinimizeEvent event) {
        minimizeEvents.add(event);
    }

    public void removeMinimizeEvent(MinimizeEvent event) {
        minimizeEvents.remove(event);
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }
}
