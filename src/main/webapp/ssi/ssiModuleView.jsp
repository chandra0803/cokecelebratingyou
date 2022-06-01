<%@ include file="/include/taglib.jspf"%>
{{! JAVA NOTE: isCreator, isManager, isSuperViewer, and isParticipant are set in the JavaScript }}

{{! this template is used for all creator and participant contest modules }}

<div class="wide-view {{#eq status "finalize_results"}}complete{{/eq}} {{#unless includeStackRanking}}noStackRanking{{/unless}}">
    <div class="row-fluid moduleHeader">
        <div class="span12">
            <div class="text-right daysToEnd">
                {{#eq status "live"}}
                    {{#ueq daysToEnd 0}}
                        {{#eq daysToEnd 1}}
                            <span class="daysRemaining label label-warning">
                                <cms:contentText key="LAST_DAY" code="ssi_contest.participant" />
                            </span>
                        {{else}}
                            <span class="daysRemaining label label-success">
                                <strong>{{daysToEnd}}</strong> <cms:contentText key="DAYS_REMAINING" code="ssi_contest.participant" />
                            </span>
                        {{/eq}}
                    {{/ueq}}
                {{/eq}}

                {{#eq status "pending"}}
                <span class="daysRemaining label label-inverse">
                    <strong>{{daysToStart}}</strong> {{#eq daysToStart 1}}<cms:contentText key="DAY_TO_START" code="ssi_contest.creator" />{{else}}<cms:contentText key="DAYS_TO_START" code="ssi_contest.creator" />{{/eq}}
                </span>
                {{/eq}}

                {{#eq daysToEnd 0}}
                    {{#if isCreator}}
                        {{#ueq status "finalize_results"}}
                            <p class="label label-warning"><i class="icon-warning-circle"></i> <cms:contentText key="UPDATE_FINAL_RESULTS" code="ssi_contest.creator" /></p>
                        {{else}}
                            <p class="label label-important"><cms:contentText key="CONTEST_OVER" code="ssi_contest.creator" /></p>
                        {{/ueq}}
                    {{else}}
                        {{#eq status "finalize_results"}}
                            <p class="label label-important"><cms:contentText key="CONTEST_OVER" code="ssi_contest.creator" /></p>
                        {{else}}
                            <p class="label label-warning"><cms:contentText key="RESULTS_PENDING" code="ssi_contest.participant" /></p>
                        {{/eq}}
                    {{/if}}
                {{/eq}}
            </div>

            <h4>
                <i class="type-icon icon-g5-ssi-{{contestType}} {{contestType}}"></i>
                {{#if isCreator}}
                    <a href="{{creatorDetailPageUrl}}&id={{id}}">
                {{else}}
                    <a href="{{participantDetailPageUrl}}&id={{id}}">
                {{/if}}
                    {{name}}
                </a>
            </h4>

            {{#eq status "pending"}}
                <p class="meta"><cms:contentText key="STARTS" code="ssi_contest.participant" /> {{startDate}}</p>
            {{else}}
                {{#if isProgressLoaded}}
                    <p class="meta"><cms:contentText key="AS_OF" code="ssi_contest.participant" /> {{updatedOnDate}}
                {{else}}
                    <p class="meta"><cms:contentText key="STARTS" code="ssi_contest.participant" /> {{startDate}}
                {{/if}}
            {{/eq}}
        </div>
    </div>

    {{#ueq contestType "doThisGetThat"}}
        {{! all other contests share the same module header}}
        {{! the days remaining counter changes posistions from 4x4 to 4x2, so it needs to be duplicated in html and hidden/shown in css}}
        <div class="moduleSubHeader row-fluid">
            <div class="span12">
                {{#if isCreator}}
                    <!-- <p>manager or creator</p> -->
                    {{#eq contestType "objectives"}}
                        {{#unless hasMultipleObjectives}}
                            <p class="activityAchieved">{{achievedParticipantsCount}} <cms:contentText key="OF" code="ssi_contest.participant" /> {{participantsCount}} <cms:contentText key="ACHIEVED" code="ssi_contest.participant" /></p>
                        {{/unless}}
                    {{else}}
                        <p class="activityDescription">{{activityDescription}}</p>
                    {{/eq}}
                {{else}}
                    {{#if includeStackRanking}}
                        {{#ueq stackRank.participantsCount "0"}}
                        <p class="rankDescription"><cms:contentText key="RANK" code="ssi_contest.participant" />: \#{{stackRank.rank}} <cms:contentText key="OF" code="ssi_contest.participant" /> {{stackRank.participantsCount}}</p>
                        {{/ueq}}
                    {{/if}}
                {{/if}}
            </div>
        </div>

    {{/ueq}}


    <!-- begin contestHtml -->
    {{{contestHtml}}}
    <!-- end contestHtml -->

    {{! footer area}}
    {{#if isCreator}}
        <div class="row-fluid contestModuleFooter">
            {{#eq status "finalize_results"}}
                {{#if totalPayout}}
                    {{#eq payoutType "points"}}
                    <div class="span6 payoutIssued">
                        <span class="payoutWrap">
                            <strong class="payoutAmount">{{totalPayout}}</strong><span class="payoutType"><cms:contentText key="POINTS_ISSUED" code="ssi_contest.participant" /></span>
                        </span>
                    </div>
                    {{/eq}}

                    {{#eq payoutType "other"}}
                    <div class="span6 payoutIssued">
                        <span class="payoutWrap">
                            <strong class="payoutAmount">{{totalPayout}}</strong><span class="payoutType"><cms:contentText key="PAYOUT_ISSUED" code="ssi_contest.participant" /></span>
                        </span>
                    </div>
                    {{/eq}}
                <div class="span6 text-right">
                {{else}}
                <div class="span12 text-right">
                {{/if}}
                    <a href="{{creatorDetailPageUrl}}&id={{id}}" class="btn btn-primary btn-inverse final-results" title=""><cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" /></a>
                </div>
            {{else}}
                <div class="span12 text-center">
                    <a href="{{creatorDetailPageUrl}}&id={{id}}" class="btn btn-primary" title=""><cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" /></a>
                </div>
            {{/eq}}
        </div>
    {{else}}
        <div class="row-fluid contestModuleFooter participantFooter">
            {{#eq status "finalize_results"}}
                {{#if totalPayout}}
                    {{#eq payoutType "points"}}

                    <div class="span6 participantPayoutIssued">
                        <span class="payoutWrap">
                            <strong class="payoutAmount">{{totalPayout}} </strong><span class="payoutType"><cms:contentText key="POINTS" code="ssi_contest.participant" /></span>
                        </span>
                    </div>

                    <div class="span6 text-right">
                        <a href="{{participantDetailPageUrl}}&id={{id}}" class="btn btn-primary btn-inverse final-results" title=""><cms:contentText key="FINAL_RESULTS" code="ssi_contest.participant" /></a>
                    </div>
                    {{else}}
                        {{#if multipleActivities}}
                            <div class="span6 participantPayoutIssued">
                                <span class="payoutWrap">
                                    <span class="payoutType"><cms:contentText key="CONGRATS_YOU_EARNED_AWARDS" code="ssi_contest.participant" /></span>
                                </span>
                            </div>

                            <div class="span6 text-right">
                                <a href="{{participantDetailPageUrl}}&id={{id}}" class="btn btn-primary btn-inverse final-results" title=""><cms:contentText key="FINAL_RESULTS" code="ssi_contest.participant" /></a>
                            </div>
                            {{else}}
                            <div class="span6 participantPayoutIssued">
                                <span class="payoutWrap">
                                    <strong class="payoutAmount">{{totalPayout}}</strong><span class="payoutType"><cms:contentText key="EARNED" code="ssi_contest.participant" /></span>
                                </span>
                            </div>

                            <div class="span6 text-right">
                                <a href="{{participantDetailPageUrl}}&id={{id}}" class="btn btn-primary btn-inverse final-results" title=""><cms:contentText key="FINAL_RESULTS" code="ssi_contest.participant" /></a>
                            </div>
                        {{/if}}
                    {{/eq}}
                {{else}}
                    <div class="span12 participantPayoutIssued">
                        <span class="payoutWrap">
                            <span class="payoutType"><cms:contentText key="NO_AWARD_ACHIEVED" code="ssi_contest.participant" /></span>
                        </span>
                    </div>
                {{/if}}
            {{else}}
                {{#if includeSubmitClaim}}
                    {{#ueq claimDaysToEnd 0}}
                        <div class="span12 text-center">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <a href="{{claimUrl}}" class="btn btn-primary submit-claim" title=""><cms:contentText key="SUBMIT_CLAIM" code="ssi_contest.participant" /></a>
                            </beacon:authorize>
                        </div>
                    {{else}}
                        <div class="span12 text-center">
                            <a href="ssi/participantContestList.do?method=display&id={{id}}" class="btn btn-primary" title=""><cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" /></a>
                        </div>
                    {{/ueq}}
                {{else}}
                    <div class="span12 text-center">
                        <a href="ssi/participantContestList.do?method=display&id={{id}}" class="btn btn-primary" title=""><cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" /></a>
                    </div>
                {{/if}}
            {{/eq}}
        </div>
    {{/if}}
</div>
