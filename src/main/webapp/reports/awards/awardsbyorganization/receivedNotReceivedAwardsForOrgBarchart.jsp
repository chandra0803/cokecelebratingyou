<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
              <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1",
   			  "rotateValues": "1",
   			  </c:if>
   			  "numberSuffix":"%",
   			  "yAxisMaxValue":"100"
   		   },
   "categories":[
  	{
      "category":[
      <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      	<c:if test="${reportDataStatus.index != 0}">,</c:if>
        {
          "label": "${fn:substring(reportItem.orgName,0,10)}<c:if test="${fn:length(reportItem.orgName) > 10 }">..</c:if>",
          "toolText":"<c:out value="${reportItem.orgName}"/>"
        }
      </c:forEach>
      ]
    }
  ],
   "dataset":[
   	{
      "seriesname":"<cms:contentText key="PCT_AWARDS_RECEIVED" code="${report.cmAssetCode}"/>",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
          		"value":"<fmt:formatNumber value="${reportItem.receivedPct}" />"
        	}
      	</c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="PCT_AWARDS_NOTRECEIVED" code="${report.cmAssetCode}"/>",
      "data":[
		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
			<c:if test="${reportDataStatus.index != 0}">,</c:if>
	      	{
	       		"value":"<fmt:formatNumber value="${reportItem.notreceivedPct}" />"
	        }
		</c:forEach>
      ]
    }
  ] 
  </c:if>
}