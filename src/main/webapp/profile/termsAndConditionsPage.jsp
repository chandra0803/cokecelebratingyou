<%@ include file="/include/taglib.jspf" %>

<%@include file="/profile/termsAndConditionsSheet.jsp"%>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->

<script>
$(document).ready(function() {
    var fqp = new PageView({
        el:$('#termsPageView'),
        pageNav : {
            back : {
                text : '<cms:contentText key="BACK" code="system.button" />',
                url : '${pageContext.request.contextPath}/homePage.do'
            },
            home : {
                text : '<cms:contentText key="HOME" code="system.general" />',
                url : '${pageContext.request.contextPath}/homePage.do'
            }
        },
        pageTitle : '<cms:contentText key="PAGE_TITLE" code="participant.termsAndConditions"/>'
    });
});
</script>