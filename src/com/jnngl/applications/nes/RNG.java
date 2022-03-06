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

import java.util.Random;


public class RNG {

    private static Random rng;

    public static int rand() {
        if(rng == null) rng = new Random();
        return rng.nextInt();
    }

    public static boolean randBool() {
        if(rng == null) rng = new Random();
        return rng.nextBoolean();
    }

}
