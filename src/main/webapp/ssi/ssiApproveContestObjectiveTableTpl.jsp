<%@ include file="/include/taglib.jspf"%>
<div class="pagination pagination-right paginationControls"></div>

<table class="table table-striped">
    <thead>
        <tr>
            <th rowspan="2" class="participant sortable string {{#eq sortedOn "lastName"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="lastName" data-sort-by="{{#eq sortedOn "lastName"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="PARTICIPANT" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th rowspan="2" class="objective sortable string {{#eq sortedOn "activityDescription"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="activityDescription" data-sort-by="{{#eq sortedOn "activityDescription"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="OBJECTIVES_DESCRIPTION" code="ssi_contest.preview"/>
                    <i class="icon-info ssiObjectiveInfo"></i>&nbsp;
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>

            <th {{#eq payoutType "points"}}colspan="2"{{else}}colspan="3"{{/eq}} class="objective header">
                <cms:contentText key="OBJECTIVE" code="ssi_contest.approvals.detail"/>
            </th>

            {{#if includeBonus}}
            <th colspan="3" class="bonus header">
                <cms:contentText key="BONUS" code="ssi_contest.approvals.detail"/>
            </th>
            {{/if}}
        </tr>
        <tr>
            <th class="objectiveAmount sortable number {{#eq sortedOn "objectiveAmount"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectiveAmount" data-sort-by="{{#eq sortedOn "objectiveAmount"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="AMOUNT" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>

           {{#eq payoutType "points"}}
           <th class="objectivePayout sortable number {{#eq sortedOn "objectivePayout"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectivePayout" data-sort-by="{{#eq sortedOn "objectivePayout"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="PAYOUT" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
           </th>
           {{else}}
           <th class="objectivePayoutDescription sortable string {{#eq sortedOn "objectivePayoutDescription"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectivePayoutDescription" data-sort-by="{{#eq sortedOn "objectivePayoutDescription"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="objectiveValue sortable number {{#eq sortedOn "objectivePayout"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectivePayout" data-sort-by="{{#eq sortedOn "objectivePayout"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="VALUE" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
           {{/eq}}

           {{#if includeBonus}}
            <th class="bonusForEvery sortable number {{#eq sortedOn "objectiveBonusIncrement"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectiveBonusIncrement" data-sort-by="{{#eq sortedOn "objectiveBonusIncrement"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="FOR_EVERY" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="bonusPayout sortable number {{#eq sortedOn "objectiveBonusPayout"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectiveBonusPayout" data-sort-by="{{#eq sortedOn "objectiveBonusPayout"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="PAYOUT" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="bonusPayoutCap sortable number {{#eq sortedOn "objectiveBonusCap"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="objectiveBonusCap" data-sort-by="{{#eq sortedOn "objectiveBonusCap"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="PAYOUT_CAP" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
           {{/if}}
        </tr>
    </thead>

    <tfoot>
        <tr>
            <td colspan="2"><cms:contentText key="TOTALS" code="ssi_contest.approvals.detail"/></td>
            <td class="objectiveAmountTotal number">{{objectiveAmountTotal}}</td>

            {{#eq payoutType "points"}}
            <td class="objectivePayoutTotal number">{{payoutTotal}}</td>

            {{#if includeBonus}}
            <td>&nbsp;</td>
            <td class="bonusPayoutTotal number">{{bonusPayoutTotal}}</td>
            <td class="bonusPayoutCapTotal number">{{bonusPayoutCapTotal}}</td>
            {{/if}}

            {{else}}
            <td>&nbsp;</td>
            <td class="objectiveValueTotal number">{{payoutTotal}}</td>
            {{/eq}}
        </tr>
    </tfoot>

    <tbody>
        {{#each results}}
        <tr>
            <td class="participantName">
                <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                    {{lastName}}, {{firstName}}
                </a>
            </td>
            <td class="objective">
                {{objective.name}}
            </td>
            <td class="objectiveAmount number">
                {{objective.amount}}
            </td>

            {{#eq ../payoutType "points"}}
                <td class="objectivePayout number">
                    {{objective.payoutAmount}}
                </td>
                {{#if ../includeBonus}}
                <td class="bonusForEvery number">
                    {{bonus.forEvery}}
                </td>
                <td class="bonusPayout number">
                    {{bonus.payoutAmount}}
                </td>
                <td class="bonusPayoutCap number">
                    {{bonus.payoutCap}}
                </td>
                {{/if}}

                {{else}}
                <td class="objectivePayoutDesc">
                	{{objective.payoutDescription}}
                </td>
                <td class="objectiveValue number">
                    {{objective.payoutAmount}}
                </td>
            {{/eq}}

        </tr>
        {{/each}}
    </tbody>
</table>

<div class="pagination pagination-right paginationControls"></div>


<!--XXXsubTpl.paginationTpl=
    NOTE: you can safely take the JSP conversion of core/base/tpl/paginationView.html and include it here. Then, remove the "XXX" above and the script will use this child template instead of the remote one
subTpl-->

<div class="ssiObjectiveInfoPopover" style="display: none">
    <p><cms:contentText key="OBJ_INFO" code="ssi_contest.approvals.detail"/></p>
</div>
