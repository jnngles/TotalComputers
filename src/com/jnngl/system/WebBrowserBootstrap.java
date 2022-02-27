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
