<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== COMMUNICATIONS PAGE ======== -->

<div id="communicationsPageView" class="communicationsPage-liner page-content">
    <p><cms:contentText key="COMMUNICATE" code="diyCommunications.common.labels"/></p>
    <div>
        <a href="participant/manageBanners.do"><cms:contentText key="BANNER" code="diyCommunications.common.labels"/></a>
    </div>
    <div>
        <a href="layout-dev.html?tplPath=../apps/communications/tpl/&tpl=communicationsManageNews.html"><cms:contentText key="NEWS_STORY" code="diyCommunications.common.labels"/></a>
    </div>
    <div>
        <a href="participant/manageResourceCenter.do"><cms:contentText key="RESOURCE_CENTER_CONTENT" code="diyCommunications.common.labels"/></a>
    </div>
    <div>
        <a href="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitAlert.do"><cms:contentText key="SEND_ALERTS" code="diyCommunications.common.labels"/></a>
    </div>
    <div>
        <a href="layout-dev.html?tplPath=../apps/communications/tpl/&tpl=communicationsManageTips.html"><cms:contentText key="TIPS" code="diyCommunications.common.labels"/></a>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

    //attach the view to an existing DOM element
    var cpv = new CommunicationsPageView({
        el:$('#communicationsPageView'),
        pageNav : {
        	 back : {
                 text : '<cms:contentText key="BACK" code="system.button" />',
                 url : 'javascript:history.go(-1);'
             },
             home : {
                 text : '<cms:contentText key="HOME" code="system.general" />',
                 url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
             }
        },
        pageTitle : '<cms:contentText key="COMMUNICATIONS" code="diyCommunications.common.labels"/>'

});
</script>