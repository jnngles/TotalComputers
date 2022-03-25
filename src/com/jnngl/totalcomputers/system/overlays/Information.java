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

package com.jnngl.totalcomputers.system.overlays;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Event;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Image;
import com.jnngl.totalcomputers.system.ui.Rectangle;
import com.jnngl.totalcomputers.system.ui.RoundRectangle;
import com.jnngl.totalcomputers.system.ui.Text;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Displays warning or error on screen
 */
public class Information extends Overlay {

    /**
     * Type of message
     */
    public enum Type {
        /**
         * Warning
         */
        WARNING,

        /**
         * Error
         */
        ERROR
    }

    private boolean displayed;
    private Event listener;

    private final Button ok;
    private final Text message;
    private final Rectangle background;
    private final RoundRectangle window;
    private final Image icon;

    /**
     * Constructor
     * @param os TotalOS instance
     */
    public Information(TotalOS os) {
        super(os);
        final Font font = os.baseFont.deriveFont(os.screenHeight/128.f*4.5f);

        int fy, sy, fx;

        background = new Rectangle(new Color(0, 0, 0, 0.66f), 0, 0, os.screenWidth, os.screenHeight);
        window = new RoundRectangle(Color.LIGHT_GRAY, ndcX(-0.5f), ndcY(0.5f), os.screenWidth/2, os.screenHeight/2, 24);

        icon = new Image(fx = ndcX(-0.5f)+(int)(os.screenHeight*0.025f), fy=ndcY(0.5f)+(int)(os.screenHeight*0.025f), (int)(os.screenHeight*0.15f), sy=(int)(os.screenHeight*0.15f), null);

        fy+=sy;

        message = new Text(fx+(int)(os.screenHeight*0.025f), fy+(int)(os.screenHeight*0.05f), ndcX(0.45f)-fx+(int)(os.screenHeight*0.025f), ndcY(-0.45f)-(int)(os.screenHeight*0.075f)-fy+(int)(os.screenHeight*0.05f), font, Color.BLACK, "");

        ok = new Button(Button.ButtonColor.BLUE, ndcX(0.45f)-(int)(os.screenWidth*0.1f), ndcY(-0.45f)-(int)(os.screenHeight*0.05f), (int)(os.screenWidth*0.1f), (int)(os.screenHeight*0.05f), font, "OK");
        ok.registerClickEvent(this::close);
    }

    /**
     * Shows message on screen
     * @param type See {@link Type}
     * @param message Message
     * @param eventOnOk Called when OK pressed. May be null
     */
    public void displayMessage(Type type, String message, Event eventOnOk) {
        this.displayed = true;
        this.listener = eventOnOk;
        BufferedImage icon;
        if(type == Type.ERROR) icon = os.fs.getResourceImage("error");
        else                   icon = os.fs.getResourceImage("warning");
        this.icon.setImage(icon);
        this.message.setText(message);
    }

    /**
     * Renders message
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        if(!displayed) return;
        background.render(g);
        window.render(g);
        icon.render(g);
        message.render(g);
        ok.render(g);
    }

    /**
     * Closes message
     */
    public void close() {
        displayed = false;
        if(listener != null)
            listener.action();
    }

    /**
     * Implementation
     * @return See {@link Overlay#isControlTaken()}
     */
    public boolean isControlTaken() {
        return displayed;
    }

    /**
     * Handles input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!displayed) return;
        if(ok != null)
            ok.processInput(x, y, type);
    }

}
