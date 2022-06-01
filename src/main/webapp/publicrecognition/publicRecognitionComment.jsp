<%@ include file="/include/taglib.jspf"%>
<!-- publicRecognitionComment {{id}} -->
<div class="app-row comment-block clearfix pubRecCommentsComment comment{{id}}" data-comment-id="{{id}}" style="display:none">

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

            {{#if isMine}}
            <span class="hide-comment">
                {{#if isHidden}}
                        <cms:contentText key="HIDDEN" code="recognition.public.recognition.item" />
                    {{else}}
                        <a href="#" class="deletePublicRecognitionLnk"><cms:contentText key="DELETE" code="purl.ajax" /></a>
                        <span class="publicRecognitionHiddenLinkText" style="display:none;">
                            <cms:contentText key="HIDDEN" code="recognition.public.recognition.item" />
                        </span>
                    {{/if}}
            </span>
            {{#if allowTranslate}}
                <span class="translateCont">
                    <a href="#" class="translatePubRec"><cms:contentText key="TRANSLATE" code="recognition.public.recognition.item" /></a>
                </span>
            {{/if}}

            {{else}}
                {{#if allowTranslate}}
                    <span class="translateCont">
                        <a href="#" class="translatePubRec"><cms:contentText key="TRANSLATE" code="recognition.public.recognition.item" /></a>
                    </span>
                {{/if}}
            {{/if}}

        </p>
    </div>
</div>
<!-- /publicRecognitionComment {{id}} -->
{{#if isMine}}
<div class="deletePublicRecognitionQTip" style="display:none">
    <p><cms:contentText key="DELETE_WARN_COMMENT" code="recognition.public.recognition.item" />
    </p>
    <p class="tc">
        <button type="submit" class="btn btn-primary btn-small publicRecognitionDeleteRecognitionConfirm"><cms:contentText key="DELETE" code="purl.ajax" /></button>
        <button type="submit" class="btn  btn-inverse-primary btn-small publicRecognitionDeleteRecognitionCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
    </p>
</div>
{{/if}}