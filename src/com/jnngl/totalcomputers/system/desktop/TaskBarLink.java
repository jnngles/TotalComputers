package com.jnngl.totalcomputers.system.desktop;
import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.image.BufferedImage;

public class TaskBarLink extends Application {

    private final String path;

    public TaskBarLink(TotalOS os, String name, String path, BufferedImage icon) {
        super(os, name);
        this.path = path;
        setIcon(icon);
    }

    @Override
    protected void onStart() {
        os.fs.launchFromApplication(path);
    }

    @Override
    protected boolean onClose() {
        return true;
    }

}
