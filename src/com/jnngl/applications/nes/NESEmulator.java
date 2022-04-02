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

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.Button;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * NES Emulator Application
 */
public class NESEmulator extends WindowApplication {

    public static void main(String[] args) {
        ApplicationHandler.open(NESEmulator.class, args[0]);
    }

    private Bus nes;
    private Map<Integer, String> mapAsm;
    private Cartridge cart;

    private boolean bEmulationRun = false;
    private float fResidualTime = 0.0f;

    private Font font;
    private FontMetrics metrics;

    private short nSelectedPalette;

    public void drawRam(Graphics2D g, int x, int y, int nAddr, int nRows, int nColumns) {
        int nRamX = x, nRamY = y;
        for(int row = 0; row < nRows; row++) {
            String sOffset = "$" + CPU6502.hex(nAddr, (short) 4) + ":";
            for(int col = 0; col < nColumns; col++) {
                sOffset += " " + CPU6502.hex(nes.cpuRead(nAddr, true), (short) 2);
                nAddr += 1;
            }
            g.setColor(Color.WHITE);
            g.drawString(sOffset, nRamX, nRamY);
            nRamY += metrics.getHeight();
        }
    }

    public void drawCpu(Graphics2D g, int _x, int _y) {
        int x = _x, y = _y;

        g.setColor(Color.WHITE);
        g.drawString("STATUS: ", x, y);
        x += metrics.stringWidth("STATUS: ");

        g.setColor((nes.cpu.status & CPU6502.N) != 0? Color.GREEN : Color.RED);
        g.drawString("N", x, y);
        x += metrics.stringWidth("N ");

        g.setColor((nes.cpu.status & CPU6502.V) != 0? Color.GREEN : Color.RED);
        g.drawString("V", x, y);
        x += metrics.stringWidth("V ");

        g.setColor((nes.cpu.status & CPU6502.U) != 0? Color.GREEN : Color.RED);
        g.drawString("-", x, y);
        x += metrics.stringWidth("- ");

        g.setColor((nes.cpu.status & CPU6502.B) != 0? Color.GREEN : Color.RED);
        g.drawString("B", x, y);
        x += metrics.stringWidth("B ");

        g.setColor((nes.cpu.status & CPU6502.D) != 0? Color.GREEN : Color.RED);
        g.drawString("D", x, y);
        x += metrics.stringWidth("D ");

        g.setColor((nes.cpu.status & CPU6502.I) != 0? Color.GREEN : Color.RED);
        g.drawString("I", x, y);
        x += metrics.stringWidth("I ");

        g.setColor((nes.cpu.status & CPU6502.Z) != 0? Color.GREEN : Color.RED);
        g.drawString("Z", x, y);
        x += metrics.stringWidth("Z ");

        g.setColor((nes.cpu.status & CPU6502.C) != 0? Color.GREEN : Color.RED);
        g.drawString("C", x, y);

        g.setColor(Color.WHITE);

        x = _x;

        y += metrics.getHeight();
        g.drawString("PC: $"+CPU6502.hex(nes.cpu.pc, (short) 4), x, y);

        y += metrics.getHeight();
        g.drawString("A: $"+CPU6502.hex(nes.cpu.a, (short) 2) + " ["+nes.cpu.a+"]", x, y);

        y += metrics.getHeight();
        g.drawString("X: $"+CPU6502.hex(nes.cpu.x, (short) 2) + " ["+nes.cpu.x+"]", x, y);

        y += metrics.getHeight();
        g.drawString("Y: $"+CPU6502.hex(nes.cpu.y, (short) 2) + " ["+nes.cpu.y+"]", x, y);

        y += metrics.getHeight();
        g.drawString("Stack P: $"+CPU6502.hex(nes.cpu.stkp, (short) 4), x, y);

    }

    void drawCode(Graphics2D g, int x, int y, int nLines) {
        var it_a = mapAsm.entrySet().iterator();
        Map.Entry<Integer, String> current = null;
        while(it_a.hasNext()) {
            if((current = it_a.next()).getKey() == nes.cpu.pc)
                break;
        }

        int nLineY = metrics.getHeight() + y;
        if(it_a.hasNext()) {
            g.setColor(Color.CYAN);
            g.drawString(current.getValue(), x, nLineY);
            g.setColor(Color.WHITE);

            while(nLineY < (nLines * metrics.getHeight()) + y) {
                nLineY += metrics.getHeight();
                if(it_a.hasNext()) {
                    g.drawString(it_a.next().getValue(), x, nLineY);
                }
            }
        }

        it_a = mapAsm.entrySet().iterator();
        while(it_a.hasNext()) {
            if((it_a.next()).getKey() == nes.cpu.pc)
                break;
        }

    }

    private Button stepEmul, frameEmul, toggleEmul, reset, changePalette;

    public NESEmulator(TotalOS os, String path) {
        super(os, "CPU6502 Emulator", os.screenWidth, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        font = os.baseFont.deriveFont((float) os.screenHeight/256*5);
        metrics = Utils.getFontMetrics(font);

        // UI
        stepEmul = new Button(Button.ButtonColor.WHITE, getHeight()+4+7*metrics.stringWidth("FFF"), 2, metrics.stringWidth("<Step Emul.>"), metrics.getHeight(), font, "Step Emul.");
        stepEmul.setX(getWidth()-stepEmul.getWidth()-4);
        stepEmul.registerClickEvent(() -> {
            do {
                nes.clock();
            } while(!nes.cpu.complete());

            do {
                nes.clock();
            } while(nes.cpu.complete());
        });

        frameEmul = new Button(Button.ButtonColor.WHITE, getHeight()+4+7*metrics.stringWidth("FFF"), 2+(4+metrics.getHeight()), metrics.stringWidth("<Frame Emul.>"), metrics.getHeight(), font, "Frame Emul.");
        frameEmul.setX(getWidth()-frameEmul.getWidth()-4);
        frameEmul.registerClickEvent(() -> {
            do {
                nes.clock();
            } while(!nes.ppu.frame_complete);

            do {
                nes.clock();
            } while(!nes.cpu.complete());

            nes.ppu.frame_complete = false;
        });

        toggleEmul = new Button(Button.ButtonColor.WHITE, 0, 2+(4+metrics.getHeight())*2, metrics.stringWidth("<Start Emul.>"), metrics.getHeight(), font, bEmulationRun? "Stop Emul." : "Start Emul.");
        toggleEmul.setX(getWidth()-toggleEmul.getWidth()-4);
        toggleEmul.registerClickEvent(() -> {
                bEmulationRun = !bEmulationRun;
                toggleEmul.setText(bEmulationRun? "Stop Emul." : "Start Emul.");
                if(bEmulationRun) {
                    frameEmul.setLocked(true);
                    stepEmul.setLocked(true);
                } else {
                    frameEmul.setLocked(false);
                    stepEmul.setLocked(false);
                }
        });

        reset = new Button(Button.ButtonColor.WHITE, 0, 2+(4+metrics.getHeight())*3, metrics.stringWidth("<Reset>"), metrics.getHeight(), font, "Reset");
        reset.setX(getWidth()-reset.getWidth()-4);
        reset.registerClickEvent(() -> {
            nes.reset();
        });

        changePalette = new Button(Button.ButtonColor.WHITE, 0, 2+(4+metrics.getHeight())*4, metrics.stringWidth("<Palette>"), metrics.getHeight(), font, "Palette");
        changePalette.setX(getWidth()-changePalette.getWidth()-4);
        changePalette.registerClickEvent(() -> {
            nSelectedPalette++;
            nSelectedPalette &= 0x07;
        });

        // EMULATION
        nes = new Bus();

        cart = new Cartridge(applicationPath+"/nestest.nes");
        nes.insertCartridge(cart);

        mapAsm = nes.cpu.disassemble(0x0000, 0xFFFF);

        nes.reset();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    public void update() {
        if(bEmulationRun) {
            do { nes.clock(); } while (!nes.ppu.frame_complete);
            nes.ppu.frame_complete = false;
        }
        renderCanvas();
    }

    @Override
    public void render(Graphics2D g) {
        g.setFont(font);
        g.setColor(Color.BLUE.darker());
        g.fillRect(0, 0, getWidth(), getHeight());

        drawCpu(g, getHeight()+4, metrics.getHeight());
        drawCode(g, getHeight()+4, 7*metrics.getHeight()+2, 26);

        BufferedImage palette = nes.ppu.GetPatternTable((short) 1, nSelectedPalette);
        g.drawImage(palette, getWidth()-palette.getWidth()-2, getHeight()-palette.getHeight()-2, palette.getWidth(), palette.getHeight(), null);
        palette = nes.ppu.GetPatternTable((short) 0, nSelectedPalette);
        g.drawImage(palette, getWidth()-palette.getWidth()*2-4, getHeight()-palette.getHeight()-2, palette.getWidth(), palette.getHeight(), null);

        g.drawImage(nes.ppu.GetScreen(), 0, 0, getHeight(), (int)(getHeight()*((float)nes.ppu.GetScreen().getHeight()/(float)nes.ppu.GetScreen().getWidth())), null);

        stepEmul.render(g);
        frameEmul.render(g);
        reset.render(g);
        toggleEmul.render(g);
        changePalette.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        stepEmul.processInput(x, y, type);
        frameEmul.processInput(x, y, type);
        toggleEmul.processInput(x, y, type);
        reset.processInput(x, y, type);
        changePalette.processInput(x, y, type);
    }
}
