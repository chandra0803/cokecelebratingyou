<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.goalquest.GoalQuestProgressReportValue"%>

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
              "name": "Goal Quest Progress Drilldown URL",
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
              "name": "<cms:contentText key="TOTAL_PAX" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_PAX_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPax}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="TOTAL_PAX_NO_GOAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_PAX_NO_GOAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPaxNoGoal}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="TOTAL_PAX_GOAL_SELECTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_PAX_GOAL_SELECTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPaxGoalSelected}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="LEVEL_0_25" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.level0To25Count}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="LEVEL_26_50" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.level26To50Count}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="LEVEL_51_75" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.level51To75Count}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="LEVEL_76_99" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.level76To99Count}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="LEVEL_100" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.level100Count}"/>"
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
              "url": "${systemUrl}/reports/displayGoalQuestProgressReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayGoalQuestProgressReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[				
				    "${reportItem.orgName}",
				    <c:choose>
				    	<c:when test="${ fn:contains( reportParametersForm.parentNodeId, reportItem.parentNodeId ) }">
				    		<%
				    			GoalQuestProgressReportValue reportValue = (GoalQuestProgressReportValue)pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getParentNodeId() );
							    paramMap.put( "nodeAndBelow", "false" );
							    String urlToUse = "/reports/displayGoalQuestProgressReport.do?method=displayDetailReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
			                "<%=drilldownUrl %>",
				    	</c:when>
				    	<c:otherwise>
				    		<%
				    			GoalQuestProgressReportValue reportValue = (GoalQuestProgressReportValue)pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getParentNodeId() );
							    paramMap.put( "nodeAndBelow", "true" );
							    String urlToUse = "/reports/displayGoalQuestProgressReport.do?method=displaySummaryReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
			                "<%=drilldownUrl %>",
				    	</c:otherwise>
				    </c:choose>			    		    						    								
				   	"<fmt:formatNumber value="${reportItem.totalPax}"/>",
				   	"<fmt:formatNumber value="${reportItem.totalPaxNoGoal}"/>",
					"<fmt:formatNumber value="${reportItem.totalPaxGoalSelected}"/>",
					"<fmt:formatNumber value="${reportItem.level0To25Count}"/>",
					"<fmt:formatNumber value="${reportItem.level26To50Count}"/>",
					"<fmt:formatNumber value="${reportItem.level51To75Count}"/>",
					"<fmt:formatNumber value="${reportItem.level76To99Count}"/>",
					"<fmt:formatNumber value="${reportItem.level100Count}"/>"
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>            
        ]
      }
    ],