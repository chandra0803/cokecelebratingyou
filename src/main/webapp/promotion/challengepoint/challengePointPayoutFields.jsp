<%@ include file="/include/taglib.jspf"%>

<c:set var="payoutForm" value="${challengePointPayoutForm}"/>

<script type="text/javascript">

function preDisableElements(  )
{
	<c:if test="${payoutForm.awardThreshold != null && payoutForm.awardThreshold == 'none'}">
		disableThresholdSelection();
	</c:if>
}

function handleAwardIncrement()
{
	var contentForm = document.getElementById("contentForm");
  	for (i = 0; i < contentForm.length; i++)
  	{
    	if (contentForm.elements[i].name == 'awardThreshold' &&
    		contentForm.elements[i].value == '' && 
    		contentForm.elements[i].checked == true )
    	{
		  	disableThresholdSelection();
      		break;
    	}
  	}
}

function disableThresholdSelection()
{
	var contentForm = document.getElementById("contentForm");
	for (i = 0; i < contentForm.length; i++)
  	{
    	if (contentForm.elements[i].name == 'awardIncrement' &&
        	contentForm.elements[i].value == 'perthresh' )
    	{
      		contentForm.elements[i].checked = false;
      		contentForm.elements[i].disabled = true;
      		contentForm.awardIncrementPctThreshAmount.value = '';
      		contentForm.awardIncrementPctThreshAmount.disabled = true;
      		break;
    	}
  	}
}


function enableThresholdSelection()
{
	var contentForm = document.getElementById("contentForm");
  	for (i = 0; i < contentForm.length; i++)
  	{
    	if (contentForm.elements[i].name == 'awardIncrement' &&
        	contentForm.elements[i].value == 'perthresh' )
    	{
      		contentForm.elements[i].disabled = false;
      		contentForm.awardIncrementPctThreshAmount.disabled = false;
      		break;
    	}
  	}
}

</script>


<tr class="form-row-spacer"><td></td></tr>
<tr>
	<beacon:label property="awardThreshold" required="true" styleClass="content-field-label-top">
 		<cms:contentText key="AWARD_THRESHOLD" code="promotion.payout.challengepoint"/>
	</beacon:label>
  	<td class="content-field">
		<html:radio property="awardThreshold" value="none" onclick="disableThresholdSelection()" disabled="${displayFlag}"/><cms:contentText code="promotion.payout.challengepoint" key="NOT_APPLICABLE"/>
	</td>
</tr>
<tr>
	<td colspan="2">&nbsp;</td>
	<td class="content-field">
		<html:radio property="awardThreshold" value="fixed" onclick="enableThresholdSelection()" disabled="${displayFlag}"/><cms:contentText code="promotion.payout.challengepoint" key="FIXED_AMOUNT"/>
        &nbsp;&nbsp;<html:text property="awardThresholdFixedAmount" size="10" disabled="${displayFlag}"/>
	</td>
</tr>
<tr>
  	<td colspan="2">&nbsp;</td>
  	<td class="content-field">
    	<html:radio property="awardThreshold" value="perofbase" onclick="enableThresholdSelection()" disabled="${displayFlag}"/><cms:contentText code="promotion.payout.challengepoint" key="PCT_OF_BASE"/>
    	&nbsp;<html:text property="awardThresholdPctAmount" size="5"/>%
 	</td>
</tr> 
<tr class="form-row-spacer"><td></td></tr>         
<tr>
  	<beacon:label property="awardIncrement" required="true" styleClass="content-field-label-top">
    	<cms:contentText key="AWARD_INCREMENT" code="promotion.payout.challengepoint"/>
  	</beacon:label>
  	<td class="content-field">
    	<html:radio property="awardIncrement" value="fixed"  disabled="${displayFlag}"/><cms:contentText code="promotion.payout.challengepoint" key="FIXED_AMOUNT"/>
    	&nbsp;&nbsp;<html:text property="awardIncrementFixedAmount" size="10" disabled="${displayFlag}"/>
  	</td>
</tr>
<tr>
  	<td colspan="2">&nbsp;</td>
  	<td class="content-field">
    	<html:radio property="awardIncrement" value="perofbase" disabled="${displayFlag}" /><cms:contentText code="promotion.payout.challengepoint" key="PCT_OF_BASE"/>
    	&nbsp;&nbsp;<html:text property="awardIncrementPctBaseAmount" size="5" disabled="${displayFlag}"/>%
  	</td>
</tr>  
<tr>
   	<td colspan="2">&nbsp;</td>
   	<td class="content-field">
     	<html:radio property="awardIncrement" value="perthresh"  disabled="${displayFlag}"/><cms:contentText code="promotion.payout.challengepoint" key="PCT_OF_THRESHOLD"/>
     	&nbsp;<html:text property="awardIncrementPctThreshAmount" size="5" disabled="${displayFlag}"/>%
   	</td>
</tr> 
<tr class="form-row-spacer"><td></td></tr>   
<tr>
	<beacon:label property="primaryAwardPerIncrement" required="true" styleClass="content-field-label-top">
     	<cms:contentText key="AWARD_PER_INCREMENT" code="promotion.payout.challengepoint"/>
   	</beacon:label>
   	<td class="content-field">
     	<html:text property="primaryAwardPerIncrement" size="5" disabled="${displayFlag}"/>
   	</td>
</tr>
<tr class="form-row-spacer"><td></td></tr> 

<tr>
	<td>&nbsp;</td>
	<td class="content-field-label"><cms:contentText code="promotion.payout.challengepoint" key="CP_AWARD_TYPE"/></td>
	<td class="content-field-review"><c:out value="${payoutForm.challengepointAwardType}" /></td>
	<html:hidden property="challengepointAwardType"/>
	<html:hidden property="challengePointAwardTypeCode"/>
</tr>                 