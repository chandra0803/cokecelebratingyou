<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayEnrollmentActivityReport.do?method=displayDetailsReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="PAX_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PAX_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": ""
            },
            {
              "id": "2",
              "name": "<cms:contentText key="ENROLLMENT_DATE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ENROLLMENT_DATE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentText key="STATUS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="STATUS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="JOB_POSITION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="JOB_POSITION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="ROLE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ROLE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="DEPARTMENT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DEPARTMENT_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": ""
            },
            {
              "id": "8",
              "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
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
		            "${reportItem.paxName}",
		            "${reportItem.enrollmentDate}",
		            "${reportItem.status}",
		            "${reportItem.jobPosition}",
		            "${reportItem.role}",
		            "${reportItem.department}",
		            "${reportItem.nodeName}", 
		            "${reportItem.country}" 
				]
	 		</c:forEach>
        ]
      }
    ],