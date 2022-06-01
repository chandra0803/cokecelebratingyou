<%@ include file="/include/taglib.jspf"%>
<span>{{orgType}}</span>

<table class="table table-striped">
    <thead>
        <tr>
            <th><cms:contentText key="RANK" code="ssi_contest.approvals.detail"/></th>
            {{#eq payoutType "points"}}
            <th><cms:contentText key="PAYOUT" code="ssi_contest.approvals.detail"/></th>
            {{else}}
            <th><cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.detail"/></th>
            <th><cms:contentText key="VALUE" code="ssi_contest.approvals.detail"/></th>
            {{/eq}}
            {{#if hasBadge}}
            <th><cms:contentText key="BADGE" code="ssi_contest.approvals.detail"/></th>
            {{/if}}
        </tr>
    </thead>
    <tfoot>
        <tr>
            <td {{#eq payoutType "other"}}colspan="2"{{/eq}}><cms:contentText key="TOTAL" code="ssi_contest.approvals.detail"/></td>
            <td>{{payoutTotal}}</td>
            {{#if hasBadge}}<td>&nbsp;</td>{{/if}}
        </tr>
    </tfoot>
    <tbody>
        {{#each ranks}}
        <tr>
            <td>{{number}}</td>
            {{#eq ../payoutType "points"}}
            <td>{{payoutAmount}}</td>
            {{else}}
            <td>{{payoutDescription}}</td>
            <td>{{payoutAmount}}</td>
            {{/eq}}
            {{#if ../hasBadge}}
            <td>
                {{#if badge.url}}<img src="{{badge.url}}" alt="{{badge.name}}" /> {{badge.name}}{{/if}}
            </td>
            {{/if}}
        </tr>
        {{/each}}
    </tbody>
</table>
