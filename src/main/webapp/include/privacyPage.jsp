<%@ include file="/include/taglib.jspf"%>

<%@include file="/include/privacySheet.jsp"%>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
    //attach the view to an existing DOM element
    var fqp = new PageView({
        el:$('#privacyPageView'),
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
        pageTitle : '<cms:contentText key="PRIVACY_POLICY" code="system.general" />'
    });
});
</script>