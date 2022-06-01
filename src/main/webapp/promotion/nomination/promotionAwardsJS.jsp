<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">

  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      // this is the way the standards work
      var style2 = document.getElementById(whichLayer).style;
      style2.display = "";
      if( whichLayer == "newBudget")
	  {
	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one 
	  	//when create budget is selected
    	  if ( $("input:radio[name='budgetOption']:checked").val()== "new")
   		   {
   		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
   			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
   		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
   		   {
   		   	 $("select[name='budgetMasterId']").attr('disabled', '');
       	   }
	  }
      
      if(whichLayer == "newCashBudget")
      {
        if ( $("input:radio[name='cashBudgetOption']:checked").val()== "new")
  	    {
  	      $("select[name='cashBudgetMasterId'] option[value='']").attr('selected', 'selected');
  		  $("select[name='cashBudgetMasterId']").attr('disabled', 'disabled');
  	    }
   	    else if($("input:radio[name='cashBudgetOption']:checked").val()== "existing")
  	    {
  	      $("select[name='cashBudgetMasterId']").attr('disabled', '');
     	}
      }
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
      if( whichLayer == "newBudget")
	  {
	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one when no budget is selected
    	  if ( $("input:radio[name='budgetOption']:checked").val()== "none")
   		   {
   		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
   			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
   		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
   		   {
   		   	 $("select[name='budgetMasterId']").attr('disabled', '');
       	   }
	  }
      
      if(whichLayer == "newCashBudget")
      {
    	if ( $("input:radio[name='cashBudgetOption']:checked").val()== "none")
  	    {
  	      $("select[name='cashBudgetMasterId'] option[value='']").attr('selected', 'selected');
  		  $("select[name='cashBudgetMasterId']").attr('disabled', 'disabled');
  	    }
   	    else if($("input:radio[name='cashBudgetOption']:checked").val()== "existing")
  	    {
  	   	  $("select[name='cashBudgetMasterId']").attr('disabled', '');
     	}
      }
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
  
function updateLayerRemoveBudgetSegment(){
	  var count = $("input[name='budgetSegmentVBListSize']").val();
	  for(i=0; i<= count; i++){
		  if( i != 0 ){
			  if( i+1 == count ){
					 showLayer('removeBudgetSegment'+i); 
				  }else{
					  hideLayer('removeBudgetSegment'+i);
				  }
	  		}else{
	  			 hideLayer('removeBudgetSegment0');
	  		}
		  };
	}  
	
function updateLayerRemoveCashBudgetSegment()
{
  var count = $("input[name='cashBudgetSegmentVBListSize']").val();
  for(i=0; i <= count; i++)
  {
    if( i != 0 )
    {
	  if( i+1 == count )
	  {
	    showLayer('removeCashBudgetSegment'+i); 
	  }
	  else
	  {
	    hideLayer('removeCashBudgetSegment'+i);
	  }
 	}
    else
    {
 	  hideLayer('removeCashBudgetSegment0');
 	}
  };
}
//-->
</script>

<script type="text/javascript">
<!--
       //Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
       
       
        var budgetOptionNoneObj = document.getElementById("budgetOptionNone");
        var budgetOptionExistsObj = document.getElementById("budgetOptionExists");
        var cashBudgetOptionNoneObj = document.getElementById("cashBudgetOptionNone");
        var cashBudgetOptionExistsObj = document.getElementById("cashBudgetOptionExists");
        //var awardAmountTypeFixedFalseObj = document.getElementById("awardAmountTypeFixedFalse");
        //var awardAmountTypeFixedTrueObj = document.getElementById("awardAmountTypeFixedTrue");
        //var fixedAmountObj = document.getElementById("fixedAmount");
        //var rangeAmountMinObj = document.getElementById("rangeAmountMin");
        //var rangeAmountMaxObj = document.getElementById("rangeAmountMax");
        var budgetMasterIdObj = document.getElementById("budgetMasterId");
        var budgetMasterNameObj = document.getElementById("budgetMasterName");
        var cashBudgetMasterIdObj = document.getElementById("cashBudgetMasterId");
        var cashBudgetMasterNameObj = document.getElementById("cashBudgetMasterName");
        
        var budgetCapTypeHardObj = document.getElementById("budgetCapTypeHard");
        var budgetOptionNewObj = document.getElementById("budgetOptionNew");
        if( budgetOptionNewObj.checked==true ){
          showLayer("newBudget");
          updateLayerRemoveBudgetSegment();
        }else{
          hideLayer("newBudget");
        }
        var cashBudgetCapTypeHardObj = document.getElementById("cashBudgetCapTypeHard");
        var cashBudgetOptionNewObj = document.getElementById("cashBudgetOptionNew");
        if( cashBudgetOptionNewObj.checked==true ){
          showLayer("newCashBudget");
          updateLayerRemoveCashBudgetSegment();
        }else{
          hideLayer("newCashBudget");
        }
        var awardsTaxableTrueObj = document.getElementById("taxableRadioYes");
        var awardsTaxableFalseObj = document.getElementById("taxableRadioNo");
        
        var payoutEachLevelTrueObj = document.getElementById("payoutEachLevelYes");
        var payoutEachLevelFalseObj = document.getElementById("payoutEachLevelNo");
        var payoutFinalLevelTrueObj = document.getElementById("payoutFinalLevelYes");
        var payoutFinalLevelFalseObj = document.getElementById("payoutFinalLevelNo");
        
        var nominatorRecommendedAwardFalseObj = document.getElementById("nominatorRecommendedAwardNo");
        var nominatorRecommendedAwardTrueObj = document.getElementById("nominatorRecommendedAwardYes");
        
        var timePeriodActiveTrueObj = document.getElementById("timePeriodActiveYes");
        var timePeriodActiveFalseObj = document.getElementById("timePeriodActiveNo");
        var timePeriodTrueObj = document.getElementById("timePeriodActiveYes");
        var timePeriodFalseObj = document.getElementById("timePeriodActiveNo");
        var timePeriodFalseObj = document.getElementById("timePeriodActiveNo");
        
        var requestMoreBudgetFalseObj = document.getElementById("requestMoreBudgetFalse");
        var requestMoreBudgetTrueObj = document.getElementById("requestMoreBudgetTrue");
        
        
        enableFields();
        //awardTypeChange();
   //End inline javascript
    
   function finalLevelAwardTypeChange( awardType )
	{
	   var finalCount = document.promotionAwardsForm.nominationPayoutFinalLevelListSize.value;
	   var oneLevelApprovalValue = document.getElementById("oneLevelApproval").value;
	   var selectBox = document.getElementById('finalAwardsType'+finalCount).value;
	   var awardSelectedValue = document.getElementById("awardSelected").value;
	   if( oneLevelApprovalValue == "true" )
		{
		   hideLayer('payoutEachLevel');
		}
	   
	   if( !awardType )
	   {
		   hideLayer('payoutFinalCalculator'+finalCount);
		   if( $("input:radio[name='payoutFinalLevel']:checked").val()== "false" && finalCount == 1 )
			{
			   hideLayer('blankFinalIndex'+finalCount);
			   hideLayer('finalLevel'+finalCount);
			   hideLayer('payoutFinalAwardsType'+finalCount);
			}
		   if( $("input:radio[name='payoutFinalLevel']:checked").val()== "true" && finalCount == 1 )
			{
			   showLayer('blankFinalIndex'+finalCount);
			   showLayer('finalLevel'+finalCount);
			   showLayer('payoutFinalAwardsType'+finalCount);
			}
		   for(index=1; index<=finalCount; index++)
		    {
			   var selectBox = document.getElementById('finalAwardsType'+index).value;
			   if( !selectBox)
				{
				    hideLayer('payoutFinalDescription'+index);
		   	  		hideLayer('payoutFinalValue'+index);
		   			hideLayer('payoutFinalQuantity'+index);
		   			hideLayer('payoutFinalAwardAmount'+index);
		   			document.getElementById('finalFixedAmount'+index).value = '';
			    	document.getElementById('finalRangeAmountMin'+index).value = '';
			    	document.getElementById('finalRangeAmountMax'+index).value = '';
			    	document.getElementById('payoutFinalCalc'+index).value = '';
			    	document.getElementById('finalPayoutDescription'+index).value = '';
			    	document.getElementById('finalPayoutValue'+index).value = '';
			    	document.getElementById('finalPayoutCurrency'+index).value = '';
			    	document.getElementById('finalQuantity'+index).value = '';
			    	document.getElementById('payoutFinalCalc'+index).disabled = true;
				}
			   else
				{
					   
				       if ( selectBox == 'none' )
				    	{
				    	   hideLayer('payoutFinalAwardAmount'+index);
				    	}
				       if (selectBox == 'other'){
					    	showLayer('payoutFinalDescription'+index);
					    	showLayer('payoutFinalValue'+index);
					    	showLayer('payoutFinalQuantity'+index);
					    	hideLayer('payoutFinalAwardAmount'+index);
			   	    	}
				       if( (selectBox == 'cash' || selectBox == 'points') ) 
				       {
			        		showLayer('payoutFinalAwardAmount'+index);
			        		if( selectBox == 'points' )
			        		{
			        			showLayer('payoutFinalCalculator'+index);
			        		}
			        		else
			        		{
			        			hideLayer('payoutFinalCalculator'+index);
			        		}
			        		hideLayer('payoutFinalDescription'+index);
			        		hideLayer('payoutFinalValue'+index);
			        		hideLayer('payoutFinalQuantity'+index);
					    	
					    	if( $("input:radio[name='payoutFinalLevel']:checked").val()== "true" && $("#awardSelected").val() == "true" )
							{
					    		   document.getElementById('finalAwardAmountTypeFixedTrue'+index).disabled = true;
								   document.getElementById('finalFixedAmount'+index).disabled = true;
								   document.getElementById('finalAwardAmountTypeFixedFalse'+index).checked = true;
								   document.getElementById('finalRangeAmountMin'+index).disabled = false;
						       	   document.getElementById('finalRangeAmountMax'+index).disabled = false;
						       	   $('#finalCalAwardAmtTypeFixedFalse'+index).attr("disabled", true);
						       	   $('#finalCalAwardAmtTypeFixedCal'+index).attr("disabled", true);
						       	   $('#payoutFinalCalc'+index).attr("disabled", true);
							}
			            }
					   
				   // Show the appropriate amount label
				   if( selectBox == 'cash' )
				   {
					   hideLayer('payoutFinalAwardAmount'+index+'-points-label');
					   showLayer('payoutFinalAwardAmount'+index+'-usd-label');
				   }
				   else
				   {
					   hideLayer('payoutFinalAwardAmount'+index+'-usd-label');
					   showLayer('payoutFinalAwardAmount'+index+'-points-label');
				   }
				}
	   	     }
	   }
	   else{
		    var value = awardType.value;
			var id = awardType.id;
			var index = id.charAt(id.length-1);
			if(finalCount == 1)
			{
			   index = 1;
			}

			if (value == 'other'){
			    	showLayer('payoutFinalDescription'+index);
			    	showLayer('payoutFinalValue'+index);
			    	showLayer('payoutFinalQuantity'+index);
			    	hideLayer('payoutFinalAwardAmount'+index);
			    	hideLayer('payoutFinalCalculator'+index);
			    	document.getElementById('finalFixedAmount'+index).value = '';
			    	document.getElementById('finalRangeAmountMin'+index).value = '';
			    	document.getElementById('finalRangeAmountMax'+index).value = '';
			    	document.getElementById('payoutFinalCalc'+index).value = '';
	    	}else if( value == 'cash' || value == 'points') {
	         	hideLayer('payoutFinalDescription'+index);
	         	hideLayer('payoutFinalValue'+index);
	         	hideLayer('payoutFinalQuantity'+index);
	         	showLayer('payoutFinalAwardAmount'+index);
	         	if ( awardSelectedValue == "false" )
				{
		         	if( value == 'points' )
		         	{
		         		showLayer('payoutFinalCalculator'+index);	
		         	}
		         	else
	        		{
	        			hideLayer('payoutFinalCalculator'+index);
	        		}
				}
	         	document.getElementById('finalAwardAmountTypeFixedTrue'+index).checked = true;
	       		document.getElementById('finalFixedAmount'+index).value = '';
		    	document.getElementById('finalRangeAmountMin'+index).value = '';
		    	document.getElementById('finalRangeAmountMax'+index).value = '';
		    	document.getElementById('payoutFinalCalc'+index).value = '';
	       		document.getElementById('finalPayoutDescription'+index).value = '';
		    	document.getElementById('finalPayoutValue'+index).value = '';
		    	document.getElementById('finalPayoutCurrency'+index).value = '';
		    	document.getElementById('finalQuantity'+index).value = '';
	       		document.getElementById('finalFixedAmount'+index).disabled = false;
	       		document.getElementById('finalRangeAmountMin'+index).disabled = true;
	       		document.getElementById('finalRangeAmountMax'+index).disabled = true;
		    	document.getElementById('payoutFinalCalc'+index).disabled = true;
		    	if( $("#awardSelected").val() == "true" )
				{
		    		   document.getElementById('finalAwardAmountTypeFixedTrue'+index).disabled = true;
					   document.getElementById('finalFixedAmount'+index).disabled = true;
					   document.getElementById('finalAwardAmountTypeFixedFalse'+index).checked = true;
					   document.getElementById('finalRangeAmountMin'+index).disabled = false;
			       	   document.getElementById('finalRangeAmountMax'+index).disabled = false;
			       	   $('#finalCalAwardAmtTypeFixedFalse'+index).attr("disabled", true);
			       	   $('#finalCalAwardAmtTypeFixedCal'+index).attr("disabled", true);
			       	   $('#payoutFinalCalc'+index).attr("disabled", true);
				}
	       }
	    	else
	    	{
	    		hideLayer('payoutFinalDescription'+index);
	    		hideLayer('payoutFinalValue'+index);
	    		hideLayer('payoutFinalQuantity'+index);
	    		hideLayer('payoutFinalAwardAmount'+index);
	    		document.getElementById('finalFixedAmount'+index).value = '';
		    	document.getElementById('finalRangeAmountMin'+index).value = '';
		    	document.getElementById('finalRangeAmountMax'+index).value = '';
		    	document.getElementById('payoutFinalCalc'+index).value = '';
		    	document.getElementById('finalPayoutDescription'+index).value = '';
		    	document.getElementById('finalPayoutValue'+index).value = '';
		    	document.getElementById('finalPayoutCurrency'+index).value = '';
		    	document.getElementById('finalQuantity'+index).value = '';
	    	}
			
		   // Show the appropriate amount label
		   if( value == 'cash' )
		   {
			   hideLayer('payoutFinalAwardAmount'+index+'-points-label');
			   showLayer('payoutFinalAwardAmount'+index+'-usd-label');
		   }
		   else
		   {
			   hideLayer('payoutFinalAwardAmount'+index+'-usd-label');
			   showLayer('payoutFinalAwardAmount'+index+'-points-label');
		   }
	   }
	   
	   // Update visibility of budget and request more budget section
	   updateBudgetSectionDisplay();
	   
	   // Update visibility of awards are taxable option
	   updateTaxableDisplay();
	}
   
   function eachLevelAwardTypeChange( awardType )
	{
	   var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
	   var oneLevelApprovalValue = document.getElementById("oneLevelApproval").value;
	   var awardLevelIndexValue = document.getElementById("awardLevelIndex").value;
	   var firstLevelAwardValue = document.getElementById("firstLevelAward").value;
	   var awardSelectedValue = document.getElementById("awardSelected").value;
	   
	   if( oneLevelApprovalValue == "true" )
		{
		   hideLayer('payoutEachLevel');
		   finalLevelAwardTypeChange(awardType);
		}
	   else
		{
		   if( !awardType )
		   {	
			   for(index=1; index<=eachCount; index++)
			    {
				   var selectBox = document.getElementById('eachAwardsType'+index).value;
				   if( !selectBox)
					{
					    hideLayer('payoutEachDescription'+index);
			   	  		hideLayer('payoutEachValue'+index);
			   			hideLayer('payoutEachQuantity'+index);
			   			hideLayer('payoutEachAwardAmount'+index);
			   			document.getElementById('eachFixedAmount'+index).value = '';
				    	document.getElementById('eachRangeAmountMin'+index).value = '';
				    	document.getElementById('eachRangeAmountMax'+index).value = '';
				    	document.getElementById('payoutEachCalc'+index).value = '';
				    	document.getElementById('eachPayoutDescription'+index).value = '';
				    	document.getElementById('eachPayoutValue'+index).value = '';
				    	document.getElementById('eachPayoutCurrency'+index).value = '';
				    	document.getElementById('eachQuantity'+index).value = '';
				    	document.getElementById('payoutEachCalc'+index).disabled = true;
					}
				   else
					{
					   if( eachCount != 1)
						{
						   if (selectBox == 'other'){
						    	showLayer('payoutEachDescription'+index);
						    	showLayer('payoutEachValue'+index);
						    	showLayer('payoutEachQuantity'+index);
						    	hideLayer('payoutEachAwardAmount'+index);
				   	    	}else if( selectBox == 'cash' || selectBox == 'points') {
				        		hideLayer('payoutEachDescription'+index);
				        		hideLayer('payoutEachValue'+index);
				        		hideLayer('payoutEachQuantity'+index);
				        		showLayer('payoutEachAwardAmount'+index);
				        		if ( awardSelectedValue == "false" )
				        		{
					        		if( selectBox == 'points')
					        		{
					           			showLayer('payoutEachCalculator'+index);
					        		}
					        		else
					        		{
					        			hideLayer('payoutEachCalculator'+index);
					        		}
				        		}
					   		}
						   
						   // Show the appropriate amount label
						   if( selectBox == 'cash' )
						   {
							   hideLayer('payoutEachAwardAmount'+index+'-points-label');
							   showLayer('payoutEachAwardAmount'+index+'-usd-label');
						   }
						   else
						   {
							   hideLayer('payoutEachAwardAmount'+index+'-usd-label');
							   showLayer('payoutEachAwardAmount'+index+'-points-label');
						   }
						}
					  
					}
		   	     }
		   }
	   else{
		   var value = awardType.value;
			var id = awardType.id;
			var index = id.charAt(id.length-1);

			if (value == 'other')
			{
			    	showLayer('payoutEachDescription'+index);
			    	showLayer('payoutEachValue'+index);
			    	showLayer('payoutEachQuantity'+index);
			    	hideLayer('payoutEachAwardAmount'+index);
			    	document.getElementById('eachFixedAmount'+index).value = '';
			    	document.getElementById('eachRangeAmountMin'+index).value = '';
			    	document.getElementById('eachRangeAmountMax'+index).value = '';
			    	document.getElementById('payoutEachCalc'+index).value = '';
	   	    }
			else if( value == 'cash' || value == 'points') 
	   	    {
	        	hideLayer('payoutEachDescription'+index);
	        	hideLayer('payoutEachValue'+index);
	        	hideLayer('payoutEachQuantity'+index);
	        	showLayer('payoutEachAwardAmount'+index);
        		showLayer('payoutEachCalculator'+index);
	       		document.getElementById('eachAwardAmountTypeFixedTrue'+index).checked = true;
	       		document.getElementById('eachFixedAmount'+index).value = '';
		    	document.getElementById('eachRangeAmountMin'+index).value = '';
		    	document.getElementById('eachRangeAmountMax'+index).value = '';
		    	document.getElementById('payoutEachCalc'+index).value = '';
	       		document.getElementById('eachPayoutDescription'+index).value = '';
		    	document.getElementById('eachPayoutValue'+index).value = '';
		    	document.getElementById('eachPayoutCurrency'+index).value = '';
		    	document.getElementById('eachQuantity'+index).value = '';
		    	document.getElementById('eachFixedAmount'+index).disabled = false;
		    	document.getElementById('eachRangeAmountMin'+index).disabled = true;
		    	document.getElementById('eachRangeAmountMax'+index).disabled = true;
		    	document.getElementById('payoutEachCalc'+index).disabled = true;
		 	   if( awardSelectedValue == "true" )
			   {		 		   
		 		   var previousAwardIndexValue = null;
		 		   if ( awardLevelIndexValue != null )
		 		   {
		 			  previousAwardIndexValue = awardLevelIndexValue - 1;
		 		   }
		 		  if ( awardLevelIndexValue != null && ( awardLevelIndexValue == index || previousAwardIndexValue == index ))
		 		  {
			 		   document.getElementById('eachAwardAmountTypeFixedTrue'+index).disabled = true;
					   document.getElementById('eachFixedAmount'+index).disabled = true; 
					   document.getElementById('eachCalAwardAmtTypeFixedCal'+index).disabled = true;
					   document.getElementById('payoutEachCalc'+index).disabled = true;
					   document.getElementById('eachAwardAmountTypeFixedFalse'+index).checked = true;
					   document.getElementById('eachRangeAmountMin'+index).disabled = false;
					   document.getElementById('eachRangeAmountMax'+index).disabled = false;
		 		  }					
				 
			   }
            }
	   	  else
		     {
	   		hideLayer('payoutEachDescription'+index);
	   		hideLayer('payoutEachValue'+index);
	   		hideLayer('payoutEachQuantity'+index);
	   		hideLayer('payoutEachAwardAmount'+index);
	   		document.getElementById('eachFixedAmount'+index).value = '';
	    	document.getElementById('eachRangeAmountMin'+index).value = '';
	    	document.getElementById('eachRangeAmountMax'+index).value = '';
	    	document.getElementById('payoutEachCalc'+index).value = '';
	    	document.getElementById('eachPayoutDescription'+index).value = '';
	    	document.getElementById('eachPayoutValue'+index).value = '';
	    	document.getElementById('eachPayoutCurrency'+index).value = '';
	    	document.getElementById('eachQuantity'+index).value = '';
		    }
			
		   // Show the appropriate amount label
		   if( value == 'cash' )
		   {
			   hideLayer('payoutEachAwardAmount'+index+'-points-label');
			   showLayer('payoutEachAwardAmount'+index+'-usd-label');
		   }
		   else
		   {
			   hideLayer('payoutEachAwardAmount'+index+'-usd-label');
			   showLayer('payoutEachAwardAmount'+index+'-points-label');
		   }
	   }
		}
	   
	   if( $("#awardSelected").val() == "true" )
	   {		   
			var previousAwardIndexValue = null;
	 		if ( awardLevelIndexValue != null )
	 		{
	 		   previousAwardIndexValue = awardLevelIndexValue - 1;
	 		}
	 		if ( awardLevelIndexValue != null && ( awardLevelIndexValue == index || previousAwardIndexValue == index ))
	 		{
	 		  document.getElementById('eachAwardAmountTypeFixedTrue'+index).disabled = true;
	 		  document.getElementById('eachFixedAmount'+index).disabled = true;	 		  
			  document.getElementById('eachCalAwardAmtTypeFixedCal'+index).disabled = true;
			  document.getElementById('payoutEachCalc'+index).disabled = true; 
			  document.getElementById('eachAwardAmountTypeFixedFalse'+index).checked = true;
			  document.getElementById('eachRangeAmountMin'+index).disabled = false;
		      document.getElementById('eachRangeAmountMax'+index).disabled = false;
	 		}			 
			
	   }
	   
	   // Update visibility of budget and request more budget section
	   updateBudgetSectionDisplay();
	   
	   // Update visibility of awards are taxable option
	   updateTaxableDisplay();
	}

	// If any of the selected award types are points, then show points budget sections.  Otherwise hide them.
	// If any of the selected award types are cash, then show cash budget sections. Otherwise hide them.
	// On hide, resets values as well.
	function updateBudgetSectionDisplay()
	{
		// Point levels
		var numPointTypes = $("select[name$='awardsType']").filter(
				function(index)
				{
					return this.value == 'points'
				}).length;
		
		// Cash levels
		var numCashTypes = $("select[name$='awardsType']").filter(
				function(index)
				{
					return this.value == 'cash'
				}).length;

		// Hide / Show point budget section
		if (numPointTypes > 0)
		{
			showLayer('budgetInfo');
		}
		else
		{
			$("#budgetOptionNone").click();
			hideLayer('budgetInfo');
		}

		// Hide / Show cash budget section
		if (numCashTypes > 0)
		{
			showLayer('cashBudgetInfo');
		}
		else
		{
			$("#cashBudgetOptionNone").click();
			hideLayer('cashBudgetInfo');
		}
		
		// Hide / Show request more budget section
		if (numPointTypes > 0 || numCashTypes >  0)
		{
			showLayer('requestMoreBudgetRow');
		}
		else
		{
			$("#requestMoreBudgetFalse").click();
			hideLayer('requestMoreBudgetRow');
		}
	}
	
	// If any award type options are points or cash, then the awards are taxable option is shown
	// Otherwise it is not (i.e. nothing selected or only other is selected)
	function updateTaxableDisplay()
    {
		var numCashOrPointsTypes = $("select[id*='AwardsType']:has(option[value='points']:selected,option[value='cash']:selected)").length;
		
		if(numCashOrPointsTypes == 0)
		{
			$("#taxableRow").hide();
		}
		else
		{
			$("#taxableRow").show();
		}
    }

	/*  function awardTypeChange()
	{
		var awardType = document.getElementById("awardsTypeText").value;
		if (awardType == 'other'){
		 		showLayer("payoutDescription");  
	  		showLayer( "payoutValue" ) ;
	  		hideLayer("budgetInfo");
	  	}else if( awardType == 'points' ){
	       	showLayer("budgetInfo");
	       	document.getElementById("payoutDescriptionText").value="";
			  	document.getElementById("payoutValueText").value="";
			  	document.getElementById("payoutCurrencyText").value="";
	 	  	hideLayer("payoutDescription");
	  	  	hideLayer( "payoutValue" ) ;
	  	}else{
				document.getElementById("payoutDescriptionText").value="";
				document.getElementById("payoutValueText").value="";
				document.getElementById("payoutCurrencyText").value="";
	 		hideLayer("payoutDescription");
	  		hideLayer( "payoutValue" ) ;
	  		hideLayer("budgetInfo");
	  	} 
	} */

	function enableFields() {
		var awardsActiveFalseObj = document.getElementById("awardsActiveFalse"); //"Awards Active? No"
		var disabled = awardsActiveFalseObj.checked == true;
		var expired = false;

		// if promotion is live then disable the awardsactive radio buttons.

		// if promotion is expired then disabled = true;
		<c:if test="${promotionStatus=='expired'}">
		disabled = true;
		expired = true;
		</c:if>
		
		// For stuff that doesn't care about whether awards are active or not, but should still disable
		// on expired promotions
		if(expired)
		{
			timePeriodActiveTrueObj.disabled = true;
			timePeriodTrueObj.disabled = true;
			timePeriodFalseObj.disabled = true;

			var count = document.promotionAwardsForm.nominationTimePeriodVBListSize.value;
			for (i = 0; i < count; i++)
			{
				var timePeriodObj = document.getElementById('timePeriod' + i);
				var timePeriodNameObj = document
						.getElementById('timePeriodName' + i);
				var timePeriodStartDateObj = document
						.getElementById('timePeriodStartDate' + i);
				var timePeriodEndDateObj = document
						.getElementById('timePeriodEndDate' + i);
				var maxSubmissionAllowedObj = document
						.getElementById('maxSubmissionAllowed' + i);
				var maxNominationsAllowedObj = document
						.getElementById('maxNominationsAllowed' + i);
				var maxWinsllowedObj = document.getElementById('maxWinsllowed'
						+ i);
				//var removeTimePeriodObj = document.getElementById('removeTimePeriod'+i);
				//var addAnotherhrefObj = document.getElementById("addAnotherhref");

				timePeriodNameObj.disabled = true;
				timePeriodStartDateObj.disabled = true;
				timePeriodEndDateObj.disabled = true;
				maxSubmissionAllowedObj.disabled = true;
				maxNominationsAllowedObj.disabled = true;
				maxWinsllowedObj.disabled = true;
				//removeTimePeriodObj.disabled=true;
				//addAnotherhrefObj.disabled=true;
			}
		}
		else
		{
			timePeriodActiveTrueObj.disabled = false;
			timePeriodTrueObj.disabled = false;
			timePeriodFalseObj.disabled = false;
			
			var count = document.promotionAwardsForm.nominationTimePeriodVBListSize.value;
			for (i = 0; i < count; i++)
			{
				var timePeriodObj = document.getElementById('timePeriod' + i);
				var timePeriodNameObj = document
						.getElementById('timePeriodName' + i);
				var timePeriodStartDateObj = document
						.getElementById('timePeriodStartDate' + i);
				var timePeriodEndDateObj = document
						.getElementById('timePeriodEndDate' + i);
				var maxSubmissionAllowedObj = document
						.getElementById('maxSubmissionAllowed' + i);
				var maxNominationsAllowedObj = document
						.getElementById('maxNominationsAllowed' + i);
				var maxWinsllowedObj = document.getElementById('maxWinsllowed'
						+ i);
				//var removeTimePeriodObj = document.getElementById('removeTimePeriod'+i);
				//var addAnotherhrefObj = document.getElementById("addAnotherhref");

				timePeriodNameObj.disabled = false;
				timePeriodStartDateObj.disabled = false;
				timePeriodEndDateObj.disabled = false;
				maxSubmissionAllowedObj.disabled = false;
				maxNominationsAllowedObj.disabled = false;
				maxWinsllowedObj.disabled = false;
				//removeTimePeriodObj.disabled=false;
				//addAnotherhrefObj.disabled=false;
			}
		}

		// Max submission allowed field - only enabled if awards active AND time period selected
		// TODO: Incorporate time period into logic
		if (!disabled) {
			payoutEachLevelTrueObj.disabled = false;
			payoutEachLevelFalseObj.disabled = false;
			payoutFinalLevelTrueObj.disabled = false;
			payoutFinalLevelFalseObj.disabled = false;

			awardsTaxableTrueObj.disabled = false;
			awardsTaxableFalseObj.disabled = false;

			var recommendedAward = document.getElementById('recommendedAward');
			recommendedAward.style.display="";

			requestMoreBudgetFalseObj.disabled = false;
			requestMoreBudgetTrueObj.disabled = false;

			// Re-enable award type selection
			if ($("#oneLevelApproval").val() == "true") {
				$("select[name$='awardsType']").attr('disabled', '');
			}
		} else {
			//payoutEachLevelTrueObj.disabled = true;
			//payoutEachLevelFalseObj.disabled = true;
			//payoutFinalLevelTrueObj.disabled = true;
			//payoutFinalLevelFalseObj.disabled = true;

			//payoutEachLevelFalseObj.checked = true;
			enablePayoutEachLevel();
			//payoutFinalLevelFalseObj.checked = true;
			enablePayoutFinalLevel();

			awardsTaxableTrueObj.disabled = true;
			awardsTaxableFalseObj.disabled = true;

			var recommendedAward = document.getElementById('recommendedAward');
			recommendedAward.style.display='none';

			requestMoreBudgetFalseObj.disabled = true;
			requestMoreBudgetTrueObj.disabled = true;

			// If awards are disabled and only one approver level, clear the award type option
			if ($("#oneLevelApproval").val() == "true") {
			
				$("select[name$='awardsType']").attr('enabled', 'enabled');
				$("select[name$='awardsType']").each(eachLevelAwardTypeChange);
			}
		}

		//Below Awards fields follow the selection made in the "Awards Active?" radio button

		//awardAmountTypeFixedFalseObj.disabled=disabled;
		//awardAmountTypeFixedTrueObj.disabled=disabled;
		//fixedAmountObj.disabled=disabled;

		// when awards active is true and fixedAmount is set, then the rangeAmountMin/Max field and nominationAwardSpecifierTypeCode field
		//should be disabled.

		/* if(fixedAmountObj.checked == true && awardsActiveFalseObj.checked == false)
		{
			rangeAmountMinObj.disabled=false;
		    rangeAmountMaxObj.disabled=false;
		    nominationAwardSpecifierTypeCodeObj.disabled=false;
		 }else{
			 rangeAmountMinObj.disabled=true;
		     rangeAmountMaxObj.disabled=true;
		     nominationAwardSpecifierTypeCodeObj.disabled=true;
		 }  */

		var promoStatus = $("input[name='live']").val();
		var budgetNotDisabled = disabled;
		var cashBudgetNotDisabled = disabled;
		if (promoStatus == 'true') {
			if (disabled) {
				document.getElementById("awardsActive").value = false;
			} else {
				document.getElementById("awardsActive").value = true;
			}

			if (budgetOptionExistsObj && budgetOptionExistsObj.checked == true) {
				console.log('budgetNotDisabled:' + budgetNotDisabled);
				budgetNotDisabled = false;
			} else {
				budgetNotDisabled = disabled;
			}

			if (cashBudgetOptionExistsObj && cashBudgetOptionExistsObj.checked == true) {
				console.log('cashBudgetNotDisabled:' + cashBudgetNotDisabled);
				cashBudgetNotDisabled = false;
			} else {
				cashBudgetNotDisabled = disabled;
			}
		}
		
		budgetOptionNoneObj.disabled = disabled;
		budgetOptionExistsObj.disabled = budgetNotDisabled;
		budgetOptionNewObj.disabled = disabled;
		// when awards active is true and budgetOptionExists is not set, then the budgetMasterId field should be disabled.
		budgetOptionExistsObj.disabled = budgetNotDisabled;
		if (budgetOptionExistsObj && budgetOptionExistsObj.checked == true) {
			budgetMasterIdObj.disabled = budgetNotDisabled;
		} else {
			budgetMasterIdObj.disabled = true;
		}
		budgetMasterNameObj.disabled = disabled;
		if (budgetCapTypeHardObj != null) {
			budgetCapTypeHardObj.disabled = disabled;
		}
		
		cashBudgetOptionNoneObj.disabled = disabled;
		cashBudgetOptionExistsObj.disabled = cashBudgetNotDisabled;
		cashBudgetOptionNewObj.disabled = disabled;
		// when awards active is true and budgetOptionExists is not set, then the budgetMasterId field should be disabled.
		cashBudgetOptionExistsObj.disabled = cashBudgetNotDisabled;
		if (cashBudgetOptionExistsObj && cashBudgetOptionExistsObj.checked == true) {
			cashBudgetMasterIdObj.disabled = cashBudgetNotDisabled;
		} else {
			cashBudgetMasterIdObj.disabled = true;
		}
		cashBudgetMasterNameObj.disabled = disabled;
		if (cashBudgetCapTypeHardObj != null) {
			cashBudgetCapTypeHardObj.disabled = disabled;
		}

		// when the awards active is set to false all the default values will be set for the page.
		if (awardsActiveFalseObj.checked == true) {
			//$("input:radio[id='awardAmountTypeFixedTrue']").attr('checked',true);
			//$("input[id='fixedAmount']").val("");
			// $("input[id='rangeAmountMin']").val("");
			//$("input[id='rangeAmountMax']").val("");
			//$("select[name='nominationAwardSpecifierTypeCode'] option[value='']").attr('selected', 'selected');
			if (promoStatus != 'true') {
				$("select[name='budgetMasterId'] option[value='']").attr(
						'selected', 'selected');
				$("input:radio[id='budgetOptionNone']").attr('checked', true);
				
				$("select[name='cashBudgetMasterId'] option[value='']").attr(
						'selected', 'selected');
				$("input:radio[id='cashBudgetOptionNone']").attr('checked', true);
			}
			hideLayer("newBudget");
			hideLayer("newCashBudget");
		}

		// If cumulative approval, force recommended award to 'No' (disable it so it can't be switched on)
		if ($("#cumulativeApproval").val() == "true") {
			nominatorRecommendedAwardFalseObj.disabled = true;
			nominatorRecommendedAwardTrueObj.disabled = true;
		}
		
	   // Initial visibility of budget and request more budget section
	   updateBudgetSectionDisplay();
	   
	   // Initial visibility of awards are taxable option
	   updateTaxableDisplay();
	   
		if( $("#firstLevelAward").val() == "true" )
		{
		   payoutEachLevelTrueObj.disabled = true;
		   payoutEachLevelFalseObj.disabled = true;
		}
		
		   var eachCount = document.promotionAwardsForm.nominationPayoutEachLevelListSize.value;
		   var oneLevelApprovalValue = document.getElementById("oneLevelApproval").value;
		   var awardLevelIndexValue = document.getElementById("awardLevelIndex").value;
		   var firstLevelAwardValue = document.getElementById("firstLevelAward").value;
		   var awardSelectedValue = document.getElementById("awardSelected").value;
		   
		   if( $("#awardSelected").val() == "true" )
		   {
			   var awardLevelIndexValue = document.getElementById("awardLevelIndex").value;
			   var previousAwardLevelIndexValue = null;
			   if ( awardLevelIndexValue != null )
			   {
			 	  previousAwardLevelIndexValue = awardLevelIndexValue - 1;  
			   } 
			   
			   for(index=1; index<=eachCount; index++)
			    {
				   var selectBox = document.getElementById('eachAwardsType'+index).value;
				   if( selectBox == 'cash' || selectBox == 'points')
				    {					    					    
					   if ( awardLevelIndexValue != null && ( awardLevelIndexValue == index || previousAwardLevelIndexValue == index ) )
					   {
						    document.getElementById('eachAwardAmountTypeFixedTrue'+index).disabled = true;						
							document.getElementById('eachFixedAmount'+index).disabled = true; 
							document.getElementById('eachCalAwardAmtTypeFixedCal'+index).disabled = true;
							document.getElementById('payoutEachCalc'+index).disabled = true; 
							document.getElementById('eachAwardAmountTypeFixedFalse'+index).checked = true;
							document.getElementById('eachRangeAmountMin'+index).disabled = false;
						    document.getElementById('eachRangeAmountMax'+index).disabled = false;
					   }						
					   				    	
				   }
			
			    }
		   }

	}
</script>