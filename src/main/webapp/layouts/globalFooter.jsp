<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>
<%@page import="java.time.LocalDateTime"%>
<% pageContext.setAttribute("year",LocalDateTime.now().getYear()); %>

<script type="text/template" id="globalFooterViewTpl">
  <div id="globalFooterView">
    <ul class="nav">
        <li><a href="${pageContext.request.contextPath}/privacy.do?method=view" id="privacyPolicyFooterLink" data-page-id="privacyPageView"><cms:contentText key="PRIVACY_POLICY" code="system.general"/></a></li>
        <c:if test="${displayTermsAndConditionsFooterLink}"><li><a href="${pageContext.request.contextPath}/termsAndConditionsView.do?method=review" id="termsFooterLink" data-page-id="termsPageView"><cms:contentText key="T&C" code="system.general"/></a></li></c:if>
        <li><a href="${pageContext.request.contextPath}/contactUs.do?method=view" id="contactUsFooterLink" data-page-id="contactFormView"><cms:contentText key="CONTACT_US" code="system.general"/></a></li>
        <li class="copyright"><cms:contentTemplateText key="COPYRIGHT_TEXT" code="system.general" args="${year}" delimiter=","/></li>
    </ul>
  </div><!-- /#globalFooterView -->
</script>
