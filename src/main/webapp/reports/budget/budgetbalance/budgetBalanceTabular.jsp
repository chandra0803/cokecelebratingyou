<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.budget.PointsBudgetBalanceReportValue"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf" %>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = ( Report ) request.getAttribute( "report" );
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
          "name": "<cms:contentText key="PROMOTION_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMO_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
        },
        {
          "id": "2",
          "name": "<cms:contentText key="BUDGET_BALANCE_DRILL_DOWN_URL" code="${report.cmAssetCode}"/>",
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
          "name": "<cms:contentText key="PROMO_DATE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMO_DATE_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="BUDGET_MASTER_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="BUDGET_MASTER_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="BUDGET_PERIOD" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="BUDGET_PERIOD_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="ORIGINAL_BALANCE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ORIGINAL_BALANCE_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.originalBudget}"/>"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="BUDGET_ADJUSTMENTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="BUDGET_ADJUSTMENTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.budgetAdjustments}"/>"
        },
        {
          "id": "8",
          "name": "<cms:contentText key="AWARDED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="AWARDED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.awarded}"/>"
        },
        {
          "id": "9",
          "name": "<cms:contentText key="AVAILABLE_BALANCE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="AVAILABLE_BALANCE_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.availableBalance}"/>"
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
          "label": "<cms:contentText key="EXTRACT_BUDGET_FULL" code="report.display.page"/>",
          "url": "${systemUrl}/reports//displayPointsBudgetBalanceReport.do?method=extractReport&exportViewType=fullView&extractType=primary"
        },
        {
          "label": "<cms:contentText key="BUDGET_ISSUANCE" code="report.display.page"/>",
          "url": "${systemUrl}/reports//displayPointsBudgetBalanceReport.do?method=extractSecondaryReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports//displayPointsBudgetBalanceReport.do?method=extractReport&exportViewType=currentView&extractType=primary"
      },
      "inlineTextDescription": "<cms:contentTemplateText key="DESCRIPTIVE_TEXT" code="${report.cmAssetCode}" args="${viewAsCountryName}" delimiter=","/>"
    },
    "results": [
       	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		[
		    "${reportItem.promotionName}",
		   
		    <%
		    	PointsBudgetBalanceReportValue reportValue = (PointsBudgetBalanceReportValue) pageContext.getAttribute( "reportItem" );
			    Map<String, Object> paramMap = new HashMap<String, Object>();
			    paramMap.put( "drilldownPromoId", reportValue.getPromotionId(  ) );
			    paramMap.put( "budgetSegmentId", reportValue.getBudgetSegmentId() );
			    String urlToUse = "/reports//displayPointsBudgetBalanceReport.do?method=displayPointsBudgetByPromotionSummaryReport";
			    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
			%>
			"<%=drilldownUrl %>",
			"${reportItem.promotionDateRange}",
		    "${reportItem.budgetMasterName}",
		   	"${reportItem.budgetPeriod}",
		   	"<fmt:formatNumber value="${reportItem.originalBudget}"/>",
		   	"<fmt:formatNumber value="${reportItem.budgetAdjustments}"/>",
		   	"<fmt:formatNumber value="${reportItem.awarded}"/>",
		   	"<fmt:formatNumber value="${reportItem.availableBalance}"/>"
		]
		<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
		</c:forEach>
    ]
  }
],
