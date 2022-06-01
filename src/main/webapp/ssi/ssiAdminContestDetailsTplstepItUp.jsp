<%@ include file="/include/taglib.jspf"%>
<div class="row-fluid">
    <div class="span12 no-pointer-event">
        <h3 class="sectionTitle"><cms:contentText key="PAX" code="ssi_contest.creator"/></h3>
    </div>
</div>

<div class="row-fluid">
    <div class="span12">
        <div class="pagination pagination-right paginationControls"></div>

        <table id="ssiContestDetailsTable" class="table table-striped">
            {{#if tabularData.columns}}
            <thead>
                <tr>
                    {{#each tabularData.columns}}
                    <th class="{{name}} {{type}} {{#if sortable}}sortable{{/if}} {{#eq ../sortedOn name}}sorted {{../sortedBy}}{{else}}asc{{/eq}}" data-sort-on="{{name}}" data-sort-by="{{#eq ../sortedOn name}}{{#eq ../sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                        {{#if sortable}}
                            <a href="#">
                                {{tableDisplayName}}
                                 <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                            </a>
                        {{else}}
                            {{tableDisplayName}}
                        {{/if}}
                    </th>
                    {{/each}}
                </tr>
            </thead>
            {{/if}}

            {{#if tabularData.footerActive}}
            <tfoot>
                <tr>
                    {{#each tabularData.columns}}
                        <td class="{{type}}">{{footerDisplayText}}</td>
                    {{/each}}
                </tr>
            </tfoot>
            {{/if}}

             <tbody>
                {{#each tabularData.paxResults}}
                <tr>
                    <td><a href="{{contestUrl}}">{{participantName}}</a></td>
                    <td>{{orgUnit}}</td>

                    {{#if ../includeBaseline}}
                    <td class="number">{{baseline}}</td>
                    {{/if}}

                    <td class="number">{{currentActivity}}</td>

                    <td class="number">{{levelCompleted}}</td>

                    {{#eq ../payoutType "points"}}
                    <td class="number">{{levelPayout}}</td>

                    {{#if ../includeBonus}}
                    <td class="number">{{bonusAmount}}</td>
                    {{/if}}

                    <td class="number">{{payoutAmount}}</td>

                    {{else}}
                        <td class="number">{{payoutDescription}}</td>
                        {{#eq ../role "creator"}}
                        <td class="number">{{payoutValue}}</td>
                        {{/eq}}
                    {{/eq}}

                </tr>
                {{/each}}
            </tbody>
        </table>

        <div class="pagination pagination-right paginationControls"></div>
        <div class="breadCrumbsWrap"></div>
    </div>
</div>
