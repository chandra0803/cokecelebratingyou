<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div id="reportsPageView" class="reportsPageDetail-liner page-content page reports">

    <div class="page-topper">

        <div class="row-fluid">

            <div class="span12">

                <form class="form-inline form-labels-inline">
                    <div class="control-group">

                        <label class="control-label" for="reportsSelect"><cms:contentText key="PICK_REPORT" code="report.display.page" /></label>

                        <div class="controls">
                            <select id="reportsSelect" data-all-reports-text="<cms:contentText key="ALL_REPORTS_PICKLIST" code="report.display.page" />">
                                <!-- dynamic -->
                                <option value="1" selected="selected"><cms:contentText key="LOADING_REPORTS" code="report.display.page" /></option>
                            </select>
                        </div>
                    </div> <!-- control-group -->

                </form>

            </div>

        </div>

    </div><!-- /.page-topper -->

    <div id="reportsPageAllView" class="childview"></div>
    <div id="reportsPageDetailView" class="childview"></div>

</div><!-- /#reportsPageView -->


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    	G5.props.URL_JSON_REPORTS_ALL = 'reportMenu.do';
    	G5.props.URL_JSON_REPORTS_FAVORITES = 'displayDashboard.do';
    	G5.props.URL_JSON_REPORTS_MODULE = 'displayDashboard.do?method=display';
    	G5.props.URL_REPORTS_ALL = 'reports/allReports.do';
    	G5.props.REPORTS_LARGE_AUDIENCE = ${largeAudience};
    	// G5.props.REPORTS_DETAIL_CUSTOM_RESULTS_PER_PAGE = 100;
        var reportId = $.query.get('reportId'),
            dashboardItemId = $.query.get('dashboardItemId'),
            // if at any time you need to pass additional parameters to a selected reportId, use this object to do so. Any number of parameters is supported
            // note that this will only work when reportId is populated above by either the query string (as written) or via JSP logic
            params = {
            		<c:if test="${isEngagement ne null}">
            			reportId: '${reportId}',
            			isEngagement:${isEngagement},
            			periodStartDate:'${fromDate}',
            			periodEndDate:'${toDate}'
				  	</c:if>
                    };

        //attach the view to an existing DOM element
        window.rpv = new ReportsPageView({
            reportId: reportId,
            dashboardItemId: dashboardItemId,
            params: params,
            noSidebar : ${isDelegate},
			noGlobalNav : ${isDelegate},
            el:$('#reportsPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request) %>/homePage.do#launch/home'
                }
            },
            pageTitle : '<cms:contentText key="REPORTS" code="system.general" />'
          });
    });

</script>

<script type="text/javascript" src="${siteUrlPrefix}/assets/libs/plugins/fusioncharts.js"></script>

<script type="text/template" id="reportsPageAllTpl">
  <!-- ======== Handlebars template for all reports list ======== -->
  <%@include file="/reports/reportsPageAll.jsp" %>
</script>

<script type="text/template" id="reportsPageDetailTpl">
  <!-- ======== Handlebars template for report detail page ======== -->
  <%@include file="/reports/reportsPageDetail.jsp" %>
</script>

<script type="text/template" id="reportsFavoritesPopoverViewTpl">
  <!-- ======== popover for report favorites ======== -->
  <%@include file="/reports/reportsFavoritesPopoverView.jsp" %>
</script>


<%@include file="/search/paxSearchStart.jsp" %>
