<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "managerUserId", reportParametersForm.getManagerUserId(  ) );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
String reportUrl = "/reports/displayChallengePointManagerReport.do?method=displayDetailReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="MANAGER_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="MANAGER_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": false,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
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
              "name": "<cms:contentText key="TOTAL_PAX_GOAL_SELECTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_PAX_GOAL_SELECTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPaxGoalSelected}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="TOTAL_PAX_ACHIEVING" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_PAX_ACHIEVING_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPaxAchieved}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="PERCENT_OF_SELECTED_PAX_ACHIEVING" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_OF_SELECTED_PAX_ACHIEVING_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.percentOfSelectedPaxAchieving}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="PERCENT_OF_TOTAL_PAX_ACHIEVING" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_OF_TOTAL_PAX_ACHIEVING_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.percentOfTotalPaxAchieving}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="PERCENT_MANAGER_OVERRIDE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_MANAGER_OVERRIDE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.percentOfManagerOveride}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="TOTAL_POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPoints}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="MANAGER_PAYOUT_PER_ACHIEVER" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="MANAGER_PAYOUT_PER_ACHIEVER_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.managerPayOutPerAchiever}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="TOTAL_MANAGER_POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_MANAGER_POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,           
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalManagerPoints}"/>"
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
              "url": "${systemUrl}/reports/displayChallengePointManagerReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayChallengePointManagerReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[				
				    "${reportItem.managerName}",
				    "${reportItem.promotionName}",		    	    		    						    								
				   	"<fmt:formatNumber value="${reportItem.totalPax}"/>",
				   	"<fmt:formatNumber value="${reportItem.totalPaxGoalSelected}"/>",
				   	"<fmt:formatNumber value="${reportItem.totalPaxAchieved}"/>",
				   	"<fmt:formatNumber value="${reportItem.percentOfSelectedPaxAchieving}"/>",
				   	"<fmt:formatNumber value="${reportItem.percentOfTotalPaxAchieving}"/>",				   	
				   	"<fmt:formatNumber value="${reportItem.percentOfManagerOveride}"/>",				   	
				   	"<fmt:formatNumber value="${reportItem.totalPoints}"/>",
				   	"<fmt:formatNumber value="${reportItem.managerPayOutPerAchiever}"/>",
				   	"<fmt:formatNumber value="${reportItem.totalManagerPoints}"/>"		   	
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>            
        ]
      }
    ],