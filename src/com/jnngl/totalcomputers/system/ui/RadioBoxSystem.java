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

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Chain of the radio boxes
 */
public class RadioBoxSystem {

    /**
     * Simple interface
     */
    public interface RadioBoxSelectEvent {

        /**
         * Called when the radio box selected
         * @param index Radio box index
         * @param box Radio box
         */
        void radioBoxSelected(int index, RadioBox box);

    }

    private final List<RadioBox> boxes;
    private int selected;
    private RadioBoxSelectEvent listener;

    /**
     * Constructor
     * @param selected selected radio box
     * @param boxes Radio boxes
     */
    public RadioBoxSystem(int selected, RadioBox... boxes) {
        this.boxes = new ArrayList<>();
        int i = 0;
        for(RadioBox box : boxes) {
            box.setSelected(selected == i);
            this.boxes.add(box);
            i++;
        }
        this.selected = selected;
    }

    /**
     * Sets the event on radio box selected
     * @param event Event
     */
    public void setListener(RadioBoxSelectEvent event) {
        listener = event;
    }

    /**
     * Renders all the radio boxes
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        for(RadioBox box : boxes) box.render(g);
    }

    /**
     * Handles input
     * @param x X coordinate
     * @param y Y coordinate
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(type != TotalComputers.InputInfo.InteractType.LEFT_CLICK) return;
        int i = 0;
        for(RadioBox box : boxes) {
            if(box.inside(x, y)) {
                if(box.isLocked()) return;
                boxes.get(selected).setSelected(false);
                box.setSelected(true);
                if(listener != null) listener.radioBoxSelected(i, box);
                selected = i;
                return;
            }
            i++;
        }
    }

    /**
     * Getter
     * @return Index of the selected element
     */
    public int getSelectedIndex() {
        return selected;
    }

    /**
     * Getter
     * @return The selected element
     */
    public RadioBox getSelected() {
        return boxes.get(selected);
    }

    /**
     * Getter
     * @param index Index
     * @return Element at specific index
     */
    public RadioBox getAt(int index) {
        if(index >= boxes.size()) return null;
        return boxes.get(index);
    }

}
