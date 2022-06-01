<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStatePasswordManager"%>
<%@page import="com.biperf.core.utils.ClientStateSerializer"%>

<!-- ======== QUIZ PAGE SHELL ======== -->

<div id="quizPageTakeView" class="quizPage quizPageTake page-content"><div class="row-fluid">

    <!-- DIV.row-fluid up next to wrapping div.page-content for IE7 -->
        <div class="span12">

            <!-- content generated by quizPageTakeTpl -->

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    	$(document).ready(function() {
	    <%
  	  	  	Long promotionId = null;
  	  	  	Long quizId = null;
  	  	  	String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
          	String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
          	String password = ClientStatePasswordManager.getPassword();
      	  	if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      	  	{
          		password = ClientStatePasswordManager.getGlobalPassword();
      	  	}
       
        	 // Deserialize the client state.
        	 Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        	 promotionId = (Long)clientStateMap.get( "promotionId" );
         	if(clientStateMap.get( "quizId" )!=null){
         	  quizId = (Long)clientStateMap.get( "quizId" );
        	 }
         	pageContext.setAttribute("quizId",quizId);
         	pageContext.setAttribute("promotionId",promotionId);
  		%>
  		//quizPageTakeQuiz.json
  		 G5.props.URL_JSON_QUIZ_PAGE_TAKE_QUIZ = G5.props.URL_ROOT+'quiz/quizPageTakeActiveQuizAction.do?method=fetchActiveQuiz&promotionId=${promotionId}&quizId=${quizId}'
  		//quizPageTakeQuestion.json
  		 G5.props.URL_JSON_QUIZ_PAGE_TAKE_QUESTION = G5.props.URL_ROOT+'quiz/quizPageTakeActiveQuizAction.do?method=fetchQuestion&promotionId=${promotionId}&quizId=${quizId}';
  		//quizPageTakeAnswer.json
  		G5.props.URL_JSON_QUIZ_PAGE_TAKE_ANSWER = G5.props.URL_ROOT+'quiz/quizPageTakeActiveQuizAction.do?method=saveAnswer&promotionId=${promotionId}&quizId=${quizId}';

        // the values for "wtName" are buried in a JSON response in the old version of this screen
        // quizPageActiveQuiz.json (static) and quizPageActiveQuizAction.do (live) are the sources
        var tabsJson = [
            {
                "id" : 1,
                "name" : "stepIntro",
                "isActive" : false,
                "state" : "unlocked",
                "contentSel" : ".wizardTabsContent .stepIntroContent",
                "wtNumber" : "",
                "wtName" : '<cms:contentText key="QUIZ_DETAILS" code="claims.quiz.submission" />',
                "hideDeedle" : false
            },
            {
                "id" : 2,
                "name" : "stepMaterials",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepMaterialsContent",
                "wtNumber" : "",
                "wtName" : '<cms:contentText key="VIEW_COURSE_MATERIAL" code="claims.quiz.submission" />',
                "hideDeedle" : false
            },
            {
                "id" : 3,
                "name" : "stepQuestions",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepQuestionsContent",
                "wtNumber" : "",
                "wtName" : '<cms:contentText key="TAKE_QUIZ" code="claims.quiz.submission" />',
                "hideDeedle" : false
            },
            {
                "id" : 4,
                "name" : "stepResults",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepResultsContent",
                "wtNumber" : "",
                "wtName" : '<cms:contentText key="VIEW_RESULTS" code="claims.quiz.submission" />',
                "hideDeedle" : false
            }
        ];

        // if desired, the quizJson object from core/base/ajax/quizPageTakeQuiz.json can be embedded here
        var quizJson;

        //attach the view to an existing DOM element
        G5.views.QuizPageTake = new QuizPageTakeView({
            el:$('#quizPageTakeView'),
            pageNav : {
            	back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/quiz/quizPage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="claims.quiz.submission" />',
            quizJson : quizJson,
            tabsJson : tabsJson
        });

    });
</script>

<!-- NOTE: the following includes were added to make this shell behave like the JSP -->
<script type="text/template" id="quizPageTakeTplTpl">
 	<%@include file="/quiz/quizPageTakeTpl.jsp" %>
</script>

<script type="text/template" id="quizPageTakeIntroTplTpl">
    <%@include file="/quiz/quizPageTakeIntroTpl.jsp" %>
</script>

<script type="text/template" id="quizPageTakeMaterialsTplTpl">
    <%@include file="/quiz/quizPageTakeMaterialsTpl.jsp" %>
</script>

<script type="text/template" id="quizPageTakeQuestionsTplTpl">
    <%@include file="/quiz/quizPageTakeQuestionsTpl.jsp" %>
</script>

<script type="text/template" id="quizPageTakeResultsTplTpl">
    <%@include file="/quiz/quizPageTakeResultsTpl.jsp" %>
</script>

<!-- wizardTab template -->
<script type="text/template" id="wizardTabTpl">
    <%@include file="/include/wizardTab.jsp" %>
</script>
