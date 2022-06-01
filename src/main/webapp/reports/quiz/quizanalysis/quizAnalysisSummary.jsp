<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.quizanalysisreport.QuizAnalysisSummaryReportValue"%>
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
          "name": "<cms:contentText key="QUIZ_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_NAME_DESC" code="${report.cmAssetCode}"/>",
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
        },
        {
          "id": "3",
          "name": "<cms:contentText key="QUIZ_TYPE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_TYPE_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="QUESTIONS_IN_POOL" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUESTIONS_IN_POOL_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.questionsInPoolCnt}" />"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="QUESTIONS_TOASK_PER_ATTEMPT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUESTIONS_TOASK_PER_ATTEMPT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quesToAskPerAttemptCnt}" />"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="REQUIRED_TO_PASS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="REQUIRED_TO_PASS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.reqToPassCnt}" />"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="QUIZ_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsCnt}" />"
        },
        {
          "id": "8",
          "name": "<cms:contentText key="QUIZ_ATTEMPTS_PCT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_PCT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsPct}" />"
        },
        {
          "id": "9",
          "name": "<cms:contentText key="PASSED_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PASSED_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=9&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.passedAttemptsCnt}" />"
        },
        {
          "id": "10",
          "name": "<cms:contentText key="PASSED_ATTEMPTS_PCT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PASSED_ATTEMPTS_PCT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=10&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.passedAttemptsPct}" />"
        },
        {
          "id": "11",
          "name": "<cms:contentText key="FAILED_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="FAILED_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=11&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.failedAttemptsCnt}" />"
        },
        {
          "id": "12",
          "name": "<cms:contentText key="FAILED_ATTEMPTS_PCT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="FAILED_ATTEMPTS_PCT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=12&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.failedAttemptsPct}" />"
        },
        {
          "id": "13",
          "name": "<cms:contentText key="INCOMPLETE_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="INCOMPLETE_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=13&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.incompleteAttemptsCnt}" />"
        },
        {
          "id": "14",
          "name": "<cms:contentText key="INCOMPLETE_ATTEMPTS_PCT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="INCOMPLETE_ATTEMPTS_PCT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=14&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.incompleteAttemptsPct}" />"
        },
        {
          "id": "15",
          "name": "<cms:contentText key="MAX_ATTEMPTS_ALLOWED_FOR_PAX" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="MAX_ATTEMPTS_ALLOWED_FOR_PAX_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=15&sortedBy=",
          "summary": "${totalsRowData.maxAttemptsAllowedPerPaxCnt}"
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
          "url": "${systemUrl}/reports/displayQuizAnalysisReport.do?method=extractReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayQuizAnalysisReport.do?method=extractReport&exportViewType=currentView"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [          	
        "${reportDataId.quizName}",
	    <%
			QuizAnalysisSummaryReportValue reportValue = (QuizAnalysisSummaryReportValue) pageContext.getAttribute( "reportDataId" );
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put( "quizId", reportValue.getQuizId(  ) );
		    String urlToUse = "/reports/displayQuizAnalysisReport.do?method=displayQuizAnalysisDetailOneReport";
		    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
		%>
		"<%=drilldownUrl %>",
        "<c:out value="${reportDataId.quizType}" />",
        "<fmt:formatNumber value="${reportDataId.questionsInPoolCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quesToAskPerAttemptCnt}" />",
        "<fmt:formatNumber value="${reportDataId.reqToPassCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quizAttemptsCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quizAttemptsPct}" />",
        "<fmt:formatNumber value="${reportDataId.passedAttemptsCnt}" />",
        "<fmt:formatNumber value="${reportDataId.passedAttemptsPct}" />",
        "<fmt:formatNumber value="${reportDataId.failedAttemptsCnt}" />",
        "<fmt:formatNumber value="${reportDataId.failedAttemptsPct}" />",
        "<fmt:formatNumber value="${reportDataId.incompleteAttemptsCnt}" />",
        "<fmt:formatNumber value="${reportDataId.incompleteAttemptsPct}" />",
        "<c:out value="${reportDataId.maxAttemptsAllowedPerPaxCnt}" />"
      ]
      </c:forEach>
    ]
  }
],