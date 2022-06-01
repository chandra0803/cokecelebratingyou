<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
  	"chart": {
  			"rotateValues": "1"
 
  	},
  	"data": 
  	[    
   		<c:if test="${displayLevel1}">
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_1_ACHIEVED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level1AchievedCnt}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel2}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_2_ACHIEVED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level2AchievedCnt}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel3}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_3_ACHIEVED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level3AchievedCnt}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel4}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_4_ACHIEVED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level4AchievedCnt}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel5}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_5_ACHIEVED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level5AchievedCnt}"/>"
	   		}
   		</c:if>
   		<c:if test="${displayLevel6}">
	   		,
	   		{       			
	    		"label":"<cms:contentText key="LEVEL_6_ACHIEVED" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.level6AchievedCnt}"/>"
	   		}
   		</c:if>
  	]
}