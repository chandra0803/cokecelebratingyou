<%@ include file="/include/taglib.jspf"%>

<!-- JAVA: i18n these -->
<div id="_msgPoints" style="display:none"><cms:contentText key="POINTS" code="ssi_contest.participant" /></div>
<div id="_msgUnits" style="display:none"></div>
<div id="_msgLockPageBottom" style="display:none">
    <cms:contentText key="SELECT_ACTIVITY_PAYOUT" code="ssi_contest.payout_stepitup" /><br>
    <cms:contentText key="AND_PAYOUT_FIRST" code="ssi_contest.payout_stepitup" />
</div>

<h4 class="defaultName"></h4>


<h5><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.payout_objectives" /></h5>
<div class="control-group validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CHOOSE_ONE_ERROR" code="ssi_contest.payout_objectives" />&quot;}">
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
        <cms:contentText key="UNITS_NUMBER" code="ssi_contest.payout_objectives" />
    </label>
</div>

<hr class="section">

<h5><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.payout_objectives" /></h5>
<div class="control-group validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CHOOSE_ONE_ERROR" code="ssi_contest.payout_objectives" />&quot;}">
    <label class="radio" for="payoutTypePoints">
        <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypePoints" name="payoutType" value="points" data-model-key="payoutType">
        <cms:contentText key="POINTS" code="ssi_contest.payout_objectives" />
    </label>
    <label class="radio" for="payoutTypeOther">
        <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypeOther" name="payoutType" value="other" data-model-key="payoutType">
        <cms:contentText key="OTHER" code="ssi_contest.payout_objectives" />
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="OTHER_INFO" code="ssi_contest.payout_objectives" />"></i>
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

<div class="badgesOuterWrapper control-group" style="display:none">
    <h5><cms:contentText key="BADGE" code="ssi_contest.payout_objectives" /></h5>
    <div class="badgesSelectorView" data-msg-no-badge="<cms:contentText key="NO_BADGE" code="ssi_contest.payout_objectives" />"><!-- dyn --></div>
</div>

<hr class="section">

<div class="control-group includeBonusWrapper">
    <label class="checkbox" for="includeBonus">
        <input type="checkbox" class="includeBonusInput autoBind" id="includeBonus" name="includeBonus" data-model-key="includeBonus">
        <strong><cms:contentText key="INCLUDE_BONUS" code="ssi_contest.payout_objectives" /></strong>
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="BONUS_INFO" code="ssi_contest.payout_objectives" />"></i>
    </label>

    <hr class="section">
</div>





<h5>
    <cms:contentText key="IS_OBJ_DES_SAME" code="ssi_contest.payout_objectives" />
    <i class="icon-info pageView_help"
        data-help-content="<cms:contentText key="IS_OBJ_DES_SAME_HELP" code="ssi_contest.payout_objectives" />"></i>
</h5>
<div class="control-group validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="MAKE_A_CHOICE" code="ssi_contest.payout_stepitup" />&quot;}">
    <label class="radio" for="sameActivityDescriptionForAllYes">
        <input type="radio" class="sameActivityDescriptionForAllRadio autoBind" id="sameActivityDescriptionForAllYes" name="sameActivityDescriptionForAll" value="true" data-model-key="sameActivityDescriptionForAll">
        <cms:contentText key="YES" code="system.button" />
    </label>
    <label class="radio" for="sameActivityDescriptionForAllOther">
        <input type="radio" class="sameActivityDescriptionForAllRadio autoBind" id="sameActivityDescriptionForAllOther" name="sameActivityDescriptionForAll" value="false" data-model-key="sameActivityDescriptionForAll">
        <cms:contentText key="NO" code="system.button" />
    </label>
</div>


<div class="control-group activityDescriptionWrapper validateme"
    data-validate-flags="nonempty"
    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="MAKE_A_DESCRIPTION" code="ssi_contest.payout_stepitup" />&quot;}"
    style="display:none">
    <hr class="section">

    <label class="control-label" for="activityDescriptionInput">
        <strong><cms:contentText key="OBJECTIVES_DESCRIPTION" code="ssi_contest.preview" /></strong>
        <i class="icon-info pageView_help"
            data-help-content="<cms:contentText key="OBJECTIVE_DESC_INFO" code="ssi_contest.payout_objectives" />"></i>
    </label>
    <div class="controls">
        <input type="text" id="activityDescriptionInput autoBind" class="activityDescriptionInput autoBind" data-model-key="activityDescription" maxlength="50">
    </div>
</div>



<div class="includeStackRankingWrapper">
    <hr class="section">
    <div class="control-group">
        <label class="checkbox" for="includeStackRanking">
            <input type="checkbox" class="includeStackRankingInput autoBind" id="includeStackRanking" name="includeStackRanking" data-model-key="includeStackRanking">
            <strong><cms:contentText key="INCLUDE_STACK_RANKING" code="ssi_contest.payout_objectives" /></strong>
            <i class="icon-info pageView_help"
                data-help-content="<cms:contentText key="STACK_RANK_INFO" code="ssi_contest.payout_objectives" />"></i>
        </label>
    </div>
    <div class="control-group stackRankingOrderWrapper">
        <label>
        	<cms:contentText key="SORT_ORDER" code="ssi_contest.payout_objectives" />
        </label>
        <label class="radio" for="stackRankingOrderDesc">
            <input type="radio" class="stackRankingOrderRadio autoBind" id="stackRankingOrderDesc" name="stackRankingOrder" value="desc" data-model-key="stackRankingOrder">
            <cms:contentText key="HIGH_TO_LOW" code="ssi_contest.payout_objectives" />
        </label>
        <label class="radio" for="stackRankingOrderAsc">
            <input type="radio" class="stackRankingOrderRadio autoBind" id="stackRankingOrderAsc" name="stackRankingOrder" value="asc" data-model-key="stackRankingOrder">
            <cms:contentText key="LOW_TO_HIGH" code="ssi_contest.payout_objectives" />
        </label>
    </div>
</div>

<hr class="section">

<h5 class="paxTableTitle ifBonusShow">
    <cms:contentText key="PAX_OBJ_PAYOUTS_BONUSES" code="ssi_contest.payout_objectives" />
</h5>
<h5 class="paxTableTitle ifBonusHide" style="display:none">
    <cms:contentText key="PAX_OBJ_PAYOUTS" code="ssi_contest.payout_objectives" />
</h5>

<!-- hiding the objectiveUpload as it was removed but may come back -->
<div class="objectiveUploadWrapper" style="display:none">
    <cms:contentText key="OBJECTIVES_DOCUMENT" code="ssi_contest.payout_objectives" />
    <div class="fileInputWrapper">
        <input type="file" name="documentFile" class="objectiveFileInput uploaderFileInput">
        <button class="btn btn-primary btn-small uploadDocBtn">

            <span class="noDocMsg docBtnMsg" style="display:none"><cms:contentText key="UPLOAD" code="ssi_contest.payout_objectives" /></span>
            <span class="hasDocMsg docBtnMsg" style="display:none"><cms:contentText key="CHANGE" code="ssi_contest.payout_objectives" /></span>
            <i class="icon-upload-1"></i>
        </button>
        <span class="label label-important uploadError" style="display:none">
            <cms:contentText key="UPLOAD_ERROR" code="ssi_contest.payout_objectives" /> <span><!-- js dyn error text --></span>
        </span>
        <span class="docOrigName" style="display:none"><!-- dyn --></span>
        <a href="#" class="removeUpload" style="display:none"><i class="icon-trash"></i></a>
    </div>

    <div class="uploadingIndicator" style="display:none">
        <div class="uploadingSpinner"></div>
        <cms:contentText key="UPLOADING" code="ssi_contest.payout_objectives" />
        <!--
        <div class="progress progress-striped active" style="display:none">
            <div class="bar" style="width:0%"></div>
        </div>
        -->
    </div>
</div><!-- /.objectiveUploadWrapper -->


<div id="ssiParticipants_objectives">
    <!-- ParticipantPaginatedView -->
</div><!-- /#ssiParticipants_objectives -->


<%-- REMOVED 5/15 div>
    <h6>
        <cms:contentText key="CLICK" code="ssi_contest.payout_objectives" />
        <a href="#" class="calculateTotalsBtn" data-msg-loading="calculating..."><cms:contentText key="CALCULATE_TOTALS" code="ssi_contest.payout_objectives" /></a>
        <cms:contentText key="TOVIEWMAXIMUMAMOUNT" code="ssi_contest.payout_objectives" />
    </h6>

    <div class="maxPayoutSection">
        <div class="maxPayoutWrapper">
            <cms:contentText key="MAXIMUMPAYOUT" code="ssi_contest.payout_objectives" />:
            <span class="currSymb pay">
            </span><span class="calcTotalsAutoBind currLabel" data-model-key="maxPayout">--</span>
            <span class="currDisp pay"></span>
        </div>
        <div class="maxPayoutWithBonusWrapper">
            <cms:contentText key="WITHBONUS" code="ssi_contest.payout_objectives" />:
            <span class="currSymb pay">
            </span><span class="calcTotalsAutoBind currLabel" data-model-key="maxPayoutWithBonus">--</span>
            <span class="currDisp pay"></span>
        </div>
    </div>

    <div>
        <div>
            <cms:contentText key="MAXIMUMCONTESTPOTENTIAL" code="ssi_contest.payout_objectives" />:
            <span class="currSymb act">
            </span><span class="calcTotalsAutoBind currLabel" data-model-key="maxPotential">--</span>
            <span class="currDisp act"></span>
        </div>
    </div>

</div --%>


<div>

    <div class="contestEstimatorWrapper clearfix">
        <div class="contestEstimator">

            <h5><cms:contentText key="CONTESTGOAL" code="ssi_contest.payout_objectives" /></h5>
            <div class="small"><cms:contentText key="USESLIDERFORCONTESTGOAL" code="ssi_contest.payout_objectives" /></div>

            <div class="maxPayoutSection">
                <div class="contestGoalWrapper ib">
                    <label class="control-label ib" for="contestGoal">
                        <cms:contentText key="CONTESTGOAL" code="ssi_contest.payout_objectives" /> <div class="goalPercent"></div>
                        <i class="icon-info pageView_help"
                            data-help-content="<cms:contentText key="GOAL_INFO" code="ssi_contest.payout_objectives" />"></i>
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
                    <span class="currSymb pay">
                    </span><span class="calcTotalsAutoBind currLabel" data-model-key="estMaxPayout">--</span>
                    <span class="currDisp pay"></span>
                </div>
                <div class="maxPayoutWithBonusWrapper ib">
                    <cms:contentText key="WITHBONUS" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb pay">
                    </span><span class="calcTotalsAutoBind currLabel" data-model-key="estBonusPayout">--</span>
                    <span class="currDisp pay"></span>
                </div>
            </div>

            <%-- REMOVED 5/15 div>
                <div>
                    <cms:contentText key="ESTIMATEDCONTESTPOTENTIAL" code="ssi_contest.payout_objectives" />:
                    <span class="currSymb act">
                    </span><span class="calcTotalsAutoBind currLabel" data-model-key="estMaxPotential">--</span>
                    <span class="currDisp act"></span>
                </div>
            </div --%>

            <div><b><cms:contentText key="ESTIMATEDPARTICIPANTACHIEVEMENT" code="ssi_contest.payout_objectives" /></b></div>
            <div class="sliderContainer">
                <span class="sliderMin"><cms:contentText key="ZERO_PERCENTAGE" code="ssi_contest.creator" /></span>
                <input id="estimateSlider" type="text" />
                <span class="sliderMax"><cms:contentText key="HUNDRED_PERCENTAGE" code="ssi_contest.creator" /></span>
            </div>

        </div><!-- /.contestEstimator -->
    </div><!-- /.contestEstimatorWrapper -->



</div>
