package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;

@RequiresAPI(apiLevel = 3)
public class MotionCaptureDesc {

    private MovementCaptureEvent requiresMovementCapture = null;
    private GazeDirectionCaptureEvent requiresGazeDirectionCapture = null;
    private JumpCaptureEvent requiresJumpCapture = null;
    private SneakCaptureEvent requiresSneakCapture = null;

    private MotionCaptureDesc() {}

    public static MotionCaptureDesc create() {
        return new MotionCaptureDesc();
    }

    public MotionCaptureDesc requiresMovementCapture(MovementCaptureEvent event) {
        requiresMovementCapture = event;
        return this;
    }

    public MotionCaptureDesc requiresGazeDirectionCapture(GazeDirectionCaptureEvent event) {
        requiresGazeDirectionCapture = event;
        return this;
    }

    public MotionCaptureDesc requiresJumpCapture(JumpCaptureEvent event) {
        requiresJumpCapture = event;
        return this;
    }

    public MotionCaptureDesc requiresSneakCapture(SneakCaptureEvent event) {
        requiresSneakCapture = event;
        return this;
    }

    public boolean requiresMovementCapture() {
        return requiresMovementCapture != null;
    }

    public boolean requiresGazeDirectionCapture() {
        return requiresGazeDirectionCapture != null;
    }

    public boolean requiresJumpCapture() {
        return requiresJumpCapture != null;
    }

    public boolean requiresSneakCapture() {
        return requiresSneakCapture != null;
    }

    public MovementCaptureEvent movementEvent() {
        return requiresMovementCapture;
    }

    public GazeDirectionCaptureEvent gazeDirectionEvent() {
        return requiresGazeDirectionCapture;
    }

    public JumpCaptureEvent jumpEvent() {
        return requiresJumpCapture;
    }

    public SneakCaptureEvent sneakEvent() {
        return requiresSneakCapture;
    }

}
