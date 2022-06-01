<%@ include file="/include/taglib.jspf"%>

<div class="promotionItem {{#if ua}}ua{{/if}} {{#if uaconnected}}ua-connected{{/if}} {{promotionItemClass}} {{#eq promotionItemClass "multiple"}}card{{/eq}} {{status}}" data-promotion-id="{{id}}">
    {{#if ua}}<div class="innerGradient" />{{/if}}
    {{#goals}}

        <div class="goalItem {{#if noGoal}}gqNoLevel{{/if}}" data-goal-id="{{id}}">

            <h4 class="promotionTitle">
				{{#if ../honeycombProgram}}
					<a href="{{rulesLink}}" target="_blank" class="promotionRulesHoneycomb">
						 <cms:contentText key="READ_RULES" code="promotion.partner.goal.selection"/>
					</a>
                {{else}}
					<a href="{{rulesLink}}" class="promotionRules">
						 <cms:contentText key="READ_RULES" code="promotion.partner.goal.selection"/>
					</a>
				{{/if}}

                <strong>{{../name}}</strong>
            </h4>

            {{#if isPartner}}
            <p class="subTitle partner">
                <cms:contentText key="PARTNERED_WITH" code="promotion.partner.goal.selection"/>
                <span class="partnerName">
                {{participant.firstName}} {{participant.lastName}}
                </span>
            </p>
            {{/if}}

            {{#or goalStarted goalEnded}}
                {{#if showProgress}}
                    {{#unless showNoGoal}}
                    <div class="progressUnit">
                        <p class="asOf"><cms:contentTemplateText key="PROGRESS_AS_OF" code="promotion.partner.goal.selection" args="{{progressDate}}"/></p>
                        <div class="progress progress-tip{{#if goalWin}} progress-success{{/if}}{{#if goalFail}} progress-danger{{/if}}" class="goalQuestProgessBarWrapper" data-value="{{progressValue}}%" data-status="{{#if goalWin}}success{{/if}}{{#if goalFail}}fail{{/if}}">
                            {{#if percentageExceeds}}
                            <div class="bar" style="width: 100%;"><!-- {{progressValue}}% --></div>
                            {{else}}
                            <div class="bar" style="width: 0;"><!-- {{progressValue}}% --></div>
                            {{/if}}
                        </div>
                    </div>
                    {{/unless}}
                {{/if}}
            {{/or}}

            {{#if goalLevel}}
            <p class="subTitle">
                <!-- <span class="levelChooseVisual">
                    <i class="levelChosen visualItem icon-check-circle"></i>
                </span> -->
                <span class="levelLabelName">
                    {{#if isPartner}}
                    <span class="levelLabel"><cms:contentText key="GOAL" code="promotion.goalquest.selection.wizard"/></span>
                    {{else}}
                    <span class="levelLabel"><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/>:</span>
                    {{/if}}
                    <span class="levelName">{{goalLevel.name}} </span>
                </span>
                <span class="levelDesc">{{goalLevel.description}}</span>
            </p>
            {{/if}}

            <p class="statusTxt">

                {{#if goalOpen}}
                {{#if noGoal}}
                {{#if canChange}}
                <span class="levelChooseVisual">
                    <i class="selectIcn visualItem icon-calendar"></i>
                </span>
                <span class="selectTxt">
                    <cms:contentText key="SELECT_GOAL_BY" code="promotion.partner.goal.selection"/>
                    {{../startDate}}
                </span>
                {{/if}}
                {{/if}}
                {{/if}}

                {{#if showNoGoal}}
                <span class="levelChooseVisual">
                    <i class="sorryIcn visualItem icon-cancel-circle"></i>
                </span>
                <span class="sorryTxt">
                    <cms:contentText key="NOT_SELECTED" code="promotion.partner.goal.selection"/>
                </span>
                {{/if}}

                {{#if goalFail}}
                <span class="levelChooseVisual">
                    <i class="notAchieveIcn visualItem icon-cancel-circle"></i>
                </span>
                <span class="notAchieveTxt">
                    {{#if isPartner}}
                    <cms:contentText key="NOT_ACHIEVED" code="promotion.partner.goal.selection"/>
                    {{else}}
                    <cms:contentText key="GOAL_NOT_ACHIEVED" code="promotion.partner.goal.selection"/>
                    {{/if}}
                </span>
                {{/if}}

                {{#if goalWin}}
                <span class="levelChooseVisual">
                    <i class="achieveIcn visualItem icon-verification"></i>
                </span>
                <span class="achieveTxt">
                    {{#if isPartner}}
                    <cms:contentText key="GOAL_ACHIEVED" code="promotion.partner.goal.selection"/>
                    {{else}}
                    <cms:contentText key="ACHIEVED_GOAL" code="promotion.partner.goal.selection"/>
                    {{/if}}
                </span>
                {{/if}}

            </p>

            <span class="btnWrap">
                {{#if showBtn_Rules}}
                <!-- <a href="{{rulesLink}}" class="btn btn-primary">
                    <cms:contentText key="READ_RULES" code="promotion.partner.goal.selection"/>
                </a> -->
                {{/if}}

				<beacon:authorize ifNotGranted="LOGIN_AS">
                {{#if showBtn_Select}}
                <a href="{{selectGoalLink}}" {{#if ../honeycombProgram}}target="_blank"{{/if}} class="btn btn-primary">
                    <cms:contentText key="SELECT_GOAL" code="promotion.partner.goal.selection"/>
                </a>
                {{/if}}

                {{#if showBtn_Change}}
                <a href="{{selectGoalLink}}" {{#if ../honeycombProgram}}target="_blank"{{/if}} class="btn btn-primary">
                    <cms:contentText key="CHANGE_GOAL" code="promotion.partner.goal.selection"/>
                </a>
                {{/if}}
				</beacon:authorize>

                {{#if showBtn_Progress}}
                <a href="{{progressLink}}" {{#if ../honeycombProgram}}target="_blank"{{/if}} class="btn btn-primary btn-inverse">
                    <cms:contentText key="VIEW_PROGRESS" code="promotion.partner.goal.selection"/>
                </a>
                {{/if}}

                {{#if showBtn_View}}
                <a href="{{resultsLink}}" {{#if ../honeycombProgram}}target="_blank"{{/if}} class="btn btn-primary btn-inverse">
                    <cms:contentText key="VIEW_RESULTS" code="promotion.partner.goal.selection"/>
                </a>
                {{/if}}
            </span>

        </div><!-- /.goalItem -->

    {{/goals}}

    {{#if ua}}
        <div class="ua-footer">
            <div class="ua-logo">
                <span></span><!-- logo goes here-->
            </div>
            <div class="ua-connect-status">
                <div class="ua-widget {{#if uaconnected}}connected{{/if}}">
                    <div class="ua-dot" {{#if uaconnected}}data-title-connect-status="Connected"{{else}} data-title-connect-status="Not Connected"{{/if}}>
                        {{#if uaconnected}}
                            <i class="icon icon-link-2"></i>
                            {{else}}
                            <i class="icon icon-link-3-broken"></i>
                        {{/if}}
                    </div>
                </div>
            </div>
        </div>
    {{/if}}

</div>
