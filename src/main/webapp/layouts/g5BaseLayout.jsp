<!DOCTYPE html>
<%@page import="com.biperf.core.utils.StringUtil"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.NumberFormatUtil"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.utils.UserManager"%>
<tiles:importAttribute/>

<tiles:useAttribute name="wrapperClass" id="wrapperClass" classname="java.lang.String"/>
<tiles:insert attribute="init" />
<!-- NOTE: the lang="${feLocaleCode}" attribute should be dependent on the actual language selected by the user -->

<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="${feLocaleCode}"> <![endif]-->
<!--[if (IE 7)&!(IEMobile)]><html class="no-js lt-ie9 lt-ie8" lang="${feLocaleCode}"><![endif]-->
<!--[if (IE 8)&!(IEMobile)]><html class="no-js lt-ie9" lang="${feLocaleCode}"><![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="${feLocaleCode}"><!--<![endif]-->

    <head>

        <!-- NOTE: the only places for variable code in the head of this application are the title tag, the CSS links, and the meta tags (if applicable). No install- or page-specific code should ever show up in the head. -->
        <meta charset="utf-8">

        <!-- Use the .htaccess and remove these lines to avoid edge case issues.
            More info: h5bp.com/i/378 -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <!-- Manual DNS prefetching (from http://html5boilerplate.com/docs/head-Tips/)
             Make a DNS handshake with a foreign domain, so the connection goes faster when the user eventually needs to access it. This works well for loading in assets (like images) from another domain, or a JavaScript library from a CDN. -->
        <!-- Prefetch for AwardslinQ -->
        <link rel="dns-prefetch" href="//www1.awardslinq.com" />
        <!-- Prefetch for PURL if applicable -->
        <link rel="dns-prefetch" href="//subdomain.recognitionpurl.com" />

        <!-- NOTE: the title tag should accurately reflect the name and location of the page. For example, "Send a Recognition : The Exchange" -->
        <title><c:out value='${webappTitle}'/></title>

        <!-- Mobile viewport optimized -->
        <meta name="HandheldFriendly" content="True">
        <meta name="MobileOptimized" content="320">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- CSS -->
        <%-- always include core.css --%>
        <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/core.css?t=@TIMESTAMP@" type="text/css">

		<c:choose>
	        <c:when test="${ null==USER_FED_RESOURCES }">
	            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/login.css?t=@TIMESTAMP@" type="text/css">
			</c:when>
			<c:otherwise>
                <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/login.css?t=@TIMESTAMP@" type="text/css">
	            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/apps.css?t=@TIMESTAMP@" type="text/css">
			</c:otherwise>
		</c:choose>

        <%-- TODO: add logic for when [app-approvals.css] should be included --%>
        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <c:if test="${ null != USER_FED_RESOURCES }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-approvals.css?t=@TIMESTAMP@" type="text/css">
        </c:if>

        <%-- TODO: add logic for when [app-celebration.css] should be included --%>
        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <c:if test="${ null != USER_FED_RESOURCES }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-celebration.css?t=@TIMESTAMP@" type="text/css">
        </c:if>

        <%-- PRODUCTCLAIMS --%>
        <c:if test="${ USER_FED_RESOURCES.productClaimsEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-claims.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- DIY COMMUNICATIONS --%>
        <c:if test="${ USER_FED_RESOURCES.diyCommunicationsEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-communications.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- ENGAGEMENT --%>
        <c:if test="${ USER_FED_RESOURCES.engagementEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-engagement.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- GOALQUEST --%>
        <c:if test="${ USER_FED_RESOURCES.goalquestEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-goalquest.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- TODO: add logic for when [app-leaderboard.css] should be included --%>
        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <c:if test="${ null != USER_FED_RESOURCES }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-leaderboard.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- NOMINATIONS --%>
        <c:if test="${ USER_FED_RESOURCES.nominationsEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-nominations.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- this is placeholder logic to prevent this file from loading on login --%>
        <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-purl.css?t=@TIMESTAMP@" type="text/css">

        <%-- QUIZES --%>
        <c:if test="${ USER_FED_RESOURCES.quizesEnabled || USER_FED_RESOURCES.diyQuizesEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-quiz.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- RECOGNITION --%>
        <c:if test="${ USER_FED_RESOURCES.recognitionsEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-recognition.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- REPORTS --%>
        <c:if test="${ USER_FED_RESOURCES.reportsEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-reports.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- SSI --%>
        <c:if test="${ USER_FED_RESOURCES.ssiEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-ssi.css?t=@TIMESTAMP@" type="text/css">
		</c:if>

        <%-- THROWDOWN --%>
        <c:if test="${ USER_FED_RESOURCES.throwdownEnabled }">
            <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-throwdown.css?t=@TIMESTAMP@" type="text/css">
        </c:if>

        <%-- always include print.css --%>
        <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/print.css?t=@TIMESTAMP@" type="text/css" media="print">
        
        <link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-bunchball.css?t=@TIMESTAMP@" type="text/css">


        <!-- All JavaScript at the bottom, except this Modernizr build.
             Modernizr enables HTML5 elements & feature detects for optimal performance. -->
        <script src="${siteUrlPrefix}/assets/libs/modernizr.js"></script>
        <!-- ALL other JS lives at the bottom EXCEPT JQUERY-->
        <script src="${siteUrlPrefix}/assets/libs/jquery.js"></script>

        <!-- For third-generation iPad with high-resolution Retina display: -->
        <!-- <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/apple-touch-icon-144x144-precomposed.png"> -->
        <!-- For iPhone with high-resolution Retina display: -->
        <!-- <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/apple-touch-icon-114x114-precomposed.png"> -->
        <!-- For first- and second-generation iPad: -->
        <!-- <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/apple-touch-icon-72x72-precomposed.png"> -->
        <!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
        <!-- <link rel="apple-touch-icon-precomposed" href="/apple-touch-icon-precomposed.png"> -->
        <!-- recommended as of iOS7 -->
        <link rel="apple-touch-icon" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/img/apple-touch-icon.png"/>
        <!-- For everything else -->
        <link rel="shortcut icon" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/img/favicon.png">

        <!--iOS -->
        <%--In full-screen stand-alone mode, links don't always stay in the app and instead kick to Safari.
            There are some potential JS-based solutions outlined here: http://stackoverflow.com/questions/6429492/how-do-you-keep-an-iphone-ipad-web-app-in-full-screen-mode
            Until we can fully vet one of these solutions, I'm simply taking away full-screen mode.
        <meta name="apple-mobile-web-app-capable" content="yes">
        --%>
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <!-- <link rel="apple-touch-startup-image" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/img/startup.png"> -->

        <!-- <script>(function(){var p,l,r=window.devicePixelRatio;if(navigator.platform==="iPad"){p=r===2?"img/startup/startup-tablet-portrait-retina.png":"img/startup/startup-tablet-portrait.png";l=r===2?"img/startup/startup-tablet-landscape-retina.png":"img/startup/startup-tablet-landscape.png";document.write('<link rel="apple-touch-startup-image" href="'+l+'" media="screen and (orientation: landscape)"/><link rel="apple-touch-startup-image" href="'+p+'" media="screen and (orientation: portrait)"/>');}else{p=r===2?"img/startup/startup-retina.png":"img/startup/startup.png";document.write('<link rel="apple-touch-startup-image" href="'+p+'"/>');}})()</script> -->
        <!--Microsoft -->

        <!-- Prevents links from opening in mobile Safari -->
        <!-- <script>(function(a,b,c){if(c in b&&b[c]){var d,e=a.location,f=/^(a|html)$/i;a.addEventListener("click",function(a){d=a.target;while(!f.test(d.nodeName))d=d.parentNode;"href"in d&&(d.href.indexOf("http")||~d.href.indexOf(e.host))&&(a.preventDefault(),e.href=d.href)},!1)}})(document,window.navigator,"standalone")</script> -->
        <meta http-equiv="cleartype" content="on">

        <!-- IE9 Pinned Sites (from http://html5boilerplate.com/docs/head-Tips/)
             Enabling your application for pinning will allow IE9 users to add it to their Windows Taskbar and Start Menu. This comes with a range of new tools that you can easily configure with the elements below. See more documentation on IE9 Pinned Sites.
              Name the Pinned Site for Windows
             Without this rule, Windows will use the page title as the name for your application. -->
        <meta name="application-name" content="Sample Title" />
        <!--  Give Your Pinned Site a Tooltip
             You know — a tooltip. A little textbox that appears when the user holds their mouse over your Pinned Site's icon. -->
        <meta name="msapplication-tooltip" content="A description of what this site does." />
   	    <script type="text/javascript" src="${siteUrlPrefix}/assets/g4skin/scripts/commonFunctions.js" ></script>


    </head>

    <!-- NOTE: the id on the body should match the base URL of the G5 install and be in the form "theexchange-performnet-com" -->
    <!-- NOTE: G5 is built around the idea of pages — no, this is not a new idea but it is going to be approached in a newish way. Each page belongs to an app. Each app and its pages is namespaced and has carefully compartmentalized JS and CSS. Pages should always come with a unique URL that can be bookmarked in the future. Each page can also have several states (the method for identifying states in the URL is TBD). -->
    <!-- NOTE: the class names and data-* attributes are necessary for app- and page-specific styling and JavaScript execution and match up with the app names and pages (and potentially page states) -->
    <body id="g5-biw" data-app="home" data-page="default" class="app_home page_default <%=wrapperClass%>">
	<!-- server-env : <%=System.getProperty("bi.appGrpNum")+ "-" + System.getProperty( "environment.name")%> -->
	<!-- <%=System.getProperty("com.sun.aas.instanceName")%> -->
		 <!-- overlay -->
        <div id="overlay"> </div>

        <div id="wrapper_outer">
            <div id="wrapper_inner">
                <header id="header">
                    <div class="container">
                        <!-- global header to go here -->
                    </div><!-- /.container -->
                </header><!-- /#header -->

                <!-- sidebar -->
                <div id="sidebar" class="sidebar">
                    <div class="sidebar-container"></div>
                </div><!-- /#sidebar -->
                <div id="sidebar-overlay"></div>

                <!-- globalNav -->
                <div id="globalNav">
                    <div class="container">
                        <!-- global nav to go here -->
                    </div><!-- /.container -->
                </div><!-- /#globalNav -->

                <section id="contents">
                    <div class="container">
                        <tiles:insert attribute='homeApp'/> <!-- /For homepage  -->
                        <tiles:insert attribute='content'/> <!-- /For detailPages  -->

                        <div id="pageLoadingSpinner" class="spincover pageLoading"><img src="${siteUrlPrefix}/assets/img/pageLoadingSpinner.gif" alt="Loading..."></div>
				   </div><!-- /.container -->
                </section><!-- /#contents -->

                <footer id="footer">
                    <div class="container">

                    </div>
                </footer>

                <!-- inline modal (stack style), the class .modal has been supplemented with .modal-stack -->
                <div class="modal modal-stack hide fade" id="sheetOverlayModal" style="display:none;" data-y-offset="adjust">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                        <h3 class="sheetOverlayModalTitle"></h3>
                    </div>

                    <div class="modal-body">
                        <!-- dynamic content -->

                    </div>
                    <%--<div class="modal-footer">
                        <a href="#" class="btn" data-dismiss="modal">Close</a>
                    </div>--%>
                </div><!-- /.modal-stack -->
				<!-- GDPR Compliance Start -->
				<c:if test="${cookiesAccepted ne true }">
					<div id="compliantModal">						
						${policyUrl}
						<a class="compliant-close" href="#"><cms:contentText key="CLOSE" code="ssi_contest.generalInfo"/> <i class="icon-close"></i></a>
					</div>
				</c:if>
		<!-- GDPR Compliance End-->

		<%-- SA Error Popup --%>

		<div class="modal fade hide" aria-hidden="false" id="saErrorModal">
			<div class="modal-header">
				<button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
				<h1><cms:contentText key="PROBLEM_LOADING_CELEBRATION" code="serviceanniversary.content"/></h1>
			</div>
			<div class="modal-body">
				<p><cms:contentText key="CELEBRATION_FOR" code="serviceanniversary.content"/> <span></span> <cms:contentText key="FAILED_TO_LOAD" code="serviceanniversary.content"/></p>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal"><cms:contentText key="CANCEL" code="ssi_contest.generalInfo"/></button>
			</div>
		</div>
		<%-- SA Error Popup End --%>
				
            </div><!-- /#wrapper_inner -->
        
        </div><!-- /#wrapper_outer -->
		
<!-- insert global embedded templates here -->
<tiles:insert attribute='header'/>
<tiles:insert attribute='globalNav'/>
<tiles:insert attribute='profile'/>
<tiles:insert attribute='globalSidebar'/>
    <tiles:insert definition="budget.tracker.module" flush="true" ignore="true"/>
    <tiles:insert definition="social.gamification.module" flush="true" ignore="true"/>
    <tiles:insert definition="work.happier.module" flush="true" ignore="true"/>
    <tiles:insert definition="engagement.module" flush="true" ignore="true"/>
    <tiles:insert definition="bunchball.sidebar.module" flush="true" ignore="true"/><!-- Client customization -->
<tiles:insert attribute='pageNav'/>
<tiles:insert attribute='footer'/>

<!-- ==================== END OF APP HTML ==================== -->

<tiles:insert attribute='layoutScripts'/>

    <!-- INIT -->
        <script>

        		<c:if test="${hideSettingsTab}">
	        	$(document).ready(function(){
		   		 _.delay(function(){
		   			 if ($(".profile-proxy-overlay").length > 0) {
		   				$('.settings').hide();
		   			   }
		   			 }, 650);
		   		});
	        	</c:if>     

        		
                G5.version = '@VERSION@'; // insert version information here

				_V_.options.flash.swf = G5.props.URL_ROOT + "assets/rsrc/video-js.swf"//swf for video in IE7/IE8
				G5.props.URL_ROOT = G5_CONTEXT_PATH + "/"; //CONTEXT PATH
				G5.props.URL_APPS_ROOT = G5.props.URL_ROOT+'../apps/'; //APPS PATH (development only)
				G5.props.URL_BASE_ROOT = G5.props.URL_ROOT+'./'; //BASE PATH (development only)
				
				

				//UNIFIED TEMPLATE DIRECTORY
				G5.props.URL_TPL_ROOT = './';//'./tpl' //if set, this will be used for all template paths
				G5.props.TMPL_SFFX = '.do'; // '.jsp', '.php', '.html'
				//G5.props.URL_JSON_HERO_BANNER = 'bannerSlides.do?method=fetchHeroImage';

                //G5.props.URL_JHTML_STYLE = '${siteUrlPrefix}'+'/assets/css/jhtmlArea.css';
                G5.props.URL_JHTML_STYLE = '${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/jhtmlArea.css';

		        G5.props.URL_JSON_PARTICIPANT_GLOBAL_SEARCH_AUTOCOMPLETE = G5.props.URL_ROOT+'search/paxHeroSearch.action';

		        G5.props.URL_JSON_XPROMO = G5.props.URL_ROOT+'xpromo/content.action';

				G5.props.URL_HOME_APP_FILTER_CHANGE = G5.props.URL_ROOT+'changeFilter.do';
				G5.props.URL_JSON_PARTICIPANT_PROFILE = G5.props.URL_ROOT+'profileView.do?method=fetchUserACInfo&doNotSaveToken=true';//current active user
				G5.props.URL_JSON_PARTICIPANT_PROFILE_POINTS = G5.props.URL_ROOT+'profileView.do?method=fetchUserPointsInfo&doNotSaveToken=true';//current active user points
				// here is how to change the number format
				G5.props.setNumberFormat('#,##0', '<%=NumberFormatUtil.getUserLocaleBasedNumberDelimiter()%>', '.');
				G5.props.URL_JSON_PARTICIPANT_PROFILE_ALERTS = G5.props.URL_ROOT+'profileView.do?method=fetchUserAlrtsInfo'<c:if test="${filterPage}"> + '&showAlert=true'</c:if>+'&doNotSaveToken=true&refreshAlerts=true';//curren active user's alerts
				// Enable the code only for local development
				// G5.props.URL_JSON_PARTICIPANT_PROFILE_ALERTS = G5.props.URL_ROOT+'assets/json/participantProfileAlerts.json';

				//attach the view to an existing DOM element
		        G5.props.URL_JSON_PUBLIC_RECOGNITION = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionResult.do?method=fetchPublicRecognitions';
		        G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_COMMENT = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionCommentAction.do?method=postComment';
				G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_LIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=like';
				G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_UNLIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=unlike';
				G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_HIDE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionHideAction.do?method=hide';

				G5.props.URL_JSON_SMACK_TALK = 'smackTalkSummary.do?method=summary&promotionId=';
		        G5.props.URL_JSON_SMACK_TALK_SAVE_COMMENT = 'smackTalkComment.do?method=postComment';
		        G5.props.URL_JSON_SMACK_TALK_SAVE_LIKE = 'smackTalkComment.do?method=like';
		        G5.props.URL_JSON_SMACK_TALK_SAVE_HIDE='smackTalkHideAction.do?method=hide';
		        G5.props.URL_JSON_SMACK_TALK_DETAIL='smackTalkMatchDetail.do?method=smackTalkDetail';
		        G5.props.URL_JSON_SMACK_TALK_NEW='smackTalkComment.do?method=saveSmackTalkPost';

		        G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

			    //purl celebrate
		        G5.props.URL_JSON_PURL_CELEBRATE_DATA = 'purl/purlCelebrateRecognitionPurlsActivity.do?method=fetchRecognitionPurls&tile=true';

		        G5.props.URL_SET_SYSTEM_DEBUG ="${pageContext.request.contextPath}/setScriptDebugAction.do";
		        G5.props.DEFAULT_FILTER ='${priorityOneFilter}';

				// budget tracker
				G5.props.URL_JSON_BUDGETS  = G5.props.URL_ROOT+'profile/budgetTracker.action';

				// gamification
				G5.props.URL_JSON_GAMIFICATION = G5.props.URL_ROOT+'profile/badges.action';

				//engagement
				G5.props.URL_JSON_ENGAGEMENT_SUMMARY_COLLECTION = G5.props.URL_ROOT+'profile/dashboard.action';

				//WorkHappier
				G5.props.URL_JSON_WORK_HAPPIER_PAST_RESULTS = G5.props.URL_ROOT+'profile/workhappier.action';

				// endpoint for getting the promotion rules
				G5.props.URL_JSON_RULES = G5.props.URL_ROOT + 'rules.do?method=fetchPromotionRules';

				G5.props.URL_JSON_GLOBAL_SEARCH_FILTERS = G5.props.URL_ROOT+'search/filters.action';
				G5.props.URL_JSON_PARTICIPANT_TOKENIZED_SEARCH_AUTOCOMPLETE = G5.props.URL_ROOT+'search/paxHeroSearch.action';
				G5.props.URL_JSON_GLOBAL_SEARCH_AUTOCOMPLETE = G5.props.URL_ROOT+'recognitionWizard/recipientSearch.do?method=doAutoComplete'
				<c:choose>
			    	<c:when test="${ null != isRARecognitionFlow }">
			    		G5.props.URL_JSON_PROGRAM_SELECT_DATA = G5.props.URL_ROOT+'recognitionWizard/paxData.do?method=allRecogPromo&isRARecognitionFlow=${isRARecognitionFlow }&reporteeId=${reporteeId}';
			    	</c:when>    
			   		<c:otherwise>
			   			G5.props.URL_JSON_PROGRAM_SELECT_DATA = G5.props.URL_ROOT+'recognitionWizard/paxData.do?method=allRecogPromo';
			    	</c:otherwise>
				</c:choose>
					
				G5.props.URL_START_RECOGNITION =  G5.props.URL_ROOT+'recognitionWizard/sendRecognitionDisplay.do';
				G5.props.VALID_USERS =   G5.props.URL_ROOT+'participantSearch/validatePaxForRecog.action';

				// news,banners,resources,tips
				G5.props.URL_JSON_COMMUNICATION_BANNERS_TABLE = G5.props.URL_ROOT+'participant/diyBannerMaintenance.do?method=populateBannerMaintenanceTable';
				G5.props.URL_JSON_COMMUNICATION_NEWS_TABLE = G5.props.URL_ROOT+'participant/diyNewsMaintenance.do?method=populateNewsMaintenanceTable';
				G5.props.URL_JSON_COMMUNICATION_RESOURCE_CENTER_TABLE = G5.props.URL_ROOT+'participant/diyResourceMaintain.do?method=populateResourceCenterTable';
				G5.props.URL_JSON_COMMUNICATION_TIPS_TABLE = G5.props.URL_ROOT+'participant/diyTipsMaintain.do?method=populateTipsTable';

				// groups
				G5.props.URL_JSON_GLOBAL_SAVE_CREATE_GROUP = G5.props.URL_ROOT+'groups/save.action';
				G5.props.URL_JSON_GLOBAL_FETCH_GROUP_BY_ID = G5.props.URL_ROOT+'groups/groupInfoByGroupId.action';
				G5.props.URL_JSON_GLOBAL_FETCH_GROUPS = G5.props.URL_ROOT+'groups/groupInfoByUserId.action';
				G5.props.URL_JSON_GLOBAL_DELETE_GROUP = G5.props.URL_ROOT+'groups/delete.action';

				//SA 
				G5.props.URL_JSON_SA_SHARE_A_MEMORY = G5.props.URL_ROOT+'sa/contributelink.action';
				G5.props.URL_JSON_SA_GIFT_CODE = G5.props.URL_ROOT+'sa/giftcodelink.action';
				
				// ID service SSO cookie end point.				
				G5.props.URL_IDS_SSO_END_POINT = '${idsSSOEndPoint}';
				
				G5.props.URL_JSON_CAREERMOMENTS_ACTIVITY_FEED = G5.props.URL_ROOT+'comments.do?method=fetchComments';
				G5.props.URL_JSON_CAREERMOMENTS_POST_COMMENT = G5.props.URL_ROOT +'saveComments.do?method=saveComments';
				G5.props.URL_JSON_CAREERMOMENTS_UPLOAD_PHOTO = G5.props.URL_ROOT+'saveComments.do?method=processPhoto';
				G5.props.URL_JSON_CAREERMOMENTS_DATA = G5.props.URL_ROOT+'careerMomentsData.do?method=fetchDataForDetail';
				G5.props.URL_JSON_CAREERMOMENTS_SEARCH_RESULTS = G5.props.URL_ROOT+'careerMomentsData.do?method=fetchDataForDetail';

		        <beacon:authorize ifNotGranted="LOGIN_AS">
		        // URL to check if the current user is a winner
		        G5.props.URL_JSON_PARTICIPANT_PROFILE_NOMINATION_WINNER_MODAL = G5.props.URL_ROOT+'nomination/viewNominationPastWinnersList.do?method=getNominationWinnerModalDetails';
		        </beacon:authorize>
		        /*  ****************************
		            i18n for JS plugins
		            ****************************/

		        // richtext editor
		        G5.props.richTextEditorLocalization = {
		            // i18n defaults to en, then these strings will be used, localize the strings here
		            en : {
		                bold: '<cms:contentText key="BOLD" code="system.richtext"/>',
		                italic: '<cms:contentText key="ITALIC" code="system.richtext"/>',
		                underline: '<cms:contentText key="UNDERLINE" code="system.richtext"/>',
		                orderedlist: '<cms:contentText key="ORDERED_LIST" code="system.richtext"/>',
		                unorderedlist: '<cms:contentText key="UNORDERED_LIST" code="system.richtext"/>',
		                html: '<cms:contentText key="HTML_SOURCE_VIEW" code="system.richtext"/>',
		                checkSpelling : '<cms:contentText key="CHECK_SPELLING" code="system.richtext"/>',
		                removeFormat : '<cms:contentText key="REMOVE_FORMATTING" code="system.richtext"/>',
		                charsRemaining: '<cms:contentText key="REMAINING_CHARS" code="system.richtext"/>'
		            },
		            <%=UserManager.getUserLanguage()%> : {
		            	bold: '<cms:contentText key="BOLD" code="system.richtext"/>',
		                italic: '<cms:contentText key="ITALIC" code="system.richtext"/>',
		                underline: '<cms:contentText key="UNDERLINE" code="system.richtext"/>',
		                orderedlist: '<cms:contentText key="ORDERED_LIST" code="system.richtext"/>',
		                unorderedlist: '<cms:contentText key="UNORDERED_LIST" code="system.richtext"/>',
		                html: '<cms:contentText key="HTML_SOURCE_VIEW" code="system.richtext"/>',
		                checkSpelling : '<cms:contentText key="CHECK_SPELLING" code="system.richtext"/>',
		                removeFormat : '<cms:contentText key="REMOVE_FORMATTING" code="system.richtext"/>',
		                charsRemaining: '<cms:contentText key="REMAINING_CHARS" code="system.richtext"/>'
		            }
		        };

		        // spellchecker (used in conjunction with jazzy spellcheck)
                G5.props.spellcheckerUrl = G5.props.URL_ROOT+'spellchecker/jazzySpellCheck.do';
		        G5.props.spellCheckerLocalesToUse = ['en_au', 'en_ca', 'fr_ca', 'de_de', 'es_mx', 'en_gb', 'en_us', 'pt_br', 'nl_nl', 'fr_fr', 'es_es', 'it_it', 'ko_kr', 'pl', 'ru'];
		        G5.props.spellCheckerLocalization = {
		            // base English strings
		            en : {
		                menu : '<cms:contentText key="EN_MENU" code="system.richtext"/>',
		                loading : '<cms:contentText key="EN_LOADING" code="system.richtext"/>',
		                nosuggestions : '<cms:contentText key="EN_NOSUGGESTIONS" code="system.richtext"/>',
		                addToDictionaryStart : '<cms:contentText key="EN_ADD_TO_DIST" code="system.richtext"/>',
		                addToDictionaryEnd : '<cms:contentText key="EN_ADD_TO_DIST_END" code="system.richtext"/>',
		                postJsonError : '<cms:contentText key="EN_POST_JSON_ERROR" code="system.richtext"/>',
		                ignoreWord : '<cms:contentText key="EN_IGNORE_WORD" code="system.richtext"/>',
		                ignoreAll : '<cms:contentText key="EN_IGNORE_ALL" code="system.richtext"/>',
		                ignoreForever : '<cms:contentText key="EN_IGNORE_FOREVER" code="system.richtext"/>',
		                ignoreForeverTitle : '<cms:contentText key="EN_IGNORE_FOREVER_TITLE" code="system.richtext"/>',
		                noMisspellings : '<cms:contentText key="EN_NO_MISSPELLINGS" code="system.richtext"/>'
		            },
		            // Australian English strings (ONLY if different from base English)
		            en_au : {
		                menu : '<cms:contentText key="EN_AU_MENU" code="system.richtext"/>'
		            },
		            // Canadian English strings (ONLY if different from base English)
		            en_ca : {
		                menu : '<cms:contentText key="EN_CA_MENU" code="system.richtext"/>'
		            },
		            // UK English strings (ONLY if different from base English)
		            en_gb : {
		                menu : '<cms:contentText key="EN_GB_MENU" code="system.richtext"/>'
		            },
		            // US English strings (ONLY if different from base English)
		            en_us : {
		                menu : '<cms:contentText key="EN_US_MENU" code="system.richtext"/>'
		            },
		            // Germany German strings
		            de_de : {
		                menu : '<cms:contentText key="DE_DE_MENU" code="system.richtext"/>'
		            },
		            // Mexican Spanish strings
		            es_mx : {
		                menu : '<cms:contentText key="ES_MX_MENU" code="system.richtext"/>'
		            },
		            // Canadian French strings
		            fr_ca : {
		                menu : '<cms:contentText key="FR_CA_MENU" code="system.richtext"/>'
		            },
		            // Brazilian Portugese strings
		            pt_br : {
		                menu : '<cms:contentText key="PT_BR_MENU" code="system.richtext"/>'
		            },
		            // Dutch strings
		            nl_nl : {
		                menu : '<cms:contentText key="NL_NL_MENU" code="system.richtext"/>'
		            },
		            // French European strings
		            fr_fr : {
		                menu : '<cms:contentText key="FR_FR_MENU" code="system.richtext"/>'
		            },
		            // Spanish European strings
		            es_es : {
		                menu : '<cms:contentText key="ES_ES_MENU" code="system.richtext"/>'
		            },
		            // Italian strings
		            it_it : {
		                menu : '<cms:contentText key="IT_IT_MENU" code="system.richtext"/>'
		            },
		            // Korean strings
		            ko_kr : {
		                menu : '<cms:contentText key="KO_KR_MENU" code="system.richtext"/>'
		            },
		            // Polish strings
		            pl : {
		                menu : '<cms:contentText key="PL_MENU" code="system.richtext"/>'
		            },
		            // Russian strings
		            ru : {
		                menu : '<cms:contentText key="RU_MENU" code="system.richtext"/>'
		            }

		        };

				//DEFAULTS FOR PAGENAV
				G5.props.pageNavDefaults = {
				    back : {
				        text : '<cms:contentText key="BACK" code="system.button" />',
				        url : '${pageContext.request.contextPath}/homePage.do#launch/${filterName}'
				    },
				    home : {
				        text : '<cms:contentText key="HOME" code="system.general" />',
				        url : '${pageContext.request.contextPath}/homePage.do'
				    }
				};

				G5.props.NEW_REDEEM_MENU = '${newRedeemMenu}';

				G5.props.globalHeader = {
						clientLogo : '${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/img/<c:out value='${clientLogo}'/>',
					    clientName : '<c:out value='${webappTitle}'/>'
					    <c:if test="${showProgramLogoAndName}">
					    ,
					    programLogo : '${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/img/<c:out value='${programLogo}'/>',
					    programName : '<c:out value='${programName}'/>'
					    </c:if>
				}
				//Cookie update for GDPR Compliance
				G5.props.URL_COOKIES_UPDATE = G5.props.URL_ROOT+'cookiesUpdate.do';
				function signOnToId( ssoUrl ) {
					var iframe = document.createElement( 'iframe' );
					iframe.setAttribute( 'name', 'id_sso_frame' );
					iframe.style = 'position: absolute; width: 0; height: 0; border: none;';

					var form = document.createElement( 'form' );
					form.setAttribute( 'method', 'POST' );
					form.setAttribute( 'action', ssoUrl );

					form.setAttribute( 'target', 'id_sso_frame' );

					var formInput = document.createElement( 'input' );
					formInput.setAttribute( 'type', 'hidden' );
					formInput.setAttribute( 'name', 'origin' );
					formInput.setAttribute( 'value', window.location.origin );
					form.appendChild( formInput );

					document.body.appendChild( iframe );
					document.body.appendChild( form );

					form.submit();
					document.body.removeChild( form );
            	}
				// Commenting out the below snippet for Upcoming dev
				if(G5.props.URL_IDS_SSO_END_POINT) {
					signOnToId( G5.props.URL_IDS_SSO_END_POINT );
				}

				

        </script>
    <!-- No additional script resources or blocks should be added after this one -->

	
	
	

	    <tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
		<tiles:insert attribute='webtracking'>
	    <tiles:put name="trackingTitle"><%=trackingTitle%></tiles:put></tiles:insert>

	</body>

</html>

