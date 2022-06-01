<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {},
  "data": [
  	<c:set var="hasData" value="false"/>
      	<c:forEach var="item" items="${reportData}">
	      	<c:if test="${item.totalCnt1 > 0 || item.totalCnt2 > 0 }">
	      		<c:set var="hasData" value="true"/>
	      	</c:if>
      	</c:forEach>
      	<c:if test="${hasData}">
		  	<logic:iterate id="reportItem" name="reportData" indexId="index">
			  	{
				  	"label":"<cms:contentText key="LOGGED_IN_CNT" code="report.login.activity"/>",
				  	"value": "<fmt:formatNumber value="${reportItem.totalCnt1}"/>"
			  	},
			  	{
				  	"label":"<cms:contentText key="NOT_LOGGED_IN_CNT" code="report.login.activity"/>",
				  	"value": "<fmt:formatNumber value="${reportItem.totalCnt2}"/>"
			  	}
		  	</logic:iterate>
  		</c:if>
  ]
  </c:if>  
}