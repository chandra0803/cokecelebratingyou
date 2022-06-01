<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.journal.Journal"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="transactionHistory">

<html:hidden property="method" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${transactionHistoryForm.userId}"/>
	 	<beacon:client-state-entry name="livePromotionId" value="${transactionHistoryForm.livePromotionId}"/>
	 	<beacon:client-state-entry name="livePromotionType" value="${transactionHistoryForm.livePromotionType}"/>
	 	<beacon:client-state-entry name="liveStartDate" value="${transactionHistoryForm.liveStartDate}"/>
	 	<beacon:client-state-entry name="liveEndDate" value="${transactionHistoryForm.liveEndDate}"/>
	 	<beacon:client-state-entry name="promotionId" value="${transactionHistoryForm.promotionId}"/>
	 	<beacon:client-state-entry name="promotionType" value="${transactionHistoryForm.promotionType}"/>
	 	<beacon:client-state-entry name="startDate" value="${transactionHistoryForm.startDate}"/>
	 	<beacon:client-state-entry name="endDate" value="${transactionHistoryForm.endDate}"/>
	</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td>
			<span class="headline"><cms:contentText key="TITLE" code="participant.transaction.payout"/></span>
			<%-- Subheadline --%>
			<br/>
			<span class="subheadline">
				<c:out value="${user.titleType.name}" />
		  	<c:out value="${user.firstName}" />
				<c:out value="${user.middleName}" />
				<c:out value="${user.lastName}" />
				<c:out value="${user.suffixType.name}" />
				<br/>
				<% 	Map clientStateMap = ClientStateUtils.getClientStateMap( request );
                    if ( ! ClientStateUtils.getParameterValue( request, clientStateMap, "promotionType" ).equals("quiz") )
                    {
				      pageContext.setAttribute( "claimNumber", ClientStateUtils.getParameterValue( request, clientStateMap, "claimNumber" ) );
				%>
				      <cms:contentText key="CLAIM_NUMBER" code="participant.transaction.payout"/>&nbsp;<c:out value="${pageScope.claimNumber}"/>
				 <% } %>
     	</span>
      <%-- End Subheadline --%>

			<%--INSTRUCTIONS--%>
			<br/><br/>
			<span class="content-instruction">
				<cms:contentText key="INSTRUCTIONS" code="participant.transaction.payout"/>
			</span>
			<br/><br/>
			<%--END INSTRUCTIONS--%>
     	
			<cms:errors/>
        
			<table width="50%">                           
				<tr>
					<td align="right">
						<%  Map<Object, Object> parameterMap = new HashMap<>();
								Journal temp;
						%>
						<display:table defaultsort="1" defaultorder="ascending" name="payouts" id="payout" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
						<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   		</display:setProperty>
				   		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
							<display:column titleKey="participant.transaction.payout.PAYOUT_TRANSACTION_ID" headerClass="crud-table-header-row" class="crud-content-link left-align" sortable="true" sortProperty="id">
				              <c:choose>
				                <c:when test="${payout.promotion.awardType.code eq 'merchandise'}">
                                  <c:out value="${payout.id}"/>
				                </c:when>
				                <c:otherwise>								
								<%  temp = (Journal)pageContext.getAttribute("payout");
										parameterMap.put( "transactionId", temp.getId() );
										Map clientStateMap2 = ClientStateUtils.getClientStateMap( request );
										parameterMap.put( "userId", ClientStateUtils.getParameterValue( request, clientStateMap2, "userId" ) );
										parameterMap.put( "promotionId", ClientStateUtils.getParameterValue( request, clientStateMap2, "promotionId" ) );
										parameterMap.put( "promotionType", ClientStateUtils.getParameterValue( request, clientStateMap2, "promotionType" ) );
										parameterMap.put( "startDate", ClientStateUtils.getParameterValue( request, clientStateMap2, "startDate" ) );
										parameterMap.put( "endDate", ClientStateUtils.getParameterValue( request, clientStateMap2, "endDate" ) );
										parameterMap.put( "open", ClientStateUtils.getParameterValue( request, clientStateMap2, "open" ) );
										parameterMap.put( "claimId", ClientStateUtils.getParameterValue( request, clientStateMap2, "claimId" ) );
										parameterMap.put( "claimNumber", ClientStateUtils.getParameterValue( request, clientStateMap2, "claimNumber" ) );
										parameterMap.put( "firstName", ClientStateUtils.getParameterValue( request, clientStateMap2, "firstName" ) );
										parameterMap.put( "lastName", ClientStateUtils.getParameterValue( request, clientStateMap2, "lastName" ) );
										parameterMap.put( "middleName", ClientStateUtils.getParameterValue( request, clientStateMap2, "middleName" ) );
										pageContext.setAttribute("payoutTransactionDetailUrl", ClientStateUtils.generateEncodedLink( "", "payoutTransactionDetailsDisplay.do?method=prepareUpdate&callingScreen=payouts", parameterMap, true ) );
								%>
				<c:set var="tmp" value="javascript:setActionDispatchAndSubmit('${payoutTransactionDetailUrl}','prepareUpdate');"/>
                <a href="<c:out value='${tmp}'/>" class="crud-content-link">
                  <c:out value="${payout.id}"/>
                </a>
                               </c:otherwise>
                             </c:choose>
				
			  </display:column>				  
			  <display:column titleKey="participant.transaction.payout.STATUS" property="journalStatusType.code" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>							  
			  <display:column titleKey="participant.transaction.payout.DEPOSIT_DATE" headerClass="crud-table-header-row" class="crud-content right-align" sortable="true">	
			  	<fmt:formatDate value="${payout.transactionDate}" pattern="${JstlDatePattern}" />
			  </display:column>			  
			  <display:column titleKey="participant.transaction.payout.AMOUNT" property="transactionAmount" headerClass="crud-table-header-row" class="crud-content right-align" sortable="true"/>				  
			  <display:column titleKey="participant.transaction.payout.ACTIVITY_ID"  headerClass="crud-table-header-row" sortable="true">	
			  	<table>
            		<c:forEach items="${payout.activityJournals}" var="activityJournal">
            		 <tr>
            		  <td class="crud-content left-align">
                  		<c:out value="${activityJournal.activity.id}"/>
            		 </td>
            		</tr>
            	  </c:forEach>
            	</table>
              </display:column>	                        
            </display:table>
            
        </td>
     </tr>  
     
      <%--BUTTON ROWS --%>
    <tr class="form-buttonrow">
     <td>
      <table width="100%">
        <tr>
          <td align="center">
						<%  Map<Object, Object> paramMap = new HashMap<>();
							String currentMode = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "mode" );
								paramMap.put( "userId", ClientStateUtils.getParameterValue( request, clientStateMap, "userId" ) );
								paramMap.put( "promotionId", ClientStateUtils.getParameterValue( request, clientStateMap, "promotionId" ) );
								paramMap.put( "promotionType", ClientStateUtils.getParameterValue( request, clientStateMap, "promotionType" ) );
								paramMap.put( "startDate", ClientStateUtils.getParameterValue( request, clientStateMap, "startDate" ) );
								paramMap.put( "endDate", ClientStateUtils.getParameterValue( request, clientStateMap, "endDate" ) );
								paramMap.put( "livePromotionId", ClientStateUtils.getParameterValue( request, clientStateMap, "livePromotionId" ) );
								paramMap.put( "livePromotionType", ClientStateUtils.getParameterValue( request, clientStateMap, "livePromotionType" ) );
								paramMap.put( "liveStartDate", ClientStateUtils.getParameterValue( request, clientStateMap, "liveStartDate" ) );
								paramMap.put( "liveEndDate", ClientStateUtils.getParameterValue( request, clientStateMap, "liveEndDate" ) );
								paramMap.put( "open", ClientStateUtils.getParameterValue( request, clientStateMap, "open" ) );
								paramMap.put( "mode", ClientStateUtils.getParameterValue( request, clientStateMap, "mode" ) );
								pageContext.setAttribute("returnToHistoryTransactionsUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/claim/transactionHistory.do?mode="+currentMode+"&open="+request.getParameter("open"), paramMap ) );
						%>
            <html:submit titleKey="participant.transaction.payout.BACK_TO_TRANSACTION_LIST"  styleClass="content-buttonstyle"
               onclick="setActionAndDispatch('${returnToHistoryTransactionsUrl}','showActivity')">
			     		<cms:contentText key="BACK_TO_TRANSACTION_LIST" code="participant.transaction.payout"/>
    		</html:submit>
          </td>          
        </tr>
      </table>
    </td>
  </tr>
</table>
</html:form>