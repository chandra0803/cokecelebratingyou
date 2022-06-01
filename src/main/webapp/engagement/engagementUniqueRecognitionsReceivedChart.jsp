<%@page import="com.biperf.core.domain.enums.ReportChartType"%>
<%@ include file="/include/taglib.jspf" %>

{
    "messages": [],
    "chart": {
        "yAxisName" : "<cms:contentText key="NUM_PEOPLE" code="engagement.participant"/>",
        "chartType" : "<%=ReportChartType.COLUMN_3D%>"
    },
    "data": [
   	  	<c:forEach var="chartValueBean" items="${chartValueBeanList}" varStatus="chartDataStatus">
        <c:if test="${chartDataStatus.index != 0}">,</c:if>
        {
        	"label": "${chartValueBean.label}",
        	"value": "<fmt:formatNumber value="${chartValueBean.value}" />"
        }
        </c:forEach>
    ],
    "chartConfigure": <%@include file="/reports/chartCmData.jsp" %>
}