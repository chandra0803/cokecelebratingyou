<%--UI REFACTORED--%> 
<%@ include file="/include/taglib.jspf"%>

<%@ page import="com.biperf.core.domain.enums.BudgetOverrideableType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetMasterAwardType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetFinalPayoutRule" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.value.BudgetValueBean" %>
<%@page import="com.biperf.core.ui.budget.BudgetMasterForm"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
  window.location=urlToCall;
}

function selectApprover(method)
{
  document.budgetMasterForm.method.value=method;
  document.budgetMasterForm.action = "budgetMasterMaintainDisplay.do";
  document.budgetMasterForm.submit();
  return false;
}

function setFinalPayoutRuleVisibility()
{
  show_finalpayout = "hidden";  
  
  for (var i=0; i<document.budgetMasterForm.budgetType.length; i++)
  { 
    if (document.budgetMasterForm.budgetType[i].checked)
    {
      if (document.budgetMasterForm.budgetType[i].value=="<%=BudgetType.CENTRAL_BUDGET_TYPE%>")
      {
              show_finalpayout = "visible"; 
              break;
      }     
      break;
    }
  }
  document.getElementById("finalPayoutRule_radio").style.visibility=show_finalpayout;  
  
  // Previously, this page was using only visibility property. It leaves a gaping blank for final payout rule, 
  //  so this will make that large space disappear
  if(show_finalpayout == "visible")
  {
	$("#finalPayoutRule_radio").show();
  }
  else
  {
	$("#finalPayoutRule_radio").hide();
  }
}

function hideLayer(whichLayer)
{
  if (document.getElementById)
  {
    // this is the way the standards work
    if (document.getElementById(whichLayer))
    {
    var style2 = document.getElementById(whichLayer).style;
    style2.display = "none";
    }
  }
  else if (document.all)
  {
    // this is the way old msie versions work
    var style2 = document.all[whichLayer].style;
    style2.display = "none";
  }
  else if (document.layers)
  {
    // this is the way nn4 works
    var style2 = document.layers[whichLayer].style;
    style2.display = "none";
  }
}


function showLayer(whichLayer)
{
  if (document.getElementById)
  {
    // this is the way the standards work
    if (document.getElementById(whichLayer))
    {
        var style2 = document.getElementById(whichLayer).style;
        style2.display = "";
    }
  }
  else if (document.all)
  {
    // this is the way old msie versions work
    var style2 = document.all[whichLayer].style;
    style2.display = "block";
  }
  else if (document.layers)
  {
    // this is the way nn4 works
    var style2 = document.layers[whichLayer].style;
    style2.display = "block";
  }
}

function updateLayerSegmentRemoveOptionDisplay()
{
	  var count = $("input[name='budgetSegmentVBListSize']").val();
	  for(i=0; i<= count; i++){
		  if( i != 0 ){
			  if( i+1 == count ){
					 showLayer('removeBudgetSegment'+i); 
				  }else{
					  hideLayer('removeBudgetSegment'+i);
				  }
	  		}else{
	  			 hideLayer('removeBudgetSegment0');
	  		}
		  };
}

function updateBudgetSegmentLayer()
{
	hideSegmentLayers();

	if ( isCreatingBudgetMaster() ) {
	for (var i=0; i<document.budgetMasterForm.budgetType.length; i++)
	  { 
	    if (document.budgetMasterForm.budgetType[i].checked && document.budgetMasterForm.budgetType[i].value=="<%=BudgetType.PAX_BUDGET_TYPE%>" )
	      {
	    	displayPaxLayers();
	    	break;
	      }     
	    if (document.budgetMasterForm.budgetType[i].checked && document.budgetMasterForm.budgetType[i].value=="<%=BudgetType.NODE_BUDGET_TYPE%>" )
	      {
	    	  displayNodeLayers();
	    	  break;
	      } 
	    if (document.budgetMasterForm.budgetType[i].checked && document.budgetMasterForm.budgetType[i].value=="<%=BudgetType.CENTRAL_BUDGET_TYPE%>" )
	      {
	    	displayCentralLayers();
	    	break;
	      } 	    
	  }
	}
}

function displayPaxLayers()
{	  
	  showLayer("budgetReallocHeaderId");
	  showLayer("budgetReallocEligHeaderId");
	  
	  hideLayer("budgetAmtValHeaderId");
	  hideLayer("origValHeaderId");
	  hideLayer("currentValHeaderId");
	  hideLayer("addOnValHeaderId");
	  
	  var segmentListSize = document.budgetMasterForm.budgetSegmentVBListSize.value;
	  for (var i=0; i<segmentListSize; i++)
	  {
	  	showLayer("budgetReallocId["+i+"]");
	  	showLayer("budgetReallocEligId["+i+"]");
	  
	 	hideLayer("budgetAmtValId["+i+"]");
	  	hideLayer("origValId["+i+"]");
	  	hideLayer("currentValId["+i+"]");
	  	hideLayer("addOnValId["+i+"]");
	  }
}

function displayNodeLayers()
{	  
	  showLayer("budgetReallocHeaderId");
	  
	  hideLayer("budgetReallocEligHeaderId");
	  hideLayer("budgetAmtValHeaderId");
	  hideLayer("origValHeaderId");
	  hideLayer("currentValHeaderId");
	  hideLayer("addOnValHeaderId");
	  
	  var segmentListSize = document.budgetMasterForm.budgetSegmentVBListSize.value;
	  for (var i=0; i<segmentListSize; i++)
	  {
	  	showLayer("budgetReallocId["+i+"]");
	  
	  	hideLayer("budgetReallocEligId["+i+"]");
	 	hideLayer("budgetAmtValId["+i+"]");
	  	hideLayer("origValId["+i+"]");
	  	hideLayer("currentValId["+i+"]");
	  	hideLayer("addOnValId["+i+"]");
	  }
}

function displayCentralLayers()
{
	  hideLayer("budgetReallocHeaderId");
	  hideLayer("budgetReallocEligHeaderId");
	  
	  showLayer("budgetAmtValHeaderId");
	  showLayer("origValHeaderId");
	  showLayer("currentValHeaderId");
	  showLayer("addOnValHeaderId");
	  	
	  var segmentListSize = document.budgetMasterForm.budgetSegmentVBListSize.value;	  	
	  for (var i=0; i<segmentListSize; i++)
	  {
	  	hideLayer("budgetReallocId["+i+"]");
	  	hideLayer("budgetReallocEligId["+i+"]");
	  	
	 	showLayer("budgetAmtValId["+i+"]");
	 	showLayer("origValId["+i+"]");
	 	showLayer("currentValId["+i+"]");
	 	showLayer("addOnValId["+i+"]");
	  }
}

function hideSegmentLayers()
{
	  hideLayer("budgetAmtValHeaderId");
	  hideLayer("origValHeaderId");
	  hideLayer("currentValHeaderId");
	  hideLayer("addOnValHeaderId");
	  
	  var segmentListSize = document.budgetMasterForm.budgetSegmentVBListSize.value;
	  for (var i=0; i<segmentListSize; i++)
	  {
		  hideLayer("budgetAmtValId["+i+"]");
		  hideLayer("origValId["+i+"]");
		  hideLayer("currentValId["+i+"]");
		  hideLayer("addOnValId["+i+"]");
	  }
}

function updateLayersShown()
{
	updateBudgetSegmentLayer();
	updateLayerSegmentRemoveOptionDisplay();
	
	var allowBudgetReallocationFalseObj = document.getElementById("allowBudgetReallocationFalse");
	var allowBudgetReallocationTrueObj = document.getElementById("allowBudgetReallocationTrue");
	hideLayer("budgetReallocationEligTypeOption");
	if (allowBudgetReallocationTrueObj != null && allowBudgetReallocationTrueObj.checked == true )
	{
		if ( isCreatingBudgetMaster() ) {
			  for (var i=0; i<document.budgetMasterForm.budgetType.length; i++)
			  { 
			    if (document.budgetMasterForm.budgetType[i].checked && document.budgetMasterForm.budgetType[i].value=="<%=BudgetType.PAX_BUDGET_TYPE%>" )
			      {
			    	  showLayer("budgetReallocationEligTypeOption");
			    	  break;
			      }     
			  }
		}
		else if( document.budgetMasterForm.budgetType.value == "<%=BudgetType.PAX_BUDGET_TYPE%>" )
		{
			showLayer("budgetReallocationEligTypeOption");
		}
	}
}

function setBudgetPropertyVisibility()
{
  var visibility = isCentralBudget() ? "visible" : "hidden";
  
  if ( isCreatingBudgetMaster() )
  { 
    document.getElementById("finalPayoutRule_radio").style.visibility = visibility;  
    if(visibility == "visible")
    {
      $("#finalPayoutRule_radio").show();
    }
    else
    {
      $("#finalPayoutRule_radio").hide();
    }
  }
  else // editing budget master
  { 
    document.getElementById("budget-original-value").style.visibility = visibility;
    document.getElementById("budget-current-value").style.visibility = visibility;
    document.getElementById("budget-delta").style.visibility = visibility;
    
    var visible = isCentralBudget() ? "visible" : "hidden";
    document.getElementById("finalPayoutRule_radio").style.visibility = visible;    
    if(visible == "visible")
    {
      $("#finalPayoutRule_radio").show();
    }
    else
    {
      $("#finalPayoutRule_radio").hide();
    }
  }
}

function setBudgetOverrideType()
{
  if ( isCreatingBudgetMaster() )
  {
    if ( isCentralBudget() )
    {
      for ( var i = 0; i < document.budgetMasterForm.overrideableType.length; i++ )
      {
        if ( document.budgetMasterForm.overrideableType[i].value == "<%= BudgetOverrideableType.HARD_OVERRIDE %>" )
        {
          document.budgetMasterForm.overrideableType[i].checked = true;
        }
        else
        {
          document.budgetMasterForm.overrideableType[i].checked = false;
        }
      }
    }
  }
}

function isCreatingBudgetMaster()
{
  return document.budgetMasterForm.budgetType.length > 0;
}


function isCentralBudget()
{
  var isCentralBudget = false;

  if ( isCreatingBudgetMaster() )
  {
    // The user is creating a new budget master.
    for ( var i = 0; i < document.budgetMasterForm.budgetType.length; i++ )
    {
      if ( ( document.budgetMasterForm.budgetType[i].value == "<%= BudgetType.CENTRAL_BUDGET_TYPE %>" )
          && document.budgetMasterForm.budgetType[i].checked )
      {
        isCentralBudget = true;
        break;
      }
    }
  }
  else
  {
    // The user is editing an existing budget master.
    if ( document.budgetMasterForm.budgetType.value == "<%= BudgetType.CENTRAL_BUDGET_TYPE %>" )
    {
      isCentralBudget = true;
    }
  }

  return isCentralBudget;
}

function isHardCapBudget()
{
  var isHardCapBudget = false;
 
  for ( var i = 0; i < document.budgetMasterForm.overrideableType.length; i++ )
  {
    if ( ( document.budgetMasterForm.overrideableType[i].value == "<%= BudgetOverrideableType.HARD_OVERRIDE %>" )
        && document.budgetMasterForm.overrideableType[i].checked )
    {
      isHardCapBudget = true;
      break;
    }
  }
  return isHardCapBudget;
}

function addAnotherSegment(method)
{
  $(":input[name='awardType']").removeAttr("disabled");
  document.budgetMasterForm.method.value=method;
  document.budgetMasterForm.action = "budgetMasterMaintainDisplay.do";
  document.budgetMasterForm.submit();
  return false;
}

function removeSegment(method)
{
  $(":input[name='awardType']").removeAttr("disabled");
  document.budgetMasterForm.method.value=method;
  document.budgetMasterForm.action = "budgetMasterMaintainDisplay.do";
  document.budgetMasterForm.submit();
  return false;
}

function clearContents(element) {
	  element.value = '';
	}

// Called when an award type radio button is clicked. 
// If cash is the award type, only central budget type is allowed.
// Changes budget amount labels to USD or points based on type
function onAwardTypeClick()
{
	// If cash is the award type...
	if($("input[name='awardType']:checked").val() == "cash")
	{
		// Select central budget type and disable other radios. Yes click needs to be here twice. No I don't know why.
		$("input[name='budgetType'][value='central']").click();
		$("input[name='budgetType'][value='central']").click();
		$("input[name='budgetType']:not([value='central'])").attr("disabled", "disabled");
		
		// Set amount labels to USD
		$(".amount-type-label").html("(USD)");
	}
	else
	{
		// Enabled budget type buttons
		$("input[name='budgetType']:not([value='central'])").removeAttr("disabled");
		
		// Set amount labels to Points
		$(".amount-type-label").html("(Points)");
	}
}

function onSaveClicked(dispatchMethod)
{
	// Enable the award type radio buttons so that the values actually submit...
	$(":input[name='awardType']").removeAttr("disabled");
	
	setDispatch(dispatchMethod);
}
//-->
</script>

<html:form styleId="contentForm" action="budgetMasterMaintain">
  <html:hidden property="method" value=""/>
  <html:hidden property="cmAssetCode"/>
  <html:hidden property="nameCmKey"/>
  <html:hidden property="hasBudget"/>
  <html:hidden property="budgetSegmentVBListSize"/>
  <c:if test='${budgetMasterForm.id != null && budgetMasterForm.id > 0}'>
      <html:hidden property="budgetType"/>
  </c:if>  
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${budgetMasterForm.id}"/>
  </beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <c:if test='${budgetMasterForm.id == null || budgetMasterForm.id == 0}'>
          <span class="headline"><cms:contentText key="ADD_TITLE" code="admin.budgetmaster.details" /></span>
          <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ADD_TITLE" code="admin.budgetmaster.details"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>       
        </c:if>

        <c:if test='${budgetMasterForm.id != null && budgetMasterForm.id > 0}'>
          <span class="headline"><cms:contentText key="EDIT_TITLE" code="admin.budgetmaster.details" /></span>
        </c:if>

        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="admin.budgetmaster.details"/>
        </span>
        <br/><br/>

        <cms:errors/>
         <%--to fix 18949 inorder to disable the fields if the budget is inactive--%>
                      
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="budgetName" required="true">
              <cms:contentText key="NAME" code="admin.budgetmaster.details"/>
            </beacon:label>
            <td class="content-field">
               <c:if test="${budgetMasterForm.active || (budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
	               <html:text property="budgetName" styleClass="content-field" size="50" maxlength="30" disabled="false"/>
	             </c:if>
	             <%-- to fix 19995 changed the logic from budgetMasterForm.id NOT EQUAL NULL to the below condition --%>
	             <c:if test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0) && !budgetMasterForm.active}">
	               <html:hidden property="budgetName"/>
	               <html:text property="budgetName" styleClass="content-field" size="50" maxlength="30" disabled="true"/>
               </c:if>    
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="active" required="true" styleClass="content-field-label-top">
              <cms:contentText key="BUDGET_ACTIVE" code="admin.budgetmaster.details"/>
            </beacon:label>
            <td class="content-field content-field-label-top">
              <html:radio property="active" value="false"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO" />
              <br>
              <html:radio property="active" value="true"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="active" required="true" styleClass="content-field-label-top">
               <cms:contentText key="ALLOW_ADDL_TRANSFERREES" code="admin.budget.details"/>
            </beacon:label>
            <td class="content-field content-field-label-top">
              <html:radio property="allowAdditionalTransferrees" value="false"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO" />
              <br>
              <html:radio property="allowAdditionalTransferrees" value="true"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />
            </td>
          </tr>
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <c:if test='${!budgetMasterForm.hasBudget &&  ( budgetMasterForm.active || !(budgetMasterForm.id == null || budgetMasterForm.id == 0))}'>
          <tr class="form-blank-row"><td colspan="2"></td><td><table><tr><td><cms:contentText code="admin.budgetmaster.details" key="BUDGET_TYPE_NOTES" /></td></tr></table></td> </tr>
          </c:if>
          
          <tr class="form-row-spacer">
            <beacon:label property="awardType" required="true" styleClass="content-field-label-top">
              <cms:contentText key="AWARD_TYPE" code="admin.budgetmaster.details"/>
            </beacon:label>

             <td class="content-field content-field-label-top">
               <%
                 request.setAttribute( "awardTypes", BudgetMasterAwardType.getList() );
               %>
              <c:if test="${budgetMasterForm.active || (budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
              	<c:if test="${ budgetMasterForm.id == null || budgetMasterForm.id == 0 }">
               	  <c:forEach items="${awardTypes}" var="masterBudgetAwardType">
                	<c:choose>
                	  <c:when test="${ budgetMasterForm.hasBudgetSweepDate == false }">
                	    <html:radio property="awardType" value="${masterBudgetAwardType.code}" disabled="false" onclick="onAwardTypeClick();" />&nbsp;<c:out value="${masterBudgetAwardType.name}"/><br/>
                	  </c:when>
                	  <c:otherwise>
                	    <html:radio property="awardType" value="${masterBudgetAwardType.code}" disabled="true" onclick="onAwardTypeClick();" />&nbsp;<c:out value="${masterBudgetAwardType.name}"/><br/>
                	  </c:otherwise>
                	</c:choose>
                  </c:forEach>
                </c:if>  
                <c:if test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
                  <c:forEach items="${awardTypes}" var="masterBudgetAwardType">
               	    <html:radio property="awardType" value="${masterBudgetAwardType.code}" disabled="true" onclick="onAwardTypeClick();" />&nbsp;<c:out value="${masterBudgetAwardType.name}"/><br/>
                  </c:forEach>
                </c:if>
              </c:if>
              <c:if test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0) && !budgetMasterForm.active}">
	            <c:forEach items="${awardTypes}" var="masterBudgetAwardType">
               	  <html:radio property="awardType" value="${masterBudgetAwardType.code}" disabled="true" onclick="onAwardTypeClick();" />&nbsp;<c:out value="${masterBudgetAwardType.name}"/><br/>
	            </c:forEach>
              </c:if>
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="budgetType" required="true" styleClass="content-field-label-top">
              <cms:contentText key="BUDGET_TYPE" code="admin.budgetmaster.details"/>
            </beacon:label>

            <c:if test='${budgetMasterForm.hasBudget}'>
              <td class="content-field-review content-field-label-top">
                <html:hidden property="budgetType"/>
                <html:hidden property="budgetTypeName"/>
                <c:out value="${budgetMasterForm.budgetTypeName}"/>                    
              </td>
            </c:if>

            <c:if test='${!budgetMasterForm.hasBudget}'>
              <td class="content-field content-field-label-top">
                <%
                  request.setAttribute( "budgetTypes", BudgetType.getList() );
                %>
               <c:if test="${budgetMasterForm.active || (budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
               	<c:if test="${ budgetMasterForm.id == null || budgetMasterForm.id == 0 }">
                	<c:forEach items="${budgetTypes}" var="masterBudgetType">
	                	<c:choose>
	                	<c:when test="${ masterBudgetType.code == 'pax' || budgetMasterForm.hasBudgetSweepDate == false }">
	                	<html:radio property="budgetType" value="${masterBudgetType.code}" disabled="false" onclick="updateLayersShown();setBudgetPropertyVisibility(); setBudgetOverrideType();setFinalPayoutRuleVisibility();"/>&nbsp;<c:out value="${masterBudgetType.name}"/><br/>
	                	</c:when>
	                	<c:otherwise>
	                	<html:radio property="budgetType" value="${masterBudgetType.code}" disabled="true" onclick="updateLayersShown();setBudgetPropertyVisibility(); setBudgetOverrideType();setFinalPayoutRuleVisibility();"/>&nbsp;<c:out value="${masterBudgetType.name}"/><br/>
	                	</c:otherwise>
	                	</c:choose>
	                </c:forEach>
	              </c:if>  
	                <c:if test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
	                	 <c:forEach items="${budgetTypes}" var="masterBudgetType">
	               			<html:radio property="budgetType" value="${masterBudgetType.code}" disabled="true"/>&nbsp;<c:out value="${masterBudgetType.name}"/><br/>
              			 </c:forEach>
	              </c:if>
	              </c:if>
	              <c:if test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0) && !budgetMasterForm.active}">
		              <c:forEach items="${budgetTypes}" var="masterBudgetType">
	               	<html:radio property="budgetType" value="${masterBudgetType.code}" disabled="true" onclick="updateLayersShown();setBudgetPropertyVisibility(); setBudgetOverrideType();setFinalPayoutRuleVisibility();"/>&nbsp;<c:out value="${masterBudgetType.name}"/><br/>
		              </c:forEach>
	              </c:if>
                </td>
            </c:if>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="multiPromotion" required="true" styleClass="content-field-label-top">
              <cms:contentText key="AVAILABLE" code="admin.budgetmaster.details"/>
            </beacon:label>

            <c:if test='${budgetMasterForm.id == null || budgetMasterForm.id == 0}'>
              <td class="content-field content-field-label-top">
                <html:radio property="multiPromotion" value="false"/>&nbsp;<cms:contentText key="ONE_PROMO" code="admin.budgetmaster.details"/><br>
                <html:radio property="multiPromotion" value="true"/>&nbsp;<cms:contentText key="MULTI_PROMO" code="admin.budgetmaster.details"/>
              </td>
            </c:if>

            <c:if test='${budgetMasterForm.id != null && budgetMasterForm.id > 0}'>
              <html:hidden property="multiPromotion" />
              <td class="content-field-review content-field-label-top">
                <c:if test="${budgetMasterForm.multiPromotion == 'false'}">
                  <cms:contentText key="ONE_PROMO" code="admin.budgetmaster.details"/>
                </c:if>
                <c:if test="${budgetMasterForm.multiPromotion == 'true'}">
                  <cms:contentText key="MULTI_PROMO" code="admin.budgetmaster.details"/>
                </c:if>
              </td>
            </c:if>
          </tr>

         <tr class="form-blank-row"><td colspan="3"></td></tr>
         
	     <tr class="form-row-spacer" id="finalPayoutRule_radio">
	        <beacon:label property="finalPayoutRule" required="true" styleClass="content-field-label-top">
	           <cms:contentText key="FINAL_PAYOUT_RULE" code="admin.budgetmaster.details"/>
	        </beacon:label>
	          <td nowrap class="content-field content-field-label-top">
	           <c:choose>
               <c:when test="${budgetMasterForm.active || (budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
		            <c:forEach items="${budgetFinalPayoutRuleList}" var="budgetFinalPayoutRule">
		             <html:radio property="finalPayoutRule" value="${budgetFinalPayoutRule.value}" disabled="false" onclick=""/>&nbsp;<c:out value="${budgetFinalPayoutRule.label}"/><br>
		            </c:forEach>
			           </c:when>
		       
              <c:when test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0) && !budgetMasterForm.active}">
                 <html:hidden property="finalPayoutRule"/>
	     	         <c:forEach items="${budgetFinalPayoutRuleList}" var="budgetFinalPayoutRule">
		              <html:radio property="finalPayoutRule" value="${budgetFinalPayoutRule.value}" disabled="true" onclick=""/>&nbsp;<c:out value="${budgetFinalPayoutRule.label}"/><br>
		             </c:forEach>
		          </c:when>
		         </c:choose>
		       </td>            
           </tr>
          <tr class="form-blank-row"><td colspan="3"></td></tr>

		<tr id="budgetMasterDates" class="form-row-spacer" >
		<beacon:label property="budgetSegment" required="true" styleClass="content-field-label-top">
		  <cms:contentText key="BUDGET_MASTER_DATES_LABEL" code="admin.budgetmaster.details"/>
		</beacon:label>
		<td>
		<table class="table table-striped table-bordered" width="100%">
          <tr id="budget-start-date" >
            <td class="content-field">
             *<cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>&nbsp;&nbsp;
             	<c:choose>
		            <c:when test="${budgetMasterForm.budgetMasterStartDateEditable}">
		              <label for="startDate" class="date">		
		               <html:text property="startDate" maxlength="10" size="10" styleId="startDate" styleClass="text usedatepicker"/>
		               <img alt="start date" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
		               </label>
		            </c:when>
		            <c:otherwise>
		    	  	    <html:hidden property="startDate"/>
		    	  	    <html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
		    	    </c:otherwise>
		        </c:choose> 
           </td>
         </tr>        
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr id="budget-end-date" >
            <td class="content-field">
              &nbsp;&nbsp;<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>&nbsp;&nbsp;
              <label for="endDate" class="date">	
              	<html:text property="endDate" styleId="endDate" maxlength="10" size="10" styleClass="text usedatepicker"/>
                <img alt="start date" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
              </label>
            </td>
          </tr>
          </table>
          </td>
         </tr>
         
        <tr class="form-blank-row"><td colspan="3"></td></tr>
          
         <!-- *******Budget Segment ******* -->
		<tr id="budgetSegmentOption" class="form-row-spacer" >
		<beacon:label property="budgetSegments" required="true" styleClass="content-field-label-top">
		  <cms:contentText key="BUDGET_SEGMENT" code="admin.budgetmaster.details"/>
		</beacon:label>
		<td>
		    <table class="table table-striped table-bordered" width="100%">
			    <tr class="form-row-spacer">
			 		<td class="crud-table-header-row">
			 			<cms:contentText key="BUDGET_SEGMENT_NAME" code="admin.budgetmaster.details"/>
			 		</td> 
			 		<td class="crud-table-header-row">
			 			<cms:contentText key="SEGMENT_START_DATE" code="admin.budgetmaster.details"/>
			 		</td>      			
			 		<td class="crud-table-header-row">
			 			<cms:contentText key="SEGMENT_END_DATE" code="admin.budgetmaster.details"/>
			 		</td>
			 		<td class="crud-table-header-row"  id="sweepBudgetDateTitle">
						<cms:contentText key="SWEEP_BUDGET_DATE" code="promotion.awards" />
		            </td>
			 		  	
			 		<td class="crud-table-header-row">
				 		<div id="budgetReallocHeaderId">
				 			<cms:contentText key="ALLOW_BUDGET_REALLOCATION" code="promotion.awards"/>
				 		</div>
			 		</td>
			 		<td class="crud-table-header-row">
				 		<div id="budgetReallocEligHeaderId">
				 			<cms:contentText key="BUDGET_REALLOCATION_ELIG_TYPE" code="promotion.awards"/>
				 		</div>
			 		</td>
		
 			 		<c:choose>
				 		<c:when test="${(budgetMasterForm.id == null || budgetMasterForm.id == 0)}"> <%-- Add and budget Type central --%>	 			 	
					 		<td class="crud-table-header-row">
					 			<div id="budgetAmtValHeaderId">
					 			<cms:contentText key="BUDGET_AMOUNT" code="admin.budgetmaster.details"/>
					 				<div class="amount-type-label">(Points)</div>
					 			</div>
					 		</td>
				 		</c:when>
				 		<c:otherwise><%-- Update and budget Type central --%>		 		
					 		<td class="crud-table-header-row" id="origValHeaderId">
					 			<cms:contentText key="ORIGINAL" code="admin.budgetmaster.details"/>
					 		</td>			 		
					 		<td class="crud-table-header-row" id="currentValHeaderId">
					 			<cms:contentText key="CURRENT" code="admin.budgetmaster.details"/>
					 		</td> 
					 		<td class="crud-table-header-row" id="addOnValHeaderId">
					 			<cms:contentText key="DELTA" code="admin.budgetmaster.details"/>
					 		</td> 
				 		</c:otherwise>
			 		</c:choose> 

			 		<c:if test="${ budgetMasterForm.budgetSegmentVBListSize ne '1'}">
			 			<td class="crud-table-header-row">
							<div class="crud-table-header-row" id="removeBudgetSegment"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></div>
						</td>
					</c:if> 	 		
			    </tr>
				<c:set var="switchColor" value="false"/>
				<%
				  int sIndex = 0;
				%>
			  	<c:forEach var="budgetSegmentVBList" items="${budgetMasterForm.budgetSegmentVBList}" varStatus="status" >
			  	<html:hidden property="id" name="budgetSegmentVBList" indexed="true"/> 
			  	
			  	<%
			  		String startDateCalendarCounter = "charStartDate" + sIndex;
			  		String endDateCalendarCounter = "charEndDate" + sIndex;
			  		String startDateCalendarTrigger = "charStartDateTrigger" + sIndex;
			  		String endDateCalendarTrigger = "charEndDateTrigger" + sIndex;
			  		String currentValue = "currentValue" + "[" + sIndex + "]";
			  		String removeBudgetSegmentCounter = "removeBudgetSegment" + sIndex;
			  		
			  		String budgetSweepDateTDCounter = "budgetSweepDateTD" + sIndex;
					String budgetSweepDateCounter = "budgetSweepDate" + sIndex;
			  		
			  		String originalCentralValue = "originalValue" + "[" + sIndex + "]";
			  		String currentCentralValue = "currentValue" + "[" + sIndex + "]";
			  		String addOnCentralValue = "addOnValue" + "[" + sIndex + "]";
			  		
			  		//div
			  		String budgetReallocId = "budgetReallocId" + "[" + sIndex + "]";
			  		String budgetReallocEligId = "budgetReallocEligId" + "[" + sIndex + "]";
			  		String budgetAmtValId = "budgetAmtValId"+ "[" + sIndex + "]";
			  		String origValId = "origValId"+ "[" + sIndex + "]";
			  		String currentValId = "currentValId" + "[" + sIndex + "]";
			  		String addOnValId = "addOnValId" + "[" + sIndex + "]";
				  	
			  	%>	
				  <c:choose>
					<c:when test="${switchColor == 'false'}">
						<tr class="crud-table-row1">
						<c:set var="switchColor" scope="page" value="true"/>
					</c:when>
					<c:otherwise>
						<tr class="crud-table-row2">
						<c:set var="switchColor" scope="page" value="false"/>
					</c:otherwise>
				  </c:choose>
		
					<%
					  String budgetAmountDisabled = "true";
					  String addOnValueDisabled = "false";
					%>
					<c:if test="${ budgetSegmentVBList.id == null || budgetSegmentVBList.id == 0 }">
						<%
						  budgetAmountDisabled = "false";
						  addOnValueDisabled = "true";
						%>
					</c:if>	
		
					<td class="crud-content">
				    	<html:text property="segmentName" size="50" maxlength="50" indexed="true" styleId="segmentName" name="budgetSegmentVBList" styleClass="content-field" />
			      	</td> 
			    	<td class="crud-content" width="20%">	         			 
		    		<c:choose>
			            <c:when test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0) && budgetSegmentVBList.startDate != null && currentDate > budgetSegmentVBList.startDate }">
			            	   <html:hidden property="startDateStr" name="budgetSegmentVBList" indexed="true"/>  
		 		    	 	   <html:text property="startDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="content-field"  readonly="true" disabled="true" />	      	  	    
			    	    </c:when>
			    	    <c:otherwise>
			    	      <label for="<%=startDateCalendarCounter%>" class="date">
			    	    	<html:text property="startDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=startDateCalendarCounter%>" readonly="true" disabled="false" />
		         			<img alt="start date" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />  
		         		  </label>    
			    	    </c:otherwise>	 
				    </c:choose>   
				    </td> 			      	
				    <td class="crud-content" width="20%">
		    		<c:choose>
			            <c:when test="${!(budgetMasterForm.id == null || budgetMasterForm.id == 0) && budgetSegmentVBList.endDate != null && currentDate > budgetSegmentVBList.endDate }">
			            		<html:hidden property="endDateStr" name="budgetSegmentVBList" indexed="true" />  
		 		    	 	   <html:text property="endDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="content-field"  readonly="true" disabled="true" />	      	  	    
			    	    </c:when>
			    	    <c:otherwise>
 						<label for="<%=endDateCalendarCounter%>" class="date">
				       		<html:text property="endDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=endDateCalendarCounter%>" />
		         			<img alt="end date" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
		         		</label>   
			    	    </c:otherwise>	 
				    </c:choose> 				    
					</td>
					<c:choose>
					<c:when test="${budgetSegmentVBList.budgetSweepDate != null}">
					<td class="crud-content" width="20%" id="<%=budgetSweepDateTDCounter%>">
							<label for="<%=budgetSweepDateCounter%>" class="date">
						       <html:text property="budgetSweepDate" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=budgetSweepDateCounter%>"/>
							   <img alt="sweep date" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
							</label>
		            </td> 
			      	</c:when>
			      	<c:otherwise>
			      	<td class="crud-content" width="20%" id="<%=budgetSweepDateTDCounter%>"></td>
			      	</c:otherwise>
			      	</c:choose>		 
			  	  <%-- pax and node budget  --%>
				    <td class="crud-content" width="20%">
				    <div id="<%=budgetReallocId%>">
				      <table>
				        <tr>
				          <td class="content-field" valign="top">
				            <html:radio styleId="allowBudgetReallocationFalse"  property="allowBudgetReallocation" value="false" indexed="true" name="budgetSegmentVBList" onclick="updateLayersShown();" disabled="${displayFlag}" />
				            <cms:contentText  code="system.common.labels" key="NO" />
				          </td>
				        </tr>
				        <tr>
				          <td class="content-field" valign="top">
							<html:radio styleId="allowBudgetReallocationTrue" property="allowBudgetReallocation" value="true" indexed="true" name="budgetSegmentVBList" onclick="updateLayersShown();" disabled="${displayFlag}" />
				            <cms:contentText  code="system.common.labels" key="YES" />
				          </td>
				        </tr>
				      </table>
				      </div>
				    </td>
		
					<td class="crud-content" width="10%">
					  <div id="<%=budgetReallocEligId%>">
						<html:select styleId="budgetReallocationEligTypeCode" property="budgetReallocationEligTypeCode" indexed="true" name="budgetSegmentVBList" styleClass="content-field">
							<html:options collection="budgetReallocationEligTypeList" property="code" labelProperty="name" />
						</html:select>
					  </div>
					</td>
					
				  <%-- central budget amount setup --%>
				  	<c:choose>
				 		<c:when test="${ (budgetMasterForm.id == null || budgetMasterForm.id == 0)}">
 							<td class="crud-content">
								<div id ="<%=budgetAmtValId%>">
							    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="<%=originalCentralValue%>" name="budgetSegmentVBList" styleClass="content-field" />
							    </div>
					      	</td>  
				      	</c:when>
			      	<c:otherwise>
			      		<c:choose>
			      		<%-- updating old segment --%>
			      		<c:when test="${budgetMasterForm.budgetType == 'central' && not empty budgetSegmentVBList.id}">
						<td class="crud-content">
							<div id ="<%=origValId%>">
								<c:out value="${budgetSegmentVBList.originalValue}"/>
								<html:hidden property="originalValue" name="budgetSegmentVBList" indexed="true"/>  
						    </div>
				      	</td> 			      	
						<td class="crud-content">
							<div id ="<%=currentValId%>">
								<c:out value="${budgetSegmentVBList.currentValue}"/>
								<html:hidden property="currentValue" name="budgetSegmentVBList" indexed="true"/>  						
						    </div>
				      	</td> 			      	
						<td class="crud-content">
							<div id ="<%=addOnValId%>">
						    	<html:text property="addOnValue" size="10" maxlength="10" indexed="true" styleId="<%=addOnCentralValue%>" name="budgetSegmentVBList" styleClass="content-field" disabled="false" />
						    </div>
				      	</td>		      		
			      		</c:when>
			      		<%-- adding new segment --%>
			      		<c:when test="${budgetMasterForm.budgetType == 'central' && empty budgetSegmentVBList.id}">
					      	<td class="crud-content">
					      	</td>   
					      	<td class="crud-content">
					      	</td> 		      		
 							<td class="crud-content">
								<div id ="<%=budgetAmtValId%> ">
							    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="<%=originalCentralValue%>" name="budgetSegmentVBList" styleClass="content-field" />
							    </div>
					      	</td> 	   					      	
			      		</c:when>	
			      		</c:choose>		      		
			      	</c:otherwise>      	
			      	</c:choose>
			      	<c:if test="${ budgetMasterForm.budgetSegmentVBListSize ne '1'}"> 
	    				<td class="crud-content" >
		    				<div id="<%=removeBudgetSegmentCounter%>">
					        	<html:button property="deleteSegmentId" styleClass="content-buttonstyle" onclick="removeSegment('removeBudgetSegment');" >
					          		<cms:contentText code="system.button" key="REMOVE" />
					        	</html:button>
							</div>			        	
				        </td>	
 			        </c:if>   
				  	<%
		   		  		sIndex = sIndex + 1;
		   			%>
				    </tr>
				  </c:forEach>
				  
				  <tr class="form-blank-row"><td></td></tr>
				    <tr class="form-row-spacer">
				        <td align="left" colspan="2">
				          	<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
				     	  		<a id="addAnotherhref"  href="#" onclick="addAnotherSegment('addAnotherSegment');updateLayersShown();" >
				       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
				     	  		</a> 
			     	  		</beacon:authorize>
			        	</td>
				    </tr>	  
			</table>
            </td>
          </tr>

          <!-- ********End of Budget Segment ****** -->
          
          <script type="text/javascript">
          <!--
            setBudgetPropertyVisibility();
          //-->
          </script>

          <%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <c:choose>
                <c:when test='${budgetMasterForm.id == null || budgetMasterForm.id == 0}'>
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                    <html:submit styleClass="content-buttonstyle" onclick="onSaveClicked('create')">
                      <cms:contentText code="system.button" key="SAVE" />
                    </html:submit>
                  </beacon:authorize>
                </c:when>
                <c:otherwise>
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                    <html:submit styleClass="content-buttonstyle" onclick="onSaveClicked('update')">
                      <cms:contentText code="system.button" key="SAVE" />
                   </html:submit>
                  </beacon:authorize>
                </c:otherwise>
              </c:choose>
              <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('budgetMasterListDisplay.do')">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:button>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
        </table>
      </td>
    </tr>
  </table>

  <script type="text/javascript">
 updateLayersShown();
 onAwardTypeClick();
  </script>

<script>
$( document ).ready(function() {
    $( "#startDate" ).change(function() {
        var curDate = $(this).val();           
            $("#charStartDate0").val(curDate);  
});
     $( "#endDate" ).change(function() {
        var curDate = $(this).val();           
        $("#charEndDate"+${lastSegmentIndex}).val(curDate);   
}); 
});
 </script>

</html:form>
