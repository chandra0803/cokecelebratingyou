{{{introText}}}
{{#if totalQuestions}}
    <p class="questionStats">
        <span><cms:contentText key="TOTAL_NUMBER_OF_QUESTIONS" code="claims.quiz.submission" />:</span> <strong>{{totalQuestions}}</strong>
        {{#if passingScore}}<br><span><cms:contentText key="TOTAL_NEEDED_TO_PASS" code="claims.quiz.submission" />:</span> <strong>{{passingScore}}</strong>{{/if}}
    </p>
{{/if}}

<div class="stepContentControls form-actions pullBottomUp">
    {{#if materials}}
    <button class="btn btn-primary nextBtn">
        <cms:contentText key="CONTINUE_COURSE" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
    </button>
    {{else}}
        {{#if _allQuestionsAnswered}}
        <button class="btn btn-primary resultsBtn">
            <cms:contentText key="VIEW_RESULTS" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
        </button>
        {{else}}
        <button class="btn btn-primary nextBtn">
            <cms:contentText key="START_TEST" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
        </button>
        {{/if}}
    {{/if}}
</div><!-- /.stepContentControls -->
