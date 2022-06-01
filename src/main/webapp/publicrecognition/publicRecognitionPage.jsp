<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div id="publicRecognitionPageView" class="page-content">
<div class="page-topper">
    <!-- Public Recognition Tabs  -->
    <div class="row-fluid">
        <div class="span12">
            <ul class="nav nav-tabs page-top-tabs pubRecTabs">

                <!-- dynamic - pub rec set tabs here -->

            </ul>
        </div>
    </div>
</div><!-- /.page-topper -->
    <!-- View and Edit links only appear on follow tab if user does not have anyone in their follow list. -->
    <ul class="recognition-controls follow-list-links" style="display: none;">
        <beacon:authorize ifNotGranted="LOGIN_AS">
        	<li>
        		<a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/FollowList">
        			<i class="icon-pencil2"></i></i><cms:contentText key="VIEW_EDIT_LIST" code="recognition.public.recognition.item" />
        		</a>
        	</li>
        </beacon:authorize>
    </ul>

    <!-- Recognitions -->
    <div class="page-liner pubRecItemsCont" data-msg-empty="<cms:contentText key="NO_RECOGNITION_FOUND" code="recognition.public.recognition.item" />">

        <div class="publicRecognitionItems">
            <!-- dynamic - pubRecItems -->
        </div>

        <!-- shown when there are more recognitions -->
        <div class="app-row">
            <p>
                <a href="#" class="viewAllRecognitions"  style="display: none">
                    <cms:contentText key="VIEW_MORE" code="recognition.public.recognition.item" />
                </a>
            </p>
        </div>

    </div>


    <!-- If the user doesn't have anyone in their follow list, display a message to add followers and button that links to the follow list page. -->
    <div class="app-slider createFollowListWrapper"  style="display:none">
        <div class="app-row">
			<beacon:authorize ifNotGranted="LOGIN_AS">
            	<h2>
                    <i class="icon-user-add"></i>
                    <cms:contentText key="ADD_PEOPLE_TO_LIST" code="recognition.public.recognition.item" />
                </h2>
                <p>
                    <cms:contentText key="CREATE_OWN_LIST" code="recognition.public.recognition.item" />
                </p>
            	<a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/FollowList" class="btn btn-primary" data-toggle="button createFollowListBtn">
            		<cms:contentText key="CREATE_FOLLOW_LIST" code="recognition.public.recognition.item" />
            	</a>
			</beacon:authorize>
        </div>
    </div>

    <!-- If the user has followees in their follow list but there are no recognitions that are public from those followees to display, then display a message to explain why they don't see anything. -->
    <div class="app-slider noRecognitionsFollowListWrapper"  style="display:none">
        <div class="app-row">

            <p><cms:contentText key="NONE" code="recognition.public.recognition.item" /></p>

        </div>
    </div>

</div><!-- /#publicRecognitionPageView -->



<!-- Instantiate the PageView - expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){

		//Mini Profile PopUp  JSON
		G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';

		//Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
        G5.props.URL_JSON_PUBLIC_RECOGNITION_TRANSLATE=G5.props.URL_ROOT + "recognitionWizard/publicRecognitionTranslate.do?method=translateComment";

		var prp = new PublicRecognitionPageView({el:$('#publicRecognitionPageView'),
            pageTitle : '<cms:contentText key="RECOGNITION_DETAIL" code="recognition.public.recognition.item" />'
        	});
    });

</script>

<script type="text/template" id="publicRecognitionItemTpl">
		<%@include file="/publicrecognition/publicRecognitionItem.jsp"%>
</script>

<script type="text/template" id="sharePopoverTpl">
		<%@include file="/publicrecognition/sharePopover.jsp"%>
</script>

<script type="text/template" id="publicRecognitionCommentTpl">
		<%@include file="/publicrecognition/publicRecognitionComment.jsp"%>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
