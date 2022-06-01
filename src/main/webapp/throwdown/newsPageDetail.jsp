<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
    
    
    <!-- ======== COMMUNICATIONS PAGE DETAIL ======== -->

<div id="newsPageDetailView" class="newsPageDetail-liner page-content">
<!--dynamic content-->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function(){
	G5.props.URL_JSON_TD_NEWS = '<%=RequestUtils.getBaseURI(request)%>/throwdownNewsDetailResult.do?method=communicationsDetail';
    //attach the view to an existing DOM element
    var cpdv = new ThrowdownNewsPageDetailView({
        el:$('#newsPageDetailView'),
        messageUniqueId : '<%= request.getParameter("messageUniqueId")%>',
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
        pageTitle : '<cms:contentText key="THROWDOWNNEWS" code="hometile.throwdownNews"/>'
    });
});
</script>

 <script type="text/template" id="throwdownNewsPageDetailItemTpl">
        <%@include file="/throwdown/newsPageDetailItem.jsp" %>
 </script>