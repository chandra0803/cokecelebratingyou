<%@ include file="/include/taglib.jspf"%>

  <SCRIPT TYPE="text/javascript">
      function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          if (document.getElementById(whichLayer))
          {
	          var style2 = document.getElementById(whichLayer).style;
	          style2.display = "";
	          if( whichLayer == "budgetInfo")
	        	  {
	        	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one when none or create
	        	  	//budget is selected
		        	  if ( $("input:radio[name='budgetOption']:checked").val()== "none" || $("input:radio[name='budgetOption']:checked").val()== "new")
		       		   {
		       		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
		       			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
		       		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
		       		   {
		       		   	 $("select[name='budgetMasterId']").attr('disabled', '');
			       	   }
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
          if (document.getElementById(whichLayer))
          {
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
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
  </SCRIPT>

  <SCRIPT type="text/javascript">
      
	  function showEnabledFeatureAwards()
	  {
	  	showLayer("featuredAwardsSpacerLyr") ;
	  	showLayer("featuredAwardsLyr") ;
	  }
	  function hideEnabledFeatureAwards()
	  {
		hideLayer("featuredAwardsSpacerLyr") ;
	  	hideLayer("featuredAwardsLyr") ;
	  }
	  function showWhatsNew()
	  {
	  	showLayer("whatsNewLyr") ;
	  	showLayer("whatsNewSpacerLyr") ;
        showLayer("stdWhatsNewItems") ;
	  }
	  function hideWhatsNew()
	  {
	  	hideLayer("whatsNewLyr") ;
	  	hideLayer("whatsNewSpacerLyr") ;
	  }
      function hideNotification()
      {
		 hideLayer("notificationLyr");
		 hideLayer("notificationSpacerLyr");
	  }
      function showNotification()
      {
		 showLayer("notificationLyr");
		 showLayer("notificationSpacerLyr");
	  }
      function hideApqConversion()
      {
		 hideLayer("apqConversionLyr");
		 hideLayer("apqConversionSpacerLyr");
	  }
      function showApqConversion()
      {
		 showLayer("apqConversionLyr");
		 showLayer("apqConversionSpacerLyr");
	  }
      function hideMerchCalculator()
      {
		 hideLayer("merchCalculatorLyr");
		 hideLayer("merchCalculatorSpacerLyr");
	  }
      function showMerchCalculator()
      {
		 showLayer("merchCalculatorLyr");
		 showLayer("merchCalculatorSpacerLyr");
	  }      
      //Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)   
        hideLayer("budgetInfo");
        hideLayer("budgetTrackerOption");
        hideLayer("newBudget");
        //hideLayer("softCapApprover"); TODO
        <c:if test="${promotionAwardsForm.budgetOption == 'none' }">
          hideLayer("budgetAmountLabel");
          hideLayer('finalPayoutRule_radio');
          hideLayer("newBudgetCentralValue");
          hideLayer("newBudgetCentralTitle");
        </c:if>        
       
        var budgetOptionNoneObj = document.getElementById("budgetOptionNone");
        var budgetOptionExistsObj = document.getElementById("budgetOptionExists");
        var budgetOptionNewObj = document.getElementById("budgetOptionNew");
        
        var budgetMasterIdObj = document.getElementById("budgetMasterId");
        var budgetMasterNameObj = document.getElementById("budgetMasterName");
        
        var budgetTypePaxObj = document.getElementById("budgetTypePax");
        var budgetTypeNodeObj = document.getElementById("budgetTypeNode");
        var budgetTypeCentralObj = document.getElementById("budgetTypeCentral");
        
        var budgetCapTypeHardObj = document.getElementById("budgetCapTypeHard");
        
        var fileloadBudgetAmountFalseObj = document.getElementById("fileloadBudgetAmountFalse");
        var fileloadBudgetAmountTrueObj = document.getElementById("fileloadBudgetAmountTrue");

        var showInBudgetTrackerFalseObj = document.getElementById("showInBudgetTrackerFalse");
        var showInBudgetTrackerTrueObj = document.getElementById("showInBudgetTrackerTrue");
        
        var allowBudgetReallocationEligTypeObj = document.getElementById("budgetReallocationEligTypeCode");
        
        if( budgetOptionExistsObj && budgetOptionExistsObj.checked == true )
        {
        	var budgetTypeSelected = $("#budgetMasterId option:selected").attr('style');
     		if( budgetTypeSelected == 'central')
     		{
     			showInBudgetTrackerFalseObj.disabled = true;
     		    showInBudgetTrackerTrueObj.disabled = true;
     		}
     		else
     		{
     			showInBudgetTrackerFalseObj.disabled = false;
     		    showInBudgetTrackerTrueObj.disabled = false;
     		}
        }
        
<c:if test="${promotionAwardsForm.awardsType == 'points'}">
        var budgetSweepEnabledFalseObj = document.getElementById("budgetSweepEnabledFalse");
        var budgetSweepEnabledTrueObj = document.getElementById("budgetSweepEnabledTrue");
        var budgetOptionSweepExistsObj = document.getElementById("budgetOptionSweepExists");
</c:if>
<c:if test="${promotionAwardsForm.awardsType != 'merchandise'}">
        var awardAmountTypeFixedFalseObj = document.getElementById("awardAmountTypeFixedFalse");
        var awardAmountTypeFixedTrueObj = document.getElementById("awardAmountTypeFixedTrue");
        var fixedAmountObj = document.getElementById("fixedAmount");
        var rangeAmountMinObj = document.getElementById("rangeAmountMin");
        var rangeAmountMaxObj = document.getElementById("rangeAmountMax");
        var scoreByObj =  document.getElementById("scoreBy");
        var calculatorIdObj =  document.getElementById("calculatorId");
       <%-- var calcTypeObj =  document.getElementById("awardAmountTypeCalculatorFalse"); --%>
        var calculatorAwardAmountTypeFixedFalseObj = document.getElementById("calculatorAwardAmountTypeFixedFalse");
        var calculatorAwardAmountTypeFixedCalObj = document.getElementById("calculatorAwardAmountTypeFixedCal");
</c:if>
<c:if test="${promotionAwardsForm.awardsType == 'merchandise'}">
        var awardStructureObj = document.getElementById("awardStructure");
        var apqConversionFalseObj =  document.getElementById("apqConversionFalse");
        var apqConversionTrueObj  =  document.getElementById("apqConversionTrue");
        var featuredAwardsFalseObj =  document.getElementById("featuredAwardsFalse");
        var featuredAwardsTrueObj  =  document.getElementById("featuredAwardsTrue");
        if(typeof(awardStructureObj) !== 'undefined' && awardStructureObj != null)
        {
	        if( awardStructureObj.value=='level')
	        {
	        	showWhatsNew() ;
	        	showEnabledFeatureAwards();
	        }
	        else
	        {
	        	hideWhatsNew() ;
	        	hideEnabledFeatureAwards() ;
	        }
        }
        var stdProductIdObj  =  document.getElementById("stdProductId");
        var noNotificationFalseObj = document.getElementById("noNotificationFalse");
        var noNotificationTrueObj = document.getElementById("noNotificationTrue");
        var useRecognitionCalculatorFalseObj = document.getElementById("useRecognitionCalculatorFalse");
        var useRecognitionCalculatorTrueObj = document.getElementById("useRecognitionCalculatorTrue");
</c:if>
     var awardsTypeObj = document.getElementById("awardsType");
     var issuanceTypeObj = document.getElementById("promotionIssuanceTypeCode");
   //End inline javascript
       
    function displayBudgetAmount( index ){
		if( budgetOptionNewObj && budgetOptionNewObj.checked==true ){
	        if(budgetTypeCentralObj!=null&&budgetTypeCentralObj!='undefined'){
			        if (budgetTypeCentralObj.checked == true){
			          showLayer("newBudgetCentralValue"+index);
			        }else{
		        	hideLayer("newBudgetCentralValue"+index);
		     	}
	    	}else{
		        	hideLayer("newBudgetCentralValue"+index);
	        	}
	     }else{
	        hideLayer("newBudgetCentralValue"+index);
	     }
	}
			
    function displaySweepDate( index ){
        if( budgetSweepEnabledTrueObj!=null && budgetSweepEnabledTrueObj!='undefined' && budgetSweepEnabledTrueObj.checked == true)
		  {
			  if(budgetTypePaxObj!=null && budgetTypePaxObj!='undefined' && budgetTypePaxObj.checked == true)
			  {
				  showLayer("budgetSweepDateTD"+index);
			  }else{
				  hideLayer("budgetSweepDateTD"+index);
			  }
		  }else{
			  hideLayer("budgetSweepDateTD"+index);
		  }
	} 			

	function updateLayerNewBudgetCentralValue(){
		  var count = $("input[name='budgetSegmentVBListSize']").val();
		  for(i=0; i<= count; i++){
			  displayBudgetAmount(i);
			  <c:choose>
		    	 <c:when test="${promotionAwardsForm.awardsType != 'merchandise'}">
			  displaySweepDate(i);
			 	 </c:when>
		 		 <c:otherwise>
		 			 hideLayer("budgetSweepDateTD"+i);
		 		 </c:otherwise>
	 		  </c:choose>
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
	
    function updateLayersShown()
    {
     destination = awardsTypeObj.value;
     var showInBudgetTrackerFalseObj = document.getElementById("showInBudgetTrackerFalse");
     var showInBudgetTrackerTrueObj = document.getElementById("showInBudgetTrackerTrue");
     var budgetTypeSelected = $("#budgetMasterId option:selected").attr('style');
     
     if( destination == 'points' )
     {
	     if((budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true) && (budgetOptionNewObj && budgetOptionNewObj.checked != true))
	     {
	        budgetTypePaxObj.checked=true;
	        budgetTypeNodeObj.disabled=false;
	        if(budgetTypeCentralObj!=null&&budgetTypeCentralObj!='undefined')
	    	{
	        	budgetTypeCentralObj.disabled=false;
	    	}
	        budgetCapTypeHardObj.checked=true;
	     }

	     if( budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true && budgetOptionNoneObj && budgetOptionNoneObj.checked == true )
	     {
	    	    var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	    budgetMasterIdObj.style.visibility = 'hidden';
	    	    document.getElementById('budgetOptionExists').checked = false;
	    	    document.getElementById('budgetMasterId').style.visibility = 'hidden';

		    	var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		    	budgetMasterSweepIdObj.style.visibility = 'visible';
		    	document.getElementById('budgetSweepMasterId').style.visibility = 'visible';
	     }

	     if( budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true && budgetOptionNewObj && budgetOptionNewObj.checked == true )
	     {
	    	    var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	    budgetMasterIdObj.style.visibility = 'hidden';
	    	    document.getElementById('budgetOptionExists').checked = false;
	    	    document.getElementById('budgetMasterId').style.visibility = 'hidden';

		    	var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		    	budgetMasterSweepIdObj.style.visibility = 'visible';
		    	document.getElementById('budgetSweepMasterId').style.visibility = 'visible';
	     }
	     

	     if( budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true && budgetOptionNewObj && budgetOptionNewObj.checked != true && budgetOptionNoneObj && budgetOptionNoneObj.checked != true )
	     {
	    	    var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	    budgetMasterIdObj.style.visibility = 'hidden';
	    	    document.getElementById('budgetOptionExists').checked = false;
	    	    document.getElementById('budgetMasterId').style.visibility = 'hidden';

		    	var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		    	budgetMasterSweepIdObj.style.visibility = 'visible';
			    document.getElementById('budgetOptionSweepExists').checked = true;
			    document.getElementById('budgetSweepMasterId').style.visibility = 'visible';
			    document.getElementById('budgetSweepMasterId').disabled=false;
	     }

	     if( ( budgetSweepEnabledFalseObj && budgetSweepEnabledFalseObj.checked == true ) && budgetOptionNewObj && budgetOptionNewObj.checked != true && budgetOptionNoneObj && budgetOptionNoneObj.checked != true && budgetOptionExistsObj && budgetOptionExistsObj.checked != true)
		 {
	    	 var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	 budgetMasterIdObj.style.visibility = 'visible';
	    	 document.getElementById('budgetOptionExists').checked = false;
	    	 document.getElementById('budgetMasterId').style.visibility = 'visible';

		     var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		     budgetMasterSweepIdObj.style.visibility = 'hidden';
		     document.getElementById('budgetOptionSweepExists').checked = false;
		     document.getElementById('budgetSweepMasterId').style.visibility = 'hidden';
	     }
	     
	     if( (budgetSweepEnabledFalseObj && budgetSweepEnabledFalseObj.checked == true) && budgetOptionNewObj && budgetOptionNewObj.checked != true && budgetOptionNoneObj && budgetOptionNoneObj.checked != true && budgetOptionExistsObj && budgetOptionExistsObj.checked == true)
		 {
	    	 var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	 budgetMasterIdObj.style.visibility = 'visible';
	    	 document.getElementById('budgetOptionExists').checked = true;
	    	 document.getElementById('budgetMasterId').style.visibility = 'visible';

		     var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		     budgetMasterSweepIdObj.style.visibility = 'hidden';
		     document.getElementById('budgetOptionSweepExists').checked = false;
		     document.getElementById('budgetSweepMasterId').style.visibility = 'hidden';
		     
	     	 if( budgetTypeSelected != 'central')
	     	 {
	     		showInBudgetTrackerFalseObj.disabled = false;
	     		showInBudgetTrackerTrueObj.disabled = false;
	     	 }
	     }

	     if( (budgetSweepEnabledFalseObj && budgetSweepEnabledFalseObj.checked == true) && budgetOptionNewObj && budgetOptionNewObj.checked == true )
		 {
	    	 var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	 budgetMasterIdObj.style.visibility = 'visible';
	    	 document.getElementById('budgetOptionExists').checked = true;
	    	 document.getElementById('budgetMasterId').style.visibility = 'visible';

		     var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		     budgetMasterSweepIdObj.style.visibility = 'hidden';
		     document.getElementById('budgetOptionSweepExists').checked = false;
		     document.getElementById('budgetSweepMasterId').style.visibility = 'hidden';

		     budgetOptionNewObj.checked = true;
	     }
	     
	     
	     if( (budgetSweepEnabledFalseObj && budgetSweepEnabledFalseObj.checked == true) && (budgetOptionNewObj && budgetOptionNewObj.checked != true) && ( budgetOptionNoneObj && budgetOptionNoneObj.checked == true ) )
		 {
	    	    budgetOptionNoneObj.checked=true;
	    	    var budgetMasterIdObj = document.getElementById('budgetMasterRowId');
	    	    budgetMasterIdObj.style.visibility = 'visible';
	    	    document.getElementById('budgetOptionExists').checked = false;
	    	    document.getElementById('budgetMasterId').style.visibility = 'visible';

		    	var budgetMasterSweepIdObj = document.getElementById('budgetMasterSweepRowId');
		    	budgetMasterSweepIdObj.style.visibility = 'hidden';
		    	document.getElementById('budgetOptionExists').checked = false;
		    	document.getElementById('budgetSweepMasterId').style.visibility = 'hidden';	    	 
	     }     
     }
     
     <c:if test="${promotionAwardsForm.awardsType == 'points'}">
     if( budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true )
  	  {
     	budgetOptionNoneObj.disabled = true;
 	  }
	  else{
 		 if( budgetOptionExistsObj && budgetOptionExistsObj.checked == false  &&  budgetOptionNewObj && budgetOptionNewObj.checked == false )
   		 {
 			budgetOptionNoneObj.checked = true;
   		 }
 	  }
 	</c:if>
     
     if( destination == 'points' || destination == 'merchandise' ){
       showLayer("budgetInfo");
       showLayer("budgetTrackerOption");
      }else{
       hideLayer("budgetInfo");
       hideLayer("budgetTrackerOption");
     }
    issuance = issuanceTypeObj.value;
    if( issuance == 'file_load' )
    {
       hideLayer("budgetInfo");
       hideLayer("budgetTrackerOption");
    }
    
     if( budgetOptionNewObj && budgetOptionNewObj.checked==true ){
        showLayer("newBudget");
    	<c:if test="${promotionAwardsForm.awardsType == 'points'}">
	     if((budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true))
	     {	
	        budgetTypePaxObj.checked=true;
	        budgetTypeNodeObj.disabled=false;
	        if(budgetTypeCentralObj!=null&&budgetTypeCentralObj!='undefined')
	    	{
	        	budgetTypeCentralObj.disabled=false;
	    	}
	        budgetCapTypeHardObj.checked=true;
	     }
		</c:if>
        updateLayerNewBudgetCentralValue();
        if(budgetTypeCentralObj!=null&&budgetTypeCentralObj!='undefined')
    	{
		        if (budgetTypeCentralObj.checked == true){
		          hideLayer("budgetAmountLabel");
		          showLayer('finalPayoutRule_radio');
		          //BugFix 18979
		          showLayer("budgetCapText");
		          showLayer("newBudgetCentralTitle");
		          
		          hideLayer("budgetCapRadio");
		          hideLayer("sweepBudgetDateTitle");
		          showInBudgetTrackerFalseObj.disabled = true;
		          showInBudgetTrackerTrueObj.disabled = true;
		        }
	        else{
	          hideLayer('finalPayoutRule_radio');
	        	showLayer("budgetAmountLabel");
	        	showInBudgetTrackerFalseObj.disabled = false;
	        	showInBudgetTrackerTrueObj.disabled = false;
	        	hideLayer("newBudgetCentralTitle");
	        	var budgetTypeNode = document.getElementById("budgetTypeNode");
	        	if (budgetTypeNode.checked == true){
	        		showLayer("budgetCapText");
	              hideLayer("budgetCapRadio");
	              hideLayer("sweepBudgetDateTitle");
	        	}
	        	else{
	        	  if(destination == 'merchandise'){
	            showLayer("budgetCapText");
	            hideLayer("budgetCapRadio");
	            hideLayer("sweepBudgetDateTitle");
	          }
	          else{
	            hideLayer("budgetCapText");
	            showLayer("budgetCapRadio");
	            if(budgetSweepEnabledTrueObj!=null && budgetSweepEnabledTrueObj!='undefined' && budgetSweepEnabledTrueObj.checked == true)
	            {
	            	showLayer("sweepBudgetDateTitle");
	            }else{
	            	hideLayer("sweepBudgetDateTitle");
	          }
	        	} 	
	     	}
    	}
    	}
        else
        	{
	            hideLayer('finalPayoutRule_radio');
	        	showLayer("budgetAmountLabel");
	        	hideLayer("newBudgetCentralTitle");
	        	showInBudgetTrackerFalseObj.disabled = false;
	    	    showInBudgetTrackerTrueObj.disabled = false;
	        	var budgetTypeNode = document.getElementById("budgetTypeNode");
	        	if (budgetTypeNode.checked == true){
	        		showLayer("budgetCapText");
	              hideLayer("budgetCapRadio");
	              hideLayer("sweepBudgetDateTitle");
	        	}
	        	else{
	        	  if(destination == 'merchandise'){
	            showLayer("budgetCapText");
	            hideLayer("budgetCapRadio");
	            hideLayer("sweepBudgetDateTitle");
	          	}
	          else{
	            hideLayer("budgetCapText");
	            showLayer("budgetCapRadio");
	            if(budgetSweepEnabledTrueObj!=null && budgetSweepEnabledTrueObj!='undefined' && budgetSweepEnabledTrueObj.checked == true)
	            {
	            	showLayer("sweepBudgetDateTitle");
	            }else{
	            	hideLayer("sweepBudgetDateTitle");
	            }
	          }
	         } 	
        	}
     }else{
        hideLayer("newBudget");
        hideLayer('finalPayoutRule_radio');
        hideLayer("budgetAmountLabel");
        hideLayer("newBudgetCentralTitle");
        if ( budgetOptionExistsObj && budgetOptionExistsObj.checked==true )
        {
   		 showLayer("budgetAmountLabel");
   	 	}
     }
     enableFields();
     if(document.getElementById('awardStructure'))
     {
    	var awardStructureObj = document.getElementById('awardStructure');
        if(typeof(awardStructureObj) !== 'undefined' && awardStructureObj != null)
        {
		      if(awardStructureObj.value == 'level')
		      {
		          hideNotification();
		          <c:choose>
		            <c:when test="${spotlightEnabled}">
	         	      hideApqConversion();
		            </c:when>
		            <c:otherwise>
		              showApqConversion();
		            </c:otherwise>
		          </c:choose>
		          showMerchCalculator();	
		          showEnabledFeatureAwards(); 
				  showWhatsNew() ;
		          showLayer("budgetInfo");         
		          showLayer("budgetTrackerOption");         
		       }
        }
	 }     
<c:if test="${promotionAwardsForm.awardsType == 'points'}">
     if((budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true) && (budgetOptionNewObj && budgetOptionNewObj.checked == true))
     {
        budgetTypeNodeObj.disabled=true;
        if(budgetTypeCentralObj!=null&&budgetTypeCentralObj!='undefined')
    	{
        	budgetTypeCentralObj.disabled=true;
    	}
     }
</c:if>

	var budgetSelected = $("input[name='budgetMasterId']").val();
	console.log('budgetSelected:'+document.getElementById("budgetMasterId"));
	//alert(document.getElementById("budgetMasterId").value);
	
	$("#budgetMasterId").change(function ()
	{
		if( budgetTypeSelected == 'central')
		{
			showInBudgetTrackerFalseObj.disabled = true;
		    showInBudgetTrackerTrueObj.disabled = true;
		}
		else
		{
			showInBudgetTrackerFalseObj.disabled = false;
		    showInBudgetTrackerTrueObj.disabled = false;
		}
	});

    }
	      
     updateLayersShown();
     
     function enableFields()
     {  
    var awardsActiveFalseObj = document.getElementById("awardsActiveFalse"); //"Awards Active? No"
    var disabled = awardsActiveFalseObj.checked == true;

 	// early only budget fields were disabled when the promotion was a recognition promotion and a live promotion.
    // now it has been extended to disable the awards active radio buttons as well - this is a recent change made in G5,
    var promoStatus = $("input[name='live']").val();
	var promoTypeCode = $("input[name='promotionTypeCode']").val();
	var budgetNotDisabled=disabled;
	if(promoStatus=='true')
	{
	    if(disabled)
	    {
	    	document.getElementById("awardsActive").value=false;
	    }
	    else
	    {
	    	document.getElementById("awardsActive").value=true;	
	    }
	    
	    if(budgetOptionExistsObj && budgetOptionExistsObj.checked == true)
		{
			budgetNotDisabled=false;
		}
	    else
	    {
	    	budgetNotDisabled=disabled;
	    }
	}
	
    // if promotion is expired then disabled = true;
    <c:if test="${promotionStatus=='expired'}">
      disabled = true;
    </c:if>
    
    //Below Awards fields follow the selection made in the "Awards Active?" radio button
    awardsTypeObj.disabled=disabled;
<c:choose>    
<c:when test="${promotionAwardsForm.awardsType == 'merchandise'}">

	if(useRecognitionCalculatorFalseObj) {
		useRecognitionCalculatorFalseObj.disabled = disabled;
	}
	if(useRecognitionCalculatorTrueObj) {
		useRecognitionCalculatorTrueObj.disabled = disabled;
	}
	  if(useRecognitionCalculatorTrueObj && useRecognitionCalculatorTrueObj.checked)
	  {
	    if( document.getElementById('calculatorId') != null )
	    {
	      document.getElementById('calculatorId').disabled=useRecognitionCalculatorTrueObj.disabled;
	    }
	    if( document.getElementById('scoreBy') != null )
	    {
	      document.getElementById('scoreBy').disabled=useRecognitionCalculatorTrueObj.disabled;
	    }	
	  }
    //awardStructureObj.disabled=disabled;    
    apqConversionFalseObj.disabled=disabled;
    apqConversionTrueObj.disabled=disabled;
    stdProductIdObj.disabled=disabled;
	if(featuredAwardsFalseObj) {
		featuredAwardsFalseObj.disabled=disabled;
	}
	if(featuredAwardsTrueObj) {
    	featuredAwardsTrueObj.disabled=disabled;
	}
	if(noNotificationFalseObj) {
    	noNotificationFalseObj.disabled=disabled;
	}
	if(noNotificationTrueObj) {
    	noNotificationTrueObj.disabled=disabled;
	}
    var countrySizeElem = document.getElementsByName('countryListCount');
    var countrySize = countrySizeElem[0].value;
    for( var j=0; j<countrySize; j++) {
        $("input[name='countryList[" +j+ "].levelName']").attr('disabled',disabled);
    	}
    	
</c:when>  
<c:otherwise>
    awardAmountTypeFixedFalseObj.disabled=disabled;
    awardAmountTypeFixedTrueObj.disabled=disabled;
    if(awardAmountTypeFixedTrueObj.checked == false)
    {
    	fixedAmountObj.disabled=true;
    }else{
    	fixedAmountObj.disabled=disabled;
    }
    if(awardAmountTypeFixedFalseObj.checked == false)
    {
	    rangeAmountMinObj.disabled=true;
	    rangeAmountMaxObj.disabled=true;
    }else{
    	rangeAmountMinObj.disabled=disabled;
	    rangeAmountMaxObj.disabled=disabled;
    }
    if (calculatorAwardAmountTypeFixedFalseObj)
    {
      calculatorAwardAmountTypeFixedFalseObj.disabled = disabled;
    }
    if (calculatorAwardAmountTypeFixedCalObj)
    {
      calculatorAwardAmountTypeFixedCalObj.disabled = disabled;
    }
</c:otherwise>
</c:choose>  
<c:if test="${promotionAwardsForm.awardsType == 'points'}">
	  if(budgetSweepEnabledFalseObj) {
      	budgetSweepEnabledFalseObj.disabled=disabled;
	  }
	  if(budgetSweepEnabledTrueObj) {
      	budgetSweepEnabledTrueObj.disabled=disabled;
	  }
</c:if>

    if(budgetOptionNoneObj) {
    	budgetOptionNoneObj.disabled=disabled;
	}
	if(budgetOptionNewObj) {
    	budgetOptionNewObj.disabled=disabled;
	}
    <c:choose>    
		<c:when test="${promotionAwardsForm.awardsType == 'merchandise'}">
			if(budgetMasterIdObj) {
				budgetMasterIdObj.disabled=true;
			}
			if(budgetOptionExistsObj) {
	    		budgetOptionExistsObj.disabled=true;
			}
	    	if(budgetOptionExistsObj && budgetOptionExistsObj.checked == true)
	    	{
	    		 budgetOptionNoneObj.disabled = disabled;
	    	     budgetOptionNewObj.disabled = disabled;
	    	}
	    </c:when>
		<c:otherwise>
			if(budgetOptionExistsObj){
	   	    	budgetOptionExistsObj.disabled=budgetNotDisabled;
			}
		    if(budgetOptionExistsObj && budgetOptionExistsObj.checked == true)
		    {
				if(budgetMasterIdObj) {
		     		budgetMasterIdObj.disabled = budgetNotDisabled;
				}
		     }else{
				 if(budgetMasterIdObj) {
		     		budgetMasterIdObj.disabled=true;
				 }
		     } 
		    <c:if test="${promotionAwardsForm.awardsType == 'points'}">
		    if( budgetSweepEnabledTrueObj && budgetSweepEnabledTrueObj.checked == true )
		 	  {
				   if(budgetOptionNoneObj) {
		    			budgetOptionNoneObj.disabled = true;
				   }
			  }else{
				 if( budgetOptionExistsObj && budgetOptionExistsObj.checked == false  && budgetOptionNewObj && budgetOptionNewObj.checked == false )
		  		 {
					if(budgetOptionNoneObj) {
						budgetOptionNoneObj.checked = true;
					}
		  		 }
			  }
			</c:if>
	    </c:otherwise>
	</c:choose>
	if(budgetMasterNameObj) {
    	budgetMasterNameObj.disabled=disabled;
	}
	if(budgetTypePaxObj) {
    	budgetTypePaxObj.disabled=disabled;
	}   
	if(fileloadBudgetAmountFalseObj) {
    	fileloadBudgetAmountFalseObj.disabled=disabled;
	}
	if(fileloadBudgetAmountTrueObj) {
    	fileloadBudgetAmountTrueObj.disabled=disabled;
	}
	if(budgetTypeCentralObj!=null&&budgetTypeCentralObj!='undefined')
	{
		budgetTypeCentralObj.disabled=disabled;
	}
	$("input:radio[name='finalPayoutRule']").attr('disabled',disabled);
    
<c:if test="${promotionAwardsForm.awardsType != 'merchandise'}">  
	if(budgetCapTypeHardObj) {
    	budgetCapTypeHardObj.disabled=disabled;
	}
   // BugFix 20635,get the values from HTML form properties instead of ActionForm.
   if(( awardAmountTypeFixedFalseObj != null && awardAmountTypeFixedFalseObj.checked == true ) || ( awardAmountTypeFixedTrueObj != null && awardAmountTypeFixedTrueObj.checked == true ))
		{
		 	if( document.getElementById('calculatorId') != null )
			{
			document.getElementById('calculatorId').disabled=true;
			}
			if( document.getElementById('scoreBy') != null )
			{
		    document.getElementById('scoreBy').disabled=true;
			}	
	  }
	  if(budgetTypeNodeObj) {
    	budgetTypeNodeObj.disabled=disabled;
	  }
</c:if>  
    <%-- calcTypeObj.disabled=disabled; --%>
    
    var issuanceTypeObj = document.getElementById("promotionIssuanceTypeCode");
      issuance = issuanceTypeObj.value;
        if( issuance == 'file_load' ){
          hideLayer("budgetInfo");
          hideLayer("budgetTrackerOption");
        }
     }
     
    </SCRIPT>


  <SCRIPT TYPE="text/javascript">   
    var redirected = false;
  function optionLinkApplication()
  {
    var selectObj = document.getElementById("awardsType");
    destination = selectObj.options[selectObj.selectedIndex].value;
    //do not redirect if the ?Select One? option is selected.
    if (destination) {
      if (destination == 'points' || destination == 'merchandise') {
        showLayer("budgetInfo");
        showLayer("budgetTrackerOption");
      }else{
        hideLayer("budgetInfo");
        hideLayer("budgetTrackerOption");
      } 
    }
  }

  var clicks = 0;
  function countClick(objectId)
  {
    clicks++;
    //the second onclick event occurs when an option is selected.
    if (clicks == 2) {
      if (objectId=="awardsType") {
        optionLinkApplication();
      }
    }
  }

  function useRecognitionCalculatorEvaluate()
  {
      if(document.getElementById('useRecognitionCalculatorFalse'))
      {
          var useRecognitionCalculatorTrueObj = document.getElementById("useRecognitionCalculatorTrue"); //"Use Recognition Calculator? No"
          var useRecognitionCalculator = useRecognitionCalculatorTrueObj.checked == true;
          
          var calculatorIdObj = document.getElementById('calculatorId');
          var scoreByObj = document.getElementById('scoreBy');
	      if(useRecognitionCalculator)
	      {
             calculatorIdObj.disabled=false;
             scoreByObj.disabled=false;
	      }
	      else
	      {
             calculatorIdObj.disabled=true;
             scoreByObj.disabled=true;
	      }
	  }
  }
  useRecognitionCalculatorEvaluate();
  </script>
  
  <script type="text/javascript">
  function setSameLevelNames()
  {
    var countrySizeElem = document.getElementsByName('countryListCount');
    var countrySize = countrySizeElem[0].value;
    var levelName = new Array();
    var countryIdElem ;  
    var origCountryId ;
    if(countrySize > 0){
        countryIdElem = document.getElementsByName('countryList[0].countryId');  
        origCountryId = countryIdElem[0].value;
    }
    
    var levelSize = 0;
    for( var i=0; i<countrySize; i++){
       var countryElem = document.getElementsByName('countryList['+i+ '].levelName' );  
       var countryIdElem = document.getElementsByName('countryList['+i+ '].countryId' );  
       if(countryIdElem[0].value == origCountryId){
           levelName[i] = countryElem[0].value;  
           levelSize = levelSize + 1;
  	   }  
  	   else{
  	   		break;
  	   }
  	}
    // alert("CountrySize="+countrySize+" levelSize="+levelSize);
    for( var j=levelSize; j<countrySize; j++) {
       var countryElem = document.getElementsByName('countryList['+j+ '].levelName' );  
        countryElem[0].value =  levelName[j%levelSize];
   	}
  }
  
</script>
  