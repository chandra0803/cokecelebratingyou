<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<h1>  
	<a href="<%=RequestUtils.getBaseURI(request)%>/homePage.do" >  
		<img src="<%= RequestUtils.getBaseURI(request) %>/assets/skins/<c:out value='${designTheme}'/>/img/<cms:contentText code="system.skin.default" key="CLIENT_LOGO" />" alt="" class="primary-logo"/>
	</a>
	
	<c:if test="${hasSecondaryLogo}">
		<img src="<%= RequestUtils.getBaseURI(request) %>/assets/skins/<c:out value='${designTheme}'/>/img/<cms:contentText code="system.skin.default" key="PROGRAM_LOGO" />" alt="" class="secondary-logo" />
	</c:if>
	
	<span class="adminHomeRed"><c:out value="${prodEnvLabel}"/></span>
</h1>
