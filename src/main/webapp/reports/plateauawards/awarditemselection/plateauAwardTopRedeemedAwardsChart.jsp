<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
  	"chart": {
  	           <c:if test="${fn:length(reportData) > 5}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1",
   			  </c:if>
   			  "rotateValues": "1"
  	         },
  	"data": 
  	[    
  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  			{       			
       			"label":"${fn:substring(fn:replace(reportItem.itemName, '"', '\\"'), 0, 20)}<c:if test="${fn:length(reportItem.itemName) > 20 }">..</c:if>",
       			"toolText":"${fn:replace(reportItem.itemName, '"', '\\"')}",
       			"value": "<fmt:formatNumber value="${reportItem.selectionCnt}"/>"
      		}
      		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
  		</c:forEach> 			    
  	]
  	</c:if>
}