package com.jnngl.system;

import java.awt.image.BufferedImage;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RemoteBrowser extends UnicastRemoteObject implements IRemoteBrowser {

    private IBrowser browser;
    private static boolean DEBUG = false;

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Invalid arguments.");
            return;
        }

        if(args[0].equalsIgnoreCase("debug")) DEBUG = true;

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
        try {
            try {
//            browser = new BrowserImpl_JCEF();
                throw new UnsupportedOperationException();
            } catch (Throwable e) {
                if (DEBUG)
                    System.out.println(" -> Failed to create AWT browser. Using Native Impl instead. " + e.getMessage());
                browser = new BrowserImpl_Native();
                if (DEBUG)
                    System.out.println(" -> Created " + browser.getClass().getName() + " instance");
                System.out.println("Using BrowserImpl_Native");
                if (DEBUG)
                    onStart(200, 100);
                return;
            }
//        System.out.println("Using BrowserImpl_JCEF");
        } catch (Throwable e) {
            System.err.println("Fatal error in SERVER process: "+e.getMessage());
            System.err.println("Instance of "+e.getClass().getName());
        }
    }

    @Override
    public String getURL() throws RemoteException {
        return browser.getURL();
    }

    @Override
    public String getTitle() throws RemoteException {
        return browser.getTitle();
    }

    public void onStart(int width, int height) throws RemoteException {
        System.out.println("Invoked onStart");
        browser.onStart(width, height);
    }

    public void close() throws RemoteException {
        browser.close();
        System.exit(0);
    }

    public SerializableImage render() throws RemoteException {
        BufferedImage buffer = browser.render();
        if(buffer != null)
            return SerializableImage.fromBufferedImage(buffer);
        return null;
    }

    public void keyTyped(String key, String text) throws RemoteException {
        browser.keyTyped(key, text);
    }

    @Override
    public void setSize(int width, int height) throws RemoteException {
        browser.setSize(width, height);
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
        browser.click(x, y);
    }

}
