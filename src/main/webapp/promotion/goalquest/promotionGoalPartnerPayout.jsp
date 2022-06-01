<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<c:set var="displayFlag" scope="request"
	value="${promotionGoalPartnerPayoutForm.promotionStatus == 'expired' || promotionGoalPartnerPayoutForm.promotionStatus == 'live'}" />
<c:set var="displayFlag2" scope="request"
	value="${promotionGoalPartnerPayoutForm.promotionStatus == 'expired' || promotionGoalPartnerPayoutForm.promotionStatus == 'live'}" />

<script type="text/javascript">

function callUrl( urlToCall )
{
  window.location=urlToCall;
}
</script>

<html:form styleId="contentForm" action="promotionPartnerPayoutSave">
	<html:hidden property="method" />
	<html:hidden property="promotionId" />
	<html:hidden property="promotionName" />
	<html:hidden property="promotionTypeName" />
	<html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
	<html:hidden property="promotionStatus" />
	<html:hidden property="alternateReturnUrl" />
	<html:hidden property="version" />
	<html:hidden property="awardType" styleId="awardType" />
	<%-- To Fix the bug 22055 --%>
	<html:hidden property="awardTypeName"/>
	<html:hidden property="goalLevelValueListSize" />

	<c:if test="${displayFlag == 'true'}">

	</c:if>
	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td><c:set var="promoTypeName" scope="request"
				value="${promotionGoalPartnerPayoutForm.promotionTypeName}" /> <c:set
				var="promoTypeCode" scope="request"
				value="${promotionGoalPartnerPayoutForm.promotionTypeCode}" /> <c:set
				var="promoName" scope="request"
				value="${promotionGoalPartnerPayoutForm.promotionName}" /> <tiles:insert
				attribute="promotion.header" /></td>
		</tr>
		<tr>
			<td><cms:errors /></td>
		</tr>
		<tr>
			<td width="50%" valign="top">
			<table>
				<tr>
					<td>&nbsp;</td>
					<td class="content-field-label"><cms:contentText
						code="promotion.payout" key="TYPE" /></td>
					<td class="content-field-review"><c:out
						value="${promotionGoalPartnerPayoutForm.awardTypeName}" /></td>
				</tr>

				<tr></tr>
				<c:if
					test="${promotionGoalPartnerPayoutForm.awardType == 'points'}">
					<tr class="form-row-spacer">
						<beacon:label property="partnerPayoutStructure" required="true"
							styleClass="content-field-label-top">
							<cms:contentText key="PARTNER_PAYOUT_STRUCTURE"
								code="promotion.payout.goalquest" />
						</beacon:label>
						<td class="content-field"><html:select
							property="partnerPayoutStructure" styleClass="content-field" >
							<html:options collection="partnerPayoutStructureList"
								property="code" labelProperty="name" />
						</html:select></td>
					</tr>
				</c:if>
				<tr></tr>
				<tr class="form-row-spacer">
					<beacon:label property="partnerEarnings" required="true"
						styleClass="content-field-label-top">
						<cms:contentText key="PARTNER_EARNINGS"
							code="promotion.payout.goalquest" />
					</beacon:label>
					<td class="content-field"><html:select
						property="partnerEarnings" styleClass="content-field" >
						<html:options collection="partnerEarningsList" property="code"
							labelProperty="name" />
					</html:select></td>
				</tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<tr></tr>
				<%-- start Awarding table details for partner--%>

				<table class="crud-table" width="100%">

					<tr>
						<th valign="top" class="crud-table-header-row"><cms:contentText
							code="promotion.payout.goalquest" key="GOAL_LEVEL" /></th>
						<th valign="top" class="crud-table-header-row">*<cms:contentText
							code="promotion.payout.goalquest" key="NAME" /></th>
						<th valign="top" class="crud-table-header-row">*<cms:contentText
							code="promotion.payout.goalquest" key="DESCRIPTION" /></th>
						<th valign="top" class="crud-table-header-row">*<cms:contentText
							code="promotion.payout.goalquest" key="PARTNER_AWARD" /></th>

						<c:set var="switchColor" value="false" />
						<nested:iterate id="goalLevel"
							name="promotionGoalPartnerPayoutForm"
							property="goalLevelValueList" indexId="goalLevelIndex">
							<nested:hidden property="goalLevelId" />
							<nested:hidden property="goalLevelcmAssetCode"/>
					        <nested:hidden property="nameKey"/>
					        <nested:hidden property="descriptionKey"/>
					        <nested:hidden property="name"/>
					        <nested:hidden property="description"/>
							<c:choose>
								<c:when test="${switchColor == 'false'}">
									<tr class="crud-table-row1">
										<c:set var="switchColor" scope="page" value="true" />
								</c:when>
								<c:otherwise>
									<tr class="crud-table-row2">
										<c:set var="switchColor" scope="page" value="false" />
								</c:otherwise>
							</c:choose>
							<td align="center"><c:out
								value="${goalLevel.sequenceNumber}" /></td>

							<td align="center"><nested:text property="name" size="20"
								maxlength="80" styleClass="content-field"  disabled="true"/></td>
							<td align="center"><nested:text property="description"
								size="50" maxlength="80" styleClass="content-field"  disabled="true"/></td>


							<td align="center"><nested:text
								property="partnerAwardAmount" size="10" maxlength="20"
								styleClass="content-field" /></td>
					</tr>
					</nested:iterate>

				</table>
				<%-- End Awarding details for Partner --%>
   		</table>
			</td>
		</tr>

		<tr>
			<td colspan="2" align="center"><tiles:insert
				attribute="promotion.footer" /></td>
		</tr>
	</table>
</html:form>

