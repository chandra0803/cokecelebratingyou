<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumDiscussionValueBean"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>

<!-- ======== FORUM MODULES ======== -->

<script type="text/template" id="forumModuleTpl">
<div class="module-liner">

    <a href="<%=RequestUtils.getBaseURI(request)%>/forum/forumPageTopics.do" class="visitAppBtn">
        <i class="icon-arrow-2-circle-right"></i>
    </a>

    <div class="title-icon-view">
        <h3><cms:contentText key="FORUM" code="forum.library" /></h3>
        <i class="icon-comment-1"></i>
    </div>
    <div class="wide-view">
        <div id="forumCarousel" class="carousel slide">
            <div class="carousel-inner cycle">
            </div>
        </div>
    </div>
</div>
</script>
