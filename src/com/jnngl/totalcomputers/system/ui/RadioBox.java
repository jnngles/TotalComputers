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

import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.*;

/**
 * Element of radio box system
 */
public class RadioBox {

    private String label;
    private boolean selected, isLocked;
    private final Font font;
    private int x, y;
    private final int size;
    private static final Color blue = new Color(33, 150, 243);

    /**
     * Constructor
     * @param label Label. Will be displayed at the right from radio box
     * @param x X coordinate
     * @param y Y coordinate
     * @param font Font
     * @param os TotalOS instance
     */
    public RadioBox(String label, int x, int y, Font font, TotalOS os) {
        this.label = label;
        this.selected = false;
        this.font = font;
        this.x = x;
        this.y = y;
        size = (int) (os.screenHeight * 0.03f);
    }

    /**
     * Renders the radio box
     * @param g Grapgics2D instnance
     */
    protected void render(Graphics2D g) {
        if(selected) {
            g.setColor(blue);
            g.fillOval(x, y, size, size);
            g.setColor(Color.WHITE);
            g.fillOval(x + size / 2 - size / 4, y + size / 2 - size / 4, size / 2, size / 2);
        } else {
            g.setColor(isLocked? Color.LIGHT_GRAY : Color.WHITE);
            g.fillOval(x, y, size, size);
        }
        g.setColor(isLocked? Color.GRAY : Color.BLACK);
        g.drawOval(x, y, size, size);
        g.setFont(font);
        g.drawString(label, x+size+10, y+size-g.getFontMetrics().getHeight()/4);
    }

    /**
     * Getter
     * @return this.label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter
     * @param label New label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter
     * @return this.selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setter
     * @param selected Selected or not
     */
    protected void setSelected(boolean selected) {
        this.selected = selected;
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
     * @return this.locked
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
     * @return this.size
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return Whether the point is inside radio box or not
     */
    protected boolean inside(int x, int y) {
        return (x >= this.x && y >= this.y && x <= this.x+size && y <= this.y+size);
    }
}
