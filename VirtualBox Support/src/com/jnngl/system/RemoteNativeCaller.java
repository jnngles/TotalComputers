package com.jnngl.system;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class RemoteNativeCaller extends UnicastRemoteObject implements INativeCaller {

    private IVBox iface;
    private ByteBuffer vb, session;
    private BufferedImage screen;

    public static void main(String[] args) {
        if(args.length < 1) {
            System.err.println("Invalid arguments.");
            return;
        }

        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(2099);
        } catch (RemoteException ignored) {
            try {
                registry = LocateRegistry.getRegistry(null, 2099);
            } catch (RemoteException e) {
                System.err.println("Failed to create RMI registry or it is already created. ("+
                        e.getClass().getSimpleName()+")");
                return;
            }
        }

        try {
            registry.rebind(args[0].trim(), new RemoteNativeCaller());
        } catch (RemoteException e) {
            System.err.println("Failed to create server ("+e.getClass().getSimpleName()+")");
            return;
        }

        System.out.println("Successfully created new server.");
    }

    public RemoteNativeCaller() throws RemoteException {
        super();
    }

    @Override
    public String[] getMachineNames() throws RemoteException {
        return iface.getMachineNames(vb);
    }

    @Override
    public void launchVM(String name) throws RemoteException {
        session = iface.launchVM(vb, name);
    }

    @Override
    public void closeVM() throws RemoteException {
        iface.closeVM(vb, session);
    }

    @Override
    public int getWidth() throws RemoteException {
        return iface.getWidth();
    }

    @Override
    public int getHeight() throws RemoteException {
        return iface.getHeight();
    }

    @Override
    public byte[] getScreen() throws RemoteException {
        if (screen == null) {
            screen = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        iface.getScreenPixels(vb, session, screen);
        if (iface.getWidth() == 0 || iface.getHeight() == 0) return null;
        if (screen.getWidth() != iface.getWidth() || screen.getHeight() != iface.getHeight()) {
            screen = new BufferedImage(iface.getWidth(), iface.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(screen, "PNG", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            screen.getGraphics().dispose();
        }
    }

    @Override
    public void init(String applicationPath) throws RemoteException {
        if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            System.load(applicationPath + "/vbox_native.dll");
            iface = new VBoxMS();
        } else {
            System.setProperty("vbox.home", "/opt/VirtualBox");
            iface = new VBoxUnix();
        }
        vb = iface.init();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.gc();
            }
        }, 20000, 20000);
    }

    @Override
    public void click(int x, int y, boolean isLeft) throws RemoteException {
        iface.touch(session, x, y, isLeft);
    }

    @Override
    public void key(int[] scancodes) throws RemoteException {
        iface.keyType(session, scancodes);
    }
}
