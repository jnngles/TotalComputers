package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ElementList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.ServerException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VBoxApplication extends WindowApplication {

    private INativeCaller server;

    private final int[] width = { 0 };
    private final int[] height = { 0 };

    private int bX, bY, bW, bH;
    private int lastW, lastH;
    private int toolbarHeight;

    private Button openKeyboard;

    private ElementList VMs;
    private Button launchBt;

    private boolean launched = false;

    public static void main(String[] args) {
        ApplicationHandler.open(VBoxApplication.class, args[0]);
    }

    public VBoxApplication(TotalOS os, String path) {
        super(os, "Virtual Box", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    private void updateBounds(int width, int height) {
        if((float)width/height > (float)this.width[0]/this.height[0]) {
            bY = toolbarHeight;
            bH = height-toolbarHeight;
            float dif = (float)this.height[0]/height;
            bW = (int) (this.width[0]/dif);
            bX = width/2-bW/2;
        }
        else {
            bX = 0;
            bW = width;
            float dif = (float)this.width[0]/width;
            bH = (int) ((this.height[0])/dif);
            bY = height/2-bH/2+toolbarHeight;
        }
    }

    @Override
    protected void onStart() {
//        if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
//            os.information.displayMessage(Information.Type.ERROR,
//                    "This application is incompatible with servers on Windows", null);
//            close();
//        }

        Font uiFont = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        toolbarHeight = Utils.getFontMetrics(uiFont).getHeight();

        openKeyboard = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), toolbarHeight,
                uiFont, "Open Keyboard");

        launchBt = new Button(Button.ButtonColor.BLUE, 0, 0, getWidth(), Utils.getFontMetrics(uiFont).getHeight(),
                uiFont, "Launch");
        VMs = new ElementList(0, 0, getWidth(), getHeight()-launchBt.getHeight(), uiFont);
        launchBt.setY(VMs.getHeight());

        addResizeEvent(new ResizeEvent() {
            private void handleResize(int width, int height) {
                if(!launched) {
                    launchBt.setWidth(getWidth());
                    VMs.setWidth(getWidth());
                    VMs.setHeight(getHeight()-launchBt.getHeight());
                    launchBt.setY(VMs.getHeight());
                } else {
                    updateBounds(width, height);
                    openKeyboard.setWidth(width);
                }
            }

            @Override
            public void onResize(int width, int height) {
                handleResize(width, height);
            }

            @Override
            public void onMaximize(int width, int height) {
                handleResize(width, height);
            }

            @Override
            public void onUnmaximize(int width, int height) {
                handleResize(width, height);
            }
        });

        new Thread(() -> {
            List<String> cmd = new ArrayList<>();
            String vmArgs = "";
            if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                cmd.add(System.getProperty("java.home")+ File.separator+"bin"+
                        File.separator+"java.exe");
            } else {

                String path;
                try {
                    path = Files.readString(new File(applicationPath, "vbox_root.txt").toPath())
                            .split("\n")[0].trim();
                } catch (IOException e) {
                    path = "/opt/VirtualBox";
                    System.err.println("Failed to read `vbox_root.txt'. Using /opt/VirtualBox");
                }
                vmArgs = "-Dsun.boot.library.path="+path+" -Dvbox.home="+path+" -Djava.library.path="+path;
                String java = System.getProperty("java.home")+File.separator+"bin"+File.separator+"java";
                cmd.add(java);
            }
            cmd.add("-Xcheck:jni");
            cmd.add("-jar");
            if(!vmArgs.isEmpty())
                cmd.addAll(Arrays.asList(vmArgs.split(" ")));
            cmd.add(applicationPath+File.separator+
                    "application.jar");
            String target = "vbox_session_"+Thread.currentThread().getId();
            cmd.add(target);
            Process process = null;
            try {
                ProcessBuilder pb = new ProcessBuilder(cmd.toArray(new String[0]));
                pb.inheritIO();
                process = pb.start();
            } catch (IOException e) {
                throw new Error(e);
            }
            long startTime = System.currentTimeMillis();
            while(true) {
                try {
                    long current = System.currentTimeMillis();
                    if(current-startTime > 10000) {
                        System.err.println("["+target+"] Remote session not found. Timed out.");
                        break;
                    }
                    server = (INativeCaller) LocateRegistry.getRegistry(null, 2199).lookup(target);
                    System.out.println("Successfully found virtual box process -> PID: "+process.pid()+"");
                    try {
                        server.init(applicationPath);
                    } catch (Throwable e) {
                        System.err.println("Error: Unable to initialize VBox ("+e.getClass().getName()+"): "+e.getMessage());
                        throw new Error(e);
                    }
                    System.out.println("Successfully bound new session to '" + target + "' [localhost:2199]");

                    String[] names = server.getMachineNames();
                    VMs.addEntries(names);

                    launchBt.registerClickEvent(() -> {
                        if(VMs.getSelectedIndex() < 0 || VMs.getSelectedIndex() >= names.length) return;
                        String selected = VMs.getSelectedElement();
                        if(selected.trim().isEmpty()) return;
                        launchBt.setLocked(true);
                        VMs.setLocked(true);
                        try {
                            server.launchVM(selected);
                        } catch (RemoteException e) {
                            throw new Error(e);
                        }
                        launched = true;

                        openKeyboard.setWidth(getWidth());
                        openKeyboard.registerClickEvent(() -> os.keyboard.invokeKeyboard(new Keyboard.KeyboardListener() {
                            private String buffer = "";

                            private int[] getScancode(Keyboard.Keys key, String text) {
                                return switch(key) {
                                    case TILDE             -> new int[] { 0x2A, 0x29, 0xA9, 0xAA };
                                    case BACKTICK          -> new int[] { 0x29, 0xA9 };
                                    case ONE               -> new int[] { 0x02, 0x82 };
                                    case EXCLAMATION_MARK  -> new int[] { 0x2A, 0x02, 0x82, 0xAA };
                                    case TWO               -> new int[] { 0x03, 0x83 };
                                    case AT_SIGN           -> new int[] { 0x2A, 0x03, 0x83, 0xAA };
                                    case THREE             -> new int[] { 0x04, 0x84 };
                                    case NUMBER_SIGN       -> new int[] { 0x2A, 0x04, 0x84, 0xAA };
                                    case FOUR              -> new int[] { 0x05, 0x85 };
                                    case DOLLAR_SIGN       -> new int[] { 0x2A, 0x05, 0x85, 0xAA };
                                    case FIVE              -> new int[] { 0x06, 0x86 };
                                    case PERCENT_SIGN      -> new int[] { 0x2A, 0x06, 0x86, 0xAA };
                                    case SIX               -> new int[] { 0x07, 0x87 };
                                    case CARET             -> new int[] { 0x2A, 0x07, 0x87, 0xAA };
                                    case SEVEN             -> new int[] { 0x08, 0x88 };
                                    case AMPERSAND         -> new int[] { 0x2A, 0x08, 0x88, 0xAA };
                                    case EIGHT             -> new int[] { 0x09, 0x89 };
                                    case ASTERISK          -> new int[] { 0x2A, 0x09, 0x89, 0xAA };
                                    case NINE              -> new int[] { 0x0A, 0x8A };
                                    case LEFT_PARENTHESIS  -> new int[] { 0x2A, 0x0A, 0x8A, 0xAA };
                                    case ZERO              -> new int[] { 0x0B, 0x8B };
                                    case RIGHT_PARENTHESIS -> new int[] { 0x2A, 0x0B, 0x8B, 0xAA };
                                    case MINUS_SIGN        -> new int[] { 0x0C, 0x8C };
                                    case UNDERSCORE        -> new int[] { 0x2A, 0x0C, 0x8C, 0xAA };
                                    case PLUS_SIGN         -> new int[] { 0x2A, 0x0D, 0x8D, 0xAA };
                                    case EQUALS_SIGN       -> new int[] { 0x0D, 0x8D };
                                    case LEFT_BRACKET      -> new int[] { 0x1A, 0x9A };
                                    case LEFT_BRACE        -> new int[] { 0x2A, 0x1A, 0x9A, 0xAA };
                                    case RIGHT_BRACKET     -> new int[] { 0x1B, 0x9B };
                                    case RIGHT_BRACE       -> new int[] { 0x2A, 0x1B, 0x9B, 0xAA };
                                    case BACKSLASH         -> new int[] { 0x2B, 0xAB };
                                    case VERTICAL_LINE     -> new int[] { 0x2A, 0x2B, 0xAB, 0xAA };
                                    case SEMICOLON         -> new int[] { 0x27, 0xA7 };
                                    case COLON             -> new int[] { 0x2A, 0x27, 0xA7, 0xAA };
                                    case APOSTROPHE        -> new int[] { 0x28, 0xA8 };
                                    case QUOTATION_MARK    -> new int[] { 0x2A, 0x28, 0xA8, 0xAA };
                                    case COMMA             -> new int[] { 0x33, 0xB3 };
                                    case LESS_THAN_SIGN    -> new int[] { 0x2A, 0x33, 0xB3, 0xAA };
                                    case PERIOD            -> new int[] { 0x34, 0xB4 };
                                    case GREATER_THAN_SIGN -> new int[] { 0x2A, 0x34, 0xB4, 0xAA };
                                    case SLASH             -> new int[] { 0x35, 0xB5 };
                                    case QUESTION_MARK     -> new int[] { 0x2A, 0x35, 0xB5, 0xAA };
                                    case ENTER             -> new int[] { 0x1C, 0x9C };
                                    case SHIFT             -> new int[] { 0x2A, 0xAA };
                                    case CONTROL           -> new int[] { 0x1D, 0x9D };
                                    case HOME              -> new int[] { 0xE0, 0x47, 0xE0, 0xC7 };
                                    case ALT               -> new int[] { 0x38, 0xB8 };
                                    case SPACE             -> new int[] { 0x39, 0xB9 };
                                    case BACKSPACE         -> new int[] { 0x0E, 0x8E };
                                    case TAB               -> new int[] { 0x0F, 0x8F };
                                    default -> switch (text) {
                                        case "q" -> new int[] { 0x10, 0x90 };
                                        case "w" -> new int[] { 0x11, 0x91 };
                                        case "e" -> new int[] { 0x12, 0x92 };
                                        case "r" -> new int[] { 0x13, 0x93 };
                                        case "t" -> new int[] { 0x14, 0x94 };
                                        case "y" -> new int[] { 0x15, 0x95 };
                                        case "u" -> new int[] { 0x16, 0x96 };
                                        case "i" -> new int[] { 0x17, 0x97 };
                                        case "o" -> new int[] { 0x18, 0x98 };
                                        case "p" -> new int[] { 0x19, 0x99 };
                                        case "a" -> new int[] { 0x1E, 0x9E };
                                        case "s" -> new int[] { 0x1F, 0x9F };
                                        case "d" -> new int[] { 0x20, 0xA0 };
                                        case "f" -> new int[] { 0x21, 0xA1 };
                                        case "g" -> new int[] { 0x22, 0xA2 };
                                        case "h" -> new int[] { 0x23, 0xA3 };
                                        case "j" -> new int[] { 0x24, 0xA4 };
                                        case "k" -> new int[] { 0x25, 0xA5 };
                                        case "l" -> new int[] { 0x26, 0xA6 };
                                        case "z" -> new int[] { 0x2C, 0xAC };
                                        case "x" -> new int[] { 0x2D, 0xAD };
                                        case "c" -> new int[] { 0x2E, 0xAE };
                                        case "v" -> new int[] { 0x2F, 0xAF };
                                        case "b" -> new int[] { 0x30, 0xB0 };
                                        case "n" -> new int[] { 0x31, 0xB1 };
                                        case "m" -> new int[] { 0x32, 0xB2 };
                                        case "Q" -> new int[] { 0x2A, 0x10, 0x90, 0xAA };
                                        case "W" -> new int[] { 0x2A, 0x11, 0x91, 0xAA };
                                        case "E" -> new int[] { 0x2A, 0x12, 0x92, 0xAA };
                                        case "R" -> new int[] { 0x2A, 0x13, 0x93, 0xAA };
                                        case "T" -> new int[] { 0x2A, 0x14, 0x94, 0xAA };
                                        case "Y" -> new int[] { 0x2A, 0x15, 0x95, 0xAA };
                                        case "U" -> new int[] { 0x2A, 0x16, 0x96, 0xAA };
                                        case "I" -> new int[] { 0x2A, 0x17, 0x97, 0xAA };
                                        case "O" -> new int[] { 0x2A, 0x18, 0x98, 0xAA };
                                        case "P" -> new int[] { 0x2A, 0x19, 0x99, 0xAA };
                                        case "A" -> new int[] { 0x2A, 0x1E, 0x9E, 0xAA };
                                        case "S" -> new int[] { 0x2A, 0x1F, 0x9F, 0xAA };
                                        case "D" -> new int[] { 0x2A, 0x20, 0xA0, 0xAA };
                                        case "F" -> new int[] { 0x2A, 0x21, 0xA1, 0xAA };
                                        case "G" -> new int[] { 0x2A, 0x22, 0xA2, 0xAA };
                                        case "H" -> new int[] { 0x2A, 0x23, 0xA3, 0xAA };
                                        case "J" -> new int[] { 0x2A, 0x24, 0xA4, 0xAA };
                                        case "K" -> new int[] { 0x2A, 0x25, 0xA5, 0xAA };
                                        case "L" -> new int[] { 0x2A, 0x26, 0xA6, 0xAA };
                                        case "Z" -> new int[] { 0x2A, 0x2C, 0xAC, 0xAA };
                                        case "X" -> new int[] { 0x2A, 0x2D, 0xAD, 0xAA };
                                        case "C" -> new int[] { 0x2A, 0x2E, 0xAE, 0xAA };
                                        case "V" -> new int[] { 0x2A, 0x2F, 0xAF, 0xAA };
                                        case "B" -> new int[] { 0x2A, 0x30, 0xB0, 0xAA };
                                        case "N" -> new int[] { 0x2A, 0x31, 0xB1, 0xAA };
                                        case "M" -> new int[] { 0x2A, 0x32, 0xB2, 0xAA };
                                        default -> null;
                                    };
                                };
                            }

                            @Override
                            public String keyTyped(String text, Keyboard.Keys key, Keyboard keyboard) {
                                if (key == Keyboard.Keys.OK) {
                                    os.keyboard.closeKeyboard();
                                    return buffer;
                                }
                                if (key == Keyboard.Keys.BACKSPACE) {
                                    if (buffer.length() > 0) {
                                        buffer = buffer.substring(0, buffer.length() - 1);
                                    }
                                }
                                if (key.text != null) {
                                    buffer += text;
                                }

                                int[] scancode = getScancode(key, text);
                                if (scancode != null) {
                                    try {
                                        server.key(scancode);
                                    } catch (RemoteException e) {
                                        System.err.println("Failed to invoke remote method ("+e.getClass().getSimpleName()+")");
                                        System.err.println(" -> "+e.getMessage());
                                    }
                                }

                                return buffer;
                            }
                        }, ""));
                    });

                    break;
                } catch (RemoteException | NotBoundException e) {
                    if(e instanceof ServerException se) {
                        throw new Error(se);
                    }
                }
            }
        }).start();

        new Thread(() -> {

        }).start();
    }

    @Override
    protected boolean onClose() {
        if(server == null) return true;
        try {
            server.closeVM();
        } catch (RemoteException e) {
            throw new Error(e);
        }
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D graphics) {
        if(!launched) {
            VMs.render(graphics);
            launchBt.render(graphics);
            return;
        }

        if(server == null) return;
        byte[] buffer;
        try {
            buffer = server.getScreen();
            width[0] = server.getWidth();
            height[0] = server.getHeight();
        } catch (RemoteException e) {
            throw new Error(e);
        }
        if(buffer == null || buffer.length < 4) return;

        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        BufferedImage screen;
        try {
            screen = ImageIO.read(bais);
        } catch (IOException e) {
            throw new Error(e);
        }

        if(lastW != width[0] || lastH != height[0]) {
            updateBounds(getWidth(), getHeight());
            lastW = width[0];
            lastH = height[0];
        }

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.drawImage(screen, bX, bY, bW, bH, null);
        openKeyboard.render(graphics);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!launched) {
            VMs.processInput(x, y, type);
            launchBt.processInput(x, y, type);
            return;
        }

        if(server == null) return;

        openKeyboard.processInput(x, y, type);

        float u = (float)(x - bX) / bW;
        float v = (float)(y - bY) / bH;

        if(u < 0 || u > 1 || v < 0 || v > 1) return;

        int absX = (int) (u * (this.width[0]-1));
        int absY = (int) (v * (this.height[0]-1));
        try {
            server.click(absX, absY, type == TotalComputers.InputInfo.InteractType.LEFT_CLICK);
        } catch (RemoteException e) {
            throw new Error(e);
        }
    }
}
