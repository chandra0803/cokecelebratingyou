<%@ include file="/include/taglib.jspf" %>

<table class="table table-striped">
    <thead>
        <tr>
            <th class="title"><cms:contentText key="SCORE_DETAILS" code="engagement.participant"/>{{#eq timeframeType "month"}} (<cms:contentText key="MONTH" code="engagement.participant"/>){{/eq}}{{#eq timeframeType "quarter"}} (<cms:contentText key="QUARTER" code="engagement.participant"/>){{/eq}}{{#eq timeframeType "year"}} (<cms:contentText key="YEARLY" code="engagement.participant"/>){{/eq}}</th>
            <th class="actual number"><cms:contentText key="ACTUAL" code="engagement.participant"/></th>
            {{#if areTargetsShown}}
            <th class="target number"><cms:contentText key="TARGET" code="engagement.participant"/></th>
            <th class="diff number"><cms:contentText key="DIFFERENCE" code="engagement.participant"/></th>
            {{/if}}
        </tr>
    </thead>

    <tbody>
        {{#each details}}
        <tr class="{{#if ../areTargetsShown}}{{ontrack}}{{/if}}">
            <td class="title">
                <i class="icon-g5-engagement-{{type}}"></i>
                {{title}}
                {{#if helpText}}<small class="helpText">{{helpText}}</small>{{/if}}
            </td>
            <td class="actual number">{{actual}}</td>
            {{#if ../areTargetsShown}}
            <td class="target number">{{target}}</td>
            <td class="diff number">{{diff}}</td>
            {{/if}}
        </tr>
        {{/each}}
    </tbody>
</table>
