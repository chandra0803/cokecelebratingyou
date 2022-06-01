<%@ include file="/include/taglib.jspf"%>
{{! NOTE: these variables are set via javascript: }}
{{!       isCreator }}

{{! NOTE: this src needs to point to the javascript lib}}
<div class="raphael" data-src="../assets/libs/raphael-min.js"></div>

<!-- {{#if isCreator}} CREATOR {{else}} MANAGER {{/if}} -->

<!--
    each pax recognition to/by item has special CM keys containing the full translated string for the various permutations of the "## of ## participants [have] achieved their objectives" text
    the keys' output will have a {0} and {1} placeholder where the number of people is inserted
    this allows the translations to have plain text and the number in any order
    we embed this CM output as a tplVariable in our ssiAdminObjective Handlebars template
    we use `G5.util.cmReplace` to add the data from the model to the string, and append it using jQuery
-->

<!--tplVariable.achievedCount={
    "description" : "<cms:contentText key="OBJECTIVE_PAX_ACHIEVE" code="ssi_contest.creator" />",
    "descriptionComplete" : "<cms:contentText key="OBJECTIVE_PAX_ACHV_COMPLETE" code="ssi_contest.creator" />"
} tplVariable-->

<div class="row-fluid">
    <div class="span12 contestData">
        <div class="row-fluid {{#if hasMultipleObjectives}}multipleObjectives{{/if}}">
            <!-- chart -->
            <div class="{{#if hasMultipleObjectives}}{{#unless participantsCount}}span12{{else}}span6{{/unless}}{{else}}span6{{/if}} paxProgressChart">
                <div class="chartPercentHolder chartHolder" data-model-id="{{id}}">
                    <div class="chartPercentProgress">
                        {{#if percentProgress}}
                            <strong>{{percentProgress}}<sup>%</sup></strong>
                        {{else}}
                            <strong>0<sup>%</sup></strong>
                        {{/if}}
                    </div>
                </div>
            </div>
            <!-- /chart -->

            {{#unless hasMultipleObjectives}}
                <!-- status -->
                <div class="span6 paxObjectiveInfo objDataSection">
                    <dl class="objDataList dl-horizontal">
                        <dt class="objTitle">
                            <span>{{#if isCreator}}<cms:contentText key="GOAL" code="ssi_contest.creator" />{{else}}<cms:contentText key="TEAM_GOAL" code="ssi_contest.creator" />{{/if}}</span>
                        </dt>
                        <dd class="objData">
                            <strong>{{goal}}</strong>
                        </dd>

                        <dt class="objTitle">
                            {{#eq status "finalize_results"}}
                                <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></span>
                            {{else}}
                                <span>{{#if isCreator}}<cms:contentText key="CONTEST_ACTIVITY" code="ssi_contest.creator" />{{else}}<cms:contentText key="CONTEST_TEAM_ACTIVITY" code="ssi_contest.creator" />{{/if}}</span>
                            {{/eq}}
                        </dt>
                        <dd class="objData">
                            <strong>{{#if progress}}{{progress}}{{else}}0{{/if}}</strong>
                        </dd>

                    {{#ueq status "finalize_results"}}
                        {{#unless goalAchieved}}
                        <dt class="objTitle">
                            <span><cms:contentText key="TO_GO" code="ssi_contest.creator" /></span>
                        </dt>
                        <dd class="objData">
                            <strong>{{remaining}}</strong>
                        </dd>
                        {{else}}
                        <dd class="objData msg-achieved">
                            <strong><i class="icon-verification"></i> <cms:contentText key="GOAL_ACHIEVED" code="ssi_contest.payout_stackrank" /></strong>
                        </dd>
                        {{/unless}}
                    {{/ueq}}
                    </dl>
                </div>
                <!-- /status -->

                {{#if participantsCount}}
            {{! closing .row-fluid and opening a new one only if we need this row }}
            </div><!-- /.row-fluid -->

            <div class="row-fluid achievedCountWrap">
                <div class="span12">
                    {{! content generated using tplVariable.achievedCount }}
                </div>
                {{/if}}

            {{else}}

                {{#if participantsCount}}
                    <div class="span6">
                        <div class="row-fluid achievedCountWrap">
                            {{! content generated using tplVariable.achievedCount }}
                        </div>
                    </div>
                {{/if}}
            {{/unless}}
        </div><!-- /.row-fluid -->
    </div>
</div>


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
                            <strong class="goalMarkerCount">{{payoutCap}}</strong>
                            {{#eq payoutType "other"}}
                                <span class="goalMarkerLabel"><cms:contentText key="PAYOUT" code="ssi_contest.creator" /></span>
                            {{else}}
                                <span class="goalMarkerLabel"><cms:contentText key="POINTS" code="ssi_contest.creator" /></span>
                            {{/eq}}
                        </div>

                        <div class="ssiPayoutGraphGoalMarker bonus">
                            <strong class="goalMarkerCount">{{payoutBonusCap}}</strong>
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
                <!-- <a href="#URL_TO_STACKRANK_VIEW/{{id}}" title="" class="viewAll">view all</a> -->
                <button type="button" class="viewAll viewAllStackRank btn btn-small btn-primary btn-inverse"><cms:contentText key="VIEW_ALL_STACK_RANK" code="ssi_contest.creator" /></button>
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
                            <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
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
