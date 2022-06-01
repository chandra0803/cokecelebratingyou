<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
  	"chart": {
 		"numberSuffix":"%",
 		"rotateValues": "1"
  	},
  	"data": 
  	[    
   		<c:if test="${displayLevel1}">
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_1_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level1AchievedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel2}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_2_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level2AchievedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel3}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_3_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level3AchievedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel4}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_4_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level4AchievedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel5}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_5_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level5AchievedPercent}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel6}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_6_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level6AchievedPercent}"/>"
	   		}
   		</c:if>
  	]
}