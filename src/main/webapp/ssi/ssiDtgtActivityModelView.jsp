<%@ include file="/include/taglib.jspf"%>

<div class="activityItem{{#_isActiveNew}} isActiveNew{{/_isActiveNew}}" data-activity-id="{{id}}">

    {{#unless _isActiveNew}}
    <div class="activityHeaders">
        <div class="activityHeader activityDesc">
            <span data-model-key="description">&nbsp;</span>
        </div>
        <div class="activityHeader activityForEvery">
            <span class="currSymb act"></span><span data-model-key="forEvery">&nbsp;</span>
        </div>
        <div class="activityHeader activityEarn">
            <span class="currSymb pay"></span><span data-model-key="willEarn">&nbsp;</span>
        </div>
        <div class="activityHeader activityMinQual">
            <span class="currSymb act"></span><span data-model-key="minQualifier">&nbsp;</span>
        </div>
        <div class="activityHeader activityIndPayCap">
            <span class="currSymb pay"></span><span data-model-key="individualPayoutCap">&nbsp;</span>
        </div>
        <div class="activityHeader activityMaxActPay">
            <span class="currSymb pay"></span><span data-model-key="maxPayout">&nbsp;</span>
        </div>
        <div class="activityHeader activityActGoal">
            <span class="currSymb act"></span><span data-model-key="goal">&nbsp;</span>
        </div>
        <div class="activityHeader activityMaxActPot">
            <span class="currSymb act"></span><span data-model-key="maxPotential">&nbsp;</span>
        </div>
        <div class="activityHeader activityEdit">
            <i class="icon-pencil2 editControl"></i>
        </div>
        <div class="activityHeader activityRemove">
            <i class="icon-trash removeControl"></i>
        </div>
    </div><!-- /.activityHeaders -->
    {{/unless}}{{!-- _isActiveNew --}}

    <div class="activityDetails" style="display:none">
        {{#if _isActiveNew}}
            <h5>
                <i class="icon-plus-circle"></i>
                <cms:contentText key="ADD_CONTEST_ACTIVITY" code="ssi_contest.payout_dtgt" />
            </h5>
        {{else}}
            <h5>
                <i class="icon-pencil2"></i>
                <cms:contentText key="EDIT_ACTIVITY_DETAILS" code="ssi_contest.payout_dtgt" />
            </h5>
        {{/if}}

        <div class="activityDetailDesc clearfix">
            <label for="description{{id}}">
                <cms:contentText key="ACTIVITY_DESCRIPTION" code="ssi_contest.payout_dtgt" />
                <i class="icon-info pageView_help"
                    data-help-content="<cms:contentText key="DESC_SPECIFIC_ACTIVITY" code="ssi_contest.payout_stepitup" />"></i>
            </label>
            <input type="text" id="description{{id}}" data-model-key="description" maxlength="50" />
        </div>

        <div class="activityDetailForEvery clearfix">
            <label for="forEvery{{id}}">
                <cms:contentText key="FOR_EVERY" code="ssi_contest.payout_dtgt" />
                <i class="icon-info pageView_help"
                data-help-content="<cms:contentText key="ACTIVITY_AMOUNT_DESC" code="ssi_contest.payout_objectives" />"></i>
            </label>

            <input type="text" id="forEvery{{id}}" data-model-key="forEvery" />
            <span class="currSymb act"></span>
            <span class="currDisp act"></span>
        </div>

            <div class="activityDetailValueDesc clearfix" style="display:none">
            <label for="valueDescription{{id}}">
                <cms:contentText key="PARTICIPANT_EARN" code="ssi_contest.payout_dtgt" />
            </label>
            <input type="text"  id="valueDescription{{id}}" data-model-key="valueDescription" maxlength="50" />
        </div>

        <div class="activityDetailEarn clearfix">
            <label class="pointsPayoutTypeMsg" for="willEarn{{id}}">
                <cms:contentText key="PARTICIPANT_EARN" code="ssi_contest.payout_dtgt" />
            </label>

            <label class="otherPayoutTypeMsg" for="willEarn{{id}}">
                <cms:contentText key="VALUE_OF_AWARD" code="ssi_contest.payout_dtgt" />
                <i class="icon-info pageView_help"
                    data-help-content="<cms:contentText key="EXP_VALUE_OF_AWARD" code="ssi_contest.payout_dtgt" />"></i>
            </label>
            <input type="text" id="willEarn{{id}}" data-model-key="willEarn" />
            <span class="currSymb pay"></span>
            <span class="currDisp pay"></span>
        </div>

        <div class="activityDetailMinQual clearfix">
            <label for="minQualifier{{id}}"><cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.payout_dtgt" /></label>
            <input type="text"  for="minQualifier{{id}}" data-model-key="minQualifier" />
            <span class="currSymb act"></span>
            <span class="currDisp act"></span>
        </div>

        <div class="activityDetailIndPayCap clearfix">
            <label for="individualPayoutCap{{id}}"><cms:contentText key="INDIVIDUAL_PAYOUT_CAP" code="ssi_contest.payout_dtgt" /></label>
            <input type="text"  for="individualPayoutCap{{id}}" data-model-key="individualPayoutCap" />
            <span class="currSymb pay"></span>
            <span class="currDisp pay"></span>
        </div>

        <br>

        <div class="activityDetailPaxCount clearfix">
            <label><cms:contentText key="NO_OF_PARTICIPANTS" code="ssi_contest.payout_dtgt" /></label>
            <span data-model-key="participantCount"></span>
        </div>

        <br>

        <%-- REMOVED 5/27
        <div class="activityDetailMaxActPay">
            <label><cms:contentText key="MAXIMUM_ACTIVITY_PAYOUT" code="ssi_contest.payout_dtgt" />: </label>
            <span class="currSymb pay"></span><span class="maxPay">--</span>
            <span class="currDisp pay"></span>
        </div>

        <div class="activityDetailMaxActPot">
            <label><cms:contentText key="MAXIMUM_ACTIVITY_POTENTIAL" code="ssi_contest.payout_dtgt" />: </label>
            <span class="currSymb act"></span><span class="maxPot">--</span>
            <span class="currDisp act"></span>
        </div>
        --%>

        <div class="contestEstimatorWrapper clearfix">
            <div class="contestEstimator">

                <h5><cms:contentText key="ACTIVITY_ESTIMATOR" code="ssi_contest.payout_dtgt" /></h5>
                <p>
                    <cms:contentText key="USE_SLIDER_INFO" code="ssi_contest.payout_dtgt" />
                </p>

                <div class="activityDetailActGoal ib">
                    <label for="goal{{id}}" class="ib">
                        <cms:contentText key="YOUR_ACTIVITY_GOAL" code="ssi_contest.payout_dtgt" />
                        <div class="goalPercent"><!-- dyn --></div>
                        <i class="icon-info pageView_help"
                            data-help-content="<cms:contentText key="GOAL_EXPLANATION" code="ssi_contest.payout_dtgt" />"></i>
                    </label>
                    &nbsp;
                    <span class="currSymb act"></span>
                    <input type="text"  for="goal{{id}}" data-model-key="goal" />
                    <span class="currDisp act"></span>
                </div>

                <div class="maxPayoutSection ib">
                    <div class="maxPayoutWrapper">
                        <cms:contentText key="ESTIMATED_ACTIVITY_PAYOUT" code="ssi_contest.payout_dtgt" />
                        <span class="currSymb pay"></span><span class="estPay">--</span>
                        <span class="currDisp pay"></span>
                    </div>
                </div>

                <%-- REMOVED 5/27
                <div>
                    <div>
                        <cms:contentText key="ESTIMATED_ACTIVITY_POTENTIAL" code="ssi_contest.payout_dtgt" />
                        <span class="currSymb act"></span><span class="estPot">--</span>
                        <span class="currDisp act"></span>
                    </div>
                </div>
                --%>

                <div><b><cms:contentText key="ESTIMATED_ACTIVITY" code="ssi_contest.payout_dtgt" /></b></div>
                <div class="sliderContainer">
                    <span class="sliderMin"><cms:contentText key="ZERO_PERCENT" code="ssi_contest.payout_dtgt" /></span>
                    <input class="estimateSlider" type="text" />
                    <span class="sliderMax"><cms:contentText key="HUNDRED_PERCENT" code="ssi_contest.payout_dtgt" /></span>
                </div>

            </div><!-- /.contestEstimator -->
        </div><!-- /.contestEstimatorWrapper -->


        <div class="activityDetailItemActions">
            <button class="btn btn-primary saveActivityBtn">
                <cms:contentText key="BTN_SAVE_ACTIVITY" code="ssi_contest.payout_dtgt" />
            </button>
            <button class="btn cancelActivityBtn">
                <cms:contentText key="CANCEL" code="system.button" />
            </button>
        </div>

    </div>



    <div class="ssiDtgtActivityModelViewErrorMsgs" style="display:none">
        <b class="req"><cms:contentText key="FIELD_REQUIRED" code="ssi_contest.payout_dtgt" /></b>
        <b class="reqNum"><cms:contentText key="ENTER_WHOLE_NUMBER" code="ssi_contest.payout_dtgt" /></b>
        <b class="reqDec_2"><cms:contentText key="TWO_DECIMAL_VALIDATION" code="ssi_contest.creator" /></b>
        <b class="reqDec_4"><cms:contentText key="FOUR_DECIMAL_VALIDATION" code="ssi_contest.creator" /></b>
        <b class="reqDec_2_pos"><cms:contentText key="GOAL_REQ_ERROR" code="ssi_contest.generalInfo" /></b>
        <b class="reqDec_4_pos"><cms:contentText key="FOUR_DECIMAL_POSITIVE_NUMBER" code="ssi_contest.creator" /></b>
        <b class="numBeyondMax"><cms:contentText key="TOO_LONG" code="ssi_contest.payout_stepitup" /></b>
        <b class="mustEditSave"><cms:contentText key="SAVE_OR_CANCEL" code="ssi_contest.payout_dtgt" /></b>
        <b class="uniqueDescReq"><cms:contentText key="REQUIRED_UNIQUE_DESC" code="ssi_contest.payout_stepitup" /></b>
    </div><!-- /.ssiDtgtActivityModelViewErrorMsgs -->

</div>
