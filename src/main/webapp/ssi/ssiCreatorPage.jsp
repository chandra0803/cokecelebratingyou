<%@ include file="/include/taglib.jspf"%>
<div id="ssiPageView" class="quizPage-liner page-content">
    <div id="ssiContestNav" class="page-topper">
        <!-- SsiContestNavigationTpl -->
    </div>
    <div id="ssiContestView">
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

  <div class="modal hide fade ssiContestCreateModal"></div>


</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
      // JAVA NOTE: url for denied contest list page
      G5.props.URL_JSON_SSI_DENIED_CONTEST = G5.props.URL_ROOT+'ajax/ssiDeniedContests.json';

    	<!-- this url provides contest details based on contest id -->
    	G5.props.URL_JSON_SSI_CONTEST = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=fetchContestDetail';

		G5.props.URL_JSON_SSI_AVAILABLE_CONTEST_TYPES = G5.props.URL_ROOT+'ssi/createContest.do?method=fetchAvailableContestTypes';

    	<!-- this url provides only archived contests -->
    	G5.props.URL_JSON_SSI_ARCHIVED_CONTEST = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=fetchArchivedContests';

    	<!-- this url provides only denied contests -->
    	G5.props.URL_JSON_SSI_DENIED_CONTEST = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=fetchDeniedContests';

    	G5.props.URL_JSON_CONTEST_CHECK_NAME = G5.props.URL_ROOT+'ssi/createContest.do?method=validateContestName';

    	G5.props.URL_JSON_SSI_COPY_CONTEST = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=copyContest';

    	G5.props.URL_JSON_SSI_DELETE_CONTEST = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=deleteContest';

    	G5.props.URL_JSON_SSI_ADMIN_CONTEST_DETAILS_TABLE = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=fetchContestDetailsTable';

    	G5.props.URL_JSON_SSI_STACK_RANK_TABLE =  G5.props.URL_ROOT+'ssi/creatorContestList.do?method=fetchStackRank';

    	//Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // JAVA NOTE: `contestData` represents a single bootstrapped contest.
        var contestData;
        <c:if test="${not empty ssiContestListForm.contestJson}">
        	contestData = ${ssiContestListForm.contestJson};
    	</c:if>
        var activeContests = ${ssiContestListForm.initializationJson};

        //attach the view to an existing DOM element
        var ssiNav = new SSICreatorPageView({
            contestData: contestData,
            activeContests: activeContests,
            el: $('#ssiPageView'),
            noSidebar : ${isDelegate},
            noGlobalNav : ${isDelegate},
            pageNav : {
                back : {
                	  text : '<cms:contentText key="BACK" code="system.button" />',
                	  url : '${pageContext.request.contextPath}/homePage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="My_CONTESTS" code="ssi_contest.creator" />'
        });
    });
</script>

<!-- Below two tpls are  to show tables for creator -->
<script type="text/template" id="SSIContestActivityTplTpl">
  <%@include file="/ssi/ssiContestActivityTpl.jsp" %>
</script>

<script type="text/template" id="ssiContestActivityATNTplTpl">
  <%@include file="/ssi/ssiContestActivityATNTpl.jsp" %>
</script>

<!--  This tpl is to show the archived contest list -->
<script type="text/template" id="ssiContestArchiveTplTpl">
  <%@include file="/ssi/ssiContestArchiveTpl.jsp" %>
</script>

<!--  This tpl is to show the denied contest list -->
<script type="text/template" id="ssiContestDeniedTplTpl">
  <%@include file="/ssi/ssiContestDeniedTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantNavigationTplTpl">
  <%@include file="/ssi/ssiParticipantNavigationTpl.jsp" %>
</script>

<script type="text/template" id="breadcrumbViewTpl">
    <%@include file="/ssi/breadcrumbView.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<!-- Below three tpls are to show the contest progress details to creator/manager -->
<script type="text/template" id="ssiAdminObjectiveTplTpl">
  <%@include file="/ssi/ssiAdminObjectiveTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminDoThisGetThatTplTpl">
  <%@include file="/ssi/ssiAdminDoThisGetThatTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminStepItUpTplTpl">
  <%@include file="/ssi/ssiAdminStepItUpTpl.jsp" %>
</script>

<!-- Below three tpls are to show the contest participants summary table to creator/manager -->
<script type="text/template" id="ssiAdminContestDetailsTplobjectivesTpl">
  <%@include file="/ssi/ssiAdminContestDetailsTplobjectives.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestDetailsTpldoThisGetThatTpl">
  <%@include file="/ssi/ssiAdminContestDetailsTpldoThisGetThat.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestDetailsTplstepItUpTpl">
  <%@include file="/ssi/ssiAdminContestDetailsTplstepItUp.jsp" %>
</script>

<script type="text/template" id="ssiCreatorStackRankTplTpl">
  <%@include file="/ssi/ssiCreatorStackRankTpl.jsp" %>
</script>

<script type="text/template" id="ssiContestCreateModalTplTpl">
    <%@include file="ssiContestCreateModalTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestTplTpl">
  <%@include file="/ssi/ssiAdminContestTpl.jsp" %>
</script>

<!--  below tpl is to show the participants stack rank  -->
<script type="text/template" id="ssiStackRankCollectionTplTpl">
  <%@include file="/ssi/ssiStackRankCollectionTpl.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>