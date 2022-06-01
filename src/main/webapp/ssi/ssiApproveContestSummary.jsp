<%@ include file="/include/taglib.jspf"%>
<div class="page-topper">
    <div class="row-fluid">
        <div class="span12">
            <h2>{{name}}</h2>
            <dl class="dl-horizontal">
                <dt><cms:contentText key="APPROVAL_STATUS" code="ssi_contest.approvals.summary"/></dt>

                {{#if approvalRequired}}
                {{#approvalLevels}}
                <dd class="ssiApproverLevel">
                    {{name}}:
                    {{#if approved}}
                        <cms:contentText key="APPROVED_BY" code="ssi_contest.approvals.detail"/> {{approvedBy}}
                    {{else}}
                        <cms:contentText key="WAITING_APPROVAL" code="ssi_contest.approvals.detail"/>
                     <a href="#" class="ssiApproverPopover"><cms:contentText key="VIEW_APPROVERS" code="ssi_contest.approvals.detail"/></a>
                        <div class="ssiApproverList" style="display: none">
                            <ul>
                                {{#approverList}}
                                <li>{{name}}</li>
                                {{/approverList}}
                            </ul>
                        </div>
                    {{/if}}
                </dd>
                {{/approvalLevels}}

                {{else}}
                <dd><cms:contentText key="APPROVAL_PENDING" code="ssi_contest.approvals.summary"/></dd>
                {{/if}}
            </dl>
            <cms:errors/>
        </div>

        {{#if showApproveDeny}}
        <div class="span4 buttons text-right">
            <form id="ssiApproveContest" method="post" action="manageSSIContestApprovalSummary.do?method=approve" enctype="multipart/form-data">
                <input type="hidden" value="{{clientState}}" name="clientState" />
                <button type="submit" class="btn btn-primary ssiApproveContestBtn" name="status" value="approved"><cms:contentText key="APPROVE_BTN" code="ssi_contest.approvals.summary"/></button>
                <button type="button" class="btn ssiDenyContestBtn"><cms:contentText key="DENY_BTN" code="ssi_contest.approvals.summary"/></button>
            </form>
        </div>
        {{/if}}
    </div>
</div>

<div class="row-fluid ssiSummaryDetails">
    <div class="span12">
        <dl class="dl-horizontal">
            <dt><cms:contentText key="CREATED_BY" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{contestCreator}}</dd>
            <dt><cms:contentText key="CONTEST_DATES" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{startDate}} - {{endDate}}</dd>
            <dt><cms:contentText key="TILE_START_DATE" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{tileStartDate}}</dd>
            <dt><cms:contentText key="CONTEST_DESC" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{description}}
                {{#if attachmentTitle}}<a href="{{attachmentUrl}}" target="_blank" class="ssiApproveDetailsAttachment">{{attachmentTitle}}</a>{{/if}}
            </dd>
            {{#if includeMessage}}
                <dt><cms:contentText key="EMAIL_MESSAGE" code="ssi_contest.approvals.summary"/></dt>
                <dd>{{{message}}}</dd>
            {{/if}}
        </dl>
    </div>
</div>

<!-- Participants and Managers Section -->
<div class="row-fluid ssiSummaryInvitees">
    <div class="span12">
        <h3><cms:contentText key="PAX_AND_MGR" code="ssi_contest.approvals.summary"/>&nbsp;<a href="#clientState={{clientState}}" class="ssiViewDetails" data-details-section="Invitees"><cms:contentText key="VIEW_DETAILS" code="ssi_contest.approvals.summary"/></a></h3>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="PAX" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{participantsCount}} <cms:contentText key="SELECTED" code="ssi_contest.approvals.summary"/></dd>
            <dt><cms:contentText key="MGR" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{#if managersCount}}{{managersCount}}{{else}}0{{/if}} <cms:contentText key="SELECTED" code="ssi_contest.approvals.summary"/></dd>
            <dt><cms:contentText key="SV" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{#if superViewersCount}}{{superViewersCount}}{{else}}0{{/if}} <cms:contentText key="SELECTED" code="ssi_contest.approvals.summary"/></dd>
        </dl>
    </div>
</div>

{{#eq contestType "stepItUp"}}
<!-- Levels and Payouts Section -->
<div class="row-fluid ssiSummaryLevels">
    <div class="span12">
        <h3><cms:contentText key="LEVEL_PAYOUTS" code="ssi_contest.approvals.summary"/></h3>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="ACTIVITY_DESC" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityDescription}}</dd>
            <dt><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityMeasuredName}}</dd>
            <dt><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{payoutTypeName}}</dd>
            {{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                            <dt><cms:contentText key="CHARGE_CONTEST_TO" code="ssi_contest.approvals.summary"/></dt>
                            {{#each billCodes}}
                                {{#ueq index null}}
                                    <dd><cms:contentText key="BILL_CODE" code="ssi_contest.approvals.summary"/> {{inc index}} - {{billCodeName}}</dd>
                                {{/ueq}}
                            {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}
            {{#if individualBaseline}}
            <dt><cms:contentText key="INDIVIDUAL_BASELINE" code="ssi_contest.approvals.summary"/></dt>
            <dd><a href="#clientState={{clientState}}" class="ssiViewDetails" data-details-section="Levels"><cms:contentText key="VIEW_DETAILS" code="ssi_contest.approvals.summary"/></a></dd>
            {{/if}}
        </dl>

        {{#each contestLevels}}
        <h4>{{name}}</h4>
        <dl class="dl-horizontal ssiContestLevels">
            <dt>{{activityLabel}}</dt>
            <dd>{{amount}}</dd>
            {{#eq ../payoutType "points"}}
            <dt><cms:contentText key="PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{payout}}</dd>
            {{else}}
            <dt><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{payoutDescription}}</dd>
            <dt><cms:contentText key="VALUE" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{payout}}</dd>
            {{/eq}}
            {{#if badge.img}}
            <dt><cms:contentText key="BADGE" code="ssi_contest.approvals.summary"/></dt>
            <dd><img src="{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}</dd>
            {{/if}}
        </dl>
        {{/each}}

        <dl class="dl-horizontal">
            {{#if includeStackRanking}}
            <dt><cms:contentText key="STACK_RANK" code="ssi_contest.approvals.summary"/></dt>
            <dd><cms:contentText key="YES" code="ssi_contest.approvals.summary"/></dd>
            {{/if}}

            {{#if includeBonus}}
            <dt><cms:contentText key="BONUS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{bonus}}</dd>
            <dt><cms:contentText key="BONUS_CAP" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{individualBonusCap}}</dd>
            {{/if}}
        </dl>

    </div>
</div>

<!-- Contest Goal Section -->
<div class="row-fluid ssiSummaryGoal">
    <div class="span12">
        <h3><cms:contentText key="CONTEST_GOAL" code="ssi_contest.approvals.summary"/></h3>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="MAX_CONTEST_POTENTIAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPotential}}</dd>
            <dt><cms:contentText key="YOUR_CONTEST_GOAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{goal}}</dd>
            <dt><cms:contentText key="MAX_PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayout}}</dd>

            {{#if includeBonus}}
            <dt><cms:contentText key="MAX_PAYOUT_BONUS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayoutBonus}}</dd>
            {{/if}}

            {{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                            <dt><cms:contentText key="CHARGE_CONTEST_TO" code="ssi_contest.approvals.summary"/></dt>
                            {{#each billCodes}}
                                {{#ueq index null}}
                                    <dd><cms:contentText key="BILL_CODE" code="ssi_contest.approvals.summary"/> {{inc index}} - {{billCodeName}}</dd>
                                {{/ueq}}
                            {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}

        </dl>
    </div>
</div>
{{/eq}}

{{#eq contestType "doThisGetThat"}}
<!-- Activities and Payouts -->
<div class="row-fluid ssiSummaryActivities">
    <div class="span12">
        <h3><cms:contentText key="ACTIVITIES_AND_PAYOUTS" code="ssi_contest.approvals.summary"/></h3>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th rowspan="2"><cms:contentText key="ACTIVITY_DESC" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="FOR_EVERY" code="ssi_contest.approvals.summary"/></th>
                    <th {{#eq payoutType "other"}} colspan="2"{{/eq}}><cms:contentText key="EARN" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="MIN_QUALIFIER" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="INDIVIDUAL_PAYOUT_CAP" code="ssi_contest.approvals.summary"/></th>
                    {{#eq payoutType "points"}}
                    <th rowspan="2"><cms:contentText key="MAX_ACTIVITY_PAYOUT_VAL" code="ssi_contest.approvals.summary"/></th>
                    {{else}}
                    <th rowspan="2"><cms:contentText key="MAX_ACTIVITY_PAYOUT_VAL" code="ssi_contest.approvals.summary"/></th>
                    {{/eq}}
                    <th rowspan="2"><cms:contentText key="ACTIVITY_GOAL" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="MAX_ACTIVITY_POTENTIAL" code="ssi_contest.approvals.summary"/></th>
                </tr>
                {{#eq payoutType "other"}}
                <tr>
                    <th><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.detail"/></th>
                    <th><cms:contentText key="VALUE" code="ssi_contest.approvals.detail"/></th>
                </tr>
                {{/eq}}
            </thead>
            <tfoot>
                <tr>
                    <td {{#eq payoutType "points"}}colspan="5" {{else}} colspan="6"{{/eq}}><cms:contentText key="TOTAL" code="ssi_contest.approvals.summary"/></td>
                    <td colspan="1" class="tr">{{totalMaxPayout}}</td>
                    <td colspan="2">&nbsp;</td>
                </tr>
            </tfoot>
            <tbody>
                {{#each activities}}
                <tr>
                    <td>{{description}}</td>
                    <td class="tr">{{forEvery}}</td>

                    {{#eq ../payoutType "points"}}
                        <td class="tr">{{payoutAmount}}</td>
                    {{else}}
                        <td>{{valueDescription}}</td>
                        <td class="tr">{{payoutAmount}}</td>
                    {{/eq}}

                    <td class="tr">{{minQualifier}}</td>
                    <td class="tr">{{individualPayoutCap}}</td>
                    <td class="tr">{{maxPayout}}</td>
                    <td class="tr">{{goal}}</td>
                    <td class="tr">{{maxPotential}}</td>
                </tr>
                {{/each}}
            </tbody>
        </table>

		<dl class="dl-horizontal">
            {{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                                <dt><cms:contentText key="CHARGE_CONTEST_TO" code="ssi_contest.approvals.summary"/></dt>
                                {{#each billCodes}}
                                    {{#ueq index null}}
                                        <dd><cms:contentText key="BILL_CODE" code="ssi_contest.approvals.summary"/> {{inc index}} - {{billCodeName}}</dd>
                                    {{/ueq}}
                                {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}

            {{#if includeStackRanking}}
            <dt><cms:contentText key="STACK_RANK" code="ssi_contest.approvals.summary"/></dt>
            <dd><cms:contentText key="YES" code="ssi_contest.approvals.summary"/></dd>
            {{/if}}
        </dl>

    </div>
</div>
{{/eq}}

{{#eq contestType "stackRank"}}
<!-- Ranks and Payouts Section -->
<div class="row-fluid ssiSummaryRanks">
    <div class="span12">
        <h3><cms:contentText key="RANKS_AND_PAYOUTS" code="ssi_contest.approvals.summary"/> <a href="#" class="ssiViewDetails" data-details-section="Ranks"><cms:contentText key="VIEW_DETAILS" code="ssi_contest.approvals.summary"/></a></h3>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="ACTIVITY_DESC" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityDescription}}</dd>
            <dt><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityMeasuredName}}</dd>
            {{#if minimumQualifier}}
            <dt><cms:contentText key="MIN_QUALIFIER" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{minimumQualifier}}</dd>
            {{/if}}
            <dt><cms:contentText key="YOUR_CONTEST_GOAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{goal}}</dd>
            <dt><cms:contentText key="MAX_PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayout}}</dd>
            {{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                            <dt><cms:contentText key="CHARGE_CONTEST_TO" code="ssi_contest.approvals.summary"/></dt>
                            {{#each billCodes}}
                                {{#ueq index null}}
                                    <dd><cms:contentText key="BILL_CODE" code="ssi_contest.approvals.summary"/> {{inc index}} - {{billCodeName}}</dd>
                                {{/ueq}}
                            {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}
            <dt><cms:contentText key="NO_OF_RANKS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{ranksToPayout}}</dd>
        </dl>

        <strong><cms:contentText key="SAME_STACK_RANK_INFO" code="ssi_contest.approvals.summary"/></strong>

        <!--  Removing for phase 1
        <table class="table table-striped">
            <thead>
                <tr>
                    <th><cms:contentText key="ORG_TYPE" code="ssi_contest.approvals.summary"/></th>
                    <th><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.approvals.summary"/></th>
                    <th># <cms:contentText key="RANKS_TO_PAYOUT" code="ssi_contest.approvals.summary"/></th>
                    <th><cms:contentText key="TOTAL" code="ssi_contest.approvals.summary"/></th>
                </tr>
            </thead>
            <tbody>
                {{#each orgTypes}}
                <tr>
                    <td>{{name}}</td>
                    <td>{{payoutTypeName}}</td>
                    <td>{{ranksToPayout}}</td>
                    <td>{{#if payoutAmount}} {{payoutAmount}} {{/if}}</td>
                </tr>
                {{/each}}
            </tbody>
        </table>-->

    </div>
</div>
{{/eq}}

{{#eq contestType "objectives"}}
<!-- Objectives and Payouts Section -->
<div class="row-fluid ssiSummaryObjectives">
    <div class="span12">
       <h3><cms:contentText key="OBJECTIVES_AND_PAYOUTS" code="ssi_contest.approvals.summary"/> <a href="#" class="ssiViewDetails" data-details-section="Objectives"><cms:contentText key="VIEW_DETAILS" code="ssi_contest.approvals.summary"/></a></h3>
       <dl class="dl-horizontal">
            <dt><cms:contentText key="OBJECTIVES_DESCRIPTION" code="ssi_contest.preview" /></dt>
            <dd>{{activityDescription}}</dd>
            <dt><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityMeasuredName}}</dd>
            <dt><cms:contentText key="OBJECTIVE" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{objectiveAmount}}</dd>

            {{#eq payoutType "points"}}
            <dt><cms:contentText key="PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{payoutAmount}}</dd>

            {{#if includeBonus}}
            <dt><cms:contentText key="BONUS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{bonus}}</dd>

            <dt><cms:contentText key="INDIVIDUAL_BONUS_CAP" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{individualBonusCap}}</dd>
            {{/if}}

            {{else}}
            <dt><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{payoutDescription}}</dd>
            <dt><cms:contentText key="VALUE" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{payoutAmount}}</dd>
            {{/eq}}

            {{#if badge.img}}
            <dt><cms:contentText key="BADGE" code="ssi_contest.approvals.summary"/></dt>
            <dd><img src="{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}</dd>
            {{/if}}

			{{#if includeStackRanking}}
            <dt><cms:contentText key="STACK_RANK" code="ssi_contest.approvals.summary"/></dt>
            <dd><cms:contentText key="YES" code="ssi_contest.approvals.summary"/></dd>
            {{/if}}

            <dt><cms:contentText key="MAX_CONTEST_POTENTIAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPotential}}</dd>
            <dt><cms:contentText key="YOUR_CONTEST_GOAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{goal}}</dd>
            <dt><cms:contentText key="MAX_PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayout}}</dd>

            {{#eq payoutType "points"}}
            {{#if includeBonus}}
            <dt><cms:contentText key="MAX_PAYOUT_BONUS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayoutBonus}}</dd>
            {{/if}}
            {{/eq}}

			{{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                                <dt><cms:contentText key="CHARGE_CONTEST_TO" code="ssi_contest.approvals.summary"/></dt>
                                {{#each billCodes}}
                                    {{#ueq index null}}
                                        <dd><cms:contentText key="BILL_CODE" code="ssi_contest.approvals.summary"/> {{inc index}} - {{billCodeName}}</dd>
                                    {{/ueq}}
                                {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}

        </dl>
    </div>
</div>
{{/eq}}
