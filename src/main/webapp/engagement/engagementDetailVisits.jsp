<%@ include file="/include/taglib.jspf" %>

{{#eq mode "team"}}

<div class="chartContainer" id="chartContainer_{{type}}"></div>

{{else}}

{{#if logins}}
<h4 class="subhead"><cms:contentText key="SITE_VISITS" code="engagement.participant"/></h4>

<table class="table table-striped">
    <thead>
        <tr>
            <th class="d"><cms:contentText key="DATE" code="engagement.participant"/></th>
            <th class="t"><cms:contentText key="TIME" code="engagement.participant"/></th>
        </tr>
    </thead>

    <tbody>
        {{#each logins}}
        <tr>
            <td class="d">{{date}}</td>
            <td class="t">{{localeTime}} {{timeZoneId}}</td>
        </tr>
        {{/each}}
    </tbody>
</table>
{{else}}
<h4 class="visits subhead"><cms:contentText key="NO_LOGINS_FOR_TIMEFRAME" code="engagement.participant"/></h4>
{{/if}}

{{/eq}}