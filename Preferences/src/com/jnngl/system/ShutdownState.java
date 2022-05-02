package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Rectangle;
import com.jnngl.totalcomputers.system.ui.Text;

import java.awt.*;

public class ShutdownState extends AppState {

    private final Rectangle bar;
    private final Button back;
    private final Text title;
    private final Font font;
    private final FontMetrics metrics;
    private Button shutdown, restart;

    private void updateUI() {
        int offset = bar.getHeight() / 5;
        shutdown = new Button(Button.ButtonColor.BLUE, application.getWidth()/3, 0,
                application.getWidth()/3, metrics.getHeight(), font, "Shutdown");
        shutdown.setY(application.getHeight()/2-shutdown.getHeight());
        shutdown.registerClickEvent(() -> application.os().turnOff());

        restart = new Button(Button.ButtonColor.BLUE, shutdown.getX(), application.getHeight()/2+offset,
                shutdown.getWidth(), metrics.getHeight(), font, "Restart");
        restart.registerClickEvent(() -> application.os().restart());
    }

    public ShutdownState(Preferences preferences) {
        super(preferences);
        bar = new com.jnngl.totalcomputers.system.ui.Rectangle(new Color(230, 230, 230), 0, 0,
                application.getWidth(), application.os().screenHeight / 3 * 2 / 10);
        int offset = bar.getHeight() / 5;
        font = application.os().baseFont.deriveFont((float) application.os().screenHeight / 128 * 3);
        metrics = Utils.getFontMetrics(font);
        back = new com.jnngl.totalcomputers.system.ui.Button(com.jnngl.totalcomputers.system.ui.Button.ButtonColor.WHITE
                , offset, offset, metrics.stringWidth("   Back    "),
                bar.getHeight() - offset * 2, font, "Back");
        back.registerClickEvent(() -> application.switchState(new BaseState(application)));
        title = new Text(application.getWidth() / 2 - metrics.stringWidth("Shutdown") / 2,
                bar.getHeight() / 2 - metrics.getHeight() / 2 + metrics.getAscent(), font, Color.BLACK,
                "Shutdown");

        updateUI();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, application.getWidth(), application.getHeight());
        bar.render(g);
        back.render(g);
        title.render(g);
        restart.render(g);
        shutdown.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        back.processInput(x, y, type);
        restart.processInput(x, y, type);
        shutdown.processInput(x, y, type);
    }

    @Override
    public void onResize() {
        bar.setWidth(application.getWidth());
        title.setX(application.getWidth()/2-metrics.stringWidth("Shutdown")/2);

        updateUI();
    }
}
