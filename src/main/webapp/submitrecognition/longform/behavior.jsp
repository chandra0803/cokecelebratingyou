<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection behaviorSection" id="recognitionFieldsetBehavior" style="display:none">
  <h3 class="headline_3"><cms:contentText key="TELL_US_WHY" code="recognition.select.recipients"/></h3>

    <div class="control-group badgesSelector validateme"
        data-no-badge-json='{"id":null,"name":"<cms:contentText key="SELECT_BEHAVIOUR" code="recognition.submit"/>","iconClass":" ","contClass":"noBadgeContent"}'
        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SELECT_BEHAVIOR" code="recognitionSubmit.errors"/>"}'
        data-validate-flags='nonempty'>
        <div class="btn-group">
            <a class="btn btn-inverse-primary dropdown-toggle badgeBtn" data-toggle="dropdown" href="#">
                <!-- dyn -->
            </a>
            <ul class="dropdown-menu badgeItems">
                <!-- dropdown menu links -->
            </ul>
        </div>
        <%-- this input is styled to be "hidden", but can't be type="hidden" simply because the validateme function can't find it to attach the qtip --%>
        <input type="text" id="selectedBehavior" name="selectedBehavior" value="" />
    </div><!-- /.control-group.badgesSelector -->



</fieldset><!-- /#recognitionFieldsetBehavior -->
