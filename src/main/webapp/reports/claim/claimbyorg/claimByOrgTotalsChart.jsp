<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
    "chart": { 
   			  <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1"
   			  </c:if>
    		 },
	"categories": [
        {
            "category": [
            	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
					{
					   "label": "${fn:substring(reportItem.orgName,0,10)}<c:if test="${fn:length(reportItem.orgName) > 10 }">..</c:if>",
                       "toolText":"<c:out value="${reportItem.orgName}"/>"
					}
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>                     
            ]
        }
    ],
    "dataset": [
        {
            "seriesname": "<cms:contentText key="CLAIMS_PER_SUBMITTER" code="${report.cmAssetCode}"/>",            
            "data": [
            	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
					{
						"value": "<fmt:formatNumber value="${reportItem.claimsPerSubmitter}"/>"
					}
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>              
            ]
        },
        {
            "seriesname": "<cms:contentText key="ITEMS_PER_SUBMITTER" code="${report.cmAssetCode}"/>",
            "data": [
            	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
					{
						"value": "<fmt:formatNumber value="${reportItem.itemsPerSubmitter}"/>"
					}
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>
            ]
        }
    ]
    </c:if>
}