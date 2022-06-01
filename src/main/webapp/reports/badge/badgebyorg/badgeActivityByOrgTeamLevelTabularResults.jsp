<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.badge.BadgeActivityByOrgReportValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf" %>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="PARTICIPANT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PARTICIPANT_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtTeamLevel&parentNodeId=${reportParametersForm.parentNodeId}&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "",
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
              "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtTeamLevel&parentNodeId=${reportParametersForm.parentNodeId}&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="BADGES_EARNED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="BADGES_EARNED_PAX_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtTeamLevel&parentNodeId=${reportParametersForm.parentNodeId}&sortedOn=4&sortedBy=",
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
              "url": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=extractReport"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=extractReport"
          },
      	  "inlineTextDescription": ""
        },
        "results": 
        [
         <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "${reportItem.participantName}",
            <%
   				BadgeActivityByOrgReportValue reportValue = (BadgeActivityByOrgReportValue) pageContext.getAttribute( "reportItem" );
			    Map<String, Object> paramMap = new HashMap<String, Object>();
			    paramMap.put( "userId", reportValue.getPaxId(  ));
			    String urlToUse = "/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtParticipantLevel";
			    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
			%>
            "<%=drilldownUrl %>",
            "<c:out value="${reportItem.country}" />",
            "<fmt:formatNumber value="${reportItem.badgesEarned}" />"
          ]
          </c:forEach>
        ]
      }
    ],