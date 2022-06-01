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
		{    
	   		"seriesname": "<cms:contentText key="TOTAL_PAX_NO_GOAL" code="${report.cmAssetCode}"/>",
	      	"data": [      			      		
	  			<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	  				{
	          			"value": "<fmt:formatNumber value="${reportItem.noGoalSelected}"/>"
	          		}
        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
       			</c:forEach>
	      	]
	    }
	    <c:if test="${displayLevel1}">
		    ,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_1_SELECTED" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level1Selected}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
		</c:if>
		<c:if test="${displayLevel2}">
		    ,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_2_SELECTED" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level2Selected}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
		<c:if test="${displayLevel3}">
		    ,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_3_SELECTED" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level3Selected}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
		<c:if test="${displayLevel4}">
		    ,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_4_SELECTED" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level4Selected}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
		<c:if test="${displayLevel5}">
		    ,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_5_SELECTED" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level5Selected}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
	    <c:if test="${displayLevel6}">
		    ,
		    {    
		      	"seriesname": "<cms:contentText key="LEVEL_6_SELECTED" code="${report.cmAssetCode}"/>",
		      	"data": [      			      		
			  		<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		  				{
		          			"value": "<fmt:formatNumber value="${reportItem.level6Selected}"/>"
		          		}
	        			<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
	       			</c:forEach>
		      	]
		    }
	    </c:if>
  ]
    </c:if>
}