<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div id="ssiApproveContestPageView" class="page-content">

    <div id="ssiApproveContestSummary">
        <!-- SSI Approve Contest Summary dynamically loaded -->
    </div>
    <div id="ssiApproveContestDetails" style="display:none">
        <!-- SSI Approve Contest Details dynamically loaded -->
    </div>



    <div class="ssiDenyContestPopover" style="display: none">
        <form id="ssiADenyContest" method="post" action="manageSSIContestApprovalSummary.do?method=deny" enctype="multipart/form-data">
            <div class="control-group validateme"
                data-validate-fail-msgs='{"nonempty":"<cms:contentText key="DENIAL_REASON_REQUIRED" code="ssi_contest.approvals.summary"/>"}'
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
                            <li><a class="check"><b><cms:contentText key="CHECK_SPELLING" code="system.richtext"/> </b></a></li>
                        </ul>
                    </span>
                </div>
                <div class="controls">
                    <div class="contribCommentWrapper">
                        <textarea class="contribCommentInp" rows="4" maxlength="1000" placeholder="<cms:contentText key="ADD_A_COMMENT" code="ssi_contest.approvals.summary"/>" name="comment"></textarea>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary ssiDenyContestSubmit" name="status" value="denied"><cms:contentText key="SUBMIT" code="system.button"/></button>
                </div>
            </div>
        </form>
    </div><!--/.ssiDenyContestPopover-->
</div><!--/#ssiApproveContestPageView-->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

    	// url to pull managers and participants
    	G5.props.URL_JSON_SSI_DETAILS_INVITEES_TABLE  = G5.props.URL_ROOT + 'ssi/loadSSIContestApprovalDetail.do?method=loadParticipantsAndManagers';

    	// url to pull objectives and payouts for objective contest type
    	G5.props.URL_JSON_SSI_DETAILS_OBJECTIVE_TABLE = G5.props.URL_ROOT + 'ssi/loadSSIContestApprovalDetail.do?method=loadObjectivesAndPayouts';

    	// url to pull levels for step it up contest type
    	G5.props.URL_JSON_SSI_DETAILS_LEVELS_TABLE = G5.props.URL_ROOT+'ssi/loadSSIContestApprovalDetail.do?method=loadStepItUpBaselines';

    	// url to pull ranks for stack rank contest type
    	G5.props.URL_JSON_SSI_DETAILS_RANKS_TABLE = G5.props.URL_ROOT+'ssi/loadSSIContestApprovalDetail.do?method=loadRanksAndPayouts';

    	// url to follow unfollow pax from the popup
    	G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    	// url to get the member info for the eazy recognition
    	G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";

        // Provide a contest object if the page should load with a contest
        var contestData = ${ssiContestApprovalForm.initializationJson};


        //attach the view to an existing DOM element
        var ssiNav = new SSIApproveContestPageView({
            el: $('#ssiApproveContestPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="ssi_contest.approvals.summary"/>',
            contestData: contestData
        });
    });
</script>

<script type="text/template" id="ssiApproveContestSummaryTpl">
  <%@include file="ssiApproveContestSummary.jsp" %>
</script>

<script type="text/template" id="ssiApproveContestDetailsTpl">
  <%@include file="ssiApproveContestDetails.jsp" %>
</script>

<!-- This is for participants and managers to show for different contests -->
<script type="text/template" id="ssiApproveContestInviteesTplTpl">
  <%@include file="ssiApproveContestInviteesTpl.jsp" %>
</script>

<!-- This is for objective contest type to show objectives and payouts-->
<script type="text/template" id="ssiApproveContestObjectiveTableTplTpl">
  <%@include file="ssiApproveContestObjectiveTableTpl.jsp" %>
</script>

<!-- This is for step it up contest type to show the levels -->
<script type="text/template" id="ssiApproveContestLevelsTableTplTpl">
  <%@include file="ssiApproveContestLevelsTableTpl.jsp" %>
</script>

<!-- This is for stack rank contest type to show the table data -->
<script type="text/template" id="ssiApproveContestRanksTableTplTpl">
  <%@include file="ssiApproveContestRanksTableTpl.jsp" %>
</script>

<!-- This is for pagination table  -->
<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
