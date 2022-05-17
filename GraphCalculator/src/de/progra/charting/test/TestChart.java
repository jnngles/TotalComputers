/*
    JOpenChart Java Charting Library and Toolkit
    Copyright (C) 2001  Sebastian Müller
    http://jopenchart.sourceforge.net

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    TestChart.java
    Created on 19. Oktober 2001, 16:34
 */

package de.progra.charting.test;

import de.progra.charting.*;
import de.progra.charting.model.*;
import de.progra.charting.render.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.text.*;
import java.awt.geom.AffineTransform;

/**
 * This class is used to test the Charting package.
 * @author  mueller
 * @version 1.0
 */
public class TestChart {
    
    /** Creates new TestChart */
    public TestChart() {
    }
    
    
    /** Used to create multiple charts for testing purposes in $USERHOME.
     * @param args empty
     */
    public static void main(String[] args) {
        makeSimpleLineTest();
        makeRadarTest();
		makeDiffSizedBarChartTest();
		makeBarChartFormatTest();
        makeAxisTitleTest();
        
        makeNevilleTest();
        
        makeSimplePieTest();
        makeLineChartTest();        
        makePlotTest();
        makePlot2Test();
        makeBarTest();
        makeStackedBarTest();
        
        makeManualBarTest();
	    makeSimpleNegativeLineTest();
        
        System.exit(0);
    }
    
    public static void makeAxisTitleTest() {
        System.out.println("** Creating Bar Chart Plot with Axis Titles.");
        int[][] model = {{23, 43, 12},
        {54, -15, 34},
        {99, 32, 45}};
        
        String[] columns = {"1997", "1998", "1999"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online"};
        
        String title = "Average Growth 1997 - 1999";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y, "Year", "Growth");
        c.addChartRenderer(new BarChartRenderer(c.getCoordSystem(),
                            data), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/axistest.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the axistest.png file, showing the bar chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the axistest.png file, showing the bar chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
	
	public static void makeRadarTest() {
        System.out.println("** Creating Radar Chart Plot.");
        int[][] model = {{23, 43, 12, 19, 12},
        {54, 20, 34, 40, 67},
        {15, 30, 8, 17, 23},
        {85, 15, 10, 26, 54}};
        
        String[] columns = {"1997", "1998", "1999", "2000", "2001"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online", "foo.co.uk"};
        
        String title = "Average Growth 1997 - 2001";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
        DefaultChart c = new DefaultChart(data, title);
        c.addChartRenderer(new RadarChartRenderer(data), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/radartest.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the radartest.png file, showing the radar chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the radartest.png file, showing the radar chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makeBarChartFormatTest() {
        System.out.println("** Creating Bar Chart Plot.");
        int[][] model = {{23, 43, 12},
        {54, -15, 34},
        {99, 32, 45}};
        
        String[] columns = {"1997", "1998", "1999"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online"};
        
        String title = "Average Growth 1997 - 1999";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y, "Year", "Growth");
		/*CoordSystem cs, ChartDataModel model, RowColorModel rcm, 
            DecimalFormat topFormat, Font topFont, float boxWidth
		*/
		c.setCoordSystem(new CoordSystem(data, new DecimalFormat(), false, true, false));
        c.addChartRenderer(new BarChartRenderer(c.getCoordSystem(),
                            data, new DecimalFormat(), new Font("sans", Font.ITALIC, 9), 1.0f), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/barchart2test.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the barchart2test.png file, showing the bar chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the barchart2test.png file, showing the bar chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makeSimpleLineTest() {
        System.out.println("** Creating Simple Line Test.");
        int[][] model = {{-200000, 0, 100, 200000}};
        
        double[] columns = {-100, 0.0, 1.0, 2000.0};
        
        String[] rows = {"f(x) = x^3"};
        
        String title = "Neville Interpolation";
        
        int width = 640;
        int height = 480;
        
        DefaultChartDataModel data = new DefaultChartDataModel(model, columns, rows);
        /*
		data.setManualScale(true);
		data.setMaximumValue(new Integer(200000));
		data.setMinimumValue(new Integer(-10));
		data.setMaximumColumnValue(500.0);
		data.setMinimumColumnValue(-10.0);
		*/
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(), data), 1);
        
        c.setBounds(new Rectangle(0, 0, width, height));
        
        try {
            ChartEncoder.createEncodedImage(new FileOutputStream(System.getProperty("user.home")+"/linetest.png"), c, "png");
        } catch(EncodingException e) {
            System.out.println("** Error creating the linetest.png file, showing the simple line chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the linetest.png file, showing the simple line chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makeNevilleTest() {
        System.out.println("** Creating Neville Interpolation Test.");
        int[][] model = {{-8, 0, 1, 8}};
        
        double[] columns = {-2.0, 0.0, 1.0, 2.0};
        
        String[] rows = {"f(x) = x^3"};
        
        String title = "Neville Interpolation";
        
        int width = 640;
        int height = 480;
        
        DefaultChartDataModel data = new DefaultChartDataModel(model, columns, rows);
        data.setAutoScale(true);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(), data), 1);
        
        c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(), data), 2);
        
        c.addChartRenderer(new InterpolationChartRenderer(c.getCoordSystem(), data), 3);
        c.setBounds(new Rectangle(0, 0, width, height));
        
        try {
            ChartEncoder.createEncodedImage(new FileOutputStream(System.getProperty("user.home")+"/nevilletest.png"), c, "png");
        } catch(EncodingException e) {
            System.out.println("** Error creating the nevilletest.png file, showing the Neville Interpolation.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the nevilletest.png file, showing the Neville Interpolation.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makeSimplePieTest() {
        System.out.println("** Creating Pie Chart Test.");
        int[][] model = {{23, 43, 12},
        {54, -15, 34},
        {99, 32, 45}};
        
        String[] columns = {"1997", "1998", "1999"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online"};
        
        String title = "Average Growth 1997 - 1999";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
        DefaultChart c = new DefaultChart(data, title);
        c.addChartRenderer(new PieChartRenderer(data), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/simplepie.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the simple.png file, showing the pie chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the simplepie.png file, showing the pie chart.");
            e.printStackTrace();
        }
        System.out.println("successfull.");
    }
    
    public static void makeLineChartTest() {
        System.out.println("** Creating Line Chart Test.");
        
        int[] quadr = {0, 1, 4, 9, 16, 25, 36};
        int[] exp = {1, 2, 4, 8, 16, 32, 64};        
        double[] columns = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        
        DefaultDataSet[] ds = new DefaultDataSet[3];
        ds[0] = new DefaultDataSet(ChartUtilities.transformArray(new int[] {0, 6}),
                                   ChartUtilities.transformArray(new double[] {0.0, 6.0}),
                                   CoordSystem.FIRST_YAXIS,
                                   "Linear Growth");
        ds[1] = new DefaultDataSet(ChartUtilities.transformArray(quadr),
                                   ChartUtilities.transformArray(columns),
                                   CoordSystem.FIRST_YAXIS,
                                   "Quadratic Growth");
        
        ds[2] = new DefaultDataSet(ChartUtilities.transformArray(exp),
                                   ChartUtilities.transformArray(columns),
                                   CoordSystem.FIRST_YAXIS,
                                   "Exponential Growth");
        
        String title = "Growth Factor Comparison";
        
        int width = 640;
        int height = 480;
        
        DefaultChartDataModel data = new DefaultChartDataModel(ds);
        
        // Look at the next 3 lines:
        data.setAutoScale(true);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(),
                           data), 1);
        
        c.setBounds(new Rectangle(0, 0, width, height));
        
        
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/simpleline.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the simpleline.png file, showing the line chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the simpleline.png file, showing the line chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makeDiffSizedBarChartTest() {
        System.out.println("** Creating Bar Chart Test with different sized DataSets.");
        
        int[] quadr = {0, 1, 4, 9, 16, 25, 36};
        int[] exp = {1, 2, 4, 8, 16, 32, 64};        
        String[] columns = {"0", "1", "2", "3", "4", "5", "6"};
        
        DefaultDataSet[] ds = new DefaultDataSet[3];
        ds[0] = new DefaultDataSet(ChartUtilities.transformArray(new int[] {0, 6}),
                                   new String[] {"0", "6"},
                                   CoordSystem.FIRST_YAXIS,
                                   "Linear Growth");
        ds[1] = new DefaultDataSet(ChartUtilities.transformArray(quadr),
                                   columns,
                                   CoordSystem.FIRST_YAXIS,
                                   "Quadratic Growth");
        
        ds[2] = new DefaultDataSet(ChartUtilities.transformArray(exp),
                                   columns,
                                   CoordSystem.FIRST_YAXIS,
                                   "Exponential Growth");
        
        String title = "Growth Factor Comparison";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(ds, columns);

        // Look at the next 3 lines:
        //data.setAutoScale(true);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y, "x", "y");
        c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(),
                           data), 1);
        
        c.setBounds(new Rectangle(0, 0, width, height));
        
        
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/objectbarchart.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the objectbarchart.png file, showing the line chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the objectbarchart.png file, showing the line chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
	
    public static void makePlotTest() {
        System.out.println("** Creating Function Plot.");
        String title = "Plotting x^3";
        
        int width = 640;
        int height = 480;
        
        DefaultChartDataModel data = FunctionPlotter.createChartDataModelInstance(-10, 10, 2000, "1/x");
        data.setAutoScale(true);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(),
                           data), 1);
        
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/plottest.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the plottest.png file, showing the function plot.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the plottest.png file, showing the function plot.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makePlot2Test() {
        System.out.println("** Creating Plot Chart Test.");
        
        double[] quadr = {0.0, 1.0, 4.0, 9.0, 16.0, 25.0, 36.0};
        double[] exp = {1.0, 2.0, 4.0, 8.0, 16.0, 32.0, 64.0};
        double[] log = {0.0, Math.log(1.0), Math.log(2.0), Math.log(3.0), Math.log(4.0), Math.log(5.0), Math.log(6.0)};
        double[] sqrt = {0.0, Math.sqrt(1.0), Math.sqrt(2.0), Math.sqrt(3.0), Math.sqrt(4.0), Math.sqrt(5.0), Math.sqrt(6.0)};
        double[] columns = {0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
        
        DefaultDataSet[] ds = new DefaultDataSet[5];
        ds[0] = new DefaultDataSet(ChartUtilities.transformArray(new double[] {0.0, 6.0}),
                                   ChartUtilities.transformArray(new double[] {0.0, 6.0}),
                                   CoordSystem.FIRST_YAXIS,
                                   "Linear Growth");
        ds[1] = new DefaultDataSet(ChartUtilities.transformArray(quadr),
                                   ChartUtilities.transformArray(columns),
                                   CoordSystem.FIRST_YAXIS,
                                   "Quadratic Growth");
        
        ds[2] = new DefaultDataSet(ChartUtilities.transformArray(exp),
                                   ChartUtilities.transformArray(columns),
                                   CoordSystem.FIRST_YAXIS,
                                   "Exponential Growth");
        
        
        ds[3] = new DefaultDataSet(ChartUtilities.transformArray(log),
                                   ChartUtilities.transformArray(columns),
                                   CoordSystem.FIRST_YAXIS,
                                   "Logarithmic Growth");
        
        
        ds[4] = new DefaultDataSet(ChartUtilities.transformArray(sqrt),
                                   ChartUtilities.transformArray(columns),
                                   CoordSystem.FIRST_YAXIS,
                                   "Square Root Growth");
        
        String title = "Growth Factor Comparison";
        
        int width = 640;
        int height = 480;
        
        DefaultChartDataModel data = new DefaultChartDataModel(ds);
        
        // Look at the next 3 lines:
        data.setAutoScale(true);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new PlotChartRenderer(c.getCoordSystem(), data), 1);
        
        c.setBounds(new Rectangle(0, 0, width, height));
        
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/secondplot.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the secondplot.png file, showing the plot chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the secondplot.png file, showing the second plot chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
	public static void makeBarTest() {
        System.out.println("** Creating Bar Chart Plot.");
        int[][] model = {{23, 43, 12},
        {54, -15, 34},
        {99, 32, 45}};
        
        String[] columns = {"1997", "1998", "1999"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online"};
        
        String title = "Average Growth 1997 - 1999";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new BarChartRenderer(c.getCoordSystem(),
                            data), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/simplebar.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the simplebar.png file, showing the bar chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the simplebar.png file, showing the bar chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
    public static void makeStackedBarTest() {
        System.out.println("** Creating Stacked Bar Chart Plot.");
        int[][] model = {{23, 43, 12, 5, 20},
        {54, -15, 34, 40, 45},
        {99, -10, 45, -10, -25}};
        
        String[] columns = {"1997", "1998", "1999", "2000", "2001"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online"};
        
        String title = "Profit Shares 1997 - 2001";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
        
        data.setChartDataModelConstraints(CoordSystem.FIRST_YAXIS, 
                                           new StackedChartDataModelConstraints(data, CoordSystem.FIRST_YAXIS, false));
        
        data.setChartDataModelConstraints(CoordSystem.SECOND_YAXIS, 
                                           new StackedChartDataModelConstraints(data, CoordSystem.SECOND_YAXIS, false));
        
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new StackedBarChartRenderer(c.getCoordSystem(),
                            data), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/stackedbar.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the stackedbar.png file, showing the stacked bar chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the stackedbar.png file, showing the stacked bar chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
    
	public static void makeManualBarTest() {
        System.out.println("** Creating Bar Chart Plot with manual scaling.");
        int[][] model = {{23, 43, 12},
        {54, 10, 34},
        {99, 32, 45}};
        
        String[] columns = {"1997", "1998", "1999"};
        
        String[] rows = {"foobar.com", "@foo Ltd.", "bar.online"};
        
        String title = "Average Growth 1997 - 1999";
        
        int width = 640;
        int height = 480;
        
        ObjectChartDataModel data = new ObjectChartDataModel(model, columns, rows);
		
		data.setManualScale(true);
		data.setMaximumValue(new Integer(100));
		data.setMinimumValue(new Integer(0));
		
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new BarChartRenderer(c.getCoordSystem(),
                            data), 1);
        c.setBounds(new Rectangle(0, 0, width, height));
        try {
            ChartEncoder.createPNG(new FileOutputStream(System.getProperty("user.home")+"/manualbar.png"), c);
        } catch(EncodingException e) {
            System.out.println("** Error creating the manualbar.png file, showing the manually scaled bar chart.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
			System.out.println("** Error creating the manualbar.png file, showing the manually scaled bar chart.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
	
    public static void makeSimpleNegativeLineTest() {
        System.out.println("** Creating Simple Negative Line Test.");
        int[][] model = {{-200, -160, -80, 0}};
        
        double[] columns = {-100, -80, -40, 0};
        
        String[] rows = {"Negative sector"};
        
        String title = "Just a Test";
        
        int width = 640;
        int height = 480;
        
        DefaultChartDataModel data = new DefaultChartDataModel(model, columns, rows);
        //data.setAutoScale(true);
        DefaultChart c = new DefaultChart(data, title, DefaultChart.LINEAR_X_LINEAR_Y);
        c.addChartRenderer(new LineChartRenderer(c.getCoordSystem(), data), 1);
        
        c.setBounds(new Rectangle(0, 0, width, height));
        
        try {
            ChartEncoder.createEncodedImage(new FileOutputStream(System.getProperty("user.home")+"/neglinetest.png"), c, "png");
        } catch(EncodingException e) {
            System.out.println("** Error creating the neglinetest.png file, showing the simple negative line test.");
            e.getCause().printStackTrace();
            return;
        } catch(Exception e) {
            System.out.println("** Error creating the neglinetest.png file, showing the simple negative line test.");
            e.printStackTrace();
            return;
        }
        System.out.println("successfull.");
    }
}
