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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;

/**
 * Port of olcNES (https://github.com/OneLoneCoder/olcNES)
 * */
public class Cartridge {

    public Cartridge(String sFileName) {

        vPRGMemory = new Vector<>();
        vCHRMemory = new Vector<>();

        class sHeader {
            public sHeader() {
                name = new char[4];
                unused = new char[5];
            }

            char[] name; // SIZE = 4
            short prg_rom_chunks;
            short chr_rom_chunks;
            short mapper1;
            short mapper2;
            short prg_ram_size;
            short tv_system1;
            short tv_system2;
            char[] unused; // SIZE = 5
        }

        sHeader header = new sHeader();

        byte[] bytes = new byte[0];

        int i = 0;
        try {
            bytes = Files.readAllBytes(new File(sFileName).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        header.name[0] = (char)bytes[i++];
        header.name[1] = (char)bytes[i++];
        header.name[2] = (char)bytes[i++];
        header.name[3] = (char)bytes[i++];
        header.prg_rom_chunks = bytes[i++];
        header.chr_rom_chunks = bytes[i++];
        header.mapper1 = bytes[i++];
        header.mapper2 = bytes[i++];
        header.prg_ram_size = bytes[i++];
        header.tv_system1 = bytes[i++];
        header.tv_system2 = bytes[i++];
        header.unused[0] = (char)bytes[i++];
        header.unused[1] = (char)bytes[i++];
        header.unused[2] = (char)bytes[i++];
        header.unused[3] = (char)bytes[i++];
        header.unused[4] = (char)bytes[i++];

        if((header.mapper1 & 0x04) != 0)
            i += 512;

        nMapperID = (short) (((header.mapper2 >> 4) << 4) | (header.mapper1 >> 4));

        short nFileType = 1;

        if(nFileType == 0) {

        }
        if(nFileType == 1) {
            nPRGBanks = header.prg_rom_chunks;
            vPRGMemory.setSize(nPRGBanks * 16384);
            for(int j = 0; j < vPRGMemory.size(); j++, i++) {
                vPRGMemory.set(j, (short)bytes[i]);
            }

            nCHRBanks = header.chr_rom_chunks;
            vCHRMemory.setSize(nCHRBanks * 8192);
            for(int j = 0; j < vCHRMemory.size(); j++, i++) {
                vCHRMemory.set(j, (short)bytes[i]);
            }
        }
        if(nFileType == 2) {

        }

        switch (nMapperID) {
            case 0: {
                pMapper = new Mapper_000(nPRGBanks, nCHRBanks);
                break;
            }
        }

    }

    private Vector<Short> vPRGMemory;
    private Vector<Short> vCHRMemory;

    private short nMapperID = 0;
    private short nPRGBanks = 0;
    private short nCHRBanks = 0;

    private Mapper pMapper;

    public boolean cpuRead(int addr, short[] data) {
        long[] mapped_addr = new long[1];

        if(pMapper.cpuMapRead(addr, mapped_addr)) {
            data[0] = vPRGMemory.get((int) mapped_addr[0]);
            return true;
        }

        return false;
    }

    public boolean cpuWrite(int addr, short data) {
        long[] mapped_addr = new long[1];

        if(pMapper.cpuMapWrite(addr, mapped_addr)) {
            vPRGMemory.set((int) mapped_addr[0], data);
            return true;
        }

        return false;
    }

    public boolean ppuRead(int addr, short[] data) {
        long[] mapped_addr = new long[1];

        if(pMapper.ppuMapRead(addr, mapped_addr)) {
            data[0] = vCHRMemory.get((int) mapped_addr[0]);
            return true;
        }

        return false;
    }

    public boolean ppuWrite(int addr, short data) {
        long[] mapped_addr = new long[1];

        if(pMapper.ppuMapWrite(addr, mapped_addr)) {
            vCHRMemory.set((int) mapped_addr[0], data);
            return true;
        }

        return false;
    }

}
