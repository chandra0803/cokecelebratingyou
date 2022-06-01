<%@ include file="/include/taglib.jspf"%>

<%--{{! 2x2 module }}
<div class="narrowModule">
    <div class="row-fluid activityGoal objectiveData">
        <div class="span4">
            <h5><cms:contentText key="GOAL" code="ssi_contest.participant" /></h5>
        </div>
        <div class="span8">
            <span>{{goal}}</span>
        </div>
    </div>
    <div class="row-fluid activityProgress objectiveData">
        <div class="span4">
            {{#eq status "finalize_results"}}
            <h5><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></h5>
            {{else}}
            <h5><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></h5>
            {{/eq}}
        </div>
        <div class="span8">
            <span>{{progress}}</span>
        </div>
    </div>
</div>

{{! 4x2 module }}
<div class="shortAndWideModule">
    <div class="row-fluid">
        <div class="span12">
            <p class="activityDescription">{{activityDescription}}</p>
        </div>
    </div>
    <div class="row-fluid objectiveData">
        <div class="span6 activityGoal dataSection">
            <h5>{{goal}}</h5>
            {{#if isCreator}}
            <span><cms:contentText key="GOAL" code="ssi_contest.creator" /></span>
            {{else}}
            <span><cms:contentText key="TEAM_GOAL" code="ssi_contest.creator" /></span>
            {{/if}}
        </div>
        <div class="span6 activityProgress dataSection">
            <h5>{{progress}}</h5>
            {{#eq status "finalize_results"}}
                <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></span>
            {{else}}
                <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></span>
            {{/eq}}
        </div>
    </div>
</div>--%>

{{! 4x4 module }}
<div class="wideModule">
    <div class="row-fluid objectiveData contestDataWrapper">
        <div class="span6 activityGoal dataSection">
            <h5>{{goal}}</h5>
            {{#if isCreator}}
            <span><cms:contentText key="GOAL" code="ssi_contest.creator" /></span>
            {{else}}
            <span><cms:contentText key="TEAM_GOAL" code="ssi_contest.creator" /></span>
            {{/if}}
        </div>
        <div class="span6 activityProgress dataSection">
            <h5>{{progress}}</h5>
            <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></span>
        </div>
    </div>
    <div class="row-fluid barChartContainer">
        <div class="span12">
            <div class="row-fluid">
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
                                <span class="label"><cms:contentText key='BONUS' code='ssi_contest.participant' /></span>
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
    </div>
</div>
