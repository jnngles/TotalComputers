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
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Wrapper for {@link java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, ImageObserver)} function
 */
public class Image {

    private int x, y, width, height;
    private BufferedImage image;

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width
     * @param height Height
     * @param image Image
     */
    public Image(int x, int y, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     * @param image Image
     */
    public Image(int x, int y, BufferedImage image) {
        this(x, y, image.getWidth(), image.getHeight(), image);
    }

    /**
     * Renders image at point [x;y]
     * @param g Graphics2D instanec
     */
    public void render(Graphics2D g) {
        if(image != null)
            g.drawImage(image, x, y, width, height, null);
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
     * @return this.image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Setter
     * @param image New image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
