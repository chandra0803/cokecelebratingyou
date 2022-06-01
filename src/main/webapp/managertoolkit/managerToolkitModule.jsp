<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.*" %>

<!-- ======== MANAGER TOOLKIT MODULE ======== -->
<c:if test="${showToolkit}">

<script type="text/template" id="managerToolkitModuleTpl">
<div class="module-liner">

    <div class="module-content">
        <h3 class="module-title"><cms:contentText key="TITLE" code="hometile.manager.toolkit" /></h3>
        <div class="module-actions">

            <ul class="button-container">
                <c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
                <c:if test="${displayBudgetTransfer}">
                    <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransfer.do?method=display"><cms:contentText key="BUDGET_ALLOCATION" code="hometile.manager.toolkit" /></a></li>
                </c:if>
                </c:if>

                <c:if test="${displayLeaderBoard}">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                        <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="leaderBoardsDetailPage.do?method=getUserRole">
                            <cms:contentText key="CREATE_LEADERBOARD" code="hometile.manager.toolkit" /></a>
                        </li>
                    </beacon:authorize>
                </c:if>

                <c:if test="${displayManageContests}">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                        <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<%= RequestUtils.getBaseURI(request)%>/ssi/creatorContestList.do?method=display">
                            <cms:contentText key="MANAGE_CONTESTS" code="hometile.manager.toolkit" /></a>
                        </li>
                    </beacon:authorize>
                </c:if>

                <c:if test="${showResource}">
                   <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<%= RequestUtils.getBaseURI(request)%>/resourceCenterPage.do"><cms:contentText key="RESOURCE_CENTER" code="home.rail" /></a></li>
                </c:if>

                <c:if test="${displayRosterMgmt}">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                        <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<%= RequestUtils.getBaseURI(request)%>/rosterMgmt.do?method=list"><cms:contentText key="ROSTER_MGMT" code="hometile.manager.toolkit" /></a></li>
                    </beacon:authorize>
                </c:if>

                <c:if test="${displayManageQuizzes}">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                        <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<%= RequestUtils.getBaseURI(request)%>/quiz/diyQuizManage.do?method=manage">
                            <cms:contentText key="MANAGE_QUIZ" code="hometile.manager.toolkit" /></a>
                        </li>
                    </beacon:authorize>
                </c:if>

    			<c:if test="${showDIYCommunication}">
                    <beacon:authorize ifNotGranted="LOGIN_AS">
                        <li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<%= RequestUtils.getBaseURI(request)%>/participant/manageCommunicationsPage.do">
                            <cms:contentText key="MANAGE_COMMUNICATIONS" code="hometile.manager.toolkit" /></a>
                        </li>
                    </beacon:authorize>
                </c:if>

    		</ul>
        </div>
    </div>
</div>
</script>

</c:if>
