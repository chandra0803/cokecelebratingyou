<%@ include file="/include/taglib.jspf"%>
<div id="_msgLockPageBottom" style="display:none">
    <cms:contentText key="SELECT_ACTIVITY_PAYOUT" code="ssi_contest.payout_stepitup" /><br>
    <cms:contentText key="AND_PAYOUT_FIRST" code="ssi_contest.payout_stepitup" />
</div>

<h4 class="defaultName"></h4>

<!-- JAVA: i18n these -->
<div id="_msgPoints" style="display:none"><cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" /></div>
<div id="_msgUnits" style="display:none"></div>


<h5><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.payout_objectives" /></h5>
<div class="control-group validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="MAKE_A_CHOICE" code="ssi_contest.payout_dtgt" />&quot;}">
    <label class="radio" for="measureActivityCurrency">
        <input type="radio" class="measureTypeRadio autoBind" id="measureActivityCurrency" name="measureType" value="currency" data-model-key="measureType">
        <cms:contentText key="CURRENCY_REVENUE" code="ssi_contest.creator" />
    </label>
    <div class="currencyTypeWrapper" style="display:none">
        <div><cms:contentText key="CURRENCY_TYPE" code="ssi_contest.payout_dtgt" /></div>
        <select name="currencyTypeSelect" class="autoBind dropdown-toggle" id="currencyTypeSelect" data-model-key="currencyTypeId">
            <!--subTpl.currencyTypeOptions=
            {{#currencyTypes}}
                <option value="{{id}}">{{name}}</option>
            {{/currencyTypes}}
            subTpl-->
        </select>
    </div>
    <label class="radio" for="measureActivityUnits">
        <input type="radio" class="measureTypeRadio autoBind" id="measureActivityUnits" name="measureType" value="units" data-model-key="measureType">
        <cms:contentText key="UNITS_OR_NUMBERS" code="ssi_contest.payout_dtgt" />
    </label>
</div>

<hr class="section">

<h5><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.payout_dtgt" /></h5>
<div class="control-group validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="MAKE_A_CHOICE" code="ssi_contest.payout_dtgt" />&quot;}">
    <label class="radio" for="payoutTypePoints">
        <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypePoints" name="payoutType" value="points" data-model-key="payoutType">
        <cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" />
    </label>
    <label class="radio" for="payoutTypeOther">
        <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypeOther" name="payoutType" value="other" data-model-key="payoutType">
        <cms:contentText key="OTHER" code="ssi_contest.payout_dtgt" />
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="OTHER_PAYOUT_HELP" code="ssi_contest.payout_dtgt" />"></i>
    </label>
    <div class="otherPayoutTypeWrapper" style="display:none">
        <div><cms:contentText key="VALUE_OF_AWARD_IN" code="ssi_contest.payout_objectives" /></div>
        <select name="otherPayoutTypeSelect" class="autoBind dropdown-toggle" id="otherPayoutTypeSelect" data-model-key="otherPayoutTypeId">
            <!-- dyn using subTpl.currencyTypeOptions -->
        </select>
    </div>
</div>

<div class="billToRoot"></div>

<hr class="section">

<div class="includeStackRankingWrapper">
    <div class="control-group">
        <label class="checkbox" for="includeStackRanking">
            <input type="checkbox" class="includeStackRankingInput autoBind" id="includeStackRanking" name="includeStackRanking" data-model-key="includeStackRanking">
            <strong><cms:contentText key="INCLUDE_STACK_RANK" code="ssi_contest.payout_dtgt" /></strong>
            <i class="icon-info pageView_help"
                data-help-content="<cms:contentText key="STACK_RANK_HELP" code="ssi_contest.payout_dtgt" />"></i>
        </label>
    </div>
</div>

<hr class="section">

<h5><cms:contentText key="ADD_CONTEST_ACTIVITIES" code="ssi_contest.payout_dtgt" /><small><cms:contentText key="ADD_UPTO" code="ssi_contest.payout_dtgt" /></small></h5>

<div id="ssiDTGTActivityCollectionViewWrapper">
    <!-- SSEContestEditDTGTActivityCollectionView -->
</div><!-- /#ssiContestEditDTGTActivityCollectionView -->

