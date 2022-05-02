package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Field;
import com.jnngl.totalcomputers.system.ui.Text;
import com.jnngl.totalcomputers.system.ui.Rectangle;

import java.awt.*;
import java.io.File;

public class ResetState extends AppState {

    private final Rectangle bar;
    private final Button back, reset;
    private final Text title;
    private final FontMetrics metrics;
    private final Text wline1, wline2;
    private final Field confirmField;

    public ResetState(Preferences preferences) {
        super(preferences);
        bar = new com.jnngl.totalcomputers.system.ui.Rectangle(new Color(230, 230, 230), 0, 0,
                application.getWidth(), application.os().screenHeight / 3 * 2 / 10);
        int offset = bar.getHeight() / 5;
        Font font = application.os().baseFont.deriveFont((float) application.os().screenHeight / 128 * 3);
        metrics = Utils.getFontMetrics(font);
        back = new Button(com.jnngl.totalcomputers.system.ui.Button.ButtonColor.WHITE
                , offset, offset, metrics.stringWidth("   Back    "),
                bar.getHeight() - offset * 2, font, "Back");
        back.registerClickEvent(() -> application.switchState(new BaseState(application)));
        title = new Text(application.getWidth() / 2 - metrics.stringWidth("Reset") / 2,
                bar.getHeight() / 2 - metrics.getHeight() / 2 + metrics.getAscent(), font, Color.RED,
                "Reset");
        wline1 = new Text(application.getWidth()/2-metrics.stringWidth("!! Warning !!")/2,
                application.getHeight()/2+metrics.getAscent()-metrics.getHeight(), font,
                Color.RED, "!! Warning !!");
        wline2 = new Text(application.getWidth()/2-metrics.stringWidth("All configuration files will be deleted!")/2,
                application.getHeight()/2+metrics.getAscent(), font,
                Color.RED, "All configuration files will be deleted!");
        reset = new Button(Button.ButtonColor.BLUE, 0, offset, metrics.stringWidth("    Reset   "),
                back.getHeight(), font, "Reset");
        reset.setX(application.getWidth()-offset-reset.getWidth());
        confirmField = new Field(0, offset, metrics.stringWidth("   Type `confirm'   "), back.getHeight(),
                font, "", "Type `confirm'", application.os().keyboard);
        confirmField.setX(reset.getX()-confirmField.getWidth());
        reset.registerClickEvent(() -> {
            if(!confirmField.getText().equals("confirm")) return;
            deleteDirectory(new File(application.os().fs.root(), "sys"));
            new File(application.os().fs.root(), "init.flag").delete();
            application.os().turnOff();
        });
    }

    private void deleteDirectory(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        dir.delete();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, application.getWidth(), application.getHeight());
        bar.render(g);
        back.render(g);
        title.render(g);
        wline1.render(g);
        wline2.render(g);
        reset.render(g);
        confirmField.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        back.processInput(x, y, type);
        reset.processInput(x, y, type);
        confirmField.processInput(x, y, type);
    }

    @Override
    public void onResize() {
        bar.setWidth(application.getWidth());
        title.setX(application.getWidth()/2-metrics.stringWidth("Shutdown")/2);
        wline1.setX(application.getWidth()/2-metrics.stringWidth("!! Warning !!")/2);
        wline1.setY(application.getHeight()/2+metrics.getAscent()-metrics.getHeight());
        wline2.setX(application.getWidth()/2-metrics.stringWidth("All configuration files will be deleted!")/2);
        wline2.setY(application.getHeight()/2+metrics.getAscent());
        int offset = bar.getHeight() / 5;
        reset.setX(application.getWidth()-offset-reset.getWidth());
        confirmField.setX(reset.getX()-confirmField.getWidth());
    }
}
