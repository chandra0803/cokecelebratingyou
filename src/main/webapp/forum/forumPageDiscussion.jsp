<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumDiscussionDetailValueBean"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>


<!-- ======== FORUM PAGE DISCUSSION ======== -->
<div id="forumPageDiscussionView" class="forumPage-liner page-content"><!-- <%--IE7 needs this comment broken onto two lines. Do not remove
   --%> --><div class="row">
        <div class="span12 discussion forumDiscussionItem">
        </div><!-- /.span12 -->
    </div>

    <div class="row">
        <div class="span12">
            <div class="forumCommentNavigation commentStats"></div>
            <div class="pagination pagination-right paginationControls"></div>

            <div class="discussionCommentsWrapper forumCommentItem">
                <ul class="unstyled discussionComments mediaCommentWrap">
                    <!-- Start Child -->
                </ul>
            </div> <!-- /end .comment_list -->

            <div class="forumCommentNavigation commentStats"></div>
            <div class="pagination pagination-right paginationControls"></div>
        </div>
    </div>

    <div class="row">
        <div class="span12">
        <beacon:authorize ifNotGranted="BI_ADMIN">
                <form class="comment-form" id="commentForm" action="submitReply.do">
                    <label for="notifyMessage"><h3><cms:contentText  key="REPLY" code="forum.library" /></h3></label>
                    <div class="discussionCommentForm commonDiscussionWrapper">
                        <div class="innerCommentWrapper">
                            <img alt="<%=UserManager.getUser().getFirstName()%> <%=UserManager.getUser().getLastName()%>" class="avatar" src="<c:out value="${imageUrl}" />">

                            <div class="indent">
                                <div class="userInfo">
                                    <span class="author">
                                        <%=UserManager.getUser().getFirstName()%> <%=UserManager.getUser().getLastName()%>
                                    </span>
                                </div>

                                <div class="text">
                                    <div class="control-group validateme" data-validate-max-length="2000" data-validate-flags="nonempty,maxlength" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="VALID_REPLY" code="forum.library" />&quot;, &quot;maxlength&quot; : &quot;<cms:contentText key="VALID_REPLY_LENGTH" code="forum.library" />&quot;}">
                                        <textarea rows="5" id="notifyMessage" data-max-chars="2000" name="notifyMessage" class="pull-left richtext input-xxlarge" required></textarea>
                                    </div>
                                    <button class="checkReply btn btn-primary alignright"><cms:contentText  key="SUBMIT" code="system.button" /></button>
                                </div>
                            </div><!-- /.indent -->
                        </div><!-- /.innerCommentWrapper -->
                    </div>
                </form>
            </beacon:authorize>
            <div class="deleteCommentSubmit" style="display:none">
                <p>
                    <b><cms:contentText key="CANCEL_TITLE" code="manager.plateauawardsreminder" /></b>
                </p>
                <p>
                    <cms:contentText key="COMMENT_DELETED" code="forum.library" />
                </p>
                <p class="tc">
                    <a class="btn btn-small confirmBtn"><cms:contentText key="YES" code="claims.form.step.element" /></a>
                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="claims.form.step.element" /></a>
                </p>
            </div><!-- /.deleteCommentSubmit -->

            <div class="deleteDiscussionSubmit" style="display:none">
                <p>
                    <b><cms:contentText key="CANCEL_TITLE" code="manager.plateauawardsreminder" /></b>
                </p>
                <p>
                    <cms:contentText key="COMMENT_DELETED_ALL" code="forum.library" />
                </p>
                <p class="tc">
                    <a class="btn btn-small confirmBtn"><cms:contentText key="YES" code="claims.form.step.element" /></a>
                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="claims.form.step.element" /></a>
                </p>
            </div><!-- /.deleteDiscussionSubmit -->

        </div><!-- /.span12 -->
    </div><!-- /.row -->

    <!--subTpl.paginationTpl=
            {{#if pagination}}
            {{#with pagination}}
                 <ul>
                    <li class="first{{#if first.state}} {{first.state}}{{/if}}" data-page="{{first.page}}">
                        <a href="#"><i class="icon-double-arrows-1-left"></i>&nbsp;</a>
                    </li>
                    <li class="prev{{#if prev.state}} {{prev.state}}{{/if}}" data-page="{{prev.page}}">
                        <a href="#"><i class="icon-arrow-1-left"></i> Previous</a>
                    </li>
                    {{#each pages}}
                    <li {{#if state}}class="{{state}}"{{/if}} data-page="{{page}}">
                        <a href="#">{{#if isgap}}&#8230;{{else}}{{page}}{{/if}}</a>
                    </li>
                    {{/each}}
                    <li class="next{{#if next.state}} {{next.state}}{{/if}}" data-page="{{next.page}}">
                        <a href="#">Next <i class="icon-arrow-1-right"></i></a>
                    </li>
                    <li class="last{{#if last.state}} {{last.state}}{{/if}}" data-page="{{last.page}}">
                        <a href="#">&nbsp;<i class="icon-double-arrows-1-right"></i></a>
                    </li>
                </ul>
            {{/with}}
            {{/if}}
        subTpl-->

    <script id="forumDiscussionItemTpl" type="text/x-handlebars-template">
        <ul class="breadcrumbs">
            <li class="forum">
                <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumPageTopics.do"><cms:contentText key="TOPIC" code="forum.library" /></a>
            </li>
            <li class="topic">
                <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumPageDiscussions.do?topicId={{topicId}}&amp;topicName={{topicName}}">{{topicName}}</a>
            </li>
            <li class="discussion active">
                {{name}}
            </li>
        </ul>

        <h2>{{name}}</h2>
        <div class="discussionDetails commonDiscussionWrapper mediaComment" data-id="[{{id}}]">
            <div class="innerCommentWrapper">
                {{#if author.avatarUrl}}<img alt="{{author.firstName}} {{author.lastName}}" class="avatar" src="{{author.avatarUrl}}">{{/if}}

                <div class="indent">
                    <div class="userInfo" data-id="{{author.id}}">
                        <span class="author">
                            <a class="profile-popover" href="#" data-participant-ids="[{{author.id}}]">{{author.firstName}} {{author.lastName}}</a>
                        </span>
                        <span class="timeStamp">
                            {{date}} {{time}}
                        </span>
                    </div>
                    <div class="text">
                        {{{text}}}
                    </div>
                    <div class="likes">
                        <p>
                            {{#unless author.isCurrentUser}}
                                {{#eq status "noLikes" }} <beacon:authorize ifNotGranted="BI_ADMIN"><a href="#" data-id="{{id}}" class="{{buttonClass}}"><cms:contentText key="LIKE" code="forum.library" /></a> </beacon:authorize>
                                {{/eq}}
                                {{#eq status "onlyYouLike" }}<cms:contentText key="YOU_LIKE_THIS" code="forum.library" />{{/eq}}
                                {{#eq status "youAndOtherLike" }}<cms:contentText key="YOU_AND" code="forum.library" /> <a href="#" data-participant-ids="[{{likedIds}}]" class="liked profile-popover"> {{numberOfOtherLikes}} <cms:contentText key="OTHER" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>
                                {{/eq}}

                                {{#eq status "youAndOthersLike" }}<cms:contentText key="YOU_AND" code="forum.library" /> <a href="#" data-participant-ids="[{{likedIds}}]" class="liked profile-popover"> {{numberOfOtherLikes}} <cms:contentText key="OTHERS" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>
                                {{/eq}}
                            {{/unless}}

                            {{#eq status "oneLike" }}{{#unless commenter.isCurrentUser}}<beacon:authorize ifNotGranted="BI_ADMIN"><a href="#" data-id="{{id}}" class="{{buttonClass}}" data-participant-ids="[{{likedIds}}]"><cms:contentText key="LIKE" code="forum.library" /></a> &#8226; </beacon:authorize>{{/unless}} <a href="#" class="liked profile-popover" data-participant-ids="[{{likedIds}}]">  {{numberOfLikes}} <cms:contentText key="PERSON_LIKE_THIS" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>
                            {{/eq}}

                            {{#eq status "moreLike" }}{{#unless commenter.isCurrentUser}}<beacon:authorize ifNotGranted="BI_ADMIN"><a href="#" data-id="{{id}}" class="{{buttonClass}}"><cms:contentText key="LIKE" code="forum.library" /></a> &#8226; </beacon:authorize>{{/unless}}<a href="#" class="liked profile-popover" data-participant-ids="[{{likedIds}}]"> {{numberOfLikes}} <cms:contentText key="PEOPLE_LIKE_THIS" code="forum.library" /></a> <cms:contentText key="OTHER_LIKE_THIS" code="forum.library" /><beacon:authorize ifAnyGranted="BI_ADMIN"> - </beacon:authorize>
                            {{/eq}}
                            <!-- Conditional JSP for admin replace path with actual path -->

                            <beacon:authorize ifAnyGranted="BI_ADMIN">
                                    <a href="#" class="removeDiscussionButton liked" data-path="<%=RequestUtils.getBaseURI(request)%>/forum/forumDiscussionListMaintainDisplay.do?method=removeDiscussion&amp;discussionId={{id}}"><cms:contentText key="DELETE" code="promotion.manageroverride" /></a>
                            </beacon:authorize>
                            <!-- /Conditional JSP -->
                        </p>
                    </div>
                </div><!-- /.indent -->
            </div><!-- /.innerCommentWrapper -->
        </div>
    </script>

    <script id="forumCommentNavigationTpl" type="text/x-handlebars-template">
        <p>
            {{firstComment}}-{{lastComment}} <cms:contentText key="OF" code="forum.library" /> {{totalNumberOfReplies}} <cms:contentText key="COMMENTS" code="forum.library" />
        </p>
    </script>
</div><!-- /end #forumPageDiscussionsView -->


        <c:choose>
		<c:when test="${submitReply != false}">
        <%
        Map parameterMap = new HashMap();
        Long topicId = (Long) request.getAttribute( "topicId" );
        String topicName = (String) request.getAttribute( "topicName" );
        Long discussionId = (Long) request.getAttribute( "discussionId" );
        String discussionName = (String) request.getAttribute( "discussionName" );

        parameterMap.put( "topicId", topicId );
        parameterMap.put( "topicName", topicName );
        parameterMap.put( "discussionId", discussionId );
        parameterMap.put( "discussionName", discussionName );
        pageContext.setAttribute( "parametersUrlSubmitReply", ClientStateUtils.generateEncodedLink( "", "forum/forumDiscussionDetailMaintainDisplay.do?method=fetchDiscussionDetailItemsAfterSubmitReply", parameterMap ) );
        %>
        </c:when>
        <c:otherwise>
        <%
        Map parameterMap = new HashMap();
        Long topicId = (Long) request.getAttribute( "topicId" );
        String topicName = (String) request.getAttribute( "topicName" );
        Long discussionId = (Long) request.getAttribute( "discussionId" );
        String discussionName = (String) request.getAttribute( "discussionName" );

        parameterMap.put( "topicId", topicId );
        parameterMap.put( "topicName", topicName );
        parameterMap.put( "discussionId", discussionId );
        parameterMap.put( "discussionName", discussionName );
        pageContext.setAttribute( "parametersUrl", ClientStateUtils.generateEncodedLink( "", "forum/forumDiscussionDetailMaintainDisplay.do?method=fetchDiscussionDetailItems", parameterMap ) );
        %>
        </c:otherwise>
        </c:choose>

<script>
  $(document).ready(function() {

      G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

      //Mini Profile PopUp Follow Unfollow Pax JSON
      G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
     	// Recognition wizard info
      G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';


  });
</script>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

    	<c:choose>
		<c:when test="${submitReply != false}">
		G5.props.URL_JSON_DISCUSSIONS = G5.props.URL_ROOT+"${parametersUrlSubmitReply}";
		</c:when>
		<c:otherwise>
		G5.props.URL_JSON_DISCUSSIONS = G5.props.URL_ROOT+"${parametersUrl}";
		</c:otherwise>
		</c:choose>

        G5.props.URL_JSON_FORUM_DISCUSSION_SAVE_LIKE = G5.props.URL_ROOT+'forum/forumDiscussionDetailMaintainDisplay.do?method=fetchForumDiscussionSaveLike';

        G5.props.URL_JSON_FORUM_COMMENT_SAVE_LIKE = G5.props.URL_ROOT+'forum/forumDiscussionDetailMaintainDisplay.do?method=fetchForumCommentSaveLike';

        G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';
        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
        //attach the view to an existing DOM element
        window.forumPageView = new ForumPageView({
            el: $('#forumPageDiscussionView'),
            pageNav  : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="FORUM" code="forum.library" />',
            discussionJson : '',
            discussionJsonUrl : G5.props.URL_JSON_DISCUSSIONS
        });
});

</script>

<script type="text/template" id="paginationViewTpl">
     <%@ include file="/include/paginationView.jsp" %>
</script>
 <%@include file="/submitrecognition/easy/flipSide.jsp"%>
