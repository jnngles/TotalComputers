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
import com.jnngl.totalcomputers.system.ui.ContextMenu;
import com.jnngl.totalcomputers.system.ui.RoundRectangle;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public class TaskBar {

    private final List<Application> applications;

    private final TotalOS os;
    private int iconSize;

    private int startX;
    private int startY;
    private final int offset;

    private RoundRectangle body;
    private final Color light, dark;
    private final Font font;

    private ContextMenu contextMenu;

    public TaskBar(TotalOS os, Font font) {
        this.os = os;
        this.font = font;
        light = new Color(1, 1, 1, 0.75f);
        dark = new Color(0, 0, 0, 0.75f);
        applications = new ArrayList<>(os.fs.loadTaskBarLinks(os));
        iconSize = os.screenHeight/16;
        offset = (int)(os.screenWidth * 0.0063f);
        updateBody();
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public void setBody(RoundRectangle body) {
        this.body = body;
    }

    private void updateBody() {
        int taskbarWidth = applications.size() * iconSize + (applications.size()>1? offset * (applications.size()-1) : 0) + (int)(os.screenWidth * 0.0125f);
        int taskbarHeight = (int)(os.screenWidth * 0.0125f) + iconSize;
        body = new RoundRectangle(Color.WHITE, os.screenWidth/2-taskbarWidth/2, os.screenHeight-taskbarHeight, taskbarWidth, taskbarHeight*2, 12);
        startX = body.getX()+offset;
        startY = body.getY()+offset;
    }

    public void addApplication(Application application) {
        applications.add(application);
        updateBody();
    }

    public void removeApplication(Application application) {
        applications.remove(application);
        updateBody();
    }

    public void removeApplication(int index) {
        applications.remove(index);
        updateBody();
    }

    public List<Application> applications() {
        return applications;
    }

    public void render(Graphics2D g) {
        body.setColor(light);
        body.render(g);
        body.setColor(dark);
        body.renderBorder(g);
        int x = startX;
        for(Application application : applications) {
            g.drawImage(application.getIcon(), x, startY, iconSize, iconSize, null);
            if(application instanceof WindowApplication windowApplication) {
                g.setColor(windowApplication.isMinimized()? Color.BLUE : Color.DARK_GRAY);
                g.fillRect(x+iconSize/5, startY+iconSize+2, (iconSize*3)/5, 20);
            }
            x+=iconSize+offset;
        }
        if(contextMenu != null) contextMenu.render(g);
    }

    public boolean processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(contextMenu != null && contextMenu.processInput(x, y, type)) return true;
        if(x > body.getX() && y > body.getY() && x <= body.getX() + body.getWidth()) {
            int index = ((int)Math.ceil((x-startX)/(float)(offset+iconSize)))-1;
            if(index < 0 || index >= applications.size()) return true;
            Application application = applications.get(index);
            if(application instanceof TaskBarLink) {
                if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                    application.start();
                } else {
                    contextMenu = new ContextMenu(font);
                    contextMenu.addEntry("Open", false, application::start);
                    contextMenu.addSeparator();
                    contextMenu.addEntry("Unpin", false,
                            () -> ApplicationHandler.removeTaskBarEntry(application.name));
                    contextMenu.show(x, y-contextMenu.getHeight());
                }
            } else if(application instanceof WindowApplication windowApplication) {
                if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                    if(windowApplication.isMinimized()) windowApplication.unminimize();
                    else windowApplication.minimize();
                } else {
                    contextMenu = new ContextMenu(font);
                    contextMenu.addEntry("Close", false, windowApplication::close);
                    contextMenu.addSeparator();
                    contextMenu.addEntry("Maximize", false,
                            () -> windowApplication.maximize(os.screenHeight/32));
                    contextMenu.addEntry("Unmaximize", false, windowApplication::unmaximize);
                    contextMenu.addSeparator();
                    contextMenu.addEntry("Minimize", false, windowApplication::minimize);
                    contextMenu.addEntry("Unminimize", false, windowApplication::unminimize);
                    contextMenu.show(x, y-contextMenu.getHeight());
                }
            }
            return true;
        }
        return false;
    }

}
