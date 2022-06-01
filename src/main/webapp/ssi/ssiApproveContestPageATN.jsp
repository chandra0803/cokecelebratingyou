<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div id="ssiApproveContestPageView" class="page-content">

    <div id="ssiApproveContestSummary">
        <script id="ssiApproveContestAtnTopTpl" type="text/x-handlebars-template">
        <div class="page-topper">
            <div class="row-fluid">
                <div class="span8">
                    <h2>{{name}}</h2>
                </div>
            </div>
        </div>
        </script>
    </div><!-- /#ssiApproveContestSummary -->

    <div id="ssiApproveContestDetails">
        <script id="ssiApproveContestAtnDetailsTpl" type="text/x-handlebars-template">
        <div class="row-fluid ssiSummaryDetails">
            <div class="span12">
                <dl class="dl-horizontal">
                    <dt><cms:contentText key="CREATED_BY" code="ssi_contest.approvals.summary"/></dt>
                    <dd>{{contestCreator}}</dd>
                    <dt><cms:contentText key="CONTEST_DATES" code="ssi_contest.approvals.summary"/></dt>
                    <dd>{{startDate}} - {{endDate}}</dd>
                    <dt><cms:contentText key="CONTEST_DESCRIPTION" code="ssi_contest.atn.summary"/> </dt>
                    <dd>{{description}}
                        {{#if attachmentTitle}}<a href="{{attachmentUrl}}" class="ssiApproveDetailsAttachment">{{attachmentTitle}}</a>{{/if}}
                    </dd>
                    {{#if badge.img}}
                        <dt><cms:contentText key="BADGE" code="ssi_contest.atn.summary"/></dt>
                        <dd><img src="{{badge.img}}" alt="{{badge.name}}" /> {{badge.name}}</dd>
                    {{/if}}
                    <dt><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary"/> </dt>
					{{#eq measureType "currency"}}
						<dd><cms:contentText key="CURRENCY" code="ssi_contest.payout_objectives"/></dd>
					{{else}}
						<dd><cms:contentText key="UNITS_NUMBER" code="ssi_contest.payout_objectives"/></dd>
					{{/eq}}

					<dt><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.payout_objectives"/></dt>
					{{#eq payoutType "points"}}
						<dd><cms:contentText key="POINTS" code="ssi_contest.payout_objectives"/></dd>
					{{else}}
						<dd><cms:contentText key="OTHER" code="ssi_contest.payout_objectives"/></dd>
					{{/eq}}
                </dl>
            </div>
        </div>
        </script>
    </div><!-- /#ssiApproveContestDetails -->


    <div class="awardHistoryWrapper row-fluid">
        <div class="span12">
            <hr class="section">
            <h3><cms:contentText key="AWARD_HISTORY" code="ssi_contest.atn.summary"/> </h3>
            <div id="ssiATNHistory" class="spinnerOverlayWrap">
                <!-- dynamic content -->
            </div>
        </div><!-- /.span12 -->
    </div><!-- /.awardHistoryWrapper.row-fluid -->

    <div class="ssiDenyContestPopover" style="display: none">
        <form id="ssiADenyContest" method="post" action="layout.html?tplPath=../apps/approvals/tpl/&tpl=approvalsPageIndex.html" enctype="multipart/form-data">
            <div class="control-group validateme"
                data-validate-fail-msgs='{"nonempty":"You must enter a reason for denial."}'
                data-validate-flags='nonempty'>
                <button type="button" class="close">&times;</button>
                <label class="control-label"><cms:contentText key="DENIAL_REASON" code="ssi_contest.approvals.summary"/>  </label>
                <div class="commentTools">
                    <cms:contentText key="REM_CHAR" code="ssi_contest.generalInfo"/>: <span class="remChars">&nbsp;</span>
                    <span class="spellchecker dropdown">
                        <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                title=<cms:contentText key="CHECK_SPELLING" code="ssi_contest.approvals.summary"/>
                                type="button"
                                data-toggle="dropdown">
                            <i class="icon-check"></i>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="check"><b><cms:contentText key="CHECKSPELLING" code="purl.contributor"/></b></a></li>
                        </ul>
                    </span>

                </div>
                <div class="controls">
                    <div class="contribCommentWrapper">
                        <textarea class="contribCommentInp" rows="4" maxlength="1000" placeholder="<cms:contentText key="ADD_A_COMMENT" code="ssi_contest.approvals.summary"/>" name="comment"></textarea>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary ssiDenyContestSubmit" name="status" value="denied"><cms:contentText key="SUBMIT" code="ssi_contest.generalInfo"/></button>
                </div>
            </div>
        </form>
    </div><!--/.ssiDenyContestPopover-->

</div><!--/#ssiApproveContestPageView-->

<!-- JAVA NOTE: render the following HTML if the user has just completed uploading progress successfully -->
	<c:if test="${showModal}">
	<div class="modal hide fade autoModal recognitionResponseModal">
		<div class="modal-header">
			<button class="close" data-dismiss="modal">
				<i class="icon-close"></i>
			</button>
            <h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
			<p>${modalMessage}</p>
		</div>
	</div>
   </c:if>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
       // var contestJson = null;
        var contestJson = ${ssiContestIssuanceApprovalForm.initializationJson};
        // JAVA NOTE: url for ATN history pagination
        G5.props.URL_JSON_CONTEST_SUMMARY_HISTORY = G5.props.URL_ROOT+'ssi/loadSSIContestIssuanceApprovalSummary.do?method=loadIssuances';

        // JAVA NOTE: url for ATN history pagination
        G5.props.URL_JSON_CONTEST_ATN_APPROVE = G5.props.URL_ROOT+'ssi/loadSSIContestIssuanceApprovalSummary.do';

        // JAVA NOTE: bootstrapped JSON
       /* contestJson = {
            id: "3",
            startDate: "01/01/2014",
            endDate: "02/01/2014",
            name: "Super Big Summer ATN Contest",
            description: "Lorem ipsum Esse eiusmod nostrud dolore laboris amet laborum cillum cillum dolor esse dolor mollit quis qui irure quis veniam consequat et qui sed anim est occaecat in commodo voluptate.",
            badge: {
                "id":2504,
                "name":"1 Year Anniversary",
                "img":"img/badges/service1_sm.png"
            },
            totalActivity : "20,000",
            totalPayoutAmount : "12,000",
            sortedOn: "dateCreated",
            sortedBy: "asc",
            totalParticipantsCount : "300",
            measureType: "currency",
            payoutType: "other",
            contestCreator: "Bob Smith"
        };*/

        var ssiApproveContestPageViewATN = new SSIApproveContestPageViewATN({
            el:$('#ssiApproveContestPageView'),
            contestJson: contestJson, // contest json to populate model
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
            pageTitle : '<cms:contentText key="TITLE" code="ssi_contest.approvals.summary"/>'
        });
    });
</script>

<!-- This is for pagination table  -->
<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>

<script type="text/template" id="ssiContestSummaryTableTplTpl">
 <%@include file="ssiContestSummaryTableTpl.jsp" %>
</script>
