<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ImageUtils"%>
<!-- DETAILS FOR MODULE and RECOGNITIONS PAGE (not DETAIL) -->

<div class="app-row detail-row">
    {{#unless isDetail}}
    <div class="st-title-row">
        <a href="{{smackTalkPageDetailUrl}}">
            {{player1.firstName}} {{player1.lastName}} <cms:contentText key="VERSUS" code="smacktalk.details"/>  {{player2.firstName}} {{player2.lastName}}
        </a>
    </div>
    {{/unless}}
    <div class="app-col">
        {{#if isDetail}}
        <a class="profile-popover" data-participant-ids="[{{commenterMain.id}}]">
            <img alt="{{commenterMain.firstName}} {{commenterMain.lastName}}" class="avatar" src="{{commenterMain.avatarUrl}}" height="60" width="60" />
        </a>
        {{else}}
        <div>
            <a {{#if player1.id}}class="profile-popover" href="#" data-participant-ids="[{{player1.id}}]"{{/if}}>
               <img alt="{{player1.firstName}} {{player1.lastName}}" src="{{player1.avatarUrl}}" class="avatar player1" height="60" width="60" /></a>
            <span class="st-vs"><cms:contentText key="VERSUS" code="smacktalk.details"/></span>
            <a {{#if player2.id}}class="profile-popover" href="#" data-participant-ids="[{{player2.id}}]"{{/if}}>
               <img alt="{{player2.firstName}} {{player2.lastName}}" src="{{player2.avatarUrl}}" class="avatar player2" height="60" width="60" />
            </a>
        </div>
        {{/if}}
    </div>

    <div class="app-col smack-talk-block">
        <div>
            <a class="profile-popover" href="#" data-participant-ids="[{{commenterMain.id}}]">
                {{commenterMain.firstName}} {{commenterMain.lastName}}
            </a>
            <span><cms:contentText key="SAYS" code="smacktalk.details"/></span>
            {{#if comment}}
            <div class="smack-talk-comment readMore"
                data-read-more-num-lines="2" data-msg-read-more="more">
                {{{comment}}}
            </div>
            {{/if}}
        </div>

        <ul class="smack-talk-props">
         {{#unless isMine}}
              {{#unless isLiked}}
                <li><a href="#" class="likeSmackTalkBtn" data-msg-liked="Liked"><cms:contentText key="LIKE" code="smacktalk.details" /></a> &bull; </li>
                {{/unless}}
                <li><a href="#" class="showCommentFormBtn"><cms:contentText key="COMMENT" code="smacktalk.details" /></a> &bull; </li>
          {{/unless}}
            {{#if isMyMatch}}
                <li>
                    {{#if isHidden}}
                         <cms:contentText key="HIDDEN" code="smacktalk.details" />
                    {{else}}
                        <a href="#" class="hideSmackTalkLnk"><cms:contentText key="HIDE" code="smacktalk.details" /></a>
                        <span class="smackTalkHiddenLinkText" style="display:none;">
                             <cms:contentText key="HIDDEN" code="smacktalk.details" />
                        </span>
                    {{/if}}
                    &bull;
                </li>
            {{/if}}




            <li>{{time}}</li>
        </ul>
    </div><!-- /.smack-talk-block -->

    {{#unless isDetail}}
    <div class="app-col chevron">
        <a href="{{smackTalkPageDetailUrl}}"><i class="icon-chevron-right"></i></a>
    </div>
    {{/unless}}

     <div class="hideSmackTalkQTip" style="display:none">
        <p><cms:contentText key="HIDE_WARN" code="smacktalk.details" /></p>
        <p class="tc">
            <button type="submit" class="btn btn-primary smackTalkHideRecognitionConfirm"><cms:contentText key="HIDE_LABEL" code="smacktalk.details" /></button>
            <button type="submit" class="btn btn-danger smackTalkHideRecognitionCancel"><cms:contentText key="CANCEL" code="smacktalk.details" /></button>
        </p>
    </div>


</div><!-- /.detail-row -->



<!-- START isPublicClaim -->


    <!-- COMMENTS -->
    <div class="app-row comment-block">
        <div class="app-col likeInfoWrapper">

            <!-- possible sentences - details filled in by JS -->
            <div class="likeSentence youOnly" style="display:none">
                <cms:contentText key="YOU_LIKE_THIS" code="smacktalk.details" />
            </div>
            <div class="likeSentence youAndOne" style="display:none">
                <cms:contentText key="YOU_AND_OTHER" code="smacktalk.details" />
            </div>
            <div class="likeSentence youAndMult" style="display:none">
                <cms:contentText key="YOU_AND_OTHERS" code="smacktalk.details" />
            </div>
            <div class="likeSentence oneOtherOnly" style="display:none">
                 <cms:contentText key="1_PERSON_LIKE_LINK" code="smacktalk.details" />
            </div>
            <div class="likeSentence multOtherOnly" style="display:none">
                 <cms:contentText key="PEOPLE_LIKE_LINK" code="smacktalk.details" />
            </div>

            <!--
                Please see SmackTalkModelView.initLikeInfo() for current
                details and markup injected into the sentences.
            -->

        </div>
    </div><!-- /.comment-block -->

    <div class="st-meta-separator app-row"></div>

    <!-- view all comments control -->
    {{#if comments.length}}
        {{#if isHideComments}}
            {{#isSingle comments}}
            {{else}}
                <div class="app-row comment-block viewAllCommentsWrapper">
                    <div class="app-col">
                        <cms:contentText key="VIEW_ALL" code="smacktalk.details" /> <a href="#" class="viewAllCommentsBtn">
                        <span class="commentCount">{{comments.length}}</span>
                         <cms:contentText key="COMMENTS_WITHOUT_COLON" code="smacktalk.details" /></a>
                    </div>
                </div>
            {{/isSingle}}
        {{/if}}
    {{/if}}

    <!-- list of comments -->
    <div class="smackTalkComments">
        <!-- dynamic - populated by view -->
    </div><!-- /.smackTalkComments -->

    <!-- add comment form -->
    <div class="app-row comment-block">
        <div class="app-col  commentInputWrapper" style="display:none">
            <!-- DEVELOPER NOTE: the form action attr below will be used as the URL to get JSON
                - make sure the class 'smackTalkCommentForm' stays on the form element
            -->
            <form class="form-inline smackTalkCommentForm" action="smackTalkComment.do?method=postComment&smackTalkId={{id}}&matchId={{matchId}}">

                 <!-- hidden inputs -->
                <%--<input type="hidden" name="smackTalkId" value="{{id}}" />
                <input type="hidden" name="matchId" value="{{matchId}}" /> --%>
                <!-- data-smack-talk-id will be passed to JSON url along with any other data-* attrs -->
                <textarea name="comment" class="comment-input commentInputTxt" placeholder="<cms:contentText key="LEAVE_A_COMMENT" code="smacktalk.details" />" maxlength="300"></textarea>

            </form>
        </div><!-- /.commentInputWrapper -->
    </div><!-- /.comment-block -->
    <!-- EOF COMMENTS -->


<!-- EOF IS PUBLIC CLAIM -->



<!-- /smackTalkItem TPL -->
