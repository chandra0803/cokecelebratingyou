<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<div id="ssiApprovePayout" class="page-content ssiApprovePayoutTablePage">
    <div id="ssiApprovePayoutDynamicContent"></div>

    <div class="promptContent" style="display:none">
        <p><cms:contentText key="APPROVE_PAYOUTS_WARN" code="ssi_contest.creator" /></p>
        <p><cms:contentText key="APPROVE_PAYOUTS_CONFIRM" code="ssi_contest.creator" /></p>
        <p>
            <button type="button" class="btn btn-primary authorizePayout"><cms:contentText key="APPROVE_PAYOUTS_CONFIRM_YES" code="ssi_contest.creator" /></button>
            <button type="button" class="btn closePrompt"><cms:contentText key="NO" code="system.button" /></button>
        </p>
    </div>

    <div class="promptContentClostContest" style="display:none">
        <p><cms:contentText key="CLOSE_CONTEST_INFO" code="ssi_contest.creator" /></p>
        <p><cms:contentText key="CLOSE_CONTEST_CONFIRM" code="ssi_contest.creator" /></p>
        <form id="ssiCloseContestForm" action="contestPayouts.do?method=closeContest&id=${id}" method="post">
            <p>
                <button class="btn btn-primary closeContest" name="closeContest" value="yes"><cms:contentText key="CLOSE_CONTEST_YES" code="ssi_contest.creator" /></button>
                <button class="btn closeContest" name="closeContest" value="no"><cms:contentText key="CLOSE_CONTEST_NO" code="ssi_contest.creator" /></button>
            </p>
        </form>
    </div>

    <div class="modal hide fade" id="ssiExtractResultsModal" data-backdrop="static"data-keyboard="false">
        <div class="modal-header text-center">
            <h4><cms:contentText key="CONTEST_RESULT_REPORT" code="ssi_contest.creator" /></h4>
            <a href="contestPayoutsDownloadAction.do?method=extractFinalReport&id=${id}" class="btn btn-primary extractResults"><cms:contentText key="YES" code="system.button" /></a>
            <button class="btn extractResults"><cms:contentText key="NO" code="system.button" /></button>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

        // JAVA NOTE: this url is where the approval will post to.
        // It should return either an error or a success response message following G5 standard behavior
        //G5.props.URL_JSON_SSI_APPROVE_PAYOUT = G5.props.URL_ROOT + 'ajax/ssiContestPayoutApprove.json';
            //G5.props.URL_JSON_SSI_APPROVE_PAYOUT = G5.props.URL_ROOT + 'assets/ajax/ssiContestPayoutApprove.json';
            G5.props.URL_JSON_SSI_APPROVE_PAYOUT = G5.props.URL_ROOT + 'ssi/contestPayouts.do?method=approveContestPayouts';
        // JAVA NOTE: this url is where stack rank updates are sent
        // G5.props.URL_JSON_SSI_APPROVE_PAYOUT_UPDATE = G5.props.URL_ROOT + 'assets/ajax/ssiContestPayoutUpdate.json';
            G5.props.URL_JSON_SSI_APPROVE_PAYOUT_UPDATE = G5.props.URL_ROOT + 'ssi/contestPayouts.do?method=updateAndApproveContestPayouts';


        // JAVA NOTE: this is the url to redirect the browser to after payouts are successfully approved
        //G5.props.URL_SSI_APPROVE_PAYOUT_SUCCESS_REDIRECT =  G5.props.URL_ROOT + 'layout.html';
        G5.props.URL_SSI_APPROVE_PAYOUT_SUCCESS_REDIRECT =  G5.props.URL_ROOT + 'ssi/creatorContestList.do?method=display';


        // JAVA NOTE: front end needs these for individual contests types
        // but only URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS is needed on the backend
   /*      G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_SIU        = G5.props.URL_ROOT + 'ajax/ssiContestPayoutParticipantData_SIU.json';
        G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_OBJECTIVES = G5.props.URL_ROOT + 'ajax/ssiContestPayoutParticipantData_objectives.json';
        G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_DTGT       = G5.props.URL_ROOT + 'ajax/ssiContestPayoutParticipantData_DTGT.json';
        G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_STACKRANK  = G5.props.URL_ROOT + 'ajax/ssiContestPayoutParticipantData_stackRank.json';
 */
         // G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_SIU        = G5.props.URL_ROOT + 'ssi/contestPayouts.do?method=fetchContestPaxPayouts';
         // G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_OBJECTIVES = G5.props.URL_ROOT + 'ssi/contestPayouts.do?method=fetchContestPaxPayouts';
         // G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_DTGT       = G5.props.URL_ROOT + 'ssi/contestPayouts.do?method=fetchContestPaxPayouts';
         // G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS_STACKRANK  = G5.props.URL_ROOT + 'ssi/contestPayouts.do?method=fetchContestPaxPayouts';
		G5.props.URL_JSON_SSI_APPROVE_PAYOUT_PARTICIPANTS  = G5.props.URL_ROOT + 'ssi/contestPayouts.do';


        // JAVA NOTE: this is the url to the participant detail page
        G5.props.URL_SSI_PARTICIPANT_DETAIL_PAGE  = G5.props.URL_ROOT + 'ssiParticipantPage.html';

        // JAVA NOTE: `contestData` represents a single bootstrapped contest.
        // A single object from `EXAMPLE_DATA` should be set as contestData - zach
        /*
        var contestData = {
            "id": "1",
            "contestType": "stepItUp"
        };
        */
        var contestData = ${ssiContestPayoutsForm.contestPayoutsTotalJson};
        //attach the view to an existing DOM element
        var ssiNav = new SSIApprovePayoutView({
            contestData: contestData,
            el: $('#ssiApprovePayout'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="CONTEST_PAYOUTS" code="ssi_contest.creator" />'
        });
    });
</script>


<script type="text/template" id="ssiApprovePayoutsTableSIUtplTpl">
  <%@include file="/ssi/ssiApprovePayoutsTableSIUTpl.jsp" %>
</script>

<script type="text/template" id="ssiApprovePayoutsTableDTGTtplTpl">
  <%@include file="/ssi/ssiApprovePayoutsTableDTGTTpl.jsp" %>
</script>

<script type="text/template" id="ssiApprovePayoutsTableObjectivestplTpl">
  <%@include file="/ssi/ssiApprovePayoutsTableObjectivesTpl.jsp" %>
</script>

<script type="text/template" id="ssiApprovePayoutsTableStackRanktplTpl">
  <%@include file="/ssi/ssiApprovePayoutsTableStackRankTpl.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>
