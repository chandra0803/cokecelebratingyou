<%@ include file="/include/taglib.jspf"%>

<!-- ANNIVERSARY (CELEBRATION)-->
<fieldset class="formSection anniversaryCelebrateSection" id="recognitionFieldsetAnniversaryCelebrate" style="display:none">
    <h3 class="headline_3"><cms:contentText key="CELEBRATION_ANNIVERSARY" code="recognition.submit"/></h3>

    <div class="annivDateYears">
        <div class="annivDateYears control-group validateme"
            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ANNIVERSARY_YEARS_REQUIRED" code="recognitionSubmit.errors"/>","numeric":"<cms:contentText key="ANNIVERSARY_YEARS_NUMERIC" code="recognitionSubmit.errors"/>"}'
            data-validate-flags='nonempty,numeric'>
            <label class="control-label" for="anniversaryYears"><cms:contentText key="ANNIVERSARY_NO_OF_YEARS" code="recognition.submit"/></label>
            <div class="controls">
                <input type="text" name="anniversaryYears" id="anniversaryYears" maxlength="5" />
            </div>
        </div>
    </div>

    <div class="annivDateDays">
        <div class="annivDateDays control-group validateme"
            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ANNIVERSARY_DAYS_REQUIRED" code="recognitionSubmit.errors"/>","numeric":"<cms:contentText key="ANNIVERSARY_DAYS_NUMERIC" code="recognitionSubmit.errors"/>"}'
            data-validate-flags='nonempty,numeric'>
            <label class="control-label" for="anniversaryDays"><cms:contentText key="ANNIVERSARY_NO_OF_DAYS" code="recognition.submit"/></label>
            <div class="controls">
                <input type="text" name="anniversaryDays" id="anniversaryDays" maxlength="5" />
            </div>
        </div>
    </div>
</fieldset><!-- /#recognitionFieldsetAnniversaryCelebrate -->
