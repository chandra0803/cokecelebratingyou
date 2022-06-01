<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.budget.BudgetForm" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--
function maintainBudget( method, action )
{
	document.budgetForm.method.value=method;	
	document.budgetForm.action = action;
	document.budgetForm.submit();
}
function callUrl( urlToCall )
{
	window.location=urlToCall;
}
//-->
</script>
<html:form styleId="contentForm" action="budgetMaintainUpdate">
	<html:hidden property="method"/>
	<html:hidden property="budgetName"/>
	<html:hidden property="paxOrNode"/>
	<html:hidden property="budgetStatusType"/>
	<html:hidden property="budgetStatusTypeDesc"/>
	<html:hidden property="version"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
	<html:hidden property="budgetType"/>
	<html:hidden property="budgetsToShow"/>
	<html:hidden property="searchQuery"/>	
	<html:hidden property="searchQuery2"/>	
	<html:hidden property="updateMethod"/>	
	<html:hidden property="amountToTransfer"/>	
	<html:hidden property="transferBudgetOriginalAmount"/>	
	<html:hidden property="transferBudgetCurrentAmount"/>	
	<beacon:client-state>
		<beacon:client-state-entry name="budgetMasterId" value="${budgetForm.budgetMasterId}"/>
		<beacon:client-state-entry name="budgetSegmentId" value="${budgetForm.budgetSegmentId}"/>
		<beacon:client-state-entry name="budgetSegmentName" value="${budgetForm.budgetSegmentName}"/>		
		<beacon:client-state-entry name="budgetId" value="${budgetForm.budgetId}"/>
		<beacon:client-state-entry name="userId" value="${budgetForm.userId}"/>
		<beacon:client-state-entry name="nodeId" value="${budgetForm.nodeId}"/>
		<beacon:client-state-entry name="ownerName" value="${budgetForm.ownerName}"/>
		<beacon:client-state-entry name="originalValue" value="${budgetForm.originalValue}"/>
		<beacon:client-state-entry name="availableValue" value="${budgetForm.availableValue}"/>
		<beacon:client-state-entry name="qtyToAdd" value="${budgetForm.qtyToAdd}"/>
		<beacon:client-state-entry name="calculatedOriginalValue" value="${budgetForm.calculatedOriginalValue}"/>
		<beacon:client-state-entry name="calculatedAvailableValue" value="${budgetForm.calculatedAvailableValue}"/>
		<beacon:client-state-entry name="selectedParticipantId" value="${budgetForm.selectedParticipantId}"/>
		<beacon:client-state-entry name="selectedNodeId" value="${budgetForm.selectedNodeId}"/>
		<beacon:client-state-entry name="selectedTransferId" value="${budgetForm.selectedTransferId}"/>
  </beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
    	<span class="headline"><cms:contentText key="REVIEW_HEADER" code="admin.budget.details"/></span>
		<br/>
		 <span class="subheadline"><c:out value="${budgetForm.budgetName}"/></span>
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="REVIEW_INFO" code="admin.budget.details"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	  
    <table>
	
		<tr class="form-row-spacer">
		  	<beacon:label property="budgetSegmentName" required="true">
  				<cms:contentText key="BUDGET_SEGMENT_DISPLAY_NAME" code="admin.budget.details"/>
		  	</beacon:label>	
		  	<td colspan="3" class="content-field-review">
				<c:out value="${budgetForm.budgetSegmentName}"/>
		  	</td>
		</tr>
		<tr class="form-row-spacer">
			<beacon:label property="owner" required="true">
				<cms:contentText key="OWNER" code="admin.budget.details"/>
		  	</beacon:label>				
			<td colspan="3" class="content-field-review">
				<c:out value="${budgetForm.ownerName}"/>	
		    </td>
		 </tr>	  
		 <tr class="form-row-spacer">
			<beacon:label required="true">
				<cms:contentText key="BUDGET_STATUS" code="admin.budget.details"/>
		  	</beacon:label>				
			<td colspan="3" class="content-field-review">
				<c:out value="${budgetForm.budgetStatusTypeDesc}"/>	
		    </td>
		 </tr>	
		 <tr class="form-row-spacer">
			<td colspan="2"></td>					
			<td class="content-field-label">
				<cms:contentText key="CURRENT" code="admin.budget.details"/>
		    </td>
		    <td class="content-field-label">
				<cms:contentText key="CALCULATED" code="admin.budget.details"/>
		    </td>
		    <td width="100%"/>
		 </tr>	
		 <tr class="form-row-spacer">
			<beacon:label required="false">
				<cms:contentText key="ORIG_AMOUNT" code="admin.budget.details"/>
		  	</beacon:label>				
			<td class="content-field-review" align="right">
				<c:out value="${budgetForm.originalValue}"/>
		    </td>
		    <td class="content-field-review"  align="right">
				<c:out value="${budgetForm.calculatedOriginalValue}"/>
		    </td>
		    <td/>
		 </tr>	
		<tr class="form-row-spacer">
			<beacon:label required="false">
				<cms:contentText key="AVAILABLE_AMOUNT" code="admin.budget.details"/>
		  	</beacon:label>				
			<td class="content-field-review"  align="right">
				<c:out value="${budgetForm.availableValue}"/>
		    </td>
		    <td class="content-field-review"  align="right">
				<c:out value="${budgetForm.calculatedAvailableValue}"/>
		    </td>
		    <td/>
		 </tr>
		 <c:if test="${budgetForm.updateMethod eq 'transfer'}">
		   <tr class="form-row-spacer">
			  <beacon:label property="owner" required="false">
			    <cms:contentText key="RECEIVER_FULL_NAME" code="admin.budget.details"/>
		  	  </beacon:label>				
			  <td colspan="3" class="content-field-review">
				<c:out value="${budgetForm.receiverFullName}"/>	
		      </td>
		   </tr>	
		 <tr class="form-row-spacer">
			<td colspan="2"></td>					
			<td class="content-field-label">
				<cms:contentText key="CURRENT" code="admin.budget.details"/>
		    </td>
		    <td class="content-field-label">
				<cms:contentText key="CALCULATED" code="admin.budget.details"/>
		    </td>
		    <td width="100%"/>
		 </tr>	
		 <tr class="form-row-spacer">
			<beacon:label required="false">
				<cms:contentText key="ORIG_AMOUNT" code="admin.budget.details"/>
		  	</beacon:label>				
			<td class="content-field-review" align="right">
				<c:out value="${budgetForm.transferBudgetOriginalAmount}"/>
		    </td>
		    <td class="content-field-review"  align="right">
				<c:out value="${budgetForm.transferBudgetOriginalAmount + budgetForm.amountToTransfer}"/>
		    </td>
		    <td/>
		 </tr>	
		<tr class="form-row-spacer">
			<beacon:label required="false">
				<cms:contentText key="AVAILABLE_AMOUNT" code="admin.budget.details"/>
		  	</beacon:label>				
			<td class="content-field-review"  align="right">
				<c:out value="${budgetForm.transferBudgetCurrentAmount}"/>
		    </td>
		    <td class="content-field-review"  align="right">
				<c:out value="${budgetForm.transferBudgetCurrentAmount + budgetForm.amountToTransfer}"/>
		    </td>
		    <td/>
		 </tr>
		 </c:if>
		<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left" colspan="3">
            <beacon:authorize ifNotGranted="LOGIN_AS">
	          	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
				  <cms:contentText code="system.button" key="SUBMIT" />
				</html:submit>
				    </beacon:authorize>
				<html:submit styleClass="content-buttonstyle" onclick="maintainBudget('prepareRedisplay','budgetDisplay.do')">
				  <cms:contentText code="system.button" key="BACK" />
				</html:submit>
				<%  Map parameterMap = new HashMap();
						BudgetForm temp = (BudgetForm)request.getAttribute("budgetForm");
						parameterMap.put( "id", temp.getBudgetMasterId() );
						parameterMap.put( "budgetSegmentId", temp.getBudgetSegmentId() );
						parameterMap.put( "budgetType", temp.getBudgetType() );
						parameterMap.put( "selectedParticipantId", temp.getSelectedParticipantId() );
						parameterMap.put( "selectedNodeId", temp.getSelectedNodeId() );
						parameterMap.put( "budgetsToShow", temp.getBudgetsToShow() );
						parameterMap.put( "searchQuery", temp.getSearchQuery() );						
						pageContext.setAttribute("urlMaster", ClientStateUtils.generateEncodedLink( "", "budgetMasterDisplay.do", parameterMap ) );
				%>
				<html:button property="backToBudgetMaster" styleClass="content-buttonstyle" onclick="callUrl('${urlMaster}')">
				  <cms:contentText code="system.button" key="CANCEL" />
				</html:button>
	        </td>
        </tr>
		<%--END BUTTON ROW--%>
  	  </table>
    </td>
  </tr>	 
</table>
</html:form>



		