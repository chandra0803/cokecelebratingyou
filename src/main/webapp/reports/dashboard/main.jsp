<%@ include file="/include/taglib.jspf"%>
<%
  response.setHeader( "Cache-Control", "cache, must-revalidate" );
  response.setHeader( "Pragma", "public" );
%>

{ 
	"messages": [],
	"reportDashboardId": "${reportData.id}",
	"favorites": [
            <%@include file="dashboardItem.jsp" %>
	] 
}
