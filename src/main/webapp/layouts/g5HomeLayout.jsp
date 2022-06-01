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
	    
	 


        G5.props.URL_JSON_PLATEAU_AWARDS_MODULE = '<%=RequestUtils.getBaseURI(request)%>/plateauAwardsResult.do';
        G5.props.URL_JSON_APPROVALS_LOAD_APPROVAL_MESSAGE_DATA  = G5.props.URL_ROOT + 'claim/approvalsCountAction.do?method=fetchApprovalsForTileAndList';

        //json response url, overrides value defined in settings.js
        G5.props.URL_JSON_BUDGETS  = G5.props.URL_ROOT+'profile/budgetTracker.action';
        G5.props.URL_JSON_BANNER_SLIDES = 'bannerSlides.do?method=fetchBannersForTile';
        //json response url, overrides value defined in settings.js
        G5.props.URL_JSON_COMMUNICATION = '<%=RequestUtils.getBaseURI(request)%>/communicationsResult.do';
        G5.props.URL_JSON_NEWS = '<%=RequestUtils.getBaseURI(request)%>/newsResult.do';
        G5.props.URL_JSON_TIPS = '<%=RequestUtils.getBaseURI(request)%>/dailyTipResult.do';
        G5.props.URL_JSON_GAMIFICATION = G5.props.URL_ROOT+'profile/badges.action';
		 G5.props.URL_JSON_LEADERBOARD_SETS = 'leaderBoardResult.do?method=fetchLeaderBoardsForTile';
		//  For Application dynamic values Use this
		G5.props.URL_JSON_PUBLIC_RECOGNITION = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionResult.do?method=fetchPublicRecognitions';
		/* Enable the below code only when you want to have data in your local for dev */
		// G5.props.URL_JSON_PUBLIC_RECOGNITION = G5.props.URL_ROOT+'assets/json/publicRecognition.json';		
		G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_COMMENT = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionCommentAction.do?method=postComment';
        G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_LIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=like';
        G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_UNLIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=unlike';
        G5.props.URL_JSON_RECENT_DISCUSSIONS = 'forum/forumResult.do?method=fetchForumTopicsForTile';
        G5.props.URL_JSON_PUBLIC_RECOGNITION_TRANSLATE=G5.props.URL_ROOT + "recognitionWizard/publicRecognitionTranslate.do?method=translateComment";

        G5.props.URL_JSON_CLAIM_ACTIVITY = G5.props.URL_ROOT+'claim/claimsRecentActivity.do?method=fetchProductClaimByIdForTile';

      	//instant poll
        G5.props.URL_JSON_INSTANT_POLL_COLLECTION = G5.props.URL_ROOT+'instantPollTake.do?method=fetchQuestions';
        G5.props.URL_JSON_INSTANT_POLL_SUBMIT_SURVEY = G5.props.URL_ROOT+'instantPollTake.do?method=saveInstantPoll';
		
     	// Client customizations for WIP #62128 
        G5.props.URL_JSON_CHEERS_INFO = G5.props.URL_ROOT+'cheersPopoverView.do?method=fetchCheersPoints';
        G5.props.URL_JSON_CHEERS_SEND_RECOGNITION = G5.props.URL_ROOT+'cheersPopoverView.do?method=postCheers';
        
        //throwdown
        G5.props.URL_JSON_THROWDOWN_MATCHES = 'matchSummary.do?method=summary&promotionId=';<%-- This JSON URL gets appended for the promotionId via the _throwdown.js file --%>
        G5.props.URL_JSON_THROWDOWN_PROMOTIONS = 'promotionSelector.do?method=list';
        G5.props.URL_JSON_THROWDOWN_STANDINGS_LINK = '<%=RequestUtils.getBaseURI(request)%>/throwdown/standingsDetail.do?method=link';
        G5.props.URL_JSON_TD_NEWS = 'throwdownNewsResult.do?method=communicationsList';
        G5.props.URL_JSON_SMACK_TALK = 'smackTalkSummary.do?method=summary&promotionId=';
        G5.props.URL_JSON_SMACK_TALK_SAVE_COMMENT = 'smackTalkComment.do?method=postComment';
        G5.props.URL_JSON_SMACK_TALK_SAVE_LIKE = 'smackTalkComment.do?method=like';
        G5.props.URL_JSON_SMACK_TALK_SAVE_HIDE='smackTalkHideAction.do?method=hide';
        G5.props.URL_THROWDOWN_ALL_MATCHES = 'viewMatchesSummary.do?method=summary&matchFilterName=all&promotionId=';
        G5.props.URL_THROWDOWN_ALL_MATCHES_TEAM = 'viewMatchesSummary.do?method=summary&matchFilterName=team&promotionId=';

        //engagement
        G5.props.URL_JSON_ENGAGEMENT_SUMMARY_COLLECTION = G5.props.URL_ROOT+'profile/dashboard.action';

        //plateau awards redeem
        G5.props.URL_JSON_PLATEAU_AWARDS_REDEEM = G5.props.URL_ROOT+'plateauAwardsRedeemModule.do?method=fetchPlateauAwardsRedeemList';

        //WorkHappier
        G5.props.URL_JSON_WORK_HAPPIER_PAST_RESULTS = G5.props.URL_ROOT+'profile/workhappier.action';


       //nominations
        G5.props.URL_JSON_NOMINATIONS_LIST = G5.props.URL_ROOT+'nomination/nominationTile.do?method=eligiblePromo';
        G5.props.URL_JSON_NOMINATIONS_PENDING_APPROVAL = G5.props.URL_ROOT+'claim/pendingNominationsApprovalTile.do?method=fetchPendingNominationsForApprovalTile';

        //purl celebrate
		G5.props.URL_JSON_PURL_CELEBRATE_DATA = 'purl/purlCelebrateRecognitionPurlsActivity.do?method=fetchRecognitionPurls&tile=true';
		/*Enable the below code only when you want to have data in your local for dev */
		// G5.props.URL_JSON_PURL_CELEBRATE_DATA = G5.props.URL_ROOT+'assets/json/purlCelebrateData.json';
		

        G5.props.URL_JSON_SMACK_TALK_DETAIL='smackTalkMatchDetail.do?method=smackTalkDetail';
        //G5.props.URL_JSON_SMACK_TALK_SAVE_HIDE = G5.props.URL_ROOT+'ajax/smackTalkSaveHide.json';
        //Mini Profile PopUp JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
        G5.props.URL_JSON_EZ_RECOGNITION_RECIPIENT_LIST = "${pageContext.request.contextPath}/recognitionWizard/easyRecognitionRecipientSearch.do";
        G5.props.URL_JSON_REPORTS_ALL = 'reports/reportMenu.do';
        G5.props.URL_JSON_REPORTS_FAVORITES = 'reports/displayDashboard.do';
        //G5.props.URL_JSON_REPORTS_FAVORITES = 'reports/displayDashboard.do?method=display';
        //G5.props.URL_JSON_REPORTS_FAVORITES_DELETE = 'reports/displayDashboard.do?method=remove';
        //G5.props.URL_JSON_REPORTS_FAVORITES_ADD = 'reports/displayDashboard.do?method=save';
        //G5.props.URL_JSON_REPORTS_FAVORITES_REORDER = 'reports/displayDashboard.do?method=reOrder';
        G5.props.URL_JSON_REPORTS_MODULE = 'reports/displayDashboard.do?method=display';
        G5.props.URL_REPORTS_ALL = 'reports/allReports.do';
        G5.props.CYCLE_SLIDE_TRANSITION_PAUSE = ${sliderRefreshRate};

        G5.props.URL_JSON_APPROVALS_LOAD_DATA = G5.props.URL_ROOT+'claim/approvalsCountAction.do?method=fetchApprovalsForTileAndList';


		G5.props.URL_JSON_NOMINATIONS_WINNERS_LIST = G5.props.URL_ROOT+'nomination/viewNominationPastWinnersList.do?method=nominationsWinnersList';
		G5.props.URL_JSON_SSI_AVAILABLE_CONTEST_TYPES = G5.props.URL_ROOT+'ssi/createContest.do?method=fetchAvailableContestTypes';

		// TODO
		// Search ajax calls if any
		G5.props.URL_JSON_SEARCH_MODULE= '';

		// endpoint for getting the promotion rules
		G5.props.URL_JSON_RULES = '${pageContext.request.contextPath}/rules.do?method=fetchPromotionRules';
		
		//Client customization
		G5.props.URL_JSON_CAREERMOMENTS_RIBBON_DATA = '${pageContext.request.contextPath}/careerMomentsData.do?method=fetchCareerMomentsData&tile=true';
		G5.props.URL_BUNCHBALL_WIDGET = '${pageContext.request.contextPath}/bunchWidget.do';
		G5.props.URL_JSON_CAREERMOMENTS_UPLOAD_PHOTO = G5.props.URL_ROOT+'saveComments.do?method=processPhoto';
		G5.props.URL_JSON_CAREERMOMENTS_DATA = G5.props.URL_ROOT+'careerMomentsData.do?method=fetchDataForDetail';
		G5.props.URL_JSON_CAREERMOMENTS_SEARCH_RESULTS = G5.props.URL_ROOT+'careerMomentsData.do?method=fetchDataForDetail';
		

		

        /*  ****************************
            add modules to homeApp
            ****************************/
        hapv.launchApp.moduleCollection.reset([
            <c:forEach var="moduleItem" items="${filteredModuleList}">
    			{
                    name:'<c:out value="${moduleItem.module.tileMappingType.code}"/>',
                    appName: '<c:out value="${moduleItem.module.appName}"/>',//name of the app
                    <c:if test="${moduleItem.module.templateName != null}">
                	  templateName: '<c:out value="${moduleItem.module.templateName}"/>',	// use this for template lookup instead of name
                	</c:if>
                    <c:if test="${moduleItem.module.viewName != null}">
               		  viewName: '<c:out value="${moduleItem.module.viewName}"/>',		//override name for view object
               		</c:if>
               	    filters:{
                    	<c:forEach var="moduleFilter" items="${moduleItem.moduleDisplay.moduleDisplayMappings}" varStatus="filterStatus">
                            <c:choose>
                				<c:when test="${moduleItem.module.tileMappingType.code == 'heroModule'}">
        							'<c:out value="${moduleFilter.filterName}"/>':{order:'<c:out value="${moduleFilter.order}"/>', searchEnabled:<c:out value="${enableMyGroups}"/>}
                				</c:when>
                    			<c:otherwise>
                					'<c:out value="${moduleFilter.filterName}"/>':{order:'<c:out value="${moduleFilter.order}"/>'}
                    			</c:otherwise>
                			</c:choose>
            				<c:if test="${filterStatus.index < fn:length(moduleItem.moduleDisplay.moduleDisplayMappings) - 1 }">,</c:if>
            			</c:forEach>
                    }
                },
            </c:forEach>
			<c:if test="${reportEligible}">
             {
                name:'reportsDashboardModule',
                appName: 'reports',
                filters:{                	
                	<c:forEach var="filterItem" items="${filterList}" varStatus="filterItemStatus">
                    	<c:choose>
        					<c:when test="${filterItem.code == 'reports'}">
        						'<c:out value="${filterItem.code}"/>':{order:0}
        					</c:when>
            				<c:otherwise>
            					'<c:out value="${filterItem.code}"/>':{order:'hidden'}
            				</c:otherwise>
        				</c:choose>
        				<c:if test="${filterItemStatus.index < fn:length(filterList) - 1 }">,</c:if>
    				</c:forEach>
                }
            },
			</c:if>
		]);// /.reset([...])	
		

     

	});

</script>
<c:forEach items="${filteredModuleList}" var="filteredModule" >
	<c:if test="${filteredModule.module.tileMappingType.code == 'onTheSpotCardModule' }">
		<tiles:insert definition="activities.onthespot" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'plateauAwardsModule' }">
		<tiles:insert definition="activities.plateau.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'xpromoModule' }">
		<tiles:insert definition="xpromo.module" flush="true" ignore="true"/>
 	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'publicRecognitionModule' }">
		<tiles:insert definition="public.recognition.module" flush="true" ignore="true"/>
 	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'newsModule' }">
		<tiles:insert definition="news.module" flush="true" ignore="true"/>
	</c:if>
	<%-- <c:if test="${filteredModule.module.tileMappingType.code == 'celebrationModule' }">
		<tiles:insert definition="celebration.module" flush="true" ignore="true"/>
	</c:if> --%>
	<c:if test="${filteredModule.module.tileMappingType.code == 'purlCelebrateModule' }">
		<tiles:insert definition="purl.celebration.module" flush="true" ignore="true"/>
	</c:if>
	<%-- <c:if test="${filteredModule.module.tileMappingType.code == 'forumModule' }">
		<tiles:insert definition="forum.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'forumModule' }">
		<tiles:insert definition="forum.module.item" flush="true" ignore="true"/>
	</c:if> --%>
	<c:if test="${filteredModule.module.tileMappingType.code == 'resourceCenterModule' }">
		<tiles:insert definition="social.resources.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'leaderboardModule' }">
		<tiles:insert definition="leaderBoard.ranking.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'tipModule' }">
		<tiles:insert definition="daily.tip.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'managerToolkitModule' }">
		<tiles:insert definition="manager.toolkit.module" flush="true" ignore="true"/>
	</c:if>
	<%-- <c:if test="${filteredModule.module.tileMappingType.code == 'quizModule' }">
		<tiles:insert definition="quiz.module" flush="true" ignore="true"/>
	</c:if> --%>

	<c:if test="${filteredModule.module.tileMappingType.code == 'approvalsModule' }">
		<tiles:insert definition="approvals.module" flush="true" ignore="true"/>
	</c:if>
	<%-- <c:if test="${filteredModule.module.tileMappingType.code== 'purlRecognitionModule' }">
		<tiles:insert definition="purl.recognition.module" flush="true" ignore="true"/>
	</c:if> --%>
	<c:if test="${filteredModule.module.tileMappingType.code == 'bannerModuleModule'}">
		<tiles:insert definition="banner.page" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'nominationsWinnersModule'}">
		<tiles:insert definition="nominations.winners.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'goalquestModule'}">
		<tiles:insert definition="goalquest.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'challengepointModule'}">
		<tiles:insert definition="challengepoint.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'goalquestManagerModule'}">
		<tiles:insert definition="managergq.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'challengepointManagerModule'}">
		<tiles:insert definition="managercp.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'claimModule' }">
		<tiles:insert definition="product.claim.module" flush="true" ignore="true"/>
 	</c:if>

	<c:if test="${filteredModule.module.tileMappingType.code == 'smackTalkModule' }">
		<tiles:insert definition="throwdown.smacktalk.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownNewsModule' }">
		<tiles:insert definition="throwdown.throwdownnews.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownTrainingCampModule' }">
		<tiles:insert definition="throwdown.trainingcamp.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownStandingsModule' }">
		<tiles:insert definition="throwdown.standings.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownPromoSelectModule' }">
		<tiles:insert definition="throwdown.promoselect.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownMatchModule' }">
		<tiles:insert definition="throwdown.matchsummary.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownRankingsModule' }">
		<tiles:insert definition="throwdown.rankings.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownAllMatchesModule' }">
		<tiles:insert definition="throwdown.matchlist.module" flush="true" ignore="true"/>
 	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'instantPollModule' }">
		<tiles:insert definition="instant.poll.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'SSIParticipantModule' }">
		<tiles:insert definition="ssi.participant.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'SSIManagerModule' }">
		<tiles:insert definition="ssi.manager.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'SSICreatorModule' }">
		<tiles:insert definition="ssi.creator.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'SSICreatorContestsModule' }">
		<tiles:insert definition="ssi.creatorcontests.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'plateauAwardsRedeemModule' }">
		<tiles:insert definition="plateau.awards.redeem.module" flush="true" ignore="true"/>
	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'heroModule'}">
		<tiles:insert definition="hero.module" flush="true" ignore="true"/>
	</c:if>
 	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownCombinedModule' }">
		<tiles:insert definition="throwdown.combined.module" flush="true" ignore="true"/>
	</c:if>
	<%--Enabling New Service Anniversary Celebration Module Tile --%>
	<c:if test="${filteredModule.module.tileMappingType.code == 'newServiceAnniversaryModule' }">
		<tiles:insert definition="new.service.anniversary.celebration.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'careerMomentsModule' }">
		<tiles:insert definition="career.moments.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'bunchBallModule' }">
		<tiles:insert definition="bunchball.module" flush="true" ignore="true"/>
	</c:if>
</c:forEach>

	<%--moved reportsmodule outside of the loop --%>
	<c:if test="${reportEligible}">
	<tiles:insert definition="reports.dashboard.module" flush="true" ignore="true"/>
	</c:if>


<c:if test="${not empty purlInvites}">
	<tiles:insert definition="purl.invite.results.modal" flush="true" ignore="true">
		<tiles:put name="autoModal" value="autoModal"/>
	</tiles:insert>
</c:if>

<c:if test="${budgetConfirmation == true}">
	<tiles:insert definition="budget.transfer.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${contactUsEmailConfirmation == true}">
	<tiles:insert definition="emailSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty recognitionSentBean && not empty raRecognitionFlowBean}">
	<tiles:insert definition="ra.recognitionSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty recognitionSentBean && empty raRecognitionFlowBean}">
	<tiles:insert definition="recognitionSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty invites}">
	<tiles:insert definition="purlInvitationSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${sendAlertsuccess == true}">
	<tiles:insert definition="alertSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>
<c:if test="${ goalSelectionModal == true}">
	<tiles:insert definition="select.goal.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${awardRemindersSent > 0}">
	<tiles:insert definition="plateauawardreminders.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty claimSubmittedBean}">
	<tiles:insert definition="claim.submission.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${surveySubmitConfirmation == true}">
	<tiles:insert definition="survey.submit.success.confirmation.modal" flush="true" ignore="true" />
</c:if>


<tiles:insert definition="engagement.manager.module" flush="true" ignore="true"/>



<tiles:insert definition="nomination.winner.modal.tpl" flush="true" ignore="true" />

<tiles:insert definition="budget.tracker.module" flush="true" ignore="true"/>
<tiles:insert definition="social.gamification.module" flush="true" ignore="true"/>
<tiles:insert definition="work.happier.module" flush="true" ignore="true"/>
<tiles:insert definition="engagement.module" flush="true" ignore="true"/>
<tiles:insert name="instant.poll.module" flush="true" ignore="true"/>
<tiles:insert definition="bunchball.sidebar.module" flush="true" ignore="true"/><!-- Client customization -->



<c:if test="${sessionScope.eligiblePromotionsExist == true}">
   <tiles:insert definition="recognition.module" flush="true" ignore="true"/>
</c:if>