<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.quizactivityreport.QuizActivitySummaryReportValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

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
          "name": "<cms:contentText key="ELIGIBLE_QUIZZES" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ELIGIBLE_QUIZZES_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligibleQuizzesCnt}" />"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="QUIZ_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsCnt}" />"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="ATTEMPTS_IN_PROGRESS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ATTEMPTS_IN_PROGRESS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsInProgressCnt}" />"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="ATTEMPTS_FAILED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ATTEMPTS_FAILED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.attemptsFailedCnt}" />"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="ATTEMPTS_PASSED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ATTEMPTS_PASSED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.attemptsPassedCnt}" />"
        },
        {
          "id": "8",
          "name": "<cms:contentText key="PERCENT_ELIGIBLE_PASSED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PERCENT_ELIGIBLE_PASSED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligiblePassedPct}" />"
        },
        {
          "id": "9",
          "name": "<cms:contentText key="PERCENT_ELIGIBLE_FAILED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PERCENT_ELIGIBLE_FAILED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=9&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligibleFailedPct}" />"
        }
        <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
        ,
        {
          "id": "10",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "${report.url}&sortedOn=10&sortedBy=",
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
          "url": "${systemUrl}/reports/displayQuizActivityReport.do?method=extractReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayQuizActivityReport.do?method=extractReport&exportViewType=currentView"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [          	
        "${reportDataId.orgName}",
        <c:choose>
			<c:when test="${ reportDataId.isTeam == 1 }">
				<c:choose>
					<c:when test="${ reportDataId.quizAttemptsCnt > 0 || reportDataId.pointsCnt > 0 }">
						<%
							QuizActivitySummaryReportValue reportValue = (QuizActivitySummaryReportValue) pageContext.getAttribute( "reportDataId" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "userId", reportValue.getNodeId(  ) );
						    paramMap.put( "nodeAndBelow", 0 );
						    String urlToUse = "/reports/displayQuizActivityReport.do?method=displayQuizActivityDetailOneReport";
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
					QuizActivitySummaryReportValue reportValue = (QuizActivitySummaryReportValue) pageContext.getAttribute( "reportDataId" );
				    Map<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
				    paramMap.put( "nodeAndBelow", 1 );
				    String urlToUse = "/reports/displayQuizActivityReport.do?method=displaySummaryReport";
				    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
				%>
				"<%=drilldownUrl %>",
			</c:otherwise>
	  	</c:choose>
        "<fmt:formatNumber value="${reportDataId.eligibleQuizzesCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quizAttemptsCnt}" />",
        "<fmt:formatNumber value="${reportDataId.quizAttemptsInProgressCnt}" />",
        "<fmt:formatNumber value="${reportDataId.attemptsFailedCnt}" />",
        "<fmt:formatNumber value="${reportDataId.attemptsPassedCnt}" />",
        "<fmt:formatNumber value="${reportDataId.eligiblePassedPct}" />",
        "<fmt:formatNumber value="${reportDataId.eligibleFailedPct}" />"
        <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
        ,
        "<fmt:formatNumber value="${reportDataId.pointsCnt}" />"
        </c:if>
      ]
      </c:forEach>
    ]
  }
],