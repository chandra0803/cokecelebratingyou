<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.nomination.NominationReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
sortMap.put( "drilldownPromoId", request.getAttribute( "drilldownPromoId" ) );
sortMap.put( "userId", reportParametersForm.getUserId() );
//Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayNominationReceiverReport.do?method=displayNomineeReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>
<c:set var="currencyCode" value="${currencyCode}"/>
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
              "name": "<cms:contentText key="DATE_SUBMITTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DATE_SUBMITTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="NOMINATION_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
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
              "name": "<cms:contentText key="NOMINEE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="NOMINEE_COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="NOMINEE_ORG" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_ORG_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="TEAM_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TEAM_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="NOMINATOR" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": ""
            },
            {
              "id": "8",
              "name": "<cms:contentText key="NOMINATOR_COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": ""
            },
			{
              "id": "9",
              "name": "<cms:contentText key="NOMINATOR_ORG" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_ORG_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": ""
            },
            {
              "id": "10",
              "name": "<cms:contentText key="PROMOTION_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": ""
            },
            {
              "id": "11",
              "name": "<cms:contentText key="STATUS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="STATUS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": ""
            },
            {
              "id": "12",
              "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.pointsAwardAmt}"/>"
            },
            {
              "id": "13",
              "name": "<cms:contentTemplateText key="CASH_RECEIVED" code="${report.cmAssetCode}" args="${currencyCode}"/>",
              "description": "<cms:contentText key="CASH_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.cashAwardAmt}"/>"
            },
            {
              "id": "14",
              "name": "<cms:contentText key="OTHER_AWARD_DESCRIPTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="OTHER_AWARD_DESCRIPTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
              "summary": ""
            },
            {
              "id": "15",
              "name": "<cms:contentTemplateText key="OTHER_AWARD" code="${report.cmAssetCode}" args="${currencyCode}"/>",
              "description": "<cms:contentText key="OTHER_AWARD_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=15&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.otherAwardAmt}"/>"
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
              "url": "${systemUrl}/reports/displayNominationReceiverReport.do?method=extractReport&exportViewType=fullView&extractType=primary"
            },
            {
              "label": "<cms:contentText key="EXTRACT_NOM_SUMMARY" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayNominationReceiverReport.do?method=extractSecondaryReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayNominationReceiverReport.do?method=extractReport&exportViewType=currentView&extractType=primary&exportLevel=pax"
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
	        	<c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataStatus">
				 	[
						"<fmt:formatDate value="${reportDataId.dateSubmitted}" pattern="${JstlDatePattern}" />",
				 		<%
			          		NominationReportValue reportValue = (NominationReportValue)pageContext.getAttribute( "reportDataId" );
			            	Map<String, Object> urlMap = new HashMap<String, Object>();
			       			String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), reportUrl, urlMap );
						      
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "promotionTypeCode", "nomination" );
						    paramMap.put( "mode", "received" );
						    paramMap.put( "claimId", reportValue.getClaimId(  ));
						    paramMap.put( "isFullPage", "false");
						    paramMap.put( "returnURL", returnURL );
						    String detailUrl = ClientStateUtils.generateEncodedLink( "", "claim/claimDetail.do", paramMap );
						%>
						"${systemUrl}/<%=detailUrl%>",
				    	"${reportDataId.nominee}",
				       	"${reportDataId.recvrCountryName}",
				       	"${reportDataId.recvrNodeName}",
				       	"${reportDataId.teamName}",
				    	"${reportDataId.nominator}",
				  		"${reportDataId.giverCountryName}",
				       	"${reportDataId.giverNodeName}",
				       	"${reportDataId.promotionName}",
				      	"${reportDataId.claimApprovalStatus}",
				    	"<fmt:formatNumber value="${reportDataId.pointsAwardAmt}" />",
				    	"<fmt:formatNumber value="${reportDataId.cashAwardAmt}" />",
				    	"${reportDataId.otherAwardDesc}",
				    	"<fmt:formatNumber value="${reportDataId.otherAwardAmt}" />"
					]
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>   
			</c:otherwise>
		</c:choose>	          
        ]
      }
    ],
