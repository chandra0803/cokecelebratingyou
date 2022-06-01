<%@ include file="/include/taglib.jspf"%>

    <div class="{{#classes}} {{this}}{{/classes}}" id="rankings{{id}}" data-id="{{id}}">


        <div class="clearfix rankingsMetaData">
            <h2 class="rankingPromotionTitle">{{name}}</h2>

            <span class="startDate"><strong><cms:contentText key="START_DATE" code="participant.promotions"/>:</strong> {{startDate}}</span> <span class="endDate"><strong><cms:contentText key="END_DATE" code="participant.promotions"/></strong> {{endDate}}</span>
			<!-- UET removed rules link -->

        </div>

        <!-- Account info and ranking -->
        {{#if titleUser}}
        <div class="clearfix highlightedUser">
            {{#if titleUser.avatarUrl}}
            <div class="fl avatarContainer">
                <div class="fl avatarContainerBorder">
                    <img alt="" class="avatar" src="{{#timeStamp titleUser.avatarUrl}}{{/timeStamp}}" />
                </div>
            </div>
            {{/if}}

            <div class="fl userInfoContainer">
                {{#unless titleUser.currentUser}}
                <span class="name pre-rank">{{titleUser.firstName}} {{titleUser.lastName}}</span>
                {{else}}
                <span class="your pre-rank"><cms:contentText key="YOU_ARE" code="promotion.stackrank.history"/> </span>
                {{/unless}}
                <span class="pre-large-rank-text">#</span> <span class="large-rank-text">{{titleUser.rank}} <cms:contentText code="participant.throwdownstats" key="OUT_OF" /> {{totalNumberLeaders}}</span>

                <div class="round-info">
                    <span><cms:contentText key="ROUND" code="promotion.stackrank.history"/></span>
                    <span class="your-round">{{titleUser.currentRound}}</span>
                    <span><cms:contentText key="OF" code="promotion.stackrank.history"/></span>
                    <span class="round-total">{{titleUser.totalRounds}}</span>
                </div>

				<span class="progress-text"><strong><cms:contentText key="TOTAL" code="promotion.throwdown.summary"/> {{titleUser.baseUnit}}: {{titleUser.score}}</strong></span>
                {{#if isProgressLoaded}}
                <span class="activitydate"><span><cms:contentText code="participant.throwdownstats" key="FROM" /> </span> <strong>{{startDate}}</strong><span> <cms:contentText code="participant.throwdownstats" key="TO" /> </span> <strong>{{progressEndDate}}</strong></span>
                {{else}}
                <span class="activitydate"> <cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" /></span>
                {{/if}}	
            </div>
        </div><!-- /.highlightedUser -->

        <div class="info-text">{{overview}}</div>
        {{/if}}

        <div class="row-fluid clearfix">
            {{#if badgeHolders}}
            <div class="top-ranked first-place span4">
                <!-- dynamic -->
            </div>

            <div class="top-ranked second-place span4">
                <!-- dynamic -->
            </div>

            <div class="top-ranked third-place span4">
                <!-- dynamic -->
            </div>
            {{/if}}
            <!--subTpl.topRankedTpl=
                {{#badgeHolders}}
                    <div class="rank-info">
                        <img src="{{icon}}" class="rank-icon">
                        <span><cms:contentText key="RANK" code="promotion.stackrank.history"/></span>
                    </div>
                    <div class="rank-player-info">
                        {{#if avatarUrl}}<img alt="{{firstName}} {{lastName}}" class="avatar" src="{{#timeStamp avatarUrl}}{{/timeStamp}}">{{/if}}
                            <span class='firstName'>{{firstName}}</span>
                            <span class='lastName'>{{lastName}}</span>
                            <span class='score'>{{scoreForDisplay}}</span>
                    </div>
                    {{#if badge}}
			<div class="rank-badge-container">
			<img src="{{badge}}" class="rank-badge {{#unless earned}}in-progress{{/unless}}">
			</div>
		    {{/if}}
                {{/badgeHolders}}
            subTpl-->

        </div>
        <div class="paginationControls pagination pagination-right"></div>
        <!--subTpl.paginationTpl=
                    {{#if pagination}}
                    {{#with pagination}}
                    <ul>
                        <li class="first{{#if first.state}} {{first.state}}{{/if}}" data-page="{{first.page}}">
                            <a href="#"><i class="icon-double-angle-left"></i>&nbsp;</a>
                        </li>
                        <li class="prev{{#if prev.state}} {{prev.state}}{{/if}}" data-page="{{prev.page}}">
                            <a href="#"><i class="icon-angle-left"></i> <cms:contentText key="PREV" code="promotion.stackrank.history"/></a>
                        </li>
                        {{#each pages}}
                        <li {{#if state}}class="{{state}}"{{/if}} data-page="{{page}}">
                            <a href="#">{{#if isgap}}&#8230;{{else}}{{page}}{{/if}}</a>
                        </li>
                        {{/each}}
                        <li class="next{{#if next.state}} {{next.state}}{{/if}}" data-page="{{next.page}}">
                            <a href="#"><cms:contentText key="NEXT" code="promotion.stackrank.history"/> <i class="icon-angle-right"></i></a>
                        </li>
                        <li class="last{{#if last.state}} {{last.state}}{{/if}}" data-page="{{last.page}}">
                            <a href="#">&nbsp;<i class="icon-double-angle-right"></i></a>
                        </li>
                    </ul>
                    {{/with}}
                    {{/if}}
                subTpl-->
        <div class="leadersContainer clearfix">

            <ol class="leaders-col leaders-col-a leadersColA">
                <!-- dynamic -->
            </ol>

            <!-- second col, responsive (float left) -->
            <ol class="leaders-col leaders-col-b leadersColB">
                <!-- dynamic -->
            </ol>

            <!--subTpl.leaderTpl=
				{{#if leaders}}
                <li class= "label">
                    <span class="score"><cms:contentText key="SCORE" code="promotion.stackrank.history"/></span>
                    <span class="rank"><cms:contentText key="RANK_PLAYER" code="promotion.stackrank.history"/></span>
                </li>
                {{#leaders}}
                <li value="{{rank}}" {{#if classes.length}}class="{{#classes}}{{this}} {{/classes}}"{{/if}}>
                    <span class="score">{{score}}</span>
                    <b>{{rank}}.</b>
                    {{#if avatarUrl}}<img alt="{{firstName}} {{lastName}}" class="avatar" src="{{#timeStamp avatarUrl}}{{/timeStamp}}">{{/if}}
                    <a class="leaderName" href="#" data-participant-ids="[{{participantId}}]">{{firstName}} {{lastName}}</a>
                </li>
				{{/leaders}}
                {{else}}
                <h4 class="td-no-rankings-message"><cms:contentText key="NO_RANKINGS" code="promotion.stackrank.history"/></h4>
                {{/if}}
            subTpl-->
            <div class="paginationControls pagination pagination-right"></div>
        </div>


    </div>

