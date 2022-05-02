package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.Wallpaper;
import com.jnngl.totalcomputers.system.states.Desktop;
import com.jnngl.totalcomputers.system.states.StateManager;
import com.jnngl.totalcomputers.system.ui.*;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Rectangle;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;

public class WallpaperState extends AppState {

    private final Rectangle bar;
    private final Button back, apply, resize;
    private final Text title, text;
    private final FontMetrics metrics;
    private final ElementList wallpapers;
    private final CheckBox dithering;

    static Wallpaper accessWallpaper(Preferences application)
            throws NoSuchFieldException, IllegalAccessException {
        Field smf = application.os().getClass().getDeclaredField("stateManager");
        boolean a = smf.canAccess(application.os());
        smf.setAccessible(true);
        StateManager sm = (StateManager) smf.get(application.os());
        smf.setAccessible(a);
        if (!(sm.getState() instanceof Desktop desktop)) return null;
        Field wpf = desktop.getClass().getDeclaredField("wallpaper");
        a = wpf.canAccess(desktop);
        wpf.setAccessible(true);
        Wallpaper wp = (Wallpaper) wpf.get(desktop);
        wpf.setAccessible(a);
        return wp;
    }

    public WallpaperState(Preferences preferences) {
        super(preferences);
        bar = new Rectangle(new Color(230, 230, 230), 0, 0,
                application.getWidth(), application.os().screenHeight/3*2/10);
        int offset = bar.getHeight()/5;
        Font font = application.os().baseFont.deriveFont((float)application.os().screenHeight/128*3);
        metrics = Utils.getFontMetrics(font);
        back = new Button(Button.ButtonColor.WHITE, offset, offset, metrics.stringWidth("   Back    "),
                bar.getHeight()-offset*2, font, "Back");
        back.registerClickEvent(() -> application.switchState(new BaseState(application)));
        apply = new Button(Button.ButtonColor.WHITE, back.getX()+back.getWidth()+offset, offset,
                metrics.stringWidth("   Apply   "), back.getHeight(), font, "Apply");
        apply.setLocked(true);
        title = new Text(application.getWidth()/2-metrics.stringWidth("Wallpaper")/2,
                bar.getHeight()/2-metrics.getHeight()/2+metrics.getAscent(), font, Color.BLACK, "Wallpaper");
        wallpapers = new ElementList(metrics.stringWidth("Select wallpaper: ")+offset*2, bar.getHeight()+offset,
                0,
                application.getHeight()-bar.getHeight()-offset*2, font, "Default");
        wallpapers.setWidth(application.getWidth()-wallpapers.getX()-offset);
        text = new Text(offset,bar.getHeight()+offset+metrics.getAscent(), font, Color.BLACK,"Select wallpaper: \n(/usr/Wallpaper/)");
        wallpapers.registerItemSelectEvent(() -> apply.setLocked(false));
        dithering = new CheckBox("Dithering", offset, bar.getHeight()+offset+metrics.getHeight()*2+offset, font,
                application.os());
        dithering.setValue(application.os().fs.isWallpaperDitheringEnabled());
        dithering.addClickEvent(() -> {
            if(dithering.getValue()) application.os().fs.enableWallpaperDithering();
            else application.os().fs.disableWallpaperDithering();
            try {
                Wallpaper wp = accessWallpaper(application);
                if (wp == null) return;
                wp.loadWallpaper();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        apply.registerClickEvent(() -> new Thread(() -> {
            try {
                Wallpaper wp = accessWallpaper(application);
                if(wp == null) return;
                if (wallpapers.getSelectedIndex() == 0) {
                    wp.changeWallpaper("/res/system/wallpaper.png");
                } else {
                    wp.changeWallpaper("/usr/Wallpapers/" + wallpapers.getSelectedElement());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start());
        resize = new Button(Button.ButtonColor.WHITE, 0, offset, metrics.stringWidth("  Resize method   "),
                back.getHeight(), font, "Resize method");
        resize.setX(application.getWidth()-resize.getWidth()-offset);
        resize.registerClickEvent(() -> application.switchState(new WallpaperResizeState(application)));
        File searchDir = new File(application.os().fs.root(), "usr/Wallpapers");
        if(!searchDir.exists()) return;
        searchDir.listFiles(pathname -> {
            if(pathname.isDirectory()) return false;
            if(pathname.getName().endsWith(".jpg") || pathname.getName().endsWith(".png")) {
                wallpapers.addEntry(pathname.getName());
                return true;
            }
            return false;
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
        wallpapers.render(g);
        text.render(g);
        dithering.render(g);
        resize.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        back.processInput(x, y, type);
        apply.processInput(x, y, type);
        wallpapers.processInput(x, y, type);
        dithering.processInput(x, y, type);
        resize.processInput(x, y, type);
    }

    @Override
    public void onResize() {
        bar.setWidth(application.getWidth());
        title.setX(application.getWidth()/2-metrics.stringWidth("Wallpaper")/2);
        int offset = bar.getHeight()/5;
        wallpapers.setWidth(application.getWidth()-wallpapers.getX()-offset);
        wallpapers.setHeight(application.getHeight()-bar.getHeight()-offset*2);
        resize.setX(application.getWidth()-resize.getWidth()-offset);
    }
}
