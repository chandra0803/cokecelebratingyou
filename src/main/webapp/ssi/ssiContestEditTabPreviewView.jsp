<%@ include file="/include/taglib.jspf"%>

{{#ueq contestType "awardThemNow"}}
<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepInfo"><cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key='TITLE' code='ssi_contest.generalInfo' /></h5>
            <dl class="dl-horizontal">
            <dt><cms:contentText key="CONTEST_NAME" code="ssi_contest.generalInfo" /></dt>
            <dd>{{name}}</dd>
            <dt><cms:contentText key="CONTEST_DATES" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{startDate}} - {{endDate}}</dd>
            <dt><cms:contentText key="TILE_START_DATE" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{tileStartDate}}</dd>
            <dt><cms:contentText key="CONTEST_DESC" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{description}}
                {{#if attachmentTitle}}
                <a href="{{attachmentUrl}}" target="_blank" class="ssiApproveDetailsAttachment">{{attachmentTitle}}</a>
                {{/if}}
            </dd>

        </dl>
    </div>
</div>

<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepParticipantsManagers"><cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key='TITLE' code='ssi_contest.pax.manager' /></h5>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="PAX" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{participantsCount}} <cms:contentText key="SELECTED" code="ssi_contest.preview"/></dd>
            <dt><cms:contentText key="MGR" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{managersCount}} <cms:contentText key="SELECTED" code="ssi_contest.preview"/></dd>
            <dt><cms:contentText key="SV" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{superViewersCount}} <cms:contentText key="SELECTED" code="ssi_contest.preview"/></dd>
        </dl>
    </div>
</div>

{{else}}


    {{#eq awardThemNowStatus "issueMoreAwards"}}
    <h5>{{name}}</h5>
    {{else}}
    <div class="ssiPreviewSection well">
        <div class="">
            <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepInfo"> <cms:contentText key="EDIT" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
            <h5><cms:contentText key="TITLE" code="ssi_contest.generalInfo" /></h5>
            <dl class="dl-horizontal">
                <dt><cms:contentText key="CONTEST_NAME" code="ssi_contest.generalInfo" /></dt>
                <dd>{{name}}</dd>
                <dt><cms:contentText key="CONTEST_DATES" code="ssi_contest.approvals.summary"/></dt>
                <dd>{{startDate}} - {{endDate}}</dd>
                <dt><cms:contentText key="CONTEST_DESC" code="ssi_contest.approvals.summary"/></dt>
                <dd>{{description}}
                    {{#if attachmentTitle}}
                    <a href="{{attachmentUrl}}" class="ssiApproveDetailsAttachment">{{attachmentTitle}}</a>
                    {{/if}}
                </dd>
                {{#if badge.img}}
                <dt><cms:contentText key="BADGE" code="ssi_contest.approvals.detail"/></dt>
                <dd><img src="{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}</dd>
                {{/if}}
                 {{#eq payoutType "points"}}
                    {{#if billCodeRequired}}
                        {{#if billCodes}}
                            {{#if billCodes.length}}
                                <dt>Charge Contest To</dt>
                                {{#each billCodes}}
                                    {{#ueq index null}}
                                        <dd>Bill Code {{inc index}} - {{billCodeName}}</dd>
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

    <div class="ssiPreviewSection well">
        <div class="">
            <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepParticipantsManagers"><cms:contentText key="EDIT" code="ssi_contest.preview"/> <i class="icon-pencil2"></i> </a>
            <h5><cms:contentText key="PAX" code="ssi_contest.approvals.summary"/></h5>
            <dl class="dl-horizontal">
                <dt><cms:contentText key="PAX" code="ssi_contest.approvals.summary"/></dt>
                <dd>{{participantsCount}} <cms:contentText key="SELECTED" code="ssi_contest.preview"/></dd>
            </dl>
        </div>
    </div>

    <div class="ssiPreviewSection well">
        <div class="">
            <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepPayouts"> <cms:contentText key="EDIT" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
            <h5><cms:contentText key="PAYOUTS" code="ssi_contest.approvals.summary"/></h5>
            <dl class="dl-horizontal">
                <dt><cms:contentText key="ACTIVITY_DESC" code="ssi_contest.approvals.summary"/></dt>
                <dd>{{activityDescription}}</dd>

                <dt><cms:contentText key="ACTIVITY_AMT" code="ssi_contest.approvals.summary"/></dt>
                <dd>{{objectiveAmount}}</dd>

                {{#ueq payoutType "other"}}
                    <dt><cms:contentText key="PAYOUT" code="ssi_contest.approvals.summary"/></dt>
                    <dd>{{payoutAmount}}</dd>
                {{/ueq}}

                {{#eq payoutType "other"}}
                    <dt><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.summary"/></dt>
                    <dd>{{payoutDescription}}</dd>
                    <dt><cms:contentText key="VALUE" code="ssi_contest.approvals.summary"/></dt>
                    <dd>{{payoutAmount}}</dd>
                {{/eq}}

                {{#if message}}
                    <dt><cms:contentText key="EMAIL_MESG" code="ssi_contest.approvals.summary"/></dt>
                    <dd>{{message}}</dd>
                {{/if}}

            </dl>
        </div>
    </div>
{{/ueq}}

{{#eq contestType "objectives"}}
<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepPayouts"><cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key='TITLE' code='ssi_contest.objectives' /></h5>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="OBJECTIVES_DESCRIPTION" code="ssi_contest.preview"/></dt>
            <dd>{{activityDescription}}</dd>
            <dt><cms:contentText key="MEASURE_OBJECTIVE_IN" code="ssi_contest.preview"/></dt>
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
                <dt><cms:contentText key="PAYOUT_VALUE" code="ssi_contest.preview"/></dt>
                <dd>{{payoutAmount}}</dd>
            {{/eq}}

            {{#if badge.img}}
            <dt><cms:contentText key="BADGE" code="ssi_contest.approvals.detail"/></dt>
            <dd><img src="{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}</dd>
            {{/if}}

            <dt><cms:contentText key="MAX_CONTEST_POTENTIAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPotential}}</dd>
            <dt><cms:contentText key="YOUR_CONTEST_GOAL" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{goal}}</dd>
            <dt><cms:contentText key="MAX_PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayout}}</dd>

            {{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                                <dt>Charge Contest To</dt>
                                {{#each billCodes}}
                                    {{#ueq index null}}
                                        <dd>Bill Code {{inc index}} - {{billCodeName}}</dd>
                                    {{/ueq}}
                                {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}

            {{#eq payoutType "points"}}
            {{#if includeBonus}}
            <dt><cms:contentText key="MAX_PAYOUT_BONUS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{maximumPayoutBonus}}</dd>
            {{/if}}
            {{/eq}}

            {{#if includeStackRanking}}
            <dt><cms:contentText key="STACK_RANK" code="ssi_contest.approvals.summary"/></dt>
            <dd><cms:contentText key="YES" code="ssi_contest.preview"/></dd>
            {{/if}}
        </dl>
    </div>
</div>
{{/eq}}

{{#eq contestType "doThisGetThat"}}
<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepPayouts"><cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key='ACTIVITIES_AND_PAYOUTS' code='ssi_contest.approvals.summary' /></h5>

        <dl class="dl-horizontal">
            <dt><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityMeasuredName}}</dd>
            <dt><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{payoutTypeName}}</dd>

            {{#eq payoutType "points"}}
                {{#if billCodeRequired}}
                    {{#if billCodes}}
                        {{#if billCodes.length}}
                            <dt>Charge Contest To</dt>
                            {{#each billCodes}}
                                {{#ueq index null}}
                                    <dd>Bill Code {{inc index}} - {{billCodeName}}</dd>
                                {{/ueq}}
                            {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}

            {{#if includeStackRanking}}
                <dt><cms:contentText key="STACK_RANK" code="ssi_contest.approvals.summary"/></dt>
                <dd><cms:contentText key="YES" code="ssi_contest.preview"/></dd>
            {{/if}}

        </dl>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th rowspan="2"><cms:contentText key="ACTIVITY_DESC" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="FOR_EVERY" code="ssi_contest.approvals.summary"/></th>
                    <th {{#eq payoutType "other"}} colspan="2"{{/eq}}><cms:contentText key="EARN" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="MIN_QUALIFIER" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="INDIVIDUAL_PAYOUT_CAP" code="ssi_contest.approvals.summary"/></th>

                    {{#eq payoutType "points"}}
                    <th rowspan="2"><cms:contentText key="MAX_ACTIVITY_PAYOUT" code="ssi_contest.approvals.summary"/></th>

                    {{else}}
                    <th rowspan="2"><cms:contentText key="MAX_ACTIVITY_PAYOUT_VAL" code="ssi_contest.preview"/></th>
                    {{/eq}}

                    <th rowspan="2"><cms:contentText key="ACTIVITY_GOAL" code="ssi_contest.approvals.summary"/></th>
                    <th rowspan="2"><cms:contentText key="MAX_ACTIVITY_POTENTIAL" code="ssi_contest.approvals.summary"/></th>
                </tr>

                {{#eq payoutType "other"}}
                <tr>
                    <th><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.summary"/></th>
                    <th><cms:contentText key="VALUE" code="ssi_contest.approvals.summary"/></th>
                </tr>
                {{/eq}}
            </thead>
            <tfoot>
                <tr>
                    <td {{#eq payoutType "points"}}colspan="5" {{else}} colspan="6"{{/eq}}><cms:contentText key="TOTAL" code="ssi_contest.approvals.summary"/></td>
                    <td class="tr">{{totalMaxPayout}}</td>
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
    </div>
</div>
{{/eq}}

{{#eq contestType "stepItUp"}}
<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepPayouts"><cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key="LEVEL_PAYOUTS" code="ssi_contest.approvals.summary"/></h5>
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
                                <dt>Charge Contest To</dt>
                                {{#each billCodes}}
                                    {{#ueq index null}}
                                        <dd>Bill Code {{inc index}} - {{billCodeName}}</dd>
                                    {{/ueq}}
                                {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}
        </dl>

        {{#each contestLevels}}
       <span class="levelTitle">{{name}}</span>
        <dl class="dl-horizontal ssiContestLevels">
            <dt>{{activityLabel}}</dt>
            <dd>{{amount}}</dd>

            {{#eq ../payoutType "points"}}
            	<dt><cms:contentText key="PAYOUT" code="ssi_contest.approvals.summary"/></dt>
            	<dd>{{payout}}</dd>
            {{else}}
            	<dt><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.summary"/></dt>
            	<dd>{{payoutDescription}}</dd>
            	<dt><cms:contentText key="VALUE" code="ssi_contest.approvals.summary"/></dt>
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
            <dd><cms:contentText key="YES" code="ssi_contest.preview"/></dd>
            {{/if}}

            {{#if includeBonus}}
            <dt><cms:contentText key="BONUS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{bonus}}</dd>
            <dt><cms:contentText key="INDIVIDUAL_BONUS_CAP" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{individualBonusCap}}</dd>
            {{/if}}
        </dl>

    </div>
</div>

<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepPayouts"><cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key="CONTEST_GOAL" code="ssi_contest.approvals.summary"/></h5>
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
        </dl>
    </div>
</div>
{{/eq}}

{{#eq contestType "stackRank"}}
<div class="ssiPreviewSection well">
    <div class="">
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepPayouts"> <cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/> <i class="icon-pencil2"></i></a>
        <h5><cms:contentText key="RANKS_AND_PAYOUTS" code="ssi_contest.approvals.summary"/></h5>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="ACTIVITY_DESC" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityDescription}}</dd>
            <dt><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{activityMeasuredName}}</dd>
            {{#if includeMinimumQualifier}}
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
                                <dt>Charge Contest To</dt>
                                {{#each billCodes}}
                                    {{#ueq index null}}
                                        <dd>Bill Code {{inc index}} - {{billCodeName}}</dd>
                                    {{/ueq}}
                                {{/each}}
                        {{/if}}
                    {{/if}}
                {{/if}}
            {{/eq}}
            <dt><cms:contentText key="NO_OF_RANKS" code="ssi_contest.approvals.summary"/></dt>
            <dd>{{rankCount}}</dd>
        </dl>
    </div>
</div>
{{/eq}}

{{#if collectDataMethod}}
<div class="ssiPreviewSection well">
    <div class="">
        {{#ueq status "live"}}
        <a href="#" class="btn btn-primary btn-small editStepBtn" data-step-name="stepDataCollection">
            <cms:contentText key="EDIT_SECTION" code="ssi_contest.preview"/>
            <i class="icon-pencil2"></i>
        </a>
        {{/ueq}}
        <h5><cms:contentText key="TITLE" code="ssi_contest.datacollection"/></h5>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="DATA_COLLECTION_METHOD" code="ssi_contest.datacollection"/></dt>
            <dd>
                {{#eq collectDataMethod "activityUpload"}}
                    <cms:contentText key="SPREAD_SHEET" code="ssi_contest.datacollection"/>
                {{/eq}}
                {{#eq collectDataMethod "claimSubmission"}}
                    <cms:contentText key="CLAIM_FORM" code="ssi_contest.datacollection"/>
                {{/eq}}
            </dd>
        </dl>
    </div>
</div>
{{/if}}
