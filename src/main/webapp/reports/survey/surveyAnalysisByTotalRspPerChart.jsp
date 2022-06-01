<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
              <c:if test="${fn:length(reportData) > 0 }">
              "labelDisplay":"ROTATE",	
		      "slantLabels":"1"
		    </c:if>
		    
           },
   "categories":[
  	{
      "category":[
      <c:forEach var="reportItem" items="${surveyResponseList}" varStatus="reportDataStatus">
      	<c:if test="${reportDataStatus.index != 0}">,</c:if>
        {
          "label": "<c:out value="${reportItem.headerValue}"/>",
          "toolText":"<c:out value="${reportItem.headerValue}"/>"
        }
      </c:forEach>
      ]
    }
  ],
   "dataset": [
	   <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
	      <c:if test="${reportDataStatus.index != 0}">,</c:if>
	      {
	        "seriesname": "<c:out value="${reportItem.nodeName}" />",
	        "data":[
	            <c:if test="${ response1 }">
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response1SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response2 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response2SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response3 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response3SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response4 }">
	        	  , 
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response4SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response5 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response5SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response6 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response6SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response7 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response7SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response8 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response8SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response9 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response9SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        	<c:if test="${ response10 }">
	        	  ,
	      		  {
	          		"value":"<fmt:formatNumber value="${reportItem.response10SelectedPerc}"/>"
	        	  }
	        	</c:if>
	        ]
	    }
      </c:forEach>
    ]
  </c:if>  
}