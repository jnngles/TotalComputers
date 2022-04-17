package com.jnngl.applications;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.motion.ItemDropEvent;
import com.jnngl.totalcomputers.motion.MotionCaptureDesc;
import com.jnngl.totalcomputers.motion.SlotCaptureEvent;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ElementList;
import snes.SNES;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SNESEmulator extends WindowApplication {

    private SNES snes;
    private boolean running = false;
    private int upElapsed, downElapsed, leftElapsed, rightElapsed, aElapsed, bElapsed, xElapsed, yElapsed, lElapsed, rElapsed, selectElapsed, startElapsed;
    private Button startBt;
    private Button launchBt;
    private boolean resized = false;
    private ElementList roms;
    private int bX, bY, bH, bW;

    public static void main(String[] args) {
        ApplicationHandler.open(SNESEmulator.class, args[0]);
    }

    public SNESEmulator(TotalOS os, String path) {
        super(os, "SNES Emulator", os.screenWidth, os.screenHeight/3*2, path);
    }

    private void resizeImage(int w, int h) {
        if((float)getWidth()/getHeight() > (float)w/h) {
            bY = startBt.getHeight();
            bH = getHeight()-startBt.getHeight();
            float dif = (float)h/getHeight();
            bW = (int) (w/dif);
            bX = getWidth()/2-bW/2;
        }
        else {
            bX = 0;
            bW = getWidth();
            float dif = (float)w/getWidth();
            bH = (int) ((h)/dif);
            bY = getHeight()/2-bH/2+startBt.getHeight();
        }
        resized = true;
    }

    @Override
    protected void onStart() {
        Font uiFont = os.baseFont.deriveFont((float) os.screenHeight / 128 * 3);
        FontMetrics metrics = Utils.getFontMetrics(uiFont);
        launchBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), metrics.getHeight(), uiFont, "Launch");
        roms = new ElementList(0, launchBt.getHeight(), getWidth(), getHeight()-launchBt.getHeight(), uiFont);

        roms.addEntries(new File(applicationPath).list((dir, name) -> name.endsWith(".sfc")||name.endsWith(".smc")));

        addResizeEvent(new ResizeEvent() {
            private void handleResize(int width, int height) {
                if(!running) {
                    launchBt.setWidth(width);
                    roms.setWidth(width);
                    roms.setHeight(height-launchBt.getHeight());
                } else resizeImage(snes.getScreen().offscreen.getWidth(null), snes.getScreen().offscreen.getHeight(null));
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

        startBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), metrics.getHeight(),
                uiFont, "Start");

        launchBt.registerClickEvent(() -> {
            if(roms.getSelectedIndex() < 0) return;
            roms.setLocked(true);
            snes = new SNES();
            snes.gamename = applicationPath+"/"+roms.getSelectedElement();
            snes.initializeSNES();
            startBt.registerClickEvent(() -> {
                snes.ppu.keydown('T');
                startElapsed = 0;
            });

            new Thread(() -> {
                snes.beforeLoop();
                int updateTime = 1000/40;
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
                        if (xElapsed < 30)
                            xElapsed++;
                        if (yElapsed < 30)
                            yElapsed++;
                        if (lElapsed < 30)
                            lElapsed++;
                        if (rElapsed < 30)
                            rElapsed++;
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

                    snes.doLoopWork();

                    if(update) {
                        if (startElapsed == 5)
                            snes.ppu.keyup('T');
                        if (aElapsed == 20)
                            snes.ppu.keyup('A');
                        if (bElapsed == 20)
                            snes.ppu.keyup('B');
                        if (xElapsed == 3)
                            snes.ppu.keyup('X');
                        if (yElapsed == 3)
                            snes.ppu.keyup('Y');
                        if (lElapsed == 3)
                            snes.ppu.keyup('L');
                        if (rElapsed == 3)
                            snes.ppu.keyup('R');
                        if (selectElapsed == 5)
                            snes.ppu.keyup('S');
                        if (downElapsed == 3)
                            snes.ppu.keyup('d');
                        if (upElapsed == 3)
                            snes.ppu.keyup('u');
                        if (leftElapsed == 3)
                            snes.ppu.keyup('l');
                        if (rightElapsed == 3)
                            snes.ppu.keyup('r');
                    }
                }
                snes.pauselock.lock();
            }).start();
            running = true;
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

        Image output = snes.getScreen().render(null);
        if(bW == 0 || bH == 0) {
            resizeImage(output.getWidth(null), output.getHeight(null));
        }
        if(resized) {
            g.clearRect(0, 0, getWidth(), getHeight());
            resized = false;
        }
        g.drawImage(output, bX, bY, bW, bH, null);
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
                        snes.ppu.keydown('X');
                        xElapsed = 0;
                    })
                    .requiresSneakCapture(() -> {
                        snes.ppu.keydown('Y');
                        System.out.println("Y");
                        yElapsed = 0;
                    })
                    .requiresSlotCapture(new SlotCaptureEvent() {
                        @Override
                        public void slotLeft() {
                            snes.ppu.keydown('L');
                            lElapsed = 0;
                        }

                        @Override
                        public void slotRight() {
                            snes.ppu.keydown('R');
                            rElapsed = 0;
                        }
                    })
                    .requiresItemDropCapture(new ItemDropEvent() {
                        @Override
                        public void itemDrop() {
                            snes.ppu.keydown('S');
                            selectElapsed = 0;
                        }

                        @Override
                        public void stackDrop() {
                            snes.ppu.keydown('T');
                            startElapsed = 0;
                        }
                    })
                    .requiresMovementCapture((dx, dy) -> {
                        if(dx < 0) {
                            snes.ppu.keydown('l');
                            leftElapsed = 0;
                        }
                        else if(dx > 0) {
                            snes.ppu.keydown('r');
                            rightElapsed = 0;
                        }

                        if(dy < 0) {
                            snes.ppu.keydown('d');
                            downElapsed = 0;
                        }
                        else if(dy > 0) {
                            snes.ppu.keydown('u');
                            upElapsed = 0;
                        }
                    }), os);
        } else {
            if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                snes.ppu.keydown('A');
                aElapsed = 0;
            } else {
                snes.ppu.keydown('B');
                bElapsed = 0;
            }
        }
    }

}
