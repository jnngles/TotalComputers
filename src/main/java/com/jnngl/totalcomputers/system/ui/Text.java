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

import com.jnngl.totalcomputers.system.Utils;

import java.awt.*;

/**
 * Text UI Element
 */
public class Text {

    private int x, y, width, height;
    private Font font;
    private Color color;
    private String text;
    protected int boundsWidth, boundsHeight;

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Maximum width
     * @param height Maximum height
     * @param font Font
     * @param color Color
     * @param text Text
     */
    public Text(int x, int y, int width, int height, Font font, Color color, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;
        this.color = color;
        updateText(text);
    }

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     * @param font Font
     * @param color Color
     * @param text Text
     */
    public Text(int x, int y, Font font, Color color, String text) {
        this(x, y, Integer.MAX_VALUE, Integer.MAX_VALUE, font, color, text);
    }

    /**
     * Renders the text
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        g.setColor(color);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int height = metrics.getHeight();
        int y = this.y-height;
        for (String line : text.split("\n"))
            g.drawString(line, x, y += height);
    }

    /**
     * Updates the text layout
     * @param text Source text
     */
    private void updateText(String text) {
        this.text = "";
        int line = 1;
        FontMetrics m = Utils.getFontMetrics(font);
        int fHeight = m.getHeight();
        if(m.stringWidth(text) < width) {
            this.text = text;
        } else {
            String[] words = text.split(" ");
            StringBuilder formatted = new StringBuilder();
            StringBuilder currentLine = new StringBuilder(words[0]);
            for(int i = 1; i < words.length; i++) {
                if(m.stringWidth(currentLine+words[i]) < width) {
                    currentLine.append(" ").append(words[i]);
                } else {
                    line++;
                    if(line*fHeight > height) {
                        if(formatted.length() > 3) {
                            this.text = formatted.substring(0, formatted.length()-3);
                            this.text += "...";
                            return;
                        }
                    }
                    currentLine.append("\n");
                    formatted.append(currentLine);
                    currentLine = new StringBuilder(words[i]);
                }
            }
            if(currentLine.toString().trim().length() > 0) {
                formatted.append(currentLine);
            }
            this.text = formatted.toString();
        }
        FontMetrics metrics = Utils.getFontMetrics(font);
        boundsHeight = 0;
        boundsWidth = 0;
        for (String _line : text.split("\n")) {
            int strWidth = metrics.stringWidth(_line);
            boundsWidth = Math.max(boundsWidth, strWidth);
            boundsHeight += metrics.getHeight();
        }
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
        updateText(text);
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
        updateText(text);
    }

    /**
     * Getter
     * @return this.font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Setter
     * @param font New font
     */
    public void setFont(Font font) {
        this.font = font;
        updateText(text);
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
        updateText(text);
    }
}
