<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStatePasswordManager"%>
<%@page import="com.biperf.core.utils.ClientStateSerializer"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf"%>

{{!debug}}
<h2>{{name}}</h2>
<div class="intro">{{{introText}}}</div>
<hr>

<!-- the action attribute will be used to post the answers -->
<form id="surveyAnswersForm" method="post" action="#" class="surveyForm{{#if isComplete}} complete {{#if showResults}}results{{/if}}{{/if}}">
    <!-- include any necessary hidden inputs here -->
    <input type="hidden" value="{{id}}" id="id" name="id" />
    <input type="hidden" value="{{promotionId}}" id="promotionId" name="promotionId" />

    {{#if multiNode}}
    <div class="nodes">
        <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="SELECT_NODE" code="survey.take" />"}'>
            <label class="control-label" for="nodes"><cms:contentText key="NODE_SELECTION" code="survey.take" /></label>
            <div class="controls">
                <select id="nodes" name="nodes">
                    {{#each nodes}}
                    <option value="{{id}}" {{#if isChosen}}selected="selected"{{/if}}>{{name}}</option>
                    {{/each}}
                </select>
            </div>
        </div>
    </div>
    <hr>
    {{else}}
        {{#each nodes}}
        <input type="hidden" value="{{id}}" id="nodes" name="nodes" />
        {{/each}}
    {{/if}}

    <b class="questionHead"><cms:contentText key="QUESTION_TITLE" code="survey.take" /></b>
    {{#each questions}}
    <h3 class="questionHead"><cms:contentText key="QUESTION" code="survey.take" /> {{index}}</h3>
    {{#eq type "essay"}}
        <div class="control-group validateme type-essay" data-validate-flags="{{#unless isOptional}}nonempty,{{/unless}}maxlength" data-validate-max-length="4000" data-validate-fail-msgs='{ {{#unless isOptional}}"nonempty" : "<cms:contentText key="ANSWER_REQUIRED" code="survey.take" />",{{/unless}} "maxlength" : "<cms:contentText key="MAX_LENGTH" code="survey.take" />"}'>
            <label class="control-label" for="{{../id}}">
                {{text}}
                {{#if isOptional}}<span class="optional"><cms:contentText key="OPTIONAL" code="survey.take" /></span>{{/if}}
            </label>
            <div class="controls">
                <textarea name="{{id}}" id="{{id}}">{{#if isAnswered}}{{answers.0.text}}{{/if}}</textarea>
            </div>
        </div>
    {{else}}
        {{#eq type "range"}}
        <div class="control-group type-range{{#unless isOptional}} validateme{{/unless}}"{{#unless isOptional}} data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="ANSWER_REQUIRED" code="survey.take" />"}'{{/unless}}>
            <label class="control-label" for="{{../id}}">
                {{text}}
                {{#if isOptional}}<span class="optional">(optional)</span>{{/if}}
            </label>
            <div class="controls orientation-{{slider.orientation}} labels-{{#or slider.minLabel slider.maxLabel}}on{{else}}off{{/or}}">
                <input type="text" name="{{id}}" id="{{id}}" class="slider"
                    data-slider-min="{{slider.min}}"
                    data-slider-max="{{slider.max}}"
                    data-slider-step="{{slider.step}}"
                    data-slider-value="{{slider.value}}"
                    data-slider-tooltip="{{slider.tooltip}}"
                    data-slider-orientation="{{slider.orientation}}"
                    {{#if isAnswered}}
                    value="{{slider.value}}"
                    {{/if}}>
                {{#or slider.minLabel slider.maxLabel}}
                <div class="labels">
                    <span class="min">{{slider.minLabel}}</span>
                    <span class="max">{{slider.maxLabel}}</span>
                </div>
                {{/or}}
            </div>
        </div>
        {{else}}
        <div class="control-group type-{{type}}{{#unless isOptional}} validateme{{/unless}}"{{#unless isOptional}} data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="ANSWER_REQUIRED" code="survey.take" />"}'{{/unless}}>
            <p class="questionText">{{{text}}}</p>
            {{#each answers}}
            <div class="controls">
                {{#and ../../isComplete ../../showResults}}
                    {{#if isChosen}}<strong>{{/if}}{{text}}{{#if isChosen}}</strong>{{/if}}
                    <div class="progress{{#if isChosen}} progress-info{{/if}}">
                        <div class="bar" style="width: {{percent}}%;">{{count}}</div>
                    </div>
                {{else}}
                    {{#eq ../type "radio"}}
                    <label class="radio">
                        <input type="radio" name="{{../id}}" value="{{id}}" {{#if ../isAnswered}}{{#if isChosen}}checked="checked"{{/if}}{{/if}}>
                        <span class="optlabel">{{text}}</span>
                    </label>
                    {{/eq}}
                    <!-- Checkbox-type questions currently aren't possible, but I started working on them so I'm leaving this in for potential future additions -->
                    {{#eq ../type "checkbox"}}
                    <label class="checkbox">
                        <input type="checkbox" name="{{../id}}" value="{{id}}" {{#if ../isAnswered}}{{#if isChosen}}checked="checked"{{/if}}{{/if}}>
                        <span class="optlabel">{{text}}</span>
                    </label>
                    {{/eq}}
                {{/and}}
            </div>
            {{/each}}
        </div>
        {{/eq}}
    {{/eq}}
    {{/each}}

    <div class="stepContentControls form-actions pullBottomUp">
    <beacon:authorize ifNotGranted="LOGIN_AS">
        {{#if isComplete}}
            <a class="btn btn-primary" href="layout.html?tplPath=../apps/survey/tpl/&amp;tpl=surveyPageList">
                {{cm.btn.done}}
            </a>
        {{else}}
            <button class="btn btn-primary submitBtn">
                {{cm.btn.submit}}
            </button>
            <button class="btn saveForLaterBtn">
                {{cm.btn.save}}
            </button>
            {{#if isSaved}}
            <a class="btn" href="layout.html?tplPath=../apps/survey/tpl/&amp;tpl=surveyPageList">
                {{cm.btn.done}}
            </a>
            {{/if}}
        {{/if}}
        </beacon:authorize>
    </div>
</form>
