<%@ include file="/include/taglib.jspf" %>

"tabularData": [
      {
        "meta": {
          "columns": [
            {
              "id": "1",
              "name": "<cms:contentText key="PAX_NAME" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            },
            {
              "id": "2",
              "name": "<cms:contentText key="ENROLLMENT_DATE" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            },
            {
              "id": "3",
              "name": "<cms:contentText key="STATUS" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            },
            {
              "id": "4",
              "name": "<cms:contentText key="JOB_POSITION" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            },
            {
              "id": "5",
              "name": "<cms:contentText key="ROLE" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
              "sortable": false,
              "sortUrl": "",
              "summary": ""
            },
            {
              "id": "6",
              "name": "<cms:contentText key="NODE_NAME" code="${report.cmAssetCode}"/>",
              "description": "",
              "type": "string",
              "alignment": "",
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
              "label": "<cms:contentText key="EXTRACT_FULL" code="report.display.page"/>",
              "url": "${systemUrl}/reports/displayRecognitionGivenByPaxReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayRecognitionGivenByPaxReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        <c:forEach var="reportDataId" items="${reportData}" varStatus="reportDataIndexId">
             <c:if test="${reportDataIndexId.index !=0 }">,</c:if>
          [
            "<c:out value="${reportDataId.paxName}" />",
            "<c:out value="${reportDataId.enrollmentDate}" />",
            "<c:out value="${reportDataId.status}" />",
            "<c:out value="${reportDataId.jobPosition}" />",
            "<c:out value="${reportDataId.role}" />",
            "<c:out value="${reportDataId.nodeName}" />" 
          ]
          </c:forEach>
        ]
      }
    ]