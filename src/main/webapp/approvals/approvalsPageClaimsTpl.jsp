<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

{{!debug}}
<div class="row-fluid">
    <div class="span12">

        <a href="#" id="printPage" class="pageView_print pull-right btn btn-small"> <cms:contentText key="PRINT" code="system.button"/> <i class="icon-printer"></i></a>

        {{#with promotion}}

        <h2 class="promoName">{{name}}</h2>
        <h3><cms:contentText key="CLAIM_APPROVALS" code="claims.product.approval"/></h3>
        {{#if timestamp}}<p id="timestamp"><cms:contentTemplateText key="AS_OF_DATE" code="promotion.goalquest.progress" args="{{timestamp}}"/></p>{{/if}}

        {{#if stats}}
        <ul class="stats unstyled">
            {{#each stats}}
            <li class="stat {{type}} {{status}}">
                <strong class="count">{{count}}</strong>
                <span class="name">{{name}}</span>
            </li>
            {{/each}}
        </ul>
        {{/if}}

        {{/with}}

    </div><!-- /.span12 -->
</div><!-- /.row-fluid -->


<div class="row-fluid">
    <div class="span12">

        <form id="claimSubmissionForm" action="approvalsClaimsListUpdate.do?method=saveApprovals" method="POST">

        {{#with promotion}}

            {{#if claims.results}}
            {{#if claims.meta.exportUrl}}
            <ul id="exportCurrentView" class="export-tools fr">
                <li class="export csv">
                    <a href="{{claims.meta.exportUrl}}" class="exportXlsButton">
                        <span class="btn btn-inverse btn-compact btn-export-csv">
                            <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                        </span>
                    </a>
                </li>
            </ul>
            {{/if}}
            {{/if}}

            {{#if claims.results}}
            <div class="pagination pagination-right paginationControls"></div>
            <%-- NOTE: you can safely take the JSP conversion of core/base/tpl/paginationView.html and include it here. Then, remove the "XXX" above and the script will use this child template instead of the remote one --%>
            <!--subTpl.paginationTpl=
                <%@include file="/include/paginationView.jsp" %>
            subTpl-->
            {{/if}}

        {{/with}}

        {{#with parameters}}
            <p class="parameters"><cms:contentTemplateText key="ALL_OPEN_CLAIMS_BETWEEN" code="claims.product.approval" args="{{startDate}}, {{endDate}}" delimiter=","/></p>
        {{/with}}

        {{#with promotion}}

            {{#if claims.results}}
            <div id="claimsTableWrapper">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            {{#each claims.meta.columns}}
                            <th class="{{name}} {{#if sortable}}sortable {{#if sortedOn}}sorted{{else}}unsorted{{/if}}{{/if}}"{{#if sortable}} data-sorted-on="{{name}}" {{#if sortedBy}} data-sorted-by="{{sortedBy}}"{{/if}}{{/if}}>
                                {{#if sortable}}
                                    <a href="#">{{text}} {{#eq sortedBy "desc"}}<i class="icon-arrow-1-down"></i>{{else}}<i class="icon-arrow-1-up"></i>{{/eq}}</a>
                                {{else}}
                                    {{text}}
                                {{/if}}
                            </th>
                            {{/each}}
                        </tr>
                    </thead>
                    <tbody>
                        {{#each claims.results}}
                        <tr data-claim-id="{{id}}">
                            <td class="number">
                                <a href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsClaimDetailsMaintain.do?method=prepareUpdate&claimId={{id}}">{{number}}</a>
                                <input type="hidden" id="claim[{{id}}].id" value="{{id}}">
                                <input type="hidden" id="claim[{{id}}].number" value="{{number}}">
                            </td>
                            <td class="date">{{date}}</td>
                            <td class="submitter">{{submitter}}</td>
                            <td class="approver">
                                {{#each products}}
                                <span class="text">{{approver}}&nbsp;</span><!-- &nbsp; acts like a shim to give this element a height when empty -->
                                {{/each}}
                            </td>
                            <td class="products">
                                {{#each products}}
                                <span class="text">{{name}}</span>
                                {{/each}}
                            </td>
                            <td class="status form-inline">
                                {{#each products}}
                                {{#unless _showSelects}}
                                    {{#eq status "approv"}}<span class="text"><cms:contentText key="APPROVED" code="promotion.approvals"/></span>{{/eq}}
                                    {{#eq status "deny"}}<span class="text"><cms:contentText key="DENIED" code="promotion.approvals"/>: {{statusReason}}</span>{{/eq}}
                                {{else}}
                                    <span class="selects" data-product-id="{{id}}">
                                        <span class="validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="profile.errors" key="SELECT_STATUS"/>"}'>
                                            <select id="claimProductApprovalFormByClaimProductIdString({{id}}).status" name="claimProductApprovalFormByClaimProductIdString({{id}}).approvalStatusType" class="status" data-start-val="{{status}}">
                                                {{#each ../../this.claims.meta.statuses}}
                                                <option value="{{value}}">{{text}}</option>
                                                {{/each}}
                                            </select>
                                        </span>
                                        <%-- the JS will dynamically add/remove the "validateme" class from this span, depending on whether or not the select is visible --%>
                                        <span class="" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="claims.product.approval.details" key="PLEASE_SELECT_REASON"/>"}'>
                                            <select id="claimProductApprovalFormByClaimProductIdString({{id}}).statusReasonDeny" name="claimProductApprovalFormByClaimProductIdString({{id}}).denyPromotionApprovalOptionReasonType" class="statusReason deny hide" data-start-val="{{statusReason}}">
                                                {{#each ../../this.claims.meta.statuses}}
                                                {{#eq value "deny"}}
                                                <option value=""><cms:contentText code="claims.product.approval" key="SELECT_RC" /></option>
                                                    {{#each reasons}}
                                                <option value="{{value}}">{{text}}</option>
                                                    {{/each}}
                                                {{/eq}}
                                                {{/each}}
                                            </select>
                                        </span>
                                        <span class="" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="claims.product.approval.details" key="PLEASE_SELECT_REASON"/>"}'>
                                            <select id="claimProductApprovalFormByClaimProductIdString({{id}}).statusReasonHold" name="claimProductApprovalFormByClaimProductIdString({{id}}).holdPromotionApprovalOptionReasonType" class="statusReason hold hide" data-start-val="{{statusReason}}">
                                                {{#each ../../this.claims.meta.statuses}}
                                                {{#eq value "hold"}}
                                                <option value=""><cms:contentText code="claims.product.approval" key="SELECT_RC" /></option>
                                                    {{#each reasons}}
                                                <option value="{{value}}">{{text}}</option>
                                                    {{/each}}
                                                {{/eq}}
                                                {{/each}}
                                            </select>
                                        </span>
                                    </span>
                                {{/unless}}
                                {{/each}}
                            </td>
                        </tr>
                        {{/each}}
                    </tbody>
                </table>

                <div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
                    <span class="singleClaim"><a href="#"><cms:contentText key="SAME_FOR_ALL" code="promotion.public.recognition"/></a></span>
                    <span class="multiClaim"><cms:contentText key="SAME_FOR_ALL" code="promotion.public.recognition"/>:<br><a href="#" class="this"><cms:contentText key="SFA_THIS_CLAIM" code="promotion.public.recognition"/></a> | <a href="#" class="all"><cms:contentText key="SFA_ALL_CLAIMS" code="promotion.public.recognition"/></a></span>
                </div><!-- /#sameForAllTipTpl -->
            </div><!-- /#claimsTableWrapper -->

            <div class="pagination pagination-right paginationControls"></div>

                {{#unless ../_hideFormActions}}
            <div class="form-actions pullBottomUp">
                <button type="submit" class="btn btn-primary btn-fullmobile" id="claimsSubmitBtn"><cms:contentText key="SAVE" code="system.button"/></button>
                <button type="button" class="btn btn-fullmobile" id="claimsCancelBtn"><cms:contentText key="CANCEL" code="system.button"/></button>

                <div class="approvalsClaimsCancelDialog" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_THIS_CLAIMS" code="claims.submission"/></b>
                    </p>
                    <p>
                        <cms:contentText key="ALL_CHANGES_DISCARDED" code="claims.submission"/>
                    </p>
                    <p class="tc">
                        <a href="${pageContext.request.contextPath}/claim/approvalsListPage.do" id="approvalsClaimsCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="system.button"/></a>
                        <a href="#" id="approvalsClaimsCancelDialogCancel" class="btn"><cms:contentText key="NO" code="system.button"/></a>
                    </p>
                </div>
            </div>
                {{/unless}}
            {{else}}
            <div class="alert noClaimsFound"><cms:contentText key="NO_CLAIMS_FOUND" code="claims.submission"/></div>
            {{/if}} <!-- /#if claims.results -->

        {{/with}}

        </form>

    </div><!-- /.span12 -->
</div><!-- /.row-fluid -->
