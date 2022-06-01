<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request" />

{
<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
  	"chart": {
  	          <c:if test="${fn:length(reportData) > 5 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1",
		      </c:if>
		      "rotateValues": "1"
		     },
 "categories": [
  	{
  	"category":[
    	<c:forEach var="reportItem" items="${reportData}"
		varStatus="reportDataStatus">
		<c:if test="${reportDataStatus.index != 0}">,</c:if>
     		{
       			"label":"<c:out value="${reportItem.monthName}" />"
       		}
    	</c:forEach>
  			]
  		}
  	],
  	
  	"dataset":
  	[
  		{	"seriesname":"<cms:contentText key="NOMINATORS_CHART_Y_AXIS_NAME" code="${report.cmAssetCode}"/>",
      		"data":[
      			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
						<c:if test="${reportDataStatus.index != 0}">,</c:if>
      						{
          						"value":"<fmt:formatNumber value="${reportItem.actualNominatorsCnt}" />"
        					}
      			</c:forEach>
      				]
    	},
    	{	"seriesname":"<cms:contentText key="NOMINEES_CHART_Y_AXIS_NAME" code="${report.cmAssetCode}"/>",
      		"data":[
      			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
						<c:if test="${reportDataStatus.index != 0}">,</c:if>
      						{
          					"value":"<fmt:formatNumber value="${reportItem.actualNomineesCnt}" />"
        					}
      			</c:forEach>
      				]
    	}
  	]
  	
	</c:if>
}
