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

import com.benjaminfaal.jcef.loader.JCefLoader;
import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.ui.Field;
import com.jnngl.totalcomputers.system.ui.Button;
import org.cef.*;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebBrowser extends WindowApplication {

    private static final Map<CefBrowser, WebBrowser> map;
    private static CefApp cefApp;
    private static CefClient client;

    static {
        map = new HashMap<>();
    }

    private CefBrowser browser;
    private JFrame frame;

    private Font font;

    private Field urlField;
    private Button go;

    private Button goBackward, goForward;

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

        goBackward = new Button(Button.ButtonColor.BLUE, 0, 0, metrics.stringWidth("<")*5,
                metrics.getHeight(), uiFont, "<");
        goForward = new Button(Button.ButtonColor.BLUE, goBackward.getWidth(), 0, goBackward.getWidth(),
                goBackward.getHeight(), uiFont, ">");

        goBackward.registerClickEvent(() -> browser.goBack());
        goForward.registerClickEvent(() -> browser.goForward());

        urlField = new Field(goForward.getWidth()*2, 0, getWidth()/5*4, goBackward.getHeight(), uiFont, "",
                "Enter URL here", os.keyboard);
        go = new Button(Button.ButtonColor.BLUE, urlField.getWidth(), 0, getWidth()-urlField.getWidth(),
                urlField.getHeight(), uiFont, "Go");

        go.registerClickEvent(() -> browser.loadURL(urlField.getText()));

        new Thread(() -> {
            Path jcefPath = Paths.get(System.getProperty("user.home")).resolve(".jcef");

            CefSettings settings = new CefSettings();
            settings.windowless_rendering_enabled = false;
            settings.cache_path = jcefPath.resolve("cache").toString();

            try {
                cefApp = JCefLoader.installAndLoad(jcefPath, settings);
                if(cefApp == null) cefApp = CefApp.getInstance(settings);
            } catch (Throwable e) {
                System.err.println("Failed to install JCEF.");
                return;
            }
            client = cefApp.createClient();

            client.addDisplayHandler(new CefDisplayHandlerAdapter() {

                @Override
                public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
                    super.onAddressChange(browser, frame, url);
                    WebBrowser application = map.getOrDefault(browser, null);
                    if(application == null) return;
                    application.urlField.setText(url);
                }

                @Override
                public void onTitleChange(CefBrowser browser, String title) {
                    super.onTitleChange(browser, title);
                    WebBrowser application = map.getOrDefault(browser, null);
                    if(application == null) return;
                    application.setName("Browser: "+title);
                }
            });

            browser = client.createBrowser("https://www.google.com/", true, false);
            map.put(browser, this);

            frame = new JFrame("TotalComputers: Web Browser");
            frame.setUndecorated(true);
            frame.getContentPane().add(browser.getUIComponent());
            frame.pack();
            frame.setSize(getWidth(), getHeight()-urlField.getHeight());
            frame.setVisible(true);

            addResizeEvent(new ResizeEvent() {
                private void resizeEvent(int width, int height) {
                    frame.setSize(width, height-urlField.getHeight());
                    urlField.setWidth(width/5*4);
                    go.setX(urlField.getWidth());
                    go.setWidth(getWidth()-urlField.getWidth());
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
        }).start();
    }

    @Override
    protected boolean onClose() {
        if (browser != null) browser.close(true);
        if (frame != null) frame.dispose();
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        BufferedImage buffer = null;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if(browser != null && frame != null && client != null && cefApp != null)
            buffer = browser.getLastRenderOutput();
        if(buffer == null) {
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Initializing CEF...", 10, 30);
            return;
        }
        g.drawImage(buffer, 0, urlField.getHeight(), getWidth(), getHeight()-urlField.getHeight(), null);
        urlField.render(g);
        go.render(g);
        goBackward.setLocked(!browser.canGoBack());
        goForward.setLocked(!browser.canGoForward());
        goBackward.render(g);
        goForward.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(browser == null) return;
        urlField.processInput(x, y, type);
        go.processInput(x, y, type);
        goBackward.processInput(x, y, type);
        goForward.processInput(x, y, type);
        if(y > urlField.getHeight()) {
            if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                for (MouseListener listener : browser.getUIComponent().getMouseListeners()) {
                    listener.mousePressed(new MouseEvent(browser.getUIComponent(), MouseEvent.MOUSE_PRESSED,
                            System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, x, y - urlField.getHeight(),
                            1, false, MouseEvent.BUTTON1));
                    listener.mouseReleased(new MouseEvent(browser.getUIComponent(), MouseEvent.MOUSE_RELEASED,
                            System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, x, y - urlField.getHeight(),
                            1, false, MouseEvent.BUTTON1));
                }
            } else {
                os.keyboard.invokeKeyboard(new Keyboard.KeyboardListener() {
                    private String buffer = "";

                    private char getCharFor(Keyboard.Keys key, String text) {
                        return switch (key) {
                            case BACKSPACE -> '\b';
                            case ENTER -> '\n';
                            case TAB -> '\t';
                            case OK, SHIFT, ALT, LANG, FUNCTION, CONTROL, HOME -> Character.MIN_VALUE;
                            default -> text.charAt(0);
                        };
                    }

                    @Override
                    public String keyTyped(String text, Keyboard.Keys key, Keyboard keyboard) {
                        if(key == Keyboard.Keys.OK || key == Keyboard.Keys.ENTER) {
                            os.keyboard.closeKeyboard();
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

                        for(KeyListener listener : browser.getUIComponent().getKeyListeners()) {
                            char keyChar = getCharFor(key, text);
                            listener.keyTyped(new KeyEvent(browser.getUIComponent(), KeyEvent.KEY_TYPED,
                                    System.currentTimeMillis(), 0, 0, keyChar));
                        }

                        return buffer;
                    }
                }, "");
            }
        }
    }
}
