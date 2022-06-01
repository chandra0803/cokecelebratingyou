<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<table id="resultsTable" class="table table-striped table-condensed">
    {{#if tabularData.meta.columns}}
    <thead>
        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
            {{#each tabularData.meta.columns}}<th class="{{type}}{{#if sortable}} sortable{{/if}}{{#if sortedOn}} sorted{{else}} unsorted{{/if}}{{#if sortedBy}} {{sortedBy}}{{/if}}" data-sort-by-id="{{id}}" data-sorted-on="{{sortedOn}}" data-sorted-by="{{sortedBy}}">{{#if
                sortable}}<a href="{{sortUrl}}">{{name}} {{#unless sortedByDesc}}<i class="icon-arrow-1-up"></i>{{else}}<i class="icon-arrow-1-down"></i>{{/unless}}</a>{{
                else}}{{name}}{{/if
            }}</th>{{/each}}
        </tr>
    </thead>
    {{/if}}

    {{#if tabularData.meta.summary}}
    <tfoot>
        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
            {{#each tabularData.meta.summary}}<td class="{{type}}{{#if sortedOn}} sorted{{/if}}">{{#if
                link}}{{#if url}}<a href="{{url}}">{{text}}</a>{{
                    else}}{{text}}{{/if
                    }}{{
                else}}{{text}}{{/if
            }}</td>{{/each}}
        </tr>
    </tfoot>
    {{/if}}

    {{#if tabularData.results}}
    <tbody>
        {{#each tabularData.results}}
        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
            {{#each .}}<td class="{{type}}{{#if sortedOn}} sorted{{/if}}">{{#if
                link}}{{#if url}}<a href="{{url}}"{{#if target}} target="{{target}}"{{/if}}{{#if urlName}} data-url-name="{{urlName}}"{{/if}}>{{text}}</a>{{
                    else}}{{text}}{{/if
                    }}{{
                else}}{{text}}{{/if
            }}</td>{{/each}}
        </tr>
        {{/each}}
    </tbody>
    {{/if}}
</table>