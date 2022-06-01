<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
              <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1"
   			  </c:if>
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
      "seriesname":"<cms:contentText key="TOTAL_VISITS" code="report.login.activity"/>",
      "data":[
      <logic:iterate id="reportItem" name="reportData" indexId="totalCntIndexId">
      <c:if test="${totalCntIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.totalCnt}"/>"
        }
      </logic:iterate>
      ]
    },
    {
      "seriesname":"<cms:contentText key="ORG_AVG_VISITS" code="report.login.activity"/>",
      "data":[
      <logic:iterate id="reportItem" name="reportData" indexId="orgAvgIndexId">
      <c:if test="${orgAvgIndexId!=0}">,</c:if>
      	{
          "value":"<fmt:formatNumber value="${reportItem.orgAvgCnt}"/>"
        }
      </logic:iterate>
      ]
    }
  ] 
  </c:if>
}