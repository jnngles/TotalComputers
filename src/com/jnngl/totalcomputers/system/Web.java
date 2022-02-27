package com.jnngl.totalcomputers.system;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Web {

    public static String readFromURL(String url) {
        try {
            Scanner scanner = new Scanner(new URL(url).openStream());
            StringBuilder text = new StringBuilder();
            while(scanner.hasNextLine()) {
                text.append(scanner.nextLine()).append("\n");
            }
            return text.substring(0, text.length()-1);
        } catch(IOException e) {
            System.err.println("Failed to read file from URL.");
            return null;
        }
    }

    public static BufferedImage readImageFromURL(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.err.println("Failed to read image from URL.");
            return null;
        }
    }

    public static boolean downloadFileFromURL(String url, String dst, Event... mbDone) throws IOException {
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(dst);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                for (Event event : mbDone) event.action();
            }
        } catch (IOException e) {
            System.err.println("Failed to download file.");
        }
        return true;
    }

}
