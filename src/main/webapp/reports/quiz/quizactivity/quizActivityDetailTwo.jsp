<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.quizactivityreport.QuizActivityDetailTwoReportValue"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "participantId", reportParametersForm.getPaxId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayQuizActivityReport.do?method=displayQuizActivityDetailTwoReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="DATE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="DATE_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
        },
        {
          "id": "2",
          "name": "<cms:contentText key="QUIZ_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "URL",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "",
          "target": "_sheet"
        },
        {
          "id": "3",
          "name": "<cms:contentText key="COMPLETED_BY" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="COMPLETED_BY_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMOTION_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="SCORE_PASSING" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SCORE_PASSING_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": ""
        },
        {
          "id": "7",
          "name": "<cms:contentText key="RESULTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="RESULTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": ""
        }
        <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
        ,
        {
          "id": "8",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.pointsCnt}" />"
        }
        </c:if>
        ,
        <%--
        {
          "id": "9",
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
          "summary": "${totalsRowData.sweepstakesWonCnt}"
        },
        --%>
        {
          "id": "10",
          "name": "<cms:contentText key="CERTIFICATE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="CERTIFICATE_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.certificate}" />"
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
          "url": "${systemUrl}/reports/displayQuizActivityReport.do?method=extractReport&exportLevel=paxLevel"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayQuizActivityReport.do?method=extractReport&exportLevel=paxLevel"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [          	
        "<fmt:formatDate value="${reportDataId.quizDate}" pattern="${JstlDatePattern}" />",
        <%
        	QuizActivityDetailTwoReportValue reportValue = (QuizActivityDetailTwoReportValue)pageContext.getAttribute( "reportDataId" );
           	Map<String, Object> urlMap = new HashMap<String, Object>();
      		String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), report.getUrl( ), urlMap );
		      
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put( "id", reportValue.getClaimId(  ));
		    paramMap.put( "submitterId", reportValue.getParticipantId(  ));
		    paramMap.put( "isFullPage", "false");
		    paramMap.put( "returnURL", returnURL );
		    String urlToUse = "claim/quizDetail.do?method=showQuizDetailG5&callingScreen=";
		    String detailUrl = ClientStateUtils.generateEncodedLink( "", urlToUse, paramMap );
		%>
		"${systemUrl}/<%=detailUrl%>",
        "<c:out value="${reportDataId.completedByName}" />",
        "<c:out value="${reportDataId.orgName}" />",
        "<c:out value="${reportDataId.promotionName}" />",
        "<c:out value="${reportDataId.scorePassing}" />",
        "<c:out value="${reportDataId.quizResult}" />"
        <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
        ,
        "<fmt:formatNumber value="${reportDataId.pointsCnt}" />"
        </c:if>
        ,<%-- "<c:out value="${reportDataId.sweepstakesWonCnt}" />", --%>
        "<fmt:formatNumber value="${reportDataId.certificate}" />"
      ]
      </c:forEach>
    ]
  }
],