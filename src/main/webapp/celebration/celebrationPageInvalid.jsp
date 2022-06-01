<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== PURLRECIPIENT: PURL INVALID ======== -->

<div id="CelebrationInvalidView" class="page-content">

    <div class="row">
        <div class="span12">
            <div class="alert alert-block alert-error">
                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                <ul>
                    <li><cms:contentText key="HEADER" code="celebration.page.invalid"/></li>
                </ul>
            </div>
            <cms:errors />
        </div>
    </div>

</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    var pcptac;

    $(document).ready(function(){
        pcptac = new PageView({
            el:$('#CelebrationInvalidView'),
            pageNav : {
                <c:if test="<%=UserManager.isUserLoggedIn()%>">
                    back : {
                        text : '<cms:contentText key="BACK" code="system.button"/>',
                        url : '${pageContext.request.contextPath}/participantProfilePage.do#profile/AlertsAndMessages'
                    },
                    home : {
                        text : '<cms:contentText key="HOME" code="system.general" />',
                        url : '<%=RequestUtils.getBaseURI(request)%>/homePage.do'
                    }
               </c:if>
            },
            pageTitle : '<cms:contentText key="TITLE" code="celebration.page.invalid"/>'
        });
    });
</script>