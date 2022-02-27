package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jogamp.nativewindow.NativeSurface;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefRequestContext;
import org.cef.callback.CefDragData;
import org.cef.callback.CefNativeAdapter;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.callback.CefStringVisitor;
import org.cef.handler.*;
import org.cef.network.CefRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

public class WebBrowser extends WindowApplication {

    private Component ui;

    static {
        CefApp.startup(new String[] {"--off-screen-rendering-enabled"});
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {});
    }

    public static void main(String[] args) {
        ApplicationHandler.open(WebBrowser.class, args[0]);
    }

    public WebBrowser(TotalOS os, String path) {
        super(os, "Web Browser", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = true;
        CefApp cefApp = CefApp.getInstance(settings);
        CefClient client = cefApp.createClient();
        CefBrowser browser = client.createBrowser("http://www.google.com", true, false);
        ui = browser.getUIComponent();
        ui.setSize(getWidth(), getHeight());
    }

    @Override
    protected boolean onClose() {
        CefApp.getInstance().dispose();
        return true;
    }

    @Override
    public void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        ui.paintAll(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
    }
}
