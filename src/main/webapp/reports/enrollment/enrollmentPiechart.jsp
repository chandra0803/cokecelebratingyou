<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
	<c:if test="${fn:length(reportData) > 0}">
    "chart": {},  
 
      "data":[
      	<c:set var="hasData" value="false"/>
      	<c:forEach var="item" items="${reportData}">
	      	<c:if test="${item.statusCnt > 0 }">
	      		<c:set var="hasData" value="true"/>
	      	</c:if>
      	</c:forEach>
      	<c:if test="${hasData}">
			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       		<c:if test="${reportDataStatus.index != 0}">,</c:if> 
	      		{
	       			"label":"<c:out value="${reportItem.hierarchyNodeName}"/>",
	       			"value": "<fmt:formatNumber value="${reportItem.statusCnt}" />"
	      		}
	      	</c:forEach>
      	</c:if>
   ]  
   </c:if>   
}