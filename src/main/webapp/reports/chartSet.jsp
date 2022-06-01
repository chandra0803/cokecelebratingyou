<%@ include file="/include/taglib.jspf" %>

 <%-- 
<c:set var="fromDateParam" value="${reportParametersForm.reportParameters['fromDate']}"/>
<c:set var="toDateParam" value="${reportParametersForm.reportParameters['toDate']}"/>

<c:set var="promoNameParam" value="${selectedPromoName}"/> --%>
<c:set var="orgNameParam" value="${topLevelNodeName}"/>
<c:set var="awardType" value="${awardType}"/>
<c:set var="currencyCode" value="${reportParametersForm.reportParameters['currencyCode']}"/>

"chartSet": [
	<c:if test="${not hideCharts}">
	<c:forEach items="${report.charts}" var="chart" varStatus="chartStatus">			
		{
			"displayName": "<cms:contentTemplateText key="${chart.caption}" code="${report.cmAssetCode}" args="${awardType}"/>",
			"category": "<c:out value="${report.categoryType.name}"/>",
			"chartType": "<c:out value="${chart.chartType.code}"/>",
		  	"dataUrl": "<c:out value="${chart.chartDataUrl}"/>",
		  	"fusionChartsParameters": {
		  		"showBorder": "0",
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
			    "exportFileName": "<cms:contentText key="${chart.caption}" code="${report.cmAssetCode}"/>",
			    "exportatclient": "1",
			    "exportenabled": "1",
			    "exportformat": "PDF",
			    "exportShowMenuItem": "0",
			    "exportaction": "download", 
			    "exportHandler": "exportToPDF.do", 
			    "exportTargetWindow": "_blank",
				<%--"caption": "<cms:contentText key="${chart.caption}" code="${report.cmAssetCode}"/>",
				"subcaption": "<cms:contentTemplateText 
									key="${chart.subCaption}" 
									code="${report.cmAssetCode}" 
									delimiter=","
									args="${fromDateParam},${toDateParam},${orgNameParam},${promoNameParam}" />",--%>
				"decimals": "2",	
				<c:choose>
		    	  <c:when test="${chart.showLabels}">
		    	    "showLabels": "1",
		    	  </c:when>
		    	  <c:otherwise>
		    	    "showLabels": "0",
		    	  </c:otherwise>
		    	</c:choose>
		   		<c:if test="${chart.showLegend}">
		   		  "showLegend": "1",
		   		</c:if>
		   		<c:choose>
		   		  <c:when test="${chart.enableSmartLabels}">
		   		    "enableSmartLabels": "1",
		   		  </c:when>
		   		  <c:otherwise>
		   		    "enableSmartLabels": "0",
		   		  </c:otherwise>
		   		</c:choose>		   		
		   		<c:if test="${chart.showPercentValues}">
		   		  "showPercentValues": "1",
		   		  "stack100Percent": "1",
		   		</c:if>
		   		<c:choose>
		   		  <c:when test="${empty chart.xAxisLabel}">
		   		    "xAxisName": "${orgNameParam}",
		   		  </c:when>
		   		  <c:otherwise>
  	                "xAxisName": "<cms:contentText key="${chart.xAxisLabel}" code="${report.cmAssetCode}"/>",
  	              </c:otherwise>
  	            </c:choose>
		   		"yAxisName": "<c:if test="${not empty chart.yAxisLabel}"><cms:contentTemplateText key="${chart.yAxisLabel}" code="${report.cmAssetCode}" args="${awardType},${currencyCode}" delimiter=","/></c:if>"
			},
			"pdfUrl": "<c:out value="${chart.chartDataUrl}&pdf=1"/>",
			"id" : "<c:out value="${chart.id}"/>",
			"chartDisplaysTop": "<c:out value="${chart.displayLimit}"/>"
		}
		<c:if test="${chartStatus.index < fn:length(report.charts) - 1}">,</c:if>
	</c:forEach>
	</c:if>
],
"chartConfigure": <%@include file="/reports/chartCmData.jsp" %>,
"selectedChartId": "<c:if test="${selectedChartId!=null}">${selectedChartId}</c:if>",
<%--"reportDetailAsOfTimestamp": "<fmt:formatDate value="${refreshDate}" pattern="${JstlDateTimeTZPattern}" />" --%>
"reportDetailAsOfTimestamp": "${refreshDate}"
