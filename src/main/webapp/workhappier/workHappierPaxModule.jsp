<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== WORKHAPPIER MODULE ======== -->
<script type="text/template" id="workHappierPaxModuleTpl">

<h3 class="section-header">
    <cms:contentText key="WORK_HAPPIER" code="work.happier" />
</h3>

<div class="module-liner">

    <div class="wide-view">
        <h4 class="work-happier-title"><cms:contentText key="HOW_HAPPY_AT_WORK" code="work.happier" /></h4>

        <img src="${pageContext.request.contextPath}/assets/img/workHappier/smilyFace.png" alt="smily face" class="static-smily-face">
        <div class="happy-face-shadow"></div>
    </div>

    <div class="modal modal-stack hide fade" id="whModal" data-backdrop="static" data-y-offset="adjust">
        <div class="happiness--container">
            <div id="happiness--beta_tag">
                <div class="content">
                </div>
            </div>

            <div class="happiness--wrapper">
                <a href="#" class="close whModalClose"><i class="icon-close"></i></a>
                <h1 id="happiness--module_header" class="break"><cms:contentText key="HOW_HAPPY_AT_WORK" code="work.happier" /></h1>

                <div id="happiness--slider_container">
                    <h2 id="happiness--result_header"><span id="happiness--result_header_feeling"></span></h2>
                    <h3 id="happiness--result_subheader"><span><cms:contentText key="PERSONALIZED_ADVICE" code="work.happier" /></span></h3>


                    <div id="happiness--analyze_smile_cta" class="happiness--button_container">
                     <beacon:authorize ifNotGranted="LOGIN_AS">
                        <a href="#" class="btn btn-primary"><cms:contentText key="ANALYZE_MY_SMILE" code="work.happier" /></a>
                     </beacon:authorize>
                    </div>


                    <div id="happiness--work_happier_cta" class="happiness--button_container">
                        <a href="${pageContext.request.contextPath}/takeWHSurvey.do" target="_blank" class="btn btn-primary"><cms:contentText key="GET_FULL_PROFILE" code="work.happier" /></a>
                    </div>

                   <%-- <div id="happiness--confidential_feedback">
                        <p><cms:contentText key="TELL_US" code="work.happier" /> <a href="#" id="whFeedbackModalTrigger"><cms:contentText key="FEEDBACK_FORM" code="work.happier" /></a></p>
                    </div> --%>

                    <div id="happiness--privacy_statement">
                        <p><cms:contentText key="CONFIDENTIAL" code="work.happier" /> <a href="${pageContext.request.contextPath}/privacy.do?method=view" id="whPrivacyModalTrigger" data-page-id="privacyPageView"><cms:contentText key="PRIVACY_POLICY" code="work.happier" /></a>.</p>
                    </div>

                </div>

                <div class="happiness--past_results"></div>
            </div>
        </div>
    </div>

    <div class="modal hide fade" id="whFeedbackModal" tabindex="-1" role="dialog" aria-labelledby="whFeedbackModalLabel">
        <div class="modal-header">
            <button type="button" class="close whFeedbackClose" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
            <h3 id="myModalLabel"><cms:contentText key="CONFIDENTIAL_FEEDBACK" code="work.happier" /></h3>
        </div>
        <div class="modal-body">
            <p><cms:contentText key="PROVIDE_FEEDBACK" code="work.happier" /></p>

            <form action="${pageContext.request.contextPath}/workHappierFeedback.do" method="post">
                <div class="control-group validateme"
                    data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_MESSAGE" code="work.happier" />"}'
                    data-validate-flags='nonempty'>
                    <textarea name="message" cols="50" rows="5"></textarea>
                </div>
            </form>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" type="submit" id="whFeedbackSubmit"><cms:contentText key="SUBMIT" code="system.button" /></button>
            <button class="btn whFeedbackClose" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button" /></button>
        </div>
    </div><!--/#whFeedbackModal-->
</div>

<!--subTpl.whPastResultsTpl=
<h3><cms:contentText key="FEELING_LATELY" code="work.happier" /></h3>

{{#if this}}
<ul class="past_results_list">
{{#each this}}
    <li>
        <img src="{{imgUrl}}" alt="{{mood}} face" class="happiness--past_results_smily" />
        <div>
            <span class="happiness--past_results_date">{{date}}</span>
            <span class="happiness--past_results_mood">{{mood}}</span>
        </div>
    </li>
{{/each}}
</ul>
{{else}}
<div class="past_results_list">
    <span class="no_results"><cms:contentText key="NO_PAST_RESPONSES" code="work.happier" /></span>
</div>
{{/if}}
subTpl-->
</script>
<!-- Instantiate new BIWHappinessSlider with options -->
<script>

$( document ).ready( function() {
    window.happinessSliderOpts =
        {
            resultZonesData: [
                {
                    headline: 'Take a deep breath',
                    feeling: 'You might be thinking: Yikes',
                    min: 0,
                    thoughts: [
                        'This is not what the brochure advertised',
                        'Houston we have a problem',
                        'Wake me when it\'s over'
                    ]
                },
                {
                    headline: 'Find a way forward',
                    feeling: 'You might be thinking: Could be better',
                    min: 37,
                    thoughts: [
                      'Meh',
                      'I grin and bear it. Well, I bear it',
                      'It\'s got to be 5 o\'clock somewhere'
                    ]
                },
                {
                    headline: 'Build on what\'s working',
                    feeling: 'You\'re probably feeling: Pretty Good',
                    min: 59,
                    thoughts: [
                        'Okay...not bad',
                        'I\'m in like with this job',
                        'It\'s a good start'
                    ]
                },
                {
                    headline: 'You\'ve figured out the big stuff',
                    feeling: 'You\'re probably feeling: Awesome',
                    min: 80,
                    thoughts: [
                      'We have lift off',
                      'Is it work if it makes me this happy?',
                      'I\'ve got that Happy song stuck in my head'
                    ]
                }
            ]
        };
} );
</script>

