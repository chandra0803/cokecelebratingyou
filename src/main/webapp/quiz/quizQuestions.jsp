<%@ include file="/include/taglib.jspf"%>

{
	"messages":[],
	"quizQuestion":[
		{	<c:if test="${claim != null}">
			<c:set var="questionNumber" value="${claim.questionsCompletedCount + 1}"/>
			<c:if test="${!responseGiven}">
        	  <c:set var="quizQuestion" value="${claim.currentQuizQuestion}"/>
            </c:if>
            <c:if test="${responseGiven}">
              <c:set var="quizQuestion" value="${claim.latestQuestionAnswered}"/>
            </c:if>
			"id" : <c:out value="${claim.currentQuizQuestion.id}"/>,
			"claimId" : <c:out value="${claim.id}"/>,
			"questionTitle":	"<cms:contentText key="QUESTION" code="quiz.question"/> <c:out value="${questionNumber}"/>",
			"questionText":		"<c:out escapeXml='false' value="${quizQuestion.questionText}"/>",
			"questionChoices":	
			[
			   <c:forEach items="${quizQuestion.quizQuestionAnswers}" var="quizQuestionAnswer" varStatus="answerStatus">
				{"choice": "<c:out escapeXml='false' value="${quizQuestionAnswer.questionAnswerText}"/>",
				 "answerIndex" : "<c:out value="${answerStatus.index}"/>"
				}				
				<c:if test="${(answerStatus.index+1) < fn:length(quizQuestion.quizQuestionAnswers)}">, </c:if>
				</c:forEach>				
			]
			</c:if>
			<c:if test="${claim == null}" >
			"id" : 1,
			"claimId" : 2,
			"questionTitle":"dummy",
			"questionText":"dummy",
			"questionChoices":	
			[
				{"choice":"1",
				 "answerIndex":"1"
				},
				{"choice":"2",
				 "answerIndex":"2"
				}								
			]
			</c:if>
		}		
	]
}



