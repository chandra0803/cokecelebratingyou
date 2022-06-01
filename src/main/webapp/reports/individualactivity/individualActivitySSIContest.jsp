<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.value.individualactivity.IndividualActivityReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.domain.report.Report"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "displayType", "ssi_contest" );
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
          "name": "<cms:contentText key="SSI_CONTEST_NAME" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "2",
          "name": "<cms:contentText key="SSI_CONTEST_DETAILS" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "URL",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "",
          "target": "_sheet"
        },
        {
          "id": "3",
          "name": "<cms:contentText key="DATE" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",                           
          "summary": ""
        },    
        {
          "id": "4",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="OTHER" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",                           
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="OTHER_VALUE" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",                           
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
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=ssicontests_sec"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=ssicontests_sec"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
          "<c:out value="${reportItem.promotionName}" />",
          <%
           	IndividualActivityReportValue reportValue = (IndividualActivityReportValue)pageContext.getAttribute( "reportItem" );
           	Map<String, Object> urlMap = new HashMap<String, Object>();
      		String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), reportUrl, urlMap );
		      
    		String detailUrl = "";
    		String isAtnContest = reportValue.getIsAtnContest();  
		    if( "N".equals( isAtnContest ) )
		    {
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    paramMap.put( "userId", reportValue.getUserId());
		    paramMap.put( "contestId", reportValue.getContestId() );
		    paramMap.put( "isReportDrillDown", "true" );
		    paramMap.put( "isParticipantDrillDown", "true" );
		    detailUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/ssi/participantContestListRedirect.do?method=display", paramMap, false, "id" );
		    }
		  %>
		  "<%=detailUrl%>",
          "<fmt:formatDate value="${reportItem.mediaDate}" pattern="${JstlDatePattern}" />",
          "<fmt:formatNumber value="${reportItem.points}" />",
          "<fmt:formatNumber value="${reportItem.other}" />",
          "<c:out value="${reportItem.otherValue}" />"
       	]
	</c:forEach>
    ]
  }
],