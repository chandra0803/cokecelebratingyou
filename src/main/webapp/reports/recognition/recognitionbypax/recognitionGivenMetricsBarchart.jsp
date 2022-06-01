<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
            "xAxisName": ""
           },
  "data": [
  	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  		<c:if test="${reportDataStatus.index != 0}">,</c:if>
  		{
  			"label":"<cms:contentText key="TOP_PARTICIPANT" code="${report.cmAssetCode}"/>",
  			"value": "<fmt:formatNumber value="${reportItem.topCnt}"/>"
  		},
  		{
  			"label":"<cms:contentText key="OVER_ALL_AVERAGE" code="${report.cmAssetCode}"/>",
  			"value": "<fmt:formatNumber value="${reportItem.overallOrgAvgCnt}"/>"
  		},
  		{
  			"label":"<cms:contentText key="TOTAL_GIVEN" code="${report.cmAssetCode}"/>",
  			"value": "<fmt:formatNumber value="${reportItem.totalCnt}"/>"
  		}
  	</c:forEach>
  ]
  </c:if>
}