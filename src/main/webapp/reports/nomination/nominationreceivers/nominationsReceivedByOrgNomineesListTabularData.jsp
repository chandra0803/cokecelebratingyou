<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.nomination.NominationReceiverReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayNominationReceivedByOrgReport.do?method=displayNomitionsReceivedNomineesList";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            <%--
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
            --%>
            {
              "id": "1",
              "name": "<cms:contentText key="NOMINEE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINEE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
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
            <%--
            {
              "id": "6",
              "name": "<cms:contentText key="NOMINATOR" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="NOMINATOR_COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": ""
            },
			{
              "id": "8",
              "name": "<cms:contentText key="NOMINATOR_ORG" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_ORG_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": ""
            },
            {
              "id": "9",
              "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": ""
            },
            {
              "id": "10",
              "name": "<cms:contentText key="STATUS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="STATUS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": ""
            },
            {
              "id": "11",
              "name": "<cms:contentText key="APPROVER" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="APPROVER_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": ""
            },
            {
              "id": "12",
              "name": "<cms:contentText key="DATE_APPROVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DATE_APPROVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
              "summary": ""
            },
            --%>
            {
              "id": "12",
              "name": "<cms:contentText key="NO_OF_NOMINATIONS" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalNominationsRcvdCnt}" />"
            },
            {
              "id": "13",
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.points}" />"
            }
            <%--
            {
              "id": "14",
              "name": "<cms:contentText key="SWEEPSTAKES" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWon}" />"
            }
            --%>
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
              "url": "${systemUrl}/reports/displayNominationReceiverReport.do?method=extractSecondaryReport&exportViewType=fullView&extractType=primary"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayNominationReceiverReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        	<c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataStatus">
			 	[
			 	    "${reportDataId.receiverName}",
					<%
						NominationReceiverReportValue reportValue = (NominationReceiverReportValue)pageContext.getAttribute( "reportDataId" );
            	        Map<String, Object> urlMap = new HashMap<String, Object>();
            		    Map<String, Object> paramMap = new HashMap<String, Object>();
            		    paramMap.put("userId", reportValue.getReceiverId() );
			    	    String urlToUse = "/reports/displayNominationReceiverReport.do?method=displaySummaryByPaxReport";
			    	    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					 %>
					"<%=drilldownUrl %>",
					"${reportDataId.receiverCountry}",
			       	"${reportDataId.receiverOrgName}",
			       	"<fmt:formatNumber value="${reportDataId.totalNominationsRcvdCnt}" />",
			    	<%--
			    	"${reportDataId.giverName}",
			  		"${reportDataId.giverCountry}",
			       	"${reportDataId.giverOrgName}",
			 		"${reportDataId.promoName}",
			      	"${reportDataId.approvalStatus}",
			    	"${reportDataId.approverName}",
			     	"<fmt:formatDate value="${reportDataId.approvalDate}" pattern="${JstlDatePattern}" />",
			     	--%>
			     	"<fmt:formatNumber value="${reportDataId.points}" />"
			     	<%--
			     	"<fmt:formatNumber value="${reportDataId.sweepstakesWon}" />"
			     	--%>          		            		            
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>             
        ]
      }
    ],