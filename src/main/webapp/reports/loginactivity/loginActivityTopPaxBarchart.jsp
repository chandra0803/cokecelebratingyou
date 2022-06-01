<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
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
       "label": "${fn:substring(reportItem.name,0,10)}<c:if test="${fn:length(reportItem.name) > 10 }">..</c:if>",
       "toolText":"<c:out value="${reportItem.name}"/>",
       "value":"<fmt:formatNumber value="${reportItem.totalCnt}"/>"
     }
    </logic:iterate>
  ]
  <c:if test="${fn:length(reportData) > 5 }">
  ,"styles": {
   "definition": [
    {
      "name": "myAxisTitlesFont",
      "type": "font",
      "font": "Arial",
      "size": "7",
      "bold": "1"
    }
    ],
    "application": [
    {
      "toobject": "DataLabels",
      "styles": "myAxisTitlesFont"
    }
    ]
  }
  </c:if>
}