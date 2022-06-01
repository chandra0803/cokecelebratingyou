<SCRIPT TYPE="text/javascript">
	function showLayer(whichLayer) {
		if (document.getElementById) {
			// this is the way the standards work
			var style2 = document.getElementById(whichLayer).style;
			style2.display = "table-row";
		} else if (document.all) {
			// this is the way old msie versions work
			var style2 = document.all[whichLayer].style;
			style2.display = "table-row";
		} else if (document.layers) {
			// this is the way nn4 works
			var style2 = document.layers[whichLayer].style;
			style2.display = "table-row";
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

	
	function enableClaimApproval() {
		showLayer('claimFormSection');
		showLayer('daysToApproveClaimSection');
	}

	function disableClaimApproval() {
		hideLayer('claimFormSection');
		hideLayer('daysToApproveClaimSection');
	}
	
	function enableDaysToApproveClaim() {
		showLayer('daysToApproveClaimSection');
	}

	function disableDaysToApproveClaim() {
		hideLayer('daysToApproveClaimSection');
	}
	
</SCRIPT>