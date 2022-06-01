<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="challengepointManagerModuleTpl">
<!-- ======== CHALLENGEPOINT MANAGER MODULE ======== -->

<div class="module-liner">
    <div class="module-content">

        <h3 class="module-title">
            <a class="title-link" href="${pageContext.request.contextPath}/goalquest/goalquestShowRules.do?method=showManagerChallengePointRules">
                <img src="${pageContext.request.contextPath}/assets/img/goalquest/challengepoint.svg" alt="Challengepoint" class="gqtitlelogo"> <cms:contentText key="CHALLENGEPOINT_MANAGER" code="promotion.partner.goal.selection"/>
            </a>
        </h3>

        <div class="module-actions">
            <a href="${pageContext.request.contextPath}/goalquest/goalquestShowRules.do?method=showManagerChallengePointRules" class="btn btn-primary">
                <cms:contentText code="promotion.goalquest.selection.wizard" key="READ_RULES" />
            </a>
        </div>
    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>
