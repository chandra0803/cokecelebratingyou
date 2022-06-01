<%@ include file="/include/taglib.jspf" %>
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
sortMap.put( "drilldownPromoId", reportParametersForm.getPromotionId(  ) );
String reportUrl = "/reports/displayIndividualActivityReport.do?method=displayQuizDetailReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="QUIZ" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUIZ_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "2",
          "name": "<cms:contentText key="DATE_COMPLETED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="DATE_COMPLETED_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",                           
          "summary": "",
          "target": "_sheet"
        },
        {
          "id": "3",
          "name": "<cms:contentText key="QUIZ_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "URL",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "",
          "target":"_sheet"
        },      
        {
          "id": "4",
          "name": "<cms:contentText key="RESULT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="RESULT_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",                           
          "summary": ""
        }, 
        {
          "id": "5",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": ""
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
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=quizDetail"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=quizDetail"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
          "${reportItem.quizName}",
          "<fmt:formatDate value="${reportItem.quizDate}" pattern="${JstlDatePattern}" />",
          <c:choose>
          <c:when test="${reportItem.claimId != 0}">
          <%
          	IndividualActivityReportValue reportValue = (IndividualActivityReportValue)pageContext.getAttribute( "reportItem" );
          	Report report = (Report)request.getAttribute( "report" );
          	Map<String, Object> urlMap = new HashMap<String, Object>();
     		String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), report.getUrl( ), urlMap );
		      
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put( "id", reportValue.getClaimId(  ));
		    paramMap.put( "submitterId", reportValue.getSubmitterId(  ));
		    paramMap.put( "isFullPage", "false");
		    paramMap.put( "returnURL", returnURL );
		    String urlToUse = "claim/quizDetail.do?method=showQuizDetailG5&callingScreen=";
		    String detailUrl = ClientStateUtils.generateEncodedLink( "", urlToUse, paramMap );
		  %>
		  "${systemUrl}/<%=detailUrl%>",
		  </c:when>
		  <c:otherwise>
		  "",
		  </c:otherwise>
		  </c:choose>
          "<c:out value="${reportItem.quizResult}" />",
          "<fmt:formatNumber value="${reportItem.awardsGiven}" />",
          "<fmt:formatNumber value="${reportItem.sweepstakesWon}" />"
       	]
	</c:forEach>
    ]
  }
],