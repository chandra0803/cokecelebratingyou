<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@page import="com.biperf.core.domain.enums.PromotionEmailNotificationType"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionNotificationFormBean"%>
<%@ page import="com.biperf.core.utils.MessageUtils"%>

<SCRIPT TYPE="text/javascript">
      function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "block";
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "block";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "block";
        }
      }
      function hideLayer(whichLayer)
      { 
        if (document.getElementById)
        {        
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
        }
        else if (document.all)
        {        
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "none";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "none";
        }
      }
      
  </SCRIPT>

<script type="text/javascript">
  function callUrl( urlToCall )
  {
	window.location=urlToCall;
  }

  function popupPreviewMessagePage(obj)
  {
  var selectObj = findElement(getContentForm(),obj);
    if (selectObj==null || selectObj.options==null)
    {
       return false;
    }

    var selectedOption = null;
    for (var i=0; i<selectObj.options.length; i++) {
 	    if (selectObj.options[i].selected) {
        selectedOption = selectObj.options[i].value
      }
    }

    if ( selectedOption == null || selectedOption <= 0 ){
      alert("There is nothing to preview for this option.");
      return false;
    }

    popUpWin('<%=request.getContextPath()%>/admin/displayPreviewMessage.do?doNotSaveToken=true&messageId='+selectedOption, 'console', 750, 500, false, true);

    return false;

  }
</script>
   
<SCRIPT TYPE="text/javascript"> 
function frequencyChange(idx)
{
 destination = $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"] option:selected').val();
 //do not do anything if the ?Select One? option is selected.
 if (destination) {
  if (destination == 'weekly') 
  {
    showLayer("dayOfWeekLayer"+idx);  
    hideLayer("dayOfMonthLayer"+idx);
  }else if (destination == 'monthly') 
  {
    hideLayer("dayOfWeekLayer"+idx);
    showLayer("dayOfMonthLayer"+idx);  
  } 
  else
  {
	  hideLayer("dayOfWeekLayer"+idx);
	  hideLayer("dayOfMonthLayer"+idx);
  }
  $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled','');
 }else if (destination == ''){
	 $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled','disabled');
	 $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('value','');
 }else{
	 $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled','');
 }
 
}


function fieldsDisable(obj,notificationtype,idx)
{
	<%--If block will execute when the drop down value is select one or no notification --%>
	 if ( obj <= 0 ){
		 <%-- This will disable the frequency field--%>
		if(notificationtype == 'contest_end_notify_creator_do_this_get_that' || notificationtype == 'contest_end_notify_mgr_do_this_get_that' 
				||notificationtype == 'contest_end_notify_pax_do_this_get_that' || notificationtype == 'contest_end_notify_creator_objectives' || notificationtype == 'contest_end_notify_mgr_objectives'
				||notificationtype == 'contest_end_notify_pax_objectives' || notificationtype == 'contest_end_notify_creator_stack_rank' || notificationtype == 'contest_end_notify_mgr_stack_rank'
				||notificationtype == 'contest_end_notify_pax_stack_rank' || notificationtype == 'contest_end_notify_creator_step_it_up' || notificationtype == 'contest_end_notify_mgr_step_it_up'
				|| notificationtype == 'contest_end_notify_pax_step_it_up'|| notificationtype == 'contest_approval_rem_notify_aprvr' || notificationtype == 'contest_final_result_rem_notify_creator'){
			 $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled','disabled');
			 //hideLayer("dayOfWeekLayer"+idx);
			 //hideLayer("dayOfMonthLayer"+idx);
			 $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"] option[value=""]').attr('selected', 'selected');
	    } 
		<%-- disable and clear the textbox--%>
		if(notificationtype == 'contest_end_notify_creator_do_this_get_that' || notificationtype == 'contest_end_notify_mgr_do_this_get_that' 
				||notificationtype == 'contest_end_notify_pax_do_this_get_that' || notificationtype == 'contest_end_notify_creator_objectives' || notificationtype == 'contest_end_notify_mgr_objectives'
				||notificationtype == 'contest_end_notify_pax_objectives' || notificationtype == 'contest_end_notify_creator_stack_rank' || notificationtype == 'contest_end_notify_mgr_stack_rank'
				||notificationtype == 'contest_end_notify_pax_stack_rank' || notificationtype == 'contest_end_notify_creator_step_it_up' || notificationtype == 'contest_end_notify_mgr_step_it_up'
				|| notificationtype == 'contest_end_notify_pax_step_it_up'){
			$('input[name="notificationList['+idx+'].daysBeforeContestEnd" ]').attr('value','');
			$('input[name="notificationList['+idx+'].daysBeforeContestEnd" ]').attr('disabled','disabled');
		}
		if(notificationtype == 'contest_approval_rem_notify_aprvr'){
			$('input[name="notificationList['+idx+'].daysAfterContestCreated" ]').attr('value','');
			$('input[name="notificationList['+idx+'].daysAfterContestCreated" ]').attr('disabled','disabled');
		}
		if(notificationtype == 'contest_final_result_rem_notify_creator'){
			$('input[name="notificationList['+idx+'].daysAfterContestEnded" ]').attr('value','');
			$('input[name="notificationList['+idx+'].daysAfterContestEnded" ]').attr('disabled','disabled');
		}
	}else {
		<%-- enable textbox--%>
		if(notificationtype == 'contest_end_notify_creator_do_this_get_that' || notificationtype == 'contest_end_notify_mgr_do_this_get_that' 
				||notificationtype == 'contest_end_notify_pax_do_this_get_that' || notificationtype == 'contest_end_notify_creator_objectives' || notificationtype == 'contest_end_notify_mgr_objectives'
				||notificationtype == 'contest_end_notify_pax_objectives' || notificationtype == 'contest_end_notify_creator_stack_rank' || notificationtype == 'contest_end_notify_mgr_stack_rank'
				||notificationtype == 'contest_end_notify_pax_stack_rank' || notificationtype == 'contest_end_notify_creator_step_it_up' || notificationtype == 'contest_end_notify_mgr_step_it_up'
				|| notificationtype == 'contest_end_notify_pax_step_it_up'){
			$('input[name="notificationList['+idx+'].daysBeforeContestEnd" ]').attr('disabled','');
		}
		if(notificationtype == 'contest_approval_rem_notify_aprvr'){
			$('input[name="notificationList['+idx+'].daysAfterContestCreated" ]').attr('disabled','');
			$('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled','');
		}
		if(notificationtype == 'contest_final_result_rem_notify_creator'){
			$('input[name="notificationList['+idx+'].daysAfterContestEnded" ]').attr('disabled','');
			$('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled','');
		}
    }
}
</SCRIPT>

<c:set var="disableField" value="${promotionStatus == 'expired' }" />

<html:form styleId="contentForm" action="promotionNotificationSave" >
  <html:hidden property="method"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="notificationListCount"/>
  <html:hidden property="claimFormStepListCount"/>
  <html:hidden property="hasParent"/>
  <html:hidden property="allowActivityUpload"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionNotificationForm.promotionId}"/>
	</beacon:client-state>
  
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	   <td colspan="2">
	    <c:set var="promoTypeName" scope="request" value="${promotionNotificationForm.promotionTypeName}" />
	    <c:set var="promoTypeCode" scope="request" value="${promotionNotificationForm.promotionTypeCode}" />	    
  	    <c:set var="promoName" scope="request" value="${promotionNotificationForm.promotionName}" />
  	    <c:set var="allowActivityUpload" scope="request" value="${promotionNotificationForm.allowActivityUpload}" />
	    <tiles:insert attribute="promotion.header" /> 
	  </td>
	</tr>
    <tr>
	  <td>  	
        <cms:errors/>
      </td>
	</tr>
	
	<tr>
	  <td width="50%" valign="top">
	  <%
	  	boolean drawnCreatorSeperator = false;
	 	boolean drawnApproverSeperator = false;
	  	boolean drawnAwardSeperator = false;
	  	boolean drawnDoThisSeperator = false;
	  	boolean drawnObjectivesSeperator = false;
	  	boolean drawnStackRankSeperator = false;
	  	boolean drawnStepItUpSeperator = false;
	  	boolean allowActivityUpload = ((Boolean)request.getAttribute("allowActivityUpload")).booleanValue();
	  %>
	  
		<table>		
        <nested:iterate id="notificationValue" name="promotionNotificationForm" property="notificationList" indexId="ndx">
          <c:set var="overallDisableField" value="${disableField || promotionNotificationForm.hasParent}" scope="page"/>
          <% boolean overallDisableField = ((Boolean)pageContext.getAttribute("overallDisableField")).booleanValue(); %>
   	      	<nested:hidden property="promotionNotificationId"/>
   	      	<nested:hidden property="version" />  	      
   	      	<nested:hidden property="promotionNotificationType"/>
	      	<nested:hidden property="notificationType"/> 
	      	<nested:hidden property="notificationTypeName"/> 
	      	<nested:hidden property="createdBy" />
		  	<nested:hidden property="dateCreated" />
		  	
		  	  <%
		         PromotionNotificationFormBean notificationValueParam = (PromotionNotificationFormBean) pageContext.getAttribute("notificationValue");
		         Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
				 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode(notificationValueParam.getNotificationType())));
		      %>
		    
		     <!--  Drawing Creator Specific Notifications Seperator -->
	        <%
	          if(!drawnCreatorSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_SETUP_LAUNCH_NOTIFY_CREATOR ) 
	              			|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR ) 
	        	            || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR )
	        	            || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_REMINDER_NOTIFY_CREATOR )
	        		  	    )) {
	        %>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="CREATOR_NOTIFICATIONS" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnCreatorSeperator = true;}%>
 			
			<!--  Drawing Approver Specific Notifications Seperator -->
	        <%if(!drawnApproverSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER ) 
	            || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_APPROVAL_REMINDER_NOTIFY_APPROVER )
	            || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER )
		  	    )) {%>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="APPROVER_NOTIFICATIONS" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnApproverSeperator = true;}%>
			
 			<!--  Drawing Award Them Now Contest seperator -->
	        <%
	          if(!drawnAwardSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_PAX_AWARD_THEM_NOW )
	        	            || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_CREATOR_AWARD_THEM_NOW )
	        	            || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_AWARD_ISSUANCE_TO_MANAGER_AWARD_THEM_NOW )
	        		  	    )) {
	        %>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="AWARD_THEM_NOW_CONTEST" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnAwardSeperator = true;}%>
			
		  	<!--  Drawing Do This Get That Contest seperator -->
		  	<%
		  	  if(!drawnDoThisSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_DO_THIS_GET_THAT ) 
		  			  			  	  	|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_DO_THIS_GET_THAT )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_DO_THIS_GET_THAT )
		  			  			  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT ) && allowActivityUpload )
		  			  			  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT ) && allowActivityUpload )
		  			  			  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT ) && allowActivityUpload )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_DO_THIS_GET_THAT )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_DO_THIS_GET_THAT )
		  			  			  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_DO_THIS_GET_THAT )
		  			  			  	    )) {
		  	%>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="DO_THIS_GET_CONTEST" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnDoThisSeperator = true;}%>
			
			<!--  Drawing Objectives Contest seperator -->
			<%
			  if(!drawnObjectivesSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_OBJECTIVES ) 
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_OBJECTIVES )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_OBJECTIVES )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES ) && allowActivityUpload )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES ) && allowActivityUpload )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES ) && allowActivityUpload )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_OBJECTIVES )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_OBJECTIVES )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_OBJECTIVES )
								  	    )) {
			%>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="OBJECTIVES_CONTEST" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnObjectivesSeperator = true;}%>
			
			<!--  Drawing Stack Rank Contest seperator -->
			<%
			  if(!drawnStackRankSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STACK_RANK ) 
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STACK_RANK )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STACK_RANK )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK ) && allowActivityUpload )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK ) && allowActivityUpload )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK ) && allowActivityUpload )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STACK_RANK )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STACK_RANK )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STACK_RANK )
								  	    )) {
			%>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="STACK_RANK_CONTEST" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnStackRankSeperator = true;}%>
			
			<!--  Drawing Step It Up Contest seperators -->
			<%
			  if(!drawnStepItUpSeperator && (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_CREATOR_STEP_IT_UP ) 
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_MGR_STEP_IT_UP )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_LAUNCH_NOTIFY_PAX_STEP_IT_UP )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP ) && allowActivityUpload )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP ) && allowActivityUpload )
								  	    || (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP ) && allowActivityUpload )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_CREATOR_STEP_IT_UP )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_MGR_STEP_IT_UP )
								  	    || notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_FINAL_RESULT_NOTIFY_PAX_STEP_IT_UP )
								  	    )) {
			%>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			    <u><b><cms:contentText key="STEP_IT_UP_CONTEST" code="promotion.ssi.notifications"/></b></u>
			</td>
			</tr>
			<%drawnStepItUpSeperator = true;}%>
			
			<!-- Create Notification Mail Dropdown -->
			<!--  contest progress notifications should be displayed only if allowActivityUpload is selected -->
			<%if( (notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT ) 
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP )
				|| notificationValueParam.getNotificationType().equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP )
				) ? allowActivityUpload : true){%>
		  	<tr class="form-row-spacer">
		  	  <td valign="top">*</td>
		      <td class="content-field-label" valign="top"><c:out value="${notificationValue.notificationTypeName}" />&nbsp;</td>
		      <td class="content-field" colspan="3">
		        <nested:select property="notificationMessageId" styleClass="content-field content-field-notification-email killme" styleId="notificationMessageId" disabled='<%=overallDisableField%>' onchange="fieldsDisable(this.value, '${notificationValue.notificationType}', '${ndx}' )">
   			      <html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
			    </nested:select>
			  </td>
			  <td>
			    &nbsp;&nbsp;
			     <c:if test="${notificationValue.notificationType != 'goal_selection_survey'}">
			  	<a class="content-link" href="#" onclick="popupPreviewMessagePage('<nested:writeNesting property="notificationMessageId" />');return false;"><cms:contentText code="promotion.notification" key="PREVIEW"/></a>
			    </c:if>
			  </td>
	        </tr>
			<%}%>
          <c:if test="${notificationValue.notificationType == 'contest_end_notify_creator_do_this_get_that' or notificationValue.notificationType == 'contest_end_notify_mgr_do_this_get_that' 
          	or notificationValue.notificationType == 'contest_end_notify_pax_do_this_get_that' or notificationValue.notificationType == 'contest_end_notify_creator_objectives'
          	or notificationValue.notificationType == 'contest_end_notify_mgr_objectives' or notificationValue.notificationType == 'contest_end_notify_pax_objectives'
          	or notificationValue.notificationType == 'contest_end_notify_creator_stack_rank' or notificationValue.notificationType == 'contest_end_notify_mgr_stack_rank'
          	or notificationValue.notificationType == 'contest_end_notify_pax_stack_rank' or notificationValue.notificationType == 'contest_end_notify_creator_step_it_up'
          	or notificationValue.notificationType == 'contest_end_notify_mgr_step_it_up' or notificationValue.notificationType == 'contest_end_notify_pax_step_it_up'}">
  		      <tr class="form-row-spacer">
  	         	 <td colspan="2">&nbsp;</td>
  	          	 <td colspan="3" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      			    <nested:text property="daysBeforeContestEnd" disabled="<%=overallDisableField%>" size="4"/>&nbsp;<cms:contentText code="promotion.ssi.notifications" key="SPECIFY_NO_DAYS_CONTEST_END"/>  
      			</td>
  		      </tr>
          </c:if>
          
          <c:if test="${notificationValue.notificationType == 'contest_approval_rem_notify_aprvr'}">
          	<% String defaultDayOfWeekDisplay = "display:none";%>
	        <c:if test="${notificationValue.promotionNotificationFrequencyType == 'weekly'}">
		    	<% defaultDayOfWeekDisplay = "display:block"; %>
			</c:if>
  		      <tr class="form-row-spacer">
  	         	 <td colspan="2">&nbsp;</td>
  	          	 <td colspan="3" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      			    <nested:text property="daysAfterContestCreated" disabled="<%=overallDisableField%>" size="4"/>&nbsp<cms:contentText code="promotion.ssi.notifications" key="CONTEST_FREQUENCY_NOTIFY"/>
      			</td>
  		      </tr>
  		      <!--  Frequency Row for contest approver -->
  		      <tr class="form-row-spacer">				  
				<beacon:label property="promotionNotificationFrequencyType" required="false" styleClass="content-field-label-top">
				  <cms:contentText key="FREQUENCY" code="process.schedule"/>
				</beacon:label>	
				<td class="content-field">
				  <table border="0" cellpadding="0" cellspacing="0">
					<tr>
					  <td>	
					  	<nested:select styleId="promotionNotificationFrequencyType[${ndx}]" property="promotionNotificationFrequencyType" onchange="frequencyChange('${ndx}')" styleClass="content-field">
				        	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							<html:options collection="frequencyList" property="code" labelProperty="name"  />
						</nested:select>
				      </td>	
					  <td>
					    <DIV id="dayOfWeekLayer${ndx}" style="<%=defaultDayOfWeekDisplay%>">
						  <table>	
						     <tr>
						     	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>						      		 
								<beacon:label property="dayOfWeekType" required="false">
								  <cms:contentText key="DAY_OF_WEEK" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <nested:select property="dayOfWeekType" styleId="dayOfWeekType${ndx}" styleClass="content-field" disabled='<%=overallDisableField%>'>
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfWeekList" property="code" labelProperty="name"  />
								  </nested:select>
								</td>
							 </tr>
						   </table>
						</DIV>
						<DIV id="dayOfMonthLayer${ndx}" style="<%=defaultDayOfWeekDisplay%>">
						  <table>	
						     <tr>
						     	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>						      		 
								<beacon:label property="dayOfMonth" required="false">
								<cms:contentText key="DAY_OF_MONTH" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <nested:select property="dayOfMonth" styleId="dayOfMonth${ndx}" styleClass="content-field" disabled='<%=overallDisableField%>'>
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfMonthList" property="id" labelProperty="name"  />
								  </nested:select>
								</td>
							 </tr>
						   </table>
						</DIV>
					  </td>
					 </tr>
				   </table>
				  </td>
			</tr>
          </c:if>
          
          <!-- Final Results Reminder Frequency to Contest Creator -->
          <c:if test="${notificationValue.notificationType == 'contest_final_result_rem_notify_creator'}">
          	<% String defaultDayOfWeekDisplay = "display:none";%>
	        <c:if test="${notificationValue.promotionNotificationFrequencyType == 'weekly'}">
		    	<% defaultDayOfWeekDisplay = "display:block"; %>
			</c:if>
  		      <tr class="form-row-spacer">
  	         	 <td colspan="2">&nbsp;</td>
  	          	 <td colspan="3" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      			    <nested:text property="daysAfterContestEnded" disabled="<%=overallDisableField%>" size="4"/>&nbsp<cms:contentText code="promotion.ssi.notifications" key="SPECIFY_NO_DAYS_CONTEST_END_CREATOR"/>
      			</td>
  		      </tr>
  		      <!--  Frequency Row for contest approver -->
  		      <tr class="form-row-spacer">				  
				<beacon:label property="promotionNotificationFrequencyType" required="false" styleClass="content-field-label-top">
				  <cms:contentText key="FREQUENCY" code="process.schedule"/>
				</beacon:label>	
				<td class="content-field">
				  <table border="0" cellpadding="0" cellspacing="0">
					<tr>
					  <td>	
					  	<nested:select styleId="promotionNotificationFrequencyType[${ndx}]" property="promotionNotificationFrequencyType" onchange="frequencyChange('${ndx}')" styleClass="content-field">
				        	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
							<html:options collection="frequencyList" property="code" labelProperty="name"  />
						</nested:select>
				      </td>	
					  <td>
					    <DIV id="dayOfWeekLayer${ndx}" style="<%=defaultDayOfWeekDisplay%>">
						  <table>	
						     <tr>
						     	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>						      		 
								<beacon:label property="dayOfWeekType" required="false">
								  <cms:contentText key="DAY_OF_WEEK" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <nested:select property="dayOfWeekType" styleId="dayOfWeekType${ndx}" styleClass="content-field" disabled='<%=overallDisableField%>'>
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfWeekList" property="code" labelProperty="name"  />
								  </nested:select>
								</td>
							 </tr>
						   </table>
						</DIV>
						<DIV id="dayOfMonthLayer${ndx}" style="<%=defaultDayOfWeekDisplay%>">
						  <table>	
						     <tr>
						     	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>						      		 
								<beacon:label property="dayOfMonth" required="false">
								<cms:contentText key="DAY_OF_MONTH" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <nested:select property="dayOfMonth" styleId="dayOfMonth${ndx}" styleClass="content-field" disabled='<%=overallDisableField%>'>
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfMonthList" property="id" labelProperty="name"  />
								  </nested:select>
								</td>
							 </tr>
						   </table>
						</DIV>
					  </td>
					 </tr>
				   </table>
				  </td>
			</tr>
          </c:if>
          
          <!-- Empty Row -->		    
		  <tr class="form-blank-row">
           <td></td>
          </tr>	     
		  </nested:iterate>
		</table>
		
	  </td>
	</tr>
  <tr class="form-row-spacer">
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>  
  </table>
  <script>
 <%-- Where ever the notificationMessageId field's pre-selected value is no notification, it clears the numberOfDays field and disables it.
 and also disables the frequency dropdown --%>
	  $(document).ready(function() {
		
		  var idx = 0;
		  $('select[id^="notificationMessageId"]').each(function() {
			  if ($(this).val() <= "0") {
				  $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled','disabled');
				  $('input[name="notificationList['+idx+'].daysBeforeContestEnd" ]').attr('disabled','disabled');
				  $('input[name="notificationList['+idx+'].daysAfterContestCreated" ]').attr('disabled','disabled');
				  $('input[name="notificationList['+idx+'].daysAfterContestEnded" ]').attr('disabled','disabled');
				  $('input[name="notificationList['+idx+'].daysClaimsPending" ]').attr('disabled','disabled');
			  }else{
				  $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled',false);
				  $('input[name="notificationList['+idx+'].daysBeforeContestEnd" ]').attr('disabled',false);
				  $('input[name="notificationList['+idx+'].daysAfterContestCreated" ]').attr('disabled',false);
				  $('input[name="notificationList['+idx+'].daysAfterContestEnded" ]').attr('disabled',false);
				  $('input[name="notificationList['+idx+'].daysClaimsPending" ]').attr('disabled',false);
				  frequencyChange(idx);
			  }
			  idx=idx+1;
		  });
		  
	  });
  </script>
</html:form>