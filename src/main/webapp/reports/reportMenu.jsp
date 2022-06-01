<%@ include file="/include/taglib.jspf"%>

{
	"messages":[],
	"reports":[
		<c:forEach var="reportItem" items="${reportList}" varStatus="reportListStatus">
		  <c:choose>
            <c:when test="${reportItem.reportCode == 'hierarchyExport' || 
                            reportItem.reportCode == 'participantExport' ||
                            reportItem.reportCode == 'awardItemSelectionByOrg' ||
                            reportItem.reportCode == 'awardLevelActivityByOrg'}">
              <beacon:authorize ifAnyGranted="BI_ADMIN,VIEW_REPORTS">
                <c:if test="${reportListStatus.index != 0}">,</c:if>
       		    {
	  			  "displayName": "<cms:contentText key="${reportItem.name}" code="${reportItem.cmAssetCode}"/>" ,
	    		  
				 <c:choose>
					<c:when test="${reportItem.name == 'CELEBRATION_ACTIVITY_REPORT'}">
					"url": "<c:out value="${reportItem.url}" escapeXml="false" />" ,
					</c:when>
					<c:otherwise>
					"url": "<c:out value="${reportItem.url}&clearForm=true" escapeXml="false" />" ,
					</c:otherwise>
				 </c:choose>	
				    		  
	    		  "id": "<c:out value="${reportItem.id}"/>" ,
	    		  "category": "<c:out value="${reportItem.categoryType.name}"/>" ,
	    		  "categoryNameId": "<c:out value="${reportItem.categoryType.code}"/>",
	    		  "desc": "<cms:contentText key="${reportItem.description}" code="${reportItem.cmAssetCode}"/>",
	    		  "exportAndFilters":${reportItem.reportCode == 'happinessPulse' || reportItem.reportCode == 'confidentialFeedback'}, 
	    		  "forceParametersPopover": ${reportItem.forceParameters},
	    		  "exportOnly": ${reportItem.exportOnly}<c:if test="${reportItem.exportOnly}">,</c:if>
	    		  <c:if test="${reportItem.exportOnly}">
	    		    <c:choose>
	    		      <c:when test="${largeAudience == true }">
	    		        "exportText": "<cms:contentText key="EXPORT_ONLY_TEXT_LARGE_AUD" code="${reportItem.cmAssetCode}"/>"
	    		      </c:when>
	    		      <c:otherwise>
	    		        "exportText": "<cms:contentText key="EXPORT_ONLY_TEXT" code="${reportItem.cmAssetCode}"/>"
	    		      </c:otherwise>
	    		    </c:choose>
	    		  </c:if>
	  		    }
	          </beacon:authorize>
	        </c:when>
	        <c:otherwise>
	          <c:if test="${reportListStatus.index != 0}">,</c:if>
	          {
	  	        "displayName": "<cms:contentText key="${reportItem.name}" code="${reportItem.cmAssetCode}"/>" ,
	    	    
				<c:choose>
				 <c:when test="${reportItem.name == 'CELEBRATION_ACTIVITY_REPORT'}">
					"url": "<c:out value="${reportItem.url}" escapeXml="false" />" ,
				 </c:when>
				 <c:otherwise>
					"url": "<c:out value="${reportItem.url}&clearForm=true" escapeXml="false" />" ,
				 </c:otherwise>
				 </c:choose>
				 	    	    
	    	    "id": "<c:out value="${reportItem.id}"/>" ,
	    	    "category": "<c:out value="${reportItem.categoryType.name}"/>" ,
	    	    "categoryNameId": "<c:out value="${reportItem.categoryType.code}"/>",
	    	    "desc": "<cms:contentText key="${reportItem.description}" code="${reportItem.cmAssetCode}"/>",
	    	    "forceParametersPopover": ${reportItem.forceParameters},
	    	    "exportOnly": ${reportItem.exportOnly}<c:if test="${reportItem.exportOnly}">,</c:if>
	    	    <c:if test="${reportItem.exportOnly}">
      		       <c:choose>
	    		      <c:when test="${largeAudience == true }">
	    		        "exportText": "<cms:contentText key="EXPORT_ONLY_TEXT_LARGE_AUD" code="${reportItem.cmAssetCode}"/>"
	    		      </c:when>
	    		      <c:otherwise>
	    		        "exportText": "<cms:contentText key="EXPORT_ONLY_TEXT" code="${reportItem.cmAssetCode}"/>"
	    		      </c:otherwise>
	    		    </c:choose>
	    	    </c:if>
	  		  }
	        </c:otherwise>
	      </c:choose>
	 	</c:forEach>
	]
}