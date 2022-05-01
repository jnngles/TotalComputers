package com.jnngl.system;

import org.cef.OS;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.ByteBuffer;

public class BrowserImpl_Native implements IBrowser {

    private ByteBuffer handle;
    private File libpath;

    private final int[] width, height;
    private BufferedImage buffer;

    private String lastURL;

    public BrowserImpl_Native() {
        width = new int[1];
        height = new int[1];
    }

    private native String N_getURL(ByteBuffer handle);
    private native String N_getTitle(ByteBuffer handle);
    private native ByteBuffer N_createCef(int width, int height, String client);
    private native void N_close(ByteBuffer handle);
    private native void N_render(ByteBuffer handle);
    private native void N_getSize(ByteBuffer handle, int[] width, int[] height);
    private native void N_getPixels(ByteBuffer handle, int[] pixels);
    private native void N_keyTyped(ByteBuffer handle, char c, String key);
    private native void N_setSize(ByteBuffer handle, int width, int height);
    private native void N_goBack(ByteBuffer handle);
    private native void N_goForward(ByteBuffer handle);
    private native void N_loadURL(ByteBuffer handle, String url);
    private native boolean N_canGoBack(ByteBuffer handle);
    private native boolean N_canGoForward(ByteBuffer handle);
    private native void N_click(ByteBuffer handle, int x, int y);

    @Override
    public String getURL() {
        if(handle == null) return "";
        String url = N_getURL(handle);
        try {
            if(url.equals(lastURL)) return null;
            return url;
        } finally {
            lastURL = url;
        }
    }

    @Override
    public String getTitle() {
        if(handle == null) return "";
        return N_getTitle(handle);
    }

    @Override
    public void onStart(int width, int height) {
        System.out.println("NATIVE -> onStart");
        this.width[0] = width;
        this.height[0] = height;
        System.out.println("Resolving natives");
        libpath = CEFNativeLoader.resolveLibpath();
        CEFNativeLoader.downloadAndLoad(libpath);
        System.out.println("Invoking N_createCef");
        System.out.println("CefClient="+libpath.getAbsolutePath()+"/cefclient"+(OS.isWindows()? ".exe" : ""));
        handle = N_createCef(width, height, libpath.getAbsolutePath()+"/cefclient"+
                (OS.isWindows()? ".exe" : ""));
        System.out.println("onStart -> done");
    }

    @Override
    public void close() {
        new Thread(() -> {
            if (handle != null)
                N_close(handle);
        }).start();
    }

    @Override
    public BufferedImage render() {
        if(handle == null) return null;
        N_render(handle);
        N_getSize(handle, width, height);
        if(buffer == null || width[0] != buffer.getWidth() || height[0] != buffer.getHeight())
            buffer = new BufferedImage(width[0], height[0], BufferedImage.TYPE_INT_RGB);
        N_getPixels(handle, ((DataBufferInt) (buffer.getRaster().getDataBuffer())).getData());
        return buffer;
    }

    @Override
    public void keyTyped(String key, String text) {
        if(handle == null) return;
        N_keyTyped(handle, text.charAt(0), key);
    }

    @Override
    public void setSize(int width, int height) {
        if(handle == null) return;
        N_setSize(handle, width, height);
    }

    @Override
    public void goBack() {
        if(handle == null) return;
        N_goBack(handle);
    }

    @Override
    public void goForward() {
        if(handle == null) return;
        N_goForward(handle);
    }

    @Override
    public void loadURL(String url) {
        if(handle == null) return;
        N_loadURL(handle, url);
    }

    @Override
    public boolean canGoBack() {
        if(handle == null) return false;
        return N_canGoBack(handle);
    }

    @Override
    public boolean canGoForward() {
        if(handle == null) return false;
        return N_canGoForward(handle);
    }

    @Override
    public void click(int x, int y) {
        if(handle == null) return;
        N_click(handle, x, y);
    }
}
