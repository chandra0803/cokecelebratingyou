<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form styleId="contentForm" action="systemVariableCreate" focus="entityName">
  <html:hidden property="method" />
	<beacon:client-state>
	  <beacon:client-state-entry name="entityId" value="${systemVariableForm.entityId}"/>
	</beacon:client-state>
  
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADING" code="admin.sys.var.create"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="admin.sys.var.create"/>
        </span>
        <br/><br/>

        <cms:errors/>  
  
		  <table>
		    <tr class="form-row-spacer">				  
		      <beacon:label property="entityName" required="true">
		        <cms:contentText key="VAR_NAME" code="admin.sys.var.list"/>
		      </beacon:label>	
		      <td class="content-field">
		        <html:text name="systemVariableForm" property="entityName" styleClass="content-field" size="50"/>
		      </td>
		    </tr>
		    <tr class="form-row-spacer">				  
		      <beacon:label property="groupName" required="true">
		        <cms:contentText key="GROUP_NAME" code="admin.sys.var.list"/>
		      </beacon:label>	
		      <td class="content-field">
		        <html:text name="systemVariableForm" property="groupName" styleClass="content-field" size="40"/>
		      </td>
		    </tr>		    
		    <tr class="form-row-spacer">				  
		      <beacon:label property="key" required="true">
		        <cms:contentText key="VAR_DESCRIPTION" code="admin.sys.var.list"/>
		      </beacon:label>	
		      <td class="content-field">
		        <html:text name="systemVariableForm" property="key" styleClass="content-field" size="50"/>
		      </td>
		    </tr>
		    <tr class="form-row-spacer">			
		      <beacon:label property="typeCode" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="VAR_TYPE" code="admin.sys.var.list"/>
		      </beacon:label>	        
		      <td class="content-field">
		        <html:select property="typeCode" onchange="showHiddenIfValue('calendarButton', this.options[this.selectedIndex].value, 7);" styleId="typeCode" >
			      <html:options collection="typeList" property="code" labelProperty="name"  />
			    </html:select>
		      </td>  	
		    </tr>	    	    
		    <tr class="form-row-spacer">				  
		      <beacon:label property="stringVal" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="VAR_VALUE" code="admin.sys.var.list"/>
		      </beacon:label>	
		      <td class="content-field">
		      	<html:textarea styleId="stringVal" name="systemVariableForm" property="stringVal"  rows="5" cols="40"/></td><td>
		        <span id="calendarButton" style="display:none;">&nbsp;&nbsp;
		          <img id="stringValTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" style="cursor: hand"/>
		        </span>        
		      </td>
		    </tr>
		    <tr class="form-buttonrow">
		      <td></td>
		      <td></td>
		      <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
			    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
				  <cms:contentText key="SAVE" code="system.button"/>
				</html:submit>
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

</script>

<SCRIPT language="JavaScript" type="text/javascript">
  function displayCalendarIconOnLoad() {
    var dateType = '<%= com.opensymphony.module.propertyset.PropertySet.DATE %>';
    var formType = '<c:out value="${systemVariableForm.typeCode}" />';
    
    if(dateType == formType){
    	block = document.getElementById('calendarButton').style;
    	block.display = "block";
    }
    
  }
</SCRIPT>

<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
  onload = displayCalendarIconOnLoad;
</SCRIPT>

