<%@ include file="/include/taglib.jspf"%>

<!-- PRIVATE RECOGNITION -->
<fieldset class="formSection recognitionPrivateSection"
	id="recognitionFieldsetPrivate" style="display: none">
	<h3 class="headline_3"><cms:contentText key="PRIVATE_RECOGNITION" code="recognition.submit"/></h3>

	<div class="control-group privateRec">
		<div class="controls">
			<label class="checkbox" for="makeRecPrivate">
				<input type="checkbox" name="makeRecPrivate" id="makeRecPrivate" /><cms:contentText key="MAKE_RECOGNITION_PRIVATE" code="recognition.submit"/>
			</label>
		</div>
	</div>
</fieldset>
<!-- /#recognitionFieldsetPrivate -->
