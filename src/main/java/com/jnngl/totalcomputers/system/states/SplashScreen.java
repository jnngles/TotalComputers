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
import com.jnngl.totalcomputers.system.states.setup.SetupLocalizationState;
import com.jnngl.totalcomputers.system.ui.Text;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Splash screen
 */
public class SplashScreen extends State {

    private int splashColor = 0;
    private long tempTime1 = 0;
    private static final long splashTime = TimeUnit.SECONDS.toMillis(2);

    private final Text splashText;

    /**
     * Constructor
     * @param stateManager StateManager instance
     * @param os TotalOS instance
     */
    public SplashScreen(StateManager stateManager, TotalOS os) {
        super(stateManager, os);
        final Font splashFont = os.baseFont.deriveFont((float) os.screenHeight / 5);
        FontMetrics metrics = Utils.getFontMetrics(splashFont);
        splashText = new Text(ndcX(0) - metrics.stringWidth("TotalOS")/2, ndcY(0), os.screenWidth, os.screenHeight, splashFont, Color.BLACK, "TotalOS");
    }

    /**
     * Update
     */
    @Override
    public void update() {
        if(splashColor < 255 && tempTime1 == 0) splashColor += 100;
        if(splashColor > 255 && tempTime1 == 0) splashColor = 255;
        if(splashColor == 255 && tempTime1 == 0) { tempTime1 = System.currentTimeMillis(); }
        if(tempTime1 != 0 && System.currentTimeMillis() - tempTime1 > splashTime) {
            if(splashColor > 0) splashColor -= 100;
            if(splashColor < 0) splashColor = 0;
            if(splashColor == 0) {
                if(os.firstRun) stateManager.setState(new SetupLocalizationState(stateManager, os));
                else stateManager.setState(new LoginScreen(stateManager, os));
            }
        }
    }

    /**
     * Render
     * @param g Graphics2D instance
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, os.screenWidth, os.screenHeight);
        splashText.setColor(new Color(splashColor, splashColor, splashColor));
        splashText.render(g);
    }

    /**
     * Input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link TotalComputers.InputInfo.InteractType}
     */
    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {}
}
