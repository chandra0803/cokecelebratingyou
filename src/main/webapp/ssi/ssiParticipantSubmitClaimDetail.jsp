<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<div id="ssiParticipantSubmitClaimDetail" class="page-content">
    <div class="row-fluid">
        <div class="span12">
            <h3>${paxClaim.contest.contestNameFromCM}</h3>
            <p><cms:contentText code="ssi_contest.claims" key="CLAIM_INFO" /></p>
            <dl class="dl-horizontal">
                <dt><cms:contentText code="ssi_contest.claims" key="CLAIM_NUMBER" /></dt>
                <dd>${paxClaim.claimNumber}</dd>
                <dt><cms:contentText code="ssi_contest.claims" key="DATE_SUBMITTED" /></dt>
                <dd>${paxClaim.displaySubmissionDate}</dd>
                <dt><cms:contentText code="ssi_contest.claims" key="CLAIM_STATUS" /></dt>
                <dd>${paxClaim.status.name}</dd>
                <c:if test="${not empty paxClaim.approveDenyDate}">
                	<c:choose>
                		<c:when test="${paxClaim.status.code eq 'approved'}">
                			<dt><cms:contentText code="ssi_contest.claims" key="APPROVED_BY" /></dt>
                			<dd>${paxClaim.approvedBy}</dd>
                			<dt><cms:contentText code="ssi_contest.claims" key="APPROVAL_DATE" /></dt>
                			<dd>${paxClaim.displayApproveDenyDate}</dd>
                		</c:when>
                		<c:otherwise>
                			<dt><cms:contentText code="ssi_contest.claims" key="DENIED_BY" /></dt>
                			<dd>${paxClaim.approvedBy}</dd>
                			<dt><cms:contentText code="ssi_contest.claims" key="DENIAL_DATE" /></dt>
                			<dd>${paxClaim.displayApproveDenyDate}</dd>
                		</c:otherwise>
                	</c:choose>
                </c:if>
            </dl>
            <dl class="dl-horizontal">
            <c:forEach items="${paxClaim.paxClaimFields}" var="paxClaimField" >
            	<c:if test="${not empty paxClaimField.fieldValue}">
            		<dt>${paxClaimField.claimField.formElement.i18nLabel}</dt>
                	<dd>${paxClaimField.fieldValue}</dd>
            	</c:if>
            </c:forEach>
            </dl>
            <a href="participantContestList.do?method=display&id=${id}" class="btn"><cms:contentText key="BACK" code="system.button" /></a>
        </div>
    </div>
</div><!--/#ssiParticipantSubmitClaimDetail -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        //attach the view to an existing DOM element
        var ssiPSCD = new SSIParticipantSubmitClaimDetailView({
            el: $('#ssiParticipantSubmitClaimDetail'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'participantContestList.do?method=display&id=${id}'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI( request )%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText code="ssi_contest.claims" key="CLAIM_DETAIL_TITLE" />'
        });
    });
</script>
