package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Account;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.overlays.Information;
import com.jnngl.totalcomputers.system.overlays.Keyboard;
import com.jnngl.totalcomputers.system.security.Password;
import com.jnngl.totalcomputers.system.ui.*;
import com.jnngl.totalcomputers.system.ui.Button;
import com.jnngl.totalcomputers.system.ui.Rectangle;

import java.awt.*;

public class AccountState extends AppState {

    private final Rectangle bar;
    private final Button back, apply;
    private final Text title;
    private final Font font;
    private final FontMetrics metrics;

    private Field computerName, password;
    private RadioBoxSystem passwordUsage;
    private Text computerNameLabel, passwordLabel;

    private void writeAccount() {
        if(passwordUsage.getSelectedIndex() == 0) {
            if(application.os().requestAdminRights()) {
                application.os().fs.writeAccount(
                        application.os().account = new Account(computerName.getText(),
                                Password.hash(password.getText(), null), true));
            }
            else
                application.os().information.displayMessage(Information.Type.ERROR,
                        "You do not have administrator rights", null);
            return;
        }
        application.os().fs.writeAccount(
                application.os().account =
                        new Account(computerName.getText(), "", false));
    }

    public AccountState(Preferences preferences) {
        super(preferences);
        bar = new com.jnngl.totalcomputers.system.ui.Rectangle(new Color(230, 230, 230), 0, 0,
                application.getWidth(), application.os().screenHeight / 3 * 2 / 10);
        int offset = bar.getHeight() / 5;
        font = application.os().baseFont.deriveFont((float) application.os().screenHeight / 128 * 3);
        metrics = Utils.getFontMetrics(font);
        back = new com.jnngl.totalcomputers.system.ui.Button(com.jnngl.totalcomputers.system.ui.Button.ButtonColor.WHITE
                , offset, offset, metrics.stringWidth("   Back    "),
                bar.getHeight() - offset * 2, font, "Back");
        back.registerClickEvent(() -> application.switchState(new BaseState(application)));
        apply = new com.jnngl.totalcomputers.system.ui.Button(Button.ButtonColor.WHITE,
                back.getX() + back.getWidth() + offset, offset,
                metrics.stringWidth("   Apply   "), back.getHeight(), font, "Apply");
        apply.registerClickEvent(() -> {
            if(computerName.isEmpty())
                application.os().information.displayMessage(Information.Type.ERROR,
                        "Computer name cannot be empty", null);
            else if(passwordUsage.getSelectedIndex() == 0 && password.isEmpty()) application.os().information.displayMessage(Information.Type.ERROR,
                    "Password field cannot be empty", null);
            else if(passwordUsage.getSelectedIndex() == 0) application.os().information.displayMessage(Information.Type.WARNING,
                    "Administrator rights are required to continue", this::writeAccount);
            else writeAccount();
        });
        title = new Text(application.getWidth() / 2 - metrics.stringWidth("Account") / 2,
                bar.getHeight() / 2 - metrics.getHeight() / 2 + metrics.getAscent(), font, Color.BLACK,
                "Account");
        Account account = application.os().account;
        resizeUI(account.usePassword? 0 : 1, account.name, "");
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, application.getWidth(), application.getHeight());
        bar.render(g);
        back.render(g);
        apply.render(g);
        title.render(g);
        computerName.render(g);
        computerNameLabel.render(g);
        password.render(g);
        passwordLabel.render(g);
        passwordUsage.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        back.processInput(x, y, type);
        apply.processInput(x, y, type);
        password.processInput(x, y, type);
        computerName.processInput(x, y, type);
        passwordUsage.processInput(x, y, type);
    }

    @Override
    public void onResize() {
        bar.setWidth(application.getWidth());
        title.setX(application.getWidth()/2-metrics.stringWidth("Account")/2);

        resizeUI(passwordUsage.getSelectedIndex(), computerName.getText(), password.getText());
    }

    private void resizeUI(int selected, String name, String pwd) {
        int offset = bar.getHeight() / 5;
        int fy, sy = application.getHeight()/2-metrics.getHeight()*2-offset*2;
        FontMetrics metrics = Utils.getFontMetrics(font);
        computerName = new Field(application.getWidth()/2, sy, application.getWidth()/4, metrics.getHeight(), font, name, "Computer name", application.os().keyboard);
        computerName.setKeyTypedEvent((character, key, keyboard) -> {
            if(key == Keyboard.Keys.ENTER) keyboard.closeKeyboard();
            if(key == Keyboard.Keys.SPACE && computerName.isEmpty()) return computerName.getText();
            if(key == Keyboard.Keys.BACKSPACE) {
                if (computerName.getText().length() > 0)
                    computerName.setText(computerName.getText().substring(0, computerName.getText().length() - 1));
            }
            if(computerName.getText().length() >= 20) return computerName.getText();
            if(key.text != null) computerName.setText(computerName.getText()+character);
            return computerName.getText();
        });
        computerNameLabel = new Text(computerName.getX()-metrics.stringWidth("Computer name")-offset, computerName.getY()+computerName.getHeight()/2+metrics.getHeight()/4, font, Color.BLACK, "Computer name:");
        sy = computerName.getY() + computerName.getHeight() + offset;
        password = new Field(application.getWidth()/2, sy, application.getWidth() / 4, fy=computerName.getHeight(), font, pwd, "Password", application.os().keyboard);
        password.setKeyTypedEvent((character, key, keyboard) -> {
            if(key == Keyboard.Keys.SPACE) return password.getText();
            if(key == Keyboard.Keys.ENTER) keyboard.closeKeyboard();
            if(key == Keyboard.Keys.BACKSPACE) {
                if (password.getText().length() > 0)
                    password.setText(password.getText().substring(0, password.getText().length() - 1));
            }
            if(password.getText().length() >= 15) return password.getText();
            if(key.text != null) password.setText(password.getText()+character);
            return password.getText();
        });
        passwordLabel = new Text(password.getX()-metrics.stringWidth("Password")-offset, password.getY()+password.getHeight()/2+metrics.getHeight()/4, font, Color.BLACK, "Password:");
        fy += sy + offset*2;
        final RadioBox usePassword = new RadioBox("Require password", application.getWidth()/2, fy, font, application.os());
        fy += offset + usePassword.getSize();
        final RadioBox notUsePassword = new RadioBox("Do not require password", application.getWidth()/2, fy, font, application.os());
        passwordUsage = new RadioBoxSystem(selected, usePassword, notUsePassword);
        passwordUsage.setListener((index, radioBox) -> {
            password.setLocked(index == 1);
            passwordLabel.setColor(index == 1? Color.GRAY : Color.BLACK);
        });
        password.setLocked(passwordUsage.getSelectedIndex() == 1);
        passwordLabel.setColor(passwordUsage.getSelectedIndex() == 1? Color.GRAY : Color.BLACK);
    }
}
