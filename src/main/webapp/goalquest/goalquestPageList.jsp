<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- ======== GOALQUEST PAGE ======== -->

<!--
    List Page for Participants, Partners and Managers
    - JSON powered
-->

<div id="goalquestPageListView" class="goalquest page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2>
                <%-- This page services both GQ and CP promotions, but we need to pick the right logo --%>
                <c:choose>
                    <c:when test="${ null!=challengepointDetailsForm }">
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/challengepoint.svg" alt="Challengepoint" class="gqtitlelogo">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo">
                    </c:otherwise>
                </c:choose>
            </h2>

            <div class="goalquestItemsWrapper">
                <%-- Dynamic content (goalquests) added here --%>
            </div>
        </div>
    </div><!-- /.row-fluid -->
</div><!-- /#goalquestPageRulesView -->

<%-- Item page elements  --%>
<script type="text/template" id="goalquestItemTpl">
	<%@ include file="/goalquest/goalquestItem.jsp"%>
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    	<%-- This page services both GQ and CP promotions, but we need to configure the right ajax call here --%>
    	<c:choose>
	    	<c:when test="${ null!=challengepointDetailsForm }">
	    		G5.props.URL_JSON_GOALQUEST_COLLECTION = G5.props.URL_ROOT + 'challengepointModuleAjax.do';
	    	</c:when>
    		<c:otherwise>
    			G5.props.URL_JSON_GOALQUEST_COLLECTION = G5.props.URL_ROOT + 'goalQuestModuleAjax.do';
    		</c:otherwise>
    	</c:choose>
        //attach the view to an existing DOM element
        window.pageView = new GoalquestPageListView({
            el:$('#goalquestPageListView'),
            pageNav : {
                back : {
                	text : '<cms:contentText key="BACK" code="system.button"/>',
                	url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.button"/>',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            <c:choose>
        	  <c:when test="${null!=challengepointDetailsForm}">
            	pageTitle : '<cms:contentText key="TITLE_CHALLENGEPOINT" code="promotion.goalquest.list"/>'
           	  </c:when>
        	  <c:otherwise>
        		pageTitle : '<cms:contentText key="TITLE" code="promotion.goalquest.list"/>'
   	          </c:otherwise>
        	</c:choose>
        });
    });
</script>
