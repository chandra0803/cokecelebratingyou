<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/include/taglib.jspf" %>
<tiles:importAttribute/>
<div id="header">
	<tiles:insert attribute='banner'/>        
	    
	<ul id="nav-meta">
		<li class="accessible"><a href="#nav"><cms:contentText key="SKIP_NAV" code="home.header"/></a></li>
		<li class="accessible"><a href="#subnav"><cms:contentText key="SKIP_SECTION" code="home.header"/></a></li>
		<li class="accessible"><a href="#main"><cms:contentText key="SKIP_CONTENT" code="home.header"/></a></li>
		<li class="contact"><a href="<%=RequestUtils.getBaseURI(request)%>/contactUs.do?method=view&isFullPage=true" onclick="selTab('home.nav.PRIVACY');"><cms:contentText key="CONTACT_US" code="home.header"/></a></li>
		<li class="help"><a href="<%=RequestUtils.getBaseURI(request)%>/faq.do" onclick="selTab('home.nav.PRIVACY');"><cms:contentText key="HELP" code="home.header"/></a></li>
		<tiles:insert attribute='userInfo'/>         
	</ul>
	
    <tiles:useAttribute name="navselected" id="navselected" classname="java.lang.String"/>
	<tiles:insert attribute="nav" >
        <c:if test="${navselected ne null and fn:length(navselected) > 0}">
		   <tiles:put name="navselected"><%=navselected%></tiles:put>
        </c:if>
    </tiles:insert>

</div><%-- /#header --%>

