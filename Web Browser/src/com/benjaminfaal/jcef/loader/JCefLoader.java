package com.benjaminfaal.jcef.loader;

import org.cef.CefApp;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.SystemBootstrap;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class JCefLoader {

    private static final String GITHUB_RELEASES_URL = "https://github.com/jcefbuild/jcefbuild/releases";

    public static final String VERSION = "v1.0.10-84.3.8+gc8a556f+chromium-84.0.4147.105";

    private static boolean loaded;

    public static CefApp getInstance() {
        if (!loaded) {
            throw new IllegalStateException("JCEF is not loaded yet, use installAndLoad to load and create an instance");
        }
        return CefApp.getInstance();
    }

    /**
     * Installs the default JCEF version to the destination and loads a {@link CefApp}.
     *
     * @param destination the destination directory
     * @param settings    see {@link CefApp#CefApp(String[], CefSettings)}
     * @param args        see {@link CefApp#CefApp(String[], CefSettings)}
     * @return a {@link CefApp} loaded from the destination directory
     */
    public static CefApp installAndLoad(Path destination, CefSettings settings, String... args) throws IOException {
        return installAndLoad(VERSION, destination, settings, args);
    }

    /**
     * Installs the specified JCEF version to the destination and loads a {@link CefApp}.
     *
     * @param version     the release version from https://github.com/jcefbuild/jcefbuild/releases
     * @param destination the destination directory
     * @param settings    see {@link CefApp#CefApp(String[], CefSettings)}
     * @param args        see {@link CefApp#CefApp(String[], CefSettings)}
     * @return a {@link CefApp} loaded from the destination directory
     */
    public static CefApp installAndLoad(String version, Path destination, CefSettings settings, String... args) throws IOException {
        if (loaded) {
            return CefApp.getInstance();
        }
        if (Files.exists(destination) && !Files.isDirectory(destination)) {
            throw new IllegalArgumentException("Destination: " + destination + " must be a directory");
        }

        destination = destination.resolve(version);
        if (!Files.exists(destination)) {
            Files.createDirectories(destination);
        }

        File installedFile = destination.resolve(".installed").toFile();
        if (!installedFile.exists()) {
            install(version, destination);
        }

        if (!loaded) {
            load(destination, args);
            loaded = true;
        }

        return CefApp.getInstance(args, settings);
    }

    private static void install(String version, Path destination) throws IOException {
        String osIdentifier = getOsIdentifier();
        String zipFileName = osIdentifier + ".zip";
        String zipUrl = GITHUB_RELEASES_URL + "/download/" + version + "/" + zipFileName;
        Path localZipFile = destination.resolve(zipFileName);
        if (!Files.exists(localZipFile)) {
            try {
                Files.copy(new URL(zipUrl).openStream(), localZipFile);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("JCEF version " + version + " does not have " + osIdentifier + " check " + GITHUB_RELEASES_URL + "/" + version);
            }
        }

        Path installedLockFile = destination.resolve(".installed");
        if (!Files.exists(installedLockFile)) {
            String dst = destination.toString();

            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(localZipFile.toFile()));
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = dst + "/" + entry.getName();
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

            Files.createFile(installedLockFile);
        }
    }

    private static void load(Path destination, String... args) {
        String path = System.getProperty("java.library.path");

        String javaHomeBin = Paths.get(System.getProperty("java.home")).resolve("bin").toString();
        path = javaHomeBin + File.pathSeparator + path;

        if (!path.endsWith(File.pathSeparator)) {
            path += File.pathSeparator;
        }
        Path libPath = resolveLibPath(destination);
        path += libPath.toString();
        System.setProperty("java.library.path", path);

        if (OS.isWindows()) {
            Toolkit.getDefaultToolkit();
        } else {
            System.loadLibrary("jawt");
        }
        SystemBootstrap.setLoader(libname -> {
            Path jcefLibrary = libPath.resolve(System.mapLibraryName(libname));
            if (Files.exists(jcefLibrary)) {
                try {
                    System.load(jcefLibrary.toString());
                } catch (Throwable ignored) {}
            } else {
                try {
                    System.loadLibrary(libname);
                } catch (Throwable ignored) {}
            }
        });

        if (!CefApp.startup(args)) {
            throw new IllegalStateException("JCEF initialization failed.");
        }
    }

    private static Path resolveLibPath(Path destination) {
        Path javaCefBuildBin = destination.resolve("java-cef-build-bin").resolve("bin").resolve("lib").resolve(getOsIdentifier());
        if (Files.exists(javaCefBuildBin)) {
            return javaCefBuildBin;
        }
        Path javaCefBuildBlobs = destination.resolve("java-cef-build-blobs");
        if (Files.exists(javaCefBuildBlobs)) {
            return javaCefBuildBlobs;
        }
        throw new IllegalStateException("Failed to find lib directory for: " + destination);
    }

    private static String getOsIdentifier() {
        String osPrefix;
        if (OS.isWindows()) {
            osPrefix = "win";
        } else if (OS.isLinux()) {
            osPrefix = "linux";
        } else {
            throw new IllegalStateException("Unsupported OS " + System.getProperty("os.name"));
        }

        String architecture = System.getProperty("os.arch").replaceAll("\\D+", "").replace("86", "32");
        if (!architecture.equals("32") && !architecture.equals("64")) {
            throw new IllegalStateException("Unsupported architecture: " + System.getProperty("os.arch"));
        }
        return osPrefix + architecture;
    }

}
