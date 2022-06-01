<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(nominationsByStatusMap) > 0}">
	"messages": [],
  	"chart": {
  	           <c:if test="${fn:length(nominationsByStatusMap) > 5 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1"
		    </c:if>
  	         },
  	"data": 
  	[
  		<c:forEach var="statusItem" items="${nominationsByStatusMap}" varStatus="statusItemStatus">
  		<c:if test="${statusItemStatus.index > 0}">,</c:if>
  		{
  			"label": "${fn:substring(statusItem.key,0,10)}<c:if test="${fn:length(statusItem.key) > 10 }">...</c:if>",
  			"toolText":"${statusItem.key}",
  			"value": "<fmt:formatNumber value="${statusItem.value}"/>"
  		}
  		</c:forEach>
  	]
  	</c:if>
}