package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Event;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Field;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Calculator extends WindowApplication {

    private List<Button> buttons;
    private Field current;
    private Field prev;

    private enum Action {
        MULTIPLY,
        DIVIDE,
        ADD,
        SUBTRACT,
        L_SHIFT,
        R_SHIFT,
        MOD;

        public static double operate(double a, double b, Action action) {
            return switch (action) {
                case ADD -> a+b;
                case MOD -> a%b;
                case DIVIDE -> a/b;
                case L_SHIFT -> (int)a<<(int)b;
                case R_SHIFT -> (int)a>>(int)b;
                case MULTIPLY -> a*b;
                case SUBTRACT -> a-b;
            };
        }
    }

    private Action action;

    public static void main(String[] args) {
        ApplicationHandler.open(Calculator.class, args[0]);
    }

    public Calculator(TotalOS os, String path) {
        super(os, "Calculator", (int)(os.screenHeight/3*2/1.5f), os.screenHeight/3*2, path);
    }

    private void updateLayout() {
        String prevData = prev == null? "" : prev.getText();
        String currentData = current == null? "0" : current.getText();
        Font font = os.baseFont.deriveFont((float)os.screenHeight/128*3);
        FontMetrics metrics = Utils.getFontMetrics(font);
        prev = new Field(0, 0, getWidth(), metrics.getHeight(), font, prevData, "", os.keyboard);
        prev.setLocked(true);
        font = os.baseFont.deriveFont((float)os.screenHeight/128*6);
        metrics = Utils.getFontMetrics(font);
        current = new Field(0, prev.getHeight(), getWidth(), metrics.getHeight(), font, currentData, "",
                os.keyboard);
        buttons = new ArrayList<>();
        final int gridSx = 4, gridSy = 5;
        final int gridW = getWidth()/gridSx, gridH = (getHeight()-prev.getHeight()-current.getHeight())/gridSy;
        final String[] texts = {
                "<<", ">>", "CE", "<-",
                "7" , "8" , "9" , "/" ,
                "4" , "5" , "6" , "*" ,
                "1" , "2" , "3" , "-" ,
                "%" , "0" , "=" , "+"
        };
        final Event[] events = {
                () -> { push(); action = Action.L_SHIFT; },
                () -> { push(); action = Action.R_SHIFT; },
                () -> { current.setText("0"); prev.setText(""); action = null; },
                () -> { current.setText(current.getText().substring(0, current.getText().length()-1)); if(current.getText().isEmpty()) current.setText("0"); },
                () -> { current.setText(current.getText()+"7"); },
                () -> { current.setText(current.getText()+"8"); },
                () -> { current.setText(current.getText()+"9"); },
                () -> { push(); action = Action.DIVIDE; },
                () -> { current.setText(current.getText()+"4"); },
                () -> { current.setText(current.getText()+"5"); },
                () -> { current.setText(current.getText()+"6"); },
                () -> { push(); action = Action.MULTIPLY; },
                () -> { current.setText(current.getText()+"1"); },
                () -> { current.setText(current.getText()+"2"); },
                () -> { current.setText(current.getText()+"3"); },
                () -> { push(); action = Action.SUBTRACT; },
                () -> { push(); action = Action.MOD; },
                () -> { current.setText(current.getText()+"0"); },
                () -> { push(); swap(); },
                () -> { push(); action = Action.ADD; }
        };
        for(int gridY = 0; gridY < 5; gridY++) {
            for(int gridX = 0; gridX < 4; gridX++) {
                Button btn = new Button(Button.ButtonColor.WHITE,
                        gridX*gridW,
                        gridY*gridH+prev.getHeight()+current.getHeight(),
                        gridW / ((gridX == 0 && gridY == 4)? 2 : 1), gridH, font,
                        texts[gridY*gridSx+gridX]);
                int finalGridY = gridY;
                int finalGridX = gridX;
                btn.registerClickEvent(() -> {
                    if(current.getText().contains("Infinity") || prev.getText().contains("Infinity") ||
                            current.getText().contains("NaN") || prev.getText().contains("NaN")) {
                        current.setText("0");
                        prev.setText("");
                    }
                    events[finalGridY*gridSx+finalGridX].action();
                });
                buttons.add(btn);
            }
        }
        buttons.add(new Button(Button.ButtonColor.WHITE, gridW/2, gridH*4+prev.getHeight()+current.getHeight(),
                gridW/2, gridH, font, "."));
        buttons.get(20).registerClickEvent(() -> current.setText(current.getText()+"."));
    }

    private void push() {
        if(prev.isEmpty()) {
            prev.setText(current.getText());
            current.setText("0");
            return;
        }

        double a = Double.parseDouble(prev.getText());
        double b = Double.parseDouble(current.getText());

        if(action != null) {
            double prevNumber = Action.operate(a, b, action);
            prev.setText(Double.toString(prevNumber));
            current.setText("0");
        } else {
            prev.setText(current.getText());
            current.setText("0");
        }
        action = null;
    }

    private void swap() {
        String tmp = prev.getText();
        prev.setText(current.getText());
        current.setText(tmp);
    }

    @Override
    protected void onStart() {
        updateLayout();
        addResizeEvent(new ResizeEvent() {
            @Override
            public void onResize(int width, int height) {
                updateLayout();
            }

            @Override
            public void onMaximize(int width, int height) {
                updateLayout();
            }

            @Override
            public void onUnmaximize(int width, int height) {
                updateLayout();
            }
        });
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void update() {
        if(current.getText().startsWith("0") && !current.getText().equals("0"))
            current.setText(current.getText().substring(1));
        buttons.get(18).setLocked(current.getText().endsWith(".") || current.getText().endsWith(","));
        buttons.get(0).setLocked(buttons.get(18).isLocked());
        buttons.get(1).setLocked(buttons.get(18).isLocked());
        buttons.get(7).setLocked(buttons.get(18).isLocked());
        buttons.get(11).setLocked(buttons.get(18).isLocked());
        buttons.get(15).setLocked(buttons.get(18).isLocked());
        buttons.get(16).setLocked(buttons.get(18).isLocked());
        buttons.get(19).setLocked(buttons.get(18).isLocked());
        buttons.get(20).setLocked(buttons.get(18).isLocked() || current.getText().contains("."));
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        for(Button btn : buttons) btn.render(g);
        prev.render(g);
        current.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        buttons.forEach((btn) -> btn.processInput(x, y, type));
    }
}
