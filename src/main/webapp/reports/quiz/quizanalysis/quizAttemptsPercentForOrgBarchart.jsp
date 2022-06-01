<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
             <c:if test="${fn:length(reportData) > 5 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1",
		    </c:if>
		    "rotateValues": "1",
		    "numberSuffix":"%"
           },
   "categories":[
  	{
      "category":[
      <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      	<c:if test="${reportDataStatus.index != 0}">,</c:if>
        {
          "label": "${fn:substring(reportItem.quizName,0,10)}<c:if test="${fn:length(reportItem.quizName) > 10 }">..</c:if>",
          "toolText":"<c:out value="${reportItem.quizName}"/>"
        }
      </c:forEach>
      ]
    }
  ],
   "dataset":[
   	{
      "seriesname":"<cms:contentText key="PASSED_ATTEMPTS_PCT_S" code="${report.cmAssetCode}"/>",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
          		"value":"<fmt:formatNumber value="${reportItem.passedAttemptsPct}" />"
        	}
      	</c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="FAILED_ATTEMPTS_PCT_S" code="${report.cmAssetCode}"/>",
      "data":[
		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
			<c:if test="${reportDataStatus.index != 0}">,</c:if>
	      	{
	       		"value":"<fmt:formatNumber value="${reportItem.failedAttemptsPct}" />"
	        }
		</c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="INCOMPLETE_ATTEMPTS_PCT_S" code="${report.cmAssetCode}"/>",
      "data":[
		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
			<c:if test="${reportDataStatus.index != 0}">,</c:if>
	      	{
	       		"value":"<fmt:formatNumber value="${reportItem.incompleteAttemptsPct}" />"
	        }
		</c:forEach>
      ]
    }
  ] 
  </c:if>
}