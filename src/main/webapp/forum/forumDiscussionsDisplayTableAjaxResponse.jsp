<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumDiscussionValueBean"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>

<script>
  $(document).ready(function() {

	  G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

	  //Mini Profile PopUp Follow Unfollow Pax JSON
	  G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

        <%
	     Map parameterMap = new HashMap();
		 ForumDiscussionValueBean temp;
		%>
        <div class="row">
        <div class="span12">
        <display:table defaultorder="ascending" name="forumDiscussionList" id="forumDiscussionValueBean" class="table table-striped crud-table"
                       pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">

	    <display:setProperty name="basic.msg.empty_list_row">
		<tr class="crud-content" align="left">
		<td colspan="{0}">
		<cms:contentText key="NOTHING_FOUND" code="system.errors" />
		</td>
		</tr>
		</display:setProperty>
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

		<%-- the Title of the Discussion --%>

		<display:column titleKey="forum.library.DISCUSSIONS" class="sortable discussionColumn sorted ascending" sortProperty="discussionTitle" sortable="true">
		<%
		temp = (ForumDiscussionValueBean)pageContext.getAttribute( "forumDiscussionValueBean" );
		parameterMap.put( "topicId", temp.getTopicId( ) );
		parameterMap.put( "topicName", temp.getTopicCmAssetCode( ));
		parameterMap.put( "discussionId", temp.getDiscussionId( ) );
		parameterMap.put( "discussionTitle", temp.getDiscussionTitle());
		pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "forumDiscussionDetailMaintainDisplay.do?method=displayDetailDiscussion", parameterMap ) );
		%>
		<a href="<c:out value="${editUrl}"/>" class="crud-content-link"> <c:out value="${forumDiscussionValueBean.discussionTitle}" /> </a>
		</display:column>

		<%-- the Number of replies to a discussion --%>

		<display:column titleKey="forum.library.REPLIES" class="sortable replyCountColumn unsorted" sortProperty="replies" sortable="true">
		<c:out value="${forumDiscussionValueBean.replies}" />
		</display:column>

		<%-- created the discussion --%>

		<display:column titleKey="forum.library.CREATED" class="sortable createdColumn sorted descending" sortProperty="createdDate" sortable="true">
		<c:if test="${forumDiscussionValueBean.createdDate != null}">
		<c:out value="${forumDiscussionValueBean.createdDate}" /> <br>
		<cms:contentText key="BY" code="forum.library" /> <a class="profile-popover ellipsis_link"
								data-participant-ids="[${forumDiscussionValueBean.createdUserId}]"
								href="#" ><c:out value="${forumDiscussionValueBean.createdBy}" />
		</a>
		</c:if>
		</display:column>

		<%-- Last reply to the discussion --%>

		<display:column titleKey="forum.library.LAST_REPLY" class="sortable lastReplyColumn unsorted" sortProperty="repliedDate" sortable="true" >
		<c:choose>
		<c:when test="${forumDiscussionValueBean.repliedDate != null}">
		<c:out value="${forumDiscussionValueBean.repliedDate}" /> <br>
		<cms:contentText key="BY" code="forum.library" /> <a class="profile-popover ellipsis_link"
								data-participant-ids="[${forumDiscussionValueBean.repliedUserId}]"
								href="#" ><c:out value="${forumDiscussionValueBean.repliedBy}" />
		</a>
		</c:when>
		<c:otherwise>
		<cms:contentText key="NO_REPLIES_YET" code="forum.library" />
		</c:otherwise>
		</c:choose>
		</display:column>


		</display:table>
		</div>
		</div>