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

public class NativeWindowApplication extends WindowApplication {

    private static long currentUID = 0;

    private final long uid;

    public native void _Init(long uid, String path, String[] args);
    public native boolean _OnClose(long uid);
    public native void _Update(long uid);
    public native void _Render(long uid, Graphics2D g);
    public native void _ProcessInput(long uid, int x, int y, boolean type);

    public static void main(String[] args) {
        System.load(new File(args[0]).getAbsolutePath()+"/application.dll");
        ApplicationHandler.open(NativeWindowApplication.class, new File(args[0]).getAbsolutePath(), args);
    }

    public NativeWindowApplication(TotalOS os, String path, String[] args) {
        super(path, os, "Unknown Native Application", 0, 0, 100, 100);
        uid = currentUID++;
        ApplicationHandler.registerApplication(this);
        _Init(uid, path, args);
        renderCanvas();
    }

    boolean ExtOnClose() {
        return _OnClose(uid);
    }

    void ExtUpdate() {
        _Update(uid);
    }

    void ExtRender(Graphics2D g) {
        _Render(uid, g);
    }

    void ExtProcessInput(int x, int y, boolean type) {
        _ProcessInput(uid, x, y, type);
    }

    @Override
    protected void onStart() {}

    @Override
    protected boolean onClose() {
        return ExtOnClose();
    }

    @Override
    public void update() {
        ExtUpdate();
    }

    @Override
    protected void render(Graphics2D g) {
        ExtRender(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        ExtProcessInput(x, y, type == TotalComputers.InputInfo.InteractType.RIGHT_CLICK);
    }

}
