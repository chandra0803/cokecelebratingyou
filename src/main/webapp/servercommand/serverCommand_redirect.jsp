<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

{
	"messages":[
		{
			"type":"serverCommand",
			"command":"redirect",
			"url":"<%=RequestUtils.getBaseURI(request)%><c:out value="${serverCommandForm.targetUrl}"/>"
		}
	]
}