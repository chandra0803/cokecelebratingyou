<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.DateUtils"%>

        <div class="td-match-details-wide">

            <div class="row-fluid">

            <div class="td-match-primary-team">
	                <div class="td-match-profile-pic">
	                    <div class="avatarContainer">
	                        <div class="avatarContainerBorder {{#if primaryTeam.win}}winner{{/if}}">
	                            {{#if primaryTeam.profileUrl}}<a href="{{primaryTeam.profileUrl}}">{{/if}}<img src="{{ primaryTeam.avatarUrl }}" alt="" class="avatar">{{#if primaryTeam.profileUrl}}</a>{{/if}}
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
							<i class="icon-ban-circle"></i>
						{{/if}}
						{{#if primaryTeam.rank}}</a>{{/if}}
						</span>
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

                <div class="td-match-vs"> <cms:contentText key="VERSES" code="participant.throwdownstats"/> </div>

                <div class="td-match-secondary-team">
                    <div class="td-match-stats-info">
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
	                        <div class="avatarContainerBorder {{#if secondaryTeam.win}}winner{{/if}}">
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
                        {{primaryTeam.currentProgress}}
                        {{else}}
                        {{#if primaryTeam.displayProgress}}
                        {{primaryTeam.currentProgress}}
                        {{else}}
                        {{/if}}
                        {{/if}}
                    </p>
                        {{#if primaryTeam.win}}
							<p class="td-match-winner">{{ primaryTeam.outcomeForDisplayFullForm }}</p>
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
                        {{secondaryTeam.currentProgress}}
                        {{else}}
                        {{/if}}
                        {{/if}}
                    </p>
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
	                        <div class="avatarContainerBorder {{#if primaryTeam.win}}winner{{/if}}">
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
                        {{primaryTeam.currentProgress}}
                        {{else}}
                        {{#if primaryTeam.displayProgress}}
                        {{primaryTeam.currentProgress}}
                        {{else}}
                        {{/if}}
                        {{/if}}
                    </p>
                        {{#if primaryTeam.win}}
							<p class="td-match-winner">{{ primaryTeam.outcomeForDisplayFullForm }}</p>
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
                        <div class="avatarContainerBorder {{#if secondaryTeam.win}}winner{{/if}}">
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
                        {{secondaryTeam.currentProgress}}
                        {{else}}
                        {{/if}}
                        {{/if}}
                    </p>
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
                </div>

            </div> <!-- /.td-match-secondary-team -->


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
