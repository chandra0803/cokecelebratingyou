<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
  	"chart": {
  	  "showPercentValues":"1",
  	  "showLabels":"0" }, 
      "data":[
      	<c:set var="hasData" value="false"/>
      	<c:forEach var="item" items="${reportData}">
	      	<c:if test="${item.selectionPct > 0 }">
	      		<c:set var="hasData" value="true"/>
	      	</c:if>
      	</c:forEach>
      	<c:if test="${hasData}">
			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       		<c:if test="${reportDataStatus.index != 0}">,</c:if> 
	      		{
	       			"label":"${fn:replace(reportItem.itemName, '"', '\\"')}",
	       			"value": "<fmt:formatNumber value="${reportItem.selectionPct}"/>"
	      		}
	      	</c:forEach>
      	</c:if>
   ]  
   </c:if>   
}