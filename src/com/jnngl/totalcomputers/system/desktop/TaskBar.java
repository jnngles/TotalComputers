package com.jnngl.totalcomputers.system.desktop;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
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

    public TaskBar(TotalOS os) {
        this.os = os;
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
    }

    public boolean processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(x > body.getX() && y > body.getY() && x <= body.getX() + body.getWidth()) {
            if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                int index = ((int)Math.ceil((x-startX)/(float)(offset+iconSize)))-1;
                if(index < 0 || index >= applications.size()) return true;
                Application application = applications.get(index);
                if(application instanceof TaskBarLink) {
                    application.start();
                } else if(application instanceof WindowApplication windowApplication) {
                    if(windowApplication.isMinimized()) windowApplication.unminimize();
                    else windowApplication.minimize();
                }
            }
            return true;
        }
        return false;
    }

}
