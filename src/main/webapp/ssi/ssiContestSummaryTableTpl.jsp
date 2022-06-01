<div class="paginationControls pagination pagination-right"></div>
<div class="emptyMsg alert" style="display:none"><cms:contentText key="ADD_NO_ONE" code="ssi_contest.creator" /></div>

    <table class="table table-striped">
        <thead>
            <tr>
                <th class="sortHeader unsorted date" data-sort="dateCreated">
                    <a href="#">
                        <cms:contentText key="DATE" code="ssi_contest.participant" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                <th class="sortHeader unsorted number" data-sort="participantsCount">
                    <a href="#">
                        <cms:contentText key="NO_OF_PARTICIPANTS" code="ssi_contest.payout_dtgt" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                <th class="sortHeader unsorted number" data-sort="amount">
                    <a href="#">
                        <cms:contentText key="ACTIVITY_AMOUNT" code="ssi_contest.creator" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#eq payoutType "points"}}
                <th class="sortHeader unsorted number" data-sort="payoutAmount">
                    <a href="#">
                        <cms:contentText key="PAYOUT" code="ssi_contest.approvals.summary" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{/eq}}
                {{#eq payoutType "other"}}
                <th class="sortHeader unsorted" data-sort="payoutDescription">
                    <a href="#">
                        <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.summary" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                <th class="sortHeader unsorted number" data-sort="payoutAmount">
                    <a href="#">
                        <cms:contentText key="VALUE" code="ssi_contest.approvals.summary" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{/eq}}
                <th class="sortHeader unsorted" data-sort="status">
                    <a href="#">
                        <cms:contentText key="APPROVAL_STATUS" code="ssi_contest.approvals.summary" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#isApproveMode}}
                    <th><!-- approve mode column for buttons --></th>
                {{/isApproveMode}}
            </tr>
        </thead>
        <tfoot>
            <tr>
                <td class="totalDisp number">
                    <cms:contentText key="TOTAL" code="ssi_contest.approvals.summary" />
                </td>
                <td class="totalDisp number">
                    {{totalParticipantsCount}}
                </td>
                <td class="totalDisp number">
                    {{totalActivity}}
                </td>
                {{#eq payoutType "other"}}
                <td></td>
                {{/eq}}
                <td class="totalDisp number">
                    {{totalPayoutAmount}}
                </td>
                <td></td>
                {{#isApproveMode}}<td></td>{{/isApproveMode}}
            </tr>
        </tfoot>
        <tbody>
        </tbody>
    </table>

<!-- <div class="paginationConts pagination pagination-right"></div> -->
<div class="paginationControls pagination pagination-right"></div>

<!--subTpl.recordRow=

    <tr class="summary-item" data-participant-id="{{id}}">

        {{#if extraJSON.isApproveMode}}
            <td class="date">
                {{! JAVA NOTE: not sure how this url should be formatted }}
                <a href="displayPayoutHistoryAwardThemNow.do?method=displayPayoutHistory&isApproveMode=true&ssiContestClientState={{id}}">{{dateCreated}}</a>
            </td>
        {{else}}
            <td class="date">
                {{! JAVA NOTE: not sure how this url should be formatted }}
                <a href="displayPayoutHistoryAwardThemNow.do?method=displayPayoutHistory&isApproveMode=false&ssiContestClientState={{id}}">{{dateCreated}}</a>
            </td>
        {{/if}}

        <td class="number">
            {{participantsCount}}
        </td>

        <td class="number">
            {{amount}}
        </td>

        {{#eq extraJSON.payoutType "points"}}
            <td class="number">
                {{payoutAmount}}
            </td>
        {{/eq}}
        {{#eq extraJSON.payoutType "other"}}
            <td>
                {{payoutDescription}}
            </td>
            <td class="number">
                {{payoutAmount}}
            </td>
        {{/eq}}


        <td class="status">
            {{statusDescription}}
            {{#if deniedReason}}
            - {{deniedReason}}
            {{/if}}
        </td>

        {{#extraJSON.isApproveMode}}
            <td>
            {{#canApprove}}
                {{#eq status "waiting_for_approval"}}
                <span class="approveModeButtons" data-contest-id="{{id}}">
                    <span class="showOnApproved hide"><cms:contentText key="APPROVED_LABEL" code="ssi_contest.creator"/></span>
                    <span class="showOnDenied hide"><cms:contentText key="DENIED_LABEL" code="ssi_contest.creator"/></span>
                    <button data-contest-id="{{id}}" class="btn btn-primary btn-small ssiApproveContestBtn hideOnSuccess">
                        <cms:contentText key='APPROVE_BTN' code='ssi_contest.approvals.summary' />
                    </button>
                    <button data-contest-id="{{id}}" class="btn btn-small ssiDenyContestBtn hideOnSuccess">
                        <cms:contentText key='DENY_BTN' code='ssi_contest.approvals.summary' />
                    </button>
                </span>
                {{/eq}}
            {{/canApprove}}
            </td>
        {{/extraJSON.isApproveMode}}

    </tr>

subTpl-->
