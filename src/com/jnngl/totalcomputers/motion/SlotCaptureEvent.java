package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;

@RequiresAPI(apiLevel = 4)
public interface SlotCaptureEvent {

    public void slotLeft();
    public void slotRight();

}
