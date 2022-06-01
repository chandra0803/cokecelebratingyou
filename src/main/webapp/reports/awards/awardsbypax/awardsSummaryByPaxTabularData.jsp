<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.awardsreport.AwardsSummaryByPaxReportValue"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = report.getUrl(  );
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
          "name": "<cms:contentText key="PARTICIPANT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PARTICIPANT_DESC" code="${report.cmAssetCode}"/>",
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
          "sortable": true,
          "sortUrl": "",
          "summary": ""
        },
        {
          "id": "3",
          "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
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
        }
        
        <c:choose>
        	<c:when test="${awardType=='Points'}">
        	,
	        {
	          "id": "7",
	          "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
	          "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
	          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
	          "summary": "<fmt:formatNumber value="${totalsRowData.pointsReceivedCnt}" />"
	        },
	        {
	          "id": "8",
	          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
	          "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
	          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
	          "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWonCnt}" />"
	        }
	         ,
	        {
	          "id": "9",
	          "name": "<cms:contentText key="ONTHESPOT_DEPOSITED" code="${report.cmAssetCode}"/>",
	          "description": "<cms:contentText key="ONTHESPOT_DEPOSITED_DESC" code="${report.cmAssetCode}"/>",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
	          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
	          "summary": "<fmt:formatNumber value="${totalsRowData.onTheSpotDepositedCnt}" />"
	        }
        	</c:when>
        	<c:when test="${awardType=='Cash'}">
        	,
	        {
	          "id": "10",
	          "name": "<cms:contentTemplateText key="CASH_AMOUNT" code="${report.cmAssetCode}" args ="${currencyCode}"/>",
	          "description": "<cms:contentText key="CASH_AMOUNT_DESC" code="${report.cmAssetCode}"/>",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
	          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
	          "summary": "<fmt:formatNumber value="${totalsRowData.cashReceivedCnt}" />"
	        }
        	</c:when>
        	<c:when test="${awardType=='Other'}">
        	,
	        {
	          "id": "11",
	          "name": "<cms:contentText key="OTHER_QUANTITY" code="${report.cmAssetCode}"/>",
	          "description": "<cms:contentText key="OTHER_QUANTITY_DESC" code="${report.cmAssetCode}"/>",
	          "type": "string",
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
	          "name": "<cms:contentText key="AWARDS_QUANTITY" code="${report.cmAssetCode}"/>",
	          "description": "<cms:contentText key="AWARDS_QUANTITY_DESC" code="${report.cmAssetCode}"/>",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
	          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
	          "summary": "<fmt:formatNumber value="${totalsRowData.plateauEarnedCnt}" />"
	        }
        	</c:when>
        </c:choose>
        
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
          "url": "${systemUrl}/reports/displayAwardsByPaxReport.do?method=extractReport"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayAwardsByPaxReport.do?method=extractReport"
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
			    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
			         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
			      [          	
			        "${reportDataId.paxName}",
					<%
						AwardsSummaryByPaxReportValue reportValue = (AwardsSummaryByPaxReportValue) pageContext.getAttribute( "reportDataId" );
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "userId", reportValue.getUserId(  ) );
					    String urlToUse = "/reports/displayAwardsByPaxReport.do?method=displayAwardsDetailByPaxReport";
					    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					%>
			        "<%=drilldownUrl %>",
			        "<c:out value="${beacon:escapeDoubleQuote(reportDataId.orgName)}" escapeXml="false"/>",
					"<c:out value="${reportDataId.country}" />",
			        "<c:out value="${reportDataId.department}" />",
			        "<c:out value="${reportDataId.jobPosition}" />"
			        
			        <c:choose>
			        <c:when test="${awardType=='Points' }">
			        ,
			        "<fmt:formatNumber value="${reportDataId.pointsReceivedCnt}" />",
			        "<fmt:formatNumber value="${reportDataId.sweepstakesWonCnt}" />",
			        "<fmt:formatNumber value="${reportDataId.onTheSpotDepositedCnt}" />"
			        </c:when>
			        <c:when test="${awardType=='Cash' }">
			        ,
			        "<fmt:formatNumber value="${reportDataId.cashReceivedCnt}" />"
			        </c:when>
			        <c:when test="${awardType=='Other' }">
			         ,
			         "<c:out value="${reportDataId.other}" />"
			        </c:when>
			        <c:when test="${awardType=='Plateau' }">
			         ,
			        "<fmt:formatNumber value="${reportDataId.plateauEarnedCnt}" />"
    			       </c:when>
			        </c:choose>
			      ]
			      </c:forEach>
			</c:otherwise>
		</c:choose>        
    ]
  }
],