<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionAwardsForm" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<SCRIPT TYPE="text/javascript">
function approverChange()
{
	  var e = document.getElementById("approverSearchResultsBox");
	  var user = e.options[e.selectedIndex].text;
	  document.getElementById("approverNameSpan").innerHTML = user;
}
// There are 3 type names: fixed, range, and calculator. 
// The type name provided will have its fields enabled. 
// The others will have their fields cleared and disabled. 
// NOTE: This page has duplicate ID'd elements between each level / final level.  This operates on both.
function awardAmountTypeChanged(typeName, level)
{
	if(typeName == 'fixed')
	{
		$("input[id='eachFixedAmount"+level+"']").removeAttr('disabled');
		$("input[id='finalFixedAmount"+level+"']").removeAttr('disabled');
	}
	else
	{
		$("input[id='eachFixedAmount"+level+"']").attr('disabled', 'disabled');
		$("input[id='eachFixedAmount"+level+"']").val("");
		
		$("input[id='finalFixedAmount"+level+"']").attr('disabled', 'disabled');
		$("input[id='finalFixedAmount"+level+"']").val("");
	}
	
	if(typeName == 'range')
	{
		$("input[id='eachRangeAmountMin"+level+"']").removeAttr('disabled');
		$("input[id='eachRangeAmountMax"+level+"']").removeAttr('disabled');
		$("input[id='eachRangeAmountMax"+level+"'] ~ select").removeAttr('disabled');
		
		$("input[id='finalRangeAmountMin"+level+"']").removeAttr('disabled');
		$("input[id='finalRangeAmountMax"+level+"']").removeAttr('disabled');
		$("input[id='finalRangeAmountMax"+level+"'] ~ select").removeAttr('disabled');
	}
	else
	{
		$("input[id='eachRangeAmountMin"+level+"']").attr('disabled', 'disabled');
		$("input[id='eachRangeAmountMax"+level+"']").attr('disabled', 'disabled');
		$("input[id='eachRangeAmountMax"+level+"'] ~ select").attr('disabled', 'disabled');
		$("input[id='eachRangeAmountMin"+level+"']").val("");
		$("input[id='eachRangeAmountMax"+level+"']").val("");
		$("input[id='eachRangeAmountMax"+level+"'] ~ select").val("");
		
		$("input[id='finalRangeAmountMin"+level+"']").attr('disabled', 'disabled');
		$("input[id='finalRangeAmountMax"+level+"']").attr('disabled', 'disabled');
		$("input[id='finalRangeAmountMax"+level+"'] ~ select").attr('disabled', 'disabled');
		$("input[id='finalRangeAmountMin"+level+"']").val("");
		$("input[id='finalRangeAmountMax"+level+"']").val("");
		$("input[id='finalRangeAmountMax"+level+"'] ~ select").val("");
	}
	
	if(typeName == 'calculator')
	{
		$("select[id='payoutFinalCalc"+level+"']").removeAttr('disabled');
		$("select[id='payoutEachCalc"+level+"']").removeAttr('disabled');
	}
	else
	{
		$("select[id='payoutFinalCalc"+level+"']").attr('disabled', 'disabled');
		$("select[id='payoutEachCalc"+level+"']").attr('disabled', 'disabled');
		$("select[id='payoutFinalCalc"+level+"']").val("");
		$("select[id='payoutEachCalc"+level+"']").val("");
	}
}
	
	function addAnotherSegment(method)
	{
	  document.promotionAwardsForm.method.value=method;
	  document.promotionAwardsForm.action = "promotionAwards.do";
	  document.promotionAwardsForm.submit();
	  return false;
	}
	
	// Collapse the search portion of the 'request more budget' section of the screen, 
	// clearing the fields.
	function hideApproverSection()
	{
		// Clear the current approver, the search field, and the search results
		$("#approverIdHidden").val("");
		$("#approverNameSpan").html("");
		$("#searchApproverLastName").val("");
		$("#approverSearchResultsBox").val("");
		$("#approverSearchResultsBox").html("");
		$("#approverSearchResultsCount").html("0");
		
		// Hide the controls
		hideLayer('budgetRequestTable');
	}
	
	// Show the 'request more budget' section of the screen
	function showApproverSection()
	{
		showLayer('budgetRequestTable');
	}
	
	function addAnotherTimePeriod(method)
	{
  		document.promotionAwardsForm.method.value=method;
  		document.promotionAwardsForm.action = "promotionAwards.do";
  		document.promotionAwardsForm.submit();
  		return false;
	}

	function removeTimePeriod(method)
	{
  		document.promotionAwardsForm.method.value=method;
  		document.promotionAwardsForm.action = "promotionAwards.do";
  		document.promotionAwardsForm.submit();
  		return false;
	}
	
	function updateLayerRemoveTimePeriod(){
		var count = document.promotionAwardsForm.nominationTimePeriodVBListSize.value;
	  	for(i=0; i< count; i++){
		  if( i != 0 ){
			  if( i+1 == count ){
					 showLayer('removeTimePeriod'+i); 
				  }else{
					  hideLayer('removeTimePeriod'+i);
				  }
	  		}else{
	  			 hideLayer('removeTimePeriod0');
	  		}
		  };
	
	}
	
	// When the award size type is multiple people ('team' or 'individual or team') - don't show submission limit columns
	function setTimePeriodColumnVisibility()
	{
		if($("#hideTpSubmissionLimitColumns").val() == "true")
		{
			$(".tp-hide-when-team").hide();
			$(".tp-hide-when-team input").val("");
		}
		else
		{
			$(".tp-hide-when-team").show();
		}
	}
	
	function enableTimePeriod()
	{
		if($("input:radio[name='timePeriodActive']:checked").val()== "true")
  		{
			$('#nominationTimePeriod').show();
  		}
  		else
  		{
  			$('#nominationTimePeriod').hide();
  		}
	}
	
	function enableAwardsPage()
	{
		var promoStatus = $("input[name='live']").val();
		
		var promotionType ='${promotionType}';
		var payoutEachLevelTrueObj = document.getElementById("payoutEachLevelYes");
        var payoutEachLevelFalseObj = document.getElementById("payoutEachLevelNo");
        var payoutFinalLevelTrueObj = document.getElementById("payoutFinalLevelYes");
        var payoutFinalLevelFalseObj = document.getElementById("payoutFinalLevelNo");
        var timePeriodYesObj = document.getElementById("timePeriodActiveYes");
        var timePeriodNoObj = document.getElementById("timePeriodActiveNo");
        
		if (promoStatus == 'true') 
		{
			payoutEachLevelTrueObj.disabled = true;
			payoutEachLevelFalseObj.disabled = true;
			payoutFinalLevelTrueObj.disabled = true;
			payoutFinalLevelFalseObj.disabled = true;
			if(($("input:radio[name='timePeriodActive']:checked").val()== "true") &&(promotionType=='nomination'))
			{
				timePeriodNoObj.disabled = true;
				timePeriodYesObj.disabled = true;
			}
		}
	}
	
	function onPayoutEachLevelChange()
	{
		enablePayoutEachLevel();
		updateRecommendedAwardState();
	}
	
	function onPayoutFinalLevelChange()
	{
		enablePayoutFinalLevel();
		updateRecommendedAwardState();
	}
	
	function disablePayoutEachLevel()
	{
	  		if($("input:radio[name='payoutEachLevel']:checked").val()== "false")
	  		{
			var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
			for(i=1; i<= eachCount; i++){
				hideLayer('blankEachIndex'+i);
				hideLayer('eachLevel'+i);
				hideLayer('eachLevelLabel'+i);
				hideLayer('payoutEachAwardsType'+i);
				hideLayer('payoutEachDescription'+i);
				hideLayer('payoutEachValue'+i);
				hideLayer('payoutEachAwardAmount'+i);
				hideLayer('payoutEachQuantity'+i); 
				hideLayer('payoutEachCalculator'+i);
				}
			}
	  		
	  		if($("input:radio[name='payoutEachLevel']:checked").val()== "true")
	  		{
	  			hideLayer('payoutFinalLevel');
	  			
	  			var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
				for(i=1; i<= finalCount; i++){
					hideLayer('blankFinalIndex'+i);
					hideLayer('finalLevel'+i);
					hideLayer('finalLevelLabel'+i);
					hideLayer('payoutFinalAwardsType'+i);
					hideLayer('payoutFinalAwardAmount'+finalCount);
					hideLayer('payoutFinalCalculator'+finalCount);
					hideLayer('payoutFinalDescription'+finalCount);
					hideLayer('payoutFinalValue'+finalCount);
					hideLayer('payoutFinalQuantity'+finalCount);
				}
				
				var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
			    for(i=1; i<= eachCount; i++){
			    	showLayer('blankEachIndex'+i);
			    	showLayer('eachLevel'+i);
			    	showLayer('eachLevelLabel'+i);
			    	showLayer('payoutEachAwardsType'+i);
			    	var selectBox = document.getElementById('eachAwardsType'+i).value;
			    	if(selectBox == 'none'){
			    		hideLayer('payoutEachAwardAmount'+i);
			    		hideLayer('payoutEachCalculator'+i);
			    	}
			    	else{
			    		showLayer('payoutEachAwardAmount'+i);
				    	showLayer('payoutEachCalculator'+i);
			    	}
			    		
			    	
				}
			}
	}
	
	function enablePayoutEachLevel()
	{
	  		if($("input:radio[name='payoutEachLevel']:checked").val()== "true")
	  		{
	  			hideLayer('payoutFinalLevel');
	  			
	  			var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
				for(i=1; i<= finalCount; i++){
					hideLayer('blankFinalIndex'+i);
					hideLayer('finalLevel'+i);
					hideLayer('finalLevelLabel'+i);
					hideLayer('payoutFinalAwardsType'+i);
					hideLayer('payoutFinalAwardAmount'+i);
					hideLayer('payoutFinalCalculator'+i);
					hideLayer('payoutFinalDescription'+finalCount);
					hideLayer('payoutFinalValue'+finalCount);
					hideLayer('payoutFinalQuantity'+finalCount);
				}
				
				var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
			    for(i=1; i<= eachCount; i++){
			    	showLayer('blankEachIndex'+i);
			    	showLayer('eachLevel'+i);
			    	showLayer('eachLevelLabel'+i);
			    	showLayer('payoutEachAwardsType'+i);
				}
			} 

           if($("input:radio[name='payoutEachLevel']:checked").val()== "false")
	  		{
        	   showLayer('payoutFinalLevel');
	  			
	  			var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
				for(i=1; i<= finalCount; i++){
					hideLayer('blankFinalIndex'+i);
					hideLayer('finalLevel'+i);
					hideLayer('finalLevelLabel'+i);
					hideLayer('payoutFinalAwardsType'+i);
					hideLayer('payoutFinalAwardAmount'+i);
					hideLayer('payoutFinalCalculator'+i);
					hideLayer('payoutFinalDescription'+finalCount);
					hideLayer('payoutFinalValue'+finalCount);
					hideLayer('payoutFinalQuantity'+finalCount);
					//clearPayoutFinalLevelValues( i );
				}
				
				var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
			    for(i=1; i<= eachCount; i++){
			    	hideLayer('blankEachIndex'+i);
			    	hideLayer('eachLevel'+i);
			    	hideLayer('eachLevelLabel'+i);
			    	hideLayer('payoutEachAwardsType'+i);
			    	hideLayer('payoutEachAwardAmount'+i);
					hideLayer('payoutEachCalculator'+i);
					hideLayer('payoutEachDescription'+i);
					hideLayer('payoutEachValue'+i);
					hideLayer('payoutEachQuantity'+i);
					clearPayoutEachLevelValues( i );
				}
			}			
           
    	   // Initial visibility of budget and request more budget section
    	   updateBudgetSectionDisplay();
    	   
    	   // Update visibility of awards are taxable option
    	   updateTaxableDisplay();
	}
	
	function disablePayoutFinalLevel()
	{	  		
	  		if($("input:radio[name='payoutFinalLevel']:checked").val()== "false")
	  		{
	  			var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
	  			if( finalCount == 1 )
	  			{
	  				hideLayer('blankFinalIndex'+finalCount);
	  				hideLayer('finalLevel'+finalCount);
	  				hideLayer('finalLevelLabel'+finalCount);
	  				hideLayer('payoutFinalAwardsType'+finalCount);
					hideLayer('payoutFinalDescription'+finalCount);
					hideLayer('payoutFinalValue'+finalCount);
					hideLayer('payoutFinalAwardAmount'+finalCount);
					hideLayer('payoutFinalQuantity'+finalCount);
					hideLayer('payoutFinalCalculator'+finalCount);
	  			}
	  			else{
	  				for(i=1; i<= finalCount; i++){
						hideLayer('blankFinalIndex'+i);
						hideLayer('finalLevel'+i);
						hideLayer('finalLevelLabel'+i);
						hideLayer('payoutFinalAwardsType'+i);
						hideLayer('payoutFinalDescription'+i);
						hideLayer('payoutFinalValue'+i);
						hideLayer('payoutFinalAwardAmount'+i);
						hideLayer('payoutFinalQuantity'+i); 
						hideLayer('payoutFinalCalculator'+i);
					}
	  			}	
			}
	  		
	  		if($("input:radio[name='payoutFinalLevel']:checked").val()== "true")
	  		{
	  			hideLayer('payoutEachLevel');
				
	  			var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
			    for(i=1; i<= eachCount; i++){
			    	hideLayer('blankEachIndex'+i);
			    	hideLayer('eachLevel'+i);
			    	hideLayer('eachLevelLabel'+i);
			    	hideLayer('payoutEachAwardsType'+i);
			    	hideLayer('payoutEachAwardAmount'+i);
					hideLayer('payoutEachCalculator'+i);
					hideLayer('payoutEachDescription'+i);
					hideLayer('payoutEachValue'+i);
					hideLayer('payoutEachQuantity'+i); 
				}
				
			    var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
				for(i=1; i<= finalCount; i++){
				showLayer('blankFinalIndex'+i);
				showLayer('finalLevel'+i);
				showLayer('finalLevelLabel'+i);
				showLayer('payoutFinalAwardsType'+i);
				showLayer('payoutFinalAwardAmount'+i);
		    	showLayer('payoutFinalCalculator'+i);
				}
			}
		  
	}
	
	function enablePayoutFinalLevel()
	{	  		
	  		if($("input:radio[name='payoutFinalLevel']:checked").val()== "true")
	  		{
	  			hideLayer('payoutEachLevel');
				
	  			var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
			    for(i=1; i<= eachCount; i++){
			    	hideLayer('blankEachIndex'+i);
			    	hideLayer('eachLevel'+i);
			    	hideLayer('eachLevelLabel'+i);
			    	hideLayer('payoutEachAwardsType'+i);
			    	hideLayer('payoutEachAwardAmount'+i);
					hideLayer('payoutEachCalculator'+i);
					hideLayer('payoutEachDescription'+i);
					hideLayer('payoutEachValue'+i);
					hideLayer('payoutEachQuantity'+i); 
				}
				
			    var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
				for(i=1; i<= finalCount; i++){
				showLayer('blankFinalIndex'+i);
				showLayer('finalLevel'+i);
				showLayer('finalLevelLabel'+i);
				showLayer('payoutFinalAwardsType'+i);
				}
			}
			
			if($("input:radio[name='payoutFinalLevel']:checked").val()== "false")
	  		{		
			    var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
				var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
				
				if( eachCount != 1 )
				{
		  			showLayer('payoutEachLevel');
				}
				
			    for(i=1; i<= eachCount; i++){
			    	hideLayer('blankEachIndex'+i);
			    	hideLayer('eachLevel'+i);
			    	hideLayer('eachLevelLabel'+i);
			    	hideLayer('payoutEachAwardsType'+i);
			    	hideLayer('payoutEachAwardAmount'+i);
					hideLayer('payoutEachCalculator'+i);
					hideLayer('payoutEachDescription'+i);
					hideLayer('payoutEachValue'+i);
					hideLayer('payoutEachQuantity'+i);
					//clearPayoutEachLevelValues( i );
				}
			    

				for(i=1; i<= finalCount; i++){
					hideLayer('blankFinalIndex'+i);
					hideLayer('finalLevel'+i);
					hideLayer('finalLevelLabel'+i);
					hideLayer('payoutFinalAwardsType'+i);
					hideLayer('payoutFinalAwardAmount'+i);
					hideLayer('payoutFinalCalculator'+i);
					hideLayer('payoutFinalDescription'+i);
					hideLayer('payoutFinalValue'+i);
					hideLayer('payoutFinalQuantity'+i);
					clearPayoutFinalLevelValues( i );
				}
			}
		  
		   // Initial visibility of budget and request more budget section
		   updateBudgetSectionDisplay();
		   
		   // Update visibility of awards are taxable option
		   updateTaxableDisplay();
	}
	
	// When cumulative approval, recommended award must be false
	// When payout each level, recommended award must be false
	// When payout final level and custom approver first or last level is award, recommended award must be true
	// When payout final level and custom approver middle level is award, recommended award must be false
	function updateRecommendedAwardState()
	{
		if ($("#cumulativeApproval").val() == "true")
		{
			$("#nominatorRecommendedAwardNo").click();
		    $("#nominatorRecommendedAwardNo").removeAttr("disabled");
		    $("#nominatorRecommendedAwardYes").attr("disabled", "disabled");
		}
		else if($("input:radio[name='payoutEachLevel']:checked").val() == "true")
		{
			$("#nominatorRecommendedAwardNo").click();
		    $("#nominatorRecommendedAwardNo").removeAttr("disabled");
		    $("#nominatorRecommendedAwardYes").attr("disabled", "disabled");
		}
		else if($("input:radio[name='payoutFinalLevel']:checked").val() == "true" && ( $("#firstLevelAward").val() == "true" || $("#finalLevelAward").val() == "true" ) )
		{		    
		    $("#nominatorRecommendedAwardNo").attr("disabled", "disabled");
		    $("#nominatorRecommendedAwardYes").click();
		    $("#nominatorRecommendedAwardYes").removeAttr("disabled");
		}
		else if($("input:radio[name='payoutFinalLevel']:checked").val() == "true" && $("#awardSelected").val() == "true")
		{
			$("#nominatorRecommendedAwardNo").click();
		    $("#nominatorRecommendedAwardNo").removeAttr("disabled");
		    $("#nominatorRecommendedAwardYes").attr("disabled", "disabled");
		}
		else
		{
			$("#nominatorRecommendedAwardNo").removeAttr("disabled");
		    $("#nominatorRecommendedAwardYes").removeAttr("disabled");
		}
	}
	
	function clearPayoutEachLevelValues( index )
	{
		document.getElementById('levelLabelEach'+index).value = '';
		document.getElementById('eachAwardsType'+index).value = '';
		document.getElementById('eachFixedAmount'+index).value = '';
    	document.getElementById('eachRangeAmountMin'+index).value = '';
    	document.getElementById('eachRangeAmountMax'+index).value = '';
    	document.getElementById('payoutEachCalc'+index).value = '';
    	document.getElementById('eachPayoutDescription'+index).value = '';
    	document.getElementById('eachPayoutValue'+index).value = '';
    	document.getElementById('eachPayoutCurrency'+index).value = '';
    	document.getElementById('eachQuantity'+index).value = '';
	}
	
	function clearPayoutFinalLevelValues( index )
	{
		document.getElementById('levelLabelFinal'+index).value = '';
		document.getElementById('finalAwardsType'+index).value = '';
		document.getElementById('finalFixedAmount'+index).value = '';
    	document.getElementById('finalRangeAmountMin'+index).value = '';
    	document.getElementById('finalRangeAmountMax'+index).value = '';
    	document.getElementById('payoutFinalCalc'+index).value = '';
    	document.getElementById('finalPayoutDescription'+index).value = '';
    	document.getElementById('finalPayoutValue'+index).value = '';
    	document.getElementById('finalPayoutCurrency'+index).value = '';
    	document.getElementById('finalQuantity'+index).value = '';
	}
	
	function isNumberKey(evt){
	    var charCode = (evt.which) ? evt.which : event.keyCode
	    		if (charCode != 46 && charCode > 31 
	    	            && (charCode < 48 || charCode > 57))
	    	        return false;
	    	    return true;
	    	}
</script>

<% 
    String enableTimePeriod = "true"; 
 %>
 
<c:if test="${promotionAwardsForm.promotionTypeCode == 'nomination'}">
<html:hidden property="nominationPayoutEachLevelListSize"/>
<html:hidden property="oneLevelApproval" styleId="oneLevelApproval"/>
<html:hidden property="cumulativeApproval" styleId="cumulativeApproval"/>
<html:hidden property="firstLevelAward" styleId="firstLevelAward"/>
<html:hidden property="finalLevelAward" styleId="finalLevelAward"/>
<html:hidden property="awardLevelIndex" styleId="awardLevelIndex"/>
<html:hidden property="approverTypeCustom" styleId="approverTypeCustom" />
<html:hidden property="numApprovalLevels" styleId="numApprovalLevels" />
<html:hidden property="hideTpSubmissionLimitColumns" styleId="hideTpSubmissionLimitColumns" />
<html:hidden property="otherLevelsAward"/>
<html:hidden property="customApproverAwardLevel"/>
<html:hidden property="behaviorActive" styleId="behaviorActive" />

<html:hidden property="approverTypeCustom" styleId="approverTypeCustom"/>
<html:hidden property="awardSelected" styleId="awardSelected"/>

 	<%
	  int eachLevelIndex = 1;
 	  PromotionAwardsForm temp = (PromotionAwardsForm)request.getAttribute("promotionAwardsForm");
 	  Long awardLevelCurrentIndex = null;
 	  Long previousAwardLevelIndex = null;
 	  awardLevelCurrentIndex = temp.getAwardLevelIndex();
 	  if ( awardLevelCurrentIndex != null )
 	  {
 		 previousAwardLevelIndex = awardLevelCurrentIndex - 1; 
 	  } 	  
 	  pageContext.setAttribute( "awardLevelCurrentIndex", awardLevelCurrentIndex );
 	  pageContext.setAttribute( "previousAwardLevelIndex", previousAwardLevelIndex ); 
 	  pageContext.setAttribute( "eachLevelIndex", eachLevelIndex ); 
	%>
	<c:forEach var="nominationPayoutEachLevelList" items="${promotionAwardsForm.nominationPayoutEachLevelList}" varStatus="status" >
	<html:hidden property="levelId" name="nominationPayoutEachLevelList" indexed="true"/>
	<html:hidden property = "payoutEachLevel" />
	 	<%
			  	    String awardsTypeCounter = "eachAwardsType" + eachLevelIndex;
 	
			  	    String payoutDescriptionCounter = "eachPayoutDescription" + eachLevelIndex;
					String payoutValueCounter = "eachPayoutValue" + eachLevelIndex;
					String payoutCurrencyCounter = "eachPayoutCurrency" + eachLevelIndex;
					String quantityCounter = "eachQuantity" + eachLevelIndex;
					
					String awardAmountTypeFixedTrueCounter = "eachAwardAmountTypeFixedTrue" + eachLevelIndex;
					String fixedAmountCounter = "eachFixedAmount" + eachLevelIndex;
					
					String awardAmountTypeFixedFalseCounter = "eachAwardAmountTypeFixedFalse" + eachLevelIndex;
					String rangeAmountMinCounter = "eachRangeAmountMin" + eachLevelIndex;
					String rangeAmountMaxCounter = "eachRangeAmountMax" + eachLevelIndex;
					
					String payoutEachAwardsTypeCntr = "payoutEachAwardsType" + eachLevelIndex;
					String payoutEachDescriptionCntr = "payoutEachDescription" + eachLevelIndex;
					String payoutEachValueCntr = "payoutEachValue" + eachLevelIndex;
					String payoutEachAwardAmountCntr = "payoutEachAwardAmount" + eachLevelIndex;
					String payoutEachQuantityCntr = "payoutEachQuantity" + eachLevelIndex;
					
					String blankEachIndexCntr = "blankEachIndex" + eachLevelIndex;
					String eachLevelCntr= "eachLevel" + eachLevelIndex;
					String levelLabel = "Level " + eachLevelIndex + " :";
				    String payoutEachCalculatorCntr = "payoutEachCalculator" + eachLevelIndex;
				    String calculatorAwardAmountTypeFixedFalseCounter = "eachCalAwardAmtTypeFixedFalse" + eachLevelIndex;
				    String calculatorAwardAmountTypeFixedCalCounter = "eachCalAwardAmtTypeFixedCal" + eachLevelIndex;
				    String calculatorEachCounter = "payoutEachCalc" + eachLevelIndex;
				    
				    String eachLevelLabelCntr = "eachLevelLabel" + eachLevelIndex;
				    String levelLabelCounter = "levelLabelEach" + eachLevelIndex;
				    pageContext.setAttribute( "eachLevelIndex", eachLevelIndex ); 
	%>
	
	    <tr class="form-row-spacer" id="<%=eachLevelCntr%>">
	      <td>&nbsp;</td>
	      <td class="content-field-label">
	        <%=levelLabel%>        
	      </td>
	    </tr>
	    
	  <tr class="form-row-spacer" id="<%=eachLevelLabelCntr%>">
  		  <beacon:label property="levelLabel" required="true" styleClass="content-field-label-top">
   			<cms:contentText key="LEVEL_LABEL" code="promotion.basics" />
  		  </beacon:label>
  		 <td colspan=2 class="content-field">  	
  			<html:text styleId="<%=levelLabelCounter%>" indexed="true" property="levelLabel" maxlength="40" size="40" styleClass="content-field" name="nominationPayoutEachLevelList"/>
  		</td>
	  </tr>
	    
	    	    
	    <%-- Award Type --%>
	    <tr class="form-row-spacer" id="<%=payoutEachAwardsTypeCntr%>">
	    <beacon:label property="awardsType" required="true" styleClass="content-field-label-top">
   			<cms:contentText key="TYPE" code="promotion.awards" />
  		  </beacon:label>
	      <td class="content-field">
	      	<c:choose>
		     	<c:when test="${promotionAwardsForm.promotionTypeCode == 'nomination'}" >
	     			<html:select property="awardsType" styleClass="content-field" onchange="eachLevelAwardTypeChange(this);" styleId="<%=awardsTypeCounter%>" indexed="true" name="nominationPayoutEachLevelList">
	     				<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
	     				<c:if test="${promotionAwardsForm.awardsActive == 'true'}">
	     				  <c:choose>	     				 
	     				  <c:when test="${ (promotionAwardsForm.awardSelected == 'true') and  (awardLevelCurrentIndex == eachLevelIndex ) || ( previousAwardLevelIndex == eachLevelIndex ) }">
	     				     <c:forEach var="awardsTypeListRows" items="${customAwardTypeList}" >
	     				  		<html:option value="${awardsTypeListRows.code}"><c:out value="${awardsTypeListRows.name}" /></html:option>	
	     					 </c:forEach>
	     				  </c:when>
	     				  <c:otherwise>
	     				     <c:forEach var="awardsTypeListRows" items="${awardTypeList}" >
	     				  		<html:option value="${awardsTypeListRows.code}"><c:out value="${awardsTypeListRows.name}" /></html:option>	
	     					 </c:forEach>
	     				  </c:otherwise>
	     				  </c:choose>
	     				</c:if>
	     			  <c:if test="${promotionAwardsForm.awardsActive == 'false'}">
	     				<c:forEach var="awardsTypeListRows" items="${awardTypeListAwardsInactive}" >
	     				  <html:option value="${awardsTypeListRows.code}"><c:out value="${awardsTypeListRows.name}" /></html:option>	
	     				</c:forEach>
	     				</c:if>
					</html:select>
					<html:hidden property="awardsType" indexed="true" name="nominationPayoutEachLevelList" />
	     		</c:when>
	     		<c:otherwise>
	        		<c:out value="${nominationPayoutEachLevelList.awardsTypeDesc}" />
		     	</c:otherwise>
		  	</c:choose>
	      </td>
	    </tr>
	    

		  
      <%-- Start payout Award Amount --%>
      <tr class="form-row-spacer" id="<%=payoutEachAwardAmountCntr%>">
         <beacon:label property="awardAmountTypeFixed" required="true" styleClass="content-field-label-top">
             <div id="<%=payoutEachAwardAmountCntr%>-points-label">
             	<cms:contentText key="AMOUNT" code="promotion.awards" />
             </div>
             <div id="<%=payoutEachAwardAmountCntr%>-usd-label">
             	<cms:contentText key="AMOUNT_USD" code="promotion.awards" />
             </div>
             <html:hidden property="approvalType" value="${promotionAwardsForm.approvalType}"  />
         </beacon:label>
         
         <td>
           <table>
              <tr>
                <td class="content-field" valign="top">
                   <html:radio styleId="<%=awardAmountTypeFixedTrueCounter%>" indexed="true" property="awardAmountTypeFixed" 
        				value="true" disabled="${displayFlag}" onclick="awardAmountTypeChanged('fixed', '${status.count}');" name="nominationPayoutEachLevelList"/>
          		   <cms:contentText code="promotion.awards" key="AMOUNT_FIXED" /> &nbsp;
          		   <html:text styleId="<%=fixedAmountCounter%>" indexed="true" property="fixedAmount" size="5" styleClass="content-field" disabled="${displayFlag}" name="nominationPayoutEachLevelList"  onkeypress="return isNumberKey(event)"/>
        		</td>
      		</tr>
      
      		<tr>
        		<td class="content-field" valign="top">
          			<html:radio styleId="<%=awardAmountTypeFixedFalseCounter%>" indexed="true" property="awardAmountTypeFixed" value="false" 
          				disabled="${displayFlag}" onclick="awardAmountTypeChanged('range', '${status.count}');" name="nominationPayoutEachLevelList"/>
          			<cms:contentText code="promotion.awards" key="AMOUNT_RANGE" />
          			<cms:contentText code="promotion.awards" key="AMOUNT_BETWEEN" /> &nbsp;
          			<html:text styleId="<%=rangeAmountMinCounter%>" indexed="true" property="rangeAmountMin" size="5" styleClass="content-field" disabled="${displayFlag}" name="nominationPayoutEachLevelList" onkeypress="return isNumberKey(event)"/>
          			<cms:contentText code="promotion.awards" key="AMOUNT_AND" />&nbsp;
          			<html:text styleId="<%=rangeAmountMaxCounter%>" indexed="true" property="rangeAmountMax" size="5" styleClass="content-field" disabled="${displayFlag}" name="nominationPayoutEachLevelList" onkeypress="return isNumberKey(event)"/>
        		</td>
      		</tr>
      	   
      	   <%-- Calculator --%>			
			<tr id="<%=payoutEachCalculatorCntr%>">
				<td class="content-field" valign="top">
				<c:choose>
					<c:when test="${! empty nominationPayoutEachLevelList.eachLevelCalculatorId }">
						<html:radio styleId="<%=calculatorAwardAmountTypeFixedFalseCounter%>"
							property="awardAmountTypeFixed" value="cal" name="nominationPayoutEachLevelList" indexed="true"
							disabled="${displayFlag or (promotionAwardsForm.cumulativeApproval=='true')}"   onclick="awardAmountTypeChanged('calculator', '${status.count}');"/>
					</c:when>
					<c:otherwise>
						<html:radio styleId="<%=calculatorAwardAmountTypeFixedCalCounter%>"
							property="awardAmountTypeFixed" value="cal" name="nominationPayoutEachLevelList" indexed="true"
							disabled="${displayFlag or (promotionAwardsForm.cumulativeApproval=='true')}"   onclick="awardAmountTypeChanged('calculator', '${status.count}');"/>
					</c:otherwise>
				</c:choose>
				 <cms:contentText code="promotion.awards" key="NOMINATION_AWARD_CALCULATOR" />	
				<BR>
				<cms:contentText code="promotion.awards" key="CALCULATOR_TYPE" />
				    &nbsp;<html:select styleId="<%=calculatorEachCounter%>" indexed="true" property="eachLevelCalculatorId" styleClass="content-field" name="nominationPayoutEachLevelList" disabled="${displayFlag or (promotionAwardsForm.cumulativeApproval=='true')}">
              		   <html:option value=''>
                		  <cms:contentText key="CHOOSE_ONE" code="system.general" />
              		   </html:option>
              		   <c:forEach var="calculatorListRows" items="${nominationPayoutEachLevelList.calculatorList}" >
	     				  <html:option value="${calculatorListRows.id}"><c:out value="${calculatorListRows.name}" /></html:option>	
	     				</c:forEach>
            		</html:select><br><br>
			  </td>
			</tr>			
			<%-- Calculator --%>
      		
    	  </table>
  		</td>
	</tr> 
	
		    <%--START payout description and value --%>
	        
		<tr class="form-row-spacer" id="<%=payoutEachDescriptionCntr%>">
  		    <beacon:label property="payoutDescription" required="true" styleClass="content-field-label-top">
   			   <cms:contentText key="PAYOUT_DESCRIPTION" code="promotion.basics" />
  			</beacon:label>
  			<td colspan=2 class="content-field">  	
  				<html:text styleId="<%=payoutDescriptionCounter%>" indexed="true" property="payoutDescription" maxlength="50" size="50" styleClass="content-field" name="nominationPayoutEachLevelList"/>
  			</td>
		</tr>
		
	    <tr class="form-row-spacer" id="<%=payoutEachValueCntr%>">
  			<beacon:label property="payoutValue" required="true" styleClass="content-field-label-top">
   				<cms:contentText key="PAYOUT_VALUE" code="promotion.basics" />
  			</beacon:label>
  			<td colspan=2 class="content-field">
  				
		   		<html:text styleId="<%=payoutValueCounter%>" indexed="true" property="payoutValue" maxlength="10" size="10" styleClass="content-field" name="nominationPayoutEachLevelList"/>
  				<html:select styleId="<%=payoutCurrencyCounter%>" indexed="true" property="payoutCurrency" styleClass="content-field" name="nominationPayoutEachLevelList" >
				   <html:option value=''><cms:contentText key="CHOOSE_CURRENCY_TYPE" code="system.general"/></html:option>	
				   <html:options collection="currencies" property="currencyCode" labelProperty="currencyName"  />
			    </html:select>
  			</td>
		</tr>		 

	   <%--STOP payout description and value --%>
		  
	   <%--START quantity and value --%>
			
	   <tr class="form-row-spacer" id="<%=payoutEachQuantityCntr%>">
  		  <beacon:label property="quantity" styleClass="content-field-label-top">
   			<cms:contentText key="QUANTITY" code="promotion.basics" />
  		  </beacon:label>
  		 <td colspan=2 class="content-field">  	
  			<html:text styleId="<%=quantityCounter%>" indexed="true" property="quantity" maxlength="10" size="10" styleClass="content-field" name="nominationPayoutEachLevelList"/>
  		</td>
	  </tr>
	  
	  
		  <%--STOP quantity and value --%> 
	
	<tr class="form-blank-row" id=<%=blankEachIndexCntr%> ><td colspan="3">&nbsp;</td></tr>	    
	   <% eachLevelIndex = eachLevelIndex + 1; %> 
	</c:forEach>

</c:if>

<c:if test="${promotionAwardsForm.promotionTypeCode == 'nomination'}"> 
<html:hidden property="nominationPayoutFinalLevelListSize"/>
    	<%
	  	int finalLevelIndex = 1;
	    %>
	<c:forEach var="nominationPayoutFinalLevelList" items="${promotionAwardsForm.nominationPayoutFinalLevelList}" varStatus="finalPayoutCount" >
	<html:hidden property="levelId" name="nominationPayoutFinalLevelList" indexed="true"/>
	<html:hidden property = "payoutFinalLevel" />
	  
      	<%
			  	    String awardsTypeCounter = "finalAwardsType" + finalLevelIndex;
 	
			  	    String payoutDescriptionCounter = "finalPayoutDescription" + finalLevelIndex;
					String payoutValueCounter = "finalPayoutValue" + finalLevelIndex;
					String payoutCurrencyCounter = "finalPayoutCurrency" + finalLevelIndex;
					String quantityCounter = "finalQuantity" + finalLevelIndex;
					
					String awardAmountTypeFixedTrueCounter = "finalAwardAmountTypeFixedTrue" + finalLevelIndex;
					String fixedAmountCounter = "finalFixedAmount" + finalLevelIndex;
					
					String awardAmountTypeFixedFalseCounter = "finalAwardAmountTypeFixedFalse" + finalLevelIndex;
					String rangeAmountMinCounter = "finalRangeAmountMin" + finalLevelIndex;
					String rangeAmountMaxCounter = "finalRangeAmountMax" + finalLevelIndex;
					
					String payoutFinalAwardsTypeCntr = "payoutFinalAwardsType" + finalLevelIndex;
					String payoutFinalDescriptionCntr = "payoutFinalDescription" + finalLevelIndex;
					String payoutFinalValueCntr = "payoutFinalValue" + finalLevelIndex;
					String payoutFinalAwardAmountCntr = "payoutFinalAwardAmount" + finalLevelIndex;
					String payoutFinalQuantityCntr = "payoutFinalQuantity" + finalLevelIndex;
					
					String blankFinaIndexCntr = "blankFinalIndex" + finalLevelIndex;
					String finalLevelCntr= "finalLevel" + finalLevelIndex;
					String finalLevelLabel = "Level " + finalLevelIndex + " :";
				    String payoutFinalCalculatorCntr = "payoutFinalCalculator" + finalLevelIndex;
				    String calculatorAwardAmountTypeFixedFalseCounter = "finalCalAwardAmtTypeFixedFalse" + finalLevelIndex;
				    String calculatorAwardAmountTypeFixedCalCounter = "finalCalAwardAmtTypeFixedCal" + finalLevelIndex;
				    String calculatorFinalCounter = "payoutFinalCalc" + finalLevelIndex;
				    
				    String finalLevelLabelCntr = "finalLevelLabel" + finalLevelIndex;
				    String levelLabelCounter = "levelLabelFinal" + finalLevelIndex;
	%>
	  
	  <tr class="form-row-spacer" id="<%=finalLevelCntr%>">
	    <td>&nbsp;</td>
	     <td class="content-field-label">
	       Level ${promotionAwardsForm.numApprovalLevels}:
	      </td>
	  </tr>
	  
	  <tr class="form-row-spacer" id="<%=finalLevelLabelCntr%>">
  		  <beacon:label property="levelLabel" required="false" styleClass="content-field-label-top">
   			<cms:contentText key="LEVEL_LABEL" code="promotion.basics" />
  		  </beacon:label>
  		 <td colspan=2 class="content-field">  	
  			<html:text styleId="<%=levelLabelCounter%>" indexed="true" property="levelLabel" maxlength="40" size="40" styleClass="content-field" name="nominationPayoutFinalLevelList"/>
  		</td>
	  </tr>
	  
	  <%-- Award Type --%>
	  <tr class="form-row-spacer" id="<%=payoutFinalAwardsTypeCntr%>">
	    <beacon:label property="awardsType" required="true" styleClass="content-field-label-top">
   			<cms:contentText key="TYPE" code="promotion.awards" />
  		  </beacon:label>
	    <td class="content-field">
	    	<c:choose>
		       <c:when test="${promotionAwardsForm.promotionTypeCode == 'nomination'}" >
	     		  <html:select property="awardsType" styleClass="content-field" onchange="finalLevelAwardTypeChange(this);" styleId="<%=awardsTypeCounter%>" indexed="true" name="nominationPayoutFinalLevelList">
	     			<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
	     			<c:if test="${promotionAwardsForm.awardsActive == 'true'}">	
	     			  <c:choose>
	     			  <c:when test="${ (promotionAwardsForm.awardSelected == 'true') }">
	     			    <c:forEach var="awardsTypeListRows" items="${customAwardTypeList}" >
	     			      <html:option value="${awardsTypeListRows.code}"><c:out value="${awardsTypeListRows.name}" /></html:option>	
	     			    </c:forEach>
	     			  </c:when>
	     			  <c:otherwise>
	     			    <c:forEach var="awardsTypeListRows" items="${awardTypeList}" >
	     			      <html:option value="${awardsTypeListRows.code}"><c:out value="${awardsTypeListRows.name}" /></html:option>	
	     			    </c:forEach>
	     			  </c:otherwise>
	     			  </c:choose>
	     			 </c:if>
	     			 <c:if test="${promotionAwardsForm.awardsActive == 'false'}">	
					  <c:forEach var="awardsTypeListRows" items="${awardTypeListAwardsInactive}" >
	     				  <html:option value="${awardsTypeListRows.code}"><c:out value="${awardsTypeListRows.name}" /></html:option>	
	     				</c:forEach>
	     			  </c:if>
					</html:select>
					<html:hidden property="awardsType" indexed="true" name="nominationPayoutFinalLevelList" />
	     		</c:when>
	     		<c:otherwise>
	        		<c:out value="${nominationPayoutFinalLevelList.awardsTypeDesc}" />
		     	</c:otherwise>
		  	</c:choose>
	     </td>
	  </tr>
	
	<%-- Award Amount --%>
	<tr class="form-row-spacer" id="<%=payoutFinalAwardAmountCntr%>">
  		<beacon:label property="awardAmountTypeFixed" required="true" styleClass="content-field-label-top">
             <div id="<%=payoutFinalAwardAmountCntr%>-points-label">
             	<cms:contentText key="AMOUNT" code="promotion.awards" />
             </div>
             <div id="<%=payoutFinalAwardAmountCntr%>-usd-label">
             	<cms:contentText key="AMOUNT_USD" code="promotion.awards" />
             </div>
    		<html:hidden property="approvalType" value="${promotionAwardsForm.approvalType}"  />
  		</beacon:label>
  
  		<td>
    	 <table>
          <tr>
            <td class="content-field" valign="top">
             <html:radio styleId="<%=awardAmountTypeFixedTrueCounter%>" indexed="true" property="awardAmountTypeFixed" 
        		value="true" disabled="${displayFlag}" onclick="awardAmountTypeChanged('fixed', '${finalPayoutCount.count}');" name="nominationPayoutFinalLevelList"/>
          		<cms:contentText code="promotion.awards" key="AMOUNT_FIXED" /> &nbsp;
          		<html:text styleId="<%=fixedAmountCounter%>" indexed="true" property="fixedAmount" size="5" styleClass="content-field" disabled="${displayFlag}" name="nominationPayoutFinalLevelList" onkeypress="return isNumberKey(event)"/>
            </td>
          </tr>
          
          <tr>
           <td class="content-field" valign="top">
             <html:radio styleId="<%=awardAmountTypeFixedFalseCounter%>" indexed="true" property="awardAmountTypeFixed" value="false" 
          	disabled="${displayFlag}" onclick="awardAmountTypeChanged('range', '${finalPayoutCount.count}');" name="nominationPayoutFinalLevelList"/>
          	<cms:contentText code="promotion.awards" key="AMOUNT_RANGE" />
          	
          	<cms:contentText code="promotion.awards" key="AMOUNT_BETWEEN" /> &nbsp;
          	<html:text styleId="<%=rangeAmountMinCounter%>" indexed="true" property="rangeAmountMin" size="5" styleClass="content-field" disabled="${displayFlag}" name="nominationPayoutFinalLevelList" onkeypress="return isNumberKey(event)"/>
          			<cms:contentText code="promotion.awards" key="AMOUNT_AND" />&nbsp;
          
          <html:text styleId="<%=rangeAmountMaxCounter%>" indexed="true" property="rangeAmountMax" size="5" styleClass="content-field" disabled="${displayFlag}" name="nominationPayoutFinalLevelList" onkeypress="return isNumberKey(event)"/>
        </td>
      </tr>
      
     <%-- Calculator --%>			
	 <tr id="<%=payoutFinalCalculatorCntr%>">
		<td class="content-field" valign="top">
		<c:choose>
					<c:when test="${! empty nominationPayoutFinalLevelList.finalLevelCalculatorId }">
						<html:radio styleId="<%=calculatorAwardAmountTypeFixedFalseCounter%>"
							property="awardAmountTypeFixed" value="cal" name="nominationPayoutFinalLevelList" indexed="true"
							disabled="${displayFlag or (promotionAwardsForm.cumulativeApproval=='true')}"   onclick="awardAmountTypeChanged('calculator', '${finalPayoutCount.count}');"/>
					</c:when>
					<c:otherwise>
						<html:radio styleId="<%=calculatorAwardAmountTypeFixedCalCounter%>"
							property="awardAmountTypeFixed" value="cal" name="nominationPayoutFinalLevelList" indexed="true"
							disabled="${displayFlag or (promotionAwardsForm.cumulativeApproval=='true')}"   onclick="awardAmountTypeChanged('calculator', '${finalPayoutCount.count}');"/>
					</c:otherwise>
	    </c:choose>
		 <cms:contentText code="promotion.awards" key="NOMINATION_AWARD_CALCULATOR" />	
			<BR>
	    		<cms:contentText code="promotion.awards" key="CALCULATOR_TYPE" />
				&nbsp; <html:select styleId="<%=calculatorFinalCounter%>" indexed="true" property="finalLevelCalculatorId"
					styleClass="content-field" name="nominationPayoutFinalLevelList" disabled="${displayFlag or (promotionAwardsForm.cumulativeApproval=='true')}">
				<html:option value=''>
					<cms:contentText key="CHOOSE_ONE" code="system.general" />
				</html:option>
				<c:forEach var="calculatorListRows" items="${nominationPayoutFinalLevelList.calculatorList}" >
	     				  <html:option value="${calculatorListRows.id}"><c:out value="${calculatorListRows.name}" /></html:option>	
	     				</c:forEach>
		    	</html:select><br><br>
	   </td>
	</tr>			
	<%-- Calculator --%>		
			
    </table>
  </td>
</tr>

	  <%--START payout description and value --%>
	        
      <tr class="form-row-spacer" id="<%=payoutFinalDescriptionCntr%>">
  		 <beacon:label property="payoutDescription" required="true" styleClass="content-field-label-top">
   			<cms:contentText key="PAYOUT_DESCRIPTION" code="promotion.basics" />
  		</beacon:label>
  		
  		<td colspan=2 class="content-field">  	
  			<html:text styleId="<%=payoutDescriptionCounter%>" indexed="true" property="payoutDescription" maxlength="50" size="50" styleClass="content-field" name="nominationPayoutFinalLevelList"/>
  		</td>
	  </tr> 
			
	  <tr class="form-row-spacer" id="<%=payoutFinalValueCntr%>">
  		  <beacon:label property="payoutValue" required="true" styleClass="content-field-label-top">
   			<cms:contentText key="PAYOUT_VALUE" code="promotion.basics" />
  		  </beacon:label>
  			
  		  <td colspan=2 class="content-field">
  			
			<html:text styleId="<%=payoutValueCounter%>" indexed="true" property="payoutValue"  maxlength="10" size="10" styleClass="content-field" name="nominationPayoutFinalLevelList" onkeypress="return isNumberKey(event)"/>
  			<html:select styleId="<%=payoutCurrencyCounter%>" indexed="true" property="payoutCurrency" styleClass="content-field" name="nominationPayoutFinalLevelList" >
			   	<html:option value=''><cms:contentText key="CHOOSE_CURRENCY_TYPE" code="system.general"/></html:option>	
			   	<html:options collection="currencies" property="currencyCode" labelProperty="currencyName"  />
			</html:select>
  		  </td>
	 </tr>
			
	 <%--STOP payout description and value --%>
	  
	 <%--START quantity and value --%>
			
	<tr class="form-row-spacer" id="<%=payoutFinalQuantityCntr%>">
  		<beacon:label property="quantity" styleClass="content-field-label-top">
   	      <cms:contentText key="QUANTITY" code="promotion.basics" />
  		</beacon:label>
  		
  		<td colspan=2 class="content-field">  	
  			<html:text styleId="<%=quantityCounter%>" indexed="true" property="quantity" maxlength="10" size="10" styleClass="content-field" name="nominationPayoutFinalLevelList" onkeypress="return isNumberKey(event)"/>
  		</td>
	</tr> 
			
	<%--STOP quantity and value --%> 
	
<tr class="form-blank-row" id="<%=blankFinaIndexCntr%>" ><td colspan="3">&nbsp;</td></tr>
	<% finalLevelIndex = finalLevelIndex + 1; %>
	  
	</c:forEach>
</c:if>

<%-- Payout Final Level Template Ends--%>

<%-- Awards Taxable Field Start--%>

<tr class="form-row-spacer" id="taxableRow">			
            <beacon:label property="taxable" required="true" styleClass="content-field-label-top">
              <cms:contentText key="TAXABLE" code="promotion.basics"/>
            </beacon:label>	

            <td colspan=2 class="content-field">
              <table>
                  <tr>
		          	<td class="content-field"><html:radio styleId="taxableRadioNo" property="taxable" value="false"/></td>
                    <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
		          </tr>
		          <tr>
		            <td class="content-field"><html:radio styleId="taxableRadioYes" property="taxable" value="true"/></td>
                    <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
		          </tr>
			  </table>
			  <html:hidden property="taxable" />
			</td>
</tr>

<%-- Awards Taxable Field End--%>


<%-- Nominator to enter a Recommended Award? Start--%>

<tr class="form-row-spacer" id="recommendedAward">			
            <beacon:label property="nominatorRecommendedAward" required="true" styleClass="content-field-label-top">
              <cms:contentText key="NOMINATOR_RECOMMENDED_AWARD" code="promotion.basics"/>
            </beacon:label>	

            <td colspan=2 class="content-field">
              <table>
                  <tr>
		          	<td class="content-field"><html:radio styleId="nominatorRecommendedAwardNo" property="nominatorRecommendedAward" value="false"/></td>
                    <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
		          </tr>
		          <tr>
		            <td class="content-field"><html:radio styleId="nominatorRecommendedAwardYes" property="nominatorRecommendedAward" value="true"/></td>
                    <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
		          </tr>
			  </table>
			</td>
</tr>
<html:hidden property="nominatorRecommendedAward" />

<%-- Nominator to enter a Recommended Award? End--%>




<c:if test="${promotionAwardsForm.timePeriodActive == 'true' }">
 <% 
    enableTimePeriod = "false"; 
 %>
</c:if>
<%-- Start Time period --%>

<c:if test="${promotionAwardsForm.promotionTypeCode == 'nomination'}" >
	<tr class="form-row-spacer" id="timePeriodActive">
  		<beacon:label property="timePeriodActive" required="true" styleClass="content-field-label-top">
   			<cms:contentText code="promotion.awards" key="TIME_PERIOD_ACTIVE" />
  		</beacon:label>
  		<td colspan=2 class="content-field">  	
  			<table>
    			<tr>
      				<td class="content-field"><html:radio property="timePeriodActive" styleId="timePeriodActiveNo" value="false" disabled="<%=enableTimePeriod%>" onclick="enableTimePeriod('false');"/></td>
      				<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    			</tr>
    			<tr>
      				<td class="content-field"><html:radio property="timePeriodActive" styleId="timePeriodActiveYes" value="true" disabled="<%=enableTimePeriod%>" onclick="enableTimePeriod('true');"/></td>
      				<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    			</tr>
  			</table>
  		</td>
	</tr>
	
	<html:hidden property="promotionStartDate"/>
	<html:hidden property="promotionEndDate"/>
	<!-- *******START Promotion Time Period  ******* -->
		<tr class="form-row-spacer" id="nominationTimePeriod" >
		<beacon:label property="nominationTimePeriods" required="true" styleClass="content-field-label-top">
		  <cms:contentText code="promotion.awards" key="NOMINATION_TIME_PERIOD" />
		</beacon:label>
		<td>
		    <table class="table table-striped table-bordered" width="100%">
		    <html:hidden property="nominationTimePeriodVBListSize"/>
			    <tr class="form-row-spacer">
			 		<td class="crud-table-header-row">
			 			<cms:contentText code="promotion.awards" key="TIME_PERIOD_NAME" />
			 		</td>      			
			 		<td class="crud-table-header-row">
			 			<cms:contentText code="promotion.awards" key="TIME_PERIOD_START_DATE" />
			 		</td>
			 		<td class="crud-table-header-row">
						<cms:contentText code="promotion.awards" key="TIME_PERIOD_END_DATE" />
		            </td>
			 		<td class="crud-table-header-row tp-hide-when-team">
				 		<cms:contentText code="promotion.awards" key="MAX_SUBMISSIONS_ALLOWED" />
			 		</td>
			 		<td class="crud-table-header-row tp-hide-when-team">
				 		<cms:contentText code="promotion.awards" key="MAX_NOMINATIONS_ALLOWED" />
			 		</td>
			 		<td class="crud-table-header-row tp-hide-when-team">
				 		<cms:contentText code="promotion.awards" key="MAX_WINS_ALLOWED" />
			 		</td>
			 		
			 		<c:if test="${ promotionAwardsForm.nominationTimePeriodVBListSize ne '1'}">
			 			<td class="crud-table-header-row">
							<div class="crud-table-header-row" id="removeTimePeriod"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></div>
						</td>
					</c:if> 	 		
			    </tr>
				<c:set var="switchColor" value="false"/>
				<%
				  int sIndex = 0;
				%>
			  	<c:forEach var="nominationTimePeriodVBList" items="${promotionAwardsForm.nominationTimePeriodVBList}" varStatus="status" >
			  	<html:hidden property="timePeriodId" name="nominationTimePeriodVBList" indexed="true"/>	
			  	   <%
			  	    String timePeriodNameCounter = "timePeriodName" + sIndex;
					String startDateCounter = "timePeriodStartDate" + sIndex;
					String endDateCounter = "timePeriodEndDate" + sIndex;
					String removeTimePeriodCounter = "removeTimePeriod" + sIndex;
					String maxSubmissionAllowedCounter = "maxSubmissionAllowed" + sIndex;
					String maxNominationsAllowedCounter = "maxNominationsAllowed" + sIndex;
					String maxWinsllowedCounter = "maxWinsllowed" + sIndex;
				 	%>	
			  	  <c:choose>
					<c:when test="${switchColor == 'false'}">
						<tr class="crud-table-row1">
						<c:set var="switchColor" scope="page" value="true"/>
					</c:when>
					<c:otherwise>
						<tr class="crud-table-row2">
						<c:set var="switchColor" scope="page" value="false"/>
					</c:otherwise>
				  </c:choose>
		
					<td class="crud-content" width="20%">	         			 
						<html:text styleId="<%=timePeriodNameCounter%>" property="timePeriodName" maxlength="50" size="15" indexed="true" name="nominationTimePeriodVBList" styleClass="content-field" />
  				    </td> 			      	
  				    <html:hidden property="timePeriodStartDateEditable" name="nominationTimePeriodVBList" indexed="true"/>
				    <td class="crud-content" width="20%">
		    			<c:choose>
		            		<c:when test="${nominationTimePeriodVBList.timePeriodStartDateEditable}">
		              			<label for="<%=startDateCounter%>" class="date">
			    	    			<html:text property="timePeriodStartDate" maxlength="10" size="10" indexed="true" name="nominationTimePeriodVBList" styleClass="text usedatepicker" styleId="<%=startDateCounter%>" readonly="true" disabled="false"/>
		         					<img alt="start date" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />  
		         				</label>
		            		</c:when>
		            		<c:otherwise>
		    	  	    		<html:text property="timePeriodStartDate" maxlength="10" size="10" indexed="true" name="nominationTimePeriodVBList" styleClass="content-field" styleId="<%=startDateCounter%>" readonly="true" />
		    	  	   		</c:otherwise>
		        		</c:choose>     	    
					</td>
					<html:hidden property="timePeriodEndDateEditable" name="nominationTimePeriodVBList" indexed="true"/>
					<td class="crud-content" width="20%">
		    			<c:choose>
		            		<c:when test="${nominationTimePeriodVBList.timePeriodEndDateEditable}">
		              			<label for="<%=endDateCounter%>" class="date">
				       				<html:text property="timePeriodEndDate" maxlength="10" size="10" indexed="true" name="nominationTimePeriodVBList" styleClass="text usedatepicker" styleId="<%=endDateCounter%>" />
		         					<img alt="end date" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
		         				</label>  
		            		</c:when>
		            		<c:otherwise>
		    	  	    		<html:text property="timePeriodEndDate" maxlength="10" size="10" indexed="true" name="nominationTimePeriodVBList" styleClass="content-field" styleId="<%=endDateCounter%>" readonly="true"/>
		    	  	   		</c:otherwise>
		        		</c:choose>		    
					</td>						 
			  	    <td class="crud-content tp-hide-when-team" width="20%">	         			 
						<html:text styleId="<%=maxSubmissionAllowedCounter%>" property="maxSubmissionAllowed" indexed="true" maxlength="10" size="12" name="nominationTimePeriodVBList" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
  				    </td>
  				    <td class="crud-content tp-hide-when-team" width="20%">	         			 
						<html:text styleId="<%=maxNominationsAllowedCounter%>" property="maxNominationsAllowed" indexed="true" maxlength="10" size="12" name="nominationTimePeriodVBList" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
  				    </td>
  				    <td class="crud-content tp-hide-when-team" width="20%">	         			 
						<html:text styleId="<%=maxWinsllowedCounter%>" property="maxWinsllowed" indexed="true" maxlength="10" size="12" name="nominationTimePeriodVBList" disabled="${displayFlag}" onkeypress="return isNumberKey(event)"/>
  				    </td>
			      	<td class="crud-content">
	    				<div id="<%=removeTimePeriodCounter%>">
		    				<html:button property="deleteTimePeriod" styleClass="content-buttonstyle" onclick="removeTimePeriod('removeTimePeriod');" >
					       		<cms:contentText code="system.button" key="REMOVE" />
					       	</html:button>
					    </div>
				    </td>	
 			        <%
		   		  		sIndex = sIndex + 1;
		   			%>
				  </c:forEach>
				  
				  <tr class="form-blank-row"><td></td></tr>
				    <tr class="form-row-spacer">
				        <td align="left" colspan="4">
				          	<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
				     	  		<a id="addAnotherhref"  href="#" onclick="addAnotherTimePeriod('addAnotherTimePeriod');updateLayerRemoveTimePeriod();" >
				       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
				     	  		</a> 
			     	  		</beacon:authorize>
			        	</td>
				    </tr>	  
			</table>
            </td>
          </tr>
          <!-- *******END Promotion Time Period  ******* -->
	</c:if>
	<%-- Stop Time period --%>

<tr class="form-row-spacer" id="budgetInfo">
  <beacon:label property="budgetOption" required="true" styleClass="content-field-label-top">
    <cms:contentText key="HAS_POINTS_BUDGET" code="promotion.awards" />
  </beacon:label>
  <td>  <%-- To fix 19628 in live promotion change the fixed amount value should not affect existing budget  --%>
  <c:if test="${promotionAwardsForm.live}">
		<beacon:client-state>
			<beacon:client-state-entry name="budgetMasterId"
				value="${promotionAwardsForm.budgetMasterId}" />
		</beacon:client-state>
	</c:if>
    
    <table> <%--  radio buttons table --%>
      <html:hidden property="hiddenBudgetMasterId"/>
      <html:hidden property="budgetSegmentVBListSize"/>
      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="budgetOptionNone" property="budgetOption" value="none" onclick="hideLayer('newBudget');" />
          <cms:contentText code="promotion.awards" key="BUDGET_NO" />
        </td>
      </tr>

      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="budgetOptionExists" property="budgetOption" value="existing" onclick="hideLayer('newBudget');" />
          <cms:contentText code="promotion.awards" key="BUDGET_EXISTING_CENTRAL" /> &nbsp;
         <c:if test="${ promoStatus == true && promotionAwardsForm.budgetOption == 'existing' }">
					<html:hidden property="budgetOption" value="${promotionAwardsForm.budgetOption}"/>
					<%-- <html:hidden property="budgetMasterId" value="${promotionAwardsForm.budgetMasterId}"/>--%>
		  </c:if>
          <html:select styleId="budgetMasterId" property="budgetMasterId" styleClass="content-field" disabled="${displayFlag}" onchange="updateLayersShown();" >
            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
            <html:options collection="budgetMasterList" property="budgetMasterId" labelProperty="budgetMasterName" />
          </html:select>
        </td>
      </tr>

      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="budgetOptionNew" property="budgetOption" value="new" onclick="showLayer('newBudget');updateLayerRemoveBudgetSegment(); "/>
          <cms:contentText code="promotion.awards" key="BUDGET_CREATE_CENTRAL" />
        </td>
      </tr>

      <%--  new budget row  --%>
      <tr>
        <td>
          <table id="newBudget"><%--  new budget fields --%>
            <tr>
              <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
              <td>
                <table>
                  <tr class="form-row-spacer">
                    <beacon:label property="budgetMasterName" required="true">
                      <cms:contentText key="BUDGET_MASTER_NAME" code="promotion.awards" />
                    </beacon:label>
                    <td  class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:text styleId="budgetMasterName" property="budgetMasterName" size="20" maxlength="30" styleClass="content-field" disabled="${displayFlag}" />
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

				 <tr>
					<beacon:label property="budgetMasterStartDate" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="BUDGET_MASTER_DATES" code="promotion.awards" />
		            </beacon:label> 
					<td class="content-field">
		              <table>	
		                <tr>
		                  <beacon:label property="budgetMasterStartDate" required="true">
				  		    <cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
				  		  </beacon:label>
				  		  <td class="content-field"> 
				  		  <label for="budgetMasterStartDate" class="date">	
			  		      	<html:text property="budgetMasterStartDate" styleId="budgetMasterStartDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
			  		       	<img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
			  		      </label>
		                  </td>
		                </tr>
		                <tr>
		                  <beacon:label property="budgetMasterEndDate">
		              		<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
		            	  </beacon:label>	
				          <td class="content-field">
				          <label for="budgetMasterEndDate" class="date">		  		  	
				            <html:text property="budgetMasterEndDate" styleId="budgetMasterEndDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
				            <img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
				          </label>
				          </td> 
		            	</tr>    
		              </table>
		            </td>
		          </tr>                  

                  <%-- Budget Type --%>
                  <tr class="form-row-spacer">
                    <beacon:label property="budgetType" required="true" styleClass="content-field-label-top">
                      <cms:contentText key="BUDGET_TYPE" code="promotion.awards" />
                    </beacon:label>
                    <td  class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:hidden property="budgetType" value="central" disabled="${displayFlag}" />
                            <cms:contentText code="promotion.awards" key="BUDGET_TYPE_CENTRAL" />
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <%-- Budget Cap Type --%>
                  <tr class="form-row-spacer">
                    <beacon:label property="budgetCapType" required="true" styleClass="content-field-label-top">
                      <cms:contentText key="CAP_TYPE" code="promotion.awards" />
                    </beacon:label>

                    <td class="content-field-label">
                      <c:choose>
                        <c:when test="${promotionAwardsForm.promotionTypeCode == 'nomination'}">
                          <html:hidden property="budgetCapType" value="hard"/>
                          <c:out value="${hardCapBudgetType.name}"/>
                        </c:when>
                        <c:otherwise>
                          <table>
                            <tr>
                              <td class="content-field" valign="top">
                                <html:radio styleId="budgetCapTypeHard" property="budgetCapType" value="hard" disabled="${displayFlag}" />
                                <cms:contentText code="promotion.awards" key="CAP_TYPE_HARD" />
                              </td>
                            </tr>
                          </table>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
            
				  <!-- *******Budget Segment start******* -->
		          <tr id="budgetSegmentOption" class="form-row-spacer" >
		          <beacon:label property="segmentName" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="BUDGET_SEGMENT" code="admin.budgetmaster.details" />
                    </beacon:label>
		          <td>
			          <table class="table table-striped table-bordered" width="120%">
					    <tr class="form-row-spacer">
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="BUDGET_SEGMENT_NAME" code="admin.budgetmaster.details"/>
					 		</th> 
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
					 		</th>      			
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
					 		</th>  
					 		<th class="crud-table-header-row"  id="newBudgetCentralTitle">
					 			<cms:contentText key="BUDGET_AMOUNT" code="admin.budgetmaster.details"/>
					 			<div>(Points)</div>
					 		</th>
					 		<c:if test="${ promotionAwardsForm.budgetSegmentVBListSize ne '1'}">
					 		<th class="crud-table-header-row" id="removeBudgetSegment"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
					 		</c:if>  			 		
					    </tr>
						<c:set var="switchColor" value="false"/>
						<%		  	 	int sIndex = 0; %>
					  	<c:forEach var="budgetSegmentVBList" items="${promotionAwardsForm.budgetSegmentVBList}" varStatus="status" >
					  	<html:hidden property="id" name="budgetSegmentVBList" indexed="true"/> 
					  	<tr>
					  	<%
							String startDateCalendarCounter = "charStartDate" + sIndex;
							String endDateCalendarCounter = "charEndDate" + sIndex;
							String removeBudgetSegmentCounter = "removeBudgetSegment" + sIndex;
						%>	
						  <c:choose>
							<c:when test="${switchColor == 'false'}">
								<tr class="crud-table-row1">
								<c:set var="switchColor" scope="page" value="true"/>
							</c:when>
							<c:otherwise>
								<tr class="crud-table-row2">
								<c:set var="switchColor" scope="page" value="false"/>
							</c:otherwise>
						  </c:choose>
						    <td class="crud-content">
						    	<html:text property="segmentName" size="50" maxlength="50" indexed="true" styleId="segmentName" name="budgetSegmentVBList" styleClass="content-field" />
					      	</td> 
					    	<td class="crud-content">
					    	<label for="<%=startDateCalendarCounter%>" class="date">
							    <html:text property="startDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=startDateCalendarCounter%>"/>
			               		<img alt="start date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
			                </label>
						    </td> 			      	
						    <td class="crud-content">
						    <label for="<%=endDateCalendarCounter%>" class="date">
						       	<html:text property="endDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=endDateCalendarCounter%>" />
			               		<img alt="end date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
			               	</label>
							</td>  
							<td class="crud-content">
						    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="originalValue" name="budgetSegmentVBList" styleClass="content-field" />
							</td>
							<td class="crud-content">
				            	<table id="<%=removeBudgetSegmentCounter%>">
				            	<tr>
				            	<td align="right">
						        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionAwards.do', 'removeBudgetSegment')" >
						          <cms:contentText code="system.button" key="REMOVE" />
						        </html:button>
						        </td>
						        </tr>
						        </table>
						    </td>

		            		<% sIndex = sIndex + 1; %>
						    </tr>
						  </c:forEach>
						  
						  <tr class="form-blank-row"><td></td></tr>
						    <tr class="form-row-spacer">
						        <td align="left" colspan="2">
					     	  		<a id="addAnotherhref"  href="#" onclick="addAnotherSegment('addAnotherSegment')" >
					       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
					     	  		</a> 
					        	</td>
						    </tr>	  
					</table>
                    </td>
                  </tr>
				  <!-- ********Budget Segment end****** -->	

                </table> <%--  sub new budget table --%>
              </td>
            </tr>
          </table>  <%-- End newBudget --%>
        </td>
      </tr> <%--  end budget row --%>
    </table> <%--  radio buttons for budget --%>
    <html:hidden property="budgetOption" />
  </td>
</tr> <%--  budgetInfo --%>

<%-- Cash Budget --%>
<c:if test="${promotionAwardsForm.promotionTypeCode == 'nomination'}">
<tr class="form-row-spacer" id="cashBudgetInfo">
  <beacon:label property="cashBudgetOption" required="true" styleClass="content-field-label-top">
    <cms:contentText key="HAS_CASH_BUDGET" code="promotion.awards" />
  </beacon:label>
  <td>  <%-- To fix 19628 in live promotion change the fixed amount value should not affect existing budget  --%>
  <c:if test="${promotionAwardsForm.live}">
		<beacon:client-state>
			<beacon:client-state-entry name="cashBudgetMasterId"
				value="${promotionAwardsForm.cashBudgetMasterId}" />
		</beacon:client-state>
	</c:if>
    
    <table> <%--  radio buttons table --%>
      <html:hidden property="hiddenCashBudgetMasterId"/>
      <html:hidden property="cashBudgetSegmentVBListSize"/>
      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="cashBudgetOptionNone" property="cashBudgetOption" value="none" disabled="${displayFlag}" onclick="hideLayer('newCashBudget');" />
          <cms:contentText code="promotion.awards" key="BUDGET_NO" />
        </td>
      </tr>

      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="cashBudgetOptionExists" property="cashBudgetOption" value="existing" disabled="${displayFlag}" onclick="hideLayer('newCashBudget');" />
          <cms:contentText code="promotion.awards" key="BUDGET_EXISTING_CENTRAL" /> &nbsp;
         <c:if test="${ promoStatus == true && promotionAwardsForm.cashBudgetOption == 'existing' }">
					<html:hidden property="cashBudgetOption" value="${promotionAwardsForm.cashBudgetOption}"/>
					<%-- <html:hidden property="budgetMasterId" value="${promotionAwardsForm.budgetMasterId}"/>--%>
		  </c:if>
          <html:select styleId="cashBudgetMasterId" property="cashBudgetMasterId" styleClass="content-field" disabled="${displayFlag}" onchange="updateLayersShown();" >
            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
            <html:options collection="cashBudgetMasterList" property="budgetMasterId" labelProperty="budgetMasterName" />
          </html:select>
        </td>
      </tr>

      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="cashBudgetOptionNew" property="cashBudgetOption" value="new" disabled="${displayFlag}" onclick="showLayer('newCashBudget');updateLayerRemoveCashBudgetSegment(); "/>
          <cms:contentText code="promotion.awards" key="BUDGET_CREATE_CENTRAL" />
        </td>
      </tr>

      <%--  new budget row  --%>
      <tr>
        <td>
          <table id="newCashBudget"><%--  new budget fields --%>
            <tr>
              <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
              <td>
                <table>
                  <tr class="form-row-spacer">
                    <beacon:label property="cashBudgetMasterName" required="true">
                      <cms:contentText key="BUDGET_MASTER_NAME" code="promotion.awards" />
                    </beacon:label>
                    <td  class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:text styleId="cashBudgetMasterName" property="cashBudgetMasterName" size="20" maxlength="30" styleClass="content-field" disabled="${displayFlag}" />
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

				 <tr>
					<beacon:label property="cashBudgetMasterStartDate" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="BUDGET_MASTER_DATES" code="promotion.awards" />
		            </beacon:label> 
					<td class="content-field">
		              <table>	
		                <tr>
		                  <beacon:label property="cashBudgetMasterStartDate" required="true">
				  		    <cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
				  		  </beacon:label>
				  		  <td class="content-field"> 
				  		  <label for="cashBudgetMasterStartDate" class="date">	
			  		      	<html:text property="cashBudgetMasterStartDate" styleId="cashBudgetMasterStartDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
			  		       	<img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
			  		      </label>
		                  </td>
		                </tr>
		                <tr>
		                  <beacon:label property="cashBudgetMasterEndDate">
		              		<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
		            	  </beacon:label>	
				          <td class="content-field">
				          <label for="cashBudgetMasterEndDate" class="date">		  		  	
				            <html:text property="cashBudgetMasterEndDate" styleId="cashBudgetMasterEndDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
				            <img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
				          </label>
				          </td> 
		            	</tr>    
		              </table>
		            </td>
		          </tr>                  

                  <%-- Budget Type --%>
                  <tr class="form-row-spacer">
                    <beacon:label property="cashBudgetType" required="true" styleClass="content-field-label-top">
                      <cms:contentText key="BUDGET_TYPE" code="promotion.awards" />
                    </beacon:label>
                    <td  class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:hidden property="cashBudgetType" value="central" disabled="${displayFlag}" />
                            <cms:contentText code="promotion.awards" key="BUDGET_TYPE_CENTRAL" />
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>

                  <%-- Budget Cap Type --%>
                  <tr class="form-row-spacer">
                    <beacon:label property="cashBudgetCapType" required="true" styleClass="content-field-label-top">
                      <cms:contentText key="CAP_TYPE" code="promotion.awards" />
                    </beacon:label>

                    <td class="content-field-label">
                      <c:choose>
                        <c:when test="${promotionAwardsForm.promotionTypeCode == 'nomination'}">
                          <html:hidden property="cashBudgetCapType" value="hard"/>
                          <c:out value="${hardCapBudgetType.name}"/>
                        </c:when>
                        <c:otherwise>
                          <table>
                            <tr>
                              <td class="content-field" valign="top">
                                <html:radio styleId="cashBudgetCapTypeHard" property="cashBudgetCapType" value="hard" disabled="${displayFlag}" />
                                <cms:contentText code="promotion.awards" key="CAP_TYPE_HARD" />
                              </td>
                            </tr>
                          </table>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
            
				  <!-- *******Budget Segment start******* -->
		          <tr id="cashBudgetSegmentOption" class="form-row-spacer" >
		          <beacon:label property="cashSegmentName" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="BUDGET_SEGMENT" code="admin.budgetmaster.details" />
                    </beacon:label>
		          <td>
			          <table class="table table-striped table-bordered" width="120%">
					    <tr class="form-row-spacer">
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="BUDGET_SEGMENT_NAME" code="admin.budgetmaster.details"/>
					 		</th> 
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
					 		</th>      			
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
					 		</th>  
					 		<th class="crud-table-header-row"  id="newBudgetCentralTitle">
					 			<cms:contentText key="BUDGET_AMOUNT" code="admin.budgetmaster.details"/>
					 			<div>(USD)</div>
					 		</th>
					 		<c:if test="${ promotionAwardsForm.cashBudgetSegmentVBListSize ne '1'}">
					 		<th class="crud-table-header-row" id="removeCashBudgetSegment"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
					 		</c:if>  			 		
					    </tr>
						<c:set var="switchColor2" value="false"/>
						<%		  	 	int sIndex2 = 0; %>
					  	<c:forEach var="cashBudgetSegmentVBList" items="${promotionAwardsForm.cashBudgetSegmentVBList}" varStatus="status2" >
					  	<html:hidden property="id" name="cashBudgetSegmentVBList" indexed="true"/> 
					  	<tr>
					  	<%
							String startDateCalendarCounter2 = "charStartDateCash" + sIndex2;
							String endDateCalendarCounter2 = "charEndDateCash" + sIndex2;
							String removeBudgetSegmentCounter2 = "removeCashBudgetSegment" + sIndex2;
						%>	
						  <c:choose>
							<c:when test="${switchColor2 == 'false'}">
								<tr class="crud-table-row1">
								<c:set var="switchColor2" scope="page" value="true"/>
							</c:when>
							<c:otherwise>
								<tr class="crud-table-row2">
								<c:set var="switchColor2" scope="page" value="false"/>
							</c:otherwise>
						  </c:choose>
						    <td class="crud-content">
						    	<html:text property="segmentName" size="50" maxlength="50" indexed="true" styleId="segmentName" name="cashBudgetSegmentVBList" styleClass="content-field" />
					      	</td> 
					    	<td class="crud-content">
					    	<label for="<%=startDateCalendarCounter2%>" class="date">
							    <html:text property="startDateStr" maxlength="10" size="10" indexed="true" name="cashBudgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=startDateCalendarCounter2%>"/>
			               		<img alt="start date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
			                </label>
						    </td> 			      	
						    <td class="crud-content">
						    <label for="<%=endDateCalendarCounter2%>" class="date">
						       	<html:text property="endDateStr" maxlength="10" size="10" indexed="true" name="cashBudgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=endDateCalendarCounter2%>" />
			               		<img alt="end date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
			               	</label>
							</td>  
							<td class="crud-content">
						    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="originalValue" name="cashBudgetSegmentVBList" styleClass="content-field" />
							</td>
							<td class="crud-content">
				            	<table id="<%=removeBudgetSegmentCounter2%>">
				            	<tr>
				            	<td align="right">
						        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionAwards.do', 'removeCashBudgetSegment')" >
						          <cms:contentText code="system.button" key="REMOVE" />
						        </html:button>
						        </td>
						        </tr>
						        </table>
						    </td>

		            		<% sIndex2 = sIndex2 + 1; %>
						    </tr>
						  </c:forEach>
						  
						  <tr class="form-blank-row"><td></td></tr>
						    <tr class="form-row-spacer">
						        <td align="left" colspan="2">
					     	  		<a id="addAnotherCashhref"  href="#" onclick="addAnotherSegment('addAnotherCashSegment')" >
					       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
					     	  		</a> 
					        	</td>
						    </tr>	  
					</table>
                    </td>
                  </tr>
				  <!-- ********Budget Segment end****** -->	

                </table> <%--  sub new budget table --%>
              </td>
            </tr>
          </table>  <%-- End newBudget --%>
        </td>
      </tr> <%--  end budget row --%>
    </table> <%--  radio buttons for budget --%>
    <html:hidden property="cashBudgetOption" />
  </td>
</tr> 
</c:if>
<%-- Cash Budget --%>

<%-- Request More Budget --%>
<tr class="form-row-spacer" id="requestMoreBudgetRow">
  <beacon:label property="requestMoreBudget" required="true" styleClass="content-field-label-top">
    <cms:contentText code="promotion.awards" key="REQUEST_MORE_BUDGET" />
  </beacon:label>
  <td>
    <table>
      <tr>
        <td class="content-field" valign="top">
          <html:radio styleId="requestMoreBudgetFalse" property="requestMoreBudget" 
             value="false" disabled="${displayFlag}" onclick="hideApproverSection();"/>
          <cms:contentText code="system.common.labels" key="NO" />
        </td>
      </tr>
      <tr>
        <td class="content-field" valign="top">
          <html:radio styleId="requestMoreBudgetTrue" property="requestMoreBudget" 
             value="true" disabled="${displayFlag}" onclick="showApproverSection();"/>
          <cms:contentText code="system.common.labels" key="YES" />
        </td>
      </tr>
      <tr>
        <td>
          <table id="budgetRequestTable"><%--  Budget approver fields --%>
            <tr>
              <td> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
              <td>
                <table>
                  <tr class="form-row-spacer">
                    <beacon:label property="budgetApprover" required="false">
                      <cms:contentText code="promotion.awards" key="BUDGET_APPROVER" />
                    </beacon:label>
                    <td class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:hidden property="nomBudgetApproverId" value="${promotionAwardsForm.nomBudgetApproverId}" styleId="approverIdHidden" />
                            <span id="approverNameSpan">
                              <c:out value="${approverName}" />
                            </span>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr class="form-row-spacer">
                  	<beacon:label property="lastName" required="false">
                      <cms:contentText code="participant.search" key="SEARCH_BY_LAST_NAME" />
                    </beacon:label>
                    <td class="content-field-label">
                      <table>
                        <tr>
                          <td>
                            <html:text styleId="searchApproverLastName" property="budgetApproverSearchLastName" size="15" styleClass="content-field" disabled="${displayFlag}" />
                          </td>
                          <td>&nbsp;&nbsp;&nbsp;</td>
                          <td>
                            <html:button styleId="approverSearchSubmitButton" property="approverSearch" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionAwards.do', 'searchApprover')" >
						      <cms:contentText code="system.button" key="SEARCH" />
						    </html:button>
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
                  <tr class="form-row-spacer">
                    <beacon:label property="lastName" required="false">
                    </beacon:label>
                    <td class="content-field-label">
                      <c:choose>
                        <c:when test="${empty approverSearchResults}">
                          <cms:contentText code="system.general" key="SEARCH_RESULTS" />
                          (<span id="approverSearchResultsCount">0</span>):
                          <br/>
                          <html:select property="selectedBudgetApproverUserId" styleId="approverSearchResultsBox" size="5" style="width: 430px" styleClass="killme" />
                          <html:hidden property="selectedBudgetApproverUserId" value="${promotionAwardsForm.selectedBudgetApproverUserId}" styleId="defaultApproverIdHidden" />
                        </c:when>

                        <c:otherwise>
                          <cms:contentText code="system.general" key="SEARCH_RESULTS" />&nbsp;
                          (<SPAN ID="approverSearchResultsCount"><c:out value="${approverSearchResultsCount}"/></SPAN>):
                          <br/>
                          <html:select property="selectedBudgetApproverUserId" styleId="approverSearchResultsBox" size="5" style="width: 430px" styleClass="killme" onchange="approverChange()">
                            <html:options collection="approverSearchResults" property="id" labelProperty="LFComma" filter="false"/>
                          </html:select>
                          <html:hidden property="selectedBudgetApproverUserId" value="${promotionAwardsForm.selectedBudgetApproverUserId}" styleId="defaultApproverIdHidden" />
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </td>
</tr>
<%-- Request More Budget --%>

<script>
 <%-- On Page the nominationAwardSpecifierTypeCode is disabled and its default value is choose one, when fixed amount is the checked option--%>
	  $(document).ready(function() {
		  
		  if($("input:radio[name='eachAwardAmountTypeFixed']:checked").val()== "false" )
			{
				$("input[id='eachRangeAmountMin']").attr('disabled','');
				$("input[id='eachRangeAmountMax']").attr('disabled','');
				$("select[id='eachNominationAwardSpecifierTypeCode']").attr('disabled','');
				$("input[id='eachFixedAmount']").attr('disabled','disabled');
			}else{
				$("input[id='eachRangeAmountMin']").attr('disabled','disabled');
				$("input[id='eachRangeAmountMax']").attr('disabled','disabled');
				$("select[id='eachNominationAwardSpecifierTypeCode']").attr('disabled','disabled');
				$("select[id='eachNominationAwardSpecifierTypeCode'] option[value='']").attr('selected', 'selected');
				$("input[id='eachFixedAmount']").attr('disabled','');
			}
		  
		  if($("input:radio[name='finalAwardAmountTypeFixed']:checked").val()== "false" )
			{
				$("input[id='finalRangeAmountMin']").attr('disabled','');
				$("input[id='finalRangeAmountMax']").attr('disabled','');
				$("select[id='finalNominationAwardSpecifierTypeCode']").attr('disabled','');
				$("input[id='finalFixedAmount']").attr('disabled','disabled');
			}else{
				$("input[id='finalRangeAmountMin']").attr('disabled','disabled');
				$("input[id='finalRangeAmountMax']").attr('disabled','disabled');
				$("select[id='finalNominationAwardSpecifierTypeCode']").attr('disabled','disabled');
				$("select[id='finalNominationAwardSpecifierTypeCode'] option[value='']").attr('selected', 'selected');
				$("input[id='finalFixedAmount']").attr('disabled','');
			}
		  
		  // Hide the budget request section when the page loads, if it needs to be
		  if($("#requestMoreBudgetFalse").attr("checked"))
		  {
			  hideApproverSection();
		  }
		  if($("#cashBudgetOptionNone").attr("checked")){
			  hideLayer('newCashBudget');
		  }
		  // For when displayFlag does not work...
		  <c:if test="${promotionStatus == 'expired' || promotionStatus == 'live' || promotionStatus == 'complete'}" >
			  if($("input:radio[name='payoutEachLevel']:checked").val()== "true")
		  	  {
					$("select[id^='eachAwardsType']").attr('disabled','disabled');
			  }
			  if($("input:radio[name='payoutFinalLevel']:checked").val()== "true")
		  	  {
			        $("select[id^='finalAwardsType']").attr('disabled','disabled');
		  	  }
			  // "Awards are Taxable"
			  $("#taxableRadioNo").attr('disabled', 'disabled');
			  $("#taxableRadioYes").attr('disabled', 'disabled');
			  // Budgets options - existing and create new. 'No Budget' is left enabled
			  $("#budgetOptionExists").attr('disabled', 'disabled');
			  $("#budgetOptionNew").attr('disabled', 'disabled');
			  $("#cashBudgetOptionExists").attr('disabled', 'disabled');
			  $("#cashBudgetOptionNew").attr('disabled', 'disabled');
		  </c:if>
		  disablePayoutEachLevel();
		  disablePayoutFinalLevel();
		  updateRecommendedAwardState();
		  setTimePeriodColumnVisibility();
		  enableTimePeriod();
		  updateLayerRemoveTimePeriod();
		  enableAwardsPage();
		  if( document.getElementById("payoutEachLevelNo").checked == true && document.getElementById("payoutFinalLevelNo").checked == true )
		  {
		  eachLevelAwardTypeChange();
		  finalLevelAwardTypeChange();
		  }
		  else if( document.getElementById("payoutEachLevelYes").checked == true )
		  {
		  eachLevelAwardTypeChange();
		  }
		  else if( document.getElementById("payoutFinalLevelYes").checked == true )
		  {
		  finalLevelAwardTypeChange();
		  }
		  else
		  {  
	      }
		  
		  // Hide the payout each level option if there is only one level
		  if($("#oneLevelApproval").val() == "true")
		  {
			  hideLayer('payoutEachLevel');
		  }
	  });
  </script>
