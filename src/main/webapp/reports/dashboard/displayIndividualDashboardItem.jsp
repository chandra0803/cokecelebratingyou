<%@ include file="/include/taglib.jspf"%>
<%
  response.setHeader( "Cache-Control", "cache, must-revalidate" );
  response.setHeader( "Pragma", "public" );
%>

{ 
	"messages": [], 
	"favorites": 
	[
        <%@include file="dashboardItem.jsp" %>
	] 
}
