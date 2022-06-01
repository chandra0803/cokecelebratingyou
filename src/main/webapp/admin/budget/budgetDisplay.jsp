<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.budget.BudgetForm" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.utils.UserManager"%>
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
<%  Map parameterMap = new HashMap();
    BudgetForm temp = (BudgetForm)request.getAttribute("budgetForm");
    String originalvalue = temp.getOriginalValue();
	parameterMap.put( "id", temp.getBudgetMasterId() );
	parameterMap.put( "budgetSegmentId", temp.getBudgetSegmentId() );
	parameterMap.put( "selectedParticipantId", temp.getSelectedParticipantId() );
	parameterMap.put( "selectedNodeId", temp.getSelectedNodeId() );
	parameterMap.put( "budgetsToShow", temp.getBudgetsToShow() );
	parameterMap.put( "searchQuery", temp.getSearchQuery() );
	parameterMap.put( "budgetType", temp.getBudgetType() );
	parameterMap.put( "budgetId", temp.getBudgetId( ) );
	pageContext.setAttribute("urlMaster", ClientStateUtils.generateEncodedLink( "", "budgetMasterDisplay.do", parameterMap ) );
	pageContext.setAttribute("urlMasterEdit", ClientStateUtils.generateEncodedLink( "", "budgetDisplay.do?method=prepareUpdateByPax", parameterMap, true ) );
%>
<html:form styleId="contentForm" action="budgetDisplay">
	<html:hidden property="method" value="prepareDisplay"/>
	<html:hidden property="budgetName"/>
	<html:hidden property="paxOrNode"/>
	<html:hidden property="version"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
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
  </beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
    	<span class="headline"><cms:contentText key="VIEW_HEADER" code="admin.budget.details"/></span>
		<br/>
		 <span class="subheadline"><c:out value="${budgetForm.budgetName}"/></span>
		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="VIEW_INFO" code="admin.budget.details"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	  
    <table>
		<tr class="form-row-spacer">	
		  <td>			  
  		  	<cms:contentText key="BUDGET_SEGMENT_DISPLAY_NAME" code="admin.budget.details"/>
  		  	</td>
		  <td class="content-field">
				<c:out value="${budgetForm.budgetSegmentName}"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
	
		<tr class="form-row-spacer">
			<td class="content-field-label" nowrap>
				<cms:contentText key="OWNER" code="admin.budget.details"/>
			</td>						
			<td class="content-field-review">
				<c:out value="${budgetForm.ownerName}"/>
				&nbsp;&nbsp;
				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
					<c:if test="${budgetForm.paxOrNode=='NODE'}">
	 			  	<a class="content-link" href="javascript: maintainBudget('prepareUpdateByNode','budgetDisplay.do');">
						<cms:contentText key="EDIT_LINK" code="admin.budget.details"/>
				  	</a>
					</c:if>
					<c:if test="${budgetForm.paxOrNode=='PAX'}">
				  	<a class="content-link" href="javascript:setActionDispatchAndSubmit('<c:out value="${urlMasterEdit}"/>','prepareUpdateByPax');">
						<cms:contentText key="EDIT_LINK" code="admin.budget.details"/>
				  	</a>			
					</c:if>
				</beacon:authorize>
		  </td>
		</tr>	
	
	 	<tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="BUDGET_STATUS" code="admin.budget.details"/>
			</td>
			<td class="content-field-review">
				<c:out value="${budgetForm.budgetStatusTypeDesc}"/>			
		    </td>
		</tr>	
	
		<tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="ORIG_AMOUNT" code="admin.budget.details"/>
			</td>
			<td class="content-field-review">
			<c:out value="${budgetForm.originalValue}"/>
		    </td>
		</tr>	
		
		<tr class="form-row-spacer">
			<td class="content-field-label">
				<cms:contentText key="AVAILABLE_AMOUNT" code="admin.budget.details"/>
			</td>
			<td class="content-field-review">
			<c:out value="${budgetForm.availableValue}"/>
		    </td>
		</tr>	
		
	<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
		<tr class="form-buttonrow">
			<td></td>
			<td align="left">
				<html:button property="backToBudgetMaster" styleClass="content-buttonstyle" onclick="callUrl('${urlMaster}')">
			  	<cms:contentText code="admin.budget.details" key="BACK_TO_BUDGET_BTN" />
				</html:button>
			</td>
		</tr>
	<%--END BUTTON ROW--%>
	</table>
	</td>
  </tr>	
</table>
</html:form>