<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
  	"chart": {
 		"numberSuffix":"%",
 		"showLabels": "0"
  	},
  	"data": 
  	[    
     	<c:if test="${reportData.noGoalSelectedPercent > 0 || reportData.level1SelectedPercent > 0 || reportData.level2SelectedPercent > 0 || reportData.level3SelectedPercent > 0 || reportData.level4SelectedPercent > 0 || reportData.level5SelectedPercent > 0 || reportData.level6SelectedPercent > 0}">
		{       			
    		"label":"<cms:contentText key="TOTAL_PAX_NO_GOAL_PERCENT" code="${report.cmAssetCode}"/>",
   			"value": "<fmt:formatNumber value="${reportData.noGoalSelectedPercent}"/>"
   		}
   		<c:if test="${displayLevel1}">
   			,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_1_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level1SelectedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel2}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_2_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level2SelectedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel3}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_3_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level3SelectedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel4}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_4_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level4SelectedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel5}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_5_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level5SelectedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel6}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_6_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level6SelectedPercent}"/>"
	   		}
   		</c:if>
   		</c:if>
  	]
}