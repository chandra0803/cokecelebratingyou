<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- ======== GOALQUEST PAGE MANAGER LIST ======== -->

<!-- go back + title -->
<div id="goalquestPageView" class="goalquest page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2>
                <%-- This page services both GQ and CP promotions, but we need to pick the right logo --%>                
                <c:choose>
                    <c:when test="${promotionType == 'challengepoint'}">
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/challengepoint.svg" alt="Challengepoint" class="gqtitlelogo">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo">
                    </c:otherwise>
                </c:choose>
            </h2>

            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><cms:contentText key="RULES" code="promotion.goalquest.selection.wizard"/></th>
                        <th><cms:contentText key="PROMOTION_DATES" code="promotion.goalquest.selection.wizard"/></th>
                    </tr>
                </thead>
                <tbody>
                	<c:forEach var="promoViewBean" items="${promotionViewBeans}">
                    <tr>
                        <td>
                        <c:choose>
                          <c:when test="${promoViewBean.honeycombProgram}">
                            <a href="${promoViewBean.detailsLink}" class="hcRulesLink" target="_blank">${ promoViewBean.promotionName }</a>
                          </c:when>
                          <c:otherwise>
                            <a href="${promoViewBean.detailsLink}" class="rulesLink">${ promoViewBean.promotionName }</a>
                          </c:otherwise>
                        </c:choose>
                        </td>
                        <td>${ promoViewBean.startDate }
                        <c:if test="${ promoViewBean.endDate != null }">
                          <cms:contentText key="THROUGH" code="promotion.goalquest.selection.wizard"/> ${ promoViewBean.endDate }
                        </c:if>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>

        </div>
    </div>

</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    $(document).ready(function(){

        //attach the view to an existing DOM element
        window.pageView = new GoalquestPageListView({
            el:$('#goalquestPageView'),
            pageNav : {
                back : {
                	text : '<cms:contentText key="BACK" code="system.button"/>',
	                url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                },
                home : {
                	text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="promotion.goalquest.list"/>',
            mode: 'manager'
        });

    });

</script>
