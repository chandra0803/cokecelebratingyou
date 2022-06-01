<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<%-- Front-end javascript property overrides --%>

<!-- ======== GAMIFICATION MODULE ======== -->
<script type="text/template" id="gamificationModuleTpl">

    <h3 class="section-header">
        <cms:contentText key="MY_BADGES" code="gamification.admin.labels" />
    </h3>

    <div class="module-liner">

        <ul class="unstyled sidebar-list badges">
            <!-- dynamic content -->
        </ul>

        <ol class="unstyled sidebar-list">
            <li class="item dashboard" data-route="profile/Badges">
                <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Badges">
				    <cms:contentText key="SEE_YOUR_BADGES" code="gamification.admin.labels" />
                </a>
            </li>
        </ol>

    </div><!-- /.module-liner -->
</script>

<!--Specify data view here. "id" should match item specified in view.js(in this case GamificationDataView.js)  -->
<script type="text/template" id="gamificationDataViewTpl">
    <%@include file="/gamification/gamificationDataView.jsp" %>
</script>
