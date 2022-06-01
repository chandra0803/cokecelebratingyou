<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionTranslationsForm"%>
<%@ page import="com.objectpartners.cms.util.ContentReaderManager"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>

<body>
	<html:form styleId="contentForm" action="promotionTranslations">
		<beacon:client-state>
			<beacon:client-state-entry name="promotionId"
				value="${promotionSSITranslationsForm.promotionId}" />
		</beacon:client-state>
		<html:hidden property="promotionName" />
		<html:hidden property="promotionTypeCode" />
		<html:hidden property="promotionTypeName" />
		<html:hidden property="method" />
		<html:hidden property="version" />

		<table border="0" cellpadding="10" cellspacing="0" width="100%">
			<tr>
				<td colspan="2"><c:set var="promoTypeName" scope="request"
						value="${promotionSSITranslationsForm.promotionTypeName}" /> <c:set
						var="promoTypeCode" scope="request"
						value="${promotionSSITranslationsForm.promotionTypeCode}" /> <c:set
						var="promoName" scope="request"
						value="${promotionSSITranslationsForm.promotionName}" /> <c:set
						var="displayFlag" scope="request"
						value="${ true }" />
					<tiles:insert attribute="promotion.header" /></td>
			</tr>

			<tr>
				<td colspan="2"><cms:errors /></td>
			</tr>

			<tr>
				<td colspan="2">
					<table>

						<tr>
							<td colspan="2">
						<tr class="form-row-spacer">
							<beacon:label property="promotionName" required="false">
								<cms:contentText key="PROMOTION_NAME" code="promotion.basics" />
							</beacon:label>
							<td class="content-field"><c:out
									value="${promotionSSITranslationsForm.promotionName}" /></td>
						</tr>

						<tr class="form-blank-row">
							<td colspan="2"></td>
						</tr>
						<tr class="form-blank-row">
							<td colspan="2"></td>
						</tr>
						
						<%--merchandise option moved to SSI_Phase_2 --%>
						<%-- 
						<c:forEach items="${promotionSSITranslationsForm.levelTranslations}"
							var="translation" varStatus="idx">
							<tr class="form-row-spacer">
								<td> </td>
								<td class="content-bold"><c:out
										value='${translation.description}' /></td>
								<td class="content-field"><c:out
										value="${translation.translationEN}" />
									<input type="hidden" name="levelTranslation[<c:out value="${idx.index}"/>].translationCMKey"
										value="<c:out value="${translation.translationCMKey }"/>"/>
											
								</td>
							</tr>
							<c:forEach items="${translation.details}" var="detail" varStatus="jdx">
							<tr class="form-row-spacer">
								<td class="content-field-label-top">
									<input type="hidden" name="levelTranslation[<c:out value="${idx.index}"/>].detail[<c:out value="${jdx.index}"/>].localeCode" 
									  	   value="<c:out value="${detail.localeCode}"/>" />
									<input type="hidden" name="levelTranslation[<c:out value="${idx.index}"/>].detail[<c:out value="${jdx.index}"/>].localeDesc" 
										   value="<c:out value="${detail.localeDesc}"/>"/>
								</td>
								<td class="content-bold"><c:out
										value='${detail.localeDesc}' /></td>
								<td class="content-field">
									<input type="text" size="50" name="levelTranslation[<c:out value="${idx.index}"/>].detail[<c:out value="${jdx.index}"/>].name" 
									       value="<c:out value="${detail.name}"/>" />
								</td>
							</tr>

							<tr class="form-blank-row">
								<td colspan="2"></td>
							</tr>
							
							</c:forEach>
							
							<tr class="form-blank-row">
								<td colspan="2"></td>
							</tr>

						</c:forEach>
						--%>

						<c:forEach items="${promotionSSITranslationsForm.translations}"
							var="translation" varStatus="idx">
							<tr class="form-row-spacer">
								<beacon:label property="translationEN" required="false">
									<cms:contentText key="BADGE" code="promotion.ssi.awards" />
								</beacon:label>
								<td class="content-field"><c:out
										value="${translation.translationEN}" />
									<input type="hidden" name="translation[<c:out value="${idx.index}"/>].translationCMKey"
										value="<c:out value="${translation.translationCMKey }"/>"/>
											
								</td>
							</tr>
							<c:forEach items="${translation.details}" var="badge" varStatus="jdx">
							<tr class="form-row-spacer">
								<td class="content-field-label-top">
									<input type="hidden" name="translation[<c:out value="${idx.index}"/>].detail[<c:out value="${jdx.index}"/>].localeCode" 
									  	   value="<c:out value="${badge.localeCode}"/>" />
									<input type="hidden" name="translation[<c:out value="${idx.index}"/>].detail[<c:out value="${jdx.index}"/>].localeDesc" 
										   value="<c:out value="${badge.localeDesc}"/>"/>
								</td>
								<td class="content-bold"><c:out
										value='${badge.localeDesc}' /></td>
								<td class="content-field">
									<input type="text" size="50" name="translation[<c:out value="${idx.index}"/>].detail[<c:out value="${jdx.index}"/>].name" 
									       value="<c:out value="${badge.name}"/>" />
								</td>
							</tr>

							<tr class="form-blank-row">
								<td colspan="2"></td>
							</tr>
							
							</c:forEach>
							
							<tr class="form-blank-row">
								<td colspan="2"></td>
							</tr>

						</c:forEach>


					</table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><tiles:insert
						attribute="promotion.footer" /></td>
			</tr>
		</table>
	</html:form>

	

</body>
