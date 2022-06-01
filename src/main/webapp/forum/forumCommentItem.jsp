<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script>
  $(document).ready(function() {

      G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

      //Mini Profile PopUp Follow Unfollow Pax JSON
      G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

        G5.props.URL_JSON_COMMENTS_MORE = G5.props.URL_ROOT+'forum/forumDiscussionDetailMaintainDisplay.do?method=fetchMoreDiscussionReplies';
});

</script>

<li class="discussionComment commonDiscussionWrapper mediaComment" data-id="{{id}}">
    <div class="innerCommentWrapper">
        {{#if commenter.avatarUrl}}<img alt="{{commenter.firstName}} {{commenter.lastName}}" class="avatar" src="{{commenter.avatarUrl}}">{{/if}}

        <div class="indent">
            <div class="userInfo" data-id="{{commenter.id}}">
                <span class="author">
                    <a class="profile-popover" href="#" data-participant-ids="[{{commenter.id}}]">{{commenter.firstName}} {{commenter.lastName}}</a>
                </span>
                <span class="timeStamp">
                    {{date}} {{time}}
                </span>
            </div>
            <div class="text">
                {{{comment}}}
            </div>
            <div class="likes">
                <p>{{#unless commenter.isCurrentUser}}{{#eq status "noLikes" }} <beacon:authorize ifNotGranted="BI_ADMIN"><a href="#" data-id="{{id}}" class="{{buttonClass}}"><cms:contentText key="LIKE" code="forum.library" /></a> </beacon:authorize> {{/eq}}
                    {{#eq status "onlyYouLike" }}<cms:contentText key="YOU_LIKE_THIS" code="forum.library" />{{/eq}}
                    {{#eq status "youAndOtherLike" }}<cms:contentText key="YOU_AND" code="forum.library" /> <a href="#" data-participant-ids="[{{likedIds}}]" class="liked profile-popover"> {{numberOfOtherLikes}} <cms:contentText key="OTHER" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>{{/eq}}

                    {{#eq status "youAndOthersLike" }}<cms:contentText key="YOU_AND" code="forum.library" /> <a href="#" data-participant-ids="[{{likedIds}}]" class="liked profile-popover"> {{numberOfOtherLikes}} <cms:contentText key="OTHERS" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>{{/eq}}
                    {{/unless}}

                    {{#eq status "oneLike" }}{{#unless commenter.isCurrentUser}}<beacon:authorize ifNotGranted="BI_ADMIN"><a href="#" data-id="{{id}}" class="{{buttonClass}}" data-participant-ids="[{{likedIds}}]"><cms:contentText key="LIKE" code="forum.library" /></a> &#8226; </beacon:authorize>{{/unless}} <a href="#" class="liked profile-popover" data-participant-ids="[{{likedIds}}]">  {{numberOfLikes}} <cms:contentText key="PERSON_LIKE_THIS" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>{{/eq}}

                    {{#eq status "moreLike" }}{{#unless commenter.isCurrentUser}}<beacon:authorize ifNotGranted="BI_ADMIN"><a href="#" data-id="{{id}}" class="{{buttonClass}}"><cms:contentText key="LIKE" code="forum.library" /></a> &#8226; </beacon:authorize>{{/unless}}<a href="#" class="liked profile-popover" data-participant-ids="[{{likedIds}}]"> {{numberOfLikes}} <cms:contentText key="PEOPLE_LIKE_THIS" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>{{/eq}}
                        <!-- Conditional JSP for admin replace path with actual path -->

                        <beacon:authorize ifAnyGranted="BI_ADMIN">
                        <a href="#" class="removeCommentButton liked" data-path="<%=RequestUtils.getBaseURI(request)%>/forum/forumDiscussionListMaintainDisplay.do?method=removeDiscussionReplies"><cms:contentText key="DELETE" code="promotion.manageroverride" /></a>
                        </beacon:authorize>                        <!-- /Conditional JSP -->
                </p>
            </div>
        </div><!-- /.indent -->
    </div><!-- /.innerCommentWrapper -->
</li>