package com.jnngl.applications;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.motion.MotionCaptureDesc;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.ElementList;
import com.jnngl.totalcomputers.system.ui.Button;
import eu.rekawek.coffeegb.Gameboy;
import eu.rekawek.coffeegb.GameboyOptions;
import eu.rekawek.coffeegb.controller.ButtonListener;
import eu.rekawek.coffeegb.memory.cart.Cartridge;
import eu.rekawek.coffeegb.serial.SerialEndpoint;
import eu.rekawek.coffeegb.sound.SoundOutput;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@RequiresAPI(apiLevel = 3)
public class GBEmulator extends WindowApplication {

    private Gameboy gb;
    private boolean running = false;
    private int upElapsed, downElapsed, leftElapsed, rightElapsed, aElapsed, bElapsed, selectElapsed, startElapsed;
    private Button startBt;
    private Button launchBt;
    private ElementList roms;
    private HeadlessController controller;
    private HeadlessDisplay display;
    private int bX, bY, bH, bW;

    public static void main(String[] args) {
        ApplicationHandler.open(GBEmulator.class, args[0]);
    }

    public GBEmulator(TotalOS os, String path) {
        super(os, "GameBoy Color Emulator", os.screenWidth, os.screenHeight/3*2, path);
    }

    private void resizeImage() {
        if((float)getWidth()/getHeight() > (float) HeadlessDisplay.DISPLAY_WIDTH / HeadlessDisplay.DISPLAY_HEIGHT) {
            bY = startBt.getHeight();
            bH = getHeight()-startBt.getHeight();
            float dif = (float) HeadlessDisplay.DISPLAY_HEIGHT /getHeight();
            bW = (int) (HeadlessDisplay.DISPLAY_WIDTH /dif);
            bX = getWidth()/2-bW/2;
        }
        else {
            bX = 0;
            bW = getWidth();
            float dif = (float) HeadlessDisplay.DISPLAY_WIDTH /getWidth();
            bH = (int) ((HeadlessDisplay.DISPLAY_HEIGHT)/dif);
            bY = getHeight()/2-bH/2+startBt.getHeight();
        }
    }

    @Override
    protected void onStart() {
        System.out.println("[GBC Emulator] Based on Coffee GB by Tomek Rekawek (https://github.com/trekawek/coffee-gb)");
        Font uiFont = os.baseFont.deriveFont((float) os.screenHeight / 128 * 3);
        FontMetrics metrics = Utils.getFontMetrics(uiFont);
        launchBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), metrics.getHeight(), uiFont, "Launch");
        roms = new ElementList(0, launchBt.getHeight(), getWidth(), getHeight()-launchBt.getHeight(), uiFont);

        roms.addEntries(new File(applicationPath).list((dir, name) -> name.endsWith(".gbc")||name.endsWith(".gb")));

        addResizeEvent(new ResizeEvent() {
            private void handleResize(int width, int height) {
                if(!running) {
                    launchBt.setWidth(width);
                    roms.setWidth(width);
                    roms.setHeight(height-launchBt.getHeight());
                } else resizeImage();
            }

            @Override
            public void onResize(int width, int height) {
                handleResize(width, height);
            }

            @Override
            public void onMaximize(int width, int height) {
                handleResize(width, height);
            }

            @Override
            public void onUnmaximize(int width, int height) {
                handleResize(width, height);
            }
        });

        maximize(os.screenHeight/32);

        launchBt.registerClickEvent(() -> {
            if(roms.getSelectedIndex() < 0) return;
            roms.setLocked(true);
            running = true;
            startBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), metrics.getHeight(),
                    uiFont, "Start");
            GameboyOptions options = new GameboyOptions(new File(applicationPath+"/"+roms.getSelectedElement()));
            try {
                gb = new Gameboy(options, new Cartridge(options), display = new HeadlessDisplay(), controller = new HeadlessController(), SoundOutput.NULL_OUTPUT, SerialEndpoint.NULL_ENDPOINT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startBt.registerClickEvent(() -> {
                controller.keyDown(ButtonListener.Button.START);
                startElapsed = 0;
            });
            new Thread(() -> {
                gb.prepareLoop();
                int updateTime = 1000/50;
                long lastTime = System.currentTimeMillis();
                while (running) {
                    boolean update = false;
                    if(System.currentTimeMillis()-lastTime > updateTime) {
                        update = true;
                        lastTime = System.currentTimeMillis();
                    }

                    if(update) {
                        if (aElapsed < 30)
                            aElapsed++;
                        if (bElapsed < 30)
                            bElapsed++;
                        if (selectElapsed <= 30)
                            selectElapsed++;
                        if (downElapsed <= 30)
                            downElapsed++;
                        if (leftElapsed <= 30)
                            leftElapsed++;
                        if (rightElapsed <= 30)
                            rightElapsed++;
                        if (upElapsed <= 30)
                            upElapsed++;
                        if (startElapsed <= 30)
                            startElapsed++;
                    }

                    gb.doLoopWork();

                    if(update) {
                        if (startElapsed == 5)
                            controller.keyUp(ButtonListener.Button.START);
                        if (aElapsed == 20)
                            controller.keyUp(ButtonListener.Button.A);
                        if (bElapsed == 20)
                            controller.keyUp(ButtonListener.Button.B);
                        if (selectElapsed == 5)
                            controller.keyUp(ButtonListener.Button.SELECT);
                        if (downElapsed == 3)
                            controller.keyUp(ButtonListener.Button.DOWN);
                        if (upElapsed == 3)
                            controller.keyUp(ButtonListener.Button.UP);
                        if (leftElapsed == 3)
                            controller.keyUp(ButtonListener.Button.LEFT);
                        if (rightElapsed == 3)
                            controller.keyUp(ButtonListener.Button.RIGHT);
                    }
                }

                gb.stop();
            }).start();
        });
    }

    @Override
    protected boolean onClose() {
        running = false;
        os.motionCapture.forceStopCapture(os);
        return true;
    }

    @Override
    public void update() {
        renderCanvas();
    }

    @Override
    public void render(Graphics2D g) {
        if(!running) {
            roms.render(g);
            launchBt.render(g);
            return;
        }

        if(display == null || controller == null || gb == null) return;
        g.clearRect(0, 0, getWidth(), getHeight());
        if(bW == 0 || bH == 0)
            resizeImage();
        g.drawImage(display.render(), bX, bY, bW, bH, null);
        startBt.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!running) {
            roms.processInput(x, y, type);
            launchBt.processInput(x, y, type);
            return;
        }
        startBt.processInput(x, y, type);
        if(y <= startBt.getHeight()) return;
        if(!os.motionCapture.isCapturing(os)) {
            os.motionCapture.startCapture(MotionCaptureDesc.create()
                    .requiresJumpCapture(() -> {
                        controller.keyDown(ButtonListener.Button.SELECT);
                        selectElapsed = 0;
                    })
                    .requiresMovementCapture((dx, dy) -> {
                        if(dx < 0) {
                            controller.keyDown(ButtonListener.Button.LEFT);
                            leftElapsed = 0;
                        }
                        else if(dx > 0) {
                            controller.keyDown(ButtonListener.Button.RIGHT);
                            rightElapsed = 0;
                        }

                        if(dy < 0) {
                            controller.keyDown(ButtonListener.Button.DOWN);
                            downElapsed = 0;
                        }
                        else if(dy > 0) {
                            controller.keyDown(ButtonListener.Button.UP);
                            upElapsed = 0;
                        }
                    }), os);
        } else {
            if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                controller.keyDown(ButtonListener.Button.A);
                aElapsed = 0;
            } else {
                controller.keyDown(ButtonListener.Button.B);
                bElapsed = 0;
            }
        }
    }
}