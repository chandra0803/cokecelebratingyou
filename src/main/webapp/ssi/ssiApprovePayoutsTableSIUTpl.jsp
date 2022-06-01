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
{{#if hasQualifiedPayouts}}
<div class="row-fluid">
    <div class="span12">
        <div class="paginationControls pagination pagination-right"></div>
        <div class="emptyMsg alert" style="display:none"><cms:contentText key="ADD_NO_ONE" code="ssi_contest.creator" /></div>

            <table class="table table-striped accentLast3Cols">
                <thead>
                    <tr class="headerContent">
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
                        <th class="sortHeader unsorted number" data-sort="levelAchieved">
                            <a href="#">
                                <cms:contentText key="LEVEL_COMPLETED" code="ssi_contest.payout_stepitup" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>

                        {{#eq payoutType "points"}}
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="levelPayout">
                                <a href="#">
                                    <cms:contentText key="LEVEL_PAYOUT" code="ssi_contest.payout_stepitup" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            {{#if includeBonus}}
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="bonusPayout">
                                <a href="#">
                                    <cms:contentText key="BONUS_PAYOUT" code="ssi_contest.payout_objectives" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            {{/if}}
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="totalPayout">
                                <a href="#">
                                    <cms:contentText key="TOTAL_PAYOUT" code="ssi_contest.payout_stepitup" />
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
                                    <cms:contentText key="TOTAL_PAYOUT_VALUE" code="ssi_contest.creator" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                        {{/eq}}
                    </tr>
                </thead>
                <tfoot>
                    <tr class="footerContent">
                        <td class="totalDisp">
                            <cms:contentText key="TOTALS" code="ssi_contest.payout_objectives" />
                        </td>
                        <td class="totalDisp number">
                            {{totalActivity}}
                        </td>
                        <td class="totalDisp">
                        </td>

                        {{#eq payoutType "points"}}
                            <td class="totalDisp columnEmphasis number">
                                {{totalLevelPayout}}
                            </td>
                            {{#if includeBonus}}
                            <td class="totalDisp columnEmphasis number">
                                {{totalBonusPayout}}
                            </td>
                            {{/if}}
                            <td class="totalDisp columnEmphasis number">
                                {{totalPayout}}
                            </td>
                        {{/eq}}

                        {{#eq payoutType "other"}}
                            <td class="totalDisp columnEmphasis">
                                &nbsp;
                            </td>
                            <td class="totalDisp columnEmphasis number">
                                {{totalPayoutValue}}
                            </td>
                        {{/eq}}
                    </tr>
                </tfoot>
                <tbody>
                </tbody>
            </table>

        <!-- <div class="paginationConts pagination pagination-right"></div> -->
        <div class="paginationControls pagination pagination-right"></div>
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

<!--subTpl.recordRow=

    <tr class="record-item" data-participant-id="{{id}}">

        <td>
            <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                {{lastName}}, {{firstName}}
            </a>
        </td>
        <td class="number">
            {{activityAmount}}
        </td>
        <td class="number">
            {{levelAchieved}}
        </td>

        {{#eq extraJSON.payoutType "points"}}
            <td class="number columnEmphasis">
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{levelPayout}}{{/if}}
            </td>
            {{#if includeBonus}}
            <td class="number columnEmphasis">
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{bonusPayout}}{{/if}}
            </td>
            {{/if}}
            <td class="number columnEmphasis">
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{totalPayout}}{{/if}}
            </td>
        {{/eq}}

        {{#eq extraJSON.payoutType "other"}}
            <td class="columnEmphasis">
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payoutDescription}}{{/if}}
            </td>
            <td class="number columnEmphasis">
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payoutValue}}{{/if}}
            </td>
        {{/eq}}
    </tr>

subTpl-->
