<%@ include file="/include/taglib.jspf"%>

<!-- ======== ENGAGEMENT MODULE ======== -->
<script type="text/template" id="engagementManagerModuleTpl">
<div class="module-liner">
    <div class="module-content">

        <h3 class="module-title">
            <a class="title-link" href="engagement/engagementDisplay.do?method=displayDashboardPage">
                <cms:contentText key="TEAM_DASHBOARD" code="engagement.participant"/>
            </a>
        </h3>

        <!-- the EngagementSummaryCollectionView should always be inserted into an element with class 'engagementSummaryCollectionView' -->
        <div class="engagementSummaryCollectionView moduleSummary">
        </div><!-- /.engagementSummaryCollectionView -->

    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>

<script type="text/template" id="engagementSummaryCollectionTpl">
  <%@include file="/engagement/engagementSummaryCollection.jsp" %>
</script>

<script type="text/template" id="engagementSummaryModelTpl">
  <%@include file="/engagement/engagementSummaryModel.jsp" %>
</script>