<%@ include file="/include/taglib.jspf"%>
{{! NOTE: these variables are set via JavaScript:
        isCreator, lastLevel }}

<div class="row-fluid">
    <div class='span12 contestData dataSectionWrap {{#eq status "finalize_results"}}complete{{/eq}}'>
        <div class="row-fluid">
            <div class="dataSection activityGoal">
                <h4>{{goal}}</h4>
                {{#if isCreator}}
                    <span><cms:contentText key="GOAL" code="ssi_contest.creator" /></span>
                {{else}}
                    <span><cms:contentText key="TEAM_GOAL" code="ssi_contest.creator" /></span>
                {{/if}}
            </div>

            <div class="dataSection activityProgress">
                <h4>{{progress}}</h4>

                {{#ueq status "finalize_results"}}
                    {{#if isCreator}}
                        <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></span>
                    {{else}}
                        <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></span>
                    {{/if}}
                {{else}}
                    {{#if isCreator}}
                        <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></span>
                    {{else}}
                        <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></span>
                    {{/if}}
                {{/ueq}}
            </div>

            {{#ueq status "finalize_results"}}
            <div class="dataSection activityToGo">
                {{#unless goalAchieved}}
                    <h4>{{remaining}}</h4>
                    <span><cms:contentText key="TO_GO" code="ssi_contest.creator" /></span>
                {{else}}
                    <h4 class="activityGoalAchieved"><i class="icon-verification"></i></h4>
                    <span><cms:contentText key="GOAL_ACHIEVED" code="ssi_contest.creator" /></span>
                {{/unless}}
            </div>
            {{/ueq}}
        </div>
    </div>
</div>

<div class="tabWrap">
    <div class="row-fluid">
        <div class="span12">
            <ul class="nav nav-tabs">
                <li class="active">
                    <a data-toggle="tab" data-tab-name="levels" data-target=".levels">
                        <cms:contentText key="GOAL_LEVEL" code="ssi_contest.creator" />
                    </a>
                </li>
                <li>
                    <a data-toggle="tab" data-tab-name="participants" data-target=".participants">
                        <cms:contentText key="PAX_BREAKDOWN_LEVEL" code="ssi_contest.creator" />
                    </a>
                </li>
            </ul>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12 tab-content">
            <div class="tab-pane active levels">
                <div class="row-fluid">
                    <div class="span12 levelChartWrapper adminChart">
                        <table class="levelData">
                            <tr class="levels">
                                <td class="span2"></td>
                                {{#each contestLevels}}
                                <td class="levelCol span2 {{classes}}">
                                    <div class="levelHeader">
                                        <span class="levelName">
                                            <span><cms:contentText key="LEVEL" code="ssi_contest.creator" /></span>
                                            <strong>{{name}}</strong>
                                        </span>
                                    </div>
                                </td>
                                {{/each}}
                            </tr>

                            <tr class="goalDetails">
                                <td class="labels">
                                    <strong><cms:contentText key="GOAL" code="ssi_contest.creator" /></strong>
                                </td>
                                {{#each contestLevels}}
                                    <td class="text-center">
                                        <span>{{#ueq ../baselineType "no"}}<cms:contentText key="BASELINE" code="ssi_contest.creator" /> + {{/ueq}}
                                        {{goalPercent}}</span>
                                    </td>
                                {{/each}}
                            </tr>

                            <tr class="payoutDetails">
                                <td class="labels">
                                    <strong><cms:contentText key="PAYOUT" code="ssi_contest.creator" /></strong>
                                </td>
                                {{#each contestLevels}}
                                    <td class="text-center">
                                        {{#eq ../payoutType "points"}}
                                            <span>{{payout}} <cms:contentText key="POINTS" code="ssi_contest.creator" /></span>
                                        {{/eq}}
                                        {{#eq ../payoutType "other"}}
                                            <span>{{payout}}</span>
                                        {{/eq}}
                                    </td>
                                {{/each}}
                            </tr>
                        </table>
                    </div>
                </div>

                {{#if includeBonus}}
                    <div class="row-fluid">
                        <div class="span12 goalBonus">
                            <strong><cms:contentText key="BONUS" code="ssi_contest.creator" /></strong> <cms:contentText key="FOR_EVERY" code="ssi_contest.payout_stepitup" /> <strong>{{bonusForEvery}}</strong> <cms:contentText key="OVER_LEVEL" code="ssi_contest.payout_stepitup" /> <strong>{{lastLevel.name}}</strong> <cms:contentText key="PAX_EARN" code="ssi_contest.payout_stepitup" />
                            {{#eq payoutType "points"}}
                                <strong>{{bonusPayout}} <cms:contentText key="POINTS" code="ssi_contest.creator" /></strong>
                            {{/eq}}
                            {{#eq payoutType "other"}}
                                <strong>{{bonusPayout}}</strong>
                            {{/eq}}
                        </div>
                    </div>
                {{/if}}
            </div> {{! #levels }}

            <div class="tab-pane participants">
                <div class="row-fluid barChartContainer">
                    <div class="span12">
                        <div class="chartHolder barChart">

                            <div class="ssiBarChart">
                                <dl class="pax-per-level levels{{contestLevels.length}} {{#if includeBonus}}withBonus{{/if}}">
                                    <dt class="x-axis"><cms:contentText key='LEVEL' code='ssi_contest.participant' /></dt>
                                    <dd class="y-axis"><cms:contentText key='PAX' code='ssi_contest.participant' /></dd>

                                    {{#each contestLevels}}
                                    <dt class="key level{{name}}" data-level="{{name}}">
                                        <span class="label">{{name}}</span>
                                    </dt>
                                    <dd class="value level{{name}}" data-level="{{name}}" data-count="{{participantsCount}}">
                                        <span class="bar"></span>
                                        <span class="label">{{participantsCount}}</span>
                                    </dd>
                                    {{/each}}

                                    {{#if includeBonus}}
                                    <dt class="key levelB" data-level="B">
                                        <span class="label"><cms:contentText key="BONUS" code="ssi_contest.creator" /></span>
                                    </dt>
                                    <dd class="value levelB" data-level="B" data-count="{{bonusParticipantsCount}}">
                                        <span class="bar"></span>
                                        <span class="label">{{bonusParticipantsCount}}</span>
                                    </dd>
                                    {{/if}}
                                </dl>
                            </div><!-- /.ssiBarChart -->

                        </div>
                    </div>
                </div>

                <div class="row-fluid">
                </div>
            </div> {{! #participants }}
        </div> <!-- /.tab-content -->
    </div>
</div> <!-- /.tabWrap -->

<!-- <div class="barGraphWrap">
    <div class="row-fluid">
        <div class="span8">
            <div class="row-fluid">
                <div class="span2">
                    Participants
                </div>
                <div class="span10">
                    <div class="row-fluid">
                        {{#each contestLevels}}
                            <div class="span2">
                                {{participantsCount}}<br>
                                {{name}}
                            </div>
                        {{/each}}
                    </div>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    Level
                </div>
            </div>
        </div>
        <div class="span4">
            <div class="row-fluid">
                <div class="span6 text-center">
                    {{goal}}<br>goal
                </div>
                <div class="span6 text-center">
                    {{progress}}<br>Team Activity
                </div>
            </div>
        </div>
    </div>
</div> -->


{{#if isCreator}}
    <div class="row-fluid">
        <div class="span12 payoutData">
            {{#eq status "finalize_results"}}
                <h3 class="sectionTitle payoutTitle"><span class="graphFlow"><cms:contentText key="FINAL_PAYOUT" code="ssi_contest.creator" /></span></h3>
            {{else}}
                {{#eq payoutType "other"}}
                    <h3 class="sectionTitle payoutTitle"><span class="graphFlow"><cms:contentText key="POTENTIAL_PAYOUT_VALUE" code="ssi_contest.creator" /></span></h3>
                {{else}}
                    <h3 class="sectionTitle payoutTitle"><span class="graphFlow"><cms:contentText key="POTENTIAL_POINTS" code="ssi_contest.creator" /></span></h3>
                {{/eq}}
            {{/eq}}

            <div class="row-fluid">
                <div class="span7">
                    <div class="ssiPayoutGraph" data-model-id="{{id}}" data-chart-type="payoutProgress">
                        <div class="ssiPayoutGraphBar">
                            <div class="barFill"></div>
                        </div>

                        <div class="ssiPayoutGraphProgressMarker">
                            <i class="arrow"></i>
                            <div class="number">
                                <p class="val">{{payoutProgress}}</p> <p class="percent">({{payoutPercentProgress}}%)</p>
                            </div>
                        </div>

                        <div class="ssiPayoutGraphGoalMarker payout">
                            <span class="goalMarkerCount">{{payoutCap}}</span>
                            {{#eq payoutType "other"}}
                                <span class="goalMarkerLabel"><cms:contentText key="PAYOUT" code="ssi_contest.creator" /></span>
                            {{else}}
                                <span class="goalMarkerLabel"><cms:contentText key="POINTS" code="ssi_contest.creator" /></span>
                            {{/eq}}
                        </div>

                        <div class="ssiPayoutGraphGoalMarker bonus">
                            <span class="goalMarkerCount">{{payoutBonusCap}}</span>
                            {{#eq payoutType "other"}}
                                <span class="goalMarkerLabel"><cms:contentText key="WITH_BONUS" code="ssi_contest.creator" /></span>
                            {{else}}
                                <span class="goalMarkerLabel"><cms:contentText key="POINTS_WITH_BONUS" code="ssi_contest.creator" /></span>
                            {{/eq}}
                        </div>
                    </div>
                </div>

                <div class="span4 offset1 dataSection activityRemaining">
                    {{#if payoutProgress}}
                        {{#eq status "finalize_results"}}
                            {{#eq payoutType "points"}}
                                <strong>{{payoutProgress}}</strong>
                                <span><cms:contentText key="POINTS_ISSUED" code="ssi_contest.creator" /></span>
                            {{/eq}}
                            {{#eq payoutType "other"}}
                                <strong>{{payoutProgress}}</strong>
                                <span><cms:contentText key="PAYOUT_ISSUED" code="ssi_contest.creator" /></span>
                            {{/eq}}
                        {{else}}
                            {{#eq payoutType "points"}}
                                <strong>{{payoutRemaining}}</strong>
                            {{/eq}}
                            {{#eq payoutType "other"}}
                                <strong>{{payoutRemaining}}</strong>
                            {{/eq}}
                            <span><cms:contentText key="REMAINING" code="ssi_contest.creator" /></span>
                        {{/eq}}
                    {{/if}}
                </div>

            </div><!-- /.row-fluid -->
        </div><!-- /.span12.payoutData -->
    </div><!-- /.row-fluid -->
{{/if}}

{{#if includeStackRanking}}
{{#if isProgressLoaded}}
<div class="row-fluid">
    <div class="span12 stackRankWrap stackRankBoard highlightedBoard">
        <div class="row-fluid">
            <div class="span6">
                <h3 class="sectionTitle"><cms:contentText key="STACK_RANK" code="ssi_contest.creator" /></h3>
            </div>
            <div class="span6 text-right">
                <!-- <a href="{{stackRankDetailPageUrl}}" title="" class="viewAll">view all</a> -->
                <button type="button" class="viewAll viewAllStackRank btn btn-small btn-primary btn-inverse"><cms:contentText key="VIEW_ALL" code="ssi_contest.creator" /></button>
            </div>
        </div>
        <div class="row-fluid">
            <div class="span12 splitColWrap">
                <ol class="splitCol splitColSingle">
                    {{#each stackRankParticipants}}
                    <li value="{{rank}}" {{#if classes.length}}class="{{#classes}}{{this}} {{/classes}}"{{/if}}>
                        {{#if score}}<span class="score">{{score}}</span>{{/if}}
                        {{#if rank}}<b class="rank">{{rank}}.</b>{{/if}}
                        <div class="avatarwrap">
                        {{#if avatarUrl}}
                            <img alt="{{firstName}} {{lastName}}"  src="{{#timeStamp avatarUrl}}{{/timeStamp}}">
                        {{else}}
                            <span >{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                        {{/if}}
                        </div>
                        {{#if contestUrl}}
                            <a href="{{contestUrl}}" class="leaderName">{{firstName}} {{lastName}}</a>
                        {{else}}
                            <span class="leaderName">{{firstName}} {{lastName}}</span>
                        {{/if}}
                    </li>
                    {{/each}}
                </ol>
            </div>
        </div>
    </div>
</div>
{{/if}}
{{/if}}
