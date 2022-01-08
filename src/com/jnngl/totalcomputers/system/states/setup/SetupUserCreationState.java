/*
    Computers are now in minecraft!
    Copyright (C) 2021  JNNGL

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

package com.jnngl.totalcomputers.system.states.setup;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Account;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.security.Password;
import com.jnngl.totalcomputers.system.states.State;
import com.jnngl.totalcomputers.system.states.StateManager;
import com.jnngl.totalcomputers.system.ui.*;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Rectangle;

import java.awt.*;

/**
 * User create stage of setup
 */
public class SetupUserCreationState extends State {

    private final Field computerName, password;
    private final Button next, back;
    private final RadioBox usePassword;
    private final RadioBoxSystem passwordUsage;
    private final Rectangle background;
    private final RoundRectangle panel;
    private final Text enterData, computerNameLabel, passwordLabel;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public SetupUserCreationState(StateManager stateManager, TotalOS os) {
        super(stateManager, os);
        final Font bigFont = os.baseFont.deriveFont((float) os.screenHeight/128*6);
        final Font uiFont  = os.baseFont.deriveFont((float) os.screenHeight/128*3);

        background = new Rectangle(Color.DARK_GRAY, 0, 0, os.screenWidth, os.screenHeight);
        int fy, sy;
        panel = new RoundRectangle(Color.LIGHT_GRAY, ndcX(-0.66f), fy=ndcY( 0.66f), (int)(os.screenWidth*0.66f), (int)(os.screenHeight*0.66f), 6);
        FontMetrics metrics = Utils.getFontMetrics(bigFont);
        enterData = new Text(ndcX(0)-metrics.stringWidth(os.localization.createAComputerAccount())/2, sy=fy+metrics.getHeight(), os.screenWidth, os.screenHeight, bigFont, Color.BLACK, os.localization.createAComputerAccount());
        sy+=metrics.getHeight()*1.5f;
        computerName = new Field(ndcX(0), sy, os.screenWidth / 4, (int) (os.screenHeight * 0.05f), uiFont, os.name, os.localization.computerName(), os.keyboard);
        computerName.setKeyTypedEvent(this::nameKeyTyped);
        metrics = Utils.getFontMetrics(uiFont);
        computerNameLabel = new Text(computerName.getX()-metrics.stringWidth(os.localization.computerName())-(int) (os.screenHeight * 0.025f), computerName.getY()+computerName.getHeight()/2+metrics.getHeight()/4, os.screenWidth, os.screenHeight, uiFont, Color.BLACK, os.localization.computerName()+":");
        sy = computerName.getY() + computerName.getHeight() + (int) (os.screenHeight * 0.025f);
        password = new Field(ndcX(0), sy, os.screenWidth / 4, fy = (int) (os.screenHeight * 0.05f), uiFont, "", os.localization.password(), os.keyboard);
        password.setKeyTypedEvent(this::passwordKeyTyped);
        passwordLabel = new Text(password.getX()-metrics.stringWidth(os.localization.password())-(int) (os.screenHeight * 0.025f), password.getY()+password.getHeight()/2+metrics.getHeight()/4, os.screenWidth, os.screenHeight, uiFont, Color.BLACK, os.localization.password()+":");
        fy+=sy + (int) (os.screenHeight * 0.025f);
        usePassword = new RadioBox(os.localization.requirePassword(), ndcX(0), fy, uiFont, os);
        fy += (int) (os.screenHeight * 0.015f) + usePassword.getSize();
        final RadioBox notUsePassword = new RadioBox(os.localization.doNotRequirePassword(), ndcX(0), fy, uiFont, os);
        passwordUsage = new RadioBoxSystem(0, usePassword, notUsePassword);
        passwordUsage.setListener(this::radioBoxSelected);
        next = new Button(Button.ButtonColor.BLUE, ndcX(0), ndcY(-0.5f), (int)(os.screenWidth * 0.1f), (int) (os.screenHeight * 0.05f), uiFont, os.localization.next());
        next.registerClickEvent(this::nextState);
        back = new Button(Button.ButtonColor.WHITE, ndcX(-0.225f), ndcY(-0.5f), next.getWidth(), next.getHeight(), uiFont, os.localization.back());
        back.registerClickEvent(this::goBack);
    }

    private String nameKeyTyped(String character, Keyboard.Keys key, Keyboard keyboard) {
        if(key == Keyboard.Keys.ENTER) keyboard.closeKeyboard();
        if(key == Keyboard.Keys.SPACE && computerName.isEmpty()) return computerName.getText();
        if(key == Keyboard.Keys.BACKSPACE) {
            if (computerName.getText().length() > 0)
                computerName.setText(computerName.getText().substring(0, computerName.getText().length() - 1));
        }
        if(computerName.getText().length() >= 20) return computerName.getText();
        if(key.text != null) computerName.setText(computerName.getText()+character);
        return computerName.getText();
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
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        background.render(g);
        panel.render(g);
        enterData.render(g);
        computerName.render(g);
        computerNameLabel.render(g);
        password.render(g);
        passwordLabel.render(g);
        passwordUsage.render(g);
        next.render(g);
        back.render(g);
    }

    private void goBack() {
        stateManager.setState(new SetupLocalizationState(stateManager, os));
    }

    private void proceed() {
        stateManager.setState(new SetupFinishedState(stateManager, os));
    }

    private void writeAccount() {
        if(usePassword.isSelected()) {
            if(os.requestAdminRights()) {
                os.fs.writeAccount(os.account = new Account(computerName.getText(), Password.hash(password.getText(), null), true));
                proceed();
            }
            else os.information.displayMessage(Information.Type.ERROR, os.localization.youDoNotHaveAdministratorRights(), null);
            return;
        }
        os.fs.writeAccount(os.account = new Account(computerName.getText(), "", false));
        proceed();
    }

    private void nextState() {
        if(computerName.isEmpty()) os.information.displayMessage(Information.Type.ERROR, os.localization.computerNameCannotBeEmpty(), null);
        else if(usePassword.isSelected() && password.isEmpty()) os.information.displayMessage(Information.Type.ERROR, os.localization.passwordFieldIsEmpty(), null);
        else if(usePassword.isSelected()) os.information.displayMessage(Information.Type.WARNING, os.localization.administratorRightsAreRequiredToContinue(), this::writeAccount);
        else writeAccount();
    }

    private void radioBoxSelected(int index, RadioBox radioBox) {
        password.setLocked(index == 1);
        passwordLabel.setColor(index == 1? Color.GRAY : Color.BLACK);
    }

    /**
     * Input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        computerName.processInput(x, y, type);
        password.processInput(x, y, type);
        passwordUsage.processInput(x, y, type);
        back.processInput(x, y, type);
        next.processInput(x, y, type);
    }

    /**
     * Update
     */
    @Override
    public void update() {

    }
}
