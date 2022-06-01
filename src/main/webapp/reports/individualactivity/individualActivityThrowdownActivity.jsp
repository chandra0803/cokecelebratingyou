<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.individualactivity.IndividualActivityReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "displayType", "throwdown" );
String reportUrl = "/reports/displayIndividualActivityReport.do?method=displayDrilldownReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
        },
        {
          "id": "2",
          "name": "Individual Activity - Throwdown Drilldown URL",
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
          "name": "<cms:contentText key="WIN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="WIN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.winsCnt}"/>"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="LOSS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="LOSS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.lossCnt}"/>"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="TIE" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="TIE_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.tiesCnt}"/>"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="ACTUAL_RESULTS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ACTUAL_RESULTS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.activityCnt}"/>"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="RANKING" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="RANKING_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": ""
        },
        {
          "id": "8",
          "name": "<cms:contentText key="POINTS_EARNED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_EARNED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
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
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=throwdown"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=throwdown"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
          "<c:out value="${reportItem.promoName}" />",
          <%
    			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
			    Map<String, Object> paramMap = new HashMap<String, Object>();
			    paramMap.put( "drilldownPromoId", reportValue.getPromoId(  ) );
			    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayThrowdownDetailReport";
			    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
			%>
           "<%=drilldownUrl %>",
		  "<fmt:formatNumber value="${reportItem.winsCnt}" />",
	      "<fmt:formatNumber value="${reportItem.lossCnt}" />",
	      "<fmt:formatNumber value="${reportItem.tiesCnt}" />",
	      "<fmt:formatNumber value="${reportItem.activityCnt}" />",
	      "<c:out value="${reportItem.rank}" />",
	      "<fmt:formatNumber value="${reportItem.points}" />"	              	
	    ]
	</c:forEach>
    ]
  }
],