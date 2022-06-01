<%@ include file="/include/taglib.jspf"%>

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
  "data": [
    <logic:iterate id="reportItem" name="reportData" indexId="index">
      <c:if test="${index != 0}">,</c:if> 
      {
      
        "label": "${fn:substring(reportItem.nodeName,0,10)}<c:if test="${fn:length(reportItem.nodeName) > 10 }">..</c:if>",
        "toolText":"<c:out value="${reportItem.nodeName}"/>",
        "value": "<fmt:formatNumber value="${reportItem.totalCnt}"/>"
      }
      </logic:iterate>
  ]
  </c:if>  
}