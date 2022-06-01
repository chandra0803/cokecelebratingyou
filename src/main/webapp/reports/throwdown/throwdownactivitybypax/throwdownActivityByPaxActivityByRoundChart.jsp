<%@ include file="/include/taglib.jspf" %>

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
  	"data": 
  	[    
  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  			{       			
       			"label":"<cms:contentText key="ROUND" code="${report.cmAssetCode}"/> <c:out value="${reportItem.roundNumber}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.activityCnt}"/>"
      		}
      		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
  		</c:forEach> 			    
  	]
  	</c:if>
}