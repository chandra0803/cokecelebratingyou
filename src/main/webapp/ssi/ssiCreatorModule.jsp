<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="SSICreatorModuleTpl">
<!-- ======== SSI CREATOR MODULE ======== -->
<div class="module-liner" data-temp-debug="creator">
    <div class="module-content">

        <h3 class="module-title">
            <a href="${pageContext.request.contextPath}/ssi/creatorContestList.do?method=display"><cms:contentText code="ssi_contest.creator" key="MANAGE_CONTESTS" /></a>
        </h3>

        <!-- the ssiCreatorContestViews should always be inserted into an element with class 'ssiContestViews' -->
        <div class="ssiContestViews"></div>

    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>

<script>
	$(document).ready(function() {

		G5.props.URL_JSON_SSI_MASTER_CREATOR  = G5.props.URL_ROOT+'ssi/creatorContestList.do?method=fetchContests';

	});
</script>


<script type="text/template" id="ssiModuleObjectiveTplTpl">
    <%@include file="ssiModuleObjectiveTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleDoThisGetThatTplTpl">
    <%@include file="ssiModuleDoThisGetThatTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleStepItUpTplTpl">
    <%@include file="ssiModuleStepItUpTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleStackRankTplTpl">
    <%@include file="ssiModuleStackRankTpl.jsp" %>
</script>

<script type="text/template" id="ssiModuleViewTpl">
    <%@include file="ssiModuleView.jsp" %>
</script>

<script type="text/template" id="ssiCreatorListModuleTpl">
    <%@include file="ssiCreatorListModule.jsp" %>
</script>

<script type="text/template" id="ssiModuleStepItUpCreatorTplTpl">
    <%@include file="ssiModuleStepItUpCreatorTpl.jsp" %>
</script>

<!--  below tpl is to show the participants stack rank  -->
<script type="text/template" id="ssiStackRankCollectionTplTpl">
  <%@include file="/ssi/ssiStackRankCollectionTpl.jsp" %>
</script>


