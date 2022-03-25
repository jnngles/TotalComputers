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

package com.jnngl.totalcomputers.system.ui;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.overlays.Keyboard;

import java.awt.*;

/**
 * Text Field Component
 */
public class Field implements ComponentUI {

    private boolean isLocked;
    private int x, y, width, height;
    private final Font font;
    private Text text;
    private Text description;
    private final Keyboard keyboard;
    private Keyboard.KeyboardListener event;

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     * @param width Text field width
     * @param height Text field height
     * @param font Font
     * @param text Initial text
     * @param description Description. Will be displayed when text is not entered.
     * @param keyboard {@link com.jnngl.totalcomputers.system.overlays.Keyboard} instance. Do not create new instance under any circumstances! It is located at {@link com.jnngl.totalcomputers.system.TotalOS#keyboard}
     */
    public Field(int x, int y, int width, int height, Font font, String text, String description, Keyboard keyboard) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;
        FontMetrics metrics = Utils.getFontMetrics(font);
        this.text = new Text(x + 10, y+height/2+metrics.getHeight()/4, width-15, height, font, Color.BLACK, text);
        this.description = new Text(x + 10, y+height/2+metrics.getHeight()/4, width-15, height, font, Color.GRAY, description);
        this.keyboard = keyboard;
    }

    /**
     * Renders Text field
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, width, height, 4, 4);
        g.setFont(font);
        if(text.getText().equals("")) { // Draw description
            description.render(g);
        } else { // Draw text
            text.setColor(isLocked? Color.GRAY : Color.BLACK);
            text.render(g);
        }
        if(isLocked) g.setColor(Color.GRAY);
        else g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 6, 6);
    }

    /**
     * Handles input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(isLocked) return;
        if(type != TotalComputers.InputInfo.InteractType.LEFT_CLICK) return;
        if(x >= this.x && y >= this.y && x <= this.x+width && y <= this.y+height) {
            keyboard.invokeKeyboard(event == null? this::keyTyped : event, text.getText());
        }
    }

    /**
     * Sets custom event on key typed
     * @param listener Event
     */
    public void setKeyTypedEvent(Keyboard.KeyboardListener listener) {
        this.event = listener;
    }

    private String keyTyped(String character, Keyboard.Keys key, Keyboard keyboard) {
        if(key == Keyboard.Keys.ENTER) text.setText(text.getText()+"\n");
        if(key == Keyboard.Keys.BACKSPACE)
            if(text.getText().length() > 0)
                text.setText(text.getText().substring(0, text.getText().length()-1));
        if(key.text != null) text.setText(text.getText()+character);
        return text.getText();
    }

    /**
     * Getter
     * @return this.isLocked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Setter
     * @param locked Locked or not
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Getter
     * @return this.x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     * @return this.font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Setter
     * @param x New X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Getter
     * @return this.y
     */
    public int getY() {
        return y;
    }

    /**
     * Setter
     * @param y New Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter
     * @return this.width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setter. Also updates position and bounds of text and description
     * @param width New width
     */
    public void setWidth(int width) {
        this.width = width;
        FontMetrics metrics = Utils.getFontMetrics(font);
        this.text = new Text(x + 10, y+height/2+metrics.getHeight()/4, width-15, height, font, Color.BLACK, text.getText());
        this.description = new Text(x + 10, y+height/2+metrics.getHeight()/4, width-15, height, font, Color.GRAY, description.getText());
    }

    /**
     * Getter
     * @return this.height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setter. Also updates position and bounds of text and description
     * @param height New height
     */
    public void setHeight(int height) {
        this.height = height;
        FontMetrics metrics = Utils.getFontMetrics(font);
        this.text = new Text(x + 10, y+height/2+metrics.getHeight()/4, width-15, height, font, Color.BLACK, text.getText());
        this.description = new Text(x + 10, y+height/2+metrics.getHeight()/4, width-15, height, font, Color.GRAY, description.getText());
    }

    /**
     * Getter
     * @return this.text.getText()
     */
    public String getText() {
        return text.getText();
    }

    /**
     * Setter. {@link Text#setText(String)} is called
     * @param text New text
     */
    public void setText(String text) {
        this.text.setText(text);
    }

    /**
     * Getter
     * @return this.description.getText()
     */
    public String getDescription() {
        return description.getText();
    }

    /**
     * Setter. {@link Text#setText(String)} is called
     * @param description New description
     */
    public void setDescription(String description) {
        this.description.setText(description);
    }

    /**
     * @return Whether field is empty or not
     */
    public boolean isEmpty() {
        return text.getText().equals("");
    }
}
