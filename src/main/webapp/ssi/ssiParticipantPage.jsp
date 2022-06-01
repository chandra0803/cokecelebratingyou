<%@ include file="/include/taglib.jspf"%>
<div id="ssiPageView" class="quizPage-liner page-content">
    <div id="ssiContestNav" class="page-topper">
        <!-- SsiContestNavigationTpl -->
    </div>
    <div id="ssiContestView">
        <div class="row-fluid">
        </div>
        <!--  -->
    </div>

    <!-- JAVA NOTE: render the following HTML if the user has just completed uploading progress successfully -->
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


</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

    	<!-- this url provides contest details based on contest id -->
    	G5.props.URL_JSON_SSI_CONTEST = G5.props.URL_ROOT+'ssi/participantContestList.do?method=fetchContestDetail';

    	<!-- this url provides only archived contests -->
    	G5.props.URL_JSON_SSI_ARCHIVED_CONTEST = G5.props.URL_ROOT+'ssi/participantContestList.do?method=fetchArchivedContest';

    	<!-- this url provides contest activity history -->
    	G5.props.URL_JSON_SSI_ACTIVITY_HISTORY_TABLE = G5.props.URL_ROOT+'ssi/participantActivityHistory.do?method=fetchActivityHistory';

    	//G5.props.URL_JSON_SSI_STACK_RANK_TABLE =  G5.props.URL_ROOT+'assets/ajax/ssiStackRankTable.json';
    	G5.props.URL_JSON_SSI_STACK_RANK_TABLE =  G5.props.URL_ROOT+'ssi/participantContestList.do?method=fetchStackRank';

    	//Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // JAVA NOTE: `contestData` represents a single bootstrapped contest
        var contestData;
        <c:if test="${not empty ssiContestListForm.contestJson}">
    		contestData = ${ssiContestListForm.contestJson};
		</c:if>
        // bootstrapped json for active contests
        var activeContests = ${ssiContestListForm.initializationJson};

        //attach the view to an existing DOM element
        var ssiNav = new SSIParticipantPageView({
            contestData: contestData,
            activeContests: activeContests,
            el: $('#ssiPageView'),
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
            pageTitle : '<cms:contentText key="CONTEST" code="ssi_contest.participant" />'
        });
    });
</script>

<script type="text/template" id="ssiContestArchiveTplTpl">
  <%@include file="/ssi/ssiContestArchiveTpl.jsp" %>
</script>

<script type="text/template" id="SSIContestActivityTplTpl">
  <%@include file="/ssi/ssiContestActivityTpl.jsp" %>
</script>

<script type="text/template" id="ssiActivityHistoryTplTpl">
    <%@include file="/ssi/ssiActivityHistoryTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantNavigationTplTpl">
  <%@include file="/ssi/ssiParticipantNavigationTpl.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<script type="text/template" id="ssiParticipantContestTplTpl">
  <%@include file="/ssi/ssiParticipantContestTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantDoThisGetThatTplTpl">
    <%@include file="/ssi/ssiParticipantDoThisGetThatTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantObjectiveTplTpl">
  <%@include file="/ssi/ssiParticipantObjectiveTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantStackRankTplTpl">
  <%@include file="/ssi/ssiParticipantStackRankTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantStepItUpTplTpl">
  <%@include file="/ssi/ssiParticipantStepItUpTpl.jsp" %>
</script>

<script type="text/template" id="ssiStackRankCollectionTplTpl">
  <%@include file="/ssi/ssiStackRankCollectionTpl.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
