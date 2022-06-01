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
<html:form styleId="contentForm" action="budgetMaintainCreate">
	<html:hidden property="method" value="create"/>
	<html:hidden property="budgetName"/>
	<html:hidden property="paxOrNode"/>
	<beacon:client-state>
		<beacon:client-state-entry name="budgetMasterId" value="${budgetForm.budgetMasterId}"/>
		<beacon:client-state-entry name="budgetSegmentId" value="${budgetForm.budgetSegmentId}"/>
		<beacon:client-state-entry name="budgetSegmentName" value="${budgetForm.budgetSegmentName}"/>
		<beacon:client-state-entry name="userId" value="${budgetForm.userId}"/>
		<beacon:client-state-entry name="nodeId" value="${budgetForm.nodeId}"/>
		<beacon:client-state-entry name="ownerName" value="${budgetForm.ownerName}"/>
  </beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
  	  <span class="headline"><cms:contentText key="ADD_HEADER" code="admin.budget.details"/></span>
  	  <br/>
  	  <span class="subheadline"><c:out value="${budgetForm.budgetName}"/></span>
	  <%--INSTRUCTIONS--%>
	  <br/><br/>
      <span class="content-instruction">
	      <cms:contentText key="ADD_INFO" code="admin.budget.details"/>  	
	  </span>
      <br/><br/>
      <%--END INSTRUCTIONS--%>

	  <cms:errors/>

	  <table>
		<tr class="form-row-spacer">				  
		  <beacon:label property="budgetSegmentName" required="true">
  			<cms:contentText key="BUDGET_SEGMENT_DISPLAY_NAME" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field">
				<c:out value="${budgetForm.budgetSegmentName}"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	
			  	
		<tr class="form-row-spacer">				  
		  <beacon:label property="owner" required="true">
  			<cms:contentText key="OWNER" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field-review">
			<c:out value="${budgetForm.ownerName}"/>&nbsp;&nbsp;
			<%  Map clientStateParameterMap = new HashMap();
					BudgetForm temp = (BudgetForm)request.getAttribute("budgetForm");
			%>
			<c:if test="${budgetForm.paxOrNode=='NODE'}">
				<%  clientStateParameterMap.put( "nodeId", temp.getNodeId() );
						clientStateParameterMap.put( "nodeName", temp.getOwnerName() );
						clientStateParameterMap.put("budgetMasterId", temp.getBudgetMasterId());
						clientStateParameterMap.put( "budgetMasterName", temp.getBudgetName() );
						clientStateParameterMap.put( "budgetSegmentId", temp.getBudgetSegmentId() );
						clientStateParameterMap.put( "budgetSegmentName", temp.getBudgetSegmentName() );
						pageContext.setAttribute("returnUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/hierarchy/nodeLookup.do?returnActionUrl=/admin/budgetDisplay.do?method=returnNodeLookupAdd", clientStateParameterMap ) );
				%>
				<a class="content-link" href="<c:out value ="${returnUrl}"/>">
					<cms:contentText key="LOOKUP_NODE" code="admin.budget.details"/>
				</a>
			</c:if>
			<c:if test="${budgetForm.paxOrNode=='PAX'}">
				<a class="content-link" href="javascript: maintainBudget('preparePaxLookup','budgetDisplay.do');">
					<cms:contentText key="LOOKUP_PARTICIPANT" code="admin.budget.details"/>
				</a>
			</c:if>		
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	
			
		<tr class="form-row-spacer">				  
		  <beacon:label property="budgetStatusType" required="true">
  			<cms:contentText key="BUDGET_STATUS" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field">
			<html:select property="budgetStatusType" styleClass="content-field" >
			  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
			  <html:options collection="budgetStatusList" property="code" labelProperty="name"  />
			</html:select>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>		

		<tr class="form-row-spacer">				  
		  <beacon:label property="originalValue" required="true">
  			<cms:contentText key="AMOUNT" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field">
			<html:text property="originalValue" size="8" maxlength="8" styleClass="content-field"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	
	
		<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
	          	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
							  <cms:contentText code="system.button" key="SAVE" />
							</html:submit>
            </beacon:authorize>
						<%  Map parameterMap = new HashMap();
								parameterMap.put( "id", temp.getBudgetMasterId() );
								parameterMap.put( "budgetSegmentId", temp.getBudgetSegmentId() );
								pageContext.setAttribute("urlMaster", ClientStateUtils.generateEncodedLink( "", "budgetMasterDisplay.do", parameterMap ) );
						%>
						<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="callUrl('${urlMaster}')">
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
