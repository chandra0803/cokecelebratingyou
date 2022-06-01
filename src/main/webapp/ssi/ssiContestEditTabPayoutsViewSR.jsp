<%@ include file="/include/taglib.jspf"%>
<h4 class="defaultName"></h4>

<!-- JAVA: i18n these -->
<div id="_msgPoints" style="display:none"><cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" /></div>
<div id="_msgUnits" style="display:none"></div>
<div id="_msgLockPageBottom" style="display:none">
    <cms:contentText key="SELECT_ACTIVITY_PAYOUT" code="ssi_contest.payout_stepitup" /><br><cms:contentText key="AND_PAYOUT_FIRST" code="ssi_contest.payout_stepitup" />
</div>


<div class="control-group activityDescriptionWrapper">
    <label class="control-label" for="activityDescriptionInput">
        <cms:contentText key="ACTIVITY_DESCRIPTION" code="ssi_contest.payout_dtgt" />
        <i class="icon-info pageView_help"
           data-help-content="<cms:contentText key="DESC_SPECIFIC_ACTIVITY" code="ssi_contest.payout_stepitup" />"></i>
    </label>
    <div class="controls">
        <input type="text" id="activityDescriptionInput autoBind" class="activityDescriptionInput autoBind" data-model-key="activityDescription" maxlength="50">
    </div>
</div>


<h5><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.payout_objectives" /></h5>
<div class="control-group">
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

<h5><cms:contentText key="INC_MIN_QUALIFIER" code="ssi_contest.payout_stackrank" /></h5>
<div class="control-group">
    <label class="radio" for="minQualYes">
        <input type="radio" class="minQualRadio autoBind" id="minQualYes" name="includeMinimumQualifier" value="yes" data-model-key="includeMinimumQualifier">
        <cms:contentText key="YES" code="system.general" />
    </label>
    <div class="minimumQualifierWrap">
        <span class="currSymb act"></span>
        <input type="text" class="minimumQualifierInput input-mini autoBind" data-model-key="minimumQualifier">
        <span class="currDisp act"></span>
    </div>

    <label class="radio" for="minQualNo">
        <input type="radio" class="minQualRadio autoBind" id="minQualNo" name="includeMinimumQualifier" value="no" data-model-key="includeMinimumQualifier">
        <cms:contentText key="NO" code="system.general" />
    </label>
</div>

<hr class="section">

<h5><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.payout_dtgt" /></h5>
<div class="control-group">
    <label class="radio" for="payoutTypePoints">
        <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypePoints" name="payoutType" value="points" data-model-key="payoutType">
        <cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" />
    </label>
    <label class="radio" for="payoutTypeOther">
        <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypeOther" name="payoutType" value="other" data-model-key="payoutType">
        <cms:contentText key="OTHER" code="ssi_contest.payout_dtgt" />
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="OTHER_HELP_TEXT" code="ssi_contest.payout_stackrank" />"></i>
    </label>
    <div class="otherPayoutTypeWrapper" style="display:none">
        <div><cms:contentText key="VALUE_OF_AWARD_IN" code="ssi_contest.payout_objectives" /></div>
        <select name="otherPayoutTypeSelect" class="autoBind" id="otherPayoutTypeSelect" data-model-key="otherPayoutTypeId">
            <!-- dyn using subTpl.currencyTypeOptions -->
        </select>
    </div>
</div>

<div class="billToRoot"></div>

<hr class="section">

<div class="control-group orderWrapper">
    <h5>
        <cms:contentText key="SORT_ORDER" code="ssi_contest.payout_stackrank" />
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="SORT_HELP_TEXT" code="ssi_contest.payout_stackrank" />"></i>
    </h5>
    <label class="radio" for="stackRankingOrderDesc">
        <input type="radio" class="stackRankingOrderRadio autoBind" id="stackRankingOrderDesc" name="stackRankingOrder" value="desc" data-model-key="stackRankingOrder">
        <cms:contentText key="HIGH_TO_LOW" code="ssi_contest.payout_stackrank" />
    </label>
    <label class="radio" for="stackRankingOrderAsc">
        <input type="radio" class="stackRankingOrderRadio autoBind" id="stackRankingOrderAsc" name="stackRankingOrder" value="asc" data-model-key="stackRankingOrder">
        <cms:contentText key="LOW_TO_HIGH" code="ssi_contest.payout_stackrank" />
    </label>
</div>

<hr class="section">

<h5><cms:contentText key="RANKS_PAYOUTS" code="ssi_contest.approvals.detail" /></h5>
<p><cms:contentText key="BUDGET_EXCEED_ERROR" code="ssi_contest.payout_stackrank" /></p>
<div class="ranksWrapper container-splitter with-splitter-styles">
    <table class="table ranksTable">
        <thead>
            <tr>
                <th class="rankNum"><cms:contentText key="RANK" code="ssi_contest.payout_stackrank" /></th>
                <th class="showOnOther">
                    <cms:contentText key="PAYOUT_DESC" code="ssi_contest.payout_stackrank" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="SR_PAYOUT_DESC_INFO" code="ssi_contest.payout_stackrank" />"></i>
                </th>
                <th>
                    <span class="hideOnOther" style="display: none">
                        <cms:contentText key="PAYOUT_AMOUNT" code="ssi_contest.payout_stackrank" />
                    </span>
                    <span class="showOnOther" style="display: none">
                        <cms:contentText key="VALUE" code="ssi_contest.payout_stackrank" />
                        <i class="icon-info pageView_help"
                            data-help-content="<cms:contentText key="EXPLANATION_OF_WHAT" code="ssi_contest.payout_stackrank" />"></i>
                    </span>
                </th>
                <th class="badgeTableHeader"><cms:contentText key="BADGE" code="ssi_contest.payout_stackrank" /></th>
                <th class="remove"><cms:contentText key="REMOVE" code="ssi_contest.payout_stackrank" /></th>
            </tr>
        </thead>

        <tbody>
            <tr class="msgNoRanks">
                <td colspan="5"><div class="alert"><cms:contentText key="NO_RANK_ADDED" code="ssi_contest.payout_stackrank" /></div></td>
            </tr>
            <!--subTpl.rankRow=
                <tr class="rankRow rankRow{{id}}" data-model-id="{{id}}">
                    <td class="rankNum">{{!altered by DOM manip, not tpl}}</td>
                    <td class="showOnOther">
                        <input data-model-id="{{id}}"
                            data-model-key="payoutDescription"
                            type="text"
                            class="payoutDescription rankRowAutoBind"
                            value="{{payoutDescription}}"
                            maxlength="50" />
                    </td>
                    <td>
                        <input data-model-id="{{id}}"
                            data-model-key="payoutAmount"
                            type="text"
                            class="payoutAmount rankRowAutoBind input-mini"
                            value="{{payoutAmount}}" />
                    </td>
                    {{#hasBadges}}
                    <td><div class="badgeView" data-msg-no-badge="<cms:contentText key="NO_BADGE" code="ssi_contest.payout_objectives" />">{{!dynamic}}</div></td>
                    {{/hasBadges}}
                    <td class="remove"><i class="icon-trash removeControl" data-model-id="{{id}}"></i></td>
                </tr>
            subTpl-->
        </tbody>

        <tfoot>
            <tr>
                <td colspan="5">
                    <button class="btn btn-primary addRankBtn">
                         <cms:contentText key="ADD_RANK" code="ssi_contest.payout_stackrank" /> <i class="icon-plus-circle"></i>
                    </button>
                </td>
            </tr>
        </tfoot>
    </table>
</div>


<div class="maxesSection">
    <div class="maxPayoutSection">
        <div class="maxPayoutWrapper">
            <cms:contentText key="MAXIMUM_PAYOUT" code="ssi_contest.payout_stackrank" />
            <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="maxPayout">--</span>
            <span class="currDisp pay"></span>
        </div>
    </div>
</div>

<hr class="section">

<div>
    <label class="control-label" for="contestGoal">
        <cms:contentText key="YOUR_CONTEST_GOAL" code="ssi_contest.payout_stackrank" />
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="GOAL_HELP_TEXT" code="ssi_contest.payout_stackrank" />"></i>
    </label>
    <div class="controls">
        <input type="text" id="contestGoal" class="contestGoalInput autoBind" data-model-key="contestGoal">
    </div>
</div>
