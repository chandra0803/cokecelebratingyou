<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.budget.BudgetForm" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>
<%@ include file="/include/paxSearch.jspf"%>

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
	<html:hidden property="version"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
    <html:hidden property="selectedTransferId" styleId="selectedTransferId"/>
	<html:hidden styleId="transferBudgetOriginalAmount" property="transferBudgetOriginalAmount"/>	
	<html:hidden styleId="transferBudgetCurrentAmount" property="transferBudgetCurrentAmount"/>	    
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
		<beacon:client-state-entry name="budgetType" value="${budgetForm.budgetType}"/>
		<beacon:client-state-entry name="selectedParticipantId" value="${budgetForm.selectedParticipantId}"/>
		<beacon:client-state-entry name="selectedNodeId" value="${budgetForm.selectedNodeId}"/>
		<beacon:client-state-entry name="budgetsToShow" value="${budgetForm.budgetsToShow}"/>
		<beacon:client-state-entry name="searchQuery" value="${budgetForm.searchQuery}"/>
  </beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
  	  <span class="headline"><cms:contentText key="UPDATE_HEADER" code="admin.budget.details"/></span>
  	  <br/>
  	  <span class="subheadline"><c:out value="${budgetForm.budgetName}"/></span>
	  <%--INSTRUCTIONS--%>
	  <br/><br/>
      <span class="content-instruction">
	      <cms:contentText key="UPDATE_INFO" code="admin.budget.details"/>  	
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
			<c:out value="${budgetForm.ownerName}"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>		
		
		<tr class="form-row-spacer">				  
		  <beacon:label property="budgetType" required="true">
  			<cms:contentText key="BUDGET_TYPE" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field-review">
			<c:out value="${budgetForm.budgetTypeForDisplay}"/>&nbsp;<cms:contentText key="BUDGET" code="admin.budget.details"/>
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
		  <beacon:label required="false">
			<cms:contentText key="ORIG_AMOUNT" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field-review">
			<c:out value="${budgetForm.originalValue}"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	
	
		<tr class="form-row-spacer">				  
		  <beacon:label required="false">
			<cms:contentText key="AVAILABLE_AMOUNT" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field-review">
			<c:out value="${budgetForm.availableValue}"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	

		<tr class="form-row-spacer">				  
		  <beacon:label required="false">
			<cms:contentText key="UPDATE_BUDGET_AMOUNT" code="admin.budget.details"/>
		  </beacon:label>	
		  <td class="content-field">
			  <html:radio property="updateMethod" styleId="noChange" value="noChange" onclick="updateMethodChanged()"/>&nbsp;<cms:contentText key="NO_CHANGE" code="admin.budget.details"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	

		<tr class="form-row-spacer">				  
		  <td/>
		  <td/>
		  <td class="content-field">
  		    <html:radio property="updateMethod" styleId="addToBudget" value="addToBudget" onclick="updateMethodChanged()"/>&nbsp;<cms:contentText key="ADD_TO_BUDGET" code="admin.budget.details"/>
		  </td>
		</tr>
		<tr class="form-row-spacer">				  
		  <td/>
		  <td/>
		  <td class="content-field">
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="AMOUNT_TO_ADD" code="admin.budget.details"/>
		  </td>
		  <td class="content-field">
			<html:text property="qtyToAdd" styleId="qtyToAdd" size="8" maxlength="8" styleClass="content-field"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>	

		<tr class="form-row-spacer">				  
		  <td/>
		  <td/>
		  <td class="content-field">
  		    <html:radio property="updateMethod" styleId="transfer" value="transfer" onclick="updateMethodChanged()"/>&nbsp;<cms:contentText key="TRANSFER_TO_ANOTHER" code="admin.budget.details"/>
		  </td>
		</tr>
		<tr class="form-row-spacer">				  
		  <td/>
		  <td/>
		  <td class="content-field">
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="AMOUNT_TO_TRANSFER" code="admin.budget.details"/>
		  </td>
		  <td class="content-field">
			<html:text property="amountToTransfer" styleId="amountToTransfer" size="8" maxlength="8" styleClass="content-field" onchange="computeNewTransferBudgetAmount();"/>
		  </td>
		</tr>
		<c:choose>
		  <c:when test="${(budgetForm.budgetType eq 'pax')}">
		    <input type="hidden" value="lastName" name="searchBy" id="searchBy"/>
		  </c:when>
		  <c:otherwise>
		    <input type="hidden" value="location" name="searchBy" id="searchBy"/>
		  </c:otherwise>
		</c:choose>		
		<tr class="form-row-spacer">				  
		  <td/>
		  <td/>
		  <td class="content-field">
		    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="RECEIVING_OWNER" code="admin.budget.details"/>
		  </td>
		  <td class="content-field">
		    <div id="autocomplete" class="yui-ac">
			  <html:text property="searchQuery2" styleId="searchQuery"  style="content-field killme" styleClass="content-field"  size="20" onchange="clearTransferInfo();"/>
	          <div id="searchResults" style="z-index:1;width:500px;"></div>
	        </div>
		  </td>
		  <td/>
		</tr>
		<tr class="form-row-spacer">				  
		  <td/>
		  <td/>
		  <td/>
		  <td colspan="2">
		      <table  id="transferInfo" style="display:none">
		        <tr id="existingOwnerRowId" style="display:none">
		          <td class="content-field" colspan="2"><cms:contentText key="EXISTING_OWNER" code="admin.budget.details"/></td>
		        </tr>
		        <tr id="newOwnerRowId" style="display:none">
		          <td class="content-field" colspan="2"><cms:contentText key="NEW_OWNER" code="admin.budget.details"/></td>
		        </tr>
		        <tr id="currentAmountRowId">
		          <td class="content-field" width="150px"><cms:contentText key="CURRENT_BUDGET_AMOUNT" code="admin.budget.details"/></td>
		          <td class="content-field"><div id="currentAmountValue"/></td>
		        </tr>
		        <tr id="newAmountRowId">
		          <td class="content-field"><cms:contentText key="NEW_BUDGET_AMOUNT" code="admin.budget.details"/></td>
		          <td class="content-field"><span id="newAmountValue"/></td>
		        </tr>
		      </table>
		  </td>
		</tr>		
		
		<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">
                <html:submit styleClass="content-buttonstyle" onclick="setDispatch('prepareReview')">
		  			<cms:contentText key="CONTINUE" code="system.button"  />
				</html:submit>
				<%  Map clientStateParameterMap = new HashMap();
						BudgetForm temp = (BudgetForm)request.getAttribute("budgetForm");
						clientStateParameterMap.put( "id", temp.getBudgetMasterId() );
						clientStateParameterMap.put( "budgetSegmentId", temp.getBudgetSegmentId() );
						clientStateParameterMap.put( "budgetType", temp.getBudgetType() );
						clientStateParameterMap.put( "selectedParticipantId", temp.getSelectedParticipantId() );
						clientStateParameterMap.put( "selectedNodeId", temp.getSelectedNodeId() );
						clientStateParameterMap.put( "budgetsToShow", temp.getBudgetsToShow() );
						clientStateParameterMap.put( "searchQuery", temp.getSearchQuery() );
						
						pageContext.setAttribute("encodedUrl", ClientStateUtils.generateEncodedLink( "", "budgetMasterDisplay.do", clientStateParameterMap ) );
				%>
				<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="callUrl('${encodedUrl}')">
					<cms:contentText key="CANCEL" code="system.button"  />
				</html:button>
            </td>
      	  </tr>  	  
	    </table>
      </td>
    </tr>				
</table>
</html:form>

<script type="text/javascript">
  var searchParams = '&transfer=true&budgetSegmentId=' + <c:out value="${budgetForm.budgetSegmentId}"/> + '&budgetId=' + <c:out value="${budgetForm.budgetId}"/>;
  <c:choose>
    <c:when test="${(budgetForm.budgetType eq 'pax')}">	
      YAHOO.example.ACXml = paxSearch_instantiate(
          '<%=RequestUtils.getBaseURI(request)%>/admin/budgetParticipantSearch.do','','',searchParams, false);
    </c:when>
    <c:otherwise>
      YAHOO.example.ACXml = paxSearch_instantiate(
          '<%=RequestUtils.getBaseURI(request)%>/admin/budgetParticipantSearch.do','','',searchParams, true);
    </c:otherwise>
  </c:choose>

  function paxSearch_selectPax( paxId, nodeId, paxDisplayName ) {
   document.getElementById('searchQuery').value = paxDisplayName;
   document.getElementById('selectedTransferId') .value=paxId;
   checkTransferBudget();
  }  
  
  // Instantiate an XHR DataSource for QUERY of transfer budget info
  this.budgetQuery = new YAHOO.widget.DS_XHR( '<%=RequestUtils.getBaseURI(request)%>/admin/budgetSearch.do', ["result","key","originalvalue","currentvalue"]);
  this.budgetQuery.responseType = YAHOO.widget.DS_XHR.TYPE_XML; // use XML
    //This function adds search by criteria
  this.budgetQueryResposeHandler = function( sQuery, aResults, oSelf ) {
      var existingOwnerRow = document.getElementById('existingOwnerRowId');
      var newOwnerRow = document.getElementById('newOwnerRowId');
      var currentAmountRow = document.getElementById('currentAmountRowId');
      var newAmountRow = document.getElementById('newAmountRowId');
      var transferInfo = document.getElementById('transferInfo');
      var currentAmountValueDiv = document.getElementById('currentAmountValue');
      var transferBudgetOriginalAmount = document.getElementById('transferBudgetOriginalAmount');
      var transferBudgetCurrentAmount = document.getElementById('transferBudgetCurrentAmount');
    if ( aResults.length == 1 ) {
      transferBudgetOriginalAmount.value = aResults[0][1];
      transferBudgetCurrentAmount.value = aResults[0][2];
    } else
    {
      transferBudgetOriginalAmount.value = 0;
      transferBudgetCurrentAmount.value = 0;
    }
    computeNewTransferBudgetAmount();
  }

  function clearTransferInfo()
  {
      var transferInfo = document.getElementById('transferInfo');
      transferInfo.style.display="none";
      document.getElementById('selectedTransferId') .value='';
  }
  
  function computeNewTransferBudgetAmount()
  {      
    if (document.getElementById('transfer'))
    {
      if (document.getElementById('transfer').checked == true)
      {
        var transferId = document.getElementById('selectedTransferId').value;
        if (transferId && transferId != '')
        {
	        var existingOwnerRow = document.getElementById('existingOwnerRowId');
	        var newOwnerRow = document.getElementById('newOwnerRowId');
	        var currentAmountRow = document.getElementById('currentAmountRowId');
	        var newAmountRow = document.getElementById('newAmountRowId');
	        var transferInfo = document.getElementById('transferInfo');
	        var currentAmountValueDiv = document.getElementById('currentAmountValue');
	        var originalAmountValue = parseInt(document.getElementById('transferBudgetOriginalAmount').value,10);
	        var currentAmountValue = parseInt(document.getElementById('transferBudgetCurrentAmount').value,10);
	        currentAmountValueDiv.innerHTML = currentAmountValue;
	        var amountToTransfer = parseInt('0'+document.getElementById('amountToTransfer').value,10);
	        var newAmount = "";
	        if (!isNaN(currentAmountValue) && !isNaN(amountToTransfer))
	        {
	          newAmount = currentAmountValue + amountToTransfer;
	        } 
	        document.getElementById('newAmountValue').innerHTML = newAmount;
	        if (originalAmountValue && originalAmountValue != 0)
	        {
	           existingOwnerRow.style.display="";
	           newOwnerRow.style.display="none";        
	           currentAmountRow.style.display="";
	           newAmountRow.style.display="";
	           transferInfo.style.display="";
	        } else
	        {
	           existingOwnerRow.style.display="none";
	           newOwnerRow.style.display="";
	           currentAmountRow.style.display="none";
	           newAmountRow.style.display="none";
	           transferInfo.style.display="";        
	        }
	     }
	     else
	     {
	       clearTransferInfo();
	     }
      }
    }
  }

  function checkTransferBudget() {
    if (document.getElementById('transfer'))
    {
      if (document.getElementById('transfer').checked == true)
      <c:choose>
        <c:when test="${(budgetForm.budgetType eq 'pax')}">
          var criteriaName = 'userBudget';
        </c:when>
        <c:otherwise>
          var criteriaName = 'nodeBudget';
        </c:otherwise>
      </c:choose>
      var searchId = document.getElementById('selectedTransferId') .value;
      if (searchId != 0)
      {
        this.budgetQuery.scriptQueryAppend = 'results=100&doNotSaveToken=true&criteria=' + criteriaName + '&budgetSegmentId=' + <c:out value="${budgetForm.budgetSegmentId}"/>;
        this.budgetQuery.doQuery( this.budgetQueryResposeHandler, searchId, this );
      }
    }
  }
  
  
  function updateMethodChanged() {
    if (document.getElementById('addToBudget'))
    {
      if (document.getElementById('addToBudget').checked == true)
      {
        document.getElementById('qtyToAdd').disabled = '';
      } else
      {
        document.getElementById('qtyToAdd').disabled = 'disabled';
      }
    }
    if (document.getElementById('transfer'))
    {
      if (document.getElementById('transfer').checked == true)
      {
        document.getElementById('amountToTransfer').disabled = '';
        document.getElementById('searchQuery').disabled = '';
        document.getElementById('searchQuery').disabled = '';
      } else
      {
        document.getElementById('amountToTransfer').disabled = 'disabled';
        document.getElementById('searchQuery').disabled = 'disabled';
      }
    }    
    return true;
  }
  </script>  
<script type="text/javascript">  
  YAHOO.util.Event.addListener(window, "load", function(){ YAHOO.util.Dom.addClass(document.body,"bodystyle bi-yui"); updateMethodChanged(); computeNewTransferBudgetAmount(); });
</script>
