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
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.states.State;
import com.jnngl.totalcomputers.system.states.StateManager;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Image;
import com.jnngl.totalcomputers.system.ui.Rectangle;
import com.jnngl.totalcomputers.system.ui.RoundRectangle;
import com.jnngl.totalcomputers.system.ui.Text;

import java.awt.*;

/**
 * Final stage of setup
 */
public class SetupFinishedState extends State {

    private final Button restart, back;
    private final Rectangle background;
    private final RoundRectangle panel;
    private final Text setupComplete, restartRequired;
    private final Image icon;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public SetupFinishedState(StateManager stateManager, TotalOS os) {
        super(stateManager, os);
        final Font bigFont = os.baseFont.deriveFont((float) os.screenHeight/128*6);
        final Font uiFont  = os.baseFont.deriveFont((float) os.screenHeight/128*3);

        background = new Rectangle(Color.DARK_GRAY, 0, 0, os.screenWidth, os.screenHeight);
        int fy, sy;
        panel = new RoundRectangle(Color.LIGHT_GRAY, ndcX(-0.66f), fy=ndcY( 0.66f), (int)(os.screenWidth*0.66f), (int)(os.screenHeight*0.66f), 6);
        FontMetrics metrics = Utils.getFontMetrics(bigFont);
        setupComplete = new Text(ndcX(0)-metrics.stringWidth(os.localization.setupComplete())/2, sy=fy+metrics.getHeight(), os.screenWidth, os.screenHeight, bigFont, Color.BLACK, os.localization.setupComplete());

        sy += metrics.getHeight()/2;

        metrics = Utils.getFontMetrics(uiFont);

        restartRequired = new Text(ndcX(0)-metrics.stringWidth(os.localization.restartRequired())/2, fy = sy + metrics.getHeight()/2, os.screenWidth, os.screenHeight, uiFont, Color.BLACK, os.localization.restartRequired());

        fy += metrics.getHeight()*2;

        int size = ndcY(-0.45f)-fy-metrics.getHeight()*2;
        icon = new Image(ndcX(0)-size/2, fy, size, size, os.fs.getResourceImage("done"));

        restart = new Button(Button.ButtonColor.BLUE, ndcX(0), ndcY(-0.5f), (int)(os.screenWidth * 0.15f), (int) (os.screenHeight * 0.05f), uiFont, os.localization.restartNow());
        restart.registerClickEvent(this::restart);
        back = new Button(Button.ButtonColor.WHITE, ndcX(-0.325f), ndcY(-0.5f), restart.getWidth(), restart.getHeight(), uiFont, os.localization.back());
        back.registerClickEvent(this::goBack);
    }

    private void restart() {
        os.fs.setConfigured();
        os.restart();
    }

    private void goBack() {
        stateManager.setState(new SetupUserCreationState(stateManager, os));
    }

    /**
     * Update
     */
    @Override
    public void update() {

    }

    /**
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        background.render(g);
        panel.render(g);
        setupComplete.render(g);
        restartRequired.render(g);
        icon.render(g);
        restart.render(g);
        back.render(g);
    }

    /**
     * Input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        restart.processInput(x, y, type);
        back.processInput(x, y, type);
    }
}
