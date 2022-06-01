<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<li class="claimItem {{eo}}">
    <h4 class="title">
        <a href="${pageContext.request.contextPath}/claim/startClaimSubmission.do?method=newClaim&promotionId={{promotionId}}" class="promotion">{{promotionName}}</a>
    </h4>

    <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/ActivityHistory/promotionId={{promotionId}}" class="history"><cms:contentText key="HISTORY" code="claims.submission" /></a>


    <p class="sub">
        {{#if promotionEndDate}}
        <span class="ends">
            <cms:contentText key="ENDS" code="claims.submission" /> {{promotionEndDate}}
        </span>
        {{/if}}
    </p>


    <div class="row-fluid">
        <p class="span4 emphasized">
            <cms:contentText key="SUBMITTED" code="claims.submission" />: <span class="highlight">{{numberSubmitted}}</span>
        </p>
        <p class="span4 emphasized">
            <cms:contentText key="CLOSED" code="claims.submission" />: <span class="highlight">{{numberOfApprovals}}</span>
        </p>
    </div>
</li>
