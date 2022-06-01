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
  //Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload) 
  <c:if test="${promotionPublicRecAddOnForm.audience == 'allactivepaxaudience' }">
 	 hideLayer("submittersaudience");
  </c:if>
  showLayer("budgetInfo");
  hideLayer("newBudget");
  <c:if test="${promotionPublicRecAddOnForm.budgetOption == 'none' }">
    hideLayer("budgetAmountLabel");
    hideLayer('finalPayoutRule_radio');
    hideLayer('newBudgetCentralValue');
    hideLayer('newBudgetCentralTitle');
  </c:if>  
  
  var budgetOptionNoneObj = document.getElementById("budgetOptionNone");
  var budgetOptionExistsObj = document.getElementById("budgetOptionExists");
  var budgetOptionNewObj = document.getElementById("budgetOptionNew");
  
  var allowPublicRecognitionPointsTrueObj = document.getElementById("allowPublicRecognitionPointsTrue");
  var allowPublicRecognitionPointsFalse = document.getElementById("allowPublicRecognitionPointsFalse"); 
  
  var budgetMasterIdObj = document.getElementById("budgetMasterId");
  var budgetMasterNameObj = document.getElementById("budgetMasterName");
  
  var budgetTypePaxObj = document.getElementById("budgetTypePax");
  var budgetTypeNodeObj = document.getElementById("budgetTypeNode");
  var budgetTypeCentralObj = document.getElementById("budgetTypeCentral");
  
  var budgetCapTypeHardObj = document.getElementById("budgetCapTypeHard");
  
  var awardAmountTypeFixedTrueObj = document.getElementById("awardAmountTypeFixedTrue");
  var awardAmountTypeFixedFalseObj = document.getElementById("awardAmountTypeFixedFalse");
 
  var fixedAmountObj = document.getElementById("fixedAmount");
  var rangeAmountMinObj = document.getElementById("rangeAmountMin");
  var rangeAmountMaxObj = document.getElementById("rangeAmountMax");
  
  var audienceAllActivePaxObj = document.getElementById("audienceAllActivePax");
  var audiencePromoPublicRecgObj = document.getElementById("audiencePromoPublicRecg");
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

function updateLayerNewBudgetCentralValue(){
	  var count = $("input[name='budgetSegmentVBListSize']").val();
	  for(i=0; i<= count; i++){
		  displayBudgetAmount(i);
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
	 	
	    showLayer("budgetInfo");
	    
	    if( budgetOptionNewObj && budgetOptionNewObj.checked==true )
	    {
	        showLayer("newBudget");
	        updateLayerNewBudgetCentralValue();
	        
	        if (budgetTypeCentralObj.checked == true)
	        {
	          hideLayer("budgetAmountLabel");
	          showLayer('finalPayoutRule_radio');
	          showLayer("budgetCapText");
	          hideLayer("budgetCapRadio");
	          showLayer('newBudgetCentralTitle');
	        }else{
	          	hideLayer('finalPayoutRule_radio');
	        	showLayer("budgetAmountLabel");
	        	hideLayer('newBudgetCentralTitle');
	        	var budgetTypeNode = document.getElementById("budgetTypeNode");
	        	if (budgetTypeNode.checked == true){
	        		showLayer("budgetCapText");
	              hideLayer("budgetCapRadio");
	        	}else{
		            hideLayer("budgetCapText");
		            showLayer("budgetCapRadio");
		        }
	        } 	
	      }else{
	        hideLayer("newBudget");
	        hideLayer('finalPayoutRule_radio');
	        hideLayer("budgetAmountLabel");
        	hideLayer('newBudgetCentralTitle');
	      }
	    enableFields();   
    }
updateLayersShown();    
function enableFields(){

    var publicRecogActiveFalseObj = document.getElementById("allowPublicRecognitionPointsFalse"); //"PublicRecognition Active? No"
    var disabled = publicRecogActiveFalseObj.checked == true;
    // if promotion is expired then disabled = true;
    <c:if test="${promotionStatus=='expired'}">
      disabled = true;
    </c:if>  
    
	<c:if test="${promotionPublicRecAddOnForm.allowPublicRecognition == 'false' }">
	 disabled = true;
	 allowPublicRecognitionPointsTrueObj.disabled=disabled;
	 allowPublicRecognitionPointsFalse.disabled=disabled;
	 budgetOptionNoneObj.disabled=disabled;
	 budgetOptionExistsObj.disabled=disabled;
	 budgetOptionNewObj.disabled=disabled;
	</c:if>

   audienceAllActivePaxObj.disabled=disabled;
   audiencePromoPublicRecgObj.disabled=disabled;	
   awardAmountTypeFixedTrueObj.disabled=disabled;
   awardAmountTypeFixedFalseObj.disabled=disabled;
   fixedAmountObj.disabled=disabled;
   rangeAmountMinObj.disabled=disabled;
   rangeAmountMaxObj.disabled=disabled;
	 budgetOptionNoneObj.disabled=disabled;
	 budgetOptionExistsObj.disabled=disabled;
	 budgetOptionNewObj.disabled=disabled;
	
	var promoTypeCode=document.getElementById("promotionTypeCode").value;
    
    budgetOptionNoneObj.disabled=disabled;
    budgetOptionExistsObj.disabled=disabled;
    budgetOptionNewObj.disabled=disabled;
   if(budgetOptionExistsObj.checked == true && publicRecogActiveFalseObj.checked == false)
   {
    budgetMasterIdObj.disabled=false;
    }else{
    	budgetMasterIdObj.disabled=true;
    } 
    budgetMasterNameObj.disabled=disabled;
    budgetTypePaxObj.disabled=disabled; 
    budgetCapTypeHardObj.disabled=disabled;
    budgetTypeNodeObj.disabled=disabled;
    budgetTypeCentralObj.disabled=disabled;
    
    // this will hide all the layers when PublicRecognition Active? No
    if(publicRecogActiveFalseObj.checked == true)
    	{
    	hideLayer("submittersaudience");
    	hideLayer("newBudget");
        hideLayer('finalPayoutRule_radio');
        hideLayer("budgetAmountLabel");
    	hideLayer('newBudgetCentralTitle');
        $("input[id='fixedAmount']").val("");
        $("input[id='rangeAmountMin']").val("");
        $("input[id='rangeAmountMax']").val("");
        $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
        $("input:radio[id='budgetOptionNone']").attr('checked',true);
        $("input:radio[id='audienceAllActivePax']").attr('checked',true);
    	}
  }
  
</SCRIPT> 