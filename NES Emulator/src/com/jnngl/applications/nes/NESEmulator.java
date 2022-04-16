/*
 * Copyright 2018, 2019, 2020, 2021 OneLoneCoder.com
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *  1. Redistributions or derivations of source code must retain the above copyright notice, this list of conditions and
 *     the following disclaimer.
 *
 *  2. Redistributions or derivative works in binary form must reproduce the above copyright notice. This list of
 *     conditions and the following disclaimer must be reproduced in the documentation and/or other materials provided with the distribution.
 *
 *  3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jnngl.applications.nes;

import com.grapeshot.halfnes.ui.HeadlessUI;
import com.grapeshot.halfnes.ui.PuppetController;
import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.motion.MotionCaptureDesc;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ElementList;

import java.awt.*;
import java.io.File;

/**
 * NES Emulator Application
 */
@RequiresAPI(apiLevel = 3)
public class NESEmulator extends WindowApplication {

    private HeadlessUI nes;
    private boolean running = false;
    private int upElapsed, downElapsed, leftElapsed, rightElapsed, aElapsed, bElapsed, selectElapsed, startElapsed;
    private Button startBt;
    private Button launchBt;
    private ElementList roms;
    private int bX, bY, bH, bW;

    public static void main(String[] args) {
        ApplicationHandler.open(NESEmulator.class, args[0]);
    }

    public NESEmulator(TotalOS os, String path) {
        super(os, "NES Emulator", os.screenWidth, os.screenHeight/3*2, path);
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
    }

    @Override
    protected void onStart() {
        System.out.println("[NES Emulator] Based on halfNES (https://github.com/andrew-hoffman/halfnes)");
        Font uiFont = os.baseFont.deriveFont((float) os.screenHeight / 128 * 3);
        FontMetrics metrics = Utils.getFontMetrics(uiFont);
        launchBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), metrics.getHeight(), uiFont, "Launch");
        roms = new ElementList(0, launchBt.getHeight(), getWidth(), getHeight()-launchBt.getHeight(), uiFont);

        roms.addEntries(new File(applicationPath).list((dir, name) -> name.endsWith(".nes")));

        addResizeEvent(new ResizeEvent() {
            private void handleResize(int width, int height) {
                if(!running) {
                    launchBt.setWidth(width);
                    roms.setWidth(width);
                    roms.setHeight(height-launchBt.getHeight());
                } else resizeImage(nes.getLastFrame().getWidth(), nes.getLastFrame().getHeight());
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
            nes = new HeadlessUI(applicationPath + "/" + roms.getSelectedElement(), true);
            startBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), metrics.getHeight(),
                    uiFont, "Start");
            startBt.registerClickEvent(() -> {
                nes.getController1().pressButton(PuppetController.Button.START);
                startElapsed = 0;
            });
            new Thread(() -> {
                while (running) {
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

                    nes.runFrame();

                    if (startElapsed == 5)
                        nes.getController1().releaseButton(PuppetController.Button.START);
                    if (aElapsed == 20)
                        nes.getController1().releaseButton(PuppetController.Button.A);
                    if (bElapsed == 20)
                        nes.getController1().releaseButton(PuppetController.Button.B);
                    if (selectElapsed == 5)
                        nes.getController1().releaseButton(PuppetController.Button.SELECT);
                    if (downElapsed == 3)
                        nes.getController1().releaseButton(PuppetController.Button.DOWN);
                    if (upElapsed == 3)
                        nes.getController1().releaseButton(PuppetController.Button.UP);
                    if (leftElapsed == 3)
                        nes.getController1().releaseButton(PuppetController.Button.LEFT);
                    if (rightElapsed == 3)
                        nes.getController1().releaseButton(PuppetController.Button.RIGHT);
                }

                nes.getNes().reset();
                nes.getNes().quit();
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

        g.clearRect(0, 0, getWidth(), getHeight());
        if(bW == 0 || bH == 0)
            resizeImage(nes.getLastFrame().getWidth(), nes.getLastFrame().getHeight());
        g.drawImage(nes.getLastFrame(), bX, bY, bW, bH, null);
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
                        nes.getController1().pressButton(PuppetController.Button.SELECT);
                        selectElapsed = 0;
                    })
                    .requiresMovementCapture((dx, dy) -> {
                        if(dx < 0) {
                            nes.getController1().pressButton(PuppetController.Button.LEFT);
                            leftElapsed = 0;
                        }
                        else if(dx > 0) {
                            nes.getController1().pressButton(PuppetController.Button.RIGHT);
                            rightElapsed = 0;
                        }

                        if(dy < 0) {
                            nes.getController1().pressButton(PuppetController.Button.DOWN);
                            downElapsed = 0;
                        }
                        else if(dy > 0) {
                            nes.getController1().pressButton(PuppetController.Button.UP);
                            upElapsed = 0;
                        }
                    }), os);
        } else {
            if(type == TotalComputers.InputInfo.InteractType.LEFT_CLICK) {
                nes.getController1().pressButton(PuppetController.Button.A);
                aElapsed = 0;
            } else {
                nes.getController1().pressButton(PuppetController.Button.B);
                bElapsed = 0;
            }
        }
    }
}
