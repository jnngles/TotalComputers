/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jnngl.applications.nes;

public class Types {

    public static short getUInt8(short s) {
        return (short) (s & 0x00FF);
    }

    public static int getUInt16(int i) {
        return i & 0x0000FFFF;
    }

    public static long getUInt32(long l) {
        return l & 0x00000000ffffffff;
    }

}
