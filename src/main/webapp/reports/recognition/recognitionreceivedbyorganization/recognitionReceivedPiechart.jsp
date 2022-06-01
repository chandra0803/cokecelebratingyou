<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">       
	"messages": [],
	"chart": {}, 
	<c:set var="hasData" value="false"/>
	<c:forEach var="reportItem" items="${reportData}">
		<c:if test="${reportItem.recognitionCnt != 0}">
			<c:set var="hasData" value="true"/>
		</c:if>
	</c:forEach>
	"data":[
		<c:if test="${hasData eq true}">
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