package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.ElementList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
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
                vmArgs = "-Dvbox.home="+path+" -Djava.library.path="+path;
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
                    server = (INativeCaller) LocateRegistry.getRegistry(null, 2099).lookup(target);
                    System.out.println("Successfully found virtual box process -> PID: "+process.pid()+"");
                    server.init(applicationPath);
                    System.out.println("Successfully bound new session to '" + target + "' [localhost:2099]");

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
                                int[] s = switch (key) {
                                    case HOME -> new int[]{0xe0, 0x47};
                                    case FUNCTION -> new int[]{0xe0, 0x5b};
                                    case LANG -> new int[]{0x2a, 0x38};
                                    case BACKSPACE -> new int[]{0x01};
                                    case SPACE -> new int[]{0x39};
                                    case TAB -> new int[]{0x0f};
                                    case ENTER -> new int[]{0x3a};
                                    case SHIFT -> new int[]{0x2a};
                                    case ALT -> new int[]{0x38};
                                    case CONTROL -> new int[]{0x1d};

                                    default -> new int[]{
                                            switch (text.charAt(0)) {
                                                case 'a' -> 0x1e;
                                                case 'b' -> 0x30;
                                                case 'c' -> 0x2e;
                                                case 'd' -> 0x20;
                                                case 'e' -> 0x12;
                                                case 'f' -> 0x21;
                                                case 'g' -> 0x22;
                                                case 'h' -> 0x23;
                                                case 'i' -> 0x17;
                                                case 'j' -> 0x24;
                                                case 'k' -> 0x25;
                                                case 'l' -> 0x26;
                                                case 'm' -> 0x32;
                                                case 'n' -> 0x31;
                                                case 'o' -> 0x18;
                                                case 'p' -> 0x19;
                                                case 'q' -> 0x10;
                                                case 'r' -> 0x13;
                                                case 's' -> 0x1f;
                                                case 't' -> 0x14;
                                                case 'u' -> 0x16;
                                                case 'v' -> 0x2f;
                                                case 'w' -> 0x11;
                                                case 'x' -> 0x2d;
                                                case 'y' -> 0x15;
                                                case 'z' -> 0x2c;
                                                case '0' -> 0x0b;
                                                case '1' -> 0x02;
                                                case '2' -> 0x03;
                                                case '3' -> 0x04;
                                                case '4' -> 0x05;
                                                case '5' -> 0x06;
                                                case '6' -> 0x07;
                                                case '7' -> 0x08;
                                                case '8' -> 0x09;
                                                case '9' -> 0x0a;
                                                case ' ' -> 0x39;
                                                case '-' -> 0xc;
                                                case '=' -> 0xd;
                                                case '[' -> 0x1a;
                                                case ']' -> 0x1b;
                                                case ';' -> 0x27;
                                                case '\'' -> 0x28;
                                                case ',' -> 0x33;
                                                case '.' -> 0x34;
                                                case '/' -> 0x35;
                                                case '`' -> 0x29;
                                                default -> 0;
                                            }
                                    };
                                };
                                if (text != null && !text.isEmpty() && s[0] != 0 && !text.toLowerCase().equals(text)) {
                                    return new int[]{s[0], 0x2a};
                                }
                                return s;
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
                                if (scancode[0] != 0) {
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
