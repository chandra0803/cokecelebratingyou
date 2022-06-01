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
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
String reportUrl = "/reports/displayChallengepointAchievementReport.do?method=displayDetailReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="PAX_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PAX_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",		             
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",		             
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentText key="PROMO_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMO_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",		             
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="LEVEL_SELECTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="LEVEL_SELECTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",		             
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="BASE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="BASE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",		             
              "summary": "<fmt:formatNumber value="${totalsRowData.baseQuantity}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="GOAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="GOAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",		             
              "summary": "<fmt:formatNumber value="${totalsRowData.amountToAchieve}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="ACTUAL_RESULTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_RESULTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",		             
              "summary": "<fmt:formatNumber value="${totalsRowData.currentValue}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="PERCENT_OF_GOAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PERCENT_OF_GOAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",		             
              "summary": "<fmt:formatNumber value="${totalsRowData.percentAchieved}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="ACHIEVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACHIEVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",		             
              "summary": ""
            },
            {
              "id": "10",
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",		             
              "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
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
				    "${reportItem.paxName}",		    	    		    						    								
				   	"${reportItem.orgName}",
				   	"${reportItem.promoName}",
				   	"${reportItem.levelNumber}",
				   	"<fmt:formatNumber value="${reportItem.baseQuantity}"/>",
				   	"<fmt:formatNumber value="${reportItem.amountToAchieve}"/>",				   	
				   	"<fmt:formatNumber value="${reportItem.currentValue}"/>",				   	
				   	"<fmt:formatNumber value="${reportItem.percentAchieved}"/>",
				   	"<c:choose><c:when test="${reportItem.isAchieved}"><cms:contentText key="YES" code="system.common.labels"/></c:when><c:otherwise><cms:contentText key="NO" code="system.common.labels"/></c:otherwise></c:choose>",				   	
				   	"<fmt:formatNumber value="${reportItem.points}"/>"			   		   	
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>            
        ]
      }
    ],