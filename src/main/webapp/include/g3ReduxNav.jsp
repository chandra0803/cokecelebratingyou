<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/include/taglib.jspf" %>
<script type="text/javascript">
	function selTab(tabid)
	{
		var queryString = "tabid="+tabid;
		$.ajax({
			url: "<%=RequestUtils.getBaseURI(request)%>/selTab.do?doNotSaveToken=Y",
			data: queryString,
			success: function(data){
				
			}
		});
	}
</script>
<ul id="nav">
	<li class="accessible"><a href="#subnav"><cms:contentText key="SKIP_SECTION" code="home.nav"/></a></li>
	<li class="accessible"><a href="#main"><cms:contentText key="SKIP_CONTENT" code="home.nav"/></a></li>
    <tiles:useAttribute name="navselected" id="navselected" classname="java.lang.String"/>
    <c:if test="${navselected ne null and fn:length(navselected) > 0}">
        <c:set var="selectedTabId" value="${navselected}" scope="session"  />
    </c:if>
 	<beacon:reduxmenu/>
</ul>