<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.claim.ClaimByPaxReportValue"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "paxId", reportParametersForm.getPaxId(  ) );
sortMap.put( "drillDownPromotionId", request.getAttribute( "drillDownPromotionId" ) );
Report report = ( Report ) request.getAttribute( "report" );
String reportUrl = "/reports/displayClaimByPaxReport.do?method=displayClaimListSummaryReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="CLAIM" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIM_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=", 
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="CLAIM_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
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
              "name": "<cms:contentText key="CLAIM_STATUS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIM_STATUS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=", 
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="DATE_SUBMITTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DATE_SUBMITTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=", 
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=", 
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=", 
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=", 
              "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl":"<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=", 
              "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWon}"/>"
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
              "url": "${systemUrl}/reports/displayClaimByPaxReport.do?method=extractReport&exportLevel=pax"
            },
            {
              "label": "<cms:contentText key="EXTRACT_ITEMS_CLAIMED" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayClaimByPaxReport.do?method=extractSecondaryReport&exportLevel=pax"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayClaimByPaxReport.do?method=extractReport&exportLevel=paxLevel"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[
				    "${reportItem.claimNumber}",
					<%
						ClaimByPaxReportValue reportValue = (ClaimByPaxReportValue)pageContext.getAttribute( "reportItem" );
			           	Map<String, Object> urlMap = new HashMap<String, Object>(); 
			      		String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), report.getUrl( ), urlMap );
					      
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "claimId", reportValue.getClaimId(  ));
					    paramMap.put( "isFullPage", "false");
					    paramMap.put( "returnURL", returnURL );
					    paramMap.put( "open", reportValue.getClaimStatus(  ) );
					    String detailUrl = ClientStateUtils.generateEncodedLink( "", "claim/productClaimDetail.do?method=display", paramMap );
					%>
					"${systemUrl}/<%=detailUrl%>",
				    "${reportItem.claimStatus}",
				    "<fmt:formatDate value="${reportItem.dateSubmitted}" pattern="${JstlDatePattern}" />",
				   	"${reportItem.orgName}",
					"${reportItem.promotionName}",
					"<fmt:formatNumber value="${reportItem.points}"/>",
					"<fmt:formatNumber value="${reportItem.sweepstakesWon}"/>"
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>         
        ]
      }
    ],