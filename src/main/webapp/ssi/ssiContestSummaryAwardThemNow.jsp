<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== CONTEST SUMMARY PAGE ======== -->

<div id="ssiContestSummary" class="contestPage ssiContestSummary page-content"><div class="row-fluid">

    <!-- DIV.row-fluid up next to wrapping div.page-content for IE7 -->
        <div class="span12">
            <div id="ssiATNGeneralInfo">
                <!-- dynamic content -->
            </div>

            <script id="ssiATNGeneralInfoTpl" type="text/x-handlebars-template">
                <div class="ssiPreviewSection well">
                    <div class="">
                        {{#eq status "live"}}
                        <a href="{{editUrl}}" class="btn btn-primary btn-small editStepBtn" data-step-name="stepInfo"> <cms:contentText key="EDIT_BUTTON" code="ssi_contest.atn.summary"/> <i class="icon-pencil2"></i></a>
                        {{/eq}}
                        <h3><cms:contentText key="GENERAL_INFO" code="ssi_contest.atn.summary"/></h3>
                            <dl class="dl-horizontal">
                            <dt><cms:contentText key="CONTEST_NAME" code="ssi_contest.atn.summary"/></dt>
                            <dd>{{name}}</dd>
                            <dt><cms:contentText key="CONTEST_DATES" code="ssi_contest.atn.summary"/></dt>
                            <dd>{{startDate}} - {{endDate}}</dd>
                            <dt><cms:contentText key="CONTEST_DESCRIPTION" code="ssi_contest.atn.summary"/></dt>
                            <dd>{{description}}
								{{#if attachmentTitle}}<a href="{{attachmentUrl}}" target="_blank" class="ssiApproveDetailsAttachment">{{attachmentTitle}}</a>{{/if}}
							</dd>
                            {{#if badge.img}}
                                <br>
                                <dt><cms:contentText key="BADGE" code="ssi_contest.atn.summary"/></dt>
                                <dd><img src="{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}</dd> <!-- NOTE FOR JAVA: need to be sure billcodes are included in data -->
                            {{/if}}
                            {{#ueq payoutType "other"}}
                            {{#if billCodeRequired}}
                                {{#if billCodes}}
                                    {{#if billCodes.length}}
                                            <dt>Charge Contest To</dt>
                                            {{#each billCodes}}
                                                {{#ueq index null}}
                                                    <dd>Bill Code {{inc index}} - {{billCodeName}}</dd>
                                                {{/ueq}}
                                            {{/each}}
                                        {{/if}}
                                    {{/if}}
                                {{/if}}
                            {{/ueq}}
                        </dl>
                        {{#eq status "live"}}
                        <a href="{{issueUrl}}" title="" class="btn btn-primary"><cms:contentText key="ISSUE_MORE_AWARDS" code="ssi_contest.atn.summary"/></a>
                        {{/eq}}
                    </div>
                </div>
            </script>
        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->


    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText key="AWARD_HISTORY" code="ssi_contest.atn.summary"/></h3>
            <div id="ssiATNHistory" class="spinnerOverlayWrap">
                <!-- dynamic content -->
            </div>
        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->

</div><!-- /#ssiContestPageEditView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        var contestJson = null;

        // JAVA NOTE: url for ATN history pagination
        G5.props.URL_JSON_CONTEST_SUMMARY_HISTORY = G5.props.URL_ROOT+'ssi/displayContestSummaryAwardThemNow.do?method=loadIssuances';

        // JAVA NOTE: bootstrapped JSON
        var contestJson = ${ssiContestSummaryAwardThemNowForm.initializationJson};

        var contest = new SSIContestSummary_ATN({
            el:$('#ssiContestSummary'),
            contestJson: contestJson, // contest json to populate model
            pageNav : {
            	 back : {
                     text : '<cms:contentText key="BACK" code="system.button" />',
                     url : 'creatorContestList.do?method=display#index'
                 },
                 home : {
                     text : '<cms:contentText key="HOME" code="system.general" />',
                     url : '<%=RequestUtils.getBaseURI( request )%>/homePage.do'
                 }
            },
            pageTitle : '<cms:contentText key="PAGE_TITLE" code="ssi_contest.atn.summary"/>'
        });
    });
</script>

<script type="text/template" id="ssiContestSummaryTableTplTpl">
 <%@include file="ssiContestSummaryAwardThemNowTableTpl.jsp" %>
</script>
<!-- paginationView template -->
<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>
