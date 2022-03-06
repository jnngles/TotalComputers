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

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.overlays.Keyboard;

import java.awt.*;
import java.util.Vector;

public abstract class ConsoleApplication extends WindowApplication {

    private Vector<Character> chars;
    private Color background, textColor;
    private Font font;
    private boolean hasNext;

    public ConsoleApplication(TotalOS os, String title, int width, int height, String path) {
        this(os, title, os.screenWidth/2-width/2, os.screenHeight/2-height/2, width, height, path);
    }

    public ConsoleApplication(TotalOS os, String title, int x, int y, int width, int height, String path) {
        super(path, os, title, x, y, width, height);
        chars = new Vector<>();
        background = Color.BLACK;
        textColor = Color.WHITE;
        font = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        start();
        renderCanvas();
    }

    private String line = "";

    public ConsoleApplication putString(String str) {
        for(char c : str.toCharArray()) {
            chars.add(c);
        }
        return this;
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(type == TotalComputers.InputInfo.InteractType.RIGHT_CLICK) {
            line = "";
            os.keyboard.invokeKeyboard((text, key, keyboard) -> {
                if(key == Keyboard.Keys.OK || key == Keyboard.Keys.ENTER) {
                    chars.add('\n');
                    os.keyboard.closeKeyboard();
                    renderCanvas();
                    hasNext = true;
                    return line;
                }
                if(key == Keyboard.Keys.BACKSPACE) {
                    if(line.length() > 0) {
                        line = line.substring(0, line.length()-1);
                        chars.remove(chars.size()-1);
                    }
                }
                if(key.text != null) {
                    for (char c : text.toCharArray()) {
                        chars.add(c);
                    }
                    line += text;
                }
                renderCanvas();
                return line;
            }, "");
        }
    }

    @Override
    protected void render(Graphics2D g) {
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int y = 3;
        y += metrics.getMaxAscent();
        int x = 3;
        g.setColor(textColor);
        for(char c : chars) {
            if(c == '\n') {
                y += metrics.getMaxAscent();
                x = 3;
            }
            String str = ""+c;
            g.drawString(str, x, y);
            x += metrics.charWidth(c);
            if(x >= getWidth()-3) {
                y += metrics.getMaxAscent();
                x = 3+metrics.charWidth(' ');
            }
        }
    }

    public boolean hasNext() {
        return hasNext;
    }

    public String next() {
        hasNext = false;
        return line;
    }

}
