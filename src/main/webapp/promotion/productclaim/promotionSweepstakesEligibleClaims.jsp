<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<tr class="form-row-spacer">
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.awards" key="TYPE" /></td>
  <td class="content-field-review"><c:out value="${promotionSweepstakesForm.awardTypeText}" /></td>
</tr>
<tr class="form-row-spacer">
  <beacon:label property="eligibleClaims" required="true" styleClass="content-field-label-top">
    <cms:contentText key="ELIGIBLE_CLAIMS" code="promotion.sweepstakes" />
  </beacon:label>
  <td class="content-field"><html:select property="eligibleClaims" styleId="eligibleClaims" size="1"
    styleClass="content-field" onchange="displayEnabledDivs()" disabled="${promotionStatus=='expired'}">
    <html:option value=''>
      <cms:contentText key="SELECT_ONE" code="system.general" />
    </html:option>
    <html:options collection="availableEligibleClaims" property="code" labelProperty="name" />
  </html:select></td>
</tr>
