<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.domain.promotion.Promotion"%>
<%@ page import="com.biperf.core.value.PromotionRoundValue"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:hidden property="method" value="" />

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td><span class="headline"><cms:contentText key="TITLE"
					code="promotion.throwdown.calculationList" /> </span> <%--INSTRUCTIONS--%>
			<br /> <br /> <span class="content-instruction"> <cms:contentText
					key="INSTRUCTION" code="promotion.throwdown.calculationList" /> </span> <br />
			<br /> <%--END INSTRUCTIONS--%> <cms:errors /> <%--  START HTML needed with display table --%>
			<%-- Table --%>
			<table width="50%">
				<tr>
					<td align="right">
						<%
						  Map parameterMap = new HashMap();
						  Promotion temp;
						  PromotionRoundValue promoRound;
						%> 
				<display:table name="promotions" id="promotions" sort="list"
							pagesize="20"
							requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
							<display:setProperty name="basic.msg.empty_list_row">
								<tr class="crud-content" align="left">
									<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
											code="system.errors" /></td>
								</tr>
							</display:setProperty>
							<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
							<display:column
								titleKey="promotion.throwdown.calculationList.NAME"
								headerClass="crud-table-header-row"
								class="crud-content left-align" sortable="true"
								sortProperty="name">
								<c:out value="${promotions.promotion.name }" />
							</display:column>
							<display:column
								titleKey="promotion.payout.throwdown.ROUND_NUMBER"
								headerClass="crud-table-header-row"
								class="crud-content left-align" sortable="true"
								sortProperty="name">
								<c:out value="${promotions.roundNumber}" />
							</display:column>
							<display:column
								titleKey="promotion.throwdown.calculationList.STATUS"
								headerClass="crud-table-header-row"
								class="crud-content left-align" sortable="true"
								sortProperty="promotionStatus">
								<c:out value="${promotions.promotion.promotionStatus.name}" />
							</display:column>
							<display:column
								titleKey="promotion.throwdown.calculationList.ACTIONS"
								headerClass="crud-table-header-row"
								class="crud-content center-align">
								<%
								  promoRound = (PromotionRoundValue)pageContext.getAttribute( "promotions" );
								      temp = promoRound.getPromotion();
								      Boolean isExpired = new Boolean( temp.getPromotionStatus().isExpired() );
								      parameterMap.put( "id", temp.getId() );
								      parameterMap.put( "isExpired", isExpired );
								      parameterMap.put( "roundNumber", String.valueOf( promoRound.getRoundNumber() ) );
								      pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( "", "throwdownAwardSummary.do?method=display", parameterMap ) );
								%>
								<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
									<cms:contentText key="RUN_CALCULATION"
										code="promotion.throwdown.calculationList" /> </a>
							</display:column>

						</display:table></td>
				</tr>
			</table> <%-- Table --%> <%--  END HTML needed with display table --%>
		</td>
	</tr>
</table>
