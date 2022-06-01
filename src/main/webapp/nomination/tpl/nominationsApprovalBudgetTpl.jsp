<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

{{debug}}
{{#tabularData}}
{{#if budgetBalance}}
<div class="approvalBudgetWrap">
    <h4>{{budgetPeriod}} <cms:contentText key="BUDGET_BALANCE" code="nomination.approvals.module"/></h4>

    <span class="currentBalance large-text">
    {{#if budgetBalance}}
        {{budgetBalance}}
        {{#if ../promotion.currencyLabel}}
            {{../promotion.currencyLabel}}
        {{/if}}

        {{#eq ../promotion.payoutType "points"}}
            <cms:contentText key="POINTS" code="nomination.approvals.module"/>
        {{else}}

        {{/eq}}
    {{/if}}
    </span>

    {{#if potentialAwardExceeded}}
    <a class="budgetIncreaseLink" data-popover-content="budgetIncrease"><cms:contentText key="REQUEST_BUDGET_INCREASE" code="nomination.approvals.module"/></a>
    {{/if}}

    {{#if showConversionLink}}
    <a class="conversionLinkTrigger popoverTrigger" data-popover-content="conversionInfo"> | <cms:contentText key="CONVERSION_INFORMATION" code="nomination.approvals.module"/></a>
    {{/if}}

    {{#if lastBudgetRequestDate}}
    <span class="budgetTimestamp"><cms:contentText key="LAST_REQUEST_ON" code="nomination.approvals.module"/> {{lastBudgetRequestDate}}</span>
    {{/if}}

</div>
{{/if}}

<div class="approverLevelsWrap">
    {{#if previousApprovers}}
    <div class="approverLevelPrevious approverLevel">
        <h5><cms:contentText key="PREVIOUS" code="nomination.approvals.module"/> {{#if previousLevelName}} {{previousLevelName}} {{else}} <cms:contentText key="LEVEL" code="nomination.approvals.module"/> {{/if}}</h5>

        {{#if pendingNominations}}
            <p>
                <span class="large-text">{{pendingNominations}}</span>
                <cms:contentText key="PENDING_NOMINATION" code="nomination.approvals.module"/>
            </p>
        {{/if}}

        <a class="viewApproverList"><cms:contentText key="VIEW" code="nomination.approvals.module"/> {{previousApproverCount}} <cms:contentText key="APPROVERS" code="nomination.approvals.module"/></a>

        <div class="approverListTooltip" style="display: none">
            <ul>
                {{#each previousApprovers}}
                <li>{{name}}</li>
                {{/each}}
            </ul>
        </div>
    </div>
    {{/if}}

    {{#unless finalLevelApprover}}
        {{#if nextApprovers}}
        <div class="approverLevelNext {{#if previousApprovers}}approverLevel {{else}}approverLevel singleLevel{{/if}}">
            <h5><cms:contentText key="NEXT" code="nomination.approvals.module"/> {{#if nextLevelName}} {{nextLevelName}} {{else}} <cms:contentText key="LEVEL" code="nomination.approvals.module"/> {{/if}}</h5>

            <a class="viewApproverList"><cms:contentText key="VIEW" code="nomination.approvals.module"/> {{nextApproverCount}} <cms:contentText key="APPROVERS" code="nomination.approvals.module"/></a>

            <div class="approverListTooltip" style="display: none">
                <ul>
                    {{#each nextApprovers}}
                    <li>{{name}}</li>
                    {{/each}}
                </ul>
            </div>
        </div>
        {{/if}}
    {{/unless}}
</div>
{{/tabularData}}
