<!-- ======== MANAGE QUIZZES PAGE ======== -->

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.ui.diyquiz.DIYQuizForm"%>
<%@page import="com.biperf.core.utils.BeanLocator"%>
<%@page import="com.biperf.core.service.system.SystemVariableService"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<%
  String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

  Long promotionId = (Long)request.getAttribute( "promotionId" );

  Map<String,Object> createNewQuiz = new HashMap<String,Object>();
  createNewQuiz.put( "source", "pax" );
  createNewQuiz.put( "type", "create" );
  createNewQuiz.put( "promotionId", promotionId);
  pageContext.setAttribute( "createUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareCreate", createNewQuiz ) );

%>

<div id="quizPageManageView" class="page-content">

    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12 form-inline">

                <fieldset>

                    <div class="control-group control-group-pull-right">
                        <!-- JAVA NOTE: set the proper add quiz URL on the HREF attribute below -->
                        <a href="<c:out value="${createUrl}"/>" class="addQuizBtn btn btn-primary">
                            <cms:contentText key="CREATE_QUIZ" code="quiz.manage.library" /> <i class="icon-plus-circle"></i>
                        </a>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                            <select id="quizFilterSelect" name="quizFilter" title='<cms:contentText key="FILTER_QUIZZES" code="quiz.manage.library" />'>
                                <!-- JAVA NOTE: adjust the list below as necessary (value will be sent to AJAX table request) -->
                                <option value="activePendingIncomplete"><cms:contentText key="ACTIVE_PENDING_QUIZZES" code="quiz.manage.library" /></option>
                                <option value="inactive"><cms:contentText key="INCOMPLETE_QUIZZES" code="quiz.manage.library" /></option>
                            </select>
                        </div>
                    </div>

                </fieldset>

            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="MANAGE_QUIZ" code="quiz.manage.library" /></h2>
            <div class="displayTableAjaxView" data-url="<%= RequestUtils.getBaseURI(request)%>/quiz/diyQuizManage.do?method=manageTable"></div>
        </div>
    </div>

    <!-- JAVA NOTE: render the following HTML if the user has just saved a quiz -->
    <c:if test="${displayPopup}">
	    <div class="modal hide fade autoModal recognitionResponseModal">
	        <div class="modal-header">
	            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
	            <h3><cms:contentText key="SUCCESS" code="claims.quiz.submission" /></h3>
	            <p><cms:contentText key="QUIZ_SAVED_FOR_LATER" code="quiz.manage.library" /></p>
	        </div>
	    </div>
	</c:if>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    $(document).ready(function(){
        //attach the view to an existing DOM element
        window.qpmv = new QuizPageManageView({
            el:$('#quizPageManageView'),
            pageNav : {
            	back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="MANAGE_QUIZ" code="quiz.manage.library" />'
        });
    });


</script>
