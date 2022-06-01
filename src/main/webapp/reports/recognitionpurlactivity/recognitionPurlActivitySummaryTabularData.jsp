<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.recognitionpurlactivity.RecognitionPurlActivityReportValue"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
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
              "name": "",
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
              "name": "<cms:contentText key="RECIPIENTS_COUNT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="RECIPIENTS_COUNT_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.recipientsCnt}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="CONTRIBUTORS_INVITED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CONTRIBUTORS_INVITED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributorsInvitedCnt}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="ACTUAL_CONTRIBUTORS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTUAL_CONTRIBUTORS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributedCnt}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="CONTRIBUTION_PERCENTAGE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CONTRIBUTION_PERCENTAGE_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributionPct}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="CONTRIBUTIONS_POSTED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="CONTRIBUTIONS_POSTED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.contributionPostedCnt}"/>"
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
              "url": "${systemUrl}/reports/displayRecognitionPurlActivityReport.do?method=extractReport&fullReport=full"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayRecognitionPurlActivityReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": 
        [
         <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "${reportItem.nodeName}",
            <c:choose>
	       		<c:when test="${ fn:contains( reportParametersForm.parentNodeId , reportItem.hierarchyNodeId ) }"> 
	       			<c:choose>
	       				<c:when test="${ reportItem.recipientsCnt > 0 || reportItem.contributorsInvitedCnt > 0 || reportItem.contributedCnt > 0 || reportItem.contributionPostedCnt > 0 }">
	       					<%
			       				RecognitionPurlActivityReportValue reportValue = (RecognitionPurlActivityReportValue) pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getHierarchyNodeId(  ) );
							    paramMap.put( "nodeAndBelow", 0 );
							    String urlToUse = "/reports/displayRecognitionPurlActivityReport.do?method=displayParticipantReport";
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
	       				RecognitionPurlActivityReportValue reportValue = (RecognitionPurlActivityReportValue) pageContext.getAttribute( "reportItem" );
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "parentNodeId", reportValue.getHierarchyNodeId(  ) );
					    paramMap.put( "nodeAndBelow", 1 );
					    String urlToUse = "/reports/displayRecognitionPurlActivityReport.do?method=displaySummaryReport";
					    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					%>
		            "<%=drilldownUrl %>",
	          	</c:otherwise>
          	</c:choose>
            "<fmt:formatNumber value="${reportItem.recipientsCnt}" />",
            "<fmt:formatNumber value="${reportItem.contributorsInvitedCnt}" />",
            "<fmt:formatNumber value="${reportItem.contributedCnt}" />",
            "<fmt:formatNumber value="${reportItem.contributionPct}" />",
            "<fmt:formatNumber value="${reportItem.contributionPostedCnt}" />"
          ]
          </c:forEach>
        ]
      }
    ],