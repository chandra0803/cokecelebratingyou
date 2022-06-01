<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
	"chart": {},  
	"data":[
		<c:forEach var="reportItem" items="${reportData}">
		<c:if test="${reportItem.haveGivenPct > 0 || reportItem.haveNotGivenPct > 0}">
			{
				"value": "<fmt:formatNumber value="${reportItem.haveGivenPct}"/>",
       			"label":" <cms:contentText key="HAVE_GIVEN_PCT" code="${report.cmAssetCode}"/> "
      		},
      		{
       			"value": "<fmt:formatNumber value="${reportItem.haveNotGivenPct}"/>",
       			"label":" <cms:contentText key="HAVE_NOT_GIVEN_PCT" code="${report.cmAssetCode}"/> "
      		}
      	</c:if>	
		</c:forEach>
   ]
   </c:if>     
}