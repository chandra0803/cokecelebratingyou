<%@ include file="/include/taglib.jspf"%>

<%-- from CVS version 1.2 of src/fe/core/apps/recognition/tpl/recognitionCalculatorScoreWrapperTemplate.html --%>

<div id="recogCalcScoreWrapper" class="recogCalcScoreWrapper">
	{{#if isDisplayScore}}
	<div id="recogCalcTotalWrapper" class="recogCalcTotalWrapper">
		<span id="recogCalcTotalLabel" class="recogCalcTotalLabel"><cms:contentText key="TOTAL_SCORE" code="recognitionSubmit.delivery.purl"/>: </span>
		<span id="recogCalcTotal" class="recogCalcTotal">{{totalScore}}</span>
	</div>
	{{/if}}
	{{#if isRange}}
	<p><cms:contentText key="SELECT_AMOUNT" code="recognitionSubmit.delivery.purl"/> {{{awardRange}}}</p>
	{{/if}}
	{{#if hasFixed}}
	<p class="recogCalcFixedAward"><cms:contentText key="AMOUNT" code="promotion.public.recognition"/>: {{fixedAmount}}</p>
	{{/if}}
	{{#if hasAwardLevel}}
	<p class="recogCalcFixedAward"><cms:contentText key="AMOUNT" code="promotion.public.recognition"/>: {{{awardLevel}}}</p>
	{{/if}}
	<button disabled="disabled"><cms:contentText key="SAVE" code="system.button"/></button>
</div>