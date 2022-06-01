<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="smackTalkModuleTpl">
<!--  SMACK TALK MODULE  -->
<!--
    NOTE:
    - JSON data structure ex: ajax/smackTalk.json
    - JSON data nested thus:
        smackTalkSet-smackTalks-smack-talk-comments-comment
-->
<div class="module-liner">

    <!-- Page Header - Nav Tabs  -->

    <a href="{{smackTalkUrl}}" class="visitAppBtn">
        <i class="icon-arrow-2-circle-right"></i>
    </a>

    <div class="app-row main-nav">

        <div class="app-col">
		<h2 class="moduleTitle"><cms:contentText key="SMACKTALK_DETAIL" code="smacktalk.details" /></h2>
            <ul class="nav nav-tabs smackTalkTabs">

                <!-- dynamic - (tabs are generated in SmackTalkModelView JS) -->

            </ul>

        </div>

    </div>

    <div class="wide-view">

        <!-- Page Body | Employee View -->

        <div class="app-slider smackTalkItemsCont" data-msg-empty="<cms:contentText key="NO_SMACK_TALK" code="smacktalk.details" />" data-empty-url="${pageContext.request.contextPath}/throwdown/viewMatchesDetail.do?method=detail">

            <div class="smackTalkItems">
                <!-- dynamic - smackTalkItems -->
            </div>

            <!-- DO NOT REMOVE FROM DOM - this element needs to be at bottom of recogs -->
            <!-- shown when there are more than n number of smack talk items -->
            <div class="app-row">
                <p>
                    <a href="#" class="viewAllSmackTalks"  style="display:none">
                        <cms:contentText key="VIEW_MORE" code="smacktalk.details" />
                    </a>
                </p>
            </div>

        </div>


    </div> <!-- ./wide-view -->

    <div class="title-icon-view">

         <h3><cms:contentText key="SMACKTALK_DETAIL" code="smacktalk.details" /></h3>

    </div>

</div> <!-- ./module-liner -->
<!--  /SMACK TALK MODULE  -->

</script>
	<script type="text/template" id="smackTalkItemTpl">
        <%@include file="/throwdown/smackTalkItem.jsp" %>
	</script>

	<script type="text/template" id="smackTalkCommentTpl">
        <%@include file="/throwdown/smackTalkComment.jsp" %>
	</script>



