<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.value.individualactivity.IndividualActivityReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "displayType", "challengePoint" );
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
          "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
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
          "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",                           
          "summary": ""
        },    
        {
          "id": "3",
          "name": "<cms:contentText key="LEVEL_SELECTED" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",                           
          "summary": ""
        }, 
        {
          "id": "4",
          "name": "<cms:contentText key="BASE" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="CHALLENGEPOINT" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="ACTUAL_RESULTS" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "7",
          "name": "<cms:contentText key="PCT_OF_CHALLENGEPOINT" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "8",
          "name": "<cms:contentText key="ACHIEVED" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "9",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",                           
          "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
        },
        {
          "id": "10",
          "name": "<cms:contentText key="PLATEAU_EARNED" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",                           
          "summary": "<fmt:formatNumber value="${totalsRowData.plateauEarned}"/>"
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
          "label":  "<cms:contentText key="EXTRACT_FULL" code="report.display.page"/>",
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=challengepoint"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=challengepoint"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
          "${reportItem.orgName}",
          "<c:out value="${reportItem.promoName}" />",
          "<c:out value="${reportItem.challengePointLevelName}" />",
          "<fmt:formatNumber value="${reportItem.baseQuantity}" />",
          "<fmt:formatNumber value="${reportItem.amountToAchieve}" />",
          "<fmt:formatNumber value="${reportItem.actualResults}" />",
          "<fmt:formatNumber value="${reportItem.percentageOfChallengePoint}" />",
          "<c:out value="${reportItem.achieved}" />",
          "<fmt:formatNumber value="${reportItem.points}" />",
          "<fmt:formatNumber value="${reportItem.plateauEarned}" />"
       	]
	</c:forEach>
    ]
  }
],