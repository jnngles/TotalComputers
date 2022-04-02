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

package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextEditor extends WindowApplication {

    private List<String> lines;
    private int startLine = 0;

    public static void main(String[] args) {
        ApplicationHandler.open(TextEditor.class, args[0], args);
    }

    public TextEditor(TotalOS os, String path, String[] args) {
        super(os, "Text Editor", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        lines = new ArrayList<>();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void update() {

    }

    @Override
    protected void render(Graphics2D g) {

    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }
}
