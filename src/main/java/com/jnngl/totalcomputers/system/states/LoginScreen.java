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

package com.jnngl.totalcomputers.system.states;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.security.Password;
import com.jnngl.totalcomputers.system.ui.Field;
import com.jnngl.totalcomputers.system.ui.RoundRectangle;
import com.jnngl.totalcomputers.system.ui.Text;
import com.jnngl.totalcomputers.system.ui.Rectangle;
import com.jnngl.totalcomputers.system.ui.Button;

import java.awt.*;

/**
 * Login screen
 */
public class LoginScreen extends State {

    private final Rectangle background;
    private final RoundRectangle panel;
    private final Text username;
    private final Field password;
    private final Button signIn;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public LoginScreen(StateManager stateManager, TotalOS os) {
        super(stateManager, os);

        final Font uiFont  = os.baseFont.deriveFont((float) os.screenHeight/128*3);
        final Font bigFont  = os.baseFont.deriveFont((float) os.screenHeight/128*6);

        FontMetrics metrics = Utils.getFontMetrics(bigFont);

        background = new Rectangle(Color.DARK_GRAY, 0, 0, os.screenWidth, os.screenHeight);
        panel = new RoundRectangle(Color.LIGHT_GRAY, ndcX(-0.5f), ndcY(0.3f), (int)(os.screenWidth*0.5f), (int)(os.screenHeight*0.2f), 24);
        username = new Text(os.screenWidth/2-metrics.stringWidth(os.account.name)/2, os.screenHeight/2-metrics.getHeight(), bigFont, Color.BLACK, os.account.name);
        if(os.account.usePassword) {
            password = new Field(ndcX(-0.475f), os.screenHeight/2-uiFont.getSize(), (int)(os.screenWidth*0.375f), bigFont.getSize(), uiFont, "", os.localization.password(), os.keyboard);
            password.setKeyTypedEvent(this::passwordKeyTyped);
            signIn = new Button(Button.ButtonColor.BLUE, password.getX()+password.getWidth()+(int)(os.screenWidth*0.0125f), password.getY(), ndcX(0.475f)-(password.getX()+password.getWidth()+(int)(os.screenWidth*0.0125f)), password.getHeight(), uiFont, os.localization.signIn());
        } else {
            password = null;
            signIn = new Button(Button.ButtonColor.BLUE, os.screenWidth/2-(int)(os.screenWidth*0.05f), os.screenHeight/2-uiFont.getSize(), (int)(os.screenWidth*0.1f), bigFont.getSize(), uiFont, os.localization.signIn());
        }
        signIn.registerClickEvent(this::tryLogIn);
    }

    private void tryLogIn() {
        if(os.account.usePassword && !Password.equals(os.account.passwordHash, password.getText())) {
            password.setText("");
            password.setDescription(os.localization.wrongPassword());
            return;
        }

        stateManager.setState(new Desktop(stateManager, os));
    }

    private String passwordKeyTyped(String character, Keyboard.Keys key, Keyboard keyboard) {
        if(key == Keyboard.Keys.SPACE) return password.getText();
        if(key == Keyboard.Keys.ENTER) keyboard.closeKeyboard();
        if(key == Keyboard.Keys.BACKSPACE) {
            if (password.getText().length() > 0)
                password.setText(password.getText().substring(0, password.getText().length() - 1));
        }
        if(password.getText().length() >= 15) return password.getText();
        if(key.text != null) password.setText(password.getText()+character);
        return password.getText();
    }

    /**
     * Update
     */
    @Override
    public void update() {}

    /**
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        background.render(g);
        panel.render(g);
        username.render(g);
        if(os.account.usePassword) password.render(g);
        signIn.render(g);
    }

    /**
     * Input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(os.account.usePassword) password.processInput(x, y, type);
        signIn.processInput(x, y, type);
    }
}
