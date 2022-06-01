
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== Kinesis Event Manage Page ======== -->

<div id="eventManageAdminView" class="raPageWrapper page-content">
	<div id="event-manage-admin"></div>
</div>

<script>
	<%
	pageContext.setAttribute("baseURI", RequestUtils.getBaseURI(request));
	%>
	window.eventManageAdmin = {
		baseURI: '${baseURI}',
		getEventListenerStatusUrl: 'event/status.action',
		getEventAttributesUrl: 'event/source.action',
		getEventListUrl: 'event/list.action',
		startEventListenerUrl: 'event/scheduler/start.action',
		instance: '<%=System.getProperty("com.sun.aas.instanceName")+ "-" + System.getProperty( "environment.name")%>'
	}
</script>

<script src="${siteUrlPrefix}/assets/js/manifest.js?t=@TIMESTAMP@" charset="utf-8"></script>
<script src="${siteUrlPrefix}/assets/js/app-events.js?t=@TIMESTAMP@"></script>

<script>
	$(document).ready(function() {    
	    //attach the view to an existing DOM element
		window.renderEventManageAdmin();
	});
</script>