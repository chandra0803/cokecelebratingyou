<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
  	"chart": {
  	           <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1",
   			  </c:if>
   			  "rotateValues": "1"
  	         },
  "categories":[{
      "category":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
       		{
        			"label": "${fn:substring(reportItem.hierarchyNodeName,0,10)}<c:if test="${fn:length(reportItem.hierarchyNodeName) > 10 }">..</c:if>",
                    "toolText":"<c:out value="${reportItem.hierarchyNodeName}"/>"
        			
       		}
      	</c:forEach>
      ]
    }
  ],
   "dataset":[
    {
      "seriesname":"<cms:contentText key="ACTIVE_STATUS" code="${report.cmAssetCode}"/>",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
          		"value":"<c:if test="${participantStatus eq null || participantStatus eq 'active'  }"><fmt:formatNumber value="${reportItem.activeCnt}" /></c:if>"
        	}
      	</c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="INACTIVE_STATUS" code="${report.cmAssetCode}"/>",
      "data":[
      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		{
         		"value":"<c:if test="${participantStatus eq null || participantStatus eq 'inactive' }" ><fmt:formatNumber value="${reportItem.inactiveCnt}" /></c:if>"
        	}
      	</c:forEach>
      ]
    }
  ]
  </c:if>
}