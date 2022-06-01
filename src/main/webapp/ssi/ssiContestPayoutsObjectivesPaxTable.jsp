<%@ include file="/include/taglib.jspf"%>

<div class="spincover" style="display:none;"><div class="spin"></div></div>
<div class="paginationConts pagination pagination-right"></div>
<div class="emptyMsg alert" style="display:none"><cms:contentText key="NOT_ADDED_ANYONE" code="ssi_contest.pax.manager"/></div>
<div class="hasPax container-splitter with-splitter-styles participantCollectionViewWrapper" style="display:none">
    <table class="paxPayoutTable table table-condensed table-striped">
        <thead>
            <tr>
                <th rowspan="2" class="participant sortHeader sortable" data-sort="lastName">
                    <a href="#">
                        <cms:contentText key="PARTICIPANT" code="ssi_contest.payout_objectives" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#extraJson.showActivityDescription}}
                    <th rowspan="2" class="activityDescription textInputCell sortHeader sortable" data-sort="activityDescription">
                        <a href="#">
                            <cms:contentText key="OBJECTIVES_DESCRIPTION" code="ssi_contest.preview" />
                            <i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="OBJECTIVE_DESC_INFO" code="ssi_contest.payout_objectives" />"></i>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                {{/extraJson.showActivityDescription}}
                <th {{#if extraJson.showPayoutDescription}}colspan="3"{{else}}colspan="2"{{/if}} class="objective textInputCell"><cms:contentText key="OBJECTIVE" code="ssi_contest.payout_objectives" /></th>
                {{#extraJson.showBonus}}
                    <th colspan="3" class="bonus textInputCell"><cms:contentText key="BONUS" code="ssi_contest.payout_objectives" /></th>
                {{/extraJson.showBonus}}
            </tr>
            <tr>
                <th class="textInputCell sortHeader sortable" data-sort="objectiveAmount">
                    <a href="#">
                        <cms:contentText key="AMOUNT" code="ssi_contest.payout_objectives" />
                        <i class="icon-info pageView_help"
                            data-help-content=" <cms:contentText key="OBJ_AMT_INFO" code="ssi_contest.payout_objectives" />"></i>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#extraJson.showPayoutDescription}}
                    <th class="textInputCell sortHeader sortable" data-sort="objectivePayoutDescription">
                        <a href="#">
                            <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.payout_objectives" />
                            <i class="icon-info pageView_help"
                                data-help-content=" <cms:contentText key="PAYOUT_DESC_INFO" code="ssi_contest.payout_objectives" />"></i>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                {{/extraJson.showPayoutDescription}}
                <th class="textInputCell sortHeader sortable" data-sort="objectivePayout">
                    <a href="#">
                        {{#if extraJson.isPayoutTypeOther}}
                        <cms:contentText key="VALUE" code="ssi_contest.payout_objectives" />
                        <i class="icon-info pageView_help"
                            data-help-content="<cms:contentText key="EXPLANATION_OF_WHAT" code="ssi_contest.payout_stackrank" />"></i>
                    {{else}}<cms:contentText key="PAYOUT" code="ssi_contest.payout_objectives" />{{/if}}
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#extraJson.showBonus}}
                    <th class="textInputCell sortHeader bonus sortable" data-sort="bonusForEvery">
                        <a href="#">
                            <cms:contentText key="FOR_EVERY" code="ssi_contest.payout_objectives" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th class="textInputCell sortHeader sortable" data-sort="bonusPayout">
                        <a href="#">
                            <cms:contentText key="PAYOUT" code="ssi_contest.payout_objectives" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th class="textInputCell sortHeader sortable" data-sort="bonusPayoutCap">
                        <a href="#">
                            <cms:contentText key="PAYOUT_CAP" code="ssi_contest.payout_objectives" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                {{/extraJson.showBonus}}
            </tr>
        </thead>
        <tfoot>
            <tr>
                <td {{#extraJson.showActivityDescription}}colspan="2"{{/extraJson.showActivityDescription}}>
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
                {{#extraJson.showBonus}}
                    <td class="bonus"><!-- no total --></td>
                    <td class="totalDisp">
                        <span class="currSymb pay">
                        </span><span class="calcTotalsAutoBind" data-model-key="bonusPayoutTotal">--</span>
                        <span class="currDisp pay"></span>
                    </td>
                    <td class="totalDisp">
                        <span class="currSymb pay">
                        </span><span class="calcTotalsAutoBind" data-model-key="bonusPayoutCapTotal">--</span>
                        <span class="currDisp pay"></span>
                    </td>
                {{/extraJson.showBonus}}
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

        <div class="errorMsg msgNumeric msg_natural" style="display:none" >
            <cms:contentText key="MUST_BE_NUMBER_ERROR" code="ssi_contest.payout_objectives" />
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
        <div class="errorMsg msgBonusPayoutCapError" style="display:none" >
            <cms:contentText key="BONUS_PAYOUT_CAP_ERR" code="ssi_contest.payout_objectives" />
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

        {{#extraJson.showActivityDescription}}
            <td class="textInputCell">
                <input type="text"
                    class="paxDatTextInput paxDatActivityDesc"
                    data-model-key="activityDescription"
                    data-model-id="{{id}}"
                    value="{{activityDescription}}"
                    maxlength="50"  />
            </td>
        {{/extraJson.showActivityDescription}}

        <td class="textInputCell">
            <input type="text"
                class="paxDatTextInput paxDatObjAmount"
                data-model-key="objectiveAmount"
                data-model-id="{{id}}"
                data-validation="number"
                value="{{objectiveAmount}}" />
        </td>
        {{#extraJson.showPayoutDescription}}
            <td class="textInputCell">
                <input type="text"
                    class="paxDatTextInput paxDatObjPayoutDesc"
                    data-model-key="objectivePayoutDescription"
                    data-model-id="{{id}}"
                    value="{{objectivePayoutDescription}}"
                    maxlength="50"  />
            </td>
        {{/extraJson.showPayoutDescription}}
        <td class="textInputCell">
            <input type="text"
                class="paxDatTextInput paxDatObjPayout"
                data-model-key="objectivePayout"
                data-model-id="{{id}}"
                data-validation="number"
                value="{{objectivePayout}}" />
        </td>

        {{#extraJson.showBonus}}
            <td class="textInputCell bonus">
                <input type="text"
                    class="paxDatTextInput paxDatBonusForEvery"
                    data-model-key="bonusForEvery"
                    data-model-id="{{id}}"
                    data-validation="number"
                    value="{{bonusForEvery}}" />
            </td>
            <td class="textInputCell">
                <input type="text"
                    class="paxDatTextInput paxDatBonusPayout"
                    data-model-key="bonusPayout"
                    data-model-id="{{id}}"
                    data-validation="number"
                    value="{{bonusPayout}}" />
            </td>
            <td class="textInputCell">
                <input type="text"
                    class="paxDatTextInput paxDatBonusPayoutCap"
                    data-model-key="bonusPayoutCap"
                    data-model-id="{{id}}"
                    data-validation="number"
                    value="{{bonusPayoutCap}}" />
            </td>
        {{/extraJson.showBonus}}
    </tr>

subTpl-->
