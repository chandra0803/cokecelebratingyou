<%@page import="com.biperf.core.domain.enums.ReportChartType"%>
<%@ include file="/include/taglib.jspf" %>

{
    "messages": [],
    "chart": {
        "chartType" : "<%=ReportChartType.PIE_2D%>",
        "baseFontColor" : "000000",
        "showZeroPies" : "1"
    },
    "data": [
    	<c:if test="${chartHasValues}">
	   	  	<c:forEach var="chartValueBean" items="${chartValueBeanList}" varStatus="chartDataStatus">
	        	<c:if test="${chartDataStatus.index != 0}">,</c:if>
		        {
		        	"label": "${fn:substring(chartValueBean.label,0,15)}<c:if test="${fn:length(chartValueBean.label) > 15 }">..</c:if>",
		        	"tooltext":"${chartValueBean.label}, ${chartValueBean.value}",
		        	"value": "<fmt:formatNumber value="${chartValueBean.value}" />"
		        }
	        </c:forEach>
        </c:if>
    ],
    "chartConfigure": <%@include file="/reports/chartCmData.jsp" %>
}