<%@ include file="/include/taglib.jspf"%>
<div class="stackRankToggleWrap stackRankShowMore" data-classtoggle="stackRankShowLess stackRankShowMore">
    {{#if includeStackRanking}}
    {{#if isProgressLoaded}}
    <div class="row-fluid">
        <div class="span12 text-right">
            <a href="#" class="stackRankToggle showLess"><cms:contentText key="HIDE_STACK_RANK" code="ssi_contest.creator" /> <i class="icon-eye-off"></i></a>
            <a href="#" class="stackRankToggle showMore"><cms:contentText key="SHOW_STACK_RANK" code="ssi_contest.creator" /> <i class="icon-eye"></i></a>
        </div>
    </div>
    {{/if}}
    {{/if}}

    <div class="activitiesWrap">
    {{#each activities}}
    <div class="activity admin card">
        <h3 class="sectionHeader activityDescription">{{activityDescription}}</h3>

        <div class="activityWrap contestActivityInfo">
            <p>
                <cms:contentText key="FOR_EVERY" code="ssi_contest.payout_dtgt" /> <strong class="forEvery">{{forEvery}}</strong>
                <cms:contentText key="LOWER_CASE_EARN" code="ssi_contest.payout_dtgt" /> <strong class="forEvery">{{#eq extraJSON.payoutType "other"}}{{payoutDescription}}{{/eq}}{{#eq extraJSON.payoutType "points"}}{{willEarn}} <cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" />{{/eq}}</strong>
            </p>

            <p class="maximumPayout"><a href="#" class="showDescription" data-toggle="tooltip" title="" data-original-title='<cms:contentText code="ssi_contest.payout_dtgt" key="MAXIMUM_ACTIVITY_COST_ICON_CONTENT" />'><i class="icon-info"></i></a> <cms:contentText key="MAXIMUM_ACTIVITY_COST" code="ssi_contest.payout_dtgt" />
                {{#eq extraJSON.payoutType "points"}}<strong>{{payoutCap}} <cms:contentText key="POINTS" code="ssi_contest.payout_dtgt" /></strong>{{/eq}}
                {{! todo }}
                {{#eq extraJSON.payoutType "other"}}<strong>{{payoutCap}}</strong>{{/eq}}
            </p>

            <p class="minQualifier"><a href="#" class="showDescription" data-toggle="tooltip" title="" data-original-title='<cms:contentText code="ssi_contest.payout_dtgt" key="MINIMUM_QUALIFIER_ICON_CONTENT" />'><i class="icon-info"></i></a> <cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.payout_dtgt" /> <strong>{{minQualifier}}</strong></p>
        </div><!-- /.activityWrap.contestActivityInfo -->

    <div class="row-fluid">
        <div class='span12 contestData dataSectionWrap adminView {{#unless ../isCreator}}ssiManagerView{{/unless}} {{#eq ../status "finalize_results"}}complete{{/eq}} {{#eq extraJSON.payoutType "points"}}payoutTypePoints{{/eq}}'>

            <div class="dataSection activityGoal">
                <h4>{{goal}}</h4>
                {{#if ../isCreator}}
                    <span><cms:contentText key="GOAL" code="ssi_contest.payout_dtgt" /></span>
                {{else}}
                    <span><cms:contentText key="TEAM_GOAL" code="ssi_contest.creator" /></span>
                {{/if}}
            </div>

            <div class="dataSection activityProgress">
                <h4>{{progress}}</h4>
                {{#eq ../status "finalize_results"}}
                    <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></span>
                {{else}}
                    <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></span>
                {{/eq}}
            </div>

            {{#ueq ../status "finalize_results"}}
            <div class="dataSection activityRemaining">
                {{#unless goalAchieved}}
                    <h4>{{remaining}}</h4>
                    <span><cms:contentText key="TO_GO" code="ssi_contest.creator" /></span>
                {{else}}
                    <h4 class="activityGoalAchieved"><i class="icon-verification"></i></h4>
                    <span><cms:contentText key="GOAL_ACHIEVED" code="ssi_contest.creator" /></span>
                {{/unless}}
            </div>
            {{/ueq}}

            <div class="dataSection activityGoalPercent">
                <h4>{{percentProgress}}</h4>
                <span>% <cms:contentText key="OF_GOAL" code="ssi_contest.payout_dtgt" /></span>
            </div>

            {{#if ../isCreator}}
            <div class="dataSection activityPayout">
                {{#eq extraJSON.payoutType "points"}}
                    <h4>{{payoutProgress}}<sup>({{payoutPercentProgress}}%)</sup></h4>
                    {{#eq ../status "finalize_results"}}
                        <span><cms:contentText key="POINTS_ISSUED" code="ssi_contest.creator" /></span>
                    {{else}}
                        <span><cms:contentText key="POTENTIAL_POINTS" code="ssi_contest.creator" /></span>
                    {{/eq}}
                {{/eq}}

                {{#eq extraJSON.payoutType "other"}}
                    <h4 class="payoutGift">
                        <i class="icon-gift"></i>
                        {{#ueq payout "0"}}
                        <div class="giftCount">
                            <span>{{payout}}</span>
                        </div>
                        {{/ueq}}
                    </h4>
                    {{#eq ../status "finalize_results"}}
                        <span><cms:contentText key="PAYOUT_VALUE_ISSUED" code="ssi_contest.creator" /></span>
                    {{else}}
                        <span><cms:contentText key="POTENTIAL_PAYOUT" code="ssi_contest.creator" /></span>
                    {{/eq}}
                {{/eq}}
            </div>

            {{else}}

                {{#eq extraJSON.payoutType "other"}}
                <div class="dataSection activityPayout">
                    <h4 class="payoutGift">
                        <i class="icon-gift"></i>
                        {{#ueq payout "0"}}
                        <div class="giftCount">
                            <span>{{payout}}</span>
                        </div>
                        {{/ueq}}
                    </h4>
                    {{#eq ../status "finalize_results"}}
                        <span><cms:contentText key="PAYOUT_VALUE_ISSUED" code="ssi_contest.creator" /></span>
                    {{else}}
                        <span><cms:contentText key="POTENTIAL_PAYOUT" code="ssi_contest.creator" /></span>
                    {{/eq}}
                </div>
                {{/eq}}
            {{/if}}
        </div>
    </div><!-- /.row-fluid -->

    {{#if extraJSON.includeStackRanking}}
    {{#if extraJSON.isProgressLoaded}}
    <div class="row-fluid">
        <div class="span12 stackRankWrap stackRankBoard highlightedBoard">
            <div class="row-fluid">
                <div class="span6">
                    <h4 class="sectionTitle"><cms:contentText key="STACK_RANK" code="ssi_contest.creator" /></h4>
                </div>
                <div class="span6 text-right">
                    <button data-stackrank-activityid="{{activityId}}" type="button" class="viewAll viewAllStackRank btn btn-small btn-primary btn-inverse"><cms:contentText key="VIEW_ALL_STACK_RANK" code="ssi_contest.creator" /></button>
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
        </div><!-- /.stackRankWrap.stackRankBoard.highlightedBoard -->
    </div><!-- /.row-fluid -->
    {{/if}}
    {{/if}}
    </div><!-- /.activity.admin.card -->
    {{/each}}
    </div><!-- /.activitiesWrap -->
</div><!-- /.stackRankToggleWrap -->
