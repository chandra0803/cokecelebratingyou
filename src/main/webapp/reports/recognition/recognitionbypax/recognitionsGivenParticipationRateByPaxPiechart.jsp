<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
	"chart": {},  
 
	"data":[
		<c:forEach var="reportItem" items="${reportData}">
			<c:if test="${reportItem.haveGivenUniquePct > 0 || reportItem.haveNotGivenUniquePct > 0}">
			{
       			"value": "<fmt:formatNumber value="${reportItem.haveGivenUniquePct}"/>",
       			"label": "<cms:contentText key="HAVE_GIVEN_PCT" code="${report.cmAssetCode}"/>"
      		},
      		{
       			"value": "<fmt:formatNumber value="${reportItem.haveNotGivenUniquePct}"/>",
       			"label": "<cms:contentText key="HAVE_NOT_GIVEN_PCT" code="${report.cmAssetCode}"/>"
      		}
      		</c:if>
      	</c:forEach>
   ] 
   </c:if>    
}