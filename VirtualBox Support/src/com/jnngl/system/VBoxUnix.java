package com.jnngl.system;

import org.virtualbox_6_1.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class VBoxUnix implements IVBox {

    private VirtualBoxManager mgr;
    private IVirtualBox vbox;
    private IProgress progress;
    private ISession session;
    private IDisplay display;
    private IConsole console;
    private IMouse mouse;
    private IKeyboard keyboard;

    Holder<Long> h_width = new Holder<>();
    Holder<Long> h_height = new Holder<>();
    Holder<Long> bpp = new Holder<>();
    Holder<Integer> x = new Holder<>();
    Holder<Integer> y = new Holder<>();
    Holder<GuestMonitorStatus> status = new Holder<>();

    @Override
    public String[] getMachineNames(ByteBuffer unused) {
        List<IMachine> machines = vbox.getMachines();
        List<String> names = new ArrayList<>();
        for(IMachine machine : machines) {
            try {
                names.add(machine.getName());
            } catch (VBoxException e) {
                System.err.println("Inaccessible machine.");
            }
        }
        return names.toArray(new String[0]);
    }

    static boolean progressBar(VirtualBoxManager mgr, IProgress p, long waitMillis)
    {
        long end = System.currentTimeMillis() + waitMillis;
        while (!p.getCompleted())
        {
            mgr.waitForEvents(0);
            p.waitForCompletion(200);
            if (System.currentTimeMillis() >= end)
                return false;
        }
        return true;
    }

    @Override
    public ByteBuffer launchVM(ByteBuffer unused, String name) {
        IMachine m = vbox.findMachine(name);

        List<String> env = new ArrayList<>();

        session = mgr.getSessionObject();
        progress = m.launchVMProcess(session, "headless", env);
        progressBar(mgr, progress, 10000);

        console = session.getConsole();
        display = console.getDisplay();
        mouse = console.getMouse();
        keyboard = console.getKeyboard();

        return null;
    }

    @Override
    public void closeVM(ByteBuffer unused, ByteBuffer unused1) {
        console.powerDown();
        progress.waitForCompletion(-1);
        session.unlockMachine();
        mgr.waitForEvents(0);
        mgr.cleanup();
    }

    @Override
    public int getWidth() {
        return h_width.value.intValue();
    }

    @Override
    public int getHeight() {
        return h_height.value.intValue();
    }

    @Override
    public void getScreenPixels(ByteBuffer vb, ByteBuffer vm, BufferedImage dst) {
        byte[] pixels = getScreen();
        if(getWidth() != dst.getWidth() || dst.getHeight() != getHeight()) return;

        for(int x = 0; x < dst.getWidth(); x++) {
            for(int y = 0; y < dst.getHeight(); y++) {
                int idx = (y*getWidth()+x)*4;
                int r = (pixels[idx++] & 0xff);
                int g = (pixels[idx++] & 0xff);
                int b = (pixels[idx++] & 0xff);
                int a = (pixels[idx  ] & 0xff);
                int argb = (a << 24) | (r << 16) | (g << 8) | b;

                dst.setRGB(x, y, argb);
            }
        }
    }

    public byte[] getScreen() {
        if(console.getState() != MachineState.Running) {
            return null;
        }
        display.getScreenResolution((long)0, h_width, h_height, bpp, x, y, status);

        return display.takeScreenShotToArray((long)0, h_width.value, h_height.value, BitmapFormat.RGBA);
    }

    @Override
    public ByteBuffer init() {
        mgr = VirtualBoxManager.createInstance(null);
        vbox = mgr.getVBox();

        return null;
    }

    @Override
    public void touch(ByteBuffer vm, int x, int y, boolean isLeft) {
        mouse.putMouseEventAbsolute(x + 1, y + 1, 0, 0, isLeft ? 0b001 : 0b010);
        mouse.putMouseEventAbsolute(x + 1, y + 1, 0, 0, 0b000);
    }

    @Override
    public void keyType(ByteBuffer vm, int[] scancodes) {
        List<Integer> sc = new ArrayList<>();
        for(int code : scancodes) sc.add(code);
        keyboard.putScancodes(sc);
    }
}
