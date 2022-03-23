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

package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Web;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Field;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AppStore extends WindowApplication {

    private static final class ApplicationData {

        private BufferedImage image;
        private final String name;
        private final String author;
        private final String uuid;
        private final String downloadURL;

        private ApplicationData(BufferedImage image, String name, String author, String uuid, String downloadURL) {
            this.image = image;
            this.name = name;
            this.author = author;
            this.uuid = uuid;
            this.downloadURL = downloadURL;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (ApplicationData) obj;
            return Objects.equals(this.image, that.image) &&
                    Objects.equals(this.name, that.name) &&
                    Objects.equals(this.author, that.author) &&
                    Objects.equals(this.uuid, that.uuid) &&
                    Objects.equals(this.downloadURL, that.downloadURL);
        }

        @Override
        public String toString() {
            return "ApplicationData[" +
                    "image=" + image + ", " +
                    "name=" + name + ", " +
                    "author=" + author + ", " +
                    "uuid=" + uuid + ", " +
                    "downloadURL=" + downloadURL + ']';
        }
    }

    private List<ApplicationData> applications;
    private List<String> installed, installing;
    private List<Button> buttons;

    private Font uiFont;

    private Button searchBt, up, down;
    private Field search;

    private int y;
    private int w,h;

    private File file;

    private String searchMask = "";

    public AppStore(TotalOS os, String path) {
        super(os, "App Store", os.screenWidth/3*2, os.screenHeight/3*2, path);
        setMinWidth((int)(os.screenWidth * 0.6f));
        setMinHeight((int)(os.screenHeight * 0.6f));
    }

    private void scaleUI() {
        search.setWidth(getWidth()-(int) (os.screenWidth * 0.1f));
        searchBt.setX(search.getWidth());
        up.setX(getWidth()-search.getHeight());
        down.setX(up.getX());
        down.setY(getHeight()-search.getHeight());
    }

    @Override
    protected void onStart() {
        setIcon(os.fs.loadImage(applicationPath + "/icon.png"));
        applications = new ArrayList<>();
        installed = new ArrayList<>();
        installing = new ArrayList<>();
        buttons = new ArrayList<>();
        w = (int) (os.screenWidth * 0.1f); h = (int) (os.screenHeight * 0.025f);
        uiFont  = os.baseFont.deriveFont((float) os.screenHeight/128*3);
        search = new Field(0, 0, getWidth()-(int) (os.screenWidth * 0.1f), (int)(h*1.5f), uiFont, "", os.localization.search(), os.keyboard);
        searchBt = new Button(Button.ButtonColor.BLUE, search.getWidth(), 0, (int) (os.screenWidth * 0.1f), search.getHeight(), uiFont, os.localization.search());
        up = new Button(Button.ButtonColor.BLUE, getWidth()-search.getHeight(), search.getHeight(), search.getHeight(), search.getHeight(), uiFont, "^");
        down = new Button(Button.ButtonColor.BLUE, getWidth()-search.getHeight(), getHeight()-search.getHeight(), search.getHeight(), search.getHeight(), uiFont, "v");

        up.registerClickEvent(() -> { y -= getHeight()/7*1.25; if(y < 0) y=0; renderCanvas(); });
        down.registerClickEvent(() -> { y += getHeight()/7*1.25; if(y < 0) y=0; renderCanvas(); });
        searchBt.registerClickEvent(() -> {
            searchMask = search.getText();
            renderCanvas();
        });

        search.setKeyTypedEvent((String character, Keyboard.Keys key, Keyboard keyboard) -> {
            renderCanvas();
            if(key == Keyboard.Keys.ENTER) keyboard.closeKeyboard();
            if(key == Keyboard.Keys.BACKSPACE)
                if(search.getText().length() > 0)
                    search.setText(search.getText().substring(0, search.getText().length()-1));
            if(key.text != null) search.setText(search.getText()+character);
            return search.getText();
        });
        file = new File(os.fs.root()+"/sys/installed");
        try {
            if(!file.exists()) {
                file.createNewFile();
                Files.writeString(file.toPath(), "0");
            }
            installed.addAll(Arrays.asList(Files.readString(file.toPath()).split("\n")));
        } catch(IOException e) {
            System.err.println("Failed to access file system.");
        }
        new Thread(this::updateList).start();
        addResizeEvent(new ResizeEvent() {
            @Override
            public void onResize(int width, int height) {
                scaleUI();
                renderCanvas();
            }

            @Override
            public void onMaximize(int width, int height) {
                scaleUI();
                renderCanvas();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                scaleUI();
                renderCanvas();
            }
        });
    }

    private void updateList() {
        applications.clear();
        String data = Web.readFromURL("https://raw.githubusercontent.com/JNNGL/TotalComputers/appbase/index");
        int current = -1;
        if(data == null) return;
        for(String line : data.split("\n")) {
            current++;
            final int index = current;
            applications.add(new ApplicationData(os.fs.getResourceImage("default-icon"), Web.readFromURL(line+"/name"), Web.readFromURL(line+"/author"), Web.readFromURL(line+"/uuid"), line+"/application.zip"));
            new Thread(() -> {
                applications.get(index).image = Web.readImageFromURL(line+"/icon.png");
                renderCanvas();
            }).start();
        }
        renderCanvas();
    }

    @Override
    protected boolean onClose() {
        return installing.isEmpty();
    }

    @Override
    public void update() {

    }

    private boolean deleteDirectory(File dir) {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return dir.delete();
    }

    @Override
    protected void render(Graphics2D g) {
        Font font = os.baseFont.deriveFont((float)getHeight()/25);
        g.setFont(font);
        FontMetrics fontMetrics = g.getFontMetrics();
        int h = getHeight()/7;
        boolean a = false;
        boolean b = false;
        int y = search.getHeight()-this.y;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        buttons.clear();
        for(ApplicationData app : applications) {
            if(searchMask.startsWith("author:")) {
                if(!app.author.equalsIgnoreCase(searchMask.substring(7).trim()))
                    continue;
            }
            else if(!app.name.toLowerCase().contains(searchMask.toLowerCase())) continue;
            int x = a? getWidth()/2+4 : 4;
            if((a && b) || (!a && !b)) {
                g.setColor(new Color(235, 235, 235));
                g.fillRect(x-6, y, getWidth()/2, (int)((h)*1.25));
            }
            g.drawImage(app.image, x, y, h, h, null);
            g.setColor(Color.BLACK);
            g.drawString(app.name, x+h+4, y+getHeight()/14);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(app.author, x+h+4, y+getHeight()/14+fontMetrics.getAscent()+4);
            if(!app.uuid.equals("0")) {
                boolean i = installed.contains(app.uuid);
                Button button = new Button(i? Button.ButtonColor.WHITE : Button.ButtonColor.BLUE, x + getWidth() / 2 - w - 12, y + getHeight() / 14 - g.getFontMetrics(uiFont).getAscent() / 2, w, this.h, uiFont, i? os.localization.delete() : os.localization.download());
                if(installing.contains(app.uuid)) {
                    button.setLocked(true);
                    button.setText(os.localization.downloading());
                }
                button.render(g);
                button.registerClickEvent(i? () -> {
                    try {
                        Files.writeString(file.toPath(), Files.readString(file.toPath()).replace("\n"+app.uuid, ""));
                        installed.remove(app.uuid);
                        deleteDirectory(new File(os.fs.root()+"/usr/Applications/"+app.name+".app"));
                        renderCanvas();
                        new File(os.fs.root()+"/usr/Desktop", app.name+".lnk").delete();
                        ApplicationHandler.refreshDesktop();
                    } catch (IOException e) {
                        System.err.println("Failed to delete app.");
                    }
                } : () -> {
                    String basePath = os.fs.root()+"/usr/Applications/"+app.name+".app";
                    new File(basePath).mkdirs();
                    installing.add(app.uuid);
                    renderCanvas();
                    new Thread(() -> {
                        try {
                            if (Web.downloadFileFromURL(app.downloadURL, basePath + "/data.zip")) {
                                ZipInputStream zipIn = new ZipInputStream(new FileInputStream(basePath + "/data.zip"));
                                ZipEntry entry = zipIn.getNextEntry();

                                while (entry != null) {
                                    String filePath = basePath + "/" + entry.getName();
                                    new File(filePath).getParentFile().mkdirs();
                                    if (!entry.isDirectory()) {
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                                        byte[] bytesIn = new byte[1024];
                                        int read = 0;
                                        while ((read = zipIn.read(bytesIn)) != -1) {
                                            bos.write(bytesIn, 0, read);
                                        }
                                        bos.close();
                                    }
                                    zipIn.closeEntry();
                                    entry = zipIn.getNextEntry();
                                }
                                zipIn.close();

                                Files.writeString(file.toPath(), Files.readString(file.toPath()) + "\n" + app.uuid);
                                installed.add(app.uuid);
                                new File(basePath + "/data.zip").delete();
                                File dst = new File(os.fs.root()+"/usr/Desktop", app.name+".lnk");
                                dst.createNewFile();
                                Files.writeString(dst.toPath(), basePath);
                                ApplicationHandler.refreshDesktop();
                            }
                        } catch (IOException e) {
                            System.err.println("Failed to install app. (" + e.getClass().getSimpleName() + ")");
                            deleteDirectory(new File(basePath));
                        }
                        installing.remove(app.uuid);
                        renderCanvas();
                    }).start();
                });
                buttons.add(button);
            }
            if(a) {
                y += (h)*1.25;
                b = !b;
            }
            a = !a;
        }
        search.render(g);
        searchBt.render(g);
        up.render(g);
        down.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        down.processInput(x, y, type);
        up.processInput(x, y, type);
        searchBt.processInput(x, y, type);
        search.processInput(x, y, type);
        for(Button bt : buttons) {
            bt.processInput(x, y, type);
        }
    }

    public static void main(String[] args) {
        ApplicationHandler.open(AppStore.class, args[0]);
    }
}
