<%@ include file="/include/taglib.jspf"%>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>


<tr>
  <td></td>
  <td class="content-field-label"><cms:contentText code="promotion.awards" key="TYPE" /></td>
  <td class="content"><c:out value="${promotionSweepstakesForm.awardTypeText}" /></td>
</tr>
<tr class="form-row-spacer">
  <beacon:label property="giversAmount" required="true" styleClass="content-field-label-top">
    <cms:contentText key="ELIGIBLE_WINNERS" code="promotion.sweepstakes" />
  </beacon:label>
  <td class="content-field"><html:text styleId="giversAmountQuiz" property="giversAmount" size="5" maxlength="10"
    styleClass="content-field" disabled="${promotionStatus=='expired'}" /></td>
  <td class="content-field"><html:select styleId="giversAmountTypeQuiz" property="giversAmountType" size="1"
    styleClass="content-field" disabled="${promotionStatus=='expired'}">
    <html:options collection="availableWinnerTypes" property="code" labelProperty="name" />
  </html:select></td>
</tr>
<tr class="form-row-spacer">
  <beacon:label property="giversAwardTypeAmount" required="true">
    <cms:contentText key="AMOUNT" code="promotion.awards" />
  </beacon:label>
  <td class="content-field"><html:text styleId="giversAwardTypeAmountQuiz" property="giversAwardTypeAmount" size="5"
    maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" /></td>
</tr>
