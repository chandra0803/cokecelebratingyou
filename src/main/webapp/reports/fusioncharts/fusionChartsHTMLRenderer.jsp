<%@ include file="/include/taglib.jspf"%>
<%
/*
 * Works with all jdk versions >=1.4.
 * Creates the object tag required to embed a chart.
 * Generates the object tag to embed the swf directly into the html page.<br>
 * Note: Only one of the parameters strURL or strXML has to be not null for this<br>
 * method to work. If both the parameters are provided then strURL is used for further processing.<br>
 *  
 * @param chartSWF -
 *                SWF File Name (and Path) of the chart which you intend
 *                to plot
 * @param strURL -
 *                If you intend to use dataURL method for this chart,
 *                pass the URL as this parameter. Else, set it to "" (in
 *                case of dataXML method)
 * @param strXML -
 *                If you intend to use dataXML method for this chart,
 *                pass the XML data as this parameter. Else, set it to ""
 *                (in case of dataURL method)
 * @param chartId -
 *                Id for the chart, using which it will be recognized in
 *                the HTML page. Each chart on the page needs to have a
 *                unique Id.
 * @param chartWidth -
 *                Intended width for the chart (in pixels)
 * @param chartHeight -
 *                Intended height for the chart (in pixels)
 * @param debugMode -
 *                Whether to start the chart in debug mode (Nt used in Free version)
 */

%>
<%
	String chartSWF= request.getParameter("chartSWF"); 
    //String strQueryString = request.getParameter("queryStringForXML");
	String strURL= (String)request.getAttribute("urlForFusionChart");
	if(strURL == null || strURL.trim().length() == 0)
	{
		strURL= (String)request.getParameter("urlForFusionChart");
	}
	String strXML= request.getParameter("strXML");
	String chartId= (String)request.getAttribute("chartId");
	if(chartId == null )
	{
		chartId = request.getParameter("chartId");
	}
	String chartWidthStr= request.getParameter("chartWidth");
	String chartHeightStr= request.getParameter("chartHeight");
	String debugModeStr= request.getParameter("debugMode"); // not used in Free version
	String divName= (String)request.getAttribute("renderDivName");
	
	int chartWidth= 0;
	int chartHeight=0;
	boolean debugMode=false;
	boolean registerWithJS=false;
	int debugModeInt =0;
	

	if(null!=chartWidthStr && !chartWidthStr.equals("")){
		chartWidth = Integer.parseInt(chartWidthStr);
	}
	if(null!=chartHeightStr && !chartHeightStr.equals("")){
		chartHeight = Integer.parseInt(chartHeightStr);
	}
	if(null!=debugModeStr && !debugModeStr.equals("")){
		debugMode = Boolean.getBoolean(debugModeStr);
		debugModeInt= debugMode?1:0;
	}
	
	
	String strFlashVars="";
	
	if (strXML.equals("")) {
	    // DataURL Mode
	    strFlashVars = "chartWidth=" + chartWidth + "&chartHeight="
	    + chartHeight + "&debugMode=" + debugModeInt
	    + "&dataURL=" + strURL + "";
	} else {
	    // DataXML Mode
	    strFlashVars = "chartWidth=" + chartWidth + "&chartHeight="
	    + chartHeight + "&debugMode=" + debugModeInt
	    + "&dataXML=" + strXML + "";
	}
%>
<script>
var myChart = new FusionCharts("<%=chartSWF%>", "<%=chartId%>", "<%= chartWidth%>", "<%= chartHeight%>", "0", "1");
myChart.setJSONUrl("<%=strURL %>");
myChart.setTransparent(true);
<% if(divName != null && divName.length() > 0 ) { %>
myChart.render("<%=divName%>");
<% } else {%>
myChart.render("fusionchartLayer");
<% } %>
</script>
 
			<%--END Code Block for Chart <%=chartId%> --%>
<%!
    /**
     * Converts a Boolean value to int value<br>
     * 
     * @param bool Boolean value which needs to be converted to int value 
     * @return int value correspoding to the boolean : 1 for true and 0 for false
     */
   public int boolToNum(Boolean bool) {
	int num = 0;
	if (bool.booleanValue()) {
	    num = 1;
	}
	return num;
    }
%>