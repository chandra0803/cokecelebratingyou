<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.domain.enums.ClaimStatusType"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
    "chart": {
    		"rotateValues": "1"
    },
	"categories": [
		{
			"category": [
      			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	        		{
	          		   "label": "${fn:substring(reportItem.orgName,0,10)}<c:if test="${fn:length(reportItem.orgName) > 10 }">..</c:if>",
                       "toolText":"<c:out value="${reportItem.orgName}"/>"
	        		}
	        		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
        		</c:forEach>
      		]
    	}
  	],
  	"dataset": [    
		{    
	   		"seriesname": "<cms:contentText key="CLAIMS" code="${report.cmAssetCode}"/> <%=ClaimStatusType.lookup(ClaimStatusType.OPEN).getName()%>",
	      	"data": [      			      		
	  			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.openStatusCount}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    },
	    {    
	      	"seriesname": "<cms:contentText key="CLAIMS" code="${report.cmAssetCode}"/> <%=ClaimStatusType.lookup(ClaimStatusType.CLOSED).getName()%>",
	      	"data": [      			      		
		  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.closedStatusCount}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    }
  ]
  </c:if>
}