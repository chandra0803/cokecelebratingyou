<div class="span12">
    {{#if this.firstContent}}
    <div id="col-right">
        {{#if this.firstTitle}}
        <h2 id="rulesTitle">{{this.firstTitle}}</h2>
        {{/if}}

        <div id="rulesContent">
            {{{this.firstContent}}}
        </div>

    </div><!-- /#col-right -->
    {{else}}
    <p id="rulesEmpty" class="alert alert-info"><cms:contentText key="NO_RULES_AVAILABLE" code="promotion.form.rules" /></p>
    {{/if}}
</div><!-- /.span9/12 -->
