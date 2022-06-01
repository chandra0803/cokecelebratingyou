<%@page import="com.biperf.core.domain.enums.ReportChartType"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>

{
    "messages": [],
    "chart": {
    <c:set var="userLocale" value="<%=UserManager.getUserLocale()%>"/>
    <c:choose> 
    <c:when test="${timeframe eq 'month'}">
    "caption" : "<cms:contentText key="TOTAL_VISITS_BY_DAY" code="engagement.participant"/>",
    </c:when>
    <c:when test="${timeframe eq 'quarter'}">
    "caption" : "<cms:contentText key="TOTAL_VISITS_BY_MONTHS" code="engagement.participant"/>",
    </c:when>
    <c:otherwise>
    "caption" : "<cms:contentText key="TOTAL_VISITS_BY_YEAR" code="engagement.participant"/>",
    </c:otherwise>
    </c:choose>  
    <c:forEach var="localeValue" items="${commaSuported}">
       <c:if test="${userLocale eq localeValue.value}">
        "decimalSeparator" : ",",
       </c:if>
    </c:forEach>
        "chartType" : "<%=ReportChartType.MS_LINE_2D %>",
        "labelDisplay" : "Rotate",
        "slantLabels" : "1",
        "showLegend" : "0"
    },
    
    "categories": [
        {
          "category": [
	    	  <c:forEach var="chartValueBean" items="${chartValueBeanList}" varStatus="chartDataStatus">
	          <c:if test="${chartDataStatus.index != 0}">,</c:if>
	          {
	          	"label": "${chartValueBean.label}"
	          }
	          </c:forEach>
	       ]
    	}
    ],
      
    "dataset": [
        {
            "seriesname": "<cms:contentText key="TEAM" code="engagement.participant"/>",
            "data": [
		    	  <c:forEach var="chartValueBean" items="${chartValueBeanList}" varStatus="chartDataStatus">
		          <c:if test="${chartDataStatus.index != 0}">,</c:if>
		          {
		          	"value": "<fmt:formatNumber value="${chartValueBean.value}" />"
		          }
		          </c:forEach>
            ]
        }
    ],
    "chartConfigure": <%@include file="/reports/chartCmData.jsp" %>
}