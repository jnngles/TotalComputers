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
import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.states.SplashScreen;
import com.jnngl.totalcomputers.system.states.StateManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main class of TotalOS Operating System
 */
public class TotalOS {

    /**
     * @return API version
     */
    public static int getApiVersion() {
        return 2;
    }

    /**
     * Simple map renderer.
     * <p>
     * Draws image to screen and handles input.
     * </p>
     */
    public static class Renderer extends MapRenderer {

        private final String name;
        private final int id;
        private final TotalComputers plugin;
        private final TotalOS os;
        private final TotalComputers.SelectionArea area;

        private static Map<TotalOS, Boolean> completionMap;

        /**
         * Constructor
         * @param name Name of the computer
         * @param id Index of monitor piece
         * @param plugin Plugin instance
         * @param os Operating System instance
         * @param area Physical data of the computer
         */
        public Renderer(String name, int id, TotalComputers plugin, TotalOS os, TotalComputers.SelectionArea area) {
            if(completionMap == null) completionMap = new HashMap<>();
            this.name = name;
            this.id = id;
            this.plugin = plugin;
            this.os = os;
            this.area = area;
        }

        public TotalOS getSystem() { return os; }

        private static BufferedImage copyImage(BufferedImage source){
            BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
            Graphics g = b.getGraphics();
            g.drawImage(source, 0, 0, null);
            g.dispose();
            return b;
        }

        /**
         * Custom implementation of {@link org.bukkit.map.MapRenderer#render(MapView, MapCanvas, Player)} function.
         * @param map Destination map
         * @param canvas Map canvas
         * @param player Idk
         */
        public synchronized void render(MapView map, MapCanvas canvas, Player player) {
            int absY = (int)Math.floor((float)id / area.width());
            int absX = id - absY*area.width();
            absX *= 128; absY *= 128;

            List<TotalComputers.InputInfo> handledInputs = new ArrayList<>();
            for (TotalComputers.InputInfo inputInfo : plugin.unhandledInputs) {
                if (inputInfo.index().name().equals(name) && inputInfo.index().index() == id) {
                    os.processTouch(absX + inputInfo.x(), absY + inputInfo.y(), inputInfo.interactType(), inputInfo.player().hasPermission("totalcomputers.admin"));
                    handledInputs.add(inputInfo);
                }
            }
            plugin.unhandledInputs.removeAll(handledInputs);

            int finalAbsX = absX;
            int finalAbsY = absY;
            Thread renderThread = new Thread(() -> {
                if(os.renderQueue) return;
                canvas.drawImage(0, 0, copyImage(os.getScreen().getSubimage(finalAbsX, finalAbsY, 128, 128)));
                if(id == os.screenWidth/128*os.screenHeight/128-1) {
                    os.renderQueue = true;
                }
            });
            renderThread.setPriority(Thread.MAX_PRIORITY);
            renderThread.start();
        }

    }

    /**
     * Describes state of the computer
     */
    public enum ComputerState {
        /**
         * Means that computer is off
         */
        OFF,

        /**
         * Means that computer is on
         */
        RUNNING
    }

    private ComputerState currentState;

    private final StateManager stateManager;

    /**
     * Screen
     */
    private final BufferedImage image;
    private final Graphics2D imageGraphics;

    /**
     * Screen width
     */
    public final int screenWidth;

    /**
     * Screen height
     */
    public final int screenHeight;

    /**
     * Computer name
     */
    public final String name;


    /**
     * File system
     */
    public FileSystem fs;

    /**
     * Whether it is a first run or not
     */
    public boolean firstRun = false;

    /**
     * Base Font
     */
    public Font baseFont;


    /**
     * Keyboard overlay instance
     */
    public Keyboard keyboard;

    /**
     * Information overlay instance
     */
    public Information information;


    /**
     * Localization
     */
    public Localization localization;

    /**
     * Account
     */
    public Account account;


    /**
     * Whether user has admin rights or not
     */
    private boolean hasAdminRights;

    public boolean renderQueue = false;

    private ScheduledExecutorService executor;

    private List<Runnable> threads;
    public MapCanvas target;
    public int x, y;

    public void runInSystemThread(Runnable action) {
        threads.add(action);
    }

    /**
     * Constructor
     * @param widthPix Width of monitor in pixels
     * @param heightPix Height of monitor in pixels
     * @param name Name of the computer
     */
    public TotalOS(int widthPix, int heightPix, String name) {
        currentState = ComputerState.OFF;
        threads = new ArrayList<>();
        image = new BufferedImage(widthPix, heightPix, BufferedImage.TYPE_INT_RGB);
        imageGraphics = image.createGraphics();
        this.screenWidth = widthPix;
        this.screenHeight = heightPix;
        this.name = name;
        hasAdminRights = false;
        stateManager = new StateManager();
    }

    public BufferedImage getScreen() {
        return image;
    }

    /**
     * Renders frame into buffered image
     */
    public void renderFrame() {
        if(!renderQueue) return;
        List<Runnable> finished = new ArrayList<>();
        for(Runnable thread : threads) {
            thread.run();
            finished.add(thread);
        }
        threads.removeAll(finished);
        stateManager.update();
        imageGraphics.setColor(Color.BLACK);
        imageGraphics.fillRect(0, 0, screenWidth, screenHeight);
        if (currentState == ComputerState.OFF) {
            imageGraphics.dispose();
            return;
        }
        stateManager.render(imageGraphics);
        if (keyboard != null) keyboard.render(imageGraphics);
        if (information != null) information.render(imageGraphics);
        renderQueue = false;
    }

    /**
     * Returns color at specific pixel
     * @param x X coordinate of the pixel
     * @param y Y coordinate of the pixel
     * @return color
     */
    public Color getColorAt(int x, int y) {
        return new Color(image.getRGB(x,y));
    }

    /**
     * Handles input.
     * @param x X coordinate of the touch position
     * @param y Y coordinate of the touch position
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     * @param adminRights Whether the player have administration rights or not
     */
    public void processTouch(int x, int y, TotalComputers.InputInfo.InteractType type, boolean adminRights) {
        if(x >= screenWidth || y >= screenHeight) return;
        hasAdminRights = adminRights;
        if(currentState == ComputerState.OFF) {
            turnOn();
            return;
        }
        if(information != null && information.isControlTaken()) {
            information.processInput(x, y, type);
            return;
        }
        if(keyboard != null && keyboard.isControlTaken()) {
            keyboard.processInput(x, y, type);
            return;
        }
        stateManager.processInput(x, y, type);
    }

    /**
     * Restarts the computer
     */
    public void restart() {
        turnOff();
        turnOn();
    }

    /**
     * Turns off the computer
     */
    public void turnOff() {
        currentState = ComputerState.OFF;
        fs = null;
        firstRun = false;
        keyboard = null;
        information = null;
        if(executor != null)
            executor.shutdown();

        stateManager.setState(null);
    }

    /**
     * Turns on the computer
     */
    public void turnOn() {
        if(currentState == ComputerState.RUNNING) return;
        currentState = ComputerState.RUNNING;
        fs = new FileSystem(name);
        firstRun = fs.firstRun;
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont = Font.createFont(Font.TRUETYPE_FONT, new File("FreeSans.ttf")));
        } catch (IOException |FontFormatException e) {
            InputStream is = this.getClass().getResourceAsStream("/FreeSans.ttf");
            try {
                if(is != null)
                    baseFont=Font.createFont(Font.TRUETYPE_FONT,is);
                else System.out.print("Failed to load font. Check your jar file.");
            } catch (FontFormatException | IOException ex) {
                ex.printStackTrace();
            }
        }
        information = new Information(this);
        keyboard = new Keyboard(this);

        if(!firstRun) loadDataFromFileSystem();
        fs.loadResources();

        stateManager.setState(new SplashScreen(stateManager, this));
//        stateManager.setState(new Desktop(stateManager, this)); // For testing

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::renderFrame, 0, 1000/20, TimeUnit.MILLISECONDS);
    }

    /**
     * Loads data from file system
     */
    private void loadDataFromFileSystem() {
        localization = fs.readLocalization();
        account = fs.readAccount();
    }

    /**
     * Requests administrator rights
     * @return Whether the player have administration rights or not
     */
    public boolean requestAdminRights() {
        return hasAdminRights;
    }


    /**
     * Simple class for testing outside the minecraft
     */
    public static class Test extends JPanel implements MouseListener, KeyListener {

        /**
         * TotalOS instance
         */
        private final TotalOS os;

        /**
         * Window
         */
        private final JFrame jf;

        /**
         * Constructor
         * @param width Width
         * @param height Height
         * @param jf JFrame instance
         */
        public Test(int width, int height, JFrame jf) {
            this.jf = jf;
            os = new TotalOS(width, height, "test");
        }

        /**
         * Start point
         * @param args Console arguments
         */
        public static void main(String[] args) {
            JFrame jf = new JFrame("Test");
            jf.setSize(4*128, 3*128);
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jf.setUndecorated(true);
            jf.setLocationRelativeTo(null);
            jf.setResizable(false);
            Test test;
            jf.add(test = new Test(jf.getWidth(), jf.getHeight(), jf));
            jf.addMouseListener(test);
            jf.addKeyListener(test);
            jf.setVisible(true);

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {
                if(!test.os.renderQueue) jf.repaint();
            }, 0, 1000/60, TimeUnit.MILLISECONDS);
        }

        /**
         * Custom implementation of paint function
         * @param g Graphics2D instance
         */
        @Override
        public void paint(Graphics g) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2D.drawImage(os.image, 0, 0, null);
            g2D.dispose();
            os.renderQueue = true;
        }

        /**
         * Mouse input
         * @param e MouseEvent
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) os.processTouch(e.getX(), e.getY(), TotalComputers.InputInfo.InteractType.LEFT_CLICK, true);
            else if(SwingUtilities.isRightMouseButton(e)) os.processTouch(e.getX(), e.getY(), TotalComputers.InputInfo.InteractType.RIGHT_CLICK, true);
        }

        /**
         * Not used
         * @param e MouseEvent
         */
        @Override
        public void mousePressed(MouseEvent e) {

        }

        /**
         * Not used
         * @param e MouseEvent
         */
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        /**
         * Not used
         * @param e MouseEvent
         */
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Not used
         * @param e MouseEvent
         */
        @Override
        public void mouseExited(MouseEvent e) {

        }

        /**
         * Not used
         * @param e KeyEvent
         */
        @Override
        public void keyTyped(KeyEvent e) {

        }

        /**
         * Exits from the app when Escape key is pressed.
         * @param e KeyEvent
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                WindowEvent closingEvent = new WindowEvent(jf, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closingEvent);
                System.exit(0);
            }
        }

        /**
         * Not used
         * @param e KeyEvent
         */
        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
