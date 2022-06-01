<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
  	"chart": {
  	           "rotateValues": "1"
  	         },
	"categories": [
		{
			"category": [
        		{
          			"label": "<cms:contentText key="RECEIVED" code="${report.cmAssetCode}"/>"
        		},
        		{
          			"label": "<cms:contentText key="GIVEN" code="${report.cmAssetCode}"/>"
        		}
      		]
    	}
  	],
  	"dataset": [    
  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  			{    
		   		"seriesname": "${reportItem.promoName}",
		      	"data": [      			      		
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.pointsReceived}"/>"
	          		},
	          		{
	          			"value": "<fmt:formatNumber value="${reportItem.pointsGiven}"/>"
	          		}
		      	]
		    }
  			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
  		</c:forEach>		
  	]
  	</c:if>
}