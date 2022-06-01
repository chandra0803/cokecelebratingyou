<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.domain.claim.ClaimRecipient" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.client.TcccNominationClaimAwardForm" %>
<script>
	function setDispatchToHome()
	{
	 document.forms[0].action="<%=RequestUtils.getBaseURI(request)%>/participantProfilePage.do#tab/AlertsAndMessages";
	 document.forms[0].submit();
	}      
</script>
<div id="selectPointsOrTrainingView" class="page-content">
    <div class="row">
        <div class="span12">
            <h1 class="selectPointsTitle"><cms:contentText key="TITLE" code="coke.custom.nomination" /></h1>
        </div>
        
        <form class="form-horizontal selectpointsformPage " method="post" name="selectpointsformPage" action="claimAwardNominationSubmitAction.do?method=process">
        <html:hidden property="claimId" value="${tcccNominationClaimAwardForm.claimId}" />
        <html:hidden property="promotionName" value="${tcccNominationClaimAwardForm.promotionName}" />
        <html:hidden property="date" value="${tcccNominationClaimAwardForm.date}" />
    	<html:hidden property="behaviour" value="${tcccNominationClaimAwardForm.behaviour}" />
    	<html:hidden property="comments" value="${tcccNominationClaimAwardForm.comments}" />
    	
    	<html:hidden property="claimItemId" value="${tcccNominationClaimAwardForm.claimItemId}" />
    	
   	
    	<html:hidden property="recipientId" value="${tcccNominationClaimAwardForm.recipientId}" />
    	<html:hidden property="recipientName" value="${tcccNominationClaimAwardForm.recipientName}" />
    	<html:hidden property="recipientAvatar" value="${tcccNominationClaimAwardForm.recipientAvatar}" />
    	<html:hidden property="recipientCountryCode" value="${tcccNominationClaimAwardForm.recipientCountryCode}" />
    	<html:hidden property="recipientCountryName" value="${tcccNominationClaimAwardForm.recipientCountryName}" />
    	<html:hidden property="recipientDeptName" value="${tcccNominationClaimAwardForm.recipientDeptName}" />
    	<html:hidden property="recipientJobName" value="${tcccNominationClaimAwardForm.recipientJobName}" />
    	
    	<html:hidden property="submitterId" value="${tcccNominationClaimAwardForm.submitterId}" />
    	<html:hidden property="submitterName" value="${tcccNominationClaimAwardForm.submitterName}" />
    	<html:hidden property="submitterAvatar" value="${tcccNominationClaimAwardForm.submitterAvatar}" />
    	<html:hidden property="submitterCountryCode" value="${tcccNominationClaimAwardForm.submitterCountryCode}" />
    	<html:hidden property="submitterCountryName" value="${tcccNominationClaimAwardForm.submitterCountryName}" />
    	<html:hidden property="submitterDeptName" value="${tcccNominationClaimAwardForm.submitterDeptName}" />
    	<html:hidden property="submitterJobName" value="${tcccNominationClaimAwardForm.submitterJobName}" />
    
		<html:hidden property="awdQuantity" value="${tcccNominationClaimAwardForm.awdQuantity}" />
    	<html:hidden property="cashAvailable" value="${tcccNominationClaimAwardForm.cashAvailable}" />
    	<html:hidden property="optOut" value="${tcccNominationClaimAwardForm.optOut}" />
    	    	
    	<c:if test="${not empty errors}">
  			<div class="span10 alert alert-block alert-error">
				<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
            	<ul><c:forEach items="${errors}" var="error"><li>${error}</li></c:forEach></ul>
			</div>
  		</c:if>
    	
            <div class="span12">
                	<div class="row">
                   		<div class="span2">
							<p><strong><cms:contentText key="TO" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>
								<a class="profile-popover" href="#" data-participant-ids="[${tcccNominationClaimAwardForm.recipientId}]">
									<img alt="${tcccNominationClaimAwardForm.recipientName}" class="recipient-detail-avatar" src="${tcccNominationClaimAwardForm.recipientAvatar}"> ${tcccNominationClaimAwardForm.recipientName}
								</a> <img class="flag" src="assets/img/flags/${tcccNominationClaimAwardForm.recipientCountryCode}.png" title="${tcccNominationClaimAwardForm.recipientCountryName}">
								${tcccNominationClaimAwardForm.recipientDeptName}, ${tcccNominationClaimAwardForm.recipientJobName}
								
							</p>
						</div>
                	</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="FROM" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>
								<a class="profile-popover" href="#" data-participant-ids="[${tcccNominationClaimAwardForm.submitterId}]">
									<img alt="${tcccNominationClaimAwardForm.submitterName}" class="recipient-detail-avatar" src="${tcccNominationClaimAwardForm.submitterAvatar}"> ${tcccNominationClaimAwardForm.submitterName}
								</a> <img class="flag" src="assets/img/flags/${tcccNominationClaimAwardForm.submitterCountryCode}.png" title="${tcccNominationClaimAwardForm.submitterCountryName}">
								${tcccNominationClaimAwardForm.submitterDeptName}, ${tcccNominationClaimAwardForm.submitterJobName}
							</p>
						</div>
					</div>

              		<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="DATE" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>${tcccNominationClaimAwardForm.date}</p>
						</div>
					</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="BEHAVIOR" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>${tcccNominationClaimAwardForm.behaviour}</p>
						</div>
					</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="COMMENTS" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<!-- do no wrap comment in P tag, it will have its own -->
							${tcccNominationClaimAwardForm.comments}
						</div>
					</div>

                <div class="row">
                    <div class="span6">

                        <h5><cms:contentText key="AWARD_SELECT_INSTRUCTION" code="coke.custom.nomination" /></h5>

                        <div class="control-group type-radio validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;Please choose any one&quot;}">
                            <div class="controls">
                                <table class="table">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <input type="radio" name="award_type_selected" value="points" class="award_type_selected pendingRadio rdochk">
                                            </td>
                                            <td>
                                                <input type="text" name="award_type_selected_points" size="7" value="${tcccNominationClaimAwardForm.awdQuantity}" id="selectPointsValue" class="span1"
                                                    readonly> </td>
                                            <td>
                                                <span class="currency_val"><cms:contentText key="POINTS" code="coke.custom.nomination" /> </span>
                                            </td>

                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="award_type_selected" value="training" class="award_type_selected pendingRadio rdochk">
                                            </td>
                                            <td><cms:contentText key="TRAINING" code="coke.custom.nomination" /> </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <input type="radio" name="award_type_selected" value="decline" class="award_type_selected pendingRadio rdochk">
                                            </td>
                                            <td><cms:contentText key="DECLINE_AWARD" code="coke.custom.nomination" /></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                    <!-- /.span6 -->

                    <div class="span12">
                        <div class="form-actions pullBottomUp">
                            <button href="#" id="confirmButton" class="btn btn-primary" disabled="disabled"><cms:contentText key="CLAIM_AWARD_BTN" code="coke.custom.nomination" /></button>
                            <a href="participantProfilePage.do#tab/AlertsAndMessages" class="btn cancelBtn" ><cms:contentText key="CANCEL" code="coke.custom.nomination" /></a>
                        </div>
                    </div>

                </div> <!-- /.row -->
            </div>

        </form>
    </div> <!-- /.row -->
    <div id="selectPointsConfirm" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <div class="padding text-center"><h2><cms:contentText key="CONFIRMATION" code="coke.custom.nomination" /></h2></div>
        </div>
        <div class="modal-body">					
            <cms:contentText key="REVIEW_INSTRUCTION" code="coke.custom.nomination" />
            <div class="control-group text-center">
                <h5><cms:contentText key="AWARD_SELECTED" code="coke.custom.nomination" /></h5>
                <h4 class="selected_award"></h4>
            </div>	
            <div class="control-group text-center form-actions pullBottomUp">
                <input type="submit" name="submitButton" id="submitConfirm" value="<cms:contentText key="CLAIM_AWARD_BTN" code="coke.custom.nomination" />" class="btn btn-primary"/>
                <input type="button" name="cancelButton" id="cancelConfirm" value="<cms:contentText key="RETURN_TO_PREV" code="coke.custom.nomination" />" class="btn cancelBtn btn-primary"/>
            </div>			
        </div>				              
    </div>
    
    <div id="selectPointsSuccess" class="modal hide fade">
        <div class="modal-header">
            <div class="padding text-center">
                <h2><cms:contentText key="SUCCESS" code="coke.custom.nomination" /></h2>
            </div>
        </div>
        <div class="modal-body">					
            <p class="pointsSuccess hide">
                <cms:contentText key="POINTS" code="coke.custom.nomination" />  <span class="pointsNumber"></span> <cms:contentText key="MSG_POINTS" code="coke.custom.nomination" />
            </p>	
            <p class="trainingSuccess hide">
                <cms:contentText key="MSG_TRAINING" code="coke.custom.nomination" />
            </p>
            <p class="pointsOptout hide">
                <cms:contentText key="MSG_DECLINE" code="coke.custom.nomination" />
            </p>
            <div class="control-group text-center form-actions pullBottomUp">
                
				<html:button  property=""  styleClass="content-buttonstyle" onclick="setDispatchToHome()">
						<cms:contentText code="system.button" key="CLOSE" />
					</html:button>
            </div>			
        </div>				              
    </div>
</div> <!-- /#selectPointsOrTrainingView -->

<script>
    $(document).ready(function () {
    	G5.props.URL_JSON_NOMINATION_RECIPIENT_SELECTION = G5.props.URL_ROOT+'/claimAwardNominationSubmitAction.do?method=process';
    	  // attach the view to an existing DOM element
        window.spot = new SelectPointsOrTrainingView({
            el: $('#selectPointsOrTrainingView'),
            // $dataForm: $('#dataForm'), //pass a reference to the 'data' form (struts populated)
            // formSetup: formSetup, //form setup data
            pageNav: {
                back: {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '${pageContext.request.contextPath}/participantProfilePage.do#tab/AlertsAndMessages'
                },
                home: {
text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle: 'Select Points or Training',
            cancelUrl: 'layout.html'

        });

    });

</script>
