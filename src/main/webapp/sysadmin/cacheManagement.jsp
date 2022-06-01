<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Cache Statistics</title>
<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/designtheme/generic/default.css" type="text/css">
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js" type="text/javascript"></script>
</head>
<body class="bodystyle">

  <b><c:out value='<%=System.getProperty("com.sun.aas.instanceName") + "-" + System.getProperty( "environment.name")%>'/></b>&nbsp;
  <br>
	<h3>Cache Statistics</h3>
	<c:forEach items="${caches}" var="cache">
		<br>
		<table>
			<tr>
				<td valign="top"><b>Cache Name:</b></td>
				<td><b><c:out value="${cache.cacheName}" /></b>
				<c:set var="cacheInstanceName" value="${ cache.cacheName }"/>
				<c:set var="divId" value="${fn:replace(cacheInstanceName,'.','_')}" />
				<html:form styleId="contentForm" action="/cacheManagementDisplay">
					<html:hidden property="method" value="display" />
					<html:hidden property="cacheName" value="${cache.cacheName }" />
					<html:submit styleClass="content-buttonstyle" onclick="setDispatchFromButton(this, 'clearCache')">Clear Cache</html:submit>&nbsp;
					<html:submit styleClass="content-buttonstyle" onclick="setDispatchFromButton(this, 'clearStatistics')">Clear Stats</html:submit>&nbsp;
					<input type="button" onClick="javascript:toggleDiv('${ divId }');" name="cache" value="Change Settings"/>
				</html:form>
				
				<div id="${ divId }" style="display: none">
					<html:form styleId="contentForm" action="/cacheManagementDisplay">
						<html:hidden property="method" value="changeSettings" />
						<html:hidden property="cacheName" value="${cache.cacheName }" />
						<table>
							<tr>
								<td>Time To Live Seconds:</td>
								<td><input size="25" name="timeToLiveSeconds" value="${cache.timeToLiveSeconds}"/></td>
							</tr>
							<tr>
								<td>Time To Idle Seconds:</td>
								<td><input size="25" name="timeToIdleSeconds" value="${cache.timeToIdleSeconds}"/></td>
							</tr>
							<tr>
								<td>Max Bytes Local Disk:</td>
								<td>${cache.maxBytesLocalDisk}</td>
							</tr>
							<tr>
								<td>Max Bytes Local Heap:</td>
								<td><input size="25" name="maxBytesLocalHeap" value="${cache.maxBytesLocalHeap}"/></td>
							</tr>
							<tr>
								<td colspan="2" align="center"><html:submit styleClass="content-buttonstyle">Save</html:submit>&nbsp;</td>
							</tr>						
						</table>
					</html:form>
				</div>
				</td>
			</tr>
			<tr>
				<td>Calc. Memory Size:</td>
				<td><fmt:formatNumber value="${cache.calculatedMemorySize}" /></td>
			</tr>
			<tr>
				<td>Calc. Disk Size:</td>
				<td><fmt:formatNumber value="${cache.calculatedOnDiskSize}" /></td>
			</tr>
			<tr>
				<td>Avg Get Time:</td>
				<td><fmt:formatNumber value="${cache.averageGetTime}" /></td>
			</tr>
			<tr>
				<td>Avg Search Time:</td>
				<td><fmt:formatNumber value="${cache.averageSearchTime}" /></td>
			</tr>
			<tr>
				<td>Memory Store Size:</td>
				<td><fmt:formatNumber value="${cache.memoryStoreSize}" /></td>
			</tr>
			<tr>
				<td>Disk Store Size:</td>
				<td><fmt:formatNumber value="${cache.diskStoreSize}" /></td>
			</tr>
			<tr>
				<td>Stats Avg Get Time: </td>
				<td><fmt:formatNumber value="${cache.statsAverageGetTime}" /></td>
			</tr>
			<tr>
				<td>Stats Avg Search Time: </td>
				<td><fmt:formatNumber value="${cache.statsAverageSearchTime}" /></td>
			</tr>
			<tr>
				<td>Cache Hits:</td>
				<td><fmt:formatNumber value="${cache.cacheHits}" /></td>
			</tr>
			<tr>
				<td>Cache Misses</td>
				<td><fmt:formatNumber value="${cache.cacheMisses}" /></td>
			</tr>
			<tr>
				<td>Disk Store Object Count:</td>
				<td><fmt:formatNumber value="${cache.diskStoreObjectCount}" /></td>
			</tr>
			<tr>
				<td>Eviction Count:</td>
				<td><fmt:formatNumber value="${cache.evictionCount}" /></td>
			</tr>
			<tr>
				<td>Object Count:</td>
				<td><fmt:formatNumber value="${cache.objectCount}" /></td>
			</tr>
			<tr>
				<td>In Memory Hits:</td>
				<td><fmt:formatNumber value="${cache.inMemoryHits}" /></td>
			</tr>
			<tr>
				<td>In Memory Misses:</td>
				<td><fmt:formatNumber value="${cache.inMemoryMisses}" /></td>
			</tr>
			<tr>
				<td>On Disk Hits:</td>
				<td><fmt:formatNumber value="${cache.onDiskHits}" /></td>
			</tr>
			<tr>
				<td>Searches Per Second:</td>
				<td><fmt:formatNumber value="${cache.searchesPerSecond}" /></td>
			</tr>
			<tr>
				<td>Statistics Accuracy:</td>
				<td><c:out value="${cache.statisticsAccuracy}" /></td>
			</tr>
			<tr>
				<td>Statistics Accuracy Description:</td>
				<td><c:out value="${cache.statisticsAccuracyDescription}" /></td>
			</tr>
			<tr>
				<td>Writer Queue Size:</td>
				<td><fmt:formatNumber value="${cache.writerQueueSize}" /></td>
			</tr>
		</table>
	</c:forEach>

<script language="JavaScript">
function setDispatchFromButton( button, dispatch )
{
	var oForm = button.form ;
	oForm.method.value=dispatch ;
}

function toggleDiv(divId) 
{		
	   $("#"+divId).toggle();
}

</script>
</body>
</html>
