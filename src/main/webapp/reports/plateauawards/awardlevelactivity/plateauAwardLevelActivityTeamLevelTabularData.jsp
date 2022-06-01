<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayPlateauAwardLevelActivityReport.do?method=displayTeamLevelReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="LEVEL_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="LEVEL_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="CODES_ISSUED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CODES_ISSUED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.issuedCnt}"/>"
            },
            {
              "id": "3",
              "name": "<cms:contentText key="CODES_REDEEMED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CODES_REDEEMED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.redeemedCnt}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="PERC_REDEEMED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERC_REDEEMED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.redeemedPct}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="CODES_NOT_REDEEMED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CODES_NOT_REDEEMED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.notRedeemedCnt}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="PERC_NOT_REDEEMED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERC_NOT_REDEEMED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.notRedeemedPct}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="CODES_EXPIRED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CODES_EXPIRED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.expiredCnt}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="PERC_EXPIRED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERC_EXPIRED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "total",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.expiredPct}"/>"
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
              "url":  "${systemUrl}/reports/displayPlateauAwardLevelActivityReport.do?method=extractReport"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayPlateauAwardLevelActivityReport.do?method=extractReport"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          		<c:if test="${reportDataStatus.index != 0}">,</c:if>
          		[
		            "<c:out value="${reportItem.levelName}" />",
		            "<fmt:formatNumber value="${reportItem.issuedCnt}" />",
		            "<fmt:formatNumber value="${reportItem.redeemedCnt}" />",
		            "<fmt:formatNumber value="${reportItem.redeemedPct}" />",
		            "<fmt:formatNumber value="${reportItem.notRedeemedCnt}" />",
		            "<fmt:formatNumber value="${reportItem.notRedeemedPct}" />",
		            "<fmt:formatNumber value="${reportItem.expiredCnt}" />",
		            "<fmt:formatNumber value="${reportItem.expiredPct}" />"
		  		]
          	</c:forEach>
        ]
      }
    ],