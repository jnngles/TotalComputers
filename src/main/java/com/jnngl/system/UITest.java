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
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.CheckBox;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ClickableText;
import com.jnngl.totalcomputers.system.ui.ProgressBar;

import java.awt.*;
import java.util.Random;

@RequiresAPI(apiLevel = 3)
public class UITest extends WindowApplication {

    private CheckBox checkBox;
    private Button lockButton;
    private ProgressBar progressBar;
    private ClickableText text;

    public static void main(String[] args) {
        ApplicationHandler.open(UITest.class, args[0]);
    }

    public UITest(TotalOS os, String path) {
        super(os, "UI Test", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        Font font = os.baseFont.deriveFont((float) os.screenHeight / 128 * 3);
        checkBox = new CheckBox("Test box", 10, 10, font, os);
        checkBox.addClickEvent(() -> checkBox.setLabel("value="+checkBox.getValue()));
        lockButton = new Button(Button.ButtonColor.BLUE, 10, 30, 100, 20, font, "Lock/Unlock");
        lockButton.registerClickEvent(() -> checkBox.setLocked(!checkBox.isLocked()));
        progressBar = new ProgressBar(10, 60, 100, 10);
        progressBar.addClickEvent(() -> progressBar.setValue(progressBar.getValue()+1));
        text = new ClickableText(10, 90, font, Color.BLACK, "Click me!");
        final Random rng = new Random();
        text.addClickEvent(() -> {
            text.setColor(new Color(rng.nextInt(255), rng.nextInt(255),rng.nextInt(255)));
        });
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    public void update() {
        renderCanvas();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        checkBox.render(g);
        lockButton.render(g);
        progressBar.render(g);
        text.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        checkBox.processInput(x, y, type);
        lockButton.processInput(x, y, type);
        progressBar.processInput(x, y, type);
        text.processInput(x, y, type);
    }
}
