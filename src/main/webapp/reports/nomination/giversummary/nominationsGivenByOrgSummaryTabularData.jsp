<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.value.nomination.NominationGivenByOrgReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = report.getUrl(  );
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>
<c:set var="currencyCode" value="${currencyCode}"/>
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
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
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
              "name": "<cms:contentText key="ELIGIBLE_NOMINATORS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ELIGIBLE_NOMINATORS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.eligibleNominatorsCnt}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="ACTUAL_NOMINATORS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_NOMINATORS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
             "summary": "<fmt:formatNumber value="${totalsRowData.actualNominatorsCnt}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="PERC_ELIGIBLE_NOMINATORS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERC_ELIGIBLE_NOMINATORS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.eligbleNominatorsPct}"/>"
            },
			{
              "id": "6",
              "name": "<cms:contentText key="ELIGIBLE_NOMINEES" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ELIGIBLE_NOMINEES_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.eligibleNomineesCnt}"/>"
            },
			{
              "id": "7",
              "name": "<cms:contentText key="ACTUAL_NOMINEES" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_NOMINEES_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
             "summary": "<fmt:formatNumber value="${totalsRowData.actualNomineesCnt}"/>"
            },
			{
              "id": "8",
              "name": "<cms:contentText key="PERC_ELIGIBLE_NOMINEES" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERC_ELIGIBLE_NOMINEES_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.eligibleNomineesPct}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="TOTAL_NOMINATIONS_SUBMITTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_NOMINATIONS_SUBM_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.nominationsSubmitted}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="TOTAL_NOMINATIONS_RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_NOMINATIONS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.nominationsReceived}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="TOTAL_NOMINATIONS_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_NOMINATIONS_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.nominationsWonCnt}"/>"
            },
            {
              "id": "12",
              "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.pointsReceived}"/>"
            },        
            {
              "id": "13",
              "name": "<cms:contentTemplateText key="CASH_RECEIVED" code="${report.cmAssetCode}" args="${currencyCode}"/>",
              "description": "<cms:contentText key="CASH_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.cashReceived}"/>"
            },
			{
              "id": "14",
              "name": "<cms:contentText key="OTHER_QTY_RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="OTHER_QTY_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.otherQtyReceived}"/>"
            },
			{
              "id": "15",
              "name": "<cms:contentTemplateText key="OTHER_VALUE_RECEIVED" code="${report.cmAssetCode}" args="${currencyCode}"/>",
              "description": "<cms:contentText key="OTHER_VALUE_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=15&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.otherValueReceived}"/>"
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
              "url": "${systemUrl}/reports/displayNominationGivenByOrgReport.do?method=extractReport&exportViewType=fullView&extractType=primary"
            },
            {
              "label": "<cms:contentText key="EXTRACT_NOM_SUMMARY" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayNominationGivenByOrgReport.do?method=extractSecondaryReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayNominationGivenByOrgReport.do?method=extractReport&exportViewType=currentView&extractType=primary"
          },
      	  "inlineTextDescription": ""
        },
        "results": 
        [
         <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "${reportItem.orgName}",
      		<c:choose>
		        <c:when test="${ fn:contains(  reportItem.orgId ,reportParametersForm.parentNodeId ) }">
		        	false,
	          	</c:when>
	          	<c:otherwise>
			        <%
			        	NominationGivenByOrgReportValue reportValue = (NominationGivenByOrgReportValue)pageContext.getAttribute( "reportItem" );
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
					    paramMap.put( "nodeAndBelow", 1 );
					    String urlToUse = "/reports/displayNominationGivenByOrgReport.do?method=displaySummaryReport";
					    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					%>
	                "<%=drilldownUrl %>",
	          	</c:otherwise>
          	</c:choose>
            "<fmt:formatNumber value="${reportItem.eligibleNominatorsCnt}" />",
            "<fmt:formatNumber value="${reportItem.actualNominatorsCnt}" />",
            "<fmt:formatNumber value="${reportItem.eligbleNominatorsPct}" />",
            "<fmt:formatNumber value="${reportItem.eligibleNomineesCnt}" />",
            "<fmt:formatNumber value="${reportItem.actualNomineesCnt}" />",
            "<fmt:formatNumber value="${reportItem.eligibleNomineesPct}" />",
            "<fmt:formatNumber value="${reportItem.nominationsSubmitted}" />",
            "<fmt:formatNumber value="${reportItem.nominationsReceived}" />",
            "<fmt:formatNumber value="${reportItem.nominationsWonCnt}" />",
            "<fmt:formatNumber value="${reportItem.pointsReceived}" />",
            "<fmt:formatNumber value="${reportItem.cashReceived}" />",
            "<fmt:formatNumber value="${reportItem.otherQtyReceived}" />",
            "<fmt:formatNumber value="${reportItem.otherValueReceived}" />"
            
            <%--
            "<fmt:formatNumber value="${reportItem.sweepstakesWon}" />"
            --%>
          ]
          </c:forEach>
        ]
      }
    ],
