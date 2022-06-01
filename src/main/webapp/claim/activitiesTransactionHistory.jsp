<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.journal.ActivityJournal" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.activity.SalesActivity" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="transactionHistory">
	<html:hidden property="method" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${transactionHistoryForm.userId}"/>
	 	<beacon:client-state-entry name="livePromotionId" value="${transactionHistoryForm.livePromotionId}"/>
	 	<beacon:client-state-entry name="liveStartDate" value="${transactionHistoryForm.liveStartDate}"/>
 		<beacon:client-state-entry name="liveEndDate" value="${transactionHistoryForm.liveEndDate}"/>
 		<beacon:client-state-entry name="promotionId" value="${transactionHistoryForm.promotionId}"/>
 		<beacon:client-state-entry name="promotionType" value="${transactionHistoryForm.promotionType}"/>
 		<beacon:client-state-entry name="startDate" value="${transactionHistoryForm.startDate}"/>
 		<beacon:client-state-entry name="endDate" value="${transactionHistoryForm.endDate}"/>
	</beacon:client-state>

	<table cellpadding="3" cellspacing="1" >
        <tr>
          <td class="headline">
            <cms:contentText key="TITLE" code="participant.transaction.activities"/>
          </td>
        </tr>
        <tr>
          <td class="subheadline">
        	<c:out value="${participant.lastName}"/>, &nbsp;<c:out value="${participant.firstName}"/>&nbsp;<c:out value="${participant.middleName}"/>
      	  </td>
    	</tr>
        <% 	Map clientStateMap = ClientStateUtils.getClientStateMap( request );

            if ( ! ClientStateUtils.getParameterValue( request, clientStateMap, "promotionType").equals("quiz") )
            {
              pageContext.setAttribute( "claimNumber", ClientStateUtils.getParameterValue( request, clientStateMap, "claimNumber" ) );

	    %>
    	<tr>
          <td class="content">


            <cms:contentText key="CLAIM_NUMBER" code="participant.transaction.activities"/>&nbsp;<c:out value="${pageScope.claimNumber}"/>

          </td>
        </tr>
		 <% } %>
        <tr>
          <td class="content">
            <cms:contentText key="SUBMITTED" code="participant.transaction.activities"/>&nbsp;
            <c:out value="${param.dateSubmitted}"/>
          </td>
        </tr>
        <tr>
          <td class="content">
            &nbsp;
          </td>
        </tr>
        <tr>
          <td class="content">
            <cms:contentText key="INSTRUCTIONS" code="participant.transaction.activities"/>
          </td>
        </tr>
        <tr>
          <td class="content">
            &nbsp;
          </td>
        </tr>
      </table>

<table width="100%">
    <tr>
        <td>
					<%  Map parameterMap = new HashMap();
							ActivityJournal temp;
					%>
            <display:table defaultsort="1" defaultorder="ascending" name="activities" id="activity" style="width: 100%">

			  <display:setProperty name="basic.msg.empty_list">
				  <tr class="crud-content" align="left"><td colspan="{0}">
            <cms:contentText key="NOTHING_TO_DISPLAY" code="participant.transaction.activities"/>
          </td></tr>
	      </display:setProperty>
	      <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

              <display:column titleKey="participant.transaction.activities.TRANSACTION_ID" title='<cms:contentText key="TRANSACTION_ID" code="participant.transaction.activities"/>' property="id" headerClass="crud-table-header-row" class="crud-content-bold center-align"/>

			  <c:if test="${transactionHistoryForm.livePromotionType == 'product_claim'}">
			  	<display:column titleKey="participant.transaction.activities.TYPE" headerClass="crud-table-header-row" class="crud-content-bold center-align">
                  	<c:if test="${activity.getClass().name == 'com.biperf.core.domain.activity.SalesActivity'}">
                  		<cms:contentText key="PRODUCT_CLAIM" code="participant.transaction.activities"/>
              		</c:if>
              		<c:if test="${activity.getClass().name == 'com.biperf.core.domain.activity.ManagerOverrideActivity'}">
                  		<cms:contentText key="MANAGER_OVERRIDE" code="participant.transaction.activities"/>
              		</c:if>
              	</display:column>
              </c:if>

			  <display:column titleKey="participant.transaction.activities.PROMOTION_NAME" property="promotion.name" headerClass="crud-table-header-row" class="crud-content-bold center-align"/>

			  <c:if test="${transactionHistoryForm.livePromotionType == 'product_claim'}">
                	<c:if test="${activity.getClass().name == 'com.biperf.core.domain.activity.SalesActivity'}">
			  			<display:column titleKey="participant.transaction.activities.PRODUCT" property="product.name" headerClass="crud-table-header-row" class="crud-content-bold center-align"/>
					  	<display:column titleKey="participant.transaction.activities.QUANTITY" property="quantity" headerClass="crud-table-header-row" class="crud-content-bold center-align"/>
					</c:if>
              </c:if>

              <display:column titleKey="participant.transaction.activities.STATUS" headerClass="crud-table-header-row" class="crud-content-bold center-align">
			  	<c:if test="${activity.posted == 'true'}">
                  	<cms:contentText key="POSTED" code="participant.transaction.activities"/>
              	</c:if>
              </display:column>

			  <display:column titleKey="participant.transaction.activities.PAYOUT_ID" headerClass="crud-table-header-row" class="crud-content-bold center-align">
			  	<table>
            <c:forEach items="${activity.activityJournals}" var="activityJournal">
            	<tr>
				  <td class="crud-content-bold">
				    <c:choose>
				      <c:when test="${activity.promotion.awardType.code eq 'merchandise'}">
			  						<c:out value="${activityJournal.journal.id}"/>
				      </c:when>
				      <c:otherwise>

            			<%  temp = (ActivityJournal)pageContext.getAttribute("activityJournal");
											parameterMap.put( "transactionId", temp.getJournal().getId() );
											//These could be individual request parameters or part of the current clientState parameter
											Map clientStateMap2 = ClientStateUtils.getClientStateMap(request);
											parameterMap.put( "userId", ClientStateUtils.getParameterValue(request, clientStateMap2, "userId") );
											parameterMap.put( "promotionId", ClientStateUtils.getParameterValue(request, clientStateMap2, "promotionId") );
											parameterMap.put( "promotionType", ClientStateUtils.getParameterValue(request, clientStateMap2, "promotionType") );
											parameterMap.put( "startDate", ClientStateUtils.getParameterValue(request, clientStateMap2, "startDate") );
											parameterMap.put( "endDate", ClientStateUtils.getParameterValue(request, clientStateMap2, "endDate") );
											parameterMap.put( "claimId", ClientStateUtils.getParameterValue(request, clientStateMap2, "claimId") );
											parameterMap.put( "claimNumber", ClientStateUtils.getParameterValue(request, clientStateMap2, "claimNumber") );
											parameterMap.put( "open", ClientStateUtils.getParameterValue(request, clientStateMap2, "open") );
											parameterMap.put( "firstName", ClientStateUtils.getParameterValue(request, clientStateMap2, "firstName") );
											parameterMap.put( "lastName", ClientStateUtils.getParameterValue(request, clientStateMap2, "lastName") );
											parameterMap.put( "middleName", ClientStateUtils.getParameterValue(request, clientStateMap2, "middleName") );
											parameterMap.put( "dateSubmitted", ClientStateUtils.getParameterValue(request, clientStateMap2, "dateSubmitted") );
											pageContext.setAttribute("payoutTransactionDetailUrl", ClientStateUtils.generateEncodedLink( "", "payoutTransactionDetailsDisplay.do?method=prepareUpdate&callingScreen=activities", parameterMap, true ) );
									%>
									<a href="javascript:setActionDispatchAndSubmit('<c:out value="${payoutTransactionDetailUrl}"/>','prepareUpdate');" class="content-link">
			  						<c:out value="${activityJournal.journal.id}"/>
			  					</a>
			  		    </c:otherwise>
			  		  </c:choose>
                </td>
							</tr>
            </c:forEach>
					</table>
        </display:column>
			</display:table>

		</td>
    </tr>
<tr>
 	<td>
 		&nbsp;
 	</td>
 </tr>
 <tr>

	<td align="center">
		<%  Map paramMap = new HashMap();
			paramMap.put( "mode", request.getAttribute("mode") );
			String currentMode = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "mode" );
			paramMap.put( "userId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "userId" ) );
			paramMap.put( "promotionType", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionType" ) );
            paramMap.put( "startDate", ClientStateUtils.getParameterValue(request, ClientStateUtils.getClientStateMap(request), "startDate") );
			paramMap.put( "endDate", ClientStateUtils.getParameterValue(request, ClientStateUtils.getClientStateMap(request), "endDate") );
			pageContext.setAttribute("returnToTransactionUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistory.do?method=showActivity&mode="+currentMode+"&open="+request.getParameter("open"), paramMap ) );
		%>
 		<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${returnToTransactionUrl}','display')">
 			<cms:contentText key="BACK_TO_TRANSACTION_LIST" code="recognition.detail"/>
 		</html:submit>
	</td>
</tr>
</table>
</html:form>