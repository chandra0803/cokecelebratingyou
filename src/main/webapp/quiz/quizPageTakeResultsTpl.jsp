<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<a href="#" id="printPage" class="pageView_print pull-right btn btn-small">
    <cms:contentText key="PRINT" code="claims.quiz.submission" /><!--JAVA: remove this comment from the JSP {{cm.claims.quiz.submission.PRINT}}-->
    <i class="icon-printer"></i>
</a>

{{#if userPassed}}
<h3><cms:contentText key="PASSING_MESSAGE" code="claims.quiz.submission" /></h3>
{{else}}
<h3><cms:contentText key="YOU_DIDNT_PASS" code="claims.quiz.submission" /></h3>
{{/if}}

<p class="questionStats">
    <span><cms:contentText key="NUMBER_OF_QUESTIONS" code="claims.quiz.submission" />:</span> <strong>{{totalQuestions}}</strong>
    {{#if passingScore}}
    <br><span><cms:contentText key="NEEDED_TO_PASS" code="claims.quiz.submission" />:</span> <strong>{{passingScore}}</strong>
    {{/if}}
    <br><span><cms:contentText key="QUESTIONS_CORRECT" code="claims.quiz.submission" />:</span> <strong>{{questionsCorrect}}</strong>
    {{#unless userPassed}}
    <br><span><cms:contentText key="ATTEMPTS_REMAINING" code="claims.quiz.submission" />:</span> <strong>{{#if isAttemptsLimit}}{{attemptsRemaining}}{{else}}<cms:contentText key="UNLIMITED" code="report.quizzes.quizactivity" />{{/if}}</strong>
    {{/unless}}
</p>

{{#if showAward}}
<div class="container-splitter with-splitter-styles clearfix">
    {{#if quizAward}}
    <h4><cms:contentText key="YOU_EARNED" code="claims.quiz.submission.review" />: <span>{{quizAward}}</span></h4>
    {{/if}}

    {{#if certificate}}
    <div class="certificateWrapper">
        <img src="{{certificate.img}}" alt="{{certificate.name}} certificate preview">
        <p>
            <strong><cms:contentText key="YOU_ALSO_EARNED" code="claims.quiz.submission.review" /></strong>
            <a href="{{certificate.url}}" target="_blank">
                <span class="muted"><cms:contentText key="VIEW_DOWNLOAD" code="claims.quiz.submission" /></span>
            </a>
        </p>
    </div>
    {{/if}}

    {{#if badge}}
    <div class="badgeWrapper">
        <img src="{{badge.img}}" alt="{{badge.name}}">
        <p class="badge-name">{{badge.name}}</p>
    </div>
    {{/if}}
</div><!-- /.container-splitter.with-splitter-styles -->
{{/if}}

<h4 class="fullResults"><cms:contentText key="FULL_RESULTS" code="claims.quiz.submission" /></h4>
<ol class="results questions">
    {{#each questions}}
    <li>
        <h5 class="question">{{text}}</h5>
        <ol class="answers">
            {{#each answers}}
            <li class="{{#if isCorrect}}isCorrect{{/if}}{{#eq ../selectedAnswerId id}} {{#if ../answeredCorrectly}}correctAnswer{{else}}incorrectAnswer{{/if}}{{/eq}}">
                {{#eq ../selectedAnswerId id}}
                    {{#if ../answeredCorrectly}}
                    <i class="icon-check-circle"></i>
                    {{else}}
                    <i class="icon-cancel-circle"></i>
                    {{/if}}
                {{else}}
                    {{#if isCorrect}}<i class="icon-check"></i>{{/if}}
                {{/eq}}
                {{text}}
            </li>
            {{/each}}
        </ol>
        {{#if correctAnswerExplanation}}
        <p class="correctAnswerExplanation">
            {{#each correctAnswerExplanation}}<span>{{this}}</span>{{/each}}
        </p>
        {{/if}}
    </li>
    {{/each}}
</ol>


<div class="stepContentControls form-actions pullBottomUp">
    {{#if allowRetake}}
    <a class="btn btn-primary" href="{{retakeQuizUrl}}">
         <cms:contentText key="RETAKE_QUIZ" code="claims.quiz.submission" /> <i class="icon-reload"></i>
    </a>
    <a class="btn" href="<%= RequestUtils.getBaseURI(request)%>/quiz/checkQuizEligibilityAction.do?method=checkQuizEligibility">
        <cms:contentText key="DONE" code="claims.quiz.submission" />
    </a>
    {{else}}
    <a class="btn btn-primary" href="<%= RequestUtils.getBaseURI(request)%>/quiz/checkQuizEligibilityAction.do?method=checkQuizEligibility">
        <cms:contentText key="DONE" code="claims.quiz.submission" />
    </a>
    {{/if}}
</div>
