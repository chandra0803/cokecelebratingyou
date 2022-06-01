<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="globalHeaderViewTpl">
    <div id="globalHeaderView">
            {{#if clientLogo}}
            <h1 class="logo">
                <c:if test="${tAndC !=  true}">
                    <a href="${pageContext.request.contextPath}/homePage.do"></c:if><img src="{{clientLogo}}" alt="{{clientName}} logo" /></a>
            </h1>
            {{/if}} 
        {{#if loggedIn}}
            {{#if programLogo}}
            <p class="proglogo">
                <img src="{{programLogo}}" alt="{{programName}} logo" />
            </p>
            {{/if}}

			 
            <div class="pointsContainer {{#if showPoints}}showPoints{{else}}hidePoints{{/if}} {{#if isExternal}}hideRedeem{{else}}showRedeem{{/if}}">
                <span class="profile-points">
                    <span class="profile-points-label">
                        <cms:contentText key="POINTS" code="promotion.goalquest.progress"/> 
                    </span>
                    <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Statement" class="pointsLink">
                        {{formatNumber data.points}}
                    </a>
                </span>

			<beacon:authorize ifNotGranted="LOGIN_AS">
                    <c:if test="${(rewardOfferingShopExist == true)  or (rewardOfferingTravelExist == true) }">
                        {{#with redeem}}
                        {{#if status}}
                        <div id="vo-app">
                            <div class="dropdown" id="rm_dropdown">                           
                                                                                                                         
                               {{#unless is_dm_exist}}
                                 <a class="btn btn-primary btn-compact btn-inverse" href="${pageContext.request.contextPath}/{{rm_btn_url}}" target="_blank">
                                   {{#eq menu.[0].title "<cms:contentText key="MENU_EXPERIENCES" code="redeem.menu"/>"}}
                                     <img class="rm_icon" src="${pageContext.request.contextPath}/assets/img/redeem/icon_EM_color.png"/>
								   {{/eq}} 
                                   {{#eq menu.[0].title "<cms:contentText key="MENU_MERCHANDISE" code="redeem.menu"/>"}}
                                     <img class="rm_icon" src="${pageContext.request.contextPath}/assets/img/redeem/icon_MM_color.png"/>
								   {{/eq}} 
                                   <span>{{menuName}}</span>
                    </a>
                                {{/unless}} 
                                {{#if is_dm_exist}}
								<span class="btn  btn-inverse btn-primary dropdown-toggle rm_title {{#if is_dm_exist}}rm_title_padding{{/if}}" data-toggle="dropdown">
                                <span class="mbl_hide">{{menuName}}</span> 
                                  <span class="show_mble"> 
								    <svg xmlns="http://www.w3.org/2000/svg" width="15" height="15.625" viewBox="0 0 15 15.625">
                                        <g id="shop_1_" data-name="shop (1)" transform="translate(0 0)">
                                        <path id="Shape" d="M12.752,15.625H2.263a.766.766,0,0,1-.749-.781V9.375h1.5V12.5H12V9.375h1.5v5.468A.767.767,0,0,1,12.752,15.625Zm.374-7.031A1.917,1.917,0,0,1,11.254,6.64a1.875,1.875,0,1,1-3.746,0A1.918,1.918,0,0,1,5.634,8.594,1.917,1.917,0,0,1,3.761,6.64,1.917,1.917,0,0,1,1.888,8.594,1.917,1.917,0,0,1,.015,6.64H0l1.573-3.82a.745.745,0,0,1,.689-.477h10.49a.747.747,0,0,1,.644.391H13.4l.022.07a.041.041,0,0,1,.009.015.085.085,0,0,0,.006.017L15,6.64A1.917,1.917,0,0,1,13.127,8.594Zm-.374-7.032H2.263A.766.766,0,0,1,1.514.781.766.766,0,0,1,2.263,0h10.49a.767.767,0,0,1,.75.781A.767.767,0,0,1,12.752,1.562Z" fill="#3b4243"/>
                                        </g>
                                    </svg>								
                                  </span>
                                  <i class="icon-arrow-1-down arrowToken"></i>                                 
                                </span>                                
                                <ul class="dropdown-menu" role="menu">                                  
                                      <div class="rm_container">
                                            {{#each menu}}
                                                <ul class="rm_ul_list">
                                                   <li class="rm_li_item">                                                      
                                                      <div class="rm_menu_div">
                                                            <a class="rm_menu_link"  target="_blank" href="${pageContext.request.contextPath}/{{url}}" title="{{title}}">
															<div class="rm_img_div">													  
                                                        {{#eq title "<cms:contentText key="MENU_EXPERIENCES" code="redeem.menu"/>"}}
    														<img class="rm_img" src="${pageContext.request.contextPath}/assets/img/redeem/redeem-EM.jpg" />
														{{/eq}}
														{{#eq title "<cms:contentText key="MENU_MERCHANDISE" code="redeem.menu"/>"}}
    														<img class="rm_img" src="${pageContext.request.contextPath}/assets/img/redeem/redeem-MM.jpg" />
														{{/eq}}

                                                      </div> 
													  <div class="rm_menu_container">
                                                            <div class="rm_menu_img_div">
															    {{#eq title "<cms:contentText key="MENU_EXPERIENCES" code="redeem.menu"/>"}}
                                                                 <img src="${pageContext.request.contextPath}/assets/img/redeem/icon_EM_color.png"/>
                                                                 <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
	                                                                viewBox="0 0 80 80" style="enable-background:new 0 0 80 80;" xml:space="preserve">
                                                                    <path id="original_EM" d="M78.4,1.7L1.6,24.9l41,9.6l0,0L45,35l10.1,43.3L78.4,1.7z M64,35.3l-13.4-3.1l20.5-20.5L64,35.3z
	                                                                M17.1,24.4L68.3,8.9L46,31.2L17.1,24.4z M62.8,39.2l-7.2,23.7L49.3,36L62.8,39.2z"/>
                                                                </svg>
																{{/eq}} 
																{{#eq title "<cms:contentText key="MENU_MERCHANDISE" code="redeem.menu"/>"}}
                                                                 <img src="${pageContext.request.contextPath}/assets/img/redeem/icon_MM_color.png"/>
                                                                 <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
	                                                                viewBox="0 0 80 80" style="enable-background:new 0 0 80 80;" xml:space="preserve">
                                                                    <style type="text/css">
	                                                                    .st0{fill-rule:evenodd;clip-rule:evenodd;}
                                                                    </style>
                                                                <path class="st0" d="M75.4,4.6H42.5L4.6,42.5l32.9,32.9l38-37.9L75.4,4.6L75.4,4.6z M35.9,68.1L12.2,44.5H38L35.9,68.1z M38.3,40.5
	                                                                H12.2l28.6-28.6L38.3,40.5z M40,67.3l5.2-58.7h26.2v27.2L40,67.3z"/>
                                                                <path class="st0" d="M64.4,12.1c-1.7,0-3,1.3-3,3s1.3,3,3,3c1.7,0,3-1.3,3-3S66.1,12.1,64.4,12.1z"/>
                                                                </svg>
																{{/eq}}                                                                                                                                  
                                                            </div>                                                            
                                                            <span class="rm_menu_title">{{title}}</span>
                                                            <div class="rm_menu_arrow_div">
															<svg width="16px" height="16px" viewBox="0 0 16 16" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
                                                                <!-- Generator: Sketch 55.2 (78181) - https://sketchapp.com -->
                                                                <title>noun_Right Up Arrow_108617@2x</title>
                                                                <desc>Created with Sketch.</desc>
                                                                <g id="Page-1" stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
                                                                <g id="redeem-nonotch-mobile-copy-6" transform="translate(-261.000000, -317.000000)" fill="#000000" fill-rule="nonzero" stroke="#676869">
                                                                <g id="Group" transform="translate(13.000000, 40.000000)">
                                                                <g id="Group-4" transform="translate(22.000000, 252.000000)">
                                                                <g id="noun_Right-Up-Arrow_108617" transform="translate(227.000000, 26.000000)">
                                                                    <path d="M14,0.17369727 C14,0.104218362 13.9480841,0.0347394541 13.8961681,0.017369727 C13.8961681,0.017369727 13.8961681,0.017369727 13.8961681,0.017369727 C13.8788628,0.017369727 13.8615575,0 13.8269468,0 L0.242274413,0 C0.138442522,0 0.0692212608,0.0694789082 0.0692212608,0.17369727 C0.0692212608,0.277915633 0.138442522,0.347394541 0.242274413,0.347394541 L13.4116193,0.347394541 L0.0519159456,13.6526055 C-0.0173053152,13.7220844 -0.0173053152,13.8263027 0.0519159456,13.8957816 C0.086526576,13.9305211 0.138442522,13.9478908 0.173053152,13.9478908 C0.224969098,13.9478908 0.259579728,13.9305211 0.294190358,13.8957816 L13.6538937,0.59057072 L13.6538937,13.8263027 C13.6538937,13.9305211 13.723115,14 13.8269468,14 C13.9307787,14 14,13.9305211 14,13.8263027 L14,0.17369727 C14,0.17369727 14,0.17369727 14,0.17369727 Z" id="Path"></path>
                                                                </g></g></g></g></g>
                                                            </svg>
															</div>
															</div>
                                                            </a>
                                                      </div>                                                  
                                                    </li>
                                                </ul>
                                                {{#gte ../menu.length 2}}
                                                   {{#eq @index 0}}                                                
                                                    <div class="rm_menu_divider"></div>        
                                                   {{/eq}} 
                                                {{/gte}}
                                            {{/each}}
                                      </div>                                   
                                </ul>
                               {{/if}}
                            </div>
                        </div>
                      {{/if}}
                      {{/with}}
                </c:if>
			</beacon:authorize>
            </div>
        {{/if}}
    </div>
</script>
