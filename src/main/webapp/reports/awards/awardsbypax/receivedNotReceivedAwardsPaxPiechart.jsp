<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
	"messages": [],
	"chart": {},  
	"data":[
		<c:forEach var="reportItem" items="${reportData}">
		<c:if test="${reportItem.receivedPct > 0 || reportItem.notreceivedPct > 0 }">
			{
				"value": "<fmt:formatNumber value="${reportItem.receivedPct}" />",
       			"label":"<cms:contentText key="PCT_AWARDS_RECEIVED" code="${report.cmAssetCode}"/>"
      		},
      		{
       			"value": "<fmt:formatNumber value="${reportItem.notreceivedPct}" />",
       			"label":"<cms:contentText key="PCT_AWARDS_NOTRECEIVED" code="${report.cmAssetCode}"/>"
      		}
      	</c:if>	
		</c:forEach>
   ] 
}