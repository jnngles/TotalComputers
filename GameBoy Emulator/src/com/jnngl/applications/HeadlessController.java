package com.jnngl.applications;

import eu.rekawek.coffeegb.controller.ButtonListener;
import eu.rekawek.coffeegb.controller.Controller;

public class HeadlessController implements Controller {

    private ButtonListener listener;

    @Override
    public void setButtonListener(ButtonListener listener) {
        this.listener = listener;
    }

    public void keyDown(ButtonListener.Button button) {
        listener.onButtonPress(button);
    }

    public void keyUp(ButtonListener.Button button) {
        listener.onButtonRelease(button);
    }

}
