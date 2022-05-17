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
<head><title>JSP Page</title></head>
<body>
<%
    int[][] model = {{1, 2, 3},
                     {3, 1, 2},
                     {2, 3, 1}};
        
    double[] columns = {-1.0, 0.0, 1.0, 2.0, 3.0, 4.0};
    String[] rows = {"insight.de", "primusavitos.de", "MediaMarkt AG"};

    String title = "Marktanteile dt. Distributoren";

    int width = 640;
    int height = 480;


    DefaultChartDataModel data = new DefaultChartDataModel(model, columns, rows);
    
    DefaultChart c = new DefaultChart(data, title);

    c.addChartRenderer(new PieChartRenderer(data, RowColorModel.getInstance(data)), 1);

    c.setBounds(new Rectangle(0, 0, width, height));

    session.setAttribute("chart", c);
%>
<%-- <jsp:useBean id="beanInstanceName" scope="session" class="package.class" /> --%>
<%-- <jsp:getProperty name="beanInstanceName"  property="propertyName" /> --%>
<h1>Chart Testing</h1>
<p>This page displays a PieChart via the ChartServlet. The Chart is configured in this 
JSP Page and is stored in the SessionContext. The ChartServlet is embedded through
the <code>&lt;img&gt;</code> Tag and renders the image following the image type parameter.</p>

<img src="http://localhost:8080/charting/ChartServlet?imageType=jpeg" alt="PieChart as JPEG Image"><br>
<img src="/charting/ChartServlet?imageType=png" alt="PieChart as PNG Image">
</body>
</html>
