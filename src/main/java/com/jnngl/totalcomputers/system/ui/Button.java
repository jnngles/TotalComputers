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

import java.awt.image.BufferedImage;
import java.util.List;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Button element
 */
public class Button implements ComponentUI {

    /**
     * Represents color of button
     */
    public enum ButtonColor {
        /**
         * White button color
         */
        WHITE(Color.WHITE, Color.GRAY),

        /**
         * Blue button color
         */
        BLUE(new Color(33, 150, 243), new Color(0, 87, 180));

        /**
         * Brighter color
         */
        public final Color brighter,

        /**
         * Darker color
         */
        darker;

        /**
         * Constructor
         * @param brighter Brighter color
         * @param darker Darker color
         */
        ButtonColor(Color brighter, Color darker) {
            this.brighter = brighter;
            this.darker = darker;
        }
    }

    private final ButtonColor color;
    private int x, y, width, height;
    private final Font font;
    private boolean isLocked, pressed;
    private String text;
    private final List<Event> events;

    /**
     * Constructor
     * @param color Color
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width
     * @param height Height
     * @param font Font
     * @param text Text
     */
    public Button(ButtonColor color, int x, int y, int width, int height, Font font, String text) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.isLocked = false;
        this.pressed = false;
        this.width = width;
        this.height = height;
        this.font = font;
        this.text = text;
        this.events = new ArrayList<>();
    }

    /**
     * Getter
     * @return this.color
     */
    public ButtonColor getColor() {
        return color;
    }

    /**
     * Getter
     * @return this.x
     */
    public int getX() {
        return x;
    }

    /**
     * Setter
     * @param x New X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter
     * @return this.y
     */
    public int getY() {
        return y;
    }

    /**
     * Setter
     * @param y New Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter
     * @return this.width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setter
     * @param width New width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter
     * @return this.height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter
     * @param height New height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter
     * @return this.font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Getter
     * @return this.isLocked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Setter
     * @param locked Locked or not
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Getter
     * @return this.text
     */
    public String getText() {
        return text;
    }

    /**
     * Setter
     * @param text New text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Registers new event on click
     * @param e Event
     */
    public void registerClickEvent(Event e) {
        events.add(e);
    }

    /**
     * Removes event
     * @param e Event
     */
    public void removeEvent(Event e) { events.remove(e); }

    /**
     * Removes event at specific index
     * @param index Index
     */
    public void removeEvent(int index) { events.remove(index); }

    /**
     * Renders button
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        if(isLocked) {
            g.setColor(color.darker);
            g.fillRoundRect(x, y, width, height, 4, 4);
            g.setColor(color.brighter);
        } else {
            g.setPaint(new GradientPaint(0, y, pressed ? color.darker : color.brighter,  0, y+height, pressed ? color.brighter : color.darker));
            pressed = false;
            g.fill(new RoundRectangle2D.Float(x, y, width, height, 4, 4));
            if(color == ButtonColor.WHITE) g.setColor(Color.BLACK);
            else if(color == ButtonColor.BLUE) g.setColor(Color.WHITE);
        }
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(text, x+width/2-metrics.stringWidth(text)/2, y+height/2+metrics.getHeight()/4);
        g.setColor(color.brighter);
        g.drawRoundRect(x, y, width, height, 6, 6);
    }

    /**
     * Handles input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        processInputN(x, y, type);
    }

    /**
     * Handles input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    public boolean processInputN(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK && !isLocked) {
            if(x >= this.x && y >= this.y && x <= this.x+width && y <= this.y+height) {
                for(Event event : events) event.action();
                pressed = true;
                return true;
            }
        }
        return false;
    }
}
