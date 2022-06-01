<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.enums.ActivityType"%>
<%@ page import="com.biperf.core.value.ActivityCenterValueBean"%>

<div id="approvalsPageIndexView" class="approvalSearchWrapper page-content">
	<div id="approvalsTableWrapper">
		<div class="row-fluid">
	    	<div class="span12">
				<table id="approvalIndexTable" class="table table-striped">
					<thead>
						<tr>
							<th><cms:contentText code="promotion.approvals" key="PROMO_HEADER" /></th>
							<th><cms:contentText code="promotion.approvals" key="MESSAGE_HEADER" /></th>
							<th><cms:contentText code="promotion.approvals" key="ACTION_HEADER" /></th>
						</tr>
					</thead>
					<tbody>
						<!-- dynamic -->
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<c:if test="${showModal}">
	<div class="modal hide fade autoModal recognitionResponseModal">
		<div class="modal-header">
			<button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
			<p>${modalMessage}</p>
		</div>
	</div>
</c:if>

<script id="claimIndex-Template" type="text/x-handlebars-template">
	<tr>
		<td>{{name}}</td>
		<td>{{alertMessage}}</td>
		<td>
			<a class="btn btn-primary btn-block approvalsReviewBtn" href="{{url}}"><cms:contentText code="promotion.approvals" key="REVIEW_APPROVALS" /></a>
		</td>
	</tr>
</script>

<script id="claimIndex-NoAlertsTemplate" type="text/x-handlebars-template">
	<tr>
		<td><cms:contentText code="promotion.approvals" key="YOU_DONT_HAVE_APP" /></td>
	</tr>
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

	G5.props.URL_JSON_APPROVALS_LOAD_DATA = G5.props.URL_ROOT+'claim/approvalsCountAction.do?method=fetchApprovalsForTileAndList';

	//attach the view to an existing DOM element
    var aimlp = new ApprovalsIndexModelView({
    	el:$('#approvalsPageIndexView'),
        pageTitle : '<cms:contentText code="promotion.approvals" key="TITLE_NEW" />'
    });

});
</script>
