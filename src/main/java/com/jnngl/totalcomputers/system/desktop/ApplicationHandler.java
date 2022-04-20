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

    private static boolean checkApiLevel(Class<?> cls) {
        RequiresAPI api;
        TotalOS os = TotalOS.current();
        if((api = cls.getAnnotation(RequiresAPI.class)) != null) {
            if(api.apiLevel() > TotalOS.getApiVersion()) {
                os.information.displayMessage(Information.Type.ERROR,
                        "Application requires API "+api.apiLevel()+" version or above. Update the plugin to use this application.", () -> {});
                return false;
            }
        }
        return true;
    }

    public static void open(Class<? extends Application> cls, String path) {
        if(TotalOS.current() == null) return;
        if(!checkApiLevel(cls)) return;
        TotalOS dst = TotalOS.current();
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

    public static void open(Class<? extends Application> cls, String path, String[] args) {
        if(TotalOS.current() == null) return;
        if(!checkApiLevel(cls)) return;
        TotalOS dst = TotalOS.current();
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

    public static void addTaskBarEntry(String name, String link, BufferedImage icon) {
        desktop.get(TotalOS.current()).taskbar.addApplication(new TaskBarLink(TotalOS.current(), name, link, icon));
        desktop.get(TotalOS.current()).getOS().fs.addTaskBarEntry(name, link);
    }

    public static void removeTaskBarEntry(String name) {
        TaskBarLink toRemove = null;
        for(Application application : desktop.get(TotalOS.current()).taskbar.applications()) {
            if(application.name.equals(name) && application instanceof TaskBarLink) {
                toRemove = (TaskBarLink) application;
                break;
            }
        }
        if(toRemove == null) return;
        desktop.get(TotalOS.current()).taskbar.removeApplication(toRemove);
        desktop.get(TotalOS.current()).getOS().fs.removeTaskBarEntry(name);
    }

    public static void refreshDesktop() {
        desktop.get(TotalOS.current()).updateDesktop();
    }

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
