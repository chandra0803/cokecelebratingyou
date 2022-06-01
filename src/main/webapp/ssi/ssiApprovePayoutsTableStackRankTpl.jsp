<%@ include file="/include/taglib.jspf"%>
<div class="row-fluid">
    <div class="span12 clearfix">
        <h3>
            {{name}} <cms:contentText key="RESULTS" code="ssi_contest.creator" />
        </h3>
        {{#unless includeSubmitClaim}}
        <beacon:authorize ifNotGranted="LOGIN_AS">
        <a href="contestResults.do?method=populateContestResults&id={{id}}" class="btn btn-small btn-primary updateResultsBtn"> <cms:contentText key="UPDATE_RESULTS" code="ssi_contest.creator" /> <i class="icon-pencil2"></i></a>
        </beacon:authorize>
        {{/unless}}
    </div>
</div>
<div class="row-fluid">
    <div class="span12">
        {{#if hasQualifiedPayouts}}
            <p><cms:contentText key="HAS_QUALIFIED_PAYOUT" code="ssi_contest.creator" /></p>

        {{else}}
            {{#if includeSubmitClaim}}
                <p><cms:contentText key="NO_PAYOUT_SUBMIT_CLAIM" code="ssi_contest.creator" /></p>
            {{else}}
                <p><cms:contentText key="NO_QUALIFIED_PAYOUT" code="ssi_contest.creator" /></p>
            {{/if}}
        {{/if}}
    </div>
</div>
{{#if hasTie}}
<div class="row-fluid">
    <div class="span12 issuePayoutsTieText">
        <p>
        	<i class="icon-warning-circle"></i><cms:contentText key="STACK_RANK_TIE_MSG" code="ssi_contest.creator" />
        </p>
    </div>
</div>
{{/if}}

{{#if hasQualifiedPayouts}}
<div class="row-fluid">
    <div class="span12">
        <div class="paginationConts paginationControls pagination pagination-right"></div>
        <div class="emptyMsg alert" style="display:none"><cms:contentText key="ADD_NO_ONE" code="ssi_contest.creator" /></div>
            <table class="table table-striped stackRank accentLast3Cols">
                <thead>
                    <tr class="headerContent">
                        <th class="sortHeader unsorted rank" data-sort="rank">
                            <a href="#">
                                <cms:contentText key="RANK" code="ssi_contest.participant" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>
                        <th class="sortHeader unsorted" data-sort="lastName">
                            <a href="#">
                                <cms:contentText key="PAX" code="ssi_contest.creator" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>
                        <th class="sortHeader unsorted number" data-sort="activityAmount">
                            <a href="#">
                                <cms:contentText key="ACTIVITY" code="ssi_contest.creator" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>

                        {{#eq payoutType "points"}}
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="totalPayout">
                                <a href="#">
                                    <cms:contentText key="PAYOUT" code="ssi_contest.participant" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                        {{/eq}}

                        {{#eq payoutType "other"}}
                            <th class="sortHeader unsorted columnEmphasis" data-sort="payoutDescription">
                                <a href="#">
                                    <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.payout_objectives" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="payoutValue">
                                <a href="#">
                                    <cms:contentText key="PAYOUT_VALUE" code="ssi_contest.participant" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                        {{/eq}}

                        <th class="badgeHeader columnEmphasis">
                            <cms:contentText key="BADGE" code="ssi_contest.payout_objectives" />
                        </th>

                    </tr>
                </thead>
                <tfoot>
                    <tr class="footerContent">
                        <td class="totalDisp" colspan="2">
                            <cms:contentText key="TOTALS" code="ssi_contest.payout_objectives" />
                        </td>
                        <td class="totalDisp number">
                            {{totalActivity}}
                        </td>
                        {{#eq payoutType "points"}}
                            <td class="totalDisp columnEmphasis number">
                                <span data-tplval="totalPayout">{{totalPayout}}</span>
                            </td>
                        {{/eq}}

                        {{#eq payoutType "other"}}
                            <td class="columnEmphasis"></td>
                            <td class="totalDisp columnEmphasis number">
                                {{currencySymbol}}<span data-tplval="totalPayoutValue">{{totalPayoutValue}}</span>
                            </td>
                        {{/eq}}

                        <td class="columnEmphasis"></td>
                    </tr>
                </tfoot>
                <tbody>
                </tbody>
            </table>
        <div class="paginationConts paginationControls pagination pagination-right"></div>
    </div>
</div>
{{/if}}

{{#if hasTie}}
<div class="row-fluid">
    <div class="span12">
        {{#eq payoutType "points"}}
            <cms:contentText key="BUDGEDTED_PAYOUT" code="ssi_contest.payout_objectives" />: <b>{{payoutCap}} <cms:contentText key="POINTS" code="ssi_contest.participant" /></b>
        {{/eq}}

        {{#eq payoutType "other"}}
            <cms:contentText key="BUDGEDTED_PAYOUT" code="ssi_contest.payout_objectives" />: <b>{{payoutCap}}</b>
        {{/eq}}
    </div>
</div>
<div class="row-fluid">
    <div class="span12">
        <a href="#" class="ssiViewContestDetails showDetails"><cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" /></a>
        <a href="#" class="ssiViewContestDetails hideDetails" style="display:none"><cms:contentText key="HIDE_DETAILS" code="ssi_contest.creator" /></a>
    </div>
</div>

<div class="row-fluid payoutsWrap" style="display: none">
    <div class="span12 payoutsRow">
        <div class="row-fluid">
            <div class="span12">
                <h4><cms:contentText key="BUDGEDTED_PAYOUT" code="ssi_contest.payout_objectives" /></h4>
            </div>
        </div>

        <div class="row-fluid">
        {{#each payouts}}
            <div class="span3 payout">
                <span class="payoutRank">{{rank}}</span>
                <div class="payoutInfo">
                    {{#if badge.img}}
                        <img src="{{badge.img}}" alt="{{badge.name}}" class="payoutBadge">
                    {{/if}}
                    {{#if payout}}
                        <span class="payoutAmount{{#unless badge.img}} no-badge{{/unless}}">
                        {{payout}}
                            {{#eq ../payoutType "points"}}
                                <cms:contentText key="POINTS" code="ssi_contest.participant" />
                            {{/eq}}
                        </span>
                    {{else}}
                        <span class="badgeName">{{badge.name}}</span>
                    {{/if}}
                </div>
            </div>
        {{/each}}
        </div>
    </div>
</div>
{{/if}}

<div class="row-fluid">
    <div class="span12">
        <div class="form-actions pullBottomUp">

            <!-- This will not work the way FE has already set up the contest URL's
                Please see updated href below
                creatorContestList.do?method=display&id={{id}}
            -->
            <a href="creatorContestList.do?method=display&id={{id}}#contest/{{id}}" class="btn">&laquo; <cms:contentText key="BACK" code="system.button" /></a>
            {{#if hasQualifiedPayouts}}
            <button type="button" class="btn btn-primary promptBtn"><cms:contentText key="ISSUE_PAYOUTS" code="ssi_contest.creator" /></button>
            {{else}}
            <button type="button" class="btn btn-primary promptBtnCloseContest"><cms:contentText key="CLOSE_CONTEST" code="ssi_contest.creator" /></button>
            {{/if}}
        </div>
    </div>
</div>

{{! NOTE: read-only contests use recordRow templates }}

<!--subTpl.recordRow=

    <tr class="record-item" data-participant-id="{{id}}">

        <td class="rank">
            {{rank}}
        </td>
        <td>
            <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                {{lastName}}, {{firstName}}
            </a>
        </td>
        <td class="number">
            {{progress}}
        </td>

        {{#eq extraJSON.payoutType "points"}}
            <td class="number columnEmphasis">
                {{#if payout}}
                    {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payout}}{{/if}}
                {{else}}
                    --
                {{/if}}
            </td>
        {{/eq}}

        {{#eq extraJSON.payoutType "other"}}
            <td class="columnEmphasis" >
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payoutDescription}}{{/if}}
            </td>
            <td class="number columnEmphasis">
                {{#if payoutValue}}
                    {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payoutValue}}{{/if}}
                {{else}}
                    --
                {{/if}}
            </td>
        {{/eq}}

        <td class="badgeCell columnEmphasis">
            {{#if badge.img}}
                <img src="${pageContext.request.contextPath}{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}
            {{else}}
                --
            {{/if}}
        </td>
    </tr>

subTpl-->

{{! NOTE: edittable contests use paxRow templates }}

<!--subTpl.paxRow=

    <tr class="record-item" data-participant-id="{{id}}">

        <td class="rank">
            {{rank}}
        </td>
        <td>
            <a class="profile-popover" data-participant-ids="[{{id}}]">
                {{lastName}}, {{firstName}}
            </a>
        </td>
        <td class="number">
            {{progress}}
        </td>

        {{#eq extraJson.payoutType "points"}}
            <td class="textInputCell columnEmphasis number">
                <input type="text"
                    class="paxDatTextInput paxDatActivityVal"
                    value="{{#if optOutAwards}}0{{else}}{{payout}}{{/if}}"
                    data-model-key="payout"
                    data-validation="number"
                    data-model-id="{{id}}" {{#if optOutAwards}}disabled="disabled"{{/if}}>
            </td>
        {{/eq}}

        {{#eq extraJson.payoutType "other"}}
            <td class="textInputCell columnEmphasis">
                <input type="text"
                    class="paxDatTextInput paxDatActivityDesc"
                    value="{{#if optOutAwards}}0{{else}}{{payoutDescription}}{{/if}}"
                    data-model-key="payoutDescription"
                    data-model-id="{{id}}" {{#if optOutAwards}}disabled="disabled"{{/if}}>
            </td>
            <td class="textInputCell columnEmphasis number">
                <input type="text"
                    class="paxDatTextInput paxDatActivityVal"
                    value="{{#if optOutAwards}}0{{else}}{{payoutValue}}{{/if}}"
                    data-model-key="payoutValue"
                    data-validation="number"
                    data-model-id="{{id}}" {{#if optOutAwards}}disabled="disabled"{{/if}}>
            </td>
        {{/eq}}

        <td class="badgeCell columnEmphasis">
            {{#if badge.img}}
                <img src="${pageContext.request.contextPath}{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}
            {{else}}
                --
            {{/if}}
        </td>
    </tr>

subTpl-->

<!-- VALIDATION MSGS - informational tooltip for validation -->
<div class="participantPaginatedViewErrorTipWrapper" style="display:none">
    <div class="errorTip">

        <div class="errorMsg msgNumeric msg_whole" style="display:none" >
            <cms:contentText key="MUST_BE_NUMBER_ERROR" code="ssi_contest.payout_objectives" />
        </div>
        <div class="errorMsg msgRequired" style="display:none" >
            <cms:contentText key="FIELDS_REQUIRED_ERROR" code="ssi_contest.payout_objectives" />
        </div>

    </div><!-- /.errorTip -->
</div><!-- /.participantPaginatedViewErrorTipWrapper -->

<div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
    <a href="#"><cms:contentText key="SAME_FOR_ALL_PARTICIPANTS" code="ssi_contest.payout_objectives" /></a>
</div><!-- /#sameForAllTipTpl -->
