package com.jnngl.system;

import java.awt.image.BufferedImage;

public interface IBrowser {

    public String getURL();
    public String getTitle();

    public void onStart(int width, int height);
    public void close();
    public BufferedImage render();

    public void keyTyped(String key, String text);
    public void setSize(int width, int height);

    public void goBack();
    public void goForward();

    public void loadURL(String url);

    public boolean canGoBack();
    public boolean canGoForward();
    public void click(int x, int y);

}
