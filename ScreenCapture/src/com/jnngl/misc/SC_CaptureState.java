package com.jnngl.misc;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.ui.Button;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class SC_CaptureState extends SC_AppState {

    private final int[] width = { 0 };
    private final int[] height = { 0 };

    private int bX, bY, bW, bH;
    private int lastW, lastH;
    private Button keyboard;
    private ByteBuffer hwnd;
    private BufferedImage buffer;

    public SC_CaptureState(ScreenCapture application) {
        super(application);
        Font font = application.os().baseFont.deriveFont((float) application.os().screenHeight / 128 * 3);
        keyboard = new Button(Button.ButtonColor.BLUE, 0, 0,
                application.getWidth(), Utils.getFontMetrics(font).getHeight(), font, "Open keyboard");
        keyboard.setY(application.getHeight()-keyboard.getHeight());
    }

    public void setHWND(ByteBuffer hwnd) {
        this.hwnd = hwnd;
    }

    private void updateBounds() {
        int height = application.getHeight()-keyboard.getHeight();
        if((float)application.getWidth()/height > (float)this.width[0]/this.height[0]) {
            bY = 0;
            bH = height;
            float dif = (float)this.height[0]/height;
            bW = (int) (this.width[0]/dif);
            bX = application.getWidth()/2-bW/2;
        }
        else {
            bX = 0;
            bW = application.getWidth();
            float dif = (float)this.width[0]/application.getWidth();
            bH = (int) ((this.height[0])/dif);
            bY = height/2-bH/2;
        }
        Graphics2D g = application.getCanvas().createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, application.getWidth(), application.getHeight());
        g.dispose();
    }

    @Override
    public void onResize() {
        keyboard.setY(application.getHeight()-keyboard.getHeight());
        keyboard.setWidth(application.getWidth());
        updateBounds();
    }

    @Override
    public void render(Graphics2D g) {
        int prevW = width[0];
        int prevH = height[0];
        ByteBuffer imgBuf;
        if(hwnd == null) imgBuf = application.win32screenScreenshot(width, height);
        else imgBuf = application.win32appScreenshot(hwnd, width, height);
        if(prevW != width[0] || prevH != height[0]) {
            buffer = new BufferedImage(width[0], height[0], BufferedImage.TYPE_INT_RGB);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, application.getWidth(), application.getHeight());
            updateBounds();
        }
        if(buffer != null) {
            for (int x = 0; x < width[0]; x++) {
                for (int y = 0; y < height[0]; y++) {
                    int idx = (y * width[0] + x);
                    if(imgBuf.limit() - idx < 3) continue;
                    int rgb = imgBuf.get(idx++) << 16 | imgBuf.get(idx++) << 8 | imgBuf.get(idx);
                    if(rgb != 0) System.out.println(rgb);
                    buffer.setRGB(x, y, rgb);
                }
            }
            g.drawImage(buffer, bX, bY, bW, bH, null);
        }
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        keyboard.processInput(x, y, type);
    }
}
