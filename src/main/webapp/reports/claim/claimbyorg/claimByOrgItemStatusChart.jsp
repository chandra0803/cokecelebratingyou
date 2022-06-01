<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.domain.enums.ApprovalStatusType"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
    "chart": { 
   			  <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1",
   			  </c:if>
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
	   		"seriesname": "<cms:contentText key="ITEMS" code="${report.cmAssetCode}"/> <%=ApprovalStatusType.lookup(ApprovalStatusType.APPROVED).getName()%>",
	      	"data": [      			      		
	  			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.itemsApproved}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    },
	    {    
	      	"seriesname": "<cms:contentText key="ITEMS" code="${report.cmAssetCode}"/> <%=ApprovalStatusType.lookup(ApprovalStatusType.DENIED).getName()%>",
	      	"data": [      			      		
		  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.itemsDenied}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    },
	    {    
	      	"seriesname": "<cms:contentText key="ITEMS" code="${report.cmAssetCode}"/> <%=ApprovalStatusType.lookup(ApprovalStatusType.HOLD).getName()%>",
	      	"data": [      			      		
		  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.itemsHeld}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    }
  ]
  </c:if>
}