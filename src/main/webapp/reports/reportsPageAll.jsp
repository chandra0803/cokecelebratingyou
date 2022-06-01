<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

{{#eq module false}}
<div class="row-fluid">

    <div class="span12">

        <h2><cms:contentText key="ALL_REPORTS_HEADER" code="report.display.page" /></h2>

    </div> <!-- span12 -->

</div> <!-- row-fluid -->
{{/eq}}

{{#each categories}}
<div class="row-fluid reportCategory">
    {{#eq module false}}
    <div class="span1">
        <img class="{{categoryNameId}}" alt="" src="<%= RequestUtils.getBaseURI(request) %>/assets/img/reports/cat_{{categoryNameId}}.jpg">
    </div><!-- /.span1 -->
    {{/eq}}

    <div class="span11">
        {{#if module}}
            <h5 class="switcher" data-target="#reports-all-{{categoryNameId}}">
                {{category}}
                <i class="icon-plus-circle"></i>
            </h5>
        {{else}}
            <h3>{{category}}</h3>
        {{/if}}

        <ul id="reports-all-{{categoryNameId}}" class="reportslist {{#if module}}hide{{/if}}">
            {{#each reports}}
            <li class="reportReport" id="reportId-{{id}}" data-report-id="{{id}}">
                {{#if ../module}}
                    <a href="<%= RequestUtils.getBaseURI(request) %>/reports/allReports.do\#{{id}}">{{displayName}}</a><br>
                {{else}}
                    <a href="{{url}}">{{displayName}}</a><br>
                {{/if}}
                <span class="desc">{{desc}}</span>
            </li>
            {{/each}}
        </ul>
    </div><!-- /.span11 -->
</div> <!-- /.row-fluid.reportCategory -->
{{/each}}
