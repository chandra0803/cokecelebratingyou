<%@ include file="/include/taglib.jspf"%>
{{! NOTE: isCreator variable is set by JavaScript }}

{{#each activities}}
	<div class="row-fluid contestData card">
		<div class="span12">
			<div class="row-fluid paxActivityData">
				<h3 class="activityDescription">{{name}}</h3>

				<div class="paxActivityInfo">
					<p>
						<cms:contentText key="BONUS_FOR_EVERY" code="ssi_contest.participant"/> <strong class="forEvery">{{forEvery}}</strong>
						<cms:contentText key="EARN" code="ssi_contest.participant"/> <strong class="forEvery">{{willEarn}} {{#eq extraJSON.payoutType "other"}}{{payoutDescription}}{{else}}<cms:contentText key="POINTS_LABEL" code="ssi_contest.creator"/>{{/eq}}</strong>
					</p>
					<p class="minQualifier"><i class="icon icon-info"></i> <cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.participant"/>: <strong>{{minQualifier}}</strong></p>
				</div>

				<div class="dataSectionWrap">
					<div class="dataSection activityPayout">
						{{#eq extraJSON.payoutType "points"}}
							<h4>{{payout}}</h4>
							<p>{{#eq ../status "finalize_results"}}<cms:contentText key="FINAL_POINTS" code="ssi_contest.creator" /> {{else}}<cms:contentText key="POTENTIAL_POINTS" code="ssi_contest.creator" />{{/eq}}</p>
						{{/eq}}
						{{#eq extraJSON.payoutType "other"}}
							<h4 class="payoutGift">
								<i class="icon-gift"></i>
								{{#ueq payout "0"}}
								<div class="giftCount">
								{{payout}}
								</div>
								{{/ueq}}
							</h4>
							<p>{{#eq ../status "finalize_results"}}<cms:contentText key="AWARD" code="ssi_contest.participant"/> {{else}} <cms:contentText key="POTENTIAL_AWARD" code="ssi_contest.participant"/>{{/eq}}</p>
						{{/eq}}
					</div>

					<div class="dataSection activityProgress">
						<h4>{{submitted}}</h4>
						<p><cms:contentText key="ACTIVITY" code="ssi_contest.participant"/></p>
					</div>
				</div><!-- /.dataSectionWrap -->
			</div><!-- /.paxActivityData -->

			{{#if extraJSON.includeStackRanking}}
			{{#with stackRank}}
				<div class="paxBottomData">
					<div class="row-fluid">
						<div class="rankData">
							<h4 class="sectionTitle"><cms:contentText key="STACK_RANK" code="ssi_contest.participant"/></h4>
							<span class="avatar">
							    {{#if thumbnailUrl}}
							        <img alt="{{firstName}} {{lastName}}" class="avatar" src="{{thumbnailUrl}}" />
							    <!-- we don't appear to have name data in the stackRank object at this time -->
							    {{else}}
							        <!-- <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span> -->
							    {{/if}}
							</span>
							<div class="rankDataMeta">
								<strong class="yourRank"> \#{{rank}} <cms:contentText key="OF" code="ssi_contest.participant"/> {{participantsCount}}</strong>
							</div>
							<a href="#" data-stackrank-activityid="{{../activityId}}" class="viewAll viewAllStackRank btn btn-small btn-primary btn-inverse"><cms:contentText key="VIEW_STACK_RANK" code="ssi_contest.participant"/></a>
						</div>
					</div>
				</div>
			{{/with}}
			{{/if}}
		</div>
	</div>
{{/each}}

<div class="ssiMinQualifierPopover" style="display: none">
<cms:contentText key="MINIMUM_QUALIFIER_INFO" code="ssi_contest.participant"/>
</div>
