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

package com.jnngl;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.GLWindow;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;

import static org.lwjgl.opengl.GL33.*;

import java.awt.*;

/**
 * Example OpenGL Application
 */
public class OpenGLApplication extends GLWindow {

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args) {
        ApplicationHandler.open(OpenGLApplication.class, args[0]);
    }

    /**
     * Constructor
     * @param os Operating System
     * @param path Absolute path to application folder
     */
    public OpenGLApplication(TotalOS os, String path) {
        super(os, "OpenGL", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    private int vao, program;

    /**
     * Render framebuffer using OpenGL
     */
    @Override
    protected void renderGL() {
        glClearColor(1, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);
        glUseProgram(program);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    /**
     * Update
     */
    @Override
    protected void updateGL() {
        renderCanvas();
    }

    /**
     * Initialization
     */
    @Override
    protected void onStart() {
        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f,  0.5f, 0.0f
        };

        vao = glGenVertexArrays();

        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        String vertexShaderSource = """
                #version 330 core
                layout (location = 0) in vec3 aPos;
                                
                void main()
                {
                    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
                }\0""";

        String fragmentShaderSource = """
                #version 330 core
                out vec4 FragColor;
                                
                void main()
                {
                    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
                }\0""";

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    /**
     * Called when app closes
     * @return true, this application always closes
     */
    @Override
    protected boolean onClose() {
        return true;
    }

    /**
     * Render overlay
     * @param unused Graphics2D instance
     */
    @Override
    public void render(Graphics2D unused) {}

    /**
     * Input
     * @param x x
     * @param y y
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }

}
