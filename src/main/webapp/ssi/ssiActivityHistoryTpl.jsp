<%@ include file="/include/taglib.jspf"%>
<div class="span12">
<h3 class="sectionTitle"><cms:contentText code="ssi_contest.claims" key="ACTIVITY_HISTORY" /></h3>

{{#if tabularData.results}}
<div class="pagination pagination-right paginationControls"></div>

<table id="ssiActivityHistoryTable" class="table table-striped">

    {{#if tabularData.meta.columns}}
    <thead>
        <tr>
            {{#each tabularData.meta.columns}}
            <th class="{{name}} {{#if sortable}}sortable{{/if}} {{#eq ../sortedOn name}}sorted {{../sortedBy}}{{else}}asc{{/eq}}" data-sort-on="{{name}}" data-sort-by="{{#eq ../sortedOn name}}{{#eq ../sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                {{#if sortable}}
                    <a href="#">
                        {{displayName}}
                         <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                    </a>
                {{else}}
                    {{displayName}}
                {{/if}}
            </th>
            {{/each}}
        </tr>
    </thead>
    {{/if}}

    {{#if tabularData.meta.footerActive}}
    <tfoot>
        <tr>
            {{#each tabularData.meta.columns}}
                <td>{{footerDisplayText}}</td>
            {{/each}}
        </tr>
    </tfoot>
    {{/if}}

    {{#if tabularData.results}}
    <tbody>
        {{#each tabularData.results}}
        <tr>
            <td><a href="{{claimDetailUrl}}">{{claimNumber}}</a></td>

            <td>{{date}}</td>

            {{#if activity}}
            <td>{{activity}}</td>
            {{else}}
            <td></td>
            {{/if}}

            {{#if status}}
            <td>{{status}}</td>
            {{/if}}

            <td>{{amount}}</td>

        </tr>
        {{/each}}
    </tbody>
    {{/if}}

</table>

<div class="pagination pagination-right paginationControls"></div>

<!--XXXsubTpl.paginationTpl=
    NOTE: you can safely take the JSP conversion of core/base/tpl/paginationView.html and include it here. Then, remove the "XXX" above and the script will use this child template instead of the remote one
subTpl-->
{{else}}
<div class="alert noClaims"><cms:contentText code="ssi_contest.claims" key="NO_CLAIMS" /></div>
{{/if}}
</div>
