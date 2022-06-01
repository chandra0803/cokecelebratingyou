<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== NOMINATIONS APPROVAL PROMO LIST PAGE ======== -->
<div id="nominationsApprovalPromoListPageView" class="page-content nominationsApprovalPromoPage">
	<div class="row-fluid">
        <div class="span12">
            <div class="pagination pagination-right paginationControls first"></div>

			<div class="nominationsApprovalPromoWrap" data-msg-empty="<cms:contentText key="NO_APPROVALS_FOUND" code="nomination.approvals.module" />"></div>

            <div class="pagination pagination-right paginationControls first"></div>
        </div>
    </div>
</div>
<script>
$(document).ready(function() {

    G5.props.URL_JSON_NOMINATIONS_APPROVAL_PROMO_LIST = G5.props.URL_ROOT+'/claim/nominationsApprovalPromoList.do?method=fetchPendingNominationsApprovalList';
    //attach the view to an existing DOM element
    var nlpv = new NominationsApprovalPromoListPageView({
        el:$('#nominationsApprovalPromoListPageView'),
        pageNav : {
            back : {
                text : '<cms:contentText key="BACK" code="system.button" />',
                url : 'javascript:history.go(-1);'
            },
            home : {
                text : '<cms:contentText key="HOME" code="system.general" />',
                url : '${pageContext.request.contextPath}/homePage.do'
            }
        },
        pageTitle : '<cms:contentText key="NOMINATION_PROMOTION_LIST" code="nomination.approvals.module"/>'
    });

});
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<script type="text/template" id="nominationsApprovalPromoListTplTpl">
  <%@include file="/nomination/tpl/nominationsApprovalPromoListTpl.jsp" %>
</script>
