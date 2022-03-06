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

import com.jnngl.applications.nes.Cartridge;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Port of olcNES (https://github.com/OneLoneCoder/olcNES)
 * */
public class PPU2C02 {

    public PPU2C02() {
        status = new UNION_status();
        mask = new UNION_mask();
        control = new PPUCTRL();

        tblName = new short[2][1024];
        tblPalette = new short[32];
        tblPattern = new short[2][4096];

        palScreen = new Color[0x40];
        sprScreen = new BufferedImage(256, 240, BufferedImage.TYPE_INT_RGB);
        sprNameTable = new BufferedImage[] {
                new BufferedImage(256, 240, BufferedImage.TYPE_INT_RGB),
                new BufferedImage(256, 240, BufferedImage.TYPE_INT_RGB)
        };
        sprPatternTable = new BufferedImage[] {
                new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB),
                new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB)
        };

        palScreen[0x00] = new Color(84, 84, 84);
        palScreen[0x01] = new Color(0, 30, 116);
        palScreen[0x02] = new Color(8, 16, 144);
        palScreen[0x03] = new Color(48, 0, 136);
        palScreen[0x04] = new Color(68, 0, 100);
        palScreen[0x05] = new Color(92, 0, 48);
        palScreen[0x06] = new Color(84, 4, 0);
        palScreen[0x07] = new Color(60, 24, 0);
        palScreen[0x08] = new Color(32, 42, 0);
        palScreen[0x09] = new Color(8, 58, 0);
        palScreen[0x0A] = new Color(0, 64, 0);
        palScreen[0x0B] = new Color(0, 60, 0);
        palScreen[0x0C] = new Color(0, 50, 60);
        palScreen[0x0D] = new Color(0, 0, 0);
        palScreen[0x0E] = new Color(0, 0, 0);
        palScreen[0x0F] = new Color(0, 0, 0);
        palScreen[0x10] = new Color(152, 150, 152);
        palScreen[0x11] = new Color(8, 76, 196);
        palScreen[0x12] = new Color(48, 50, 236);
        palScreen[0x13] = new Color(92, 30, 228);
        palScreen[0x14] = new Color(136, 20, 176);
        palScreen[0x15] = new Color(160, 20, 100);
        palScreen[0x16] = new Color(152, 34, 32);
        palScreen[0x17] = new Color(120, 60, 0);
        palScreen[0x18] = new Color(84, 90, 0);
        palScreen[0x19] = new Color(40, 114, 0);
        palScreen[0x1A] = new Color(8, 124, 0);
        palScreen[0x1B] = new Color(0, 118, 40);
        palScreen[0x1C] = new Color(0, 102, 120);
        palScreen[0x1D] = new Color(0, 0, 0);
        palScreen[0x1E] = new Color(0, 0, 0);
        palScreen[0x1F] = new Color(0, 0, 0);
        palScreen[0x20] = new Color(236, 238, 236);
        palScreen[0x21] = new Color(76, 154, 236);
        palScreen[0x22] = new Color(120, 124, 236);
        palScreen[0x23] = new Color(176, 98, 236);
        palScreen[0x24] = new Color(228, 84, 236);
        palScreen[0x25] = new Color(236, 88, 180);
        palScreen[0x26] = new Color(236, 106, 100);
        palScreen[0x27] = new Color(212, 136, 32);
        palScreen[0x28] = new Color(160, 170, 0);
        palScreen[0x29] = new Color(116, 196, 0);
        palScreen[0x2A] = new Color(76, 208, 32);
        palScreen[0x2B] = new Color(56, 204, 108);
        palScreen[0x2C] = new Color(56, 180, 204);
        palScreen[0x2D] = new Color(60, 60, 60);
        palScreen[0x2E] = new Color(0, 0, 0);
        palScreen[0x2F] = new Color(0, 0, 0);
        palScreen[0x30] = new Color(236, 238, 236);
        palScreen[0x31] = new Color(168, 204, 236);
        palScreen[0x32] = new Color(188, 188, 236);
        palScreen[0x33] = new Color(212, 178, 236);
        palScreen[0x34] = new Color(236, 174, 236);
        palScreen[0x35] = new Color(236, 174, 212);
        palScreen[0x36] = new Color(236, 180, 176);
        palScreen[0x37] = new Color(228, 196, 144);
        palScreen[0x38] = new Color(204, 210, 120);
        palScreen[0x39] = new Color(180, 222, 120);
        palScreen[0x3A] = new Color(168, 226, 144);
        palScreen[0x3B] = new Color(152, 226, 180);
        palScreen[0x3C] = new Color(160, 214, 228);
        palScreen[0x3D] = new Color(160, 162, 160);
        palScreen[0x3E] = new Color(0, 0, 0);
        palScreen[0x3F] = new Color(0, 0, 0);
    }

    private short[][] tblName;
    private short[] tblPalette;
    private short[][] tblPattern;

    public short cpuRead(int addr, boolean rdonly) {
        short data = 0x00;

        switch (addr) {
            case 0x0000: // Control
                data = control.reg();
                break;
            case 0x0001: // Mask
                data = mask.reg();
                break;
            case 0x0002: // Status
                data = (short) ((status.reg() & 0xE0) | (ppu_data_buffer & 0x1F));
                status.vertical_blank((short)0);
                address_latch = 0;
                break;
            case 0x0003: // OAM Address
                break;
            case 0x0004: // OAM Data
                break;
            case 0x0005: // Scroll
                break;
            case 0x0006: // PPU Address
                break;
            case 0x0007: // PPU Data
                data = ppu_data_buffer;
                ppu_data_buffer = ppuRead(ppu_address);

                if(ppu_address > 0x3f00) data = ppu_data_buffer;
                ppu_address += (control.increment_mode() != 0? 32 : 1);
                break;
        }

        return data;
    }

    public short cpuRead(int addr) { return cpuRead(addr, false); }
    public void cpuWrite(int addr, short data) {
        switch (addr) {
            case 0x0000: // Control
                control.reg(data);
                break;
            case 0x0001: // Mask
                mask.reg(data);
                break;
            case 0x0002: // Status
                break;
            case 0x0003: // OAM Address
                break;
            case 0x0004: // OAM Data
                break;
            case 0x0005: // Scroll
                break;
            case 0x0006: // PPU Address
                if(address_latch == 0) {
                    ppu_address = (ppu_address & 0x00FF) | ((data & 0x3F) << 8);
                    address_latch = 1;
                } else {
                    ppu_address = (ppu_address & 0xFF00) | (data & 0x3F);
                    address_latch = 0;
                }
                break;
            case 0x0007: // PPU Data
                ppuWrite(ppu_address, data);
                ppu_address += (control.increment_mode() != 0 ? 32 : 1);
                break;
        }
    }

    public short ppuRead(int addr, boolean rdonly) {
        short data = 0x00;
        addr &= 0x3FFF;

        short[] data_arr = new short[1];
        if(cart.ppuRead(addr, data_arr)) {
            data = data_arr[0];
        }
        else if(addr >= 0x0000 && addr <= 0x1FFF) {
            data = tblPattern[(addr & 0x1000) >> 12][addr & 0x0FFF];
        }
        else if(addr >= 0x2000 && addr <= 0x3EFF) {

        }
        else if(addr >= 0x3F00 && addr <= 0x3FFF) {
            addr &= 0x001F;
            if(addr == 0x0010) addr = 0x0000;
            if(addr == 0x0014) addr = 0x0004;
            if(addr == 0x0018) addr = 0x0008;
            if(addr == 0x001C) addr = 0x000C;
            data = tblPalette[addr];
        }

        return data;
    }

    public short ppuRead(int addr) { return ppuRead(addr, false); }
    public void ppuWrite(int addr, short data) {
        addr &= 0x3FFF;

        if(cart.ppuWrite(addr, data)) {}
        else if(addr >= 0x0000 && addr <= 0x1FFF) {
            tblPattern[(addr & 0x1000) >> 12][addr & 0x0FFF] = data;
        }
        else if(addr >= 0x2000 && addr <= 0x3EFF) {

        }
        else if(addr >= 0x3F00 && addr <= 0x3FFF) {
            addr &= 0x001F;
            if(addr == 0x0010) addr = 0x0000;
            if(addr == 0x0014) addr = 0x0004;
            if(addr == 0x0018) addr = 0x0008;
            if(addr == 0x001C) addr = 0x000C;
            tblPalette[addr] = data;
        }
    }

    private Cartridge cart;

    public void connectCartridge(Cartridge cartridge) {
        cart = cartridge;
    }

    public void clock() {
        if(cycle >= 1 && scanline >= 0 && cycle <= sprScreen.getWidth() && scanline < sprScreen.getHeight()) {
            sprScreen.setRGB(cycle - 1, scanline, palScreen[RNG.randBool()? 0x3F : 0x30].getRGB());
        }

        cycle++;
        if(cycle >= 341) {
            cycle = 0;
            scanline++;
            if(scanline >= 261) {
                scanline = -1;
                frame_complete = true;
            }
        }
    }

    private final Color[] palScreen;
    private final BufferedImage sprScreen;
    private final BufferedImage[] sprNameTable;
    private final BufferedImage[] sprPatternTable;

    public BufferedImage GetScreen() {
        return sprScreen;
    }

    public BufferedImage GetNameTable(short i) {
        return sprNameTable[i];
    }

    private Color GetColorFromPaletteRam(short palette, short pixel) {
        return palScreen[ppuRead(0x3F00 + (palette << 2) + pixel) & 0x3F];
    }

    public BufferedImage GetPatternTable(short i, short palette) {
        for(int nTileY = 0; nTileY < 16; nTileY++) {
            for(int nTileX = 0; nTileX < 16; nTileX++) {
                int nOffset = nTileY * 256 + nTileX * 16;

                for(int row = 0; row < 8; row++) {
                    short tile_lsb = ppuRead(i * 0x1000 + nOffset + row);
                    short tile_msb = ppuRead(i * 0x1000 + nOffset + row + 8);

                    for(int col = 0; col < 8; col++) {
                        short pixel = (short) ((tile_lsb & 0x01) + (tile_msb & 0x01));
                        tile_lsb >>= 1; tile_msb >>= 1;

                        sprPatternTable[i].setRGB(
                                nTileX * 8 + (7 - col),
                                nTileY * 8 + row,
                                GetColorFromPaletteRam(palette, pixel).getRGB()
                        );
                    }
                }
            }
        }

        return sprPatternTable[i];
    }

    public boolean frame_complete = false;

    private short scanline = 0;
    private short cycle = 0;

    private static class UNION_status {
        private short reg = 0b00000000;

        public UNION_status() {

        }

        public short reg() {
            return Types.getUInt8(reg);
        }

        public void reg(short reg) {
            this.reg = Types.getUInt8(reg);
        }

        public short unused() {
            return (short) ((reg & 0b11111000) >> 3);
        }

        public void unused(short unused) {
            short value = (short) ((unused << 3) & 0b11111000);
            reg |= value;
            reg &= value;
        }

        public short sprite_overflow() {
            return (short) ((reg & 0b00000100) >> 2);
        }

        public void sprite_overflow(short sprite_overflow) {
            short value = (short) ((sprite_overflow << 2) & 0b00000100);
            reg |= value;
            reg &= value;
        }

        public short sprite_zero_hit() {
            return (short) ((reg & 0b00000010) >> 1);
        }

        public void sprite_zero_hit(short sprite_zero_hit) {
            short value = (short) ((sprite_zero_hit << 1) & 0b00000010);
            reg |= value;
            reg &= value;
        }

        public short vertical_blank() {
            return (short) (reg & 0b00000001);
        }

        public void vertical_blank(short vertical_blank) {
            short value = (short) (vertical_blank & 0b00000001);
            reg |= value;
            reg &= value;
        }

    }

    private UNION_status status;

    private static class UNION_mask {
        private short reg = 0b00000000;

        public UNION_mask() {}

        public short reg() {
            return Types.getUInt8(reg);
        }

        public void reg(short reg) {
            this.reg = Types.getUInt8(reg);
        }

        public short grayscale() {
            return (short) ((reg & 0b10000000) >> 7);
        }

        public void grayscale(short grayscale) {
            short value = (short) ((grayscale << 7) & 0b10000000);
            reg |= value;
            reg &= value;
        }

        public short render_background_left() {
            return (short) ((reg & 0b01000000) >> 6);
        }

        public void render_background_left(short render_background_left) {
            short value = (short) ((render_background_left << 6) & 0b01000000);
            reg |= value;
            reg &= value;
        }

        public short render_sprites_left() {
            return (short) ((reg & 0b00100000) >> 5);
        }

        public void render_sprites_left(short render_sprites_left) {
            short value = (short) ((render_sprites_left << 5) & 0b00100000);
            reg |= value;
            reg &= value;
        }

        public short render_background() {
            return (short) ((reg & 0b00010000) >> 4);
        }

        public void render_background(short render_background) {
            short value = (short) ((render_background << 4) & 0b00010000);
            reg |= value;
            reg &= value;
        }

        public short render_sprites() {
            return (short) ((reg & 0b00001000) >> 3);
        }

        public void render_sprites(short render_sprites) {
            short value = (short) ((render_sprites << 3) & 0b00001000);
            reg |= value;
            reg &= value;
        }

        public short enhance_red() {
            return (short) ((reg & 0b00000100) >> 2);
        }

        public void enhance_red(short enhance_red) {
            short value = (short) ((enhance_red << 2) & 0b00000100);
            reg |= value;
            reg &= value;
        }

        public short enhance_green() {
            return (short) ((reg & 0b00000010) >> 1);
        }

        public void enhance_green(short enhance_green) {
            short value = (short) ((enhance_green << 1) & 0b00000010);
            reg |= value;
            reg &= value;
        }

        public short enhance_blue() {
            return (short) (reg & 0b00000001);
        }

        public void enhance_blue(short enhance_blue) {
            short value = (short) (enhance_blue & 0b00000001);
            reg |= value;
            reg &= value;
        }

    }

    private UNION_mask mask;

    private static class PPUCTRL {
        private short reg = 0b00000000;

        public PPUCTRL() {}

        public short reg() {
            return Types.getUInt8(reg);
        }

        public void reg(short reg) {
            this.reg = Types.getUInt8(reg);
        }

        public short nametable_x() {
            return (short) ((reg & 0b10000000) >> 7);
        }

        public void nametable_x(short nametable_x) {
            short value = (short) ((nametable_x << 7) & 0b10000000);
            reg |= value;
            reg &= value;
        }

        public short nametable_y() {
            return (short) ((reg & 0b01000000) >> 6);
        }

        public void nametable_y(short nametable_y) {
            short value = (short) ((nametable_y << 6) & 0b01000000);
            reg |= value;
            reg &= value;
        }

        public short increment_mode() {
            return (short) ((reg & 0b00100000) >> 5);
        }

        public void increment_mode(short increment_mode) {
            short value = (short) ((increment_mode << 5) & 0b00100000);
            reg |= value;
            reg &= value;
        }

        public short pattern_sprite() {
            return (short) ((reg & 0b00010000) >> 4);
        }

        public void pattern_sprite(short pattern_sprite) {
            short value = (short) ((pattern_sprite << 4) & 0b00010000);
            reg |= value;
            reg &= value;
        }

        public short pattern_background() {
            return (short) ((reg & 0b00001000) >> 3);
        }

        public void pattern_background(short pattern_background) {
            short value = (short) ((pattern_background << 3) & 0b00001000);
            reg |= value;
            reg &= value;
        }

        public short sprite_size() {
            return (short) ((reg & 0b00000100) >> 2);
        }

        public void sprite_size(short sprite_size) {
            short value = (short) ((sprite_size << 2) & 0b00000100);
            reg |= value;
            reg &= value;
        }

        public short slave_mode() {
            return (short) ((reg & 0b00000010) >> 1);
        }

        public void slave_mode(short slave_mode) {
            short value = (short) ((slave_mode << 1) & 0b00000010);
            reg |= value;
            reg &= value;
        }

        public short enable_nmi() {
            return (short) (reg & 0b00000001);
        }

        public void enable_nmi(short enable_nmi) {
            short value = (short) (enable_nmi & 0b00000001);
            reg |= value;
            reg &= value;
        }

    }

    private PPUCTRL control;

    private short address_latch = 0x00;
    private short ppu_data_buffer = 0x00;
    private int ppu_address = 0x0000;

}
