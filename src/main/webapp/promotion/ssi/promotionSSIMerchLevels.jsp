<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromoMerchProgramLevelFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>

<html:hidden property="awardsType" />
   <c:if test="${promotionSSIAwardsForm.allowAwardMerchandise}">  
	<%
	Map paramMap = new HashMap();
	%>
	<html:hidden property="countryListCount" />
	<tr class="form-blank-row">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr class="form-row-spacer" id="levelnames">
		<td class="content-field" valign="top"><c:if
			test="${promotionSSIAwardsForm.countryList != null }"> 
			<table border="0" cellpadding="10" cellspacing="0" width="75%" align="right">
				<c:set var="countryIndex" value="${1}" />
				<c:forEach items="${promotionSSIAwardsForm.countryList}"
					var="countryList">
				
					<c:if test="${countryList.newCountry }">
						<tr class="form-blank-row">
							<td colspan="3">&nbsp;</td>
						</tr>
						<c:set var="countryIndex" value="${1}" />
	 					<tr>
							<td class="content-bold" valign="top" align="left"><cms:contentText
								code="${countryList.countryAssetKey}" key="COUNTRY_NAME" />&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
								<a href="javascript:openModalPopup('<c:out value="${linkUrl}"/>',POPUP_SIZE_LARGE,true)">
								<cms:contentText key="VIEW_AWARD_FOR_COUNTRY" code="promotion.awards" /> </a>
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
						<td class="content-field" valign="top" align="left">
						<html:text
							name="countryList" property="levelName" indexed="true"
							maxlength="50" size="20" /> 
							
							<html:hidden name="countryList"
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