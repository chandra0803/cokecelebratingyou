<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>
<%@ page import="com.biperf.core.domain.enums.ApprovalType" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<c:set var="disabled" scope="page" value="disabled='true'"/>
<c:set var="enabled" scope="page" value="disabled='false'"/>

<SCRIPT TYPE="text/javascript">
      function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "block";
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
      function hideLayer(whichLayer)
      { 
        if (document.getElementById)
        {        
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
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
      function approverChange()
      {
    	  var e = document.getElementById("defaultApproverSearchResultsBox");
    	  var user = e.options[e.selectedIndex].text;
    	  document.getElementById("defaultApproverNameSpan").innerHTML = user;
      }
  </SCRIPT>

<SCRIPT TYPE="text/javascript"> 

function frequencyChange(destination)
{
 //var selectObj = document.getElementById("customApproverTypeValue");
 //destination = selectObj.options[selectObj.selectedIndex].value;
 //var destination = $('select[name="promotionApprovalForm.customApproverValueBeanListAsList['+index+'].customApproverTypeValue"] option:selected').val();
 //do not do anything if the ?Select One? option is selected.
 if (destination) {
	 var index=destination.id;
 
	 
 if ( destination.value == 'award' || destination.value == 'behavior'  ||  destination.value == 'characteristic') {  
    showLayer("customApproverMsgLayer"+index);
  }else{
    hideLayer("customApproverMsgLayer"+index);
  } 
 
 
  if(destination.value == 'specific_approv' ){
		 showLayer("customSpecApproverLayer"+index);
	 }
	 else{
		 hideLayer("customSpecApproverLayer"+index);
	 }
 
  
  if ( destination.value == 'characteristic' ) {
	  showLayer("customApproverCharLayer"+index);
	  showLayer("customApproverRoutingLayer"+index);
  }
  else{
	  hideLayer("customApproverCharLayer"+index);
	  hideLayer("customApproverRoutingLayer"+index);
  }
  
 }
 else{
	 var listSize = document.promotionApprovalForm.customApproverValueBeanListCount.value ;
	 var awardGroupType = document.promotionApprovalForm.awardGroupMethod.value ;
	 
	 for(index=1; index<=listSize; index++)
	 {
		 var selectBox = document.getElementById(index).value;
		 if ( selectBox == 'award' || selectBox == 'behavior' ||  selectBox == 'characteristic' ) {  
		    showLayer("customApproverMsgLayer"+index);
		  }else{
		    hideLayer("customApproverMsgLayer"+index);
		  } 
		 
		 if(selectBox == 'specific_approv' ){
			 showLayer("customSpecApproverLayer"+index);
		 }
		 else{
			 hideLayer("customSpecApproverLayer"+index);
		 }
		 
		 
		  if ( selectBox == 'characteristic' ) {
			  showLayer("customApproverCharLayer"+index);
			  showLayer("customApproverRoutingLayer"+index);
			  if(awardGroupType != 'individual'){
				var e1 = document.getElementById("approverRoutingNumber"+index);
				  e1.selectedIndex = 1;
				 e1.setAttribute("disabled", "true");	
			  }else if(index!=1 ){
				for(index1=1; index1<=index-1; index1++)
				{
			  var selectBox1 = document.getElementById(index1).value;
			  if(selectBox1 == 'characteristic'){
				 
				  var e = document.getElementById("approverRoutingNumber"+index1);
				  var a = e.options.selectedIndex
				  var e1 = document.getElementById("approverRoutingNumber"+index);
				  e1.selectedIndex = a;
				 e1.setAttribute("disabled", "true");
			  }
				}
			  } 
			  
		  }
		  else{
			  hideLayer("customApproverCharLayer"+index);
			  hideLayer("customApproverRoutingLayer"+index);
		  }
	 }
 }
}

</SCRIPT>

<html:form styleId="contentForm" action="promotionApprovalSave">
	<html:hidden property="method" value=""/>
	<html:hidden property="promotionName"/>
	<html:hidden property="promotionTypeName"/>
	<html:hidden property="promotionTypeCode"/>
	<html:hidden property="promotionEndDate"/>
	<html:hidden property="version"/>
	<html:hidden property="participantApproverListCount"/>
	<html:hidden property="customApproverValueBeanListCount"/>
	<html:hidden property="participantSubmitterListCount"/>
	<html:hidden property="hasParent"/>
	<html:hidden property="hasChildren"/>
	<html:hidden property="behaviorActive"/>
	<html:hidden property="cumulativeNomination"/>
	<html:hidden property="promotionLive"/>
	
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionApprovalForm.promotionId}"/>
		<beacon:client-state-entry name="claimFormId" value="${promotionApprovalForm.claimFormId}"/>
	</beacon:client-state>

    <c:set var="promoTypeName" scope="request" value="${promotionApprovalForm.promotionTypeName}"/>
    <c:set var="promoTypeCode" scope="request" value="${promotionApprovalForm.promotionTypeCode}"/>
    <c:set var="promoName" scope="request" value="${promotionApprovalForm.promotionName}"/>
    <tiles:insert attribute="promotion.header"/>

    <br/><br/>


    <cms:errors/>

<table>
<tr class="form-row-spacer">
        <beacon:label property="code" required="true" styleClass="content-field-label-top">
          <cms:contentText code="promotion.approvals" key="APPROVAL_TYPE"/>
          <html:hidden property="scoreBy" value="${promotionApprovalForm.scoreBy}"  />
          <html:hidden property="reDisplay" value="${promotionApprovalForm.reDisplay}"  />
        </beacon:label>

  <c:choose>
  <c:when test="${promotionApprovalForm.promotionTypeCode != 'nomination'}">
  <td class="content-field">
  <html:select property="approvalType" styleClass="content-field"
	       onchange="setActionDispatchAndSubmit('promotionApproval.do','redisplay')"
	       disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') }" >
	<html:options collection="approvalTypes" property="code" labelProperty="name"/>
  </html:select>
  </td>
  
  </c:when>
  <c:otherwise>
  <td class="content-field-review">
   <%-- client customization wip#56492 start --%>
  		<html:select property="approvalType" styleClass="content-field"
		         onchange="setActionDispatchAndSubmit('promotionApproval.do','redisplay')"
		         disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') }" >
			<html:options collection="approvalTypes" property="code" labelProperty="name"/>
		</html:select>  
		<%-- client customization wip#56492 end --%>
  </td>
  </c:otherwise>
  </c:choose>
  
  
  </tr>

<tr class="form-blank-row">
  <td></td>
</tr>


<c:if test="${promotionApprovalForm.approvalType != 'auto_approve' and promotionApprovalForm.approvalType != 'auto_delayed'}">
  <tr>
    <td></td>
    <td class="content-field-label content-field-label-top"><cms:contentText code="promotion.approvals" key="APPROVAL_DATES"/>
    </td>
    <td>
        <table><tr>
          <beacon:label property="approvalStartDate" required="true" styleClass="content-field-label-top">
            <cms:contentText code="promotion.basics" key="START"/>
          </beacon:label>
          <td class="content-field">
             <html:text property="approvalStartDate" styleId="approvalStartDate" styleClass="content-field" size="13" readonly="true"  onfocus="clearDateMask(this);"
                       disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}"/>

            <c:choose>
              <c:when test="${  promotionApprovalForm.hasParent or  (promotionApprovalForm.promotionStatus=='expired') or 
                				(promotionApprovalForm.promotionStatus=='complete') or ( promotionApprovalForm.promotionLive == 'true' ) &&
                                ( promotionApprovalForm.approvalStartDate != '' )}">
                <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="img.calendar-icon" alt="<cms:contentText key='START_DATE' code='admin.budget.details'/>" border="0" />
              </c:when>
              <c:otherwise>
                <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="img.calendar-icon" alt="<cms:contentText key='START_DATE' code='admin.budget.details'/>" border="0" />
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
          <tr>
            <beacon:label property="approvalEndDate" required="false" styleClass="content-field-label-top">
            <cms:contentText code="promotion.basics" key="END"/>
          </beacon:label>
          <td class="content-field">
          <html:text property="approvalEndDate" styleId="approvalEndDate" styleClass="content-field" size="13" readonly="true"  onfocus="clearDateMask(this);"
                       disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}"/>
            <c:if test="${ !promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
              <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="img.calendar-icon" alt="<cms:contentText key='END_DATE' code='admin.budget.details'/>" border="0" />
            </c:if>
          </td>
          </tr>
        </table>
    </td>
  </tr>
  
  <script type="text/javascript">
        Calendar.setup(
        {
          inputField  : "approvalStartDate",           // ID of the input field
          ifFormat    : "${TinyMceDatePattern}",        // the date format
          button      : "startDateTrigger"    // ID of the button
        });
        Calendar.setup(
        {
          inputField  : "approvalEndDate",           // ID of the input field
          ifFormat    : "${TinyMceDatePattern}",        // the date format
          button      : "endDateTrigger"      // ID of the button
        });
      </script>
  
</c:if>
<tr class="form-blank-row">
  <td></td>
</tr>

<%-- client customization wip#58122 start --%>
<c:choose>
<c:when test="${(promotionApprovalForm.approvalType == 'coke_custom') && (promotionApprovalForm.approvalLevelPayout == 'true')}">
  <tr class="form-blank-row">
  <td></td>
</tr>

  <tr class="form-row-spacer">
      <beacon:label property="approvalNodeLevels" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/>
      </beacon:label>
  <td class="content-field">
    <html:text property="approvalNodeLevels" size="10"  maxlength="3" styleClass="content-field"  disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}"/>
  </td>
  </tr>
<tr class="form-blank-row">
  <td></td>
</tr>
</c:when>
</c:choose>
<%-- client customization wip#58122 start --%>

<c:choose>
<c:when test="${promotionApprovalForm.approvalType == 'auto_approve'}">
  <%-- Nothing additional to add --%>
</c:when>
<c:when test="${promotionApprovalForm.approvalType == 'auto_delayed'}">
  <tr class="form-row-spacer">
        <beacon:label property="approvalAutoDelayDays" required="true" styleClass="content-field-label-top">
          <cms:contentText code="promotion.approvals" key="APPROVAL_NUMBER_OF_DAYS"/>
        </beacon:label>
  <td class="content-field">
    <html:text property="approvalAutoDelayDays" size="10" maxlength="3" disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}" styleClass="content-field"/>
  </td></tr>
  <tr class="form-blank-row">
    <td></td>
  </tr>
</c:when>

<c:when test="${promotionApprovalForm.approvalType == 'manual'}">
  <%-- Nothing additional to add --%>
  		<c:if test="${(promotionApprovalForm.promotionLive == 'true' or promotionApprovalForm.promotionStatus=='complete') && (promotionApprovalForm.promotionTypeCode == 'nomination')}">
  				<html:hidden property="approverType"/>
  				<html:hidden property="approvalNodeLevels"/>
  		</c:if>
</c:when>

<c:when test="${promotionApprovalForm.approvalType == 'cond_nth'}">
  <tr class="form-row-spacer">
        <beacon:label property="approvalConditionalClaimCount" required="true" styleClass="content-field-label-top">
          <cms:contentText code="promotion.approvals" key="APPROVAL_REVIEW_EVERY"/>
        </beacon:label>
  <td class="content-field">
   <html:text property="approvalConditionalClaimCount" size="10" maxlength="3" disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}" styleClass="content-field"/>
  </td></tr>
  <tr class="form-blank-row">
    <td></td>
  </tr>
</c:when>

<c:when test="${promotionApprovalForm.approvalType == 'cond_amt'}">
    <tr class="form-row-spacer">
        <beacon:label property="approvalConditionalClaimFormStepElementId" required="true" styleClass="content-field-label-top">
          <cms:contentText code="promotion.approvals" key="APPROVAL_AMOUNT_FIELD"/>
        </beacon:label>
  <td class="content-field">
   <html:select property="approvalConditionalClaimFormStepElementId" styleClass="content-field"
                   disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/>
        </html:option>
        <html:options collection="numericClaimFormStepElements" property="id"
                      labelProperty="i18nLabel"/>
      </html:select>&nbsp;<cms:contentText code="promotion.approvals" key="APPROVAL_MUST_BE"/>&nbsp;
      <html:select property="approvalConditionalAmountOperator" styleClass="content-field"
                   disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/>
        </html:option>
        <html:options collection="approvalConditionalAmountOperators" property="code"
                      labelProperty="name"/>
      </html:select> &nbsp;
      <html:text property="approvalConditionalAmount" size="10" maxlength="40"
                 disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}" styleClass="content-field"/>
  </td></tr>
  <tr class="form-blank-row">
    <td></td>
  </tr>
</c:when>


<c:when test="${promotionApprovalForm.approvalType == 'cond_pax'}">
  <tr class="form-row-spacer">
    <beacon:label property="submitter" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="SUBMITTERS"/>
    </beacon:label>
    <td class="content-field">
<table>
        <tr>
          <td colspan="2">
            <display:table defaultsort="1" defaultorder="ascending" name="promotionApprovalForm.participantSubmitterList" id="submitter"
                           requestURI="promotionApproval.do">
             <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
              <display:column titleKey="promotion.approvals.CRUD_NAME_LABEL"
                              headerClass="crud-table-header-row" class="crud-content">
				<input type="hidden"
                       name="participantSubmitterList[<c:out value="${submitter_rowNum - 1}"/>].id"
                       value="<c:out value="${submitter.id}"/>">
                <input type="hidden"
                       name="participantSubmitterList[<c:out value="${submitter_rowNum - 1}"/>].participantId"
                       value="<c:out value="${submitter.participantId}"/>">
 				<input type="hidden"
                       name="participantSubmitterList[<c:out value="${submitter_rowNum - 1}"/>].firstName"
                       value="<c:out value="${submitter.firstName}"/>">
                <input type="hidden"
                       name="participantSubmitterList[<c:out value="${submitter_rowNum - 1}"/>].lastName"
                       value="<c:out value="${submitter.lastName}"/>">
                <c:out value="${submitter.lastName}"/>, <c:out value="${submitter.firstName}"/>
              </display:column>
              <display:column titleKey="promotion.approvals.CRUD_REMOVE_LABEL"
                              headerClass="crud-table-header-row" class="crud-content">
                <c:choose>
                  <c:when test="${ promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
                    <input type="checkbox"
                           name="participantSubmitterList[<c:out value="${submitter_rowNum - 1}"/>].remove"
                           value="Y" disabled>&nbsp;&nbsp;
                  </c:when>
                  <c:otherwise>
                    <input type="checkbox"
                           name="participantSubmitterList[<c:out value="${submitter_rowNum - 1}"/>].remove"
                           value="Y">&nbsp;&nbsp;
                  </c:otherwise>
                </c:choose>
              </display:column>
            </display:table>
          </td>
        </tr>
        <tr>
          <td>
            <% String addSubmitterOnClickCall = "setActionDispatchAndSubmit('promotionApproval.do?returnUrl==/"+request.getAttribute("modulePath")+"/promotionApproval.do?method=returnSubmitterLookup','prepareSubmitterLookup')";
              String removeSubmitterOnClickCall = "setActionDispatchAndSubmit('promotionApproval.do', 'removeSubmitters')";
            %>

            <%-- TODO: Set the correct url to lookup pax --%>
            <html:button property="add_submitter" styleClass="content-buttonstyle"
                         onclick="<%=addSubmitterOnClickCall%>"
                         disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
              <cms:contentText code="promotion.approvals" key="ADD_SUBMITTER"/>
            </html:button>
          </td>
          <td>
            <html:button property="remove" styleClass="content-buttonstyle"
                         onclick="<%=removeSubmitterOnClickCall%>"
                         disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
              <cms:contentText code="system.button" key="REMOVE_SELECTED"/>
            </html:button>
          </td>
        </tr>
      </table>
  </tr>

  <tr class="form-blank-row">
    <td></td>
  </tr>
</c:when>
</c:choose>

<%-- If the approval type is 'auto_approve' then it shouldn't refresh the page --%>
<c:if test="${!(promotionApprovalForm.approvalType == 'auto_approve')}">
  <c:choose>
    <c:when test="${promotionApprovalForm.promotionTypeCode != 'nomination'}">
      <tr class="form-row-spacer">
        <beacon:label property="approverType" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="APPROVER_TYPE"/>
        </beacon:label>
        <td class="content-field">
        	<% String approverTypeOnChange = "setActionDispatchAndSubmit('promotionApproval.do', 'redisplay')"; %>
        	<html:select property="approverType" styleClass="content-field"
         	    onchange="<%=approverTypeOnChange%>" 
       		    disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
        	  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
        	  <html:options collection="approverTypes" property="code" labelProperty="name"/>
        	</html:select>
        </td>
      </tr>
      <tr class="form-blank-row">
        <td></td>
      </tr>

<c:choose>
  <c:when test="${promotionApprovalForm.approverType == 'specific_approv'}">
    <tr class="form-row-spacer">
      <beacon:label property="approver" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="APPROVAL_APPROVERS"/>
      </beacon:label>
      <td class="content-field">
        <table>
          <tr>
            <td colspan="2">
              <display:table defaultsort="1" defaultorder="ascending" name="promotionApprovalForm.participantApproverList" id="approver"
                       requestURI="promotionApproval.do">
				<display:setProperty name="basic.msg.empty_list_row">
					<tr class="crud-content" align="left"><td colspan="{0}">
                      <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       	 </td></tr>
				 </display:setProperty>  
				 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>                     
              <display:column titleKey="promotion.approvals.CRUD_NAME_LABEL"
                          headerClass="crud-table-header-row" class="crud-content">
             <input type="hidden"
                       name="participantApproverList[<c:out value="${approver_rowNum - 1}"/>].id"
                       value="<c:out value="${approver.id}"/>">
                <input type="hidden"
                       name="participantApproverList[<c:out value="${approver_rowNum - 1}"/>].participantId"
                       value="<c:out value="${approver.participantId}"/>">
 				<input type="hidden"
                       name="participantApproverList[<c:out value="${approver_rowNum - 1}"/>].firstName"
                       value="<c:out value="${approver.firstName}"/>">
                <input type="hidden"
                       name="participantApproverList[<c:out value="${approver_rowNum - 1}"/>].lastName"
                       value="<c:out value="${approver.lastName}"/>">
                <c:out value="${approver.lastName}"/>, <c:out value="${approver.firstName}"/>
              </display:column>
         
              <display:column titleKey="promotion.approvals.CRUD_REMOVE_LABEL"
                              headerClass="crud-table-header-row" class="crud-content">
                <c:choose>
                  <c:when test="${ promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
                    <input type="checkbox" styleClass="content-field"
                           name="participantApproverList[<c:out value="${approver_rowNum - 1}"/>].remove"
                           value="Y" disabled>&nbsp;&nbsp;
                  </c:when>
                  <c:otherwise>
                    <input type="checkbox" styleClass="content-field"
                           name="participantApproverList[<c:out value="${approver_rowNum - 1}"/>].remove"
                           value="Y">&nbsp;&nbsp;
                  </c:otherwise>
                </c:choose>

              </display:column>
            </display:table>
          </td>
        </tr>
        <tr>
          <td>
            <%-- TODO: Set the correct url to lookup pax --%>
            <% String addApproverOnClickCall = "setActionDispatchAndSubmit('promotionApproval.do?returnUrl==/"+request.getAttribute("modulePath")+"/promotionApproval.do?method=returnApproverLookup','prepareApproverLookup')";
               String removeApproverOnClickCall = "setActionDispatchAndSubmit('promotionApproval.do', 'removeApprovers')";
            %>
        
            <html:button property="add_approver" styleClass="content-buttonstyle"
                         onclick="<%=addApproverOnClickCall%>"
                         disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
              <cms:contentText code="promotion.approvals" key="ADD_APPROVER"/>
            </html:button>
          </td>
          <td>
            <html:button property="remove" styleClass="content-buttonstyle"
                         onclick="<%=removeApproverOnClickCall%>"
                         disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
              <cms:contentText code="system.button" key="REMOVE_SELECTED"/>
            </html:button>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr class="form-blank-row">
    <td></td>
  </tr>
</c:when>

<c:when test="${promotionApprovalForm.approverType == 'submitter_mgr'}">
  <%-- Nothing additional to add --%>
</c:when>
<c:when test="${ promotionApprovalForm.approverType == 'node_owner' ||
                 promotionApprovalForm.approverType == 'nominator_node_owner' ||
                 promotionApprovalForm.approverType == 'nominee_node_owner'}">
  <% String nodeLookupLink = "javascript:setActionDispatchAndSubmit('promotionApproval.do?nodeLookupReturnUrl==/"+request.getAttribute("modulePath")+"/promotionApproval.do?method=returnNodeLookup','prepareNodeLookup');"; %>
  
<tr class="form-blank-row">
  <td></td>
</tr>

  <tr class="form-row-spacer">
      <beacon:label property="approvalNodeLevels" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/>
      </beacon:label>
  <td class="content-field">
    <html:text property="approvalNodeLevels" size="10"  maxlength="3" styleClass="content-field" onkeyup="setActionDispatchAndSubmit('promotionApproval.do','redisplay')" disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}"/>
  </td>
  </tr>
<tr class="form-blank-row">
  <td></td>
</tr>
</c:when>

<c:when test="${promotionApprovalForm.approverType == 'node_owner_by_type' ||
                promotionApprovalForm.approverType == 'nominator_node_owner_by_type' ||
                promotionApprovalForm.approverType == 'nominee_node_owner_by_type'}">
  <tr class="form-row-spacer">
    <beacon:label property="approvalHierarchyId" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_HIERARCHY"/>
    </beacon:label>
    <td class="content-field">
      <html:select property="approvalHierarchyId" styleClass="content-field"
                   disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') }" >
        <html:options collection="activeHierarchies" property="id" labelProperty="i18nName"/>
      </html:select>
    </td>
  </tr>

  <tr class="form-blank-row"><td colspan="3"></td></tr>

  <tr class="form-row-spacer">
    <beacon:label property="approvalNodeTypeId" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_TYPE"/>
    </beacon:label>
    <td class="content-field">
      <html:select property="approvalNodeTypeId" styleClass="content-field"
                   disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') }" >
        <html:options collection="nodeTypes" property="id" labelProperty="i18nName"/>
      </html:select>
    </td>
  </tr>

  <tr class="form-blank-row"><td colspan="3"></td></tr>

  <tr class="form-row-spacer">
    <beacon:label property="approvalNodeLevels" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/>
    </beacon:label>
    <td class="content-field">
      <html:text property="approvalNodeLevels" size="10" maxlength="3" styleClass="content-field" onkeyup="setActionDispatchAndSubmit('promotionApproval.do','redisplay')"
                 disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}"/>
    </td>
  </tr>

  <tr class="form-blank-row"><td colspan="3"></td></tr>
</c:when>
</c:choose>
</c:when>
     
<c:otherwise>
    <tr class="form-row-spacer">
    <beacon:label property="approverType" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVER_TYPE"/>
    </beacon:label>
<td class="content-field">
        <% String approverTypeOnChange = "setActionDispatchAndSubmit('promotionApproval.do', 'redisplay')"; %>
        <table>
        <tr>
	      <td class="content-field"><html:radio property="approverType" value="nominator_node_owner" onchange="<%=approverTypeOnChange%>" 
	        disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') or (promotionApprovalForm.cumulativeNomination == 'true') or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/></td>
	      <td class="content-field"><cms:contentText code="promotion.approvals" key="NOMINATOR_ORG_UNIT_OWNER"/></td>
	    </tr>
	    <tr>
	      <td class="content-field"><html:radio property="approverType" value = "nominator_node_owner_by_type" onchange="<%=approverTypeOnChange%>" 
	        disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')or (promotionApprovalForm.cumulativeNomination == 'true') or (promotionApprovalForm.promotionLive=='true' ) or (promotionApprovalForm.promotionStatus=='complete')}"/></td>
	      <td class="content-field"><cms:contentText code="promotion.approvals" key="NOMINATOR_ORG_UNIT_BY_TYPE"/></td>
	    </tr>
	    <tr>
	      <td class="content-field"><html:radio property="approverType" value="nominee_node_owner" onchange="<%=approverTypeOnChange%>" 
	        disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/></td>
	      <td class="content-field"><cms:contentText code="promotion.approvals" key="NOMINEE_ORG_UNIT_OWNER"/></td>
	    </tr>
	    <tr>
	      <td class="content-field"><html:radio property="approverType" value = "nominee_node_owner_by_type" onchange="<%=approverTypeOnChange%>" 
	        disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/></td>
	      <td class="content-field"><cms:contentText code="promotion.approvals" key="NOMINEE_ORG_UNIT_BY_TYPE"/></td>
	    </tr>
	    <tr>
	      <td class="content-field"><html:radio property="approverType" value="custom_approvers" onchange="<%=approverTypeOnChange%>" 
	        disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/></td>
	      <td class="content-field"><cms:contentText code="promotion.approvals" key="CUSTOM_APPROVER"/></td>
	    </tr>
	    </table>
	    
        <%-- <html:select property="approverType" styleClass="content-field"
            onchange="<%=approverTypeOnChange%>" 
           disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
        <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
        <html:options collection="approverTypes" property="code" labelProperty="name"/>
        </html:select> --%>
      </td>
        </tr>
<tr class="form-blank-row">
  <td></td>
</tr>

<c:choose>
<c:when test="${promotionApprovalForm.approverType == 'submitter_mgr'}">
  <%-- Nothing additional to add --%>
</c:when>
<c:when test="${ promotionApprovalForm.approverType == 'node_owner' ||
                 promotionApprovalForm.approverType == 'nominator_node_owner' ||
                 promotionApprovalForm.approverType == 'nominee_node_owner'}">
  <% String nodeLookupLink = "javascript:setActionDispatchAndSubmit('promotionApproval.do?nodeLookupReturnUrl==/"+request.getAttribute("modulePath")+"/promotionApproval.do?method=returnNodeLookup','prepareNodeLookup');"; %>
  
<tr class="form-blank-row">
  <td></td>
</tr>

  <tr class="form-row-spacer">
      <beacon:label property="approvalNodeLevels" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/>
      </beacon:label>
  <td class="content-field">
    <html:text property="approvalNodeLevels" size="10"  maxlength="3" styleClass="content-field" onkeyup="setActionDispatchAndSubmit('promotionApproval.do','redisplay')" disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/>
  </td>
  </tr>
<tr class="form-blank-row">
  <td></td>
</tr>
</c:when>

<c:when test="${promotionApprovalForm.approverType == 'node_owner_by_type' ||
                promotionApprovalForm.approverType == 'nominator_node_owner_by_type' ||
                promotionApprovalForm.approverType == 'nominee_node_owner_by_type'}">
  <tr class="form-row-spacer">
    <beacon:label property="approvalHierarchyId" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_HIERARCHY"/>
    </beacon:label>
    <td class="content-field">
      <html:select property="approvalHierarchyId" styleClass="content-field"
                   disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}" >
        <html:options collection="activeHierarchies" property="id" labelProperty="i18nName"/>
      </html:select>
    </td>
  </tr>

  <tr class="form-blank-row"><td colspan="3"></td></tr>

  <tr class="form-row-spacer">
    <beacon:label property="approvalNodeTypeId" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_TYPE"/>
    </beacon:label>
    <td class="content-field">
      <html:select property="approvalNodeTypeId" styleClass="content-field"
                   disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}" >
        <html:options collection="nodeTypes" property="id" labelProperty="i18nName"/>
      </html:select>
    </td>
  </tr>

  <tr class="form-blank-row"><td colspan="3"></td></tr>

  <tr class="form-row-spacer">
    <beacon:label property="approvalNodeLevels" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/>
    </beacon:label>
    <td class="content-field">
      <html:text property="approvalNodeLevels" size="10" maxlength="3" styleClass="content-field" onkeyup="setActionDispatchAndSubmit('promotionApproval.do','redisplay')"
                 disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/>
    </td>
  </tr>

  <tr class="form-blank-row"><td colspan="3"></td></tr>
</c:when>

  <c:when test="${ promotionApprovalForm.approverType == 'custom_approvers'}">  
     <tr class="form-blank-row">
       <td></td>
     </tr>
     
     <tr class="form-row-spacer">
      
      <td class="content-field">
      	<beacon:label property="approvalNodeLevels" required="false" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="UPLOAD_APPROVER_FILE_MESSAGE"/>
      </beacon:label>       
      </td>
     </tr>
     
     <tr class="form-row-spacer">
      
      <td class="content-field">
      	<beacon:label property="approvalNodeLevels" required="false" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="PREVIOUS_APPROVERS_DELETE_MESSAGE"/>
      </beacon:label> 
      </td>
     </tr>
     
     <tr class="form-row-spacer">
      <beacon:label property="approvalNodeLevels" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/>
      </beacon:label>
      <td class="content-field">
         <html:text property="approvalNodeLevels" size="10"  maxlength="3" styleClass="content-field" onkeyup="setActionDispatchAndSubmit('promotionApproval.do','redisplay')" disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired') or (promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"/>
         <cms:contentText code="promotion.approvals" key="LEVEL_RANGE"/>
      </td>
     </tr> 
	
	<%
	  int eachLevelIndex = 1;
	%>
	
    <c:forEach var="customApproverValueBean" items="${promotionApprovalForm.customApproverValueBeanListAsList}" varStatus="approverTableCount" >   
    <tr class="form-row-spacer">
      <beacon:label property="customApproverTypeValue" required="true" styleClass="content-field-label-top">
        <cms:contentText code="promotion.approvals" key="CUSTOM_LEVEL"/> <c:out value="${customApproverValueBean.level}"></c:out>
      </beacon:label>
      <td  class="content-field"> 
        <table>
        <tr class="form-row-spacer" id="customApproverType${customApproverValueBean.level}"> 
          <td class="content-field"> 
            <html:hidden property="approverOptionId" name="customApproverValueBean" indexed="true"/>
            <html:hidden property="level" name="customApproverValueBean"  value="${customApproverValueBean.level}" indexed="true"/>
            <c:if test="${ promotionApprovalForm.promotionLive=='true' or promotionApprovalForm.promotionStatus=='complete'}" >
              <html:hidden property="customApproverTypeValue" name="customApproverValueBean"  value="${customApproverValueBean.customApproverTypeValue}" indexed="true"/>  
            </c:if>  
            <html:select styleId="${customApproverValueBean.level}" property="customApproverTypeValue" indexed="true" name="customApproverValueBean" onchange="frequencyChange(this);setActionDispatchAndSubmit('promotionApproval.do','redisplayList');" disabled="${(promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}" styleClass="content-field">
		      <html:optionsCollection name="promotionApprovalForm" property="customApproverTypes" value="code" label="name" />
	        </html:select> 
	      </td>
	      <td  class="content-field" id="customApproverMsgLayer${customApproverValueBean.level}">
	      <c:choose>
			<c:when test="${empty customApproverValueBean.nomApproverList}">
				<div><cms:contentText code="promotion.approvals" key="UPLOAD_APPROVER_FILE"/></div>
			</c:when>
		 <c:otherwise>	
			        <a  href='#modal1<c:out value="${customApproverValueBean.approverOptionId}" />' rel="tomodal[static|wider]"><cms:contentText code="promotion.approvals" key="VIEW_APPROVER_LIST"/></a>	
		</c:otherwise>
		  </c:choose>
		</td>
	    </tr>
  	<tr class="form-row-spacer" id="customApproverCharLayer${customApproverValueBean.level}">
  	  
  	   <td class="content-field">
  	   <DIV id="customApproverCharLayer${customApproverValueBean.level}">
  	     	<c:if test="${ promotionApprovalForm.promotionLive=='true'}" >
	          <html:hidden property="characteristicId" name="customApproverValueBean"  value="${customApproverValueBean.characteristicId}" indexed="true"/>  
            </c:if>
	     <html:select styleId="approverCharNumber${customApproverValueBean.level}" property="characteristicId" indexed="true" name="customApproverValueBean" onchange="setActionDispatchAndSubmit('promotionApproval.do','redisplayList')" disabled="${(promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"  styleClass="content-field">
	           <html:option value=''>
      			<cms:contentText key="SELECT_ONE" code="system.general" />
    		   </html:option>
	       <html:optionsCollection name="customApproverValueBean" property="characteristics" value="id" label="characteristicName" />
	     </html:select> 
	      </DIV>
	  </td>
  	</tr> 
  	
  	<tr class="form-row-spacer" id="customSpecApproverLayer${customApproverValueBean.level}">  	
	      <td class="content-field">
	        <table>
	          <tr>
	            <td colspan="2">
	              <display:table defaultsort="1" defaultorder="ascending" name='promotionApprovalForm.customApproverValueBean[${customApproverValueBean.level - 1}].approverListAsList' id="paxApprovalBean"
	                       requestURI="promotionApproval.do">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left"><td colspan="{0}">
	                      <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
	                       	 </td></tr>
					 </display:setProperty>  
					 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>                     
	              <display:column titleKey="promotion.approvals.CRUD_NAME_LABEL"
	                          headerClass="crud-table-header-row" class="crud-content">
	               <input type="hidden"
	                       name="customApproverValueBean[<c:out value="${customApproverValueBean.level -1 }" />].approver[<c:out value="${paxApprovalBean_rowNum-1 }" />].id"
	                       value="<c:out value="${paxApprovalBean.id}"/>" >
	                 <input type="hidden"
	                       name="customApproverValueBean[<c:out value="${customApproverValueBean.level -1 }" />].approver[<c:out value="${paxApprovalBean_rowNum-1 }" />].participantId"
	                       value="<c:out value="${paxApprovalBean.participantId}"/>" >
	                 <input type="hidden"
	                       name="customApproverValueBean[<c:out value="${customApproverValueBean.level - 1}" />].approver[<c:out value="${paxApprovalBean_rowNum-1 }" />].firstName"
	                       value="<c:out value="${paxApprovalBean.firstName}"/>" >
	                <input type="hidden"
	                       name="customApproverValueBean[<c:out value="${customApproverValueBean.level-1}" />].approver[<c:out value="${paxApprovalBean_rowNum-1 }"  />].lastName"
	                       value="<c:out value="${paxApprovalBean.lastName}"/>"  >
	                <c:out value="${paxApprovalBean.lastName}"/>, <c:out value="${paxApprovalBean.firstName}"/>
	              </display:column>
	         
	              <display:column titleKey="promotion.approvals.CRUD_REMOVE_LABEL"
	                              headerClass="crud-table-header-row" class="crud-content">
	                <c:choose>
	                  <c:when test="${ promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}">
	                    <input type="checkbox" styleClass="content-field"
	                           name="customApproverValueBean[<c:out value="${customApproverValueBean.level - 1}" />].approver[<c:out value="${paxApprovalBean_rowNum-1 }"/>].remove"
	                           value="Y" >&nbsp;&nbsp;
	                  </c:when>
	                  <c:otherwise>
	                    <input type="checkbox" styleClass="content-field"
	                           name="customApproverValueBean[<c:out value="${customApproverValueBean.level - 1}" />].approver[<c:out value="${paxApprovalBean_rowNum-1 }"/>].remove"
	                           value="Y" >&nbsp;&nbsp;
	                  </c:otherwise>
	                </c:choose>
	
	              </display:column>
	            </display:table>
	          </td>
	        </tr>
	        <tr>
	          <td>
			      <%-- TODO: Set the correct url to lookup pax --%>
			      <% String addApproverOnClickCall = "setActionDispatchAndSubmit('promotionApproval.do?returnUrl==/"+request.getAttribute("modulePath")+"/promotionApproval.do?method=returnApproverLookup&levelId="+eachLevelIndex+"','prepareApproverLookup')";
				     String removeApproverOnClickCall = "setActionDispatchAndSubmit('promotionApproval.do?levelId="+eachLevelIndex+"', 'removeCustomApprovers')";
			      %>
	        
	            <html:button property="add_approver" styleClass="content-buttonstyle" onchange="frequencyChange('${customApproverValueBean.level}')"
					 onclick="<%=addApproverOnClickCall%>"
					 disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}" indexed="true">
	              <cms:contentText code="promotion.approvals" key="ADD_APPROVER"/>
	            </html:button>
	          </td>
	          <td>
	            <html:button property="remove" styleClass="content-buttonstyle"
					 onclick="<%=removeApproverOnClickCall%>"
					 disabled="${promotionApprovalForm.hasParent or (promotionApprovalForm.promotionStatus=='expired')}" indexed="true" >
	              <cms:contentText code="system.button" key="REMOVE_SELECTED"/>
	            </html:button>
	          </td>
	        </tr>
	      </table>
	    </td>
	  </tr>
     
     <tr class="form-row-spacer" id="customApproverRoutingLayer${customApproverValueBean.level}">
  	 
  	   <td class="content-field">
  	   <DIV id="customApproverRoutingLayer${customApproverValueBean.level}">
  	     	<c:if test="${ promotionApprovalForm.promotionLive=='true'}" >
	          <html:hidden styleId="customApproverRoutingTypeId" property="customApproverRoutingTypeValue" name="customApproverValueBean"  value="${customApproverValueBean.customApproverRoutingTypeValue}" indexed="true"/>  
            </c:if>
	     <html:select styleId="approverRoutingNumber${customApproverValueBean.level}" property="customApproverRoutingTypeValue" indexed="true" name="customApproverValueBean" onchange="setActionDispatchAndSubmit('promotionApproval.do','redisplayList')" disabled="${(promotionApprovalForm.promotionLive=='true') or (promotionApprovalForm.promotionStatus=='complete')}"  styleClass="content-field">
	           <html:option value='select'>
      			<cms:contentText key="SELECT_ONE" code="system.general" />
    		   </html:option>
	       <html:optionsCollection name="customApproverValueBean" property="customApproverRoutingList" value="code" label="name" />
	     </html:select> 
	      </DIV>
	  </td>
  	</tr> 
     </table>        
    </td>
    </tr>
    <% eachLevelIndex = eachLevelIndex + 1; %> 
    </c:forEach>			    

    <tr class="form-blank-row">
      <td></td>
    </tr>
  </c:when>
</c:choose>
</c:otherwise>
</c:choose>
</c:if>

<%-- If the approval type is 'auto_approve' then it shouldn't refresh the page --%>
<tr valign="top">
<c:choose>
<c:when test="${promotionApprovalForm.promotionTypeCode != 'nomination'}">

<tr class="form-row-spacer">
    <beacon:label property="displayApproved" required="false" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="APPROVAL_OPTIONS"/>
    </beacon:label>
<td class="content-field">
    <table>
      <tr>
        <td valign="top" class="crud-content">
          <%-- Approved will always be checked and disabled --%>
          <input type="checkbox" name="displayApproved" styleClass="content-field" checked disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="APPROVED"/></input>
        </td>
      </tr>
      <tr>
        <td valign="top" class="crud-content">
          <%-- Pending will always be checked and disabled --%>
          <input type="checkbox" name="displayPending" styleClass="content-field" checked disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="PENDING"/></input>
        </td>
      </tr>
      <c:forEach items="${promoApprovalOptions}" var="promoApprovalOption">
        <tr>
          <td valign="top" class="crud-content">
            <c:if test="${ (promoApprovalOption.code != 'approved') &&
                           (promoApprovalOption.code != 'pending') }">
              <c:if test="${ (promoApprovalOption.code == 'denied') }">
                <input type="checkbox" name="displayDenied" checked styleClass="content-field" disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="DENIED"/></input>
                <html:hidden property="promotionApprovalOptions" value="denied"/>
              </c:if>
              <c:if test="${ (promoApprovalOption.code == 'held') }">
                <html:multibox property="promotionApprovalOptions" styleClass="content-field"
                               value="${promoApprovalOption.code}"
                               disabled="${promotionApprovalForm.hasParent or promotionApprovalForm.promotionStatus=='expired'}"/>
                <c:out value="${promoApprovalOption.name}"/>
              </c:if>
            </c:if>
          </td>
          <td>
            <c:if test="${ promoApprovalOption.code=='held' }">
              <html:select property="heldPromotionApprovalOptionReasons" multiple="multiple"
                           size="5" disabled="${promotionApprovalForm.hasParent or promotionApprovalForm.promotionStatus=='expired'}">
                <html:options collection="promoApprovalOptionReasons" property="code"
                              labelProperty="name" filter="false"/>
              </html:select>
            </c:if>
            <c:if test="${ promoApprovalOption.code=='denied' }">
              <html:select property="deniedPromotionApprovalOptionReasons" multiple="multiple"
                           size="5" disabled="${promotionApprovalForm.hasParent or promotionApprovalForm.promotionStatus=='expired'}">
                <html:options collection="promoApprovalOptionReasons" property="code"
                              labelProperty="name" filter="false"/>
              </html:select>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </table>
  </td>
</tr>

</c:when>
<%-- nomination approval options --%>
<c:otherwise>
<html:hidden property="awardGroupMethod"/>
<html:hidden property="evaluationType"/>
<html:hidden property="nomPublicationDate"/>

<tr class="form-row-spacer">
<beacon:label property="displayApproved" required="false" styleClass="content-field-label-top">
  <cms:contentText code="promotion.approvals" key="APPROVAL_OPTIONS"/>
</beacon:label>
<td class="content-field">
<table>

  <tr>
    <td valign="top" class="crud-content">
      <%-- Pending will always be checked and disabled --%>
      <input type="checkbox" name="displayPending" styleClass="content-field" checked disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="PENDING"/></input>
    </td>
  </tr>
  <tr>
  <td valign="top" class="crud-content">
    <%-- Approved will always be checked and disabled --%>
    <input type="checkbox" name="displayWinner" styleClass="content-field" checked disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="WINNER"/></input>
  </td>
  </tr>

  <tr>
  <td valign="top" class="crud-content">
    <input type="checkbox" name="displayNonWinner" styleClass="content-field" checked disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="NON_WINNER"/></input>
    
  </td>
  </tr>
  <tr>
  <td valign="top" class="crud-content">

    <input type="checkbox" name="displayExpired" styleClass="content-field" checked disabled>&nbsp;&nbsp;<cms:contentText code="promotion.approvals" key="EXPIRED"/></input>
  </td>
  </tr>

  
</table>
</td>
</tr>

</c:otherwise>
</c:choose>

<%-- Default Approver --%>
<c:if test="${promotionApprovalForm.promotionTypeCode == 'nomination'}">
  <tr>
    <beacon:label property="defaultApprover" required="true" styleClass="content-field-label-top">
      <cms:contentText code="promotion.approvals" key="DEFAULT_APPROVER" />
    </beacon:label>
    <td>
      <table>
        <tr class="form-row-spacer">
          <td class="content-field-label">
            <table>
              <tr>
                <td class="content-field" valign="top">
                  <html:hidden property="defaultApproverId" value="${promotionApprovalForm.defaultApproverId}" styleId="defaultApproverIdHidden" />
                  <span id="defaultApproverNameSpan">
                    <c:out value="${defaultApproverName}" />&nbsp;
                  </span>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr class="form-row-spacer">
          <td class="content-field-label">
            <cms:contentText code="participant.search" key="SEARCH_BY_LAST_NAME" />
          </td>
          <td class="content-field">
            <table>
              <tr>
                <td>
                  <html:text styleId="searchDefaultApproverLastName" property="defaultApproverSearchLastName" size="15" styleClass="content-field" />
                </td>
                <td>&nbsp;&nbsp;&nbsp;</td>
                <td>
                  <html:button styleId="defaultApproverSearchSubmitButton" property="defaultApproverSearch" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionApproval.do', 'searchDefaultApprover')" >
                    <cms:contentText code="system.button" key="SEARCH" />
                  </html:button>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr class="form-row-spacer">
          <td class="content-field-label">
          </td>
          <td class="content-field">
            <c:choose>
              <c:when test="${empty defaultApproverSearchResults}">
                <cms:contentText code="system.general" key="SEARCH_RESULTS" />
                (<span id="defaultApproverSearchResultsCount">0</span>):
                <br/>
                <html:select property="selectedDefaultApproverUserId" styleId="defaultApproverSearchResultsBox" size="5" style="width: 430px" styleClass="killme" />
                <html:hidden property="selectedDefaultApproverUserId" value="${promotionApprovalForm.selectedDefaultApproverUserId}" styleId="defaultApproverIdHidden" />
              </c:when>

              <c:otherwise>
                <cms:contentText code="system.general" key="SEARCH_RESULTS" />&nbsp;
                (<SPAN ID="defaultApproverSearchResultsCount"><c:out value="${defaultApproverSearchResultsCount}"/></SPAN>):
                <br/>
                <html:select property="selectedDefaultApproverUserId" styleId="defaultApproverSearchResultsBox" size="5" style="width: 430px" styleClass="killme" onchange="approverChange()">
                  <html:options collection="defaultApproverSearchResults" property="id" labelProperty="LFComma" filter="false"/>
                </html:select>
                <html:hidden property="selectedDefaultApproverUserId" value="${promotionApprovalForm.selectedDefaultApproverUserId}" styleId="defaultApproverIdHidden" />
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</c:if>
<%-- Default Approver --%>

<tr><td colspan=2></td>
  <td><tiles:insert attribute="promotion.footer"/></td>
</tr>
</table>

<script>
 <%-- On Page the nominationAwardSpecifierTypeCode is disabled and its default value is choose one, when fixed amount is the checked option--%>
	  $(document).ready(function() {
		  frequencyChange();
		  
		  if($('div.modal').length >= 1){
		  var modalDom = $('div.modal');
		    $('div.modal').remove();
		    $('body').append(modalDom);
		  }
		  
		  
		  // Hide the default approver section when the page loads, if it needs to be
		  if($("#defaultApproverFalse").attr("checked"))
		  {
			  hideDefaultApproverSection();
		  }
	  });
  </script>
    
</html:form>
 <%@include file="nominationApproverModal.jsp" %>
 