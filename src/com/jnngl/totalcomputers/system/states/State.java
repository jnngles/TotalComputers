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

package com.jnngl.totalcomputers.system.states;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.*;

/**
 * Abstraction for computers states
 */
public abstract class State {

    /**
     * StateManager instance
     */
    protected final StateManager stateManager;

    /**
     * TotalOS instance
     */
    protected final TotalOS os;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public State(StateManager stateManager, TotalOS os) {
        this.stateManager = stateManager;
        this.os = os;
    }

    /**
     * Converts X from NDC coordinate system to AWT coordinate system
     * @param x NDC X
     * @return x
     */
    public int ndcX(float x) {
        return (int)((x * 0.5f + 0.5f)*os.screenWidth);
    }

    /**
     * Converts Y from NDC coordinate system to AWT coordinate system
     * @param y NDC Y
     * @return y
     */
    public int ndcY(float y) {
        return (int)(((-y) * 0.5f + 0.5f)*os.screenHeight);
    }

    /**
     * Abstraction of update function
     */
    public abstract void update();

    /**
     * Abstraction of render function
     * @param g Graphics2D instance
     */
    public abstract void render(Graphics2D g);

    /**
     * Abstraction of input handling function
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    public abstract void processInput(int x, int y, TotalComputers.InputInfo.InteractType type);

}
