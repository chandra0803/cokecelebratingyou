<SCRIPT TYPE="text/javascript">
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
	
	function enableContestApproval() {
		showLayer('contestApprovalLevelsSection');
		showLayer('daysToApproveOnSubmissionSection');
		showLayer('teamlvl1audiencelist');
		showLayer('teamlvl2audiencelist');
	}
	
	
	function disableContestApproval() {
		hideLayer('contestApprovalLevelsSection');
		hideLayer('daysToApproveOnSubmissionSection');
		hideLayer('teamlvl1audiencelist');
		hideLayer('teamlvl2audiencelist');
	}
	
	function populateAudience() {
		showLayer('contestApprovalLevelsSection');
		showLayer('daysToApproveOnSubmissionSection');
	}
</SCRIPT>