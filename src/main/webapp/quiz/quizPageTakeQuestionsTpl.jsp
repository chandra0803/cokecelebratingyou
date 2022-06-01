{{!debug}}
<!-- if there are errors, they will render here -->
{{#if isError}}
<div class="alert alert-error">
    {{#eq errorType "mismatch"}}<cms:contentText code="claims.quiz.submission" key="SUBMIT_RESPONSE" /> <code><cms:contentText code="claims.quiz.submission" key="SUBMIT_RESPONSE_QUESTIONID_MISMATCH" /></code>{{/eq}}

    {{#eq errorType "noserveranswer"}}<cms:contentText code="claims.quiz.submission" key="SCORING_RESPONSE" /> <code><cms:contentText code="claims.quiz.submission" key="SCORING_RESPONSE_SCORING_MISMATCH" /></code>{{/eq}}
</div>
{{/if}}

<!-- the action attribute will be used to post the answers -->
<form id="quizAnswersForm" method="post">
    <p class="lead">{{{text}}}</p>

    <!-- include any necessary hidden inputs here -->
    <input type="hidden" value="{{quizId}}" id="quizId" name="quizId" />
    <input type="hidden" value="{{id}}" id="questionId" name="questionId" />
    <input type="hidden" value="{{claimId}}" id="claimId" name="claimId" />

    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="OPTION_REQUIRED" code="claims.quiz.submission" />"}'>
    {{#each answers}}
        <div class="controls{{#if ../selectedAnswerId}}{{#if isCorrect}} isCorrect{{/if}}{{/if}}{{#eq ../selectedAnswerId id}} {{#if ../answeredCorrectly}}correctAnswer{{else}}incorrectAnswer{{/if}}{{/eq}}">
            <label class="radio">
                <input type="radio" name="selectedAnswer" value="{{id}}" {{#if ../selectedAnswerId}}disabled="disabled"{{/if}} {{#eq id ../selectedAnswerId}}checked="checked"{{/eq}}>
                <span class="optlabel">
                    {{#if ../selectedAnswerId}}
                        {{#eq ../selectedAnswerId id}}
                            {{#if ../answeredCorrectly}}
                            <i class="icon-check-circle correctAnswer"></i>
                            {{else}}
                            <i class="icon-cancel-circle incorrectAnswer"></i>
                            {{/if}}
                        {{else}}
                            {{#if isCorrect}}<i class="icon-check"></i>{{/if}}
                        {{/eq}}
                    {{/if}}

                    {{text}}
                </span>
            </label>
        </div>
    {{/each}}
    </div>

    {{#if selectedAnswerId}}
    <!-- This is currently hidden by CSS. Seems unncessary but I can bring it back if there are howls of protest -->
    <p class="lead results">
        {{#if answeredCorrectly}}
        <i class="icon-check-circle correctAnswer"></i>
        {{else}}
        <i class="icon-cancel-circle incorrectAnswer"></i>
        {{/if}}
        <cms:contentText key="CORRECT_ANSWER_IS" code="claims.quiz.submission" /> <strong>{{correctAnswerText}}</strong>.
    </p>
    {{/if}}

    {{#if answeredCorrectly}}
        {{#if correctAnswerExplanation}}
        <p class="correctAnswerExplanation">
            {{#each correctAnswerExplanation}}<span>{{this}}</span>{{/each}}
        </p>
        {{/if}}
    {{else}}
        {{#if incorrectAnswerExplanation}}
        <p class="incorrectAnswerExplanation">
            <span>{{incorrectAnswerExplanation}}</span>
        </p>
        {{/if}}
    {{/if}}

    <div class="stepContentControls form-actions pullBottomUp">
        {{#if selectedAnswerId}}
            {{#if _isLast}}
            <button class="btn btn-primary nextBtn">
                <cms:contentText key="VIEW_RESULTS" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
            </button>
            {{else}}
            <button class="btn btn-primary nextQuestionBtn">
                <cms:contentText key="NEXT_QUESTION" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
            </button>
            <button class="btn saveForLaterBtn">
                <cms:contentText key="SAVE_FOR_LATER" code="claims.quiz.submission" />
            </button>
            {{/if}}
        {{else}}
        <button class="btn btn-primary submitQuestionBtn">
            <cms:contentText key="SUBMIT_ANSWER" code="claims.quiz.submission" />
        </button>
        <button class="btn saveForLaterBtn">
            <cms:contentText key="SAVE_FOR_LATER" code="claims.quiz.submission" />
        </button>
        {{/if}}
    </div>
</form>
