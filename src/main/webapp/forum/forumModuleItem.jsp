<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumDiscussionValueBean"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script>
  $(document).ready(function() {

	  G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

	  //Mini Profile PopUp Follow Unfollow Pax JSON
	  G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

<script type="text/template" id="forumModuleItemTpl">
{{#each .}}
<div class="item">
    <div class="discussion">
        <p class="discussionTopic">
            <span class="discussionTopicLabel"><cms:contentText key="TOPIC" code="forum.library" />:</span>
            <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumPageDiscussionsFromTile.do?topicId={{topicId}}&topicName={{topicName}}" class="discussionTopicLink">{{topicName}}</a>
        </p>

    {{#if id}}

        <h3 class="discussionHeadline">
            <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumDiscussionDetailMaintainDisplay.do?method=displayDetailDiscussionFromTile&discussionId={{id}}&topicId={{topicId}}&topicName={{topicName}}&discussionName={{name}}" class="viewLink">
                <span class="headline ellipsis multiline">{{name}}</span>
            </a>
        </h3>

        <div class="discussionDetails">
            {{#if author.avatarUrl}}<img alt="{{author.firstName}} {{author.lastName}}" class="avatar" src="{{author.avatarUrl}}" width="48" height="48">{{/if}}

            <div class="indent">
                <div class="userInfo" data-id="{{author.id}}">
                    <span class="author">
                        <cms:contentText key="BY" code="forum.library" /> <a class="profile-popover" href="#" data-participant-ids="[{{author.id}}]">{{author.firstName}} {{author.lastName}}</a>
                    </span>
                    <span class="timeStamp">
                        {{time}}
                    </span>
                </div>
                <div class="text ellipsis multiline">
                    {{{text}}}
                </div>
            </div><!-- /.indent -->

            {{#if comments.0.comment}}
            <div class="discussionLatestComment">
                <div class="innerCommentWrapper">
                    {{#if comments.0.commenter.avatarUrl}}<img alt="{{comments.0.commenter.firstName}} {{comments.0.commenter.lastName}}" class="avatar" src="{{comments.0.commenter.avatarUrl}}" width="48" height="48">{{/if}}

                    <div class="indent">
                        <div class="userInfo" data-id="{{comments.0.commenter.id}}">
                            <span class="author">
                                <a class="profile-popover" href="#" data-participant-ids="[{{comments.0.commenter.id}}]">{{comments.0.commenter.firstName}} {{comments.0.commenter.lastName}}</a>
                            </span>
                            <span class="timeStamp">
                                {{comments.0.time}}
                            </span>
                        </div>
                        <div class="text ellipsis multiline">
                            {{{comments.0.comment}}}
                        </div>
                    </div><!-- /.indent -->
                </div><!-- /.innerCommentWrapper -->
            </div>
            {{/if}}

            <div class="commentsWrapper">
                <span class="commentsCount"><i class="icon-chat-1"></i>
                    {{#if comments}}
						{{#if multipleComments}}
							<cms:contentTemplateText code="forum.library" key="TOTAL_COMMENTS" args="{{numberOfReplies}}"/>
						{{else}}
							<cms:contentTemplateText code="forum.library" key="TOTAL_COMMENT" args="{{numberOfReplies}}"/>
						{{/if}}
                    {{else}}
                    <cms:contentText key="NO_COMMENTS" code="forum.library" />
                    {{/if}}
                </span>
                <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumDiscussionDetailMaintainDisplay.do?method=displayDetailDiscussionFromTile&discussionId={{id}}&topicId={{topicId}}&topicName={{topicName}}&discussionName={{name}}#commentForm" class="btn btn-primary"><i class="icon-megaphone-2"></i> <cms:contentText key="ADD_YOURS" code="forum.library" /></a>
            </div>

        </div>

    {{else}}

        <h3 class="discussionHeadline noResults">
            <cms:contentText key="NO_RESULTS" code="forum.library" /> <span class="topicName">{{topicName}}</span> <cms:contentText key="FORUM" code="forum.library" />
            <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumDiscussionStart.do?method=createDiscussion&topicId={{topicId}}&topicName={{topicName}}" class="btn btn-primary"><cms:contentText key="START_ONE_DISCUSSION" code="forum.library" /> <i class="icon-arrow-1-right"></i></a>
        </h3>

    {{/if}}

    </div>
</div>
{{/each}}

</script>
