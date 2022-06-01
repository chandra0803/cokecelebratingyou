<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<div id="ssiApproveClaimsDetailPageView" class="page-content">

    <div id="ssiApproveClaimsSummary">
        <script id="ssiApproveClaimSummaryTpl" type="text/x-handlebars-template">
        <div class="page-topper">
            <div class="row-fluid">
                <div class="span8">
                    <h2>{{name}}</h2>
                    <dl class="dl-horizontal">
                        <dt><cms:contentText code="ssi_contest.approvals.detail" key="APPROVAL_STATUS" /></dt>
                        {{#eq status "waiting_for_approval"}}<dd><cms:contentText key="WAITING_FOR_APPROVAL" code="ssi_contest.claims"/></dd>{{/eq}}

                        {{#eq status "approved"}}
                        <dd><cms:contentText key="APPROVED" code="ssi_contest.claims"/></dd>

                        <dt></dt>
                        <dd>{{dateApproved}} <cms:contentText key="BY" code="ssi_contest.claims"/> {{approvedBy}}</dd>
                        {{/eq}}

                        {{#eq status "denied"}}
                        <dd><cms:contentText key="DENIED" code="ssi_contest.claims"/></dd>

                        <dt></dt>
                        <dd>{{dateDenied}} <cms:contentText key="BY" code="ssi_contest.claims"/> {{deniedBy}}</dd>
                        <dt></dt>
                        <dd>{{deniedReason}}</dd>
                        {{/eq}}
                    </dl>
                </div>
                <div class="span4 buttons text-right">
                    {{#if showApproveDeny}}
                    <form id="ssiApproveContest" method="post" action="approveContestClaims.do?method=approve" enctype="multipart/form-data">
                        <input type="hidden" value="{{clientState}}" name="clientState" />
						<button type="submit" class="btn btn-primary ssiApproveContestBtn" name="status" value="approved"><cms:contentText key="APPROVE" code="ssi_contest.approvals.detail"/></button>
                        <button type="button" class="btn ssiDenyClaimBtn"><cms:contentText key="DENY" code="ssi_contest.approvals.detail"/></button>
                    </form>
                    {{/if}}
                </div>
            </div>
        </div>

        <div class="row-fluid ssiClaimsSummaryDetails">
            <div class="span12">
                <dl class="dl-horizontal">
                    <dt><cms:contentText key="CLAIM_NUMBER" code="claims.form.details"/>:</dt>
                    <dd>{{id}}</dd>

                    <dt><cms:contentText key="DATE_SUBMITTED" code="nomination.approval.details"/></dt>
                    <dd>{{dateSubmitted}}</dd>

                    <dt><cms:contentText key="SUBMITTED_BY" code="ssi_contest.claims"/></dt>
                    <dd>{{submittedBy}}</dd>
                </dl>
            </div>
        </div>

        {{#if activities}}
            {{#eq contestType "2"}}
            <div class="row-fluid">
                <div class="span12">
                    <table class="ssiClaimActivitiesTable table table-striped">
                        <thead>
                            <tr>
                                <th><cms:contentText key="ACTIVITY" code="ssi_contest.payout_stepitup"/></th>
                                {{#eq measureType "currency"}}
                                <th><cms:contentText key="AMOUNT" code="ssi_contest.participant"/></th>
                                {{else}}
                                <th><cms:contentText key="QUANTITY" code="ssi_contest.creator"/></th>
                                {{/eq}}
                            </tr>
                        </thead>
                        <tbody>
                        {{#each activities}}
                        <tr>
                            <td>{{activityDescription}}</td>
                            <td>{{activityAmount}}</td>
                        </tr>
                        {{/each}}
                        </tbody>
                    </table>
                </div>
            </div>
        {{else}}
            <div class="row-fluid ssiClaimsSummaryDetails">
                <div class="span12">
                    <dl class="dl-horizontal">
                        {{#each activities}}
                            <dt><cms:contentText key="ACTIVITY" code="ssi_contest.payout_stepitup"/>:</dt>
                            <dd>{{activityDescription}}</dd>

                            {{#eq measureType "currency"}}
                            <dt><cms:contentText key="AMOUNT" code="ssi_contest.participant"/>:</dt>
                            {{else}}
                            <dt><cms:contentText key="QUANTITY" code="ssi_contest.creator"/>:</dt>
                            {{/eq}}
                            <dd>{{activityAmount}}</dd>
                        {{/each}}
                    </dl>
                </div>
            </div>
	{{/eq}}
        {{/if}}

        <div class="row-fluid ssiClaimsSummaryDetails">
            <div class="span12">
               <dl class="dl-horizontal">
                {{#each fields}}
                    <dt>{{name}}</dt>
                    {{#if docURL}}
                    <dd><a href="{{docURL}}" target="_blank">{{description}}</a></dd>
                    {{else}}
                    <dd>{{#if description}}{{description}}{{else}}&nbsp;{{/if}}</dd>
                    {{/if}}
                {{/each}}
                </dl>
            </div>
        </div>
        </script>

    </div>

    <div class="row-fluid">
        <div class="span12">
            <a href="${ssiContestClaimDetailForm.backButtonUrl}" class="btn"><cms:contentText key="BACK" code="ssi_contest.generalInfo"/></a>
        </div>
    </div>


    <div class="ssiDenyClaimPopover" style="display: none">
        <!-- JAVA NOTE: Not sure what URL this needs to POST to -->
        <form id="ssiDenyClaim" method="post" action="approveContestClaims.do?method=deny" enctype="multipart/form-data">
            <div class="control-group validateme"
                data-validate-fail-msgs='{"nonempty":"You must enter a reason for denial."}'
                data-validate-flags='nonempty'>
                <button type="button" class="close">&times;</button>
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
                            <li><a class="check"><b><a class="check"><b><cms:contentText key="CHECK_SPELLING" code="system.richtext"/></b></a></li>
                        </ul>
                    </span>
                </div>
                <div class="controls">
                    <div class="contribCommentWrapper">
                        <textarea class="contribCommentInp" rows="4" maxlength="1000" placeholder="<cms:contentText key="ADD_A_COMMENT" code="ssi_contest.approvals.summary"/>" name="comment"></textarea>
                    </div>
                </div>
                <div>
                    <button type="submit" class="btn btn-primary ssiDenyClaimSubmit" name="status" value="denied"><cms:contentText key="SUBMIT" code="system.button"/></button>
                </div>
            </div>
        </form>
    </div><!--/.ssiDenyClaimPopover-->
</div><!--/#ssiApproveClaimsDetailPageView-->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        var claimData = ${ssiContestClaimDetailForm.initializationJson};

         //attach the view to an existing DOM element
        var ssiNav = new SSIApproveClaimsDetailPageView({
            el: $('#ssiApproveClaimsDetailPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI( request )%>/homePage.do'
                }
            },
            pageTitle : '${pageTitle}',
            claimData: claimData
        });
    });
</script>
