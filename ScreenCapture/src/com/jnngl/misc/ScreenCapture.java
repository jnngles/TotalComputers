package com.jnngl.misc;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Information;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Vector;

@RequiresAPI(apiLevel = 8)
public class ScreenCapture extends WindowApplication {

    public static void main(String[] args) {
        ApplicationHandler.open(ScreenCapture.class, args[0]);
    }

    public SC_AppState state;

    /*
        HWND -> HANDLE -> PVOID -> void* --> ByteBuffer
                                         \-> long (pointer)
     */

    public native ByteBuffer[] win32applications(); // HWND win32applications();
    public native ByteBuffer win32screenScreenshot(int[] width, int[] height); // void* win32screenScreenshot(int* width, int* height);
    public native ByteBuffer win32appScreenshot(ByteBuffer hwnd, int[] width, int[] height); // void* win32appScreenshot(HWND hwnd, int* width, int* height);
    public native String win32appName(ByteBuffer hwnd); // const char* win32appName(HWND hwnd);

    public ScreenCapture(TotalOS os, String path) {
        super(os, "Screen Capture", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        if(!os.isClientbound()) {
            os.information.displayMessage(Information.Type.ERROR,
                    "This application does not support serverbound computers", this::close);
            return;
        }
        if(!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            os.information.displayMessage(Information.Type.ERROR,
                    "Your OS ("+System.getProperty("os.name")+") is not supported yet!", this::close);
            return;
        }

        try {
            File tmp = File.createTempFile("sc_native_win32", ".dll");
            Files.delete(tmp.toPath());
            Files.copy(new File(applicationPath, "native_win32.dll").toPath(), tmp.toPath());
            System.load(tmp.getAbsolutePath());
            tmp.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        state = new SC_SelectState(this);

        addResizeEvent(new ResizeEvent() {
            @Override
            public void onResize(int width, int height) {
                state.onResize();
            }

            @Override
            public void onMaximize(int width, int height) {
                state.onResize();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                state.onResize();
            }
        });
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        state.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        state.processInput(x, y, type);
    }

    public TotalOS os() {
        return os;
    }

}
