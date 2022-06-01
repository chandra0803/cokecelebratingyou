<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>

<!-- ======== FORUM PAGE DISCUSSIONS ======== -->
<div id="forumPageDiscussionsView" class="forumPage-liner page-content">
    <div class="row">
        <div class="span12">
            <ul class="breadcrumbs">
                <li class="forum">
                    <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumPageTopics.do"><cms:contentText key="TOPIC" code="forum.library" /></a>
                </li>
                <li class="topic active">
                    <%=request.getAttribute( "topicName" )%>
                </li>
            </ul>
            <a href="<c:out value="${startDiscussionUrl}"/>" class="btn btn-primary pull-right"><cms:contentText key="START_DISCUSSION" code="forum.library" /></a>

            <h3><%=request.getAttribute( "topicName" )%></h3>

            <div class="displayTableDiscussions">
                <!-- Dynmaic Content from Display Table-->
            </div>
        </div>
    </div>
</div>

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
        G5.props.URL_HTML_DISCUSSIONS = '<%=request.getAttribute( "discussionsAjaxUrl" )%>';
        //bootstrap topic information
        //attach the view to an existing DOM element
        var forumPageView = new ForumPageView({
            el:$('#forumPageDiscussionsView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="FORUM" code="forum.library" />'
        });
    });

</script>

 <%@include file="/submitrecognition/easy/flipSide.jsp"%>