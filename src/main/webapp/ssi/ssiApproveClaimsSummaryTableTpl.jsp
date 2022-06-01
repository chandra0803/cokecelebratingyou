<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div class="paginationControls pagination pagination-right"></div>
<div class="emptyMsg alert" style="display:none"><cms:contentText key="ADD_NO_ONE" code="ssi_contest.creator"/></div>

<table class="table table-striped">
    <thead>
        <tr>
            <th class="sortHeader unsorted number" data-sort="claimNumber">
                <a href="#">
                    <cms:contentText key="CLAIM_NUMBER" code="claims.form.details"/>
                    <i class="icon-arrow-1-up"></i>
                    <i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="sortHeader unsorted date" data-sort="dateSubmitted">
                <a href="#">
                    <cms:contentText key="DATE_SUBMITTED" code="nomination.approval.details"/>
                    <i class="icon-arrow-1-up"></i>
                    <i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="sortHeader unsorted" data-sort="submitter">
                <a href="#">
                    <cms:contentText key="SUBMITTED_BY" code="ssi_contest.claims"/>
                    <i class="icon-arrow-1-up"></i>
                    <i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th class="sortHeader unsorted" data-sort="approver">
                <a href="#">
                    <cms:contentText key="APPROVED_BY" code="ssi_contest.approvals.detail"/>
                    <i class="icon-arrow-1-up"></i>
                    <i class="icon-arrow-1-down"></i>
                </a>
            </th>
            <th>
                <cms:contentText key="ACTIVITY" code="ssi_contest.payout_stepitup"/>
            </th>
            <th class="sortHeader unsorted" data-sort="status">
                <a href="#">
                    <cms:contentText key="STATUS" code="ssi_contest.atn.summary"/>
                    <i class="icon-arrow-1-up"></i>
                    <i class="icon-arrow-1-down"></i>
                </a>
            </th>
            {{#if canApprove}}
            <th class="approveDenyBtns">
                <form id="approveAllClaims" action="approveContestClaims.do">
                    <input type="hidden" name="contestId" value="{{contestId}}">
                    <input type="hidden" name="method" value="approveAll">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                    <button class="btn btn-primary ssiApproveAllClaimsBtn hidden">
                        <cms:contentText key="APPROVE_ALL" code="ssi_contest.claims"/>
                    </button>
                    </beacon:authorize>
                </form>
            </th>
            {{/if}}
        </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<!-- <div class="paginationConts pagination pagination-right"></div> -->
<div class="paginationControls pagination pagination-right"></div>

<!--subTpl.recordRow=

    <tr class="summary-item" data-participant-id="{{id}}">

        <td class="number">
            {{! JAVA NOTE: not sure how this url should be formatted }}
            <a href ="approveClaimDetail.do?method=displayClaimDetail&clientState={{id}}">{{claimNumber}}</a>
        </td>

        <td class="date">
            {{dateSubmitted}}
        </td>

        <td>
            {{submittedBy}}
        </td>

        <td>
            {{approvedBy}}
        </td>

        <td>
            {{#each activities}}
                {{this}}<br>
            {{/each}}
        </td>

        <td class="status">
            {{statusDescription}}
        </td>

        {{#if extraJSON.canApprove}}
        <td class="approveDenyBtns">
            {{#eq status "waiting_for_approval"}}
            <form action="approveContestClaims.do">
                <input type="hidden" name="claimId" value="{{id}}">
                <span class="approveModeButtons">
                    <button type="submit" name="method" value="approve" class="btn btn-primary btn-small ssiApproveClaimBtn">
                        Approve
                    </button>
                    <button class="btn btn-small ssiDenyClaimBtn" data-claim-id="{{id}}">
                        Deny
                    </button>
                </span>
            </form>
            {{/eq}}
        </td>
        {{/if}}

    </tr>

subTpl-->

<div class="noDataMessages row">
    <span class="hidden pending"><p><cms:contentText key="NO_PENDING_CLAIMS" code="ssi_contest.claims"/></p></span>
    <span class="hidden approved"><p><cms:contentText key="NO_APPROVED_CLAIMS" code="ssi_contest.claims"/></p></span>
    <span class="hidden denied"><p><cms:contentText key="NO_DENIED_CLAIMS" code="ssi_contest.claims"/></p></span>
    <span class="hidden all"><p><cms:contentText key="NO_CLAIMS_TO_DISPLAY" code="ssi_contest.claims"/></p></span>
</div>

{{#if canApprove}}
<div class="row-fluid alert {{#lt 0 claimsPendingCount}}alert-info{{else}}alert-warning{{/lt}}">
    <div class="span9">
        <input type="hidden" name="contestId" value="{{contestId}}">
        <p><cms:contentText key="AFTER_APPROVING_CLAIMS" code="ssi_contest.claims"/></p>        
    </div>
    <div class="span3">
        <button class="btn btn-primary ssiApproveClaimsUpdate pull-right" data-contest-id="{{contestId}}"><cms:contentText key="DONE" code="ssi_contest.claims"/></button>
    </div>
</div>
{{/if}}
