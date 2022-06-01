<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- This page will be displayed when the participant is no longer part of promotion audience or recipient is inactive -->

<div id="recognitionPageAddPointsView"  class="page-content publicRecognition public-recognition-page-detail">
	<div class="row-fluid">
        <div class="span12">
            <div class="alert alert-info">
	            <c:choose>
					<c:when test="${ not empty isRecipientInActive}">
		                <h4><cms:contentText key="RECIPIENT_INACTIVE" code="recognitionSubmit.errors"/></h4>
		                <cms:contentText key="RECIPIENT_INACTIVE_ERROR" code="recognitionSubmit.errors"/>
	                </c:when>
	                <c:when test="${ not empty participantNotInPromotion }">
		                <h4><cms:contentText key="NOT_IN_PROMOTION" code="recognitionSubmit.errors"/></h4>
	                	<cms:contentText key="NOT_IN_PROMOTION_ERROR" code="recognitionSubmit.errors"/>
	                </c:when>	
               </c:choose> 
            </div>
        </div>
    </div>
</div>
	

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){

    	window.recognitionPageAddPointsView = new PageView({
            el: $('#recognitionPageAddPointsView'),
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
            pageTitle : '<cms:contentText key="PAGE_TITLE" code="recognition.send.manager"/>'
        });

    });
</script>

