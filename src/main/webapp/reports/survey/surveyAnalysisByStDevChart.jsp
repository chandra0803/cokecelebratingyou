<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
              <c:if test="${fn:length(reportData) > 0 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1"
		    </c:if>
		    
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
      "seriesname":"<cms:contentText key="SERIES_STD_DEV" code="${report.cmAssetCode}" />",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
          		"value":"<fmt:formatNumber value="${reportItem.standardDeviation}"/>"
        	}
      	</c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="MEAN" code="${report.cmAssetCode}" />",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
          		"value":"<fmt:formatNumber value="${reportItem.meanValue}"/>"
        	}
      	</c:forEach>
      ]
    }
  ]
  </c:if>  
}