<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
    <c:if test="${fn:length(reportData) > 0}">
	"messages": [],
  	"chart": {},  
  	"data": 
  	[    
  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  		<c:if test="${reportItem.submitterCount > 0 || reportItem.nonSubmitterCount > 0 }">
  		  
  			{       			
       			"label":"<cms:contentText key="SUBMITTED" code="${report.cmAssetCode}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.submitterCount}"/>"
      		},
      		{       			
       			"label":"<cms:contentText key="NOT_SUBMITTED" code="${report.cmAssetCode}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.nonSubmitterCount}"/>"
      		}
      		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
      	</c:if>
  		</c:forEach> 			    
  	]
  	</c:if>
}