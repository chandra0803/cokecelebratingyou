<%@ include file="/include/taglib.jspf"%>
<%
  response.setHeader( "Cache-Control", "cache, must-revalidate" );
  response.setHeader( "Pragma", "public" );
%>

{ 
	"messages": [ 
		{
		    "type" : "dataUpdate",
	    	"name" : "reportsFavorites",
	    	"data" : {
		        "favorites":[
                    <%@include file="dashboardItem.jsp" %>
		        ]
		    }
	   	}
	],
	"reportDashboardId": "${reportData.id}",
	"favorites": [
            <%@include file="dashboardItem.jsp" %>
	] 
}
