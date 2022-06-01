<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.individualactivity.IndividualActivityReportValue"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.value.claim.ClaimByPaxReportValue"%>
<%@page import="com.biperf.core.domain.report.Report"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "displayType", "productClaims" );
Report report = ( Report ) request.getAttribute( "report" );
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
          "name": "<cms:contentText key="CLAIM_NUMBER" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="CLAIM_NUMBER_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": ""
        },
        {
              "id": "2",
              "name": "<cms:contentText key="CLAIM_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
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
          "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMOTION_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
          "summary": ""
        },
        {
          "id": "4",
          "name": "<cms:contentText key="DATE_SUBMITTED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="DATE_SUBMITTED_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
          "summary": ""
        },          
        {
          "id": "5",
          "name": "<cms:contentText key="SOLD_TO" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SOLD_TO_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="STATUS" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="STATUS_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
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
          "label": "<cms:contentText key="EXTRACT_FULL" code="report.display.page"/>",
          "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=productClaims"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayIndividualActivityReport.do?method=extractReport&exportLevel=productClaims"
      },
  	  "inlineTextDescription": ""
    },
    "results": [
     <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
     	<c:if test="${reportDataStatus.index !=0 }">,</c:if>
       	[ 
          "${reportItem.claimNumber}",
           <%
						IndividualActivityReportValue reportValue = (IndividualActivityReportValue)pageContext.getAttribute( "reportItem" );
			           	Map<String, Object> urlMap = new HashMap<String, Object>(); 
			      		String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), report.getUrl( ), urlMap );
					      
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "claimId", reportValue.getClaimId(  ));
					    paramMap.put( "isFullPage", "false");
					    paramMap.put( "returnURL", returnURL );
					    paramMap.put( "open", reportValue.getClaimStatus(  ) );
					    String detailUrl = ClientStateUtils.generateEncodedLink( "", "claim/productClaimDetail.do?method=display", paramMap );
			%>
		  "${systemUrl}/<%=detailUrl%>",         	
          "${reportItem.promoName}",
          "<fmt:formatDate value="${reportItem.dateSubmitted}" pattern="${JstlDatePattern}" />",
          "<c:out value="${reportItem.soldTo}" />",
          "<c:out value="${reportItem.claimStatus}" />"
       	]
	</c:forEach>
    ]
  }
],