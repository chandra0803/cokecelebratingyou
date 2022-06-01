<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.value.claim.ClaimByPaxReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>

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
          	<c:choose>
			<c:when test="${displayHaveNotFilterExportMessage}">
				{
	              "id": "1",
	              "name": "Data not available for display",
	              "description": "This tabular data is only available for export",
	              "type": "string",
	              "alignment": "",
	              "nameId": "",
	              "sortable": false,
	              "sortUrl": "",
	              "summary": ""
	            }
			</c:when>
			<c:otherwise>
            {
              "id": "1",
              "name": "<cms:contentText key="SUBMITTER" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SUBMITTER_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "Claim by Pax Drilldown URL",
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
              "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
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
              "name": "<cms:contentText key="DEPARTMENT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DEPARTMENT_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="JOB_POSITION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="JOB_POSITION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": ""
            },
            {
              "id": "8",
              "name": "<cms:contentText key="CLAIMS_TOTAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIMS_TOTAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalClaims}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="CLAIMS_OPEN" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIMS_OPEN_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.openClaims}"/>"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="CLAIMS_CLOSED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIMS_CLOSED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.closedClaims}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="ITEMS_APPROVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ITEMS_APPROVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.approvedItems}"/>"
            },
            {
              "id": "12",
              "name": "<cms:contentText key="ITEMS_DENIED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ITEMS_DENIED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.deniedItems}"/>"
            },
            {
              "id": "13",
              "name": "<cms:contentText key="ITEMS_ON_HOLD" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ITEMS_ON_HOLD_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.heldItems}"/>"
            },
            {
              "id": "14",
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
            },
            {
              "id": "15",
              "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=16&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWon}"/>"
            }
            </c:otherwise>
      		</c:choose> 
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
              "url": "${systemUrl}/reports/displayClaimByPaxReport.do?method=extractReport&exportLevel=pax"
            },
            {
              "label": "<cms:contentText key="EXTRACT_ITEMS_CLAIMED" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayClaimByPaxReport.do?method=extractSecondaryReport&exportLevel=pax"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayClaimByPaxReport.do?method=extractReport&exportLevel=pax"
          },
          "inlineTextDescription": ""
        },
        "results": [
        	<c:choose>
			<c:when test="${displayHaveNotFilterExportMessage}">
				[
					"<cms:contentText key="HAVE_NOT_FILTER_MSG" code="report.display.page"/>"
				]
			</c:when>
			<c:otherwise>
	           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
					[
					    "${reportItem.paxName}",
	            		<%
	            			ClaimByPaxReportValue reportValue = (ClaimByPaxReportValue) pageContext.getAttribute( "reportItem" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "paxId", reportValue.getPaxId(  ) );
						    paramMap.put( "drilldownPromoId", reportValue.getPromotionId(  ) );
						    String urlToUse = "/reports/displayClaimByPaxReport.do?method=displayClaimListSummaryReport";
						    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
						%>
			            "<%=drilldownUrl %>",
					    "${reportItem.country}",
					    "${reportItem.orgName}",
					   	"${reportItem.department}",
						"${reportItem.jobPosition}",
						"${reportItem.promotionName}",
						"<fmt:formatNumber value="${reportItem.totalClaims}"/>",
						"<fmt:formatNumber value="${reportItem.openClaims}"/>",
						"<fmt:formatNumber value="${reportItem.closedClaims}"/>",
						"<fmt:formatNumber value="${reportItem.approvedItems}"/>",
						"<fmt:formatNumber value="${reportItem.deniedItems}"/>",
						"<fmt:formatNumber value="${reportItem.heldItems}"/>",
						"<fmt:formatNumber value="${reportItem.points}"/>",
						"<fmt:formatNumber value="${reportItem.sweepstakesWon}"/>"
					]
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>	         
        ]
      }
    ],