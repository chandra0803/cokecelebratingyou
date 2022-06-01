<!--
    each pax recognition to/by item has special CM keys containing the full translated string for the various permutations of the "XXX have/has recognized [by] ## people" text
    the keys' output will have a {0} placeholder where the number of people is inserted
    this allows the translations to have plain text and the number in any order
    we embed this CM output as a tplVariable in our engagementDetailPax Handlebars template
    we also have a subTpl embedded in our engagementDetailPax Handlebars template
    we pass the various values from the JSON to the subTpls, then replace the {0} with the rendered output
    the final string is assigned to recognizedCountFormatted in the JSON to be passed to the main template
-->
<!--subTpl.recognizedCount= {{recognizedCount}} subTpl-->

<!--tplVariable.recognizedCount={
    "paxRecTo" : "<cms:contentText key="YOU_RECOGNIZED" code="engagement.participant"/>",
    "paxRecBy" : "<cms:contentText key="RECOGNIZED_YOU" code="engagement.participant"/>",
    "paxRecToSingle" : "<cms:contentText key="YOU_RECOGNIZED_PERSON" code="engagement.participant"/>",
    "paxRecBySingle" : "<cms:contentText key="RECOGNIZED_YOU_PERSON" code="engagement.participant"/>"
} tplVariable-->

{{#eq mode "team"}}
    <div class="topper">

        {{#eq type "paxRecTo"}}
        <h4 class="paxRecTo subhead"><cms:contentText key="YOUR_TEAM_RECOGNIZED" code="engagement.participant"/></h4>
        {{else}}
        <h4 class="paxRecBy subhead"><cms:contentText key="RECOGNIZED_YOUR_TEAM" code="engagement.participant"/></h4>
        {{/eq}}

        {{#if detailUrl}}
        {{#if count}}
        <a class="detailUrl btn btn-primary btn-inverse btn-mini" {{#eq type "paxRecTo"}}data-title='<cms:contentText key="TEAM_RECOGNIZED" code="engagement.participant"/>'{{else}}data-title='<cms:contentText key="TEAM_RECOGNIZED_BY" code="engagement.participant"/>'{{/eq}} href="engagementPageRecognized.do?mode={{mode}}&amp;type={{type}}&amp;userId={{userId}}&amp;nodeId={{nodeId}}&amp;modelUrl={{detailUrl}}&amp;recognizedCountFormatted={{count}}&amp;timeframeType={{timeframeType}}&amp;timeframeMonthId={{timeframeMonthId}}&amp;timeframeYear={{timeframeYear}}">
            <cms:contentText key="VIEW_DETAILS" code="engagement.participant"/>
        </a>
        {{/if}}
        {{/if}}
    </div>

    {{#if chartUrl}}<div class="chartContainer" id="chartContainer_{{type}}"></div>{{/if}}

{{else}}

    <h4 class="{{#eq type "paxRecTo"}}paxRecTo{{else}}paxRecBy{{/eq}} subhead">{{recognizedCountFormatted}}</h4>

    <div class="engagementRecognizedModelView"></div>

{{/eq}}
