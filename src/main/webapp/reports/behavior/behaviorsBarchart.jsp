<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(promotionBehaviorTypeList) > 0}">
	"messages": [],
  	"chart": {
  	           <c:if test="${fn:length(promotionBehaviorTypeList) > 5 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1"
		    </c:if>
  	         },
  	"data": 
  	[    	
    	<c:forEach var="promotionBehavior" items="${promotionBehaviorTypeList}" varStatus="behaviorStatus">
			<c:set var="behaviorTotal" value="0"/>
			<c:forEach var="reportItem" items="${reportTotals}">
				<c:if test="${reportItem.key == promotionBehavior}">
					<c:set var="behaviorTotal" value="${reportItem.value}"/>
				</c:if>
			</c:forEach>
			
			<c:if test="${behaviorStatus.index > 0}">,</c:if> 
      		{       			
      		   "label": "${fn:substring(promotionBehavior,0,10)}<c:if test="${fn:length(promotionBehavior) > 10 }">..</c:if>",
               "toolText":"${promotionBehavior}",
       		   "value": "<fmt:formatNumber value="${behaviorTotal}"/>"
      		}
		</c:forEach>
  	]
  	</c:if>
}