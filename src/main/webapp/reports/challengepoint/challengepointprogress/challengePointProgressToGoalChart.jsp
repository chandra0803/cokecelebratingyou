<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	"messages": [],
	"chart": {
  			"rotateValues": "1"
  	},
	"data": [
	  <c:set var="hasData" value="false"/>
  	  
  	  <c:if test="${ (reportData.level0To25Count != null && reportData.level0To25Count > 0) ||
  	                 (reportData.level26To50Count != null && reportData.level26To50Count > 0) ||
  	                 (reportData.level51To75Count != null && reportData.level51To75Count > 0) ||
  	                 (reportData.level76To99Count != null && reportData.level76To99Count > 0) ||
  	                 (reportData.level100Count != null && reportData.level100Count > 0)}">
  	    <c:set var="hasData" value="true"/>
  	  </c:if>
  	  <c:if test="${hasData == 'true' }">  
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
	   </c:if>
  	]
}