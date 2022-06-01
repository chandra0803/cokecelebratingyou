<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<%--<div id="myFavoriteReportsWrapper">--%>
    <div id="myFavoriteReportsHeader" class="modal-header">
        <button class="close closePopoverBtn" type="button"><i class="icon-close"></i></button>
        <h3><cms:contentText key="FAVORITE_HEADER" code="report.dashboard" /></h3>
    </div> <!-- myFavoriteReportsHeader -->

    <div class="modal-body">
        <p><cms:contentText key="INSTRUCTION_1" code="report.dashboard" /> <span id="minimumAlert"><cms:contentText key="INSTRUCTION_2" code="report.dashboard" /></span></p>
        <p id="dragToChange"><cms:contentText key="INSTRUCTION_3" code="report.dashboard" /></p>
        <p id="pleaseRemoveAlert" class="alert alert-error"><cms:contentText key="INSTRUCTION_4" code="report.dashboard" /></p>

        <div class="drag-group drag-group-vertical myFavoriteReports">
            {{#favorites}}
            <div id="{{this.id}}" class="ui-state-default dragItem">
                <span class="move icon-swap-vertical"></span><span class="remove icon-trash"></span>
                {{#if this.id}}
                <div class="reportFavoriteIcon"><img class="{{this.chartType}}" alt="{{this.chartType}}" src="<%= RequestUtils.getBaseURI(request) %>/assets/img/reports/type_{{this.chartType}}.jpg"></div>
                <div class="reportFavoriteText"><p>{{this.displayName}}</br>
                <small>{{this.favoriteParameters}}</small></div>
                {{/if}}
            </div>
            {{/favorites}}
        </div> <!-- myFavoriteReports -->
        {{#if showViewMyReportsLink}}
        <div class="viewMyReportsButtonArea form-actions">
            <a href="<%= RequestUtils.getBaseURI(request) %>/homePage.do#launch/reports" class="btn btn-primary"><i class="icon-eye"></i> <cms:contentText key="VIEW_MY_REPORTS" code="report.dashboard" /></a>
        </div>
        {{/if}}
    </div><!-- /.modal-body -->
<!-- </div> --><!-- myFavoriteReportsWrapper -->
