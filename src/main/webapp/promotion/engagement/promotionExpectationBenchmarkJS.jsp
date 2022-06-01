<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
	function showLayer(whichLayer) {
		if (document.getElementById) {
			// this is the way the standards work
			var style2 = document.getElementById(whichLayer).style;
			style2.display = "block";
		} else if (document.all) {
			// this is the way old msie versions work
			var style2 = document.all[whichLayer].style;
			style2.display = "block";
		} else if (document.layers) {
			// this is the way nn4 works
			var style2 = document.layers[whichLayer].style;
			style2.display = "block";
		}
	}
	function hideLayer(whichLayer) {
		if (document.getElementById) {
			// this is the way the standards work
			var style2 = document.getElementById(whichLayer).style;
			style2.display = "none";
		} else if (document.all) {
			// this is the way old msie versions work
			var style2 = document.all[whichLayer].style;
			style2.display = "none";
		} else if (document.layers) {
			// this is the way nn4 works
			var style2 = document.layers[whichLayer].style;
			style2.display = "none";
		}
	}

	function setHiddenAndSubmit(deleteIndex) {
		document.getElementById('deleteIndex').value = deleteIndex;
		setDispatchAndSubmit('deleteBenchmark');
	}

	function isNumberKey(evt)
    {
       var charCode = (evt.which) ? evt.which : event.keyCode
       if (charCode > 31 && (charCode < 48 || charCode > 57))
          return false;

       return true;
    }
</script>

<script type="text/javascript">
  function submitBenchmarkAudiences()
  {
	var promoType = document.getElementById("promotionTypeCode");
    if ( promoType.value == "engagement" )
    {
    	var benchmarkCount = document.getElementById("benchmarkValueBeansCount").value;
		if( benchmarkCount ){
			for (k = 0; k < benchmarkCount; k++) { 
				var selected = "benchmarkValueBeans[".concat(k).concat("].selectedAudiences");
				var notSelected = "benchmarkValueBeans[".concat(k).concat("].notSelectedAudiences");
				selectAll(selected);
	        	selectAll(notSelected);
			}
		}
    }
    return true;
  }
</script>