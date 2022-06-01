<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.biperf.core.domain.hierarchy.Node"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf" %>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "Organization drill-down URL",
              "description": "",
              "type": "URL",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            }<c:if test="${! empty promotionBehaviorTypeList }">,</c:if>
            <c:forEach var="promotionBehavior" items="${promotionBehaviorTypeList}" varStatus="behaviorStatus">
				{
	              "id": "<c:out value="${behaviorStatus.index + 3}"/>",
	              "name": "${promotionBehavior}",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              <c:set var="totalAmount" value="0"/>
	              <c:forEach var="totalRow" items="${totalsRowData}">
	              	<c:if test="${totalRow.behavior eq promotionBehavior}"><c:set var="totalAmount" value="${totalRow.bCnt}"/></c:if>
	              </c:forEach>
	              "summary": "<fmt:formatNumber value="${totalAmount}"/>"
	            }		
	            <c:if test="${behaviorStatus.index < fn:length(promotionBehaviorTypeList) - 1}">,</c:if>
			</c:forEach>                               
          ],
          <c:choose>
	          <c:when test="${reportParametersForm.sortedOn > 0}">
		          "sortedOn": "${reportParametersForm.sortedOn}",
		          "sortedBy": "${reportParametersForm.sortedBy}",
	          </c:when>
	          <c:otherwise>
		          "sortedOn": "1",
		          "sortedBy": "asc",
	          </c:otherwise>
          </c:choose>
          "maxRows": "${maxRows}",
          "exportFullReportUrl": [
            {
              "label": "<cms:contentText key="EXTRACT_FULL" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayBehaviorsReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayBehaviorsReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
         <c:if test="${not empty promotionBehaviorTypeList}" >
          <c:forEach var="childNode" items="${childNodeList}" varStatus="childNodeStatus">
        		[
		            "${childNode.name}",
        			
        			<c:choose>
		    			<c:when test="${(isLeaf!=null && isLeaf) or fn:endsWith(childNode.name, 'Team')}">
		    				false,
		    			</c:when>
		    			<c:otherwise>
		    				<%
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", ((Node)pageContext.getAttribute( "childNode" )).getId() );
							    String urlToUse = "/reports/displayBehaviorsReport.do?method=displaySummaryReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
			                "<%=drilldownUrl %>",
		    			</c:otherwise>
		    	  	</c:choose>
		            
		            <c:forEach var="promotionBehavior" items="${promotionBehaviorTypeList}" varStatus="behaviorStatus">
						<c:set var="countFound" value="false"/>
						
						<c:forEach var="reportDataId" items="${reportData}">
							<c:if test="${reportDataId.detailNodeId == childNode.id and reportDataId.behavior == promotionBehavior}">
								 <c:set var="countFound" value="true"/>
								 "<fmt:formatNumber value="${reportDataId.bCnt}" />"						 
							</c:if>
						</c:forEach>
						
						<c:if test="${not countFound}">"<c:out value="0"/>"</c:if>												
      					<c:if test="${behaviorStatus.index < fn:length(promotionBehaviorTypeList) - 1}">,</c:if>
					</c:forEach>
		        ]
		        <c:if test="${childNodeStatus.index < fn:length(childNodeList) - 1}">,</c:if>
        	</c:forEach>    
         </c:if>       
        ]
      }
    ],