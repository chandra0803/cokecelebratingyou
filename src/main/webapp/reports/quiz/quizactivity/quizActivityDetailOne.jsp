<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.quizactivityreport.QuizActivityDetailOneReportValue"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayQuizActivityReport.do?method=displayQuizActivityDetailOneReport";
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
          "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="ELIGIBLE_QUIZZES" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ELIGIBLE_QUIZZES_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligibleQuizzesCnt}" />"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="QUIZ_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsCnt}" />"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="ATTEMPTS_IN_PROGRESS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ATTEMPTS_IN_PROGRESS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsInProgressCnt}" />"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="ATTEMPTS_FAILED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ATTEMPTS_FAILED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.attemptsFailedCnt}" />"
        },
        {
          "id": "8",
          "name": "<cms:contentText key="ATTEMPTS_PASSED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ATTEMPTS_PASSED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.attemptsPassedCnt}" />"
        }
        <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
        ,
        {
          "id": "9",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.pointsCnt}" />"
        }
        </c:if>
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
          "url": "${systemUrl}/reports/displayQuizActivityReport.do?method=extractReport"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayQuizActivityReport.do?method=extractReport"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [          	
        "${reportDataId.paxName}",
        <%
			QuizActivityDetailOneReportValue reportValue = (QuizActivityDetailOneReportValue) pageContext.getAttribute( "reportDataId" );
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put( "paxId", reportValue.getParticipantId(  ) );
		    String urlToUse = "/reports/displayQuizActivityReport.do?method=displayQuizActivityDetailTwoReport";
		    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
		%>
		"<%=drilldownUrl %>",
		"<c:out value="${reportDataId.country}" />",
        "<fmt:formatNumber value="${reportDataId.eligibleQuizzesCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quizAttemptsCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quizAttemptsInProgressCnt}" />",
        "<fmt:formatNumber value="${reportDataId.attemptsFailedCnt}" />",
        "<fmt:formatNumber value="${reportDataId.attemptsPassedCnt}" />"
        <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
        ,
        "<fmt:formatNumber value="${reportDataId.pointsCnt}" />"
        </c:if>
      ]
      </c:forEach>
    ]
  }
],