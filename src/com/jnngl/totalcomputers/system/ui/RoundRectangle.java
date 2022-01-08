/*
    Computers are now in minecraft!
    Copyright (C) 2021  JNNGL

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

import java.awt.*;

/**
 * Alternative for {@link java.awt.geom.RoundRectangle2D}
 */
public class RoundRectangle {

    private int x, y, width, height, arc;
    private Color color;

    /**
     * Constructor
     * @param color Color
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width
     * @param height Height
     * @param arc Arc size
     */
    public RoundRectangle(Color color, int x, int y, int width, int height, int arc) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.arc = arc;
        this.color = color;
    }

    /**
     * Renders round rectangle
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillRoundRect(x, y, width, height, arc, arc);
    }

    /**
     * Render the border of round rectangle
     * @param g Graphics2D instance
     */
    public void renderBorder(Graphics2D g) {
        g.setColor(color);
        g.drawRoundRect(x, y, width, height, arc, arc);
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
     * @return this.arc
     */
    public int getArc() {
        return arc;
    }

    /**
     * Setter
     * @param arc New arc size
     */
    public void setArc(int arc) {
        this.arc = arc;
    }

    /**
     * Getter
     * @return this.color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter
     * @param color New color
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
