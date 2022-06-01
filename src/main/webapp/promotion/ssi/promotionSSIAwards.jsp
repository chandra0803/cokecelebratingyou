<%@page import="java.util.List"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>

<html:form styleId="contentForm" action="promotionAwardsSave">
	<html:hidden property="method" />
	<html:hidden property="promotionStatus" />
	<html:hidden property="promotionName" />
    <html:hidden property="promotionTypeName" />
	<html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
	<input type="hidden" name="activeCountryListCount"
		value="<c:out value="${promotionSSIAwardsForm.activeCountryListCount}"/>" />

	<beacon:client-state>
		<beacon:client-state-entry name="promotionId"
			value="${promotionSSIAwardsForm.promotionId}" />
	</beacon:client-state>
	
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	   <td colspan="2">
	    <c:set var="promoTypeName" scope="request" value="${promotionSSIAwardsForm.promotionTypeName}" />
	    <c:set var="promoTypeCode" scope="request" value="${promotionSSIAwardsForm.promotionTypeCode}" />	    
  	    <c:set var="promoName" scope="request" value="${promotionSSIAwardsForm.promotionName}" />
	    <tiles:insert attribute="promotion.header" /> 
	  </td>
	</tr>
	<tr>
		<td><cms:errors /></td>
	</tr>

	<tr class="form-blank-row">
		<td></td>
	</tr>
	<tr class="form-row-spacer">
		<td class="content-field" align="left" width="100%">
			<table>
				<tr class="form-row-spacer">
					*&nbsp;<b><cms:contentText key="AWARD_TYPES_SELECT" code="promotion.ssi.awards" /></b>
				</tr>
				<tr>
					<td class="content-field" valign="top"><html:checkbox
							styleId="allowAwardPoints" property="allowAwardPoints" onchange="enableFields();"/> <cms:contentText
							code="promotion.ssi.awards" key="AWARD_POINTS" />
					</td>
				</tr>
				<%-- Mercheandise option has been moved to SSI_Phase_2. This section is commented out --%>
				<%--
				<tr>
					<td class="content-field" valign="top"><html:checkbox
							styleId="allowAwardMerchandise" property="allowAwardMerchandise" />
						<cms:contentText code="promotion.ssi.awards"
							key="AWARD_MERCH" /></td>

					<c:set var="disableFlag"
						value="${promotionSSIAwardsForm.promotionStatus == 'live'}" />
					<c:if test="${promotionSSIAwardsForm.activeCountryListCount > 0}">
						<table>
							<tbody id="requiredActiveCountryList">
								<c:forEach items="${promotionSSIAwardsForm.activeCountryList}"
									var="activeCountryList">
									<tr>
										<td width="120"></td>
										<td width="120" class="content-field"><cms:contentText
												key="COUNTRY_NAME" code="${activeCountryList.countryName}" /></td>
										<html:hidden name="activeCountryList" indexed="true"
											property="countryId" />
										<html:hidden name="activeCountryList" indexed="true"
											property="promoMerchCountryId" />
										<html:hidden name="activeCountryList" indexed="true"
											property="countryName" />
										<td><c:choose>
												<c:when
													test="${disableFlag and not empty activeCountryList.programId }">
													<c:set var="currentDisabled" value="${true }" />
													<html:hidden name="activeCountryList" property="programId"
														indexed="true" />
												</c:when>
												<c:otherwise>
													<c:set var="currentDisabled" value="${false }" />
												</c:otherwise>
											</c:choose> <html:text name="activeCountryList" indexed="true"
												property="programId" size="10" maxlength="10"
												styleClass="content-field" disabled="${currentDisabled}" />
										</td>
										<td>&nbsp;&nbsp;&nbsp;&nbsp;
											<html:submit styleClass="content-buttonstyle"
												onclick="setDispatchAndSubmit('saveAndPopulateMerchLevels')">
												<cms:contentText key="LOOKUP" code="system.button" />
											</html:submit>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>

					<tiles:insert attribute="promotionSSIMerchLevels" />

				</tr>
				--%>
				<tr class="form-blank-row">
					<td></td>	
				</tr>	<br />
				<tr>
					<td class="content-field" valign="top"><html:checkbox
							styleId="allowAwardOther" property="allowAwardOther" onchange="enableFields();"/> <cms:contentText
							code="promotion.ssi.awards" key="AWARD_OTHER" />
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr class="form-blank-row">
		<td></td>
	</tr>
    	<tr>			
	     <td class="content-field">
	       	<table>
	       		<tr>
	       		  <td class="content-field" valign="top" colspan="2">
			         &nbsp;&nbsp;&nbsp;<b>*<cms:contentText key="TAXABLE" code="promotion.basics" /></b>
			      </td>
			      <td class="content-field" valign="top" colspan="2">
			         &nbsp;&nbsp;&nbsp;&nbsp;<html:radio styleId="taxableFalse" property="taxable" value="false" /><cms:contentText code="system.common.labels" key="NO"/>
			      </td>
	       		</tr>
	        	<tr>
 					<td class="content-field" valign="top" colspan="2"></td>
 					<td class="content-field" valign="top">
 					  &nbsp;&nbsp;&nbsp;&nbsp;<html:radio styleId="taxableTrue" property="taxable" value="true" /><cms:contentText code="system.common.labels" key="YES"/>
 					</td>
		        </tr>
	      	</table>
	     </td>
	</tr>

	<tr class="form-blank-row">
		<td></td>
	</tr>
	<br />
	<br />
	<tiles:insert attribute="promotionSSIAwardsBadge" />

	<tr>
		<td colspan="3" align="center">
			<tiles:insert attribute="promotion.footer" />
		</td>
	</tr>
</table>

<script>
$(document).ready(function() {
	<c:if test="${promotionStatus == 'expired' || promotionStatus == 'live' || promotionStatus == 'complete'}" >
		allowAwardPoints.disabled = true;
		allowAwardOther.disabled = true;
	</c:if>
});
</script>

</html:form>
<tiles:insert attribute="promotionSSIAwardsJS"/>