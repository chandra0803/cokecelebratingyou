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

<div class="row-fluid dtgtFormBtns">
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

<!--subTpl.tableSection=
<div class="row-fluid">
    <div class="span12">
        <hr class="section">

        <h4>{{activityDescription}}</h4> <br>
        <p>
            {{#eq payoutType "points"}}
                <cms:contentText key="FOR_EVERY" code="ssi_contest.participant" /> {{forEvery}},<br>
                <cms:contentText key="EARN" code="ssi_contest.participant" /> {{willEarn}} <cms:contentText key="POINTS" code="ssi_contest.participant" />
            {{/eq}}
            {{#eq payoutType "other"}}
                <cms:contentText key="FOR_EVERY" code="ssi_contest.participant" /> {{forEvery}},<br>
                <cms:contentText key="EARN" code="ssi_contest.participant" /> {{payoutDescription}}"
            {{/eq}}
        </p>
        <p>
            <cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.participant" />: {{minQualifier}}
        </p>
    </div>
</div>

<div class="row-fluid">
    <div class="span12">
        <div class="paginationControls pagination pagination-right"></div>
        <div class="emptyMsg alert" style="display:none"><cms:contentText key="NOT_ADDED_ANYONE" code="ssi_contest.pax.manager" />.</div>

            <table class="table table-striped accentLastCol">
                <thead>
                    <tr>
                        <th class="sortHeader unsorted" data-sort="lastName">
                            <a href="#">
                                <cms:contentText key="PARTICIPANTS" code="ssi_contest.creator" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>
                        <th class="sortHeader unsorted number" data-sort="progress">
                            <a href="#">
                                <cms:contentText key="TOTAL_ACTIVIT_AS_OF" code="ssi_contest.creator" />{{updatedOnDate}}
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>
                        <th class="number">
                            <cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.payout_dtgt" />
                        </th>
                        <th class="sortHeader unsorted number" data-sort="qualifiedActivity">
                            <a href="#">
                                <cms:contentText key="TOTAL_ACTIVIT_AS_OF" code="ssi_contest.creator" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>
                        <th class="sortHeader unsorted number" data-sort="payoutIncrements">
                            <a href="#">
                                <cms:contentText key="NUMBER_OF_INCREMENT_FOR_PAYOUT" code="ssi_contest.creator" />
                                <i class="icon-arrow-1-up"></i>
                                <i class="icon-arrow-1-down"></i>
                            </a>
                        </th>

                        {{#eq payoutType "other"}}
                            <th class="sortHeader unsorted" data-sort="payoutDescription">
                                <a href="#">
                                    <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.creator" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                        {{/eq}}

                        {{#eq payoutType "points"}}
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="totalPayout">
                                <a href="#">
                                    <cms:contentText key="PAYOUT" code="ssi_contest.creator" />
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                        {{/eq}}
                        {{#eq payoutType "other"}}
                            <th class="sortHeader unsorted columnEmphasis number" data-sort="payoutValue">
                                <a href="#">
                                    <cms:contentText key="PAYOUT_VALUE" code="ssi_contest.participant" />
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
                            <cms:contentText key="TOTALS" code="ssi_contest.participant" />
                        </td>
                        <td class="totalDisp number">
                            {{totalActivity}}
                        </td>
                        <td class="totalDisp">
                        </td>
                        <td class="totalDisp number">
                            {{qualifiedActivity}}
                        </td>
                        <td class="totalDisp number">
                            {{totalIncrementPayout}}
                        </td>

                        {{#eq payoutType "other"}}
                            <td>
                                &nbsp;
                            </td>
                        {{/eq}}

                        <td class="totalDisp columnEmphasis number">
                            {{#eq payoutType "points"}}
                                {{totalPayout}} <cms:contentText key="POINTS" code="ssi_contest.participant" />
                            {{/eq}}
                            {{#eq payoutType "other"}}
                                {{totalPayoutValue}}
                            {{/eq}}
                        </td>
                    </tr>
                </tfoot>
                <tbody>
                </tbody>
            </table>

        <div class="paginationControls pagination pagination-right"></div>
    </div>
</div>
subTpl-->

<!--subTpl.recordRow=
    <tr class="record-item" data-participant-id="{{id}}">

        <td>
            <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                {{lastName}}, {{firstName}}
            </a>
        </td>
        <td class="number">
            {{progress}}
        </td>
        <td class="number">
            {{extraJSON.minQualifier}}
        </td>
        <td class="number">
            {{qualifiedActivity}}
        </td>
        <td class="number">
            {{payoutIncrements}}
        </td>
        {{#eq extraJSON.payoutType "other"}}
            <td>
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payoutDescription}}{{/if}}
            </td>
        {{/eq}}
        <td class="number columnEmphasis">
            {{#eq extraJSON.payoutType "points"}}
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{totalPayout}} <cms:contentText key="POINTS" code="ssi_contest.participant" />{{/if}}
            {{/eq}}
            {{#eq extraJSON.payoutType "other"}}
                {{#if optOutAwards}}<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>{{else}}{{payoutValue}}{{/if}}
            {{/eq}}
        </td>

    </tr>
subTpl-->
