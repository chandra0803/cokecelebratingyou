<%@ include file="/include/taglib.jspf"%>
{{! NOTE: this src needs to point to the javascript lib}}
<div class="raphael" data-src="../assets/libs/raphael-min.js"></div>

{{! NOTE: these variables are set via JavaScript:
		isFirstLevel, isLastLevel, classes, lastLevel }}
<div class="row-fluid">
    <div class="span12 levelChartWrapper">
        <table class="levelData">
            <tr class="levels">
                <td class="span2"></td>
                {{#each contestLevels}}
                <td class="levelCol span2 {{classes}}">
                    <div class="levelHeader">
                        <span class="levelName">
                            <span><cms:contentText key="LEVEL" code="ssi_contest.participant" /></span>
                            <strong>{{name}}</strong>
                        </span>

                    {{#if isCompleted}}
                        <div class="levelStatus levelCompleted">
                    {{else}}
                        <div class="levelStatus levelNotCompleted">
                    {{/if}}
                            <div class="backgroundLinkBar"></div>

                            {{#if isCurrentLevel}}
                            <div class="levelStatusInProgress">
                                <div class="chartHolder" data-chart-type="circle" data-model-id="{{id}}">
                                <div class="chartDataWrapper">
                                    <div class="chartData">
                                        <div class="chartDataInner">
                                            <div class="levelProgress">
                                                <span>{{progress}}</span>
                                            </div>
                                            <div class="levelGoal">
                                                <span>{{goalAmount}}</span>
                                            </div>
                                        </div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                            <div class="levelStatusRemaining">
                                {{#ueq ../status "finalize_results"}}
                                {{#if ../includeBonus}}
                                {{#if isCompleted}}
                                    <strong class="offset">{{../bonusActivity}} <cms:contentText key="IN_BONUS" code="ssi_contest.participant" /></strong>
                                {{else}}
                                    <strong><span>{{remaining}}</span> <cms:contentText key="TO_GO_LOWER" code="ssi_contest.participant" /></strong>
                                {{/if}}
                                {{else}}
                                {{#if isCompleted}}
                                {{#if isLastLevel}}
                                    <strong class="offset"><cms:contentText key="HIGHEST_LVL_ACHIEVED" code="ssi_contest.participant" /></strong>
                                {{else}}
                                    <strong><cms:contentText key="LEVEL" code="ssi_contest.participant" /> {{name}} <cms:contentText key="ACHIEVED" code="ssi_contest.participant" /></strong>
                                {{/if}}
                                {{else}}
                                    <strong><span>{{remaining}}</span> <cms:contentText key="TO_GO_LOWER" code="ssi_contest.participant" /></strong>
                                {{/if}}
                                {{/if}}
                                {{else}}
                                    <strong class="offset"><cms:contentText key="LEVEL" code="ssi_contest.participant" /> {{name}} <cms:contentText key="ACHIEVED" code="ssi_contest.participant" /></strong>
                                {{/ueq}}
                            </div>
                            {{else}}
                            <div class="circleData">
                                <i class="icon-more-circle notcomplete"></i>
                                <i class="icon-cancel-circle notcomplete done"></i>
                                <i class="icon-check-circle complete"></i>
                            </div>
                            {{/if}}


                            <!-- {{#eq ../status "finalize_results"}}

                               {{#if isCurrentLevel}}
                                  <div class="levelStatusInProgress">
                                     <div class="chartHolder" data-chart-type="circle" data-model-id="{{id}}">
                                        <div class="chartDataWrapper">
                                            <div class="chartData">
                                                <div class="chartDataInner">
                                                   <div class="levelProgress">
                                                      <span>{{progress}}</span>
                                                   </div>
                                                   <div class="levelGoal">
                                                      <span>{{goalAmount}}</span>
                                                   </div>
                                                </div>
                                           </div>
                                        </div>
                                     </div>
                                  </div>
                                  <div class="levelStatusRemaining">
                                     <strong class="offset">Level {{name}} Achieved</strong>
                                  </div>
                               {{/if}}
                            {{/eq}} -->

                        </div> {{! / .levelStatus }}

                    </div>
                </td>
                    {{! #if isLastLevel}}
                        {{! isbonus }}
                    {{! /if}}
                {{/each}}
            </tr>

            <tr class="goalDetails">
                <td class="labels">
                    {{#if baseline}}
                        <strong><cms:contentText key="BASE_LINE" code="ssi_contest.participant" /></strong>
                        <strong><cms:contentText key="GOAL_INIT_CAP" code="ssi_contest.participant" /></strong>
                    {{/if}}
                    <strong><cms:contentText key="GOAL_AMOUNT" code="ssi_contest.participant" /></strong>
                </td>
                {{#each contestLevels}}
                    <td class="text-center {{#if isCurrentLevel}}currentLevelValue{{/if}}">
                        {{#if ../baseline}}
                            <span>{{../baseline}}</span>
                            <span>{{goalPercent}}</span>
                        {{/if}}
                        <span>{{goalAmount}}</span>
                    </td>
                {{/each}}
            </tr>

            <tr class="payoutDetails">
                <td class="labels">
                    <strong><cms:contentText key="PAYOUT" code="ssi_contest.participant" /></strong>
                    {{#if includeBonus}}
                        <strong>Bonus</strong>
                    {{/if}}
                </td>
                {{#each contestLevels}}
                    <td class="text-center {{#if isCurrentLevel}}currentLevelValue{{/if}}">
                        {{#eq ../payoutType "points"}}
                            <span>{{payout}} <cms:contentText key="POINTS_UPPERCASE" code="ssi_contest.participant" /></span>
                            {{#if ../includeBonus}}
                                {{#if isLastLevel}}
                                    {{#if ../bonusEarned}}
                            <span>{{../bonusEarned}} <cms:contentText key="POINTS_UPPERCASE" code="ssi_contest.participant" /></span>
                                    {{else}}
                            <span>--</span>
                                    {{/if}}
                                {{else}}
                            <span>&nbsp;</span>
                                {{/if}}
                            {{/if}}
                        {{/eq}}
                        {{#eq ../payoutType "other"}}
                            <span>{{payout}}</span>
                        {{/eq}}
                    </td>
                {{/each}}
            </tr>
        </table>

        {{#or includeStackRanking includeBonus}}
        <div class="paxBottomData">
            <div class="row-fluid">
                {{#if includeStackRanking}}
                    <div class="{{#if includeBonus}}span4{{/if}} rankData">
                        <h4 class="sectionTitle"><cms:contentText key="STACK_RANK" code="ssi_contest.participant" /></h4>
                        <span class="avatar">
                            {{#if stackRank.thumbnailUrl}}
                                <img alt="{{stackRank.firstName}} {{stackRank.lastName}}" class="avatar" src="{{stackRank.thumbnailUrl}}" />
                            <%-- we don't appear to have name data in the stackRank object at this time --%>
                            {{else}}
                                <!-- <span class="avatar-initials">{{trimString stackRank.firstName 0 1}}{{trimString stackRank.lastName 0 1}}</span> -->
                            {{/if}}
                        </span>
                        <div class="rankDataMeta">
                            <strong class="yourRank">\#{{stackRank.rank}} <cms:contentText key="OF" code="ssi_contest.participant" /> {{stackRank.participantsCount}}</strong>
                        </div>
                        <a href="#" data-stackrank-activityid="{{../activityId}}" class="viewAll viewAllStackRank btn btn-small btn-primary btn-inverse"><cms:contentText key="VIEW_STACK_RANK" code="ssi_contest.participant" /></a>
                    </div>
                {{/if}}
                {{#if includeBonus}}
                    <div class="{{#if includeStackRanking}}span8{{/if}} bonusData">
                        <h4 class="sectionTitle"><cms:contentText key="BONUS" code="ssi_contest.participant" /></h4>
                        <p><cms:contentTemplateText code="ssi_contest.participant" key="BONUS_ELIGIBILITY" args="{{contestLevels.length}}"/>:</p>
                        <strong class="forEvery"><cms:contentText key="BONUS_FOR_EVERY" code="ssi_contest.participant" /> {{bonusForEvery}} <span>&bull;</span> <cms:contentText key="AMOUNT_BONUS_EARN" code="ssi_contest.participant" /> {{bonusPayout}} {{#eq bonusPayout "1"}}<cms:contentText key="POINT" code="ssi_contest.participant" />{{else}}<cms:contentText key="POINTS" code="ssi_contest.participant" />{{/eq}}
                        </strong>
                    </div>
                {{/if}}
            </div>
        </div>
        {{/or}}
    </div>
</div>
