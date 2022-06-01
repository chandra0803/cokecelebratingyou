<%@ include file="/include/taglib.jspf"%>
<div class="levelItem{{#_isActiveNew}} isActiveNew{{/_isActiveNew}}" data-level-id="{{id}}">

    {{#unless _isActiveNew}}
    <div class="levelHeaders">
        <div class="levelHeader levelSequenceNumber">
            <div data-model-key="sequenceNumber">&nbsp;</div>
            <div class="lowestLevel levelCaption" style="display:none">(<cms:contentText key="LOWEST_LEVEL" code="ssi_contest.payout_stepitup" />)</div>
            <div class="highestLevel levelCaption" style="display:none">(<cms:contentText key="HIGHEST_LEVEL" code="ssi_contest.payout_stepitup" />)</div>
        </div>
        <div class="levelHeader levelAmount">
            <span class="currSymb act">
            </span><span data-model-key="amount" data-msg-edit="edit">&nbsp;
            </span><span class="currSymbAfter act"></span>
        </div>
        <div class="levelHeader levelPayout">
            <span class="currSymb pay"></span><span data-model-key="payout">&nbsp;</span>
        </div>
        <div class="levelHeader levelPayoutDesc">&nbsp;
            <span data-model-key="payoutDescription"></span>
        </div>
        <div class="levelHeader levelBadge">
            <!-- dyn -->
        </div>
        <div class="levelHeader levelEdit">
            <i class="icon-pencil2 editControl"></i>
        </div>
        <div class="levelHeader levelRemove">
            <i class="icon-trash removeControl"></i>
        </div>
    </div><!-- /.levelHeaders -->
    {{/unless}}{{!-- _isActiveNew --}}

    <div class="levelDetails" style="display:none">

        {{#if _isActiveNew}}
            <h5>
                <i class="icon-plus-circle"></i>
                <cms:contentText key="ADD_CONTEST_LEVEL" code="ssi_contest.payout_stepitup" />
                {{_levelNum}}{{#eq _levelNum 1}}: <cms:contentText key="THIS_IS_LOWEST_LEVEL" code="ssi_contest.payout_stepitup" />{{/eq}}
            </h5>
        {{else}}
            <h5>
                <i class="icon-pencil2"></i>
                <cms:contentText key="EDIT_LEVEL_DETAILS" code="ssi_contest.payout_stepitup" />
                <span id="level{{id}}" data-model-key="sequenceNumber" ></span> <!--NEED FOR CM sequenceNumber needs to be inside cm string line Edit Level 2 Details-->
            </h5>
        {{/if}}

        <div class="levelDetailAmount clearfix">
            <label for="amount{{id}}">
                <span class="msg measureLabel amount hide">
                    <cms:contentText key="AMOUNT" code="ssi_contest.participant" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="ACTIVITY_AMOUNT_DESC" code="ssi_contest.payout_objectives" />"></i>
                </span>
                <span class="msg measureLabel currency hide">
                    <cms:contentText key="CURRENCY" code="ssi_contest.payout_dtgt" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="ACTIVITY_AMOUNT_DESC" code="ssi_contest.payout_objectives" />"></i>
                </span>
                <span class="msg measureLabel percentOverBaseline hide">
                    <cms:contentText key="PERCENTAGE_OVER_BASELINE" code="ssi_contest.payout_stepitup" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="PERCNT_BASE_INFO" code="ssi_contest.payout_stepitup" />"></i>
                </span>
                <span class="msg measureLabel amountOverBaseline hide">
                    <cms:contentText key="AMOUNT_OVER_BASELINE" code="ssi_contest.payout_stepitup" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="AMT_BASE_INFO" code="ssi_contest.payout_stepitup" />"></i>
                </span>
                <span class="msg measureLabel currencyOverBaseline hide">
                    <cms:contentText key="CURRENCY_OVER_BASELINE" code="ssi_contest.payout_stepitup" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="CURNCY_BASE_INFO" code="ssi_contest.payout_stepitup" />"></i>
                </span>
            </label>
            <input type="text" id="amount{{id}}" data-model-key="amount" />
            <span class="currSymb act"></span>
            <span class="currDisp act"></span>
        </div>

        <div class="levelDetailPayoutDesc clearfix" style="display:none">
            <label for="payoutDescription{{id}}">
                <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.payout_objectives" />
                <i class="icon-info pageView_help"
                    data-help-content="<cms:contentText key="PAYOUT_DESC_INFO" code="ssi_contest.payout_objectives" />"></i>
            </label>
            <input type="text"  id="payoutDescription{{id}}" data-model-key="payoutDescription" maxlength="50" />
        </div>

        <div class="levelDetailPayout clearfix">
            <label for="payout{{id}}">
                <span class="showForPOTOther">
                    <cms:contentText key="VALUE_OF_AWARD" code="ssi_contest.payout_stepitup" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="MONETARY_VALUE_INFO" code="ssi_contest.payout_stepitup" />"></i>
                </span>
                <span class="hideForPOTOther">
                    <cms:contentText key="PAYOUT" code="ssi_contest.payout_objectives" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="PAYOUT_INFO" code="ssi_contest.payout_stepitup" />"></i>
                </span>
            </label>
            <input type="text" id="payout{{id}}" data-model-key="payout" />
            <span class="currSymb pay"></span>
            <span class="currDisp pay"></span>
        </div>

        <div class="badgesOuterWrapper" >
            <h5><cms:contentText key="BADGE" code="ssi_contest.payout_objectives" /></h5>
            <div class="badgesSelectorView" data-msg-no-badge="<cms:contentText key='NO_BADGE' code='ssi_contest.payout_objectives' />"><!-- dyn --></div>
        </div>

        <div class="levelDetailItemActions">
            <button class="btn btn-primary saveLevelBtn">
               <cms:contentText code="system.button" key="SAVE" />
            </button>
            <button class="btn cancelLevelBtn">
                <cms:contentText code="system.button" key="CANCEL" />
            </button>
        </div>

    </div>



    <div class="ssiSiuLevelModelViewErrorMsgs" style="display:none">
        <b class="req"><cms:contentText key="FIELD_REQUIRED" code="ssi_contest.payout_dtgt" /></b>
        <b class="reqNum"><cms:contentText key="ENTER_WHOLE_NUMBER" code="ssi_contest.payout_dtgt" />.</b>
         <b class="reqDec_2"><cms:contentText key="TWO_DECIMAL_VALIDATION" code="ssi_contest.creator" /></b>
        <b class="reqDec_4"><cms:contentText key="FOUR_DECIMAL_VALIDATION" code="ssi_contest.creator" /></b>
        <b class="reqDec_pos_2"><cms:contentText key="GOAL_REQ_ERROR" code="ssi_contest.generalInfo" /></b>
        <b class="reqDec_pos_4"><cms:contentText key="FOUR_DECIMAL_POSITIVE_NUMBER" code="ssi_contest.creator" /></b>
        <b class="numBeyondMax"><cms:contentText key="TOO_LONG" code="ssi_contest.payout_stepitup" /></b>
        <b class="mustEditSave"><cms:contentText key="SAVE_OR_CANCEL" code="ssi_contest.payout_dtgt" /></b>
        <b class="uniqueDescReq"><cms:contentText key="REQUIRED_UNIQUE_DESC" code="ssi_contest.payout_dtgt" /></b>
        <b class="amountOutOfOrder"><cms:contentText key="AMOUNT_OUT_OF_ORDER" code="ssi_contest.payout_stepitup" /></b>
        <b class="payoutOutOfOrder"><cms:contentText key="PAYOUT_OUT_OF_ORDER" code="ssi_contest.payout_stepitup" /></b>
    </div><!-- /.ssiSiuLevelModelViewErrorMsgs -->

</div>
