<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div id="ssiApproveClaimsPageView" class="page-content">

    <!-- JAVA NOTE: render the following HTML if the user has just approved a claim -->
	<c:if test="${showModal}">
	<div class="modal hide fade autoModal recognitionResponseModal">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">
				<i class="icon-close"></i>
			</button>

            <h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
			<p>${modalMessage}</p>
			</div>
		</div>
   	</c:if>

	<div class="span12 backToContestWrap">

        <a href="#" class="btn btn-link btn-icon"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK_TO_CLAIMS" code="ssi_contest.claims"/></a>

    </div>

	<div id="ssiApproveClaimsSummary">
        <script id="ssiApproveClaimsTopTpl" type="text/x-handlebars-template">
        <div class="page-topper">
            <div class="row-fluid">
                <div class="span8">
                    <h2>{{name}}</h2>
                </div>
            </div>

            {{#if canApprove}}
            <div class="row-fluid alert {{#lt 0 claimsPendingCount}}alert-info{{else}}alert-warning{{/lt}}">
                <div class="span9">
                    <input type="hidden" name="contestId" value="{{contestId}}">
                    <p><cms:contentText key="AFTER_APPROVING_CLAIMS" code="ssi_contest.claims"/></p>
                </div>
                <div class="span3">
                    <button class="btn btn-primary ssiApproveClaimsUpdate pull-right" data-contest-id="{{contestId}}"><cms:contentText key="DONE" code="ssi_contest.claims"/></button>
                </div>
            </div>
            {{/if}}
        </div>
        </script>
    </div><!-- /#ssiApproveClaimsSummary -->

    <div id="ssiApproveClaimsStats">
        <script id="ssiApproveClaimsStatsTpl" type="text/x-handlebars-template">
        <div class="row-fluid ssiSummaryDetails">
            <div class="span12">
                <ul class="stats unstyled">
                    <li class="stat claims submitted">
                        <strong class="count">{{claimsSubmittedCount}}</strong>
                        <span class="name"><cms:contentText key="CLAIMS_SUBMITTED" code="ssi_contest.claims"/></span>
                    </li>
                    <li class="stat claims pending">
                        <strong class="count">{{claimsPendingCount}}</strong>
                        <span class="name"><cms:contentText key="CLAIMS_PENDING" code="ssi_contest.claims"/></span>
                    </li>
                    <li class="stat claims approved">
                        <strong class="count">{{claimsApprovedCount}}</strong>
                        <span class="name"><cms:contentText key="CLAIMS_APPROVED" code="ssi_contest.claims"/></span>
                    </li>
                    <li class="stat claims denied">
                        <strong class="count">{{claimsDeniedCount}}</strong>
                        <span class="name"><cms:contentText key="CLAIMS_DENIED" code="ssi_contest.claims"/></span>
                    </li>
                </ul>
            </div>
        </div>
        </script>
    </div><!-- /#ssiApproveClaimsStats -->

    <div class="row-fluid filterClaimsWrapper">
        <div class="span8">
            <cms:contentText key="FILTER_CLAIMS" code="ssi_contest.claims"/>:
            <select name="claimsFilter" id="claimsFilter">
                <option value="waiting_for_approval"><cms:contentText key="WAITING_FOR_APPROVAL" code="ssi_contest.claims"/></option>
                <option value="approved"><cms:contentText key="APPROVED" code="ssi_contest.claims"/></option>
                <option value="denied"><cms:contentText key="DENIED" code="ssi_contest.claims"/></option>
                <option value="all"><cms:contentText key="ALL" code="ssi_contest.claims"/></option>
            </select>
        </div>
        <div class="span4 text-right">
            <!-- JAVA NOTE: Will need path to spreadsheet download -->
            <ul class="export-tools fr">
                <li class="export csv">
                    <a href="contestClaimsExtract.do?method=extractFinalReport&id=${id}" class="exportXlsButton">
                        <span class="btn btn-inverse btn-compact btn-export-csv">
                            <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                        </span>
                    </a>
                </li>
            </ul>
        </div>
    </div>


    <div class="awardHistoryWrapper row-fluid">
        <div class="span12">
            <div id="ssiApproveClaimsHistory" class="spinnerOverlayWrap">
                <!-- dynamic content -->
            </div>
            <div class="approveAllConfirm" style="display:none">
                <p class="text-center">
                   <b><cms:contentText key="ARE_YOU_SURE" code="ssi_contest.claims"/></b>
                </p>
                <p>
                    <cms:contentText key="WILL_APPROVE_ALL" code="ssi_contest.claims"/> <span class="pendingClaimsCount"></span> <cms:contentText key="OPEN_CLAIMS" code="ssi_contest.claims"/>.
                </p>
                <p class="tc">
                    <a class="btn btn-small confirmBtn closeTip"><cms:contentText key="YES" code="ssi_contest.preview"/></a>
                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="ssi_contest.preview"/></a>
                </p>
            </div><!-- /.approveAllConfirm -->
        </div><!-- /.span12 -->
    </div><!-- /.awardHistoryWrapper.row-fluid -->

    <div class="ssiDenyClaimPopover" style="display: none">
        <form id="ssiDenyClaim" method="post" action="approveContestClaims.do" enctype="multipart/form-data">
            <div class="control-group validateme"
                data-validate-fail-msgs='{"nonempty":"You must enter a reason for denial."}'
                data-validate-flags='nonempty'>
                <button type="button" class="close">&times;</button>
                <label class="control-label"><cms:contentText key="DENIAL_REASON" code="ssi_contest.approvals.summary"/></label>
                <div class="commentTools">
                    <cms:contentText key="REMAINING_CHARS" code="system.richtext"/>: <span class="remChars">&nbsp;</span>

                    <span class="spellchecker dropdown">
                        <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                title=<cms:contentText key="CHECK_SPELLING" code="ssi_contest.approvals.summary"/>
                                type="button"
                                data-toggle="dropdown">
                            <i class="icon-check"></i>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="check"><b><cms:contentText key="CHECK_SPELLING" code="system.richtext"/></b></a></li>
                        </ul>
                    </span>
                </div>
                <div class="controls">
                    <div class="contribCommentWrapper">
                        <textarea class="contribCommentInp" rows="4" maxlength="1000" placeholder="<cms:contentText key="ADD_A_COMMENT" code="ssi_contest.approvals.summary"/>" name="comment"></textarea>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary ssiDenyClaimSubmit" name="status" value="denied"><cms:contentText key="SUBMIT" code="system.button"/></button>
                </div>
            </div>
        </form>
    </div><!--/.ssiDenyClaimsPopover-->

    <!-- Button to trigger modal -->
    <!-- <a href="#myModal" role="button" class="btn" data-toggle="modal">Launch demo modal</a> -->



</div><!--/#ssiApproveClaimsPageView-->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        var contestJson = ${ssiContestApproveClaimsForm.initializationJson};

     	// JAVA NOTE: url for claims history pagination
        G5.props.URL_JSON_CLAIMS_APPROVAL_HISTORY = G5.props.URL_ROOT+'ssi/approveContestClaims.do?method=loadClaimsSummary';

        // JAVA NOTE: url for sending approval/denial requests
        G5.props.URL_JSON_CLAIMS_APPROVE = G5.props.URL_ROOT+'ssi/approveContestClaims.do';

        // JAVA NOTE: url for sending update requests
        G5.props.URL_JSON_CLAIMS_UPDATE = G5.props.URL_ROOT+'ssi/approveContestClaims.do';

        var ssiApproveClaimsPageView = new SSIApproveClaimsPageView({
            el:$('#ssiApproveClaimsPageView'),
            contestJson: contestJson, // contest json to populate model
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI( request )%>/homePage.do'
                }
            },
            pageTitle : 'Approve Claims'
        });
    });
</script>

<script type="text/template" id="ssiApproveClaimsSummaryTableTplTpl">
 <%@include file="ssiApproveClaimsSummaryTableTpl.jsp" %>
</script>

<!-- paginationView template -->
<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>
