<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.BudgetFinalPayoutRule"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>
<%@ page
	import="com.biperf.core.ui.promotion.PromoMerchProgramLevelFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<SCRIPT TYPE="text/javascript">
	function disableCalculator(disable)
	{
		if(disable == 'true')
		{
		   if( document.getElementById('calculatorId') != null )
			{
		   document.getElementById('calculatorId').value='';
		   document.getElementById('calculatorId').selected="\""+'<cms:contentText key="CHOOSE_ONE" code="system.general" />'+"\"";
			 document.getElementById('calculatorId').disabled='true';
			}
			if( document.getElementById('scoreBy') != null )
			{
			 document.getElementById('scoreBy').value='';
			  document.getElementById('scoreBy').selected='\"'+'<cms:contentText key="CHOOSE_ONE" code="system.general" />'+'\"';
			 document.getElementById('scoreBy').disabled='true';
			}
			if( document.getElementById('rangeAmountMin') != null )
			{
			 document.getElementById('rangeAmountMin').value='';
			}
			if( document.getElementById('rangeAmountMax') != null )
			{
			 document.getElementById('rangeAmountMax').value='';
			}
			if($("input:radio[name='awardAmountTypeFixed']:checked").val()== "false" )
				{
					$("input[id='fixedAmount']").val("");
					$("input[id='fixedAmount']").attr('disabled','disabled');
					$("input[id='rangeAmountMin']").attr('disabled','');
					$("input[id='rangeAmountMax']").attr('disabled','');
				}else{
					$("input[id='fixedAmount']").attr('disabled','');
					$("input[id='rangeAmountMin']").attr('disabled','disabled');
					$("input[id='rangeAmountMax']").attr('disabled','disabled');
				}
		}else if(disable == 'false')
		{
		
			if( document.getElementById('calculatorId') != null )
			{
			 document.getElementById('calculatorId').disabled=false;
			}
			if( document.getElementById('scoreBy') != null )
			{
			 document.getElementById('scoreBy').disabled=false;
			}
			if($("input:radio[name='awardAmountTypeFixed']:checked").val()== "cal" )
			{
				$("input[id='fixedAmount']").val("");
				$("input[id='rangeAmountMin']").val("");
				$("input[id='rangeAmountMax']").val("");
				$("input[id='fixedAmount']").attr('disabled','disabled');
				$("input[id='rangeAmountMin']").attr('disabled','disabled');
				$("input[id='rangeAmountMax']").attr('disabled','disabled');
				
			}else{
				$("input[id='fixedAmount']").attr('disabled','');
				$("input[id='rangeAmountMin']").attr('disabled','');
				$("input[id='rangeAmountMax']").attr('disabled','');
			}
		}
	}
	
	function addAnotherSegment(method)
	{
	  document.promotionAwardsForm.method.value=method;
	  document.promotionAwardsForm.action = "promotionAwards.do";
	  document.promotionAwardsForm.submit();
	  return false;
	}	
</script>
<html:hidden property="awardsType" />
<c:if test="${promotionAwardsForm.awardsType == 'merchandise'}">
	<%
	Map paramMap = new HashMap();
	%>
	<html:hidden property="countryListCount" />
	<html:hidden property="numOfLevelsEqual" />
	<html:hidden property="numberOfLevels" />
	<html:hidden property="awardStructure" value="level" />
	<%-- Award Structure --%>
	<c:if test="${promotionAwardsForm.numOfLevelsEqual}">
		<%-- Calculator --%><c:if test="${!isPurlIncluded}">
		<tr class="form-blank-row" id="merchCalculatorSpacerLyr">
			<td colspan="3">&nbsp;</td>
		</tr>
		<tr class="form-row-spacer" id="merchCalculatorLyr">
			<beacon:label property="merchCalculator" required="true"
				styleClass="content-field-label-top">
				<cms:contentText key="AWARD_CALCULATOR" code="promotion.awards" />
			</beacon:label>
			<td class="content-field" valign="top"><html:radio
				styleId="useRecognitionCalculatorFalse"
				property="useRecognitionCalculator" value="false"
				disabled="${displayFlag}"
				onclick="javascript: useRecognitionCalculatorEvaluate();" /> <cms:contentText
				code="system.common.labels" key="NO" /><br>
			<html:radio styleId="useRecognitionCalculatorTrue"
				property="useRecognitionCalculator" value="true"
				disabled="${displayFlag}"
				onclick="javascript: useRecognitionCalculatorEvaluate();" /> <cms:contentText
				code="system.common.labels" key="YES" /> &nbsp;&nbsp;&nbsp; <cms:contentText
				code="promotion.awards" key="CALCULATOR_TYPE" /> &nbsp; <html:select
				styleId="calculatorId" property="calculatorId"
				styleClass="content-field" disabled="${displayFlag}">
				<html:option value=''>
					<cms:contentText key="CHOOSE_ONE" code="system.general" />
				</html:option>
				<html:options collection="calculatorTypeList" property="id"
					labelProperty="name" />
			</html:select>&nbsp;&nbsp; <cms:contentText code="promotion.awards" key="SCORE_BY" />
			&nbsp; <html:select styleId="scoreBy" property="scoreBy"
				styleClass="content-field" disabled="${displayFlag}">
				<html:option value=''>
					<cms:contentText key="CHOOSE_ONE" code="system.general" />
				</html:option>
				<html:options collection="scoreTypeList" property="code"
					labelProperty="name" />
			</html:select></td>
		</tr></c:if>
		<%-- Calculator --%>
	</c:if>
	<%-- Allow Points Conversion --%>
	<tr class="form-blank-row" id="apqConversionSpacerLyr">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr id="apqConversionLyr" class="form-row-spacer">
		<beacon:label property="apqConversion" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="ALLOW_APQ_CONVERSION" code="promotion.awards" />
		</beacon:label>
		<td class="content-field" align="left" width="75%">
		<table>
			<tr>
				<td class="content-field" valign="top"><html:radio
					styleId="apqConversionFalse" property="apqConversion" value="false"
					disabled="${displayFlag}" /> <cms:contentText
					code="system.common.labels" key="NO" /></td>
			</tr>
			<tr>
				<td class="content-field" valign="top"><html:radio
					styleId="apqConversionTrue" property="apqConversion" value="true"
					disabled="${displayFlag}" /> <cms:contentText
					code="system.common.labels" key="YES" /></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr class="form-blank-row" id="notificationSpacerLyr">
		<td colspan="3">&nbsp;</td>
	</tr>
	<%-- Recipient Notification --%>
	<tr id="notificationLyr" class="form-row-spacer">
		<beacon:label property="notificationType" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="RECIPIENT_NOTIFICATION" code="promotion.awards" />
		</beacon:label>
		<td class="content-field" align="left" width="75%">
		<table>
			<tr>
				<td class="content-field" valign="top"><html:radio styleId="noNotificationFalse"
					property="noNotification" value="false" disabled="${displayFlag}" />
				<cms:contentText code="promotion.awards" key="STANDARD_NOTIFICATION" />
				</td>
			</tr>
			<tr>
				<td class="content-field" valign="top"><html:radio styleId="noNotificationTrue"
					property="noNotification" value="true" disabled="${displayFlag}" />
				<cms:contentText code="promotion.awards" key="NO_NOTIFICATION" /></td>
			</tr>
		</table>
		</td>
	</tr>

	<%-- Allow What's New --%>
	<tr class="form-blank-row" id="whatsNewLyrSpacerLyr">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr class="form-row-spacer">
	
		<beacon:label property="featuredAwardsEnabled" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="WHATS_NEW" code="promotion.awards" />
		</beacon:label>
		<td class="content-field" align="left" width="75%">
		
		<div>
		
			<html:select styleId="stdProductId" property="stdProductId"
				styleClass="content-field">
				<html:option value="-1">
					<cms:contentText key="NONE" code="promotion.awards" />
				</html:option>
				<html:optionsCollection value="productId" property="stdHomePageItemAsList" label="description" />
			</html:select>
			<c:forEach items="${promotionAwardsForm.stdHomePageItemAsList}" var="stdHomePageItemList" varStatus="homePageItemStatus">
			  <html:hidden name="stdHomePageItemList" property="productId" indexed="true" />
			  <html:hidden name="stdHomePageItemList" property="catalogId" indexed="true" />
			  <html:hidden name="stdHomePageItemList" property="productSetId" indexed="true" />
			  <html:hidden name="stdHomePageItemList" property="copy" indexed="true" />
			  <html:hidden name="stdHomePageItemList" property="description" indexed="true" />
			  <html:hidden name="stdHomePageItemList" property="tmbImageUrl" indexed="true" />
			  <html:hidden name="stdHomePageItemList" property="detailImgUrl" indexed="true" />
			</c:forEach>
			
		</div>	
		
		</td>
	</tr>
	 
<%-- Allow Feature Awards --%>
<%-- 	<tr class="form-blank-row" id="featuredAwardsSpacerLyr">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr id="featuredAwardsLyr" class="form-row-spacer">
		<beacon:label property="featuredAwardsEnabled" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="ENABLE_FEATURED_ITEMS" code="promotion.awards" />?
		</beacon:label>
		<td class="content-field" align="left" width="75%">
		<table>
			<tr>
				<td class="content-field" valign="top"><html:radio
					property="featuredAwardsEnabled" styleId="featuredAwardsFalse" value="false" disabled="${displayFlag}" />
				<cms:contentText code="system.common.labels" key="NO" />
				</td>
			</tr>
			<tr>
				<td class="content-field" valign="top"><html:radio
					property="featuredAwardsEnabled" styleId="featuredAwardsTrue" value="true" disabled="${displayFlag}" />
				<cms:contentText code="system.common.labels" key="YES" /></td>
			</tr>
		</table>
		</td>
	</tr> --%>
	
	<tr class="form-blank-row">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr class="form-row-spacer" id="levelnames">
		<beacon:label property="levelNames" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="AWARD_LEVEL_NAMES" code="promotion.awards" />
		</beacon:label>
		<td class="content-field" valign="top"><c:if
			test="${promotionAwardsForm.countryList != null }">

			<c:if
				test="${promotionAwardsForm.numOfLevelsEqual and promotionAwardsForm.countryListCount>1}">
				<nobr><a href="javascript:setSameLevelNames()"> <cms:contentText
					code="promotion.awards" key="USE_SAME_LEVEL_NAMES" /> </a> </nobr>
				<br>
			</c:if>

			<table>
				<c:set var="countryIndex" value="${1}" />

				<c:forEach items="${promotionAwardsForm.countryList}"
					var="countryList">

					<c:if test="${countryList.newCountry }">
						<tr class="form-blank-row">
							<td colspan="3">&nbsp;</td>
						</tr>
						<c:set var="countryIndex" value="${1}" />
						<tr>
							<td class="content-bold" valign="top" align="left"><cms:contentText
								code="${countryList.countryAssetKey}" key="COUNTRY_NAME" /></td>
							<td class="content-bold" valign="top" align="left" nowrap
								colspan="2"><c:if test="${countryList.programId != null }">
								<cms:contentText key="PROGRAM_NUMBER" code="promotion.awards" />&nbsp;&nbsp;&nbsp;&nbsp;
          <c:out value="${countryList.programId}" />&nbsp;&nbsp;&nbsp;&nbsp;
          <%
                    PromoMerchProgramLevelFormBean countryInfo = (PromoMerchProgramLevelFormBean)pageContext
                    .getAttribute( "countryList" );
                if ( countryInfo != null )
                {
                  paramMap.put( "countryId", countryInfo.getCountryId() );
                  paramMap.put( "programId", countryInfo.getProgramId() );
                }
                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
						"/promotionRecognition/displayMerchLevelsDetail.do", paramMap, true ) );
          %>
								<a
									href="javascript:openModalPopup('<c:out value="${linkUrl}"/>',POPUP_SIZE_LARGE,true)">
								<cms:contentText key="VIEW_AWARD_FOR_COUNTRY"
									code="promotion.awards" /> </a>
							</c:if></td>
						</tr>
						<tr class="form-blank-row">
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td class="content-bold" valign="top" align="left"><cms:contentText code="promotion.awards" key="LEVEL_PREFIX" /></td>
							<td class="content-bold" valign="top" align="left"><cms:contentText code="promotion.awards" key="MAX_VALUE" /></td>
							<td class="content-bold" valign="top" align="left"><cms:contentText code="promotion.awards" key="LEVEL_LABEL" /></td>
						</tr>
					</c:if>

					<tr>
						<td class="content-field" valign="top" align="left"><cms:contentText
							code="promotion.awards" key="LEVEL_PREFIX" /> <c:out
							value="${countryIndex}" /> <c:set var="countryIndex"
							value="${countryIndex+1}" /></td>
						<td class="content-field" valign="top" align="left"><c:out
							value="${countryList.maxValue}" /></td>
						<td class="content-field" valign="top" align="left"><html:text
							name="countryList" property="levelName" indexed="true"
							maxlength="50" size="20" /> <html:hidden name="countryList"
							property="maxValue" indexed="true" /> <html:hidden
							name="countryList" property="programId" indexed="true" /> <html:hidden
							name="countryList" property="newCountry" indexed="true" /> <html:hidden
							name="countryList" property="countryId" indexed="true" /> <html:hidden
							name="countryList" property="countryAssetKey" indexed="true" /> <html:hidden
							name="countryList" property="ordinalPosition" indexed="true" /> <html:hidden
							name="countryList" property="promoMerchLevelId" indexed="true" />
						<html:hidden name="countryList" property="omLevelName"
							indexed="true" /> <html:hidden name="countryList"
							property="promoMerchCountryId" indexed="true" /></td>
					</tr>

				</c:forEach>
			</table>
		</c:if></td>
	</tr>
</c:if>

<html:hidden property="approvalType" value="${promotionAwardsForm.approvalType}" />
<c:if test="${promotionAwardsForm.awardsType != 'merchandise'}">
	<%-- Award Amount --%>
	<tr class="form-row-spacer">
		<beacon:label property="awardAmountTypeFixed" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="AMOUNT" code="promotion.awards" />
		</beacon:label>
		<td>
		<table>
			<tr>
				<td class="content-field" valign="top"><html:radio
					styleId="awardAmountTypeFixedTrue" property="awardAmountTypeFixed"
					value="true" disabled="${displayFlag}"  onclick="disableCalculator('true');"/> <cms:contentText
					code="promotion.awards" key="AMOUNT_FIXED" /> &nbsp; <html:text
					styleId="fixedAmount" property="fixedAmount" size="5"
					styleClass="content-field" disabled="${displayFlag}" />
				</td>
			</tr>
			<tr>
				<td class="content-field" valign="top"><html:radio
					styleId="awardAmountTypeFixedFalse" property="awardAmountTypeFixed"
					value="false" disabled="${displayFlag}"  onclick="disableCalculator('true');"/> <cms:contentText
					code="promotion.awards" key="AMOUNT_RANGE" /> <cms:contentText
					code="promotion.awards" key="AMOUNT_BETWEEN" /> &nbsp; <html:text
					styleId="rangeAmountMin" property="rangeAmountMin" size="5"
					styleClass="content-field" disabled="${displayFlag}" /> <cms:contentText
					code="promotion.awards" key="AMOUNT_AND" />&nbsp; <html:text
					styleId="rangeAmountMax" property="rangeAmountMax" size="5"
					styleClass="content-field" disabled="${displayFlag}" /></td>
			</tr>
			<%-- Calculator --%>
			<c:if test="${!isPurlIncluded && !isMobAppEnabled}">
			<tr>
				<td class="content-field" valign="top"><c:choose>
					<c:when test="${! empty promotionAwardsForm.calculatorId }">
						<html:radio styleId="calculatorAwardAmountTypeFixedFalse"
							property="awardAmountTypeFixed" value="cal"
							disabled="${displayFlag}"   onclick="disableCalculator('false');"/>
					</c:when>
					<c:otherwise>
						<html:radio styleId="calculatorAwardAmountTypeFixedCal"
							property="awardAmountTypeFixed" value="cal"
							disabled="${displayFlag}"   onclick="disableCalculator('false');"/>
					</c:otherwise>
				</c:choose> <cms:contentText code="promotion.awards" key="AWARD_CALCULATOR" />
				<%-- Commenting out to fix in a later release
				&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H4', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">
				<BR>
				--%>				
				<%-- REMOVE when above is uncommented
				&nbsp; &nbsp;<BR>
				--%>				
				<BR>
				<cms:contentText code="promotion.awards" key="CALCULATOR_TYPE" />
				&nbsp; <html:select styleId="calculatorId" property="calculatorId"
					styleClass="content-field" disabled="${displayFlag}">
					<html:option value=''>
						<cms:contentText key="CHOOSE_ONE" code="system.general" />
					</html:option>
					<html:options collection="calculatorTypeList" property="id"
						labelProperty="name" />
				</html:select><br>
				<br>
				<cms:contentText code="promotion.awards" key="SCORE_BY" /> &nbsp; <html:select
					styleId="scoreBy" property="scoreBy" styleClass="content-field"
					disabled="${displayFlag}">
					<html:option value=''>
						<cms:contentText key="CHOOSE_ONE" code="system.general" />
					</html:option>
					<html:options collection="scoreTypeList" property="code"
						labelProperty="name" />
				</html:select></td>
			</tr>
			</c:if>
			<%-- Calculator --%>

		</table>
		</td>
	</tr>
</c:if>

<c:if test="${promotionAwardsForm.awardsType == 'points' and promotionAwardsForm.promotionIssuanceTypeCode == 'online'}">
<html:hidden property="promotionStartDate" />
<html:hidden property="promotionEndDate" />
    <tr class="form-row-spacer">
      <beacon:label property="budgetSweepEnabled" required="true" styleClass="content-field-label-top">
        <cms:contentText key="SWEEP_BUDGET_PROCESS" code="promotion.awards" />
      </beacon:label>
      <td class="content-field" align="left" width="75%">
        <table>
          <tr>
            <td class="content-field" valign="top">
                <html:radio styleId="budgetSweepEnabledFalse"  property="budgetSweepEnabled" value="false" onclick="updateLayersShown();" disabled="${displayFlag}" />
                <cms:contentText  code="system.common.labels" key="NO" />
            </td>
          </tr>
          <tr>
            <td class="content-field" valign="top">
			    <html:radio styleId="budgetSweepEnabledTrue" property="budgetSweepEnabled" value="true" onclick="updateLayersShown();" disabled="${displayFlag}" />
                <cms:contentText  code="system.common.labels" key="YES" />
            </td>
          </tr>
        </table>
      </td>
    </tr>
</c:if>

<c:if test="${promotionAwardsForm.promotionIssuanceTypeCode == 'online'}">
<tr class="form-row-spacer" id="budgetInfo">
	<beacon:label property="budgetOption" required="true"
		styleClass="content-field-label-top">
		<cms:contentText key="HAS_BUDGET" code="promotion.awards" />
	</beacon:label>
	<td><c:if test="${promotionAwardsForm.live}">
		
		<beacon:client-state>
			<beacon:client-state-entry name="budgetMasterId"
				value="${promotionAwardsForm.budgetMasterId}" />
		</beacon:client-state>
	</c:if>
	<table>
		<%--  radio buttons table --%>
		<html:hidden property="hiddenBudgetMasterId"/>
		<html:hidden property="budgetSegmentVBListSize"/>
		<tr>
			<td class="content-field" nowrap valign="top"><html:radio
				styleId="budgetOptionNone" property="budgetOption" value="none"
				disabled="${displayFlag}"
				onclick="updateLayersShown();" />
			<cms:contentText code="promotion.awards" key="BUDGET_NO" /></td>
		</tr>

		<tr>
			<td class="content-field" nowrap valign="top" id="budgetMasterRowId" >
			<c:choose>
				<c:when test="${promotionAwardsForm.awardsType != 'merchandise' }">
					<html:radio
					styleId="budgetOptionExists" property="budgetOption"
					value="existing" disabled="${displayFlag}"
					onclick="updateLayersShown();" />
					<cms:contentText code="promotion.awards" key="BUDGET_EXISTING" />&nbsp; 
					<input type="hidden" id="existingBudgetType" name="existingBudgetType"/>
					<select id="budgetMasterId" onchange="updateLayersShown();"
						name="budgetMasterId" class="content-field"
						disabled="${displayFlag}">
						<option value=''>
							<cms:contentText key="CHOOSE_ONE" code="system.general" />
						</option>
						<c:forEach var="bmItem" items="${budgetMasterList}">						
							<option value="${bmItem.budgetMasterId}" style="${bmItem.budgetType}" bud-type="${bmItem.budgetType}" ${bmItem.budgetMasterId == promotionAwardsForm.budgetMasterId ? 'selected' : ''}><c:out value="${bmItem.budgetMasterName}"/></option>
						</c:forEach>
					</select>
				
				</c:when>
				<c:otherwise>
					<html:radio
					styleId="budgetOptionExists" property="budgetOption"
					value="existing" disabled="${displayFlag}" onclick="updateLayersShown();"/>
					<cms:contentText code="promotion.awards" key="BUDGET_EXISTING" />&nbsp;
					<c:if test="${promotionAwardsForm.budgetOption == 'existing' }">
					<html:hidden property="budgetOption" value="${promotionAwardsForm.budgetOption}"/>
					<html:hidden property="budgetMasterId" value="${promotionAwardsForm.budgetMasterId}"/>
					</c:if>
							
					<select id="budgetMasterId" onchange="updateLayersShown();" disabled="${displayFlag}"
					name="budgetMasterId" class="content-field">
						<option value=''>
							<cms:contentText key="CHOOSE_ONE" code="system.general" />
						</option>						
						<c:forEach var="bmItem" items="${budgetMasterList}">						
							<option value="${bmItem.budgetMasterId}" style="${bmItem.budgetType}" bud-type="${bmItem.budgetType}" ${bmItem.budgetMasterId == promotionAwardsForm.budgetMasterId ? 'selected' : ''}><c:out value="${bmItem.budgetMasterName}"/></option>
						</c:forEach>
					</select>
				</c:otherwise>
			</c:choose>
			</td>
			</tr>
			
			<tr>
			<td class="content-field" nowrap valign="top" id="budgetMasterSweepRowId">
				<c:if test="${promotionAwardsForm.awardsType != 'merchandise'}">
					<html:radio
					styleId="budgetOptionSweepExists" property="budgetOption"
					value="sweepexisting" disabled="${displayFlag}"
					onclick="updateLayersShown();" />
					<cms:contentText code="promotion.awards" key="BUDGET_EXISTING" />&nbsp; 
					<input type="hidden" id="existingBudgetType" name="existingBudgetType"/>	
					<select id="budgetSweepMasterId" onchange="updateLayersShown();"
					name="budgetMasterId" class="content-field"
					disabled="true">
						<option value=''>
							<cms:contentText key="CHOOSE_ONE" code="system.general" />
						</option>						
						<c:forEach var="bmItem1" items="${budgetMasterWithSweepBudgetList}">						
							<option value="${bmItem1.budgetMasterId}" style="${bmItem1.budgetType}" bud-type="${bmItem1.budgetType}" ${bmItem1.budgetMasterId == promotionAwardsForm.budgetMasterId ? 'selected' : ''}><c:out value="${bmItem1.budgetMasterName}"/></option>
						</c:forEach>
					</select>	
				</c:if>
				</td>
		</tr>

		<tr>
			<td class="content-field" nowrap valign="top"><html:radio styleId="budgetOptionNew" property="budgetOption"
						value="new" disabled="${displayFlag}"
						onclick="updateLayersShown();" />
			 <cms:contentText code="promotion.awards" key="BUDGET_CREATE" />
			</td>
		</tr>

		<%--  new budget row  --%>
		<tr>
			<td>

			<table id="newBudget">
				<%--  new budget fields --%>
				<tr>
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
					<table>

						<tr class="form-row-spacer">
							<beacon:label property="budgetMasterName" required="true">
								<cms:contentText key="BUDGET_MASTER_NAME"
									code="promotion.awards" />
							</beacon:label>
							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><html:text
										styleId="budgetMasterName" property="budgetMasterName" maxlength="30"
										size="20" styleClass="content-field" disabled="${displayFlag}" /></td>
								</tr>
							</table>
							</td>
						</tr>

						<%-- Budget Type --%>
						<tr class="form-row-spacer"  id="budgetType">
							<beacon:label property="budgetType" required="true"
								styleClass="content-field-label-top">
								<cms:contentText key="BUDGET_TYPE" code="promotion.awards" />
							</beacon:label>
							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top">
										<html:radio styleId="budgetTypePax" property="budgetType"
											value="pax" disabled="${displayFlag}"
											onclick="updateLayersShown();" />
										<cms:contentText code="promotion.awards" key="BUDGET_TYPE_PAX" />
									</td>
								</tr>

								<tr>
									<td class="content-field" valign="top">
										<html:radio styleId="budgetTypeNode" property="budgetType"
													value="node" disabled="${displayFlag}"
													onclick="updateLayersShown();" />
									 	<cms:contentText code="promotion.awards" key="BUDGET_TYPE_NODE" />
									</td>
								</tr>
								<tr>
								
									<td class="content-field" valign="top">
									<c:if test="${promotionAwardsForm.awardsType != 'merchandise'}">
											<html:radio styleId="budgetTypeCentral" property="budgetType"
												value="central" disabled="${displayFlag}"
												onclick="updateLayersShown();" />
												<cms:contentText code="promotion.awards"
										key="BUDGET_TYPE_CENTRAL" />
									</c:if>
									</td>
									
										
								</tr>

							</table>
							</td>
						</tr>

						<%-- Budget Cap Type --%>

						<tr class="form-row-spacer" id="budgetCapRadio">
							<beacon:label property="budgetCapType" required="true"
								styleClass="content-field-label-top">
								<cms:contentText key="CAP_TYPE" code="promotion.awards" />
							</beacon:label>

							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><html:radio
										styleId="budgetCapTypeHard" property="budgetCapType"
										value="hard" disabled="${displayFlag}"
										onclick="updateLayersShown();" /> <cms:contentText
										code="promotion.awards" key="CAP_TYPE_HARD" /></td>
								</tr>

								<tr>
									<td class="content-field">
									</td>
								</tr>

							</table>
							</td>
						</tr>


						<%-- Budget Cap Type Text--%>
						<tr class="form-row-spacer" id="budgetCapText">
							<beacon:label property="budgetCapType" required="true"
								styleClass="content-field-label-top">
								<cms:contentText key="CAP_TYPE" code="promotion.awards" />
							</beacon:label>

							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><cms:contentText
										code="promotion.awards" key="CAP_TYPE_HARD" /></td>
								</tr>

							</table>
							</td>
						</tr>
						
						 <tr>
							<beacon:label property="budgetMasterStartDate" required="true" styleClass="content-field-label-top">
				              <cms:contentText key="BUDGET_MASTER_DATES" code="promotion.awards" />
				            </beacon:label> 
							<td class="content-field">
				              <table>	
				                <tr>
				                  <beacon:label property="budgetMasterStartDate" required="true">
						  		    <cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
						  		  </beacon:label>
						  		  <td class="content-field"> 
						  		  <label for="budgetMasterStartDate" class="date">	
					  		      	<html:text property="budgetMasterStartDate" styleId="budgetMasterStartDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
					  		       	<img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
					  		      </label>
				                  </td>
				                </tr>
				                <tr>
				                  <beacon:label property="budgetMasterEndDate">
				              		<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
				            	  </beacon:label>	
						          <td class="content-field">
						          <label for="budgetMasterEndDate" class="date">		  		  	
						            <html:text property="budgetMasterEndDate" styleId="budgetMasterEndDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
						            <img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
						          </label>
						          </td> 
				            	</tr>    
				              </table>
				            </td>
				          </tr>
				          
						  <%-- *******Budget Segment start******* --%>
				          <tr id="budgetSegmentOption" class="form-row-spacer" >
				          <beacon:label property="segmentName" required="true" styleClass="content-field-label-top">
				              <cms:contentText key="BUDGET_SEGMENT" code="admin.budgetmaster.details" />
				           </beacon:label> 
				          <td>
					          <table class="table table-striped table-bordered" width="120%">
							    <tr class="form-row-spacer">
							 		<th class="crud-table-header-row">
							 			<cms:contentText key="BUDGET_SEGMENT_NAME" code="admin.budgetmaster.details"/>
							 		</th> 
							 		<th class="crud-table-header-row">
							 			<cms:contentText key="SEGMENT_START_DATE" code="admin.budgetmaster.details"/>
							 		</th>      			
							 		<th class="crud-table-header-row">
							 			<cms:contentText key="SEGMENT_END_DATE" code="admin.budgetmaster.details"/>
							 		</th>  
							 		<th class="crud-table-header-row"  id="newBudgetCentralTitle">
							 			<cms:contentText key="BUDGET_AMOUNT" code="admin.budgetmaster.details"/>
							 		</th>
							 		<th class="crud-table-header-row"  id="sweepBudgetDateTitle">
							 			<cms:contentText key="SWEEP_BUDGET_DATE" code="promotion.awards" />
							 		</th>
							 		<c:if test="${ promotionAwardsForm.budgetSegmentVBListSize ne '1'}">
							 		<th class="crud-table-header-row" id="removeBudgetSegment"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
							 		</c:if>  			 		
							    </tr>
								<c:set var="switchColor" value="false"/>
								<%		  	 	int sIndex = 0; %>
							  	<c:forEach var="budgetSegmentVBList" items="${promotionAwardsForm.budgetSegmentVBList}" varStatus="status" >
							  	<html:hidden property="id" name="budgetSegmentVBList" indexed="true"/> 
							  	
							  	<%
									String startDateCalendarCounter = "charStartDate" + sIndex;
									String endDateCalendarCounter = "charEndDate" + sIndex;
									String newBudgetCentralValueCounter = "newBudgetCentralValue" + sIndex;
									String removeBudgetSegmentCounter = "removeBudgetSegment" + sIndex;
									String budgetSweepDateTDCounter = "budgetSweepDateTD" + sIndex;
									String budgetSweepDateCounter = "budgetSweepDate" + sIndex;
								%>	
								  <c:choose>
									<c:when test="${switchColor == 'false'}">
										<tr class="crud-table-row1">
										<c:set var="switchColor" scope="page" value="true"/>
									</c:when>
									<c:otherwise>
										<tr class="crud-table-row2">
										<c:set var="switchColor" scope="page" value="false"/>
									</c:otherwise>
								  </c:choose>
									    <td class="crud-content">
									    	<html:text property="segmentName" size="50" maxlength="50" indexed="true" styleId="segmentName" name="budgetSegmentVBList" styleClass="content-field" />
								      	</td>
								    	<td class="crud-content date-picker">
								    	<label for="<%=startDateCalendarCounter%>" class="date">
										    <html:text property="startDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=startDateCalendarCounter%>"/>
						               		<img alt="start date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
						                </label>
									    </td>
									    <td class="crud-content date-picker">
									    <label for="<%=endDateCalendarCounter%>" class="date">
									       	<html:text property="endDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=endDateCalendarCounter%>" />
						               		<img alt="end date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
						               	</label>
										</td>
										<td class="crud-content" id="<%=newBudgetCentralValueCounter%>">
									    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="originalValue" name="budgetSegmentVBList" styleClass="content-field" />
										</td>
										<td class="crud-content date-picker" id="<%=budgetSweepDateTDCounter%>">
										<label for="<%=budgetSweepDateCounter%>" class="date">
						                <html:text property="budgetSweepDate" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=budgetSweepDateCounter%>"/>
										<img alt="sweep date" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
										</label>
										</td>
							            <td class="crud-content">
							            	<table id="<%=removeBudgetSegmentCounter%>">
							            	<tr>
							            	<td align="right">
									        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionAwards.do', 'removeBudgetSegment')" >
									          <cms:contentText code="system.button" key="REMOVE" />
									        </html:button>
									        </td>
									        </tr>
									        </table>
									    </td>
				            		<% sIndex = sIndex + 1; %>
								    </tr>
								  </c:forEach>
								  
								  <tr class="form-blank-row"><td></td></tr>
								    <tr class="form-row-spacer">
								        <td align="left" colspan="2">
							     	  		<a id="addAnotherhref"  href="#" onclick="addAnotherSegment('addAnotherSegment')" >
							       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
							     	  		</a> 
							        	</td>
								    </tr>	  
							</table>
					    </td>
					    </tr>
				        <%-- ********Budget Segment end****** --%>				          
					</table>
					<%--  sub new budget table --%></td>
				</tr>
			</table>
			<%-- End newBudget --%></td>
		</tr>
		<%--  end budget row --%>
	</table>
	<%--  radio buttons for budget --%></td>
</tr>
<%--  budgetInfo --%>

<%--  Budget Final rule payout --%>
<tr class="form-row-spacer" id="finalPayoutRule_radio">
	<beacon:label property="finalPayoutRule" required="true"
		styleClass="content-field-label-top">
		<cms:contentText key="FINAL_PAYOUT_RULE"
			code="admin.budgetmaster.details" />
	</beacon:label>
	<td class="content-field-label" valign="top" align="left" nowrap>
	<c:forEach items="${budgetFinalPayoutRuleList}"
		var="budgetFinalPayoutRule">
		<html:radio property="finalPayoutRule"
			value="${budgetFinalPayoutRule.value}" onclick="" />&nbsp;<c:out
			value="${budgetFinalPayoutRule.label}" />
		<br>
	</c:forEach></td>
</tr>


<%-- Budget Amounts --%>

<tr class="form-row-spacer" id="budgetAmountLabel">
	<beacon:label property="fileloadBudgetAmount" required="true"
		styleClass="content-field-label-top">
		<cms:contentText key="BUDGET_AMOUNTS" code="promotion.awards" />
	</beacon:label>

	<td class="content-field-label" valign="top" align="left" nowrap>
	<table>
		<tr>
			<td class="content-field" valign="top" align="left"><html:radio
				styleId="fileloadBudgetAmountFalse" property="fileloadBudgetAmount"
				value="false" /> <cms:contentText code="promotion.awards"
				key="BUDGET_AMOUNTS_ONLINE" /></td>
		</tr>

		<tr>
			<td class="content-field" valign="top" align="left">
				<html:radio styleId="fileloadBudgetAmountTrue" property="fileloadBudgetAmount" value="true" />
				<cms:contentText code="promotion.awards" key="BUDGET_AMOUNTS_FILE_LOAD" />
			</td>
		</tr>

	</table>
	</td>
</tr>

<tr class="form-row-spacer" id="budgetTrackerOption">
      <beacon:label property="showInBudgetTracker" required="true" styleClass="content-field-label-top">
        <cms:contentText key="SHOW_IN_BUDGET_TRACKER" code="promotion.awards" />
      </beacon:label>
      <td class="content-field" align="left" width="75%">
        <table>
          <tr>
            <td class="content-field" valign="top">
                <html:radio styleId="showInBudgetTrackerFalse"  property="showInBudgetTracker" value="false" onclick="updateLayersShown();" disabled="${displayFlag}" />
                <cms:contentText  code="system.common.labels" key="NO" />
            </td>
          </tr>
          <tr>
            <td class="content-field" valign="top">
			    <html:radio styleId="showInBudgetTrackerTrue" property="showInBudgetTracker" value="true" onclick="updateLayersShown();" disabled="${displayFlag}" />
                <cms:contentText  code="system.common.labels" key="YES" />
            </td>
          </tr>
        </table>
      </td>
</tr>

</c:if>

<!-- Client customization start -->
<tr class="form-row-spacer" id="parentBudgetOption">
      <beacon:label property="utilizeParentBudgets" required="true" styleClass="content-field-label-top">
        <cms:contentText key="UTILIZE_PARENT_BUDGETS" code="promotion.awards" />
      </beacon:label>
      <td class="content-field" align="left" width="75%">
        <table>
          <tr>
            <td class="content-field" valign="top">
                <html:radio styleId="showInBudgetTrackerFalse"  property="utilizeParentBudgets" value="false" onclick="updateLayersShown();" disabled="${displayFlag}" />
                <cms:contentText  code="system.common.labels" key="NO" />
            </td>
          </tr>
          <tr>
            <td class="content-field" valign="top">
			    <html:radio styleId="showInBudgetTrackerTrue" property="utilizeParentBudgets" value="true" onclick="updateLayersShown();" disabled="${displayFlag}" />
                <cms:contentText  code="system.common.labels" key="YES" />
            </td>
          </tr>
        </table>
      </td>
</tr>
<!-- Client customization end -->

<script>
$( document ).ready(function() {
  var lastSegmentIndex = '<%=request.getAttribute("lastSegmentIndex")%>';
    $( "#budgetMasterStartDate" ).change(function() {
        var curDate = $(this).val();           
		$("#charStartDate0").val(curDate);  
});
    $( "#budgetMasterEndDate" ).change(function() {
        var curDate = $(this).val();           
		$("#charEndDate"+lastSegmentIndex).val(curDate);  
});
});
</script>