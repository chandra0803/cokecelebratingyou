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
		    "numberSuffix":"%",
		    "yAxisMaxValue":"100"
           },
  "categories":[
  	{
      "category":[
      <logic:iterate id="reportItem" name="reportData" indexId="catIndexId">
      <c:if test="${catIndexId!=0}">,</c:if>
        {
          "label": "${fn:substring(reportItem.nodeName,0,10)}<c:if test="${fn:length(reportItem.nodeName) > 10 }">..</c:if>",
          "toolText":"<c:out value="${reportItem.nodeName}"/>"
        }
      </logic:iterate>
      ]
    }
  ],
  "dataset":[
 	{
      "seriesname":"<cms:contentText key="LOGGED_IN_PCT" code="report.login.activity"/>",
      "data":[
	     <logic:iterate id="reportItem" name="reportData" indexId="logInIndexId">
	     <c:if test="${logInIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.loggedInPct}"/>"
        }
	     </logic:iterate>
      ]
    },
    {
      "seriesname":"<cms:contentText key="NOT_LOGGED_IN_PCT" code="report.login.activity"/>",
      "data":[
      	<logic:iterate id="reportItem" name="reportData" indexId="notLogInIndexId">
		<c:if test="${notLogInIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.notLoggedInPct}"/>"
        }
         </logic:iterate>
      ]
    }
  ]
  </c:if>  
}