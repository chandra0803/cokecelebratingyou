<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.goalquest.GoalQuestSelectionReportValue"%>

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
              "name": "Goal Quest Selection Drilldown URL",
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
              "summary": "<fmt:formatNumber value="${totalsRowData.paxCnt}"/>"
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
              "summary": "<fmt:formatNumber value="${totalsRowData.noGoalSelected}"/>"
            }
            <c:if test="${displayLevel1}">
	            ,
	            {
	              "id": "5",
	              "name": "<cms:contentText key="LEVEL_1_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL1_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level1Selected}"/>"
	            },
	            {
	              "id": "6",
	              "name": "<cms:contentText key="LEVEL_1_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL1_SELECTED_PERCENT_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level1SelectedPercent}"/>"
	            }
            </c:if>
            <c:if test="${displayLevel2}">
	            ,
	            {
	              "id": "7",
	              "name": "<cms:contentText key="LEVEL_2_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL2_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level2Selected}"/>"
	            },
	            {
	              "id": "8",
	              "name": "<cms:contentText key="LEVEL_2_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL2_SELECTED_PERCENT_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level2SelectedPercent}"/>"
	            }
            </c:if>
            <c:if test="${displayLevel3}">
            	,
	            {
	              "id": "9",
	              "name": "<cms:contentText key="LEVEL_3_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL3_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level3Selected}"/>"
	            },
	            {
	              "id": "10",
	              "name": "<cms:contentText key="LEVEL_3_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL3_SELECTED_PERCENT_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level3SelectedPercent}"/>"
	            }
            </c:if>
            <c:if test="${displayLevel4}">
            	,
	            {
	              "id": "11",
	              "name": "<cms:contentText key="LEVEL_4_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL4_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level4Selected}"/>"
	            },
	            {
	              "id": "12",
	              "name": "<cms:contentText key="LEVEL_4_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL4_SELECTED_PERCENT_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level4SelectedPercent}"/>"
	            }
            </c:if>
            <c:if test="${displayLevel5}">
            	,
	            {
	              "id": "13",
	              "name": "<cms:contentText key="LEVEL_5_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL5_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level5Selected}"/>"
	            },
	            {
	              "id": "14",
	              "name": "<cms:contentText key="LEVEL_5_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL5_SELECTED_PERCENT_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level5SelectedPercent}"/>"
	            }
            </c:if>
            <c:if test="${displayLevel6}">
            	,
	            {
	              "id": "15",
	              "name": "<cms:contentText key="LEVEL_6_SELECTED" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL6_SELECTED_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=15&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level6Selected}"/>"
	            },
	            {
	              "id": "16",
	              "name": "<cms:contentText key="LEVEL_6_SELECTED_PERCENT" code="${report.cmAssetCode}"/>",
	              "description": "<cms:contentText key="LEVEL6_SELECTED_PERCENT_DESC" code="${report.cmAssetCode}"/>",
	              "type": "number",
	              "alignment": "",
	              "nameId": "",
	              "sortable": true,
	              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=16&sortedBy=",
	              "summary": "<fmt:formatNumber value="${totalsRowData.level6SelectedPercent}"/>"
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
              "url": "${systemUrl}/reports/displayGoalQuestSelectionReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayGoalQuestSelectionReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[				
				    "${reportItem.orgName}",
				    <c:choose>
				    	<c:when test="${ fn:contains( reportParametersForm.parentNodeId, reportItem.orgId ) }">
				    		<%
				    			GoalQuestSelectionReportValue reportValue = (GoalQuestSelectionReportValue)pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
							    paramMap.put( "nodeAndBelow", "false" );
							    String urlToUse = "/reports/displayGoalQuestSelectionReport.do?method=displayDetailReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
			                "<%=drilldownUrl %>",
				    	</c:when>
				    	<c:otherwise>
				    		<%
				    			GoalQuestSelectionReportValue reportValue = (GoalQuestSelectionReportValue)pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
							    paramMap.put( "nodeAndBelow", "true" );
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), reportUrl, paramMap );
							%>
			                "<%=drilldownUrl %>",
				    	</c:otherwise>
				    </c:choose>			    	    		    						    								
				   	"<fmt:formatNumber value="${reportItem.paxCnt}"/>",
				   	"<fmt:formatNumber value="${reportItem.noGoalSelected}"/>"
				   	<c:if test="${displayLevel1}">
				   		,
						"<fmt:formatNumber value="${reportItem.level1Selected}"/>",
						"<fmt:formatNumber value="${reportItem.level1SelectedPercent}"/>"
					</c:if>
					<c:if test="${displayLevel2}">
						,
						"<fmt:formatNumber value="${reportItem.level2Selected}"/>",
						"<fmt:formatNumber value="${reportItem.level2SelectedPercent}"/>"
					</c:if>
					<c:if test="${displayLevel3}">
						,
						"<fmt:formatNumber value="${reportItem.level3Selected}"/>",
						"<fmt:formatNumber value="${reportItem.level3SelectedPercent}"/>"
					</c:if>
					<c:if test="${displayLevel4}">
						,
						"<fmt:formatNumber value="${reportItem.level4Selected}"/>",
						"<fmt:formatNumber value="${reportItem.level4SelectedPercent}"/>"
					</c:if>
					<c:if test="${displayLevel5}">
						,
						"<fmt:formatNumber value="${reportItem.level5Selected}"/>",
						"<fmt:formatNumber value="${reportItem.level5SelectedPercent}"/>"
					</c:if>
					<c:if test="${displayLevel6}">
						,
						"<fmt:formatNumber value="${reportItem.level6Selected}"/>",
						"<fmt:formatNumber value="${reportItem.level6SelectedPercent}"/>"
					</c:if>
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>            
        ]
      }
    ],