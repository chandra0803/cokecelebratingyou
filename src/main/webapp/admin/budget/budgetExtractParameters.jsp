<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.BudgetOverrideableType"%>
<%@ page import="com.biperf.core.domain.enums.BudgetType"%>
<%@ page import="com.biperf.core.domain.enums.BudgetFinalPayoutRule"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.budget.NodeTypeFormBean" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}


//-->
</script>

<script type="text/javascript">
<!--

function enableListElements(xForm, element)
{
	//alert("Element 1: " + element);
	 objElems = xForm.elements;
  for(i=0;i<objElems.length;i++){  	
  	if(objElems[i].name == element)
  	{
  		//alert("Element 2: " + objElems[i].name);
  		objElems[i].disabled = false;
  	}    
  }

}

function disableListElements(xForm, element)
{
	 objElems = xForm.elements;
  for(i=0;i<objElems.length;i++){  	
  	if(objElems[i].name == element)
  	{
 		//alert("Element 2: " + objElems[i].name);
  		objElems[i].disabled = true;
  	}    
  }

}

function disableListElementsAndRadio(xForm, element)
{
	 objElems = xForm.elements;
  for(i=0;i<objElems.length;i++){  	
  	if(objElems[i].name == element)
  	{
 		//alert("Element 2: " + objElems[i].name);
  		objElems[i].disabled = true;
  		objElems[i].checked = false;
  	}    
  }

}

function enableElement(xForm, element)
{
	element.disabled = false;
}

function enableDisableNode(xForm, chbox, index, charIndex, size) {
	var nodeSelected = "nodeType[" + index + "].allBudgets";
	if (chbox.checked == false) {
		disableListElementsAndRadio(document.budgetExtractParametersForm,nodeSelected);		
	} else {
		enableListElements(document.budgetExtractParametersForm,nodeSelected);
	}
	objElems = xForm.elements;
	for(i=0;i<objElems.length;i++){  	
	 	 if(objElems[i].name == nodeSelected)
	  	 {
	 		//alert("Element 2: " + objElems[i].name);
	 		if (chbox.checked == false) {
	 			disableNodeFormElements(xForm, charIndex, size);
	 		} 
	  	 }    
	  }	
}

function disableNodeFormElements(xForm, index, size){
  	 
   	for (cIndex=index;cIndex<size + index;cIndex++) {

	 objElems = xForm.elements;
	 var characteristicSelected = "characteristic[" + cIndex + "].selected";
	 disableListElements(document.budgetExtractParametersForm,characteristicSelected);
	 for(i=0;i<objElems.length;i++){  	
	  	 if(objElems[i].name == characteristicSelected)
	  	 {
	 		//alert("Element 2: " + objElems[i].name);
	 		objElems[i].checked = false;
	  		enableCheckBox(objElems[i]);
	  	 }    
	  }
	}
  	
  	xForm.headcount.disabled=false;
 	xForm.submit.disabled=false;
 	xForm.backToBudgetMaster.disabled=false; 
}

function enableNodeFormElements(xForm, index, size){

   	for (cIndex=index;cIndex<size+index;cIndex++) {

	   	var characteristicSelected = "characteristic[" + cIndex + "].selected";
	   	enableListElements(document.budgetExtractParametersForm,characteristicSelected);
	   	objElems = xForm.elements;
		 for(i=0;i<objElems.length;i++){  	
		  	 if(objElems[i].name == characteristicSelected)
		  	 {
		 		//alert("Element 2: " + objElems[i].name);
		  		enableCheckBox(objElems[i]);
		  	 }    
		  }
	   	
	}
  
  xForm.ownersnode.disabled=true;
  xForm.ownersnode.value='';
  xForm.awardsPerParticipant.disabled=true;
}

function disablePaxFormElements(xForm){
  clearCharacteristicSelection();
  objElems = xForm.elements;
  for(i=0;i<objElems.length;i++){  
  		if( objElems[i].name !='budgetSegmentId'&& objElems[i].name !='id'&& objElems[i].name !='submit' && objElems[i].name !='method' )
		  {
  		objElems[i].disabled = true; 
		  }
	 
  }  
  
  	  <logic:iterate name="nodeList" id="nodeType" indexId="nodeIndex">
		<c:if test="${paxType}">	
			<c:set var="charIndex" value="${-1}" />				
					<c:set var="characteristicList1" value="${characteristicMap[nodeType] }" />					
					<c:if test="${characteristicList1 != null }">
						<logic:iterate name="characteristicList1" id="characteristic">
							<c:set var="charIndex" value="${charIndex+1}" />
							var characteristicElements = "characteristic[" + <c:out value="${charIndex}"/> + "].displaySelected";
							enableListElements(document.budgetExtractParametersForm,characteristicElements);
						</logic:iterate>
					</c:if>					
		</c:if>
	</logic:iterate>  
  
  	xForm.allOwners[0].disabled=false;
  	xForm.allOwners[1].disabled=false;	
  	xForm.headcount.disabled=false;
  	xForm.headcount.checked=false;
  	xForm.ownersnode.value='';
 	xForm.submit.disabled=false;
 	xForm.backToBudgetMaster.disabled=false; 
}

function enablePaxFormElements(xForm){
  objElems = xForm.elements;
  for(i=0;i<objElems.length;i++){
   	  if( objElems[i].name !='characteristic[1].booleanSet')
		  {	  
   			 objElems[i].disabled = false;
		  }
  }  
  i = 0;
  <logic:iterate name="nodeList" id="nodeType" indexId="nodeIndex">
		<c:if test="${paxType}">	
			<c:set var="charIndex" value="${-1}" />				
					<c:set var="characteristicList1" value="${characteristicMap[nodeType] }" />					
					<c:if test="${characteristicList1 != null }">
						<logic:iterate name="characteristicList1" id="characteristic">
							<c:set var="charIndex" value="${charIndex+1}" />
							<c:choose>
								<c:when test="${characteristic.dataType.textType}">
									var characteristicOptionValue = "characteristic[" + <c:out value="${charIndex}"/> + "].optionValue";
									var characteristicTextValue = "characteristic[" + <c:out value="${charIndex}"/> + "].value";
									disableListElements(document.budgetExtractParametersForm,characteristicOptionValue);
									disableListElements(document.budgetExtractParametersForm,characteristicTextValue);
								</c:when>							
							</c:choose>
							<c:choose>
								<c:when test="${characteristic.dataType.booleanType}">
									var characteristicBooleanSet = "characteristic[" + <c:out value="${charIndex}"/> + "].booleanSet";
									disableListElements(document.budgetExtractParametersForm,characteristicBooleanSet);
								</c:when>							
							</c:choose>
							<c:choose>
								<c:when test="${characteristic.dataType.selectType}">									
									<c:choose>
										<c:when test="${characteristic.dataType.multiSelect}">
											var characteristicOptionValue = "characteristic[" + <c:out value="${charIndex}"/> + "].optionValue";
											disableListElements(document.budgetExtractParametersForm,characteristicOptionValue);							
										</c:when>
									</c:choose>
									<logic:iterate id="checkItem" name="characteristic" property="checkList" type="com.biperf.core.ui.budget.CheckItem" indexId="checkIndex">
										var characteristicOptionCheckItem = "characteristic[" + <c:out value="${charIndex}"/> + "].checkItem[" + <c:out value="${checkIndex}"/> + "].checked";
										disableListElements(document.budgetExtractParametersForm,characteristicOptionCheckItem);									
									</logic:iterate>
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${characteristic.dataType.numberType}">
									var characteristicLowValue = "characteristic[" + <c:out value="${charIndex}"/> + "].lowValue";
									var characteristicHighValue = "characteristic[" + <c:out value="${charIndex}"/> + "].highValue";
									disableListElements(document.budgetExtractParametersForm,characteristicLowValue);
									disableListElements(document.budgetExtractParametersForm,characteristicHighValue);
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${characteristic.dataType.dateType}">
									var characteristicLowDate = "characteristic[" + <c:out value="${charIndex}"/> + "].lowDate";
									var characteristicHighDate = "characteristic[" + <c:out value="${charIndex}"/> + "].highDate";
									var characteristicLowDateControl = "charStartDateTrigger[" + <c:out value="${charIndex}"/> + "]";
									disableListElements(document.budgetExtractParametersForm,characteristicLowDate);
									disableListElements(document.budgetExtractParametersForm,characteristicHighDate);
									disableListElements(document.budgetExtractParametersForm,characteristicLowDateControl);	
									i = i + 1;				
								</c:when>
							</c:choose>
						</logic:iterate>
					</c:if>					
		</c:if>
	</logic:iterate> 
  
  xForm.ownersnode.disabled=true;
  xForm.ownersnode.value='';  
  xForm.awardsPerParticipant.disabled=true;
}


function enableCheckBox(chboxname)
{

  var elementName = chboxname.name;
  objElems = document.budgetExtractParametersForm.elements;
  for(i=0;i<objElems.length;i++){
    var elementSelect = objElems[i].name;
    if(elementSelect.substring(0,17) == elementName.substring(0,17) && elementSelect.substring(18,25) != "display" && elementSelect != elementName)
    {
	   	if(chboxname.checked == true)
		{
			objElems[i].disabled = false;
		}
		
		if(chboxname.checked == false)
		{
			objElems[i].disabled = true;
		}    	
    }
  }  
}

function headCount(xForm, chboxname){
   	if(chboxname.checked == true)
	{
  		xForm.ownersnode.disabled=false;
  		xForm.awardsPerParticipant.disabled=false;
	}
	if(chboxname.checked == false)
	{
		xForm.ownersnode.disabled=true;
		xForm.ownersnode.value='';		
  		xForm.awardsPerParticipant.disabled=true;
	}    	
}



function clearCharacteristicSelection()
{	
	<logic:iterate name="nodeList" id="nodeType" indexId="nodeIndex">
		<c:if test="${paxType}">	
			<c:set var="charIndex" value="${-1}" />				
					<c:set var="characteristicList1" value="${characteristicMap[nodeType] }" />					
					<c:if test="${characteristicList1 != null }">
						<logic:iterate name="characteristicList1" id="characteristic">
							<c:set var="charIndex" value="${charIndex+1}" />
							var characteristicOptionValue = "characteristic[" + <c:out value="${charIndex}"/> + "].selected";
							clearElementSelection(document.budgetExtractParametersForm,characteristicOptionValue);							
						</logic:iterate>
					</c:if>					
		</c:if>
		
	</logic:iterate> 
}

function clearElementSelection(xForm, element)
{
  objElems = xForm.elements;
  for(i=0;i<objElems.length;i++){  	
  	if(objElems[i].name == element)
  	{
  		objElems[i].checked = false;
  	}    
  }
}


//-->
</script>

<%
int calendarCounter = 0;
%>

  <beacon:client-state>
	<beacon:client-state-entry name="budgetSegmentId" value="${budgetExtractParametersForm.budgetSegmentId}" />	
  </beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td><span class="headline"><cms:contentText key="VIEW_TITLE"
			code="admin.budgetextract.parameters" /></span><br />
		<span class="subheadline"> <c:out value="${budgetMaster.budgetName}" /></span><br />
		<span class="subheadline"> <c:out value="${budgetSegment.displaySegmentName}" /></span>
		<%--INSTRUCTIONS--%>
		
		<br />
		<br />
			<span class="content-instruction"> 
				<cms:contentText key="INSTRUCTION" code="admin.budgetextract.parameters" /> 
			</span> 
		
		<br />
		<br />
		<%--END INSTRUCTIONS--%> <cms:errors />
		<table>
			<html:form styleId="contentForm" action="budgetExtract">
				<html:hidden property="method" value="" />
				<html:hidden property="id" />
				<html:hidden property="budgetSegmentId" value="${budgetSegment.id}" />
				<tr>
					<beacon:label required="true"
						styleClass="content-field-label-top">
						<b><cms:contentText key="OWNER_EXTRACT_LABEL"
							code="admin.budgetextract.parameters" /></b>
					</beacon:label>
				</tr>
				<c:choose>
				
					<%-- Start PAX type radio button options --%>
					<c:when test="${paxType}">
						<tr>
							<beacon:label required="false" colspan="4"
								styleClass="content-field-label-top">
								<html:radio property="allOwners" value="true" onclick="disablePaxFormElements(document.budgetExtractParametersForm)"/>
								<cms:contentText key="ALL_OWNERS"
									code="admin.budgetextract.parameters" />
							</beacon:label>
						</tr>
						<tr>
							<beacon:label required="false" colspan="4"
								styleClass="content-field-label-top">								
								
								<c:if test="${not empty characteristicList}">
									<html:radio property="allOwners" value="false" onclick="enablePaxFormElements(document.budgetExtractParametersForm)"/>								
								</c:if>								
								<c:if test="${empty characteristicList}">
									<html:radio property="allOwners" value="false" disabled="true" onclick="enablePaxFormElements(document.budgetExtractParametersForm)"/>																								
								</c:if>								
								
								<cms:contentText key="ONLY_OWNERS_WITH_CHARACTERISTICS"
									code="admin.budgetextract.parameters" />
							</beacon:label>
						</tr>
					</c:when>
					<%-- End PAX type radio button options --%>
					
				</c:choose>
				<c:set var="charIndex" value="${-1}" />
				
				<%-- Start Node type radio button options --%>
				<logic:iterate name="nodeList" id="nodeType" indexId="nodeIndex">
					<c:if test="${not paxType}">
						<c:set var="characteristicList"
							value="${characteristicMap[nodeType] }" />
						<tr>
							<c:if test="${characteristicList != null}">
										<%  
											NodeTypeFormBean nt = (NodeTypeFormBean)pageContext.getAttribute("nodeType");
											Map characteristicMap = (Map)request.getAttribute("characteristicMap");
											List l = (List)characteristicMap.get(nt);
											pageContext.setAttribute("numChars",String.valueOf(l.size()));
										%>
							</c:if>
							<beacon:label required="false"
								styleClass="content-field-label-top">&nbsp;
							<html:hidden property="nodeType[${nodeIndex}].nodeTypeId"
								value="${nodeType.nodeType.id }" /> <html:checkbox
								property="nodeType[${nodeIndex}].selected" value="true"
								onclick="enableDisableNode(document.budgetExtractParametersForm, this, ${nodeIndex}, ${charIndex+1}, ${numChars})"/> <span
								class="content-field-label"><cms:contentText
								key="${nodeType.nodeType.nameCmKey}"
								code="${nodeType.nodeType.cmAssetCode}" /></span>
							</beacon:label>
						</tr>
						<c:choose>
							<c:when test="${characteristicList != null }">
								<tr>
									<beacon:label required="false" colspan="4"
										styleClass="content-field-label-top">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<html:radio property="nodeType[${nodeIndex}].allBudgets" disabled="true"
											value="true"  onclick="disableNodeFormElements(document.budgetExtractParametersForm, ${charIndex+1}, ${numChars})"/>
										<cms:contentText key="ALL_OWNERS"
											code="admin.budgetextract.parameters" />
									</beacon:label>
								</tr>
								<tr>
									<beacon:label required="false" colspan="4"
										styleClass="content-field-label-top">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 										<html:radio property="nodeType[${nodeIndex}].allBudgets" disabled="true"
											value="false" onclick="enableNodeFormElements(document.budgetExtractParametersForm, ${charIndex+1}, ${numChars})"/>
										<cms:contentText key="ONLY_OWNERS_WITH_CHARACTERISTICS"
											code="admin.budgetextract.parameters" />
									</beacon:label>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<beacon:label required="false" colspan="4"
										styleClass="content-field-label-top"> 
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 	
		     							<cms:contentText key="NO_CHARACTERISTICS"
											code="admin.budgetextract.parameters" />
		    						</beacon:label>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:if>	
					<%-- End Node type radio button options --%>
					
					<c:if test="${(empty characteristicList) and 
									paxType}">					
						<tr>
							<beacon:label required="false" styleClass="content-field-label-top">
								<cms:contentText key="NO_PAX_CHAR"
											code="admin.budgetextract.parameters" />
							</beacon:label>
						</tr>
					</c:if>
								
					<c:if test="${characteristicList != null }">
						<logic:iterate name="characteristicList" id="characteristic">
							<c:set var="charIndex" value="${charIndex+1}" />
							<tr>
							<%-- Start Assign Hidden characteristic properties --%>
								<html:hidden
									property="characteristic[${charIndex}].characteristicId"
									value="${characteristic.characteristicId }" />
								<html:hidden property="characteristic[${charIndex}].nameCmKey"
									value="${characteristic.nameCmKey }" />
								<html:hidden property="characteristic[${charIndex}].cmAssetCode"
									value="${characteristic.cmAssetCode }" />
								<c:if test="${not paxType}">
									<html:hidden property="characteristic[${charIndex}].nodeTypeId"
										value="${nodeType.nodeType.id }" />
								</c:if>
								<%-- End Hidden Properties --%>
								
								<%-- Start Define the Characteristic Checkbox --%>
								<beacon:label required="false"
									styleClass="content-field-label-top">        
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
	   								<html:checkbox property="characteristic[${charIndex}].selected"
												value="true"  disabled="true"  onclick="enableCheckBox(this)"/>
									<cms:contentText key="${characteristic.nameCmKey}"
										code="${characteristic.cmAssetCode}" />&nbsp;
										
									<%-- Specific for BOOLEAN type --%>
									<c:if test="${characteristic.dataType.booleanType }">
	    								<span class="content-field-label"><cms:contentText key="IS"
											code="admin.budgetextract.parameters" />
										</span>
									</c:if>
									
									<%-- Specific for NUMBER type --%>
									<c:if test="${(characteristic.dataType.numberType) or 
													(characteristic.dataType.dateType)}">
           								<cms:contentText key="IS_BETWEEN" code="admin.budgetextract.parameters" />
									</c:if>									
								</beacon:label>
								<%-- End Define the Characteristic Checkbox --%>
								
								<%-- Start adding characteristic options for the given characteristic type --%>
								<c:choose>
								
									<%-- Start If the characteristic type is TEXT --%>
									<c:when test="${characteristic.dataType.textType}">
										<td colspan="3"><html:select
											property="characteristic[${charIndex}].optionValue" styleClass="content-field killme" disabled="true">
											<html:option
												value='${characteristic.constants["startsWith"] }'>
												<cms:contentText key="STARTS_WITH"
													code="admin.budgetextract.parameters" />
											</html:option>
											<html:option
												value='${characteristic.constants["doesNotStartWith"] }'>
												<cms:contentText key="DOES_NOT_START_WITH"
													code="admin.budgetextract.parameters" />
											</html:option>
											<html:option value='${characteristic.constants["contains"] }'>
												<cms:contentText key="CONTAINS"
													code="admin.budgetextract.parameters" />
											</html:option>
											<html:option
												value='${characteristic.constants["doesNotContain"] }'>
												<cms:contentText key="DOES_NOT_CONTAIN"
													code="admin.budgetextract.parameters" />
											</html:option>
											<html:option value='${characteristic.constants["endsWith"] }'>
												<cms:contentText key="ENDS_WITH"
													code="admin.budgetextract.parameters" />
											</html:option>
											<html:option
												value='${characteristic.constants["doesNotEndWith"] }'>
												<cms:contentText key="DOES_NOT_END_WITH"
													code="admin.budgetextract.parameters" />
											</html:option>
										</html:select> <html:text
											property="characteristic[${charIndex}].value"   disabled="true"/></td>
									</c:when>
									<%-- End If the characteristic type is TEXT--%>
									
									<%-- Start If the characteristic type is BOOLEAN--%>
									<c:when test="${characteristic.dataType.booleanType}">
										<td colspan="2"><html:radio
											property="characteristic[${charIndex}].booleanSet"
											value="true"   disabled="true"/><span class="content-field-label"><cms:contentText
											key="TRUE" code="system.boolean.values" /></span><br>
										<html:radio property="characteristic[${charIndex}].booleanSet"
											value="false"   disabled="true"/><span class="content-field-label"><cms:contentText
											key="FALSE" code="system.boolean.values" /></span><br>
										</td>
									</c:when>
									<%-- End If the characteristic type is BOOLEAN--%>
									
									<%-- Start If the characteristic type is  MULTI OR SINGLE SELECT --%>
									<c:when test="${characteristic.dataType.selectType}">
										<td class="content-field-label-top"><c:choose>
											<c:when test="${characteristic.dataType.multiSelect}">
												<html:select
													property="characteristic[${charIndex}].optionValue" styleClass="content-field killme"  disabled="true">
													<html:option
														value='${characteristic.constants["isOneOf"] }'>
														<cms:contentText key="IS_ONE_OF"
															code="admin.budgetextract.parameters" />
													</html:option>
													<html:option
														value='${characteristic.constants["isAllOf"] }'>
														<cms:contentText key="IS_ALL_OF"
															code="admin.budgetextract.parameters" />
													</html:option>
												</html:select>
											</c:when>
											<c:otherwise>
												<span class="content-field-label"><cms:contentText
													key="IS_ONE_OF" code="admin.budgetextract.parameters" /></span>
											</c:otherwise>
										</c:choose></td>
										<td><html:hidden
											property="characteristic[${charIndex}].characteristicPossibleValues"
											value="${characteristic.possibleValuesAsString}" /> <logic:iterate
											id="checkItem" name="characteristic" property="checkList"
											type="com.biperf.core.ui.budget.CheckItem"
											indexId="checkIndex">
											<html:checkbox
												property="characteristic[${charIndex}].checkItem[${checkIndex}].checked"
												value="true"  disabled="true"/>
											<span class="content-field-label"><c:out
												value="${checkItem.title}" /></span>
											<br>
										</logic:iterate></td>
									</c:when>
									<%-- End If the characteristic type is  MULTI OR SINGLE SELECT --%>
									
									<%-- Start If the characteristic type is  NUMBER --%>
									<c:when test="${characteristic.dataType.numberType}">
										<td colspan="2"><html:text
											property="characteristic[${charIndex}].lowValue" size="5"   disabled="true"/>
										&nbsp;<span class="content-field-label"><cms:contentText
											key="AND" code="admin.budgetextract.parameters" /></span>&nbsp;
										<html:text property="characteristic[${charIndex}].highValue"
											size="5"   disabled="true"/></td>
									</c:when>
									<%-- End If the characteristic type is  NUMBER --%>
									
									<%-- Start If the characteristic type is DATE --%>
									<c:when test="${characteristic.dataType.dateType}">
										<%
										String startDateCalendarCounter = "charStartDate" + calendarCounter;
										String endDateCalendarCounter = "charEndDate" + calendarCounter;
										String startDateCalendarTrigger = "charStartDateTrigger" + calendarCounter;
										String endDateCalendarTrigger = "charEndDateTrigger" + calendarCounter;
										%>
										<td colspan="2"><html:text
											property="characteristic[${charIndex}].lowDate" styleId="<%=startDateCalendarCounter%>" styleClass="content-field" readonly="true"  disabled="true" onfocus="clearDateMask(this);"/>
											<img alt="" id="<%=startDateCalendarTrigger%>" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />&nbsp;
											<span class="content-field-label"><cms:contentText key="AND" code="admin.budgetextract.parameters" /></span>&nbsp;
											<html:text property="characteristic[${charIndex}].highDate" styleId="<%=endDateCalendarCounter%>" styleClass="content-field" readonly="true" disabled="true" onfocus="clearDateMask(this);"/>
											<img alt="" id="<%=endDateCalendarTrigger%>" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />&nbsp;
										</td>
								          <script type="text/javascript">
								              Calendar.setup(
								                {
								                  inputField  : "<%=startDateCalendarCounter%>",         // ID of the input field
								                  ifFormat    : "${TinyMceDatePattern}",    // the date format
								                  button      : "<%=startDateCalendarTrigger%>"       // ID of the button
								                }
								              );
								              Calendar.setup(
								                {
								                  inputField  : "<%=endDateCalendarCounter%>",         // ID of the input field
								                  ifFormat    : "${TinyMceDatePattern}",    // the date format
								                  button      : "<%=endDateCalendarTrigger%>"       // ID of the button
								                }
								              );
								          </script>
								          <%
								          calendarCounter = calendarCounter + 1;
								          %>
									</c:when>
									<%-- End If the characteristic type is DATE --%>
								</c:choose>
								<%-- End adding characteristic options for the given characteristic type --%>
								
								
							</tr>
						</logic:iterate>
					</c:if>
				</logic:iterate>
				<tr class="form-blank-row" />
				<tr class="form-blank-row" />
				<tr>
					<beacon:label required="false" styleClass="content-field-label-top">
						<b><cms:contentText key="DATA_TO_EXTRACT_LABEL"
							code="admin.budgetextract.parameters" /></b>
					</beacon:label>
				</tr>
				<tr>
					<beacon:label required="false"
						styleClass="content-field-label-top"> 
						&nbsp;&nbsp;
					<html:checkbox property="headcount" value="true"  onclick="headCount(document.budgetExtractParametersForm, this)"/>
						<span class="content-field-label">
							<cms:contentText key="CALCULATE_AWARD" code="admin.budgetextract.parameters" /> 
						</span>
					</beacon:label>
				</tr>
				<tr>
					<beacon:label required="false"
						styleClass="content-field-label-top">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*	
							<cms:contentText key="HEADCOUNT" code="admin.budgetextract.parameters" /> 
							&nbsp;&nbsp;
						<html:select property="ownersnode" value="" disabled="true">	
							<html:option value="">
								<cms:contentText key="SELECT_ONE" code="admin.budgetextract.parameters" />   
							</html:option>
							<html:option value="true">
								<cms:contentText key="AT_OWNERS_NODE"
									code="admin.budgetextract.parameters" />
							</html:option>
							<html:option value="false">
								<cms:contentText key="AT_OWNERS_NODE_WITH_CHILDREN"
									code="admin.budgetextract.parameters" />
							</html:option>
						</html:select>
					</beacon:label>
				</tr>
				<tr>
					<beacon:label required="false" styleClass="content-field-label-top">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<cms:contentText key="AWARDS_PER_PARTICIPANT"
							code="admin.budgetextract.parameters" />
						&nbsp;&nbsp;		
						<html:text property="awardsPerParticipant" size="5"   disabled="true"/>
					</beacon:label>
				</tr>
				<tr class="form-blank-row" />
				<tr class="form-blank-row" />
				<tr class="form-blank-row" />
				<tr class="form-blank-row" />								
				<c:set var="charIndex" value="${-1}" />
				<logic:iterate name="nodeList" id="nodeType" indexId="nodeIndex">
					<c:set var="characteristicList"
						value="${characteristicMap[nodeType] }" />
					<c:if test="${characteristicList != null }">
						<tr>
							<beacon:label required="false"
								styleClass="content-field-label-top">
								&nbsp;&nbsp;
								<c:if test="${not paxType}">
									<cms:contentText key="${nodeType.nodeType.nameCmKey}"
									code="${nodeType.nodeType.cmAssetCode}" />&nbsp;
								</c:if>	
								<c:if test="${not empty characteristicList}">
									<cms:contentText key="CHARACTERISTICS_TO_APPEAR" code="admin.budgetextract.parameters" />
								</c:if>	
							</beacon:label>
						</tr>
					</c:if>
					<c:if test="${characteristicList != null }">
						<logic:iterate name="characteristicList" id="characteristic">
							<c:set var="charIndex" value="${charIndex+1}" />
							<tr>
								<beacon:label required="false"
									styleClass="content-field-label-top">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<html:checkbox property="characteristic[${charIndex}].displaySelected"
															value="true"/>&nbsp;
									<cms:contentText key="${characteristic.nameCmKey}"
										code="${characteristic.cmAssetCode}" />
								</beacon:label>
							</tr>
						</logic:iterate>
					</c:if>
				</logic:iterate>
				<tr class="form-blank-row" />

				<%--BUTTON ROWS --%>
				<beacon:authorize
					ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
					<tr class="form-buttonrow">
						<td colspan="2" />
						<td colspan="3">
						<table width="100%">
							<tr>

								<td class="left-align"><html:submit  property="submit"
									styleClass="content-buttonstyle"
									onclick="setDispatch('extract')">
									<cms:contentText code="admin.budgetextract.parameters"
										key="EXTRACT" />
								</html:submit> <c:url var="urlMaster"
									value="budgetMasterDisplay.do">
									<c:param name="id" value="${budgetMaster.id}" />
								</c:url> <html:button property="backToBudgetMaster"
									styleClass="content-buttonstyle"
									onclick="callUrl('${urlMaster}')">
									<cms:contentText code="system.button" key="CANCEL" />
								</html:button></td>
							</tr>
						</table>
						</td>
					</tr>
				</beacon:authorize>
			</html:form>
		</table>
		</td>
	</tr>
</table>
