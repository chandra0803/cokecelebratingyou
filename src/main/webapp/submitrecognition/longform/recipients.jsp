<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<fieldset class="formSection recipientsSection" id="recognitionFieldsetRecipients" style="display:none">
  <div class="container-splitter with-splitter-styles participantCollectionViewWrapper"
       data-msg-validation-over-budget="<cms:contentText key="OVER_BUDGET" code="recognitionSubmit.errors"/>" >

    <input type="hidden" name="paxCount" value="0" class="participantCount" />

    <h3 class="selectRecipientTitle headline_3"
        data-purl-title="<cms:contentText key="SELECTED_RECIPIENT" code="recognition.select.recipients"/>">
      <cms:contentText key="SELECTED_RECIPIENTS" code="recognition.submit"/>
    </h3>

    <div class="recipCount" style="display:none">
      <b class="num"></b>/<b class="den"></b>
      <i class="icon icon-user" alt="team members"></i>
    </div>

    <!-- Hidden unless calc needs it -->
    <%-- <a id="recogCalcPayoutTableLink" class="recogCalcPayoutTableLink" href="#" style="display:none"><cms:contentText key="VIEW_PAYOUT_GRID" code="calculator.response"/></a> --%>

    <table class="table table-condensed table-striped">
      <thead>
        <tr>
          <th class="participant"><cms:contentText key="RECIPIENT" code="purl.contribution.list"/></th>
          <th data-msg-points-range="<cms:contentText key="AWARD_RANGE" code="calculator.payouts"/>"
              data-msg-points-fixed="<cms:contentText key="AWARD" code="calculator.payouts"/>"
              data-msg-levels="<cms:contentText key="AWARD_LEVEL" code="recognition.submit"/>"
              data-msg-calculated="<cms:contentText key="AWARD" code="calculator.payouts"/>"
              class="award"><!-- dynamic --></th>
          <th class="calcDeduction"><cms:contentText key="CALCULATED_AWARD" code="recognition.select.awards"/></th>          
           <!-- Client customization for WIP #42701 starts -->
		  <th data-msg-points-range="Award" data-msg-points-fixed="Award"
              data-msg-levels="Award Level" data-msg-calculated="Award"
              class="awardCash" style="display:none"><cms:contentText key="AWARD" code="coke.cash.recognition"/><a href="#managerInfoModal" class="exampleLink exampleBanner" data-toggle="modal"><i class="icon-info"></i></a></th>
          <th class="currency" style="display:none"><cms:contentText key="CURRENCY" code="coke.cash.recognition"/></th>
          <th class="remove"><cms:contentText key="REMOVE" code="system.button"/></th>
          <!-- Client customization for WIP #42701 ends -->
        </tr>
      </thead>

      <tbody id="recipientsView"
             class="participantCollectionView"
             data-msg-empty="<cms:contentText key="NOT_ADDED" code="recognitionSubmit.errors"/>"
             data-hide-on-empty="false"
             data-msg-validation="<cms:contentText key="RECIPIENT_MANDATORY" code="recognition.submit"/>">
      </tbody>
    </table>

    <div class="budgetDeduction">
      <h3><cms:contentText key="BUDGET_DEDUCTION" code="recognitionSubmit.delivery.purl"/>
      <span class="budgetOwner" style="font-size:0.6em; margin-left: 18px"><cms:contentText key="BUDGET_OWNER" code="client.cheers.recognition"/>: [[budget owner here]]</span>
      </h3>
      <div class="progress">
        <div class="bar" style="width: 0%;"></div>
      </div>
      <div class="totals clearfix">
        <div class="budgetMin">0</div>
        <div class="budgetMax"></div>
      </div>
      <p class="discrepancyWarning" style="display:none">
    	<small><i><cms:contentText key="BUDGET_DISCREPANCY" code="recognition.submit"/></i></small>
      </p>
    </div>

    <div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
      <a href="#"><cms:contentText key="SAME_FOR_ALL" code="recognitionSubmit.delivery.purl"/></a>
    </div><!-- /#sameForAllTipTpl -->

    <div id="levelMerchModal" class="modal modal-stack hide fade" data-y-offset="adjust">
      <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button"><i class="icon-close"></i></button>
        <h3><cms:contentText key="BROWSE_PLATEAU_AWARDS" code="hometile.plateauAward"/></h3>
      </div>
      <div class="modal-body">
        <!-- dynamic -->
      </div>
    </div><!-- /.levelMerchModal -->

    <div class="maxRecipientsExceededMsg" style="display:none">
      <cms:contentText key="MAXIMUM" code="recognitionSubmit.errors"/> {{max}} <cms:contentText key="TEAM_MEMBER_LIMIT" code="recognitionSubmit.errors"/>
      <i class="icon-warning-circle"></i>
    </div>

  </div><!-- /.participantCollectionViewWrapper -->
</fieldset><!-- /#recognitionFieldsetRecipients -->
