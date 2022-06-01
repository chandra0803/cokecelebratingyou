<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumTopicValueBean"%>
<%@ page import="com.biperf.core.domain.forum.ForumTopic"%>


<script>
  $(document).ready(function() {

	  G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

	  //Mini Profile PopUp Follow Unfollow Pax JSON
	  G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

        <%
		Map parameterMap = new HashMap();
		ForumTopicValueBean temp;
		%>
        <div class="row">
        <div class="span12">

        <display:table name="forumTopicList" id="forumTopic" class="table table-striped crud-table"
						   pagesize="50" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" >

		<display:setProperty name="basic.msg.empty_list_row">
		        <tr class="crud-content" align="left"><td colspan="{0}">
				      <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
				    </td>
				</tr>
	   </display:setProperty>
	   <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

	   <%-- the name of the topic --%>

		<display:column titleKey="forum.library.TOPICS" class="sortable topicColumn sorted ascending" sortProperty="topicCmAssetCode" sortable="true">
		<%
			temp = (ForumTopicValueBean)pageContext.getAttribute( "forumTopic" );
			parameterMap.put( "topicId", temp.getId() );
			parameterMap.put( "topicCmAssetCode", temp.getTopicCmAssetCode() );
			pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "forumPageDiscussions.do", parameterMap ) );
		%>
			<a href="<c:out value="${editUrl}"/>" class="crud-content-link"> <c:out value="${forumTopic.topicNameFromCM}" /> </a>
		</display:column>


		<%-- the number of discussions in a topic --%>
		<display:column titleKey="forum.library.DISCUSSIONS" sortProperty="discussionCount"  class="sortable discussionCountColumn unsorted" sortable="true">
		<c:out value="${forumTopic.discussionCount}" />
		</display:column>

		<%-- the number of replies for the discussions of a topic --%>
		<display:column titleKey="forum.library.REPLIES" sortProperty="repliesCount"  class="sortable replyCountColumn unsorted" sortable="true">
		<c:out value="${forumTopic.repliesCount}" />
		</display:column>

		<%-- last post for a discussion in a topic --%>
		<display:column titleKey="forum.library.LAST_POST" sortProperty="lastActivityDateString"  class="sortable lastPostColumn unsorted" sortable="true">
		<c:choose>
		<c:when test="${forumTopic.lastActivityDateString != null}">
		<c:out value="${forumTopic.lastActivityDateString}" /> <br>
		<cms:contentText key="BY" code="forum.library" /> <a class="profile-popover ellipsis_link"
								data-participant-ids="[${forumTopic.lastPostUserId}]"
								href="#" ><c:out value="${forumTopic.lastPostUserName}" />
		</a>
		</c:when>
		<c:otherwise>
		<cms:contentText key="NO_POSTS_YET" code="forum.library" />
		</c:otherwise>
		</c:choose>
		</display:column>
        </display:table>
        </div>
        </div>
