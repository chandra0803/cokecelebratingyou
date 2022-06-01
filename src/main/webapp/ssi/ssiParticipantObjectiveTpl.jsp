<%@ include file="/include/taglib.jspf"%>
{{! NOTE: this src needs to point to the javascript lib}}
<div class="raphael" data-src="../assets/libs/raphael-min.js"></div>

<div class="row-fluid">
	<div class="span12 contestData">
		<div class="row-fluid">
			<!-- chart -->
			<div class="span6 paxProgressChart">
				<div class="chartPercentHolder chartHolder" data-model-id="{{id}}">
					<div class="chartPercentProgress">
						<strong>{{percentProgress}}<sup><cms:contentText key="PERCENTAGE" code="ssi_contest.participant" /></sup></strong>
					</div>
				</div>
			</div>
			<!-- /chart -->

			<!-- status -->
			<div class="span6 paxObjectiveInfo objDataSection">
				<dl class="objDataList dl-horizontal">
					<dt class="objTitle">
						<span><cms:contentText key="YOUR_OBJECTIVE" code="ssi_contest.participant" /></span>
					</dt>
					<dd class="objData">
						<strong>{{goal}}</strong>
					</dd>

					<dt class="objTitle">
						{{#eq daysToEnd 0}}
							<span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.participant" /></span>
						{{else}}
							<span><cms:contentText key="ACTIVITY" code="ssi_contest.participant" /></span>
						{{/eq}}
					</dt>
					<dd class="objData">
						<strong>{{progress}}</strong>
					</dd>

				{{#ueq daysToEnd 0}}
					<dt class="objTitle">
						<span><cms:contentText key="TO_GO" code="ssi_contest.participant" /></span>
					</dt>
					<dd class="objData">
						<strong>{{remaining}}</strong>
					</dd>
				{{/ueq}}

				{{#if includeBonus}}
					{{#if objectiveAchieved}}
						<dt class="objTitle">
							<span><cms:contentText key="IN_BONUS" code="ssi_contest.participant" /></span>
						</dt>
						<dd class="objData">
							<strong>{{bonusActivity}}</strong>
						</dd>
					{{/if}}
				{{/if}}
				</dl>
			</div>
			<!-- /status -->
		</div>
		<div class="row-fluid">
			<!-- reward details -->
			<div class="span12 paxObjectiveInfo paxObjectiveEarnInfo">
				{{#if objectiveAchieved}}
					<div class="objectiveAchievedWrap msg-achieved">
						<span><i class="icon-verification"></i> <cms:contentText key="OBJECTIVE_ACHIEVED" code="ssi_contest.participant" /></span>
						<strong>{{#eq payoutType "points"}}{{payout}} {{#eq payout "1"}}<cms:contentText key="POINT" code="ssi_contest.participant" />{{else}}<cms:contentText key="POINTS" code="ssi_contest.participant" />{{/eq}}{{else}} {{payoutDescription}}{{/eq}}</strong>
					</div>
					{{#if includeBonus}}
						<span class="msg-achieved">
							<i class="icon-star-circle"></i>
						    <span><cms:contentText key="BONUS" code="ssi_contest.participant" /></span>
						    <strong>{{bonusEarned}} {{#eq bonusEarned "1"}}<cms:contentText key="POINT" code="ssi_contest.participant" />{{else}}<cms:contentText key="POINTS" code="ssi_contest.participant" />{{/eq}}</strong>
					    </span>
					{{/if}}
				{{else}}
					{{#eq status "finalize_results"}}
						<span class="msg-notAchieved">
							<i class="icon-cancel-circle"></i> <cms:contentText key="OBJECTIVE_NOT_ACHIEVED" code="ssi_contest.participant" />
						</span>
					{{else}}
						<span>
							<cms:contentText key="REACH_YOUR_OBJECTIVE" code="ssi_contest.participant" /> <cms:contentText key="AND" code="ssi_contest.participant" />
							<cms:contentText key="EARN" code="ssi_contest.participant" /> <strong>{{#eq payoutType "points"}}{{payout}} {{#eq payout "1"}}<cms:contentText key="POINT" code="ssi_contest.participant" />{{else}}<cms:contentText key="POINTS" code="ssi_contest.participant" />{{/eq}}{{else}} {{payoutDescription}}{{/eq}}</strong>
							{{#if badge.img}}
							    <cms:contentText key="AND_THIS_BADGE" code="ssi_contest.participant" />
							{{/if}}
						</span>
					{{/eq}}
				{{/if}}
				{{#ueq status "finalize_results"}}
					{{#if badge.img}}
						<img src="{{badge.img}}" alt="{{badge.name}}" class="badgeImg{{#if objectiveAchieved}} goalAchieved{{/if}}">
					{{/if}}
				{{/ueq}}
			</div>
			<!-- /reward details -->
		</div>

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
						<p><cms:contentText key="OBJECTIVE_ELIGIBILITY" code="ssi_contest.participant" />:</p>
						<strong class="forEvery"><cms:contentText key="BONUS_FOR_EVERY" code="ssi_contest.participant" /> {{bonusForEvery}} <span>&bull;</span> <cms:contentText key="AMOUNT_BONUS_EARN" code="ssi_contest.participant" /> {{bonusPayout}} {{#eq bonusPayout "1"}}Point{{else}}<cms:contentText key="POINTS" code="ssi_contest.participant" />{{/eq}}
						<span>&bull;</span> <cms:contentText key="UPTO" code="ssi_contest.participant" /> {{formatNumber objectiveBonusCap}} <cms:contentText key="POINTS" code="ssi_contest.participant" />
						</strong>
					</div>
				{{/if}}
				</div>
			</div>
		{{/or}}
	</div>
</div>
