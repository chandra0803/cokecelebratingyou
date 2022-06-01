<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
	"messages": [],
	"chart": {
		"numberSuffix":"%"
		},  
	"data":[
		<c:forEach var="reportItem" items="${reportData}">
		<c:if test="${reportItem.receivedPct > 0 || reportItem.notreceivedPct > 0 }">
			{
			    "label":"<cms:contentText key="PCT_AWARDS_RECEIVED" code="${report.cmAssetCode}"/>",
				"value": "<fmt:formatNumber value="${reportItem.receivedPct}" />"
      		},
      		{
      		    "label":"<cms:contentText key="PCT_AWARDS_NOTRECEIVED" code="${report.cmAssetCode}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.notreceivedPct}" />"
      		}
      	</c:if>	
		</c:forEach>
   ] 
}