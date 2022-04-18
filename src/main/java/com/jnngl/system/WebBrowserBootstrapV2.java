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

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class WebBrowserBootstrapV2 extends WindowApplication {

    private ByteBuffer nativeHandle;

    public WebBrowserBootstrapV2(TotalOS os, String path) {
        super(os, "Browser", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    public static void main(String[] args) throws IOException {
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
        System.load(path+"/cef2java.dll");
        ApplicationHandler.open(WebBrowserBootstrapV2.class, path);
    }

    private static native boolean InitCEF(String path);
    private static native ByteBuffer CreateBrowser(int width, int height);
    private static native void Update(ByteBuffer browser, int width, int height);
    private static native int[] GetPixels(ByteBuffer browser);
    private static native void ProcessTouch(ByteBuffer browser, int x, int y, boolean type);
    private static native void RunLoop();

    @Override
    protected void onStart() {
        InitCEF(applicationPath);
        nativeHandle = CreateBrowser(getWidth(), getHeight());
        new Thread(WebBrowserBootstrapV2::RunLoop).start();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    public void update() {
        Update(nativeHandle, getWidth(), getHeight());
        renderCanvas();
    }

    @Override
    public void render(Graphics2D g) {
        int[] pixels = GetPixels(nativeHandle);
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                int index = y*getWidth()+x;
                if(index >= pixels.length) return;
                g.setColor(new Color(pixels[index]));
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        ProcessTouch(nativeHandle, x, y, type == TotalComputers.InputInfo.InteractType.RIGHT_CLICK);
    }
}
