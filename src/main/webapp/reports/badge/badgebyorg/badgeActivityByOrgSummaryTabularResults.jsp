<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.badge.BadgeActivityByOrgReportValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf"%>

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
          "sortUrl": "${report.url}&parentNodeId=${reportParametersForm.parentNodeId}&sortedOn=1&sortedBy=",
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
        },
        {
          "id": "3",
          "name": "<cms:contentText key="BADGES_EARNED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="BADGES_EARNED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&parentNodeId=${reportParametersForm.parentNodeId}&sortedOn=2&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.badgesEarned}" />"
        }
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
          "url": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=extractReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=extractReport&exportViewType=currentView"
      },
      "inlineTextDescription": ""
    },
    "results": [
    	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
   			<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		[		          	
				"<c:out value="${reportItem.orgName}" />",
				<c:choose>
				    <c:when test="${ reportItem.isTeam == 1 }">
						<c:choose>
							<c:when test="${reportItem.badgesEarned > 0 }">
								<%
				    				BadgeActivityByOrgReportValue reportValue = (BadgeActivityByOrgReportValue) pageContext.getAttribute( "reportItem" );
								    Map<String, Object> paramMap = new HashMap<String, Object>();
								    paramMap.put( "parentNodeId", reportValue.getHierarchyNodeId(  ) );
								    paramMap.put( "nodeAndBelow", 0 );
								    String urlToUse = "/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtTeamLevel";
								    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
								%>
					            "<%=drilldownUrl %>",
							</c:when>
							<c:otherwise>
								false,
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<%
		    				BadgeActivityByOrgReportValue reportValue = (BadgeActivityByOrgReportValue) pageContext.getAttribute( "reportItem" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "parentNodeId", reportValue.getHierarchyNodeId(  ) );
						    paramMap.put( "nodeAndBelow", 1 );
						    String urlToUse = "/reports/displayBadgeActivityByOrgReport.do?method=displaySummaryReport";
						    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
						%>
			            "<%=drilldownUrl %>",
					</c:otherwise>
				</c:choose>
				"<fmt:formatNumber value="${reportItem.badgesEarned}" />"
			]
      </c:forEach>
    ]
  }
],