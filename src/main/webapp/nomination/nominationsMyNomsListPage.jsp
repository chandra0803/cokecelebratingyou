<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== MY NOMINATIONS PAGE ======== -->
<div id="NominationsMyNomsListPageView" class="page-content">
    <div class="row-fluid">

        <div class="span12">
            <div class="pagination pagination-right paginationControls first"></div>

                <div class="nominationsListTableWrapper" data-msg-empty="<cms:contentText code="nomination.past.winners" key="NO_BANNER"/>"></div>

                <script id="nominationsListPageTableTpl" type="text/x-handlebars-template">
                    <table id="nominationsListPageTable" class="table table-striped">
                        {{#if tabularData.meta.columns}}
                        <thead>
                            <tr>
                                {{#each tabularData.meta.columns}}
                                <th class="{{name}} {{#if sortable}}sortable{{/if}} {{#eq ../sortedOn name}}sorted {{../sortedBy}}{{else}}asc{{/eq}}" data-sort-on="{{name}}" data-sort-by="{{#eq ../sortedOn name}}{{#eq ../sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}" data-column-id="{{id}}">
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

                        {{#if tabularData.results}}
                        <tbody>
                            {{#each tabularData.results}}
                            <tr>
                                <td>{{promotionName}}</td>

                                <td>{{dateWon}}</td>

                                <td><a href="{{detailUrl}}" class="btn btn-primary"><cms:contentText key="VIEW_DETAILS" code="nomination.approval.list" /></a></td>
                            </tr>
                            {{/each}}
                        </tbody>
                        {{/if}}
                    </table>
                </script>

            <div class="pagination pagination-right paginationControls first"></div>
        </div>
    </div>
</div>

<script>
$(document).ready(function() {

	G5.props.URL_JSON_NOMINATIONS_MY_LIST_DATA = G5.props.URL_ROOT+'nomination/viewNominationPastWinnersList.do?method=getNominationMyWinnersList';

    //attach the view to an existing DOM element
    var nmnlpv = new NominationsMyNomsListPageView({
        el:$('#NominationsMyNomsListPageView'),
        pageNav : {
            back : {
            	 text : '<cms:contentText key="BACK" code="system.button" />',
	              url : 'javascript:history.go(-1);'
            },
            home : {
            	    text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
            }
        },
        pageTitle : 'My Nominations List'
    });

});
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>
