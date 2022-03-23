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

package com.jnngl.totalcomputers.system.states;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.TaskBar;
import com.jnngl.totalcomputers.system.desktop.Wallpaper;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.ui.ContextMenu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Desktop
 */
public class Desktop extends State {

    private static class DesktopIconInfo {
        int x;
        int y;
        int width;
        int height;
        File file;
    }

    public final List<WindowApplication> drawable;
    public final TaskBar taskbar;

    private final int titleBarHeight;
    private final int buttonSize;
    private final Color greenLight, yellowLight, redLight;
    private final int redX, buttonY, yellowX, greenX, titleX, titleY;

    private final Wallpaper wallpaper;
    private final Font uiFont;

    private BufferedImage desktop;

    private int selectedIconIndex = -1;
    private final List<DesktopIconInfo> icons;

    private final ContextMenu contextMenu;
    private ContextMenu fileMenu;

    public void updateDesktop() {
        Font font = os.baseFont.deriveFont((float) os.screenHeight/128*3);
        FontMetrics metrics = Utils.getFontMetrics(font);
        int offset = (int) (metrics.getHeight()*2);
        int x = offset;
        int y = offset;
        File[] files = new File(os.fs.root(), "usr/Desktop").listFiles();
        if(files == null) return;
        selectedIconIndex = -1;
        icons.clear();
        desktop = new BufferedImage(os.screenWidth, os.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = desktop.createGraphics();
        g.setColor(Color.BLACK);
        g.setFont(font);
        for(File file : files) {
            DesktopIconInfo info = new DesktopIconInfo();
            info.x = x-5;
            info.y = y-5;
            info.file = file;
            String name = file.getName();
            String croppedName = name;
            if(name.contains(".")) {
                String[] parts = name.split("\\.");
                String ext = parts[parts.length - 1];
                croppedName = name.substring(0, name.length() - 1 - ext.length());
            }
            BufferedImage icon = os.fs.getIconForFile(file);

            g.drawImage(icon, x, y, taskbar.getIconSize(), taskbar.getIconSize(), null);

            if(croppedName.length() >= 10) {
                croppedName = croppedName.substring(0, 9)+"...";
            }

            y += taskbar.getIconSize()+offset;
            g.drawString(croppedName, x+taskbar.getIconSize()/2-metrics.stringWidth(croppedName)/2, y-10-offset/3);
            y += 10;

            info.width = taskbar.getIconSize() + 10;
            info.height = y - info.y - 5;

            icons.add(info);

            if(y >= os.screenHeight-taskbar.getIconSize()-offset-10) {
                y = offset;
                x += taskbar.getIconSize()+offset+10;
            }
        }
        g.dispose();
    }

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
        taskbar = new TaskBar(os, uiFont);
        wallpaper = new Wallpaper(this);
        drawable = new ArrayList<>();
        icons = new ArrayList<>();
        contextMenu = new ContextMenu(new Rectangle(0, 0, os.screenWidth, os.screenHeight), uiFont);
        contextMenu.addEntry("Create file", false, () ->
                os.keyboard.invokeKeyboard(new Keyboard.KeyboardListener() {
                    private String buffer = "";

                    @Override
                    public String keyTyped(String text, Keyboard.Keys key, Keyboard keyboard) {
                        if(key == Keyboard.Keys.OK || key == Keyboard.Keys.ENTER) {
                            os.keyboard.closeKeyboard();
                            try {
                                File file = new File(os.fs.root()+"/usr/Desktop", buffer);
                                file.getParentFile().mkdirs();
                                file.createNewFile();
                            } catch (IOException e) {
                                System.err.println("Failed to create new file.");
                            }
                            updateDesktop();
                            return buffer;
                        }
                        if(key == Keyboard.Keys.BACKSPACE) {
                            if(buffer.length() > 0) {
                                buffer = buffer.substring(0, buffer.length()-1);
                            }
                        }
                        if(key.text != null) {
                            buffer += text;
                        }
                        return buffer;
                    }
                }, ""));
        contextMenu.addEntry("Create folder", false, () ->
                os.keyboard.invokeKeyboard(new Keyboard.KeyboardListener() {
                    private String buffer = "";

                    @Override
                    public String keyTyped(String text, Keyboard.Keys key, Keyboard keyboard) {
                        if(key == Keyboard.Keys.OK || key == Keyboard.Keys.ENTER) {
                            if(buffer.endsWith(".app")) return buffer;
                            new File(os.fs.root()+"/usr/Desktop", buffer).mkdirs();
                            os.keyboard.closeKeyboard();
                            updateDesktop();
                            return buffer;
                        }
                        if(key == Keyboard.Keys.BACKSPACE) {
                            if(buffer.length() > 0) {
                                buffer = buffer.substring(0, buffer.length()-1);
                            }
                        }
                        if(key.text != null) {
                            buffer += text;
                        }
                        return buffer;
                    }
                }, ""));
        contextMenu.addSeparator();
        contextMenu.addEntry("Refresh", false, this::updateDesktop);
        updateDesktop();
        ApplicationHandler.init(this);
    }

    /**
     * Update
     */
    @Override
    public void update() {
        for(WindowApplication application : drawable) application.updateApplication();
    }

    /**
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        wallpaper.render(g);
        if(selectedIconIndex >= 0) {
            DesktopIconInfo selected = icons.get(selectedIconIndex);
            g.setColor(new Color(255, 255, 255, 128));
            g.fillRoundRect(selected.x, selected.y, selected.width, selected.height, 4, 4);
        }
        g.drawImage(desktop, 0, 0, null);
        contextMenu.render(g);
        if(fileMenu != null) fileMenu.render(g);
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
        boolean handled = false;
        WindowApplication moveToTop = null;
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
                moveToTop = application;
                if(application.isMinimized()) continue;
                if(x >= application.getX() && x <= application.getX()+application.getWidth()+5 && y >= application.getY()-titleBarHeight && y <= application.getY()+application.getHeight()+5) {
                    handled = true;
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
                                break;
                            }
                            if(x >= application.getX() + greenX && x <= application.getX() + greenX + buttonSize) {
                                // Green Light
                                if(application.isMaximized()) application.unmaximize();
                                else application.maximize(titleBarHeight);
                                break;
                            }
                        }

                        // Move
                        wantToMove = application;
                        wtOffsetX = application.getX()-x;
                        wtOffsetY = application.getY()-y;
                        break;
                    } else if(x >= application.getX() && x <= application.getX()+application.getWidth() && y >= application.getY() && y <= application.getY()+application.getHeight()) { // App touch
                        application.processInput(x - application.getX(), y - application.getY(), type);
                        break;
                    } else { // Resize touch
                        wantToResize = application;
                        break;
                    }
                }
            }
        } else {
            selectedIconIndex = -1;
            return;
        }
        if(moveToTop != null) {
            drawable.remove(moveToTop);
            drawable.add(moveToTop);
        }
        if(!handled) {
            if(contextMenu.processInput(x, y, type)) {
                if(fileMenu != null)
                    fileMenu.close();
            }
            if(fileMenu != null) {
                if(fileMenu.processInput(x, y, type)) return;
            }
            for (DesktopIconInfo iconInfo : icons) {
                if (x >= iconInfo.x && y >= iconInfo.y && x <= iconInfo.x + iconInfo.width && y <= iconInfo.y + iconInfo.height) {
                    if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                        int pressed = icons.indexOf(iconInfo);
                        if (selectedIconIndex == pressed) {
                            os.fs.executeFile(iconInfo.file);
                            selectedIconIndex = -1;
                        } else selectedIconIndex = pressed;
                    } else {
                        selectedIconIndex = -1;
                        contextMenu.close();
                        fileMenu = new ContextMenu(uiFont);
                        fileMenu.addEntry("Open", false, () -> os.fs.executeFile(iconInfo.file));
                        fileMenu.addSeparator();
                        fileMenu.addEntry("Create link", false, () -> {
                            File target = new File(iconInfo.file.getAbsolutePath()+" (Link).lnk");
                            try {
                                target.createNewFile();
                                String root = new File(os.fs.root()).getAbsolutePath();
                                String localPath = iconInfo.file.getAbsolutePath()
                                        .replaceFirst(root.replace("\\", "\\\\"), "")
                                        .replace('\\', '/');
                                if(!localPath.startsWith("/")) localPath = "/"+localPath;
                                Files.writeString(target.toPath(), localPath);
                            } catch (IOException e) {
                                System.out.println("Failed to create link.");
                            }
                            updateDesktop();
                        });
                        fileMenu.addEntry("Rename", false, () ->
                        os.keyboard.invokeKeyboard(new Keyboard.KeyboardListener() {
                            private String buffer = iconInfo.file.getName();

                            @Override
                            public String keyTyped(String text, Keyboard.Keys key, Keyboard keyboard) {
                                if(key == Keyboard.Keys.OK || key == Keyboard.Keys.ENTER) {
                                    File dst = new File(iconInfo.file
                                            .getParentFile()
                                            .getAbsolutePath()
                                            +"/"+buffer);
                                    os.keyboard.closeKeyboard();
                                    dst.getParentFile().mkdirs();
                                    iconInfo.file.renameTo(dst);
                                    updateDesktop();
                                    return buffer;
                                }
                                if(key == Keyboard.Keys.BACKSPACE) {
                                    if(buffer.length() > 0) {
                                        buffer = buffer.substring(0, buffer.length()-1);
                                    }
                                }
                                if(key.text != null) {
                                    buffer += text;
                                }
                                return buffer;
                            }
                        }, iconInfo.file.getName()));
                        fileMenu.addEntry("Delete", false, () -> {
                            iconInfo.file.delete();
                            updateDesktop();
                        });
                        fileMenu.addSeparator();
                        fileMenu.addEntry("Add to taskbar", false, () -> {
                            String root = new File(os.fs.root()).getAbsolutePath();
                            String localPath = iconInfo.file.getAbsolutePath()
                                    .replaceFirst(root.replace("\\", "\\\\"), "")
                                    .replace('\\', '/');
                            if(!localPath.startsWith("/")) localPath = "/"+localPath;
                            ApplicationHandler.addTaskBarEntry(iconInfo.file.getName(), localPath,
                                    os.fs.getIconForFile(iconInfo.file));
                        });
                        fileMenu.show(x, y);
                    }
                    break;
                }
            }
        } else selectedIconIndex = -1;
    }

    public TotalOS getOS() {
        return os;
    }
}
