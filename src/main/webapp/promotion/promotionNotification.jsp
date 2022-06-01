<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@page import="java.util.HashMap"%>
<%@page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
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
  function launchProcess(obj,idx)
  {
	var promoId='${promotionNotificationForm.promotionId}';
	//alert('promotion:'+promoId);
  	var selectObj = findElement(getContentForm(),obj);
  	var notificationName=$("#notificationTypeName"+idx).val();
  	//alert('notificationName:'+notificationName);
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
      alert("There is nothing to launch for this option.");
      return false;
    }

    popUpWin('<%=request.getContextPath()%>/admin/launchProcess.do?method=openLaunchProcess&doNotSaveToken=true&messageId='+selectedOption+'&promotionId='+promoId+'&notificationName='+notificationName, 'console', 750, 500, false, true);
    
    
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
		if(notificationtype == 'approver_reminder' || notificationtype == 'pax_inactivity' || notificationtype == 'pax_inactivity_recognition' ||notificationtype == 'pax_inactivity_nomination' 
			|| notificationtype == 'non_redemption_reminder' || notificationtype == 'budget_end' || notificationtype == 'purl_manager_nonresponse' || notificationtype == 'purl_contributor_nonresponse' 
			|| notificationtype == 'participant_kpm_metric_update' || notificationtype == 'manager_kpm_metric_update'	){
			 $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled','disabled');
			 hideLayer("dayOfWeekLayer"+idx);
			 hideLayer("dayOfMonthLayer"+idx);
			 $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"] option[value=""]').attr('selected', 'selected');
	    } 
	    	
		<%-- disable and clear the textbox--%>
		$('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled','disabled');
		$('input[name="notificationList['+idx+'].everyDaysAfterIssuence" ]').attr('disabled','disabled');
		$('input[name="notificationList['+idx+'].numberOfDays" ]').attr('value','');
		$('input[name="notificationList['+idx+'].everyDaysAfterIssuance" ]').attr('value','');
	}else {
		if(notificationtype == 'approver_reminder' || notificationtype == 'pax_inactivity' || notificationtype == 'pax_inactivity_recognition' ||notificationtype == 'pax_inactivity_nomination' 
			|| notificationtype == 'non_redemption_reminder' || notificationtype == 'budget_end' || notificationtype == 'purl_manager_nonresponse' || notificationtype == 'purl_contributor_nonresponse'
			|| notificationtype == 'participant_kpm_metric_update' || notificationtype == 'manager_kpm_metric_update' ){
			$('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled','');
		}
		if(notificationtype != 'approver_reminder'){
			$('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled','');
		}
			$('input[name="notificationList['+idx+'].everyDaysAfterIssuance" ]').attr('disabled','');
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
  <html:hidden property="budgetSweepEnabled"/>
  <html:hidden property="purlEnabled"/>
  <html:hidden property="budgetEnabled"/>
  <html:hidden property="promotionStatus"/>
  <html:hidden property="publicRecPointsEnabled"/>
  <html:hidden property="timePeriodActive"/>
  <html:hidden property="celebrationsEnabled"/>
   
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionNotificationForm.promotionId}"/>
	</beacon:client-state>
  
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	   <td colspan="2">
	    <c:set var="promoTypeName" scope="request" value="${promotionNotificationForm.promotionTypeName}" />
	    <c:set var="promoTypeCode" scope="request" value="${promotionNotificationForm.promotionTypeCode}" />	    
  	    <c:set var="promoName" scope="request" value="${promotionNotificationForm.promotionName}" />
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
		<table>		
      <tiles:insert attribute="promotionNotificationMiddle" />
      <%int count=0; %>
      	  <c:out value=""/>
        <nested:iterate id="notificationValue" name="promotionNotificationForm" property="notificationList" indexId="ndx">
          
          <c:set var="overallDisableField" value="${disableField || promotionNotificationForm.hasParent }" scope="page"/>
        
          
          <%
            boolean overallDisableField = ((Boolean)pageContext.getAttribute("overallDisableField")).booleanValue();
          %>
   	      	<nested:hidden property="promotionNotificationId"/>
   	      	<nested:hidden property="version" />  	      
   	      	<nested:hidden property="promotionNotificationType"/>
	      	<nested:hidden property="notificationType"/> 
	      	<nested:hidden property="notificationTypeName" styleId="notificationTypeName${ndx}"/> 
	      	<nested:hidden property="createdBy" />
		  	<nested:hidden property="dateCreated" />
		  	<nested:hidden property="inactiveAlert" />   	      
     	<c:if test="${notificationValue.notificationType != 'budget_sweep' or ( notificationValue.notificationType == 'budget_sweep' && promotionNotificationForm.budgetSweepEnabled) }">
     	    <c:if test="${!(notificationValue.notificationType == 'pax_inactivity_recognition' and promotionNotificationForm.purlEnabled)}">
     	     <c:if test="${!(notificationValue.notificationType == 'approver_reminder'  or notificationValue.notificationType == 'pax_inactivity'  or notificationValue.notificationType == 'pax_inactivity_recognition'
          		 or notificationValue.notificationType == 'pax_inactivity_nomination' or notificationValue.notificationType == 'non_redemption_reminder'
         		 or ( notificationValue.notificationType == 'budget_end'&& promotionNotificationForm.budgetEnabled )  or ( notificationValue.notificationType == 'budget_reminder'&& promotionNotificationForm.budgetEnabled ) 
         		 or ( promotionNotificationForm.purlEnabled && ( notificationValue.notificationType == 'purl_manager_nonresponse' or notificationValue.notificationType == 'purl_contributor_nonresponse' ) )
         		 or notificationValue.notificationType == 'manager_kpm_metric_update' or notificationValue.notificationType == 'participant_kpm_metric_update') }">
		  	<tr class="form-row-spacer">
		  	  <td valign="top">*</td>
		      <td class="content-field-label" valign="top"><c:out value="${notificationValue.notificationTypeName}" />&nbsp;</td>
		      <%
		         PromotionNotificationFormBean notificationValueParam = (PromotionNotificationFormBean) pageContext.getAttribute("notificationValue");
		         Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
				 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode(notificationValueParam.getNotificationType())));
		      %>
		      <td class="content-field" colspan="3">		        
		        <nested:select property="notificationMessageId" styleClass="content-field content-field-notification-email killme" styleId="notificationMessageId[${ndx}]" disabled='<%=overallDisableField%>' onchange="fieldsDisable(this.value, '${notificationValue.notificationType}', '${ndx}' )">
   			      <html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
			    </nested:select>
			  </td>
			  <td>
			    &nbsp;&nbsp;
			  	<a class="content-link" href="#" onclick="popupPreviewMessagePage('<nested:writeNesting property="notificationMessageId" />');return false;"><cms:contentText code="promotion.notification" key="PREVIEW"/></a>
			  </td>
	        </tr>
	        </c:if>
	        </c:if>
	    </c:if>
	    
	      <c:if test="${notificationValue.notificationType == 'approver_reminder'
          or notificationValue.notificationType == 'pax_inactivity'
          or notificationValue.notificationType == 'manager_kpm_metric_update' 
          or notificationValue.notificationType == 'participant_kpm_metric_update'
          or notificationValue.notificationType == 'pax_inactivity_recognition'
          or notificationValue.notificationType == 'pax_inactivity_nomination'
          or notificationValue.notificationType == 'non_redemption_reminder'
          or ( notificationValue.notificationType == 'budget_end'&& promotionNotificationForm.budgetEnabled ) 
          or ( notificationValue.notificationType == 'budget_reminder'&& promotionNotificationForm.budgetEnabled ) 
          or ( promotionNotificationForm.purlEnabled && ( notificationValue.notificationType == 'purl_manager_nonresponse' or notificationValue.notificationType == 'purl_contributor_nonresponse' ) ) }">
	        <% String defaultDayOfWeekDisplay = "display:none";
	        %>
	        <c:if test="${notificationValue.promotionNotificationFrequencyType == 'weekly'}">
		    	<% defaultDayOfWeekDisplay = "display:block"; %>
			</c:if>      	
			<%if(count==0) {%>
			<tr class="form-row-spacer">
			<td class="content-field" colspan="4">
			  <c:if test="${ promotionNotificationForm.promotionTypeCode != 'engagement' }">
			    <u><b><cms:contentText code="promotion.notification" key="INACTIVITY_NOTIFICATIONS"/></b></u>
			  </c:if>
			</td>
			</tr>
			<%}
			count++;
			%>
			
			<tr class="form-row-spacer">
		  	  <td valign="top">*</td>
		      <td class="content-field-label" valign="top"><c:out value="${notificationValue.notificationTypeName}" />&nbsp;</td>
		      <%
		         PromotionNotificationFormBean notificationValueParam = (PromotionNotificationFormBean) pageContext.getAttribute("notificationValue");
		         Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
				 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode(notificationValueParam.getNotificationType())));
		      %>
		      <td class="content-field" colspan="3">		        
		        <nested:select property="notificationMessageId" styleClass="content-field content-field-notification-email killme" styleId="notificationMessageId[${ndx}]" disabled='<%=overallDisableField%>' onchange="fieldsDisable(this.value, '${notificationValue.notificationType}', '${ndx}' )">
   			      <html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
			    </nested:select>
			  </td>
			  <td>
			    &nbsp;&nbsp;
			  	<a class="content-link" href="#" onclick="popupPreviewMessagePage('<nested:writeNesting property="notificationMessageId" />');return false;"><cms:contentText code="promotion.notification" key="PREVIEW"/></a>
			  </td>
			  <c:if test="${promotionNotificationForm.promotionStatus eq 'live' and  notificationValue.notificationType != 'budget_reminder' and notificationValue.notificationType != 'manager_kpm_metric_update' and notificationValue.notificationType != 'participant_kpm_metric_update'}">
			  <td>
			    &nbsp;&nbsp;
			  	<a class="content-link" href="#" onclick="launchProcess('<nested:writeNesting property="notificationMessageId" />','${ndx}');return false;"><cms:contentText code="promotion.notification" key="LAUNCH_NOW"/></a>
			  </td>
			  </c:if>
	        </tr>
			
			 <c:if test="${notificationValue.notificationType != 'budget_reminder'}">
			<tr class="form-row-spacer">				  
				<beacon:label property="promotionNotificationFrequencyType" required="false" styleClass="content-field-label-top">
				  <cms:contentText key="FREQUENCY" code="process.schedule"/>
				</beacon:label>	
				<td class="content-field">
				  <table border="0" cellpadding="0" cellspacing="0">
					<tr>
					  <td>
					  		<c:choose>
					  		 <c:when test="${notificationValue.notificationType == 'manager_kpm_metric_update' or notificationValue.notificationType == 'participant_kpm_metric_update'}">
					          <cms:contentText code="promotion.notification" key="MONTHLY"/>
					         </c:when>
					         <c:otherwise> 						
							  <nested:select styleId="promotionNotificationFrequencyType[${ndx}]" property="promotionNotificationFrequencyType" onchange="frequencyChange('${ndx}')" styleClass="content-field" disabled='<%=overallDisableField%>'>
				                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
								<html:options collection="frequencyList" property="code" labelProperty="name"  />
							  </nested:select>
						  </c:otherwise>
						  </c:choose>
				      </td>	
					  <td>
					    <DIV id="dayOfWeekLayer${ndx}"  style="<%=defaultDayOfWeekDisplay%>">
						  <table>	
						     <tr>
						     	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>						      		 
								<beacon:label property="dayOfWeekType" required="false">
								  <cms:contentText key="DAY_OF_WEEK" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <nested:select property="dayOfWeekType" styleId="dayOfWeekType${ndx}" styleClass="content-field"  disabled='<%=overallDisableField%>'>
								    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
									<html:options collection="dayOfWeekList" property="code" labelProperty="name"  />
								  </nested:select>
								</td>
							 </tr>
						   </table>
						</DIV>
						<DIV id="dayOfMonthLayer${ndx}"  style="<%=defaultDayOfWeekDisplay%>">
						  <table>	
						     <tr>
						     	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>						      		 
								<beacon:label property="dayOfMonth" required="false">
								<cms:contentText key="DAY_OF_MONTH" code="process.schedule"/>
								</beacon:label>	
								<td class="content-field">
								  <nested:select property="dayOfMonth" styleId="dayOfMonth${ndx}" styleClass="content-field"  disabled='<%=overallDisableField%>'>
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
	      </c:if>
	      
          <c:if test="${notificationValue.notificationType == 'program_end'
          or notificationValue.notificationType == 'approver_reminder_approval_end_date'
          or notificationValue.notificationType == 'approver_reminder_tp_expired'
          or notificationValue.notificationType == 'pax_inactivity'
          or notificationValue.notificationType == 'pax_inactivity_recognition'
          or notificationValue.notificationType == 'pax_inactivity_nomination'          
          or notificationValue.notificationType == 'program_launch'
          or notificationValue.notificationType == 'approver_reminder'
          or notificationValue.notificationType == 'non_redemption_reminder'
          or notificationValue.notificationType == 'next_round'
          or ( notificationValue.notificationType == 'celebration_manager_nonresponse'&& promotionNotificationForm.celebrationsEnabled )
          or ( notificationValue.notificationType == 'budget_sweep'&& promotionNotificationForm.budgetSweepEnabled ) 
          or ( notificationValue.notificationType == 'budget_end'&& promotionNotificationForm.budgetEnabled )
          or ( promotionNotificationForm.purlEnabled && ( notificationValue.notificationType == 'purl_manager_nonresponse' or notificationValue.notificationType == 'purl_contributor_nonresponse' ) ) }">
          <c:if test="${!(notificationValue.notificationType == 'pax_inactivity_recognition' and promotionNotificationForm.purlEnabled)}">
  	        <tr class="form-row-spacer">
  	        
  	        	
  	         <c:if test="${notificationValue.inactiveAlert==true }">
  	         	<td>&nbsp;</td>
  	         	<td class="content-field-label" valign="top"><cms:contentText code="promotion.notification" key="TIMING"/></td>
  	         	<td colspan="3" class="content-field">
  	         </c:if>
  	         
  	         <c:if test="${notificationValue.inactiveAlert==false }">
  	         <td colspan="2">&nbsp;</td>
  	          <td colspan="3" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	          &nbsp;&nbsp;&nbsp;&nbsp;
  	         </c:if>
  	          		<c:if test="${notificationValue.notificationType == 'approver_reminder'}">
      			      <cms:contentText code="promotion.notification" key="REMIND_WHEN"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'non_redemption_reminder'}">
		      			<nested:hidden property="numberOfDays" styleId="numberOfDays${ndx}"/>
		      			    
						<nested:radio property="descriminator" value="every_days_after_issuance" disabled="${displayFlag}"/>
		      			    <cms:contentText code="promotion.notification" key="EVERY"/>
		      			<nested:text property="everyDaysAfterIssuance" disabled="<%=overallDisableField%>" size="4"/>
	      			</c:if>	
	      			    
	      			<c:if test="${notificationValue.notificationType != 'non_redemption_reminder'}">  	
	      				<nested:text property="numberOfDays" styleId="numberOfDays${ndx}" size="4" disabled="<%=overallDisableField%>"/>&nbsp; 
	      			</c:if>     			
		    
      			    <c:if test="${notificationValue.notificationType == 'program_end'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'approver_reminder_tp_expired'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
      			    </c:if>    			    
      			    <c:if test="${notificationValue.notificationType == 'approver_reminder_approval_end_date'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'pax_inactivity' or notificationValue.notificationType == 'pax_inactivity_nomination' }">
      			      <cms:contentText code="promotion.notification" key="DAYS_OF_INACTIVITY"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'pax_inactivity_recognition'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_INACTIVE_RECOGNITION"/>
      			    </c:if>		
      			    <c:if test="${notificationValue.notificationType == 'program_launch'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_LAUNCH"/>
      			    </c:if>	
      			    <c:if test="${notificationValue.notificationType == 'approver_reminder'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PAST_SUBMISSION"/>
      			    </c:if>		
      			    <c:if test="${notificationValue.notificationType == 'non_redemption_reminder'}">
      			    	<cms:contentText code="promotion.notification" key="DAYS_AFTER_ISSUANCE"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'budget_sweep'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_SWEEP_DATE"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'budget_end'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_BUDGET_END_DATE"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'purl_manager_nonresponse'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'purl_contributor_nonresponse'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR"/>
      			    </c:if>	
      			    <c:if test="${notificationValue.notificationType == 'next_round'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_TO_NEXT_MATCH"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'celebration_manager_nonresponse'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_OF_INACTIVITY"/>
      			    </c:if>	      	
      			  </td>
  		      </tr>
  		      <c:if test="${notificationValue.notificationType == 'non_redemption_reminder' && promotionNotificationForm.endSubmissionDate != null }">
  		      <tr class="form-row-spacer">
  	          <td colspan="2">&nbsp;</td>
  	          <td colspan="3" class="content-field">
  	             							
      			    <nested:radio property="descriminator" value="days_after_promo_end" disabled="${displayFlag}"/>
      			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      			    <nested:text property="numberOfDaysAfterPromoEnd" disabled="<%=overallDisableField%>" size="4"/>
      			    	<cms:contentText code="promotion.notification" key="DAYS_AFTER_PROMOTION_END"/>  
      			  </td>
  		      </tr>
  		      </c:if>
          </c:if>  
          </c:if>
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
				  $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled','disabled');
				  $('input[name="notificationList['+idx+'].everyDaysAfterIssuance" ]').attr('disabled','disabled');
				  $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('value','');
				  $('input[name="notificationList['+idx+'].everyDaysAfterIssuance" ]').attr('value','');
			  }else{
				  $('select[name="notificationList['+idx+'].promotionNotificationFrequencyType"]').attr('disabled',false);
				  $('input[name="notificationList['+idx+'].numberOfDays" ]').attr('disabled',false);
				  $('input[name="notificationList['+idx+'].everyDaysAfterIssuance" ]').attr('disabled',false);
				  frequencyChange(idx);
			  }
			  idx=idx+1;
		  });
		  
	  });
  </script>
 
</html:form>