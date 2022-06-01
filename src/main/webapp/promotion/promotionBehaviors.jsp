<%@ include file="/include/taglib.jspf"%>
<html:form styleId="contentForm" action="promotionBehaviorsSave" method="POST">
	<html:hidden property="promotionName" />
	<html:hidden property="version" />
	<html:hidden property="promotionTypeName" />
	<html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
	<html:hidden property="expired" />
	<html:hidden property="live" styleId="live" />
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId"
			value="${promotionBehaviorsForm.promotionId}" />
	</beacon:client-state>

	<table class="crud-table" width="100%" cellpadding="3" cellspacing="1">
		<tr>
			<td colspan="2"><c:set var="promoTypeCode" scope="request"
				value="${promotionBehaviorsForm.promotionTypeCode}" /> <c:set
				var="promoTypeName" scope="request"
				value="${promotionBehaviorsForm.promotionTypeName}" /> <c:set
				var="promoName" scope="request"
				value="${promotionBehaviorsForm.promotionName}" /> <tiles:insert
				attribute="promotion.header" />
			</td>
		</tr>
		<tr>
			<td><cms:errors /></td>
		</tr>
		<tiles:insert attribute="promotionBehaviorsMiddle" />
	</table>
	<tiles:insert attribute="promotion.footer" />
</html:form>
