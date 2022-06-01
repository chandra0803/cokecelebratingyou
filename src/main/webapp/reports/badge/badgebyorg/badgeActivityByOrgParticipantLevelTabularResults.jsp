<%@ include file="/include/taglib.jspf" %>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="EARNED_DATE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="EARNED_DATE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtParticipantLevel&paxId=${reportParametersForm.paxId}&sortedOn=1&sortedBy=",
              "summary": ""
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
              "name": "<cms:contentText key="PARTICIPANT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PARTICIPANT_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtParticipantLevel&paxId=${reportParametersForm.paxId}&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtParticipantLevel&paxId=${reportParametersForm.paxId}&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="PROMOTION_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtParticipantLevel&paxId=${reportParametersForm.paxId}&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="BADGE_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="BADGE_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=displayBadgesEarnedAtParticipantLevel&paxId=${reportParametersForm.paxId}&sortedOn=6&sortedBy=",
              "summary": ""
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
            "url": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=extractReport&exportLevel=pax"
          }
        ],
        "exportCurrentView": {
          "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
          "url": "${systemUrl}/reports/displayBadgeActivityByOrgReport.do?method=extractReport&exportLevel=pax"
         },
      	 "inlineTextDescription": ""
        },
        "results": 
        [
         <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "<fmt:formatDate value="${reportItem.badgeEarnedDate}" pattern="${JstlDatePattern}" />",
            false,
            "<c:out value="${reportItem.participantName}" />",
            "<c:out value="${reportItem.orgName}" />",
            "<c:out value="${reportItem.promotionName}" />",
            "<c:out value="${reportItem.badgeName}" escapeXml="false"/>"
          ]
          </c:forEach>
        ]
      }
    ],