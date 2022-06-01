<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.domain.enums.nomination.NominationsInProgressConstants"%>


<!-- ======== MY NOMINATIONS PAGE ======== -->
<div id="nominationsInprogressListPageView" class="page-content nominationsInprogressList">
    <div class="row-fluid">
        <div class="span12">
        	<div class="tableTitle"><cms:contentText key="MY_NOMINATIONS" code="nomination.inprogress"/></div>
            <p><cms:contentText key="IN_PROGRESS_COPY" code="nomination.inprogress"/></p>
        </div>
    </div>
	<div class="row-fluid">
        <div class="span12">
             <div class="pagination pagination-right paginationControls first"></div>

			<div class="nominationsTableWrap" data-msg-empty="<cms:contentText key="NO_NOMINATIONS_FOUND" code="nomination.inprogress"/>"></div>

            <div class="pagination pagination-right paginationControls first"></div>
        </div>
    </div>

	<div class="nominationRemoveConfirmDialog" style="display:none">
		<p>
			<b><cms:contentText key="ARE_YOU_SURE" code="nomination.inprogress"/></b>
		</p>
		<p class="tc">
			<button type="button" id="nominationRemoveDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="nomination.inprogress"/></button>
			<button type="button" id="nominationRemoveDialogCancel" class="btn"><cms:contentText key="NO" code="nomination.inprogress"/></button>
		</p>
	</div>
</div>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

<%pageContext.setAttribute("inProgressView", NominationsInProgressConstants.GET_INPROGRESS_NOM_CLAIM_URL );%>
<%pageContext.setAttribute("removeClaimAttr", NominationsInProgressConstants.REMOVE_INPROGRESS_NOM_CLAIM_URL );%>

$(document).ready(function() {
	G5.props.URL_JSON_NOMINATIONS_INPROGRESS_TABLE = G5.props.URL_ROOT + '${inProgressView}';
	G5.props.URL_JSON_NOMINATIONS_INPROGRESS_LIST_REMOVE = G5.props.URL_ROOT + '${removeClaimAttr}';
    //attach the view to an existing DOM element
    var nlpv = new NominationsInprogressListPageView({
        el:$('#nominationsInprogressListPageView'),
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
        pageTitle : '<cms:contentText key="MY_NOMINATIONS" code="nomination.inprogress"/>'
    });
});
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>


