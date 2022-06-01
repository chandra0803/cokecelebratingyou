<!-- ======== PARTICIPANT CHATTER AUTHORIZATION PAGE ======== -->
<%@ include file="/include/taglib.jspf" %>

<div id="participantChatterPageView" class="profilePage-liner page-content">

    <div class="row-fluid">
        <div class="span12">

            <logic:present name="org.apache.struts.action.ERROR">
            <!-- JAVA NOTE: in the rare event that the server has an error (FE should catch all errors) -->
            <div class="alert alert-block alert-error">
                <button type="button" class="close" data-dismiss="alert"><i class="icon-close"></i></button>
                <div class="error">
                    <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                    <ul>
                        <html:messages id="error" >
                            <li><c:out value="${error}"/></li>
                        </html:messages>
                    </ul>
                </div>
            </div>
            </logic:present>

            <div class="participantChatterInstructions">
                <p><cms:contentText key="CHATTER_INSTRUCTIONS" code="purl.common" /></p>
            </div>

            <form action="chatterAuthorizationSubmit.do?method=chatterAuthorizationCallback" method="POST" class="participantChatterForm">

                <div class="participantChatterRecognition">
                    <!-- This text gets replaced through js with the "state" key text that comes in through URL -->
                    <p class="currentRecognitionText">
                        <span>"Test Data. This should get swapped out on load"</span>
                        <a href="#linkUrl" target="_blank" class="chatterRecognitionLink">Link Text</a>
                    </p>
                </div>

                <textarea name="commentText" placeholder="Add a comment..."></textarea>

                <div class="formButtons">
                    <button type="submit" id="chatterFormSubmit" name="button" value="" formaction="" class="btn btn-primary"><cms:contentText key="SEND" code="system.button" /></button>
                    <a href="" class="btn chatterFormCancel"><cms:contentText key="CANCEL" code="system.button" /></a>
                </div>
            </form>

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        G5.views.pcav = new ParticipantChatterAuthorizationView({
            el:$('#participantChatterPageView'),
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
            pageTitle : '<cms:contentText key="CHATTER_PREVIEW" code="purl.common" />'
        });
    });

</script>
