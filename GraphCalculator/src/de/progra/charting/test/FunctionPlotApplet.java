/*
 * FunctionPlotApplet.java
 *
 * Created on 29. Juni 2002, 02:02
 */

package de.progra.charting.test;


import de.progra.charting.*;
import de.progra.charting.swing.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author  smueller
 */
public class FunctionPlotApplet extends JApplet {

    JTextField function = new JTextField("x^2", 20);
    JTextField xmin = new JTextField("-10", 10);
    JTextField xmax = new JTextField("10", 10);
    JButton action = new JButton("Plot");
    ChartPanel chart;
    DefaultChartDataModel model;
    JLabel status = new JLabel(" ");
    
    /** Creates a new instance of FunctionPlotApplet */
    public FunctionPlotApplet() {
        initComponents();
    }

    protected void initComponents() {
        JPanel bag = new JPanel();
        bag.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        
        panel.add(new JLabel("f(x) = "));
        panel.add(function);
        panel.add(new JLabel("Minimum x-value: "));
        panel.add(xmin);
        panel.add(new JLabel("Maximum x-value: "));
        panel.add(xmax);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(action);
        
        action.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionPressed(e);
            }
        });
  
        bag.add(panel, BorderLayout.NORTH);
        bag.add(buttonPanel, BorderLayout.WEST);
        
        this.getContentPane().add(bag, BorderLayout.NORTH);
        
        model = FunctionPlotter.createChartDataModelInstance(-10.0, 10.0, 2000, "x^2");
        chart = new ChartPanel(model, "Plotting x^2", DefaultChart.LINEAR_X_LINEAR_Y);
        chart.getCoordSystem().setPaintLabels(false);
        chart.setLegend(null);
        chart.addChartRenderer(new LineChartRenderer(chart.getCoordSystem(), model), 1);
        this.getContentPane().add(chart, BorderLayout.CENTER);
        
        this.getContentPane().add(status, BorderLayout.SOUTH);
    }
    
    public void actionPressed(ActionEvent evt) {
        xmin.setForeground(Color.black);
        xmax.setForeground(Color.black);
        function.setForeground(Color.black);
        status.setText(" ");
        
        double x_min = 0.0;
        double x_max = 1.0;
        try {
            x_min = Double.parseDouble(xmin.getText());
        } catch(Exception e) {
            xmin.setForeground(Color.red);
            status.setText("The minimum x-value was no valid number.");
            return;
        }
        
        try {
            x_max = Double.parseDouble(xmax.getText());
        } catch(Exception e) {
            xmax.setForeground(Color.red);
            status.setText("The maximum x-value was no valid number.");
            return;
        }
        
        try {
            model = FunctionPlotter.createChartDataModelInstance(x_min, x_max, 2000, function.getText());
        } catch(Exception e) {
            function.setForeground(Color.red);
            status.setText("The function couldn't be parsed.");
            return;
        }
        
        chart.setTitle(new Title("Plotting "+function.getText()));
        chart.setLegend(null);
        chart.setChartDataModel(model);
        chart.setCoordSystem(new CoordSystem(model));
        chart.getCoordSystem().setPaintLabels(false);
        chart.addChartRenderer(new LineChartRenderer(chart.getCoordSystem(), model), 1);
        chart.revalidate();
        repaint();
    }
}
