<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!--   PROFILE PAGE SHELL  -->

<script>
function getTDStats(promoId)
{
	G5.props.URL_HTML_THROWDOWN_PUBLIC_PROFILE = "${profileUrl}&isPlayerStatsView=true&promotionId="+promoId;
}
</script>

<div id="profilePageView" class="profilePage-liner page-content">

    <!-- User menu  -->
    <div class="row-fluid">
        <div class="span12">
            <div id="profilePageShellActiveTabSet">
                <div class="tab-content">
                    <div class="tab-pane" id="profilePagePersonalInfoTab"></div>
                    <div class="tab-pane" id="profilePageBadgesTab"></div>
                    <div class="tab-pane" id="profilePageAlertsAndMessagesTab"></div>
                    <div class="tab-pane" id="profilePageStatementTab"></div>
                    <div class="tab-pane" id="profilePageFollowListTab"></div>
                    <div class="tab-pane" id="profilePageActivityHistoryTab"></div>
                    <%-- TODO: add logic for displaying this tab --%>
                    <div class="tab-pane" id="profilePageDashboardTab"></div>
                    <div class="tab-pane" id="profilePageGroupsTab"></div>
                    <div class="tab-pane" id="profilePageThrowdownStatsTab">
                        <%-- TODO: add CM key --%>
                        <h2><cms:contentText key="THROWDOWN_STATS" code="participant.participant" /></h2>

                        <div class="row-fluid" id="profilePagePlayerStatsTab">
                            <div class="span12">

                                <div class="row-fluid">
                                    <div class="span6">
                                        <form class="form-inline form-labels-inline">
                                            <fieldset>
                                                <div class="control-group" id="controlMatchSelect">
                                                    <div class="controls">
                                                        <select id="promotionSelect" name="promotionId" data-no-rankings="There are no promotions for this Match Filter." onchange="getTDStats(this.value)">
                                                           <c:forEach items="${promotions}" var="promo">
                                                            <option value="${promo.promotion.id}">${promo.promotion.name}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div><!-- /.control-group -->
                                            </fieldset>
                                        </form>
                                    </div>
                                    <div class="span6">
                                        <ul class="export-tools fr">
                                            <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT"/> <i class="icon-printer"></i></a></li>
                                        </ul>
                                   </div>
                                </div><!-- /.row-fluid -->

                                <%@include file="/throwdown/throwdownPublicProfile.jsp" %>

                            </div><!-- /.span12 -->
                        </div><!-- /.row-fluid -->
                    </div><!-- /#profilePageThrowdownStatsTab -->
                    <div class="tab-pane" id="profilePageSecurityTab"></div>
                    <div class="tab-pane" id="profilePagePreferencesTab"></div>
                    <div class="tab-pane" id="profilePageProxiesTab"></div>
                </div>
            </div>
        </div>
    </div>
</div> <!-- ./profilePage-liner -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    	//Overriding response URLs with server responses(action/controller)

		G5.props.URL_JSON_PARTICIPANT_PROFILE_ALERTS = G5.props.URL_ROOT+'profileView.do?method=fetchUserAlrtsInfo&refreshAlerts=true';//curren active user's alerts

		//profile page personal info tab upload profile picture JSON
		G5.props.URL_JSON_PROFILE_PAGE_PERSONAL_INFORMATION_IMAGE_UPLOAD = G5.props.URL_ROOT+'profilePagePersonalInfoTab.do?method=uploadAvatar';

		//profile page personal info tab About Me JSON
		G5.props.URL_JSON_PROFILE_PAGE_PERSONAL_INFORMATION_ABOUT_ME = G5.props.URL_ROOT+'profilePagePersonalInfoTab.do?method=saveAboutMeInfo';

		//profile page badges tab JSON
    	G5.props.URL_JSON_GAMIFICATION_DETAIL = 'profilePageBadgesTab.do?method=fetchBadgesForProfile&isFromMiniProfile=false';

		//profile page Alerts & Messages tab JSON
        G5.props.URL_JSON_PROFILE_PAGE_ALERTS_TAB_ALERTS = G5.props.URL_ROOT+'profilePageAlertsAndMessagesTabAlerts.do?method=fetchAlrts';
        // Enable the below piece only while test with local json
        // G5.props.URL_JSON_PROFILE_PAGE_ALERTS_TAB_ALERTS = G5.props.URL_ROOT+'assets/json/profilePageAlertsAndMessagesTabAlerts.json';
        G5.props.URL_JSON_PROFILE_PAGE_ALERTS_TAB_MESSAGES = G5.props.URL_ROOT+'profilePageAlertsAndMessagesTabMessages.do?method=fetchMessages';
        // Enable the below piece only while test with local json
        // G5.props.URL_JSON_PROFILE_PAGE_ALERTS_TAB_MESSAGES = G5.props.URL_ROOT+'assets/json/profilePageAlertsAndMessagesTabMessages.json';
        
        G5.props.PROFILE_ALERTS_MESSAGES_PER_PAGE = 25;

		//profile page Follow List tab JSON
    	G5.props.URL_JSON_PUBLIC_RECOGNITION_FOLLOWEES_LIST = G5.props.URL_ROOT+'profilePageFollowListTab.do?method=fetchFollowingPax';

    	//profile page Add to Follow List pax table JSON
    	G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

        // RPM/Engagement dashboard model
        G5.props.URL_JSON_ENGAGEMENT_MODEL = G5.props.URL_ROOT+'profilePageEngagementDashboardTab.do?method=fetchDashboardData';

    	//profile page Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

		// Profile Page Delegate/Proxies Page
		G5.props.URL_JSON_PROFILE_PAGE_PROXIES = G5.props.URL_ROOT + 'prePopulateDelegates.do?method=fetchDelegates';

		//profile page preferences tab
		G5.props.URL_JSON_PROFILE_PAGE_PREFERENCES_TAB = G5.props.URL_ROOT +'saveProfilePagePreferences.do?method=updatePreferences';

		//attach the view to an existing DOM element
		var PP = new ProfilePageView({
            el:$('#profilePageView'),
            pageTitle : '<cms:contentText key="PROFILE_TITLE" code="profile.personal.info" />',
            // passing the userId is entirely unnecessary, as the profile always shows the information of the currently logged-in user, but it can be helpful to be explicit.
            userId: '<c:out value="${pax.id}" />'
        });

    });
</script>

<script type="text/template" id="profilePageAlertsTabAlertsTpl">
    <%@include file="/profile/alertsMessages/profilePageAlertsTabAlerts.jsp"%>
</script>

<script type="text/template" id="profilePageAlertsTabMessagesTpl">
    <%@include file="/profile/alertsMessages/profilePageAlertsTabMessages.jsp"%>
</script>

	<c:if test="${preferenceSaved == true}">
		<tiles:insert definition="preferenceSave.success.confirmation.modal" flush="true" ignore="true" />
	</c:if>

<c:if test="${not empty redeemedSuccessMsg}">
	<tiles:insert definition="coke.cash.recognition.redeemed.confirmation.modal" flush="true" ignore="true" />
</c:if>

 <!--
 Folowing tabs are ajax call with html response
	profilePagePersonalInfoTab
	profilePageStatementTab
	profilePageActivityHistoryTab
	profilePageSecurityTab
	profilePagePreferencesTab
	profilePageProxiesTab
-->

<!--Specify templates for all Tabs that are JSON response based -->

<script type="text/template" id="profilePageBadgesTabTpl">
    <%@include file="/profile/profilePageBadgesTab.jsp"%>
</script>

<script type="text/template" id="profilePageAlertsAndMessagesTabTpl">
	<%@include file="/profile/alertsMessages/profilePageAlertsAndMessagesTab.jsp"%>
</script>

<script type="text/template" id="profilePageFollowListTabTpl">
	<%@include file="/profile/profilePageFollowListTab.jsp"%>
</script>

<script type="text/template" id="profilePageGroupsSaveEditViewTpl">
	<%@include file="/profile/profilePageGroupsSaveEditView.jsp"%>
</script>

<c:if test="${displayEngagementTab eq true}">
<script type="text/template" id="profilePageDashboardTabTpl">
	<%@include file="/profile/profilePageDashboardTab.jsp"%>
</script>

<script type="text/javascript" src="${siteUrlPrefix}/assets/libs/plugins/fusioncharts.js"></script>
</c:if>

<%@include file="/search/paxSearchStart.jsp" %>

<script type="text/template" id="participantSearchViewTpl">
	<%@include file="/profileutil/participantSearchView.jsp"%>
</script>

<script type="text/template" id="participantSearchTableRowTpl">
	<%@include file="/profileutil/participantSearchTableRow.jsp"%>
</script>

<script type="text/template" id="participantRowItemTpl">
	<%@include file="/profileutil/participantRowItem.jsp"%>
</script>

<script type="text/template" id="engagementModelTpl">
    <%@include file="../engagement/engagementModel.jsp" %>
</script>

<script type="text/template" id="engagementSummaryCollectionTpl">
    <%@include file="../engagement/engagementSummaryCollection.jsp" %>
</script>

<script type="text/template" id="engagementSummaryModelTpl">
    <%@include file="../engagement/engagementSummaryModel.jsp" %>
</script>

<script type="text/template" id="engagementDetailScoreTpl">
    <%@include file="../engagement/engagementDetailScore.jsp" %>
</script>

<script type="text/template" id="engagementDetailRecTpl">
    <%@include file="../engagement/engagementDetailRec.jsp" %>
</script>

<script type="text/template" id="engagementDetailPaxTpl">
    <%@include file="../engagement/engagementDetailPax.jsp" %>
</script>

<script type="text/template" id="engagementDetailVisitsTpl">
    <%@include file="../engagement/engagementDetailVisits.jsp" %>
</script>

<script type="text/template" id="engagementRecognizedTpl">
  <%@include file="/engagement/engagementRecognized.jsp" %>
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
