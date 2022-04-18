package com.jnngl.totalcomputers.motion;

import com.jnngl.totalcomputers.system.RequiresAPI;

@RequiresAPI(apiLevel = 4)
public interface ItemDropEvent {

    public void itemDrop();
    public void stackDrop();

}
