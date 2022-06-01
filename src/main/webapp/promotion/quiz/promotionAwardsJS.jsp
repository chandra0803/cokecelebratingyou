<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      // this is the way the standards work
      var style2 = document.getElementById(whichLayer).style;
      style2.display = "";
      if( whichLayer == "newBudget")
	  {
	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one when create
	  	//budget is selected
    	  if ( $("input:radio[name='budgetOption']:checked").val()== "new")
   		   {
   		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
   			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
   		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
   		   {
   		   	 $("select[name='budgetMasterId']").attr('disabled', '');
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
	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one when
	  	// no budget is selected
    	  if ( $("input:radio[name='budgetOption']:checked").val()== "none")
   		   {
   		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
   			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
   		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
   		   {
   		   	 $("select[name='budgetMasterId']").attr('disabled', '');
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
//-->
</script>


<SCRIPT TYPE="text/javascript"> 

  //Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
  hideLayer("budgetInfo");
  hideLayer("newBudget");
  hideLayer("finalPayout");

  var budgetOptionNoneObj = $("input:radio[id='budgetOptionNone']");
  var budgetOptionExistsObj = $("input:radio[id='budgetOptionExists']");
  var budgetMasterIdObj = $("select[id='budgetMasterId']");
  var budgetMasterNameObj = $("input[id='budgetMasterName']");
        
  var awardsTypeObj = document.getElementById("awardsType");
  destination = awardsTypeObj.value;
  if( destination == 'points' )
  {
    showLayer("budgetInfo");
  }
  else
  {
    hideLayer("budgetInfo");
  }
      
  var budgetOptionNewObj = $("input:radio[id='budgetOptionNew']");
  if( $('#budgetOptionNew').is(':checked') == true)
  {
    showLayer("newBudget");
    showLayer("finalPayout");
    updateLayerRemoveBudgetSegment();
  }
  else
  {
    hideLayer("newBudget");
    hideLayer("finalPayout");
  }
        
  enableFields();
 
 function enableFields()
 {
   var fixedAmountObj = $("input[id='fixedAmount']");
   var awardsActiveFalseObj = $("input[id='awardsActiveFalse']");
   var disabled =  $('#awardsActiveFalse').is(':checked')== true;
   
   var promoStatus = $("input[name='live']").val();
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
	    	console.log('budgetNotDisabled:'+budgetNotDisabled);
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
     
      fixedAmountObj.attr('disabled', disabled );
      
    //Bug fix 19091 
      // if promotion is quiz and live then disable all of the budget fields
      <c:if test="${  (promotionStatus == 'live') }">
         disabled = true;
      </c:if> 
   
      budgetOptionNoneObj.attr('disabled', disabled );
      budgetOptionExistsObj.attr('disabled', disabled );
      budgetOptionNewObj.attr('disabled', disabled );
      
   // when awards active is true and budgetOptionExists is not set, then the budgetMasterId field should be disabled.
      if($('#budgetOptionExists').is(':checked') == true)
      {
        budgetMasterIdObj.attr('disabled', disabled );
      } 
      budgetMasterNameObj.attr('disabled', budgetNotDisabled );
   // when the awards active is set to false all the default values will be set for the page.
      if( $('#awardsActiveFalse').is(':checked') == true)
      	{
    	  fixedAmountObj.val("");
    	  if(promoStatus!='true')
          {
	    	  budgetOptionNoneObj.attr('checked',true);
	          $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
          }
          hideLayer("newBudget");
          hideLayer("finalPayout");
      	}
 }
</script> 
