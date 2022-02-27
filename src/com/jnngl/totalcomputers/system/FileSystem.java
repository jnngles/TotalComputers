/*
    Computers are now in minecraft!
    Copyright (C) 2021  JNNGL

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

package com.jnngl.totalcomputers.system;

import com.jnngl.system.NativeWindowApplication;
import com.jnngl.totalcomputers.system.desktop.TaskBarLink;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Virtual File System for TotalOS
 */
public class FileSystem {

    /** /                 */ private final File rootfs;
    /** /sys              */ private final File   sys;
    /** /sys/wallpaper    */ private final File       wallpaper;
    /** /sys/taskbar      */ private final File       taskbar;
    /** /sys/name         */ private final File       name;
    /** /sys/locale       */ private final File       locale;
    /** /sys/account      */ private final File       account;
    /** /sys/associations */ private final File       associations;
    /** /usr              */ private final File   usr;
    /** /usr/Desktop      */ private final File       Desktop;


    /**
     * Whether it is a first launch or not
     */
    public final boolean firstRun;

    /**
     * Image resources
     */
    public final Map<String, BufferedImage> images;

    /**
     * Associations
     */
    public final Map<String, String> associationMap;

    /**
     * Constructor
     * @param name Name of the computer
     */
    public FileSystem(String name) {
        rootfs = new File("rootfs/"+name);
        rootfs.mkdirs();

        try {
            String libName = "native.dll";
            URL url = FileSystem.class.getResource("/" + libName);
            File tmpDir = Files.createTempDirectory("libs").toFile();
            tmpDir.deleteOnExit();
            File nativeLibTmpFile = new File(tmpDir, libName);
            nativeLibTmpFile.deleteOnExit();
            try (InputStream in = url.openStream()) {
                Files.copy(in, nativeLibTmpFile.toPath());
            }
            System.load(nativeLibTmpFile.getAbsolutePath());
        } catch (Exception e) {
            System.loadLibrary("native");
        }

        images = new HashMap<>();
        associationMap = new HashMap<>();

        File initFlag = new File(rootfs.getPath()+"/init.flag");
        usr = new File(rootfs.getPath()+"/usr");
        Desktop = new File(usr.getPath()+"/Desktop");
        sys = new File(rootfs.getPath()+"/sys");
        taskbar = new File(sys.getPath()+"/taskbar");
        this.name = new File(sys.getPath()+"/name");
        locale = new File(sys.getPath()+"/locale");
        account = new File(sys.getPath()+"/account");
        wallpaper = new File(sys.getPath()+"/wallpaper");
        associations = new File(sys.getPath()+"/associations");
        if(!initFlag.exists()) {
            firstRun = true;
            try {
                usr.mkdirs();
                Desktop.mkdirs();
                sys.mkdirs();
                taskbar.createNewFile();
                this.name.createNewFile();
                associations.createNewFile();
                Files.writeString(this.name.toPath(), name);
                Files.writeString(this.taskbar.toPath(), "Files\n!/sys/bin/Files.app\nApp Store\n!/sys/bin/AppStore.app\n");
                Files.writeString(this.wallpaper.toPath(), "/res/system/wallpaper.png");
                Files.writeString(this.associations.toPath(), "png,jpg: /sys/bin/ImageViewer.app");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else firstRun = false;
    }

    /**
     * Writes <code>init.flag</code> file
     */
    public void setConfigured() {
        try {
            new File(rootfs.getPath()+"/init.flag").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the localization info
     * @param locale Localization
     */
    public void writeLocalization(Localization locale) {
        try {
            if(locale instanceof Localization.English) Files.writeString(this.locale.toPath(), "en-US$eof");
            else if(locale instanceof Localization.Russian) Files.writeString(this.locale.toPath(), "ru-RU$eof");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the localization info
     * @return Localization
     */
    public Localization readLocalization() {
        try {
            String contents = Files.readString(locale.toPath());
            String locale = contents.split("\\$")[0];
            if(locale.equals("en-US")) return new Localization.English();
            if(locale.equals("ru-RU")) return new Localization.Russian();
            System.out.print("Failed to load localization. English locale will be used.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Localization.English();
    }

    /**
     * Saves the account info
     * @param account Account
     */
    public void writeAccount(Account account) {
        try {
            Files.writeString(this.account.toPath(), account.name+"#"+account.passwordHash+"#"+account.usePassword+"#eof");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the account info
     * @return Account
     */
    public Account readAccount() {
        try {
            String contents = Files.readString(account.toPath());
            String[] parts = contents.split("#");
            return new Account(parts[0], parts[1], Boolean.parseBoolean(parts[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeWallpaper(String path) {
        try {
            Files.writeString(this.wallpaper.toPath(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all resources
     */
    public void loadResources() {
        addResourceImage("error", loadImage("res/system/error.png"));
        addResourceImage("warning", loadImage("res/system/warning.png"));
        addResourceImage("done", loadImage("res/system/done.png"));
        addResourceImage("default-icon", loadImage("res/system/default_icon.png"));
        loadAssociations();
    }

    /**
     * Puts the resource into the map
     * @param name Name of the resource
     * @param image Data
     * @return Whether the resource was successfully added or not
     */
    public boolean addResourceImage(String name, BufferedImage image) {
        if(images.containsKey(name)) return false;
        images.put(name, image);
        return true;
    }

    /**
     * Returns the resource.
     * <p>
     * Null will be returned if there is no resource with that name
     * </p>
     * @param key Key of the resource
     * @return The resource
     */
    public BufferedImage getResourceImage(String key) {
        return images.getOrDefault(key, null);
    }

    /**
     * Loads image
     * @param path Path of the file
     * @return BufferedImage
     */
    public BufferedImage loadImage(String path) {
        BufferedImage result;
        String _path;
        if(path.startsWith("/")) _path = path.substring(1);
        else _path = path;
        InputStream is = this.getClass().getResourceAsStream("/"+_path);
        try {
            if(is == null) throw new IOException();
            result = ImageIO.read(is);
        } catch (IOException e) {
            try {
                result = ImageIO.read(new File(rootfs.getPath()+"/"+_path));
            } catch (IOException ignored) {
                try {
                    result = ImageIO.read(new File(_path));
                } catch (IOException ex) {
                    System.err.println("Failed to load image.");
                    return null;
                }
            }
        }
        return result;
    }

    public void launchApplication(String Rpath, String className, String... args) {
        URL url;
        String _path;
        if(Rpath.startsWith("/")) _path = Rpath.substring(1);
        else _path = Rpath;
        String path = new File(_path).getAbsolutePath();
        if(className.contains(":")) {
            try {
                String[] parts = className.split(":");
                String appName = parts[0].trim();

                String[] nArgs = new String[args.length+1];
                nArgs[0] = path;
                System.arraycopy(args, 0, nArgs, 1, args.length);

                if(!(new File(root()+"/usr/Applications/"+appName+".app/application.jar").exists())) {
                    NativeWindowApplication.main(nArgs);
                    return;
                }

                String clsName = parts[1].trim();

                URLClassLoader child = new URLClassLoader(
                        new URL[] { new File(root()+"/usr/Applications/"+appName+".app/application.jar").toURI().toURL() },
                        this.getClass().getClassLoader()
                );
                Class<?> cls = Class.forName(clsName, true, child);
                Method main = cls.getMethod("main", String[].class);
                main.invoke(null, (Object) nArgs);
            } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException | MalformedURLException e) {
                System.err.println("Failed to launch application.");
            }
            return;
        }
        try { // Try to load from jar
            URLClassLoader child = new URLClassLoader(
                    new URL[] { new URL("jar:"+FileSystem.class.getProtectionDomain().getCodeSource().getLocation()+"!/"+_path) },
                    this.getClass().getClassLoader()
            );
            Class<?> cls = Class.forName(className, true, child);
            Method main = cls.getMethod("main", String[].class);
            String[] nArgs = new String[args.length+1];
            nArgs[0] = path;
            System.arraycopy(args, 0, nArgs, 1, args.length);
            main.invoke(null, (Object) nArgs);
            return;
        } catch (IOException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            try { // Try to load from file system
                File file = new File(rootfs.getPath()+"/"+_path);
                if(!file.exists()) throw new IOException();
                url = file.toURI().toURL();
            } catch (IOException ignored) {
                try { // Try to load from relative path
                    File file = new File(_path);
                    if(!file.exists()) throw new IOException();
                    url = file.toURI().toURL();
                } catch (IOException ex) { // Failed to load =(
                    System.err.println("Failed to launch application.");
                    return;
                }
            }
        }
        ClassLoader loader = new URLClassLoader(new URL[]{ url });
        try {
            Class<?> cls = loader.loadClass(className);
            Method main = cls.getMethod("main", String[].class);
            String[] nArgs = new String[args.length+1];
            nArgs[0] = path;
            System.arraycopy(args, 0, nArgs, 1, args.length);
            main.invoke(null, (Object) nArgs);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public List<TaskBarLink> loadTaskBarLinks(TotalOS os) {
        try {
            String data = Files.readString(taskbar.toPath());
            String[] lines = data.split("\n");
            String name = null;
            String path = null;
            List<TaskBarLink> links = new ArrayList<>();
            for(String line : lines) {
                if(line.startsWith("!")) path = line.substring(1);
                else name = line;
                if(name != null && path != null) {
                    BufferedImage maybeIcon = loadImage(path+"/icon.png");
                    links.add(new TaskBarLink(os, name, path, maybeIcon == null? getResourceImage("default-icon") : maybeIcon));
                    name = null;
                    path = null;
                }
            }
            return links;
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void launchFromApplication(String path, String... args) {
        launchApplication(path, readFile(path + "/application"), args);
    }

    public String readFile(String path) {
        String _path;
        if(path.startsWith("/")) _path = path.substring(1);
        else _path = path;
        InputStream inputStream;
        try {
            inputStream = this.getClass().getResourceAsStream("/"+_path);
            if(inputStream == null) throw new IOException();
        } catch (IOException ignored) {
            try {
                inputStream = new FileInputStream(rootfs.getPath() + "/"+_path);
            } catch(FileNotFoundException ex) {
                try {
                    inputStream = new FileInputStream(_path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        try {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File toFile(String path) {
        String _path;
        if(path.startsWith("/")) _path = path.substring(1);
        else _path = path;
        File file;
        try {
            file = new File(getClass().getResource("/"+_path).toURI());
            if(!file.exists()) throw new Exception();
        } catch (Exception e) {
            file = new File(rootfs.getPath()+"/"+_path);
            if(!file.exists()) {
                file = new File(_path);
                if(!file.exists()) {
                    new IOException("Failed to find file.").printStackTrace();
                    return null;
                }
            }
        }
        return file;
    }

    public String getAssociatedProgram(String ext) {
        return associationMap.get(ext);
    }

    public void loadAssociations() {
        String contents = readFile(associations.getPath());
        String[] lines = contents.split("\n");

        associationMap.clear();

        for(String line : lines) {
            String[] association = line.split(":");
            String fExtensions = association[0].trim();
            String program = association[1].trim();
            String[] extensions = fExtensions.split(",");
            for(String extension : extensions) {
                associationMap.put(extension.trim(), program);
            }
        }
    }

    public String root() {
        return rootfs.getPath();
    }

    public void addTaskBarEntry(String name, String app) {
        try {
            Files.writeString(taskbar.toPath(), Files.readString(taskbar.toPath()) + name+"\n!" + app + "\n");
        } catch (IOException e) {
            System.err.println("Failed to access system files.");
        }
    }

    public void removeTaskBarEntry(String name) {
        try {
            String contents = Files.readString(taskbar.toPath());
            if(!contents.contains(name)) return;
            String path = contents.split(name+"\n")[1].split("\n")[0];
            Files.writeString(taskbar.toPath(), contents.replace(name+"\n"+path+"\n", ""));
        } catch (IOException e) {
            System.err.println("Failed to access system files.");
        }
    }
}
