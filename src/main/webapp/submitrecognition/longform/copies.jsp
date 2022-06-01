<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection copiesSection" id="recognitionFieldsetCopies" style="display:none">
  <h3 class="headline_3"><cms:contentText key="SEND_COPY" code="recognition.submit"/></h3>
  <div  class="control-group copyManagerWrapper">
    <div class="controls">
      <label class="checkbox" for="sendCopyToManager">
        <!-- placeholder -->
        <input type="checkbox" name="sendCopyToManager" id="sendCopyToManager" />
			<cms:contentText key="COPY_TO_MGR" code="recognition.submit"/>
      </label>
    </div>
  </div>
  <div  class="control-group copyMeWrapper">
    <div class="controls">
      <label class="checkbox" for="sendCopyToMe">
        <input type="checkbox" name="sendCopyToMe" id="sendCopyToMe" />
			 <cms:contentText key="COPY_TO_ME" code="recognition.submit"/>
      </label>
    </div>
  </div>
  <div  class="control-group form-horizontal validateme  copyOthersWrapper"
        data-validate-fail-msgs='{"email":"<cms:contentText key="EMAIL_ERROR" code="recognitionSubmit.errors" />"}' data-validate-flags='email' >
    <label class="control-label checkbox" for="sendCopyToOthers"><cms:contentText key="COPY_TO_OTH" code="recognition.submit"/></label>
    <div class="controls">
      <input type="text" class="" name="sendCopyToOthers" id="sendCopyToOthers" maxlength="1000"/>
    </div>
  </div>
</fieldset><!-- /#recognitionFieldsetCopies -->
