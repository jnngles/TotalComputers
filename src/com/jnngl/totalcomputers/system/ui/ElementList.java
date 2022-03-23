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

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Selectable list of elements
 */
public class ElementList implements ComponentUI {

    private final List<String> entries;
    private int iOffset = 0;
    private int x, y, width, height;
    private int maxOffset = -1;
    private int rowHeight = -1;
    private int selected = -1;
    private final Font font;
    private final List<Event> eventsOnSelect, eventOnScroll;
    private boolean isLocked;

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Width
     * @param height Height
     * @param font Font
     * @param entryList Initial entries
     */
    public ElementList(int x, int y, int width, int height, Font font, String... entryList) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.font=font;
        entries = new ArrayList<>();
        entries.addAll(Arrays.asList(entryList));
        eventsOnSelect = new ArrayList<>();
        eventOnScroll = new ArrayList<>();
    }

    /**
     * Adds new entry
     * @param entry Entry
     */
    public void addEntry(String entry) {
        entries.add(entry);
    }

    /**
     * Removes entry
     * @param entry Entry
     */
    public void removeEntry(String entry) {
        entries.remove(entry);
    }

    /**
     * Removes entry at specific index
     * @param idx Index
     */
    public void removeEntry(int idx) {
        entries.remove(idx);
    }

    /**
     * Getter
     * @return Selected index
     */
    public int getSelectedIndex() {
        return selected;
    }

    /**
     * Getter
     * @return Selected element
     */
    public String getSelectedElement() {
        return selected >= 0? entries.get(selected) : null;
    }

    /**
     * Registers new event on item selection
     * @param e Event
     */
    public void registerItemSelectEvent(Event e) {
        eventsOnSelect.add(e);
    }

    /**
     * Removes event
     * @param e Event
     */
    public void removeSelectEvent(Event e) { eventsOnSelect.remove(e); }

    /**
     * Removes event at specific index
     * @param index Index
     */
    public void removeSelectEvent(int index) { eventsOnSelect.remove(index); }

    /**
     * Registers new event on scroll
     * @param e Event
     */
    public void registerScrollEvent(Event e) {
        eventOnScroll.add(e);
    }

    /**
     * Removes event
     * @param e Event
     */
    public void removeScrollEvent(Event e) { eventOnScroll.remove(e); }

    /**
     * Removes event at specific index
     * @param index Index
     */
    public void removeScrollEvent(int index) { eventOnScroll.remove(index); }

    /**
     * Setter
     * @param locked Locked or not
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Getter
     * @return this.isLocked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Renders element list
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int rowHeight = metrics.getHeight()+2;
        this.rowHeight = rowHeight;
        int nY = y;
        int i = iOffset;
        while(true) {
            if(i == selected) g.setColor(new Color(33, 150, 243));
            else g.setColor(i % 2 == 0? Color.LIGHT_GRAY.brighter() : Color.LIGHT_GRAY);
            boolean breakNeed = false;
            if(nY+rowHeight > y+height) {
                rowHeight = (y+height)-nY;
                breakNeed = true;
            }
            g.fillRect(x, nY, width, rowHeight);
            g.setColor(Color.BLACK);
            if(i < entries.size() && rowHeight == metrics.getHeight()+2)
                g.drawString(entries.get(i), x+10, nY+rowHeight-metrics.getHeight()/4);
            if(breakNeed) break;
            nY += rowHeight;
            i++;
        }
        g.setColor(Color.LIGHT_GRAY);
        g.fillRoundRect(x+width - 15, y, 15, height, 8, 8);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x+width - 16, y, 1, height);
        g.drawRoundRect(x, y, width, height, 6, 6);
    }

    /**
     * Setter
     * @param maxOffset Maximum times user can scroll list down
     */
    public void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    /**
     * Handles input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(isLocked) return;
        if(x >= this.x && y >= this.y && x <= this.x + width && y <= this.y + height) {
            if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                if(this.x + width - x < 15) { // Scrollbar
                    if(y - this.y > height/2) {
                        if(maxOffset < 0 || iOffset < maxOffset) {
                            iOffset++;
                            for(Event event : eventOnScroll)
                                event.action();
                        }
                    }
                    else if(iOffset > 0) {
                        iOffset--;
                        for(Event event : eventOnScroll)
                            event.action();
                    }
                }
                else { // Selection
                    int index = (int)Math.floor((float)(y - this.y) / rowHeight);
                    if(index >= 0 && index < entries.size()) {
                        selected = index;
                        for (Event event : eventsOnSelect) event.action();
                    }
                }
            }
        }
    }

    /**
     * Getter
     * @return Scroll
     */
    public int getScroll() {
        return iOffset;
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
     * Adds multiple entries
     * @param list Entries
     */
    public void addEntries(String... list) {
        entries.addAll(Arrays.asList(list));
    }

    /**
     * Removes all entries
     */
    public void clear() {
        entries.clear();
    }

    /**
     * Unselects element
     */
    public void unsetSelected() {
        selected = -1;
    }
}
