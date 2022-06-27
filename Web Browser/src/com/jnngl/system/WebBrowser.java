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
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Field;
import org.cef.OS;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebBrowser extends WindowApplication {

    private IRemoteBrowser server;
    private boolean init = false;

    private Font font;
    private Field urlField;
    private com.jnngl.totalcomputers.system.ui.Button go;
    private com.jnngl.totalcomputers.system.ui.Button goBackward, goForward;

    public static void main(String[] args) {
        ApplicationHandler.open(WebBrowser.class, args[0]);
    }

    public WebBrowser(TotalOS os, String path) {
        super(os, "Web Browser", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        font = os.baseFont.deriveFont((float)os.screenHeight/128*7);
        Font uiFont = os.baseFont.deriveFont((float)os.screenHeight/128*4);
        FontMetrics metrics = Utils.getFontMetrics(uiFont);

        goBackward = new com.jnngl.totalcomputers.system.ui.Button(com.jnngl.totalcomputers.system.ui.Button.ButtonColor.BLUE, 0, 0, metrics.stringWidth("<")*5,
                metrics.getHeight(), uiFont, "<");
        goForward = new com.jnngl.totalcomputers.system.ui.Button(com.jnngl.totalcomputers.system.ui.Button.ButtonColor.BLUE, goBackward.getWidth(), 0, goBackward.getWidth(),
                goBackward.getHeight(), uiFont, ">");

        goBackward.registerClickEvent(() -> {
            try {
                server.goBack();
            } catch (RemoteException e) {
                System.err.println("Failed to call remote method. ("+e.getClass().getSimpleName()+")");
            }
        });
        goForward.registerClickEvent(() -> {
            try {
                server.goForward();
            } catch (RemoteException e) {
                System.err.println("Failed to call remote method. ("+e.getClass().getSimpleName()+")");
            }
        });

        urlField = new Field(goForward.getWidth()*2, 0, getWidth()/5*4, goBackward.getHeight(), uiFont, "",
                "Enter URL here", os.keyboard);
        go = new com.jnngl.totalcomputers.system.ui.Button(Button.ButtonColor.BLUE, urlField.getWidth(), 0, getWidth()-urlField.getWidth(),
                urlField.getHeight(), uiFont, "Go");

        go.registerClickEvent(() -> {
            try {
                server.loadURL(urlField.getText());
            } catch (RemoteException e) {
                System.err.println("Failed to call remote method. ("+e.getClass().getSimpleName()+")");
            }
        });

        new Thread(() -> {
            List<String> cmd = new ArrayList<>();
            final String vmArgs = "--add-exports java.base/java.lang=ALL-UNNAMED " +
                    "--add-exports java.desktop/sun.awt=ALL-UNNAMED --add-exports java.desktop/sun.java2d=ALL-UNNAMED";
            if(OS.isWindows()) {
                cmd.add(System.getProperty("java.home")+File.separator+"bin"+
                        File.separator+"java.exe");
            } else {
                String java = System.getProperty("java.home")+File.separator+"bin"+File.separator+"java";
                cmd.add(java);
            }
            cmd.add("-jar");
            cmd.addAll(Arrays.asList(vmArgs.split(" ")));
            cmd.add(applicationPath+File.separator+
                    "application.jar");
            String target = "browser_session_"+Thread.currentThread().getId();
            cmd.add(target);
            Process process = null;
            try {
                ProcessBuilder pb = new ProcessBuilder(cmd.toArray(new String[0]));
                pb.inheritIO();
                process = pb.start();
            } catch (IOException e) {
                System.err.println("Failed to spawn subprocess. ");
                System.err.println(" -> "+e.getMessage());
            }
            long startTime = System.currentTimeMillis();
            while(true) {
                try {
                    long current = System.currentTimeMillis();
                    if(current-startTime > 15000) {
                        System.err.println("["+target+"] Remote session not found. Timed out.");
                        break;
                    }
                    server = (IRemoteBrowser) LocateRegistry.getRegistry(null, 2099).lookup(target);
                    System.out.println("Successfully found browser process -> PID: "+process.pid()+"");
                    server.onStart(getWidth(), getHeight() - urlField.getHeight());
                    init = true;
                    System.out.println("Successfully bound new session to '" + target + "' [localhost:2099]");

                    addResizeEvent(new ResizeEvent() {
                        private void resizeEvent(int width, int height) {
                            try {
                                server.setSize(width, height - urlField.getHeight());
                            } catch (RemoteException e) {
                                System.err.println("Failed to call remote method. (" + e.getClass().getSimpleName() + ")");
                            }
                            urlField.setWidth(width / 5 * 4);
                            go.setX(urlField.getWidth());
                            go.setWidth(getWidth() - urlField.getWidth());
                        }

                        @Override
                        public void onResize(int width, int height) {
                            resizeEvent(width, height);
                        }

                        @Override
                        public void onMaximize(int width, int height) {
                            resizeEvent(width, height);
                        }

                        @Override
                        public void onUnmaximize(int width, int height) {
                            resizeEvent(width, height);
                        }
                    });

                    break;
                } catch (RemoteException | NotBoundException e) {
                    if(e instanceof NotBoundException) continue;
//                    System.err.println("(WebBrowser::onStart) -> Failed to create/access remote object. (" +
//                            e.getClass().getSimpleName() + ")");
//                    System.err.println(" -> " + e.getMessage());
//                    break;
                }
            }
        }).start();
    }

    @Override
    protected boolean onClose() {
        init = false;
        if(server == null) return true;
        try {
            server.close();
        } catch (RemoteException e) {
            System.err.println("Failed to call remote method. ("+e.getClass().getSimpleName()+")");
        }
        server = null;
        return true;
    }

    @Override
    protected void update() {
        if(!init) return;
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        if(!init) return;
        SerializableImage img;
        try {
            img = server.render();
        } catch (RemoteException e) {
            return;
        }
        if(img == null) {
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("Initializing CEF (It might take some time)", 10, 100);
            return;
        }
        try {
            String url = server.getURL();
            if(url != null) urlField.setText(url);
            setName(server.getTitle());
            BufferedImage screen = img.toBufferedImage();
            g.drawImage(screen, 0, urlField.getHeight(), getWidth(), getHeight() - urlField.getHeight(),
                    null);
            urlField.render(g);
            go.render(g);
            goBackward.setLocked(!server.canGoBack());
            goForward.setLocked(!server.canGoForward());
            goBackward.render(g);
            goForward.render(g);
        } catch (IOException e) {
            System.err.println("Failed to read buffered image.");
        }
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!init) return;
        urlField.processInput(x, y, type);
        go.processInput(x, y, type);
        goBackward.processInput(x, y, type);
        goForward.processInput(x, y, type);
        try {
            if (y > urlField.getHeight()) {
                if (type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                    server.processInput(x, y - urlField.getHeight(), true);
                } else {
                    os.keyboard.invokeKeyboard(new Keyboard.KeyboardListener() {
                        private String buffer = "";

                        @Override
                        public String keyTyped(String text, Keyboard.Keys key, Keyboard keyboard) {
                            if (key == Keyboard.Keys.OK || key == Keyboard.Keys.ENTER) {
                                os.keyboard.closeKeyboard();
                                return buffer;
                            }
                            if (key == Keyboard.Keys.BACKSPACE) {
                                if (buffer.length() > 0) {
                                    buffer = buffer.substring(0, buffer.length() - 1);
                                }
                            }
                            if (key.text != null) {
                                buffer += text;
                            }

                            try {
                                server.keyTyped(key.name(), text);
                            } catch (RemoteException e) {
                                System.err.println("Failed to call remote method. ("+e.getClass().getSimpleName()+")");
                            }

                            return buffer;
                        }
                    }, "");
                }
            }
        } catch (RemoteException e) {
            System.err.println("Failed to call remote method. ("+e.getClass().getSimpleName()+")");
        }
    }

}
