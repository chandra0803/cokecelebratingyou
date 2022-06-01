<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== NOMINATIONS: APPROVALS PAGE ======== -->
<div id="nominationsApprovalPageView" class="page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="TITLE" code="nomination.approval.list"/></h2>
        </div>
    </div>
    <div id="pendingNominationsWrapper">
        <div class="approvalPromotionWrapper">

            <div class="row-fluid">
                <div class="span10 promotionList control-group">
                    <!-- Generated content from promotionsListTpl -->
                </div>

                <div class="span2">
                    <ul class="export-tools fr">
                        <li><a href="#" class="pageView_print btn btn-small"><cms:contentText key="PRINT" code="nomination.approvals.module"/> <i class="icon-printer"></i></a></li>
                    </ul>
                </div>
            </div>
        </div><!-- /.approvalPromotionWrapper.row-fluid-->

        <!-- promotions name view template -->
        <script id="promotionNameTpl" type="text/x-handlebars-template">
            <div class="controls promoChangeWrapper">
                <span class="nominationPromotionName headline_3">
                    {{name}}
                </span>

                {{#gte totalPromotionCount 2}}
                <button type="button" id="nominationChangePromoBtn" class="btn btn-primary btn-inverse popoverTrigger" data-popover-content="promoChange"><cms:contentText key="CHANGE" code="nomination.approvals.module"/></button>
                {{/gte}}

                {{#if rulesText}}
                <a href="#" class="doViewRules"><cms:contentText key="VIEW_RULES" code="nomination.approvals.module"/></a>
                {{/if}}
            </div><!-- /.promoChangeWrapper -->
        </script><!-- /#promotionsListTpl -->

        <!-- promotions list client side template -->
        <script id="promotionsListTpl" type="text/x-handlebars-template">
            <div class="controls promoWrapper">
                <select id="promotionId" name="promotionId">
                    {{#each this}}
                        <option value="{{promoId}}">
                            {{name}}
                        </option>
                    {{/each}}
                </select>
            </div><!-- /.promoWrapper -->
        </script><!-- /#promotionsListTpl -->

        <div class="approvalSearchWrapper"><!--nominationsApprovalPageTpl.html--></div>

    </div><!-- /#pendingNominationsWrapper-->

    <div id="rulesModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3><cms:contentText key="RULES" code="nomination.approvals.module"/></h3>
        </div>
        <div class="modal-body">
            <!-- dynamic -->
        </div>
    </div><!-- /#rulesModal -->

    <div class="promoChangePopover" style="display:none">
        <p>
            <b><cms:contentText key="CHANGE_THE_PROMOTION" code="nomination.approvals.module"/></b>
        </p>
        <p>
            <cms:contentText key="CURRENT_SELECTIONS_LOST" code="nomination.approvals.module"/>
        </p>
        <p class="tc">
            <button id="nominationChangePromoConfirmBtn" class="btn btn-primary"><cms:contentText key="YES" code="system.button" /></button>
            <button id="nominationChangePromoCancelBtn" class="btn"><cms:contentText key="CANCEL" code="system.button" /></button>
        </p>
    </div><!-- /.promoChangeConfirmDialog -->

    <div id="statusModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3><cms:contentText key="STATUS" code="nomination.approvals.module"/></h3>
        </div>
        <div class="modal-body">
            <cms:contentText key="SAVE_CHANGES" code="nomination.approvals.module"/>
        </div>
    </div><!-- /#statusModal -->

    <div id="sortModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3><cms:contentText key="STATUS" code="nomination.approvals.module"/></h3>
        </div>
        <div class="modal-body">
            <cms:contentText key="CLICK_DONE" code="nomination.approvals.module"/>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" data-dismiss="modal"><cms:contentText key="OK" code="nomination.approvals.module"/></button>
            <button class="btn btn-primary doTheSort" data-dismiss="modal"><cms:contentText key="SORT_ANYWAY" code="nomination.approvals.module"/></button>
        </div>
    </div><!-- /#sortModal -->

    <!-- Success Modal -->
     <div class="modal hide fade approvalSuccessModal" data-backdrop="static">
        <div class="modal-header">
            <h3><cms:contentText key="STATUS_UPDATE" code="nomination.approvals.module"/></h3>
        </div>
        <div class="modal-body">
            <!-- <p>Your statuses have been updated.</p> -->

            <button class="btn btn-primary updateBtn"><cms:contentText key="BACK" code="nomination.approvals.module"/></button>
        </div>
    </div><!-- /.approvalSuccessModal -->

</div><!-- /#nominationsApprovalPageView. -->
<div class="modal hide fade certificateModal" id="certificateModal">
    <div class="modal-body loading">
        <div class="progress-indicator">
            <span class="spin">
            </span>
            <p><cms:contentText key="GENERATING_PDF" code="nomination.approvals.module"/></p>
        </div>
        <div class="pdf-wrapper" style="height: 500px;">
        </div>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn btn-primary" data-dismiss="modal" aria-hidden="true"><cms:contentText key="CLOSE" code="system.button" /></a>
    </div>
</div>
<script>

$(document).ready(function() {
    // Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to
    G5.props.URL_JSON_NOMINATIONS_LIST = G5.props.URL_ROOT+'claim/nominationApprovalPagePromotionsList.do?method=fetchEligiblePromotions';
    G5.props.URL_JSON_NOMINATIONS_APPROVAL_PAGE_DATA = G5.props.URL_ROOT+'claim/nominationsApprovalPage.do?method=fetchNominationsApprovalPageData';
    G5.props.URL_JSON_NOMINATIONS_APPROVAL_TABLE_DATA =  G5.props.URL_ROOT+'claim/nominationsApprovalPage.do?method=fetchNominationsApprovalPageTableData';
    G5.props.URL_JSON_NOMINATIONS_CALCULATOR_DATA  =  "${pageContext.request.contextPath}/nomination/nominationPromotionNodeCheck.do?method=nominationPromo";
    G5.props.URL_JSON_NOMINATIONS_CALCULATOR_SEND_INFO = "${pageContext.request.contextPath}/nomination/sendNominationCalculatorInfo.do";
    G5.props.URL_CERT_TPL_ROOT = G5.props.URL_ROOT + 'assets/tpl/';
    G5.props.URL_JSON_NOMINATIONS_CERTIFICATE_DATA = G5.props.URL_ROOT + 'nomination/viewCertificate.do?method=getCertificate';
    G5.props.URL_JSON_NOMINATIONS_SEND_BUDGET_REQUEST = G5.props.URL_ROOT+'claim/nominationsApprovalPage.do?method=requestMoreBudget';
    G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
    G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
    G5.props.URL_JSON_NOMINATIONS_CUMULATIVE = G5.props.URL_ROOT+'claim/nominationsApprovalPage.do?method=fetchCumulativeApprovalNominatorTableData';
    G5.props.URL_JSON_NOMINATIONS_APPROVALS_TRANSLATE_COMMENT =G5.props.URL_ROOT + 'claim/approvalTranslate.do';
    G5.props.URL_PDF_SERVICE = '${pdfServiceUrl}';

    var idJson = ${idJson};

    //attach the view to an existing DOM element
    var napv = new NominationsApprovalPageView({
        el:$('#nominationsApprovalPageView'),
        noSidebar : ${isDelegate},
        noGlobalNav : ${isDelegate},
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
        pageTitle : '<cms:contentText key="NOMINATION_APPROVALS" code="nomination.approvals.module"/>',
        idJson: idJson
    });

});

</script>

<script type="text/template" id="nominationsApprovalPageTplTpl">
  <%@include file="/nomination/tpl/nominationsApprovalPageTpl.jsp" %>
</script>
<script type="text/template" id="nominationsApprovalTableTplTpl">
  <%@include file="/nomination/tpl/nominationsApprovalTableTpl.jsp" %>
</script>
<script type="text/template" id="nominationsApprovalTableRowTplTpl">
  <%@include file="/nomination/tpl/nominationsApprovalTableRowTpl.jsp" %>
</script>
<script type="text/template" id="nominationsApprovalBudgetTplTpl">
  <%@include file="/nomination/tpl/nominationsApprovalBudgetTpl.jsp" %>
</script>
<script type="text/template" id="nominationsCalcTemplateTpl">
  <%@include file="/nomination/tpl/nominationsCalcTemplate.jsp" %>
</script>
<script type="text/template" id="nominationsCalcPayoutGridTplTpl">
  <%@include file="/nomination/tpl/nominationsCalcPayoutGrid.jsp" %>
</script>
<script type="text/template" id="nominationsCalcScoreWrapperTplTpl">
  <%@include file="/nomination/tpl/nominationsCalcScoreWrapper.jsp" %>
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp" %>
