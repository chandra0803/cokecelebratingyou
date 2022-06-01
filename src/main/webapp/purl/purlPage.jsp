<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.purl.PurlRecipient" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.service.system.SystemVariableService" %>
<%@ page import="com.biperf.core.utils.*" %>
<!-- ======== PURLCONTRIBUTE: PURL MAIN PAGE ======== -->

<c:choose>
    <c:when test="${not empty purlContributionForm}">
        <c:set var="isContributor" value="true"/>
        <c:set var="commentOrderDescending" value="true"/>
        <c:set var="baseURI" value="<%=RequestUtils.getBaseURI(request)%>"/>
        <c:set var="targetPurlRecipient" value="${purlContributor.purlRecipient}"/>
        <c:set var="purlContributor" value="${purlContributor}"/>
         <c:set var="process" value="${targetPurlRecipient.awardProcessed}" />
         <c:set var="defaultInvitee" value="${purlContributor.defaultInvitee}"/>
    </c:when>
    <c:otherwise>
        <c:set var="isContributor" value="false"/>
        <c:set var="commentOrderDescending" value="false"/>
        <c:set var="baseURI" value="${siteUrlPrefix}"/>
        <c:set var="targetPurlRecipient" value="${purlRecipient}"/>
         <c:set var="process" value="${targetPurlRecipient.awardProcessed}" />

        <%
            String siteUrlPrefixParam = (String)request.getAttribute("siteUrlPrefix");
            Long loggedinUserId = (Long)request.getAttribute("loggedinUserId");
            PurlRecipient purlRecipientInfo = (PurlRecipient)request.getAttribute("purlRecipient");
            Map<String,Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put( "purlRecipientId", purlRecipientInfo.getId() );
            parameterMap.put( "loggedinUserId", loggedinUserId );
            pageContext.setAttribute("redeemAwardUrl", ClientStateUtils.generateEncodedLink( siteUrlPrefixParam, "/purl/purlRecipientSubmit.do?method=redeemAward", parameterMap ) );
            parameterMap.put( "showThankYou", new String( "true") );
            pageContext.setAttribute("thankyouModalForwardUrl", ClientStateUtils.generateEncodedLink( siteUrlPrefixParam, "/purl/purlRecipientSubmit.do?method=display&PurlUrlForward=viewThankYou", parameterMap ) );
            String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
            request.setAttribute( "siteUrl", siteUrl );
        %>
    </c:otherwise>
</c:choose>

<c:choose><c:when test="${targetPurlRecipient.customElements[0].value != null}"><c:set var="formElement1" value="${targetPurlRecipient.customElements[0].value} "/></c:when><c:otherwise><c:set var="formElement1" value=""/> </c:otherwise></c:choose>
<c:choose><c:when test="${targetPurlRecipient.customElements[1].value != null}"><c:set var="formElement2" value="${targetPurlRecipient.customElements[1].value} "/></c:when><c:otherwise><c:set var="formElement2" value=""/> </c:otherwise></c:choose>
<c:choose><c:when test="${targetPurlRecipient.customElements[2].value != null}"><c:set var="formElement3" value="${targetPurlRecipient.customElements[2].value} "/></c:when><c:otherwise><c:set var="formElement3" value=""/> </c:otherwise></c:choose>


<!-- if contributor add class 'isContributor'  -->
<c:choose>
    <c:when test="${isContributor}">
        <div id="purlPageView" class="page-content purlPage isContributor">
    </c:when>
    <c:otherwise>
        <div id="purlPageView" class="page-content purlPage">
    </c:otherwise>
</c:choose>

    <!--
        need to check if media
        if media set position of els
        if video, hide video bg and show image bg
        add ro remove no-media class
        if media add class no-media
    -->

    <c:choose>
        <c:when test="${mediaType}">
            <div class="row-fluid purlDetailsSection ">
        </c:when>
        <c:otherwise>
            <div class="row-fluid purlDetailsSection  no-media">
        </c:otherwise>
    </c:choose>


         <div class="span12">
            <div class="page-topper">

                <!-- bg containers -->
                <div class="purlmain-vid-container purlmain-fulldiv">
                    <video id='video' playsinline autoplay muted loop preload="auto"></video>
                </div>

                <div class="purlmain-bg-container purlmain-fulldiv"></div>
                <div class="purlDetailsOuterWrapper">



                    <div class="contributeToPurlSection">
                        <div class="avatarOuterWrap">
                            <span class="avatarwrap contribAvatar">
    	                        <c:if test="${ targetPurlRecipient.user.avatarSmall != null }">
                                <%
                                    Long time = new Date().getTime(); 
                                    String ts = "?ts=" + time.toString();;
                                    pageContext.setAttribute("ts", ts);
			                    %>
    	                        	<img id="recipientPartProfAvatar" src="<c:out value="${targetPurlRecipient.user.avatarSmall}"/><%=ts%>">
    	                        </c:if>
                            	<c:if test="${ targetPurlRecipient.user.avatarSmall == null }">
                                	<c:set var="fcName" value="${targetPurlRecipient.user.firstName.substring(0,1)}"/>
                                	<c:set var="lcName" value="${targetPurlRecipient.user.lastName.substring(0,1)}"/>
                                	<div class="avatar-initials">${fcName}${lcName}</div>
                          	 	</c:if>
                            </span>
                            <c:if test="${ targetPurlRecipient.anniversaryNumberOfYears != 0 }">
                                <div class="celebrationInfo">
                                    <span class="promotion">${ targetPurlRecipient.anniversaryNumberOfYears }</span>
                                    <span class="balloon"></span>
                                    <span class='celebrationCircleMask'></span>
                                </div>
                            </c:if>
                        </div>

                        <div class="contributeRecipentInfo">
                            <span class="headline_2 congrats-intro"><cms:contentText key="CONGRATULATE" code="purl.contributor"/></span>
                            <span class="headline_3 congrats-name">${targetPurlRecipient.user.firstName}</span>
                        </div>

                        <p class="contribIntro">
                       	   <c:if test="${defaultInvitee}">
                            <cms:contentTemplateText code="purl.contributor" key="WELCOME_TITLE_2" args="${targetPurlRecipient.contributorDisplayInfo},${targetPurlRecipient.user.firstName},${targetPurlRecipient.user.lastName},${targetPurlRecipient.user.firstName}" delimiter=","/>
                           </c:if>
                           <c:if test="${!defaultInvitee}">
                             <cms:contentTemplateText code="purl.contributor" key="WELCOME_TITLE_3" args="${targetPurlRecipient.contributorDisplayInfo},${targetPurlRecipient.user.firstName},${targetPurlRecipient.user.lastName},${targetPurlRecipient.user.firstName}" delimiter=","/>
						   </c:if>
                        </p>
                        <div class="contribTopControls">
                            <c:set var="purlExpireOn"><fmt:formatDate value="${targetPurlRecipient.closeDate}" pattern="${JstlDatePattern}" /></c:set>
                            <i class="contribUntil"><cms:contentTemplateText code="purl.contributor" key="PURL_EXPIRE" args="${purlExpireOn}"/></i>
                        </div>
                    </div><!-- /.contributeToPurlSection -->

                    <div class="recipientSection">
                        <c:if test="${ targetPurlRecipient.anniversaryNumberOfYears != 0 }">
                            <div class="purlCelebrateWrapper">
                                <div class="celebrationInfo">
                                    <span class="promotion">${ targetPurlRecipient.anniversaryNumberOfYears }</span>
                                    <span class="balloon"></span>
                                    <span class='celebrationCircleMask'></span>
                                </div>
                            </div>
                        </c:if>
                        <!-- JAVA NOTE: populate recipient info -->
                        <span class="headline_2 congrats-intro"><cms:contentText key="CONGRATS" code="purl.recipient"/></span>
                        <span class="headline_3 congrats-name">${targetPurlRecipient.user.firstName}</span>
                        <span class="headline_4 congrats-msg"><cms:contentTemplateText code="purl.recipient" key="CONGRATULATIONS_SUBHEADER" args="${formElement1},${formElement2},${formElement3},${targetPurlRecipient.promotion.name}" delimiter=","/></span>

                        <div class="purlDetailsWrapper row-fluid ">
                            <div class="purlTextWrapper span8">

                                <div class="purlDetailText "><cms:contentTemplateText code="purl.recipient" key="CONGRATULATORY_MESSAGE" args="${targetPurlRecipient.user.firstName}"/></div>

                                <!-- JAVA NOTE: for recipient only -->
                                <c:if test="${isRecipient}">
                                    <div class="purlDetailsRecipientControls" style="display:none">

                                        <!-- JAVA NOTE: populate date -->
                                        <c:set var="purlExpireOn"><fmt:formatDate value="${awardExpirationDate}" pattern="${JstlDatePattern}" /></c:set>
                                        <p class="expirationNote"><cms:contentTemplateText code="purl.recipient" key="PURL_EXPIRE" args="${purlExpireOn}"/></p>


                                        <!-- JAVA NOTE: populate href to redeem award -->
                                        <beacon:authorize ifNotGranted="LOGIN_AS">
                                            <c:if test="${displayRedeemLink}">
                                                <c:choose>
                                                    <c:when test="${isUserLoggedIn}">
                                                        <a href="<c:out value="${redeemAwardUrl}"/>" target="_blank" class="btn redeemAwardBtn btn-primary btn-fullmobile"><cms:contentText key="REDEEM" code="purl.recipient"/></a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="<c:out value="${redeemAwardUrl}"/>" class="btn redeemAwardBtn btn-primary btn-fullmobile"><cms:contentText key="REDEEM" code="purl.recipient"/></a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:if>
                                        </beacon:authorize>

                                        <c:if test="${displayThankyouLink}">
                                            <c:choose>
                                                <c:when test="${isUserLoggedIn}">
                                                    <beacon:authorize ifNotGranted="LOGIN_AS">
                                                      <button class="btn thankEveryoneBtn btn-inverse btn-fullmobile"><cms:contentText key="THANK_EVERYONE" code="purl.recipient"/></button>
                                                    </beacon:authorize>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<c:out value="${thankyouModalForwardUrl}"/>" class="btn btn-primary btn-fullmobile"><cms:contentText key="THANK_EVERYONE" code="purl.recipient"/></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>


                                    </div><!-- /.purlDetailsRecipientControls -->

                                    <div class="purlDetailsViewerControls" style="display:none"></div>

                                    <c:if test="${isRecipient && ( not empty chatterUrl || not empty facebookUrl || not empty linkedInUrl || not empty twitterUrl )}">
                                        <ul class="share-print fr">
                                            <li><cms:contentText code="purl.common" key="SHARE_TO_FACEBOOK"/></li>
                                            <!-- JAVA NOTE: for each share link -->

                                            <c:if test="${not empty chatterUrl}">
                                            <li>
                                                <a target="_blank"
                                                    class="social-icon chatter icon-size-16"
                                                    href="${chatterUrl}"></a>
                                            </li>
                                            </c:if>

                                            <c:if test="${not empty facebookUrl}">
                                            <li>
                                                <a target="_blank"
                                                    class="social-icon facebook icon-size-16"
                                                    href="${facebookUrl}"></a>
                                            </li>
                                            </c:if>

                                           <c:if test="${not empty linkedInUrl}">
                                              <li>
                                                <a target="_blank"
                                                    class="social-icon linkedin icon-size-16"
                                                    href="${linkedInUrl}"></a>
                                              </li>
                                            </c:if>

                                            <c:if test="${not empty twitterUrl}">
                                              <li>
                                                <a target="_blank"
                                                    class="social-icon twitter icon-size-16"
                                                    href="${twitterUrl}"></a>
                                              </li>
                                            </c:if>
                                        </ul>
                                    </c:if>
                                </c:if>
                            </div><!-- /.purlTextWrapper -->

                            <div class="purlVideoWrapper span4">
                                <!-- JAVA NOTE: output proper video urls for each format -->
                                <c:if test="${mediaType == 'video'}">
                                <video id="purlDetailVideo" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                                    <source type="video/mp4" src="${baseURI}/videos/purl/${mediaName}/${mediaName}.mp4">
                                    <source type="video/webm" src="${baseURI}/videos/purl/${mediaName}/${mediaName}.webm">
                                    <source type="video/ogg" src="${baseURI}/videos/purl/${mediaName}/${mediaName}.ogg">
                                </video>
                                </c:if>
                                <c:if test="${mediaType == 'picture'}">
                                <img src="${baseURI}/videos/purl/${mediaName}/${mediaName}.jpg" alt="foo" class="purlMainImage" />
                                </c:if>
                            </div><!-- /.purlVideoWrapper -->

                        </div><!-- /.purlDetailsWrapper -->
                    </div><!-- /.recipientSection -->
                </div><!-- /.purlDetailsOuterWrapper -->

                <div class="contribCommentWrapper contributeToPurlSection">
                    <div class="mask" style="display:none"></div>

                    <!-- JAVA NOTE:  The image is getting corrupted, while processing in PurlImageRenderServlet, so handled in different logic -->
                    <span class="contribAvatar avatarwrap">
                        <!-- if profile photo -->
                         <c:if test="${ purlContributor.avatarUrl != null &&  purlContributor.avatarUrl != '' }">
                         <%
                            Long time = new Date().getTime(); 
                            String ts = "?ts=" + time.toString();;
                            pageContext.setAttribute("ts", ts);
			            %>

                            <img id="partProfAvatar" alt="<c:out value="${pax.firstName}" /> <c:out value="${pax.lastName}" />" src="<c:out value="${purlContributor.avatarUrl}"/><%=ts%>">
                        </c:if>
                        <c:if test="${ purlContributor.avatarUrl == null }">
                            <c:set var="fcName" value="${purlContributor.firstName.substring(0,1)}"/>
                            <c:set var="lcName" value="${purlContributor.lastName.substring(0,1)}"/>
                            <span class="avatar-initials">${fcName}${lcName}</span>
                        </c:if>
                    </span>

                    <div class="indent">
                        <div class="indent-row">
                            <div class="commentWrapper">
                                <div class="contribName">
                                    <span class="contributor_firstName">&nbsp;</span>
                                    <span class="contributor_lastName">&nbsp;</span>
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
                                    <%--<textarea class="contribCommentInp" rows="4" maxlength="1000" placeholder="<cms:contentText key="COMMENTS_HEADER" code="purl.contributor"/>"></textarea>--%>
                                    <%--
                                        Commented out textarea with CMS key as placeholder and instead using textarea with hard-coded placeholder below.
                                        This is because this placeholder text needs to specifically instruct the user with this particular language.
                                    --%>
                                    <textarea class="contribCommentInp" rows="4" maxlength="<c:out value="${beacon:systemVarInteger('purl.comment.size.limit')}"/>" placeholder=<cms:contentText key="ADD_COMMENT_OR_PASTE_VIDEO" code="purl.contributor"/>></textarea>

                                    <div class="contribCommentBadWords"></div>

                                    <div class="uploadWrapper">
                                        <input type="file" name="purlUploadFiles" class="uploadInput ready"/>
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
                                    </div><!-- /.uploadWrapper -->

                                </div><!-- /.commentFauxInput -->
                            </div><!-- /.commentWrapper -->
                        </div>

                        <div class="contribBottomControls">
                            <button class="btn btn-primary btn-fullmobile submitContribCommentBtn">
                                <cms:contentText key="SUBMIT" code="system.button"/>
                            </button>
                            <button class="btn btn-primary btn-inverse btn-fullmobile inviteContributorsBtn"><cms:contentText key="INVITE_OTHERS" code="purl.contributor"/></button>
                        </div>

                    </div><!-- /.indent -->
                </div><!-- /.contribCommentWrapper -->
            </div><!-- /.page-topper -->
        </div><!-- /.span12 -->
    </div><!-- /.purlDetailsSection -->

    <div class="purlCommentsSection">
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
    </div><!-- /.purlCommentsSection -->


    <script id="commentItemTpl" type="text/x-handlebars-template">
        {{#if _isEmpty}}
        <div class="row-fluid commentItemWrapper isEmptyMessage">
            <div class="span12">
                <div class="alert alert-info"><cms:contentText key="NO_CONTRIBUTION_MSG" code="purl.contributor"/></div>
            </div>
        </div>
        {{else}}
        <div class="row-fluid commentItemWrapper activity{{activityId}}" data-id="{{activityId}}">
            <div class="span12 commentItem">
                <div class="innerCommentWrapper">
                    <!-- need to update json to have first name and last name -->
                    <span class="avatarwrap avatarImg">
                        {{#if userInfo.0.profilePhoto}}
                            <img src="{{#timeStamp userInfo.0.profilePhoto}}{{/timeStamp}}"  />
                        {{else}}
                        <div class="avatar-initials">{{trimString userInfo.0.firstName 0 1}}{{trimString userInfo.0.lastName 0 1}}</div>
                        {{/if}}
                    </span>
                    <div class="indent">
                        <div class="userInfo" data-id="{{userInfo.0.contributorID}}">
                            {{userInfo.0.userName}}
                        </div>
                        {{#if commentText}}
                        <div class="text">
                            {{commentText}}
                        </div>
                        {{/if}}
						<c:if test="${isUserLoggedIn}">
	 					<c:if test="${isOtherlocale}">
						<div class="translateTextLink"><a href="${translateClientState}" data-id="{{activityId}}"><cms:contentText key="TRANSLATE" code="purl.contributor"/></a></div>
						</c:if>
                        </c:if>
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
                                        <video id="purlContribVideo{{activityId}}" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                                            {{#each media.0.video}}
                                                <source src="{{src}}" type="video/{{fileType}}">
                                            {{/each}}
                                        </video>
                                    </div>
                                </div>
                                {{/if}}

                                {{#if media.0.photo.0.src}}
                                <img src="{{media.0.photo.0.src}}">
                                {{/if}}
                            {{/if}}
                        </div>
                        {{/or}}
                    </div><!-- /.indent -->
                </div><!-- /.innerCommentWrapper -->
            </div>
        </div>
        {{/if}}
    </script>







    <!-- ============= -->
    <!-- initial modal -->
    <!-- ============= -->
    <c:if test="${displayContributorSwitchModal}" >
    <div id="purlInitialModal" class="modal hide fade" data-backdrop="static">
        <div class="modal-header">
            <h3><cms:contentTemplateText code="purl.contributor" key="WELCOME_TITLE" args="${targetPurlRecipient.user.firstName}, ${targetPurlRecipient.user.lastName}${targetPurlRecipient.user.possessiveSuffix}" delimiter=","/></h3>
        </div>
        <div class="modal-body form-horizontal">
            <p><cms:contentTemplateText code="purl.contributor" key="INSTRUCTION" args="${targetPurlRecipient.user.firstName}, ${targetPurlRecipient.user.lastName}, ${formElement1},${formElement2},${formElement3},${targetPurlRecipient.promotion.name}" delimiter=","/></p>

            <div class="control-group validateme"
                data-validate-flags="nonempty"
                data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="FIRST_NAME_FAIL_MSG" code="purl.contributor"/>"}'>
                <label for="modalFirstName" class="control-label"><cms:contentText key="INVITEE_FIRST_NAME" code="purl.contributor"/></label>
                <div class="controls">
                    <!-- dynamic value below (js populated) -->
                    <input type="text" id="modalFirstName" name="modalFirstName" class="contribInp" data-key="firstName">
                </div>
            </div>

            <div class="control-group validateme"
                data-validate-flags="nonempty"
                data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="LAST_NAME_FAIL_MSG" code="purl.contributor"/>"}'>
                <label for="modalLastName" class="control-label"><cms:contentText key="INVITEE_LAST_NAME" code="purl.contributor"/></label>
                <div class="controls">
                    <!-- dynamic value below (js populated) -->
                    <input type="text" id="modalLastName" name="modalLastName" class="contribInp" data-key="lastName">
                </div>
            </div>

            <div class="control-group validateme"
                data-validate-flags="nonempty,email"
                data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="EMAIL_ADDRESS_FAIL_MSG" code="purl.contributor"/>", "email" : "<cms:contentText key="EMAIL_ADDRESS_INVALID_MSG" code="purl.contributor"/>"}'>
                <label for="modalEmailAddress" class="control-label"><cms:contentText key="INVITEE_EMAIL_ADDRESS" code="purl.contributor"/></label>
                <div class="controls">
                    <!-- dynamic value below (js populated) -->
                    <input type="text" id="modalEmailAddress" name="modalEmailAddress" class="contribInp" data-key="email">
                </div>
            </div>

            <div class="control-group" id="modalProfilePhotoContainer">
                <label for="modalProfilePhoto" class="control-label"><cms:contentText key="YOUR_PHOTO" code="purl.contributor"/></label>
                <div class="controls">

                    <div class="avatarUploadWrapper">
                        <div class="imgWrap">
                            <span class="avatarwrap">
                            <!-- dyn -->
                            </span>
                        </div>
                        <input type="file" id="modalAvatarInput" name="modalAvatarInput" class="imageFileInput uploaderFileInput">
                        <span class="upSpin" style="display:none">&nbsp;</span>
                        <p class="muted"><cms:contentTemplateText key="UPLOAD_INSTRUCTIONS" code="profile.personal.info" args="${beacon:systemVarInteger('system.image.upload.size.limit')}" /></p>
                    </div>

                </div>
            </div>

            <c:set var="purlExpireOn"><fmt:formatDate value="${targetPurlRecipient.closeDate}" pattern="${JstlDatePattern}" /></c:set>
            <p><em><cms:contentTemplateText code="purl.contributor" key="PURL_EXPIRE" args="${purlExpireOn}"/></em></p>

        </div>
        <div class="modal-footer tl">
            <a class="btn btn-primary submitContributorBtn">
                <cms:contentText key="CONTRIBUTE_BTN" code="purl.contributor"/>
            </a>
        </div>
    </div><!-- /#purlInitialModal -->
    </c:if>




    <!-- ============= -->
    <!-- invite qtip   -->
    <!-- ============= -->
    <div id="inviteContribsTip" style="display:none">
        <a href="#" class="close closeInviteContribsTip fr"><i class="icon-close"></i></a>

        <div class="inviteMode">
            <h4><cms:contentText key="INVITE_CONTRIBUTORS" code="purl.contributor"/></h4>
            <p>
                <cms:contentText key="INVITE_INSTRUCTION" code="purl.contributor"/>
            </p>

            <textarea class="inviteContribsEmailsInp" spellcheck="false"
                placeholder="<cms:contentText key="EXAMPLE_EMAIL" code="recognitionSubmit.contributors" />"></textarea>

            <div class="parseEmailsControls">
                <button type="button" class="btn btn-primary btn-small addEmailsToInviteListBtn">
                    <i class="icon-plus-circle"></i> <cms:contentText key="ADD" code="system.button"/>
                </button>

                <!-- email parse error messaging -->
                <div class="alert alert-danger parseEmailsFeedback" style="display:none">
                    <div class="msg errorEmailsFound">
                        <b><cms:contentText key="ERRORS_WITH_FOLLOWING_EMAILS" code="purl.contributor"/></b><br>
                        <div class="errorEmailsList">
                            <!-- dyn -->
                        </div>
                    </div>
                    	<!-- Client customziations for wip #26532 starts-->
					<div class="msg acceptedDomain">
						<b><cms:contentText key="NOT_ALLOWED_EMAILS" code="client.recognitionSubmit"/></b><br>
						<div class="errorEmailsList">
							<!-- dyn -->
						</div>
					</div>
					<!-- Client customziations for wip #26532 ends-->
                    <div class="msg noEmailsFound">
                        <cms:contentText key="NO_EMAIL_ADDRESSES_FOUND" code="purl.contributor"/>
                    </div>
                </div>
            </div>

            <div class="pendingContribsWrapper">
                <!-- dyn -->
            </div>

            <div class="pendingContribsControls" style="display:none">
                <button class="btn btn-primary btn-small inviteContribsBtn"><cms:contentText key="INVITE" code="purl.contributor"/></button>
                <button class="btn btn-small cancelInviteContribsBtn"><cms:contentText key="CANCEL" code="system.button"/></button>
            </div>
        </div><!-- /.inviteMode -->

        <div class="inviteResultsMode" style="display:none">
            <!-- dyn -->
        </div>

        <div class="addedContribsWrapper">
            <!-- dyn -->
        </div>

    </div><!-- /#inviteContribsTip -->

    <script id="pendingContribsTpl" type="text/x-handlebars-template">
        {{#pendingInvitees}}
        <div class="pendingContribItem">
            <span class="remBtn btn btn-mini btn-icon btn-danger" data-email="{{email}}"><i class="icon-trash"></i></span>
            <span class="emailAndNameWrapper">
                <span class="email">{{email}}</span>
                {{#if firstName}}
                    <span class="name">
                        (<span class="first">{{firstName}}</span>
                        {{#if lastName}} <span class="last">{{lastName}}{{/if}}</span>)
                    </span>
                {{/if}}
            </span>
        </div>
        {{/pendingInvitees}}
    </script><!-- /#pendingContribsTpl -->

    <script id="addedContribsTpl" type="text/x-handlebars-template">
        {{#if namedInvitees}}
        <div class="addedContribsTitle">
			<cms:contentText key="INVITE_EXISTS" code="purl.contributor"/>:
        </div>
        <p class="addedContribs">
            {{#namedInvitees}}
            <span class="addedInviteeItem label label-inverse">{{fNameLInitial}}</span>
            {{/namedInvitees}}
        </p>
        {{/if}}
        {{#if namelessInvitees.length}}
        {{! NOTE: different formulation of the message below to avoid multiple sentences involving plurals etc.
            wireframe uses a diff. pattern that will create great headaches for such a simple little thing
            do not acquiesce! oh brave programmer. }}
        <cms:contentText key="PEOPLE_NOT_PROVIDED" code="purl.contributor"/>: <b>{{namelessInvitees.length}}</b>
        {{/if}}
    </script><!-- /#addedContribsTpl -->

    <script id="inviteResultsTpl" type="text/x-handlebars-template">
        <h4><cms:contentText key="INVITES_SENT" code="purl.contributor"/></h4>
        {{#invitees}}
            <div class="{{status}}Status inviteeResultItem">
            {{#eq status "success"}}<i class="icon-check-circle goodRes"></i>{{/eq}}
            {{#eq status "exists"}}<i class="icon-check-circle neutralRes"></i>{{/eq}}
            {{#eq status "fail"}}<i class="icon-cancel-circle badRes"></i>{{/eq}}
            {{emailAddress}}
            {{#if firstName}}
                ({{firstName}} {{lastName}})
            {{/if}}
            </div>
        {{/invitees}}
        <br>
        <button class="btn btn-small inviteeResultsCloseBtn"><cms:contentText key="CLOSE" code="system.button"/></button>
        &nbsp;&nbsp;&nbsp;
        <i class="icon-check-circle goodRes"></i> <cms:contentText key="ADDED" code="purl.contributor"/> &nbsp;&nbsp;
        <i class="icon-check-circle neutralRes"></i> <cms:contentText key="INVITE_EXISTS" code="purl.contributor"/> &nbsp;&nbsp;
        <i class="icon-cancel-circle badRes"></i> <cms:contentText key="INVITE_FAILED" code="purl.contributor"/>
    </script><!-- /#inviteResultsTpl -->





    <!-- ==================== -->
    <!-- thank everyone modal -->
    <!-- ==================== -->
    <c:choose>
	    <c:when test="${openThankYouModal}">
		    <div id="purlThankEveryoneModal" class="modal hide fade autoModal">
		        <div class="modal-header">
		            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
		            <h3><cms:contentText key="THANK_EVERYONE" code="purl.recipient"/></h3>
		        </div>
		        <div class="modal-body">
		            <div class="thankyouData">
		                <p><cms:contentTemplateText code="purl.recipient" key="THANK_EVERYONE_COMMENTS_STATIC" args="${targetPurlRecipient.user.firstName}, ${targetPurlRecipient.user.lastName}, ${targetPurlRecipient.promotion.name}" delimiter=","/></p>
		                <textarea class="thankyouTextInp input-block-level" rows="5" maxlength="<c:out value="${beacon:systemVarInteger('purl.comment.size.limit')}"/>"></textarea>
		            </div>
		            <div class="spinner" style="display:none">&nbsp;</div>
		            <div class="thankyouResults alert" style="display:none">
		                <b><cms:contentText key="THANKYOU_CONFIRM_HEADER" code="purl.recipient"/></b>
		            </div>
		        </div>
		        <div class="modal-footer">
		            <a class="btn btn-primary submitThankEveryoneBtn btn-fullmobile"><cms:contentText key="SUBMIT" code="system.button"/></a>
		            <button type="button" class="btn closeThankEveryoneModalBtn btn-fullmobile" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button"/></button>
		      </div>
		    </div><!-- /#purlThankEveryoneModal -->
	    </c:when>
	    <c:otherwise>
		    <div id="purlThankEveryoneModal" class="modal hide fade">
		        <div class="modal-header">
		            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
		            <h3><cms:contentText key="THANK_EVERYONE" code="purl.recipient"/></h3>
		        </div>
		        <div class="modal-body">
		            <div class="thankyouData">
		                <p><cms:contentTemplateText code="purl.recipient" key="THANK_EVERYONE_COMMENTS_STATIC" args="${targetPurlRecipient.user.firstName}, ${targetPurlRecipient.user.lastName}, ${targetPurlRecipient.promotion.name}" delimiter=","/></p>
		                <textarea class="thankyouTextInp input-block-level" rows="5" maxlength="<c:out value="${beacon:systemVarInteger('purl.comment.size.limit')}"/>"></textarea>
		            </div>
		            <div class="spinner" style="display:none">&nbsp;</div>
		            <div class="thankyouResults alert" style="display:none">
		                <b><cms:contentText key="THANKYOU_CONFIRM_HEADER" code="purl.recipient"/></b>
		            </div>
		        </div>
		        <div class="modal-footer">
		            <a class="btn btn-primary submitThankEveryoneBtn btn-fullmobile"><cms:contentText key="SUBMIT" code="system.button"/></a>
		            <button type="button" class="btn closeThankEveryoneModalBtn btn-fullmobile" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button"/></button>
		      </div>
		    </div><!-- /#purlThankEveryoneModal -->
	    </c:otherwise>
    </c:choose>

</div><!-- /#purlPageView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function(){
    <c:if test="<%=!UserManager.isUserLoggedIn()%>">
    G5.props.globalHeader = {
            clientLogo : '',
            programLogo : ''
    }
    </c:if>

    _V_.options.flash.swf = G5.props.URL_ROOT + 'assets/rsrc/video-js.swf';
    G5.props.URL_JSON_PURL_ACTIVITY_FEED = G5.props.URL_ROOT+'purl/purlRecipientLoadActivity.do?method=loadActivityFeed';
    G5.props.URL_JSON_PURL_POST_COMMENT = G5.props.URL_ROOT+'purl/purlContribution.do?method=postComment';
    G5.props.URL_JSON_PURL_UPLOAD_PHOTO = G5.props.URL_ROOT+'purl/purlContribution.do?method=processPhoto';
    G5.props.URL_JSON_PURL_INVITE_CONTRIBUTORS_EMAILED = G5.props.URL_ROOT+'purl/purlContribution.do?method=updateContributorPersonalInfo';
    G5.props.URL_JSON_PURL_UPLOAD_MODAL_PROFILE_PICTURE = G5.props.URL_ROOT+'purl/purlContribution.do?method=processAvatar';
    G5.props.URL_JSON_PURL_INVITE_CONTRIBUTORS = G5.props.URL_ROOT+'purl/purlContribution.do?method=sendInvite';
    G5.props.URL_JSON_PURL_SEND_THANK_YOU = G5.props.URL_ROOT+'purl/purlRecipientSubmit.do?method=sendThankyou';
    G5.props.URL_JSON_PURL_TRANSLATE_COMMENT = G5.props.URL_ROOT + "purl/translate.do";

    var json;

    // bootstrap json
    json = {
        <c:if test="${isContributor}">
            <c:if test="${displayContributorSwitchModal}" >
               // show welcome modal to collect info?
               collectContributorInformation: true,
               // show contribute section (if true, isRecipient should be false)
               allowContribution: true,
               // is this the recipient (if true, allow contribution should be false)
               isRecipient: false,
            </c:if>
            <c:if test="${!displayContributorSwitchModal}" >
               // show welcome modal to collect info?
               collectContributorInformation: false,
               // show contribute section (if true, isRecipient should be false)
               allowContribution: true,
               // is this the recipient (if true, allow contribution should be false)
               isRecipient: false,
             </c:if>
        </c:if>

        <c:choose>
        <c:when test="${isRecipient}">
        // show welcome modal to collect info?
        collectContributorInformation: false,
        // show contribute section (if true, isRecipient should be false)
        allowContribution: false,
        // is this the recipient (if true, allow contribution should be false)
        isRecipient: true,
        <c:choose>
        <c:when test="${process}">
        awardProcessed: true,
        </c:when>
        <c:otherwise>
        awardProcessed: false,
        </c:otherwise>
        </c:choose>
        </c:when>
        </c:choose>
        // useful image URL helpers
        stagerPrefixURL : '${stagerPrefixURL}',
        finalPrefixURL : '${finalPrefixURL}',
        // contributor information
        // 1) from a logged in G5 user
        // 2) from associated PURL link user email
        contributor: {
            id: "<c:out value="${purlContributor.id}"/>",
            firstName: "${purlContributor.firstName}",
            lastName: "${purlContributor.lastName}",
			<c:if test="${empty purlContributor.avatarUrl}">
        		avatarUrl: null,
        	</c:if>
        	<c:if test="${not empty purlContributor.avatarUrl}">
           		avatarUrl: "<c:out value="${purlContributor.avatarUrl}"/>",
        	</c:if>
            email: "<c:out value="${purlContributor.emailAddr}"/>",
        },
        recipient: {
            id: "<c:out value="${targetPurlRecipient.id}"/>",
        },

        // idividuals previously invited (ID SHOULD BE THE SAME AS EMAIL - or already invited list will get funky)
        invitees: [<c:forEach var="invitee" items="${invitees}" varStatus="bt">
        {
            "lastName":"<c:out value="${invitee.lastName}"/>",
            "firstName":"<c:out value="${invitee.firstName}"/>",
            "email":"<c:out value="${invitee.email}"/>",
            "id":"<c:out value="${invitee.id}"/>"
        }
        <c:if test="${(bt.index+1) < fn:length(invitees)}">, </c:if>
        </c:forEach>],
     // Client customizationfor wip #26532 starts
		purlAllowOutsideDomains: [${allowedDomains}]
        // Client customizationfor wip #26532 starts
    };


    //attach the view to an existing DOM element
    ppv = G5.views.purlPageView = new PurlPageView({
        el : $('#purlPageView'),
        userRole : 'manager', // 'manager' or 'participant'
        loggedIn : <%=UserManager.isUserLoggedIn()%>,
        <c:if test="<%=!UserManager.isUserLoggedIn()%>">
        	pageNav : {},
        </c:if>
        <c:if test="<%=UserManager.isUserLoggedIn()%>">
        	pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    url : 'javascript:history.go(-1);'
                },
            	home : {
                	text : '<cms:contentText key="HOME" code="system.general" />',
                	url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
            	}
        	},
        </c:if>
        pageTitle :'<cms:contentText key="RECOGNITION_PURL_TITLE" code="purl.common"/>',

        json: json
    });

});
</script>
