<div id="nominationsCalcScoreWrapper" class="nominationsCalcScoreWrapper">
    <div id="nominationsCalcTotalWrapper" class="nominationsCalcTotalWrapper">
        <span id="nominationsCalcTotalLabel" class="nominationsCalcTotalLabel"><cms:contentText key="TOTAL_SCORE" code="calculator.response"/>: </span>
        <span id="nominationsCalcTotal" class="nominationsCalcTotal">{{totalScore}}</span>
    </div>
    {{#if isRange}}
    <p><cms:contentText key="SELECT_AMOUNT" code="calculator.response"/> {{{awardRange}}}</p>
    {{/if}}
    {{#if hasFixed}}
    <p class="nominationsCalcFixedAward"><cms:contentText key="AWARD_AMOUNT" code="calculator.response"/>: {{fixedAmount}}</p>
    {{/if}}
    {{#if hasAwardLevel}}
    <p class="nominationsCalcFixedAward"><cms:contentText key="AWARD_LEVEL" code="calculator.response"/>: {{{awardLevel}}}</p>
    {{/if}}
    <button disabled="disabled"><cms:contentText key="SAVE" code="system.button"/></button>
</div>
