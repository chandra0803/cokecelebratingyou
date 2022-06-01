<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
  	"chart": {
 			"rotateValues": "1"
  	},
  	"data": 
  	[ 
		{       			
    		"label":"<cms:contentText key="TOTAL_PAX_NO_GOAL" code="${report.cmAssetCode}"/>",
   			"value": "<fmt:formatNumber value="${reportData.noGoalSelected}"/>"
   		}
   		<c:if test="${displayLevel1}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_1_SELECTED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level1Selected}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel2}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_2_SELECTED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level2Selected}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel3}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_3_SELECTED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level3Selected}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel4}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_4_SELECTED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level4Selected}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel5}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_5_SELECTED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level5Selected}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel6}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_6_SELECTED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level6Selected}"/>"
	   		}
   		</c:if>
  	]
}