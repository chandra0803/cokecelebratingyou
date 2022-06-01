<%@ include file="/include/taglib.jspf"%>

{{#ueq daysToEnd 0}}
    {{#if payouts}}
    <div class="row-fluid payoutsWrap payoutsShowLess" data-classtoggle="payoutsShowLess payoutsShowMore">
        <div class="span12 payoutsRow">
            {{#gte payouts.length 4}}
            <div class="bottomPayoutInfo text-right">
                <a href="#" class="payoutsToggle showMore"><cms:contentText key="VIEW_ALL_PAYOUTS" code="ssi_contest.participant" /> <i class="icon-eye"></i></a>
                <a href="#" class="payoutsToggle showLess"><cms:contentText key="VIEW_LESS" code="ssi_contest.participant" /> <i class="icon-eye-off"></i></a>
            </div>
            {{/gte}}

            <h3 class="sectionTitle"><cms:contentText key="CONTEST_PAYOUT" code="ssi_contest.participant" /></h3>

            <div class="payoutsList">
            {{#each payouts}}
                <div class="payout">
                    <span class="payoutRank">{{rank}}</span>
                    <div class="payoutInfo">
                        {{#if badge.img}}
                            <img src="${pageContext.request.contextPath}{{badge.img}}" alt="{{badge.name}}" class="payoutBadge">
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

<div class="row-fluid">
    <div class='span12 contestData dataSectionWrap {{#eq status "finalize_results"}}complete{{/eq}}'>
        <div class="row-fluid">
            <div class="dataSection activityGoal">
                <h4>{{goal}}</h4>
                <span><cms:contentText key="GOAL" code="ssi_contest.creator" /></span>
            </div>

            <div class="dataSection activityProgress">
                <h4>{{progress}}</h4>

                {{#ueq status "finalize_results"}}
                    <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /></span>
                {{else}}
                    <span><cms:contentText key="FINAL_ACTIVITY" code="ssi_contest.creator" /></span>
                {{/ueq}}
            </div>

            {{#ueq status "finalize_results"}}
            <div class="dataSection activityToGo">
                {{#if goalAchieved}}
                    <h4 class="activityGoalAchieved"><i class="icon-verification"></i></h4>
                    <span><cms:contentText key="GOAL_ACHIEVED" code="ssi_contest.participant" /></span>
                {{else}}
                    <h4>{{remaining}}</h4>
                    <span><cms:contentText key="TO_GO" code="ssi_contest.creator" /></span>
                {{/if}}
            </div>
            {{/ueq}}

            <div class="dataSection activityMaxPayout">
                <h4>{{payoutCap}}</h4>

                {{#ueq status "finalize_results"}}
                    {{#eq payoutType "points"}}
                        <span><cms:contentText key="MAXIMUM_POINTS" code="ssi_contest.creator" /></span>
                    {{/eq}}
                    {{#eq payoutType "other"}}
                        <span><cms:contentText key="MAXIMUM_PAYOUT" code="ssi_contest.creator" /></span>
                    {{/eq}}
                {{else}}
                    {{#eq payoutType "points"}}
                        <span><cms:contentText key="POINTS_ISSUED" code="ssi_contest.payout_dtgt" /></span>
                    {{/eq}}
                    {{#eq payoutType "other"}}
                        <span><cms:contentText key="PAYOUT_ISSUED" code="ssi_contest.creator" /></span>
                    {{/eq}}
                {{/ueq}}
            </div>
        </div>
    </div>
</div>

{{#eq status "finalize_results"}}
    <div class="row-fluid">
        <div class="span12">
            <h3 class="sectionTitle"><cms:contentText key="FINAL_PAYOUT" code="ssi_contest.creator" /></h3>
        </div>
    </div>
{{/eq}}

<div class="row-fluid">
    <div class="span12">

    {{#if isProgressLoaded}}
        <ul class="pullRightBtns export-tools">
            <li class="export csv">
                <a href="contestDetailsExportAction.do?method=downloadContest&role={{role}}&id={{id}}" class="exportCsvButton">
                    <span class="btn btn-inverse btn-compact btn-export-csv">
                        <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                    </span>
                </a>
            </li>
        </ul>

        <h4 class="stackRankBoardHeader"><cms:contentText key='PAX' code='ssi_contest.creator' /></h4>
    {{/if}}

    {{#unless isProgressLoaded}}
        <p class="alert alert-info progressNotLoaded"><i class="icon-timer-2"></i> <cms:contentText key="WAITING_FOR_DATA" code="ssi_contest.participant" /></p>
    {{/unless}}

    </div>
</div>

{{#if isProgressLoaded}}
    {{#ueq status "finalize_results"}}
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
                <div class="stackRankBoard payoutBoard highlightedBoard">
                <!-- <div class="payoutBoard"> -->
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
	                                               <span class="payoutAmount {{#if badge.img}}payoutWithBadge{{/if}}">{{payout}} <cms:contentText key="POINTS" code="ssi_contest.participant" /></span>
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
                </div><!-- /.stackRankBoard.payoutBoard.highlightedBoard -->
            </div>
        </div>

        {{#if viewOrHideAllSr}}
        <div class="stackRankAllWrap stackRankShowAll" data-classtoggle="stackRankShowAll stackRankShowLess">
            <div class="row-fluid">
                <div class="span12 creatorDetailsBtns">
                    <button type="button" class="btn btn-primary btn-inverse stackRankParticipantsToggle showMore"><cms:contentText key="HIDE_PAX" code="ssi_contest.creator" /></button>
                    <button type="button" class="btn btn-primary btn-inverse stackRankParticipantsToggle showLess"><cms:contentText key="SHOW_ALL_PAX" code="ssi_contest.creator" /></button>
                </div>
            </div>

            <div class="row-fluid showMore">
                <div class="span12">
                    <div class="stackRankBoard jsStackRankBoard"
                        data-stackrank-offset="{{payoutCount}}"
                        data-stackrank-limit=""
                        data-stackrank-rowcount=""
                        data-stackrank-paginate="true"
                        data-stackrank-deferred="true">
                        {{! subTpl.leaderTpl}}
                    </div>
                    <div class="pagination pagination-right paginationControls"></div>
                </div>
            </div>
        </div>
        {{/if}}
    {{/ueq}}
{{/if}}{{! isProgressLoaded}}

</div><!-- /.container-splitter.with-splitter-styles -->
