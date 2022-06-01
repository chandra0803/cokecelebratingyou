<!-- ======== SMACK TALK PAGE ======== -->
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div id="smackTalkPageView" class="page-content">

    <div class="page-topper">
        <!-- Public Recognition Tabs  -->
        <div class="row-fluid">
            <div class="span6">
                <ul class="nav nav-tabs page-top-tabs smackTalkTabs">

                    <!-- dynamic - pub rec set tabs here -->

                </ul>
            </div>
            <div class="span6">
                <a href="${pageContext.request.contextPath}/throwdown/viewMatchesDetail.do?method=detail" class="btn btn-primary tdBlueBtn"><cms:contentText key="SMACK_TALK_MATCHES" code="smacktalk.details" /></a>
            </div>
        </div>
    </div><!-- /.page-topper -->

   
    <!-- Recognitions -->
    <div class="page-liner smackTalkItemsCont" data-msg-empty="<cms:contentText key="NO_SMACK_TALK" code="smacktalk.details" />">

        <div class="smackTalkItems">
            <!-- dynamic - smackTalkItems -->
        </div>

        <!-- shown when there are more smack talk itesm -->
        <div class="app-row">
            <p>
                <a href="#" class="viewAllSmackTalks"  style="display: none">
                    <cms:contentText key="VIEW_MORE" code="smacktalk.details" />
                </a>
            </p>
        </div>

    </div>

</div><!-- /#smackTalkPageView -->



<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    
$(document).ready(function(){
	    G5.throwdown.promoId = "${promotionId}";
		//Mini Profile PopUp  JSON
		G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';
		
		//Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
        
		var prp = new SmackTalkPageView({el:$('#smackTalkPageView'),
            pageTitle : '<cms:contentText key="SMACK" code="smacktalk.details" />&nbsp;<cms:contentText key="TALK" code="smacktalk.details" />'
        	});
    });
</script>
	<script type="text/template" id="smackTalkItemTpl">
        <%@include file="/throwdown/smackTalkItem.jsp" %>
	</script>

	<script type="text/template" id="smackTalkCommentTpl">
        <%@include file="/throwdown/smackTalkComment.jsp" %>
	</script>

