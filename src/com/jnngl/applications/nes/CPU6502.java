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

import java.util.*;

/**
 * Port of olcNES (https://github.com/OneLoneCoder/olcNES)
 * */
public class CPU6502 {

    public static class Instruction {
        public Instruction(String name, OPERATE_fn operate, ADDRMODE_fn addrmode, short cycles) {
            this.name = name;
            this.operate = operate;
            this.addrmode = addrmode;
            this.cycles = cycles;
        }

        public interface OPERATE_fn { short func(); }
        public interface ADDRMODE_fn { short func(); }

        public String name;
        public OPERATE_fn operate;
        public ADDRMODE_fn addrmode;
        public short cycles = 0;
    }

    public CPU6502() {

        lookup = new Vector<>();

        lookup.add(new Instruction("BRK", this::BRK, imm, (short) 7));
        lookup.add(new Instruction("ORA", this::ORA, izx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 3));
        lookup.add(new Instruction("ORA", this::ORA, zp0, (short) 3));
        lookup.add(new Instruction("ASL", this::ASL, zp0, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("PHP", this::PHP, imp, (short) 3));
        lookup.add(new Instruction("ORA", this::ORA, imm, (short) 2));
        lookup.add(new Instruction("ASL", this::ASL, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("ORA", this::ORA, abs, (short) 4));
        lookup.add(new Instruction("ASL", this::ASL, abs, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("BPL", this::BPL, rel, (short) 2));
        lookup.add(new Instruction("ORA", this::ORA, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("ORA", this::ORA, zpx, (short) 4));
        lookup.add(new Instruction("ASL", this::ASL, zpx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("CLC", this::CLC, imp, (short) 2));
        lookup.add(new Instruction("ORA", this::ORA, aby, (short) 4));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("ORA", this::ORA, abx, (short) 4));
        lookup.add(new Instruction("ASL", this::ASL, abx, (short) 7));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("JSR", this::JSR, abs, (short) 6));
        lookup.add(new Instruction("AND", this::AND, izx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("BIT", this::BIT, zp0, (short) 3));
        lookup.add(new Instruction("AND", this::AND, zp0, (short) 3));
        lookup.add(new Instruction("ROL", this::ROL, zp0, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("PLP", this::PLP, imp, (short) 4));
        lookup.add(new Instruction("AND", this::AND, imm, (short) 2));
        lookup.add(new Instruction("ROL", this::ROL, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("BIT", this::BIT, abs, (short) 4));
        lookup.add(new Instruction("AND", this::AND, abs, (short) 4));
        lookup.add(new Instruction("ROL", this::ROL, abs, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("BMI", this::BMI, rel, (short) 2));
        lookup.add(new Instruction("AND", this::AND, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("AND", this::AND, zpx, (short) 4));
        lookup.add(new Instruction("ROL", this::ROL, zpx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("SEC", this::SEC, imp, (short) 2));
        lookup.add(new Instruction("AND", this::AND, aby, (short) 4));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("AND", this::AND, abx, (short) 4));
        lookup.add(new Instruction("ROL", this::ROL, abx, (short) 7));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("RTI", this::RTI, imp, (short) 6));
        lookup.add(new Instruction("EOR", this::EOR, izx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 3));
        lookup.add(new Instruction("EOR", this::EOR, zp0, (short) 3));
        lookup.add(new Instruction("LSR", this::LSR, zp0, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("PHA", this::PHA, imp, (short) 3));
        lookup.add(new Instruction("EOR", this::EOR, imm, (short) 2));
        lookup.add(new Instruction("LSR", this::LSR, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("JMP", this::JMP, abs, (short) 3));
        lookup.add(new Instruction("EOR", this::EOR, abs, (short) 4));
        lookup.add(new Instruction("LSR", this::LSR, abs, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("BVC", this::BVC, rel, (short) 2));
        lookup.add(new Instruction("EOR", this::EOR, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("EOR", this::EOR, zpx, (short) 4));
        lookup.add(new Instruction("LSR", this::LSR, zpx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("CLI", this::CLI, imp, (short) 2));
        lookup.add(new Instruction("EOR", this::EOR, aby, (short) 4));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("EOR", this::EOR, abx, (short) 4));
        lookup.add(new Instruction("LSR", this::LSR, abx, (short) 7));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("RTS", this::RTS, imp, (short) 6));
        lookup.add(new Instruction("ADC", this::ADC, izx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 3));
        lookup.add(new Instruction("ADC", this::ADC, zp0, (short) 3));
        lookup.add(new Instruction("ROR", this::ROR, zp0, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("PLA", this::PLA, imp, (short) 4));
        lookup.add(new Instruction("ADC", this::ADC, imm, (short) 2));
        lookup.add(new Instruction("ROR", this::ROR, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("JMP", this::JMP, ind, (short) 5));
        lookup.add(new Instruction("ADC", this::ADC, abs, (short) 4));
        lookup.add(new Instruction("ROR", this::ROR, abs, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("BVS", this::BVS, rel, (short) 2));
        lookup.add(new Instruction("ADC", this::ADC, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("ADC", this::ADC, zpx, (short) 4));
        lookup.add(new Instruction("ROR", this::ROR, zpx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("SEI", this::SEI, imp, (short) 2));
        lookup.add(new Instruction("ADC", this::ADC, aby, (short) 4));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("ADC", this::ADC, abx, (short) 4));
        lookup.add(new Instruction("ROR", this::ROR, abx, (short) 7));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("STA", this::STA, izx, (short) 6));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("STY", this::STY, zp0, (short) 3));
        lookup.add(new Instruction("STA", this::STA, zp0, (short) 3));
        lookup.add(new Instruction("STX", this::STX, zp0, (short) 3));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 3));
        lookup.add(new Instruction("DEY", this::DEY, imp, (short) 2));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("TXA", this::TXA, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("STY", this::STY, abs, (short) 4));
        lookup.add(new Instruction("STA", this::STA, abs, (short) 4));
        lookup.add(new Instruction("STX", this::STX, abs, (short) 4));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 4));
        lookup.add(new Instruction("BCC", this::BCC, rel, (short) 2));
        lookup.add(new Instruction("STA", this::STA, izy, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("STY", this::STY, zpx, (short) 4));
        lookup.add(new Instruction("STA", this::STA, zpx, (short) 4));
        lookup.add(new Instruction("STX", this::STX, zpy, (short) 4));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 4));
        lookup.add(new Instruction("TYA", this::TYA, imp, (short) 2));
        lookup.add(new Instruction("STA", this::STA, aby, (short) 5));
        lookup.add(new Instruction("TXS", this::TXS, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 5));
        lookup.add(new Instruction("STA", this::STA, abx, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("LDY", this::LDY, imm, (short) 2));
        lookup.add(new Instruction("LDA", this::LDA, izx, (short) 6));
        lookup.add(new Instruction("LDX", this::LDX, imm, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("LDY", this::LDY, zp0, (short) 3));
        lookup.add(new Instruction("LDA", this::LDA, zp0, (short) 3));
        lookup.add(new Instruction("LDX", this::LDX, zp0, (short) 3));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 3));
        lookup.add(new Instruction("TAY", this::TAY, imp, (short) 2));
        lookup.add(new Instruction("LDA", this::LDA, imm, (short) 2));
        lookup.add(new Instruction("TAX", this::TAX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("LDY", this::LDY, abs, (short) 4));
        lookup.add(new Instruction("LDA", this::LDA, abs, (short) 4));
        lookup.add(new Instruction("LDX", this::LDX, abs, (short) 4));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 4));
        lookup.add(new Instruction("BCS", this::BCS, rel, (short) 2));
        lookup.add(new Instruction("LDA", this::LDA, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("LDY", this::LDY, zpx, (short) 4));
        lookup.add(new Instruction("LDA", this::LDA, zpx, (short) 4));
        lookup.add(new Instruction("LDX", this::LDX, zpy, (short) 4));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 4));
        lookup.add(new Instruction("CLV", this::CLV, imp, (short) 2));
        lookup.add(new Instruction("LDA", this::LDA, aby, (short) 4));
        lookup.add(new Instruction("TSX", this::TSX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 4));
        lookup.add(new Instruction("LDY", this::LDY, abx, (short) 4));
        lookup.add(new Instruction("LDA", this::LDA, abx, (short) 4));
        lookup.add(new Instruction("LDX", this::LDX, aby, (short) 4));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 4));
        lookup.add(new Instruction("CPY", this::CPY, imm, (short) 2));
        lookup.add(new Instruction("CMP", this::CMP, izx, (short) 6));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("CPY", this::CPY, zp0, (short) 3));
        lookup.add(new Instruction("CMP", this::CMP, zp0, (short) 3));
        lookup.add(new Instruction("DEC", this::DEC, zp0, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("INY", this::INY, imp, (short) 2));
        lookup.add(new Instruction("CMP", this::CMP, imm, (short) 2));
        lookup.add(new Instruction("DEX", this::DEX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("CPY", this::CPY, abs, (short) 4));
        lookup.add(new Instruction("CMP", this::CMP, abs, (short) 4));
        lookup.add(new Instruction("DEC", this::DEC, abs, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("BNE", this::BNE, rel, (short) 2));
        lookup.add(new Instruction("CMP", this::CMP, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("CMP", this::CMP, zpx, (short) 4));
        lookup.add(new Instruction("DEC", this::DEC, zpx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("CLD", this::CLD, imp, (short) 2));
        lookup.add(new Instruction("CMP", this::CMP, aby, (short) 4));
        lookup.add(new Instruction("NOP", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("CMP", this::CMP, abx, (short) 4));
        lookup.add(new Instruction("DEC", this::DEC, abx, (short) 7));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("CPX", this::CPX, imm, (short) 2));
        lookup.add(new Instruction("SBC", this::SBC, izx, (short) 6));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("CPX", this::CPX, zp0, (short) 3));
        lookup.add(new Instruction("SBC", this::SBC, zp0, (short) 3));
        lookup.add(new Instruction("INC", this::INC, zp0, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 5));
        lookup.add(new Instruction("INX", this::INX, imp, (short) 2));
        lookup.add(new Instruction("SBC", this::SBC, imm, (short) 2));
        lookup.add(new Instruction("NOP", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::SBC, imp, (short) 2));
        lookup.add(new Instruction("CPX", this::CPX, abs, (short) 4));
        lookup.add(new Instruction("SBC", this::SBC, abs, (short) 4));
        lookup.add(new Instruction("INC", this::INC, abs, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("BEQ", this::BEQ, rel, (short) 2));
        lookup.add(new Instruction("SBC", this::SBC, izy, (short) 5));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 8));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("SBC", this::SBC, zpx, (short) 4));
        lookup.add(new Instruction("INC", this::INC, zpx, (short) 6));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 6));
        lookup.add(new Instruction("SED", this::SED, imp, (short) 2));
        lookup.add(new Instruction("SBC", this::SBC, aby, (short) 4));
        lookup.add(new Instruction("NOP", this::NOP, imp, (short) 2));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));
        lookup.add(new Instruction("???", this::NOP, imp, (short) 4));
        lookup.add(new Instruction("SBC", this::SBC, abx, (short) 4));
        lookup.add(new Instruction("INC", this::INC, abx, (short) 7));
        lookup.add(new Instruction("???", this::XXX, imp, (short) 7));

    }

    public static short  C = (1 << 0), // Carry Bit
                         Z = (1 << 1), // Zero
                         I = (1 << 2), // Disable Interrupts
                         D = (1 << 3), // Decimal Mode
                         B = (1 << 4), // Break
                         U = (1 << 5), // Unused
                         V = (1 << 6), // Overflow
                         N = (1 << 7); // Negative

    public short a = 0x00; // Accumulator
    public short x = 0x00;
    public short y = 0x00;
    public short stkp = 0x00; // Stack Pointer
    public int pc = 0x0000; // Program Counter
    public short status = 0x00;

    public void connectBus(Bus n) { bus = n; }

    // Addressing Modes
    private final Instruction.ADDRMODE_fn imp = this :: IMP;
    public short IMP() {
        fetched = a;
        return 0;
    }

    private final Instruction.ADDRMODE_fn imm = this :: IMM;
    public short IMM() {
        addr_abs = pc;
        pc = Types.getUInt16(pc+1);
        return 0;
    }

    private final Instruction.ADDRMODE_fn zp0 = this :: ZP0;
    public short ZP0() {
        addr_abs = read(pc);
        pc = Types.getUInt16(pc+1);
        addr_abs = Types.getUInt16(addr_abs & 0x00FF);
        return 0;
    }

    private final Instruction.ADDRMODE_fn zpx = this :: ZPX;
    public short ZPX() {
        addr_abs = Types.getUInt16(read(pc) + x);
        pc = Types.getUInt16(pc+1);
        addr_abs = Types.getUInt16(addr_abs & 0x00FF);
        return 0;
    }

    private final Instruction.ADDRMODE_fn zpy = this :: ZPY;
    public short ZPY() {
        addr_abs = Types.getUInt16(read(pc) + y);
        pc = Types.getUInt16(pc+1);
        addr_abs = Types.getUInt16(addr_abs & 0x00FF);
        return 0;
    }

    private final Instruction.ADDRMODE_fn rel = this :: REL;
    public short REL() {
        addr_rel = read(pc);
        pc = Types.getUInt16(pc+1);
        if(Types.getUInt16(addr_rel & 0x80) != 0)
            addr_rel = Types.getUInt16(addr_rel | 0xFF00);
        return 0;
    }

    private final Instruction.ADDRMODE_fn abs = this :: ABS;
    public short ABS() {
        int lo = read(pc);
        pc = Types.getUInt16(pc+1);
        int hi = read(pc);
        pc = Types.getUInt16(pc+1);

        addr_abs = Types.getUInt16(hi << 8) | lo;

        return 0;
    }

    private final Instruction.ADDRMODE_fn abx = this :: ABX;
    public short ABX() {
        int lo = read(pc);
        pc = Types.getUInt16(pc+1);
        int hi = read(pc);
        pc = Types.getUInt16(pc+1);

        addr_abs = Types.getUInt16(hi << 8) | lo;
        addr_abs = Types.getUInt16(addr_abs+x);

        if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(hi << 8)) return 1;
        return 0;
    }

    private final Instruction.ADDRMODE_fn aby = this :: ABY;
    public short ABY() {
        int lo = read(pc);
        pc = Types.getUInt16(pc+1);
        int hi = read(pc);
        pc = Types.getUInt16(pc+1);

        addr_abs = Types.getUInt16(hi << 8) | lo;
        addr_abs = Types.getUInt16(addr_abs+y);

        if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(hi << 8)) return 1;
        return 0;
    }

    private final Instruction.ADDRMODE_fn ind = this :: IND;
    public short IND() {
        int ptr_lo = read(pc);
        pc = Types.getUInt16(pc+1);
        int ptr_hi = read(pc);
        pc = Types.getUInt16(pc+1);

        int ptr = Types.getUInt16(ptr_hi << 8) | ptr_lo;

        addr_abs = Types.getUInt16(read(Types.getUInt16(ptr+1) << 8)) | read(ptr);

        return 0;
    }

    private final Instruction.ADDRMODE_fn izx = this :: IZX;
    public short IZX() {
        int t = read(pc);
        pc = Types.getUInt16(pc+1);

        int lo = read(Types.getUInt16(Types.getUInt16(t + x) & 0x00FF));
        int hi = read(Types.getUInt16(Types.getUInt16(t+x+1) & 0x00FF));

        addr_abs = Types.getUInt16(hi << 8) | lo;

        return 0;
    }

    private final Instruction.ADDRMODE_fn izy = this :: IZY;
    public short IZY() {
        int t = read(pc);
        pc = Types.getUInt16(pc+1);

        int lo = read(Types.getUInt16(t & 0x00FF));
        int hi = read(Types.getUInt16(Types.getUInt16(t + 1) & 0x00FF));

        addr_abs = Types.getUInt16(hi << 8) | lo;
        addr_abs = Types.getUInt16(addr_abs + y);

        if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(hi << 8)) return 1;
        return 0;
    }

    // Opcodes
    public short ADC() {
        fetch();
        int temp = Types.getUInt16((int)a + (int)fetched + (int)GetFlag(C));
        SetFlag(C, temp > 255);
        SetFlag(Z, Types.getUInt16(temp & 0x00FF) == 0);
        SetFlag(N, Types.getUInt16(temp & 0x80) != 0);
        SetFlag(V, Types.getUInt16(Types.getUInt16((~Types.getUInt16((int)a ^ (int)fetched) & Types.getUInt16((int)a ^ temp))) & 0x0080) != 0);
        a = Types.getUInt8((short) (temp & 0x00FF));
        return 1;
    }

    public short AND() {
        fetch();
        a = Types.getUInt8((short) (a & fetched));
        SetFlag(Z, Types.getUInt8(a) == 0x00);
        SetFlag(N, Types.getUInt8((short) (a & 0x80)) != 0);
        return 1;
    }

    public short ASL() {
        fetch();
        int temp = Types.getUInt16(fetched << 1);
        SetFlag(C, (temp & 0xFF00) > 0);
        SetFlag(Z, (temp & 0x00FF) == 0x00);
        SetFlag(N, (temp & 0x80) != 0);
        if(lookup.get(opcode).addrmode.equals((Instruction.ADDRMODE_fn) imp)) a = Types.getUInt8((short) (temp & 0x00FF));
        else write(addr_abs, Types.getUInt8((short) (temp & 0x00FF)));
        return 0;

    }

    public short BCC() {
        if(GetFlag(C) == 0) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BCS() {
        if(GetFlag(C) == 1) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BEQ() {
        if(GetFlag(Z) == 1) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BIT() {
        fetch();
        int temp = a & fetched;
        SetFlag(Z, (temp & 0x00FF) == 0x00);
        SetFlag(N, (fetched & (1 << 7)) != 0);
        SetFlag(V, (fetched & (1 << 6)) != 0);
        return 0;
    }

    public short BMI() {
        if(GetFlag(N) == 1) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BNE() {
        if(GetFlag(Z) == 0) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BPL() {
        if(GetFlag(N) == 0) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BRK() {
        pc++;

        SetFlag(I, true);
        write(0x0100 + stkp, Types.getUInt8((short) ((pc >> 8) & 0x00FF)));
        stkp = Types.getUInt8((short) (stkp-1));
        write(0x0100 + stkp, Types.getUInt8((short) (pc & 0x00FF)));
        stkp = Types.getUInt8((short) (stkp-1));

        SetFlag(B, true);
        write(0x0100 + stkp, status);
        stkp = Types.getUInt8((short) (stkp-1));
        SetFlag(B, false);

        pc = read(0xFFFE) | Types.getUInt16(read(0xFFFF) << 8);
        return 0;
    }

    public short BVC() {
        if(GetFlag(V) == 0) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short BVS() {
        if(GetFlag(V) == 1) {
            cycles++;
            addr_abs = Types.getUInt16(pc + addr_rel);

            if(Types.getUInt16(addr_abs & 0xFF00) != Types.getUInt16(pc & 0xFF00))
                cycles++;

            pc = addr_abs;
        }

        return 0;
    }

    public short CLC() {
        SetFlag(C, false);
        return 0;
    }

    public short CLD() {
        SetFlag(D, false);
        return 0;
    }

    public short CLI() {
        SetFlag(I, false);
        return 0;
    }

    public short CLV() {
        SetFlag(V, false);
        return 0;
    }

    public short CMP() {
        fetch();
        int temp = Types.getUInt16((int)a - (int)fetched);
        SetFlag(C, a >= fetched);
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        return 1;
    }

    public short CPX() {
        fetch();
        int temp = Types.getUInt16((int)x - (int)fetched);
        SetFlag(C, x >= fetched);
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        return 0;
    }

    public short CPY() {
        fetch();
        int temp = Types.getUInt16((int)y - (int)fetched);
        SetFlag(C, y >= fetched);
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        return 0;
    }

    public short DEC() {
        fetch();
        int temp = Types.getUInt16(fetched - 1);
        write(addr_abs, Types.getUInt8((short) (temp & 0x00FF)));
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        return 0;
    }

    public short DEX() {
        x = Types.getUInt8((short) (x-1));
        SetFlag(Z, x == 0x00);
        SetFlag(N, (x & 0x80) != 0);
        return 0;
    }

    public short DEY() {
        y = Types.getUInt8((short) (y - 1));
        SetFlag(Z, y == 0x00);
        SetFlag(N, (y & 0x80) != 0);
        return 0;
    }

    public short EOR() {
        fetch();
        a = Types.getUInt8((short) (a ^ fetched));
        SetFlag(Z, a == 0x00);
        SetFlag(N, (a & 0x80) != 0);
        return 1;
    }

    public short INC() {
        fetch();
        int temp = fetched + 1;
        write(addr_abs, Types.getUInt8((short) (temp & 0x00FF)));
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        return 0;
    }

    public short INX() {
        x = Types.getUInt8((short) (x+1));
        SetFlag(Z, x == 0x00);
        SetFlag(N, (x & 0x80) != 0);
        return 0;
    }

    public short INY() {
        y = Types.getUInt8((short) (y+1));
        SetFlag(Z, y == 0x00);
        SetFlag(N, (y & 0x80) != 0);
        return 0;
    }

    public short JMP() {
        pc = addr_abs;
        return 0;
    }

    public short JSR() {
        pc = Types.getUInt16(pc-1);

        write(0x0100 + stkp, Types.getUInt8((short) ((pc >> 8) & 0x00FF)));
        stkp = Types.getUInt8((short) (stkp-1));
        write(0x0100 + stkp, Types.getUInt8((short) (pc & 0x00FF)));
        stkp = Types.getUInt8((short) (stkp-1));

        pc = addr_abs;
        return 0;
    }

    public short LDA() {
        fetch();
        a = fetched;
        SetFlag(Z, a == 0x00);
        SetFlag(N, (a & 0x80) != 0);
        return 1;
    }

    public short LDX() {
        fetch();
        x = fetched;
        SetFlag(Z, x == 0x00);
        SetFlag(N, (x & 0x80) != 0);
        return 1;
    }

    public short LDY() {
        fetch();
        y = fetched;
        SetFlag(Z, y == 0x00);
        SetFlag(N, (y & 0x80) != 0);
        return 1;
    }

    public short LSR() {
        fetch();
        SetFlag(C, (fetched & 0x0001) != 0);
        int temp = fetched >> 1;
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        if(lookup.get(opcode).addrmode.equals((Instruction.ADDRMODE_fn) imp)) a = (short) (temp & 0x00FF);
        else write(addr_abs, (short) (temp & 0x00FF));
        return 0;
    }

    public short NOP() {
        return (short) switch (opcode) {
            case 0x1C, 0x3C, 0x5C, 0x7C, 0xDC, 0xFC -> 1;
            default -> 0;
        };
    }

    public short ORA() {
        fetch();
        a = (short) (a | fetched);
        SetFlag(Z, a == 0x00);
        SetFlag(N, (a & 0x80) != 0);
        return 1;
    }

    public short PHA() {
        write(Types.getUInt16(0x0100 + stkp), a);
        stkp--;
        return 0;
    }

    public short PHP() {
        write(0x0100 + stkp, (short) (status | B | U));
        SetFlag(B, false);
        SetFlag(U, false);
        stkp = Types.getUInt8((short) (stkp - 1));
        return 0;
    }

    public short PLA() {
        stkp = Types.getUInt8((short) (stkp+1));
        a = read(Types.getUInt16(0x0100 + stkp));
        SetFlag(Z, a == 0x00);
        SetFlag(N, Types.getUInt8((short) (a & 0x80)) != 0);
        return 0;
    }

    public short PLP() {
        stkp = Types.getUInt8((short) (stkp+1));
        status = read(0x0100 + stkp);
        SetFlag(U, true);
        return 0;
    }

    public short ROL() {
        fetch();
        int temp = (fetched << 1) | GetFlag(C);
        SetFlag(C, (temp & 0xFF00) != 0);
        SetFlag(Z, (temp & 0x00FF) == 0x0000);
        SetFlag(N, (temp & 0x0080) != 0);
        if(lookup.get(opcode).addrmode.equals((Instruction.ADDRMODE_fn) imp)) a = (short) (temp & 0x00FF);
        else write(addr_abs, (short) (temp & 0x00FF));
        return 0;
    }

    public short ROR() {
        fetch();
        int temp = (GetFlag(C) << 7) | (fetched >> 1);
        SetFlag(C, (fetched & 0x01) != 0);
        SetFlag(Z, (temp & 0x00FF) == 0x00);
        SetFlag(N, (temp & 0x0080) != 0);
        if(lookup.get(opcode).addrmode.equals((Instruction.ADDRMODE_fn) imp)) a = (short) (temp & 0x00FF);
        else write(addr_abs, (short) (temp & 0x00FF));
        return 0;
    }

    public short RTI() {
        stkp = Types.getUInt8((short) (stkp+1));
        status = read(0x0100 + stkp);
        status = Types.getUInt8((short) (status & Types.getUInt8((short) ~B)));
        status = Types.getUInt8((short) (status & Types.getUInt8((short) ~U)));

        stkp = Types.getUInt8((short) (stkp+1));
        pc = read(0x0100 + stkp);
        stkp = Types.getUInt8((short) (stkp+1));
        pc = Types.getUInt16(pc | Types.getUInt16(read(0x0100 + stkp) << 8));
        return 0;
    }

    public short RTS() {
        stkp = Types.getUInt8((short) (stkp + 1));
        pc = read(0x0100 + stkp);
        stkp = Types.getUInt8((short) (stkp + 1));
        pc |= read(0x0100 + stkp) << 8;

        pc = Types.getUInt16(pc + 1);
        return 0;
    }

    public short SBC() {
        fetch();

        int value = Types.getUInt16((int)fetched ^ 0x00FF);

        int temp = Types.getUInt16((int) a + value + (int)GetFlag(C));
        SetFlag(C, Types.getUInt16(temp & 0xFF00) != 0);
        SetFlag(Z, Types.getUInt16(temp & 0x00FF) == 0);
        SetFlag(V, Types.getUInt16(Types.getUInt16(temp ^ (int)a) & Types.getUInt16(temp ^ value) & 0x0080) != 0);
        SetFlag(N, Types.getUInt16(temp & 0x0080) != 0);
        a = Types.getUInt8((short) (temp & 0x00FF));

        return 1;
    }
    public short SEC() {
        SetFlag(C, true);
        return 0;
    }

    public short SED() {
        SetFlag(D, true);
        return 0;
    }

    public short SEI() {
        SetFlag(I, true);
        return 0;
    }

    public short STA() {
        write(addr_abs, a);
        return 0;
    }

    public short STX() {
        write(addr_abs, x);
        return 0;
    }

    public short STY() {
        write(addr_abs, y);
        return 0;
    }

    public short TAX() {
        x = a;
        SetFlag(Z, x == 0x00);
        SetFlag(N, (x & 0x80) != 0);
        return 0;
    }

    public short TAY() {
        y = a;
        SetFlag(Z, y == 0x00);
        SetFlag(N, (y & 0x80) != 0);
        return 0;
    }

    public short TSX() {
        x = stkp;
        SetFlag(Z, x == 0x00);
        SetFlag(N, (x & 0x80) != 0);
        return 0;
    }

    public short TXA() {
        a = x;
        SetFlag(Z, a == 0x00);
        SetFlag(N, (a & 0x80) != 0);
        return 0;
    }

    public short TXS() {
        stkp = x;
        return 0;
    }

    public short TYA() {
        a = y;
        SetFlag(Z, a == 0x00);
        SetFlag(N, (a & 0x80) != 0);
        return 0;
    }

    // Illegal opcodes
    public short XXX() {
        return 0;
    }

    public void clock() {
        if(cycles == 0) {
            opcode = Types.getUInt8(read(pc));
            pc = Types.getUInt16(pc+1);

            cycles = lookup.get(opcode).cycles;

            short adc1 = lookup.get(opcode).addrmode.func();
            short adc2 = lookup.get(opcode).operate.func();

            cycles += Types.getUInt8((short) (adc1 & adc2));
        }

        cycles--;
    }

    public void reset() {
        a = 0;
        x = 0;
        y = 0;
        stkp = 0xFD;
        status = U;

        addr_abs = 0xFFFC;
        int lo = read(addr_abs);
        int hi = read(Types.getUInt16(addr_abs + 1));

        pc = Types.getUInt16(hi << 8) | lo;

        addr_rel = 0x0000;
        addr_abs = 0x0000;
        fetched = 0x00;

        cycles = 8;
    }

    public void irq() {
        if(GetFlag(I) == 0) {
            write(0x0100 + stkp, Types.getUInt8((short) ((pc >> 8) & 0x00FF)));
            stkp = Types.getUInt8((short) (stkp-1));
            write(0x0100 + stkp, Types.getUInt8((short) (pc & 0x00FF)));
            stkp = Types.getUInt8((short) (stkp-1));

            SetFlag(B, false);
            SetFlag(U, true);
            SetFlag(I, true);
            write(0x0100 + stkp, status);
            stkp = Types.getUInt8((short) (stkp-1));

            addr_abs = 0xFFFE;
            int lo = read(addr_abs);
            int hi = read(Types.getUInt16(addr_abs + 1));
            pc = Types.getUInt16(hi << 8) | lo;

            cycles = 7;
        }
    }

    public void nmi() {
        write(0x0100 + stkp, Types.getUInt8((short) ((pc >> 8) & 0x00FF)));
        stkp = Types.getUInt8((short) (stkp-1));
        write(0x0100 + stkp, Types.getUInt8((short) (pc & 0x00FF)));
        stkp = Types.getUInt8((short) (stkp-1));

        SetFlag(B, false);
        SetFlag(U, true);
        SetFlag(I, true);
        write(0x0100 + stkp, status);
        stkp = Types.getUInt8((short) (stkp-1));

        addr_abs = 0xFFFA;
        int lo = read(addr_abs);
        int hi = read(Types.getUInt16(addr_abs + 1));
        pc = Types.getUInt16(hi << 8) | lo;

        cycles = 8;
    }

    public short fetch() {
        if(!lookup.get(opcode).addrmode.equals((Instruction.ADDRMODE_fn) imp)) fetched = read(addr_abs);
        return fetched;
    }

    public short fetched = 0x00;

    public int addr_abs = 0x0000;
    public int addr_rel = 0x00;
    public short opcode = 0x00;
    public short cycles = 0;

    private Bus bus;

    private short read(int a) {
        return bus.cpuRead(a);
    }

    private void write(int a, short d) {
        bus.cpuWrite(a, d);
    }

    public short GetFlag(short f) {
        return (short) ((Types.getUInt8((short) (status & f)) > 0) ? 1 : 0);
    }

    public void SetFlag(short f, boolean v) {
        if(v) status = Types.getUInt8((short) (status | f));
        else status = Types.getUInt8((short) (status & Types.getUInt8((short) ~f)));
    }

    public Vector<Instruction> lookup;



    public boolean complete() {
        return cycles == 0;
    }

    public Map<Integer, String> disassemble(int nStart, int nStop) {
        int addr = nStart;
        short value = 0x00, lo = 0x00, hi = 0x00;
        Map<Integer, String> mapLines = new LinkedHashMap<>();
        int line_addr;

        while (addr <= nStop) {
            line_addr = addr;

            String sInst = "$" + hex(addr, (short) 4) + ": ";

            short opcode = Types.getUInt8(bus.cpuRead(addr, true)); addr++;
            sInst += lookup.get(opcode).name + " ";

            Instruction.ADDRMODE_fn addrmode = lookup.get(opcode).addrmode;

            if(addrmode.equals((Instruction.ADDRMODE_fn) imp)) {
                sInst += " {IMP}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) imm)) {
                value = bus.cpuRead(addr, true); addr++;
                sInst += "#$" + hex(value, (short) 2) + " {IMM}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) zp0)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = 0x00;
                sInst += "$" + hex(lo, (short) 2) + " {ZP0}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) zpx)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = 0x00;
                sInst += "$" + hex(lo, (short) 2) + ", X {ZPX}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) zpy)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = 0x00;
                sInst += "$" + hex(lo, (short) 2) + ", Y {ZPY}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) izx)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = 0x00;
                sInst += "($" + hex(lo, (short) 2) + "), X {IZX}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) izy)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = 0x00;
                sInst += "($" + hex(lo, (short) 2) + "), Y {IZY}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) abs)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = bus.cpuRead(addr, true); addr++;
                sInst += "$" + hex((hi << 8) | lo, (short) 4) + " {ABS}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) abx)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = bus.cpuRead(addr, true); addr++;
                sInst += "$" + hex((hi << 8) | lo, (short) 4) + ", X {ABX}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) aby)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = bus.cpuRead(addr, true); addr++;
                sInst += "$" + hex((hi << 8) | lo, (short) 4) + ", Y {ABY}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) ind)) {
                lo = bus.cpuRead(addr, true); addr++;
                hi = bus.cpuRead(addr, true); addr++;
                sInst += "($" + hex((hi << 8) | lo, (short) 4) + ") {IND}";
            } else if(addrmode.equals((Instruction.ADDRMODE_fn) rel)) {
                value = bus.cpuRead(addr, true); addr++;
                sInst += "$" + hex(value, (short) 2) + " [$" + hex(addr + (byte)value, (short) 4) + "] {REL}";
            }

            mapLines.put(line_addr, sInst);
        }

        return mapLines;

    }

    public static String hex(long n, short d) {
        char[] chars = new char[d];
        Arrays.fill(chars, '0');
        for(int i = d - 1; i >= 0; i--, n >>= 4) chars[i] = "0123456789ABCDEF".charAt((int) (n & 0xF));
        return new String(chars);
    }

}
