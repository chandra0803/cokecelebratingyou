<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}"> 
      "messages": [],
      "chart": {},  
 
      "data":[
       <c:forEach var="reportItem" items="${reportData}">
       <c:if test="${reportItem.haveReceivedPct > 0 || reportItem.haveNotReceivedPct > 0}">
	      {
	       "value": "<fmt:formatNumber value="${reportItem.haveReceivedPct}"/>",
	       "label": "<cms:contentText key="HAVE_RECEIVED_PCT" code="${report.cmAssetCode}"/>"
	      },
	      {
	       "value": "<fmt:formatNumber value="${reportItem.haveNotReceivedPct}"/>",
	       "label": "<cms:contentText key="HAVE_NOT_RECEIVED_PCT" code="${report.cmAssetCode}"/>"
	      }
      </c:if>
      </c:forEach>
   ]
   </c:if>     
}