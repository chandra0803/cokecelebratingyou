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
              "url": "${systemUrl}/reports/displayRecognitionReceivedByPaxReport.do?method=extractReport&exportViewType=fullView"
            }
          ],
          "exportCurrentView": {
            "label": "<cms:contentText key="EXTRACT_CURRENT" code="report.display.page"/>",
            "url": "${systemUrl}/reports/displayRecognitionReceivedByPaxReport.do?method=extractReport&exportViewType=currentView"
          },
      	  "inlineTextDescription": ""
        },
        "results": [
        <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
             <c:if test="${reportDataStatus.index != 0}">,</c:if>
          [
            "<c:out value="${reportItem.paxName}" />",
            "<c:out value="${reportItem.enrollmentDate}" />",
            "<c:out value="${reportItem.status}" />",
            "<c:out value="${reportItem.jobPosition}" />",
            "<c:out value="${reportItem.role}" />",
            "<c:out value="${reportItem.nodeName}" />" 
          ]
    	</c:forEach>
        ]
      }
    ]