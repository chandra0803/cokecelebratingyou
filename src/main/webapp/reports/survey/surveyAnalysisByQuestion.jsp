<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.survey.SurveyAnalysisByQuestionReportValue"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@ include file="/include/taglib.jspf"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
Report report = (Report)request.getAttribute( "report" );
String reportUrl = report.getUrl(  );
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
  {
    "meta": {
      "columns": [
        {
          "id": "1",
          "name": "<cms:contentText key="QUESTIONS_ASKED_ID" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUESTIONS_ASKED_ID" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
        },
        {
          "id": "2",
          "name": "<cms:contentText key="QUESTIONS_ASKED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUESTIONS_ASKED_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=2&sortedBy=",
          "summary": ""
        },
        {
          "id": "3",
          "name": "<cms:contentText key="ELIGIBLE_PAX" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ELIGIBLE_PAX_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligiblePax}"/>"
        },
        {
          "id": "4",
          "name": "<cms:contentText key="SURVEYS_TAKEN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SURVEYS_TAKEN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "<fmt:formatNumber value="${totalsRowData.surveysTaken}"/>"
        },
        {
          "id": "5",
          "name": "<cms:contentText key="SURVEYS_TAKEN_PERC" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SURVEYS_TAKEN_PERC_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "<fmt:formatNumber value="${totalsRowData.surveysTakenPerc}"/>"
        },
        <c:set var="idValue" value="6"/>
         <c:if test="${response1}">
             {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader1}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response1Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response1SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2 }"/>
         </c:if>
         <c:if test="${response2}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader2}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response2Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response2SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2 }"/>
          </c:if>
          <c:if test="${response3}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader3}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response3Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response3SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2 }"/>
          </c:if>
          <c:if test="${response4}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader4}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response4Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response4SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2 }"/>
          </c:if>
          <c:if test="${response5}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader5}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response5Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response5SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2}"/>
          </c:if>
          <c:if test="${response6}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader6}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response6Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response6SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2}"/>
          </c:if>
          <c:if test="${response7}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader7}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response7Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response7SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2}"/>
          </c:if>
          <c:if test="${response8}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader8}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response8Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response8SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2}"/>
          </c:if>
          <c:if test="${response9}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader9}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response9Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response9SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2}"/>
          </c:if>
          <c:if test="${response10}">
              {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportHeader10}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response10Selected}"/>"
	        },
	         {
	          "id": "<c:out value="${idValue+1}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": false,
	          "sortUrl": "",
	          "summary": "<fmt:formatNumber value="${totalsRowData.response10SelectedPerc}"/>"
	        },
	        <c:set var="idValue" value="${idValue+2}"/>
        </c:if>
        {
          "id": "<c:out value="${idValue}"/>",
          "name": "<cms:contentText key="MEAN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="MEAN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "<fmt:formatNumber value="${totalsRowData.meanValue}"/>"
        },
        {
          "id": "<c:out value="${idValue+1}"/>",
          "name": "<cms:contentText key="STD_DEVIATION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="STD_DEVIATION_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": "<fmt:formatNumber value="${totalsRowData.standardDeviation}"/>"
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
          "url": "${systemUrl}/reports/displaySurveyAnalysisReport.do?method=extractReport&exportViewType=fullView"
        },
        {
          "label": "<cms:contentText key="OPEN_ENDED_EXTRACT" code="${report.cmAssetCode}"/>",
          "url": "${systemUrl}/reports/displaySurveyAnalysisReport.do?method=extractSecondaryReport&exportViewType=fullView"
        }    
      ],
      "exportCurrentView": {
        "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
        "url": "${systemUrl}/reports/displaySurveyAnalysisReport.do?method=extractReport&exportViewType=currentView"
      },
      "inlineTextDescription": ""
    },
    "results": [
    <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
         <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
      [ 
        "<c:out value="${reportDataId.surveyQuestionId}" />",	         
        "<c:out value="${reportDataId.surveyQuestion}" />",	
        "<fmt:formatNumber value="${reportDataId.eligiblePax}" />",
        "<fmt:formatNumber value="${reportDataId.surveysTaken}" />",
        "<fmt:formatNumber value="${reportDataId.surveysTakenPerc}" />",
        <c:if test="${response1}">
          "<fmt:formatNumber value="${reportDataId.response1Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response1SelectedPerc}" />",
        </c:if>
        <c:if test="${response2}">
          "<fmt:formatNumber value="${reportDataId.response2Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response2SelectedPerc}" />",
        </c:if>
        <c:if test="${response3}">
          "<fmt:formatNumber value="${reportDataId.response3Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response3SelectedPerc}" />",
        </c:if>
        <c:if test="${response4}">
          "<fmt:formatNumber value="${reportDataId.response4Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response4SelectedPerc}" />",
        </c:if>
        <c:if test="${response5}">
          "<fmt:formatNumber value="${reportDataId.response5Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response5SelectedPerc}" />",
        </c:if>
        <c:if test="${response6}">
          "<fmt:formatNumber value="${reportDataId.response6Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response6SelectedPerc}" />",
        </c:if>
        <c:if test="${response7}">
          "<fmt:formatNumber value="${reportDataId.response7Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response7SelectedPerc}" />",
        </c:if>
        <c:if test="${response8}">
          "<fmt:formatNumber value="${reportDataId.response8Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response8SelectedPerc}" />",
        </c:if>
        <c:if test="${response9}">
          "<fmt:formatNumber value="${reportDataId.response9Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response9SelectedPerc}" />",
        </c:if>
        <c:if test="${response10}">
          "<fmt:formatNumber value="${reportDataId.response10Selected}" />",
          "<fmt:formatNumber value="${reportDataId.response10SelectedPerc}" />",
        </c:if>
        "<fmt:formatNumber value="${reportDataId.meanValue}" />",
        "<fmt:formatNumber value="${reportDataId.standardDeviation}" />"
      ]
      </c:forEach>
    ]
  }
],