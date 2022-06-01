<%@ include file="/include/taglib.jspf" %>
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
sortMap.put( "displayType", "nominationsReceived" );
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
          "name": "<cms:contentText key="PROMOTIONS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMOTIONS_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": ""
        },
        {
          "id": "2",
          "name": "<cms:contentText key="DATE_APPROVED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="DATE_APPROVED_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
          "summary": ""
        },
        {
          "id": "3",
          "name": "<cms:contentText key="NOMINATION_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
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
          "id": "4",
          "name": "<cms:contentText key="NOMINATOR" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="NOMINATOR_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
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
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "",
          "type": "number",
          "alignment": "",
          "nameId": "",
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
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=nominationsReceived"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=nominationsReceived"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
           "${reportItem.promoName}",
           "<fmt:formatDate value="${reportItem.dateApproved}" pattern="${JstlDatePattern}" />",
           <c:choose>
	           	<c:when test="${reportItem.sweepstakesWon > 0}">
	           		false,
	           	</c:when>
	           	<c:otherwise>
	           		<%
			          	IndividualActivityReportValue reportValue = (IndividualActivityReportValue)pageContext.getAttribute( "reportItem" );
			          	Report report = (Report)request.getAttribute( "report" );
			          	Map<String, Object> urlMap = new HashMap<String, Object>();
			     		String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), report.getUrl( ), urlMap );
				      
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "promotionTypeCode", "nomination" );
					    paramMap.put( "mode", "received" );
					    paramMap.put( "claimId", reportValue.getClaimId(  ));
					    paramMap.put( "isFullPage", "false");
					    paramMap.put( "returnURL", returnURL );
					    String detailUrl = ClientStateUtils.generateEncodedLink( "", "claim/claimDetail.do", paramMap );
					  %>
					"${systemUrl}/<%=detailUrl%>",
	           	</c:otherwise>
           </c:choose>
          "<c:out value="${reportItem.nominatorName}" />",
          "<fmt:formatNumber value="${reportItem.points}" />",
          "<fmt:formatNumber value="${reportItem.sweepstakesWon}" />"
       	]
	</c:forEach>
    ]
  }
],