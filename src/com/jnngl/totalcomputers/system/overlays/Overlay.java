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

package com.jnngl.totalcomputers.system.overlays;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.*;

/**
 * Abstraction for overlays
 */
public abstract class Overlay {

    /**
     * TotalOS instance
     */
    protected final TotalOS os;

    /**
     * Constructor
     * @param os TotalOS instance
     */
    public Overlay(TotalOS os) {
        this.os = os;
    }

    /**
     * Abstraction for render
     * @param g Graphics2D instance
     */
    public abstract void render(Graphics2D g);

    /**
     * Abstraction for input handling
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    public abstract void processInput(int x, int y, TotalComputers.InputInfo.InteractType type);

    /**
     * @return Whether control is taken by overlay
     */
    public abstract boolean isControlTaken();

    /**
     * See {@link com.jnngl.totalcomputers.system.states.State#ndcX(float)}
     * @param x NDC X
     * @return x
     */
    public int ndcX(float x) {
        return (int)((x * 0.5f + 0.5f)*os.screenWidth);
    }

    /**
     * See {@link com.jnngl.totalcomputers.system.states.State#ndcY(float)}
     * @param y NDC Y
     * @return y
     */
    public int ndcY(float y) {
        return (int)(((-y) * 0.5f + 0.5f)*os.screenHeight);
    }

}
