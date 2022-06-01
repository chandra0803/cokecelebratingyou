<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div class="row-fluid">

    <div class="span12">

    <h2 id="reportName">{{displayName}}</h2>

    {{#if tabularData.results}}

        {{#if tabularData.meta.exportFullReportUrl}}
        <ul id="exportFullReport" class="export-tools fr">

            {{#each tabularData.meta.exportFullReportUrl}}
            <li class="export csv">
                <a href="{{url}}" class="exportCsvButton">
                    <span>{{label}}</span>
                    <span class="btn btn-inverse btn-compact btn-export-csv">
                        <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                    </span>
                </a>
            </li>
            {{/each}}

        </ul>
        {{/if}}

    {{else}}
        {{#if exportOnly}}
        {{! duplication of code, but Handlebars doesn't support compound if statements}}

        {{#if tabularData.meta.exportFullReportUrl}}
        <ul id="exportFullReport" class="export-tools fr">

            {{#each tabularData.meta.exportFullReportUrl}}
            <li class="export csv">
                <a href="{{url}}" class="exportCsvButton">
                    <span>{{label}}</span>
                    <span class="btn btn-inverse btn-compact btn-export-csv">
                        <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                    </span>
                </a>
            </li>
            {{/each}}

        </ul>
        {{/if}}

        {{/if}}
    {{/if}}

        <p id="reportDesc">{{desc}}</p>

        <!--
            each report has a special CM key containing the full translated string for the "As of" text
            the key ouput will have a {0} placeholder where the timestamp value is inserted
            this allows the translations to have plain text and the timestamp in any order
            we embed this CM output as a tplVariable in our reportsPageDetail Handlebars template
            we also have an asOfTimestamp subTpl embedded in our reportsPageDetail Handlebars template
            we pass the report.reportDetailAsOfTimestamp value from the JSON to the subTpl, then replace the {0} with the rendered output
            the final string is assigned to report.reportDetailAsOfTimestampFormatted in the JSON to be passed to the main template
        -->
        <!--tplVariable.asOfTimestamp= "<cms:contentText key="AS_OF" code="report.display.page" />" tplVariable-->
        <!--subTpl.asOfTimestamp= <span class="stamp">{{reportDetailAsOfTimestamp}}</span> subTpl-->
        {{#if reportDetailAsOfTimestampFormatted}}<p id="timeStamp">{{{reportDetailAsOfTimestampFormatted}}}</p>{{/if}}

        {{#if exportOnly}}

        <div class="alert alert-info row-fluid" id="noResultsText">{{{exportText}}}</div>

        {{/if}}

    </div><!-- /.span12 -->

</div><!-- /.row-fluid -->

{{#unless exportOnly}}

<div id="chartRowContainer" class="container-fluid container-splitter with-splitter-styles">
    <div class="row-fluid">
        <div id="chartRow" class="span12">
            {{#if chartSet}}
            <div class="span7">
                <ul class="chartThumbs">
                    {{#each chartSet}}
                    <li id="thumb-chartId-{{id}}" {{#eq ../chartSet.length "1"}}class="activeSlide"{{/eq}}><a href="#" data-chart-id="{{id}}"><img title="{{displayName}}" class="individualChartThumb {{chartType}}" src="<%= RequestUtils.getBaseURI(request) %>/assets/img/reports/type_{{chartType}}.jpg" /></a><i class="icon-arrow-1-right"></i><i class="icon-arrow-1-down"></i></li>
                    {{/each}}
                </ul> <!-- /.chartThumbs -->
                <div id="chartInFocus">
                    {{#each chartSet}}
                    <div id="chartContainer-{{id}}" class="reportsChart-moveable-item" data-chart-id="{{id}}"><img class="individualChartBkgd {{chartType}}" src="<%= RequestUtils.getBaseURI(request) %>/assets/img/reports/type_{{chartType}}.jpg" /></div>
                    {{/each}}
                </div> <!-- /#chartInFocus -->
            </div> <!-- /.span7 -->
            {{/if}}
            <div class="span5">
                {{#if chartSet}}
                <ul class="export-tools fr">
                    <li class="export pdf">
                        <a href="#" id="exportChartPdf" class="">
                            <span class="btn btn-inverse btn-compact btn-export-pdf">
                                <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                            </span>
                        </a>
                    </li>
                </ul>
                {{/if}}
                <div id="chartInfo">
                    {{#if chartSet}}<h3 id="chartName"></h3>{{/if}}
                    <p><strong><cms:contentText key="REPORT_FILTERS" code="report.display.page" /></strong> <button type="button" class="btn btn-primary reportsChangeFiltersPopoverTrigger"><cms:contentText key="CHANGE_FILTERS_BUTTON" code="report.display.page" /> <i class="icon-pencil2"></i></button></p>
                    <p id="currentParametersSummary">
                        {{#each parameters}}
                        {{#gte value.length 201}}
                        	<strong>{{name}}:</strong><p style="height:200px; overflow:scroll;">{{value}}</p><br>
                        {{else}}
                            <strong>{{name}}:</strong>{{value}}<br>
                        {{/gte}}
                        {{/each}}
                    </p>
                    {{#if chartSet}}
                    <%-- Commenting out for now as per business owners request --%>
                    <%-- <p class="disclaimer"><em>* <cms:contentText key="CHART_DISCLAIMER" code="report.display.page" /> <span class="chartDisplaysTop" data-default="20">20</span>.</em></p> --%>
                    <a id="reportsFavoritesPopoverTrigger"><button type="button" class="btn btn-primary"><cms:contentText key="ADD_TO_FAVORITES" code="report.display.page" /> <i class="icon-plus"></i></button></a>
                    {{/if}}
                </div> <!-- chartThumbs -->
            </div> <!-- span5 -->

        </div> <!-- /#chartRow.span12 -->
    </div> <!-- /.row-fluid -->
</div> <!-- /.container-fluid -->

{{#unless exportAndFilters}}
<div id="tabularResultsContainer" class="row-fluid">

    <div class="span12">

        <div class="row-fluid">

            <div class="span6">

                <h4 id="reportDefinitionList"><cms:contentText key="SUMMARY" code="report.display.page" /> <a id="columnDefinitionsPopover" class="columnDefinitions-popover" title='<cms:contentText key="COLUMN_DEFINITION" code="system.general" />'><i id="iconInfoSign" class="icon-info"></i></a></h4>

    {{#if tabularData.results}}

                <div id="reportDefinitionListContents" class="hide">
                    <dl id="columnDefinitions">
                    {{#each tabularData.meta.columns}}
                        {{#if description}}
                        <dt>{{name}}</dt>
                        <dd>{{description}}</dd>
                        {{/if}}
                    {{/each}}
                    </dl>
                </div>

            </div>

            <div class="span2 offset4">

                {{#if tabularData.meta.exportCurrentView}}
                <ul id="exportCurrentView" class="export-tools fr">

                    <!-- <li>Export Current View:</li> -->

                    <li class="export csv">
                        <a href="{{tabularData.meta.exportCurrentView.url}}" class="exportCsvButton">
                            <span>{{tabularData.meta.exportCurrentView.label}}</span>
                            <span class="btn btn-inverse btn-compact btn-export-csv">
                                <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                            </span>
                        </a>
                    </li>

                </ul>
                {{/if}}

    {{/if}}

            </div>

        </div>

        <div class="row-fluid">

            <div class="span12">

                <div id="breadCrumbs"></div>
                <!--subTpl.breadcrumbTpl=
                    <%@include file="/include/breadcrumbView.jsp" %>
                subTpl-->

            </div>

        </div>

    {{#if tabularData.results}}

        <div class="row-fluid">

            <div class="span12">
                <div class="pagination pagination-right paginationControls"></div>
                <!--subTpl.paginationTpl=
                    <%@include file="/include/paginationView.jsp" %>
                subTpl-->

                <div id="resultsTableWrapper"></div>
                <!--subTpl.tableTpl=
                    <%@include file="/reports/reportsTable.jsp" %>
                subTpl-->

                <div class="pagination pagination-right paginationControls"></div>
            </div>

        </div>

    {{else}}
        <div class="alert alert-block alert-error">
            <p><cms:contentText key="NO_RESULTS_FOUND" code="report.display.page" /></p>
    </div>
    {{/if}}

    </div><!-- /.span12 -->

</div><!-- /#tabularResultsContainer.row-fluid -->
{{/unless}} {{! /#unless exportAndFilters}}
{{/unless}} {{! /#unless exportOnly}}
