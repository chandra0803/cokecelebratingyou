<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
  	"chart": {
  			"rotateValues": "1"
  	},
  	  "categories": [
        {
            "category": [
             <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  	          <c:if test="${reportDataStatus.index != 0}">,</c:if>
                {
                    "label": "<c:out value="${reportItem.goals}"/>"
                }
                 </c:forEach> 
            ]
        }
    ],
     "dataset": [
    
        {
            "seriesname": "<cms:contentText key="TOTAL_BASELINE_OBJECTIVE" code="${report.cmAssetCode}"/>",
            "data": [
            <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  	        <c:if test="${reportDataStatus.index != 0}">,</c:if>
                {
                    "value": "<fmt:formatNumber value="${reportItem.totalBaseLineObjective}"/>"
                }
                </c:forEach> 
            ]
        },
        {
            "seriesname": "<cms:contentText key="TOTAL_ACTUAL_PRODUCTION" code="${report.cmAssetCode}"/>",
            "data": [
            <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  	        <c:if test="${reportDataStatus.index != 0}">,</c:if>
                {
                    "value": "<fmt:formatNumber value="${reportItem.totalActualProduction}"/>"
                }
                </c:forEach>
            ]
        }
            ]
            
}
