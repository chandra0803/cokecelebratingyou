<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<%-- from version 1.3 of goalQuestModule.html --%>
<script type="text/template" id="goalquestModuleTpl">
<!-- ======== GOALQUEST MODULE ======== -->

<div class="module-liner">
    <div class="module-content">

        <h3 class="module-title">
            <a class="title-link" href="${pageContext.request.contextPath}/goalquest/goalQuestPromotions.do?method=showPromotions">
                <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo">
            </a>
        </h3>

        <div class="goalquestItemsWrapper">
            <!-- Dynamic content (goalquests) added here -->
        </div>
    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>

<script>
	$(document).ready(function() {
		//specific GoalQuest json data
		G5.props.URL_JSON_GOALQUEST_COLLECTION = G5.props.URL_ROOT+'goalQuestModuleAjax.do';
	});
</script>

<%-- Item page elements  --%>
<script type="text/template" id="goalquestItemTpl">
	<%@ include file="/goalquest/goalquestItem.jsp"%>
</script>
