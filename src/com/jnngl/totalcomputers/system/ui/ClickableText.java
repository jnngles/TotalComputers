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
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@RequiresAPI(apiLevel = 2)
public class ClickableText extends Text implements ComponentUI {

    private boolean isLocked = false;
    private final List<Event> clickEvents = new ArrayList<>();

    public ClickableText(int x, int y, int width, int height, Font font, Color color, String text) {
        super(x, y, width, height, font, color, text);
    }

    public ClickableText(int x, int y, Font font, Color color, String text) {
        super(x, y, font, color, text);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(isLocked) return;
        int ascent = Utils.getFontMetrics(getFont()).getAscent();
        int _y = getY()-ascent;
        if(x >= getX() && y >= _y && x <= getX()+boundsWidth && y <= _y+boundsHeight) {
            for(Event event : clickEvents) event.action();
        }
    }

    public void addClickEvent(Event event) {
        clickEvents.add(event);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void lock() {
        setLocked(true);
    }

    public void unlock() {
        setLocked(false);
    }
}
