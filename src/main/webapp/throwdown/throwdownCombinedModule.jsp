<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="throwdownCombinedModuleTpl">
<!-- ======== THROWDOWN PROMO SELECT MODULE ======== -->

<div class="module-liner">

    <div class="td-promo-info">

        <div class="td-promo-select-container">
            <div class="td-promo-select-selected">
                <div class="outer">
                    <div>{{ promotion.0.promoName }}</div>
                </div>
            </div>
            <div class="down-arrow-container">
                <span class="down-arrow"></span>
            </div>
        </div>
         <ul id="promotionSelect" class="td-promo-select-list">
            {{#each promotion}}
            <li class="td-promo-select-item" data-promoid="{{ promoId }}">{{ promoName }}</li>
            {{/each}}
        </ul>

        <div class="td-promo-countdown">
            <ul class="clearfix">
                <li class="d"><span class="cd-digit">00</span>
                    <hr/>
                    <hr/><span class="cd-label"><cms:contentText key="DAYS" code="participant.throwdownstats" /></span></li>
                <li class="h"><span class="cd-digit">00</span>
                    <hr/>
                    <hr/><span class="cd-label"><cms:contentText key="HOURS" code="participant.throwdownstats" /></span></li>
                <li class="m"><span class="cd-digit">00</span>
                    <hr/>
                    <hr/><span class="cd-label"><cms:contentText key="MINUTES" code="participant.throwdownstats" /></span></li>
                <li class="s"><span class="cd-digit">00</span>
                    <hr/>
                    <hr/><span class="cd-label"><cms:contentText key="SECONDS" code="participant.throwdownstats" /></span></li>
            </ul>
            <a class="td-promo-info-link"><cms:contentText key="RULES" code="nomination.approvals.module" /><a>
        </div>
    </div> <!-- /.td-promo-info -->
        <div id="matchData"></div>

    <a href="{{ promotion.0.matchesUrl }}" class="td-view-matches">
        <div class="title-icon-view">
                <cms:contentText key="VIEW_MATCHES" code="participant.throwdownstats" />
        </div>
    </a>
<div id="allMatchesContainer" style="display:none;">
    <div class="app-row main-nav">

        <div class="app-col">
            <h3><cms:contentText key="MATCHES" code="participant.throwdownstats" /></h3>

            <ul class="nav nav-tabs allMatchesTabs">
                <li class="tabGlobal active"><a title="" href="#AllMatches" data-toggle="tab" data-original-title="<cms:contentText key="ALL" code="system.general" />" data-name-id="global"><i class="icon-internet"></i><span><cms:contentText key="GLOBAL" code="participant.throwdownstats" /></span></a></li>
                <li class=" tabTeam"><a title="" href="#TeamMatches" data-toggle="tab" data-original-title="<cms:contentText key="TEAM" code="system.general" />" data-name-id="team"><i class="icon-team-1"></i><span><cms:contentText key="TEAM" code="participant.throwdownstats" /></span></a></li>
            </ul>

        </div>

    </div>

    <div class="wide-view">
        <div class="tab-content">
            <div id="AllMatches" class="tab-pane active">
                <div id="allMatchesTab">

                    <div class="roundPagination pagination pagination-center paginationControls first"></div>

                    <div class="allMatchesTable"></div>

                </div>
            </div>

            <div id="TeamMatches" class="tab-pane">
                <div id="allMatchesTeamTab">
                    <div class="roundPagination pagination pagination-center paginationControls first"></div>

                    <div class="allMatchesTeamTable"></div>

                </div>
            </div>
        </div>
    </div>
</div>
    <!--/.wide-view-->

    <!--subTpl.roundPaginationTpl=
<ul>
    <li class="prev {{#eq currentRound "1"}} disabled {{/eq}}">
        <a>«</a>
    </li>

    <li>
        <p>Round <span class="your-round">{{currentRound}}</span> of <span class="round-total">{{totalRounds}}</span></p>

    </li>

    <li class="next {{#eq totalRounds currentRound}} disabled {{/eq}}">
        <a>»</a>
    </li>

</ul>

subTpl-->

    <!--subTpl.throwdownAllMatchesTpl=

<div class="clearBoth">
    <span class="td-fine-print">Sales by units, from {{roundStartDate}} to {{progressEndDate}}</span>
</div>

<table id="allMatchesTable" class="table table-striped">

            {{#if tabularData.meta.columns}}
            <thead>
            <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
            {{#each tabularData.meta.columns}}<th class="{{type}}" data-sort-by-id="{{id}}" {{#if colSpan}} colspan="{{colSpan}}" {{/if}}> {{name}} </th>{{/each}}
            </tr>
            </thead>
            {{/if}}

            {{#if teams}}
            <tbody>
            {{#each teams}}
            <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE }}
                <td colspan="2"><img src="{{primaryTeam.smallAvatarUrl}}" alt="{{primaryTeam.name}}" /><p><a class="profile-popover" href="" data-participant-ids="[{{primaryTeam.id}}]">{{primaryTeam.name}}</a> </p> <br /> <img src="{{secondaryTeam.smallAvatarUrl}}" alt="{{secondaryTeam.name}}" /><p><a class="profile-popover" href="" data-participant-ids="[{{secondaryTeam.id}}]">{{secondaryTeam.name}}</a></p></td>
                <td class="td-record"><p>W: <span>{{primaryTeam.stats.wins}}</span> L: <span>{{primaryTeam.stats.losses}}</span> T: <span>{{primaryTeam.stats.ties}}</span></p><br /> <p>W: <span>{{secondaryTeam.stats.wins}}</span> L: <span>{{secondaryTeam.stats.losses}}</span> T: <span>{{secondaryTeam.stats.ties}}</span></p></td>
                <td class="td-current-progress"><p>{{#if displayProgress}} {{primaryTeam.currentProgress}} {{#if primaryTeam.isWinnerFinal}}<span>W</span> {{else}} {{#if primaryTeam.isTiedFinal}}<span>T</span> {{/if}} {{/if}} {{else}} {{primaryTeam.currentProgressText}} {{/if}}</p><br /> <p>{{#if displayProgress}} {{secondaryTeam.currentProgress}} {{#if secondaryTeam.isWinnerFinal}}<span>W</span> {{else}} {{#if secondaryTeam.isTiedFinal}}<span>T</span> {{/if}} {{/if}} {{else}} {{secondaryTeam.currentProgressText}} {{/if}}</p></td>
                <td><div class="app-col chevron"><a href="{{matchUrl}}"><i class="icon-double-arrows-1-right"></i></a></div></td>
            </tr>
            {{/each}}
            </tbody>
            {{/if}}
            </table>

            <a href="{{allMatchesUrl}}">View More</a>
subTpl-->

    <!--subTpl.throwdownAllMatchesTeamTpl=

<div class="clearBoth">
    <span class="td-fine-print">Sales by units, from {{roundStartDate}} to {{progressEndDate}}</span>
</div>

<table id="allMatchesTable" class="table table-striped">

            {{#if tabularData.meta.columns}}
            <thead>
            <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
            {{#each tabularData.meta.columns}}<th class="{{type}}" data-sort-by-id="{{id}}" {{#if colSpan}} colspan="{{colSpan}}" {{/if}}> {{name}} </th>{{/each}}
            </tr>
            </thead>
            {{/if}}

            {{#if teams}}
            <tbody>
            {{#each teams}}
            <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE }}
                <td colspan="2"><img src="{{primaryTeam.smallAvatarUrl}}" alt="{{primaryTeam.name}}" /><p><a class="profile-popover" href="" data-participant-ids="[{{primaryTeam.id}}]">{{primaryTeam.name}}</a> </p> <br /><img src="{{secondaryTeam.smallAvatarUrl}}" alt="{{secondaryTeam.name}}" /><p><a class="profile-popover" href="" data-participant-ids="[{{secondaryTeam.id}}]">{{secondaryTeam.name}}</a></p></td>
                <td><p>W: <span>{{primaryTeam.stats.wins}}</span> L: <span>{{primaryTeam.stats.losses}}</span> T: <span>{{primaryTeam.stats.ties}}</span></p> <br/> <p>W: <span>{{secondaryTeam.stats.wins}}</span> L: <span>{{secondaryTeam.stats.losses}}</span> T: <span>{{secondaryTeam.stats.ties}}</span></p></td>
                <td ><p>{{#if displayProgress}} {{primaryTeam.currentProgress}} {{else}} {{primaryTeam.currentProgressText}} {{/if}}</p><br/> <p>{{#if displayProgress}} {{secondaryTeam.currentProgress}} {{else}} {{secondaryTeam.currentProgressText}} {{/if}}</p></td>
                <td><div class="app-col chevron"><a href="{{matchUrl}}"><i class="icon-double-arrows-1-right"></i></a></div></td>
            </tr>
            {{/each}}
            </tbody>
            {{/if}}
            </table>

            <a href="{{allMatchesUrl}}">View More</a>
subTpl-->

        <!--subTpl.throwdownMatchDetailsTpl=

            <div class="td-match-details-wide">

                <div class="row-fluid">

                    <div class="td-match-primary-team">
                        <div class="td-match-profile-pic">
                            <div class="avatarContainer">
                                <div class="avatarContainerBorder {{#if promotionEnded}}{{#if primaryTeam.win}}winner{{/if}}{{/if}}">
                                    <a href="{{primaryTeam.profileUrl}}"><img src="{{ primaryTeam.avatarUrl }}" alt="" class="avatar"></a>
                                </div>
                            </div>
                        </div>
                        <div class="td-match-stats-info">
                            <p class="td-match-rank">
                                {{#if primaryTeam.rank}}<a href="{{ primaryTeam.rankUrl }}">{{/if}}
                                    <cms:contentText key="RANK" code="promotion.stackrank.history"/>
                                    <span>
                                    {{#if primaryTeam.rank}}
                                        {{ primaryTeam.rank }}
                                    {{else}}
                                        <i class="icon-ban"></i>
                                    {{/if}}
                                    </span>
                                    {{#if primaryTeam.rank}}
                                        </a>
                                    {{/if}}
                            </p>
                            <ul class="td-match-wl-count">
                                <li><cms:contentText key="WIN_SHORT_FORM" code="participant.throwdownstats" />:<span>{{ primaryTeam.stats.wins }}</span></li>
                                <li><cms:contentText key="LOSS_SHORT_FORM" code="participant.throwdownstats" />:<span>{{ primaryTeam.stats.losses }}</span></li>
                                <li><cms:contentText key="TIE_SHORT_FORM" code="participant.throwdownstats" />:<span>{{ primaryTeam.stats.ties }}</span></li>
                            </ul>
                            {{#if primaryTeam.badgeInfos.0.badges}}
                            <ul class="td-match-badge-list">
                                {{#each primaryTeam.badgeInfos.0.badges}}
                                <li><img src="{{ img }}" alt="{{ name }}"><span>{{ displayCount earnCount }}</span></li>
                                {{/each}}
                            </ul>
                            {{/if}}
                        </div>
                    </div>

                    <div class="td-match-vs"> <cms:contentText key="VERSES" code="participant.throwdownstats"/> <span class="reflect"><cms:contentText key="VERSES" code="participant.throwdownstats"/> </span></div>

                    <div class="td-match-secondary-team">
                        <div class="td-match-stats-info">
                            <p class="td-match-rank">
                                {{#if secondaryTeam.rank}}<a href="{{ secondaryTeam.rankUrl }}">{{/if}}
                                    <cms:contentText key="RANK" code="promotion.stackrank.history"/>
                                    <span>
                                        {{#if secondaryTeam.rank}}
                                        {{ secondaryTeam.rank }}
                                    {{else}}
                                        <i class="icon-ban"></i>
                                    {{/if}}
                                    </span>
                                    {{#if secondaryTeam.rank}}
                                        </a>
                                    {{/if}}
                            </p>
                            <ul class="td-match-wl-count">
                            <li><cms:contentText key="WIN_SHORT_FORM" code="participant.throwdownstats" />:<span>{{ secondaryTeam.stats.wins }}</span></li>
                            <li><cms:contentText key="LOSS_SHORT_FORM" code="participant.throwdownstats" />:<span>{{ secondaryTeam.stats.losses }}</span></li>
                            <li><cms:contentText key="TIE_SHORT_FORM" code="participant.throwdownstats" />:<span>{{ secondaryTeam.stats.ties }}</span></li>
                            </ul>
                            {{#if secondaryTeam.badgeInfos.0.badges}}
                            <ul class="td-match-badge-list">
                                {{#each secondaryTeam.badgeInfos.0.badges}}
                                <li><img src="{{ img }}" alt="{{ name }}"><span>{{ displayCount earnCount }}</span></li>
                                {{/each}}
                            </ul>
                            {{/if}}
                        </div>
                        <div class="td-match-profile-pic">
                            <div class="avatarContainer">
                                <div class="avatarContainerBorder {{#if promotionEnded}}{{#if secondaryTeam.win}}winner{{/if}}{{/if}}">
                                    {{#if secondaryTeam.profileUrl}}<a href="{{secondaryTeam.profileUrl}}">{{/if}}<img src="{{ secondaryTeam.avatarUrl }}" alt="" class="avatar">{{#if secondaryTeam.profileUrl}}</a>{{/if}}
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="row-fluid">

                        <div class="td-match-primary-units">
                            <p class="td-match-participant-name">{{ primaryTeam.name }}</p>
                            <p class="td-match-participant-units">
                                {{#if primaryTeam.isMyTeam}}
                                {{comma primaryTeam.currentProgress}}
                                {{else}}
                                {{#if primaryTeam.displayProgress}}
                                {{comma primaryTeam.currentProgress}}
                                {{else}}
                                {{primaryTeam.progressVerbiage}}
                                {{/if}}
                                {{/if}}
                            </p>
                            {{#if promotionEnded}}
                                {{#if primaryTeam.win}}
                                    <p class="td-match-winner">{{ primaryTeam.outcomeForDisplayFullForm }}</p>
                                {{/if}}
                            {{else}}
                            {{#if primaryTeam.tie}}
                            <p class="td-match-winner">{{ primaryTeam.outcomeForDisplayFullForm }}</p>
                        {{else}}
                            {{#unless primaryTeam.displayProgress}}
                                {{#if primaryTeam.isMyTeam}}
                                    <span class="td-progress-text">{{primaryTeam.progressVerbiage}}</span>
                                {{/if}}
                            {{/unless}}
                            {{/if}}
                            {{/if}}
                        </div>

                        <div class="td-match-round-info">
                            <p><cms:contentText key="ROUND" code="promotion.stackrank.history"/> <span class="td-match-round-current">{{ roundNumber }}</span> <cms:contentText key="OF" code="promotion.stackrank.history"/> <span>{{ numberOfRounds }}</span></p>
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                            <a href="{{ matchUrl }}" class="btn td-match-button">
                            <c:choose>
                                <c:when test="${matchDetail=='Y'}">
                                <cms:contentText code="participant.throwdownstats" key="VIEW_MATCH_LIST" />
                                </c:when>
                                <c:otherwise>
                                {{#if smackTalkAvailable}}
                                      <cms:contentText code="participant.throwdownstats" key="SMACK_TALK" />
                                 {{else}}
                                       <cms:contentText code="participant.throwdownstats" key="MATCH_DETAILS" />
                                   {{/if}}
                                </c:otherwise>
                            </c:choose>
                            </a>
                            </beacon:authorize>
                        </div>

                        <div class="td-match-secondary-units">
                            <p class="td-match-participant-name">{{ secondaryTeam.name }}</p>
                            <p class="td-match-participant-units">
                            {{#if secondaryTeam.isMyTeam}}
                            {{secondaryTeam.currentProgress}}
                            {{else}}
                                {{#if secondaryTeam.displayProgress}}
                                {{comma secondaryTeam.currentProgress}}
                                {{else}}
                                {{secondaryTeam.progressVerbiage}}
                                {{/if}}
                                {{/if}}
                            </p>
                            {{#if promotionEnded}}
                                {{#if secondaryTeam.win}}
                                    <p class="td-match-winner">{{ secondaryTeam.outcomeForDisplayFullForm }}</p>
                                    {{else}}
                                    {{#if secondaryTeam.tie}}
            							<p class="td-match-winner">{{ secondaryTeam.outcomeForDisplayFullForm }}</p>
                                    {{else}}
                                    {{#unless primaryTeam.displayProgress}}
            							<span class="td-progress-text">{{ secondaryTeam.progressVerbiage }}</span>
                                    {{/unless}}
                                {{/if}}
                            {{/if}}
                        {{/if}}
                        </div>

                    </div>

                    <p class="td-fine-print">{{promotionOverview}}, {{#if isProgressLoaded}}<cms:contentText code="participant.throwdownstats" key="FROM" /> {{roundStartDate}} <cms:contentText code="participant.throwdownstats" key="TO" /> {{asOfDate}}
                    {{else}} {{#if roundCompleted}}<cms:contentTemplateText code="participant.throwdownstats" key="AS_OF" args="{{roundEndDate}}" />{{else}} <cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" />
                    {{/if}}
                    {{/if}}
                </p>

            </div>

            <div class="td-match-details-condensed">

                <div class="td-match-primary-team">
                    <div class="td-match-profile-pic">
                        <div class="avatarContainer">
                            <div class="avatarContainerBorder {{#if promotionEnded}}{{#if primaryTeam.win}}winner{{/if}}{{/if}}">
                                <img src="{{ primaryTeam.avatarUrl }}" alt="" class="avatar">
                            </div>
                        </div>
                    </div>
                    <div class="td-match-stats-info">
                        <p class="td-match-participant-name">{{ primaryTeam.name }}</p>
                        <p class="td-match-rank">
                            {{#if primaryTeam.rank}}<a href="{{ primaryTeam.rankUrl }}">{{/if}}
                                <cms:contentText key="RANK" code="promotion.stackrank.history"/>
                                <span>
                                {{#if primaryTeam.rank}}
                                    {{ primaryTeam.rank }}
                                {{else}}
                                    <i class="icon-ban-circle"></i>
                                {{/if}}
                                {{#if primaryTeam.rank}}</a>{{/if}}
                                </span>
                        </p>
                        <p class="td-match-participant-units">
                            {{#if primaryTeam.isMyTeam}}
                            {{comma primaryTeam.currentProgress}}
                            {{else}}
                            {{#if primaryTeam.displayProgress}}
                            {{comma primaryTeam.currentProgress}}
                            {{else}}
                            {{primaryTeam.currentProgressText}}
                            {{/if}}
                            {{/if}}
                        </p>
                        {{#if promotionEnded}}
                            {{#if primaryTeam.win}}
                                <p class="td-match-winner">{{ primaryTeam.outcomeForDisplayFullForm }}</p>
                            {{/if}}
                        {{else}}
                        {{#if primaryTeam.tie}}
							<p class="td-match-winner">{{ primaryTeam.outcomeForDisplayFullForm }}</p>
                        {{else}}
                        {{#unless primaryTeam.displayProgress}}
							<span class="td-progress-text">{{ primaryTeam.progressVerbiage }}</span>
                        {{/unless}}
                        {{/if}}
                        {{/if}}
                    </div>
                </div>

                <div class="td-match-vs"> <cms:contentText key="VERSES" code="participant.throwdownstats"/> </div>

                <div class="td-match-secondary-team">
                    <div class="td-match-profile-pic">
                        <div class="avatarContainer">
                            <div class="avatarContainerBorder {{#if promotionEnded}}{{#if secondaryTeam.win}}winner{{/if}}{{/if}}">
                                <img src="{{ secondaryTeam.avatarUrl }}" alt="" class="avatar">
                            </div>
                        </div>

                    </div>
                    <div class="td-match-stats-info">
                        <p class="td-match-participant-name">{{ secondaryTeam.name }}</p>
                        <p class="td-match-rank">
                                                    {{#if secondaryTeam.rank}}<a href="{{ secondaryTeam.rankUrl }}">{{/if}}
                                <cms:contentText key="RANK" code="promotion.stackrank.history"/>
                                <span>
                                    {{#if secondaryTeam.rank}}
                                    {{ secondaryTeam.rank }}
                                {{else}}
                                    <i class="icon-ban-circle"></i>
                                {{/if}}
                                {{#if secondaryTeam.rank}}</a>{{/if}}
                                </span>
                        </p>
                        <p class="td-match-participant-units">
                        {{#if secondaryTeam.isMyTeam}}
                        {{secondaryTeam.currentProgress}}
                        {{else}}
                            {{#if secondaryTeam.displayProgress}}
                            {{comma secondaryTeam.currentProgress}}
                            {{else}}
                            {{secondaryTeam.currentProgressText}}
                            {{/if}}
                            {{/if}}
                        </p>
                        {{#if promotionEnded}}
                        {{#if secondaryTeam.win}}
                            <p class="td-match-winner">{{ secondaryTeam.outcomeForDisplayFullForm }}</p>
                        {{else}}
                        {{#if secondaryTeam.tie}}
                            <p class="td-match-winner">{{ secondaryTeam.outcomeForDisplayFullForm }}</p>
                        {{else}}
                        {{#unless primaryTeam.displayProgress}}
                            <span class="td-progress-text">{{ secondaryTeam.progressVerbiage }}</span>
                        {{/unless}}
                        {{/if}}
                        {{/if}}
                        {{/if}}
                    </div>

                </div>

                <div class="td-match-round-info">
                <p> <cms:contentText key="ROUND" code="promotion.stackrank.history"/> <span class="td-match-round-current">{{ roundNumber }}</span> <cms:contentText key="OF" code="promotion.stackrank.history"/> <span>{{ numberOfRounds }}</span></p>
                <beacon:authorize ifNotGranted="LOGIN_AS">
                <a href="{{ matchUrl }}" class="btn td-match-button">
                    <c:choose>
                        <c:when test="${matchDetail=='Y'}">
                        <cms:contentText code="participant.throwdownstats" key="VIEW_MATCH_LIST" />
                        </c:when>
                        <c:otherwise>
                        <cms:contentText code="participant.throwdownstats" key="MATCH_DETAILS" />
                        </c:otherwise>
                    </c:choose>
                </a>
                </beacon:authorize>
                </div>
            </div>

        subTpl-->

</div> <!-- /.module-liner -->
</script>
