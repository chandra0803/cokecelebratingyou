<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.value.survey.SurveyAnalysisSummaryReportValue"%>
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
          "name": "<cms:contentText key="NODE_NAME" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="NODE_NAME_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
          "summary": ""
        },
        {
          "id": "2",
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
          "id": "3",
          "name": "<cms:contentText key="QUESTIONS_ASKED" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="QUESTIONS_ASKED_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": ""
        },
        {
          "id": "4",
          "name": "Questions drill-down URL",
          "description": "",
          "type": "URL",
          "alignment": "",
          "nameId": "",
          "sortable": false,
          "sortUrl": "",
          "summary": ""
        },
        {
          "id": "5",
          "name": "<cms:contentText key="ELIGIBLE_PAX" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="ELIGIBLE_PAX_DESC" code="${report.cmAssetCode}"/>",
          "type": "string",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.eligiblePax}"/>"
        },
        {
          "id": "6",
          "name": "<cms:contentText key="SURVEYS_TAKEN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SURVEYS_TAKEN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.surveysTaken}"/>"
        },
        {
          "id": "7",
          "name": "<cms:contentText key="SURVEYS_TAKEN_PERC" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="SURVEYS_TAKEN_PERC_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=7&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.surveysTakenPerc}"/>"
        },
        <c:set var="idValue" value="8"/>
        <c:forEach var="reportItem" items="${surveyResponseList}" varStatus="reportDataStatus">
	        {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "<c:out value="${reportItem.headerValue}" escapeXml="false"/>",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
          	  "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=<c:out value="${idValue}"/>&sortedBy=",
	          "summary": ""
	        },
	        <c:set var="idValue" value="${idValue+1 }"/>
	         {
	          "id": "<c:out value="${idValue}"/>",
	          "name": "%",
	          "description": "",
	          "type": "number",
	          "alignment": "",
	          "nameId": "",
	          "sortable": true,
          	  "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=<c:out value="${idValue}"/>&sortedBy=",
	          "summary": ""
	        }
	        <c:if test="${reportDataStatus.index <= surveyResponseListSize}">,</c:if>
	        <c:set var="idValue" value="${idValue+1 }"/>
	    </c:forEach>
        {
          "id": "100",
          "name": "<cms:contentText key="MEAN" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="MEAN_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=100&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.meanValue}"/>"
        },
        {
          "id": "101",
          "name": "<cms:contentText key="STD_DEVIATION" code="${report.cmAssetCode}"/>",
          "description": "<cms:contentText key="STD_DEVIATION_DESC" code="${report.cmAssetCode}"/>",
          "type": "number",
          "alignment": "",
          "nameId": "",
          "sortable": true,
          "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=101&sortedBy=",
          "summary": "<fmt:formatNumber value="${totalsRowData.standardDeviation}"/>"
        }
      ],
      
      <c:choose>
        <c:when test="${reportParametersForm.sortedOn != 0}">
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
        "${reportDataId.nodeName}",
        <c:choose>
    		  <c:when test="${ fn:contains( reportParametersForm.parentNodeId, reportDataId.nodeId ) }">
    		  	<c:choose>
    		  		<c:when test="${ reportDataId.surveysTaken >= surveyMinResponse }">
						 <%
					        SurveyAnalysisSummaryReportValue reportValue = (SurveyAnalysisSummaryReportValue) pageContext.getAttribute( "reportDataId" );
						    Map<String, Object> paramMap = new HashMap<String, Object>();
						    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
						    paramMap.put( "nodeName", reportValue.getNodeName(  ) );
						    String urlToUse = "/reports/displaySurveyAnalysisReport.do?method=displayDetailReport";
						    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
						%>
			            "<%=drilldownUrl %>",
    		  		</c:when>
    		  		<c:otherwise>
    		  			false,
    		  		</c:otherwise>
    		  	</c:choose>
    		  </c:when>
    		  <c:otherwise>
    		    <c:choose>
    		      <c:when test="${ reportDataId.surveysTaken >= 0 }">
    		        <%
				        SurveyAnalysisSummaryReportValue reportValue = (SurveyAnalysisSummaryReportValue) pageContext.getAttribute( "reportDataId" );
					    Map<String, Object> paramMap = new HashMap<String, Object>();
					    paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
					    paramMap.put( "nodeName", reportValue.getNodeName(  ) );
					    String urlToUse = "/reports/displaySurveyAnalysisReport.do?method=displaySummaryReport";
					    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
					%>
		            "<%=drilldownUrl %>",
    		      </c:when>
    		      <c:otherwise>
    		  		false,
    		      </c:otherwise>
    		    </c:choose>
    		  </c:otherwise>
    	 </c:choose>
    	 "<cms:contentText key="VIEW" code="report.display.page"/>",
    	 <c:choose>
	       <c:when test="${ reportDataId.surveysTaken >= surveyMinResponse  && isShowView}">
	         <%
		         SurveyAnalysisSummaryReportValue reportValue = (SurveyAnalysisSummaryReportValue) pageContext.getAttribute( "reportDataId" );
			     Map<String, Object> paramMap = new HashMap<String, Object>();
			     paramMap.put( "parentNodeId", reportValue.getNodeId(  ) );
			     paramMap.put( "nodeName", reportValue.getNodeName(  ) );
			     String urlToUse = "/reports/displaySurveyAnalysisReport.do?method=displayDetailReport";
			     String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
			 %>
			
	         "<%=drilldownUrl %>",
	       </c:when>
	       <c:otherwise>
	  		   false,
	       </c:otherwise>
    	 </c:choose>
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