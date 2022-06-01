<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.value.recognitionreceivedbypax.RecognitionSummaryReceivedByParticipantValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
//Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayRecognitionReceivedByPaxReport.do?method=displayActivityDetailReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
             {
              "id": "1",
              "name": "<cms:contentText key="DATE" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="DATE_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "<cms:contentText key="RECOGNITION_DETAIL_PAGE" code="${report.cmAssetCode}"/>",
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
              "name": "<cms:contentText key="RECIPIENT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="RECIPIENT_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
             {
              "id": "4",
              "name": "<cms:contentText key="COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
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
              "name": "<cms:contentText key="JOB_POSITION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="JOB_POSITION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
              "summary": ""
            },
            {
              "id": "8",
              "name": "<cms:contentText key="GIVER" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="GIVER_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": ""
            },
            {
              "id": "9",
              "name": "<cms:contentText key="PROMOTION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PROMOTION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": ""
            },
            <%--
            {
              "id": "9",
              "name": "<cms:contentText key="NUMBER_OF_RECOGNITIONS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NUMBER_OF_RECOGNITIONS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "total",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.recognitionsCnt}"/>"
            },
            --%>
            {
              "id": "10",
              "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "total",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.pointsIssued}"/>"
            },
            {
              "id": "11",
              "name": "<cms:contentText key="PLATEAU_EARNED" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="PLATEAU_EARNED_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.plateauEarnedCnt}"/>"
            }
            <%--
            {
              "id": "12",
              "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWonCnt}"/>"
            }
            --%>
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
              "url": "${systemUrl}/reports/displayRecognitionReceivedByPaxReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayRecognitionReceivedByPaxReport.do?method=extractReport&exportViewType=currentView&exportLevel=pax"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
           "<fmt:formatDate value="${reportItem.dateApproved}" pattern="${JstlDatePattern}" />",
            
            <%
           		RecognitionSummaryReceivedByParticipantValue reportValue = (RecognitionSummaryReceivedByParticipantValue)pageContext.getAttribute( "reportItem" );
            	Map<String, Object> urlMap = new HashMap<String, Object>();
       			String returnURL = ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), reportUrl, urlMap );
			      
			    Map<String, Object> paramMap = new HashMap<String, Object>();
			    paramMap.put( "promotionTypeCode", "recognition" );
			    paramMap.put( "mode", "received" );
			    paramMap.put( "claimId", reportValue.getClaimId(  ));
			    paramMap.put( "isFullPage", "false");
			    paramMap.put( "returnURL", returnURL );
			    String detailUrl = ClientStateUtils.generateEncodedLink( "", "claim/claimDetail.do", paramMap );
			%>
			"${systemUrl}/<%=detailUrl%>",
			"${reportItem.receiver}",
			"${reportItem.country}",
            "${reportItem.orgName}",
            "${reportItem.department}",
            "${reportItem.title}",
            "${reportItem.giver}",
            "${reportItem.promotion}",
            "<fmt:formatNumber value="${reportItem.pointsIssued}" />",
            "<fmt:formatNumber value="${reportItem.plateauEarnedCnt}" />"
          ]
          </c:forEach>
        ]
      }
    ],