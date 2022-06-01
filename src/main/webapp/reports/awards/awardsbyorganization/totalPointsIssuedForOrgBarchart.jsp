<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {<c:if test="${fn:length(reportData) > 5 }">
            "labelDisplay":"ROTATE",	
		    "slantLabels":"1",
		    </c:if>
		    "rotateValues": "1"
		   },
   "dataset":[
   	{
      "seriesname":"<cms:contentText key="TOTAL_POINTS_ISSUED" code="${report.cmAssetCode}"/>",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
      	  		"label":"<c:out value="${reportItem.orgName}"/>",
          		"value":"<fmt:formatNumber value="${reportItem.earnedCount}" />"
        	}
      	</c:forEach>
      ]
    }
  ]
  </c:if>
} 
