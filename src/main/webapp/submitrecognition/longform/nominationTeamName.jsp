<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection teamSection" id="recognitionFieldsetTeam" style="display:none">
  <div class="control-group validateme" 
    data-validate-flags="nonempty,maxlength"
    data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SELECT_TEAM_NAME" code="recognitionSubmit.errors"/>","maxlength":"<cms:contentText key="TEAM_NAME_LIMIT" code="recognitionSubmit.errors"/>"}'
    data-validate-max-length="40">
    <label for="teamName" class="control-label"><cms:contentText key="TEAM_NAME" code="recognitionSubmit.node.details"/></label>
    <div class="controls">
      <!-- placeholder -->
      <input type="text" value="" id="teamName" name="teamName">
    </div>
  </div>
</fieldset><!-- /#recognitionFieldsetCopies -->
