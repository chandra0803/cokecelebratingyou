<%@page import="com.biperf.core.utils.UserManager"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection deliverDatePurlSection form-horizontal" id="recognitionFieldsetDeliverDatePurl" style="display:none">
  <h3 class="headline_3"><cms:contentText key="DATE_TO_SEND" code="recognitionSubmit.delivery.purl"/></h3>
  <div>
    <p><cms:contentText key="SET_DATE" code="recognitionSubmit.delivery.purl"/></p>
  </div>

  <div class="control-group">
    <label class="control-label"><cms:contentText key="CONTRIBUTION_OPEN" code="recognition.purl.submit"/></label>
    <div class="controls">
      <strong id="contributionsOpen" class="contributionsDate"><cms:contentText key="TODAY" code="recognitionSubmit.delivery.purl"/></strong>
    </div>
  </div>

  <div class="control-group">
    <label class="control-label"><cms:contentText key="CONTRIBUTION_CLOSE" code="recognition.purl.submit"/></label>
    <div class="controls">
      <strong id="contributionsClose" class="contributionsDate"><!-- dynamic --></strong>
    </div>
  </div>

  <div class="control-group validateme"
       data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SELECT_DATE" code="recognitionSubmit.delivery.purl"/>"}'
       data-validate-flags='nonempty' >
    <label for="recipientSendDatePurl" class="control-label"><cms:contentText key="SEND_PURL" code="recognitionSubmit.delivery.purl"/></label>
    <div class="controls">
      <!--
        NOTE: JSP set data-date-format AND data-date-startdate
      -->
      <span class="input-append datepickerTrigger"
        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
        data-date-language="<%=UserManager.getUserLocale()%>"
        data-date-startdate="${sendRecognitionForm.calendarStartDate}"
        data-date-autoclose="true">
        <input type="text" class="date datepickerInp"
          name="recipientSendDate"
          id="recipientSendDatePurl"
          readonly="readonly"><button class="btn datepickerBtn"><i class="icon-calendar"></i></button>
      </span>
    </div>
  </div>
</fieldset><!-- /#recognitionFieldsetDeliverDatePurl -->
