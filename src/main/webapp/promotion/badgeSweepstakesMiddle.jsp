<%@ include file="/include/taglib.jspf"%>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<html:hidden property="active" value="true"/>
<tr class="form-row-spacer">
  <beacon:label property="eligibleWinners" required="true" styleClass="content-field-label-top">
    <cms:contentText key="ELIGIBLE_WINNERS" code="promotion.sweepstakes" />
  </beacon:label>

  <td class="content-field"><html:select property="eligibleWinners" styleId="eligibleWinners" size="1"
    styleClass="content-field" onchange="displayEnabledDivs();enableFields();" disabled="${promotionStatus=='expired'}">
    <html:option value=''>
      <cms:contentText key="SELECT_ONE" code="system.general" />
    </html:option>
    <html:options collection="availableEligibleWinners" property="code" labelProperty="name" />
  </html:select></td>
</tr>
<tr>
  <td></td>
  <td></td>
  <td>
  <DIV id="badgeReceivers">
  <table>
    <tr>
    <td class="content-bold"><cms:contentText code="promotion.sweepstakes" key="SELECT_EITHER" /></td>
      <td class="content">&nbsp;&nbsp;<cms:contentText key="WINNERS" code="promotion.sweepstakes" /></td>
      <td class="content-field">&nbsp;<html:text styleId="giversAmount" property="giversAmount"
        styleClass="content-field" size="5" maxlength="10" disabled="${promotionStatus=='expired'}" /></td>
      <td class="content-field">&nbsp;&nbsp;&nbsp; <html:select styleId="giversAmountType" property="giversAmountType"
        size="1" styleClass="content-field" disabled="${promotionStatus=='expired'}">
        <html:options collection="availableWinnerTypes" property="code" labelProperty="name" />
      </html:select></td>
    </tr>
    <tr>
      <td></td>
      <td class="content">&nbsp;&nbsp;<c:out value="${promotionSweepstakesForm.awardTypeText}" /></td>
      <td class="content-field">&nbsp;
      		<html:text styleId="giversAwardTypeAmount" property="giversAwardTypeAmount"
        		size="5" maxlength="10" styleClass="content-field" disabled="${promotionStatus=='expired'}" />
      </td>
    </tr>
  </table>
  </DIV>
</tr>
