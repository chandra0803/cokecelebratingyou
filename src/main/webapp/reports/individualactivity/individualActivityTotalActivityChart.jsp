<%@ include file="/include/taglib.jspf"%>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

{ 
    "chart": {},  
	"data":
	[   
		<c:set var="hasData" value="false"/>
      	<c:forEach var="item" items="${reportData}">
	      	<c:if test="${item.received > 0 }">
	      		<c:set var="hasData" value="true"/>
	      	</c:if>
      	</c:forEach>
      	<c:if test="${hasData}"> 	
     	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
  			{       			
       			"label":"<c:out value="${reportItem.moduleName}"/>",
       			"value": "<fmt:formatNumber value="${reportItem.received}"/>"
      		}
      		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
  		</c:forEach> 
  		</c:if>	
   ]  
}