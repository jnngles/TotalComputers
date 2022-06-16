package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.TotalOS;

public class DummyControl implements MotionCapture {

    @Override
    public MotionCapabilities getCapabilities() {
        return new MotionCapabilities(false, false, false, false, false, false);
    }

    @Override
    public boolean startCapture(MotionCaptureDesc desc, TotalOS os) {
        return false;
    }

    @Override
    public void forceStopCapture(TotalOS os) {}

    @Override
    public boolean stopCapture(TotalOS os) {
        return false;
    }

    @Override
    public boolean isCapturing(TotalOS os) {
        return false;
    }
}
