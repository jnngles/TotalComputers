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
import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiresAPI(apiLevel = 2)
public class CheckBox implements ComponentUI {

    private int x, y;
    private String label;
    private final int size;
    private Font font;
    private boolean value, isLocked;
    private final List<Event> clickEvents;
    private static final Color blue = new Color(33, 150, 243);

    public CheckBox(String label, int x, int y, Font font, TotalOS os) {
        clickEvents = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.font = font;
        this.label = label;
        this.isLocked = false;
        size = (int) (os.screenHeight * 0.03f);
    }

    public void addClickEvent(Event event) {
        clickEvents.add(event);
    }

    @Override
    public void render(Graphics2D g) {
        if(value) {
            g.setColor(blue);
            g.fillRoundRect(x, y, size, size, 5, 5);
            g.setColor(Color.WHITE);
            g.fillRoundRect(x+size/3, y+size/3, size/3*2, size/3*2, 3, 3);
        } else {
            g.setColor(isLocked? Color.LIGHT_GRAY : Color.WHITE);
            g.fillRoundRect(x, y, size, size, 5, 5);
        }
        g.setColor(isLocked? Color.GRAY : Color.BLACK);
        g.drawRoundRect(x, y, size, size, 5, 5);
        g.setFont(font);
        g.drawString(label, x+size+10, y+size-g.getFontMetrics().getHeight()/4);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!isLocked && inside(x, y)) {
            value = !value;
            for(Event event : clickEvents) event.action();
        }
    }

    protected boolean inside(int x, int y) {
        return (x >= this.x && y >= this.y && x <= this.x+size && y <= this.y+size);
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

    public void setValue(boolean value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean getValue() {
        return value;
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
