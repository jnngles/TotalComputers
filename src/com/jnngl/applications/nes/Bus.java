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

import java.util.Arrays;

/**
 * Port of olcNES (https://github.com/OneLoneCoder/olcNES)
 * */
public class Bus {

    public Bus() {
        cpuRam = new short[2048];
        Arrays.fill(cpuRam, (short) 0x00);

        ppu = new PPU2C02();
        cpu = new CPU6502();

        cpu.connectBus(this);
    }

    public CPU6502 cpu;
    public PPU2C02 ppu;

    public short[] cpuRam; // SIZE = 2048

    public Cartridge cart;

    public void cpuWrite(int addr, short data) {
        if(cart.cpuWrite(addr, data)) {}
        else if(Types.getUInt16(addr) >= 0x0000 && Types.getUInt16(addr) <= 0x1FFF)
            cpuRam[Types.getUInt16(addr) & 0x07FF] = data;
        else if(Types.getUInt16(addr) >= 0x2000 && Types.getUInt16(addr) <= 0x3FFF)
            ppu.cpuWrite(Types.getUInt16(addr & 0x0007), data);
    }

    public short cpuRead(int addr, boolean readOnly) {
        short data = 0x00;

        short[] data_arr = new short[1];
        if(cart.cpuRead(addr, data_arr))
            data = data_arr[0];
        if(Types.getUInt16(addr) >= 0x0000 && Types.getUInt16(addr) <= 0x1FFF)
            data = cpuRam[Types.getUInt16(addr) & 0x07FF];
        else if(Types.getUInt16(addr) >= 0x2000 && Types.getUInt16(addr) <= 0x3FFF)
            data = ppu.cpuRead(Types.getUInt16(addr) & 0x0007, readOnly);

        return data;
    }

    public short cpuRead(int addr) { return cpuRead(addr, false); }

    public void insertCartridge(Cartridge cartridge) {
        cart = cartridge;
        ppu.connectCartridge(cartridge);
    }

    public void reset() {
        cpu.reset();
        nSystemClockCounter = 0;
    }

    public void clock() {
        ppu.clock();
        if(nSystemClockCounter % 3 == 0) {
            cpu.clock();
        }
        nSystemClockCounter++;
    }

    private long nSystemClockCounter = 0;

}
