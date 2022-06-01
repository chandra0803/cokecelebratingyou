<%@ include file="/include/taglib.jspf"%>
{{! NOTE: extraJSON is set in JS }}

{{#if isParticipantDrillDown}}
<div class="row-fluid paxDrillWrap page-topper">
    <div class="span12">
        <h3>{{participantDrillName}}</h3>
    </div>
</div>
{{#unless isReportDrillDown}}
<div class="row-fluid">
    <div class="span12 backToContestWrap">
        <c:if test="${ isCreator }">
        	<a href="#" class="btn btn-link btn-icon"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK_TO_CONTEST" code="ssi_contest.participant" /></a>
        </c:if>
    </div>
</div>
{{/unless}}
{{/if}}

<c:if test="${isSuperViewer eq true }">
<div class="row-fluid">
    <div class="span12 backToContestWrap">
        <a href="#" class="btn btn-link btn-icon"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK_TO_CONTEST" code="ssi_contest.participant" /></a>
    </div>
</div>
</c:if>

<div class="row-fluid backToContestRow">
    <div class="span12 creatorDetailsBtns backToContestWrap" style="display: none;">
        <button type="button" class="btn btn-link btn-icon backToContest"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK" code="system.button" /></button>
    </div>
</div>

<div class="row-fluid">
    <div class="span12 contestInfo">
        {{#eq status "live"}}
        {{#ueq daysToEnd 0}}
            {{#eq daysToEnd 1}}
        <p class="daysToGo label label-warning"><i class="icon icon-clock"></i>
                <cms:contentText key="LAST_DAY" code="ssi_contest.participant" />
            {{else}}
        <p class="daysToGo label label-success"><i class="icon icon-clock"></i>
                {{daysToEnd}} <cms:contentText key="DAYS_TO_GO" code="ssi_contest.participant" />
            {{/eq}}
        </p>
        {{else}}
        <p class="daysToGo label label-important"><cms:contentText key="CONTEST_OVER" code="ssi_contest.creator" /></p>
        {{/ueq}}
        {{/eq}}

        {{#eq status "finalize_results"}}
        <p class="daysToGo label label-warning"><cms:contentText key="CONTEST_OVER" code="ssi_contest.creator" /></p>
        {{/eq}}

        {{#eq status "pending"}}
        <p class="daysToGo label label-inverse"><i class="icon icon-clock"></i>
            {{daysToStart}} {{#eq daysToStart 1}}<cms:contentText key="DAY_TO_START" code="ssi_contest.creator" />{{else}}<cms:contentText key="DAYS_TO_START" code="ssi_contest.creator" />{{/eq}}
        </p>
        {{/eq}}

        <p class="contestDates"><cms:contentText key="STARTS" code="ssi_contest.participant" /> {{startDate}}<br><cms:contentText key="ENDS" code="ssi_contest.participant" /> {{endDate}}</p>

        <h2 class="contestTitle">
            <i class="type-icon icon-g5-ssi-{{contestType}} {{contestType}}"></i>
            {{name}}
        </h2>

        {{#if attachmentUrl}}
        <div class="contestAttachment">
            <a href="{{attachmentUrl}}" title="" class="fileLink">
                {{attachmentTitle}}
                {{#eq attachmentType "pdf"}}
                <i class="icon-file-pdf btn btn-icon invert-btn btn-export-pdf"></i>
                {{else}}
                    {{#eq attachmentType "word"}}
                    <i class="icon-file-doc btn btn-icon invert-btn btn-export-doc"></i>
                    {{/eq}}
                {{/eq}}
            </a>
        </div>
        {{/if}}

        <span class="contestMeta"><cms:contentText key="FOR_QUESTION_CONTACT" code="ssi_contest.participant" /> {{creatorName}}</span>

        <br/>

        {{#eq activityMeasuredIn "currency"}}
            <span class="contestMeta"><cms:contentText key="CURRENCY_USED_FOR_THIS_CONTEST" code="ssi_contest.participant" /> {{currencyAbbr}}</span>
        {{/eq}}

        <div class="partialDescription toggleDescriptionWrap">
            {{description}}
        </div>

        <div class="fullDescription toggleDescriptionWrap">
            {{description}}
        </div>

        <a href="#" class="activityDescriptionToggle hide readMore"><cms:contentText key="READ_MORE" code="ssi_contest.participant" /></a>
        <a href="#" class="activityDescriptionToggle hide readLess"><cms:contentText key="READ_LESS" code="ssi_contest.participant" /></a>
    </div>
</div>

<div class="row-fluid">
    <div class='{{#unless isParticipantDrillDown}}{{#if includeSubmitClaim}}{{#ueq claimDaysToEnd 0}}span9{{else}}span12{{/ueq}}{{else}}span12{{/if}}{{else}}span12{{/unless}}'>
        {{#eq contestType "doThisGetThat"}}
            {{#eq status "finalize_results"}}
                <h3 class="sectionTitle"><cms:contentText key="FINAL_RESULTS" code="ssi_contest.participant" /> &ndash; <cms:contentText key="YOUR_ACTIVITY" code="ssi_contest.participant" /></h3>
                {{#ueq status "pending"}}
                <p class="asOfDate"><cms:contentText key="AS_OF" code="ssi_contest.participant" /> {{updatedOnDate}}</p>
                {{/ueq}}
            {{else}}
                <h3 class="sectionTitle"><cms:contentText key="CURRENT_ACTIVITY" code="ssi_contest.participant" /></h3>
                {{#ueq status "pending"}}
                <p class="asOfDate"><cms:contentText key="AS_OF" code="ssi_contest.participant" /> {{updatedOnDate}}</p>
                {{/ueq}}
            {{/eq}}
        {{else}}
            <h3 class="sectionTitle">
                {{#eq status "finalize_results"}}
                    <cms:contentText key="FINAL_RESULTS" code="ssi_contest.participant" /> &ndash;
                {{/eq}}
                {{activityDescription}}
            </h3>
            {{#ueq contestType "stackRank"}}
                {{#ueq status "pending"}}
                    <p class="asOfDate"><cms:contentText key="AS_OF" code="ssi_contest.participant" /> {{updatedOnDate}}</p>
                {{/ueq}}
            {{else}}
                {{#ueq status "isProgressLoaded"}}
                    <p class="asOfDate"><cms:contentText key="AS_OF" code="ssi_contest.participant" /> {{updatedOnDate}}</p>
                {{/ueq}}
            {{/ueq}}
        {{/eq}}
    </div>

    {{#unless isParticipantDrillDown}}
        {{#if includeSubmitClaim}}
            {{#ueq claimDaysToEnd 0}}
                <beacon:authorize ifNotGranted="LOGIN_AS">
                <div class='span3 creatorDetailsBtns'>
                    <div class="pullRightBtns">
                        <a href="{{claimUrl}}" class="btn btn-primary"><cms:contentText key="SUBMIT_CLAIM" code="ssi_contest.participant" /></a>
                    </div>
                </div>
                </beacon:authorize>
            {{/ueq}}
        {{/if}}
    {{/unless}}
</div>

{{#ueq contestType "doThisGetThat"}}
<div class="row-fluid earnAwardDescription">
    <div class="span12">
        {{#eq contestType "objectives"}}
        <p><cms:contentText key="ACHIEVE_FULL_OBJ" code="ssi_contest.participant" /></p>
        {{/eq}}

        {{#eq contestType "stepItUp"}}
        <p><cms:contentText key="AWARDS_NOT_CUMMLATIVE" code="ssi_contest.participant" /></p>
        {{/eq}}

        {{#eq contestType "stackRank"}}
        <p><cms:contentText key="EARN_AWARD_DESC" code="ssi_contest.participant" /></p>

        {{#if includeMinimumQualifier}}<p class="minimumQualifier"><cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.payout_dtgt" /> <strong>{{minQualifier}}</strong></p>{{/if}}
        {{/eq}}
    </div>
</div>
{{/ueq}}

{{#eq daysToEnd 0}}
    <div class="row-fluid paxStatusBanner">
        <div class="span12 statusBanner">

            {{#if badge.img}}
                {{#eq status "finalize_results"}}
            <div class="paxBadge">
                <img src="{{badge.img}}" alt="{{badge.name}}" />
            </div>
                {{/eq}}
                {{#eq status "closed"}}
            <div class="paxBadge">
                <img src="{{badge.img}}" alt="{{badge.name}}" />
            </div>
                {{/eq}}
            {{/if}}

            {{#eq status "finalize_results"}}
                <div class="payoutAmount">
                {{#if totalPayout}}
                    {{#if multipleActivities}}
                        <span class="msg-achieved"><i class="icon-verification"></i> <cms:contentText key="CONGRATS_YOU_EARNED_AWARDS" code="ssi_contest.participant" /></span>
                    {{else}}
                        {{#eq payoutType "points"}}
                        <span class="msg-achieved msg-payout"><i class="icon-verification"></i> <cms:contentText key="YOU_EARNED" code="ssi_contest.participant" /> <strong>{{totalPayout}}</strong> <cms:contentText key="POINTS" code="ssi_contest.participant" /></span>
                        {{else}}
                        <span class="msg-achieved msg-payout"><i class="icon-verification"></i> <cms:contentText key="YOU_EARNED" code="ssi_contest.participant" /> <strong>{{totalPayout}}</strong></span>
                        {{/eq}}
                    {{/if}}
                {{else}}
                    <span class="msg-notAchieved"><i class="icon-cancel-circle"></i> <cms:contentText key="NO_AWARD_ACHIEVED" code="ssi_contest.participant" /></span>
                {{/if}}
                </div>
            {{else}}

                {{#eq status "closed"}}
                    <div class="payoutAmount">
                    {{#if totalPayout}}
                        {{#if multipleActivities}}
                            <span class="msg-achieved"><i class="icon-verification"></i> <cms:contentText key="CONGRATS_YOU_EARNED_AWARDS" code="ssi_contest.participant" /></span>
                        {{else}}
                            {{#eq payoutType "points"}}
                            <span class="msg-achieved msg-payout"><i class="icon-verification"></i> <cms:contentText key="YOU_EARNED" code="ssi_contest.participant" /> <strong>{{totalPayout}}</strong> <cms:contentText key="POINTS" code="ssi_contest.participant" /></span>
                            {{else}}
                            <span class="msg-achieved msg-payout"><i class="icon-verification"></i> <cms:contentText key="YOU_EARNED" code="ssi_contest.participant" /> <strong>{{totalPayout}}</strong></span>
                            {{/eq}}
                        {{/if}}
                    {{else}}
                        <span class="msg-notAchieved"><i class="icon-cancel-circle"></i> <cms:contentText key="NO_AWARD_ACHIEVED" code="ssi_contest.participant" /></span>
                    {{/if}}
                    </div>

                {{else}}
                    <div class="payoutAmount">
                        <span class="msg-pending"><i class="icon-wrist-watch"></i> <cms:contentText key="RESULTS_PENDING" code="ssi_contest.participant" /></span>
                    </div>
                {{/eq}}
            {{/eq}}

            {{#unless isParticipantDrillDown}}
                {{#eq payoutType "points"}}
                    {{#if totalPayout}}
                <div class="text-center shopNow">
                    <a href="{{shopUrl}}" title="" class="btn btn-primary" target="_blank"><cms:contentText key="SHOP_NOW" code="ssi_contest.participant" /> <i class="icon-cart-1"></i></a>
                </div>
                    {{/if}}
                {{/eq}}
            {{/unless}}
        </div><!-- /.statusBanner -->
    </div><!-- /.paxStatusBanner -->
{{/eq}}

<div class="contestUniqueContentWrapper">
    <div class="contestUniqueContent participantContest">
        <div class='contestWrapper {{contestType}} {{#eq status "finalize_results"}}complete{{/eq}} {{#eq status "closed"}}complete{{/eq}} {{#ueq contestType "stackRank"}}container-splitter with-splitter-styles{{/ueq}}'>
            {{! triple curly brackets prevents Handlebars from processing the data }}
            {{{contestHtml}}}
        </div>

        {{#if includeSubmitClaim}}
        <div class="row-fluid activityHistory">
            <div class="span12">
            </div>
        </div>
        {{/if}}
    </div><!-- .contestUniqueContent -->

{{#if includeStackRanking}}
    <div class="contestFullStackRankWrap" style="display:none">

    <!-- Need splitter styles nested inside this wrapper -->
    <div class="container-splitter with-splitter-styles">
        {{! contest stack rank table}}
        <div class="row-fluid">
            <div class="span12">
                <div class="stackRankBoard contestFullStackRank"
                    data-stackrank-offset=""
                    data-stackrank-limit="{{payoutCount}}"
                    data-stackrank-rowcount="11">
                    {{! subTpl.leaderTpl}}
                </div>
                <div class="pagination pagination-right paginationControls"></div>
            </div>
        </div><!-- .row-fluid -->
    </div><!-- /.container-splitter.with-splitter-styles -->

    </div>
{{/if}}
{{#if includeSubmitClaim}}
    <div class="row-fluid activityHistoryWrap" style="display:none">
        <div class="span12">
        </div>
    </div>
{{/if}}

</div><!-- /.contestUniqueContentWrapper -->

<div class="row-fluid backToContestRow">
    <div class="span12 creatorDetailsBtns backToContestWrap" style="display: none;">
        <button type="button" class="btn btn-link btn-icon backToContest"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK" code="system.button" /></button>
    </div>
</div>
