<%@ include file="/include/taglib.jspf"%>
<div class="raphael" data-src="assets/libs/raphael-min.js"></div>

<div class="row-fluid">
    <div class="span12">
        <p class="activityDescription">{{activityDescription}}</p>
    </div>
</div>

{{debug}}

<%--{{! 2x2 module }}
<div class="narrowModule">
    {{#each contestLevels}}
        {{#if isCurrentLevel}}
            <div class="row-fluid">
                <div class="span8 chartWrapper">
                    <div class="chartHolder participantChartWrap" data-model-id="{{id}}">
                        <div class="chartDataWrapper">
                        <div class="chartData">
                            <div class="levelProgress">
                                <span>{{progress}}</span>
                            </div>
                            <div class="levelGoal">
                                <span>{{goal}}</span>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
                <div class="span4">
                    <span class="levelTitle"><cms:contentText key="LEVEL" code="ssi_contest.participant" /></span>
                    <span class="levelAchieved">{{name}}</span>
                </div>
            </div>
            {{#ueq ../status "finalize_results"}}
                <div class="row-fluid">
                    <div class="span12 text-center">
                        {{#and isLastLevel isCompleted}}
                        <span class="levelRemaining {{#and ../includeBonus ../bonusEarned}}hidden{{/and}}"><cms:contentText key="HIGHEST_LVL_ACHIEVED" code="ssi_contest.participant" /></span>
                        {{else}}
                        <span class="levelRemaining">{{remaining}} <span><cms:contentText key="UNTIL_NEXT_LEVEL" code="ssi_contest.participant" /></span></span>
                        {{/and}}
                    </div>
                </div>
            {{/ueq}}
        {{/if}}
    {{/each}}
</div>--%>

{{! 4x4 and 4x2 module }}
<div class="shortAndWideModule wideModule">
    <div class="levelChartWrapper numLevels{{contestLevels.length}}">
    <div class="levelChartTrack">
        <div class="row-fluid levelCircles">
            {{#each contestLevels}}
                {{#if isCurrentLevel}}
                    <div class="span3 levelCol currentLevelCol">
                        <div class="chartHolder participantChartWrap" data-model-id="{{id}}">
                        <div class="chartDataWrapper">
                            <div class="chartData">
                                <div class="levelProgress">
                                    <span>{{progress}}</span>
                                </div>
                                <div class="levelGoal">
                                    <span>{{goal}}</span>
                                </div>
                            </div>
                            </div>
                        </div>
                        <div class="backgroundLinkBar"></div>
                    </div>
                {{else}}
                    {{#if isCompleted}}
                        <div class="span2 levelCol levelCompleted">
                    {{else}}
                        <div class="span2 levelCol levelNotCompleted">
                    {{/if}}
                            <div class="circleData">
                                <i class="icon-more-circle notcomplete"></i>
                                <i class="icon-cancel-circle notcomplete done"></i>
                                <i class="icon-check-circle complete"></i>
                            </div>
                            <div class="backgroundLinkBar"></div>
                        </div>
                {{/if}}
            {{/each}}
        </div>
        <div class="row-fluid levelMeta">
            {{#each contestLevels}}
                {{#if isCurrentLevel}}
                    <div class="span3 levelCol currentLevelCol">
                        <strong class="levelName currentLevel"><cms:contentText key="LEVEL" code="ssi_contest.participant" /> <span>{{name}}</span></strong>
                    </div>
                {{else}}
                    <div class="span2 levelCol">
                        <strong class="levelName {{#unless isCompleted}}notComplete{{/unless}}"><cms:contentText key="LEVEL" code="ssi_contest.participant" /> <span>{{name}}</span></strong>
                    </div>
                {{/if}}
            {{/each}}
        </div>
    </div><!-- /.levelChartTrack -->
    <div class="levelChartFades"></div>
    </div><!-- /.levelChartWrapper -->
    {{#ueq daysToEnd 0}}
        {{#each contestLevels}}
            {{#if isCurrentLevel}}
                <div class="row-fluid levelRemaining">
                    <div class="span12 text-center">
                        {{#and isLastLevel isCompleted}}
                        <span><cms:contentText key="HIGHEST_LVL_ACHIEVED" code="ssi_contest.participant" /></span>
                        {{else}}
                        <span><strong>{{remaining}}</strong> <cms:contentText key="UNTIL_NEXT_LEVEL" code="ssi_contest.participant" /></span>
                        {{/and}}
                    </div>
                </div>
            {{/if}}
        {{/each}}
    {{/ueq}}

</div>

{{#ueq status "finalize_results"}}
    {{#and includeBonus bonusActivity}}
    <div class="bonusLabel"><i class="icon-star-circle"></i> {{bonusActivity}} <cms:contentText key="IN_BONUS" code="ssi_contest.participant" /></div>
    {{/and}}
{{/ueq}}
