<%@ include file="/include/taglib.jspf"%>

<%-- from CVS version 1.2 of src/fe/core/apps/recognition/tpl/calculator/calculatorTemplate.html --%>

<div id="recogCalcWrapper" class="recogCalcWrapper">

    <div class="msgSelectRatingInstruction" style="display:none"><cms:contentText key="SELECT_RATING" code="recognitionSubmit.delivery.purl"/></div>
	<h1><cms:contentText key="AWARD_TITLE" code="calculator.response"/></h1>    
	<button type="button" id="recogCalcCloseBtn" class="close recogCalcCloseBtn"><i class="icon-close"></i></button>
	<ul>
		<li class="recogCalcRatingLabel"><cms:contentText key="RATING" code="recognitionSubmit.delivery.purl"/></li>   

		{{#if hasScore}}
		<li class="recogCalcScoreLabel">{{scoreLabel}}</li>
		{{/if}}

		{{#if hasWeight}}
		<li class="recogCalcWeightLabel">{{weightLabel}}</li>
		{{/if}}
	</ul>
	<div id="recogCalcInnerWrapper" class="recogCalcInnerWrapper">
		{{{criteriaDiv}}}
	</div>
	{{#if showPayTable}}
	<a id="recogCalcPayoutTableLink" class="recogCalcPayoutTableLink" href="#"><i class="icon-th"></i><cms:contentText key="VIEW_PAYOUT_GRID" code="calculator.response"/></a>
	{{/if}}
</div>
