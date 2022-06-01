<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
            <c:if test="${fn:length(reportData) > 4}">
   			  "labelDisplay": "ROTATE",
   			  "slantLabels": "1",
   			  </c:if>
   			  "rotateValues": "1"
           },
  "categories":[
  	{
      "category":[
      <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
        	<c:if test="${reportDataStatus.index != 0}">,</c:if>
          {
            "label": "${fn:substring(reportItem.paxName,0,10)}<c:if test="${fn:length(reportItem.paxName) > 10 }">..</c:if>",
            "toolText":"<c:out value="${reportItem.paxName}"/>"
          }
      </c:forEach>
      ]
    }
  ],
  "dataset":[
 	{
      "seriesname":"<cms:contentText key="PLATEAU_EARNED" code="${report.cmAssetCode}" />",
      "data":[
	     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       <c:if test="${reportDataStatus.index != 0}">,</c:if>
      	    {
      	      
              "value":"<c:if test="${reportItem.plateauEarnedCnt > 0 }"><fmt:formatNumber value="${reportItem.plateauEarnedCnt}" /></c:if>"
            }

	     </c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
      "data":[
	     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       <c:if test="${reportDataStatus.index != 0}">,</c:if>
      	    {
              "value":"<c:if test="${reportItem.sweepstakesWonCnt > 0 }"><fmt:formatNumber value="${reportItem.sweepstakesWonCnt}" /></c:if>"
            }
	     </c:forEach>
      ]
    },
    <c:if test="${ onTheSpotAvailable }">
    {
      "seriesname":"<cms:contentText key="ONTHESPOT_DEPOSITED" code="${report.cmAssetCode}"/>",
      "data":[
	     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       
      		 <c:if test="${reportDataStatus.index != 0}">,</c:if>
      	     {
               "value":"<c:if test="${reportItem.onTheSpotDepositedCnt > 0 }"><fmt:formatNumber value="${reportItem.onTheSpotDepositedCnt}" /></c:if>"
             }
           
	     </c:forEach>
      ]
    },
    </c:if>
    {
      "seriesname":"<cms:contentText key="OTHER" code="${report.cmAssetCode}"/>",
      "data":[
	     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      	    {
              "value":"<c:if test="${reportItem.other > 0 }"><fmt:formatNumber value="${reportItem.other}" /></c:if>"
            }
          
	     </c:forEach>
      ]
    },
    {
      "seriesname":"<cms:contentText key="BADGES_EARNED" code="${report.cmAssetCode}"/>",
      "data":[
	     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	       
      		<c:if test="${reportDataStatus.index != 0}">,</c:if>
      	    {
              "value":"<c:if test="${reportItem.badgesEarnedCnt > 0 }"><fmt:formatNumber value="${reportItem.badgesEarnedCnt}" /></c:if>"
            }
          
	     </c:forEach>
      ]
    }
  ]
  <c:if test="${fn:length(reportData) > 5 }">
  ,"styles": {
   "definition": [
    {
      "name": "myAxisTitlesFont",
      "type": "font",
      "font": "Arial",
      "size": "7",
      "bold": "1"
    }
    ],
    "application": [
    {
      "toobject": "DataLabels",
      "styles": "myAxisTitlesFont"
    }
    ]
  }
  </c:if>
  </c:if>
}