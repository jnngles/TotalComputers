/*
    Plugin for computers in vanilla minecraft!
    Copyright (C) 2022  JNNGL

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.jnngl.totalcomputers.system.desktop;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.RequiresAPI;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.states.Desktop;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationHandler {

    private static Map<TotalOS, Desktop> desktop = new ConcurrentHashMap<>();

    public static void init(Desktop desktop) {
        ApplicationHandler.desktop.put(desktop.getOS(), desktop);
    }

    public static void registerApplication(Application application) {
        if(!desktop.containsKey(application.os)) return;
        Desktop d = desktop.get(application.os);
        d.taskbar.addApplication(application);
        if(application instanceof WindowApplication) {
            d.drawable.add((WindowApplication) application);
        }
    }

    public static void unregisterApplication(Application application) {
        if(desktop == null) return;
        Desktop d = desktop.get(application.os);
        d.taskbar.removeApplication(application);
        if(application instanceof WindowApplication) d.drawable.remove((WindowApplication) application);
    }

    private static boolean checkApiLevel(TotalOS os, Class<?> cls) {
        RequiresAPI api;
        if((api = cls.getAnnotation(RequiresAPI.class)) != null) {
            if(api.apiLevel() > TotalOS.getApiVersion()) {
                os.information.displayMessage(Information.Type.ERROR,
                        "Application requires API "+api.apiLevel()+" version or above. Update the plugin to use this application.", () -> {});
                return false;
            }
        }
        return true;
    }

    public static void open(TotalOS dst, Class<? extends Application> cls, String path) {
        if(dst == null) return;
        if(!checkApiLevel(dst, cls)) return;
        dst.runInSystemThread(() -> {
            try {
                Constructor<? extends Application> constructor = cls.getConstructor(TotalOS.class, String.class);
                constructor.newInstance(dst, path);
            } catch (NoSuchMethodException e) {
                System.err.println("Constructor '"+cls.getSimpleName()+"(TotalOS, String)' not found.");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                System.err.println("Failed to create new instance. ("+e.getClass().getSimpleName()+")");
            }
        });
    }

    public static void open(Class<? extends Application> cls, String path) {
        open(TotalOS.current(), cls, path);
    }

    public static void open(TotalOS dst, Class<? extends Application> cls, String path, String[] args) {
        if(dst == null) return;
        if(!checkApiLevel(dst, cls)) return;
        dst.runInSystemThread(() -> {
            try {
                Constructor<? extends Application> constructor = cls.getConstructor(TotalOS.class, String.class, String[].class);
                constructor.newInstance(dst, path, args);
            } catch (NoSuchMethodException e) {
                System.err.println("Constructor '"+cls.getSimpleName()+"(TotalOS, String)' not found.");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                System.err.println("Failed to create new instance.");
            }
        });
    }

    public static void open(Class<? extends Application> cls, String path, String[] args) {
        open(TotalOS.current(), cls, path, args);
    }

    public static void addTaskBarEntry(TotalOS os, String name, String link, BufferedImage icon) {
        desktop.get(os).taskbar.addApplication(new TaskBarLink(os, name, link, icon));
        os.fs.addTaskBarEntry(name, link);
    }

    public static void addTaskBarEntry(String name, String link, BufferedImage icon) {
        addTaskBarEntry(TotalOS.current(), name, link, icon);
    }

    public static void removeTaskBarEntry(TotalOS os, String name) {
        TaskBarLink toRemove = null;
        for(Application application : desktop.get(os).taskbar.applications()) {
            if(application.name.equals(name) && application instanceof TaskBarLink) {
                toRemove = (TaskBarLink) application;
                break;
            }
        }
        if(toRemove == null) return;
        desktop.get(os).taskbar.removeApplication(toRemove);
        os.fs.removeTaskBarEntry(name);
    }

    public static void removeTaskBarEntry(String name) { removeTaskBarEntry(TotalOS.current(), name); }

    public static void refreshDesktop(TotalOS os) {
        desktop.get(os).updateDesktop();
    }

    public static void refreshDesktop() { refreshDesktop(TotalOS.current()); }

    @RequiresAPI(apiLevel = 5)
    public static WindowApplication[] getApplicationsForClass(TotalOS os, Class<? extends WindowApplication> cls) {
        List<WindowApplication> applications = new ArrayList<>();
        for(WindowApplication app : desktop.get(os).drawable) {
            if(app.getClass().isInstance(cls)) {
                applications.add(app);
            }
        }
        return applications.toArray(new WindowApplication[0]);
    }

}
