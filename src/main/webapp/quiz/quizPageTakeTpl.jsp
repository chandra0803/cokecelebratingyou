<h2>{{name}}</h2>

<!-- **************************************************************
    WizardTabsView
 ***************************************************************** -->
<ul class="wizardTabsView" data-content=".wizardTabsContent" style="visibility:hidden">

</ul><!-- /.wizardTabsView -->


<!-- **************************************************************
    WIZARD TABS CONTENT
 ***************************************************************** -->
<div class="wizardTabsContent">

    <!-- **************************************************************
        INTRO
     ***************************************************************** -->
    <div id="quizTakeTabIntroView" class="stepIntroContent stepContent" style="display:none">
    </div><!-- /.stepIntroContent -->

    {{#if materials}}
    <!-- **************************************************************
        MATERIALS
     ***************************************************************** -->
    <div id="quizTakeTabMaterialsView" class="stepMaterialsContent stepContent" style="display:none">
    </div><!-- /.stepMaterialsContent -->
    {{/if}}

    <!-- **************************************************************
        QUESTIONS
     ***************************************************************** -->
    <div id="quizTakeTabQuestionsView" class="stepQuestionsContent stepContent" style="display:none">
    </div><!-- /.stepQuestionsContent -->

    <!-- **************************************************************
        RESULTS
    ***************************************************************** -->
    <div id="quizTakeTabResultsView" class="stepResultsContent stepContent" style="display:none">
    </div><!-- /.stepResultsContent -->

</div><!-- /.wizardTabsContent -->

<div class="modal hide fade saveForLaterResponseModal in" id="quizPageShell">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
        <h3><cms:contentText key="SUCCESS" code="claims.quiz.submission" /></h3>
        <p><cms:contentText key="QUIZ_SAVED_FOR_LATER" code="claims.quiz.submission" /></p>
    </div>
</div>
