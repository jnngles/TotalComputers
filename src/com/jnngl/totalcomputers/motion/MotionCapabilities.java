package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;

@RequiresAPI(apiLevel = 3)
public class MotionCapabilities {

    private final boolean supportsMovementCapture;
    private final boolean supportsGazeDirectionCapture;
    private final boolean supportsJumpCapture;
    private final boolean supportsShiftCapture;

    public MotionCapabilities(boolean movement, boolean gaze, boolean jump, boolean shift) {
        this.supportsMovementCapture = movement;
        this.supportsGazeDirectionCapture = gaze;
        this.supportsJumpCapture = jump;
        this.supportsShiftCapture = shift;
    }

    public boolean supportsMovementCapture() {
        return supportsMovementCapture;
    }

    public boolean supportsGaveDirectionCapture() {
        return supportsGazeDirectionCapture;
    }

    public boolean supportsJumpCapture() {
        return supportsJumpCapture;
    }

    public boolean supportsShiftCapture() {
        return supportsShiftCapture;
    }

}
