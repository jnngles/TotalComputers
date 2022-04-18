package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;

@RequiresAPI(apiLevel = 3)
public interface MovementCaptureEvent {

    public void onMove(double dx, double dz);

}
