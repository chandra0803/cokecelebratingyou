<%@ include file="/include/taglib.jspf"%>
<div class="spincover" style="display:none;"><div class="spin"></div></div>
<div class="paginationConts pagination pagination-right"></div>
<div class="emptyMsg alert" style="display:none"><cms:contentText key="NOT_ADDED_ANYONE" code="ssi_contest.pax.manager" /></div>
<div class="hasPax container-splitter with-splitter-styles participantCollectionViewWrapper" style="display:none">
    <table class="paxPayoutTable table table-condensed table-striped">
        <thead>
            <tr>
                <th class="participant sortHeader sortable" data-sort="lastName">
                    <a href="#">
                        <cms:contentText key="PARTICIPANT" code="ssi_contest.pax.manager" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>

                <th class="baseline textInputCell sortHeader sortable" data-sort="baselineAmount">
                    <a href="#">
                        <cms:contentText key="BASE_LINE" code="ssi_contest.participant" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>

                {{#extraJson.levels}}
                <th class="level levelTD">
                    <div><cms:contentText key="LEVEL" code="ssi_contest.participant" /> {{sequenceNumber}}</div>
                    <div>+ <span class="currSymb act"></span>{{amount}}<span class="currSymbAfter act"></span></div>
                </th>
                {{/extraJson.levels}}

            </tr>
        </thead>
        <tfoot>
            <tr>
                <td>
                    <button class="calculateTotalsBtn btn btn-primary" data-msg-loading="<cms:contentText key="CALCULATING" code="ssi_contest.payout_stepitup" />"><cms:contentText key="CALCULATE_TOTALS" code="ssi_contest.payout_objectives" /></button>
                </td>

                <td class="totalDisp">
                    <span class="currSymb act"></span><!--
                    --><span class="baselineTotal">--</span>
                    <span class="currDisp act"></span>
                </td>

                {{#extraJson.levels}}
                <td class="totalDisp">
                    <span class="currSymb act"></span><!--
                    --><span class="levelTotal{{id}}">--</span>
                    <span class="currDisp act"></span>
                </td>
                {{/extraJson.levels}}
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
            <cms:contentText key="WHOLE_NUMBER" code="ssi_contest.creator" />
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
    <a href="#"><cms:contentText key="SAME_FOR_ALL_PARTICIPANTS" code="ssi_contest.payout_objectives" /> </a>
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
                class="paxDatTextInput paxDatBaseline"
                data-model-key="baselineAmount"
                data-model-id="{{id}}"
                data-validation="number"
                value="{{baselineAmount}}" />
        </td>

        {{#extraJson.levels}}
        <td class="levelTD">
            <span class="levelCalc level{{id}}"></span>
        </td>
        {{/extraJson.levels}}
    </tr>

subTpl-->
