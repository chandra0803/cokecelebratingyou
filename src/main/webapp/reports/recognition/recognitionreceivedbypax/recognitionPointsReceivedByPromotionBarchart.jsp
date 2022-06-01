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
   "dataset":[
   	{
      "data":[
      <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      <c:if test="${reportDataStatus.index != 0}">,</c:if>
      	{
      	  "label": "${fn:substring(reportItem.promotionName,0,10)}<c:if test="${fn:length(reportItem.promotionName) > 10 }">..</c:if>",
          "toolText":"<c:out value="${reportItem.promotionName}"/>",
          "value":"<fmt:formatNumber value="${reportItem.recognitionPointsCnt}"/>"
        }
      </c:forEach>
      ]
    }
  ] 
  </c:if>
}