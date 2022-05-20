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

package com.jnngl.totalcomputers.system;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Web {

    /**
     * Reads raw data from URL
     * @param url
     * @return Raw data
     */
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

    /**
     * Reads image from URL
     * @param url URL
     * @return Image
     */
    public static BufferedImage readImageFromURL(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.err.println("Failed to read image from URL.");
            return null;
        }
    }

    /**
     * Downloads file from URL
     * @param url URL
     * @param dst Destination file
     * @param mbDone Called on every downloaded kilobyte
     * @return Whether file was successfully downloaded
     * @throws IOException I/O error
     */
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
            throw e;
        }
        return true;
    }

}
