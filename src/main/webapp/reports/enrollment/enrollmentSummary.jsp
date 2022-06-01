<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.enrollment.EnrollmentActivityReportValue"%>

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
          "name": "<cms:contentText key="ACTIVE_STATUS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ACTIVE_STATUS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.activeCnt}" />"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="INACTIVE_STATUS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="INACTIVE_STATUS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.inactiveCnt}" />"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="TOTAL_STATUS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="TOTAL_STATUS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.totalCnt}" />"
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
          "url": "${systemUrl}/reports/displayEnrollmentActivityReport.do?method=extractReport&exportViewType=fullView"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayEnrollmentActivityReport.do?method=extractReport&exportViewType=currentView"
      },
      "inlineTextDescription": ""
    },
    "results": [
    	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
   			<c:if test="${reportDataStatus.index != 0}">,</c:if>
      		[		          	
	          "${reportItem.hierarchyNodeName}",
	          <c:choose>
	          	<c:when test="${ fn:contains(  reportItem.hierarchyNodeId ,reportParametersForm.parentNodeId ) }">
	      			<c:choose>
						<c:when test="${reportItem.activeCnt > 0 || reportItem.inactiveCnt > 0}">
			      			<%
			      				EnrollmentActivityReportValue reportValue = (EnrollmentActivityReportValue) pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "parentNodeId", reportValue.getHierarchyNodeId(  ) );
							    paramMap.put( "nodeAndBelow", 0 );
							    String urlToUse = "/reports/displayEnrollmentActivityReport.do?method=displayDetailsReport";
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
	      				EnrollmentActivityReportValue reportValue = (EnrollmentActivityReportValue) pageContext.getAttribute( "reportItem" );
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "parentNodeId", reportValue.getHierarchyNodeId(  ) );
					    paramMap.put( "nodeAndBelow", 1 );
					    String urlToUse = "/reports/displayEnrollmentActivityReport.do?method=displaySummaryReport";
					    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					%>
		            "<%=drilldownUrl %>",
	      			
	         	</c:otherwise>
	        	</c:choose>
	          "<fmt:formatNumber value="${reportItem.activeCnt}" />",
	          "<fmt:formatNumber value="${reportItem.inactiveCnt}" />",
	          "<fmt:formatNumber value="${reportItem.totalCnt}" />"
			]
      </c:forEach>
    ]
  }
],