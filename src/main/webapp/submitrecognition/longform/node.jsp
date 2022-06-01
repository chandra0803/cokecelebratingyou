<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<div class="control-group nodeWrapper validateme" style="display:none"
     data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SELECT_NODE" code="recognitionSubmit.errors"/>"}'
     data-validate-flags='nonempty' >

  <h2>
	    <cms:contentText key="SELECT_NODE" code="recognitionSubmit.node.details"/>
  </h2>

  <div class="controls nodeControls">
    <select id="nodeId" name="nodeId" data-msg-instructions="<cms:contentText key="SELECT_NODE_INFO" code="recognitionSubmit.node.details"/>" >
      <option class="defaultOption" selected="selected" value=""><cms:contentText key="CHOOSE_ONE" code="system.general"/></option>
      <!-- dynamic - on promo change  -->
    </select>
  </div><!-- /.nodeControls -->

  <div class="controls nodeChangeWrapper" style="display:none">
    <span class="recognitionNodeName"><!-- dynamic --></span>
    <button type="button" id="recognitionButtonChangeNode" class="btn btn-primary"><cms:contentText key="CHANGE" code="recognitionSubmit.node.details"/></button>
  </div><!-- /.promoChangeWrapper -->

  <div class="nodeChangeConfirmDialog" style="display:none">
    <p>
      <b><cms:contentText key="NODE_CHANGE" code="recognitionSubmit.node.details"/></b>
    </p>
    <p>
	     <cms:contentText key="WARNING" code="recognitionSubmit.node.details"/>
    </p>
    <p class="tc">
      <button id="recognitionButtonChangeNodeConfirm" class="btn btn-primary btn-small"><cms:contentText key="YES" code="system.button"/></button>
      <button id="recognitionButtonChangeNodeCancel" class="btn btn-inverse-primary btn-smal"><cms:contentText key="CANCEL" code="system.button"/></button>
    </p>
  </div><!-- /.nodeChangeConfirmDialog -->

</div><!-- /.nodeWrapper -->
