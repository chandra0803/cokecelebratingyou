<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="form-actions formSection actionsSection pullBottomUp" id="recognitionFieldsetActions" style="display:none">

<beacon:authorize ifNotGranted="LOGIN_AS">
    <button class="btn btn-primary "
            value="recognitionButtonSendPurl"
            name="button"
            id="recognitionButtonSendPurl"
            type="submit"
            style="display:none">
            <cms:contentText key="SEND" code="system.button"/>
    </button>
</beacon:authorize>

    <button class="btn btn-primary "
            value="recognitionButtonPreview"
            name="button"
            id="recognitionButtonPreview"
            type="submit"
            style="display:none">
            <cms:contentText key="PREVIEW" code="recognition.submit"/>
    </button>

    <button class="btn btn-inverse-primary purlBtn backBtn" style="display:none">
        &laquo; <cms:contentText key="BACK" code="system.button"/>
    </button>
    <button class="btn btn-primary purlBtn nextBtn" style="display:none">
        <cms:contentText key="NEXT" code="system.button"/> &raquo;
    </button>

  <button class="btn btn-inverse-primary "
          type="button"
          value="recognitionButtonCancel"
          name="button"
          id="recognitionButtonCancel"
          data-url="${pageContext.request.contextPath}/homePage.do"
          type="reset">
          <cms:contentText key="CANCEL" code="system.button"/>
  </button>

  <div class="recognitionSendCancelDialog" style="display:none">
    <p>
        <b><cms:contentText key="CANCEL_SENDING" code="recognitionSubmit.page.preview"/></b>
      </p>
      <p>
        <cms:contentText key="CHANGES_DISCARDED" code="recognitionSubmit.page.preview"/>
      </p>
    <p class="tc">
        <button type="submit" id="recognitionSendCancelDialogConfirm" class="btn btn-primary btn-small"><cms:contentText key="YES" code="system.button"/></button>
        <button type="submit" id="recognitionSendCancelDialogCancel" class="btn btn-inverse-primary btn-small"><cms:contentText key="NO" code="system.button"/></button>
    </p>
  </div>

</fieldset><!-- /recognitionFieldsetActions -->
