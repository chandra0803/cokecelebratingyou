<%@ include file="/include/taglib.jspf"%>

<!-- PURL - MANAGER INVITE CONTRIBUTORS TO PURL -->

<!-- =====================================================================
    DATA FORM (java/struts generated)

    METHOD and ACTION from this form will be used on the 'send form' below.

    Values:
        Promo/Node - to be sent with pax search requests
        contributors - pre-selected contributors
        preselectedLocked - is the preselected list editable?

    ====================================================================== -->

<form id="dataForm" class="recognitionDataForm" action="purlInviteContributors.do" method="post">

    <!-- method -->
    <input type="hidden" name="method" value="save" />
    <%@include file="dataForm.jsp" %>

</form><!-- /#dataForm -->
<!-- =============== END - DATA FORM =================== -->



<!-- =============== PAGE CONTENT ========================= -->
<div id="recognitionPageManagerInviteContributorsView" class="recognitionPageManagerInviteContributors-liner page-content">

    <div class="row-fluid"> <!-- Select contributors -->
        <div class="span12">

            <!-- for errors -->
            <div id="serverErrorsContainer" class="alert alert-block alert-error" style="display:none">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <!-- dynamic content from dataForm (struts form) -->
            </div>

            <!-- JSP generated -->
            <div class="purlRecipientInfo">
                <h4><c:out value="${recognitionState.purlRecipient.user.firstName}"/> <c:out value="${recognitionState.purlRecipient.user.lastName}"/></h4>
                <h5>
	                <c:forEach var="formElement" items="${recognitionState.purlRecipient.customElements}">
	                	<c:out value="${formElement.value}"/>
	                </c:forEach>
	                <c:out value="${recognitionState.purlRecipient.promotion.name}"/>
              	</h5>
            </div>
            <!-- /JSP -->

            <!-- SEND FORM
                - this is not a struts form (but it uses the struts form @ the top to set its values)
                - the action of this form will be taken from the #dataForm (struts) above
                - this is the form displayed to the user
                - JAVA: only i18n, and custom form input placeholders (if any) in this form
            -->
            <form id="sendForm" method="post">

                <!-- method - populate from struts form -->
                <input type="hidden" name="method" id="sendFormMethod" />
  				<input type="hidden" name="purlRecipientId" value="${recognitionState.purlRecipientId}" />
  				<input type="hidden" name="purlReturnUrl" value="${recognitionState.purlReturnUrl}" />

                <!-- CONTRIBUTORS (PURL) -->
                <fieldset class="formSection contributorsSection" id="recognitionFieldsetContributors">
                    <div id="contributorsView">
                        <!-- dynamic -->
                    </div>
                </fieldset><!-- /#recognitionFieldsetContributors -->

                <!-- ACTIONS -->
                <fieldset class="form-actions pullBottomUp">
                    <button id="recognitionButtonCancel" class="btn cancelBtn">
                        <cms:contentText key="CANCEL" code="system.button"/>
                    </button>

                    <button class="btn purlBtn backBtn" style="display:none">
                        &laquo; <cms:contentText key="BACK" code="system.button"/>
                    </button>
                    <button class="btn btn-primary purlBtn nextBtn" style="display:none">
                        <cms:contentText key="NEXT" code="system.button"/> &raquo;
                    </button>

                    <button id="recognitionButtonSend" type="submit" name="button" class="btn btn-primary sendBtn">
                        <cms:contentText key="SEND" code="system.button"/>
                    </button>
                </fieldset>

            </form><!-- /#sendForm -->

        </div><!-- /.span12 -->
    </div><!-- eof Select contributors row -->


    <!-- cancel dialog -->
    <div class="inviteContribsCancelDialog" style="display:none">
        <p>
            <b><cms:contentText key="CANCEL_CONTRIBUTION" code="recognition.purl.submit"/></b>
        </p>
        <p>
           <cms:contentText key="CHANGES_DISCARDED" code="system.general"/>
        </p>
        <p class="tc">
            <button type="submit" id="inviteContribsCancelDialogConfirm" class="btn btn-primary btn-small"><cms:contentText key="YES" code="system.button"/></button>
            <button type="submit" id="inviteContribsCancelDialogCancel" class="btn btn-inverse-primary btn-small"><cms:contentText key="NO" code="system.button"/></button>
        </p>
    </div>

</div> <!-- ./recognitionPageManagerInviteContributors-liner -->
<script  type="text/javascript" >

$(document).ready(function() {
	var json;
    // bootstrap json            
    json = {        
		// Client customizations for WIP #26532 starts
		purlAllowOutsideDomains: [${allowedDomains}]
		// Client customizations for WIP #26532 ends
    };
window.rpmicv = new RecognitionPageManagerInviteContributorsView({
    el: $('#recognitionPageManagerInviteContributorsView'),
    $dataForm: $('#dataForm'), //pass a reference to the 'data' form (struts populated)
    pageNav : {
        back : {
            text : '<cms:contentText key="BACK" code="system.button" />',
            url : '${pageContext.request.contextPath}/purl/purlMaintenanceList.do'
        },
        home : {
            text : '<cms:contentText key="HOME" code="system.general" />',
            url : '${pageContext.request.contextPath}/homePage.do'
        }
    },
    cancelUrl : '${recognitionState.purlReturnUrl}',
    pageTitle : '<cms:contentText key="ADD_CONTRIBUTORS" code="recognition.purl.submit"/>',
    json: json // Client customization for WIP #26532 
});
});
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->


<%@include file="/submitrecognition/easy/flipSide.jsp"%>
<%@include file="/search/paxSearchStart.jsp" %>
<script type="text/template" id="wizardTabTpl">
    <%@include file="/include/wizardTab.jsp" %>
</script>