<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.throwdown.ThrowdownActivityByPaxReportValue" %>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = report.getUrl(  );
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="PARTICIPANT_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PARTICIPANT_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
        },
        {
          "id": "2",
          "name": "Participant drill-down URL",
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
          "name": "<cms:contentText key="LOGIN_ID" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="LOGIN_ID_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="PARTICIPANT_STATUS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PARTICIPANT_STATUS_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="PRIMARY_ORG_UNIT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PRIMARY_ORG_UNIT_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": ""
        },
        {
          "id": "7",
          "name": "<cms:contentText key="JOB_POSITION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="JOB_POSITION_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": ""
        },
        {
          "id": "8",
          "name": "<cms:contentText key="DEPARTMENT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="DEPARTMENT_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": ""
        },
        {
          "id": "9",
          "name": "<cms:contentText key="PROMOTION_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMOTION_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
          "summary": ""
        },
        {
          "id": "10",
          "name": "<cms:contentText key="WIN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="WIN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.winsCnt}"/>"
        },
        {
          "id": "11",
          "name": "<cms:contentText key="LOSS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="LOSS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.lossCnt}"/>"
        },
        {
          "id": "12",
          "name": "<cms:contentText key="TIE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="TIE_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.tiesCnt}"/>"
        },
        {
          "id": "13",
          "name": "<cms:contentText key="ACTUAL_RESULTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ACTUAL_RESULTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.activityCnt}"/>"
        },
        {
          "id": "14",
          "name": "<cms:contentText key="RANKING" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="RANKING_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
          "summary": ""
        },
        {
          "id": "15",
          "name": "<cms:contentText key="POINTS_EARNED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_EARNED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=15&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
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
          "url": "${systemUrl}/reports/displayThrowdownActivityByPaxSummaryReport.do?method=extractReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayThrowdownActivityByPaxSummaryReport.do?method=extractReport&exportViewType=currentView"
      },
      "inlineTextDescription": ""
    },
    "results": [
    	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
   			<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		[		          	
	          "${reportItem.participantName}",
      			<%
      				ThrowdownActivityByPaxReportValue reportValue = (ThrowdownActivityByPaxReportValue) pageContext.getAttribute( "reportItem" );
				    Map<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put( "userId", reportValue.getUserId( ) );
				    String urlToUse = "/reports/displayThrowdownActivityByPaxSummaryReport.do?method=displayDetailReport";
				    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
				%>
	          "<%=drilldownUrl %>",
	          "<c:out value="${reportItem.loginId}" />",
	          "<c:out value="${reportItem.country}" />",
	          "<c:out value="${reportItem.participantStatus}" />",
	          "<c:out value="${reportItem.orgName}" />",
	          "<c:out value="${reportItem.jobPosition}" />",
	          "<c:out value="${reportItem.department}" />",
	          "<c:out value="${reportItem.promotionName}" />",
	          "<fmt:formatNumber value="${reportItem.winsCnt}" />",
	          "<fmt:formatNumber value="${reportItem.lossCnt}" />",
	          "<fmt:formatNumber value="${reportItem.tiesCnt}" />",
	          "<fmt:formatNumber value="${reportItem.activityCnt}" />",
	          "<c:out value="${reportItem.rank}" />",
	          "<fmt:formatNumber value="${reportItem.points}" />"	            
			]
      </c:forEach>
    ]
  }
],