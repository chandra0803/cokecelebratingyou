<%@ include file="/include/taglib.jspf"%>

<SCRIPT type="text/javascript">

	var billCodesActiveTrueObj = document.getElementById("billCodesActiveTrue");
	var billCodesActiveFalseObj = document.getElementById("billCodesActiveFalse"); 
	
	
	
 	function enableFields() 
 	{	
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
			  $("#billCodeCustom\\[1\\]").hide();
			  
			  $("input[name='customValue2']").hide();
			  $("#billCodeCustom\\[2\\]").hide();
			  
			  $("input[name='customValue3']").hide();
			  $("#billCodeCustom\\[3\\]").hide();
			  
			  $("input[name='customValue4']").hide();
			  $("#billCodeCustom\\[4\\]").hide();
			  
			  $("input[name='customValue5']").hide();
			  $("#billCodeCustom\\[5\\]").hide();
			  
			  $("input[name='customValue6']").hide();
			  $("#billCodeCustom\\[6\\]").hide();
			  
			  $("input[name='customValue7']").hide();
			  $("#billCodeCustom\\[7\\]").hide();
			  
			  $("input[name='customValue8']").hide();
			  $("#billCodeCustom\\[8\\]").hide();
			  
			  $("input[name='customValue9']").hide();
			  $("#billCodeCustom\\[9\\]").hide();
			  
			  $("input[name='customValue10']").hide();
			  $("#billCodeCustom\\[10\\]").hide();
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
    enableFields();
    
    function enableBillCodes()
    {
	
		for( var i = 1; i <= 10; i++ ) 
		{
		      
		      var patt = new RegExp("custom");
		      var res = patt.test( document.getElementById("billCode["+i+"]").value);
		      if(  res )
		      {
		            document.getElementById('customValue['+i+']').style.display = 'inline';
		            document.getElementById('billCodeCustom['+i+']').style.display = 'table-row';
		            
		      }
		      else
		      {
		            document.getElementById('customValue['+i+']').style.display = 'none';
		            document.getElementById('billCodeCustom['+i+']').style.display = 'none';
		            
		      }
		}
    }
	enableBillCodes();
        
</SCRIPT> 



<script type="text/javascript">
$(document).ready(function(){ 
	var billAr = [];
	$('.billCodes').each(function(value) {
		var value = this.value;
		if( value && value !== "customvalue") {
			billAr.push(value);
			$('.billCodes').each(function() {
				if( $(this) && $(this).val() !== "customvalue") {
					$(this).find('option[value='+value+']').not(':last-child').hide();
				}
			});
		}
	});
	
	$('input[type=radio][name=billCodesActive][value=false]').change(function() {
		
		$('.billCodes').each(function() {
			$(this).find('option').show();
				
		});	
	});
	
	$('.billCodes').change( function(){
		var value = $(this).val();
		if(value) {	
			$('.billCodes').each(function() {
				if( billAr.indexOf(value) == -1 ) {
					$(this).find('option[value='+value+']').not(':last-child').hide();
				}else {
					$(this).find('option[value='+value+']').not(':last-child').show();
					billAr.splice( $.inArray(value, billAr), 1 );
				}
			});
			
		}else {
			var value = this.getAttribute("data-selcBill");
			$('.billCodes').each(function() {
				if( value ) {
					$(this).find('option[value='+value+']').not(':last-child').show();
					billAr.splice( $.inArray(value, billAr), 1 );
				}
			});
		}
		billAr.push(value);	
		this.setAttribute( "data-selcBill", value);
	});
});

</script>

