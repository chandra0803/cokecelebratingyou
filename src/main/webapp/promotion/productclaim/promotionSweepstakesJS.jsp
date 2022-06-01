<%@ include file="/include/taglib.jspf"%>

<SCRIPT language="JavaScript" type="text/javascript">
  hideLayer("givers");
  hideLayer("receivers");
  hideLayer("giversAndReceivers");
  hideLayer("fullAwards");
  hideLayer("trimmedAwards");
    
  var selectedOption = null;
  var selectBox = document.getElementById('eligibleWinners');
  for (var i = 0; i < selectBox.options.length; i++)
  {
    if(selectBox.options[i].selected)
    {
      selectedOption = selectBox.options[i].value
      break;
    }
  }
   
  if( selectedOption == "submittersdraw" )
  {
    showLayer("givers");
    showLayer("trimmedAwards");
  }
  if( selectedOption == "teammembersdraw" )
  {
    showLayer("receivers");
    showLayer("trimmedAwards");
  }
  if( selectedOption == "separatedraw" )
  {
    showLayer("givers");
    showLayer("receivers");
    showLayer("fullAwards");
  }
  if( selectedOption == "combineddraw" )
  {
    showLayer("giversAndReceivers");
    showLayer("trimmedAwards");
  }

</SCRIPT>

<SCRIPT TYPE="text/javascript">  
  
  var eligibleClaimsObj = document.getElementById("eligibleClaims");
  var eligibleWinnersObj = document.getElementById("eligibleWinners");
                
  var giversAmountObj = document.getElementById("giversAmount");
  var giversAmountTypeObj = document.getElementById("giversAmountType");
  var giversAwardTypeAmountObj = document.getElementById("giversAwardTypeAmount");
          
  var receiversAmountObj = document.getElementById("receiversAmount");
  var receiversAmountTypeObj = document.getElementById("receiversAmountType");
  var receiversAwardTypeAmountObj = document.getElementById("receiversAwardTypeAmount");
          
  var combinedAmountObj = document.getElementById("combinedAmount");
  var combinedAmountTypeObj = document.getElementById("combinedAmountType");
  var combinedAwardTypeAmountObj = document.getElementById("combinedAwardTypeAmount");
  
  var multipleAwardsFullObj = document.getElementById("multipleAwardsFull");
  var multipleAwardsTrimmedObj = document.getElementById("multipleAwardsTrimmed");
  
  var billCodesActiveTrueObj = document.getElementById("billCodesActiveTrue");
  var billCodesActiveFalseObj = document.getElementById("billCodesActiveFalse"); 
	
    var billCode1Obj = document.getElementById("billCode1");
	var customValue1Obj = $("input[name='customValue1']").val();
	
	var billCode2Obj = document.getElementById("billCode2");
	var customValue2Obj = document.getElementById("customValue2");
	
	var billCode3Obj = document.getElementById("billCode3");
	var customValue3Obj = document.getElementById("customValue3");
	
	var billCode4Obj = document.getElementById("billCode4");
	var customValue4Obj = document.getElementById("customValue4");
	
	var billCode5Obj = document.getElementById("billCode5");
	var customValue5Obj = document.getElementById("customValue5");
	
	var billCode6Obj = document.getElementById("billCode6");
	var customValue6Obj = document.getElementById("customValue6");
	
	var billCode7Obj = document.getElementById("billCode7");
	var customValue7Obj = document.getElementById("customValue7");
	
	var billCode8Obj = document.getElementById("billCode8");
	var customValue8Obj = document.getElementById("customValue8");
	
	var billCode9Obj = document.getElementById("billCode9");
	var customValue9Obj = document.getElementById("customValue9");
	
	var billCode10Obj = document.getElementById("billCode10");
	var customValue10Obj = document.getElementById("customValue10");
	         
  var activeFalseObj = document.getElementById("activeFalse");
  var activeTrueObj = document.getElementById("activeTrue");

  enableFields();
  //End inline javascript
    
  function enableFields()
  {
    var disabled = activeFalseObj.checked == true;
    // if promotion is expired then disabled = true;
    <c:if test="${promotionStatus=='expired'}">
      disabled = true;
    </c:if>
      
    eligibleClaimsObj.disabled=disabled;
    eligibleWinnersObj.disabled=disabled;
      
    giversAmountObj.disabled=disabled;
    giversAmountTypeObj.disabled=disabled;
    <c:if test="${empty promotionSweepstakesForm.giversAmountType}" >
		giversAmountTypeObj.value='specific';
	</c:if>    
    giversAwardTypeAmountObj.disabled=disabled;
      
    receiversAmountObj.disabled=disabled;
    receiversAmountTypeObj.disabled=disabled;
    <c:if test="${empty promotionSweepstakesForm.receiversAmountType}" >
		receiversAmountTypeObj.value='specific';
	</c:if> 
    receiversAwardTypeAmountObj.disabled=disabled;
      
    combinedAmountObj.disabled=disabled;
    combinedAmountTypeObj.disabled=disabled;
    <c:if test="${empty promotionSweepstakesForm.combinedAmountType}" >
		combinedAmountTypeObj.value='specific';
	</c:if>      
    combinedAwardTypeAmountObj.disabled=disabled;
  
    multipleAwardsFullObj.disabled=disabled;
    multipleAwardsTrimmedObj.disabled=disabled;
    
    if( activeFalseObj.checked == true )
	{
    	$("select[name='eligibleClaims'] option[value='']").attr('selected', 'selected');
    	$("select[name='eligibleClaims']").attr('disabled', 'disabled');
    	$("select[name='eligibleWinners'] option[value='']").attr('selected', 'selected');
    	$("select[name='eligibleWinners']").attr('disabled', 'disabled');
	}
    if( activeTrueObj.checked == true )
	{
		$("select[name='eligibleClaims']").attr('disabled', false);
		$("select[name='eligibleWinners']").attr('disabled', false);
	}
	
    var sweepBillCodesNo = document.getElementById('billCodesActiveFalse');
	  var sweepBillCodesYes = document.getElementById('billCodesActiveTrue');
	  if(disabled)
	  {
		  sweepBillCodesNo.disabled=disabled;
		  sweepBillCodesYes.disabled=disabled;
	  }
	  else
	  {
		  sweepBillCodesNo.disabled=false;
		  sweepBillCodesYes.disabled=false;
	  }
    
    if( billCodesActiveFalseObj.checked == true)
  	{
          $("select[name='billCode1'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode1']").attr('disabled', 'disabled');
          $("select[name='billCode2'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode2']").attr('disabled', 'disabled');
          $("select[name='billCode3'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode3']").attr('disabled', 'disabled');
          $("select[name='billCode4'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode4']").attr('disabled', 'disabled');
          $("select[name='billCode5'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode5']").attr('disabled', 'disabled');
          $("select[name='billCode6'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode6']").attr('disabled', 'disabled');
          $("select[name='billCode7'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode7']").attr('disabled', 'disabled');
          $("select[name='billCode8'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode8']").attr('disabled', 'disabled');
          $("select[name='billCode9'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode9']").attr('disabled', 'disabled');
          $("select[name='billCode10'] option[value='']").attr('selected', 'selected');
          $("select[name='billCode10']").attr('disabled', 'disabled');
          
          $("input[name='customValue1']").val("");
          $("input[name='customValue2']").val("");
          $("input[name='customValue3']").val("");
          $("input[name='customValue4']").val("");
          $("input[name='customValue5']").val("");
          $("input[name='customValue6']").val("");
          $("input[name='customValue7']").val("");
          $("input[name='customValue8']").val("");
          $("input[name='customValue9']").val("");
          $("input[name='customValue10']").val("");
          
          
          $("input[name='customValue1']").hide();
		  $("#billCode1Custom").hide();
		  
		  $("input[name='customValue2']").hide();
		  $("#billCode2Custom").hide();
		  
		  $("input[name='customValue3']").hide();
		  $("#billCode3Custom").hide();
		  
		  $("input[name='customValue4']").hide();
		  $("#billCode4Custom").hide();
		  
		  $("input[name='customValue5']").hide();
		  $("#billCode5Custom").hide();
		  
		  $("input[name='customValue6']").hide();
		  $("#billCode6Custom").hide();
		  
		  $("input[name='customValue7']").hide();
		  $("#billCode7Custom").hide();
		  
		  $("input[name='customValue8']").hide();
		  $("#billCode8Custom").hide();
		  
		  $("input[name='customValue9']").hide();
		  $("#billCode9Custom").hide();
		  
		  $("input[name='customValue10']").hide();
		  $("#billCode10Custom").hide();
			  
  	}
	if( billCodesActiveTrueObj.checked == true)
  	{
		$("select[name='billCode1']").attr('disabled', false);
		$("select[name='billCode2']").attr('disabled', false);
		$("select[name='billCode3']").attr('disabled', false);
		$("select[name='billCode4']").attr('disabled', false);
		$("select[name='billCode5']").attr('disabled', false);
		$("select[name='billCode6']").attr('disabled', false);
		$("select[name='billCode7']").attr('disabled', false);
		$("select[name='billCode8']").attr('disabled', false);
		$("select[name='billCode9']").attr('disabled', false);
		$("select[name='billCode10']").attr('disabled', false);
	}
}  
</SCRIPT>

<SCRIPT type="text/javascript">
function enableBillCode1()
{
  var billCode1Selected = document.getElementById("billCode1").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode1Selected);
  if(  res )
    {
        document.getElementById('customValue1').style.display = 'inline';
        document.getElementById('billCode1Custom').style.display = 'table-row';
        enableBillCode2();
    }
  else
  {
        document.getElementById('customValue1').style.display = 'none';
        document.getElementById('billCode1Custom').style.display = 'none';
        enableBillCode2();
    }
}
 enableBillCode1();

function enableBillCode2()
{
   var billCode2Selected = document.getElementById("billCode2").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode2Selected);
  if(  res )
    {
        document.getElementById('customValue2').style.display = 'inline';
        document.getElementById('billCode2Custom').style.display = 'table-row';
        enableBillCode3();
    }
  else
  {
        document.getElementById('customValue2').style.display = 'none';
        document.getElementById('billCode2Custom').style.display = 'none';
        enableBillCode3();
    }
}
enableBillCode2();


function enableBillCode3()
{
   var billCode3Selected = document.getElementById("billCode3").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode3Selected);
  if(  res )
    {
        document.getElementById('customValue3').style.display = 'inline';
        document.getElementById('billCode3Custom').style.display = 'table-row';
        enableBillCode4();
    }
  else
  {
        document.getElementById('customValue3').style.display = 'none';
        document.getElementById('billCode3Custom').style.display = 'none';
        enableBillCode4();
    }
}
enableBillCode3();


function enableBillCode4()
{
   var billCode4Selected = document.getElementById("billCode4").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode4Selected);
  if(  res )
    {
        document.getElementById('customValue4').style.display = 'inline';
        document.getElementById('billCode4Custom').style.display = 'table-row';
        enableBillCode5();
    }
  else
  {
        document.getElementById('customValue4').style.display = 'none';
        document.getElementById('billCode4Custom').style.display = 'none';
        enableBillCode5();
    }
}
enableBillCode4();

function enableBillCode5()
{
   var billCode5Selected = document.getElementById("billCode5").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode5Selected);
  if(  res )
    {
        document.getElementById('customValue5').style.display = 'inline';
        document.getElementById('billCode5Custom').style.display = 'table-row';
        enableBillCode6();
    }
  else
  {
        document.getElementById('customValue5').style.display = 'none';
        document.getElementById('billCode5Custom').style.display = 'none';
        enableBillCode6();
    }
}
enableBillCode5();

function enableBillCode6()
{
   var billCode6Selected = document.getElementById("billCode6").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode6Selected);
  if(  res )
    {
        document.getElementById('customValue6').style.display = 'inline';
        document.getElementById('billCode6Custom').style.display = 'table-row';
        enableBillCode7();
    }
  else
  {
        document.getElementById('customValue6').style.display = 'none';
        document.getElementById('billCode6Custom').style.display = 'none';
        enableBillCode7();
    }
}
enableBillCode6();


function enableBillCode7()
{
   var billCode7Selected = document.getElementById("billCode7").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode7Selected);
  if(  res )
    {
        document.getElementById('customValue7').style.display = 'inline';
        document.getElementById('billCode7Custom').style.display = 'table-row';
        enableBillCode8();
    }
  else
  {
        document.getElementById('customValue7').style.display = 'none';
        document.getElementById('billCode7Custom').style.display = 'none';
        enableBillCode8();
    }
}
enableBillCode7();

function enableBillCode8()
{
   var billCode8Selected = document.getElementById("billCode8").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode8Selected);
  if(  res )
    {
        document.getElementById('customValue8').style.display = 'inline';
        document.getElementById('billCode8Custom').style.display = 'table-row';
        enableBillCode9();
    }
  else
  {
        document.getElementById('customValue8').style.display = 'none';
        document.getElementById('billCode8Custom').style.display = 'none';
        enableBillCode9();
    }
}
enableBillCode8();

function enableBillCode9()
{
   var billCode9Selected = document.getElementById("billCode9").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode9Selected);
  if(  res )
    {
        document.getElementById('customValue9').style.display = 'inline';
        document.getElementById('billCode9Custom').style.display = 'table-row';
        enableBillCode10();
    }
  else
  {
        document.getElementById('customValue9').style.display = 'none';
        document.getElementById('billCode9Custom').style.display = 'none';
        enableBillCode10();
    }
}
enableBillCode9();


function enableBillCode10()
{
   var billCode10Selected = document.getElementById("billCode10").value;
  var patt = new RegExp("custom");
  var res = patt.test(billCode10Selected);
  if(  res )
    {
        document.getElementById('customValue10').style.display = 'inline';
        document.getElementById('billCode10Custom').style.display = 'table-row';
    }
  else
  {
        document.getElementById('customValue10').style.display = 'none';
        document.getElementById('billCode10Custom').style.display = 'none';
    }
}
enableBillCode10();

</SCRIPT> 