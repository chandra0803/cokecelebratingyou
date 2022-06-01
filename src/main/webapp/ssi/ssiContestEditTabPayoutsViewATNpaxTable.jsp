<%@ include file="/include/taglib.jspf"%>
<!--
isPayoutTypeOther
showActivityDescription
measureCurrencyLabel
showPayoutDescription
payoutCurrencyLabel
id
lastName
firstName
activityDescription
objectiveAmount
objectivePayoutDescription
-->

<div class="spincover" style="display:none;"><div class="spin"></div></div>
<div class="paginationConts pagination pagination-right"></div>
<div class="emptyMsg alert" style="display:none"><cms:contentText key="YOU_HAVE_NOT_ADDED_ANYONE" code="claims.submission" /></div>
<div class="hasPax container-splitter with-splitter-styles participantCollectionViewWrapper" style="display:none">
    <table class="paxPayoutTable table table-condensed table-striped">
        <thead>
            <tr>
                <th rowspan="2" class="participant sortHeader sortable" data-sort="lastName">
                    <a href="#">
                        <cms:contentText key="PARTICIPANT" code="ssi_contest.generalInfo" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                <th rowspan="4" class="activityDescription textInputCell sortHeader">
                    <cms:contentText key="ACTIVITYDESCRIPTION" code="ssi_contest.payout_objectives" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="ACTIVITY_DESC_EXPLANATION" code="ssi_contest.payout_objectives" />"></i>
                </th>

                <th rowspan="2" class="activityAmount textInputCell sortHeader sortable tr" data-sort="objectiveAmount">
                    <a href="#">
                        <cms:contentText key="ACTIVITY_AMOUNT" code="ssi_contest.creator" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>



                {{#extraJson.isPayoutTypeOther}}
                    <th rowspan="2" class="objectivePayoutDescription textInputCell sortHeader sortable" data-sort="objectivePayoutDescription">
                        <a href="#">
                            <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.payout_objectives" />
                            <i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="ATN_PAY_DESC_INFO" code="ssi_contest.payout_objectives" />"></i>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th rowspan="2" class="objectivePayout textInputCell sortHeader sortable" data-sort="objectivePayout">
                        <a href="#">
                            <cms:contentText key="VALUE" code="ssi_contest.approvals.detail" />
                            <i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="EXPLANATION_OF_WHAT" code="ssi_contest.payout_stackrank" />"></i>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                {{else}}
                    <th rowspan="2" class="objectivePayout textInputCell sortHeader sortable" data-sort="objectivePayout">
                        <a href="#">
                            <cms:contentText key="PAYOUT" code="ssi_contest.approvals.detail" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                {{/extraJson.isPayoutTypeOther}}


            </tr>
        </thead>
        <tfoot>
            <tr>
                <!-- {{#extraJson.showActivityDescription}}colspan="2"{{/extraJson.showActivityDescription}} -->
                <td colspan="2">
                    <button class="calculateTotalsBtn btn btn-primary" data-msg-loading="<cms:contentText key="CALCULATING" code="ssi_contest.payout_stepitup" />"><cms:contentText key="CALCULATE_TOTALS" code="ssi_contest.payout_objectives" /></button>
                </td>
                <td class="totalDisp">
                    <span class="currSymb act">
                    </span><span class="calcTotalsAutoBind" data-model-key="objectiveAmountTotal">--</span>
                    <span class="currDisp act"></span>
                </td>
                {{#extraJson.showPayoutDescription}}
                    <td><!-- no total --></td>
                {{/extraJson.showPayoutDescription}}
                <td class="totalDisp">
                    <span class="currSymb pay">
                    </span><span class="calcTotalsAutoBind" data-model-key="objectivePayoutTotal">--</span>
                    <span class="currDisp pay"></span>
                </td>
            </tr>
        </tfoot>
        <tbody>
        </tbody>
    </table>
</div>
<div class="paginationConts pagination pagination-right"></div>

<!-- VALIDATION MSGS - informational tooltip for validation -->
<div class="participantPaginatedViewErrorTipWrapper" style="display:none">
    <div class="errorTip">

        <div class="errorMsg msgNumeric" style="display:none" >
            <cms:contentText key="MUST_BE_NUMBER_ERROR" code="ssi_contest.payout_objectives" />
        </div>
        <div class="errorMsg msg_natural" style="display:none" >
            <cms:contentText key="INVALID_NUMBER_ERROR" code="ssi_contest.payout_objectives" />
        </div>
        <div class="errorMsg msgDecimal2 msg_decimal_2" style="display:none" >
            <cms:contentText key="TWO_DECIMAL_VALIDATION" code="ssi_contest.creator" />
        </div>
        <div class="errorMsg msgDecimal4 msg_decimal_4" style="display:none" >
            <cms:contentText key="FOUR_DECIMAL_VALIDATION" code="ssi_contest.creator" />
        </div>
        <div class="errorMsg msgNumberTooLarge" style="display:none" >
            <cms:contentText key="TOO_LONG" code="ssi_contest.payout_stepitup" />
        </div>
        <div class="errorMsg msgRequired" style="display:none" >
            <cms:contentText key="FIELDS_REQUIRED_ERROR" code="ssi_contest.payout_objectives" />
        </div>

    </div><!-- /.errorTip -->
</div><!-- /.participantPaginatedViewErrorTipWrapper -->

<div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
    <a href="#"><cms:contentText key="SAME_FOR_ALL_PARTICIPANTS" code="ssi_contest.payout_objectives" /></a>
</div><!-- /#sameForAllTipTpl -->

<!--subTpl.paxRow=

    <tr class="participant-item" data-participant-id="{{id}}">


        <td class="participant">
            <a class="participant-popover" href="#" data-participant-ids="[{{userId}}]">
                {{lastName}}, {{firstName}}
            </a>
        </td>

        <td class="textInputCell">
            <input type="text"
                class="paxDatTextInput paxDatActivityDesc"
                data-model-key="activityDescription"
                data-model-id="{{id}}"
                value="{{activityDescription}}"
                maxlength="50"  />
        </td>

        <td class="textInputCell">
            <input type="text"
                class="paxDatTextInput paxDatObjAmount tr"
                data-model-key="objectiveAmount"
                data-model-id="{{id}}"
                data-validation="number"
                value="{{objectiveAmount}}" />
        </td>

        {{#extraJson.isPayoutTypeOther}}
            {{! todo}}
            <td class="textInputCell">
                <input type="text"
                    class="paxDatTextInput paxDatObjPayoutDesc"
                    data-model-key="objectivePayoutDescription"
                    data-model-id="{{id}}"
                    value="{{objectivePayoutDescription}}"
                    maxlength="50"  />
            </td>
        {{/extraJson.isPayoutTypeOther}}

        <td class="textInputCell">
            <input type="text"
                class="paxDatTextInput tr atnPayoutInput"
                data-model-key="objectivePayout"
                data-model-id="{{id}}"
                value="{{objectivePayout}}" />
        </td>

    </tr>

subTpl-->
