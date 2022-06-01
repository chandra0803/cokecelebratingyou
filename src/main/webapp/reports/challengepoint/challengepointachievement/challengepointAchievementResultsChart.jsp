<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  "messages": [],
  "chart": {
  	        <c:if test="${fn:length(reportData) > 5 }">
            "labelDisplay":"ROTATE",	
		    "slantLabels":"1",
		    </c:if>
		    "rotateValues": "1"
		   },
	<c:if test="${fn:length(reportData) > 0}">
	"categories": [
		{
			"category": [
      			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	        		{
	          			"label": "<c:out value="${reportItem.paxName}"/>"
	        		}
	        		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
        		</c:forEach>
      		]
    	}
  	],
  	"dataset": [    
		{    
	   		"seriesname": "<cms:contentText key="BASE" code="${report.cmAssetCode}"/>",
	      	"data": [      			      		
	  			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.baseQuantity}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    },
	    {    
	      	"seriesname": "<cms:contentText key="GOAL" code="${report.cmAssetCode}"/>",
	      	"data": [      			      		
		  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.amountToAchieve}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    },
	    {    
	      	"seriesname": "<cms:contentText key="ACTUAL_RESULTS" code="${report.cmAssetCode}"/>",
	      	"data": [      			      		
		  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.currentValue}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    }
  ]
  </c:if>
}