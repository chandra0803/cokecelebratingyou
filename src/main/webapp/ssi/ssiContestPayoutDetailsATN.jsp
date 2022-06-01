<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== CONTEST SUMMARY PAGE ======== -->

<div id="ssiContestPayoutDetails" class="contestPage ssiContestSummary page-content">

    <div id="ssiATNPayoutDetails">
        <!-- dynamic content -->
    </div>

    <script id="ssiATNPayoutDetailsTpl" type="text/x-handlebars-template">

        {{#if isApproveMode}}

            <div class="page-topper">
                <div class="row-fluid">
                    <div class="span8">
                        <h2>{{name}}</h2>
                        <dl class="dl-horizontal">
                            <dt>Approval Status</dt>

                            {{#if approvalRequired}}
                            {{#approvalLevels}}
                            <dd class="ssiApproverLevel">
                                {{name}}:
                                {{#if approved}}
                                    <cms:contentText key="APPROVED_BY" code="ssi_contest.approvals.detail"/> {{approvedBy}}
                                {{else}}
                                    <cms:contentText key="WAITING_APPROVAL" code="ssi_contest.approvals.detail"/>
                                <a href="#" class="ssiApproverPopover" data-participant-ids="[{{id}}]">View Approvers</a>
                                    <div class="ssiApproverList" style="display: none">
                                        <ul>
                                            {{#approverList}}
                                            <li>{{name}}</li>
                                            {{/approverList}}
                                        </ul>
                                    </div>
                                {{/if}}
                            </dd>
                            {{/approvalLevels}}

                            {{else}}
                            <dd><cms:contentText key="WAITING_APPROVAL" code="ssi_contest.approvals.detail"/></dd>
                            {{/if}}
                        </dl>
                    </div>
                    <div class="span4 buttons text-right">
                      <beacon:authorize ifNotGranted="LOGIN_AS">
                        <form id="ssiApproveContest" method="post" action="loadSSIContestIssuanceApprovalSummary.do?method=approve" enctype="multipart/form-data">
                            <input type="hidden" value="${ssiContestPayoutHistoryAwardThemNowForm.id}" name="clientState" />
                            <button type="submit" class="btn btn-primary ssiApproveContestBtn" name="status" value="approved"><cms:contentText key="APPROVE" code="ssi_contest.approvals.detail"/></button>
                            <button type="button" class="btn ssiDenyContestBtn"><cms:contentText key="DENY" code="ssi_contest.approvals.detail"/></button>
                        </form>
                      </beacon:authorize>
                    </div>
				</div>
            </div>

        {{else}}

            <div class="row-fluid">
                <div class="span12">
                    <div class="ssiPreviewSection well">
                        <div class="">
                            <h3>{{name}}</h3>
                                <dl class="dl-horizontal">
                                <dt><cms:contentText key="STATUS" code="ssi_contest.atn.summary"/></dt>
                                <dd>{{statusDescription}}</dd>
                            </dl>
                        </div>
                    </div>
                </div><!-- /.span12 -->
            </div><!-- /.row-fluid -->

        {{/if}}

    </script>



    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText key="PARTICIPANT_PAYOUTS" code="ssi_contest.atn.summary"/></h3>
            <div id="ssiATNHistory" class="spinnerOverlayWrap">
                <!-- dynamic content -->
            </div>
            <!-- JAVA NOTE: back url set in jsp -->
            	<a href="${ssiContestPayoutHistoryAwardThemNowForm.backButtonUrl}" class="btn"><cms:contentText key="BACK" code="ssi_contest.generalInfo"/></a>

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->



    <div class="ssiDenyContestPopover" style="display: none">
        <form id="ssiADenyContest" method="post" action="loadSSIContestIssuanceApprovalSummary.do?method=deny" enctype="multipart/form-data">
            <input type="hidden" value="${ssiContestPayoutHistoryAwardThemNowForm.id}" name="clientState" />
            <div class="control-group validateme"
                data-validate-fail-msgs='{"nonempty":"<cms:contentText key="DENIAL_REASON_REQUIRED" code="ssi_contest.approvals.summary"/>"}'
                data-validate-flags='nonempty'>
                <label class="control-label"><cms:contentText key="DENIAL_REASON" code="ssi_contest.approvals.summary"/></label>
                <div class="commentTools">
                    <cms:contentText key="REMAINING_CHARS" code="system.richtext"/>: <span class="remChars">&nbsp;</span>

                    <span class="spellchecker dropdown">
                        <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                title=<cms:contentText key="CHECK_SPELLING" code="ssi_contest.approvals.summary"/>
                                type="button"
                                data-toggle="dropdown">
                            <i class="icon-check"></i>
                        </button>
                        <ul class="dropdown-menu">
                            <li><a class="check"><b><cms:contentText key="CHECK_SPELLING" code="system.richtext"/></b></a></li>
                        </ul>
                    </span>
                </div>
                <div class="controls">
                    <textarea class="contribCommentInp" rows="4" maxlength="1000" placeholder="<cms:contentText key="ADD_A_COMMENT" code="ssi_contest.approvals.summary"/>" name="comment"></textarea>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary ssiDenyContestSubmit" name="status" value="denied"><cms:contentText key="SUBMIT" code="system.button"/></button>
                </div>
            </div>
        </form>
    </div><!--/.ssiDenyContestPopover-->

</div><!-- /#ssiContestPageEditView -->

<script>
    $(document).ready(function() {
        var contestJson = null;

        // JAVA NOTE: url for ATN history pagination
         G5.props.URL_JSON_CONTEST_SUMMARY_PAYOUT_DETAILS = G5.props.URL_ROOT+'ssi/displayPayoutHistoryAwardThemNow.do?method=paxNav';



		contestJson = ${ssiContestPayoutHistoryAwardThemNowForm.initializationJson};

        // FE ONLY - this should be removed before pushing to production
        // this section can be use to switch between different data types
        // using a GET value in the URL like:
        // ssiApprovePayout.html&contestType=stepItUp&payoutType=other
        (function(){
            if (!window.location.search) return;

            var search = window.location.search,
                parts  = search.split(/&(?:amp;)?/), // split at "&" or "&amp;"
                pairs  = function (key, str) {
                        var regex = (/^(?:(?:.+)?\?)?(\w+)=(.+)$/i), // get "layout.html?foo=bar" as "foo", "bar"
                            res   = regex.exec(str);
                        map[res[1]] = res[2]
                    },
                map = {};

            $.each(parts, pairs);

            // use GET value to over write contestData
         <%--  if (map.payoutType === 'other') {
                contestJson = {
                    id: "3other",
                    name: "Objective Creator Example",
                    status: "inprogress",
                    sortedOn: "lastName",
                    sortedBy: "asc",
                    totalActivity: "20,000",
                    totalPayoutAmount: "12,000",
                    payoutType: "other"
                };
            }

           // approve mode
            if(map.canApprove) {
                contestJson.canApprove = map.canApprove == 'true';
            }--%>

        }());
        // END FE ONLY





     var contest = new SSIContestPayoutDetails_ATN({
            el:$('#ssiContestPayoutDetails'),
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
            pageTitle : '<cms:contentText key="APPROVE_CONTEST" code="ssi_contest.approvals.summary"/>'
        });

    });
</script>

<script type="text/template" id="ssiContestPayoutDetailsTableTplTpl">
  <%@include file="ssiContestPayoutDetailsTableTpl.jsp" %>
</script>
<!-- paginationView template -->
<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>
