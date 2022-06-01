<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
  	"chart": {},
  	"data": 
  	[    
	   		{       			
	    		"label":"<cms:contentText key="ACHIEVING_GOALS" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.totalAchieved}"/>"
	   		},
	   		{       			
	    		"label":"<cms:contentText key="NOT_ACHIEVING_GOALS" code="${report.cmAssetCode}"/>",
	    		"value": "<fmt:formatNumber value="${reportData.totalNotAchieved}"/>"
	   		}
  	]
}