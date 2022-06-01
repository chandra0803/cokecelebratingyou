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
  "data": [
    <logic:iterate id="reportItem" name="reportData" indexId="totalCntIndexId">
      <c:if test="${totalCntIndexId!=0}">,</c:if>
     {
       "label":"<c:out value="${reportItem.month}"/>",
       "value":"<fmt:formatNumber value="${reportItem.totalCnt}"/>"
     }
    </logic:iterate>
  ]
  </c:if>  
}