<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.objectpartners.cms.util.CmsResourceBundle"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>

<%@page import="com.biperf.core.service.reports.ReportsService"%>
<%@page import="com.biperf.core.utils.BeanLocator"%>
<%@page import="java.text.MessageFormat" %>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>

<%@ include file="/include/taglib.jspf"%>

<c:forEach items="${reportData.reportDashboardItems}" var="reportDashboardItem" varStatus="status">
	<c:if test="${reportDashboardItem!=null}">
		<c:if test="${ status.count != 1 }">
	      ,
	    </c:if>
		{
		   "id" : "${reportDashboardItem.id}",
		   <c:forEach items="${searchCriteria}" var="searchItem" varStatus="counter">
		   <c:if test="${counter.index==status.index}">
		   "favoriteParameters" : "${searchItem}",
		   </c:if>
		   </c:forEach>
		   <c:set var="reportUrl" value="${reportDashboardItem.reportChart.report.url}"/>
		   <c:set var="dashboardItemId" value="${reportDashboardItem.id}"/>
		   <c:set var="reportChart" value="${reportDashboardItem.reportChart}"/>
		   <c:set var="assetCode" value="${reportDashboardItem.reportChart.report.cmAssetCode}"/>
		   <c:set var="caption" value="${reportDashboardItem.reportChart.caption}"/>
		   <c:set var="reportId" value="${reportDashboardItem.reportChart.report.id}"/>
		   <%
			   Map<String, Object> reportUrlMap = new HashMap<String, Object>();
		   	   reportUrlMap.put( "dashboardItemId", (Long) pageContext.getAttribute( "dashboardItemId" ) );
			   String reportDataUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), (String) pageContext.getAttribute( "reportUrl" ), reportUrlMap );
			   pageContext.setAttribute("reportDataUrl", reportDataUrl);
		   %>
  		   "reportUrl" : "<c:out value="${reportDataUrl}"/>",
  		   <%
  		   String displayName =null;
  		   String awardType =null;
  		   
  		   String content = (String)pageContext.getAttribute( "assetCode" )+"."+(String)pageContext.getAttribute( "caption" );
  		   
  		   if(content.equals("report.awards.participant.TOTAL_POINTS_PAX_CHART_CAPTION")||content.equals("report.awards.organization.TOTAL_POINTS_CHART_CAPTION"))
  		   {
  		  	
  		  	
  		  	awardType = ( (ReportsService)BeanLocator.getBean( ReportsService.BEAN_NAME ) ).getAwardType((Long) pageContext.getAttribute( "dashboardItemId" ) , (Long) pageContext.getAttribute( "reportId" ) );
  		    displayName= MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( content ), new Object[] {awardType  } );
  		   }
  		   else
  		   {
  		     displayName = CmsResourceBundle.getCmsBundle().getString( content );
  		   }
  		   pageContext.setAttribute("displayName", displayName);
  		   pageContext.setAttribute("awardType", awardType);
  		   %>
		   "displayName" : "<c:out value="${displayName}"/>",
		   "category" : "${reportDashboardItem.reportChart.report.categoryType.name}",
		   "chartType" : "${reportDashboardItem.reportChart.chartType.code}",
		   <c:set var="chartUrl" value="${reportDashboardItem.reportChart.chartDataUrl}"/>
		   <%
			   Map<String, Object> chartUrlParamMap = new HashMap<String, Object>();
		   	   chartUrlParamMap.put( "dashboardItemId", (Long) pageContext.getAttribute( "dashboardItemId" ) );
			   String chartDataUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), (String) pageContext.getAttribute( "chartUrl" ), chartUrlParamMap );
			   pageContext.setAttribute("chartDataUrl", chartDataUrl);
		   %>
		   "dataUrl" : "<c:out value="${chartDataUrl}"/>",
		   "parentId" : "${reportDashboardItem.reportChart.report.id}",
		   "fusionChartsParameters": {
		   		"formatnumberscale":"0",
		   		"inthousandseparator":",",
                "indecimalseparator":".",
                "thousandseparator":",",
                "decimalseparator":".",
		        "legendBgColor": "FFFFFF",
		        "canvasBgColor": "E8E8E8",
			    "canvasBgAlpha": "60",
			    "bgColor": "FFFFFF",
			    "bgAlpha": "60",
			    "showBorder": "0",
		        "decimals": "2",
				<c:if test="${ not empty reportDashboardItem.reportChart.xAxisLabel }">
				  "xAxisName" : "<cms:contentText key="${reportDashboardItem.reportChart.xAxisLabel}" code="${reportDashboardItem.reportChart.report.cmAssetCode}"/>",
				</c:if>
		      	<c:if test="${ not empty reportDashboardItem.reportChart.yAxisLabel }">
		      	  "yAxisName" : "<cms:contentTemplateText key="${reportDashboardItem.reportChart.yAxisLabel }" code="${reportDashboardItem.reportChart.report.cmAssetCode}" args="${awardType}"/>",
		      	</c:if>
		      	<c:if test="${reportDashboardItem.reportChart.showPercentValues}">
		   		  "showPercentValues": "1",
		   		  "stack100Percent": "1",
		   		</c:if>
		   		<c:choose>
		   		  <c:when test="${reportDashboardItem.reportChart.showLabels}">
		   		    "showLabels" : "1",
		   		  </c:when>
		   		  <c:otherwise>
		   		    "showLabels" : "0",
		   		  </c:otherwise>
		   		</c:choose>
		      	<c:if test="${reportDashboardItem.reportChart.showLegend}">
		   		  "showLegend": "1",
		   		</c:if>
		      	<c:choose>
		      	  <c:when test="${reportDashboardItem.reportChart.enableSmartLabels}">
		      	    "enablesmartlabels": "1"
		      	  </c:when>
		      	  <c:otherwise>
		      	    "enablesmartlabels": "0"
		      	  </c:otherwise>
		      	</c:choose>
            },
            "chartConfigure": <%@include file="/reports/chartCmData.jsp" %>,
		    "pdfUrl" : "<c:out value="${chartDataUrl}"/>&pdf=1"
		}
	</c:if>
</c:forEach>	