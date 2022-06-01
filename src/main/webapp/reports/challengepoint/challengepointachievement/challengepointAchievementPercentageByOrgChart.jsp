<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
	"messages": [],
  	"chart": {
 		"numberSuffix":"%",
 		"rotateValues": "1"
  	},
	"categories": [
		{
			"category": [
      			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	        		{
	          			"label": "<c:out value="${reportItem.orgName}"/>"
	        		}
	        		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
        		</c:forEach>
      		]
    	}
  	],
  	"dataset": [    
  		<c:if test="${displayLevel1}">
			{    
		   		"seriesname": "<cms:contentText key="LEVEL_1_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
		  			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level1AchievedPercent}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
	    <c:if test="${displayLevel2}">
	    	,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_2_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level2AchievedPercent}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
	    <c:if test="${displayLevel3}">
	    	,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_3_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level3AchievedPercent}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
	    <c:if test="${displayLevel4}">
	    	,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_4_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level4AchievedPercent}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
	    <c:if test="${displayLevel5}">
	    	,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_5_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level5AchievedPercent}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
	    <c:if test="${displayLevel6}">
	    	,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_6_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level6AchievedPercent}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
  ]
  </c:if>
}