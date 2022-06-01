<%@ include file="/include/taglib.jspf"%>
<!-- ======== SSI MODULE ======== -->
{{! NOTE: 'contestType', 'activity', and 'leader' are set in the js }}

<div class="module-topper {{contestType}}">
    {{#eq contestType "objectives"}}
        <h4><i class="type-icon icon-g5-ssi-objectives objectives"></i> <cms:contentText key="OBJECTIVES_LIST" code="ssi_contest.participant" /></h4>
    {{/eq}}
    {{#eq contestType "stepItUp"}}
        <h4><i class="type-icon icon-g5-ssi-stepItUp stepItUp"></i> <cms:contentText key="STEP_IT_UP_LIST" code="ssi_contest.participant" /></h4>
    {{/eq}}
    {{#eq contestType "doThisGetThat"}}
        <h4><i class="type-icon icon-g5-ssi-doThisGetThat doThisGetThat"></i> <cms:contentText key="DO_THIS_GET_THAT_LIST" code="ssi_contest.participant" /></h4>
    {{/eq}}
    {{#eq contestType "stackRank"}}
        <h4><i class="type-icon icon-g5-ssi-stackRank stackRank"></i> <cms:contentText key="STACKRANK_LIST" code="ssi_contest.participant" /></h4>
    {{/eq}}
</div>

<div class="contestListWrap scrollArea">
    <ul class="contestList unstyled">
        {{#each contests}}
            <li class="contestListItem">
                <div class="row-fluid">
                    <div class="span12 contestInfo">
                        <h5 class="contestName">{{name}}</h5>
                        <p class="contestAsOf">
                        {{#eq status "pending"}}
                            <cms:contentText key="STARTS" code="ssi_contest.participant" /> {{startDate}}
                        {{else}}
                            <cms:contentText key="AS_OF" code="ssi_contest.participant" /> {{updatedOnDate}}
                        {{/eq}}
                        </p>
                    </div>
                </div>

                <div class="row-fluid">
                    {{#eq contestType "objectives"}}
                        <%--<div class="narrowModule">
                            <div class="span6 activityAchieved">
                                <span><cms:contentText key="ACTIVITY" code="ssi_contest.creator" />: {{progress}}</span>
                            </div>
                            <div class="span6 text-right">
                                <a class="btn btn-primary" href="{{creatorDetailPageUrl}}&id={{id}}">
                                    {{#eq status "finalize_results"}}
                                        <cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" />
                                    {{else}}
                                       <cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" />
                                    {{/eq}}
                                </a>
                            </div>
                        </div>--%>
                        <div class="shortAndWideModule wideModule">
                            <div class="span8 activityAchieved">
                                {{#if participantsCount}}
                                <span>
                                    <strong><cms:contentText key="ACTIVITY" code="ssi_contest.creator" />:</strong> {{progress}}
                                    <span class="participantEarned">
                                        <em>{{achievedParticipantsCount}} <cms:contentText key="OF" code="system.general" /> {{participantsCount}}</em> <cms:contentText key="ACHIEVED" code="ssi_contest.participant" />
                                    </span>
                                </span>
                                {{/if}}
                            </div>
                            <div class="span4 text-right">
                                <a class="btn btn-primary" href="{{creatorDetailPageUrl}}&id={{id}}">
                                    {{#eq status "finalize_results"}}
                                        <cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" />
                                    {{else}}
                                        <cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" />
                                    {{/eq}}
                                </a>
                            </div>
                        </div>
                    {{/eq}}
                    {{#eq contestType "stepItUp"}}
                        <div class="span6 activityAchieved">
                            <span class="goal"><strong><cms:contentText key="GOAL_INIT_CAP" code="ssi_contest.participant" />:</strong> {{goal}}</span>
                            <span class="activity"><strong><cms:contentText key="CURRENT_ACTIVITY" code="ssi_contest.participant" />:</strong> {{progress}}</span>
                        </div>
                        <div class="span6 text-right">
                            <a class="btn btn-primary" href="{{creatorDetailPageUrl}}&id={{id}}">
                                {{#eq status "finalize_results"}}
                                    <cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" />
                                {{else}}
                                    <cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" />
                                {{/eq}}
                            </a>
                        </div>
                    {{/eq}}
                    {{#eq contestType "doThisGetThat"}}
                        <div class="span12 text-right">
                            <a class="btn btn-primary" href="{{creatorDetailPageUrl}}&id={{id}}">
                                {{#eq status "finalize_results"}}
                                    <cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" />
                                {{else}}
                                    <cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" />
                                {{/eq}}
                            </a>
                        </div>
                    {{/eq}}
                    {{#eq contestType "stackRank"}}
                        <div class="span6 activityAchieved">
                            <span><strong><cms:contentText key="ACTIVITY" code="ssi_contest.creator" />:</strong> {{progress}}</span>
                        </div>
                        <div class="span6 text-right">
                            <a class="btn btn-primary" href="{{creatorDetailPageUrl}}&id={{id}}">
                                {{#eq status "finalize_results"}}
                                    <cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" />
                                {{else}}
                                    <cms:contentText key="VIEW_DETAILS" code="ssi_contest.creator" />
                                {{/eq}}
                            </a>
                        </div>
                        {{#if leader.firstName}}
                        <div class="span12 stackRankLeader">
                            {{#with leader}}
                                {{#ueq rank 0}}
                                <b>{{rank}}.</b>
                                 <div class="avatarwrap">
                                    {{#if avatarUrl}}
                                        <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" class="listTileAvatar" alt="{{trimString firstName 0 1}}{{trimString lastName 0 1}}">
                                    {{else}}
                                        <span class="avatar-initials listTileAvatar">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                                    {{/if}}
                                </div>
                                <span class="score">{{score}}</span>
                                <span class="leaderName">
                                    {{firstName}} {{lastName}}
                                </span>
                                {{/ueq}}
                            {{/with}}
                        </div>
                        {{/if}}
                    {{/eq}}
                </div>
            </li>
        {{/each}}
    </ul>
</div>
