/*
    Computers are now in minecraft!
    Copyright (C) 2021  JNNGL

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
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.TaskBar;
import com.jnngl.totalcomputers.system.desktop.Wallpaper;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Desktop
 */
public class Desktop extends State {

    public final List<WindowApplication> drawable;
    public final TaskBar taskbar;

    private final int titleBarHeight;
    private final int buttonSize;
    private final Color greenLight, yellowLight, redLight;
    private final int redX, buttonY, yellowX, greenX, titleX, titleY;

    private final Wallpaper wallpaper;
    private final Font uiFont;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public Desktop(StateManager stateManager, TotalOS os) {
        super(stateManager, os);
        uiFont = os.baseFont.deriveFont((float) os.screenHeight/128*3);
        titleBarHeight = os.screenHeight/32;
        buttonSize = (int)(titleBarHeight*0.66f);
        int offset = titleBarHeight / 2 - buttonSize / 2;
        greenLight = new Color(0, 202, 78);
        yellowLight = new Color(255, 189, 68);
        redLight = new Color(255, 96, 92);
        redX = offset;
        buttonY = -titleBarHeight+offset;
        yellowX = redX+buttonSize+offset;
        greenX = yellowX+buttonSize+offset;
        titleX = greenX+buttonSize*2-offset;
        titleY = Utils.getFontMetrics(uiFont).getHeight()/2;
        taskbar = new TaskBar(os);
        wallpaper = new Wallpaper(this);
        drawable = new ArrayList<>();
        ApplicationHandler.init(this);
    }

    /**
     * Update
     */
    @Override
    public void update() {
        for(WindowApplication application : drawable) application.update();
    }

    /**
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        wallpaper.render(g);
        for(WindowApplication application : drawable) {
            if(application.isMinimized()) continue;
            g.setColor(Color.WHITE);
            g.fillRoundRect(application.getX()-2, application.getY()-titleBarHeight, application.getWidth()+4, titleBarHeight*2, 10, 10);
            g.drawImage(application.getCanvas(), application.getX(), application.getY(), null);
            g.setColor(Color.BLACK);
            g.drawRect(application.getX()-1, application.getY()-1, application.getWidth()+1, application.getHeight()+1);
            g.setColor(Color.WHITE);
            g.drawRect(application.getX()-2, application.getY()-2, application.getWidth()+3, application.getHeight()+3);
            int y = application.getY()+buttonY;
            g.setColor(redLight);
            g.fillOval(application.getX()+redX, y, buttonSize, buttonSize);
            g.setColor(yellowLight);
            g.fillOval(application.getX()+yellowX, y, buttonSize, buttonSize);
            g.setColor(greenLight);
            g.fillOval(application.getX()+greenX, y, buttonSize, buttonSize);
            g.setColor(Color.BLACK);
            g.setFont(uiFont);
            g.drawString(application.getName(), application.getX()+titleX, y+titleY);
        }
        taskbar.render(g);
    }

    private WindowApplication wantToMove, wantToResize;
    private int wtOffsetX, wtOffsetY;

    /**
     * Input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!taskbar.processInput(x, y, type)) {
            if(type == TotalComputers.InputInfo.InteractType.RIGHT_CLICK) {
                if(wantToMove != null) {
                    wantToMove.move(wtOffsetX + x, wtOffsetY + y);
                    wantToMove = null;
                    return;
                } else if(wantToResize != null) {
                    if(x >= wantToResize.getX() && y >= wantToResize.getY())
                        wantToResize.resize(x-wantToResize.getX(), y-wantToResize.getY());
                    wantToResize = null;
                    return;
                }
            }
            wantToMove = null;
            wantToResize = null;
            ListIterator<WindowApplication> iterator = drawable.listIterator(drawable.size());
            while(iterator.hasPrevious()) {
                WindowApplication application = iterator.previous();
                if(application.isMinimized()) continue;
                if(x >= application.getX() && x <= application.getX()+application.getWidth()+5 && y >= application.getY()-titleBarHeight && y <= application.getY()+application.getHeight()+5) {
                    if(y <= application.getY()) { // Title bar
                        boolean _y = (y >= application.getY()+buttonY && y <= application.getY()+buttonY+buttonSize);
                        if(_y) {
                            if (x >= application.getX() + redX && x <= application.getX() + redX + buttonSize) {
                                application.close(); // Red Light
                                return;
                            }
                            if(x >= application.getX() + yellowX && x <= application.getX() + yellowX + buttonSize) {
                                // Yellow Light
                                application.minimize();
                                return;
                            }
                            if(x >= application.getX() + greenX && x <= application.getX() + greenX + buttonSize) {
                                // Green Light
                                if(application.isMaximized()) application.unmaximize();
                                else application.maximize(titleBarHeight);
                                return;
                            }
                        }

                        // Move
                        wantToMove = application;
                        wtOffsetX = application.getX()-x;
                        wtOffsetY = application.getY()-y;
                        return;
                    } else if(x >= application.getX() && x <= application.getX()+application.getWidth() && y >= application.getY() && y <= application.getY()+application.getHeight()) { // App touch
                        application.processInput(x - application.getX(), y - application.getY(), type);
                        return;
                    } else { // Resize touch
                        wantToResize = application;
                        return;
                    }
                }
            }
        }
    }

    public TotalOS getOS() {
        return os;
    }
}
