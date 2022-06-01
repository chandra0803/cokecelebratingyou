<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="approvalsModuleTpl">
<!-- ======== APPROVALS: APPROVALS MODULE ======== -->

<!-- ======== APPROVALS: APPROVALS MODULE ======== -->

<div class="module-liner">
    <div class="wide-view">
        <h3 class="module-title"><cms:contentText code="promotion.approvals" key="APPROVAL_DASHBOARD" /></h3>

        <div class="row-fluid">
            <div class="span12">
                <div class="approvalDashboardWrap">

                </div>
            </div>
        </div>
    </div> <!-- ./wide-view -->

</div> <!-- ./module-liner -->
<!--subTpl.approvalDashboardTpl=
{{#if nominationApprovals}}
    {{#gte nominationapprovalsCount 1}}
        <div class="approvalCard card">
            <div class="card-front">
                <div class="card-top">
                    <span class="approvalCount">{{nominationApprovals.[0].numberOfApprovables}}</span>
                </div>

                   <div class="card-details">
                       <h4 class="type">
                            <cms:contentText code="promotion.approvals" key="NOMINATIONS" />
                        </h4>

                    {{#ueq nominationApprovals.[0].numberOfApprovables 0}}
                        <a href="${pageContext.request.contextPath}/claim/nominationsApprovalPromoList.do?method=display"><cms:contentText code="promotion.approvals" key="VIEW_ALL" /></a>
                    {{else}}
                        <a href="${pageContext.request.contextPath}/claim/nominationsApprovalPromoList.do?method=display"><cms:contentText code="promotion.approvals" key="VIEW_PAST_APPROVALS" /></a>
                    {{/ueq}}
                   </div>
                </div>
            </div>
        {{/gte}}
    {{/if}}
    {{#if RecognitionApprovals}}
        {{#gte recognitionapprovalsCount 1}}
            <div class="approvalCard card">
                <div class="card-front">
                    <div class="card-top">
                        <span class="approvalCount">{{RecognitionApprovals.[0].numberOfApprovables}}</span>
                    </div>

                    <div class="card-details">
                        <h4 class="type">
                        <cms:contentText code="promotion.approvals" key="RECOGNITIONS" />
                        </h4>

                        {{#ueq RecognitionApprovals.[0].numberOfApprovables 0}}
                            <a href="${pageContext.request.contextPath}/claim/approvalsRecognitionList.do"><cms:contentText code="promotion.approvals" key="VIEW_ALL" /></a>
                        {{else}}
                            <a href="${pageContext.request.contextPath}/claim/approvalsRecognitionList.do"><cms:contentText code="promotion.approvals" key="VIEW_PAST_APPROVALS" /></a>
                        {{/ueq}}
                    </div>
                </div>
            </div>
        {{/gte}}
    {{/if}}

    {{#if claimApprovals}}
        <div class="approvalCard card">
            <div class="card-front">
                <div class="card-top">
                    <span class="approvalCount">{{claimapprovalsCount}}</span>
                </div>

                <div class="card-details">
                    <h4 class="type">
                        <cms:contentText code="promotion.approvals" key="CLAIMS" />
                    </h4>

                    {{#ueq claimapprovalsCount 0}}
                        <a href="{{claimUrl}}"><cms:contentText code="promotion.approvals" key="VIEW_ALL" /></a>
                    {{else}}
                        <a href="{{claimUrl}}"><cms:contentText code="promotion.approvals" key="VIEW_PAST_APPROVALS" /></a>
                    {{/ueq}}
                </div>

            </div>
        </div>
    {{/if}}

    {{#if ssiApprovals}}
        <div class="approvalCard card">
            <div class="card-front">
                <div class="card-top">
                    <span class="approvalCount">{{ssiapprovalsPendingCount}}</span>
                </div>

                <div class="card-details">
                    <h4 class="type">
                        <cms:contentText code="promotion.approvals" key="CLAIMS" />
                    </h4>

                    {{#ueq ssiapprovalsPendingCount 0}}
                        <a href="${pageContext.request.contextPath}/ssi/creatorContestList.do?method=display#index"><cms:contentText code="promotion.approvals" key="VIEW_ALL" /></a>
                    {{else}}
                        <a href="${pageContext.request.contextPath}/ssi/creatorContestList.do?method=display#index"><cms:contentText code="promotion.approvals" key="VIEW_PAST_APPROVALS" /></a>
                    {{/ueq}}
                </div>
            </div>
        </div>
    {{/if}}
subTpl-->

</script>
