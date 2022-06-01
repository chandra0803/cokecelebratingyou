<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
String reportUrl = "/reports/displayRecognitionPurlActivityReport.do?method=displayParticipantReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="RECIPIENT_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="RECIPIENT_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="PROMOTION_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentText key="RECIPIENT_COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="RECIPIENT_COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="OWNER_MANAGER_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="OWNER_MANAGER_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="AWARD_DATE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="AWARD_DATE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="PURL_STATUS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PURL_STATUS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="AWARD" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="AWARD_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": ""
            },
            {
              "id": "8",
              "name": "<cms:contentText key="CONTRIBUTORS_INVITED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CONTRIBUTORS_INVITED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributorsInvitedCnt}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="ACTUAL_CONTRIBUTORS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_CONTRIBUTORS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributedCnt}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="CONTRIBUTION_PERCENTAGE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CONTRIBUTION_PERCENTAGE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributionPct}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="CONTRIBUTIONS_POSTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CONTRIBUTIONS_POSTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributionPostedCnt}"/>"
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
              "url": "${systemUrl}/reports/displayRecognitionPurlActivityReport.do?method=extractReport&fullReport=full"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayRecognitionPurlActivityReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": 
        [
         <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "<c:out value="${reportItem.recipientFullName}" />",
            "<c:out value="${reportItem.promotionName}" />",
            "<c:out value="${reportItem.recipientCountry}" />",
            "<c:out value="${reportItem.managerFullName}" />",
            "<fmt:formatDate value="${reportItem.awardDate}" pattern="${JstlDatePattern}" />",
            "<c:out value="${reportItem.purlStatus}" />",
            <c:choose>
          		<c:when test="${empty reportItem.awardLevel}">
	         		"<fmt:formatNumber value="${reportItem.award}" />",
          		</c:when>
          		<c:otherwise>
	         		"<c:out value="${reportItem.awardLevel}" />",
          		</c:otherwise>
          	</c:choose>
            "<fmt:formatNumber value="${reportItem.contributorsInvitedCnt}" />",
            "<fmt:formatNumber value="${reportItem.contributedCnt}" />",
            "<fmt:formatNumber value="${reportItem.contributionPct}" />",
            "<fmt:formatNumber value="${reportItem.contributionPostedCnt}" />"
          ]
          </c:forEach>
        ]
      }
    ],