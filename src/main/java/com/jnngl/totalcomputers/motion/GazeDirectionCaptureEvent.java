package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;

@RequiresAPI(apiLevel = 3)
public interface GazeDirectionCaptureEvent {

    public void gazeDirectionChanged(double dx, double dz);

}
