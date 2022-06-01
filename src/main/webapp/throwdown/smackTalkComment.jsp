<%@ include file="/include/taglib.jspf"%>
<!-- smackTalkComment {{id}} -->
<div class="app-row comment-block clearfix smackTalkCommentsComment comment{{id}}" data-comment-id="{{id}}" style="display: none;">

    <div class="app-col">
        <a class="profile-popover" data-participant-ids="[{{commenter.id}}]">
            <img alt="" class="avatar" src="{{commenter.avatarUrl}}" >
        </a>
    </div>

    <div class="app-col comment {{#if isMine}}mine{{/if}}">

        <p>
            <a class="profile-popover" href="#" data-participant-ids="[{{commenter.id}}]">
                {{commenter.firstName}} {{commenter.lastName}}
            </a>
             <span><cms:contentText key="SAYS" code="smacktalk.details"/></span>
            <div class="readMore" data-read-more-num-lines="2" data-msg-read-more="more">
                {{comment}}
            </div>
            {{#if isMine}}
            <span class="hide-comment">
                {{#unless isHidden}}
                <a href="#" class="hideSmackTalkLnk" title="<cms:contentText key="DELETE_THIS_COMMENT" code="smacktalk.details"/>" </a>
                {{#unless isMine}} &bull; {{/unless}}
                <span class="smackTalkHiddenLinkText" style="display:none;">
                    Hidden
                </span>
                {{/unless}}
            </span>
           {{else}}
            {{#if isLiked}}
                Liked
            {{else}}
                <a href="#" class="likeSmackTalkBtn" data-msg-liked="Liked"><cms:contentText key="LIKE" code="smacktalk.details"/></a>
            {{/if}}
          {{/if}}
        </p>

    </div>

</div>
<!-- /smackTalkComment {{id}} -->