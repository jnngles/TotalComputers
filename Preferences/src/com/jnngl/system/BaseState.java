package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.ui.Button;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BaseState extends AppState {

    private Set<Button> buttons = new HashSet<>();
    private Font font;

    private void init() {
        buttons.clear();
        Button tmp;
        buttons.add(tmp=new Button(Button.ButtonColor.WHITE, 0, 0,application.getWidth()/2,
                application.getHeight()/3, font, "Wallpaper"));
        tmp.registerClickEvent(() -> application.switchState(new WallpaperState(application)));
    }

    public BaseState(Preferences preferences) {
        super(preferences);
        this.font = application.os().baseFont.deriveFont((float)application.os().screenHeight/128*5);
        init();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, application.getWidth(), application.getHeight());
        for(Button bt : buttons)
            bt.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        for(Button bt : buttons)
            bt.processInput(x, y, type);
    }

    @Override
    public void onResize() {
        init();
    }

}
