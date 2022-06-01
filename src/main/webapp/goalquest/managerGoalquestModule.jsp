<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="goalquestManagerModuleTpl">
<!-- ======== GOALQUEST MANAGER MODULE ======== -->

<div class="module-liner">
    <div class="module-content">

        <h3 class="module-title">
            <a class="title-link" href="${pageContext.request.contextPath}/goalquest/goalquestShowRules.do?method=showManagerGoalQuestRules">
                <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo"> <cms:contentText code="promotion.goalquest.selection.wizard" key="GOALQUEST_MANAGER" />
            </a>
        </h3>

        <div class="module-actions">
            <c:choose>
                <c:when test="${isHoneycombLink eq true}">
                    <a href="${honeycombSSOLink}" class="btn btn-primary" target="_blank">
                        <cms:contentText code="promotion.goalquest.selection.wizard" key="READ_RULES" />
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/goalquest/goalquestShowRules.do?method=showManagerGoalQuestRules" class="btn btn-primary">
                        <cms:contentText code="promotion.goalquest.selection.wizard" key="READ_RULES" />
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>
