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
              "name": "<cms:contentText code="${report.cmAssetCode}" key="PROMOTION_NAME"></cms:contentText>",
              "description": "<cms:contentText code="${report.cmAssetCode}" key="PROMOTION_NAME_DESC"></cms:contentText>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText code="${report.cmAssetCode}" key="TIME_PERIOD"></cms:contentText>",
              "description": "<cms:contentText code="${report.cmAssetCode}" key="TIME_PERIOD_DESC"></cms:contentText>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=2&sortedBy=",
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING" args="1"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING_DESC" args="1"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[0].numberPending}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING" args="1"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING_DESC" args="1"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[0].averageWait}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED" args="1"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED_DESC" args="1"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[0].numberApproved}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER" args="1"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER_DESC" args="1"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[0].numberWinner}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER" args="1"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER_DESC" args="1"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[0].numberDenied}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO" args="1"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO_DESC" args="1"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[0].numberMoreInfo}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING" args="2"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING_DESC" args="2"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[1].numberPending}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING" args="2"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING_DESC" args="2"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[1].averageWait}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED" args="2"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED_DESC" args="2"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[1].numberApproved}"/>"
            },
            {
              "id": "12",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER" args="2"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER_DESC" args="2"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=12&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[1].numberWinner}"/>"
            },
            {
              "id": "13",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER" args="2"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER_DESC" args="2"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=13&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[1].numberDenied}"/>"
            },
            {
              "id": "14",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO" args="2"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO_DESC" args="2"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=14&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[1].numberMoreInfo}"/>"
            },
            {
              "id": "15",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING" args="3"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING_DESC" args="3"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=15&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[2].numberPending}"/>"
            },
            {
              "id": "16",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING" args="3"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING_DESC" args="3"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=16&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[2].averageWait}"/>"
            },
            {
              "id": "17",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED" args="3"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED_DESC" args="3"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=17&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[2].numberApproved}"/>"
            },
            {
              "id": "18",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER" args="3"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER_DESC" args="3"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=18&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[2].numberWinner}"/>"
            },
            {
              "id": "19",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER" args="3"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER_DESC" args="3"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=19&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[2].numberDenied}"/>"
            },
            {
              "id": "20",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO" args="3"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO_DESC" args="3"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=20&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[2].numberMoreInfo}"/>"
            },
            {
              "id": "21",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING" args="4"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING_DESC" args="4"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=21&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[3].numberPending}"/>"
            },
            {
              "id": "22",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING" args="4"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING_DESC" args="4"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=22&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[3].averageWait}"/>"
            },
            {
              "id": "23",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED" args="4"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_APPROVED_DESC" args="4"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=23&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[3].numberApproved}"/>"
            },
            {
              "id": "24",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER" args="4"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER_DESC" args="4"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=24&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[3].numberWinner}"/>"
            },
            {
              "id": "25",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER" args="4"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER_DESC" args="4"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=25&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[3].numberDenied}"/>"
            },
            {
              "id": "26",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO" args="4"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO_DESC" args="4"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=26&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[3].numberMoreInfo}"/>"
            },
            {
              "id": "27",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING" args="5"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_PENDING_DESC" args="5"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=27&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[4].numberPending}"/>"
            },
            {
              "id": "28",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING" args="5"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_DAYS_WAITING_DESC" args="5"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=28&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[4].averageWait}"/>"
            },
            {
              "id": "29",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER" args="5"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_WINNER_DESC" args="5"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=29&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[4].numberWinner}"/>"
            },
            {
              "id": "30",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER" args="5"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_NON_WINNER_DESC" args="5"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=30&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[4].numberDenied}"/>"
            },
            {
              "id": "31",
              "name": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO" args="5"></cms:contentTemplateText>",
              "description": "<cms:contentTemplateText code="${report.cmAssetCode}" key="APPROVAL_MORE_INFO_DESC" args="5"></cms:contentTemplateText>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=31&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData[4].numberMoreInfo}"/>"
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
              "label": "<cms:contentText key="DETAIL_EXTRACT" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayNominationAgingReport.do?method=extractReport&exportViewType=fullView&extractType=primary"
            },
            {
              "label": "<cms:contentText key="SUMMARY_EXTRACT" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayNominationAgingReport.do?method=extractSecondaryReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="DETAIL_EXTRACT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayNominationAgingReport.do?method=extractReport&exportViewType=currentView&extractType=primary"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
          <c:forEach var="agingValue" items="${reportData}" varStatus="agingValueStatus">
            [
              "${agingValue.promotionName}",
              "${agingValue.timePeriod}",
              <c:forEach var="agingLevelValue" items="${agingValue.levelData}" varStatus="agingLevelValueStatus">
                "${agingLevelValue.numberPending}",
                "${agingLevelValue.averageWait}",
                <c:if test="${agingLevelValueStatus.index < fn:length(agingValue.levelData) - 1}">
                "${agingLevelValue.numberApproved}",
                </c:if>
                "${agingLevelValue.numberWinner}",
                "${agingLevelValue.numberDenied}",
                "${agingLevelValue.numberMoreInfo}"
                <c:if test="${agingLevelValueStatus.index < fn:length(agingValue.levelData) - 1}">,</c:if>
              </c:forEach>
            ]
            <c:if test="${agingValueStatus.index < fn:length(reportData) - 1}">,</c:if>
          </c:forEach>
        ]
      }
    ],