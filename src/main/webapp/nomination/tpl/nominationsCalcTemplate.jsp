<div id="nominationsCalcWrapper" class="nominationsCalcWrapper">

    <!-- JAVA NOTE: set the i18n for "Select Rating" calc tooltip dropdown -->
    <div class="msgSelectRatingInstruction" style="display:none"><cms:contentText key="SELECT_RATING" code="calculator.response"/></div>

    <h1><cms:contentText key="NOMINATION_AWARD_TITLE" code="calculator.response"/></h1>
    <button type="button" id="nominationCalcCloseBtn" class="close nominationsCalcCloseBtn"><i class="icon-close"></i></button>
    <ul>
        <li class="nominationsCalcRatingLabel"><cms:contentText key="RATING" code="calculator.response"/></li>

        {{#if hasScore}}
        <li class="nominationsCalcScoreLabel">{{scoreLabel}}</li>
        {{/if}}

        {{#if hasWeight}}
        <li class="nominationsCalcWeightLabel">{{weightLabel}}</li>
        {{/if}}
    </ul>
    <div id="nominationsCalcInnerWrapper" class="nominationsCalcInnerWrapper">
        {{{criteriaDiv}}}
    </div>
    <a id="nominationsCalcPayoutTableLink" class="nominationsCalcPayoutTableLink" href="#"><i class="icon-grid-squares-2"></i><cms:contentText key="VIEW_PAYOUT_GRID" code="calculator.response"/></a>
    <!-- {{#if showPayTable}} -->

    <!-- {{/if}} -->
</div>
