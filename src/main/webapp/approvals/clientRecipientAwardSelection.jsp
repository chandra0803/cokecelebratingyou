<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.domain.claim.ClaimRecipient" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== Recipient selection DETAIL ======== -->
<div id="recipientSelection" class="page-content">
    <div id="recipientSelectionDetailWrapper">
        <div class="row">
            <div class="span6">
                <h3>${tcccRecognitionClaimAwardForm.promotionName}</h3>
            </div>
        </div>
        <form class="form-horizontal recipientformPage " method="post" name="recipientformPage" action="claimAwardSubmitAction.do?method=process">
        
        <html:hidden property="claimId" value="${tcccRecognitionClaimAwardForm.claimId}" />
        <html:hidden property="promotionName" value="${tcccRecognitionClaimAwardForm.promotionName}" />
        <html:hidden property="date" value="${tcccRecognitionClaimAwardForm.date}" />
    	<html:hidden property="behaviour" value="${tcccRecognitionClaimAwardForm.behaviour}" />
    	<html:hidden property="comments" value="${tcccRecognitionClaimAwardForm.comments}" />
    	<html:hidden property="currencyCode" value="${tcccRecognitionClaimAwardForm.currencyCode}" />
    	<html:hidden property="claimItemId" value="${tcccRecognitionClaimAwardForm.claimItemId}" />
    	
    	<html:hidden property="currencyFullAmt" value="${tcccRecognitionClaimAwardForm.currencyFullAmt}" />
        <html:hidden property="currencyFullAmtString" value="${tcccRecognitionClaimAwardForm.currencyFullAmtString}" />
    	<html:hidden property="currencyHalfAmt" value="${tcccRecognitionClaimAwardForm.currencyHalfAmt}" />
    	<html:hidden property="currencyHalfAmtString" value="${tcccRecognitionClaimAwardForm.currencyHalfAmtString}" />
    	<html:hidden property="pointsFullAmt" value="${tcccRecognitionClaimAwardForm.pointsFullAmt}" />
    	<html:hidden property="pointsHalfAmt" value="${tcccRecognitionClaimAwardForm.pointsHalfAmt}" />
    	
    	<html:hidden property="recipientId" value="${tcccRecognitionClaimAwardForm.recipientId}" />
    	<html:hidden property="recipientName" value="${tcccRecognitionClaimAwardForm.recipientName}" />
    	<html:hidden property="recipientAvatar" value="${tcccRecognitionClaimAwardForm.recipientAvatar}" />
    	<html:hidden property="recipientCountryCode" value="${tcccRecognitionClaimAwardForm.recipientCountryCode}" />
    	<html:hidden property="recipientCountryName" value="${tcccRecognitionClaimAwardForm.recipientCountryName}" />
    	<html:hidden property="recipientDeptName" value="${tcccRecognitionClaimAwardForm.recipientDeptName}" />
    	<html:hidden property="recipientJobName" value="${tcccRecognitionClaimAwardForm.recipientJobName}" />
    	
    	<html:hidden property="submitterId" value="${tcccRecognitionClaimAwardForm.submitterId}" />
    	<html:hidden property="submitterName" value="${tcccRecognitionClaimAwardForm.submitterName}" />
    	<html:hidden property="submitterAvatar" value="${tcccRecognitionClaimAwardForm.submitterAvatar}" />
    	<html:hidden property="submitterCountryCode" value="${tcccRecognitionClaimAwardForm.submitterCountryCode}" />
    	<html:hidden property="submitterCountryName" value="${tcccRecognitionClaimAwardForm.submitterCountryName}" />
    	<html:hidden property="submitterDeptName" value="${tcccRecognitionClaimAwardForm.submitterDeptName}" />
    	<html:hidden property="submitterJobName" value="${tcccRecognitionClaimAwardForm.submitterJobName}" />
    
    	<html:hidden property="cashAvailable" value="${tcccRecognitionClaimAwardForm.cashAvailable}" />
    	<html:hidden property="optOut" value="${tcccRecognitionClaimAwardForm.optOut}" />
  		
  		<c:if test="${not empty errors}">
  			<div class="span10 alert alert-block alert-error">
				<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
            	<ul><c:forEach items="${errors}" var="error"><li>${error}</li></c:forEach></ul>
			</div>
  		</c:if>
  		
			<!-- using styles from recipient Detail -->
			<div class="recipient-item">

				<div class="span12 recipientInformation">
					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="TO" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>
								<a class="profile-popover" href="#" data-participant-ids="[${tcccRecognitionClaimAwardForm.recipientId}]">
									<img alt="${tcccRecognitionClaimAwardForm.recipientName}" class="recipient-detail-avatar" src="${tcccRecognitionClaimAwardForm.recipientAvatar}"> ${tcccRecognitionClaimAwardForm.recipientName}
								</a> <img class="flag" src="assets/img/flags/${tcccRecognitionClaimAwardForm.recipientCountryCode}.png" title="${tcccRecognitionClaimAwardForm.recipientCountryName}">
								${tcccRecognitionClaimAwardForm.recipientDeptName}, ${tcccRecognitionClaimAwardForm.recipientJobName}
								
							</p>
						</div>
					</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="FROM" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>
								<a class="profile-popover" href="#" data-participant-ids="[${tcccRecognitionClaimAwardForm.submitterId}]">
									<img alt="${tcccRecognitionClaimAwardForm.submitterName}" class="recipient-detail-avatar" src="${tcccRecognitionClaimAwardForm.submitterAvatar}"> ${tcccRecognitionClaimAwardForm.submitterName}
								</a> <img class="flag" src="assets/img/flags/${tcccRecognitionClaimAwardForm.submitterCountryCode}.png" title="${tcccRecognitionClaimAwardForm.submitterCountryName}">
								${tcccRecognitionClaimAwardForm.submitterDeptName}, ${tcccRecognitionClaimAwardForm.submitterJobName}
							</p>
						</div>
					</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="DATE" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>${tcccRecognitionClaimAwardForm.date}</p>
						</div>
					</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="BEHAVIOR" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<p>${tcccRecognitionClaimAwardForm.behaviour}</p>
						</div>
					</div>

					<div class="row">
						<div class="span2">
							<p><strong><cms:contentText key="COMMENTS" code="coke.cash.recognition" />:</strong></p>
						</div>
						<div class="span6">
							<!-- do no wrap comment in P tag, it will have its own -->
							${tcccRecognitionClaimAwardForm.comments}
						</div>
					</div>

				</div><!-- /.span8 -->

			</div><!-- /.recipient-item -->

            <div class="row">
                <div class="span6">
                <h5><cms:contentText key="HEADING1" code="coke.cash.recognition" /></h5>
				<div class="control-group type-radio validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;Please choose any one&quot;}">
					<div class="controls">  
						<table class="table">
							<tbody>
								<tr>
									<td><input type="radio" name="awardType" value="points" class="cash_selection pendingRadio rdochk"></td>
									<td><input type="text" name="cash_selection.points" size="7" value="${tcccRecognitionClaimAwardForm.pointsFullAmt}" class="span1" disabled="disabled"></td>
									<td><span class="currency_val"><cms:contentText key="POINTS" code="coke.cash.recognition" /></span></td>
								</tr>
								<c:if test="${tcccRecognitionClaimAwardForm.cashAvailable}">
									<tr>
										<td><input type="radio" name="awardType" value="cash" class="cash_selection pendingRadio rdochk"></td>
										<td><input type="text" name="cash_selection.cash" size="7" value="${tcccRecognitionClaimAwardForm.currencyFullAmtString}" class="span1" disabled="disabled"></td>
										<td><span class="currency_val">${tcccRecognitionClaimAwardForm.currencyCode}</span></td>
									</tr>
									<tr>
										<td><input type="radio" name="awardType" value="cashAndPoints" class="cash_selection pendingRadio rdochk"></td>
										<td><input type="text" name="cash_selection.Points" size="7" value="${tcccRecognitionClaimAwardForm.pointsHalfAmt}" class="span1" disabled="disabled"></td>
										<td><cms:contentText key="POINTS" code="coke.cash.recognition" /></td>
										<td><cms:contentText key="AND" code="coke.cash.recognition" /></td>
										<td><input type="text" name="cash_selection.cash" size="7" value="${tcccRecognitionClaimAwardForm.currencyHalfAmtString}" class="span1" disabled="disabled"></td>
										<td><span class="currency_val">${tcccRecognitionClaimAwardForm.currencyCode}</span></td>
									</tr>
								</c:if>
								<c:if test="${tcccRecognitionClaimAwardForm.optOut}">
									<tr>
										<td><input type="radio" name="awardType" value="optout" class="cash_selection pendingRadio rdochk"></td>
										<td><input type="text" name="cash_selection.nopoints" size="7" value="0" class="span1" disabled="disabled"></td>
										<td><span class="currency_val">No Points</span></td>
									</tr>
								</c:if>
							</tbody>
						</table>
                    </div>
				</div>
                   
                </div><!-- /.span6 -->
				
					<div class="span12">
						<div class="form-actions pullBottomUp">							
							<a href="#" id="confirmButton" class="btn btn-primary"><cms:contentText key="CLAIM_AWARD_BTN" code="coke.cash.recognition" /></a>
							<a href="participantProfilePage.do#tab/AlertsAndMessages" class="btn cancelBtn" ><cms:contentText key="CANCEL" code="coke.cash.recognition" /></a>
						</div>
					</div>
				
            </div><!-- /.row -->			
	
			<div id="recipientConfirm" class="modal hide fade">
				<div class="modal-header">
					<button data-dismiss="modal" class="close" type="button">
						<i class="icon-remove"></i>
					</button>
					<div class="padding text-center"><h2><cms:contentText key="CONFIRMATION" code="coke.cash.recognition" /></h2></div>
				</div>
				<div class="modal-body">					
					<cms:contentText key="CONFIRMATION_TXT" code="coke.cash.recognition" />
					<div class="control-group text-center">
						<h5><cms:contentText key="AWARD_SELD" code="coke.cash.recognition" /></h5>
						<h4 class="selected_award"></h4>
					</div>	
					<div class="control-group text-center">
							<input type="submit" name="submitButton" id="submitButton" value="<cms:contentText key="CONFIRM" code="coke.cash.recognition" />" class="btn btn-primary"/>
							<a href="#" class="btn btn-primary" data-dismiss="modal"><cms:contentText key="CANCEL" code="coke.cash.recognition" /></a>							
					</div>			
				</div>				
			</div>
        </form>
    </div>
</div>


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
	
    //attach the view to an existing DOM element
    aimlp = new RecipientSelectionModelView({
            el:$('#recipientSelection'),            
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '${pageContext.request.contextPath}/participantProfilePage.do#tab/AlertsAndMessages'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="coke.cash.recognition" />'
        });
    });
</script>