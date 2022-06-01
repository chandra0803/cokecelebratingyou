<%@ include file="/include/taglib.jspf"%>

<%-- from version CVS version 1.2 of src/fe/core/apps/recognition/tpl/recognitionCalculatorPayoutGridTemplate.html --%>

<div id="recogCalcInnerWrapper" class="recogCalcInnerWrapper">
	<h1><cms:contentText key="PAYOUT_GRID" code="recognitionSubmit.delivery.purl"/></h1>   
	<button type="button" id="recogCalcPayoutCloseBtn" class="close recogCalcPayoutCloseBtn"><i class="icon-close"></i></button>
	<table>
		<tbody>
			<tr>
				<th><cms:contentText key="SCORE" code="recognition.detail"/></th>   
				<th><cms:contentText key="PAYOUT" code="promotion.pending.stackranking.node.details"/></th>   
			</tr>
		</tbody>
	</table>
</div>