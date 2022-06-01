<%@ include file="/include/taglib.jspf"%>

<%@include file="/help/faqSheet.jsp"%>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
    var fqp = new PageView({
        el:$('#faqPageView'),
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
        pageTitle : '<cms:contentText key="FAQ" code="system.general" />'
    });
    $(".questionList li a").click(function( event ) {
        event.preventDefault();
        var navbarHeight = $('.slick-track').outerHeight(),
            answerHeading = $( event.target.attributes.href.nodeValue + ' h4')[0],
            answerHeadingMargin = Math.ceil(window.getComputedStyle(answerHeading).getPropertyValue("margin-top").replace(/px/,'')),
            offset = navbarHeight + answerHeadingMargin; //(Height of the navbar + Margin top value of answer heading h4)
        $('html, body').animate({
            scrollTop: $(event.target.attributes.href.nodeValue).offset().top - offset
        }, 100);
    });
});
</script>