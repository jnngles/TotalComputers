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

package com.jnngl.totalcomputers.system.ui;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Event;
import com.jnngl.totalcomputers.system.RequiresAPI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiresAPI(apiLevel = 2)
public class ProgressBar implements ComponentUI {

    private int x, y;
    private int width, height;
    private boolean isLocked = false;
    private Color fillColor = new Color(33, 150, 243);
    private Color backgroundColor = new Color(235, 235, 235);
    private final List<Event> clickEvents;
    private float value;

    public ProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        clickEvents = new ArrayList<>();
        value = 0;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(backgroundColor);
        g.fillRoundRect(x, y, width, height, 5, 5);
        g.setColor(fillColor);
        g.fillRoundRect(x, y, (int)(width*value), height, 7, 7);
        g.setColor(isLocked? Color.BLACK : Color.GRAY);
        g.drawRoundRect(x, y, width, height, 5, 5);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(isLocked) return;
        if(x > this.x && y > this.y && x < this.x+this.width && y < this.y+this.height) {
            for(Event event : clickEvents) event.action();
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getValue() {
        return value*100;
    }

    public void setValue(float value) {
        this.value = Math.max(Math.min(value/100.f, 1), 0);
    }

    public void addClickEvent(Event event) {
        clickEvents.add(event);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void lock() {
        setLocked(true);
    }

    public void unlock() {
        setLocked(false);
    }

}
