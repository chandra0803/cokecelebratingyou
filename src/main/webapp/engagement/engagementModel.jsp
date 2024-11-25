<%@ include file="/include/taglib.jspf"%>

<div class="timeframe">
    <ul class="controls inline">
        <li class="prev"><a href="#" data-timeframe="{{timeframeType}}" data-timeframe-nav="prev"><i class="icon-arrow-1-left"></i></a></li>
        <li class="now">{{timeframeName}}</li>
        <li class="next"><a href="#" data-timeframe="{{timeframeType}}" data-timeframe-nav="next"><i class="icon-arrow-1-right"></i></a></li>
    </ul>

    <div class="btn-group timeframeToggle">
        <a href="#" class="btn {{#eq timeframeType "month"}}btn-primary{{/eq}}" data-timeframe="month"><cms:contentText key="MONTH" code="engagement.participant"/></a>
        <a href="#" class="btn {{#eq timeframeType "quarter"}}btn-primary{{/eq}}" data-timeframe="quarter"><cms:contentText key="QUARTER" code="engagement.participant"/></a>
        <a href="#" class="btn {{#eq timeframeType "year"}}btn-primary{{/eq}}" data-timeframe="year"><cms:contentText key="YEARLY" code="engagement.participant"/></a>
    </div>
</div><!-- /.timeframe -->

{{#unless errors}}
<div class="title">
    <ul class="export-tools fr hide">
        <li class="print"><a href="#" class="pageView_print btn btn-small"><cms:contentText key="PRINT" code="system.button"/> <i class="icon-printer"></i></a></li>
    </ul>

    {{#if multiNode}}
    <select id="nodeSelect" name="nodeSelect">
        <option value="{{allNodeIds}}">{{allNodeNames}}</option>
        <option disabled>&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;</option>
        {{#each nodes}}
        <option value="{{id}}" {{#unless ../_allNodesSelected}}{{#if selected}}selected="selected"{{/if}}{{/unless}}>{{name}}</option>
        {{/each}}
    </select>
    {{else}}
        {{#if displayBreadcrumbs}}
            <div id="breadCrumbs" class="headline_3"></div>
            <!--subTpl.breadcrumbTpl=
                <%@include file="/include/breadcrumbView.jsp" %>
            subTpl-->
        {{else}}
            {{#if title}}<h3 class="title">{{title}}</h3>{{/if}}
        {{/if}}
    {{/if}}
</div><!-- /.title -->
{{/unless}}

{{#if summary}}
<!-- the EngagementSummaryCollectionView should always be inserted into an element with class 'engagementSummaryCollectionView' -->
<div class="engagementSummaryCollectionView container-splitter"></div><!-- /.engagementSummaryCollectionView -->
{{/if}}

{{#if detail}}
<!-- the EngagementDetail*Views should always be inserted into an element with class 'engagementDetailView' -->
<div class="engagementDetailView"></div><!-- /.engagementDetailView -->
{{/if}}


<!-- START if team display -->
{{#eq mode "team"}}
{{#if team}}
<!-- the EngagementTeamMembersView should always be inserted into an element with class 'engagementTeamMembersView' -->
<div class="engagementTeamMembersView"></div><!-- /.engagementTeamMembersView -->
{{/if}}
{{/eq}}
<!-- END if team display -->

<!--subTpl.errors=
<div id="errors" class="alert alert-error">
    <button type="button" class="close" data-dismiss="alert"><i class="icon-close"></i></button>
    <ul>
    {{#each .}}
        <li>{{text}}</li>
    {{/each}}
    </ul>
</div>
subTpl-->
