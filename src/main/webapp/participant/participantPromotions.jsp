<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.value.PromotionPaxValue" %>
<%@ page import="com.biperf.core.ui.participant.ParticipantPromotionsForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>

<script type="text/javascript">


</script>

<html:form styleId="contentForm" action="participantPromotionsDisplay">
  <html:hidden property="method" value="display" />
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${participantPromotionsForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PAX_PROMO_TITLE" code="participant.promotions" /></span>

		<br/>
        <beacon:username userId="${displayNameUserId}"/>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="participant.promotions"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table width="80%">
          <tr>
            <td>
            <c:if test="${moduleTypeListSize > 1}">
              <table>
                <tr class="form-row-spacer">
                  <beacon:label property="moduleType" required="false">
                    <cms:contentText key="TYPE_TO_SHOW" code="participant.promotions"/>
                  </beacon:label>
                  <td>
                    <html:select property="moduleType" styleClass="content-field">
                      <html:option value="all"><cms:contentText key="ALL_PROMO_TYPES" code="participant.promotions"/></html:option>
                      <html:options collection="moduleTypeList" property="key" labelProperty="value" />
                    </html:select>
                  </td>
                  <td>
                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('display')" >
                      <cms:contentText key="GO" code="system.button"/>
                    </html:submit>
                  </td>
                </tr>
              </table>
            </c:if>
            </td>
          </tr>
          <%	
            Map parameterMap = new HashMap();
            PromotionPaxValue promoPaxValue;
	        String userId = (String) request.getAttribute("displayNameUserId");
	        parameterMap.put( "userId", userId ); 
	      %>
          <tr>
            <td align="right">
              <display:table defaultsort="1" defaultorder="ascending" name="promotionPaxValueList" id="promotionPaxValue" >
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			  <% 
			     promoPaxValue = (PromotionPaxValue) pageContext.getAttribute( "promotionPaxValue" );
				 if (promoPaxValue != null)
				 {
				   parameterMap.put( "promotionId", promoPaxValue.getPromotion().getId() );
				   if(promoPaxValue.getPromotion().isChallengePointPromotion())
				   {
					   pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink("", RequestUtils.getBaseURI(request)+"/challengepoint/challengepointDetailsDisplay.do", parameterMap ) );
				   }
				   else if ( promoPaxValue.getPromotion().isGoalQuestPromotion() )
				   {
				       pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink("", "paxGoalQuestDetailsDisplay.do", parameterMap ) );
				   }
				   else if ( promoPaxValue.getPromotion().isThrowdownPromotion() )
				   {
				       pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink("", "paxThrowdownDetailsDisplay.do", parameterMap ) );
				   }
				   else if ( promoPaxValue.getPromotion().isRecognitionPromotion() )
				   {
				     if ( promoPaxValue.isPurlDetailLink() )
				     {
				       parameterMap.put( "purlRecipientId", promoPaxValue.getPurlRecipientId() );
				       pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "purlAdminMaintainDisplay.do", parameterMap ) );				       
				     }
				     else
				     {
				       pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink("", "paxPurlRecipientListDisplay.do", parameterMap ) );
				     }				     
				   }
				 }
			  %>
                <c:if test="${ promotionPaxValue.linkEnable}">
                <display:column titleKey="participant.promotions.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
                
                    <a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${promotionPaxValue.promotion.promotionName}"/></a>
                 
                </display:column>
                </c:if>
                <c:if test="${ !promotionPaxValue.linkEnable}">
                <display:column titleKey="participant.promotions.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
                
                    <c:out value="${promotionPaxValue.displayName}"/>
                 
                </display:column>
                </c:if>
                <display:column property="moduleCode" titleKey="participant.promotions.MODULE" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top nowrap" />
                <display:column titleKey="participant.promotions.START_DATE" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top nowrap" >
                  <fmt:formatDate value='${promotionPaxValue.startDate}' pattern='${JstlDatePattern}'/>
                </display:column>
                <display:column titleKey="participant.promotions.END_DATE" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top nowrap" >
                  <fmt:formatDate value='${promotionPaxValue.endDate}' pattern='${JstlDatePattern}'/>
                </display:column> 
                <display:column titleKey="participant.promotions.ROLE" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top nowrap" >
                  <cms:contentText key="${promotionPaxValue.roleKey}" code="promotion.types"/>
                </display:column>
              </display:table>
            </td>
          </tr>
          <tr>
            <td align="center">
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('display')" >
                <cms:contentText key="BACK_TO_PAX_OVERVIEW" code="participant.promotions" />
              </html:cancel>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>

