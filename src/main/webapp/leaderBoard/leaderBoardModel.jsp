   <%@ include file="/include/taglib.jspf"%>

    <div class="leaderboardModel{{#classes}} {{this}}{{/classes}}" id="leaderboard{{id}}" data-id="{{id}}">

        {{#eq parentViewType "module"}}
        <h3 class="module-title">
            <a class="title-link" href="leaderBoardsDetailPage.do?method=getUserRole#set/{{setId}}/{{id}}">{{name}}</a>
        </h3>
        {{else}}
        <h2>{{name}}</h2>

        <div class="clearfix leaderboardMetaData">

            <p class="dates">
                <span class="startdate"><span><cms:contentText
                        key="START_DATE" code="leaderboard.label" /></span> <strong>{{startDate}}</strong></span>
                <span class="enddate"><span><cms:contentText
                        key="END_DATE" code="leaderboard.label" /></span> <strong>{{endDate}}</strong></span>
            </p>

            <ul class="actions upper">
                <li class="viewrules">
                    <a href="#rules" data-sheet-title="<cms:contentText key="VIEW_RULES_HEADING" code="leaderboard.label" />"><i class="icon-book"></i>
                        <cms:contentText key="VIEW_RULES" code="leaderboard.label" />
                    </a>
                </li>
                <beacon:authorize ifNotGranted="LOGIN_AS">
                {{#if editableByUser}}
                    {{#unless archivedFlag}}
                <li class="editinfo">
                    <a href="{{urls.editPromoInfo}}"><i class="icon-pencil2"></i><cms:contentText
                        key="EDIT_LEADER_BOARD" code="leaderboard.label" /></a>
                </li>
                    {{else}}
                <li class="copytonew">
                    <a href="{{urls.copyToNew}}"><i class="icon-file-copy"></i><cms:contentText
                        key="COPY_TO_NEW" code="leaderboard.label" /></a>
                </li>
                    {{/unless}}

                {{/if}}
                </beacon:authorize>
            </ul>

            <div class="rulestoview">{{rules}}</div>

        </div>
        {{/eq}}

        <!-- Account info and ranking -->

        {{#leaders}}
        {{#if highlightedUser}}
        <div class="card card-large card-tall highlightedUser{{#if classes.length}}{{#classes}} {{this}}{{/classes}}{{/if}}">
            <div class="card-front">
                <div class="card-top">
                    <span class="avatar">
                        <span class="avatarContainer">
                        {{#if avatarUrl}}
                            <img alt="{{firstName}} {{lastName}}" class="avatar" src="{{#timeStamp avatarUrl}}{{/timeStamp}}" />
                        {{else}}
                            <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                        {{/if}}
                        </span>
                    </span>
                </div><!-- /.card-top -->

                <div class="card-details">
                    <span class="rank">
                        {{#unless currentUser}}
                            <span class="name pre-rank">
                                <span class="firstname">{{firstName}}</span> {{lastName}} <span class="large-rank-text">{{rank}}</span>
                            </span>
                        {{else}}
                            <span class="your pre-rank">
                                <!--
                                    each leaderboard has a special CM key containing the full translated string for the "Your rank" text
                                    the key ouput will have a {0} placeholder where the rank value is inserted
                                    this allows the translations to have plain text and the rank in any order
                                    we embed this CM output as a tplVariable in our leaderboardModel Handlebars template
                                    we also have an largeRankText subTpl embedded in our leaderboardModel Handlebars template
                                    we pass the leader.rank value from the JSON to the subTpl, then replace the {0} with the rendered output
                                    the final string is assigned to leader.rankFormatted in the JSON to be passed to the main template
                                -->
                                <!--tplVariable.largeRankText= "<cms:contentText key="RANK_PREFIX" code="leaderboard.label" />" tplVariable-->
                                <!--subTpl.largeRankText= <span class="large-rank-text">{{rank}}</span> subTpl-->
                                {{{rankFormatted}}}
                            </span>
                        {{/unless}}
                    </span>
                    <span class="progress-text">{{../activityTitle}} - {{score}}</span>
                    <!--
                        each leaderboard has a special CM key containing the full translated string for the "As of" text
                        the key ouput will have a {0} placeholder where the activityDate value is inserted
                        this allows the translations to have plain text and the activityDate in any order
                        we embed this CM output as a tplVariable in our leaderboardModel Handlebars template
                        we also have an activityDate subTpl embedded in our leaderboardModel Handlebars template
                        we pass the leaderboard.activityDate value from the JSON to the subTpl, then replace the {0} with the rendered output
                        the final string is assigned to leaderboard.activityDateFormatted in the JSON to be passed to the main template
                    -->
                    <!--tplVariable.activityDate= "<cms:contentText key="AS_OF" code="leaderboard.label" />" tplVariable-->
                    <!--subTpl.activityDate= <strong>{{activityDate}}</strong> subTpl-->
                    <span class="activitydate">{{{../activityDateFormatted}}}</span>
                </div><!-- /.card-details -->
            </div><!-- /.card-front -->
        </div><!-- /.card.highlightedUser -->
        {{/if}}
        {{/leaders}}

        <div class="clearfix list-container">
            <!--subTpl.leaderTpl=
            <ol class="leaders-col leaders-col-{{index}}">
                {{#leaders}}
                <li value="{{rank}}" {{#if classes.length}}class="{{#classes}}{{this}} {{/classes}}"{{/if}}>
                    <span class="score">{{score}}</span>
                    <b class="rank">{{rank}}.</b>
                    <div class='avatarwrap'>
                    {{#if avatarUrl}}
                        <img alt="{{firstName}} {{lastName}}"  src="{{avatarUrl}}" />
                    {{else}}
                        <span class=" avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                    {{/if}}
                    </div>
                    <a class="leaderName" href="#" data-participant-ids="[{{participantId}}]">{{firstName}} {{lastName}}</a>
                </li>
                {{/leaders}}
            </ol>
            subTpl-->

        </div>

    </div>
