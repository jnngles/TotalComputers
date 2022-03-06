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

package com.jnngl.system;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WebBrowserBootstrap {

    public static void main(String[] args) throws IOException {
        System.out.println("Loading libraries...");
        String path = new File(args[0]).getAbsolutePath();
        System.load(path+"/chrome_elf.dll");
        System.load(path+"/d3dcompiler_47.dll");
        System.load(path+"/libcef.dll");
        System.load(path+"/libEGL.dll");
        System.load(path+"/libGLESv2.dll");
        System.load(path+"/vk_swiftshader.dll");
        System.load(path+"/vulkan-1.dll");
        System.load(path+"/swiftshader/libEGL.dll");
        System.load(path+"/swiftshader/libGLESv2.dll");
        NativeWindowApplication.main(args);
    }

    public static void renderArray(Graphics2D g, int[] pixels, int viewportHeight, int width, int height) {
        if(pixels.length < width*viewportHeight) return;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < viewportHeight; y++) {
                g.setColor(new Color(pixels[y*width+x]));
                g.fillRect(x, y, 1, 1);
            }
        }
    }

}
