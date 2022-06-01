<%@page import="com.biperf.core.domain.claim.QuizClaim"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.ui.claim.QuizHistoryValueObject"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== PROFILE: VIEW QUIZ DETAILS ======== -->
<!-- go back + title -->
  <div id="profilePageQuizDetailView" class="quizPage-liner page-content">
      <div class="row-fluid">
          <div class="span11">
            <ul class="export-tools fr">
                <li>
                  <a href="#" class="pageView_print btn btn-small">
                    <cms:contentText key="PRINT" code="system.button"/>
                    <i class="icon-printer"></i>
                  </a>
                </li>
            </ul>
            <c:choose>
              <c:when test="${claim ne null && claim.promotion ne null && claim.promotion.promotionType.code eq 'diy_quiz'}">
                <h3><c:out value="${claim.quiz.name}" /> </h3>
              </c:when>
              <c:otherwise>
                <h3><c:out value="${claim.promotion.name}" /> </h3>
              </c:otherwise>
            </c:choose>
          </div>
      </div>

      <div class="row-fluid">
          <div class="span12">
              <h4><cms:contentText key="QUIZ_RESULT" code="quiz.history.detail"/></h4>
              <dl class="dl-horizontal dl-h1">
                  <dt> <cms:contentText key="QUIZ_RESULT" code="quiz.history.detail"/></dt>
                  <dd>
                     <c:choose>
                        <c:when test="${claim.pass}">
                          <cms:contentText code="quiz.history.detail" key="PASS" />
                        </c:when>
                        <c:when test="${claim.quizComplete}">
                          <cms:contentText code="quiz.history.detail" key="DID_NOT_PASS" />
                        </c:when>
                      </c:choose>

                      <c:if test="${claim.pass && claim.promotion.includePassingQuizCertificate}">
                        <%  Map parameterMap = new HashMap();
                          Claim temp = (Claim)request.getAttribute( "claim" );
                          parameterMap.put( "claimId", temp.getId() );
                          parameterMap.put( "promotionId", temp.getPromotion().getId() );
                          parameterMap.put( "userId", temp.getSubmitter().getId() );
                          pageContext.setAttribute("certificateUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/claim/displayCertificate.do?method=showCertificateQuizDetail", parameterMap ) );
                        %>
                          &nbsp;&nbsp;
                          <a href='<c:out value="${certificateUrl}"/>' target="_new" class="content-link">
                              <cms:contentText key="CERTIFICATE" code="quiz.history.detail"/>
                          </a>
                      </c:if>

                      <c:if test="${claim.pass}">
                        <c:if test="${claim.promotion.awardActive}">
                        <c:if test="${journal.displayTransactionAmount != null }">
                        <dt>
                              <cms:contentText key="POINTS" code="quiz.history.detail"/>
                          </dt>
                          <dd>
                            <c:out value="${journal.displayTransactionAmount}"/>
                          </dd>
                          </c:if>
                          <c:if test="${journal.displayTransactionDate != null }">
                          <dt>
                              <cms:contentText key="AWARD_DATE" code="quiz.history.detail"/>
                          </dt>
                          <dd>
                              <c:out value="${journal.displayTransactionDate}"/>
                          </dd>
                          </c:if>
                        </c:if>
                      </c:if>
                  </dd>

                  <dt><cms:contentText key="NUMBER_CORRECT" code="quiz.history.detail"/></dt>
                  <dd> <c:out value="${claim.score}"/></dd>

                  <dt><cms:contentText key="NEEDED_TO_PASS" code="quiz.history.detail"/></dt>
                  <dd> <c:out value="${claim.passingScore}"/></dd>

                  <dt><cms:contentText key="DATE_COMPLETED" code="quiz.history.detail"/></dt>
                  <dd><c:out value="${quizHistoryValueObject.displayDateCompleted}" /></dd>
                  <c:if test="${quizHistoryValueObject.attemptsRemaining}">
                    <dt> <cms:contentText key="ATTEMPTS_REMAINING" code="quiz.history.detail"/></dt>
                    <dd>
                      <c:out value="${quizHistoryValueObject.quizAttemptsRemaining}" />&nbsp;&nbsp;
                    <c:if test="${quizHistoryValueObject.retakeQuiz}">
                      <%  Map paramMap = new HashMap();
                        QuizClaim temp = (QuizClaim)request.getAttribute( "claim" );
                        paramMap.put( "promotionId", temp.getPromotion().getId() );
                        if(temp.getPromotion().isDIYQuizPromotion(  )){
                          paramMap.put( "quizId", temp.getQuiz(  ).getId() );
                        }
                        pageContext.setAttribute("retakeQuizUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/quiz/quizPageTake.do", paramMap ) );
                      %>
                        <a href="<c:out value='${retakeQuizUrl}'/>" >
                            <cms:contentText code="quiz.history" key="RETAKE_QUIZ" />
                          </a>
                    </c:if>
                    </dd>
                  </c:if>

                <c:if test="${quizHistoryValueObject.badgeImgUrl != null}">
                <dt><cms:contentText key="BADGE" code="quiz.history.detail"/></dt>
                <dd><img src="<c:out value='${quizHistoryValueObject.badgeImgUrl}'/>" alt=""></dd>
                </c:if>
              </dl>
          </div>
      </div>

      <div class="row-fluid">
          <div class="span12">
              <h4><cms:contentText key="QUIZ_REVIEW" code="quiz.history.detail"/></h4>
          </div>
      </div>

      <c:forEach items="${claim.quizResponses}" var="quizResponse" varStatus="quizResponseStatus">
          <div class="row-fluid">
            <div class="span2">
                <p><strong><cms:contentText key="QUESTION" code="quiz.history.detail"/>&nbsp;<c:out value="${quizResponseStatus.count}"/></strong></p>
            </div>
            <div class="span1">
              <p>
                <c:if test="${quizResponse.correct}">
                    <cms:contentText key="CORRECT_LOWER" code="quiz.history.detail"/>
                </c:if>
                <c:if test="${!quizResponse.correct}">
                    <cms:contentText key="INCORRECT_LOWER" code="quiz.history.detail"/>
                </c:if>
              </p>
            </div>
            <div class="span9">
                <p><c:out escapeXml='false' value="${quizResponse.quizQuestion.questionText}"/></p>
                <p><cms:contentText key="YOUR_RESPONSE" code="quiz.history.detail"/> <c:out escapeXml='false' value="${quizResponse.selectedQuizQuestionAnswer.questionAnswerText}"/><br />
                </p>
            </div>
          </div>
      </c:forEach>
	  <c:forEach items="${claim.quizResponses}" var="quizResponse" varStatus="quizResponseStatus">
                <c:forEach items="${quizResponse.quizQuestion.correctQuizQuestionAnswers}" var="quizQuestionAnswer" varStatus="answerStatus">
                  <c:if test="${answerStatus.first}">
                    <c:if test="${answerStatus.last}">
                        <c:if test="${!quizResponse.correct}">
                            <cms:contentText key="CORRECT_RESPONSE" code="quiz.history.detail"/>
                          <c:out escapeXml='false' value="${quizQuestionAnswer.questionAnswerText}"/>
                        </c:if>
                    </c:if>
                    <c:if test="${!answerStatus.last}">
                      <cms:contentText key="CORRECT_RESPONSES" code="quiz.history.detail"/>
                      <c:out escapeXml='false' value="${quizQuestionAnswer.questionAnswerText}"/>
                    </c:if>
                  </c:if>
                </c:forEach>
	  </c:forEach>      
  </div> <!-- ./quizPage-liner -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
        //attach the view to an existing DOM element
      var prp = new ApprovalsSearchModelView({
        el:$('#profilePageQuizDetailView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '${pageContext.request.contextPath}/participantProfilePage.do#profile/ActivityHistory/promotionId=allQuizzes'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="quiz.history" />'
      });
    });
</script>
