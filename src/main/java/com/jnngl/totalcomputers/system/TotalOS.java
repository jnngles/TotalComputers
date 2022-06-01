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

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.bsod.Cause;
import com.jnngl.totalcomputers.motion.MotionCapture;
import com.jnngl.totalcomputers.sound.discord.DiscordBot;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.states.SplashScreen;
import com.jnngl.totalcomputers.system.states.StateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main class of TotalOS Operating System
 */
public class TotalOS {

    private static final SharedStorage singletonStorage = new SharedStorage();

    public static DiscordBot audio;

    public static boolean isDiscordBotAvailable() {
        return audio != null;
    }

    private static final List<TotalOS> active = new ArrayList<>();
    private static TotalOS current;
    public static TotalOS current() { return current; }

    /**
     * @return API version
     */
    public static int getApiVersion() {
        return 7;
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

    public final SharedStorage storage;

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

    @RequiresAPI(apiLevel = 3)
    public MotionCapture motionCapture;

    private final List<Runnable> threads;
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
        this.screenWidth = widthPix;
        this.screenHeight = heightPix;
        this.name = name;
        storage = singletonStorage;
        hasAdminRights = false;
        stateManager = new StateManager();
    }

    /**
     * Whether operating system supports motion capture
     * @return boolean
     */
    @RequiresAPI(apiLevel = 3)
    public boolean supportsMotionCapture() {
        return motionCapture != null;
    }

    /**
     * Renders frame into buffered image
     * @return BufferedImage
     */
    public BufferedImage renderFrame() {
        current = this;
        List<Runnable> finished = new ArrayList<>();
        for (Runnable thread : threads) {
            thread.run();
            finished.add(thread);
        }
        BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D imageGraphics = image.createGraphics();
        imageGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        imageGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        imageGraphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
        imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        threads.removeAll(finished);
        stateManager.update();
        stateManager.render(imageGraphics);
        if (keyboard != null) keyboard.render(imageGraphics);
        if (information != null) information.render(imageGraphics);
        current = null;
        imageGraphics.dispose();
        return image;
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
        current = this;
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
        current = null;
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

        stateManager.setState(null);
        active.remove(this);
    }

    /**
     * See {@link ComputerState}
     * @return State
     */
    public ComputerState getState() {
        return currentState;
    }

    /**
     * Turns on the computer
     */
    public void turnOn() {
        if(currentState == ComputerState.RUNNING) return;
        currentState = ComputerState.RUNNING;
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
        if(baseFont == null) {
            baseFont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0];
            System.out.println(baseFont.getName());
        }
        fs = new FileSystem(name);
        firstRun = fs.firstRun;
        information = new Information(this);
        keyboard = new Keyboard(this);

        if(!firstRun) loadDataFromFileSystem();
        fs.loadResources();

        stateManager.setState(new SplashScreen(stateManager, this));

        active.add(this);
    }

    /**
     * Terminates OS and displays BSoD
     * @param title Title
     * @param error Error or exception
     * @param cause See {@link Cause}
     */
    @RequiresAPI(apiLevel = 6)
    public void invokeBSoD(String title, Throwable error, Cause cause) {
        stateManager.setState(new BSoD(stateManager, this, title, error, cause));
    }

    /**
     * Terminates OS and displays BSoD
     * @param title Title
     * @param error Error or exception
     * @param code See {@link Cause}
     */
    @RequiresAPI(apiLevel = 5)
    public void invokeBSoD(String title, Throwable error, int code) {
        invokeBSoD(title, error, Cause.fromCode(code));
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

    @RequiresAPI(apiLevel = 5)
    public static TotalOS forName(String name) throws RuntimeException {
        for(TotalOS os : active) {
            if(os.name.equals(name)) {
                return os;
            }
        }
        throw new RuntimeException("Failed to find active OS for name `"+name+"'");
    }

    @RequiresAPI(apiLevel = 5)
    public static TotalOS[] getActiveComputers() {
        return active.toArray(new TotalOS[0]);
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
            executor.scheduleAtFixedRate(jf::repaint, 0, 1000/60, TimeUnit.MILLISECONDS);
        }

        /**
         * Custom implementation of paint function
         * @param g Graphics2D instance
         */
        @Override
        public void paint(Graphics g) {
            BufferedImage img = os.renderFrame();
            Graphics2D g2D = (Graphics2D) g;
            g2D.drawImage(img, 0, 0, null);
            g2D.dispose();
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
