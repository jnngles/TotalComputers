package com.jnngl.totalcomputers.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.bsod.Cause;
import com.jnngl.totalcomputers.system.states.State;
import com.jnngl.totalcomputers.system.states.StateManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

class BSoD extends State {

    private final String title;
    private final Throwable error;
    private final Cause cause;
    private final State prevState;
    private BufferedImage buffer;
    private boolean canBeRestarted = false;

    private void prerender() {
        buffer = new BufferedImage(os.screenWidth, os.screenHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffer.createGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, os.screenWidth, os.screenHeight);
        Font font = os.baseFont.deriveFont((float)os.screenHeight/128*3f).deriveFont(Font.BOLD);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int x = os.screenWidth/2, y = os.screenHeight/5;
        Rectangle2D bounds = metrics.getStringBounds(title, g);
        x -= bounds.getWidth()/2;
        y -= bounds.getHeight()/2;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, (int)bounds.getWidth(), (int)bounds.getHeight());
        g.setColor(Color.BLUE);
        g.drawString(title, x, y+=metrics.getAscent());
        font = font.deriveFont(Font.PLAIN);
        g.setFont(font);
        metrics = g.getFontMetrics();
        g.setColor(Color.LIGHT_GRAY);
        Throwable cause = error;
        String tmp;
        y += metrics.getAscent();
        if(error.getCause() == null) {
            StackTraceElement[] stackTrace = error.getStackTrace();
            if(stackTrace.length > 0)
                g.drawString(tmp="Cause: "+error.getCause().getStackTrace()[0].getFileName() + " at line " + error.getCause().getStackTrace()[0].getLineNumber(), os.screenWidth/2-metrics.stringWidth(tmp)/2, y += metrics.getHeight());
        } else {
            StackTraceElement[] stackTrace = error.getStackTrace();
            if(stackTrace.length > 0)
                g.drawString(tmp="Invoked by " + stackTrace[0].getFileName() + " at line " + error.getStackTrace()[0].getLineNumber(), os.screenWidth/2-metrics.stringWidth(tmp)/2, y += metrics.getHeight());
            stackTrace = error.getCause().getStackTrace();
            if(stackTrace.length > 0) {
                g.drawString(tmp = "Cause: " + error.getCause().getStackTrace()[0].getFileName() + " at line " + error.getCause().getStackTrace()[0].getLineNumber(), os.screenWidth / 2 - metrics.stringWidth(tmp) / 2, y += metrics.getHeight());
                cause = error.getCause();
            }
        }
        y += metrics.getAscent();
        if(cause.getMessage() != null)
            g.drawString(cause.getMessage(), os.screenWidth/2-metrics.stringWidth(cause.getMessage())/2, y += metrics.getHeight());
        y += metrics.getAscent();
        g.drawString("Available processors (cores): "+Runtime.getRuntime().availableProcessors(), os.screenWidth/2-metrics.stringWidth("Available processors (cores): "), y+=metrics.getHeight());
        g.drawString("Free memory (bytes): "+Runtime.getRuntime().freeMemory(), os.screenWidth/2-metrics.stringWidth("Free memory (bytes): "), y+=metrics.getHeight());
        g.drawString("Maximum memory (bytes): "+(Runtime.getRuntime().maxMemory() == Long.MAX_VALUE? "No limit" : Runtime.getRuntime().maxMemory()), os.screenWidth/2-metrics.stringWidth("Maximum memory (bytes): "), y+=metrics.getHeight());
        g.drawString("Total memory (bytes): "+Runtime.getRuntime().totalMemory(), os.screenWidth/2-metrics.stringWidth("Total memory (bytes): "), y+=metrics.getHeight());
        y += metrics.getAscent();
        g.drawString("Host Operating System: "+System.getProperty("os.name"), os.screenWidth/2-metrics.stringWidth("Host Operating System: "), y+=metrics.getHeight());
        g.drawString("Java Version: "+System.getProperty("java.version"), os.screenWidth/2-metrics.stringWidth("Java Version: "), y+=metrics.getHeight());
        y += metrics.getHeight()*3;
        String str = Integer.toHexString(this.cause.getCode()).toUpperCase();
        g.drawString("  0x"+(str.length()==1?"0"+str : str)+": "+this.cause+"   "+prevState.getClass().getName()+"   "+os.name+" "+os.screenWidth+"x"+os.screenHeight, 0, y+=metrics.getHeight());
        g.drawString("  API: "+TotalOS.getApiVersion(), 0, y+=metrics.getHeight());

        int timer = 10;
        long lastTime = System.currentTimeMillis();
        while(timer>=0) {
            while(System.currentTimeMillis()-lastTime < 1000);
            lastTime = System.currentTimeMillis();
            g.setColor(Color.BLUE);
            g.fillRect(0, y+metrics.getHeight()-metrics.getAscent(), os.screenWidth, metrics.getHeight());
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("  Please wait "+timer+" more second"+(timer==1?"":"s")+" before restart", 0, y+metrics.getHeight());
            timer--;
        }
        g.setColor(Color.BLUE);
        g.fillRect(0, y+metrics.getHeight()-metrics.getAscent(), os.screenWidth, metrics.getHeight());
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("  Click to restart", 0, y+metrics.getHeight());
        canBeRestarted = true;
    }

    public BSoD(StateManager manager, TotalOS os, String title, Throwable error, Cause cause) {
        super(manager, os);
        this.prevState = manager.getState();
        this.title = title;
        this.error = error;
        this.cause = cause;
        new Thread(this::prerender).start();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        if(buffer != null)
            g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(canBeRestarted)
            os.restart();
    }
}
