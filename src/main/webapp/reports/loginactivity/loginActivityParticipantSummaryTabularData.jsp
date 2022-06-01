<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.loginreport.ReportLogonActivityListOfParticipantsValue"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf" %>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = ( Report ) request.getAttribute( "report" );
String reportUrl = report.getUrl(  );
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

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
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalCnt!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="DEPARTMENT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DEPARTMENT_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentText key="POSITION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POSITION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="PARTICIPANT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PARTICIPANT_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
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
              "id": "6",
              "name": "<cms:contentText key="LAST_LOGGED_IN" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="LAST_LOGIN_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            {
              "id": "7",
              "name": "<cms:contentText key="TOTAL_VISITS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="TOTAL_VISITS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalCnt}"/>"
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
              "url": "${systemUrl}/reports/displayLoginActivityByPaxReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayLoginActivityByPaxReport.do?method=extractReport&exportViewType=currentViewTeam"
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
		        <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
		          <c:if test="${reportDataStatus.index != 0}">,</c:if>
		          [
		            "<c:out value="${reportItem.nodeName}" escapeXml="false" />",
		            "<c:out value="${reportItem.department}" />",
		            "<c:out value="${reportItem.position}" />",
		        	"${reportItem.fullName}", 
		        	<c:choose>
		        	  <c:when test="${reportItem.totalCnt > 0 }">
		        	    <%
		        	    	ReportLogonActivityListOfParticipantsValue reportValue = (ReportLogonActivityListOfParticipantsValue) pageContext.getAttribute( "reportItem" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "userId", reportValue.getUserId(  ) );
						    String urlToUse = "/reports/displayLoginActivityByPaxReport.do?method=displayParticipantLogonActivityReport";
						    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
						%>
			            "<%=drilldownUrl %>",
		        	  </c:when>
		        	  <c:otherwise>
		        	     false,
		        	  </c:otherwise>
		        	</c:choose>
		            "<fmt:formatDate value="${reportItem.lastLoggedIn}" pattern="${JstlDatePattern}" />",
		            "<fmt:formatNumber value="${reportItem.totalCnt}" />"
		          ]
		          </c:forEach>
		   </c:otherwise>
		</c:choose>          
        ]
      }
    ],
    