package com.jnngl;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.GLWindow;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;

import static org.lwjgl.opengl.GL33.*;

import java.awt.*;

public class OpenGLApplication extends GLWindow {

    public static void main(String[] args) {
        ApplicationHandler.open(OpenGLApplication.class, args[0]);
    }

    public OpenGLApplication(TotalOS os, String path) {
        super(os, "OpenGL", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    private int vao, program;

    @Override
    protected void renderGL() {
        glClearColor(1, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);
        glUseProgram(program);
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }

    @Override
    protected void updateGL() {
        renderCanvas();
    }

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

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void render(Graphics2D unused) {}

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }

}
