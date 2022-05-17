/*
    JOpenChart Java Charting Library and Toolkit
    Copyright (C) 2001  Sebastian M�ller
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

    ChartPanel.java
    Created on 6. September 2001, 14:10
*/

package de.progra.charting.swing;

import javax.swing.JPanel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.event.*;
import de.progra.charting.Chart;
import de.progra.charting.Legend;
import de.progra.charting.Title;
import de.progra.charting.CoordSystem;
import de.progra.charting.DefaultChart;
import java.awt.geom.Rectangle2D;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.Map;
import de.progra.charting.model.ChartDataModel;

/**
 * This Panel provides the possibility to include a Chart into a Swing 
 * Application. I choose not to make every Chart extend JComponent because
 * of the overhead this would have meant. Instead, this class is an adaptor.
 * It implements the Chart interface and contains a DefaultChart instance
 * to which all Chart calls are promoted.
 * @author  mueller
 */
public class ChartPanel extends JPanel implements Chart {

    /** The chart instance to which all method calls are promoted.*/
    DefaultChart chart;
    
    /** Creates new ChartPanel */
    private ChartPanel() {
    }
    
    /** Creates a new ChartPanel with the given model
     * and title string. 
     * @param model the ChartDataModel
     * @param title the title String
     */
    public ChartPanel(ChartDataModel model, String title) {
        this();
        chart = new DefaultChart(model, title);
    }

    /** Creates a new ChartPanel with the given model
     * and title string and a coordinate system.
     * @param model the ChartDataModel
     * @param title the title String
     * @param coord the id of the coordinate system configuration
     */
    public ChartPanel(ChartDataModel model, String title, int coord) {
        this();
        chart = new DefaultChart(model, title, coord);
    }

    /** This method is write-protected by the IDE but isn't used at all.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        
        setLayout(new java.awt.BorderLayout());
        
    }//GEN-END:initComponents

    /** Adds a ChartRenderer with a specific z-coordinate.
     * @param renderer the ChartRenderer
     * @param z its z-coordinate.
     */
    public void addChartRenderer(AbstractChartRenderer renderer, int z) {
        chart.addChartRenderer(renderer, z);
    }    

    /** Returns the Bounds for the ChartPanel.
     * @return the bounds
     */
    public Rectangle getBounds() {
        return chart.getBounds();
    }    

    /** Returns the ChartDataModel.
     * @return the ChartDataModel
     */
    public ChartDataModel getChartDataModel() {
        return chart.getChartDataModel();
    }
    
    /** Returns the Map of all ChartRenderers.
     * @return the Map of Renderers.
     */
    public Map getChartRenderer() {
        return chart.getChartRenderer();
    }
    
    /** Returns the ChartRenderer with a specific z-coordinate.
     * @param z the z-coordinate of the desired ChartRenderer.
     * @return the ChartRenderer or <CODE>null</CODE> if none has been found.
     */
    public AbstractChartRenderer getChartRenderer(int z) {
        return chart.getChartRenderer(z);
    }
    
    /** Returns the coordinate system.
     * @return the Coordinate System for the Chart. Could be <CODE>null</CODE>.
     */
    public CoordSystem getCoordSystem() {
        return chart.getCoordSystem();
    }
    
    /** Returns this chart's legend.
     * @return the Legend for this Chart. Could be <CODE>null</CODE>.
     */
    public Legend getLegend() {
        return chart.getLegend();
    }
    
    /** Returns the title for this chart.
     * @return this Chart's Title. Could be <CODE>null</CODE>.
     */
    public Title getTitle() {
        return chart.getTitle();
    }
    
    /** Sets the Bounds for this Chart.
     * @param r the <CODE>Rectangle</CODE> object defining the bounds
     */
    public void setBounds(Rectangle r) {
        chart.setBounds(r);
    }
    
    /** Stores the ChartDataModel for this Chart.
     * @param model the ChartDataModel
     */
    public void setChartDataModel(ChartDataModel model) {
        chart.setChartDataModel(model);
    }
    
    /** Sets the Map with all ChartRenderers. The keys
     * have to be the z-coordinates of the ChartRenderers.
     * @param renderer The Map of ChartRenderers.
     */
    public void setChartRenderer(Map renderer) {
        chart.setChartRenderer(renderer);
    }
    
    /** Sets the coordinate system for this chart,
     * which can be null if the ChartRenderer
     * doesn't need a coordinate system, e.g. if it's a
     * PieChart.
     * @param c The Coordinate System for the Chart.
     */
    public void setCoordSystem(CoordSystem c) {
        chart.setCoordSystem(c);
    }
    
    /** Sets the legend for this chart.
     * @param l The Legend this Chart contains.
     */
    public void setLegend(Legend l) {
        chart.setLegend(l);
    }
    
    /** Sets the title for this chart.
     * @param t This Chart's Title.
     */
    public void setTitle(Title t) {
        chart.setTitle(t);
    }
    
    /** Computes the preferred size of the ChartPanel.
     * @return <code>new java.awt.Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)</code>
     */
    public Dimension getPreferredSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    
    /** Paints the ChartPanel. Calls <code>chart.render((Graphics2D)graphics)</code>
     * @param graphics the Graphics2D object to paint in
     */
    public void paint(Graphics graphics) {
        chart.setBounds(new Rectangle(this.getWidth(), this.getHeight()));
        chart.render((Graphics2D)graphics);
    }
    
    /** Does the layout of the title, legend and coordinate system and
     * calls the render method of all those including the ChartRenderers.
     * @param g the <CODE>Graphics2D</CODE> object to paint in.
     * Just calls paint(Graphics).
     */
    public void render(Graphics2D g) {
        paint(g);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
}
