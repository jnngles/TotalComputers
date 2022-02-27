package com.jnngl.totalcomputers.system.desktop;

import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.states.Desktop;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ApplicationHandler {

    private static Desktop desktop;

    public static void init(Desktop desktop) {
        ApplicationHandler.desktop = desktop;
    }

    public static void registerApplication(Application application) {
        if(desktop == null) return;
        desktop.taskbar.addApplication(application);
        if(application instanceof WindowApplication) desktop.drawable.add((WindowApplication) application);
    }

    public static void unregisterApplication(Application application) {
        if(desktop == null) return;
        desktop.taskbar.removeApplication(application);
        if(application instanceof WindowApplication) desktop.drawable.remove((WindowApplication) application);
    }

    public static Application open(Class<? extends Application> cls, String path) {
        try {
            Constructor<? extends Application> constructor = cls.getConstructor(TotalOS.class, String.class);
            return constructor.newInstance(desktop.getOS(), path);
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor '"+cls.getSimpleName()+"(TotalOS, String)' not found.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("Failed to create new instance. ("+e.getClass().getSimpleName()+")");
        }
        return null;
    }

    public static Application open(Class<? extends Application> cls, String path, String[] args) {
        try {
            Constructor<? extends Application> constructor = cls.getConstructor(TotalOS.class, String.class, String[].class);
            return constructor.newInstance(desktop.getOS(), path, args);
        } catch (NoSuchMethodException e) {
            System.err.println("Constructor '"+cls.getSimpleName()+"(TotalOS, String)' not found.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            System.err.println("Failed to create new instance.");
        }
        return null;
    }

    public static void addTaskBarEntry(String name, String app, BufferedImage icon) {
        desktop.taskbar.addApplication(new TaskBarLink(desktop.getOS(), name, app, icon));
        desktop.getOS().fs.addTaskBarEntry(name, app);
    }

    public static void removeTaskBarEntry(String name) {
        TaskBarLink toRemove = null;
        for(Application application : desktop.taskbar.applications()) {
            if(application.name.equals(name) && application instanceof TaskBarLink) {
                toRemove = (TaskBarLink) application;
                break;
            }
        }
        if(toRemove == null) return;
        desktop.taskbar.removeApplication(toRemove);
        desktop.getOS().fs.removeTaskBarEntry(name);
    }
}
