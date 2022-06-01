<%@ include file="/include/taglib.jspf" %>
    <!-- JavaScript at the bottom for fast page loading: http://developer.yahoo.com/performance/rules.html#js_bottom -->

    <script>var G5_CONTEXT_PATH = "${pageContext.request.contextPath}";</script>
    <c:choose>
    <c:when test="${isfrontEndDebug}">

        <!--
         *                                           oo
         *
         *    .d8888b. .d8888b. 88d888b. .d8888b.    dP .d8888b.
         *    88'  `"" 88'  `88 88'  `88 88ooood8    88 Y8ooooo.
         *    88.  ... 88.  .88 88       88.  ... dP 88       88
         *    `88888P' `88888P' dP       `88888P' 88 88 `88888P'
         *                                           88
         *                                           dP
         -->
        <!-- Libraries -->
        <script src="${siteUrlPrefix}/assets/libs/safeconsole.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/underscore.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/backbone.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/handlebars.js"></script>
        <!-- FED PLUGIN UPDATE -->
        <!--<script src="${siteUrlPrefix}/assets/libs/json2.js"></script>-->
        <script src="${siteUrlPrefix}/assets/libs/jquery-ui.js"></script>

        <!-- Plugins -->
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-transition.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-modal-patch.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-tooltip.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-popover.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-alert.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-collapse.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-datepicker.patchedG5.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-slider.patchedG5.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-tab.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-button.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/bootstrap-dropdown.js"></script>
        <!-- FED PLUGIN UPDATE -->
        <!--<script src="${siteUrlPrefix}/assets/libs/plugins/jquery.cycle.all.js"></script>-->
        <!-- FED PLUGIN UPDATE -->
        <!--<script src="${siteUrlPrefix}/assets/libs/plugins/jquery-animate-background.js"></script>-->
        <script src="${siteUrlPrefix}/assets/libs/plugins/shake-animator.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/spin.patchedG5.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/number-animator.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.scrollTo.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.color.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.chosen.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.qtip.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.elastic.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.ellipsis.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.placeholder.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.query.js"></script>
        <!-- FED PLUGIN UPDATE -->
        <!--<script src="${siteUrlPrefix}/assets/libs/plugins/jquery.ui.widget.js"></script>-->
        <script src="${siteUrlPrefix}/assets/libs/plugins/jHtmlArea-0.8.patchedG5.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.spellchecker.patchedG5.js"></script>
        <!-- FED PLUGIN UPDATE -->
        <!--<script src="${siteUrlPrefix}/assets/libs/plugins/jquery.iframe-transport.js"></script>-->
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.fileupload.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/jquery.format.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/responsiveTables.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/video-js/video.min.js"></script>
        <script src="${siteUrlPrefix}/assets/libs/plugins/slick.min.js"></script>

        <!-- FED PLUGIN UPDATE -->
        <script src="${siteUrlPrefix}/assets/libs/plugin-patch.js" ></script>
	
		<!-- FED PLUGIN FOR TEXTAREA RESIZE FOR IE -->
        <script src="${siteUrlPrefix}/assets/libs/plugins/polyfill-resize.js" ></script>

        <!-- Application core scripts -->
        <script src="${siteUrlPrefix}/assets/js/settings.js"></script>
        <script src="${siteUrlPrefix}/assets/js/settings.i18n.js"></script>
        <script src="${siteUrlPrefix}/assets/js/G5.util.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ServerResponse.js"></script>
        <script src="${siteUrlPrefix}/assets/js/TemplateManager.js"></script>

        <script src="${siteUrlPrefix}/assets/js/PageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GlobalNavRouter.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GlobalNavView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GlobalHeaderView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GlobalSidebarView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GlobalFooterView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SidebarModule.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SidebarModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PageNavView.js"></script>

        <script src="${siteUrlPrefix}/assets/js/ParticipantCollectionView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ParticipantProfileView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ParticipantPopoverView.js"></script>
        
        <!-- Client customizations for WIP #62128  -->
        <script src="${siteUrlPrefix}/assets/js/CheersPopoverView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CheersRecognitionEzView.js"></script>
        
        <!-- FED PLUGIN UPDATE -->
        <!--<script src="${siteUrlPrefix}/assets/js/ParticipantSearchView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ParticipantSearchModel.js"></script>-->
        <script src="${siteUrlPrefix}/assets/js/SharePopoverPlugin.js"></script>
        <script src="${siteUrlPrefix}/assets/js/BreadcrumbView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaginationView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/DisplayTableAjaxView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/WizardTabsView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ParticipantChatterAuthorizationView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SelectAudienceParticipantsView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SelectAudienceParticipantsModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ParticipantPaginatedView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/BadgesSelectorView.js"></script>

        <!-- drawTool -->
        <script src="${siteUrlPrefix}/assets/js/DrawToolView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/DrawCanvasView.js"></script>

        <!-- new pax search -->
        <script src="${siteUrlPrefix}/assets/js/PaxSearchStartView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSearchCollectionView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSearchCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSelectedPaxView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSelectedPaxCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSearchModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSearchView.js"></script>

        <!-- homeApp
        <script src="${siteUrlPrefix}/assets/js/BinPacker.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ModuleLayoutManager.js"></script>
        <script src="${siteUrlPrefix}/assets/js/Module.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ModuleCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ModuleContainerView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/HomeApp.js"></script>
        <script src="${siteUrlPrefix}/assets/js/HomeAppPageView.js"></script>
        -->
        <!-- Launch -->
        <script src="${siteUrlPrefix}/assets/js/LaunchApp.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LaunchPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LaunchModuleContainerView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LaunchModuleLayoutManager.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LaunchModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LaunchModule.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LaunchModuleCollection.js"></script>

        <!-- contactForm -->
        <script src="${siteUrlPrefix}/assets/js/ContactFormView.js"></script>

        <!--
         *    dP                   oo             oo
         *    88
         *    88 .d8888b. .d8888b. dP 88d888b.    dP .d8888b.
         *    88 88'  `88 88'  `88 88 88'  `88    88 Y8ooooo.
         *    88 88.  .88 88.  .88 88 88    88 dP 88       88
         *    dP `88888P' `8888P88 dP dP    dP 88 88 `88888P'
         *                     .88                88
         *                 d8888P                 dP
         -->
        <script src="${siteUrlPrefix}/assets/js/LoginFormView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LoginPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LoginPageFirstTimeView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ChangePasswordPageView.js"></script>

        <!--
         *                                           oo
         *
         *    .d8888b. 88d888b. 88d888b. .d8888b.    dP .d8888b.
         *    88'  `88 88'  `88 88'  `88 Y8ooooo.    88 Y8ooooo.
         *    88.  .88 88.  .88 88.  .88       88 dP 88       88
         *    `88888P8 88Y888P' 88Y888P' `88888P' 88 88 `88888P'
         *             88       88                   88
         *             dP       dP                   dP
         -->
        <!-- bannerModule -->
        <script src="${siteUrlPrefix}/assets/js/BannerModuleModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/BannerModuleCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/BannerModuleModuleView.js"></script>

        <!-- budgetTracker -->
        <script src="${siteUrlPrefix}/assets/js/BudgetCollectionView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/BudgetTrackerModuleView.js"></script>

        <!-- gamification -->
        <script src="${siteUrlPrefix}/assets/js/GamificationCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GamificationDataView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/GamificationModuleView.js"></script>

        <!-- hero -->
        <script src="${siteUrlPrefix}/assets/js/HeroModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/HeroModuleCollection.js"></script>

		<%--<c:if test="${ USER_FED_RESOURCES.instantPollsEnabled }">--%>
            <!-- instantPoll -->
            <script src="${siteUrlPrefix}/assets/js/InstantPollCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/InstantPollModuleView.js"></script>
		<%--</c:if>--%>

        <!-- resourceCenter -->
        <!-- out of alpha order on purpose (managerToolkit requires it) -->
        <script src="${siteUrlPrefix}/assets/js/ResourceCenterModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ResourceCenterPageView.js"></script>

        <!-- managerToolkit -->
        <script src="${siteUrlPrefix}/assets/js/ManagerToolkitModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ManagerToolkitPageBudgetView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ManagerToolkitPageHistoryView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ManagerToolkitPageRosterView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ManagerToolkitPageRosterEditView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ManagerToolKitSendAlertPageView.js"></script>

        <!-- news -->
        <script src="${siteUrlPrefix}/assets/js/NewsModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/NewsModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/NewsCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/NewsCollectionView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/NewsPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/NewsPageDetailView.js"></script>

        <!-- onTheSpotCard -->
        <script src="${siteUrlPrefix}/assets/js/OnTheSpotCardModuleView.js"></script>

        <!-- plateauAwards -->
        <script src="${siteUrlPrefix}/assets/js/PlateauAwardsPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PlateauAwardsPageReminderView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PlateauAwardsCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PlateauAwardsModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PlateauAwardsRedeemModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PlateauAwardsRedeemModel.js"></script>

        <!-- profile page shell -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageView.js"></script>

        <!-- profile page activity history tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageActivityHistoryTabView.js"></script>

        <!-- profile page alerts and messages tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabAlertsModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabMessagesModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabMessagesCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabAlertsCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabAlertsView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabMessagesView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabMessageDetailModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabMessageDetailCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsTabMessageDetailView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageAlertsAndMessagesTabView.js"></script>

        <!-- profile page badges tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageBadgesTabCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePageBadgesTabView.js"></script>

        <!-- profile page dashboard (engagement) tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageDashboardTabView.js"></script>

        <!-- profile page follow list tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageFollowListTabView.js"></script>

        <!--profile page personal info tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePagePersonalInfoTabView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePagePersonalInformationTabModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProfilePagePersonalInformationTabCollection.js"></script>

        <!-- profile page preferences tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePagePreferencesTabView.js"></script>

        <!-- profile page proxies tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageProxiesTabView.js"></script>

        <!-- profile page security tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageSecurityTabView.js"></script>

        <!-- profile page personal information tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageStatementTabView.js"></script>

        <!-- profile page throwdown player stats tab -->
        <script src="${siteUrlPrefix}/assets/js/ProfilePageThrowdownStatsTabView.js"></script>

        <!-- publicProfile -->
        <script src="${siteUrlPrefix}/assets/js/publicProfilePageView.js"></script>

        <!-- programSelect -->
        <script src="${siteUrlPrefix}/assets/js/ProgramSelectPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ProgramSelectPageModel.js"></script>

        <!-- rulesPage -->
        <script src="${siteUrlPrefix}/assets/js/RulesPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/RulesPromotionModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/RulesPromotionModelView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/RulesPromotionCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/RulesPromotionCollectionView.js"></script>

        <!-- survey -->
        <script src="${siteUrlPrefix}/assets/js/SurveyModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SurveyTakeView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SurveyPageListView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/SurveyPageTakeView.js"></script>

        <!-- tip -->
        <script src="${siteUrlPrefix}/assets/js/TipModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/TipCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/TipModuleView.js"></script>

        <!-- WorkHappier -->
        <script src="${siteUrlPrefix}/assets/js/BIWHappinessSlider.min.js"></script>
        <script src="${siteUrlPrefix}/assets/js/WorkHappierPaxModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/WorkHappierPaxModel.js"></script>

        <!-- xpromo -->
        <script src="${siteUrlPrefix}/assets/js/xpromoModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/xpromoModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/xpromoCollection.js"></script>

        <!--
         *                                                                                                       dP             oo
         *                                                                                                       88
         *    .d8888b. 88d888b. 88d888b.          .d8888b. 88d888b. 88d888b. 88d888b. .d8888b. dP   .dP .d8888b. 88 .d8888b.    dP .d8888b.
         *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88'  `88 88'  `88 88'  `88 88'  `88 88   d8' 88'  `88 88 Y8ooooo.    88 Y8ooooo.
         *    88.  .88 88.  .88 88.  .88          88.  .88 88.  .88 88.  .88 88       88.  .88 88 .88'  88.  .88 88       88 dP 88       88
         *    `88888P8 88Y888P' 88Y888P'          `88888P8 88Y888P' 88Y888P' dP       `88888P' 8888P'   `88888P8 dP `88888P' 88 88 `88888P'
         *             88       88                         88       88                                                          88
         *             dP       dP                         dP       dP                                                          dP
         -->
        <!-- approvals -->
        <script src="${siteUrlPrefix}/assets/js/ApprovalsModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsIndexModelView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsSearchModelView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsClaimDetailModelView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsNominationDetailModelView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsPageClaimsModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/ApprovalsPageClaimsView.js"></script>

        <!--
         *                                                          dP          dP                           dP   oo                      oo
         *                                                          88          88                           88
         *    .d8888b. 88d888b. 88d888b.          .d8888b. .d8888b. 88 .d8888b. 88d888b. 88d888b. .d8888b. d8888P dP .d8888b. 88d888b.    dP .d8888b.
         *    88'  `88 88'  `88 88'  `88 88888888 88'  `"" 88ooood8 88 88ooood8 88'  `88 88'  `88 88'  `88   88   88 88'  `88 88'  `88    88 Y8ooooo.
         *    88.  .88 88.  .88 88.  .88          88.  ... 88.  ... 88 88.  ... 88.  .88 88       88.  .88   88   88 88.  .88 88    88 dP 88       88
         *    `88888P8 88Y888P' 88Y888P'          `88888P' `88888P' dP `88888P' 88Y8888' dP       `88888P8   dP   dP `88888P' dP    dP 88 88 `88888P'
         *             88       88                                                                                                        88
         *             dP       dP                                                                                                        dP
         -->
        <!-- celebration -->
        <script src="${siteUrlPrefix}/assets/js/CelebrationPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationChooseAwardModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationBrowseAwardsModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationAnniversaryFactsModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationCompanyTimelineModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationCongratsModuleView.js"></script>
        <!--<script src="${siteUrlPrefix}/assets/js/CelebrationEcardModuleView.js"></script>-->
        <script src="${siteUrlPrefix}/assets/js/CelebrationManagerMessageModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationManagerMessagePageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationRecognitionPurlModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationRecognitionPurlModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationCorporateVideoModuleView.js"></script>
        <!--<script src="${siteUrlPrefix}/assets/js/CelebrationImageFillerModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/CelebrationImageFillerModel.js"></script>-->

		<c:if test="${ USER_FED_RESOURCES.productClaimsEnabled }">
            <!--
             *                                                 dP          oo                        oo
             *                                                 88
             *    .d8888b. 88d888b. 88d888b.          .d8888b. 88 .d8888b. dP 88d8b.d8b. .d8888b.    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `"" 88 88'  `88 88 88'`88'`88 Y8ooooo.    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88.  ... 88 88.  .88 88 88  88  88       88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          `88888P' dP `88888P8 dP dP  dP  dP `88888P' 88 88 `88888P'
             *             88       88                                                               88
             *             dP       dP                                                               dP
             -->
            <!-- claim -->
            <script src="${siteUrlPrefix}/assets/js/ClaimProductModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ClaimProductCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ClaimModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ClaimPageView.js"></script>
		</c:if>

		<c:if test="${ USER_FED_RESOURCES.diyCommunicationsEnabled }">
            <!--
             *                                                                                                  oo                     dP   oo                               oo
             *                                                                                                                         88
             *    .d8888b. 88d888b. 88d888b.          .d8888b. .d8888b. 88d8b.d8b. 88d8b.d8b. dP    dP 88d888b. dP .d8888b. .d8888b. d8888P dP .d8888b. 88d888b. .d8888b.    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `"" 88'  `88 88'`88'`88 88'`88'`88 88    88 88'  `88 88 88'  `"" 88'  `88   88   88 88'  `88 88'  `88 Y8ooooo.    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88.  ... 88.  .88 88  88  88 88  88  88 88.  .88 88    88 88 88.  ... 88.  .88   88   88 88.  .88 88    88       88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          `88888P' `88888P' dP  dP  dP dP  dP  dP `88888P' dP    dP dP `88888P' `88888P8   dP   dP `88888P' dP    dP `88888P' 88 88 `88888P'
             *             88       88                                                                                                                                       88
             *             dP       dP                                                                                                                                       dP
             -->
            <!-- communications -->
            <script src="${siteUrlPrefix}/assets/js/CommunicationsPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/CommunicationsManageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/CommunicationsManageModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/CommunicationsEditView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/CommunicationsEditModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/CommunicationsImageUploadView.js"></script>
		</c:if>

		<c:if test="${ USER_FED_RESOURCES.engagementEnabled }">
            <!--
             *                                                                                                                             dP      oo
             *                                                                                                                             88
             *    .d8888b. 88d888b. 88d888b.          .d8888b. 88d888b. .d8888b. .d8888b. .d8888b. .d8888b. 88d8b.d8b. .d8888b. 88d888b. d8888P    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88ooood8 88'  `88 88'  `88 88'  `88 88'  `88 88ooood8 88'`88'`88 88ooood8 88'  `88   88      88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88.  ... 88    88 88.  .88 88.  .88 88.  .88 88.  ... 88  88  88 88.  ... 88    88   88   dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          `88888P' dP    dP `8888P88 `88888P8 `8888P88 `88888P' dP  dP  dP `88888P' dP    dP   dP   88 88 `88888P'
             *             88       88                                       .88               .88                                                 88
             *             dP       dP                                   d8888P            d8888P                                                  dP
             -->
            <!-- engagement -->
            <script src="${siteUrlPrefix}/assets/js/EngagementDrawScoreMeter.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementSummaryModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementSummaryModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementSummaryCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementSummaryCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailScoreView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailRecView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailRecSentView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailRecRecvView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailPaxView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailPaxRecToView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailPaxRecByView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementDetailVisitsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementRecognizedModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementRecognizedModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementTeamMembersModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementTeamMembersCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementTeamMembersCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementModuleManagerView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementPageDashboardView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementPageUserDashboardView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementPageTeamDashboardView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementPageRecognizedView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/EngagementTeamMembersView.js"></script>
		</c:if>

		<c:if test="${ USER_FED_RESOURCES.goalquestEnabled }">
            <!--
             *                                                                   dP                                       dP      oo
             *                                                                   88                                       88
             *    .d8888b. 88d888b. 88d888b.          .d8888b. .d8888b. .d8888b. 88 .d8888b. dP    dP .d8888b. .d8888b. d8888P    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88'  `88 88'  `88 88 88'  `88 88    88 88ooood8 Y8ooooo.   88      88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88.  .88 88.  .88 88.  .88 88 88.  .88 88.  .88 88.  ...       88   88   dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          `8888P88 `88888P' `88888P8 dP `8888P88 `88888P' `88888P' `88888P'   dP   88 88 `88888P'
             *             88       88                     .88                            88                                      88
             *             dP       dP                 d8888P                             dP                                      dP
             -->
            <!-- goalquest -->
            <script src="${siteUrlPrefix}/assets/js/GoalquestCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/GoalquestModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/GoalquestManagerModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ChallengepointModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ChallengepointManagerModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/GoalquestPageListView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/GoalquestPageEditView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/GoalquestPageDetailView.js"></script>
		</c:if>

        <!--
         *                                        dP                         dP                   dP                                        dP    oo
         *                                        88                         88                   88                                        88
         *    .d8888b. 88d888b. 88d888b.          88 .d8888b. .d8888b. .d888b88 .d8888b. 88d888b. 88d888b. .d8888b. .d8888b. 88d888b. .d888b88    dP .d8888b.
         *    88'  `88 88'  `88 88'  `88 88888888 88 88ooood8 88'  `88 88'  `88 88ooood8 88'  `88 88'  `88 88'  `88 88'  `88 88'  `88 88'  `88    88 Y8ooooo.
         *    88.  .88 88.  .88 88.  .88          88 88.  ... 88.  .88 88.  .88 88.  ... 88       88.  .88 88.  .88 88.  .88 88       88.  .88 dP 88       88
         *    `88888P8 88Y888P' 88Y888P'          dP `88888P' `88888P8 `88888P8 `88888P' dP       88Y8888' `88888P' `88888P8 dP       `88888P8 88 88 `88888P'
         *             88       88                                                                                                                88
         *             dP       dP                                                                                                                dP
         -->
        <!-- leaderboard -->
        <script src="${siteUrlPrefix}/assets/js/LeaderboardModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardSetModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardSetCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardModelView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/LeaderboardPageCreateEditCopyView.js"></script>

		<c:if test="${ USER_FED_RESOURCES.nominationsEnabled }">
            <!--
             *                                                                     oo                     dP   oo                               oo
             *                                                                                            88
             *    .d8888b. 88d888b. 88d888b.          88d888b. .d8888b. 88d8b.d8b. dP 88d888b. .d8888b. d8888P dP .d8888b. 88d888b. .d8888b.    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88'  `88 88'`88'`88 88 88'  `88 88'  `88   88   88 88'  `88 88'  `88 Y8ooooo.    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88    88 88.  .88 88  88  88 88 88    88 88.  .88   88   88 88.  .88 88    88       88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          dP    dP `88888P' dP  dP  dP dP dP    dP `88888P8   dP   dP `88888P' dP    dP `88888P' 88 88 `88888P'
             *             88       88                                                                                                          88
             *             dP       dP                                                                                                          dP
             -->
            <!-- Nominations -->
            <script src="${siteUrlPrefix}/assets/js/NominationsWizardTabsVerticalView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsWizardTabsVerticalModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitTabNominatingView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitTabNomineeView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitTabBehaviorView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitTabEcardView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitTabWhyView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsSubmitModel.js"></script>

            <script src="${siteUrlPrefix}/assets/js/NominationsInprogressListPageModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsInprogressListPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/NominationsApprovalPromoListPageModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsApprovalPromoListPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsApprovalPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsApprovalPageModel.js"></script>

            <script src="${siteUrlPrefix}/assets/js/NominationWinnersListPageModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationWinnersListPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/NominationsWinnersModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsWinnersDetailPageModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsWinnersDetailPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/NominationsMyNomsListPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsMyNomsListPageModel.js"></script>

            <script src="${siteUrlPrefix}/assets/js/NominationsMoreInfoPageModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/NominationsMoreInfoPageView.js"></script>
		</c:if>

        <!--
         *                                                                   dP    oo
         *                                                                   88
         *    .d8888b. 88d888b. 88d888b.          88d888b. dP    dP 88d888b. 88    dP .d8888b.
         *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88    88 88'  `88 88    88 Y8ooooo.
         *    88.  .88 88.  .88 88.  .88          88.  .88 88.  .88 88       88 dP 88       88
         *    `88888P8 88Y888P' 88Y888P'          88Y888P' `88888P' dP       dP 88 88 `88888P'
         *             88       88                88                               88
         *             dP       dP                dP                               dP
         -->
        <!-- purlContribute -->
        <script src="${siteUrlPrefix}/assets/js/purlContributeModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/purlContributeCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/purlContributeTermsPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/purlContributePurlListPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PurlPageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PurlModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PurlCelebrateModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PurlCelebratePageView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PurlCelebrateModuleView.js"></script>

		<c:if test="${ USER_FED_RESOURCES.quizesEnabled }">
            <!--
             *                                                          oo             oo
             *
             *    .d8888b. 88d888b. 88d888b.          .d8888b. dP    dP dP d888888b    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88    88 88    .d8P'    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88.  .88 88.  .88 88  .Y8P    dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          `8888P88 `88888P' dP d888888P 88 88 `88888P'
             *             88       88                      88                         88
             *             dP       dP                      dP                         dP
             -->
            <!-- quiz -->
            <!-- old take a quiz -->
            <script src="${siteUrlPrefix}/assets/js/QuizPageView.js"></script>
            <!-- new take a quiz -->
            <script src="${siteUrlPrefix}/assets/js/QuizModelTake.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizPageTakeView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizTakeTabIntroView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizTakeTabMaterialsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizTakeTabQuestionsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizTakeTabResultsView.js"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.diyQuizesEnabled }">
            <!-- diy quiz -->
            <script src="${siteUrlPrefix}/assets/js/QuizModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizPageEditView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizEditTabIntroView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizEditTabMaterialsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizEditTabQuestionsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizEditTabResultsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizEditTabPreviewView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/QuizPageManageView.js"></script>
        </c:if>

		<c:if test="${ USER_FED_RESOURCES.recognitionsEnabled }">
            <!--
             *                                                                                              oo   dP   oo                      oo
             *                                                                                                   88
             *    .d8888b. 88d888b. 88d888b.          88d888b. .d8888b. .d8888b. .d8888b. .d8888b. 88d888b. dP d8888P dP .d8888b. 88d888b.    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88ooood8 88'  `"" 88'  `88 88'  `88 88'  `88 88   88   88 88'  `88 88'  `88    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88       88.  ... 88.  ... 88.  .88 88.  .88 88    88 88   88   88 88.  .88 88    88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          dP       `88888P' `88888P' `88888P' `8888P88 dP    dP dP   dP   dP `88888P' dP    dP 88 88 `88888P'
             *             88       88                                                         .88                                            88
             *             dP       dP                                                     d8888P                                             dP
             -->
            <!-- publicRecognition -->
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionSetModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionSetCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionSetCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionPageDetailView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/PublicRecognitionPageFollowListView.js"></script>

            <!-- recognition/PURL -->
            <script src="${siteUrlPrefix}/assets/js/RecognitionPageSendView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/RecognitionPageManagerInviteContributorsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/AwardeeCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ContributorsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/RecognitionPageAddPointsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/RecognitionEzView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/RecognitionPageBadgesView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/RecognitionPageRecogView.js"></script>

            <!-- recognition preview pages -->
            <script src="${siteUrlPrefix}/assets/js/RecognitionPagePreviewView.js"></script>
        </c:if>

		<c:if test="${ USER_FED_RESOURCES.reportsEnabled }">
            <!--
             *                                                                                       dP               oo
             *                                                                                       88
             *    .d8888b. 88d888b. 88d888b.          88d888b. .d8888b. 88d888b. .d8888b. 88d888b. d8888P .d8888b.    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 88'  `88 88ooood8 88'  `88 88'  `88 88'  `88   88   Y8ooooo.    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88          88       88.  ... 88.  .88 88.  .88 88         88         88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          dP       `88888P' 88Y888P' `88888P' dP         dP   `88888P' 88 88 `88888P'
             *             88       88                                  88                                            88
             *             dP       dP                                  dP                                            dP
             -->
            <!-- reports -->
            <script src="${siteUrlPrefix}/assets/js/ReportsDashboardModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportsModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/AllReportsModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/FavoriteReportsModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportsPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportsPageAllView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportsPageDetailView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportsFavoritesPopoverView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ReportsChangeFiltersPopoverView.js"></script>
        </c:if>

		<c:if test="${ USER_FED_RESOURCES.ssiEnabled }">
            <!--
             *                                                          oo    oo
             *
             *    .d8888b. 88d888b. 88d888b.          .d8888b. .d8888b. dP    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888 Y8ooooo. Y8ooooo. 88    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88                88       88 88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'          `88888P' `88888P' dP 88 88 `88888P'
             *             88       88                                        88
             *             dP       dP                                        dP
             -->
            <!-- ssi -->
            <script src="${siteUrlPrefix}/assets/js/SSIModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISharedHelpersView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISharedPaginatedTableView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIEventBus.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIDTGTActivityModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIDTGTActivityCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIDTGTActivityModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIDTGTActivityCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISIULevelModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISIULevelCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISIULevelModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISIULevelCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestSummaryCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestSummary_ATN.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestPayoutDetailsCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestPayoutDetails_ATN.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApproveContestPageViewATN.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestPageEditView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTranslationsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabInfoView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabParticipantsManagersView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabPayoutsView_objectives.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabPayoutsView_dtgt.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabPayoutsView_siu.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabPayoutsView_atn.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabPayoutsView_sr.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabDataCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestEditTabPreviewView.js"></script>

            <!-- ssi nav -->
            <script src="${siteUrlPrefix}/assets/js/SSIPageNavView.js"></script>

            <!-- ssi contest generic -->
            <script src="${siteUrlPrefix}/assets/js/SSIPageCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICircleChartModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICircleChartCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICircleChartView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIBarChartView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIPayoutChartView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIActionPromptView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISortableTableView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISortableTableCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestListView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICreatorContestModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIContestCreateModalView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIStackRankCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIStackRankCollectionView.js"></script>

            <!-- ssi participant -->
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantContestModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantContestView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantDoThisGetThatView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantObjectivesView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantStackRankView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantStepItUpView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantSubmitClaimPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantSubmitClaimModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantSubmitClaimDetailView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIParticipantModuleView.js"></script>

            <!-- ssi manager -->
            <script src="${siteUrlPrefix}/assets/js/SSIManagerPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIManagerModuleView.js"></script>

            <!-- ssi super viewer -->
            <script src="${siteUrlPrefix}/assets/js/SSISuperViewerPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSISuperViewerModuleView.js"></script>

            <!-- ssi creator -->
            <script src="${siteUrlPrefix}/assets/js/SSICreatorPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICreatorModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICreatorListModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSICreatorContestsModuleView.js"></script>

            <!-- ssi admin contest details -->
            <script src="${siteUrlPrefix}/assets/js/SSIAdminContestDetailsView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIAdminContestDetailsModel.js"></script>

            <!-- ssi approve contest -->
            <script src="${siteUrlPrefix}/assets/js/SSIApproveContestPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApproveContestDetailsModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApproveContestMembersCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApproveContestMembersCollectionView.js"></script>

            <!-- ssi approve payout -->
            <script src="${siteUrlPrefix}/assets/js/SSIApprovePayoutView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApprovePayoutCollection.js"></script>

            <!-- ssi activity history -->
            <script src="${siteUrlPrefix}/assets/js/SSIActivityHistoryView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIActivityHistoryModel.js"></script>

            <!-- ssi update results / approve payout -->
            <script src="${siteUrlPrefix}/assets/js/SSIUpdateResultsPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIUpdateResultsModel.js"></script>

            <!-- ssi approve claims -->
            <script src="${siteUrlPrefix}/assets/js/SSIApproveClaimsPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApproveClaimsDetailPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SSIApproveClaimsSummaryCollection.js"></script>
            
        </c:if>

		<c:if test="${ USER_FED_RESOURCES.throwdownEnabled }">
            <!--
             *                                          dP   dP                                          dP                                 oo
             *                                          88   88                                          88
             *    .d8888b. 88d888b. 88d888b.          d8888P 88d888b. 88d888b. .d8888b. dP  dP  dP .d888b88 .d8888b. dP  dP  dP 88d888b.    dP .d8888b.
             *    88'  `88 88'  `88 88'  `88 88888888   88   88'  `88 88'  `88 88'  `88 88  88  88 88'  `88 88'  `88 88  88  88 88'  `88    88 Y8ooooo.
             *    88.  .88 88.  .88 88.  .88            88   88    88 88       88.  .88 88.88b.88' 88.  .88 88.  .88 88.88b.88' 88    88 dP 88       88
             *    `88888P8 88Y888P' 88Y888P'            dP   dP    dP dP       `88888P' 8888P Y8P  `88888P8 `88888P' 8888P Y8P  dP    dP 88 88 `88888P'
             *             88       88                                                                                                      88
             *             dP       dP                                                                                                      dP
             -->
            <!-- throwdown -->
            <script src="${siteUrlPrefix}/assets/js/_throwdown.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownPromoSelectModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownPromoSelectCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownPromoSelectModuleView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownMatchModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownMatchCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownMatchModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownMatchPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownAllMatchesModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownAllMatchesModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownAllMatchesTeamModel.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsSetModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsSetCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownRankingsPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownStandingsModuleView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownStandingsModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownStandingsPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownTrainingCampModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownTrainingCampPageView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownNewsModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownNewsModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownNewsCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownNewsCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownNewsPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/ThrowdownNewsPageDetailView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/ThrowdownCombinedModuleView.js"></script>

            <script src="${siteUrlPrefix}/assets/js/SmackTalkModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkSetModel.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkSetCollection.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkModelView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkSetCollectionView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkModuleView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkPageView.js"></script>
            <script src="${siteUrlPrefix}/assets/js/SmackTalkPageDetailView.js"></script>
		</c:if>


        <!-- NOT INCLUDED IN ANY BUILT SCRIPTS -->
        <!-- testModule -->
        <script src="${siteUrlPrefix}/assets/js/TestModuleView.js"></script>

        <!-- theDude -->
        <script src="${siteUrlPrefix}/assets/js/TheDudeModuleView.js"></script>
        <script src="${siteUrlPrefix}/assets/js/TheDudeListCollection.js"></script>

        <!-- paxSearchReact
        <script src="${siteUrlPrefix}/assets/js/PaxSearchReactModel.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSearchReactCollection.js"></script>
        <script src="${siteUrlPrefix}/assets/js/PaxSearchReactModelView.js"></script>-->

		<script src="${siteUrlPrefix}/assets/js/SelectPointsOrTrainingView.js"></script>
    </c:when>
    <c:otherwise>
        <%-- always include core.js --%>
		<script src="${siteUrlPrefix}/assets/js/manifest.js?t=@TIMESTAMP@" charset="utf-8"></script>
        
        <script src="${siteUrlPrefix}/assets/js/core.js?t=@TIMESTAMP@" charset="utf-8"></script>
        


        <c:choose>
	        <c:when test="${ null==USER_FED_RESOURCES }">
                <script src="${siteUrlPrefix}/assets/js/login-backbone.js?t=@TIMESTAMP@" charset="utf-8"></script>
			</c:when>
			<c:otherwise>
                <script src="${siteUrlPrefix}/assets/js/login-backbone.js?t=@TIMESTAMP@" charset="utf-8"></script>
                
	            <script src="${siteUrlPrefix}/assets/js/apps.js?t=@TIMESTAMP@" charset="utf-8"></script>
			</c:otherwise>
		</c:choose>
        <script src="${siteUrlPrefix}/assets/js/app-sa.js?t=@TIMESTAMP@" charset="utf-8"></script>
        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <c:if test="${ null != USER_FED_RESOURCES }">
            <script src="${siteUrlPrefix}/assets/js/app-approvals.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>
        
        <c:if test="${ null != USER_FED_RESOURCES }">
			<script src="${siteUrlPrefix}/assets/js/app-advisor.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        
        

        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <c:if test="${ null != USER_FED_RESOURCES }">
            <script src="${siteUrlPrefix}/assets/js/app-celebration.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.productClaimsEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-claims.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.diyCommunicationsEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-communications.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.engagementEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-engagement.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.goalquestEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-goalquest.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <c:if test="${ null != USER_FED_RESOURCES }">
            <script src="${siteUrlPrefix}/assets/js/app-leaderboard.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.nominationsEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-nominations.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <script src="${siteUrlPrefix}/assets/js/app-purl.js?t=@TIMESTAMP@" charset="utf-8"></script>

        <c:if test="${ USER_FED_RESOURCES.quizesEnabled || USER_FED_RESOURCES.diyQuizesEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-quiz.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.recognitionsEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-recognition.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.reportsEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-reports.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.ssiEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-ssi.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>

        <c:if test="${ USER_FED_RESOURCES.throwdownEnabled }">
            <script src="${siteUrlPrefix}/assets/js/app-throwdown.js?t=@TIMESTAMP@" charset="utf-8"></script>
        </c:if>
        <%--
            *
            * standard script below
        <script src="${siteUrlPrefix}/assets/js/script.js" charset="utf-8"></script>
        --%>
    </c:otherwise>
    </c:choose>

    <script>
            //Prevent iframe carjacking XFS vulnerability
            <c:if test="${!beacon:isCMSDebugEnabled()}">
            this.top.location !== this.location && (this.top.location = this.location);
            </c:if>
    </script>

    <!-- SKIN JS -->
    <!-- <script src="skins/skinname/js/script.js"> -->
