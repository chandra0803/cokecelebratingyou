<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">       
	"messages": [],
	"chart": {}, 
	"data":[
		<c:set var="hasData" value="false"/>
      	<c:forEach var="item" items="${reportData}">
	      	<c:if test="${item.recognitionCnt > 0 }">
	      		<c:set var="hasData" value="true"/>
	      	</c:if>
      	</c:forEach>
      	<c:if test="${hasData}">
		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
       		<c:if test="${reportDataStatus.index != 0}">,</c:if> 
      		{
       			"value": "<fmt:formatNumber value="${reportItem.recognitionCnt}"/>",
       			"label":"<c:out value="${reportItem.nodeName}"/>"
      		}
      	</c:forEach>
      	</c:if>
   ]  
   </c:if>   
}