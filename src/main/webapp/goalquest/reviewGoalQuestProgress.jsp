<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.domain.promotion.Promotion"%>
<%@ page import="com.biperf.core.value.GoalLevelValueBean"%>
<%@ page import="com.biperf.awardslinqDataRetriever.client.ProductGroupDescription" %>
<%@ page import="com.biperf.core.domain.goalquest.PaxGoal"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>

<c:set var="prec" value="${paxGoal.goalQuestPromotion.achievementPrecision.precision }" />

<script type="text/javascript">
<!--
function openItemDetail(itemCopy, itemName, detailImageUrl)
{
    popUpWin('<%=request.getContextPath()%>/goalquest/itemDetail.do?itemName=' + escape(itemName) + '&imageUrl=' + detailImageUrl + '&itemCopy=' + itemCopy , 'console', 400, 400, false, true);
}    
//-->
</script>
<%  
  Map paramMap = new HashMap();
  Long userId = (Long)request.getAttribute("userId");
  Promotion promotion = (Promotion)request.getAttribute("promotion");
  paramMap.put( "userId", userId );
  if (promotion != null) {
    paramMap.put( "promotionId", promotion.getId() );
  }
  pageContext.setAttribute("detailURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/paxGoalQuestDetailsDisplay.do", paramMap ) );
%>
<html:hidden property="method" value="" />

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td><span class="headline">
		<c:choose>
		  <c:when test="${adminView}">
            <cms:contentText key="TITLE" code="participant.goalquest.promo.detail" />
          </c:when>
          <c:otherwise>
            <cms:contentText key="TITLE" code="promotion.goalquest.progress" />
          </c:otherwise>
        </c:choose>		
		</span> <br />
		<br />
		<span class="content-instruction"> <b>
		<c:choose>
		  <c:when test="${adminView}">
            <beacon:username userId="${userId}"/> 
          </c:when>
          <c:otherwise>
          	<c:out value="${promotion.upperCaseName}" />
          </c:otherwise>
        </c:choose>
        </b>
        </span>
       <%--INSTRUCTIONS--%>

		<span class="content-instruction"> 
		<c:choose>
		  <c:when test="${adminView}">
            <cms:contentText key="INSTRUCTIONS" code="participant.goalquest.promo.detail" />
          </c:when>
          <c:otherwise>
            <cms:contentText key="INSTRUCTION" code="promotion.goalquest.progress" /> 
          </c:otherwise>
        </c:choose>		
		</span> 

		<%--END INSTRUCTIONS--%> <cms:errors /> <%--  START HTML needed with display table --%>
		<%-- Table --%>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td valign="top">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td class="content-center" width="65%" valign="top">
						<table>
							<c:if test="${not interim and not adminView}">
								<tr>
									<td colspan="2" class="content-field left-align"><c:if
										test="${isAchieved}">
										<cms:contentText key="ACHIEVED_MESSAGE_1"
											code="promotion.goalquest.progress" />
									<%--BugFix 22857 Starts --%>
									<beacon:authorize ifNotGranted="LOGIN_AS">
									 <a onClick="recordOutboundLink('Outbound Links', 'Shopping')" href="<c:out  value="${goalLevelValueBean.shoppingURL}" />" target="_blank" >
									</beacon:authorize>												
										<%--BugFix 17937 Starts --%>
										<c:if
										test="${isgiftCodeRedeemed}">
										<cms:contentText key="CHECK_STATUS"
											code="promotion.goalquest.progress" /> &nbsp;
										<cms:contentText key="ACHIEVED_MESSAGE_3"
											code="promotion.goalquest.progress" />
										</c:if>	
										<c:if
										test="${!isgiftCodeRedeemed}">
										<cms:contentText key="ORDER_NOW"
											code="promotion.goalquest.progress" /> 
										</c:if>	
										<%--BugFix 17937 ends --%>
										</a>
										</c:if> <c:if test="${!isAchieved}">
										<cms:contentText key="NOT_ACHIEVED_MESSAGE"
											code="promotion.goalquest.progress" />
									</c:if></td>
								</tr>
								<tr class="form-blank-row">
									<td colspan="2"></td>
								</tr>
								<tr class="form-blank-row">
									<td colspan="2"></td>
								</tr>
							</c:if>
							<c:if test="${adminView}">
							  <tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="PROMOTION_NAME" code="promotion.goalquest.progress" /></td>
								<td class="content-field-review">          		
								  <c:out value="${promotion.upperCaseName}" />
								</td>
							  </tr>
							</c:if>
							<tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="LEVEL_SELECTED" code="promotion.goalquest.progress" /></td>
								<td class="content-field-review"><c:out
									value="${goalLevelValueBean.goalLevel.goalLevelName}" /></td>
							</tr>
							<tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="DESCRIPTION" code="promotion.goalquest.progress" /></td>
								<td class="content-field-review"><c:out
									value="${goalLevelValueBean.goalLevel.goalLevelDescription}" />
								</td>
							</tr>
							<c:if test="${ ! empty goalLevelValueBean.baseAmount }">
								<tr class="form-row-spacer">
									<td class="content-field-label"><cms:contentText
										key="BASE" code="promotion.goalquest.progress" /></td>
									<td class="content-field-review">
								<c:if test="${promotion.baseUnit != null }"><c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }"><c:out value="${promotion.baseUnit}" escapeXml="false"/></c:if></c:if><fmt:formatNumber value="${goalLevelValueBean.baseAmount}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/><c:if test="${promotion.baseUnit != null }"><c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }"><c:out value="${promotion.baseUnit}" escapeXml="false"/></c:if></c:if>
					     </td>
							</tr>
							</c:if>
							<tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="GOAL_NUMBER" code="promotion.goalquest.progress" /></td>
								<%-- code fix for bug 17935 to add the Goal Value --%>
								<td class="content-field-review">
								<c:if test="${promotion.baseUnit != null }"><c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }"><c:out value="${promotion.baseUnit}" escapeXml="false"/></c:if></c:if><fmt:formatNumber value="${goalQuestProgress.amountToAchieve}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/><c:if test="${promotion.baseUnit != null }"><c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }"><c:out value="${promotion.baseUnit}" escapeXml="false"/></c:if></c:if>
							 </td>
						  </tr>
							<tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="PROGRESS_DATE" code="promotion.goalquest.progress" /></td>
								<td class="content-field-review"><c:out
									value="${goalQuestProgress.displaySubmissionDate}" /></td>
							</tr>
							<tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="TOTAL_QUANTITY" code="promotion.goalquest.progress" /></td>
								<td class="content-field-review">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnit}" escapeXml="false"/></c:if></c:if><fmt:formatNumber  value="${paxGoal.currentValue}" type="NUMBER" minFractionDigits="${prec}" maxFractionDigits="${prec}"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnit}" escapeXml="false"/></c:if></c:if>
								</td>
							</tr>
							<tr class="form-row-spacer">
								<td class="content-field-label"><cms:contentText
									key="PERCENT_TO_GOAL" code="promotion.goalquest.progress" /></td>
								<td class="content-field-review"><c:out
									value="${goalQuestProgress.displayPercentToGoal}" /></td>
							</tr>
                            <c:if test="${partner}">
							
							<tr class="form-row-spacer">
							<td class="content-field-label"><cms:contentText key="PARTNER" code="promotion.goalquest.selectpartner"/></td>
                            
						<c:if test="${partnerrsList!=null && not empty partnerrsList }">
							
							<td class="content-field-review">
                            
							<c:forEach items="${partnerrsList}" var="pax" varStatus="status">
							<c:if test="${status.index >0 }">
							   <tr class="form-row-spacer"><td class="content-field-review"><td class="content-field-review">
							</c:if>
							<c:out value="${pax.partner.lastName}"/>, <c:out value="${pax.partner.firstName}"/><br/>
							 <c:if test="${status.index == 0 }">
								 </td>
								 </c:if>
								  <c:if test="${status.index > 0 }">
								</tr>
								 </c:if>
				            </c:forEach>
							</td>
						</c:if>
					    <c:if test="${partnerrsList==null || empty partnerrsList }">
						<td class="content-field-review">
						<cms:contentText
								key="NONE_SELECTED" code="promotion.goalquest.selectpartner" />
				        </td>

						</c:if>
							</tr>
							
							</c:if>

						</table>
						</td>
						<td class="content-right" width="35%" valign="top">
						  <c:if	test="${not adminView and goalLevelValueBean.selectedProduct != null}">
							<table border="0" cellpadding="0" cellspacing="0" width="186"
								class="right-rail">
								<tr align="center">
									<td colspan="2" class="gutter-content"><b><cms:contentText
										key="AWARD" code="promotion.goalquest.review.submit" /></b></td>
								</tr>
								<tr>
									<td colspan="2"><span class="gutter-content">
									<center><img alt=""
										src="<c:out value='${goalLevelValueBean.selectedProduct.thumbnailImageURL}'/>"
										border="0" class="item-thumbnail" width="100" height="100">
									</center>
									</span>
								</tr>
								<tr>
									<td colspan="2" class="gutter-content center-align"><c:choose>
										<c:when
											test="${goalLevelValueBean.selectedProduct.detailImageURL != null }">
									<%		  
											  Map sweetImageMap = new HashMap();
												GoalLevelValueBean goalLevelValueBean = (GoalLevelValueBean)request.getAttribute("goalLevelValueBean");
												ProductGroupDescription productDescription = goalLevelValueBean.getSelectedProduct().getProductGroupDescriptions().get(Locale.US);
												String productCopy = productDescription.getCopy().substring(0, (productDescription.getCopy().length() > 1024?1024: productDescription.getCopy().length()));
											    String detailImageURL = goalLevelValueBean.getSelectedProduct().getDetailImageURL().toString();
											  
											  sweetImageMap.put( "productCopy", productCopy );
											  sweetImageMap.put( "productDescription", productDescription.getDescription() );
											  sweetImageMap.put( "detailImageURL", detailImageURL );
											  pageContext.setAttribute("imageURL", ClientStateUtils.generateEncodedLink( "", "itemDetail.do", sweetImageMap,true ) );
										%>
								      
								  	    <a class="content-link" href="javascript:popUpWin('<c:out value="${imageURL}"/>', 'console', 400, 400, false, true);">
								  	      <c:out escapeXml="false" value="${goalLevelValueBean.selectedProduct.productDescription}" />
								  	    </a> 
										</c:when>
										<c:otherwise>
											<c:out escapeXml="false"
												value='${goalLevelValueBean.selectedProduct.productDescription}' />
										</c:otherwise>
									</c:choose></td>
								</tr>
							</table>
						</c:if></td>
					</tr>
					<c:if test="${isAutomotive}">
						<tr class="form-blank-row">
							<td colspan="2"></td>
						</tr>
						<tr class="form-blank-row">
							<td colspan="2"></td>
						</tr>
						<tr class="form-blank-row">
							<td colspan="2"></td>
						</tr>
						<tr>
							<td colspan="2">
							<table>
								<tr>
									<td align="right"><display:table
										name="goalQuestProgressList" id="goalQuestReviewProgress"
										sort="list"
										requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">>
										<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				                  <display:column
											titleKey="promotion.goalquest.progress.SALE_DATE"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.displaySaleDate}" />
										</display:column>
										<display:column
											titleKey="promotion.goalquest.progress.DELIVERY_DATE"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.displayDeliveryDate}" />
										</display:column>
										<display:column
											titleKey="promotion.goalquest.progress.MODEL"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.model}" />
										</display:column>
										<display:column
											titleKey="promotion.goalquest.progress.VIN"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.vin}" />
										</display:column>
										<display:column
											titleKey="promotion.goalquest.progress.TRANSACTION_TYPE"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.transactionType.name}" />
										</display:column>
										<display:column
											titleKey="promotion.goalquest.progress.DEALER_CODE"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.dealerCode}" />
										</display:column>
										<display:column
											titleKey="promotion.goalquest.progress.DEALER_NAME"
											headerClass="crud-table-header-row"
											class="crud-content left-align" sortable="true">
											<c:out value="${goalQuestReviewProgress.dealerName}" />
										</display:column>

									</display:table></td>
								</tr>
							</table>
							</td>
						</tr>
					</c:if>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<%-- Table --%>
<%--  END HTML needed with display table --%>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr class="form-blank-row">
		<td></td>
	</tr>
	<tr align="center">
		<td>
		<c:choose>
		  <c:when test="${adminView}">
            <input type="submit" class="content-buttonstyle"
			   onclick="callUrl('<c:out value='${detailURL}'/>')"
			   value="<cms:contentText code="promotion.goalquest.progress" key="BACK" />">
          </c:when>
          <c:otherwise>
            <input type="submit" class="content-buttonstyle"
			   onclick="callUrl('<%=request.getContextPath()%>/homePage.do')"
			   value="<cms:contentText code="promotion.goalquest.progress" key="BACK_TO_HOME" />">
          </c:otherwise>
        </c:choose>		
		</td>
	</tr>
</table>


