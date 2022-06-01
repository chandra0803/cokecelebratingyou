<%@ include file="/include/taglib.jspf"%>

{
	"messages":[],
	"quizAnswer":[
		{
      <c:if test="${!responseGiven}">
        <c:set var="quizQuestion" value="${claim.currentQuizQuestion}"/>
      </c:if>
      <c:if test="${responseGiven}">
        <c:set var="quizQuestion" value="${claim.latestQuestionAnswered}"/>
      </c:if>
            <c:set var="selectedAnswerExplanation" value="${quizQuestion.quizQuestionAnswers[quizClaimSubmissionForm.answerIndex].questionAnswerExplanation}"/>
            "id" : <c:out value="${claim.currentQuizQuestion.id}"/>,
            "isUserCorrect": <c:out value="${correct}"/>,
			"questionAnswer": <c:out escapeXml="false" value="${quizClaimSubmissionForm.answerIndex}"/>,
			 <c:forEach var="correctAnswer" items="${quizQuestion.correctQuizQuestionAnswers}" varStatus="correctAnswerStatus">
			"questionAnswerTitle": "<cms:contentText key="CORRECT_ANSWER_IS" code="claims.quiz.submission"/><c:out escapeXml='false' value="${correctAnswer.questionAnswerText}"/>.",
			 </c:forEach>
			 <c:if test="${!empty selectedAnswerExplanation}">
			   "questionAnswerDescription":  "<c:out escapeXml="false" value="${selectedAnswerExplanation}"/>"
			 </c:if>
			 <c:if test="${empty selectedAnswerExplanation}">
			   "questionAnswerDescription": ""
			 </c:if>
			
		}		
	]
}		



