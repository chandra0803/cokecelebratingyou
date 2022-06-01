<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<tiles:importAttribute/>
<div id="homeAppPageView">


    <div class="moduleContainerViewElement">

        <!-- dynamic content -->

    </div> <!-- /.moduleContainerViewElement -->
</div><!-- /#homeAppPageView -->

                        <script>
                            //Main Home Application Setup Function
                            $(document).ready(function(){

                                //instantiate HomeApp
                                hapv = new HomeAppPageView({
                                    el : $('#homeAppPageView'),
                                    pageNav : {},
                                    pageTitle : '',
                                    isFooterSheets : false
                                });

                                // homeApp = new HomeApp({
                                //     mcvSelector : '.moduleContainerViewElement' //the DOM root element of the module container
                                // });

								 G5.props.URL_JSON_PLATEAU_AWARDS_MODULE = 'plateauAwardsResult.do';
								 G5.props.URL_JSON_APPROVALS_LOAD_APPROVAL_MESSAGE_DATA  = G5.props.URL_ROOT + 'claim/approvalsCountAction.do?method=fetchApprovalsForTileAndList';
								 G5.props.numberFormat = "#,##0"; //default
								 //json response url, overrides value defined in settings.js
								 G5.props.URL_JSON_BUDGETS  = G5.props.URL_ROOT+'profile/budgetTracker.action';
								 G5.props.URL_JSON_BANNER_SLIDES = 'bannerSlides.do?method=fetchBannersForTile';
								 //json response url, overrides value defined in settings.js
								 G5.props.URL_JSON_COMMUNICATION = 'communicationsResult.do';
								 G5.props.URL_JSON_TIPS = 'dailyTipResult.do';
								 G5.props.URL_JSON_GAMIFICATION = G5.props.URL_ROOT+'profile/badges.action';
						 		 G5.props.URL_JSON_LEADERBOARD_SETS = 'leaderBoardResult.do?method=fetchLeaderBoardsForTile';
								 G5.props.URL_JSON_PUBLIC_RECOGNITION = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionResult.do?method=fetchPublicRecognitions';
								 G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_COMMENT = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionCommentAction.do?method=postComment';
								 G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_LIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=like';
								 G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_UNLIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=unlike';
								 G5.props.URL_JSON_RECENT_DISCUSSIONS = 'forum/forumResult.do?method=fetchForumForTile';

								 //Mini Profile PopUp JSON
								 G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

								 //Mini Profile PopUp Follow Unfollow Pax JSON
							     G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
							     G5.props.URL_JSON_EZ_RECOGNITION_RECIPIENT_LIST = "recognitionWizard/easyRecognitionRecipientSearch.do";
									G5.props.URL_JSON_REPORTS_ALL = 'reports/reportMenu.do';
						            G5.props.URL_JSON_REPORTS_FAVORITES = 'reports/displayDashboard.do';
									//G5.props.URL_JSON_REPORTS_FAVORITES = 'reports/displayDashboard.do?method=display';
									//G5.props.URL_JSON_REPORTS_FAVORITES_DELETE = 'reports/displayDashboard.do?method=remove';
									//G5.props.URL_JSON_REPORTS_FAVORITES_ADD = 'reports/displayDashboard.do?method=save';
									//G5.props.URL_JSON_REPORTS_FAVORITES_REORDER = 'reports/displayDashboard.do?method=reOrder';
									G5.props.URL_JSON_REPORTS_MODULE = 'reports/displayDashboard.do?method=display';
									G5.props.URL_REPORTS_ALL = 'reports/allReports.do';

									G5.props.URL_JSON_APPROVALS_LOAD_DATA = G5.props.URL_ROOT+'claim/approvalsCountAction.do?method=fetchApprovalsForTileAndList';

								// TODO
								// Search ajax calls if any
								G5.props.URL_JSON_SEARCH_MODULE= '';

                                //ADD MODULES
                                hapv.homeApp.moduleCollection.reset([
                                    {
                                        name:'onTheSpotCardModule',
                                        appName: 'onTheSpotCard',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:0},
                                            'recognition':{order:0},
                                            'activities':{order:0},
                                            'social':{order:0},
                                            'shop':{order:0},
                                            'reports':{order:'hidden'},
                                            'all':{order:0}
                                        }
                                    },
                                    {
                                        name:'plateauAwardsModule',
                                        appName: 'plateauAwards',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:1},
                                            'recognition':{order:1},
                                            'activities':{order:1},
                                            'social':{order:1},
                                            'shop':{order:1},
                                            'reports':{order:'hidden'},
                                            'all':{order:1}
                                        }
                                    },
                                    {
                                        name:'leaderboardModule',
                                        appName: 'leaderboard',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:5},
                                            'recognition':{order:5},
                                            'activities':{order:5},
                                            'social':{order:5},
                                            'shop':{order:5},
                                            'reports':{order:'hidden'},
                                            'all':{order:5}
                                        }
                                    },
                                    {
                                        name:'publicRecognitionModule',
                                        appName: 'publicRecognition',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:6},
                                            'recognition':{order:6},
                                            'activities':{order:6},
                                            'social':{order:6},
                                            'shop':{order:6},
                                            'reports':{order:'hidden'},
                                            'all':{order:6}
                                        }
                                    },
                                    {
                                        name:'EZRecognitionModule',
                                        appName: 'recognition',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'EZRecognitionModule',//override name for view object
                                        filters:{
                                            'default':{order:7},
                                            'recognition':{order:7},
                                            'activities':{order:7},
                                            'social':{order:7},
                                            'shop':{order:7},
                                            'reports':{order:'hidden'},
                                            'all':{order:7}
                                        }
                                    },
                                    {
                                        name:'tipModule',
                                        appName: 'tip',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:16},
                                            'recognition':{order:16},
                                            'activities':{order:16},
                                            'social':{order:16},
                                            'shop':{order:16},
                                            'reports':{order:'hidden'},
                                            'all':{order:16}
                                        }
                                    },
                                    /* Let's keep these out until development continues
                                    {
                                        name:'goalquestModule',
                                        appName: 'goalquest',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:0},
                                            'home':{order:4},
                                            'activities':{order:2},
                                            'social':{order:2},
                                            'shop':{order:2},
                                            'reports':{order:'hidden'},
                                            'all':{order:2}
                                        }
                                    },
                                    {
                                        name:'goalquestManagerModule',
                                        appName: 'goalquest',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:0},
                                            'home':{order:4},
                                            'activities':{order:2},
                                            'social':{order:2},
                                            'shop':{order:2},
                                            'reports':{order:'hidden'},
                                            'all':{order:'hidden'}
                                        }
                                    },
                                    {
                                        name:'challengepointModule',
                                        appName: 'goalquest',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:17},
                                            'home':{order:17},
                                            'activities':{order:17},
                                            'social':{order:17},
                                            'shop':{order:17},
                                            'reports':{order:'hidden'},
                                            'all':{order:17}
                                        }
                                    },
                                    */
                                    {
                                        name:'managerToolkitModule',
                                        appName: 'managerToolkit',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:18},
                                            'recognition':{order:18},
                                            'activities':{order:18},
                                            'social':{order:18},
                                            'shop':{order:18},
                                            'reports':{order:'hidden'},
                                            'all':{order:18}
                                        }
                                    },
                                    {
                                        name:'claimModule',
                                        appName: 'claim',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:20},
                                            'recognition':{order:20},
                                            'activities':{order:20},
                                            'social':{order:20},
                                            'shop':{order:20},
                                            'reports':{order:'hidden'},
                                            'all':{order:20}
                                        }
                                    },
                                    {
                                        name:'approvalsModule',
                                        appName: 'approvals',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:21},
                                            'recognition':{order:21},
                                            'activities':{order:21},
                                            'social':{order:21},
                                            'shop':{order:21},
                                            'reports':{order:'hidden'},
                                            'all':{order:21}
                                        }
                                    },
                                    /*
                                    {
                                        name:'theDudeModule',
                                        appName: 'theDude',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:'hidden'},
                                            'home':{order:'hidden'},
                                            'activities':{order:'hidden'},
                                            'social':{order:'hidden'},
                                            'shop':{order:'hidden'},
                                            'reports':{order:'hidden'},
                                            'all':{order:'hidden'}
                                        }
                                    },*/
                                    {
                                        name:'resourceCenterModule',
                                        appName: 'resourceCenter',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:22},
                                            'recognition':{order:22},
                                            'activities':{order:22},
                                            'social':{order:22},
                                            'shop':{order:22},
                                            'reports':{order:'hidden'},
                                            'all':{order:22}
                                        }
                                    },

                                    {
                                        name:'bannerModuleModule',
                                        appName: 'bannerModule',//name of the app
                                        //templateName: 'testModule',//use this for template lookup instead of name
                                        //viewName: 'testModule',//override name for view object
                                        filters:{
                                            'default':{order:23},
                                            'recognition':{order:23},
                                            'activities':{order:23},
                                            'social':{order:23},
                                            'shop':{order:23},
                                            'reports':{order:'hidden'},
                                            'all':{order:23}
                                        }
                                    },
									{
										name:'reportsDashboardModule',          // REQUIRED If not, blankModule and/or other BAD
										appName: 'reports',             // core/apps/[appName]/tpl/... or if not core/base/tpl/...
										// templateName: 'reportsModule',   // .../tpl/[templateName].html or if not .../tpl/[name].html
										// viewName: 'ReportsModule',       // 1-[<V>iewName]View 2-[<N>ame]View 3-ModuleView
										filters:{
											'recognition':{ order:'hidden' },
											'programs':{ order:'hidden' },
											'information':{ order:'hidden' },
											'manager':{ order:'hidden' },
											'reports':{ order:0 }
										}
									},
                                    // pax SearchModule
                                    {
						                name:'heroModule',
						                appName: 'paxSearch',//name of the app
						                //templateName: 'testModule',//use this for template lookup instead of name
						                //viewName: 'testModule',//override name for view object
							      filters:{
						                    'default':{order:1, searchEnabled:'true'},
						                    'recognition':{order:1, searchEnabled:'true'},
						                    'information':{order:1, searchEnabled:'false'},
						                    'programs':{order:1, searchEnabled:'true'},
						                    'manager':{order:1, searchEnabled:'false'},
						                    'reports':{order:'hidden', searchEnabled:'false'}
               							 }
						            }
                                ]
                			);
                		});
</script>


<tiles:insert definition="activities.onthespot" flush="true" ignore="true"/>

<tiles:insert definition="activities.plateau.module" flush="true" ignore="true"/>
*
<tiles:insert definition="budget.tracker.module" flush="true" ignore="true"/>

<tiles:insert definition="public.recognition.module" flush="true" ignore="true"/>

<tiles:insert definition="communications.module" flush="true" ignore="true"/>

<tiles:insert definition="social.gamification.module" flush="true" ignore="true"/>

<tiles:insert definition="social.resources.module" flush="true" ignore="true"/>

<tiles:insert definition="leaderBoard.ranking.module" flush="true" ignore="true"/>

<tiles:insert definition="daily.tip.module" flush="true" ignore="true"/>

<tiles:insert definition="manager.toolkit.module" flush="true" ignore="true"/>

<tiles:insert definition="quiz.module" flush="true" ignore="true"/>

<tiles:insert definition="reports.module" flush="true" ignore="true"/>

<tiles:insert definition="approvals.module" flush="true" ignore="true"/>

<tiles:insert definition="banner.page" flush="true" ignore="true"/>

<%-- <tiles:insert definition="forum.module" flush="true" ignore="true"/> --%>

<tiles:insert definition="hero.module" flush="true" ignore="true"/>

<tiles:insert definition="nomination.winner.modal.tpl" flush="true" ignore="true" />

<c:if test="${not empty purlInvites}">
	<tiles:insert definition="purl.invite.results.modal" flush="true" ignore="true">
		<tiles:put name="autoModal" value="autoModal"/>
	</tiles:insert>
</c:if>

<c:if test="${budgetConfirmation == true}">
	<tiles:insert definition="budget.transfer.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty recognitionSentBean}">
	<tiles:insert definition="recognitionSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>

<c:if test="${not empty invites}">
	<tiles:insert definition="purlInvitationSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>
