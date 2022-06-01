<%@ include file="/include/taglib.jspf"%>

<div class="pagination pagination-right paginationControls"></div>
<table class="table table-striped">
    <thead>
        <tr>
            <th class="participant sortable string {{#eq sortedOn "lastName"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="lastName" data-sort-by="{{#eq sortedOn "lastName"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="PARTICIPANT" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="baseline sortable number {{#eq sortedOn "stepItUpBaselineAmount"}}sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="stepItUpBaselineAmount" data-sort-by="{{#eq sortedOn "stepItUpBaselineAmount"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                <a href="#">
                    <cms:contentText key="INDIVIDUAL_BASELINE" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>

            {{#each levels}}
            <th class="level sortable number" data-sort-on="{{sortedOnName}}">
                <a href="#">
                    <span>{{name}}</span>
                    <span>+{{baseline}}</span>
                    <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                </a>
            </th>
            {{/each}}
        </tr>
    </thead>

    <tfoot>
        <tr>
            <td><cms:contentText key="TOTAL" code="ssi_contest.approvals.detail"/></td>
            <td class="number">{{baselineTotal}}</td>
            {{#each levels}}
            <td class="number">{{total}}</td>
            {{/each}}
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
            <td class="baseline number">
                {{baseline}}
            </td>

            {{#each levelAmounts}}
            <td class="level number">
                {{amount}}
            </td>
            {{/each}}
        </tr>
        {{/each}}
    </tbody>
</table>

<div class="pagination pagination-right paginationControls"></div>


<!--XXXsubTpl.paginationTpl=
    NOTE: you can safely take the JSP conversion of core/base/tpl/paginationView.html and include it here. Then, remove the "XXX" above and the script will use this child template instead of the remote one
subTpl-->
