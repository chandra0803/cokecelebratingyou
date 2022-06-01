<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
	<c:if test="${fn:length(reportData) > 0}">
    "messages": [],
  	"chart": {
  	          <c:if test="${fn:length(reportData) > 5 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1",
		      </c:if>
		      "rotateValues": "1"
		     },
  "categories": [
    {
      "category": [
      	<c:forEach var="approvalStatus" items="${approvalStatusTypeList}" varStatus="statusIndex">
	        {
	          "label": "${fn:substring(approvalStatus.name,0,10)}<c:if test="${fn:length(approvalStatus.name) > 10 }">..</c:if>",
              "toolText":"<c:out value="${approvalStatus.name}"/>"
	        }
	        <c:if test="${statusIndex.index < fn:length(approvalStatusTypeList) - 1}">,</c:if>
        </c:forEach>
      ]
    }
  ],
  "dataset": [
  <c:set var="tempNominationName" value=""/>
  <c:set var="count" value="0"/>
  	<c:forEach var="nominationPromotion" items="${reportData}" varStatus="promotionStatus">
  	   <c:if test="${ tempNominationName != nominationPromotion.promoName }">
  	     <c:if test="${count != 0}">,</c:if> 
      	{    
	      "seriesname": "<c:out value="${nominationPromotion.promoName}" escapeXml="false"/>",
	      "data": [
	      	<c:forEach var="approvalStatus" items="${approvalStatusTypeList}" varStatus="statusIndex">
	      		<c:set var="nominationCount" value="0"/>
	      		<c:forEach var="reportItem" items="${reportData}">
	      			<c:if test="${reportItem.approvalStatus == approvalStatus.name && nominationPromotion.promoName eq reportItem.promoName}">
	      				<c:set var="nominationCount" value="${reportItem.nominationCount}"/>
	      			</c:if>
	      		</c:forEach>	      			      		
		        {
		          "value": "<fmt:formatNumber value="${nominationCount}"/>"
		        }
		        <c:if test="${statusIndex.index < fn:length(approvalStatusTypeList) - 1}">,</c:if>
	        </c:forEach>
	       <c:set var="tempNominationName" value="${nominationPromotion.promoName}"/>
	      ]
	    }
	    <c:set var="count" value="${count+1}"/>
	   </c:if>		    
    </c:forEach>
  ]
  </c:if>
}