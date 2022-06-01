<%@ include file="/include/taglib.jspf"%>

<!-- ======== FORUM PAGE ======== -->

<div id="forumPageView" class="forumPage-liner page-content">
    <div class="row">
        <div class="span12">
            <ul class="breadcrumbs">
                <li class="forum">
                    <cms:contentText key="TOPIC" code="forum.library" />
                </li>
            </ul>
            
            <h3><cms:contentText key="HEADER" code="forum.library" /></h3>

            <div class="displayTableTopics"></div>
        </div>
    </div>
</div>

<script>
  $(document).ready(function() {
    
	  G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

      G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';
	  
	  //Mini Profile PopUp Follow Unfollow Pax JSON
	  G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
	    
  });
</script> 

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

	   	G5.props.URL_HTML_TOPICS = G5.props.URL_ROOT+'forum/forumTopicsDisplayTableAjaxResponse.do';
	   	
    //attach the view to an existing DOM element
	    var forumPageView = new ForumPageView({

	    	el:$('#forumPageView'),
	        pageTitle : '<cms:contentText key="FORUM" code="forum.library" />'
	    });
	});
</script>
 <%@include file="/submitrecognition/easy/flipSide.jsp"%>