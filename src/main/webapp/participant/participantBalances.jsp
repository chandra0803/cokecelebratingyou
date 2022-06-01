<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="participantBalances">
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${participantBalancesForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="participant.balances"/></span>
        <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="participant.balances"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table width="50%">
          <tr>
            <td align="right">        
		        <display:table defaultsort="1" defaultorder="ascending" name="paxBalances" id="balancesFormBean" >
		        <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				  </display:setProperty>
				  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>        
		            <display:column titleKey="participant.balances.AWARD_TYPE" property="awardType" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap"/>
		            <display:column titleKey="participant.balances.TOTAL_EARNED" property="totalEarned" headerClass="crud-table-header-row" class="crud-content right-align top-align nowrap"/>
		            <display:column titleKey="participant.balances.CURRENT_BALANCE"  headerClass="crud-table-header-row" class="crud-content right-align top-align nowrap">
			           <c:choose>
			           	 <c:when test="${balancesFormBean.currentBalance != null}">
			           	 	<c:out value="${balancesFormBean.currentBalance}"/>
			           	 </c:when>
			           	 <c:otherwise>
			           	 	<cms:contentText key="NOT_AVAILABLE" code="participant.balances"/>
			           	 </c:otherwise>
			           </c:choose> 
		            </display:column>
		        </display:table>
		    </td>
		 </tr> 
		 
		 <tr>
            <td>
              <table width="100%">
              <tr>
              	<td align="center">
                  <html:button styleClass="content-buttonstyle" property="Back" onclick="setActionDispatchAndSubmit('participantDisplay.do', 'display');">
						<cms:contentText code="participant.balances" key="BACK_TO_OVERVIEW" />
			      </html:button>
                </td>
              </tr>
              </table>
            </td>
         </tr>
	   </table>        
     </td>
  	</tr>
 </table>
</html:form>