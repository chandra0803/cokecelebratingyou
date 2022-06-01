<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>
<%@ page import="java.time.LocalDateTime"%>

<% pageContext.setAttribute("year",LocalDateTime.now().getYear()); %>
<div id="footer">            
	<p class="copyright"><cms:contentTemplateText key="COPYRIGHT" code="home.footer" args="${year}" delimiter=","/></p>

	<ul id="nav-final">
		<li class="accessible"><a href="#container"><cms:contentText key="RETURN_TOP" code="home.footer"/></a></li>
		<li class="accessible"><a href="#nav"><cms:contentText key="RETURN_NAV" code="home.footer"/></a></li>
		<li class="accessible"><a href="#main"><cms:contentText key="RETURN_CONTENT" code="home.footer"/></a></li>
	  <c:if test="${footerIncludePrivacyPol}"> 
		<li><a href="<%=RequestUtils.getBaseURI(request)%>/privacy.do" onclick="selTab('home.nav.PRIVACY');"><cms:contentText key="PRIVACY_POL" code="home.footer"/></a></li>
	  </c:if>
	  <c:if test="${footerIncludeTerms && footerTermsUsed}">
		<li><a href="<%=RequestUtils.getBaseURI(request)%>/termsAndConditionsReview.do?method=review" onclick="selTab('home.nav.PRIVACY');"><cms:contentText key="TERMS" code="home.footer"/></a></li>
	  </c:if>
		<li class="last"><a href="<%=RequestUtils.getBaseURI(request)%>/contactUs.do?method=view" onclick="selTab('home.nav.PRIVACY');"><cms:contentText key="CONTACT_US" code="home.footer"/></a></li>
	</ul>
</div><%-- /#footer --%>

<put name='footer' value='/include/preloginfooter.jsp' />