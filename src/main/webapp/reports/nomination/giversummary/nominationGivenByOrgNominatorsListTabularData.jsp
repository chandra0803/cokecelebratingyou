<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.value.nomination.NominationReportValue"%>
<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.reports.ReportParametersForm"%>

<%
Map<String, Object> sortMap = new HashMap<String, Object>();
ReportParametersForm reportParametersForm = (ReportParametersForm) request.getSession(  ).getAttribute( ReportParametersForm.FORM_NAME );
sortMap.put( "parentNodeId", reportParametersForm.getParentNodeId(  ) );
String reportUrl = "/reports/displayNominationGivenByOrgReport.do?method=displayNomitionGiverNominatorsList";
String sortUrl = ClientStateUtils.generateEncodedLink( "", reportUrl, sortMap );
pageContext.setAttribute("sortUrl", sortUrl);
%>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="NOMINATOR" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=1&sortedBy=",
              "summary": "<c:if test="${totalsRowData!=null}"><cms:contentText key="TOTALS" code="${report.cmAssetCode}"/></c:if>"
            },
            {
              "id": "2",
              "name": "",
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
              "name": "<cms:contentText key="NOMINATOR_COUNTRY" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_COUNTRY_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=3&sortedBy=",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="NOMINATOR_ORG_NAME" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_ORG_NAME_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=4&sortedBy=",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="NOMINATOR_DEPARTMENT" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_DEPARTMENT_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=5&sortedBy=",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="NOMINATOR_JOB_POSITION" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="NOMINATOR_JOB_POSITION_DESC" code="${report.cmAssetCode}"/>",
              "type": "string",
              "alignment": "",
              "nameId": "",
              "sortable": false,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=6&sortedBy=",
              "summary": ""
            },
            <%--
            
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
            },
            --%>
            {
              "id": "8",
              "name": "<cms:contentText key="NO_OF_NOMINATIONS" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=8&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalNoOfNominations}"/>"
            },
            {
              "id": "9",
              "name": "<cms:contentText key="POINTS" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="POINTS_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=9&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalPoints}"/>"
            }
            <%--
            {
              "id": "10",
              "name": "<cms:contentText key="SWEEPSTAKES_WON" code="${report.cmAssetCode}"/>",
              "description": "<cms:contentText key="SWEEPSTAKES_WON_DESC" code="${report.cmAssetCode}"/>",
              "type": "number",
              "alignment": "",
              "nameId": "",
              "sortable": true,
              "sortUrl": "<c:out value="${sortUrl}"/>&sortedOn=10&sortedBy=",
              "summary": "<fmt:formatNumber value="${totalsRowData.totalSweepstakesWon}"/>"
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
              "url": "${systemUrl}/reports/displayNominationGivenByOrgReport.do?method=extractReport&exportViewType=fullView&extractType=primary"
            },
            {
              "label": "<cms:contentText key="EXTRACT_NOM_SUMMARY" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayNominationGivenByOrgReport.do?method=extractSecondaryReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayNominationGivenByOrgReport.do?method=extractReport&exportViewType=currentView&extractType=primary"
          },
      	  "inlineTextDescription": ""
        },
        "results": 
        [
         <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
          <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "${reportItem.nominatorFullName}",
            <c:choose>
              <c:when test="${reportItem.noOfNominations > 0 }">
                <%
               		NominationReportValue reportValue = (NominationReportValue)pageContext.getAttribute( "reportItem" );
				    Map<String, Object> paramMap = new HashMap<String, Object>();
				    paramMap.put( "paxId", reportValue.getGiverUserId(  ) );
				    //paramMap.put( "drilldownPromoId", reportValue.getPromotionId(  ) );
				    String urlToUse = "/reports/displayNominationGivenByOrgReport.do?method=displayNomitionGiverNominationsList";
				    String drilldownUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), urlToUse, paramMap );
				%>
	            "<%=drilldownUrl %>",
              </c:when>
              <c:otherwise>
                false,
              </c:otherwise>
            </c:choose>
            "${reportItem.nominatorCountry}",
            "${reportItem.nominatorOrgName}",
            "${reportItem.nominatorDept}",
            "${reportItem.nominatorJobPosition}",
            <%--"<fmt:formatNumber value="${reportItem.promotionName}" />", --%>
            "<fmt:formatNumber value="${reportItem.noOfNominations}" />",
            "<fmt:formatNumber value="${reportItem.points}" />"
            <%--
            "<c:out value="${reportItem.sweepstakesWon}" />" --%>
          ]
          </c:forEach>
        ]
      }
    ],