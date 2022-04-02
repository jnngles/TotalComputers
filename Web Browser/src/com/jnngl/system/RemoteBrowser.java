package com.jnngl.system;

import com.benjaminfaal.jcef.loader.JCefLoader;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefDisplayHandlerAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RemoteBrowser extends UnicastRemoteObject implements IRemoteBrowser {

    private static Map<CefBrowser, RemoteBrowser> map;
    private static CefApp cefApp;
    private static CefClient client;

    private CefBrowser browser;
    private JFrame frame;

    private String title = "Web Browser";
    private String url = "";

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Invalid arguments.");
            return;
        }

        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(2099);
        } catch (RemoteException ignored) {
            try {
                registry = LocateRegistry.getRegistry(null, 2099);
            } catch (RemoteException e) {
                System.err.println("Failed to create RMI registry or it is already created. ("+
                        e.getClass().getSimpleName()+")");
                return;
            }
        }

        try {
            registry.rebind(args[0].trim(), new RemoteBrowser());
        } catch (RemoteException e) {
            System.err.println("Failed to create server ("+e.getClass().getSimpleName()+")");
        }
    }

    public RemoteBrowser() throws RemoteException {
        super();
        frame = new JFrame("TotalComputers: Web Browser");
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public String getURL() throws RemoteException {
        try {
            return url;
        } finally {
            url = null;
        }
    }

    @Override
    public String getTitle() throws RemoteException {
        return title;
    }

    public void onStart(int width, int height) throws RemoteException {

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
                RemoteBrowser application = map.getOrDefault(browser, null);
                if(application == null) return;
                application.url = url;
            }

            @Override
            public void onTitleChange(CefBrowser browser, String title) {
                super.onTitleChange(browser, title);
                RemoteBrowser application = map.getOrDefault(browser, null);
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

    public void close() throws RemoteException {
        if (browser != null) browser.close(true);
        if (frame != null) frame.dispose();
        browser = null;
        frame = null;
        System.exit(0);
    }

    public byte[] render() throws RemoteException {
        if(browser != null && frame != null && client != null && cefApp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage buffer = browser.getLastRenderOutput();
            if(buffer == null) return null;
            try {
                ImageIO.write(buffer, "PNG", baos);
            } catch (IOException e) {
                System.err.println("Failed to compress buffered image.");
                return null;
            }
            return baos.toByteArray();
        }
        return null;
    }

    private char getCharFor(Keyboard.Keys key, String text) throws RemoteException {
        return switch (key) {
            case BACKSPACE -> '\b';
            case ENTER -> '\n';
            case TAB -> '\t';
            case OK, SHIFT, ALT, LANG, FUNCTION, CONTROL, HOME -> Character.MIN_VALUE;
            default -> text.charAt(0);
        };
    }

    public void keyTyped(String key, String text) throws RemoteException {
        for(KeyListener listener : browser.getUIComponent().getKeyListeners()) {
            char keyChar = getCharFor(Keyboard.Keys.valueOf(key), text);
            listener.keyTyped(new KeyEvent(browser.getUIComponent(), KeyEvent.KEY_TYPED,
                    System.currentTimeMillis(), 0, 0, keyChar));
        }

    }

    @Override
    public void setSize(int width, int height) throws RemoteException {
        frame.setSize(width, height);
    }

    @Override
    public void goBack() throws RemoteException {
        browser.goBack();
    }

    @Override
    public void goForward() throws RemoteException {
        browser.goForward();
    }

    @Override
    public void loadURL(String url) throws RemoteException {
        browser.loadURL(url);
    }

    @Override
    public boolean canGoBack() throws RemoteException {
        return browser.canGoBack();
    }

    @Override
    public boolean canGoForward() throws RemoteException {
        return browser.canGoForward();
    }

    public void processInput(int x, int y, boolean isLeft) throws RemoteException {
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
