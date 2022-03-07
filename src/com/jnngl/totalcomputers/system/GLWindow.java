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

package com.jnngl.totalcomputers.system;

import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public abstract class GLWindow extends WindowApplication {

    private long handle;

    private int[] pbo;
    private int viewportW, viewportH;
    private BufferedImage glCanvas;

    private int dma=0, read=1;

    public GLWindow(TotalOS os, String title, int width, int height, String path) {
        super(path, os, title, os.screenWidth/2-width/2, os.screenHeight/2-height/2, width, height);
        init(width, height, title);
        start();
        renderCanvas();
    }

    public GLWindow(TotalOS os, String title, int x, int y, int width, int height, String path) {
        super(path, os, title, x, y, width, height);
        init(width, height, title);
        start();
        renderCanvas();
    }

    private void init(int w, int h, String title) {
        try {
            GLFW.glfwInit();
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
            handle = GLFW.glfwCreateWindow(w, h, "TotalComputers: " + title, 0, 0);
            GLFW.glfwMakeContextCurrent(handle);
            GL.createCapabilities();
            createBuffer(w, h);
        } catch(Exception e) {
            System.err.println("Something went wrong! ("+e.getClass().getSimpleName()+")");
        }
    }

    protected void createBuffer(int w, int h) {
        glViewport(0, 0, w, h);

        pbo = new int[2];

        glGenBuffers(pbo);
        glBindBuffer(GL_PIXEL_PACK_BUFFER, pbo[0]);
        glBufferData(GL_PIXEL_PACK_BUFFER, (long)w*h*4, GL_STREAM_READ);

        glBindBuffer(GL_PIXEL_PACK_BUFFER, pbo[1]);
        glBufferData(GL_PIXEL_PACK_BUFFER, (long)w*h*4, GL_STREAM_READ);
        glBindBuffer(GL_PIXEL_PACK_BUFFER, 0);

        viewportW = w;
        viewportH = h;

        glCanvas = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public boolean close() {
        if(super.close()) {
            GLFW.glfwDestroyWindow(handle);
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        updateGL();
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    @Override
    public void renderCanvas() {
        renderGL();

        glBindBuffer(GL_PIXEL_PACK_BUFFER, pbo[dma]);
        glReadPixels(0, 0, viewportW, viewportH, GL_BGRA, GL_UNSIGNED_BYTE, 0);

        glBindBuffer(GL_PIXEL_PACK_BUFFER, pbo[read]);
        IntBuffer buffer = glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_ONLY, null).asIntBuffer();

        Graphics2D graphics = canvas.createGraphics();

        WritableRaster wr = glCanvas.getRaster();
        DataBufferInt db = (DataBufferInt) wr.getDataBuffer();

        int[] cpuArray = db.getData();

        buffer.clear();
        int width = wr.getWidth();
        int height = wr.getHeight();

        for (int y = height - 1; y >= 0; y--)
        {
            buffer.get(cpuArray, y * width, width);
        }
        glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
        glBindBuffer(GL_PIXEL_PACK_BUFFER, 0);

        graphics.drawImage(glCanvas, 0, 0, getWidth(), getHeight(), null);
        dma = read;
        read = 1-dma;
        render(graphics);
        graphics.dispose();
    }

    protected abstract void renderGL();
    protected abstract void updateGL();
}
