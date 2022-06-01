<%@ include file="/include/taglib.jspf"%>
<div class="raphael" data-src="assets/libs/raphael-min.js"></div>

<div class="row-fluid">
    <div class="span12">
        <p class="activityDescription">{{activityDescription}}</p>
    </div>
</div>


{{! 4x4 module }}
<div class="wideModule">
    {{#if isCreator}}
        <div class="row-fluid">
            {{#eq daysToEnd 0}}
                <div class="span6 chartWrapper">
                    <div class="chartHolder chartPercentHolder" data-model-id="{{id}}">
                        <div class="chartPercentProgress">
                            <strong>{{percentProgress}}<sup><cms:contentText key="PERCENTAGE" code="ssi_contest.participant" /></sup></strong>
                        </div>
                    </div>

                </div>
                <div class="span6 contestDataWrapper">
                    <div class="row-fluid">
                        <div class="span12 objectiveProgress">
                            {{#if hasMultipleObjectives}}
                                <span>{{achievedParticipantsCount}}</span>
                            {{else}}
                                <span>{{progress}}</span>
                            {{/if}}
                        </div>
                        <div class="span12 objectiveGoal">
                            {{#if hasMultipleObjectives}}
                                <span>{{participantsCount}}</span>
                            {{else}}
                                <span>{{goal}}</span>
                            {{/if}}
                        </div>
                    </div>
					{{#if hasMultipleObjectives}}
						{{#lt percentProgress 100}}
							<div class="span8 hasMultipleObjAchieved"> <cms:contentText key="ACHIEVED" code="ssi_contest.participant" /></div>
						{{/lt}}
					{{/if}}                    
                </div>
            {{else}}
                <div class="span6 chartWrapper">
                    <div class="chartHolder chartPercentHolder" data-model-id="{{id}}">
                        <div class="chartPercentProgress">
                            <strong>{{percentProgress}}<sup><cms:contentText key="PERCENTAGE" code="ssi_contest.participant" /></sup></strong>
                        </div>
                    </div>

                </div>
                <div class="span6 contestDataWrapper">
                    <div class="row-fluid">
						<div class="span4">
							<div class="span12 objectiveProgress">
								{{#if hasMultipleObjectives}}
									<span>{{achievedParticipantsCount}} </span>
								{{else}}
									<span>{{progress}}</span>
								{{/if}}
							</div>
							<div class="span12 objectiveGoal">
								{{#if hasMultipleObjectives}}
									<span>{{participantsCount}}</span>
								{{else}}
									<span>{{goal}}</span>
								{{/if}}
							</div>
						</div>
						{{#if hasMultipleObjectives}}
							{{#lt percentProgress 100}}
								<div class="span8 hasMultipleObjAchieved"> <cms:contentText key="ACHIEVED" code="ssi_contest.participant" /></div>
							{{/lt}}
						{{/if}}
                    </div>
                </div>
                <div class="span12 objectiveMeta">
                    <div class="row-fluid">
                        {{#if hasMultipleObjectives}}
                        	{{#gte percentProgress 100}}
                        		<div class="span12">
                           			<span class="multiGoalAchieved"><i class="icon-verification"></i> <cms:contentText key="ACHIEVED" code="ssi_contest.participant" /></span>
                        		</div>
                        	{{/gte}}
                        {{else}}
                            {{#unless goalAchieved}}
                            <div class="span12">
                                <span class="multiGoalToGo"><strong>{{remaining}}</strong> <cms:contentText key="TO_GO" code="ssi_contest.participant" /></span>
                            </div>
                            {{else}}
                            <div class="span12">
                                <span class="multiGoalAchieved"><i class="icon-verification"></i> <cms:contentText key="GOAL_ACHIEVED" code="ssi_contest.participant" /></span>
                            </div>
                            {{/unless}}
                        {{/if}}
                    </div>
                </div>
            {{/eq}}
        </div>
    {{else}}
        <div class="row-fluid">
            <div class="span6 chartWrapper">
                <div class="chartHolder chartPercentHolder" data-model-id="{{id}}">
                    <div class="chartPercentProgress">
                        <strong>{{percentProgress}}<sup><cms:contentText key="PERCENTAGE" code="ssi_contest.participant" /></sup></strong>
                    </div>
                </div>

            </div>
            <div class="span6 contestDataWrapper">
                <div class="row-fluid">
                    <div class="span12 objectiveProgress">
                        <span>{{progress}}</span>
                    </div>
                    <div class="span12 objectiveGoal">
                        <span>{{goal}}</span>
                    </div>
                </div>
            </div>
            <div class="span12">
                {{#eq daysToEnd 0}}
                    <div class="row-fluid objectiveMeta">
                        {{#if objectiveAchieved}}
                            <div class="span12">
                                <span class="msg-achieved"><i class="icon-verification"></i> <cms:contentText key="OBJECTIVE_ACHIEVED" code="ssi_contest.participant" /></span>
                            </div>
                        {{/if}}
                        {{#if bonusEligible}}
                            <div class="span12">
                                <span class="msg-bonus"><i class="icon-star-circle"></i> <cms:contentText key="BONUS_ELIGIBLE" code="ssi_contest.participant" /></span>
                            </div>
                        {{/if}}
                    </div>
                {{else}}
                    <div class="row-fluid objectiveMeta">
                        {{#unless objectiveAchieved}}
                            <div class="span12">
                                <span class="multiGoalToGo"><strong>{{remaining}}</strong> <cms:contentText key="TO_GO" code="ssi_contest.participant" /></span>
                            </div>
                        {{else}}
                            <div class="span12">
                                <span class="msg-achieved"><i class="icon-verification"></i> <cms:contentText key="OBJECTIVE_ACHIEVED" code="ssi_contest.participant" /></span>
                            </div>
                            {{#if bonusEligible}}
                                <div class="span12">
                                    <span class="msg-bonus"><i class="icon-star-circle"></i> <cms:contentText key="BONUS_ELIGIBLE" code="ssi_contest.participant" /></span>
                                </div>
                            {{/if}}
                        {{/unless}}
                    </div>
                {{/eq}}
            </div>
        </div>
    {{/if}}
</div>
