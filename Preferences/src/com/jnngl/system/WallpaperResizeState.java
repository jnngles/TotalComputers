package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.Wallpaper;
import com.jnngl.totalcomputers.system.ui.*;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Rectangle;

import java.awt.*;

public class WallpaperResizeState extends AppState {

    private final Rectangle bar;
    private final Button back, apply;
    private final Text title, description;
    private final FontMetrics metrics;
    private final RadioBoxSystem rbs;
    private final RadioBox[] rb;

    private void updateDescription(int i, RadioBox radioBox) {
        Wallpaper.ResizeType type = Wallpaper.ResizeType.valueOf
                (radioBox.getLabel().toUpperCase().replace(' ', '_'));
        description.setText("Description: "+switch (type) {
            case FIT_HEIGHT -> "The wallpaper is stretched so that the entire screen is filled in height. Aspect ratio is preserved.";
            case FIT_WIDTH -> "The wallpaper is stretched so that the entire screen is filled in width. Aspect ratio is preserved.";
            case NATIVE_SIZE -> "The wallpaper is centered, the aspect ratio and size of the original image are preserved.";
            case STRETCH -> "Wallpaper is stretched to fill the screen.";
            case AUTO_FULL -> "The aspect ratio is preserved and the wallpaper is stretched so that nothing is cut off.";
            case AUTO_CROP -> "Wallpaper is stretched to fill the screen, but the aspect ratio is preserved.";
            default -> "No description";
        });
    }

    private void updateDescription() {
        updateDescription(rbs.getSelectedIndex(), rbs.getSelected());
    }

    public WallpaperResizeState(Preferences preferences) {
        super(preferences);
        bar = new com.jnngl.totalcomputers.system.ui.Rectangle(new Color(230, 230, 230), 0, 0,
                application.getWidth(), application.os().screenHeight / 3 * 2 / 10);
        int offset = bar.getHeight() / 5;
        Font font = application.os().baseFont.deriveFont((float) application.os().screenHeight / 128 * 3);
        metrics = Utils.getFontMetrics(font);
        back = new com.jnngl.totalcomputers.system.ui.Button(com.jnngl.totalcomputers.system.ui.Button.ButtonColor.WHITE
                , offset, offset, metrics.stringWidth("   Back    "),
                bar.getHeight() - offset * 2, font, "Back");
        back.registerClickEvent(() -> application.switchState(new WallpaperState(application)));
        apply = new com.jnngl.totalcomputers.system.ui.Button(Button.ButtonColor.WHITE,
                back.getX() + back.getWidth() + offset, offset,
                metrics.stringWidth("   Apply   "), back.getHeight(), font, "Apply");
        apply.setLocked(true);
        title = new Text(application.getWidth() / 2 - metrics.stringWidth("Wallpaper Scaling") / 2,
                bar.getHeight() / 2 - metrics.getHeight() / 2 + metrics.getAscent(), font, Color.BLACK,
                "Wallpaper Scaling");
        description = new Text(offset, bar.getHeight() + offset + metrics.getAscent()
                , application.getWidth() / 2 - offset * 2,
                application.getHeight() - bar.getHeight() - offset * 2 - metrics.getAscent(), font, Color.BLACK,
                "Description: No description");

        rb = new RadioBox[Wallpaper.ResizeType.values().length];
        int y = bar.getHeight() + offset;
        Wallpaper wp = null;
        try {
            wp = WallpaperState.accessWallpaper(application);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < rb.length; i++) {
            String name = Wallpaper.ResizeType.values()[i].name();
            rb[i] = new RadioBox(name.charAt(0) + name.substring(1).toLowerCase().replace('_', ' '),
                    application.getWidth() / 2, y, font, application.os());
            y += metrics.getHeight();
            if (wp == null)
                rb[i].setLocked(true);
        }
        rbs = new RadioBoxSystem(-1, rb);
        if (wp == null) return;
        for (RadioBox r : rb) {
            if (r.getLabel().toUpperCase().replace(' ', '_').equals(wp.getResizeType().name())) {
                rbs.setSelected(r);
                break;
            }
        }
        if (rbs.getSelectedIndex() == -1) {
            for (RadioBox r : rb)
                r.setLocked(true);
        }
        rbs.setListener(this::updateDescription);
        updateDescription();
        apply.setLocked(false);
        Wallpaper finalWp = wp;
        apply.registerClickEvent(() -> {
            finalWp.setResizeType(Wallpaper.ResizeType.valueOf
                    (rbs.getSelected().getLabel().toUpperCase().replace(' ', '_')));
        });
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, application.getWidth(), application.getHeight());
        bar.render(g);
        back.render(g);
        apply.render(g);
        title.render(g);
        rbs.render(g);
        description.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        back.processInput(x, y, type);
        apply.processInput(x, y, type);
        rbs.processInput(x, y, type);
    }

    @Override
    public void onResize() {
        bar.setWidth(application.getWidth());
        title.setX(application.getWidth()/2-metrics.stringWidth("Wallpaper Scaling")/2);
        int offset = bar.getHeight()/5;
        description.setWidth(application.getWidth()/2-offset*2);
        description.setHeight(application.getHeight()-bar.getHeight()-offset*2-metrics.getAscent());
        for(RadioBox r : rb)
            r.setX(application.getWidth()/2);
    }
}
