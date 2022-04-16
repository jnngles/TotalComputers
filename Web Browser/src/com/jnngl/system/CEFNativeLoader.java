package com.jnngl.system;

import com.jnngl.totalcomputers.system.Web;
import org.cef.OS;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CEFNativeLoader {

    public static File resolveLibpath() {
        File path = new File(System.getProperty("user.home"), ".tcmp_cef");
        path.mkdirs();
        return path.getAbsoluteFile();
    }

    public static void load(File path) {
        String[] toLoad;
        if(OS.isWindows()) {
            toLoad = new String[] {
                    "swiftshader/libEGL.dll",
                    "swiftshader/libGLESv2.dll",
                    "d3dcompiler_47.dll",
                    "libEGL.dll", "libGLESv2.dll",
                    "vk_swiftshader.dll",
                    "vulkan-1.dll",
                    "chrome_elf.dll",
                    "libcef.dll",
                    "application.dll"
            };
        } else {
            toLoad = new String[] {
                    "swiftshader/libEGL.so",
                    "swiftshader/libGLESv2.so",
                    "libEGL.so", "libGLESv2.so",
                    "libvk_swiftshader.so",
                    "libvulkan.so.1",
                    "libcef.so",
                    "libapplication.so"
            };
        }

        for(String lib : toLoad)
            System.load(new File(path, lib).getAbsoluteFile().getAbsolutePath());
    }

    public static void download(File path) {
        File flag = new File(path, ".init");
        if(!flag.exists())
            forceDownload(path);
        try {
            flag.createNewFile();
        } catch (IOException e) {
            System.err.println("CEFNativeLoader -> Failed to create new file");
        }
    }

    public static void forceDownload(File path) {
        System.out.println("Downloading natives for "+(OS.isWindows()? "Windows" : "Linux")+"...");
        String url;
        if(OS.isWindows()) {
            url = "https://drive.google.com/uc?export=download&confirm=yTib&id=1V6QEdBdCCn4YGWp-9oaqutuQVv-_xMTf";
        } else {
            url = "https://drive.google.com/uc?export=download&confirm=yTib&id=1O93autsbszjC4bm7_PNwMhP1WceKRC_Z";
        }

        File dst = new File(path, "native_bin.zip");
        try {
            Web.downloadFileFromURL(url, dst.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to download natives");
            return;
        }

        System.out.println("Extracting natives for "+(OS.isWindows()? "Windows" : "Linux")+"...");
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(dst));
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = path.getAbsolutePath() + "/" + entry.getName();
                new File(filePath).getParentFile().mkdirs();
                if (!entry.isDirectory()) {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                    byte[] bytesIn = new byte[1024];
                    int read = 0;
                    while ((read = zipIn.read(bytesIn)) != -1) {
                        bos.write(bytesIn, 0, read);
                    }
                    bos.close();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch(IOException e) {
            System.err.println("Failed to extract natives");
            System.err.println(" -> " + e.getMessage());
        }
    }

    public static void downloadAndLoad(File path) {
        download(path);
        load(path);
    }

}
