<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.JournalStatusType"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--

//-->
</script>
<html:form styleId="contentForm" action="payoutTransactionDetailsMaintain">
	<html:hidden property="method" />
	<html:hidden property="promotionName" />
	<html:hidden property="open" />
	<html:hidden property="mode" />
	<html:hidden property="firstName" />
	<html:hidden property="lastName" />
	<html:hidden property="middleName" />
	<html:hidden property="callingScreen" />
	<html:hidden property="dateSubmitted" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${payoutTransactionDetailsForm.userId}"/>
	 	<beacon:client-state-entry name="livePromotionId" value="${payoutTransactionDetailsForm.livePromotionId}"/>
	 	<beacon:client-state-entry name="livePromotionType" value="${payoutTransactionDetailsForm.livePromotionType}"/>
	 	<beacon:client-state-entry name="liveStartDate" value="${payoutTransactionDetailsForm.liveStartDate}"/>
	 	<beacon:client-state-entry name="liveEndDate" value="${payoutTransactionDetailsForm.liveEndDate}"/>
	 	<beacon:client-state-entry name="promotionId" value="${payoutTransactionDetailsForm.promotionId}"/>
	 	<beacon:client-state-entry name="promotionType" value="${payoutTransactionDetailsForm.promotionType}"/>
	 	<beacon:client-state-entry name="startDate" value="${payoutTransactionDetailsForm.startDate}"/>
	 	<beacon:client-state-entry name="endDate" value="${payoutTransactionDetailsForm.endDate}"/>
	 	<beacon:client-state-entry name="transactionId" value="${payoutTransactionDetailsForm.transactionId}"/>
	 	<beacon:client-state-entry name="claimId" value="${payoutTransactionDetailsForm.claimId}"/>
	 	<beacon:client-state-entry name="claimGroupId" value="${payoutTransactionDetailsForm.claimGroupId}"/>
	 	<beacon:client-state-entry name="claimNumber" value="${payoutTransactionDetailsForm.claimNumber}"/>
	</beacon:client-state>

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PAYOUT_TRANSACTION_DETAIL" code="participant.payout.transaction.detail"/>
        </span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline">
			<c:out value="${participant.titleType.name}" />
		  	<c:out value="${participant.firstName}" />
			<c:out value="${participant.middleName}" />
			<c:out value="${participant.lastName}" />
			<c:out value="${participant.suffixType.name}" />			               
        </span>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="participant.payout.transaction.detail"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <%-- Start Input Example  --%>
        <table>
          <tr class="form-row-spacer">				  
            <beacon:label property="journal.id">
              <cms:contentText key="TRANSACTION_ID" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <c:out value="${journal.id}"/>	
            </td>
          </tr>          
          <tr class="form-blank-row">
            <td></td>
          </tr>	  
                
          <tr class="form-row-spacer">				  
            <beacon:label property="promotionName">
              <cms:contentText key="PROMOTION" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <c:out value="${payoutTransactionDetailsForm.promotionName}"/>	    		
            </td>
          </tr>          
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="journal.journalStatusType.name">
              <cms:contentText key="STATUS" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <c:out value="${journal.journalStatusType.name}"/>		
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr>	  
          
          <tr class="form-row-spacer">				  
            <beacon:label property="journal.transactionDate">
              <cms:contentText key="DATE" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <fmt:formatDate value="${journal.transactionDate}" pattern="${JstlDatePattern}" />	
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr>	                             
		
          <tr class="form-row-spacer">				  
            <beacon:label property="journal.transactionAmount">
              <cms:contentText key="AMOUNT" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <c:out value="${journal.transactionAmount}"/>	
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">				  
            <beacon:label property="journal.transactionDescription">
              <cms:contentText key="DESCRIPTION" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <c:out value="${journal.transactionDescription}"/>
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr>          	              
				
         <c:if test="${ !empty actionList}">
          <tr class="form-row-spacer">				  
            <beacon:label property="actionCode" required="true">
              <cms:contentText key="ACTION" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
             <html:select property="actionCode" styleClass="content-field" onchange="setActionDispatchAndSubmit('payoutTransactionDetailsDisplay.do','changeActionCode')">
			  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
			  <html:options collection="actionList" property="code" labelProperty="name"  />
			 </html:select>
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr> 
    
          <c:if test="${payoutTransactionDetailsForm.actionCode == 'deny'}">
	       <tr class="form-row-spacer">				  
	        <beacon:label property="reasonCode" required="true">
	          <cms:contentText key="REASON_CODE" code="participant.payout.transaction.detail"/>
	        </beacon:label>	
	        <td class="content-field">
	         <html:select property="reasonCode" styleClass="content-field" >
			  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
			  <html:options collection="reasonCodeList" property="code" labelProperty="name"  />
			 </html:select>
	        </td>
	       </tr>
	       <tr class="form-blank-row">
	        <td></td>
		 </c:if>
		
         <tr class="form-row-spacer">				  
            <beacon:label property="comments" required="true">
              <cms:contentText key="COMMENTS" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
             <html:text property="comments" size="30" maxlength="60" styleClass="content-field"/>
            </td>
        </tr>         
       </c:if>
     
       <c:if test="${ empty actionList}">
     
      	<c:if test="${journal.journalStatusType.code == 'deny'}">
          <tr class="form-row-spacer">				  
            <beacon:label property="journal.reasonType.name" >
              <cms:contentText key="REASON_CODE" code="participant.payout.transaction.detail"/>
            </beacon:label>	
            <td class="content-field">
              <c:out value="${journal.reasonType.name}"/>	
            </td>
          </tr>      	       		
      	</c:if>
      	      	                   
	    <tr class="form-row-spacer">				  
	      <beacon:label property="journal.comments">
	        <cms:contentText key="COMMENTS" code="participant.payout.transaction.detail"/>
	      </beacon:label>	
	      <td class="content-field">
	        <c:out value="${journal.comments}" escapeXml="false" />
	      </td>
	    </tr>      	

       </c:if>

         <%--BUTTON ROWS ... For Input--%>
         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
            <c:if test="${ !empty actionList}">
            <beacon:authorize ifNotGranted="LOGIN_AS" ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
			 <html:submit styleClass="content-buttonstyle">
			  <cms:contentText code="system.button" key="SUBMIT" />
			 </html:submit>
            </beacon:authorize>
			</c:if>           
            
            <%--  build url based on callingScreen, e.g. transaction, activities --%>
            <% 
            	String callingScreen = (String)request.getParameter("callingScreen");
                if (callingScreen.equals("transactions") || callingScreen.equals("activities") )
                {
                  Map paramMap = new HashMap();
      			  paramMap.put( "mode", request.getAttribute("mode") );      			  
      			  paramMap.put( "userId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "userId" ) );
      			  paramMap.put( "claimGroupId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "claimGroupId" ) );
      			  paramMap.put( "promotionType", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionType" ) );
                  paramMap.put( "startDate", ClientStateUtils.getParameterValue(request, ClientStateUtils.getClientStateMap(request), "startDate") );
      			  paramMap.put( "endDate", ClientStateUtils.getParameterValue(request, ClientStateUtils.getClientStateMap(request), "endDate") );	
      			  pageContext.setAttribute("returnToTransactionUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistory.do?method=showActivity&mode=received&open=closed", paramMap ) );      	
      
                }                
                else if (callingScreen.equals("payouts"))
                {
	              Map paramMap = new HashMap();
	              paramMap.put( "userId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "userId" ) );
	              paramMap.put( "claimId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "claimId" ) );
	              paramMap.put( "claimGroupId", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "claimGroupId" ) );
	              paramMap.put( "promotionType", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "promotionType" ) );
	              paramMap.put( "livePromotionType", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "livePromotionType" ) );            	
	              paramMap.put( "claimNumber", ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap(request), "claimNumber" ) );
	              pageContext.setAttribute("returnToTransactionUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistoryClaimPayouts.do?method=showPayouts", paramMap ) );
                }
            %>
            <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('${returnToTransactionUrl}')">
				<cms:contentText code="system.button" key="CANCEL" />
			</html:cancel>            
           </td>
         </tr>
         <%--END BUTTON ROW--%>
      
        </table>
        <%-- End Inputs  --%>
      </td>
     </tr>        
   </table>
</html:form>



