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
import com.jnngl.totalcomputers.system.TotalOS;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Keyboard overlay
 */
public class Keyboard extends Overlay {

    /**
     * Keys on keyboard
     */
    public enum Keys {
        /**
         * Tilde key
         */
        TILDE("~"),

        /**
         * Backtick key
         */
        BACKTICK("`"),

        /**
         * One key
         */
        ONE("1"),

        /**
         * Exclamation mark key
         */
        EXCLAMATION_MARK("!"),

        /**
         * Two key
         */
        TWO("2"),

        /**
         * At sign key
         */
        AT_SIGN("@"),

        /**
         * Three key
         */
        THREE("3"),

        /**
         * Number sign key
         */
        NUMBER_SIGN("#"),

        /**
         * Four key
         */
        FOUR("4"),

        /**
         * Dollar sign key
         */
        DOLLAR_SIGN("$"),

        /**
         * Five key
         */
        FIVE("5"),

        /**
         * Percent sign key
         */
        PERCENT_SIGN("%"),

        /**
         * Six key
         */
        SIX("6"),

        /**
         * Caret key
         */
        CARET("^"),

        /**
         * Seven key
         */
        SEVEN("7"),

        /**
         * Ampersand key
         */
        AMPERSAND("&"),

        /**
         * Eight key
         */
        EIGHT("8"),

        /**
         * Asterisk key
         */
        ASTERISK("*"),

        /**
         * Nine key
         */
        NINE("9"),

        /**
         * Left parenthesis key
         */
        LEFT_PARENTHESIS("("),

        /**
         * Zero key
         */
        ZERO("0"),

        /**
         * Right parenthesis key
         */
        RIGHT_PARENTHESIS(")"),

        /**
         * Minus sign key
         */
        MINUS_SIGN("-"),

        /**
         * Underscore key
         */
        UNDERSCORE("_"),

        /**
         * Plus sign key
         */
        PLUS_SIGN("+"),

        /**
         * Equals sign key
         */
        EQUALS_SIGN("="),

        /**
         * Q key
         */
        Q("Q"),

        /**
         * W key
         */
        W("W"),

        /**
         * E key
         */
        E("E"),

        /**
         * R key
         */
        R("R"),

        /**
         * T key
         */
        T("T"),

        /**
         * Y key
         */
        Y("Y"),

        /**
         * U key
         */
        U("U"),

        /**
         * I key
         */
        I("I"),

        /**
         * O key
         */
        O("O"),

        /**
         * P key
         */
        P("P"),

        /**
         * Left bracket key
         */
        LEFT_BRACKET("["),

        /**
         * Left brace key
         */
        LEFT_BRACE("{"),

        /**
         * Right bracket key
         */
        RIGHT_BRACKET("]"),

        /**
         * Right brace key
         */
        RIGHT_BRACE("}"),

        /**
         * Backslash key
         */
        BACKSLASH("\\"),

        /**
         * Vertical line key
         */
        VERTICAL_LINE("|"),

        /**
         * OK key
         */
        OK(null),

        /**
         * A key
         */
        A("A"),

        /**
         * S key
         */
        S("S"),

        /**
         * D key
         */
        D("D"),

        /**
         * F key
         */
        F("F"),

        /**
         * G key
         */
        G("G"),

        /**
         * H key
         */
        H("H"),

        /**
         * J key
         */
        J("J"),

        /**
         * K key
         */
        K("K"),

        /**
         * L key
         */
        L("L"),

        /**
         * Semicolon key
         */
        SEMICOLON(";"),

        /**
         * Colon key
         */
        COLON(":"),

        /**
         * Apostrophe key
         */
        APOSTROPHE("'"),

        /**
         * Quotation mark key
         */
        QUOTATION_MARK("\""),

        /**
         * Z key
         */
        Z("Z"),

        /**
         * X key
         */
        X("X"),

        /**
         * C key
         */
        C("C"),

        /**
         * V key
         */
        V("V"),

        /**
         * B key
         */
        B("B"),

        /**
         * N key
         */
        N("N"),

        /**
         * M key
         */
        M("M"),

        /**
         * Comma key
         */
        COMMA(","),

        /**
         * Less than sign key
         */
        LESS_THAN_SIGN("<"),

        /**
         * Period key
         */
        PERIOD("."),

        /**
         * Greater than sign key
         */
        GREATER_THAN_SIGN(">"),

        /**
         * Slash key
         */
        SLASH("/"),

        /**
         * Question mark key
         */
        QUESTION_MARK("?"),

        /**
         * Enter key
         */
        ENTER(null),

        /**
         * Shift key
         */
        SHIFT(null),

        /**
         * CTRL key
         */
        CONTROL(null),

        /**
         * FN key
         */
        FUNCTION(null),

        /**
         * Home key
         */
        HOME(null),

        /**
         * ALT key
         */
        ALT(null),

        /**
         * Space key
         */
        SPACE(" "),

        /**
         * Backspace key
         */
        BACKSPACE(null),

        /**
         * Tab key
         */
        TAB(null),

        /**
         * Lang key
         */
        LANG(null);

        /**
         * Key character
         */
        public final String text;

        Keys(String text) {
            this.text = text;
        }
    }

    private enum ShiftState {
        OFF, FOR_ONE_SYMBOL, ON
    }

    /**
     * Private class for keys
     */
    private static class KeyboardButton {

        public int x, y, w, h;
        public String text, textOnShift;
        public Keys key, keyOnShift;
        private final Keyboard keyboard;
        private static final Color blue = new Color(33, 150, 243);

        private KeyboardButton(float x, float y, float w, float h, String text, String textOnShift, Keys key, Keys keyOnShift, Keyboard keyboard) {
            this.x = (int)x;
            this.y = (int)y;
            this.w = (int)w;
            this.h = (int)h;
            this.text = text;
            this.textOnShift = textOnShift;
            this.key = key;
            this.keyOnShift = keyOnShift;
            this.keyboard = keyboard;
        }

        private void render(Graphics2D g, FontMetrics metrics) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(x, y, w, h, 8, 8);
            String string = keyboard.shiftState != ShiftState.OFF? textOnShift : text;
            if(key == Keys.SHIFT && keyboard.shiftState == ShiftState.ON) g.setColor(blue);
            else g.setColor(Color.BLACK);
            g.drawString(string, x+w/2-metrics.stringWidth(string)/2, y+h/2+metrics.getHeight()/4);
        }

        private String processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
            if(type != TotalComputers.InputInfo.InteractType.LEFT_CLICK) return null;
            if(x >= this.x && y >= this.y && x <= this.x+w && y <= this.y+h) {
                if(keyboard.shiftState != ShiftState.OFF) {
                    if(keyboard.shiftState == ShiftState.FOR_ONE_SYMBOL && key != Keys.SHIFT) keyboard.shiftState = ShiftState.OFF;
                    return textOnShift;
                }
                return text;
            }
            return null;
        }

    }

    /**
     * Simple interface
     */
    public interface KeyboardListener {

        /**
         * Called when key typed
         * @param text Pressed key character
         * @param key Pressed key
         * @param keyboard this
         * @return Text that will be displayed
         */
        String keyTyped(String text, Keys key, Keyboard keyboard);

    }

    private boolean invoked = false;
    private final Font font;
    private final List<KeyboardButton> buttons;
    private ShiftState shiftState;
    private String displayableText;
    private KeyboardListener listener;

    /**
     * Constructor
     * @param os TotalOS instance
     */
    public Keyboard(TotalOS os) {
        super(os);
        displayableText = "";
        shiftState = ShiftState.OFF;
        font = os.baseFont.deriveFont(os.screenHeight/128.f*4f);
        buttons = new ArrayList<>();
        final int paddingX = (os.screenWidth/14-os.screenWidth/15)/2;
        final int paddingY = (os.screenHeight/10-os.screenHeight/11)/2;
        final int width = os.screenWidth/15;
        final int height = os.screenHeight/11;
        final int halfHeight = os.screenHeight/2;
        final int offsetX = os.screenWidth/14;
        final int offsetY = os.screenHeight/10;
        buttons.add(new KeyboardButton(paddingX, halfHeight+paddingY, width*0.5f, height, "`", "~", Keys.BACKTICK, Keys.TILDE, this));
        buttons.add(new KeyboardButton(0.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "1", "!", Keys.ONE, Keys.EXCLAMATION_MARK, this));
        buttons.add(new KeyboardButton(1.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "2", "@", Keys.TWO, Keys.AT_SIGN, this));
        buttons.add(new KeyboardButton(2.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "3", "#", Keys.THREE, Keys.NUMBER_SIGN, this));
        buttons.add(new KeyboardButton(3.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "4", "$", Keys.FOUR, Keys.DOLLAR_SIGN, this));
        buttons.add(new KeyboardButton(4.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "5", "%", Keys.FIVE, Keys.PERCENT_SIGN, this));
        buttons.add(new KeyboardButton(5.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "6", "^", Keys.SIX, Keys.CARET, this));
        buttons.add(new KeyboardButton(6.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "7", "&", Keys.SEVEN, Keys.AMPERSAND, this));
        buttons.add(new KeyboardButton(7.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "8", "*", Keys.EIGHT, Keys.ASTERISK, this));
        buttons.add(new KeyboardButton(8.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "9", "(", Keys.NINE, Keys.LEFT_PARENTHESIS, this));
        buttons.add(new KeyboardButton(9.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "0", ")", Keys.ZERO, Keys.RIGHT_PARENTHESIS, this));
        buttons.add(new KeyboardButton(10.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "-", "_", Keys.MINUS_SIGN, Keys.UNDERSCORE, this));
        buttons.add(new KeyboardButton(11.5f*offsetX+paddingX, halfHeight+paddingY, width, height, "+", "=", Keys.PLUS_SIGN, Keys.EQUALS_SIGN, this));
        buttons.add(new KeyboardButton(12.5f*offsetX+paddingX, halfHeight+paddingY, width*1.5f, height, "←", "←", Keys.BACKSPACE, Keys.BACKSPACE, this));
        buttons.add(new KeyboardButton(paddingX, offsetY+halfHeight+paddingY, width, height, "Tab", "Tab", Keys.TAB, Keys.TAB, this));
        buttons.add(new KeyboardButton(offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "q", "Q", Keys.Q, Keys.Q, this));
        buttons.add(new KeyboardButton(2*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "w", "W", Keys.W, Keys.W, this));
        buttons.add(new KeyboardButton(3*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "e", "E", Keys.E, Keys.E, this));
        buttons.add(new KeyboardButton(4*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "r", "R", Keys.R, Keys.R, this));
        buttons.add(new KeyboardButton(5*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "t", "T", Keys.T, Keys.T, this));
        buttons.add(new KeyboardButton(6*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "y", "Y", Keys.Y, Keys.Y, this));
        buttons.add(new KeyboardButton(7*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "u", "U", Keys.U, Keys.U, this));
        buttons.add(new KeyboardButton(8*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "i", "I", Keys.I, Keys.I, this));
        buttons.add(new KeyboardButton(9*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "o", "O", Keys.O, Keys.O, this));
        buttons.add(new KeyboardButton(10*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "p", "P", Keys.P, Keys.P, this));
        buttons.add(new KeyboardButton(11*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "[", "{", Keys.LEFT_BRACKET, Keys.LEFT_BRACE, this));
        buttons.add(new KeyboardButton(12*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "]", "}", Keys.RIGHT_BRACKET, Keys.RIGHT_BRACE, this));
        buttons.add(new KeyboardButton(13*offsetX+paddingX, offsetY+halfHeight+paddingY, width, height, "\\", "|", Keys.BACKSLASH, Keys.VERTICAL_LINE, this));
        buttons.add(new KeyboardButton(paddingX, 2*offsetY+halfHeight+paddingY, width*1.5f, height, "OK", "OK", Keys.OK, Keys.OK, this));
        buttons.add(new KeyboardButton(1.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "a", "A", Keys.A, Keys.A, this));
        buttons.add(new KeyboardButton(2.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "s", "S", Keys.S, Keys.S, this));
        buttons.add(new KeyboardButton(3.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "d", "D", Keys.D, Keys.D, this));
        buttons.add(new KeyboardButton(4.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "f", "F", Keys.F, Keys.F, this));
        buttons.add(new KeyboardButton(5.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "g", "G", Keys.G, Keys.G, this));
        buttons.add(new KeyboardButton(6.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "h", "H", Keys.H, Keys.H, this));
        buttons.add(new KeyboardButton(7.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "j", "J", Keys.J, Keys.J, this));
        buttons.add(new KeyboardButton(8.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "k", "K", Keys.K, Keys.K, this));
        buttons.add(new KeyboardButton(9.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "l", "L", Keys.L, Keys.L, this));
        buttons.add(new KeyboardButton(10.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, ";", ":", Keys.SEMICOLON, Keys.COLON, this));
        buttons.add(new KeyboardButton(11.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width, height, "'", "\"", Keys.APOSTROPHE, Keys.QUOTATION_MARK, this));
        buttons.add(new KeyboardButton(12.5f*offsetX+paddingX, 2*offsetY+halfHeight+paddingY, width*1.5f, height, "Enter", "Enter", Keys.ENTER, Keys.ENTER, this));
        buttons.add(new KeyboardButton(paddingX, 3*offsetY+halfHeight+paddingY, width*2, height, "Shift", "Shift", Keys.SHIFT, Keys.SHIFT, this));
        buttons.add(new KeyboardButton(2*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "z", "Z", Keys.Z, Keys.Z, this));
        buttons.add(new KeyboardButton(3*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "x", "X", Keys.X, Keys.X, this));
        buttons.add(new KeyboardButton(4*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "c", "C", Keys.C, Keys.C, this));
        buttons.add(new KeyboardButton(5*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "v", "V", Keys.V, Keys.V, this));
        buttons.add(new KeyboardButton(6*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "b", "B", Keys.B, Keys.B, this));
        buttons.add(new KeyboardButton(7*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "n", "N", Keys.N, Keys.N, this));
        buttons.add(new KeyboardButton(8*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "m", "M", Keys.M, Keys.M, this));
        buttons.add(new KeyboardButton(9*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, ",", "<", Keys.COLON, Keys.LESS_THAN_SIGN, this));
        buttons.add(new KeyboardButton(10*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, ".", ">", Keys.PERIOD, Keys.GREATER_THAN_SIGN, this));
        buttons.add(new KeyboardButton(11*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width, height, "/", "?", Keys.SLASH, Keys.QUESTION_MARK, this));
        buttons.add(new KeyboardButton(12*offsetX+paddingX, 3*offsetY+halfHeight+paddingY, width*2, height, "Shift", "Shift", Keys.SHIFT, Keys.SHIFT, this));
        buttons.add(new KeyboardButton(paddingX, 4*offsetY+halfHeight+paddingY, width, height, "CTRL", "CTRL", Keys.CONTROL, Keys.CONTROL, this));
        buttons.add(new KeyboardButton(offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "FN", "FN", Keys.FUNCTION, Keys.FUNCTION, this));
        buttons.add(new KeyboardButton(2*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "Home", "Home", Keys.HOME, Keys.HOME, this));
        buttons.add(new KeyboardButton(3*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "ALT", "ALT", Keys.ALT, Keys.ALT, this));
        buttons.add(new KeyboardButton(4*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width*6, height, " ", " ", Keys.SPACE, Keys.SPACE, this));
        buttons.add(new KeyboardButton(10*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "ALT", "ALT", Keys.ALT, Keys.ALT, this));
        buttons.add(new KeyboardButton(11*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "Lang", "Lang", Keys.LANG, Keys.LANG, this));
        buttons.add(new KeyboardButton(12*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "FN", "FN", Keys.FUNCTION, Keys.FUNCTION, this));
        buttons.add(new KeyboardButton(13*offsetX+paddingX, 4*offsetY+halfHeight+paddingY, width, height, "CTRL", "CTRL", Keys.CONTROL, Keys.CONTROL, this));
        os.keyboard = this;
    }

    /**
     * Renders keyboard overlay
     * @param g Graphics2D instance
     */
    public void render(Graphics2D g) {
        if(!invoked) return;
        g.setColor(new Color(0, 0, 0, 0.66f));
        g.fillRect(0, 0, os.screenWidth, os.screenHeight);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, os.screenHeight/2, os.screenWidth, os.screenHeight/2);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        for(KeyboardButton button : buttons) button.render(g, metrics);
        g.setColor(Color.WHITE);
        g.drawString(displayableText, os.screenWidth/2-metrics.stringWidth(displayableText)/2, os.screenHeight/4+metrics.getHeight()/4);
    }

    /**
     * Call it when shift is pressed
     */
    public void shiftPressed() {
        if(shiftState == ShiftState.OFF) shiftState = ShiftState.FOR_ONE_SYMBOL;
        else if(shiftState == ShiftState.FOR_ONE_SYMBOL) shiftState = ShiftState.ON;
        else if(shiftState == ShiftState.ON) shiftState = ShiftState.OFF;
    }

    /**
     * Handles input
     * @param x X coordinate of the touch
     * @param y Y coordinate of the touch
     * @param type See {@link com.jnngl.totalcomputers.TotalComputers.InputInfo.InteractType}
     */
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        if(!invoked) return;
        for(KeyboardButton button : buttons) {
            String output;
            if((output = button.processInput(x, y, type)) != null) {
                if(listener != null)
                    displayableText = listener.keyTyped(output, shiftState == ShiftState.OFF? button.key : button.keyOnShift, this);
                if(button.key == Keys.OK) closeKeyboard();
                if(button.key == Keys.SHIFT) shiftPressed();
            }
        }
    }

    /**
     * Implementation
     * @return See {@link Overlay#isControlTaken()}
     */
    public boolean isControlTaken() {
        return invoked;
    }

    /**
     * Shows keyboard
     * @param listener Key pressed event
     * @param displayableText Initial text
     */
    public void invokeKeyboard(KeyboardListener listener, String displayableText) {
        invoked = true;
        this.listener = listener;
        this.displayableText = displayableText;
    }

    /**
     * Hides keyboard
     */
    public void closeKeyboard() {
        invoked = false;
        listener = null;
        displayableText = "";
    }

}
