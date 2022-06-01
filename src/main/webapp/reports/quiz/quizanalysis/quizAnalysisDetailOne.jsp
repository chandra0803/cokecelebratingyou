<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.quizanalysisreport.QuizAnalysisDetailOneReportValue"%>



<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "quizId", reportParametersForm.getQuizId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayQuizAnalysisReport.do?method=displayQuizAnalysisDetailOneReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="QUESTIONS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUESTIONS_DESC" code="${report.cmAssetCode}"/>",
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
          "name": "<cms:contentText key="NUMBER_OF_TIMES_ASKED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="NUMBER_OF_TIMES_ASKED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.nbrOfTimesAsked}" />"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="NUMBER_OF_CORRECT_RESPONSES" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="NUMBER_OF_CORRECT_RESPONSES_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.nbrCorrectResponses}" />"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="PERCENT_OF_CORRECT_RESPONSES" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PERCENT_OF_CORRECT_RESPONSES_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.correctResponsesPct}" />"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="NUMBER_OF_INCORRECT_RESPONSES" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="NUMBER_OF_INCORRECT_RESPONSES_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.nbrIncorrectResponses}" />"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="PERCENT_OF_INCORRECT_RESPONSES" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PERCENT_OF_INCORRECT_RESPONSES_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.incorrectResponsesPct}" />"
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
        "${reportDataId.question}",
	    <%
			QuizAnalysisDetailOneReportValue reportValue = (QuizAnalysisDetailOneReportValue) pageContext.getAttribute( "reportDataId" );
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put( "qqId", reportValue.getQqId(  ) );
		    String urlToUse = "/reports/displayQuizAnalysisReport.do?method=displayQuizAnalysisDetailTwoReport";
		    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
		%>
		"<%=drilldownUrl %>",
        "<c:out value="${reportDataId.nbrOfTimesAsked}" />",
        "<c:out value="${reportDataId.nbrCorrectResponses}" />",
        "<c:out value="${reportDataId.correctResponsesPct}" />",
        "<c:out value="${reportDataId.nbrIncorrectResponses}" />",
        "<c:out value="${reportDataId.incorrectResponsesPct}" />"
      ]
      </c:forEach>
    ]
  }
],