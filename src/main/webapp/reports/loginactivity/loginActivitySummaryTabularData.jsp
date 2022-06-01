<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.loginreport.ReportLogonActivityTopLevelValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf" %>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
Report report = ( Report ) request.getAttribute( "report" );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
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
              "name": "<cms:contentText key="ELG_PTCPT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ELG_PTCPT_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.elgParticipantsCnt}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="LOGGED_IN_CNT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="LOGGED_IN_CNT_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.loggedInCnt}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="LOGGED_IN_PCT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="LOGGED_IN_PCT_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.loggedInPct}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="NOT_LOGGED_IN_CNT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOT_LOGGED_IN_CNT_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.notLoggedInCnt}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="NOT_LOGGED_IN_PCT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOT_LOGGED_IN_PCT_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.notLoggedInPct}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="TOTAL_VISITS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_VISITS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "total",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalVisitsCnt}"/>"
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
              "url": "${systemUrl}/reports/displayLoginActivityByOrgReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayLoginActivityByOrgReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "${reportItem.nodeName}",
          	<c:choose>
    		  <c:when test="${ fn:contains( reportParametersForm.parentNodeId, reportItem.nodeId ) }">
    		  	<c:choose>
    		  		<c:when test="${ reportItem.totalVisitsCnt > 0 }">
		    			<%
		    				ReportLogonActivityTopLevelValue reportValue = (ReportLogonActivityTopLevelValue) pageContext.getAttribute( "reportItem" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
						    paramMap.put( "nodeAndBelow", 0 );
						    String urlToUse = "/reports/displayLoginActivityByOrgReport.do?method=displayParticipantList";
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
    				ReportLogonActivityTopLevelValue reportValue = (ReportLogonActivityTopLevelValue) pageContext.getAttribute( "reportItem" );
				    Map<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
				    paramMap.put( "nodeAndBelow", 1 );
				    String urlToUse = "/reports/displayLoginActivityByOrgReport.do?method=displaySummaryReport";
				    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
				%>
	            "<%=drilldownUrl %>",
    		  </c:otherwise>
    	    </c:choose>
            "<fmt:formatNumber value="${reportItem.elgParticipantsCnt}" />",
            "<fmt:formatNumber value="${reportItem.loggedInCnt}" />",
            "<fmt:formatNumber value="${reportItem.loggedInPct}" />",
            "<fmt:formatNumber value="${reportItem.notLoggedInCnt}" />",
            "<fmt:formatNumber value="${reportItem.notLoggedInPct}" />",
            "<fmt:formatNumber value="${reportItem.totalVisitsCnt}" />"
          ]
        </c:forEach>
        ]
      }
    ],