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

package com.jnngl.totalcomputers.system.desktop;

import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.image.BufferedImage;

abstract class Application {

    protected BufferedImage icon;
    protected final TotalOS os;
    protected String name;

    public Application(TotalOS os, String name) {
        this.os = os;
        icon = os.fs.getResourceImage("default-icon");
        this.name = name;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    protected void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    protected boolean close() {
        return onClose();
    }

    protected void start() {
        onStart();
    }

    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected abstract void onStart();
    protected abstract boolean onClose();

}
