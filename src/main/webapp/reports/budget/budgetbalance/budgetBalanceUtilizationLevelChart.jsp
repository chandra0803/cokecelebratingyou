<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
  	"chart": {
  	           <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1"
   			  </c:if>
  	         },
  	"dataset": [    
		{    
	   		"seriesname": "<cms:contentText key="PERCENT_USED" code="${report.cmAssetCode}"/>",
	      	"data": [      			      		
	  			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{	
	  					"label": "${fn:substring(reportItem.budgetMasterName,0,10)}<c:if test="${fn:length(reportItem.budgetMasterName) > 10 }">..</c:if>",
                        "toolText":"<c:out value="${reportItem.budgetMasterName}"/>",
	          			"value": "<fmt:formatNumber value="${reportItem.awarded}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    }
  ]
  </c:if>
}