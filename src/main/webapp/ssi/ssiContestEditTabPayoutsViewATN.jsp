<%@ include file="/include/taglib.jspf"%>
<!-- JAVA: i18n these -->
<div id="_msgPoints" style="display:none"><cms:contentText key="POINTS_UPPERCASE" code="ssi_contest.participant" /></div>
<div id="_msgUnits" style="display:none"></div>

<div id="ssiParticipants_atn">
    <!-- ParticipantPaginatedView -->
</div><!-- /#ssiParticipants_objectives -->

<hr class="section">

<div class="ssiPersonalMessage_atn">
    <h5><cms:contentText key="ADD_EMAIL_MESSGE_PAX" code="ssi_contest.creator" /> <span class="optional"><cms:contentText key="OPTIONAL" code="ssi_contest.generalInfo" /></span></h5>
    <div class="controls validateme"
        data-validate-flags="maxlength"
        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="EMAIL_MESSAGE_REQUIRED" code="ssi_contest.generalInfo" />","maxlength" : "<cms:contentText key="MAX_CHARS_ERROR" code="ssi_contest.generalInfo" />"}'
        data-validate-max-length="140">
        <textarea name="message" rows="5" data-max-chars="140" class="richtext" data-model-key="notifyText"></textarea>
    </div>
</div>
