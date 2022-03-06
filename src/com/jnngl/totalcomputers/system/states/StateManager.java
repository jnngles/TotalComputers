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

import java.awt.*;

/**
 * Switching between states
 */
public class StateManager {

    private State currentState = null;

    /**
     * Constructor. Do nothing
     */
    public StateManager() {}

    /**
     * Sets state
     * @param state New state
     */
    public void setState(State state) {
        currentState = state;
    }

    /**
     * Returns current state
     * @return State
     */
    public State getState() {
        return currentState;
    }

    /**
     * Update state if state is present
     */
    public void update() {
        if(currentState != null)
            currentState.update();
    }

    /**
     * Render state if state is present
     * @param g Graphics2D g
     */
    public void render(Graphics2D g) {
        if(currentState != null)
            currentState.render(g);
    }

    /**
     * Process input if state is present
     * @param x X coordinate of touch
     * @param y Y coordinate of touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(currentState != null)
            currentState.processInput(x, y, type);
    }

}
