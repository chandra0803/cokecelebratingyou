<%@ include file="/include/taglib.jspf"%>

{{! NOTE: extraJSON is set in the javascript }}

{{#ueq daysToEnd 0}}
    {{#if payouts}}
    <div class="row-fluid payoutsWrap payoutsShowLess" data-classtoggle="payoutsShowLess payoutsShowMore">
        <div class="span12 payoutsRow">
            {{#unless extraJSON.hideShowAllPayouts}}
            <div class="bottomPayoutInfo text-right">
                <a href="#" class="payoutsToggle showMore"><cms:contentText key="VIEW_ALL_PAYOUTS" code="ssi_contest.participant" /> <i class="icon-eye"></i></a>
                <a href="#" class="payoutsToggle showLess"><cms:contentText key="VIEW_LESS" code="ssi_contest.participant" /> <i class="icon-eye-off"></i></a>
            </div>
            {{/unless}}

            <h3 class="sectionTitle"><cms:contentText key="WHAT_YOU_CAN_EARN" code="ssi_contest.participant" /></h3>

            <div class="payoutsList">
            {{#each payouts}}
                <div class="payout">
                    <span class="payoutRank">{{rank}}</span>
                    <div class="payoutInfo">
                        {{#if badge.img}}
                            <img src="${pageContext.request.contextPath}{{badge.img}}" alt="{{badge.name}}" class="payoutBadge" />
                        {{/if}}
                        {{#if payout}}
                            <span class="payoutAmount{{#unless badge.img}} no-badge{{/unless}}">
                            {{payout}}
                                {{#eq ../payoutType "points"}}
                                    <cms:contentText key="POINTS" code="ssi_contest.participant" />
                                {{/eq}}
                            </span>
                        {{else}}
                            <span class="badgeName">{{badge.name}}</span>
                        {{/if}}
                    </div>
                </div>
            {{/each}}
            </div>
        </div>
    </div>

    {{/if}}
{{/ueq}}

<!-- Need splitter styles nested inside this wrapper -->
<div class="container-splitter with-splitter-styles">

{{#if isProgressLoaded}}
    {{#if stackRank.rank}}
    <div class="row-fluid">
        <div class="span12 paxRankInfo card">
            <div class="row-fluid">
                <div class="span7">
                    {{#if stackRank.avatarUrl}}
                        <div class="avatarwrap">
                            <img alt="{{stackRank.firstName}} {{stackRank.lastName}}"  src="{{stackRank.avatarUrl}}" />
                        </div>
                     {{/if}}
                        <!-- we don't appear to have name data in the stackRank object at this time -->
                            <!-- <span class="avatar-initials">{{trimString stackRank.firstName 0 1}}{{trimString stackRank.lastName 0 1}}</span> -->
                    <strong class="rank">\#{{stackRank.rank}} <cms:contentText key="OF" code="ssi_contest.participant" /> {{stackRank.participantsCount}} </strong>
                    <span class="rankActivity">{{activityDescription}}: <strong>{{progress}}</strong></span>
                    {{#if includeSubmitClaim}}
                    <a href="#" title="" class="viewActivityHistory"><cms:contentText key="VIEW_ACTIVITY_HISTORY" code="ssi_contest.payout_stackrank" /></a>
                    {{/if}}
                </div>
                {{#ueq daysToEnd 0}}
                    <div class="span5 text-right">
                        {{#eq stackRank.rank 1}}
                            <span class="behindLeader isLeader"><cms:contentText key="LEADER" code="ssi_contest.participant" /></span>
                        {{else}}
                            {{#if behindLeader}}
                                <span class="behindLeader"><cms:contentText key="BEHIND_LEADER" code="ssi_contest.participant" />: <strong>{{behindLeader}}</strong></span>
                            {{/if}}
                        {{/eq}}
                    </div>
                {{/ueq}}
            </div>
        </div>
    </div>
    {{/if}}

    <!-- <div class="row-fluid">
        <div class="span3">
            <cms:contentText key="FILTER_BY" code="ssi_contest.participant" />:
        </div>
    </div>

    <div class="row-fluid">
        <div class="span3">
            <select class="filter" name="filter">
                {{#each filters}}
                    <option value="{{id}}">{{name}}</option>
                {{/each}}
            </select>
        </div>
    </div> -->

    {{#ueq daysToEnd 0}}
        <div class="row-fluid">
            <div class="span12">
                <div class="stackRankBoard jsStackRankBoard"
                    data-stackrank-offset=""
                    data-stackrank-limit=""
                    data-stackrank-rowcount=""
                    data-stackrank-paginate="true">
                    {{! subTpl.leaderTpl}}
                </div>
                <div class="pagination pagination-right paginationControls"></div>
            </div>
        </div>
    {{else}}
        <div class="row-fluid">
            <div class="span12">
                <div class="stackRankBoard highlightedBoard">
                    <div class="splitColWrap{{#classes}} {{this}}{{/classes}}" id="leaderboard{{id}}" data-id="{{id}}">
                        <!-- Account info and ranking -->
                        <div class="clearfix">
                            <!-- <ol class="leaders-col leaders-col-a leadersColA"> -->
                            <ol class="row splitColSingle tableHeader">	
	                           <li class="span6 name"><cms:contentText key="NAME" code="ssi_contest.participant" /></li>
                               <li class="span2 point"><cms:contentText key="REWARD" code="ssi_contest.participant" /></li>
                               <li class="span4 activity"><cms:contentText key="ACTIVITY" code="ssi_contest.participant" /></li>		
	                            </ol>
                            <ol class="splitCol splitColSingle tableData">
                                <!-- dynamic -->
                                {{#each stackRankParticipants}}
                                <span class="row">
                                <li value="{{rank}}" {{#if classes.length}}class="{{#classes}}{{this}} {{/classes}}"{{/if}}>
                                <span class="span6">
                                    <b class="rank">{{rank}}.</b>
                                    <div class="avatarwrap">
                                    {{#if avatarUrl}}
                                        <img alt="{{firstName}} {{lastName}}" src="{{#timeStamp avatarUrl}}{{/timeStamp}}">
                                    {{else}}
                                        <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                                    {{/if}}
                                    </div>
                                    <span class="leaderName">{{firstName}} {{lastName}}</span>
                                 </span>
                                 <span class="span4 pull-right">
                                    <span class="score">{{score}}</span>
                                 </span>
                                    {{#if payout}}{{#with payout}}
                                     <span class="span2">
                                        <span class="rankPayout">
                                        {{#if badge.img}}
                                            <img alt="{{badge.name}}" class="payoutBadge" src="${pageContext.request.contextPath}{{badge.img}}">
                                        {{/if}}
                                        {{#if payout}}
                                            {{#eq ../payoutType "points"}}
                                               <span class="payoutAmount {{#if badge.img}}payoutWithBadge{{/if}}">{{payout}} points</span>
                                            {{/eq}}
                                            {{#eq ../payoutType "other"}}
                                                <span class="payoutAmount {{#if badge.img}}payoutWithBadge{{/if}}">{{payout}}</span>
                                            {{/eq}}
                                        {{else}}
                                            <span class="badgeName">{{badge.name}}</span>
                                        {{/if}}
                                        </span>
                                        </span>
                                    {{/with}}{{/if}}
                                </li>
                                </span>
                                {{/each}}
                            </ol>
                        </div><!-- /.clearfix -->
                    </div><!-- /.splitColWrap -->
                </div><!-- /.stackRankBoard.highlightedBoard -->
            </div>
        </div>

        <div class="stackRankAllWrap">
            <div class="row-fluid showMore">
                <div class="span12">
                    <div class="stackRankBoard jsStackRankBoard"
                        data-stackrank-offset="{{payoutCount}}"
                        data-stackrank-limit=""
                        data-stackrank-rowcount=""
                        data-stackrank-paginate="true">
                        {{! subTpl.leaderTpl}}
                    </div>
                    <div class="pagination pagination-right paginationControls"></div>
                </div>
            </div>
        </div>
    {{/ueq}}
{{else}}
{{#if includeSubmitClaim}}
<div class="row-fluid">
    <div class="span12 paxRankInfo">
        <div class="row-fluid">
            <div class="span7">
                <a href="#" title="" class="viewActivityHistory"><cms:contentText key="VIEW_ACTIVITY_HISTORY" code="ssi_contest.payout_stackrank" /></a>
            </div>
        </div>
    </div>
</div>
{{/if}}
<div class="row-fluid">
    <div class="span12">
        <p class="alert alert-info progressNotLoaded"><i class="icon-timer-2"></i> <cms:contentText key="WAITING_FOR_DATA" code="ssi_contest.participant" /></p>
    </div>
</div>
{{/if}}

</div><!-- /.container-splitter.with-splitter-styles -->
