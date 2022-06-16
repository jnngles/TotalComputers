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

import com.jnngl.totalcomputers.MapColor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Common functions
 */
public class Utils {

    private static Graphics2D g;

    /**
     * Creates font metrics
     * @param f Font
     * @return {@link java.awt.FontMetrics} of a font
     */
    public static FontMetrics getFontMetrics(Font f) {
        if(g == null) g = new BufferedImage(1,1, BufferedImage.TYPE_BYTE_GRAY).createGraphics();
        return g.getFontMetrics(f);
    }

    private static class C3 {
        int r, g, b;

        public C3(int c) {
            Color color = new Color(c);
            r = color.getRed();
            g = color.getGreen();
            b = color.getBlue();
        }

        public C3(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public C3 add(C3 o) {
            return new C3(r + o.r, g + o.g, b + o.b);
        }

        public int clamp(int c) {
            return Math.max(0, Math.min(255, c));
        }

        public int diff(C3 o) {
            int Rdiff = o.r - r;
            int Gdiff = o.g - g;
            int Bdiff = o.b - b;
            return Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
        }

        public C3 mul(double d) {
            return new C3((int) (d * r), (int) (d * g), (int) (d * b));
        }

        public C3 sub(C3 o) {
            return new C3(r - o.r, g - o.g, b - o.b);
        }

        public Color toColor() {
            return new Color(clamp(r), clamp(g), clamp(b));
        }
    }

    private static C3 findClosestPaletteColor(C3 c, C3[] palette) {
        C3 closest = palette[0];

        for (C3 n : palette) {
            if (n.diff(c) < closest.diff(c)) {
                closest = n;
            }
        }

        return closest;
    }

    private static C3[] palette = null;

    /**
     * Dithers image using Floyd-Steinberg method
     * @param img Source image
     * @return Dithered image
     */
    public static BufferedImage floydSteinbergDithering(BufferedImage img) {
        if(palette == null) {
            palette = new C3[MapColor.colors.length];
            for (int i = 0; i < palette.length; i++) {
                Color c = MapColor.colors[i];
                palette[i] = new C3(c.getRed(), c.getGreen(), c.getBlue());
            }
        }

        int w = img.getWidth();
        int h = img.getHeight();

        C3[][] d = new C3[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                d[y][x] = new C3(img.getRGB(x, y));
            }
        }

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                C3 oldColor = d[y][x];
                C3 newColor = findClosestPaletteColor(oldColor, palette);
                img.setRGB(x, y, newColor.toColor().getRGB());

                C3 err = oldColor.sub(newColor);

                if (x + 1 < w) {
                    d[y][x + 1] = d[y][x + 1].add(err.mul(7. / 16));
                }

                if (x - 1 >= 0 && y + 1 < h) {
                    d[y + 1][x - 1] = d[y + 1][x - 1].add(err.mul(3. / 16));
                }

                if (y + 1 < h) {
                    d[y + 1][x] = d[y + 1][x].add(err.mul(5. / 16));
                }

                if (x + 1 < w && y + 1 < h) {
                    d[y + 1][x + 1] = d[y + 1][x + 1].add(err.mul(1. / 16));
                }
            }
        }

        return img;
    }

    public static BufferedImage copyImage(BufferedImage source) {
        ColorModel cm = source.getColorModel();
        WritableRaster raster = source.copyData(null);
        return new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null)
                .getSubimage(0, 0, source.getWidth(), source.getHeight());
    }

}
