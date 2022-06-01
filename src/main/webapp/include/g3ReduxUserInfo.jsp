<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>

<li class="account">
	<cms:contentText key="WELCOME" code="home.header"/>&nbsp;<%= UserManager.getUserFullName() %>
    	<a href="<%=RequestUtils.getBaseURI(request)%>/logout.do" class="logout"><cms:contentText key="LOG_OUT" code="home.header"/></a>
</li>