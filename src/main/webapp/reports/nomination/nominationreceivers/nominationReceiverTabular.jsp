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
              "name": "<cms:contentText key="NOMINEE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_DESC" code="${report.cmAssetCode}"/>",
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
              "name": "<cms:contentText key="NOMINEE_COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="NOMINEE_ORG" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_ORG_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="NO_OF_NOMINATIONS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NO_OF_NOMINATIONS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.nominatedCnt}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.pointsReceived}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentTemplateText key="CASH_RECEIVED" code="${report.cmAssetCode}" args="${currencyCode}"/>",
              "description": "<cms:contentText key="CASH_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.cashReceived}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="OTHER_QTY_VALUE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="OTHER_QTY_VALUE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.otherQytReceived}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentTemplateText key="OTHER_AWARD" code="${report.cmAssetCode}" args="${currencyCode}"/>",
              "description": "<cms:contentText key="OTHER_AWARD_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.otherAmtReceived}"/>"
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
            "url": "${systemUrl}/reports/displayNominationReceiverReport.do?method=extractReport&exportViewType=currentView&extractType=primary"
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
						"${reportDataId.nominee}",
						<%
						NominationReportValue reportValue = (NominationReportValue)pageContext.getAttribute( "reportDataId" );
            	        Map<String, Object> urlMap = new HashMap<String, Object>();
            		    Map<String, Object> paramMap = new HashMap<String, Object>();
            		    paramMap.put("userId", reportValue.getRecvrUserId( ) );
			    	    String urlToUse = "/reports/displayNominationReceiverReport.do?method=displayNomineeReport";
			    	    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					     %>
					    "<%=drilldownUrl %>",
				       	"${reportDataId.recvrCountryName}",
				       	"${reportDataId.recvrNodeName}",
				    	"<fmt:formatNumber value="${reportDataId.nominatedCnt}"/>",
				    	"<fmt:formatNumber value="${reportDataId.pointsReceived}"/>",
				    	"<fmt:formatNumber value="${reportDataId.cashReceived}"/>",
				    	"<fmt:formatNumber value="${reportDataId.otherQytReceived}"/>",
				    	"<fmt:formatNumber value="${reportDataId.otherAmtReceived}"/>"
					]
					<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
				</c:forEach>   
			</c:otherwise>
		</c:choose>	          
        ]
      }
    ],