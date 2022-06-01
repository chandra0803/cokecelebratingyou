<%@ include file="/include/taglib.jspf"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<button class="btn btn-inverse btn-mini" id="participantProfileMobileToggle" data-toggle="button">
    <i class="icon-plus"></i><i class="icon-minus"></i>
</button>

{{#if avatarUrl}}
    <a class="profile-pic-wrapper"
        {{#unless isDelegate}}
            href="layout.html?tpl=profilePage&amp;tplPath=apps/profile/tpl/"
        {{/unless}}>
        <img class="profile-pic avatar" src="{{#timeStamp avatarUrl}}{{/timeStamp}}" />
    </a>
{{/if}}

<div class="profile-meta">

    <div class="profile-head">
        {{#if isDelegate}}
        <span class="profile-before-delegator-name">
            <cms:contentText key="ACTING_AS" code="profile.proxies.tab" />
        </span>
        <span class="profile-name">{{firstName}} {{lastName}}</span>
        {{else}}
        <a href="layout.html?tpl=profilePage&amp;tplPath=apps/profile/tpl/">
            <span class="profile-name">{{firstName}} {{lastName}}</span>
        </a>
        {{/if}}
    </div>

    <div class="profile-body">
        {{#if showPoints}}
            <span class="profile-points">
                {{#if points}}
                    {{formatNumber points}}{{else}}&nbsp;
                {{/if}}
            </span>
            <span class="profile-points-label">
                <cms:contentText key="POINTS" code="report.challengepoint.achievement" />
            </span>
        {{/if}}

        {{#if isDelegate}}
            <a class="profile-switch-back" href="layout.html?tplPath=base/tpl/&amp;tpl=modulesPage.html">
                <cms:contentText key="SWITCH_TO" code="profile.proxies.tab" />  {{delegateFirstName}} {{delegateLastName}}
            </a>
        {{/if}}
    </div>

    <div class="profile-foot">
        {{#if largeAudience}}
            <!--
            This href will need to be changed -->
            <a href="layout.html?tplPath=base/tpl/&amp;tpl=participantAdvancedSearchPage.html" class="profile-gps-trigger"><i class="icon-search"></i><cms:contentText key="FIND_OTHERS" code="profile.proxies.tab" /></a>
        {{else}}
            <a href="#" class="profile-gps-trigger"><i class="icon-search"></i><cms:contentText key="FIND_OTHERS" code="profile.proxies.tab" /></a>
        {{/if}}

        {{#if delegators}}
        	{{#if isDelegate}}
					<a href="#" class="profile-act-as-delegate"><i class="icon-group"></i><cms:contentText key="CHANGE_DELEGATE" code="profile.proxies.tab" /></a>
				 {{else}}
                	<a href="#" class="profile-act-as-delegate"><i class="icon-group"></i><cms:contentText key="ACT_AS_DELEGATE" code="profile.proxies.tab" /></a>
			{{/if}}
            <div class="profile-delegators" style="display:none">
                <!--
                    Java: if the 'act as delegate' URL is insufficient
                    for your needs, please let me know. (Aaron Stricker)
                -->
                {{#delegators}}
                <a data-delegator-id="{{id}}"
                    {{#unless active}}href="${pageContext.request.contextPath}{{delegatorUrl}}"{{/unless}}
                    class="{{#if active}} active {{/if}}" >
                    {{firstName}} {{lastName}}
                </a>
                {{/delegators}}
            </div>
        {{/if}}

        {{#unless isDelegate}}
        <a href="layout.html?tpl=profilePage&amp;tplPath=apps/profile/tpl/#profile/AlertsAndMessages" class="profile-messages">
            <i class="icon-g5-alerts"></i>
            <span class="profile-alerts-text"><cms:contentText key="ALERTS" code="promotion.nomination.submit" /></span>
            <span class="profile-num-messages">{{numMessages}}</span>
        </a>
        {{/unless}}

        {{#unless isDelegate}}
        <a href="#"><i class="icon-g5-admin"></i><cms:contentText key="ADMIN" code="participant.myaccount" /></a>
        {{/unless}}

        <a href="layout.html?tplPath=base/tpl/&amp;tpl=loginPage.html" class="profile-logout">
            <i class="icon-signout"></i>
            <cms:contentText key="LOGOUT" code="participant.myaccount" />
        </a>
    </div><!-- /.profile-foot -->

</div><!-- /.profile-meta -->

<script>
$(document).ready( function() {

	G5.props.URL_JSON_PARTICIPANT_PROFILE_NOMINATION_WINNER_MODAL = G5.props.URL_ROOT+'nomination/ajax/participantProfileNominationWinnerModal.json';//curren active user's alerts

});
</script>


