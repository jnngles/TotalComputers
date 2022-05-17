<!--
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

    ChartPage.jsp
    Created on 2. September 2001, 14:44
//-->

<%@page contentType="text/html"%>
<%@page import="de.progra.charting.*"%>
<%@page import="de.progra.charting.model.*"%>
<%@page import="de.progra.charting.render.*"%>
<%@page import="java.awt.*"%>
<html>
<head><title>Functionplotting</title></head>
<body>

<%
    String function = "5*x^2-4*x+3";
    if(request.getParameter("function") != null)
        function = request.getParameter("function");
    double lowrange = -10.0;
    try {
        lowrange = Double.parseDouble(request.getParameter("lowrange"));
    } catch(Exception e) {}

    double highrange = 10.0;
    try {
        highrange = Double.parseDouble(request.getParameter("highrange"));
    } catch(Exception e) {}

    int amount = 2000;
    try {
        amount = Integer.parseInt(request.getParameter("amount"));
    } catch(Exception e) {}
%>

<h1>Plotting Example</h1>
<form action="FunctionPlot.jsp" method="post">
<pre>
function:  <input type="text" name="function" value="<%=function%>" size="40">
           You have to use the Python syntax for mathematical formulae.<br>
lowrange:  <input type="text" name="lowrange" value="<%=lowrange%>" size="40">
           The lowest value that should be rendered. (default -10.0)<br>
highrange: <input type="text" name="highrange" value="<%=highrange%>" size="40">
           The highest value that should be rendered. (default 10.0)<br>
amount:    <input type="text" name="amount" value="<%=amount%>" size="40">
           The amount of values that will be computed in the interval. (default 2000)<br>
</pre>	
<input type="submit" name="Berechnen">
</form>
<%
    

    if(function != null) {
        DefaultChartDataModel data = 
            FunctionPlotter.createChartDataModelInstance(lowrange, highrange, amount, function);

        String title = "Dynamic Function Plotting";

        DefaultChart c = new DefaultChart(data, title);

        CoordSystem coord = new CoordSystem(data);
        c.setCoordSystem(coord);
        
        c.addChartRenderer(new LineChartRenderer(coord, data, RowColorModel.getInstance(data)), 1);
        
        c.setBounds(new Rectangle(0, 0, 640, 480));

        session.setAttribute("chart", c);
%>
        <img src="/charting/ChartServlet?imageType=png" alt="LineChart with FunctionPlot"><br>
<%
        
    }
    
%>

</body>
</html>

