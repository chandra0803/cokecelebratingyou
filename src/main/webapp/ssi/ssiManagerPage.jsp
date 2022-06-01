<%@ include file="/include/taglib.jspf"%>

<div id="ssiPageView" class="quizPage-liner page-content">
    <div id="ssiContestNav" class="page-topper">
        <!-- SsiContestNavigationTpl -->
    </div>
    <div id="ssiContestView">
        <!--  -->
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

    	<!-- this url provides contest details based on contest id -->
    	G5.props.URL_JSON_SSI_CONTEST = G5.props.URL_ROOT+'ssi/managerContestList.do?method=fetchContestDetail';
    	
		G5.props.URL_JSON_SSI_AVAILABLE_CONTEST_TYPES = G5.props.URL_ROOT+'ssi/createContest.do?method=fetchAvailableContestTypes';	
		
    	<!-- this url provides only archived contests -->
    	G5.props.URL_JSON_SSI_ARCHIVED_CONTEST = G5.props.URL_ROOT+'ssi/managerContestList.do?method=fetchArchivedContests';
    	
    	G5.props.URL_JSON_CONTEST_CHECK_NAME = G5.props.URL_ROOT+'ssi/createContest.do?method=validateContestName';
    	
		G5.props.URL_JSON_SSI_ADMIN_CONTEST_DETAILS_TABLE = G5.props.URL_ROOT+'ssi/managerContestList.do?method=fetchContestDetailsTable'; 
		
		G5.props.URL_JSON_SSI_STACK_RANK_TABLE =  G5.props.URL_ROOT+'ssi/managerContestList.do?method=fetchStackRank';
		
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
        var ssiNav = new SSIManagerPageView({
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
            pageTitle : '<cms:contentText key="My_CONTESTS" code="ssi_contest.creator" />'
        });
    });
</script>

<script type="text/template" id="SSIContestActivityTplTpl">
  <%@include file="/ssi/ssiContestActivityTpl.jsp" %>
</script>

<script type="text/template" id="ssiParticipantNavigationTplTpl">
  <%@include file="/ssi/ssiParticipantNavigationTpl.jsp" %>
</script>

<script type="text/template" id="ssiContestArchiveTplTpl">
  <%@include file="/ssi/ssiContestArchiveTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestTplTpl">
  <%@include file="/ssi/ssiAdminContestTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminDoThisGetThatTplTpl">
  <%@include file="/ssi/ssiAdminDoThisGetThatTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminObjectiveTplTpl">
  <%@include file="/ssi/ssiAdminObjectiveTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminStepItUpTplTpl">
  <%@include file="/ssi/ssiAdminStepItUpTpl.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestDetailsTplobjectivesTpl">
    <%@include file="ssiAdminContestDetailsTplobjectives.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestDetailsTpldoThisGetThatTpl">
  <%@include file="/ssi/ssiAdminContestDetailsTpldoThisGetThat.jsp" %>
</script>

<script type="text/template" id="ssiAdminContestDetailsTplstepItUpTpl">
  <%@include file="/ssi/ssiAdminContestDetailsTplstepItUp.jsp" %>
</script>

<script type="text/template" id="breadcrumbViewTpl">
    <%@include file="/ssi/breadcrumbView.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<script type="text/template" id="ssiStackRankCollectionTplTpl">
  <%@include file="/ssi/ssiStackRankCollectionTpl.jsp" %>
</script>

<script type="text/template" id="ssiManagerStackRankTplTpl">
  <%@include file="/ssi/ssiManagerStackRankTpl.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>