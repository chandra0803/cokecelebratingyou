<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== NEWS PAGE DETAIL ======== -->

<div id="newsPageDetailView" class="newsPageDetail-liner page-content">
<!--dynamic content-->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

	G5.props.URL_JSON_NEWS = '<%=RequestUtils.getBaseURI(request)%>/newsDetailResult.do?method=newsDetail';
    //attach the view to an existing DOM element
    var cpdv = new NewsPageDetailView({
        el:$('#newsPageDetailView'),
        messageUniqueId : $.query.get('messageUniqueId'),
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
        pageTitle : '<cms:contentText key="COMMUNICATION" code="hometile.communication" />'
    });

});
</script>

 <script type="text/template" id="newsPageDetailItemTpl">
        <%@include file="/news/newsPageDetailItem.jsp" %>
 </script>