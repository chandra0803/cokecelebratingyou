<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
             "xAxisName": ""
           },
  "data": [
  	<c:forEach var="reportItem" items="${reportData}">
  	{
  	"label":"<cms:contentText key="TOP_PARTICIPANT" code="${report.cmAssetCode}"/>",
  	"value": "<fmt:formatNumber value="${reportItem.topCnt}"/>",
  	"color": "AFD8F8"
  	},
  	{
  	"label":"<cms:contentText key="OVER_ALL_AVERAGE" code="${report.cmAssetCode}"/>",
  	"value": "<fmt:formatNumber value="${reportItem.overallOrgAvgCnt}"/>",
  	"color": "F6BD0F"
  	},
  	{
  	"label":"<cms:contentText key="TOTAL" code="${report.cmAssetCode}"/>",
  	"value": "<fmt:formatNumber value="${reportItem.totalCnt}"/>",
  	"color": "A66EDD"
  	}
  	</c:forEach>
  ]
  </c:if>
}