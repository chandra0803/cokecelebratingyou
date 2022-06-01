<%@page import="com.biperf.core.utils.UserManager"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection deliverDateSection" id="recognitionFieldsetDeliverDate" style="display:none">
  <h3 class="headline_3"><cms:contentText key="DELIVER_ON" code="recognition.submit"/></h3>
  <div class="control-group">
  <label class="control-label"><cms:contentText key="BASED_ON_TIMEZONE" code="recognition.submit"/></label>
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
               readonly="readonly"><button class="btn datepickerBtn"><i class="icon-calendar"></i></button>
      </span>
    </div>
  </div>
</fieldset><!-- /#recognitionFieldsetDeliverDate -->
