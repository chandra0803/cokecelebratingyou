<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== ENGAGEMENT PAGE TEAM DASHBOARD ======== -->

<div id="engagementPageTeamDashboard" class="engagement engagementPage engagementPageTeamDashboard page-content"><div class="row-fluid">

    <!-- DIV.ROW up next to wrapping div.page-content for IE7 -->
        <div class="span12">
            <h2><cms:contentText key='TEAM_DASHBOARD' code='engagement.participant'/></h2>

            <!-- the EngagementModelView should always be inserted into an element with class 'engagementModelView' -->
            <div class="engagementModelView"></div><!-- /.engagementModelView -->

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        // overrides for the JSON variables (these are set in settings.js but provided here for convenience in creating a JSP)
        G5.props.URL_JSON_ENGAGEMENT_MODEL = G5.props.URL_ROOT+'engagement/engagementDisplay.do?method=fetchDashboardData';
        G5.props.URL_JSON_ENGAGEMENT_TEAM_MEMBERS_COLLECTION = G5.props.URL_ROOT+'engagement/engagementDisplay.do?method=sortTeamsOrTeamMembers';
        G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'/publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        //attach the view to an existing DOM element
        G5.views.EngagementPageTeamDashboard = new EngagementPageTeamDashboardView({
            el:$('#engagementPageTeamDashboard'),
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
            pageTitle : "<cms:contentText key='TEAM_DASHBOARD' code='engagement.participant'/>",

            // mode can be team/user
            mode : 'team',
            // if this page needs to display the dashboard for a team that isn't the currently-logged in user's main team, send a nodeId. Otherwise, null is fine.
            nodeId: null
        });

    });
</script>

<script type="text/javascript" src="${siteUrlPrefix}/assets/libs/plugins/fusioncharts.js"></script>

<!-- NOTE: the following includes were added to make this shell behave like the JSP -->
<script type="text/template" id="engagementModelTpl">
    <%@include file="engagementModel.jsp" %>
</script>

<script type="text/template" id="engagementSummaryCollectionTpl">
    <%@include file="engagementSummaryCollection.jsp" %>
</script>

<script type="text/template" id="engagementSummaryModelTpl">
    <%@include file="engagementSummaryModel.jsp" %>
</script>

<script type="text/template" id="engagementDetailScoreTpl">
    <%@include file="engagementDetailScore.jsp" %>
</script>

<script type="text/template" id="engagementDetailRecTpl">
    <%@include file="engagementDetailRec.jsp" %>
</script>

<script type="text/template" id="engagementDetailPaxTpl">
    <%@include file="engagementDetailPax.jsp" %>
</script>

<script type="text/template" id="engagementDetailVisitsTpl">
    <%@include file="engagementDetailVisits.jsp" %>
</script>

<script type="text/template" id="engagementRecognizedTpl">
    <%@include file="engagementRecognized.jsp"%>
</script>

<script type="text/template" id="engagementTeamMembersTpl">
    <%@include file="engagementTeamMembers.jsp" %>
</script>

<script type="text/template" id="engagementTeamMembersCollectionTpl">
    <%@include file="engagementTeamMembersCollection.jsp" %>
</script>

<script type="text/template" id="publicRecognitionItemTpl">
    <%@include file="../publicrecognition/publicRecognitionItem.jsp"%>
</script>

<script type="text/template" id="sharePopoverTpl">
    <%@include file="../publicrecognition/sharePopover.jsp"%>
</script>

<script type="text/template" id="publicRecognitionCommentTpl">
    <%@include file="../publicrecognition/publicRecognitionComment.jsp"%>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
