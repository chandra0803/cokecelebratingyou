<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_CHART}" scope="request"/>

<%
response.setHeader("Cache-Control","cache, must-revalidate"); 
response.setHeader("Pragma","public"); 
%>
{
  <c:if test="${fn:length(reportData) > 0}">
  "messages": [],
  "chart": {
             "drawQuadrant": "1",
             "quadrantLabelTL": "<cms:contentText code="report.recognition.receivedbypax" key="RECOGNIZED_RECENTLY"/>",
             "quadrantLabelTR": "<cms:contentText code="report.recognition.receivedbypax" key="MOST_RECOGNIZED"/>",
             "quadrantLabelBL": "<cms:contentText code="report.recognition.receivedbypax" key="LEAST_RECOGNIZED"/>",
             "quadrantLabelBR": "<cms:contentText code="report.recognition.receivedbypax" key="NOT_RECOGNIZED_RECENTLY"/>",
             "bgImage": "${siteUrlPrefix}/assets/img/reports/heat_gradient.png",
             "bgImageDisplayMode" : "fill", 
             "showBorder":"0",
             "bgImageAlpha": "50",
             "canvasBgAlpha" : "0",
             "showAlternateHGridColor" : "0"   
           },
  "dataset":[
  {
    "anchorBgColor":"000000",
    "anchorSides":"20",
    "anchorRadius":"5",
      "data":[
  <c:forEach var="reportItem" items="${reportData}" varStatus="reportDataStatus">
      <c:if test="${reportDataStatus.index != 0}">,</c:if>
    {
        "id":"${reportItem.participantName}",
        "x":"${reportItem.recognitionCount}",
        "y":"${reportItem.daysSinceLastRec * -1 }",
        "toolText":"<cms:contentTemplateText code="report.recognition.receivedbypax" key="POPUP_SUMMARY" args="${reportItem.recognitionCount},${reportItem.daysSinceLastRec}" delimiter=","/><br /><c:forEach var="participant" items="${reportItem.participants}">${participant}<br /></c:forEach>"
      }
    
  </c:forEach>
  ]
  }
  ]
  </c:if>
}