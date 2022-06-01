<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.domain.promotion.ThrowdownPromotion"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
	function disableAndSubmitForm(theform)
	{
		if (document.all || document.getElementById) 
		{
			for (i = 0; i < theform.length; i++)
			 {
				var tempobj = theform.elements[i];
				if (tempobj.type.toLowerCase() == "submit" ||
					tempobj.type.toLowerCase() == "button" || tempobj.type.toLowerCase() == "reset")
				tempobj.disabled = true;
			 }
			callUrl('throwdownProcessConfirmation.do');
		}
	} 
	
	$(document).ready(function(){
		var viewUrl = "${viewUrl}";
		$.ajax({
			url: '<%=RequestUtils.getBaseURI(request)%>${viewUrl}',
			dataType: 'application/json',
			success: function(data){
				if(data != null && data != '' && data != 'null')
				{
				  	var object = jQuery.parseJSON(data);
				  	
					var titleHeader ='<cms:contentText key="TITLE" code="promotion.throwdown.summary" />';
				  	var instructionHeader ='<cms:contentText key="INSTRUCTION" code="promotion.throwdown.summary" />';
				  	var promotionHeader ='<cms:contentText key="PROMOTION" code="promotion.throwdown.summary" />';
				  	var roundHeader ='<cms:contentText key="FOR_ROUND" code="promotion.throwdown.summary" />';
				  	var stackRankAwardsHeader ='<cms:contentText key="STACK_RANK_AWARDS" code="promotion.throwdown.summary" />';
				  	var nodeTypeHeader ='<cms:contentText key="NODE_TYPE" code="promotion.throwdown.summary" />';
				  	var nodeNameHeader ='<cms:contentText key="NODE_NAME" code="promotion.throwdown.summary" />';
				  	var payoutSummaryHeader ='<cms:contentText key="PAYOUT_SUMMARY" code="promotion.throwdown.summary" />';
				  	var totalHeader ='<cms:contentText key="TOTAL"	code="promotion.throwdown.summary" />';
				  	var nothingFoundText = '<cms:contentText key="NOTHING_FOUND" code="system.errors"/>';
				  	var divisionAwardsHeader ='<cms:contentText key="DIVISION_AWARDS" code="promotion.throwdown.summary" />';
				  	var totalPointsIssuedHeader ='<cms:contentText key="TOTAL_POINTS_ISSUED" code="promotion.throwdown.summary" />';
				  	var totalNumberWinsHeader ='<cms:contentText key="TOTAL_NUMBER_WINS" code="promotion.throwdown.summary" />';
				  	var totalNumberLossesHeader ='<cms:contentText key="TOTAL_NUMBER_LOSSES" code="promotion.throwdown.summary" />';
				  	var totalNumberTiesHeader ='<cms:contentText key="TOTAL_NUMBER_TIES" code="promotion.throwdown.summary" />';

				  	var htmlString='<table border="0" cellpadding="10" cellspacing="0" width="100%">';
				  	htmlString+='<tr><td><span class="headline">' + titleHeader + '</span> <br /> <span class="content-instruction"> <b>'+ object.promotionUpperCaseName + '</b><span class="content-instruction">' + instructionHeader + '</span>';
				  	htmlString+='<br /><cms:errors /><table><tr><th><b>' + promotionHeader + '&nbsp;:&nbsp;</b>' + object.promotionUpperCaseName + '&nbsp;' +  roundHeader + object.throwdownRoundCalculationResult.roundNumber + '&nbsp;'
				  	htmlString+='(' + object.throwdownRoundCalculationResult.displayStartDate + '&nbsp;-&nbsp;' + object.throwdownRoundCalculationResult.displayEndDate + ')</th></tr></table>';

				  	if( object.stackRankingCalculationResult.roundStackRanking.nodeRankings!='undefined' &&  object.stackRankingCalculationResult.roundStackRanking.nodeRankings!= null)
					{
				  		htmlString+='<h3><b>' + stackRankAwardsHeader + '</b></h3><table border="1" cellpadding="0" cellspacing="0"><thead>';
				  		htmlString+='<tr><th> <b>' + nodeTypeHeader + '</b></th><th> <b>' + nodeNameHeader + '</b></th><th> <b>' + payoutSummaryHeader + '</b></th></tr></thead><tbody>';
				  		for(var i=0, len=object.stackRankingCalculationResult.roundStackRanking.nodeRankings.length; i < len; i++)
					  	{
				  			htmlString+='<tr><td width="30%">'+ object.stackRankingCalculationResult.roundStackRanking.nodeRankings[i].nodesNodeTypeName + '</td><td width="30%">' + object.stackRankingCalculationResult.roundStackRanking.nodeRankings[i].nodeName + '</td><td width="30%">' + object.stackRankingCalculationResult.roundStackRanking.nodeRankings[i].pointsIssuedForNode + '</td></tr>';
						}
				  		htmlString+='</tbody></table><b>' + totalHeader + ':</b>&nbsp;' + object.stackRankingCalculationResult.totalPointsIssued + '<hr>' ;
					}else{
						htmlString+='<h3><b>' + stackRankAwardsHeader + '</b></h3><table><tr><td class="content-field left-align">' + nothingFoundText + '</td></tr></table><hr>';	
					}
				
				  	htmlString+='<h3><b>' + divisionAwardsHeader + '</b></h3><table width="25%"><tr><td>' + totalPointsIssuedHeader + '</td><td>' + object.throwdownRoundCalculationResult.totalPointsIssued +'</td></tr><tr>';
				  	htmlString+='<td>' + totalNumberWinsHeader + '</td><td>' + object.throwdownRoundCalculationResult.totalNumberofWins + '</td></tr><tr><td>';
				  	htmlString+= totalNumberLossesHeader + '</td><td>' + object.throwdownRoundCalculationResult.totalNumberofLosses + '</td></tr><tr><td>';
				  	htmlString+= totalNumberTiesHeader + '</td><td>' + object.throwdownRoundCalculationResult.totalNumberofTies +'</td></tr></table><hr></td></tr></table>';
				  	
				  	$("#stackRankingResults").html(htmlString);
				  	
				  	var divisionHeader='<cms:contentText key="DIVISION"	code="promotion.throwdown.summary" />';
				  	var minimumQualifierHeader='<cms:contentText key="MINUMUM_QUALIFIER" code="promotion.payout.throwdown" />';
				  	var outcometypeHeader='<cms:contentText key="OUTCOME_TYPE"	code="promotion.throwdown.summary" />';
				  	var countHeader='<cms:contentText key="COUNT" code="promotion.throwdown.summary" />';
				  	var pointsComeHeader='<cms:contentText key="POINTS_OUTCOME" code="promotion.throwdown.summary" />';
				  	var totalPointsForOutcomeHeader='<cms:contentText key="TOTAL_POINTS_FOR_OUTCOME" code="promotion.throwdown.summary" />';
				  	var winsHeader='<cms:contentText key="WINS" code="promotion.throwdown.summary" />';
				  	var tiesHeader='<cms:contentText key="TIES" code="promotion.throwdown.summary" />';
				  	var lossesHeader='<cms:contentText key="LOSSES" code="promotion.throwdown.summary" />';
				  	
				  	var htmlString='<table border="1" cellpadding="0" cellspacing="0" width="100%">';
				  	for(var i=0, len=object.throwdownRoundCalculationResult.divisionResults.length; i < len; i++)
				  	{
							 htmlString+='<tr><td><b>'+ divisionHeader + ':' + object.throwdownRoundCalculationResult.divisionResults[i].division.divisionName + '</b></td></tr>';
							 htmlString+='<tr><td>' + minimumQualifierHeader + ':' + object.throwdownRoundCalculationResult.divisionResults[i].division.minimumQualifier + '</td></tr>';
							 htmlString+='<tr><td>' + totalPointsIssuedHeader + ':' + object.throwdownRoundCalculationResult.divisionResults[i].totalPointsIssued + '</td></tr>';
					
							 htmlString+='<tr><td>';
							 htmlString+='<table border="1" cellpadding="0" cellspacing="0" width="50%">';
							 htmlString+='<thead><tr><th> <b>' + outcometypeHeader + '</b></th><th> <b>' + countHeader +'</b></th><th> <b>'+ pointsComeHeader + '</b></th><th> <b>'+ totalPointsForOutcomeHeader +'</b></th></tr></thead>';
							 htmlString+='<tbody><tr><td>'+ winsHeader + '</td>'+ '<td>' + object.throwdownRoundCalculationResult.divisionResults[i].numberOfWins + '</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].payoutForWins + '</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].totalPointsIssuedForWins + '</td></tr>';
							 htmlString+='<tr><td>' + tiesHeader +'</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].numberOfTies + '</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].payoutForTies +	'</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].totalPointsIssuedForTies + '</td></tr>';
							 htmlString+='<tr><td>' + lossesHeader + '</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].numberOfLosses +	'</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].payoutForLosses +	'</td><td>' + object.throwdownRoundCalculationResult.divisionResults[i].totalPointsIssuedForLosses +	'</td></tr></tbody></table><hr>';
							 htmlString+='</td><td></td></tr>';

					}
				  	htmlString+='</table>';
					$("#calculationResults").html(htmlString);
				  }
				},
			       complete: function(){
				      $('#pageLoadingSpinner').fadeOut();
		   		}
			});
		});
</script>
<html:form styleId="contentForm" action="/throwdownProcessConfirmation">
	<div id="pageLoadingSpinner" class="spincover pageLoading">
		<img src="${siteUrlPrefix}/assets/img/pageLoadingSpinner.gif" alt="Loading..." height="5%" width="5%" >
	</div>
	
	<div id="stackRankingResults">
		<!-- data will be displayed here automatically from the above ajax call -->
	</div>
	
	<div id="calculationResults">
		<!-- data will be displayed here automatically from the above ajax call -->	
	</div> 
	
	<div>
		<table border="0" cellpadding="10" cellspacing="0" width="50%">
			<tr align="center">
				<td>
					<html:button property="approveBtn" styleClass="content-buttonstyle"
						onclick="disableAndSubmitForm(this.form)">
						<cms:contentText key="APPROVE_AND_ISSUE_AWARDS"
							code="promotion.throwdown.summary" />
					</html:button>
				</td>
				<td>
					<html:button property="extractBtn"
						styleClass="content-buttonstyle"
						onclick="callUrl('throwdownExtractDetails.do')">
						<cms:contentText key="EXTRACT_DETAILS"
							code="promotion.throwdown.summary" />
					</html:button>
				</td>
				<td>
					<html:button property="cancelBtn"
						styleClass="content-buttonstyle"
						onclick="callUrl('throwdownListDisplay.do')">
						<cms:contentText code="system.button" key="CANCEL" />
					</html:button>
				</td>
			</tr>
		</table>
	</div>
</html:form>