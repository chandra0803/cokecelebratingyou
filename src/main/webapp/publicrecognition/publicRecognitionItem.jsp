<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ImageUtils"%>


{{#if isDetail}}

<!-- DETAILS FOR DETAIL PAGE -->

<div class="row-fluid">
    <div class="span8">
        <h2>{{promotionName}}</h2>
    </div>

    <div class="span4">
        <ul class="share-print">
            {{#if isMine}}
                {{#if shareLinks}}
                <li><cms:contentText key="SHARE" code="recognition.public.recognition.item" /></li>
                {{#shareLinks}}
                    <li>
                        <a target="_blank"
                            class="social-icon {{this.nameId}} icon-size-16"
                            href="{{this.url}}"></a>
                    </li>
                {{/shareLinks}}
                {{/if}}
            {{/if}}
            <li class="print-wrap">
                <a href="#" class="pageView_print btn btn-small printBtn">

                    <cms:contentText key="PRINT" code="recognition.public.recognition.item" />
                    <i class="icon-printer"></i>
                </a>
            </li>
        </ul>
    </div>
</div>

    {{#if isCumulative}}
        <div class="row-fluid pubRecDetails">
            <div class="span12">

                <div class="row-fluid section-container {{#if recognizer.[0].eCard}}span8{{else}}span12{{/if}}">
                    <div class="span3">
                        <p><strong><cms:contentText key="TO_NOCOLON" code="recognition.public.recognition.item" /></strong></p>
                    </div>

                    <div class="recipientsContainer span9">
                        {{#if teamName}}
                            <div class="teamName">
                                <p>{{teamName}}</p>
                            </div>
                        {{/if}}

                        {{#recipients}}
                        <div class="recipientWrap">
                            <div class="avatarWrap avatarwrap recipientAvatar">

                                <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                    {{#if this.avatarUrl}}
                                    <img alt="" src="{{#timeStamp this.avatarUrl}}{{/timeStamp}}" />
                                    {{else}}
                                    <span class="avatar-initials" data-name="{{firstName}} {{lastName}}">{{trimString this.firstName 0 1}}{{trimString this.lastName 0 1}}</span>
                                    {{/if}}
                                </a>
                            </div>

                            <div class="recipientInfoWrap">
                                <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                {{this.lastName}}, {{this.firstName}}
                                </a>

                                <span class="paxMeta">
                                    {{#if this.orgName}}
                                    {{this.orgName}}
                                    {{/if}}

                                    {{#if this.department}}
                                    | {{this.department}}
                                    {{/if}}

                                    {{#if this.title}}
                                    | {{this.title}}
                                    {{/if}}
                                </span>

                                {{#if this.viewCertificateUrl}}
                                <a href="{{this.viewCertificateUrl}}" target="_blank"><cms:contentText key="CERTIFICATE_LINK" code="recognition.public.recognition.item" /></a>
                                <!--<a href="#" class="certificateUrl" data-recipientId="{{this.id}}"><cms:contentText key="CERTIFICATE_LINK" code="recognition.public.recognition.item" /></a>-->
                                {{/if}}
                            </div>
                        </div>
                        {{/recipients}}

                        {{#if recipientIdsHidden.length}}
                        <p class="showHiddenWrap">
                            <cms:contentText key="AND" code="recognition.public.recognition.item" />
                            <a class="showHidden" href="#">
                                <span class="likeCount">{{recipientIdsHidden.length}}</span> <cms:contentText code="recognition.public.recognition.item" key="OTHERS" />
                            </a>
                        </p>

                        <div class="hiddenRecipients"></div>
                        {{/if}}

                    </div>
                </div>

                {{#if awardAmount}}
                <div class="row-fluid section-container">
                    <div class="span3">
                        <strong><cms:contentText key="AWARD_NOCOLON" code="recognition.public.recognition.item" /></strong>
                    </div>
                    <div class="span9">
                        <p>{{awardAmount}}</p>
                    </div>
                </div>
                {{/if}}

                {{#ueq promotionType "nomination"}}
                {{! if additional types don't have copies, nest #ueq here}}
                <div class="row-fluid section-container">
                    <div class="span3">
                        <strong><cms:contentText key="COPIES_NOCOLON" code="recognition.public.recognition.item" /></strong>
                    </div>
                {{#if copies}}
                    <div class="span9">
                        {{#copies}}
                            <p>{{this}}</p>
                        {{/copies}}
                    </div>
                    {{else}}
                    <div class="span9">
                    <cms:contentText key="NONE_SELECTED" code="recognition.public.recognition.item" />
                    </div>
                    {{/if}}
                </div>
                {{/ueq}}

                {{#each recognizer}}
                <div class="row-fluid section-container cumulativeRecognizerWrap">
                    <div class="{{#if eCard}}span8{{else}}span12{{/if}}">
                        <div class="row-fluid">
                            <div class="span3">
                                <strong><cms:contentText key="FROM_NOCOLON" code="recognition.public.recognition.item" /></strong>
                            </div>

                            <div class="span9">
                                <div class="recipientWrap">
                                    <div class="avatarWrap">

                                        <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                            {{#if this.avatarUrl}}
                                            <img alt="" src="{{#timeStamp this.avatarUrl}}{{/timeStamp}}" class="avatar recipientAvatar" height="60" width="60" />
                                            {{else}}
                                            <span class="avatar recipientAvatar" data-name="{{firstName}} {{lastName}}">{{trimString this.firstName 0 1}}{{trimString this.lastName 0 1}}</span>
                                            {{/if}}
                                        </a>
                                    </div>

                                    <div class="recipientInfoWrap">
                                        <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                        {{this.lastName}}, {{this.firstName}}
                                        </a>

                                        <span class="paxMeta">
                                            {{#if this.orgName}}
                                            {{this.orgName}}
                                            {{/if}}

                                            {{#if this.department}}
                                            | {{this.department}}
                                            {{/if}}

                                            {{#if this.title}}
                                            | {{this.title}}
                                            {{/if}}
                                        </span>
                                    </div>
                                </div>
                            </div>

                            {{#if date}}
                            <div class="row-fluid">
                                <div class="span3">
                                    <strong><cms:contentText key="DATE_NOCOLON" code="recognition.public.recognition.item" /></strong>
                                </div>
                                <div class="span9">
                                    <p>{{date}}</p>
                                </div>
                            </div>
                            {{/if}}

                            {{#if comment}}
                            <div class="row-fluid">
                                <div class="span3">
                                    <strong><cms:contentText key="COMMENTS_NOCOLON" code="recognition.public.recognition.item" /></strong>
                                </div>
                                <div class="span9 detail-comment">
                                    <!-- do no wrap comment in P tag, it will have its own --> <!-- not getting wrapped in P tags, so adding -->
                                    <p>{{{comment}}}</p>
                                </div>
                            </div>
                            {{/if}}

                            {{#if behavior}}
                            <div class="row-fluid">
                                <div class="span3">
                                    <strong><cms:contentText key="BEHAVIOR_NOCOLON" code="recognition.public.recognition.item" /></strong>
                                </div>
                                <div class="span9">
                                    <p><!--<img class="behaviorBadge" src="{{badgeUrl}}" alt=""/>-->{{behavior}}</p>
                                </div>
                            </div>
                            {{/if}}

                            {{#extraFields}}
                            <div class="row-fluid">
                                <div class="span3">
                                    <strong>{{this.name}}</strong>
                                </div>
                                <div class="span9">
                                    <p>{{{this.value}}}</p>
                                </div>
                            </div>
                            {{/extraFields}}
                        </div>
                    </div><!--/.span8-->

                    {{#if eCard}}
                        <div class="span4 eCard">
                            {{#eq eCard.type "image"}}
                                <img alt="{{eCard.name}}" src="{{eCard.imgUrl}}">
                            {{/eq}}
                            {{#eq eCard.type "video"}}
                            <div class="recognitionVideoWrapper">
                                    <video id="recognitionVideo" class="ecard video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" >
                                    <source src="{{eCard.videoSrc}}" type="{{eCard.videoFileType}}">
                                </video>
                            </div>
                            {{/eq}}
                        </div>
                    {{/if}}
                </div>
                {{/each}}

            </div><!--/.span12-->
        </div><!--/.row-fluid-->

        {{else}}

        <div class='row-fluid pubRecDetails'>
            <div class="span12">
                <div class="row-fluid section-container {{#if recognizer.[0].eCard}}span8{{else}}span12{{/if}}">
                    <div class="span12">
                        <div class="row-fluid">
                            <div class="span3">
                                <p><strong><cms:contentText key="TO_NOCOLON" code="recognition.public.recognition.item" /></strong></p>
                            </div>

                            <div class="span9 recipientsContainer">
                                {{#if teamName}}
                                    <div class="teamName">
                                        <p>{{teamName}}</p>
                                    </div>
                                {{/if}}

                                {{#recipients}}
                                <div class="recipientWrap">
                                    <div class="avatarWrap avatarwrap recipientAvatar">
                                        <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                            {{#if this.avatarUrl}}
                                            <img alt="" src="{{#timeStamp this.avatarUrl}}{{/timeStamp}}" />
                                            {{else}}
                                            <span class="avatar-initials" data-name="{{firstName}} {{lastName}}">{{trimString this.firstName 0 1}}{{trimString this.lastName 0 1}}</span>
                                            {{/if}}
                                        </a>
                                    </div>

                                    <div class="recipientInfoWrap">
                                        <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                        {{this.lastName}}, {{this.firstName}}
                                        </a>

                                        <span class="paxMeta">
                                            {{#if this.orgName}}
                                            {{this.orgName}}
                                            {{/if}}

                                            {{#if this.department}}
                                            | {{this.department}}
                                            {{/if}}

                                            {{#if this.title}}
                                            | {{this.title}}
                                            {{/if}}
                                        </span>

                                        {{#if this.viewCertificateUrl}}
                                        <a href="{{this.viewCertificateUrl}}" target="_blank"><cms:contentText key="CERTIFICATE_LINK" code="recognition.public.recognition.item" /></a>
                                        <!--<a href="#" class="certificateUrl" data-recipientId="{{this.id}}"><cms:contentText key="CERTIFICATE_LINK" code="recognition.public.recognition.item" /></a>-->
                                        {{/if}}
                                    </div>

                                </div>
                                {{/recipients}}

                                {{#if recipientIdsHidden.length}}
                                <p class="showHiddenWrap">
                                    <cms:contentText key="AND" code="recognition.public.recognition.item" />
                                    <a class="showHidden" href="#">
                                        <span class="likeCount">{{recipientIdsHidden.length}}</span> <cms:contentText key="OTHERS" code="recognition.public.recognition.item" />
                                    </a>
                                </p>

                                <div class="hiddenRecipients"></div>
                                {{/if}}
                            </div>
                        </div>
                    </div>
                </div>
                    {{#recognizerShown}}
                        {{#if this.eCard}}

                                {{#eq this.eCard.type "image"}}
                                <div class="span4 ecard-span">
                                    <img alt="{{this.eCard.name}}" src="{{this.eCard.imgUrl}}">
                                </div>
                                {{/eq}}
                                {{#eq this.eCard.type "video"}}
                                <div class="span4 recognitionVideoWrapper">
                                        <video id="recognitionVideo" class="span5 ecard-span video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                                        <source src="{{this.eCard.videoSrc}}" type="{{this.eCard.videoFileType}}">
                                    </video>
                                </div>
                                {{/eq}}

                        {{/if}}
                    {{/recognizerShown}}


                {{#recognizerShown}}

                <div class="row-fluid section-container recognizerCotainer {{#if ../recognizer.[0].eCard}}span8{{else}}span12{{/if}}">
                    <div class="span3">
                        <strong><cms:contentText key="FROM_NOCOLON" code="recognition.public.recognition.item" /></strong>
                    </div>
                    <div class="span9">
                        <div class="recipientWrap">
                            <div class="avatarWrap avatarwrap recipientAvatar">

                                <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                    {{#if this.avatarUrl}}
                                    <img alt="" src="{{#timeStamp this.avatarUrl}}{{/timeStamp}}" />
                                    {{else}}
                                    <span class="avatar-initials" data-name="{{firstName}} {{lastName}}">{{trimString this.firstName 0 1}}{{trimString this.lastName 0 1}}</span>
                                    {{/if}}
                                </a>
                            </div>

                            <div class="recipientInfoWrap">
                                <a class="profile-popover" data-participant-ids="[{{id}}]" href="#">
                                {{this.lastName}}, {{this.firstName}}
                                </a>

                                <span class="paxMeta">
                                    {{#if this.orgName}}
                                    {{this.orgName}}
                                    {{/if}}

                                    {{#if this.department}}
                                    | {{this.department}}
                                    {{/if}}

                                    {{#if this.title}}
                                    | {{this.title}}
                                    {{/if}}
                                </span>
                            </div>

                        </div>
                    </div>
                </div>
                {{/recognizerShown}}

                <div class="row-fluid section-container {{#if recognizer.[0].eCard}}span8{{else}}span12{{/if}}">
                    {{#recognizerShown}}
                    <div class="row-fluid">
                        <div class="span3">
                            <strong><cms:contentText key="DATE_NOCOLON" code="recognition.public.recognition.item" /></strong>
                        </div>
                        <div class="span9">
                            <p>{{this.date}}</p>
                        </div>
                    </div>
                    {{/recognizerShown}}

                    {{#if awardAmount}}
                    <div class="row-fluid">
                        <div class="span3">
                            <strong><cms:contentText key="AWARD_NOCOLON" code="recognition.public.recognition.item" /></strong>
                        </div>
                        <div class="span9">
                            <p>{{awardAmount}}</p>
                        </div>
                    </div>
                    {{/if}}

                    {{#recognizerShown}}
                    {{#if behavior}}
                    <div class="row-fluid">
                        <div class="span3">
                            <strong><cms:contentText key="BEHAVIOR_NOCOLON" code="recognition.public.recognition.item" /></strong>
                        </div>
                        <div class="span9">
                            <p><!--<img class="behaviorBadge" src="{{this.badgeUrl}}" alt=""/> -->{{this.behavior}}</p>
                        </div>
                    </div>
                    {{/if}}
                    {{/recognizerShown}}


                    {{#ueq promotionType "nomination"}}
                    {{! if additional types don't have copies, nest #ueq here}}
                    <div class="row-fluid print-hide">
                        <div class="span3">
                            <strong><cms:contentText key="COPIES_NOCOLON" code="recognition.public.recognition.item" /></strong>
                        </div>
                    {{#if copies}}
                        <div class="span9">
                            {{#copies}}
                                <p>{{this}}</p>
                            {{/copies}}
                        </div>
                        {{else}}
                        <div class="span9">
                                <p><cms:contentText key="NONE_SELECTED" code="recognition.public.recognition.item" /></p>
                        </div>
                        {{/if}}
                    </div>
                    {{/ueq}}

                    {{#recognizerShown}}
                    {{#extraFields}}
                    <div class="row-fluid">
                        <div class="span3">
                            <strong>{{this.name}}</strong>
                        </div>
                        <div class="span9">
                            <p>{{{this.value}}}</p>
                        </div>
                    </div>
                    {{/extraFields}}
                    {{/recognizerShown}}

                    {{#recognizerShown}}
                    {{#if comment}}
                    <div class="row-fluid">
                        <div class="span3">
                            <strong><cms:contentText key="COMMENTS_NOCOLON" code="recognition.public.recognition.item" /></strong>
                        </div>
                        <div class="span9 detail-comment">
                            <!-- do no wrap comment in P tag, it will have its own -->
                            {{{this.comment}}}
                        </div>
                    </div>
                    {{/if}}
                    {{/recognizerShown}}
                </div>

                {{#if purlUrl}}
                <div class="row-fluid span8 purlBtnContainer">
                    <div>
                        <a class="btn btn-primary purlBtn" href="{{purlUrl}}"><cms:contentText key="VIEW_PURL" code="recognition.public.recognition.item" /></a>
                    </div>
                </div>
                {{/if}}

            </div><!-- /.span8 -->

        </div><!-- /.row-fluid -->
    {{/if}}

    {{#if isPublicClaim}}
        <div class="row-fluid">
            <div class="span12 recognition-container">

                    <ul class="recognition-props {{#if recognizer.[0].eCard}}span8{{else}}span12{{/if}}">

                        {{#if isHidden}}
                                    <li class="propLineTop"><span class="text-error"><cms:contentText key="HIDE_INFO" code="recognition.public.recognition.item" /></span></li>
                            {{else}}
                                {{#if isMine}}
                                    <li class="propLineTop"><a href="#" class="hidePublicRecognitionLnk"><cms:contentText key="HIDE" code="recognition.public.recognition.item" /></a>
                                        <span class="publicRecognitionHiddenLinkText text-error" style="display:none;">
											<cms:contentText key="HIDE_INFO" code="recognition.public.recognition.item" />
                                        </span>
                                    </li>
                                {{/if}}
                        {{/if}}
           			<beacon:authorize ifNotGranted="LOGIN_AS,BI_ADMIN">                        
                        <!-- !isHidden || isMine -->
                        {{#ifnotor isHidden isMine}}

                            {{#if allowTranslate}}
                                <li class="propLineTop"><a href="#" class="translatePubRec"><cms:contentText key="TRANSLATE" code="recognition.public.recognition.item" /></a></li>
                            {{/if}}

                            {{#unless isMine}}
                                {{#if budget}}
                                    <li class="propLineTop"><a href="#" class="showAddPointsFormBtn"><cms:contentText key="ADD_POINTS" code="recognition.public.recognition.item" /></a> </li>
                                {{/if}}
                            {{/unless}}
                            <li>
                                {{#if isLiked}}
                                    <a href="#" class="likePubRecBtn liked mylike" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i><cms:contentText key="UNLIKE" code="recognition.public.recognition.item" /></a>
                                    {{#gte numLikers 2}}
                                        <a class="profile-popover likeCountStatus" href="#" data-recognition-id="{{id}}" data-participant-info-type="likers"> <span class="badge">{{numLikers}}</span></a>
                                    {{else}}
                                        <span class="likeCountStatus" ><span class="badge">{{numLikers}}</span></span>
                                    {{/gte}}
                                {{else}}
                                    <a href="#" class="likePubRecBtn" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i><cms:contentText key="LIKE" code="recognition.public.recognition.item" /></a>
                                    {{#gte numLikers 1}}
                                        <a class="profile-popover likeCountStatus" href="#" data-recognition-id="{{id}}" data-participant-info-type="likers"> <span class="badge">{{numLikers}}</span></a>
                                    {{else}}
                                        <span class="likeCountStatus" ><span class="badge">{{numLikers}}</span></span>
                                    {{/gte}}
                                {{/if}}


                            </li>
                            <li>|</li>
                            <li>
                                <a href="#" class="viewAllCommentsBtn showCommentFormBtn"><i class="icon-message-2"></i><cms:contentText key="COMMENT" code="recognition.public.recognition.item" /></a>
                                {{#gte comments.length 1}}
                                     <a href="#" class="viewAllCommentsBtn commentCountStatus" data-comment="Comment" data-comments="Comments"> <span class="badge">{{comments.length}}</span></a>
                                {{else}}
                                    <span class="commentCountStatus" ><span class="badge">{{comments.length}}</span></span>
                                {{/gte}}
                            </li>



                        {{/ifnotor}}
                        <li class="timeStamp">{{time}}</li>
                    </ul><!-- /.recognition-props -->
                </beacon:authorize>
				{{#if isMine}}

                    <div class="hidePublicRecognitionQTip" style="display: none">
                        <p>
                            <b><cms:contentText key="HIDE_RECOGNITION" code="recognition.public.recognition.item" /></b>
                        </p>
                        <p>
                            <cms:contentText key="HIDE_WARN" code="recognition.public.recognition.item" />
                        </p>
                        <p class="tc">
                            <button type="submit" class="btn btn-primary publicRecognitionHideRecognitionConfirm"><cms:contentText key="HIDE" code="purl.ajax" /></button>
                            <button type="submit" class="btn btn-primary btn-inverse publicRecognitionHideRecognitionCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
                        </p>
                    </div>
                    {{/if}}

            </div><!-- /.span12 -->
        </div><!-- /.row-fluid -->
    {{/if}}

{{else}} <!-- isDetail ELSE -->


<!-- DETAILS FOR MODULE and RECOGNITIONS PAGE (not DETAIL) -->
<div class="app-row detail-row">
    
    {{#eq promotionType "purl"}}
        {{#if hasPurl }}
            <%-- Included new sa markup in purl to bring the new UI of SA temporarily with static content --%>
            <div class="newSAContainer">
                <%-- Appending img as bg for better img manipulation  --%>
                {{#if celebImageUrl}}
                    {{#if3notelse celebImageUrl null "null" ""}}                

                    {{else}}
                        <div class="yearSpan"  style="background-image: url({{#timeStamp celebImageUrl}}{{/timeStamp}});">
                    </div>
                    {{/if3notelse}}
                {{/if}}
                <div class="sa-details">  
                    {{#if programHeader}}                  
                    <h5 class="congrats-msg">{{programHeader}}</h5>
                    {{/if}}
                    <div class="avatar-name-wrap clearfix">
                    {{#isSingle recipients}}
                        {{#if single.avatarUrl}}
                        <div class="avatar"><a href="#"><img alt="{{single.firstName}} {{single.lastName}}" src="{{#timeStamp single.avatarUrl}}{{/timeStamp}}"></a></div>
                        {{else}}
                        <span class="avatar-no">{{trimString single.firstName 0 1}}{{trimString single.lastName 0 1}}</span>
                        {{/if}}
                        <div class="name" style="color: {{../primaryColor}}">{{single.firstName}}</div>
                    {{/isSingle}}
                        
                    </div>
                    <h4 class="company">{{promotionName}}</h4>
                    <h5 class="years">{{celebMessage}}</h5>
                    <button class="btn sa-button" style="background: linear-gradient(to right, {{primaryColor}} 0%, {{secondaryColor}} 100%) {{primaryColor}}" data-cid="{{celebrationId}}"><cms:contentText key="VIEW_CELEBRATION" code="serviceanniversary.content" /></button>                    
                </div>
            </div>
            
            {{else}}
            <div class="celebrationBalloons animation-not-started">
                <div class="balloonContainer">
                    <div class="balloon"><span class="highlight"></span><span class="balloonNum"></span></div>
                    <div class="balloon"><span class="highlight"></span></div>
                    <div class="balloon"><span class="highlight"></span></div>
                    <div class="balloon"><span class="highlight"></span></div>
                    <div class="balloon"><span class="highlight"></span></div>
                    <div class="balloon"><span class="highlight"></span></div>
                </div>
            </div>
        {{/if}}
    {{/eq}}

    {{#eq promotionType "nomination"}}
        <div class="sparkleContainer animation-not-started">
            <div class="nominationBadge sparkleImg">
                <div class="sparkle1 sparkle">&#x2726;</div>
                <div class='sparkle2 sparkle'>&#x2726;</div>
            </div>
        </div>
    {{/eq}}
 <!--Cheers Customization - Celebrating you wip-62128 - Added additional promotype with name cheers and displayed the content - starts -->
 {{#if cheersPromotion}}
        <div class="cheersRecognition">
            <div class="cheersLeft">
                <h1><cms:contentText key="CHEERS_EXCLAMATION" code="client.cheers.recognition" /></h1>
            </div>
            <div class="cheersRight">
                <div class="username">
                    {{#isSingle recipients}}
                        <span class="recipient">{{single.firstName}} {{single.lastName}}</span><span class="cheersBy"><cms:contentText key="WAS_HIGHFIVED_BY" code="client.cheers.recognition" /></span><span class="recognizer">{{../recognizer.firstName}} {{../recognizer.lastName}}</span>
                        <div class="comments recognition-comment readMore"
                            data-read-more-num-lines="2" data-msg-read-more="more">
                            {{{../comment}}}
                        </div>
                        {{else}} 
                            {{#each recipients}}
                                <span class="recipient pubRecRecipient">{{firstName}} {{lastName}}.</span>
                            {{/each}} 
                            <a class="othersWrap" style="display:none"></a>
                            <span class="othersCountWrap" style="display:none"><cms:contentText key="AND" code="recognition.public.recognition.item" /> <span class="othersCount"></span> <cms:contentText key="OTHERS" code="recognition.public.recognition.item" /></span>                                                        
                            <span class="cheersBy"><cms:contentText key="WAS_HIGHFIVED_BY" code="client.cheers.recognition" /></span><span class="recognizer">{{recognizer.firstName}} {{recognizer.lastName}}</span>
                            <div class="comments recognition-comment readMore"
                                data-read-more-num-lines="2" data-msg-read-more="more">
                                {{{comment}}}
                            </div>
                    {{/isSingle}}                      
                </div>                                                
            </div>
            <div class="timeStamp">
                <span class="timeStamp">{{time}}</span> 
            </div>
        </div>
  
    <!--Cheers Customization - Celebrating you wip-62128 - Added additional promotype with name cheers and displayed the content - ends -->
 

 <!--Cheers Customization - Celebrating you wip-62128 - Added non cheers promocheck - starts -->
{{else}}   
{{#ueq hasPurl true }}
<%-- This unequality has been created to check whether the iteration item has sa enabled or not --%>
   <div class="app-col pubRecAvatarWrap">
        {{#isSingle recipients}}
            <div class="avatarwrap recipientAvatar">
                <a class="profile-popover recipientPopover" data-participant-ids="[{{single.id}}]">
                    {{#if single.avatarUrl}}
                    <img alt="{{firstName}} {{lastName}}" src="{{#timeStamp single.avatarUrl}}{{/timeStamp}}" />
                    {{else}}
                    <span class="avatar-initials">{{trimString single.firstName 0 1}}{{trimString single.lastName 0 1}}</span>
                    {{/if}}
                </a>
            </div>
            {{#eq ../promotionType "recognition"}}
                <div class="avatarwrap recognizerAvatar recognizerPopover">
                    <a class="profile-popover " data-participant-ids="[{{../recognizer.id}}]">
                        {{#if ../recognizer.avatarUrl}}
                        <img alt="{{../recognizer.firstName}} {{../recognizer.lastName}}"  src="{{#timeStamp ../recognizer.avatarUrl}}{{/timeStamp}}" />
                        {{else}}
                        <span class="avatar-initials">{{trimString ../recognizer.firstName 0 1}}{{trimString ../recognizer.lastName 0 1}}</span>
                        {{/if}}
                    </a>
                </div>
            {{/eq}}
            {{else}}
                {{#eq ../promotionType "nomination"}}
                    <div class="avatarwrap recipientAvatar teamRecipientPopover">
                        <a class="viewAllRecip" data-participant-ids="[{{id}}]" href="#">
                            <span class="avatar-initials"><i class="icon-team-1"></i></span>
                        </a>
                    </div>
                    {{else}}
                        {{#each recipients}}
                            <div class="avatarwrap multiRecipientAvatar">
                                <a class="profile-popover " data-participant-ids="[{{id}}]" href="#">
                                    {{#if avatarUrl}}
                                    <img alt="" src="{{#timeStamp avatarUrl}}{{/timeStamp}}" data-name="{{firstName}} {{lastName}}" />
                                    {{else}}
                                    <span class="avatar-initials" data-name="{{firstName}} {{lastName}}">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                                    {{/if}}
                                </a>
                            </div>
                        {{/each}}

                    <div class="avatarwrap allRecipAvatar">
                        <a class="recipientAvatar viewAllRecip">
                            <span class="avatar-initials "></span>
                        </a>
                    </div>

                    <div class="viewOthersPopover" style="display:none">
                        <ul>
                            <!-- dynamic from PublicRecognitionModuleView.js -->
                        </ul>
                    </div>
                    {{#eq promotionType "recognition"}}
                        <hr />
                        <div class="avatarwrap multiReceiverAvatar">
                            <a class="profile-popover " data-participant-ids="[{{recognizer.id}}]">
                                {{#if recognizer.avatarUrl}}
                                    <img alt="{{recognizer.firstName}} {{recognizer.lastName}}" data-name="{{firstName}} {{lastName}}"  src="{{#timeStamp recognizer.avatarUrl}}{{/timeStamp}}"  />
                                {{else}}
                                    <span data-name="{{firstName}} {{lastName}}" class="avatar-initials">{{trimString recognizer.firstName 0 1}}{{trimString recognizer.lastName 0 1}}</span>
                                {{/if}}
                            </a>
                        </div>
                    {{/eq}}
                {{/eq}}
            <div class="viewAllRecipPopover" style="display:none">
                <ul>
                    {{#each recipients}}
                        <li>{{firstName}} {{lastName}}</li>
                    {{/each}}
                </ul>
            </div>
        {{/isSingle}}
    </div><!-- /pubRecAvatarWrap -->
    
    <div class="app-col recognition-block">
        {{#if purlUrl}}
            <a href="{{purlUrl}}" class="recDetailLink"></a>
            {{else}}
                <a href="{{publicRecognitionPageDetailUrl}}" class="recDetailLink"></a>
        {{/if}}
        <div class="comment-top">
            {{#isSingle recipients}}
                {{#eq ../promotionType "recognition"}}
                <div {{#if ../badgeImg}}class="topWrapper"{{/if}}>
                    <div class="recipNameWrap">
                        <a class="pubRecRecipient" href="{{../publicRecognitionPageDetailUrl}}">{{single.firstName}} {{single.lastName}}<span class="paxMeta">{{#if single.orgName}} {{single.orgName}}|
                        {{/if}}

                        {{#if single.departmentName}}
                        {{single.departmentName}}|
                        {{/if}}

                        {{#if single.jobName}}
                        {{single.jobName}}
                        {{/if}}
                        </span>
                        </a>
                    </div>

                    <span class="recAction"><cms:contentText key="WAS_RECOGNIZED_BY" code="recognition.public.recognition.item" /></span>

                    <a class="pubRecRecognizer" href="{{../publicRecognitionPageDetailUrl}}">{{../recognizer.firstName}} {{../recognizer.lastName}}</a>
                </div>

                {{/eq}}

                {{#eq ../promotionType "nomination"}}
                    <div class="recipNameWrap">
                       <a class="pubRecRecipient" href="{{../publicRecognitionPageDetailUrl}}">{{single.firstName}} {{single.lastName}}<span class="paxMeta">{{#if single.orgName}} {{single.orgName}}|
                        {{/if}}

                        {{#if single.departmentName}}
                        {{single.departmentName}}|
                        {{/if}}

                        {{#if single.jobName}}
                        {{single.jobName}}
                        {{/if}}
                        </span>
                        </a>
                    </div>

                    <span class="won"><cms:contentText key="WON_THE" code="recognition.public.recognition.item" /></span>

                    <span class="pubRecNomPromoName">{{../promotionName}}</span>

                {{/eq}}

                {{#eq ../promotionType "purl"}}
                    <div class="recipNameWrap">
                        <a class="pubRecRecipient" href="{{../purlUrl}}">{{single.firstName}} {{single.lastName}}
                        </a>
                    </div>

                    <span class="celebrate"><cms:contentText key="CELEBRATED" code="recognition.public.recognition.item" /></span>

                    <span class="purlPromoName">{{../promotionName}}</span>
                {{/eq}}

                {{else}}

                {{#eq promotionType "recognition"}}
                    <div {{#if badgeImg}}class="topWrapper"{{/if}}>
                        <div class="recipNameWrap">
                            {{#each recipients}}
                            <a class="pubRecRecipient" href="{{../publicRecognitionPageDetailUrl}}">{{firstName}} {{trimString lastName 0 1}}.</a>
                            {{/each}}

                            <a class="othersWrap" style="display:none"><cms:contentText key="AND" code="recognition.public.recognition.item" /> <span class="othersCount"></span> <cms:contentText key="OTHERS" code="recognition.public.recognition.item" /></a>
                        </div>

                        <span class="recAction"><cms:contentText key="WERE_RECOGNIZED_BY_NO_PARAMS" code="recognition.public.recognition.item" /></span>

                        <a class="pubRecRecognizer" href="{{../publicRecognitionPageDetailUrl}}">{{recognizer.firstName}} {{recognizer.lastName}}</a>
                    </div>
                {{/eq}}

                {{#eq promotionType "nomination"}}
                    <div class="recipNameWrap">
                         <a class="pubRecRecipient" href="{{../publicRecognitionPageDetailUrl}}">{{teamName}}</a>
                    </div>

                    <span><cms:contentText key="WON_THE" code="recognition.public.recognition.item" /></span>

                    <span class="pubRecNomPromoName">{{promotionName}}</span>

                {{/eq}}

            {{/isSingle}}

            {{#if badgeImg}}
                <span class="badgeWrap">
                    <img src="{{badgeImg}}" alt="" class="badgeImg" rel="tooltip" data-placement="top" data-original-title="{{badgeName}}" data-trigger="click" />

                    {{#if additionalBadges}}
                       <span class="additionalBadges">+{{additionalBadges}}</span>
                    {{/if}}
                </span>
            {{/if}}

            <span class="timeStamp">{{time}}</span>

        </div><!-- /comment-top -->


            {{#eq promotionType "recognition"}}
			 <div class="commentBody">
                {{#if allowTranslate}}
                    <span class="translateCont">
                        <a href="#" class="translatePubRec"><cms:contentText key="TRANSLATE" code="recognition.public.recognition.item" /></a>
                    </span>
                {{/if}}

                {{#if comment}}
                <div class="recognition-comment readMore {{#unless eCardImg}}noEcard{{/unless}}"
                    data-read-more-num-lines="5" data-msg-read-more="more">
                    {{{comment}}}
                </div>
                {{/if}}

                {{#if eCardImg}}
                    <img src="{{eCardImg}}" alt="" class="recEcard" />
                {{/if}}

                {{#if videoUrl}}
                    <div class="videoPlayerWrap">
                        <video id="recognitionVideo_{{id}}" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                            <source src="{{videoUrl}}" type="">
                        </video>
                    </div>
                {{/if}}
			 </div>
            {{/eq}}

            {{#if purlUrl}}
                <a href="{{purlUrl}}" class="btn btn-inverse  btn-fullmobile"><cms:contentText key="VIEW_PURL" code="recognition.public.recognition.item" /></a>
            {{/if}}

            {{#eq promotionType "nomination"}}
                <a href="{{publicRecognitionPageDetailUrl}}" class="btn btn-inverse btn-fullmobile"><cms:contentText key="CHECK_IT_OUT" code="recognition.public.recognition.item" /></a>
            {{/eq}}


        {{#eq promotionType "recognition"}}
        <div>
            <beacon:authorize ifNotGranted="LOGIN_AS,BI_ADMIN">


                <ul class="recognition-props">
                    {{#if isMine}}
                        <li class="propLineTop">
                            {{#if isHidden}}
                                Hidden
                            {{else}}
                                <a href="#" class="hidePublicRecognitionLnk">Hide</a>
                                <span class="publicRecognitionHiddenLinkText" style="display:none;">
                                    Hidden
                                </span>
                            {{/if}}
                        </li>
                    {{/if}}
                    {{#unless isMine}}
                        {{#if allowAddPoints}}
                            <li class="pubRecPoints propLineTop"><a href="#" class="showAddPointsFormBtn"><cms:contentText key="ADD_POINTS" code="recognition.public.recognition.item" /></a></li>
                        {{/if}}
                    {{/unless}}
                    <li>
                        {{#if isLiked}}
                            <a href="#" class="likePubRecBtn liked mylike" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i><cms:contentText key="UNLIKE" code="recognition.public.recognition.item" /></a>
                             {{#gte numLikers 2}}
                                <a class="profile-popover likeCountStatus" href="#" data-recognition-id="{{id}}" data-participant-info-type="likers"> <span class="badge">{{numLikers}}</span></a>
                            {{else}}
                                <span class="likeCountStatus" ><span class="badge">{{numLikers}}</span></span>
                        {{/gte}}
                        {{else}}
                            <a href="#" class="likePubRecBtn" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i><cms:contentText key="LIKE" code="recognition.public.recognition.item" /></a>
                             {{#gte numLikers 1}}
                                <a class="profile-popover likeCountStatus" href="#" data-recognition-id="{{id}}" data-participant-info-type="likers"> <span class="badge">{{numLikers}}</span></a>
                            {{else}}
                                <span class="likeCountStatus" ><span class="badge">{{numLikers}}</span></span>
                            {{/gte}}
                        {{/if}}


                    </li>
                    <li>|</li>
                    <li>
                        <a href="#" class="viewAllCommentsBtn showCommentFormBtn"><i class="icon-message-2"></i><cms:contentText key="COMMENT" code="recognition.public.recognition.item" /></a>
                        {{#gte comments.length 1}}
                             <a href="#" class="viewAllCommentsBtn commentCountStatus" data-comment="Comment" data-comments="Comments"> <span class="badge">{{comments.length}}</span></a>
                        {{else}}
                            <span class="commentCountStatus" ><span class="badge">{{comments.length}}</span></span>
                        {{/gte}}
                    </li>
                    {{#unless isMine}}
					{{#isSingle recipients}}
					 <li>
								    <!-- hidden inputs --> 
										<input type="hidden"  id="recognitionId"  value="{{id}}" />
									  <input type="hidden" id="promotionId"    value="{{id}}" />

									  <input type="hidden" id="cheersPromotionId"    value="{{id}}" />
									   <input type="hidden" id="participantId"    value="{{#each recipients}}{{id}}{{/each}}" />
									   <input type="hidden" id="participantIds"    value="{{#each recipients}}{{/each}}" />

					 				<!--Cheers Customization - Celebrating you wip-62128 - Added cheers link which will display popup with eligible points - starts -->
					                <!-- Java need to bring the data-cheers-promotion-id from system variable. There will be one cheers recognition promotion will be setup in admin side. That can be mapped in system variable. That can be brought into this attribute --> 
							 <a href="#"   data-cheers-promotion-id="{{id}}" data-participant-ids="{{single.id}}" class="cheers-popover"> <i class="icon-cheers"></i><cms:contentText key="CHEERS" code="client.cheers.recognition" /></a>        
							<!--Cheers Customization - Celebrating you wip-62128 - Added cheers link which will display popup with eligible points - ends -->

					</li>
					{{/isSingle}}
					{{/unless}}

                </ul><!-- /recognition-props -->
             </beacon:authorize>
             <beacon:authorize ifAnyGranted="LOGIN_AS,BI_ADMIN">
                 <ul class="recognition-props launched-as">
                     {{#if isMine}}
                         <li class="propLineTop">
                             {{#if isHidden}}
                                 Hidden
                             {{else}}
                                 <a href="#" class="hidePublicRecognitionLnk">Hide</a>
                                 <span class="publicRecognitionHiddenLinkText" style="display:none;">
                                     Hidden
                                 </span>
                             {{/if}}
                         </li>
                     {{/if}}
                     {{#unless isMine}}
                         {{#if allowAddPoints}}
                             <li class="pubRecPoints propLineTop"><a href="#" class="showAddPointsFormBtn"><cms:contentText key="ADD_POINTS" code="recognition.public.recognition.item" /></a></li>
                         {{/if}}
                     {{/unless}}
                     <li>
                         {{#if isLiked}}
                             <a href="#" class="likePubRecBtn liked mylike" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i><cms:contentText key="UNLIKE" code="recognition.public.recognition.item" /></a>
                              {{#gte numLikers 2}}
                                 <a class="profile-popover likeCountStatus" href="#" data-recognition-id="{{id}}" data-participant-info-type="likers"> <span class="badge">{{numLikers}}</span></a>
                             {{else}}
                                 <span class="likeCountStatus" ><span class="badge">{{numLikers}}</span></span>
                         {{/gte}}
                         {{else}}
                             <a href="#" class="likePubRecBtn" data-like="Like" data-unlike="Unlike"><i class="icon-star"></i><cms:contentText key="LIKE" code="recognition.public.recognition.item" /></a>
                              {{#gte numLikers 1}}
                                 <a class="profile-popover likeCountStatus" href="#" data-recognition-id="{{id}}" data-participant-info-type="likers"> <span class="badge">{{numLikers}}</span></a>
                             {{else}}
                                 <span class="likeCountStatus" ><span class="badge">{{numLikers}}</span></span>
                             {{/gte}}
                         {{/if}}


                     </li>
                     <li>|</li>
                     <li>
                         <a href="#" class="viewAllCommentsBtn showCommentFormBtn"><i class="icon-message-2"></i><cms:contentText key="COMMENT" code="recognition.public.recognition.item" /></a>
                         {{#gte comments.length 1}}
                              <a href="#" class="viewAllCommentsBtn commentCountStatus" data-comment="Comment" data-comments="Comments"> <span class="badge">{{comments.length}}</span></a>
                         {{else}}
                             <span class="commentCountStatus" ><span class="badge">{{comments.length}}</span></span>
                         {{/gte}}
                     </li>



                 </ul><!-- /recognition-props -->
             </beacon:authorize>
        </div>
        {{/eq}}

    {{#if isMine}}
        <div class="hidePublicRecognitionQTip" style="display: none">
            <p><b><cms:contentText key="HIDE_RECOGNITION" code="recognition.public.recognition.item" /></b></p>
            <p><cms:contentText key="HIDE_WARN" code="recognition.public.recognition.item" /></p>
            <p class="tc">
                <button type="submit" class="btn btn-primary publicRecognitionHideRecognitionConfirm"><cms:contentText key="HIDE" code="recognition.public.recognition.item" /></button>
                <button type="submit" class="btn publicRecognitionHideRecognitionCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
            </p>
        </div>
        {{/if}}
    </div><!-- /.recognition-block -->
{{/ueq}}<!-- /.sa ueq check to hide the unrelevant items with regards to SA -->
{{/if}}
 <!--Cheers Customization - Celebrating you wip-62128 - Added non cheers promocheck - ends -->

</div><!-- /.detail-row -->


{{/if}}<!-- END isDetail ELSE...IF -->


<!-- START isPublicClaim -->
{{#if isPublicClaim}}

    <!-- COMMENTS -->

    <!-- list of comments -->
    <div class="pubRecComments" style="display:none">
        <!-- dynamic - populated by view -->
    </div><!-- /.pubRecComments -->

    <!-- add comment form -->
    <div class="app-row comment-block">
        <div class="app-col  commentInputWrapper" style="display:none">
            <!-- DEVELOPER NOTE: the form action attr below will be used as the URL to get JSON
                - make sure the class 'publicRecognitionCommentForm' stays on the form element
            -->
            <form class="form-inline publicRecognitionCommentForm" action="<%=RequestUtils.getBaseURI( request )%>/publicRecognitionCommentAction.do?method=postComment" method="POST">

                <!-- hidden inputs -->
                <input type="hidden" name="recognitionId" value="{{id}}" />
                <input type="hidden" name="org.apache.struts.action.TOKEN" value="<%=session.getAttribute("org.apache.struts.action.TOKEN") %>"/>

                <!-- data-recognition-id will be passed to JSON url along with any other data-* attrs -->
                <textarea name="comment" class="comment-input commentInputTxt" placeholder="<cms:contentText key="LEAVE_A_COMMENT" code="recognition.public.recognition.item"/>" maxlength="300"></textarea>

            </form>
        </div><!-- /.commentInputWrapper -->
    </div><!-- /.comment-block -->
    <!-- EOF COMMENTS -->


    {{#if allowAddPoints}}
    <!-- add points form -->
    <div class="app-row comment-block">
        <div class="app-col  addPointsWrapper clearfix" style="display:none">

            <div class="alert alert-success msgAddPointsSuccess" style="display:none">
                    <!--a class="close" data-dismiss="alert" href="#"><i class="icon-close"></i></a-->
                    <h4 class="alert-heading"><cms:contentText key="SUCCESS" code="recognition.public.recognition.item" /></h4>
                    <cms:contentText key="POINTS_ADDED" code="recognition.public.recognition.item" />
            </div>

            <!-- DEVELOPER NOTE: the form action attr below will be used as the URL to get JSON
                - make sure the class 'publicRecognitionAddPointsForm' stays on the form element
                - MUST HAVE CLASS publicRecognitionAddPointsForm
                - 'name' attributes may be set to match java params
            -->
            <form class="form-inline publicRecognitionAddPointsForm" action="<%=RequestUtils.getBaseURI( request )%>/publicRecognitionAddPointsAction.do?method=addPoints">

                <!-- hidden inputs -->
                <input type="hidden" name="recognitionId" value="{{id}}" />
                <input type="hidden" name="org.apache.struts.action.TOKEN" value="<%=session.getAttribute("org.apache.struts.action.TOKEN") %>"/>

                <div>
                    <div class="control-group budgetSelectWrapper">
                        <select name="budgetId" class="budgetSelect dropdown-toggle">
                            <option value="-1"><cms:contentText key="SELECT_BUDGET" code="recognition.public.recognition.item" /></option>
                            <!-- dynamic -->
                        </select>
                    </div>
                </div>

                <div class="control-group">
                    <!-- MUST HAVE CLASS pointsInputTxt -->
                    <input type="text" name="points" class="input-mini pointsInputTxt" placeholder="<cms:contentText key="POINTS" code="recognition.public.recognition.item" />"

                        {{#if awardAmountFixed}}
                            value="{{awardAmountFixed}}"
                            readonly="readonly"
                            data-tooltip="<cms:contentText key="AWARD_FIXED" code="recognition.public.recognition.item" />"
                        {{/if}}

                        {{#if awardAmountMax}}
                            data-tooltip="<cms:contentText key="RANGE" code="recognition.public.recognition.item" />: {{awardAmountMin}} - {{awardAmountMax}}"
                        {{/if}}
                    />

                    <span class="help-inline"><cms:contentText key="POINTS" code="recognition.public.recognition.item" /></span>

                    <button class="btn btn-primary addPointsBtn"
                        data-msg-err-out-of-range = "<cms:contentText key="OUT_OF_RANGE" code="recognition.public.recognition.item" />"
                        data-msg-err-over-budget = "<cms:contentText key="OVER_BUDGET" code="recognition.public.recognition.item" />"
                        data-msg-err-no-comment = "<cms:contentText key="COMMENT_REQUIRED" code="recognition.public.recognition.item" />"
                        data-msg-err-no-points = "<cms:contentText key="POINTS_REQUIRED" code="recognition.public.recognition.item" />"
                        data-msg-err-no-budg-sel = "<cms:contentText key="BUDGET_REQUIRED" code="recognition.public.recognition.item" />"
                    ><cms:contentText key="SUBMIT" code="recognition.public.recognition.item" /></button>


                    <span class="help-inline budgetInfo">
                        <a href="#" class="budgetPopoverTrigger">
                            <span class="budgetRemaining">{{budget}}</span>
                            <cms:contentText key="BUDGET_REMAINING" code="recognition.public.recognition.item" />
                            <i class="icon-info"></i>
                        </a>
                    </span>

                    <div style="display:none"><!-- this hides stuff, that is all -->
                        <!-- popover table starts life here -->
                        <table class='table budgetPopover'>
                            <tr>
                                <td><cms:contentText key="AVAILABLE_BUDGET" code="recognition.public.recognition.item" /></td>
                                <td class="budgetAvailable">{{budget}}</td>
                            </tr>
                            <tr>
                                <td><cms:contentText key="CALCULATED_BUDGET" code="recognition.public.recognition.item" /></td>
                                <td class="budgetDeduction">0</td>
                            </tr>
                            <tr>
                                <td><cms:contentText key="REMAINING_BUDGET" code="recognition.public.recognition.item" /></td>
                                <td class="budgetRemaining">{{budget}}</td>
                            </tr>
                        </table>
                    </div>

                </div><!-- /.control-group -->

                <div>
                    <textarea name="comment"
                        class="comment-input addPointsCommentInputTxt" maxlength="300"
                        placeholder="<cms:contentText key="LEAVE_A_COMMENT" code="recognition.public.recognition.item"/>"></textarea>
                </div>

            </form>
        </div><!-- /.addPointsWrapper -->
    </div><!-- /.comment-block -->
    <!-- EOF ADD POINTS -->
    {{/if}}

<!-- EOF IS PUBLIC CLAIM -->
{{/if}}


<!-- /publicRecognitionItem TPL -->