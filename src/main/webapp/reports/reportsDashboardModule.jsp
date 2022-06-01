<%@ include file="/include/taglib.jspf"%>


<script type="text/template" id="reportsDashboardModuleTpl">
<!-- ======== REPORTS DASHBOARD MODULE ======== -->
<div class="module-liner">
    <div class="module-content">

        <ul class="nav nav-tabs" id="reports-tabs">
            <li class="active"><a href="#reports-dashboard-favorites" data-toggle="tab"><cms:contentText key="MANAGE_FAVORITE1" code="report.dashboard"/></a></li>
            <li><a href="#reports-dashboard-all" data-toggle="tab"><cms:contentText key="ALL_REPORTS_HEADER" code="report.dashboard"/></a></li>
        </ul>

        <div class="tab-content">
            <div class="tab-pane dashboard-favorites active" id="reports-dashboard-favorites">
                <h4 class="reportHeader"><cms:contentText key="MANAGE_FAVORITE1" code="report.dashboard"/></h4>

                <ul class="favoritesContainer">
                    <!-- dynamic -->
                </ul>
            </div><!-- /.tab-pane -->

            <div class="tab-pane dashboard-all" id="reports-dashboard-all">
                <h4 class="reportHeader"><cms:contentText key="ALL_REPORTS" code="report.dashboard"/></h4>

                <div class="allContainer">
                    <!-- dynamic -->
                </div>
            </div><!-- /.tab-pane -->
        </div>

    </div><!-- /.module-content -->
</div><!-- /.module-liner -->

<!--subTpl.favoriteTpl=
    <li class="favorite card{{#if isEmpty}} isEmpty{{/if}}" data-report-index="{{reportIndex}}" data-id="{{id}}" data-parent-id="{{parentId}}">
        {{#if isEmpty}}
            <h5 class="emptyTitle">
                <a href="<%= RequestUtils.getBaseURI(request) %>/reports/allReports.do">
                   	<cms:contentText key="ADD_A_FAVORITE_REPORT" code="report.dashboard"/>
                    <span class="subtitle"><cms:contentText key="SELECT_REPORT2" code="report.dashboard"/></span>
                </a>
            </h5>
        {{else}}
            <i class="reorder icon-drag-1"></i>
            <a href="{{clickThruUrl}}" class="visitAppBtn">
                <i class="icon-arrow-1-circle-right"></i>
            </a>

            <div class="description">
                <h5 class="displayName"><a href="{{clickThruUrl}}">{{displayName}}</a></h5>
                <p class="parameters">{{favoriteParameters}}</p>
            </div>

            <div id="chartContainer{{id}}" class="chartContainer"></div>
        {{/if}}
    </li>
subTpl-->
</script>

<script type="text/template" id="reportsPageAllTpl">
    <%@include file="/reports/reportsPageAll.jsp" %>
</script>

<script type="text/javascript" src="${siteUrlPrefix}/assets/libs/plugins/fusioncharts.js"></script>
