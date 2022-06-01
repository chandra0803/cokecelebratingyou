<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<tiles:importAttribute/>
<div id="LaunchPageView">
    <h2 class="page-title"></h2>

	<beacon:authorize ifAnyGranted="MANAGER,PROCESS_TEAM,BI_ADMIN,PROJ_MGR,VIEW_REPORTS">
		<c:set var="reportEligible" value="true"/>
	</beacon:authorize>

    <div class="moduleContainerViewElement">
        <div class="modules-woke"></div>
        <div class="modules-slept"></div>

        <!-- dynamic content -->

    </div> <!-- /.moduleContainerViewElement -->
</div><!-- /#homeAppPageView -->
<script>
		//Main Home Application Setup Function
		$(document).ready(function(){

		    //instantiate HomeApp
		    hapv = new LaunchPageView({
		        el : $('#LaunchPageView'),
		        pageNav : {},
		        pageTitle : '',
		        allowFollow: ${beacon:systemVarBoolean('profile.followlist.tab.show')},
		        userHomeFilter: 'recognition',
		        isFooterSheets : false
		    });
		    
		    
		 //_.delay(function(){$('.settings').hide();}, 500);  
		 
		 <c:if test="${hideSettingsTab}">
		   _.delay(function(){$('.settings').hide();}, 500);
		 </c:if>
		    
		    

		 G5.props.URL_JSON_CLAIM_ACTIVITY = G5.props.URL_ROOT+'claim/claimsRecentActivity.do?method=fetchProductClaimByIdForTile';

 		 G5.props.URL_JSON_LEADERBOARD_SETS = 'leaderBoardResult.do?method=fetchLeaderBoardsForTile';

		 //Mini Profile PopUp JSON
		 G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

		 //Mini Profile PopUp Follow Unfollow Pax JSON

	     G5.props.URL_JSON_EZ_RECOGNITION_RECIPIENT_LIST = "${pageContext.request.contextPath}/recognitionWizard/easyRecognitionRecipientSearch.do";

	     // Nominations
	     G5.props.URL_JSON_NOMINATIONS_LIST = G5.props.URL_ROOT+'nomination/nominationTile.do?method=eligiblePromo';

	     // budget tracker
	     G5.props.URL_JSON_BUDGETS  = G5.props.URL_ROOT+'profile/budgetTracker.action';

	     // gamification
	     G5.props.URL_JSON_GAMIFICATION = G5.props.URL_ROOT+'profile/badges.action';

         //engagement
         G5.props.URL_JSON_ENGAGEMENT_SUMMARY_COLLECTION = G5.props.URL_ROOT+'profile/dashboard.action';

         //WorkHappier
         G5.props.URL_JSON_WORK_HAPPIER_PAST_RESULTS = G5.props.URL_ROOT+'profile/workhappier.action';

 		 // endpoint for getting the promotion rules
 		 G5.props.URL_JSON_RULES = '${pageContext.request.contextPath}/rules.do?method=fetchPromotionRules';

         hapv.launchApp.moduleCollection.reset([
         <c:forEach var="moduleItem" items="${moduleList}" varStatus="moduleStatus">
	         						{
	                                    name:'<c:out value="${moduleItem.tileMappingType.code}"/>',
	                                    appName: '<c:out value="${moduleItem.appName}"/>',//name of the app
	                                    filters:{
	                                    	<c:if test="${moduleItem.tileMappingType.code == 'heroModule' }">
	                                    	'recognition':{size:'4x2', order:1, searchEnabled:true}
	                                    	</c:if>
	                                    	<c:if test="${moduleItem.tileMappingType.code == 'leaderboardModule' }">
	                                    	'recognition':{size:'4x2', order:2}
	                                    	</c:if>
	                                    	<c:if test="${moduleItem.tileMappingType.code == 'managerToolkitModule' }">
	                                    	'recognition':{size:'2x1', order:3}
	                                    	</c:if>
	                                    	<c:if test="${moduleItem.tileMappingType.code == 'claimModule' }">
	                                    	'recognition':{size:'4x2', order:5}
	                                    	</c:if>
	                                    }	                                    
	                                }<c:if test="${moduleStatus.index < fn:length(moduleList) - 1}">,</c:if>
         </c:forEach>
                                    ]
                			);
                		});

</script>
<c:forEach items="${moduleList}" var="filteredModule" >
	<c:if test="${filteredModule.tileMappingType.code == 'resourceCenterModule' }">
		<tiles:insert definition="social.resources.module" flush="true" ignore="true"/>
	</c:if>

	<c:if test="${filteredModule.tileMappingType.code == 'leaderboardModule' }">
		<tiles:insert definition="leaderBoard.ranking.module" flush="true" ignore="true"/>
	</c:if>

	<c:if test="${filteredModule.tileMappingType.code == 'managerToolkitModule' }">
		<tiles:insert definition="manager.toolkit.module" flush="true" ignore="true"/>
	</c:if>

	<c:if test="${filteredModule.tileMappingType.code == 'claimModule'}">
		<tiles:insert definition="product.claim.module" flush="true" ignore="true"/>
 	</c:if>
</c:forEach>


<c:if test="${budgetConfirmation == true}">
	<tiles:insert definition="budget.transfer.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${contactUsEmailConfirmation == true}">
	<tiles:insert definition="emailSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty recognitionSentBean}">
	<tiles:insert definition="recognitionSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty invites}">
	<tiles:insert definition="purlInvitationSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${sendAlertsuccess == true}">
	<tiles:insert definition="alertSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty claimSubmittedBean}">
	<tiles:insert definition="claim.submission.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<tiles:insert definition="budget.tracker.module" flush="true" ignore="true"/>
<tiles:insert definition="social.gamification.module" flush="true" ignore="true"/>
<tiles:insert definition="work.happier.module" flush="true" ignore="true"/>
<tiles:insert definition="engagement.module" flush="true" ignore="true"/>
<tiles:insert definition="bunchball.sidebar.module" flush="true" ignore="true"/><!-- Client customization -->
<c:if test="${sessionScope.eligiblePromotionsExist == true}">
   <tiles:insert definition="recognition.module" flush="true" ignore="true"/>
</c:if>
<tiles:insert definition="nomination.winner.modal.tpl" flush="true" ignore="true" />
<tiles:insert definition="hero.module" flush="true" ignore="true"/>
