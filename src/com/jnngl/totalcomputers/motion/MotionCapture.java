package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;

@RequiresAPI(apiLevel = 3)
public interface MotionCapture {

    public MotionCapabilities getCapabilities();
    public boolean startCapture(MotionCaptureDesc desc, TotalOS os);
    public void forceStopCapture(TotalOS os);
    public boolean stopCapture(TotalOS os);
    public boolean isCapturing(TotalOS os);

}
