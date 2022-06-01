<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>

<html:hidden property="awardsType" />
<c:if test="${promotionAwardsForm.awardsType == 'points'}">
	<%-- Award Amount --%>
	<tr class="form-row-spacer">
		<beacon:label property="awardAmountTypeFixed" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="AMOUNT" code="promotion.awards" />
		</beacon:label>
		<td>
		<table>
			<tr>
				<td class="content-field" valign="top">
				<html:radio styleId="awardAmountTypeFixedTrue" property="awardAmountTypeFixed" value="true" disabled="${displayFlag}"  /> 
				<cms:contentText code="promotion.awards" key="AMOUNT_FIXED" /> &nbsp; 
				<html:text styleId="fixedAmount" property="fixedAmount" size="5" styleClass="content-field" disabled="${displayFlag}" /> 
				</td>
			</tr>
			<tr>
				<td class="content-field" valign="top">
				 <html:radio styleId="awardAmountTypeFixedFalse" property="awardAmountTypeFixed" value="false" disabled="${displayFlag}" /> 
				 <cms:contentText code="promotion.awards" key="AMOUNT_RANGE" /> 
				 <cms:contentText code="promotion.awards" key="AMOUNT_BETWEEN" /> &nbsp; 
				 <html:text styleId="rangeAmountMin" property="rangeAmountMin" size="5" styleClass="content-field" disabled="${displayFlag}" /> 
				 <cms:contentText code="promotion.awards" key="AMOUNT_AND" />&nbsp; 
				 <html:text styleId="rangeAmountMax" property="rangeAmountMax" size="5" styleClass="content-field" disabled="${displayFlag}" />
				 </td>
			</tr>			
		</table>
		</td>
	</tr>
</c:if>