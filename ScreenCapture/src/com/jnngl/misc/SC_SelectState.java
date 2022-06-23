package com.jnngl.misc;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.Utils;
import com.jnngl.totalcomputers.system.ui.ElementList;
import com.jnngl.totalcomputers.system.ui.Button;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SC_SelectState extends SC_AppState {

    private final List<ByteBuffer> hwnds = new ArrayList<>();
    private final Button startBtn;
    private final ElementList variants;

    public SC_SelectState(ScreenCapture application) {
        super(application);
        Font font = application.os().baseFont.deriveFont((float) application.os().screenHeight / 128 * 3);
        startBtn = new Button(Button.ButtonColor.BLUE, 0, 0, application.getWidth(),
                Utils.getFontMetrics(font).getHeight(), font, "Start Capture");
        variants = new ElementList(0, startBtn.getHeight(), application.getWidth(),
                application.getHeight() - startBtn.getHeight(), font, "Screen");
        for(ByteBuffer hwnd : application.win32applications()) {
            String name = application.win32appName(hwnd);
            if(name.isBlank()) continue;
            hwnds.add(hwnd);
            variants.addEntry(name);
        }
        startBtn.registerClickEvent(() -> {
            if(variants.getSelectedIndex() < 0) return;
            SC_CaptureState state = new SC_CaptureState(application);
            if(variants.getSelectedIndex() > 0)
                state.setHWND(hwnds.get(variants.getSelectedIndex()-1));
            application.state = state;
        });
    }

    @Override
    public void onResize() {
        startBtn.setWidth(application.getWidth());
        variants.setWidth(application.getWidth());
        variants.setHeight(application.getHeight()-startBtn.getHeight());
    }

    @Override
    public void render(Graphics2D g) {
        startBtn.render(g);
        variants.render(g);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {
        startBtn.processInput(x, y, type);
        variants.processInput(x, y, type);
    }

}
