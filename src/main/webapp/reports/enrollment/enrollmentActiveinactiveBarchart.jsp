<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
  	"chart": {
  		"numberSuffix": "%"
  		},
  	"data": 
  	[    
  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  			{       			
       			"label":"<cms:contentText key="ACTIVE_STATUS" code="${report.cmAssetCode}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.activePct}" />"
      		},
      		{       			
       			"label":"<cms:contentText key="INACTIVE_STATUS" code="${report.cmAssetCode}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.inActivePct}" />"
      		}
      		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
  		</c:forEach> 			    
  	]
  	</c:if>
}