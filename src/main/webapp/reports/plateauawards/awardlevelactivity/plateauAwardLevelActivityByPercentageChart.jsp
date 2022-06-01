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
		    "numberSuffix":"%"
           },
  "categories":[
  	{
      "category":[
      <logic:iterate id="reportItem" name="reportData" indexId="catIndexId">
      <c:if test="${catIndexId!=0}">,</c:if>
        {
          "label": "${fn:substring(reportItem.levelName,0,10)}<c:if test="${fn:length(reportItem.levelName) > 10 }">..</c:if>",
          "toolText":"<c:out value="${reportItem.levelName}"/>"
        }
      </logic:iterate>
      ]
    }
  ],
  "dataset":[
 	{
      "seriesname":"<cms:contentText key="PERC_EXPIRED" code="report.awardlevelactivity.byorg"/>",
      "data":[
	     <logic:iterate id="reportItem" name="reportData" indexId="expiredIndexId">
	     <c:if test="${expiredIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.expiredPct}"/>"
        }
	     </logic:iterate>
      ]
    },
    {
      "seriesname":"<cms:contentText key="PERC_NOT_REDEEMED" code="report.awardlevelactivity.byorg"/>",
      "data":[
      	<logic:iterate id="reportItem" name="reportData" indexId="notRedeemedIndexId">
		<c:if test="${notRedeemedIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.notRedeemedPct}"/>"
        }
         </logic:iterate>
      ]
    },
    {
      "seriesname":"<cms:contentText key="PERC_REDEEMED" code="report.awardlevelactivity.byorg"/>",
      "data":[
      	<logic:iterate id="reportItem" name="reportData" indexId="redeemedIndexId">
		<c:if test="${redeemedIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.redeemedPct}"/>"
        }
         </logic:iterate>
      ]
    }
  ]
  </c:if>  
}