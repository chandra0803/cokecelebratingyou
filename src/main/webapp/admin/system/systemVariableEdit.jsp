<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form styleId="contentForm" action="systemVariableEdit" focus="entityName">
  <html:hidden property="method" />
  <html:hidden property="entityName" /> 
  <html:hidden property="key" /> 
  <html:hidden property="editable" value="${systemVariableForm.editable }" />
  <html:hidden property="viewable" value="${systemVariableForm.viewable }"/>
  <html:hidden property="groupName" value="${systemVariableForm.groupName }"/>  
	<beacon:client-state>
	  <beacon:client-state-entry name="entityId" value="${systemVariableForm.entityId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADING" code="admin.sys.var.edit"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="admin.sys.var.edit"/>
        </span>
        <br/><br/>        
        <cms:errors/>

		  <table>	    
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="VAR_NAME" code="admin.sys.var.list"/></td>
			  <td><c:out value="${systemVariableForm.entityName}"/></td>
			</tr>
			
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="GROUP_NAME" code="admin.sys.var.list"/></td>
			  <td><c:out value="${systemVariableForm.groupName}"/></td>
			</tr>
			
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="VAR_DESCRIPTION" code="admin.sys.var.list"/></td>
		      <td><c:out value="${systemVariableForm.key}"/></td>
		    </tr>  
		    
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="VAR_TYPE" code="admin.sys.var.list"/></td>
			  <td class="content-field">
		        <html:select property="typeCode" onchange="showHiddenIfValue('calendarButton', this.options[this.selectedIndex].value, 7);" styleId="typeCode">
			      <html:options collection="typeList" property="code" labelProperty="name"  />
			    </html:select>
			  </td>
			</tr>
			<tr class="form-row-spacer">
			  <td valign="top" class="content-field-label"><cms:contentText key="VAR_VALUE" code="admin.sys.var.list"/></td>
			  <td class="content-field"><html:textarea styleId="stringVal" name="systemVariableForm" property="stringVal"  rows="5" cols="40"/></td><td>
		        <span id="calendarButton" style="display:none;">&nbsp;&nbsp;
		          <img id="stringValTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" style="cursor: hand"/>
		        </span>
			  </td>
			</tr>	    	    	    	    
		    <tr class="form-buttonrow">
		       <td></td>
		       <td align="left">
	             <beacon:authorize ifNotGranted="LOGIN_AS">
	             	<c:if test="${systemVariableForm.editable == 'true'}">
					   <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
					  		<cms:contentText key="SAVE" code="system.button"/>
					   </html:submit>	             	
					</c:if>
	           	 </beacon:authorize>
					&nbsp;&nbsp;
					<html:cancel styleClass="content-buttonstyle">
					  <cms:contentText key="CANCEL" code="system.button"/>
					</html:cancel>
		       </td>
		     </tr>	    	    	    	    
          </table>
      </td>
     </tr>        
   </table>
</html:form>

<script type="text/javascript">
Calendar.setup(
    {
      inputField  : "stringVal",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "stringValTrigger"       // ID of the button
    }
);

function showHiddenIfValue(hiddenId, value, expectedValue) {
   block = document.getElementById(hiddenId).style;
   if (value == expectedValue) {
      block.display = "block";
      document.getElementById('stringVal').readOnly=true;
   }
   else {
      block.display = "none";
      document.getElementById('stringVal').readOnly=false;
   }
}

showHiddenIfValue('calendarButton', document.systemVariableForm.typeCode.options[document.systemVariableForm.typeCode.selectedIndex].value, 7);
</script>
