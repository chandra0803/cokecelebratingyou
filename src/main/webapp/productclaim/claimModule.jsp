<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="claimModuleTpl">
<!-- ======== CLAIM MODULE ======== -->
<div class="module-liner">
    <div class="module-content">

        <h3 class="module-title"><cms:contentText key="CLAIMS" code="claims.submission"/></h3>

        <div class="module-actions">
            <a href="${pageContext.request.contextPath}/claim/startClaimSubmission.do?method=newClaim" class="btn btn-primary btn-custom"><cms:contentText key="SUBMIT_CLAIM" code="claims.submission"/></a>
        </div>

        <div class="claimsWrap"><ul class="unstyled"></ul></div> <!-- holds claimModuleItem -->

    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>

<script type="text/template" id="claimModuleItemTpl">
	<%@include file="claimModuleItem.jsp" %>
</script>
