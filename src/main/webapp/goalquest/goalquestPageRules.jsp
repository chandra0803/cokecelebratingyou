<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- ======== GOALQUEST PAGE ======== -->

<!--
    Rules Page for Participants, Partners and Managers
    - JSP powered
-->

<div id="goalquestPageRulesView" class="goalquest page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2>
                <%-- This page services both GQ and CP promotions, but we need to pick the right logo --%>
                <c:choose>
                    <c:when test="${promotionMenuBean.promotion.promotionType.code == 'challengepoint'}">
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/challengepoint.svg" alt="Challengepoint" class="gqtitlelogo">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo">
                    </c:otherwise>
                </c:choose>
            </h2>
            <ul class="export-tools fr">
                <li><a class="pageView_print btn btn-small" href="#"><cms:contentText key="PRINT" code="system.button"/> <i class="icon-printer"></i></a></li>
            </ul>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h3><c:out value="${promotionMenuBean.promotion.promoNameFromCM}"/></h3>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <dl class="dl-horizontal dl-h1">
               <dt><cms:contentText key="PROMOTION_OBJECTIVE" code="promotion.goalquest.selection.wizard"/></dt>
               <dd><c:out value="${ promotionMenuBean.promotion.objectiveFromCM }" escapeXml="false"/></dd>
               <dt><cms:contentText key="PROGRAM_PERIOD" code="promotion.goalquest.selection.wizard"/></dt>
               <dd><fmt:formatDate value="${ promotionMenuBean.promotion.submissionStartDate }" type="date" pattern="${sessionScope.loggedInUserDate}"/> <cms:contentText key="THROUGH" code="promotion.goalquest.selection.wizard"/> <fmt:formatDate value="${ promotionMenuBean.promotion.submissionEndDate }" type="date" pattern="${sessionScope.loggedInUserDate}"/></dd>
            </dl>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <c:out value="${ rules }" escapeXml="false"/>
        </div>
    </div>

</div><!-- /#goalquestPageRulesView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
        //attach the view to an existing DOM element
        window.pageView = new PageView({
            el:$('#goalquestPageRulesView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.button"/>',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            <c:choose>
              <c:when test="${promotionMenuBean.promotion.promotionType.code == 'challengepoint'}">
                pageTitle : '<cms:contentText key="CP_RULES_TITLE" code="promotion.goalquest.selection.wizard"/>'
              </c:when>
              <c:otherwise>
                pageTitle : '<cms:contentText key="PAGE_TITLE" code="promotion.goalquest.selection.wizard"/>'
              </c:otherwise>
            </c:choose>
        });
    });
</script>
