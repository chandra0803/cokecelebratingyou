<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<%-- TODO: add CM key --%>
<h2><cms:contentText key="MY_BADGES" code="gamification.admin.labels" /></h2>

<div class="badge-groups">
    {{#badgeGroups}}
        <div class="badge-group">
            <h3 class="badge-group-title">{{this.headerTitle}}</h3>
              {{#if this.showProgress}}
                {{#if this.allBehaviorPoints}}
                	<p><cms:contentTemplateText code="gamification.admin.labels" key="ALL_BEHAVIOR_POINTS_MSG" args="{{this.allBehaviorPoints}}" /></p>
            	{{/if}}
            	  {{/if}}
            <div class="badges">
                <ul>
                    {{#badges}}
                        <li class="badge-item {{this.type}}-type earned-{{#if earned}}true{{else}}false{{/if}}" id="badge{{id}}">
                            <img src="<%=RequestUtils.getBaseURI(request)%>/{{this.img}}" align="left" />
                            <div class="badge-name{{#if howToEarnText}} hasHowToEarnText{{/if}}{{#if this.progressVisible}} hasProgressBar{{/if}}{{#if dateEarned}} hasWasEarned{{/if}}{{#if contestName}} hasContestName{{/if}}">
                            {{this.name}}

                            {{#if earnCount}}
                                ({{this.earnCount}})
                            {{/if}}
                            </div>
                            {{#if howToEarnText}}
                            <div class="badge-how-to-earn">{{this.howToEarnText}}</div>
                            {{/if}}
                            {{#if this.progressVisible}}
                            <div class="progress">
                                <div class="bar">{{this.progressNumerator}}/{{this.progressDenominator}}</div>
                            </div>
                            {{/if}}
                            {{#if contestName}}
                                <div class="badge-contest-name">{{this.contestName}}</div>
                            {{/if}}
                            {{#if earned}}
                            <div class="badge-date-earned"><cms:contentText key="EARNED_DATE" code="gamification.admin.labels" /> {{this.dateEarned}}</div>
                            {{/if}}
                        </li>
                    {{/badges}}
                </ul>
            </div>
        </div>
    {{/badgeGroups}}
    {{#unless badgeGroups}}
        <p class="alert alert-error"><cms:contentText key="NO_BADGES_TO_VIEW" code="gamification.validation.messages" /><p>
    {{/unless}}
</div>

