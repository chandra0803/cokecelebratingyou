<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="SSIParticipantModuleTpl">
<!-- ======== SSI PARTICIPANT MODULE ======== -->
{{debug}}
<div class="module-liner" data-temp-debug="participant">
    <div class="module-content">

        <h3 class="module-title">
            <a href="${pageContext.request.contextPath}/ssi/participantContestList.do?method=display"><cms:contentText code="ssi_contest.generalInfo" key="MY_CONTESTS" /></a>
        </h3>

        <!-- the ssiParticipantContestViews should always be inserted into an element with class 'ssiContestViews' -->
        <div class="ssiContestViews"></div>

    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>

<script>
	$(document).ready(function() {

		G5.props.URL_JSON_SSI_ACTIVITY_HISTORY_TABLE = G5.props.URL_ROOT+'ssi/participantActivityHistory.do?method=fetchActivityHistory';

		//G5.props.URL_JSON_SSI_ACTIVE_CONTESTS = G5.props.URL_ROOT+'assets/ajax/ssiActiveContests.json';

		G5.props.URL_JSON_SSI_STACK_RANK_TABLE =  G5.props.URL_ROOT+'assets/ajax/ssiStackRankTable.json';

		G5.props.URL_JSON_SSI_MASTER_PARTICIPANT = G5.props.URL_ROOT+'ssi/participantContestList.do?method=fetchContests';


	});
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

<script type="text/template" id="ssiModuleDoThisGetThatTplTpl">
    <%@include file="/ssi/ssiModuleDoThisGetThatTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleObjectiveTplTpl">
    <%@include file="/ssi/ssiModuleObjectiveTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleStackRankTplTpl">
    <%@include file="/ssi/ssiModuleStackRankTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleStepItUpTplTpl">
    <%@include file="/ssi/ssiModuleStepItUpTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleViewTpl">
    <%@include file="/ssi/ssiModuleView.jsp" %>
</script>

<script type="text/template" id="ssiActivityHistoryTplTpl">
    <%@include file="/ssi/ssiActivityHistoryTpl.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<!--  below tpl is to show the participants stack rank  -->
<script type="text/template" id="ssiStackRankCollectionTplTpl">
  <%@include file="/ssi/ssiStackRankCollectionTpl.jsp" %>
</script>