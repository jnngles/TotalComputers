package com.jnngl.system;

import com.benjaminfaal.jcef.loader.JCefLoader;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandlerAdapter;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BrowserImpl_JCEF implements IBrowser {

    private static Map<CefBrowser, BrowserImpl_JCEF> map;
    private static CefApp cefApp;
    private static CefClient client;

    private CefBrowser browser;
    private JFrame frame;

    private String title = "Web Browser";
    private String url = "";

    public BrowserImpl_JCEF() throws UnsupportedOperationException {
        try {
            frame = new JFrame("TotalComputers: Web Browser");
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch(Throwable e) {
            throw new UnsupportedOperationException("`JFrame' is not supported on this machine.", e);
        }
    }

    @Override
    public String getURL() {
        try {
            return url;
        } finally {
            url = null;
        }
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void onStart(int width, int height) {
        map = new HashMap<>();

        Path jcefPath = Paths.get(System.getProperty("user.home")).resolve(".jcef");

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = false;
        settings.cache_path = jcefPath.resolve("cache").toString();

        try {
            cefApp = JCefLoader.installAndLoad(jcefPath, settings);
            if(cefApp == null) cefApp = CefApp.getInstance(settings);
        } catch (Throwable e) {
            System.err.println("Failed to install JCEF. ("+e.getClass().getSimpleName()+")");
            System.err.println("-> "+e.getLocalizedMessage());
            return;
        }
        client = cefApp.createClient();

        client.addDisplayHandler(new CefDisplayHandlerAdapter() {
            @Override
            public void onAddressChange(CefBrowser browser, CefFrame frame, String url) {
                super.onAddressChange(browser, frame, url);
                BrowserImpl_JCEF application = map.getOrDefault(browser, null);
                if(application == null) return;
                application.url = url;
            }

            @Override
            public void onTitleChange(CefBrowser browser, String title) {
                super.onTitleChange(browser, title);
                BrowserImpl_JCEF application = map.getOrDefault(browser, null);
                if(application == null) return;
                application.title = "Browser: "+title;
            }
        });

        browser = client.createBrowser("https://www.google.com/", true, false);
        map.put(browser, this);

        frame.getContentPane().add(browser.getUIComponent());
        frame.pack();
        frame.setSize(width, height);
        frame.setVisible(true);
    }

    @Override
    public void close() {
        if (browser != null) browser.close(true);
        if (frame != null) frame.dispose();
        browser = null;
        frame = null;
        System.exit(0);
    }

    @Override
    public BufferedImage render() {
        if(browser != null && frame != null && client != null && cefApp != null) {
            return browser.getLastRenderOutput();
        }
        return null;
    }

    private char getCharFor(Keyboard.Keys key, String text){
        return switch (key) {
            case BACKSPACE -> '\b';
            case ENTER -> '\n';
            case TAB -> '\t';
            case OK, SHIFT, ALT, LANG, FUNCTION, CONTROL, HOME -> Character.MIN_VALUE;
            default -> text.charAt(0);
        };
    }

    @Override
    public void keyTyped(String key, String text) {
        for(KeyListener listener : browser.getUIComponent().getKeyListeners()) {
            char keyChar = getCharFor(Keyboard.Keys.valueOf(key), text);
            listener.keyTyped(new KeyEvent(browser.getUIComponent(), KeyEvent.KEY_TYPED,
                    System.currentTimeMillis(), 0, 0, keyChar));
        }
    }

    @Override
    public void setSize(int width, int height) {
        frame.setSize(width, height);
    }

    @Override
    public void goBack() {
        browser.goBack();
    }

    @Override
    public void goForward() {
        browser.goForward();
    }

    @Override
    public void loadURL(String url) {
        browser.loadURL(url);
    }

    @Override
    public boolean canGoBack() {
        return browser.canGoBack();
    }

    @Override
    public boolean canGoForward() {
        return browser.canGoForward();
    }

    @Override
    public void click(int x, int y) {
        if(browser == null) return;
        for (MouseListener listener : browser.getUIComponent().getMouseListeners()) {
            listener.mousePressed(new MouseEvent(browser.getUIComponent(), MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, x, y,
                    1, false, MouseEvent.BUTTON1));
            listener.mouseReleased(new MouseEvent(browser.getUIComponent(), MouseEvent.MOUSE_RELEASED,
                    System.currentTimeMillis(), InputEvent.BUTTON1_DOWN_MASK, x, y,
                    1, false, MouseEvent.BUTTON1));
        }
    }
}
