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

    TestChartDataModel.java
    Created on 1. August 2001, 18:07
 */

package de.progra.charting.test;

import de.progra.charting.model.*;
import de.progra.charting.render.*;
import de.progra.charting.*;

/**
 *
 * @author  mueller
 * @version 
 */
public class TestChartDataModel {

    int[][] intval = {{1, 2, 4, 234, 45, 0, -102, Integer.MAX_VALUE},
                        {23, 2, 45, -2000, 34, 566, 766, Integer.MIN_VALUE},
                        {40594, 32493, -12020, 21023, 103, 3498, -1202, -12039}};
                        
    Number[][] numval = {{new Integer(45), new Integer(0), new Integer(-102), new Integer(Integer.MAX_VALUE)},
                        {new Integer(34), new Integer(566), new Integer(766), new Integer(Integer.MIN_VALUE)},
                        {new Integer(103), new Integer(3498), new Integer(-1202), new Integer(-12039)}};
    
    double[] numcolumns = {Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    
    double[] columns = {1.0, Double.MIN_VALUE, Double.MAX_VALUE, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0, -20.0, 9458.340};
    
    Object[] objcolumns = {new Double(Double.MIN_VALUE), new Double(Double.MAX_VALUE), 
                           new Double(Double.POSITIVE_INFINITY), new Double(Double.NEGATIVE_INFINITY)};
    
    String[] strcolumns = {"1.0", ""+Double.MIN_VALUE, ""+Double.MAX_VALUE, 
                           ""+Double.POSITIVE_INFINITY, ""+Double.NEGATIVE_INFINITY, 
                           ""+0.0, ""+-20.0, ""+9458.340};
    
                        
    /** Creates new TestChartDataModel */
    public TestChartDataModel() {
        //DefaultChartDataModel model;
        ObjectChartDataModel model;
        
        // Create Empty DefaultChartDataModel
        //model = new DefaultChartDataModel();
        model = new ObjectChartDataModel();
        System.out.println("** Teste leeres Modell");
        testDefaultChartDataModel(model);
        
        // Create Number[][] DefaultChartDataModel
        //model = new DefaultChartDataModel(numval, numcolumns);
        model = new ObjectChartDataModel(numval, objcolumns, new String[] {"1", "2", "3", "4"});
        System.out.println("** Teste Modell mit Number[][]");
        
        testDefaultChartDataModel(model);
                
        // Create int[][] DefaultChartDataModel
        //model = new DefaultChartDataModel(intval, columns);
        model = new ObjectChartDataModel(intval, strcolumns, new String[] {"1", "2", "3", "4"});
        System.out.println("** Teste Modell mit int[][]");
        testDefaultChartDataModel(model);
        
    }
    
    public void testDefaultChartDataModel(DefaultChartDataModel m) {
        try {
            System.out.println("** AxisBinding von DataSet 0 = "+m.getAxisBinding(0));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** ColumnValue At index 3 = "+m.getColumnValueAt(3));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** ColumnValue At index 7 = "+m.getColumnValueAt(7));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Länge von DataSet 2 = "+m.getDataSetLength(2));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        try {
            System.out.println("** Anzahl von DataSets = "+m.getDataSetNumber());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        try {
            System.out.println("** Wert an Index 0,7 = "+m.getValueAt(0, 7));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        try {
            System.out.println("** Wert an Index 2,2 = "+m.getValueAt(2, 2));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        ChartDataModelConstraints con;
        
        try {
            System.out.println("** Lade ChartDataModelConstraints für CoordSystem.SECOND_YAXIS");
            con = m.getChartDataModelConstraints(CoordSystem.SECOND_YAXIS);
        } catch(Exception e) {
            System.out.println("** Fehler");
            return;
        }
        
        try {
            System.out.println("** Kleinster Wert = "+con.getMinimumValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Größter Wert = "+con.getMaximumValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Kleinster Column Wert = "+con.getMinimumColumnValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Größter Column Wert = "+con.getMaximumColumnValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
    }
    
    public void testDefaultChartDataModel(ObjectChartDataModel m) {
        try {
            System.out.println("** AxisBinding von DataSet 0 = "+m.getAxisBinding(0));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** ColumnValue At index 3 = "+m.getColumnValueAt(3));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** ColumnValue At index 7 = "+m.getColumnValueAt(7));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Länge von DataSet 2 = "+m.getDataSetLength(2));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        try {
            System.out.println("** Anzahl von DataSets = "+m.getDataSetNumber());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        try {
            System.out.println("** Wert an Index 0,7 = "+m.getValueAt(0, 7));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        try {
            System.out.println("** Wert an Index 2,2 = "+m.getValueAt(2, 2));
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        ChartDataModelConstraints con;
        
        try {
            System.out.println("** Lade ChartDataModelConstraints für CoordSystem.SECOND_YAXIS");
            con = m.getChartDataModelConstraints(CoordSystem.SECOND_YAXIS);
        } catch(Exception e) {
            System.out.println("** Fehler");
            return;
        }
        
        try {
            System.out.println("** Kleinster Wert = "+con.getMinimumValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Größter Wert = "+con.getMaximumValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Kleinster Column Wert = "+con.getMinimumColumnValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
        
        try {
            System.out.println("** Größter Column Wert = "+con.getMaximumColumnValue());
        } catch(Exception e) {
            System.out.println("** Fehler");
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        new TestChartDataModel();
    }

}
