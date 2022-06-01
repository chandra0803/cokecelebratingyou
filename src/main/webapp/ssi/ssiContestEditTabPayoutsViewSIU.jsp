<%@ include file="/include/taglib.jspf"%>
<h4 class="defaultName"></h4>

<!-- JAVA: i18n these -->
<div id="_msgPoints" style="display:none"><cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" /></div>
<div id="_msgUnits" style="display:none"><cms:contentText key="UNITS_NUMBER" code="ssi_contest.payout_stepitup" /></div>
<div id="_msgCurrency" style="display:none"><cms:contentText key="CURRENCY_REVENUE" code="ssi_contest.payout_stepitup" /></div>
<div id="_msgLockPageBottom" style="display:none">
    <cms:contentText key="SELECT_ACTIVITY_PAYOUT" code="ssi_contest.payout_stepitup" /> <br> <cms:contentText key="AND_PAYOUT_FIRST" code="ssi_contest.payout_stepitup" />
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
<div class="control-group baselineControls" style="visibility: hidden">
    <label class="control-label" for="measureOverBaseline">
        <h5><cms:contentText key="MEASURE_OVER_INDV_BASELINE" code="ssi_contest.payout_stepitup" /></h5>
    </label>
    <div class="controls">
        <select name="measureOverBaseline" class="autoBind dropdown-toggle" id="measureOverBaseline" data-model-key="measureOverBaseline">
            <option value="no"><cms:contentText key="NO" code="ssi_contest.payout_dtgt" /></option>
            <option value="percent"><cms:contentText key="PERCENTAGE_OVER_BASELINE" code="ssi_contest.payout_stepitup" /></option>
            <option class="dynLang" value="currency" data-msg="" data-msg-currency="Currency over baseline">
                {
                    "units" : "<cms:contentText key="AMOUNT_OVER_BASELINE" code="ssi_contest.payout_stepitup" />" ,
                    "currency" : "<cms:contentText key="CURRENCY_OVER_BASELINE" code="ssi_contest.payout_stepitup" />"
                }
            </option>
        </select>
    </div>
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
            data-help-content="<cms:contentText key="OTHER_PAYOUT_HELP" code="ssi_contest.payout_stepitup" />"></i>
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

<h5><cms:contentText key="ADD_CONTEST_LEVELS" code="ssi_contest.payout_stepitup" /><small><cms:contentText key="TWO_FIVE_LEVELS" code="ssi_contest.payout_stepitup" /></small></h5><%--TODO: check to see why there is a stray </small> tag here --%>

<div id="ssiSIULevelCollectionViewWrapper">
    <!-- SSISIUActivityCollectionView -->
</div><!-- /#ssiSIULevelCollectionViewWrapper -->

<div class="includeStackRankingWrapper">
    <div class="control-group">
        <label class="checkbox" for="includeStackRanking">
            <input type="checkbox" class="includeStackRankingInput autoBind" id="includeStackRanking" name="includeStackRanking" data-model-key="includeStackRanking">
            <strong><cms:contentText key="INCLUDE_STACK_RANK" code="ssi_contest.payout_dtgt" /></strong>
            <i class="icon-info pageView_help"
                data-help-content="<cms:contentText key="STACK_RANK_HELP" code="ssi_contest.payout_stepitup" />"></i>
        </label>
    </div>
</div>

<hr class="section">

<div class="control-group includeBonusWrapper" style="display: block;">
    <label class="checkbox" for="includeBonus">
        <input type="checkbox" class="includeBonusInput autoBind" id="includeBonus" name="includeBonus" data-model-key="includeBonus">
        <strong><cms:contentText key="INCLUDE_BONUS" code="ssi_contest.payout_objectives" /></strong>
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="BONUS_HELP" code="ssi_contest.payout_stepitup" />"></i>
    </label>
    <div class="includeBonusDetailsWrapper" style="display:none">
        <p><cms:contentText key="FOR_EVERY" code="ssi_contest.payout_stepitup" />
            <input type="text" class="bonusForEveryInput input-mini autoBind" data-model-key="bonusForEvery" />
            <span class="currSymb act"></span>
            <span class="currDisp act"></span>
            <cms:contentText key="OVER_LEVEL" code="ssi_contest.payout_stepitup" /> <span class="highestLevelNum">[levNum]</span> <cms:contentText key="PAX_EARN" code="ssi_contest.payout_stepitup" />
            <%-- ABOVE SHOULD READ: "over the Level <span class="highestLevelNum">[levNum]</span> objective, the participant will earn" --%>
            <input type="text" class="bonusPayoutInput input-mini autoBind" data-model-key="bonusPayout" />
            <span class="currSymb pay"></span>
            <span class="currDisp pay"></span>
        </p>
        <p>
            <cms:contentText key="INDIVIDUAL_BONUS_CAP" code="ssi_contest.approvals.summary" /><br>
            <input type="text" class="bonusPayoutCapInput input-small autoBind" data-model-key="bonusPayoutCap" />
            <span class="currSymb pay"></span>
            <span class="currDisp pay"></span>
        </p>
    </div>

    <hr class="section">
</div>

<h5 class="hideIfNoBaseline"><cms:contentText key="LEVELS_PAYOUT_DESC_PART1" code="ssi_contest.payout_stepitup" /><small><cms:contentText key="LEVELS_PAYOUT_DESC_PART2" code="ssi_contest.payout_stepitup" /></small></h5><%--TODO: check to see why there is a stray </small> tag here --%>

<div id="ssiParticipantsSiU">
    <div style="background:gray;height:100px"><cms:contentText key="PARTICIPANT_PAGINATION_LOADING" code="ssi_contest.payout_stepitup" /></div>
    <!-- ParticipantPaginatedView -->
</div><!-- /#ssiParticipantsSiU -->

<%-- REMOVED 5/15 div class="maxesSection">
    <h6 class="hideIfNoBaseline">
        <cms:contentText key="CLICK" code="ssi_contest.payout_objectives" />
        <cms:contentText key="CALCULATE_TOTALS" code="ssi_contest.payout_objectives" />
        <cms:contentText key="TOVIEWMAXIMUMAMOUNT" code="ssi_contest.payout_objectives" />
    </h6>

    <div class="maxPayoutSection">
        <div class="maxPayoutWrapper">
            <cms:contentText key="MAXIMUMPAYOUT" code="ssi_contest.payout_objectives" />:
            <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="maxPayout">--</span>
            <span class="currDisp pay"></span>
        </div>
        <div class="maxPayoutWithBonusWrapper">
            <cms:contentText key="WITHBONUS" code="ssi_contest.payout_objectives" />:
            <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="maxPayoutWithBonus">--</span>
            <span class="currDisp pay"></span>
        </div>
    </div>
    <div>
        <div>
            <cms:contentText key="MAXIMUMCONTESTPOTENTIAL" code="ssi_contest.payout_objectives" />:
            <span class="currSymb act"></span><span class="calcTotals currLabel" data-model-key="maxPotential">--</span>
            <span class="currDisp act"></span>
        </div>
    </div>
</div --%>

<div>


    <div class="contestEstimatorWrapper">

        <div class="contestEstimator">
            <h5><cms:contentText key="CONTESTGOAL" code="ssi_contest.payout_objectives" /></h5>
            <p><cms:contentText key="USE_SLIDERS" code="ssi_contest.payout_stepitup" /></p>

            <div><b><cms:contentText key="ESTIMATEDPARTICIPANTACHIEVEMENT" code="ssi_contest.payout_objectives" /></b></div>


            <div class="maxPayoutSection">
                <div class="maxPayoutWrapper">
                    <cms:contentText key="PAYOUT" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="estMaxPayout">--</span>
                    <span class="currDisp pay"></span>
                </div>
                <div class="maxPayoutWithBonusWrapper">
                    <cms:contentText key="WITHBONUS" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="estBonusPayout">--</span>
                    <span class="currDisp pay"></span>
                </div>
            </div>

            <div class="maxPayoutSection">
                <div class="contestGoalWrapper ib">
                    <label class="control-label ib" for="contestGoal">
                        <cms:contentText key="CONTESTGOAL" code="ssi_contest.payout_objectives" />
                        <div class="goalPercent"></div>
                        <i class="icon-info pageView_help"
                            data-help-content="<cms:contentText key="GOAL_INFO" code="ssi_contest.payout_stepitup" />"></i>
                    </label>
                    &nbsp;
                    <div class="ib">
                        <div class="controls">
                            <span class="currSymb act"></span>
                            <input type="text" id="contestGoal" class="contestGoalInput" data-model-key="contestGoal" maxlength="10">
                            <span class="currDisp act"></span>
                        </div>
                    </div>
                </div>
                <div class="maxPayoutWrapper ib">
                    <cms:contentText key="PAYOUT" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="estMaxPayout">--</span>
                    <span class="currDisp pay"></span>
                </div>
                <div class="estPayoutWithBonusWrapper ib">
                    <cms:contentText key="WITHBONUS" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb pay"></span><span class="calcTotals currLabel" data-model-key="estBonusPayout">--</span>
                    <span class="currDisp pay"></span>
                </div>
            </div>

            <%-- REMOVED 5/15 div>
                <div>
                    <cms:contentText key="ESTIMATEDCONTESTPOTENTIAL" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb act"></span><span class="calcTotals currLabel" data-model-key="estMaxPotential">--</span>
                    <span class="currDisp act"></span>
                </div>
            </div --%>

            <div class="estimatorsList" data-msg-no-level="<cms:contentText key='NO_LEVEL_ACHIEVED' code='ssi_contest.participant' />">

                <!--subTpl.estimator=
                {{#levels}}
                <div class="estimatorWrapper{{#isDisabled}} disabled{{/isDisabled}}">
                    {{#if label}}
                        <div><b>{{label}} {{amount}}</b></div>
                    {{else}}
                        <div><b><cms:contentText key='LEVEL' code='ssi_contest.participant' /> {{sequenceNumber}}</b></div>
                    {{/if}}
                    <div>
                        <cms:contentText key='ESTIMATED_ACTIVITY' code='ssi_contest.payout_dtgt' />:
                        <b class="goalPercent">(100%)</b>
                    </div>
                    <div class="sliderContainer">
                        <span class="sliderMin">0%</span>
                        <input class="estimateSlider slider{{id}}" data-level-id="{{id}}" type="text" />
                        <span class="sliderMax">100%</span>
                    </div>
                </div>
                {{/levels}}
                subTpl-->

            </div>

        </div><!-- /.contestEstimator -->
    </div><!-- /.contestEstimatorWrapper -->

</div>

