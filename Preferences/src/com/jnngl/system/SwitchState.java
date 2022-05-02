package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SwitchState extends AppState {

    private float x;
    private final BufferedImage overlay;
    private AppState to;

    private void slide() {
        long lastTime = System.currentTimeMillis();
        while(x > 0) {
            while(System.currentTimeMillis()-lastTime < 1000/60);
            lastTime = System.currentTimeMillis();
            x -= application.getWidth()/15.f;
        }
        x = 0;
        application.setState(to);
    }

    public SwitchState(Preferences preferences, AppState to) {
        super(preferences);
        this.to = to;
        x = preferences.getWidth();
        overlay = new BufferedImage(application.getWidth(), application.getHeight(), BufferedImage.TYPE_INT_RGB);
        to.render(overlay.createGraphics());
        new Thread(this::slide).start();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(overlay, (int)x, 0, application.getWidth(), application.getHeight(), null);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }
}
