<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.individualactivity.IndividualActivityReportValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.domain.report.Report"%>

<%
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="ACTIVITY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ACTIVITY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "Individual Activity Detail URL",
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
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=3&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.points}"/>"
            },
            {
              "id": "4",
              "name": "<cms:contentText key="PLATEAU_EARNED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PLATEAU_EARNED_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=4&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.plateauEarned}"/>"
            },
            {
              "id": "5",
              "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=5&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWon}"/>"
            },
            {
              "id": "6",
              "name": "<cms:contentText key="OTHER_AWARDS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="OTHER_AWARDS_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=6&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.otherAward}"/>"
            },
            {
              "id": "7",
              "name": "<cms:contentText key="RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=7&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.received}"/>"
            },
            {
              "id": "8",
              "name": "<cms:contentText key="SENT" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "${report.url}&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.sent}"/>"
            },
            {
              "id": "9",
              "name": "",
              "description": "",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            },
            {
              "id": "10",
              "name": "Individual Activity Detail URL",
              "description": "",
              "type": "URL",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "",
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
              "label":  "<cms:contentText key="EXTRACT_FULL" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=summary"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=summary"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        	<c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
			 	[
			 	    <c:choose>
				    	<c:when test="${reportItem.viewAllAwards == null }">
							<c:choose>
						    	<c:when test="${reportItem.moduleName != null && ( reportItem.moduleName eq 'Badge' || reportItem.moduleName eq 'SSI Contest' ) && reportItem.points > 0}">
							    	"${reportItem.moduleName}",
							 		<%
							 			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
									    Map<String, Object> paramMap = new HashMap<String, Object>();
									    paramMap.put( "displayType", reportValue.getModuleAssetCode(  ) );
									    paramMap.put( "paxId", reportParametersForm.getPaxId(  ) );
									    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayDrilldownReport";
									    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
									%>
						            "<%=drilldownUrl %>",
						 		</c:when>
						 		<c:when test="${reportItem.moduleName != null && reportItem.moduleName ne 'Badge' && ( (reportItem.received > 0 || reportItem.sent > 0) )}">
							    	"${reportItem.moduleName}",
							 		<%
							 			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
									    Map<String, Object> paramMap = new HashMap<String, Object>();
									    paramMap.put( "displayType", reportValue.getModuleAssetCode(  ) );
									    paramMap.put( "paxId", reportParametersForm.getPaxId(  ) );
									    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayDrilldownReport";
									    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
									%>
						            "<%=drilldownUrl %>",
						 		</c:when>
						 		<c:otherwise>
							 		"${reportItem.moduleName}",
							    	false,
						    	</c:otherwise>
					    	</c:choose> 			 		
					    	"<c:choose><c:when test="${empty reportItem.points}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise><fmt:formatNumber value="${reportItem.points}" /></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.plateauEarned}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise><fmt:formatNumber value="${reportItem.plateauEarned}" /></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.sweepstakesWon}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise><fmt:formatNumber value="${reportItem.sweepstakesWon}" /></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.otherAward}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise><fmt:formatNumber value="${reportItem.otherAward}" /></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.received}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise><fmt:formatNumber value="${reportItem.received}" /></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.sent}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise><fmt:formatNumber value="${reportItem.sent}" /></c:otherwise></c:choose>",
					    	"${reportItem.viewAllAwards}",
					    	false
				    	</c:when>
				    	<c:otherwise>
					    	<c:choose>
					    	   <c:when test="${reportItem.moduleName != null && reportItem.moduleName eq 'Badge' && reportItem.points > 0}">
							    	"${reportItem.moduleName}",
							 		<%
							 			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
									    Map<String, Object> paramMap = new HashMap<String, Object>();
									    paramMap.put( "displayType", reportValue.getModuleAssetCode(  ) );
									    paramMap.put( "paxId", reportParametersForm.getPaxId(  ) );
									    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayDrilldownReport";
									    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
									%>
						            "<%=drilldownUrl %>",
						 		</c:when>
						    	<c:when test="${reportItem.moduleName != null && reportItem.moduleName ne 'Badge' && (reportItem.received > 0 || reportItem.sent > 0)}">
							    	"${reportItem.moduleName}",
							 		<%
							 			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
									    Map<String, Object> paramMap = new HashMap<String, Object>();
									    paramMap.put( "displayType", reportValue.getModuleAssetCode(  ) );
									    paramMap.put( "paxId", reportParametersForm.getPaxId(  ) );
									    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayDrilldownReport";
									    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
									%>
						            "<%=drilldownUrl %>",
						 		</c:when>
						 		<c:otherwise>
							 		"${reportItem.moduleName}",
							    	false,
						    	</c:otherwise>
					    	</c:choose> 				 		
					    	"<c:choose><c:when test="${empty reportItem.points}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.plateauEarned}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.sweepstakesWon}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.otherAward}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.received}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise></c:otherwise></c:choose>",
					    	"<c:choose><c:when test="${empty reportItem.sent}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:when><c:otherwise></c:otherwise></c:choose>",
					    	"${reportItem.viewAllAwards}",
					    	<%
					 			IndividualActivityReportValue reportValue = (IndividualActivityReportValue) pageContext.getAttribute( "reportItem" );
							    Map<String, Object> paramMap = new HashMap<String, Object>();
							    paramMap.put( "paxId", reportParametersForm.getPaxId(  ) );
							    String urlToUse = "/reports/displayIndividualActivityReport.do?method=displayAwardsReceivedSummaryReport";
							    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
							%>
				            "<%=drilldownUrl %>"
				    	</c:otherwise>
			    	</c:choose>  		            		            
				]
				<c:if test="${reportDataStatus.index < fn:length(reportData) - 1}">,</c:if>
			</c:forEach>             
        ]
      }
    ],