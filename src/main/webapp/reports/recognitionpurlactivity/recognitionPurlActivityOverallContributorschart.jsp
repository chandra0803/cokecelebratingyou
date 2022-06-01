<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {},
      "data":[
      <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      	<c:if test="${reportDataStatus.index != 0}">,</c:if>
      	{
      	  "label":"<cms:contentText key="CONTRIBUTED_COUNT" code="report.recognition.purlactivity"/>",
          "value":"<fmt:formatNumber value="${reportItem.contributedCnt}"/>"
        },
        {
      	  "label":"<cms:contentText key="NOT_CONTRIBUTED_COUNT" code="report.recognition.purlactivity"/>",
          "value":"<fmt:formatNumber value="${reportItem.notContributedCnt}"/>"
        }
      </c:forEach>
   ]
   </c:if>
}
