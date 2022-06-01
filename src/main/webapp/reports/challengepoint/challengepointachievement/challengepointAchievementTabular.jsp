<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.challengepoint.ChallengePointAchievementReportValue"%>

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
              "name": "<cms:contentText key="CHALLENGE_POINT_DRILLDOWN_URL" code="${report.cmAssetCode}"/>",
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
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPaxCnt}"/>"
            },
            <c:if test="${displayLevel1}">
	            {
	              "id": "4",
	              "name": "<cms:contentText key="LEVEL_1_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL1_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level1SelectedCnt}"/>"
	            },
	            {
	              "id": "5",
	              "name": "<cms:contentText key="LEVEL_1_ACHIEVED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL1_ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level1AchievedCnt}"/>"
	            },
	            {
	              "id": "6",
	              "name": "<cms:contentText key="LEVEL_1_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level1AchievedPercent}"/>"
	            },
	            {
	              "id": "7",
	              "name": "<cms:contentText key="LEVEL_1_AWARDS" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL1_AWARDS_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level1AwardCnt}"/>"
	            },
            </c:if>
            <c:if test="${displayLevel2}">
	            {
	              "id": "8",
	              "name": "<cms:contentText key="LEVEL_2_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL2_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level2SelectedCnt}"/>"
	            },
	            {
	              "id": "9",
	              "name": "<cms:contentText key="LEVEL_2_ACHIEVED" code="${report.cmAssetCode}"/>",
	               "description": "<cms:contentText key="LEVEL2_ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level2AchievedCnt}"/>"
	            },
	            {
	              "id": "10",
	              "name": "<cms:contentText key="LEVEL_2_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level2AchievedPercent}"/>"
	            },
	            {
	              "id": "11",
	              "name": "<cms:contentText key="LEVEL_2_AWARDS" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL2_AWARDS_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level2AwardCnt}"/>"
	            },
            </c:if>
            <c:if test="${displayLevel3}">
	            {
	              "id": "12",
	              "name": "<cms:contentText key="LEVEL_3_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL3_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level3SelectedCnt}"/>"
	            },
	            {
	              "id": "13",
	              "name": "<cms:contentText key="LEVEL_3_ACHIEVED" code="${report.cmAssetCode}"/>",
	               "description": "<cms:contentText key="LEVEL3_ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level3AchievedCnt}"/>"
	            },
	            {
	              "id": "14",
	              "name": "<cms:contentText key="LEVEL_3_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level3AchievedPercent}"/>"
	            },
	            {
	              "id": "15",
	              "name": "<cms:contentText key="LEVEL_3_AWARDS" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL3_AWARDS_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level3AwardCnt}"/>"
	            },
            </c:if>
            <c:if test="${displayLevel4}">
	            {
	              "id": "16",
	              "name": "<cms:contentText key="LEVEL_4_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL4_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level4SelectedCnt}"/>"
	            },
	            {
	              "id": "17",
	              "name": "<cms:contentText key="LEVEL_4_ACHIEVED" code="${report.cmAssetCode}"/>",
	               "description": "<cms:contentText key="LEVEL4_ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level4AchievedCnt}"/>"
	            },
	            {
	              "id": "18",
	              "name": "<cms:contentText key="LEVEL_4_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level4AchievedPercent}"/>"
	            },
	            {
	              "id": "19",
	              "name": "<cms:contentText key="LEVEL_4_AWARDS" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL4_AWARDS_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level4AwardCnt}"/>"
	            },
            </c:if>
            <c:if test="${displayLevel5}">
	            {
	              "id": "20",
	              "name": "<cms:contentText key="LEVEL_5_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL5_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level5SelectedCnt}"/>"
	            },
	            {
	              "id": "21",
	              "name": "<cms:contentText key="LEVEL_5_ACHIEVED" code="${report.cmAssetCode}"/>",
	               "description": "<cms:contentText key="LEVEL5_ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level5AchievedCnt}"/>"
	            },
	            {
	              "id": "22",
	              "name": "<cms:contentText key="LEVEL_5_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level5AchievedPercent}"/>"
	            },
	            {
	              "id": "23",
	              "name": "<cms:contentText key="LEVEL_5_AWARDS" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL5_AWARDS_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level5AwardCnt}"/>"
	            },
            </c:if>
            <c:if test="${displayLevel6}">
	            {
	              "id": "24",
	              "name": "<cms:contentText key="LEVEL_6_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL6_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level6SelectedCnt}"/>"
	            },
	            {
	              "id": "25",
	              "name": "<cms:contentText key="LEVEL_6_ACHIEVED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL6_ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level6AchievedCnt}"/>"
	            },
	            {
	              "id": "26",
	              "name": "<cms:contentText key="LEVEL_6_ACHIEVED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level6AchievedPercent}"/>"
	            },
	            {
	              "id": "27",
	              "name": "<cms:contentText key="LEVEL_6_AWARDS" code="${report.cmAssetCode}"/>",
	             "description": "<cms:contentText key="LEVEL6_AWARDS_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level6AwardCnt}"/>"
	            },
            </c:if>
            {
              "id": "28",
              "name": "<cms:contentText key="BASE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="BASE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
              "summary": "<fmt:formatNumber value="${totalsRowData.baseQuantity}"/>"
            },
            {
              "id": "29",
              "name": "<cms:contentText key="GOAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="GOAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
              "summary": "<fmt:formatNumber value="${totalsRowData.amountToAchieve}"/>"
            },
            {
              "id": "30",
              "name": "<cms:contentText key="ACTUAL_RESULTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_RESULTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
              "summary": "<fmt:formatNumber value="${totalsRowData.currentValue}"/>"
            },
            {
              "id": "31",
              "name": "<cms:contentText key="PERCENT_OF_GOAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_OF_GOAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
              "summary": "<fmt:formatNumber value="${totalsRowData.percentAchieved}"/>"
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
              "url": "${systemUrl}/reports/displayChallengepointAchievementReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayChallengepointAchievementReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[
				    "${reportItem.orgName}",
				    <c:choose>
				        <c:when test="${ fn:contains(  reportItem.orgId ,reportParametersForm.parentNodeId ) }">
				    		<%
				    			ChallengePointAchievementReportValue reportValue = (ChallengePointAchievementReportValue)pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
							    paramMap.put( "nodeAndBelow", "false" );
							    String urlToUse = "/reports/displayChallengepointAchievementReport.do?method=displayDetailReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
			                "<%=drilldownUrl %>",
				    	</c:when>
				    	<c:otherwise>
				    		<%
				    			ChallengePointAchievementReportValue reportValue = (ChallengePointAchievementReportValue)pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
							    paramMap.put( "nodeAndBelow", "true" );
							    String urlToUse = "/reports/displayChallengepointAchievementReport.do?method=displaySummaryReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
			                "<%=drilldownUrl %>",
				    	</c:otherwise>
				    </c:choose>
				   	"<fmt:formatNumber value="${reportItem.totalPaxCnt}"/>",
					 <c:if test="${displayLevel1}">
					 	"<fmt:formatNumber value="${reportItem.level1SelectedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level1AchievedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level1AchievedPercent}"/>",
						"<fmt:formatNumber value="${reportItem.level1AwardCnt}"/>",
					</c:if>
					<c:if test="${displayLevel2}">
						"<fmt:formatNumber value="${reportItem.level2SelectedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level2AchievedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level2AchievedPercent}"/>",
						"<fmt:formatNumber value="${reportItem.level2AwardCnt}"/>",
					</c:if>
					<c:if test="${displayLevel3}">
						"<fmt:formatNumber value="${reportItem.level3SelectedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level3AchievedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level3AchievedPercent}"/>",
						"<fmt:formatNumber value="${reportItem.level3AwardCnt}"/>",
					</c:if>
					<c:if test="${displayLevel4}">
						"<fmt:formatNumber value="${reportItem.level4SelectedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level4AchievedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level4AchievedPercent}"/>",
						"<fmt:formatNumber value="${reportItem.level4AwardCnt}"/>",
					</c:if>
					<c:if test="${displayLevel5}">
						"<fmt:formatNumber value="${reportItem.level5SelectedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level5AchievedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level5AchievedPercent}"/>",
						"<fmt:formatNumber value="${reportItem.level5AwardCnt}"/>",
					</c:if>
					<c:if test="${displayLevel6}">
						"<fmt:formatNumber value="${reportItem.level6SelectedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level6AchievedCnt}"/>",
						"<fmt:formatNumber value="${reportItem.level6AchievedPercent}"/>",
						"<fmt:formatNumber value="${reportItem.level6AwardCnt}"/>",
					</c:if>
					"<fmt:formatNumber value="${reportItem.baseQuantity}"/>",
					"<fmt:formatNumber value="${reportItem.amountToAchieve}"/>",
					"<fmt:formatNumber value="${reportItem.currentValue}"/>",
					"<fmt:formatNumber value="${reportItem.percentAchieved}"/>"
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>
        ]
      }
    ],