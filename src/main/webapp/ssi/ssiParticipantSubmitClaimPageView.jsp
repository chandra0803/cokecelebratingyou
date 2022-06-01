<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<div id="ssiParticipantSubmitClaimPageView" class="page-content">
    <div class="row-fluid">
        <div class="span12">
             <h2><cms:contentText key="SUBMIT_CLAIM" code="ssi_contest.participant" /></h2>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <h3>${contestName}</h3>

			<!--JAVA NOTE: Display error text in the below list when page submit returns an error and set the hidden input value to true. -->
			<logic:present name="org.apache.struts.action.ERROR">
				<div id="ssiSubmitClaimErrorBlock" class="alert alert-block alert-error">
					<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
					<ul><html:messages id="error"><li><c:out value="${error}" /></li></html:messages></ul>
				</div>
			</logic:present>

            <form action="participantSubmitClaimSave.do?method=saveClaim" class="ssiPaxSubmitClaimForm" method="POST">
                <fieldset id="ssiClaimFormTpl">
                <!-- dynamic from ssiSubmitClaimFormTpl.html -->
                </fieldset>

                <fieldset class="ssiClaimFormActions">
                    <button class="btn btn-primary ssiClaimFormPreview"><cms:contentText key="PREVIEW" code="system.button" /></button>
                    <button class="btn ssiClaimFormCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
                </fieldset>

                <fieldset class="ssiClaimPreviewView" style="display:none">
                    <dl class="dl-horizontal">
                        <!-- dynamic generated in JS -->
                    </dl>
                </fieldset>

                <fieldset class="ssiClaimPreviewActions" style="display:none">
                    <button type="submit" class="btn btn-primary ssiClaimFormSubmit"><cms:contentText key="SUBMIT" code="system.button" /></button>
                    <button class="btn ssiClaimFormEdit"><cms:contentText key="EDIT" code="system.button" /></button>
                    <button class="btn ssiClaimFormCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
                </fieldset>
            </form>
        </div>
    </div>

    <!-- Confirm Cancel Popover -->
    <div class="ssiClaimCancelConfirm" style="display:none">
        <p>
            <b><cms:contentText key="CLAIM_DETAILS_LOST" code="ssi_contest.participant" /></b>
        </p>
        <p class="tc">
            <button type="submit" class="btn btn-primary ssiClaimDialogConfirm" data-url="participantContestList.do?method=display&id=${contestId}"><cms:contentText key="YES" code="system.button" /></button>
            <button type="button" class="btn ssiClaimDialogCancel"><cms:contentText key="NO" code="system.button" /></button>
        </p>
    </div>

    <!-- modal to catch errors from server on submit OR save draft -->
    <div class="modal hide fade contestErrorsModal">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h3>
                <i class="icon-warning-circle"></i>
                <cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" />
            </h3>
        </div>
        <div class="modal-body">
            <ul class="errorsList">
                <!-- dynamic -->
            </ul>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button" /></a>
        </div>
    </div><!-- /.contestErrorsModal -->
</div><!--/#ssiParticipantSubmitClaimPageView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

    	G5.props.URL_JSON_SSI_SUBMIT_CLAIM_FORM = G5.props.URL_ROOT+'ssi/participantSubmitClaim.do?method=populateClaimFormFields&id=${contestId}';

    	G5.props.URL_JSON_SSI_CLAIM_UPLOAD_DOCUMENT = G5.props.URL_ROOT+'ssi/participantSubmitClaim.do?method=uploadDocument';

        //attach the view to an existing DOM element
        var ssiNav = new SSIParticipantSubmitClaimPageView({
            el: $('#ssiParticipantSubmitClaimPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI( request )%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="CLAIM_TITLE" code="ssi_contest.participant" />'
        });
    });
</script>

<script type="text/template" id="ssiSubmitClaimFormTplTpl">
  <%@include file="ssiSubmitClaimFormTpl.jsp" %>
</script>
