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

package com.jnngl.totalcomputers.system.states.setup;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Localization;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.states.State;
import com.jnngl.totalcomputers.system.states.StateManager;
import com.jnngl.totalcomputers.system.ui.*;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Rectangle;

import java.awt.*;

/**
 * Localization stage of setup
 */
public class SetupLocalizationState extends State {

    private final ElementList languages;
    private final Button next;
    private final Rectangle background;
    private final RoundRectangle window;
    private final Text welcome, language;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public SetupLocalizationState(StateManager stateManager, TotalOS os) {
        super(stateManager, os);
        final Font bigFont = os.baseFont.deriveFont((float) os.screenHeight/128*6);
        final Font uiFont  = os.baseFont.deriveFont((float) os.screenHeight/128*3);

        FontMetrics metrics = Utils.getFontMetrics(bigFont);

        background = new Rectangle(Color.DARK_GRAY, 0, 0, os.screenWidth, os.screenHeight);
        int fy, sy;
        window = new RoundRectangle(Color.LIGHT_GRAY, ndcX(-0.66f), fy=ndcY( 0.66f), (int)(os.screenWidth*0.66f), (int)(os.screenHeight*0.66f), 6);
        welcome = new Text(ndcX(0)-metrics.stringWidth("Welcome")/2, sy=fy+metrics.getHeight(), os.screenWidth, os.screenHeight, bigFont, Color.BLACK, "Welcome");
        sy += metrics.getHeight()/2;

        metrics = Utils.getFontMetrics(uiFont);

        language = new Text(ndcX(0)-metrics.stringWidth("Select your language:")/2, fy = sy + metrics.getHeight()/2, os.screenWidth, os.screenHeight, uiFont, Color.BLACK, "Select your language:");
        fy += metrics.getHeight()*1.5f;

        languages = new ElementList(ndcX(-0.5f), fy, (int) (os.screenWidth * 0.33f), sy = (int) (os.screenHeight * 0.45f), uiFont, "English", "Русский");
        languages.registerItemSelectEvent(this::unlockButton);

        sy+=fy-(int) (os.screenHeight * 0.05f);

        next = new Button(Button.ButtonColor.BLUE, languages.getX()+languages.getWidth()+(int) (os.screenHeight * 0.025f), sy, (int) (os.screenHeight * 0.3f), (int) (os.screenHeight * 0.05f), uiFont, "Next");
        next.setLocked(true);
        next.registerClickEvent(this::nextPressed);
    }

    /**
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        background.render(g);
        window.render(g);
        welcome.render(g);
        language.render(g);
        languages.render(g);
        next.render(g);
    }

    /**
     * Update
     */
    @Override
    public void update() {
    }

    /**
     * Input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        languages.processInput(x, y, type);
        next.processInput(x, y, type);
    }

    private void unlockButton() {
        next.setLocked(false);
    }

    private void nextPressed() {
        if(languages.getSelectedIndex() == 0) { // English
            os.localization = new Localization.English();
        } else if(languages.getSelectedIndex() == 1) { // Russian
            os.localization = new Localization.Russian();
        }
        os.fs.writeLocalization(os.localization);
        stateManager.setState(new SetupUserCreationState(stateManager, os));
    }

}
