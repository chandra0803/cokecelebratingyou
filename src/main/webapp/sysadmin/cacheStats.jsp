<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Cache Statistics</title>
<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/designtheme/generic/default.css" type="text/css">
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js" type="text/javascript"></script>
</head>
<body class="bodystyle">
<html:form styleId="contentForm" action="/cacheStatsDisplay">
	<html:hidden property="method" value="display" />
  <b><c:out value='<%=System.getProperty("com.sun.aas.instanceName") + "-" + System.getProperty( "environment.name")%>'/></b>&nbsp;
  <br>
	<h3>Platform Cache Statistics</h3>
    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('flushPlatform')">Flush</html:submit>&nbsp;
    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('clearPlatform')">Clear</html:submit>&nbsp;
	<table>
		<tr>
			<td>Entry Added Count</td>
			<td><c:out value="${cache.entryAddedCount}" /></td>
		</tr>
		<tr>
			<td>Entry Flushed Count</td>
			<td><c:out value="${cache.entryFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Entry Removed Count</td>
			<td><c:out value="${cache.entryRemovedCount}" /></td>
		</tr>
		<tr>
			<td>Entry Updated Count</td>
			<td><c:out value="${cache.entryUpdatedCount}" /></td>
		</tr>
		<tr>
			<td>Group Flushed Count</td>
			<td><c:out value="${cache.groupFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Pattern Flushed Count</td>
			<td><c:out value="${cache.patternFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Cache Flushed Count</td>
			<td><c:out value="${cache.cacheFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Hit Count</td>
			<td><c:out value="${cache.hitCount}" /></td>
		</tr>
		<tr>
			<td>Miss Count</td>
			<td><c:out value="${cache.missCount}" /></td>
		</tr>
		<tr>
			<td>Stale Hit Count</td>
			<td><c:out value="${cache.staleHitCount}" /></td>
		</tr>
	</table>
	<h3>ContentManager Cache Statistics</h3>
    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('flushContentManager')">Flush</html:submit>&nbsp;
    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('clearContentManager')">Clear</html:submit>&nbsp;
	<table>
		<tr>
			<td>Entry Added Count</td>
			<td><c:out value="${cmsCache.entryAddedCount}" /></td>
		</tr>
		<tr>
			<td>Entry Flushed Count</td>
			<td><c:out value="${cmsCache.entryFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Entry Removed Count</td>
			<td><c:out value="${cmsCache.entryRemovedCount}" /></td>
		</tr>
		<tr>
			<td>Entry Updated Count</td>
			<td><c:out value="${cmsCache.entryUpdatedCount}" /></td>
		</tr>
		<tr>
			<td>Group Flushed Count</td>
			<td><c:out value="${cmsCache.groupFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Pattern Flushed Count</td>
			<td><c:out value="${cache.patternFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Cache Flushed Count</td>
			<td><c:out value="${cmsCache.cacheFlushedCount}" /></td>
		</tr>
		<tr>
			<td>Hit Count</td>
			<td><c:out value="${cmsCache.hitCount}" /></td>
		</tr>
		<tr>
			<td>Miss Count</td>
			<td><c:out value="${cmsCache.missCount}" /></td>
		</tr>
		<tr>
			<td>Stale Hit Count</td>
			<td><c:out value="${cmsCache.staleHitCount}" /></td>
		</tr>
	</table>
</html:form>
</body>
</html>
