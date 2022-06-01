<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.awardsreport.AwardsSummaryReportValue"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = report.getUrl(  );
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>
<c:set var="awardType" value="${awardType}"/>
<c:set var="currencyCode" value="${currencyCode}"/>
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
          "name": "Organization drill-down URL",
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
          "name": "<cms:contentText key="ELIGIBLE_PARTICIPANTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ELIGIBLE_PARTICIPANTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligiblePaxCnt}" />"
        },
        {
          "id": "4",
          "name": "<cms:contentTemplateText key="RECEIVED_AWARD" code="${report.cmAssetCode}" args="${awardType}"/>",
          "description": "<cms:contentTemplateText key="RECEIVED_AWARD_DESC" code="${report.cmAssetCode}" args="${awardType}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.receivedAwardCnt}" />"
        },
        {
          "id": "5",
          "name": "<cms:contentTemplateText key="PERCENT_RECEIVED_AWARD" code="${report.cmAssetCode}" args="${awardType}"/>",
          "description": "<cms:contentTemplateText key="PERCENT_RECEIVED_AWARD_DESC" code="${report.cmAssetCode}" args="${awardType}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.receivedAwardPct}" />"
        },
        {
          "id": "6",
          "name": "<cms:contentTemplateText key="HAVE_NOTRECEIVED_AWARD" code="${report.cmAssetCode}" args="${awardType}"/>",
          "description": "<cms:contentTemplateText key="HAVE_NOTRECEIVED_AWARD_DESC" code="${report.cmAssetCode}" args="${awardType}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.haveNotReceivedAwardCnt}" />"
        },
        {
          "id": "7",
          "name": "<cms:contentTemplateText key="PERCENT_HAVE_NOTRECEIVED_AWARD" code="${report.cmAssetCode}" args="${awardType}"/>",
          "description": "<cms:contentTemplateText key="PERCENT_NOTRECEIVED_AWARD_DESC" code="${report.cmAssetCode}" args="${awardType}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.haveNotReceivedAwardPct}" />"
        }
        
        <c:choose>
        <c:when test="${awardType=='Points' }">
        ,
        {
          "id": "8",
          "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.pointsReceivedCnt}" />"
        }
        ,
        {
          "id": "9",
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWonCnt}" />"
        }
        ,
        {
          "id": "10",
          "name": "<cms:contentText key="ONTHESPOT_DEPOSITED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ONTHESPOT_DEPOSITED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.onTheSpotDepositedCnt}" />"
        }
        </c:when>
        <c:when test="${awardType=='Cash' }">
        ,
        {
          "id": "10",
          "name": "<cms:contentTemplateText key="CASH_AMOUNT" code="${report.cmAssetCode}" args="${currencyCode}"/>",
          "description": "<cms:contentText key="CASH_AMOUNT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.cashReceived}" />"
        }
        </c:when>
        <c:when test="${awardType=='Other' }">
        ,
        {
          "id": "11",
          "name": "<cms:contentText key="OTHER_QUANTITY" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="OTHER_QUANTITY_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.other}" />"
        }
        </c:when>
        <c:when test="${awardType=='Plateau'}">
        ,
        {
          "id": "12",
          "name": "<cms:contentText key="PLATEAU_EARNED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PLATEAU_EARNED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.plateauEarnedCnt}" />"
        }
        </c:when>
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
          "url": "${systemUrl}/reports/displayAwardsByOrgReport.do?method=extractReport&fullReport=full"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayAwardsByOrgReport.do?method=extractReport"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [          	
        "${reportDataId.orgName}",
        <c:choose>
			<c:when test="${ fn:contains(  reportDataId.nodeId ,reportParametersForm.parentNodeId ) }">
				<c:choose>
					<c:when test="${reportDataId.receivedAwardCnt > 0}">
						<%
							AwardsSummaryReportValue reportValue = (AwardsSummaryReportValue) pageContext.getAttribute( "reportDataId" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
						    paramMap.put( "nodeAndBelow", 0 );
						    String urlToUse = "/reports/displayAwardsByOrgReport.do?method=displayAwardsFirstDetailReport";
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
					AwardsSummaryReportValue reportValue = (AwardsSummaryReportValue) pageContext.getAttribute( "reportDataId" );
				    Map<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
				    paramMap.put( "nodeAndBelow", 1 );
				    String urlToUse = "/reports/displayAwardsByOrgReport.do?method=displaySummaryReport";
				    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
				%>
		        "<%=drilldownUrl %>",
			</c:otherwise>
	  	</c:choose>
        "<fmt:formatNumber value="${reportDataId.eligiblePaxCnt}" />",
        "<fmt:formatNumber value="${reportDataId.receivedAwardCnt}" />",
        "<fmt:formatNumber value="${reportDataId.receivedAwardPct}" />",
        "<fmt:formatNumber value="${reportDataId.haveNotReceivedAwardCnt}" />",
        "<fmt:formatNumber value="${reportDataId.haveNotReceivedAwardPct}" />"
        <c:if test="${awardType=='Points'}">
        ,
        "<fmt:formatNumber value="${reportDataId.pointsReceivedCnt}" />",
        "<fmt:formatNumber value="${reportDataId.sweepstakesWonCnt}" />",
         "<fmt:formatNumber value="${reportDataId.onTheSpotDepositedCnt}" />"
        </c:if>
        <c:if test="${awardType=='Cash'}">
        ,
        "<fmt:formatNumber value="${reportDataId.cashReceived}" />"
        </c:if>
        
        <c:if test="${awardType=='Other'}">
        ,
        "<fmt:formatNumber value="${reportDataId.other}" />"
        </c:if>
        
        <c:if test="${awardType=='Plateau'}">
        ,
        "<fmt:formatNumber value="${reportDataId.plateauEarnedCnt}" />"
        </c:if>
        
      ]
      </c:forEach>
    ]
  }
],