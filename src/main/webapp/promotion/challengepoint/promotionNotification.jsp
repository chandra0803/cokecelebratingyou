
<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
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
function frequencyChange()
{
 var selectObj = document.getElementById("promotionNotificationFrequencyType");
 destination = selectObj.options[selectObj.selectedIndex].value;
 //do not do anything if the ?Select One? option is selected.
 if (destination) {
  if (destination == 'weekly') {
    showLayer("dayOfWeekLayer");          
  }else{
    hideLayer("dayOfWeekLayer");
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
           <c:if test="${promotionNotificationForm.promotionTypeCode == 'goalquest' ||   promotionNotificationForm.promotionTypeCode == 'challengepoint' }">
	        <tr class="form-row-spacer">
		  	  <td valign="top"> </td>
		      <td valign="top" class="content-field-label">
		       </td><td class="content-field-label-req">
					*
					</td>
					<td class="content-field-label">
	             <cms:contentText code="promotion.webrules" key="GOAL_SELECTION_SURVEY"/>&nbsp;</td>
		      <td colspan="3" class="content-field">
		         <html:select property="promotionNotificationSurvey"  multiple="multiple" size="3" style="width: 600px" styleClass="killme" styleId="promotionNotificationSurvey"  >
   			      <html:options collection="notificationSurvey" property="id" labelProperty="name" />
			    </html:select>
			  </td>
			  <td>
			  
			  	&nbsp;
			  </td>
	        </tr>
	        
	        
	       </c:if>
	       
	       
	       
	       
        <nested:iterate id="notificationValue" name="promotionNotificationForm" property="notificationList">
          <c:set var="overallDisableField" value="${disableField}" scope="page"/>
          <%
            boolean overallDisableField = ((Boolean)pageContext.getAttribute("overallDisableField")).booleanValue();
          %>
   	      	<nested:hidden property="promotionNotificationId"/>
   	      	<nested:hidden property="version" />  	      
   	      	<nested:hidden property="promotionNotificationType"/>
	      	<nested:hidden property="notificationType"/> 
	      	<nested:hidden property="notificationTypeName"/> 
	      	<nested:hidden property="createdBy" />
		  	<nested:hidden property="dateCreated" />   	      
		  	<tr class="form-row-spacer">
		  	  <td valign="top"></td>
		      <td class="content-field-label" valign="top">
		       <beacon:label property="${notificationValue.notificationTypeName}" required="true">
	            <c:out value="${notificationValue.notificationTypeName}" /></beacon:label>
		      </td>
		      <%
		         PromotionNotificationFormBean notificationValueParam = (PromotionNotificationFormBean) pageContext.getAttribute("notificationValue");
		         Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
				 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode(notificationValueParam.getNotificationType())));
		      %>
		      <td class="content-field" colspan="3">
		        <nested:select property="notificationMessageId" styleClass="content-field content-field-notification-email killme" styleId="notificationMessageId" disabled='<%=overallDisableField%>'>
   			      <html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
			    </nested:select>
			  </td>
			  <td>
			  
			  	<a class="content-link" href="#" onclick="popupPreviewMessagePage('<nested:writeNesting property="notificationMessageId" />');return false;"><cms:contentText code="promotion.notification" key="PREVIEW"/></a>
			  </td>
	        </tr>
	      
          <c:if test="${notificationValue.notificationType == 'program_end'
          or notificationValue.notificationType == 'pax_inactivity'
          or notificationValue.notificationType == 'program_launch'         
          or notificationValue.notificationType == 'goal_not_selected'
          or notificationValue.notificationType == 'challengepoint_not_selected'
          or notificationValue.notificationType == 'cp_non_redemption_reminder'}">
  	        <tr class="form-row-spacer">
  	          <td colspan="2">&nbsp;</td>
  	          <td colspan="2">&nbsp;</td>
  	          <td colspan="3" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	          &nbsp;&nbsp;&nbsp;&nbsp;
  	          		<c:if test="${notificationValue.notificationType == 'cp_non_redemption_reminder'}">
		      			<nested:hidden property="numberOfDays"/>
		      			    
						<nested:radio property="descriminator" value="every_days_after_issuance" disabled="${displayFlag}"/>
		      			    <cms:contentText code="promotion.notification" key="EVERY"/>
		      			<nested:text property="everyDaysAfterIssuance" disabled="<%=overallDisableField%>" size="4"/>
	      			</c:if>	
  	          		
  	          		<c:if test="${notificationValue.notificationType != 'cp_non_redemption_reminder'}">
  	          		  <nested:text property="numberOfDays" size="4" disabled="<%=overallDisableField%>"/>
  	          		</c:if>
  	          		  	          
      			    <c:if test="${notificationValue.notificationType == 'program_end'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_TO_END"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'pax_inactivity'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_OF_INACTIVITY"/>
      			    </c:if>	
      			    <c:if test="${notificationValue.notificationType == 'program_launch'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_LAUNCH"/>
      			    </c:if>
      			    <c:if test="${notificationValue.notificationType == 'goal_not_selected'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_GOAL_END"/>
      			    </c:if>	 
      			     <c:if test="${notificationValue.notificationType == 'challengepoint_not_selected'}">
      			      <cms:contentText code="promotion.notification" key="DAYS_PRIOR_CHALLENGEPOINT_END"/>
      			    </c:if>	 
      			    <c:if test="${notificationValue.notificationType == 'cp_non_redemption_reminder'}">
      			    	<cms:contentText code="promotion.notification" key="DAYS_AFTER_ISSUANCE"/>
      			    </c:if>	 
      			  </td>
  		      </tr>
  		      <c:if test="${notificationValue.notificationType == 'cp_non_redemption_reminder' && promotionNotificationForm.endSubmissionDate != null }">
  		        <tr class="form-row-spacer">
  	            <td colspan="2">&nbsp;</td>
  	            <td colspan="2">&nbsp;</td>
  	            <td colspan="3" class="content-field">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  	            &nbsp;&nbsp;&nbsp;&nbsp;
      			    <nested:radio property="descriminator" value="days_after_promo_end" disabled="${displayFlag}"/>
      			    
      			    <nested:text property="numberOfDaysAfterPromoEnd" disabled="<%=overallDisableField%>" size="4"/>
      			    	<cms:contentText code="promotion.notification" key="DAYS_AFTER_PROMOTION_END"/>  
      			  </td>
  		      </tr>
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
</html:form>