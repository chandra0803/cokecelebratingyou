<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "userId", reportParametersForm.getUserId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = "/reports/displayAwardsByPaxReport.do?method=displayAwardsDetailByPaxReport";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>
<c:set var="currencyCode" value="${currencyCode}"/>
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
          "summary": "<c:if test="${awardType!='other' }"><c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if></c:if>"
        },
        {
          "id": "2",
          "name": "<cms:contentText key="PARTICIPANT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PARTICIPANT_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
          "summary": ""
        },
        {
          "id": "3",
          "name": "<cms:contentText key="ORG_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
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
          "name": "<cms:contentText key="DEPARTMENT" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="DEPARTMENT_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": ""
        },
        {
          "id": "6",
          "name": "<cms:contentText key="JOB_POSITION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="JOB_POSITION_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": ""
        },
        {
          "id": "7",
          "name": "<cms:contentText key="PROMOTION_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="PROMOTION_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": ""
        }
        
        <c:choose>
        <c:when test="${awardType=='Points' }">
        ,
        {
          "id": "8",
          "name": "<cms:contentText key="POINTS_RECEIVED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="POINTS_RECEIVED_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.pointsReceivedCnt}" />"
        },
        {
          "id": "9",
          "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.sweepstakesWonCnt}" />"
        }
         ,
        {
          "id": "10",
          "name": "<cms:contentText key="ONTHESPOT_SERIAL" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ONTHESPOT_SERIAL_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.onTheSpotSerial}" />"
        }
        </c:when>
        <c:when test="${awardType=='Cash' }">
        ,
        {
          "id": "11",
          "name": "<cms:contentTemplateText key="CASH_AMOUNT" code="${report.cmAssetCode}" args ="${currencyCode}"/>",
          "description": "<cms:contentText key="CASH_AMOUNT_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=11&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.cashReceivedCnt}" />"
        }
        </c:when>
        <c:when test="${awardType=='Other' }">
        ,
        {
          "id": "12",
          "name": "<cms:contentText key="OTHER" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="OTHER_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=12&sortedBy=",
          "summary": ""
        },
        {
          "id": "13",
          "name": "<cms:contentTemplateText key="OTHER_VALUE" code="${report.cmAssetCode}" args ="${currencyCode}"/>",
          "description": "<cms:contentText key="OTHER_VALUE_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=13&sortedBy=",
          "summary": ""
        }
        
        </c:when>
        <c:when test="${awardType=='Plateau' }">
        ,
        {
          "id": "14",
          "name": "<cms:contentText key="LEVEL_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="LEVEL_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "String",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=14&sortedBy=",
          "summary": ""
        }
        
        </c:when>
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
          "url": "${systemUrl}/reports/displayAwardsByPaxReport.do?method=extractReport"
        }
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displayAwardsByPaxReport.do?method=extractReport&exportLevel=pax"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [ 
        "<fmt:formatDate value="${reportDataId.date}" pattern="${JstlDatePattern}" />",
        "<c:out value="${reportDataId.paxName}" escapeXml="false"/>",
        "<c:out value="${reportDataId.orgName}" escapeXml="false"/>",
        "<c:out value="${reportDataId.country}" escapeXml="false"/>",
        "<c:out value="${reportDataId.department}" escapeXml="false"/>",
        "<c:out value="${reportDataId.jobPosition}" escapeXml="false"/>",
        "<c:out value="${reportDataId.promotionName}"  escapeXml="false"/>"
        
        <c:choose>
        <c:when test="${awardType=='Points'}">
         ,
        "<fmt:formatNumber value="${reportDataId.pointsReceivedCnt}" />",
        "<fmt:formatNumber value="${reportDataId.sweepstakesWonCnt}" />",
        "${reportDataId.onTheSpotSerial}"
        </c:when>
        <c:when test="${awardType=='Cash'}">,
        "<fmt:formatNumber value="${reportDataId.cashReceivedCnt}" />"
        </c:when>
        <c:when test="${awardType=='Other'}">
        ,
        "<c:out value="${reportDataId.other}"  escapeXml="false"/>",
        "<c:out value="${reportDataId.otherValue}" escapeXml="false"/>"
        </c:when>
        <c:when test="${awardType=='Plateau'}">
        ,
        "${reportDataId.levelName}"
        </c:when>
        </c:choose>
      ]
      </c:forEach>
    ]
  }
],