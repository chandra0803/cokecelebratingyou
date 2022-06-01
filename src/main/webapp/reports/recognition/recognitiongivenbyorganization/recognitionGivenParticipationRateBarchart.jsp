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
		    "rotateValues": "1"
           },
   "categories":[
  	{
      "category":[
      <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      	<c:if test="${reportDataStatus.index != 0}">,</c:if>
        {
          "label": "${fn:substring(reportItem.nodeName,0,10)}<c:if test="${fn:length(reportItem.nodeName) > 10 }">..</c:if>",
          "toolText":"<c:out value="${reportItem.nodeName}"/>"
        }
      </c:forEach>
      ]
    }
  ],
   "dataset":[
   	{
      "seriesname":"<cms:contentText key="RECOGNITIONS_HAVE_GIVEN" code="${report.cmAssetCode}"/>",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
          		"value":"<fmt:formatNumber value="${reportItem.haveGivenCnt}"/>"
        	}
      	</c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="RECOGNITIONS_HAVE_NOT_GIVEN" code="${report.cmAssetCode}"/>",
      "data":[
		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
			<c:if test="${reportDataStatus.index != 0}">,</c:if>
	      	{
	       		"value":"<fmt:formatNumber value="${reportItem.haveNotGivenCnt}"/>"
	        }
		</c:forEach>
      ]
    }
  ] 
  </c:if>
}