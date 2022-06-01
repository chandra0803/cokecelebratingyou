<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
  	"chart": {
  	           <c:if test="${fn:length(reportData) > 5 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1",
		    </c:if>
		    "rotateValues": "1"
  	         },
  	"data": 
  	[    
  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  			{       			
       			"label": "${fn:substring(reportItem.orgName,0,10)}<c:if test="${fn:length(reportItem.orgName) > 10 }">..</c:if>",
                "toolText":"<c:out value="${reportItem.orgName}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.nominationCount}"/>"
      		}
      		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
  		</c:forEach> 			    
  	]
  	</c:if>
}