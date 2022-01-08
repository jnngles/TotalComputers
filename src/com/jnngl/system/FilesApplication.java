package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.ElementList;

import java.awt.*;
import java.io.File;

public class FilesApplication extends WindowApplication {

    public FilesApplication(TotalOS os, String path) {
        super(os, "Files", (int)(os.screenWidth * 0.66f), (int)(os.screenHeight * 0.66f), path);
        setMinWidth((int)(os.screenWidth * 0.33f));
        setMinHeight((int)(os.screenHeight * 0.33f));
    }

    public static void main(String[] args) {
        ApplicationHandler.open(FilesApplication.class, args[0]);
    }

    private ElementList list, contents;
    private String directory = null;
    private int prevSelect = -1;

    @Override
    protected void onStart() {
        setIcon(os.fs.loadImage(applicationPath + "/icon.png"));
        final Font uiFont = os.baseFont.deriveFont((float) os.screenHeight / 128 * 3);
        contents = new ElementList(getWidth()/3, 0, getWidth()/3*2, getHeight(), uiFont);
        list = new ElementList(0, 0, getWidth()/3, getHeight(), uiFont, "Desktop", "Root");
        list.registerItemSelectEvent(() -> {
            contents.unsetSelected();
            String path = null;
            if(list.getSelectedElement().equals("Desktop")) {
                path = "/usr/Desktop";
            } else if(list.getSelectedElement().equals("Root")) {
                path = "/rootfs/"+os.name;
            }
            if(path == null) {
                directory = null;
            } else {
                File dir = os.fs.toFile(path);
                contents.clear();
                contents.addEntries("<--");
                contents.addEntries(dir.list());
                directory = path;
            }
            prevSelect = -1;
            renderCanvas();
        });
        contents.registerItemSelectEvent(() -> {
            if(prevSelect == contents.getSelectedIndex()) {
                File file = os.fs.toFile(directory);
                if(prevSelect == 0) {
                    String[] parts = directory.split("/");
                    directory = directory.replaceAll('/'+parts[parts.length-1]+'$', "");
                    if(directory.equals("")) directory = "/rootfs/"+os.name;
                    if(directory.contains("/rootfs") && !directory.contains(os.name)) directory = "/rootfs/"+os.name;
                    file = os.fs.toFile(directory);
                } else {
                    list.unsetSelected();
                    String dir = directory + "/" + contents.getSelectedElement();
                    File temp = os.fs.toFile(dir);
                    if(temp.isDirectory()) {
                        directory = dir;
                        file = temp;
                    }
                    else {
                        String[] parts = dir.split("\\.");
                        os.fs.launchFromApplication(os.fs.getAssociatedProgram(parts[parts.length-1]), dir);
                    }
                }
                contents.clear();
                contents.addEntries("<--");
                contents.addEntries(file.list());
                contents.unsetSelected();
            }
            prevSelect = contents.getSelectedIndex();
            renderCanvas();
        });

        list.registerScrollEvent(this::renderCanvas);
        contents.registerScrollEvent(this::renderCanvas);
        updateLayout();
        addResizeEvent(new ResizeEvent() {
            @Override
            public void onResize(int width, int height) {
                updateLayout();
                renderCanvas();
            }

            @Override
            public void onMaximize(int width, int height) {
                updateLayout();
                renderCanvas();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                updateLayout();
                renderCanvas();
            }
        });
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    protected void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        list.render(g);
        contents.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        list.processInput(x, y, type);
        contents.processInput(x, y, type);
    }

    public void updateLayout() {
        list.setWidth(getWidth()/3);
        list.setHeight(getHeight());
        contents.setX(getWidth()/3);
        contents.setWidth(getWidth()/3*2);
        contents.setHeight(getHeight());
    }
}
