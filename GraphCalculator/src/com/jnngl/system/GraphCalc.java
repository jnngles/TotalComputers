package com.jnngl.system;

import com.jnngl.totalcomputers.TotalComputers;
import com.jnngl.totalcomputers.system.TotalOS;
import com.jnngl.totalcomputers.system.desktop.ApplicationHandler;
import com.jnngl.totalcomputers.system.desktop.WindowApplication;
import de.progra.charting.DefaultChart;
import de.progra.charting.model.DefaultChartDataModel;
import de.progra.charting.model.FunctionPlotter;
import de.progra.charting.render.LineChartRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphCalc extends WindowApplication {

    private BufferedImage graph;

    public static void main(String[] args) {
        ApplicationHandler.open(GraphCalc.class, args[0]);
    }

    public GraphCalc(TotalOS os, String path) {
        super(os, "Graphing Calculator", os.screenWidth/3*2, os.screenHeight/3*2, path);
    }

    @Override
    protected void onStart() {
        new Thread(() -> {
            String function = "x";
            DefaultChartDataModel data = FunctionPlotter.createChartDataModelInstance(-200, 200,
                    2000, function);
            data.setAutoScale(true);
            DefaultChart chart = new DefaultChart(data, function, DefaultChart.LINEAR_X_LINEAR_Y);
            chart.addChartRenderer(new LineChartRenderer(chart.getCoordSystem(), data), 1);
            chart.setBounds(new Rectangle(0, 0, getWidth(), getHeight()));
            graph = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = graph.createGraphics();
            chart.render(g);
            g.dispose();
        }).start();
    }

    @Override
    protected boolean onClose() {
        return true;
    }

    @Override
    protected void update() {
        renderCanvas();
    }

    @Override
    protected void render(Graphics2D g) {
        if(graph != null)
            g.drawImage(graph, 0, 0, null);
    }

    @Override
    public void processInput(int x, int y, TotalComputers.InputInfo.InteractType type) {

    }

}
