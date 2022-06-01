<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.individualactivity.IndividualActivityReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "displayType", "quizzes" );
String reportUrl = "/reports/displayIndividualActivityReport.do?method=displayDrilldownReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
        },
        {
          "id": "2",
          "name": "Individual Activity - Quiz Drilldown URL",
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
          "name": "<cms:contentText key="QUIZ_ATTEMPTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttempts}"/>"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="QUIZ_ATTEMPTS_FAILED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_FAILED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsFailed}"/>"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="QUIZ_ATTEMPTS_PASSED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_ATTEMPTS_PASSED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.quizAttemptsPassed}"/>"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="AWARDS_GIVEN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="AWARDS_GIVEN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.awardsGiven}"/>"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWon}"/>"
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
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=quizSummary"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=quizSummary"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
          "${reportItem.promoName}",
          <%
    			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
			    Map<String, Object> paramMap = new HashMap<String, Object>();
			    paramMap.put( "drilldownPromoId", reportValue.getPromoId(  ) );
			    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayQuizDetailReport";
			    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
			%>
           "<%=drilldownUrl %>",
          "<fmt:formatNumber value="${reportItem.quizAttempts}" />",
          "<fmt:formatNumber value="${reportItem.quizAttemptsFailed}" />",
          "<fmt:formatNumber value="${reportItem.quizAttemptsPassed}" />",
          "<fmt:formatNumber value="${reportItem.awardsGiven}" />",
          "<fmt:formatNumber value="${reportItem.sweepstakesWon}" />"
          
       	]
	</c:forEach>
    ]
  }
],