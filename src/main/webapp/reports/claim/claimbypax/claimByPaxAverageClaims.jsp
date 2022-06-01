<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
    "chart": { 
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1"
    		 },
	  "data":[
		      	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
					{
					   "label": "${fn:substring(reportItem.paxName,0,10)}<c:if test="${fn:length(reportItem.paxName) > 10 }">..</c:if>",
					   "toolText":"<c:out value="${reportItem.paxName}"/>",
					   "value": "<fmt:formatNumber value="${reportItem.totalClaims}"/>"
					}
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>    
	  ],
	  "trendlines":{
	  	<%-- Adding two lines so that the label and value both are displayed. Fusion charts is displaying only either value or name not both when there is only one line. Bug 51113  --%>
	    "line":[
	      {
	        "startvalue":"${averageClaims}",
	        "color":"009933",
	        "valueOnRight" :"1",
	        "thickness":"3",
	        "displayvalue":"<fmt:formatNumber value="${averageClaims}"/>"
	      },
	      {
	        "startvalue":"${averageClaims}",
	        "color":"009933",
	        "thickness":"0",
	        "displayvalue":"<cms:contentText key="AVERAGE" code="${report.cmAssetCode}"/>"
	      }
	    ]
	  }
    </c:if>
}