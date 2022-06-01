<%@ include file="/include/taglib.jspf"%>
<div class="page-topper">
    <div class="row-fluid">
        <div class="span8">
            <h2>{{name}}</h2>
            <dl class="dl-horizontal">
                <dt><cms:contentText key="APPROVAL_STATUS" code="ssi_contest.approvals.detail"/></dt>

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
                <dd><cms:contentText key="WAITING_APPROVAL" code="ssi_contest.approvals.detail"/></dd>
                {{/if}}
            </dl>
        </div>
        <div class="span4 buttons text-right">
            {{#if showApproveDeny}}
            <form id="ssiApproveContest" method="post" action="manageSSIContestApprovalSummary.do?method=approve" enctype="multipart/form-data">
                <input type="hidden" value="{{clientState}}" name="clientState" />
                <button type="submit" class="btn btn-primary ssiApproveContestBtn" name="status" value="approved"><cms:contentText key="APPROVE" code="ssi_contest.approvals.detail"/></button>
                <button type="button" class="btn ssiDenyContestBtn"><cms:contentText key="DENY" code="ssi_contest.approvals.detail"/></button>
            </form>
            {{/if}}
        </div>
    </div>
</div>

<div class="row-fluid ssiDetailsAction">
    <div class="span8">
        <a href="#" class="ssiBackToSummary btn btn-primary"><cms:contentText key="BACK_TO_SUMMARY" code="ssi_contest.approvals.detail"/></a>
    </div>
</div>

<div class="row-fluid ssiDetailsInvitees ssiDetailsSection" style="display:none">
    <div class="ssiDetialsParticipants">
        <div class="span12">
            <h3><cms:contentText key="PARTICIPANTS" code="ssi_contest.approvals.detail"/></h3>
            <p><cms:contentText key="PARTICIPANTS_DESC" code="ssi_contest.approvals.detail"/></p>

            <div id="ssiParticipantTableTpl" class="ssiInviteesCollectionView">
                <!-- dynamic content added from ssiApproveContestparticipantTableTpl.html-->
            </div>
        </div>
    </div>

    <div class="ssiDetialsManagers">
        <div class="span12">
            <h3><cms:contentText key="MANAGERS" code="ssi_contest.approvals.detail"/></h3>
            {{#if managersCount}}
            <p><cms:contentText key="MANAGERS_DESC" code="ssi_contest.approvals.detail"/></p>

            <div id="ssiManagersTableTpl" class="ssiInviteesCollectionView">
                <!-- dynamic content added from ssiApproveContestparticipantTableTpl.html-->
            </div>
            {{else}}
            <p><cms:contentText key="NO_MANAGERS_FOR_CONTEST" code="ssi_contest.approvals.detail"/></p>
            {{/if}}
        </div>
    </div>

    <div class="ssiDetialsSuperViewers">
        <div class="span12">
            <h3><cms:contentText key="SV" code="ssi_contest.approvals.summary"/></h3>
            {{#if superViewersCount}}
            <p><cms:contentText key="SV_DESC" code="ssi_contest.approvals.detail"/></p>

            <div id="ssiSuperViewersTableTpl" class="ssiInviteesCollectionView">
                <!-- dynamic content added from ssiApproveContestparticipantTableTpl.html-->
            </div>
            {{else}}
            <p><cms:contentText key="NO_SUPERVIEWERS_FOUND" code="ssi_contest.pax.manager"/></p>
            {{/if}}
        </div>
    </div>
</div>

<div class="row-fluid ssiDetailsLevels ssiDetailsSection" style="display:none">
    <div class="span12">
        <h3><cms:contentText key="INDIVIDUAL_BASELINE" code="ssi_contest.approvals.detail"/></h3>

        <div class="ssiLevelsTableTpl">
            <!-- dynamic content added from ssiApproveContestLevelsTableTpl.html-->
        </div>
    </div>
</div>

<div class="row-fluid ssiDetailsObjectives ssiDetailsSection" style="display:none">
    <div class="span12">
        <h3><cms:contentText key="OBJECTIVES_PAYOUTS" code="ssi_contest.approvals.detail"/></h3>
        <dl class="dl-horizontal">
            <dt><cms:contentText key="CONTEST_GOAL" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{goal}}</dd>
            <dt><cms:contentText key="CONTEST_POTENTIAL" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{maximumPotential}}</dd>
            <dt><cms:contentText key="MAXIMUM_PAYOUTS" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{maximumPayout}}</dd>

            {{#eq payoutType "points"}}
            {{#if includeBonus}}
            <dt><cms:contentText key="MAXIMUM_PAYOUTS_WITH_BONUS" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{maximumPayoutBonus}}</dd>
            {{/if}}
            {{/eq}}
        </dl>

        <div class="ssiObjectivesTableTpl">
            <!-- dynamic content added from ssiApproveContestObjectiveTableTpl.html-->
        </div>
    </div>
</div>

<div class="row-fluid ssiDetailsRanks ssiDetailsSection" style="display:none">
    <div class="span12">
        <h3><cms:contentText key="RANKS_PAYOUTS" code="ssi_contest.approvals.detail"/></h3>

        <dl class="dl-horizontal">
            <dt><cms:contentText key="CONTEST_GOAL" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{goal}}</dd>
            {{#eq payoutType "points"}}
            <dt><cms:contentText key="MAXIMUM_PAYOUTS" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{maximumPayout}}</dd>
            {{else}}
            <dt><cms:contentText key="MAXIMUM_PAYOUT_VALUE" code="ssi_contest.approvals.detail"/></dt>
            <dd>{{maximumPayout}}</dd>
            {{/eq}}
        </dl>

        <!-- removing for phase 1
        <div class="control-group">
            <label for="ssiRankPayouts"><cms:contentText key="SHOW_PAYOUTS" code="ssi_contest.approvals.detail"/>:</label>
            <div class="controls">
                <select id="ssiRankPayouts" name="ssiRankPayouts">
                    <option value="all" name="All Participants"><cms:contentText key="ALL_PAX" code="ssi_contest.approvals.detail"/></option>
                    <option value="region" name="Region"><cms:contentText key="REGION" code="ssi_contest.approvals.detail"/></option>
                    <option value="district" name="District"><cms:contentText key="DISTRICT" code="ssi_contest.approvals.detail"/></option>
                </select>
            </div>
        </div>-->

        <div class="ssiRanksTableTpl">
            <!-- dynamic content added from ssiApproveContestRanksTableTpl.html-->
        </div>

    </div>
</div>
