package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.ui.Button;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BaseState extends AppState {

    private final Set<Button> buttons = new HashSet<>();
    private final Font font;

    private void init() {
        buttons.clear();
        Button tmp;
        buttons.add(tmp=new Button(Button.ButtonColor.WHITE, 0, 0,application.getWidth()/2,
                application.getHeight()/2, font, "Wallpaper"));
        tmp.registerClickEvent(() -> application.switchState(new WallpaperState(application)));
        buttons.add(tmp=new Button(Button.ButtonColor.WHITE, application.getWidth()/2, 0,
                application.getWidth()/2, application.getHeight()/2, font, "Account"));
        tmp.registerClickEvent(() -> application.switchState(new AccountState(application)));
        buttons.add(tmp=new Button(Button.ButtonColor.WHITE, 0, application.getHeight()/2,
                application.getWidth()/2, application.getHeight()/2, font, "Shutdown"));
        tmp.registerClickEvent(() -> application.switchState(new ShutdownState(application)));
        buttons.add(tmp=new Button(Button.ButtonColor.WHITE, application.getWidth()/2,
                application.getHeight()/2,
                application.getWidth()/2, application.getHeight()/2, font, "Reset"));
        tmp.registerClickEvent(() -> application.switchState(new ResetState(application)));
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
