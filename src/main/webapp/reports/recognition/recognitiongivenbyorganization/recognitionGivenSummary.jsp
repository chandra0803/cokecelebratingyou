<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.value.recognition.RecognitionSummaryValue"%>
<%@page import="com.biperf.core.domain.report.Report"%>
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
          "name": "<cms:contentText key="ELIGIBLE_GIVERS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ELIGIBLE_GIVERS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligibleCnt}"/>"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="ACTUAL_GIVERS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ACTUAL_GIVERS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.actualCnt}"/>"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="PERCENT_GAVE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PERCENT_GAVE_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligiblePct}"/>"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="TOTAL_RECOGNITION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="TOTAL_RECOGNITION_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.totalRecognition}"/>"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
        },
        {
          "id": "8",
          "name": "<cms:contentText key="PLATEAU_EARNED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PLATEAU_EARNED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.plateauEarnedCnt}"/>"
        }
        <%--
        {
          "id": "9",
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWonCnt}"/>"
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
          "url": "${systemUrl}/reports/displayRecognitionGivenByOrgReport.do?method=extractReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayRecognitionGivenByOrgReport.do?method=extractReport&exportViewType=currentView"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [          	
        "${reportDataId.nodeName}",
        <c:choose>
			<c:when test="${ fn:contains(  reportDataId.nodeId ,reportParametersForm.parentNodeId ) }">
				<c:choose>
					<c:when test="${reportDataId.actualCnt > 0 || reportDataId.totalRecognition > 0 || reportDataId.points > 0 || reportDataId.plateauEarnedCnt > 0}">
			 			<%
			 				RecognitionSummaryValue reportValue = (RecognitionSummaryValue) pageContext.getAttribute( "reportDataId" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
						    paramMap.put( "nodeAndBelow", 0 );
						    String urlToUse = "/reports/displayRecognitionGivenByOrgReport.do?method=displayDetailReport";
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
 					RecognitionSummaryValue reportValue = (RecognitionSummaryValue) pageContext.getAttribute( "reportDataId" );
				    Map<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
				    paramMap.put( "nodeAndBelow", 1 );
				    String urlToUse = "/reports/displayRecognitionGivenByOrgReport.do?method=displaySummaryReport";
				    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
				%>
          		"<%=drilldownUrl %>",
			</c:otherwise>
	  	</c:choose>
        "<fmt:formatNumber value="${reportDataId.eligibleCnt}" />",
        "<fmt:formatNumber value="${reportDataId.actualCnt}" />",
        "<fmt:formatNumber value="${reportDataId.eligiblePct}" />",
        "<fmt:formatNumber value="${reportDataId.totalRecognition}" />",
        "<fmt:formatNumber value="${reportDataId.points}" />",
        "<fmt:formatNumber value="${reportDataId.plateauEarnedCnt}" />"
        <%-- "<fmt:formatNumber value="${reportDataId.sweepstakesWonCnt}" />"--%>
      ]
      </c:forEach>
    ]
  }
],