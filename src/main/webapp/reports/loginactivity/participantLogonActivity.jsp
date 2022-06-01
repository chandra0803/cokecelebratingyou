<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf" %>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "userId", reportParametersForm.getUserId() );
String reportUrl = "/reports/displayLoginActivityByPaxReport?method=displayParticipantLogonActivityReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="PARTICIPANT" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": ""
            },
            {
              "id": "2",
              "name": "<cms:contentText key="POSITION" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentText key="DEPARTMENT" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="LOGIN_DATE" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
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
              "url": "${systemUrl}/reports/displayLoginActivityByPaxReport.do?method=extractReport&exportLevel=pax"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayLoginActivityByPaxReport.do?method=extractReport&exportLevel=pax"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
           "<c:out value="${reportItem.participant}" />",
           "<c:out value="${reportItem.position}" />",
           "<c:out value="${reportItem.department}" />",
           "<c:out value="${reportItem.country}" />",
           "<c:out value="${reportItem.organization}" escapeXml="false" />",
           "<fmt:formatDate value="${reportItem.loginDate}" pattern="${JstlDatePattern}" />"
          ]
          </c:forEach>
        ]
      }
    ],