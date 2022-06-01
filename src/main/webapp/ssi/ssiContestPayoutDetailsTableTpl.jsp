<%@ include file="/include/taglib.jspf"%>
<div class="paginationControls pagination pagination-right"></div>
<div class="emptyMsg alert" style="display:none"><cms:contentText key="YOU_HAVE_NOT_ADDED_ANYONE" code="claims.submission" /></div>

    <table class="table table-striped">
        <thead>
            <tr>
                <th class="sortHeader unsorted" data-sort="lastName">
                    <a href="#">
                        <cms:contentText key="PARTICIPANT" code="ssi_contest.generalInfo"/>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
              	<th class="sortHeader unsorted" data-sort="activityDescription">
                    <a href="#">
                        <cms:contentText key="ACTIVITY_DESCRIPTION" code="ssi_contest.payout_objectives"/>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                <th class="sortHeader unsorted number" data-sort="activityAmount">
                    <a href="#">
                        <cms:contentText key="ACTIVITY_AMOUNT" code="ssi_contest.creator"/>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#eq payoutType "points"}}
                <th class="sortHeader unsorted number" data-sort="payoutValue">
                    <a href="#">
                        <cms:contentText key="PAYOUT" code="ssi_contest.creator"/>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{/eq}}
                {{#eq payoutType "other"}}
                <th class="sortHeader unsorted" data-sort="payoutDescription">
                    <a href="#">
                        <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.payout_objectives"/>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                <th class="sortHeader unsorted number" data-sort="payoutValue">
                    <a href="#">
                        <cms:contentText key="PAYOUT_VALUE" code="ssi_contest.participant"/>
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{/eq}}
            </tr>
        </thead>
        <tfoot>
            <tr>
                <td colspan="2" class="totalDisp">
                    <cms:contentText key="TOTAL" code="ssi_contest.approvals.summary"/>
                </td>
                <td class="totalDisp number">
                    {{totalActivity}}
                </td>
                <td class="totalDisp number" {{#eq payoutType "other"}}colspan="2"{{/eq}}>
                    {{totalPayoutAmount}}
                    {{#eq payoutType "points"}}
                        <cms:contentText key="POINTS_UPPERCASE" code="ssi_contest.participant"/>
                    {{/eq}}
                </td>
            </tr>
        </tfoot>
        <tbody>
        </tbody>
    </table>

<!-- <div class="paginationConts pagination pagination-right"></div> -->
<div class="paginationControls pagination pagination-right"></div>

<!--subTpl.recordRow=
    {{debug}}
    <tr class="summary-item" data-participant-id="{{id}}">

        <td>
            {{lastName}}, {{firstName}}
        </td>
        <td>
            {{activityDescription}}
        </td>
        <td class="number">
            {{objectiveAmount}}
        </td>
        {{#eq extraJSON.payoutType "points"}}
            <td class="number">
                {{objectivePayout}}
            </td>
        {{/eq}}
        {{#eq extraJSON.payoutType "other"}}
            <td>
                {{objectivePayoutDescription}}
            </td>
            <td class="number">
                {{objectivePayout}}
            </td>
        {{/eq}}

    </tr>

subTpl-->
