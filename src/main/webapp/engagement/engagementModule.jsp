<%@ include file="/include/taglib.jspf"%>

<!-- ======== ENGAGEMENT MODULE ======== -->
<script type="text/template" id="engagementModuleTpl">

    <h3 class="section-header">
    	{{#eq mode "team"}}
        	<cms:contentText key="TEAM_DASHBOARD" code="engagement.participant"/>
    	{{else}}
        	<cms:contentText key="MY_DASHBOARD" code="engagement.participant"/>
     	{{/eq}}
    </h3>

    <div class="module-liner">

        <!-- the EngagementSummaryCollectionView should always be inserted into an element with class 'engagementSummaryCollectionView' -->
        <div class="engagementSummaryCollectionView moduleSummary">
        </div><!-- /.engagementSummaryCollectionView -->

        <ol class="unstyled sidebar-list">
            <li class="item dashboard" data-route="profile/Dashboard">
                <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Dashboard/score" class="engagement-view-details">
                    <cms:contentText key="SEE_DETAILS" code="engagement.participant"/>
                </a>
            </li>
        </ol>
    </div><!-- /.module-liner -->
</script>

<script type="text/template" id="engagementSummaryCollectionTpl">
  <%@include file="/engagement/engagementSummaryCollection.jsp" %>
</script>

<script type="text/template" id="engagementSummaryModelTpl">
  <%@include file="/engagement/engagementSummaryModel.jsp" %>
</script>
