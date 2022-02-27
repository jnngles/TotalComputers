package com.jnngl.system;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SimpleFrameExample {

    static {
        CefApp.startup(new String[] {});
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {});
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setSize(100, 100);
        jf.setVisible(true);

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = false;
        CefApp cefApp = CefApp.getInstance(settings);
        CefClient client = cefApp.createClient();
        CefBrowser browser = client.createBrowser("http://www.google.com", false, false);
        Component browserUI = browser.getUIComponent();
        JFrame frame = new JFrame();
        frame.getContentPane().add(browserUI);
        frame.pack();
        frame.setSize(800, 600);
        //frame.setUndecorated(true);
        frame.setVisible(true);
    }
}