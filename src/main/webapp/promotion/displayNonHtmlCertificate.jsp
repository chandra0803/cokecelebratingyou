<%@ page import="com.biperf.core.ui.reports.ReportsUtils"%><%
 byte[] report = (byte[])request.getAttribute( "report" );
 try {
   ReportsUtils.writeReport( response, report);
 } catch (Throwable t) {
      t.printStackTrace( );
   }

%>