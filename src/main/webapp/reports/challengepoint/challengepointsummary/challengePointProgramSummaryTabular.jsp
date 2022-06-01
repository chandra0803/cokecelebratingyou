<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.value.challengepoint.ChallengePointSummaryReportValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
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

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "",
              "description": "",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "3",
              "name": "<cms:contentText key="NO_OF_PARTICIPANTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NO_OF_PARTICIPANTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.achieved}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="PERCENT_OF_PARICIPANTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_OF_PARICIPANTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.percentAchieved}"/>"
            },
            
            {
              "id": "5",
              "name": "<cms:contentText key="TOTAL_BASELINE_OBJECTIVE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_BASELINE_OBJECTIVE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalBaseLineObjective}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="TOTAL_CHALLENGEPOINT_PRODUCTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_CHALLENGEPOINT_PRODUCTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalChallengeProduction}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="TOTAL_ACTUAL_PRODUCTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_ACTUAL_PRODUCTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalActualProduction}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="PERCENTAGE_INCREASE_OVER_BASELINE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENTAGE_INCREASE_OVER_BASELINE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.percIncreaseBaseline}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="UNIT_DOLLAR_INCR_OVER_BASELINE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="UNIT_DOLLAR_INCR_OVER_BASELINE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.dolIncBaseline}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="PERCENT_INCREASE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_INCREASE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.percentIncreseChallengepoint}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="UNIT_DOLLAR_INCR_OVER_CP" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="UNIT_DOLLAR_INCR_OVER_CP_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.unitDolIncCP}"/>"
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
              "url": "${systemUrl}/reports/displayChallengePointProgramSummaryReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayChallengePointProgramSummaryReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[	
				"${reportItem.goals}",			
				    
				    <%--
				    ChallengePointSummaryReportValue reportValue = (ChallengePointSummaryReportValue)pageContext.getAttribute( "reportItem" );
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
					    String urlToUse = "/reports/displayChallengePointProgramSummaryReport.do?method=displaySummaryReport";
					    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					%>
	                "<%=drilldownUrl %>", --%>
	                
	                
	                "<fmt:formatNumber value="${reportItem.achieved}"/>",
	                "<fmt:formatNumber value="${reportItem.percentAchieved}"/>",		    	    		    						    								
				   	"<fmt:formatNumber value="${reportItem.totalBaseLineObjective}"/>",
					"<fmt:formatNumber value="${reportItem.totalChallengeProduction}"/>",
					"<fmt:formatNumber value="${reportItem.totalActualProduction}"/>",
					"<fmt:formatNumber value="${reportItem.percIncreaseBaseline}"/>",
					"<fmt:formatNumber value="${reportItem.dolIncBaseline}"/>",
					"<fmt:formatNumber value="${reportItem.percentIncreseChallengepoint}"/>",
					"<fmt:formatNumber value="${reportItem.unitDolIncCP}"/>"
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>            
        ]
      }
    ],