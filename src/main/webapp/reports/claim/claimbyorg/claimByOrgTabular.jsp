<%@page import="com.biperf.core.value.claim.ClaimByOrgReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@ include file="/include/taglib.jspf" %>
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
              "name": "Claim by Org Drilldown URL",
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
              "name": "<cms:contentText key="ELIGIBLE_SUBMITTERS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ELIGIBLE_SUBMITTERS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "${totalsRowData.eligSubmitters}"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="ACTUAL_SUBMITTERS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_SUBMITTERS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "${totalsRowData.actualSubmitters}"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="PARTICIPATION_RATE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PARTICIPATION_RATE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "${totalsRowData.participationRate}"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="CLAIMS_TOTAL" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIMS_TOTAL_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "${totalsRowData.totalClaims}"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="CLAIMS_OPEN" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIMS_OPEN_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "${totalsRowData.openClaims}"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="CLAIMS_CLOSED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CLAIMS_CLOSED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "${totalsRowData.closedClaims}"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="ITEMS_APPROVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ITEMS_APPROVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "${totalsRowData.approvedItems}"
            },
            {
              "id": "10",
              "name": "<cms:contentText key="ITEMS_DENIED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ITEMS_DENIED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "${totalsRowData.deniedItems}"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="ITEMS_ON_HOLD" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ITEMS_ON_HOLD_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "${totalsRowData.holdItems}"
            },
            {
              "id": "12",
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
              "summary": "${totalsRowData.points}"
            },
            {
              "id": "13",
              "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
              "summary": "${totalsRowData.sweepstakesWon}"
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
              "url": "${systemUrl}/reports/displayClaimByOrgReport.do?method=extractReport&exportViewType=fullView"
            },
            {
              "label": "<cms:contentText key="EXTRACT_ITEMS_CLAIMED" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayClaimByOrgReport.do?method=extractSecondaryReport&exportViewType=fullView"
            }  
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayClaimByOrgReport.do?method=extractReport&exportViewType=currentView"
          },
       	  "inlineTextDescription": ""
        },
        "results": [
           	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
				[
				    "${reportItem.orgName}",
				    <c:choose>
				    	<c:when test="${ fn:contains(  reportItem.orgId ,reportParametersForm.parentNodeId ) }">
				    		<c:choose>
				    			<c:when test="${reportItem.actualSubmitters > 0 || reportItem.totalClaims > 0 || reportItem.approvedItems > 0 || reportItem.deniedItems > 0 || reportItem.holdItems > 0 || reportItem.points > 0 }">
						    		<%
				            			ClaimByOrgReportValue reportValue = (ClaimByOrgReportValue) pageContext.getAttribute( "reportItem" );
									    Map<String, Object> paramMap = new HashMap<String, Object>();
									    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
									    paramMap.put( "nodeAndBelow", 0 );
									    String urlToUse = "/reports/displayClaimByOrgReport.do?method=displayClaimByPaxSummaryReport";
									    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
									%>
						            "<%=drilldownUrl %>",		    		
				    			</c:when>
				    			<c:otherwise>
				    				false,
				    			</c:otherwise>
				    		</c:choose>
				    	</c:when>
				    	<c:otherwise>
				    		<%
		            			ClaimByOrgReportValue reportValue = (ClaimByOrgReportValue) pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getOrgId(  ) );
							    paramMap.put( "nodeAndBelow", 1 );
							    String urlToUse = "/reports/displayClaimByOrgReport.do?method=displaySummaryReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
				            "<%=drilldownUrl %>",
				    	</c:otherwise>
				    </c:choose>										
				   	"${reportItem.eligSubmitters}",
					"${reportItem.actualSubmitters}",
					"${reportItem.participationRate}",
					"${reportItem.totalClaims}",
					"${reportItem.openClaims}",
					"${reportItem.closedClaims}",
					"${reportItem.approvedItems}",
					"${reportItem.deniedItems}",
					"${reportItem.holdItems}",
					"${reportItem.points}",
					"${reportItem.sweepstakesWon}"
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>            
        ]
      }
    ],