<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<h2 class="headline_2"><cms:contentText key="PROMOTION" code="recognitionSubmit.node.details"/></h2>

<div class="control-group validateme"
     data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SELECT_PROMO" code="recognitionSubmit.errors"/>"}'
     data-validate-flags='nonempty' >
  <label class="control-label" for="promotionId">
    <!-- Select Promotion - big, set above in H2 -->
  </label>

  <div class="controls promoWrapper">
    <select id="promotionId" name="promotionId" data-msg-instructions="<cms:contentText key="SELECT_PROMO_INFO" code="recognitionSubmit.node.details"/>">
      <option class="defaultOption" selected="selected" value=""><cms:contentText key="SELECT_PROMO" code="system.general"/></option>
      <!-- dynamic -  once on page load  -->
    </select>
    <a href="#" class="doViewRules btn" style="display:none"><cms:contentText key="VIEW_RULES" code="recognition.submit"/></a>
  </div><!-- /.promoWrapper -->

  <div class="controls promoChangeWrapper" style="display:none">
    <span class="recognitionPromotionName"><!-- dynamic --></span>
    <button type="button" id="recognitionButtonChangePromo" class="btn btn-primary btn-small"><cms:contentText key="CHANGE" code="recognitionSubmit.node.details"/></button>
    <a href="#" class="doViewRules btn tbn-inverse-primary btn-small" style="display:none"><cms:contentText key="VIEW_RULES" code="recognition.submit"/></a>
  </div><!-- /.promoChangeWrapper -->

  <div id="rulesModal" class="modal hide fade">
    <div class="modal-header">
      <button data-dismiss="modal" class="close" type="button">
        <i class="icon-close"></i>
      </button>
      <h3><cms:contentText key="RULES" code="system.general"/></h3>
    </div>
    <div class="modal-body">
      <!-- dynamic -->
    </div>
  </div><!-- /#rulesModal -->

  <div class="promoChangeConfirmDialog" style="display:none">
    <p>
      <b><cms:contentText key="PROMOTION_CHANGE" code="recognitionSubmit.node.details"/></b>
    </p>
    <p>
	     <cms:contentText key="SELECTION_WARNING" code="recognitionSubmit.node.details"/>
    </p>
    <p class="tc">
      <button id="recognitionButtonChangePromoConfirm" class="btn btn-primary btn-small"><cms:contentText key="YES" code="system.button"/></button>
      <button id="recognitionButtonChangePromoCancel" class="btn btn-onverse-primary btn-small"><cms:contentText key="CANCEL" code="system.button"/></button>
    </p>
  </div><!-- /.promoChangeConfirmDialog -->

</div>
