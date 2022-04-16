package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.motion.MotionCaptureDesc;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;

import java.awt.*;

public class MotionTest extends WindowApplication {

    private double x = 0, y = 0;
    private Color color = Color.WHITE;

    public static void main(String[] args) {
        ApplicationHandler.open(MotionTest.class, args[0]);
    }

    public MotionTest(TotalOS os, String path) {
        super(os, "Motion Test", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {

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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);
        g.fillRect((int)x, (int)y, 20, 20);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(os.motionCapture.isCapturing(os)) os.motionCapture.stopCapture(os);
        else os.motionCapture.startCapture(MotionCaptureDesc
                .create()
                .requiresJumpCapture(() -> { this.x=0; this.y=0; })
                .requiresMovementCapture((dx, dy) -> {this.x+=dx; this.y-=dy;})
//                .requiresSneakCapture(new SneakCaptureEvent() {
//                    @Override
//                    public void onShiftPressed() {
//                        color = Color.RED;
//                    }
//
//                    @Override
//                    public void onShiftReleased() {
//                        color = Color.WHITE;
//                    }
//                })
//                .requiresGazeDirectionCapture((dx, dy) -> {this.x+=dx*20; this.y-=dy*20;})
                , os);
    }
}
