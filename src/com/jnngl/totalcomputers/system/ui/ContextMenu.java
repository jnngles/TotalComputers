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
import com.jnngl.totalcomputers.system.Utils;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ContextMenu {

    private interface RenderableContextMenuComponent {
        int render(Graphics2D g, int x, int y, int width);
    }

    private static class Separator implements RenderableContextMenuComponent {
        @Override
        public int render(Graphics2D g, int x, int y, int width) {
            g.setColor(Color.LIGHT_GRAY);
            g.setStroke(new BasicStroke(2));
            g.drawLine(x+3, y+3, x+width-3, y+3);
            return 6;
        }
    }

    private class ContextMenuEntry implements RenderableContextMenuComponent {
        public String text;
        public boolean locked;
        public Runnable action;
        public int x, y, width, height;

        @Override
        public int render(Graphics2D g, int x, int y, int width) {
            this.x = x+3;
            this.y = y;
            this.width = width-3;
            this.height = metrics.getHeight();
            g.setColor(locked? Color.LIGHT_GRAY : Color.BLACK);
            g.setFont(font);
            g.drawString(text, this.x, this.y+metrics.getAscent());
            return metrics.getHeight();
        }
    }

    private int x, y;
    private boolean shown;
    private final Rectangle sensitiveZone;
    private BufferedImage buffer;
    private final List<RenderableContextMenuComponent> components;
    private final FontMetrics metrics;
    private final Font font;

    private void updateBuffer(int width, int height) {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = buffer.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRoundRect(0, 0, width, height, 3, 3);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawRoundRect(0, 0, width, height, 3, 3);
        int y = 3;
        for(RenderableContextMenuComponent component : components)
            y += component.render(graphics, 0, y, width);
    }

    public ContextMenu(Rectangle sensitiveZone, Font font) {
        this.font = font;
        this.metrics = Utils.getFontMetrics(font);
        this.sensitiveZone = sensitiveZone;
        components = new ArrayList<>();
        updateBuffer(6, 6);
    }

    public ContextMenu(Font font) {
        this(null, font);
    }

    public int addSeparator() {
        RenderableContextMenuComponent instance = new Separator();
        components.add(instance);
        updateBuffer(buffer.getWidth(), buffer.getHeight()+6);
        return components.indexOf(instance);
    }

    public int addEntry(String text, boolean locked, Runnable action) {
        ContextMenuEntry instance = new ContextMenuEntry();
        instance.text = text;
        instance.locked = locked;
        instance.action = action;
        components.add(instance);
        int width = metrics.stringWidth(text)+6;
        updateBuffer(Math.max(width, buffer.getWidth()), buffer.getHeight()+metrics.getHeight());
        return components.indexOf(instance);
    }

    public void removeComponent(int index) {
        components.remove(index);
    }

    public int addSeparator(int index) {
        RenderableContextMenuComponent instance = new Separator();
        components.add(index, instance);
        updateBuffer(buffer.getWidth(), buffer.getHeight()+6);
        return components.indexOf(instance);
    }

    public int addEntry(int index, String text, boolean locked, Runnable action) {
        ContextMenuEntry instance = new ContextMenuEntry();
        instance.text = text;
        instance.locked = locked;
        instance.action = action;
        components.add(index, instance);
        int width = metrics.stringWidth(text)+6;
        updateBuffer(Math.max(width, buffer.getWidth()), buffer.getHeight()+metrics.getHeight());
        return components.indexOf(instance);
    }

    public Object getComponent(int index) {
        return components.get(index);
    }

    public void lockEntry(int index) {
        Object o = getComponent(index);
        if(o instanceof ContextMenuEntry entry) {
            entry.locked = true;
            updateBuffer(buffer.getWidth(), buffer.getHeight());
        }
    }

    public void unlockEntry(int index) {
        Object o = getComponent(index);
        if(o instanceof ContextMenuEntry entry) {
            entry.locked = false;
            updateBuffer(buffer.getWidth(), buffer.getHeight());
        }
    }

    public boolean isLocked(int index) {
        if(getComponent(index) instanceof ContextMenuEntry entry) return entry.locked;
        return false;
    }

    public void show(int x, int y) {
        this.x = x;
        this.y = y;
        shown = true;
    }


    public int getWidth() {
        return buffer.getWidth();
    }

    public int getHeight() {
        return buffer.getHeight();
    }

    public void close() {
        x = -1;
        y = -1;
        shown = false;
    }

    public void render(Graphics2D g) {
        if(shown) g.drawImage(buffer, x, y, null);
    }

    public boolean processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(type == TotalComputers.InputInfo.InteractType.RIGHT_CLICK) {
            if(sensitiveZone == null) return false;
            if(x > sensitiveZone.x && y > sensitiveZone.y &&
                    x < sensitiveZone.x+sensitiveZone.width && y < sensitiveZone.y+sensitiveZone.height) {
                show(x, y);
                return true;
            }
        } else {
            if (shown) {
                if (x > this.x && y > this.y && x < this.x+buffer.getWidth() && y < this.y+buffer.getHeight()) {
                    int locX = x-this.x;
                    int locY = y-this.y;
                    for(RenderableContextMenuComponent c : components) {
                        if(c instanceof ContextMenuEntry entry) {
                            if(locX >= entry.x && locY >= entry.y
                                    && locX <= entry.x+entry.width && locY <= entry.y+entry.height) {
                                entry.action.run();
                                break;
                            }
                        }
                    }
                    close();
                    return true;
                }
            }
            close();
        }
        return false;
    }

}
