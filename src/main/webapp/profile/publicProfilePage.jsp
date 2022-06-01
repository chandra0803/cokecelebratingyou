<%@ page import="java.util.Date" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.*" %>

<script>
function getTDStats(promoId)
{
	G5.props.URL_HTML_THROWDOWN_PUBLIC_PROFILE = "${profileUrl}"+'&isPlayerStatsView=true'+'&promotionId='+promoId;
}
</script>

<!-- ======== PUBLIC PROFILE PAGE ======== -->
<div id="publicProfileWrapper" class="page-content profile page publicProfile">

    <div class="page-topper">
        <%-- User info and user menu  --%>
        <div class="row-fluid">
            <div class="span6">
                <span class="avatarwrap small48">
                        <!-- if profile photo -->
                        <c:if test="${ pax.avatarSmallFullPath != null }">
                        <%
                            Long time = new Date().getTime(); 
                            String ts = "?ts=" + time.toString();;
                            pageContext.setAttribute("ts", ts);
                        %>
                           <img id="partProfAvatar" alt="<c:out value="${pax.firstName}" /> <c:out value="${pax.lastName}" /> Picture" src="${pax.avatarSmallFullPath}<%= ts %>">
                        </c:if>
                        <c:if test="${ pax.avatarSmallFullPath == null }">
                            <c:set var="fcName" value="${pax.firstName.substring(0,1)}"/>
                            <c:set var="lcName" value="${pax.lastName.substring(0,1)}"/>
                            <span class="avatar-initials">${fcName}${lcName}</span>
                        </c:if>
                    </span>


                <h3><span id="partProfFirstName"><c:out value="${pax.firstName}" /></span> <span id="partProfLastName"><c:out value="${pax.lastName}" /></span></h3>
            </div>
            <div class="span6">
            <c:if test="${beacon:systemVarBoolean('install.recognition')}">
                <c:if test="${ recognizeOther }">

                    <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
	                	<a href='#' class="btn btn-primary regonizeFromPubProfile fr" data-participant-id="${pax.id}" data-node-id="${pax.primaryUserNode.node.id}"> <cms:contentText key="RECOGNIZE" code="participant.profile" /> <!--<i class="icon-team-1"></i>--></a>
	                	<c:if test="${beacon:systemVarBoolean('profile.followlist.tab.show')}">
			                <c:choose>
			                  <c:when test="${ followed }">
			                	<a href='#' class="btn btn-danger fl miniProfFollowLink unfollow" style="display:block" data-participant-id="${pax.id}"><cms:contentText key="UNFOLLOW" code="participant.profile" /> <!--<i class="icon-user"></i>--></a>
			                	<a href='#' class="btn btn-primary fl miniProfFollowLink follow" style="display:none" data-participant-id="${pax.id}"> <cms:contentText key="FOLLOW" code="participant.profile" /> <!--<i class="icon-user-add"></i>--></a>
			                  </c:when>
			                  <c:otherwise>
			                	<a href='#' class="btn btn-danger fl miniProfFollowLink unfollow" style="display:none" data-participant-id="${pax.id}"> <cms:contentText key="UNFOLLOW" code="participant.profile" /> <!--<i class="icon-user"></i>--></a>
			                	<a href='#' class="btn btn-primary fl miniProfFollowLink follow" style="display:block" data-participant-id="${pax.id}"> <cms:contentText key="FOLLOW" code="participant.profile" /> <!--<i class="icon-user-add"></i>--></a>
			                  </c:otherwise>
			                </c:choose>
		                </c:if>
	                </c:if>
                </c:if>
             </c:if>
            </div>
        </div>

        <div class="row-fluid">
            <div class="span12" id="publicProfileTabs">
                <ul class="nav nav-tabs">
                    <li class="tabPersonalInfo active"><a href="#PersonalInfo" data-toggle="tab"><cms:contentText key="PERSONAL_INFO" code="profile.personal.info" /></a></li>
                    <c:if test="${ showBadge }"><li class="tabBadges"><a href="#Badges" data-toggle="tab"><cms:contentText key="EARNED_BADGE" code="profile.personal.info" /></a></li></c:if>
                    <c:if test="${beacon:systemVarBoolean('install.recognition')}">
                    	<li class="tabRecognition"><a href="#Recognition" data-toggle="tab"><cms:contentText key="RECOGNITION" code="profile.personal.info" /></a></li>
                    </c:if>
                    <c:if test="${ not empty promotions }"><li class="tabPlayerStats"><a href="#PlayerStats" data-toggle="tab"><cms:contentText key="THROWDOWN_STATS" code="participant.participant" /></a></li></c:if>
                </ul>
            </div><!-- /#publicProfileTabs.span12 -->
        </div><!-- /.row-fluid -->
    </div><!-- /.page-topper -->

    <div class="tab-content">
        <div class="tab-pane active" id="PersonalInfo">
            <div class="row-fluid" id="profilePagePersonalInfoTab"><%-- ID borrowed from the regular profile page --%>

                <div class="span12">
                    <h2><cms:contentText key="TITLE" code="profile.personal.info" /></h2>
                </div>

                <div class="span3">
                    <span class="avatarwrap">
                        <!-- if profile photo -->
                        <c:if test="${ pax.avatarSmallFullPath != null }">
                        <%
                            Long time = new Date().getTime(); 
                            String ts = "?ts=" + time.toString();;
                            pageContext.setAttribute("ts", ts);
			            %>
                           <img class="avatar" height="160" width="160" alt="<c:out value="${pax.firstName}" /> <c:out value="${pax.lastName}" /> Picture" id="personalInformationAvatar" src="${pax.avatarSmallFullPath}<%= ts %>">
                        </c:if>
                        <c:if test="${ pax.avatarSmallFullPath == null }">
                            <c:set var="fcName" value="${pax.firstName.substring(0,1)}"/>
                            <c:set var="lcName" value="${pax.lastName.substring(0,1)}"/>
                            <span class="avatar-initials">${fcName}${lcName}</span>
                        </c:if>
                    </span>


                </div>

                <div id="profileMainInfoWrapper" class="span5">
                    <dl class="dl-horizontal">
                        <dt><cms:contentText key="NAME" code="profile.personal.info" /></dt>
                        <dd>
                            <span><c:out value="${pax.firstName}" />&nbsp;<c:out
                                    value="${pax.lastName}" /> </span>
                        </dd>

                        <dt><cms:contentText key="ORG_NAME" code="profile.personal.info" /></dt>
                        <dd>
                            <c:forEach var="node" items="${pax.userNodes}">
                                <p>
                                    <c:out value="${node.hierarchyRoleType.name}"/> - <c:out value="${node.node.name}" />
                                </p>
                            </c:forEach>
                        </dd>
                        <dt><cms:contentText key="DEPARTMENT" code="profile.personal.info" /></dt>

                         <c:if test="${deptName ne null}">
                        <dd>
                            <span>${ deptName }</span>
                        </dd>
                        </c:if>

                        <dt><cms:contentText key="JOB_TITLE" code="profile.personal.info" /></dt>

                        <c:if test="${positionName ne null}">
                        <dd>
                            <span>${ positionName }</span>
                        </dd>
                        </c:if>

                        <dt><cms:contentText key="COUNTRY" code="profile.personal.info" /></dt>
                        <dd>
                            <img src="<%=RequestUtils.getBaseURI(request)%>/assets/img/flags/${pax.primaryCountryCode}.png"/>
                            <c:out value="${pax.primaryCountryName}" />
                        </dd>

                       <c:if test="${beacon:systemVarBoolean('show.participant.hire.date')}">
						<c:if test="${ pax.allowPublicHireDate }">
                          <dt><cms:contentText key="HIRE_DATE" code="profile.personal.info" /></dt>
                          <c:forEach items="${pax.participantEmployers}" var="employer">
                          <c:if test="${employer.terminationDate eq null}">
                          <dd>
                            <fmt:formatDate value="${employer.hireDate}" type="date" pattern="${sessionScope.loggedInUserDate}"/>
                          </dd>
                          </c:if>
                          </c:forEach>
                        </c:if>
                       </c:if>
                       <c:if test="${beacon:systemVarBoolean('show.participant.birth.date')}">
						<c:if test="${ pax.birthDate != null }">
	                       <c:if test="${ pax.allowPublicBirthDate }">
	                          <dt><cms:contentText key="BIRTH_DATE" code="profile.personal.info" /></dt>
	                          <dd>
	                            <fmt:formatDate type="date" pattern="MMMM dd" value="${pax.birthDate}" />
	                          </dd>
	                       </c:if>
                        </c:if>
                       </c:if>
                    </dl>
                    <c:if test="${ recognizeOther }">
                    	<button type="button" class="btn btn-primary btnCheers cheers-popover" data-iseasy="true" data-cheers-promotion-id="" data-participant-ids="${paxId}"><i class="icon-cheers"></i><cms:contentText key="CHEERS" code="client.cheers.recognition" /></button>
                    </c:if>
                </div><!-- /#profileMainInfoWrapper.span5 -->
                
                
                                <!-- Cheers Customization - Celebrating you wip-62128 starts- Totally restructured this profileQuestionsWrapper - This section will have additional functionalities like comments, uploads and like options -->
                <div id="profileQuestionsWrapper">
                <c:if test="${not empty aboutMe}">
                    <h3><cms:contentText key="ABOUT_ME" code="profile.personal.info" /></h3>                    
                    <div class="row-fluid" id="iAmWrapper">
                    
                    </div>
					<script id="iAmItemTpl" type="text/x-handlebars-template">
                        <div class="span4 likeAndCommentsWrapper" id="iam_{{id}}">
                            <label for="question1" class="statement"><strong>I am</strong></label>
                            <p class="question">{{iamComment}}</p>

                            {{#if isLiked}}
                                <a href="#" class="iamCommentsLike liked mylike" data-iam-id="{{id}}" id="iamCommentsLike_{{id}}" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i>Unlike</a>      
                                <span id="iamLikeCounts_{{id}}"><span class="numLikers">({{numLikers}})</span></span>
                                <div class="mask" style="display:none"></div>    
                            {{else}}
                                <a href="#" class="iamCommentsLike" data-like="Like" data-unlike="Unlike" data-iam-id="{{id}}" id="iamCommentsLike_{{id}}"><i class="icon-star"></i>Like</a>     
                                    <span id="iamLikeCounts_{{id}}" ><span class="numLikers">({{numLikers}})</span></span>    
                                    <div class="mask" style="display:none"></div>     
                            {{/if}}
                        </div>
                    </script>
                    </c:if>
                    <div class="contribCommentWrapper careerMomentsCommentsWrapper">
                       <div class="mask" style="display:none"></div>                       
                       <div class="indent">
                          <div class="indent-row">
                             <div class="commentWrapper">
                                <div class="contribName">
                                    <h3>Share something with <c:out value="${pax.firstName}" />&nbsp;<c:out
                                    value="${pax.lastName}" /> </h3>
                                </div>
								<div class="commentTools">
                                    <cms:contentText key="REMAINING_CHARACTERS" code="purl.contributor"/> <span class="remChars"><c:out value="${beacon:systemVarInteger('purl.comment.size.limit')}"/></span>
                                    <span class="spellchecker dropdown">
                                        <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                                title=<cms:contentText key="CHECK_SPELLING" code="purl.contributor"/>
                                                data-toggle="dropdown">
                                                <i class="icon-check"></i>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li><a class="check"><b><cms:contentText key="CHECK_SPELLING" code="purl.contributor"/></b></a></li>
                                        </ul>
                                    </span>
                                </div>
								
                                <div class="commentFauxInput">
                                   <textarea class="contribCommentInp" rows="4" maxlength="<c:out value="${beacon:systemVarInteger('purl.comment.size.limit')}"/>" placeholder=<cms:contentText key="ADD_COMMENT_OR_PASTE_VIDEO" code="purl.contributor"/>></textarea>

                                   <div class="contribCommentBadWords"></div>
                                   <div class="uploadWrapper">
                                      <input type="file" name="commentUploadFiles" class="uploadInput ready">
                                      <!-- <img class="uploadBtn" src="../../skins/default/img/video-upload-icon-white.png"/>
                                         <span class="uploadBtn-mobile ready">
                                             <img src="../../skins/default/img/upload-icon-mobile.png"/>
                                             <span>Upload Photo or Video</span>
                                         </span> -->
                                      <button class="uploadBtn btn"><i class="icon-camera"></i> <i class="icon-video-camera"></i></button>
                                      <div class="attachedMediaDisplayWrapper" style="display:none">
                                         <div class="attachedMediaContent">
                                            <!-- dyn -->
                                         </div>
                                      </div>
                                      <span class="upSpin" style="display:none">&nbsp;</span>
                                   </div>
                                   <!-- /.uploadWrapper -->
                                </div>
                                <!-- /.commentFauxInput -->
                             </div>
                             <!-- /.commentWrapper -->
                          </div>
                          <!-- /.indent-row -->
                          <div class="contribBottomControls">
                             <button class="btn btn-primary btn-fullmobile submitContribCommentBtn" disabled="disabled">
                             <cms:contentText key="SUBMIT" code="system.button"/>
                             </button>                             
                          </div>
                          <!-- /.contribBottomControls -->
                       </div>
                       <!-- /.indent -->
                    </div>
                    <div class="careerMomentsCommentsSection">
                        <div class="lowerControls">
                            <c:if test="${isUserLoggedIn}">
                <c:if test="${isOtherlocale}">
                    <button class="translateTextBtn btn btn-primary btn-fullmobile" style="display:none"><cms:contentText key="TRANSLATE" code="purl.recipient"/></button>
                </c:if>
            </c:if>
                        </div>

                        <div class="commentsListWrapper">
                            <!-- dyn -->
                        </div>
                    </div><!-- /.careerMomentsCommentsSection -->
                    
                    <script id="levelOneCommentItemTpl" type="text/x-handlebars-template">
                    <!-- publicProfileSubComments {{id}} -->
                    <div class="app-row comment-block clearfix publicProfileSubComments comment{{commentId}}" data-comment-id="{{commentId}}">

                        <div class="app-col">
                            <div class="avatarwrap commenterAvatar">
                                <a class="profile-popover " data-participant-ids="[{{commenter.id}}]">
                                    {{#if commenter.avatarUrl}}
                                        <img alt="{{commenter.firstName}} {{commenter.lastName}}" src="{{commenter.avatarUrl}}" />
                                    {{else}}
                                        <span class="avatar-initials">{{trimString commenter.firstName 0 1}}{{trimString commenter.lastName 0 1}}</span>
                                    {{/if}}
                                </a>
                            </div>

                        </div>

                        <div class="app-col comment {{#if isMine}}mine{{/if}}">

                            <p>
                                <a class="profile-popover" href="#" data-participant-ids="[{{commenter.id}}]">
                                    {{commenter.firstName}} {{commenter.lastName}}
                                </a>
                                <div class="readMore" data-read-more-num-lines="2" data-msg-read-more="more">
                                    {{comment}}
                                </div>
                                <div class="likeAndCommentsWrapper" data-comment-id="{{levelOneId}}">
                                    <div class="mask" style="display:none"></div>                      
                                    {{#if isLiked}}
                                        <a href="#" class="commentsLike liked mylike" id="commentsLike_{{levelOneId}}" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i>Unlike</a>      
                                        <span id="likeCounts_{{levelOneId}}"><span class="numLikers">({{numLikers}})</span></span>
                                        
                                    {{else}}
                                        <a href="#" class="commentsLike" data-like="Like" data-unlike="Unlike" id="commentsLike_{{levelOneId}}"><i class="icon-star"></i>Like</a>     
                                            <span id="likeCounts_{{levelOneId}}" ><span class="numLikers">({{numLikers}})</span></span>         
                                    {{/if}}    
                                </div>                                

                                {{#if isMine}}
                                    
                                    {{#if allowTranslate}}
                                        <span class="translateCont">
                                            <a href="#" class="translatePubRec">Translate</a>
                                        </span>
                                    {{/if}}

                                {{else}}
                                    {{#if allowTranslate}}
                                        <span class="translateCont">
                                            <a href="#" class="translatePubRec">Translate</a>
                                        </span>
                                    {{/if}}

                                {{/if}}

                            </p>
                        </div>
                    </div>
                    <!-- /publicProfileSubComments {{id}} -->


                    </script>
                    
                    <script id="commentItemTpl" type="text/x-handlebars-template">
                        {{#if _isEmpty}}
                        <div class="row-fluid commentsItemWrapper isEmptyMessage">
                            <div class="span12">
                                <div class="alert alert-info">No contributions yet. You could be first!</div>
                            </div>
                        </div>
                        {{else}}
                        <div class="row-fluid commentsItemWrapper comment_{{commentId}}" data-id="{{commentId}}">
                            <div class="span12 commentItem">
                                <div class="innerCommentWrapper">
                                    <!-- need to update json to have first name and last name -->
                                    <span class="avatarwrap avatarImg">
                                        {{#if userInfo.0.profilePhoto}}
                                            <img src="{{userInfo.0.profilePhoto}}"  />
                                        {{else}}
                                            <div class="avatar-initials">{{trimString userInfo.0.firstName 0 1}}{{trimString userInfo.0.lastName 0 1}}</div>
                                        {{/if}}
                                    </span>
                                    <div class="indent">
                                        <div class="userInfo" data-id="{{userInfo.0.contributorID}}">
                                            {{userInfo.0.lastName}} {{userInfo.0.firstName}}
                                        </div>
                                        {{#if commentText}}
                                        <div class="text">
                                            {{commentText}}
                                        </div>
                                        <!--div class="translateTextLink"><a href="clientState=XXX"  data-id="{{commentId}}">Translate</a></div-->
                                        {{/if}}

                                        

                                        {{#or videoHtml media}}
                                        <div class="media">
                                            {{#if videoHtml}}
                                                {{#each videoHtml}}
                                                <div class="videoWrapper">
                                                    <div class="responsiveVideoContainer">
                                                        {{{this}}}
                                                    </div>
                                                </div>
                                                {{/each}}
                                            {{else}}
                                                {{#if videoWebLink}}
                                                    <a class="mediaLink" href="{{videoWebLink}}" target="_blank">{{videoWebLink}}</a>
                                                    <i class="icon-share-1"></i>
                                                {{/if}}
                                            {{/if}}

                                            {{#if media}}
                                                {{#if media.0.video}}
                                                <div class="videoWrapper">
                                                    <div class="responsiveVideoContainer">
                                                        <video id="careerMomentContribVideo{{commentId}}" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                                                            <source src="{{media.0.video.src}}" type="video/{{media.0.video.fileType}}">
                                                        </video>
                                                    </div>
                                                </div>
                                                {{/if}}

                                                {{#if media.0.photo.src}}
                                                	<img src="{{media.0.photo.src}}">
                                                {{/if}}
                                            {{/if}}
                                        </div>
                                        {{/or}}

<div class="likeAndCommentsWrapper" data-contributor-id="{{userInfo.0.contributorID}}" data-comment-id="{{commentId}}" id="likeAndCommentsWrapper_{{commentId}}">
                                            <div class="mask" style="display:none"></div>                      
                                            {{#if isLiked}}
                                                <a href="#" class="commentsLike liked mylike" id="commentsLike_{{commentId}}" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i>Unlike</a>      
                                                <span id="likeCounts_{{commentId}}"><span class="numLikers">({{numLikers}})</span></span>
                                                
                                            {{else}}
                                                <a href="#" class="commentsLike" data-like="Like" data-unlike="Unlike" id="commentsLike_{{commentId}}"><i class="icon-star"></i>Like</a>     
                                                    <span id="likeCounts_{{commentId}}" ><span class="numLikers">({{numLikers}})</span></span>         
                                            {{/if}}
                                            <a href="#" class="userComment">
                                               <i class="icon-message-2"></i>Comment
                                            </a>
                                            <div class="app-row comment-block">
                                                <div class="app-col commentInputWrapper" style="display:none;">
                                                    <!-- DEVELOPER NOTE: the form action attr below will be used as the URL to get JSON
                                                        - make sure the class 'publicRecognitionCommentForm' stays on the form element
                                                    -->
                                                    <form class="form-inline publicProfileCommentForm" action="saveComments.do?method=saveSubComment">

                                                        <textarea name="comment" class="comment-input commentInputTxt" placeholder="Leave A Comment" maxlength="300"></textarea>

                                                            <div class="levelOneComments" style="">
                                                                    <!-- dynamic - populated by view -->
                                                            </div><!-- /.pubRecComments -->

                                                    </form>
                                                </div><!-- /.commentInputWrapper -->
                                            </div>
                                        </div>
                                    </div><!-- /.indent -->
                                </div><!-- /.innerCommentWrapper -->
                            </div>
                        </div>
                        {{/if}}
                    </script>  
                    <!-- Cheers Customization - Celebrating you wip-62128 ends -->
                </div><!-- /#profileQuestionsWrapper.span12 -->

                

            </div><!-- /.row-fluid -->
        </div><!-- /#PersonalInfo.tab-pane -->

        <div class="tab-pane pubProfileBadgePane" id="Badges">
            <div class="row-fluid" id="profilePageBadgesTab"><%-- ID borrowed from the regular profile page --%>
                <div class="span12">

                    <div class="badge-groups"></div>

                </div><!-- /.span12 -->
            </div><!-- /.row-fluid -->
        </div><!-- /#Badges.pubProfileBadgePane.tab-pane -->

        <div class="tab-pane pubRecTabs publicRecognition page" id="Recognition"><%-- .publicRecognition and .page were borrowed to apply the pubRec styles --%>
            <%-- publicRecognition doesn't get row-fluid or span12 on purpose --%>
            <%-- <div class="publicRecognitionItems"></div> --%>

            <!-- Recognitions -->
            <div class="page-liner pubRecItemsCont" data-msg-empty="<cms:contentText key="NO_RECOGNITION_FOUND" code="recognition.public.recognition.item" />">

                <div class="publicRecognitionItems">
                    <!-- dynamic - pubRecItems -->
                </div>

                <!-- shown when there are more recognitions -->
                <div class="app-row-fluid">
                    <p>
                        <a href="#" class="viewAllRecognitions"  style="display: none">
                            <cms:contentText key="VIEW_MORE" code="recognition.public.recognition.item" />
                        </a>
                    </p>
                </div>

            </div>

        </div><!-- /#Recognition.pubRecTabs.tab-pane -->

         <div class="tab-pane pubProfilePlayerStats" id="PlayerStats">
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
         </div><!-- /#PlayerStats.pubProfilePlayerStats.tab-pane -->

    </div>

    <div class="modal hide fade module recognition" id="ezRecognizeMiniProfileModal" data-backdrop="static" data-keyboard="false">
    </div>



</div>

<input type="hidden" id="participantId" value="${paxId}">

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    $(document).ready(function(){
        //attach the view to an existing DOM element
        var $profileWrapper = $('#publicProfileWrapper');
        var json;

        // bootstrap json
        json = ${jsonBean};
        var PPP = new PublicProfilePageView({
            el: $profileWrapper,
            json : json,
            pageTitle : '<cms:contentText key="PUBLIC_PROFILE_TITLE" code="profile.personal.info" />'
        });
        
        G5.props.URL_JSON_CAREERMOMENTS_IAMSAVE_LIKE = G5.props.URL_ROOT+'likeUnlike.do?method=likeAboutMe';
        G5.props.URL_JSON_CAREERMOMENTS_IAMSAVE_UNLIKE = G5.props.URL_ROOT+'likeUnlike.do?method=unlikeAboutMe';
        G5.props.URL_JSON_CAREERMOMENTS_ACTIVITY_FEED = G5.props.URL_ROOT+'comments.do?method=fetchComments';
        G5.props.URL_JSON_CAREERMOMENTS_POST_COMMENT = G5.props.URL_ROOT +'saveComments.do?method=saveComments';
        G5.props.URL_JSON_CAREERMOMENTS_SAVE_LIKE = G5.props.URL_ROOT+'likeUnlike.do?method=likeComment';
        G5.props.URL_JSON_CAREERMOMENTS_SAVE_UNLIKE = G5.props.URL_ROOT+'likeUnlike.do?method=unlikeComment';
        G5.props.URL_JSON_CAREERMOMENTS_UPLOAD_PHOTO = G5.props.URL_ROOT+'saveComments.do?method=processPhoto';
        
      	//Client customizations for WIP #62128
		G5.props.URL_JSON_CHEERS_INFO = G5.props.URL_ROOT+'cheersPopoverView.do?method=fetchCheersPoints';
        G5.props.URL_JSON_CHEERS_SEND_RECOGNITION = G5.props.URL_ROOT+'cheersPopoverView.do?method=postCheers';
		
        //public profile page Earned Badges tab JSON
       G5.props.URL_JSON_PUBLIC_PROFILE_LOAD_BADGES = G5.props.URL_ROOT+'profilePageBadgesTab.do?method=fetchBadgesForProfile&isFromMiniProfile=true';


  	   G5.props.URL_JSON_PUBLIC_RECOGNITION = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionResult.do?method=fetchPublicRecognitions';
	   G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_COMMENT = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionCommentAction.do?method=postComment';
	   G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_LIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=like';
	   G5.props.URL_JSON_PUBLIC_RECOGNITION_SAVE_UNLIKE = G5.props.URL_ROOT+'recognitionWizard/publicRecognitionLikeAction.do?method=unlike';
	   G5.props.URL_JSON_PUBLIC_RECOGNITION_TRANSLATE=G5.props.URL_ROOT + "recognitionWizard/publicRecognitionTranslate.do?method=translateComment";

		//public profile page Mini Profile Popup JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = '<%=RequestUtils.getBaseURI(request)%>/participantPublicProfile.do?method=populatePax';

        //public profile page Mini Profile PopUp Follow Unfollow Pax JSON, Action Class is PublicRecognitionAction
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = '<%=RequestUtils.getBaseURI(request)%>/publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // throwdown player stats
        G5.props.URL_HTML_THROWDOWN_PUBLIC_PROFILE = "${profileUrl}"+'&promotionId='+document.getElementById("promotionSelect").value;

        if('${defaultTab}' == "playerStats")
      	{
        	$('#publicProfileWrapper .tabPlayerStats a').click();
      	}
    });

</script>


<script type="text/template" id="profilePageBadgesTabTpl">
	<div class="badge-groups">
    {{#badgeGroups}}
        <div class="badge-group">
            <h3 class="badge-group-title">{{this.headerTitle}}</h3>
            <div class="badges">
                <ul>
                    {{#badges}}
                       <li class="badge-item {{this.type}}-type earned-{{#if earned}}true{{else}}false{{/if}}" id="badge{{id}}">
                            <img src="<%=RequestUtils.getBaseURI(request)%>/{{this.img}}" align="left" />
                            <span class="badge-name">{{this.name}}<br /></span>
                            <span class="badge-how-to-earn">{{this.howToEarnText}}<br /></span>
 							{{#if this.progressVisible}}
                            <div class="progress">
                                <div class="bar">{{this.progressNumerator}}/{{this.progressDenominator}}</div>
                            </div>
 							{{/if}}
							{{#if earned}}
                            <span class="badge-date-earned"><cms:contentText key="EARNED_DATE" code="gamification.admin.labels" /> {{this.dateEarned}}</span>
							{{/if}}
                        </li>
                    {{/badges}}
                </ul>
            </div>
        </div>
    {{/badgeGroups}}
	{{#unless badgeGroups}}
        <p class="alert alert-error"><cms:contentText key="NO_BADGES_TO_VIEW" code="gamification.validation.messages" /><p>
    {{/unless}}
</div>
</script>

<script type="text/template" id="publicRecognitionItemTpl">
		<%@include file="/publicrecognition/publicRecognitionItem.jsp"%>
</script>

<script type="text/template" id="publicRecognitionCommentTpl">
		<%@include file="/publicrecognition/publicRecognitionComment.jsp"%>
</script>

<script type="text/template" id="sharePopoverTpl">
		<%@include file="/publicrecognition/sharePopover.jsp"%>
</script>
<script type="text/template" id="cheersPopoverViewTpl">
		<%@include file="/client/cheersPopoverView.jsp" %>
	</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
