<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>Performance Statistics</title>
  <%-- TODO do not hardcode to springboard --%>
  <link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/designtheme/generic/default.css" type="text/css">
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js" type="text/javascript"></script>
</head>
<body class="bodystyle">
<html:form styleId="contentForm" action="/performanceStatsDisplay">
<html:hidden property="method" value="display" />
<b><c:out value='<%=System.getProperty("com.sun.aas.instanceName") + "-" + System.getProperty( "environment.name")%>'/></b>
<html:submit styleClass="content-buttonstyle" onclick="setDispatch('display')">Refresh</html:submit>&nbsp;
<html:submit styleClass="content-buttonstyle" onclick="setDispatch('reset')">Reset</html:submit>&nbsp;
<html:submit styleClass="content-buttonstyle" onclick="setDispatch('enable')">Enable</html:submit>&nbsp;
<html:submit styleClass="content-buttonstyle" onclick="setDispatch('disable')">Disable</html:submit>&nbsp;
Error Limit:&nbsp;<input type="text" name="limit" />&nbsp;
<html:submit styleClass="content-buttonstyle" onclick="setDispatch('setLimit')">Set Error Limit</html:submit>&nbsp;
<hr />
<display:table export="true" defaultsort="1" defaultorder="ascending" name="data" sort="list" pagesize="50" requestURI="performanceStatsDisplay.do">
  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
  <display:column class="crud-content" headerClass="crud-table-header-row" property="label" title="Monitor Label" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="hits" title="Hits" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="avgDuration" title="Avg ms" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="totalDuration" title="Total ms" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="stdDevDuration" title="Std Dev ms" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="minDuration" title="Min ms" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="maxDuration" title="Max ms" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="activeCount" title="Active" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="avgActiveCount" title="Avg Active" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="maxActiveCount" title="Max Active" sortable="true" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[0]" title="0-10ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[1]" title="11-20ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[2]" title="21-40ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[3]" title="41-80ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[4]" title="81-160ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[5]" title="161-320ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[6]" title="321-640ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[7]" title="641-1280ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[8]" title="1281-2560ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[9]" title="2561-5120ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[10]" title="5121-10240ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[11]" title="10241-20480ms" />
  <display:column class="crud-content" headerClass="crud-table-header-row" property="buckets[12]" title=">20480ms" />
</display:table>
</html:form>
</body>
</html>
