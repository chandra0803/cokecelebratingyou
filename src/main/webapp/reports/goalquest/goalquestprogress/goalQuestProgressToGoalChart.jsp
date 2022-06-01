<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
	"chart": {
  			"rotateValues": "1"
  	},
	"data": [
		{
			"label":"<cms:contentText key="LEVEL_0_25_COUNT" code="${report.cmAssetCode}"/>",
	      		"value":"<fmt:formatNumber value="${reportData.level0To25Count}"/>"
	    	},
	    	{
			"label":"<cms:contentText key="LEVEL_26_50_COUNT" code="${report.cmAssetCode}"/>",
	      		"value":"<fmt:formatNumber value="${reportData.level26To50Count}"/>"
	    	},
	    	{
			"label":"<cms:contentText key="LEVEL_51_75_COUNT" code="${report.cmAssetCode}"/>",
	      		"value":"<fmt:formatNumber value="${reportData.level51To75Count}"/>"
	    	},
	    	{
			"label":"<cms:contentText key="LEVEL_76_99_COUNT" code="${report.cmAssetCode}"/>",
	      		"value":"<fmt:formatNumber value="${reportData.level76To99Count}"/>"
	    	},
	    	{
			"label":"<cms:contentText key="LEVEL_100_COUNT" code="${report.cmAssetCode}"/>",
	      		"value":"<fmt:formatNumber value="${reportData.level100Count}"/>"
	    	}
  	]
}