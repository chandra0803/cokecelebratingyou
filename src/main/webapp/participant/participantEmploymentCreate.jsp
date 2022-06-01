<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--
function maintainPaxEmployment( method )
{
	getContentForm().method.value=method;
  getContentForm().action="participantEmploymentMaintainCreate.do";
	clearDateMask(getContentForm().previousTerminationDate);
}
//-->
</script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form styleId="contentForm" action="/participantEmploymentMaintainCreate">
	<html:hidden property="method" value="create"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${participantEmploymentCreateForm.userId}"/>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="CREATE_HEADER" code="participant.employment.history"/></span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline">
			<c:out value="${user.titleType.name}" />
		  	<c:out value="${user.firstName}" />
			<c:out value="${user.middleName}" />
			<c:out value="${user.lastName}" />
			<c:out value="${user.suffixType.name}" />		                
        </span>
        
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="participant.employment.history"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
       

        <%-- Start data inputs  --%>
        <table>
          <tr class="form-row-spacer">				  
            <beacon:label property="employerId" required="true">
              <cms:contentText key="EMPLOYER" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="employerId" styleClass="content-field">
				<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
				<html:options collection="employerList" property="id" labelProperty="name"  />
	          </html:select>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">				  
            <beacon:label property="position">
              <cms:contentText key="JOB_POSITION" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="position" styleClass="content-field">
				<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
				<html:options collection="jobPositionList" property="code" labelProperty="name"  />
	          </html:select>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">				  
            <beacon:label property="department">
              <cms:contentText key="DEPARTMENT" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">
				<html:select property="department" styleClass="content-field">
				<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="departmentList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>                   				
          
          <%-- Ex Date - note the 'calendar' class on the img tag --%>
          <tr class="form-row-spacer">				  
            <beacon:label property="hireDate">
              <cms:contentText key="HIRE_DATE" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">		  		  	
              <html:text property="hireDate" styleId="hireDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                <img id="hireDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='HIRE_DATE' code="participant.participant"/> "/>
            </td>              
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
	

		<%-- previous Termination date--%>
		<tr class="form-row-spacer">				  
            <beacon:label property="previousTerminationDate" required="true">
              <cms:contentText key="PREV_TERM_DATE" code="participant.employment.history"/>
            </beacon:label>	
            <td class="content-field">		  		  	
              <html:text property="previousTerminationDate" styleId="previousTerminationDate" size="10" maxlength="10"  readonly="true" styleClass="content-field" onfocus="clearDateMask(this);"/>
                <img id="previousTerminationDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key="PREV_TERM_DATE" code="participant.employment.history"/>"/>
            </td>              
        </tr> 
        
    <%--
    <c:choose>
      <c:when test="${participantEmploymentCreateForm.previousTerminationDate==''}">
          <tr class="form-row-spacer">				  
            <beacon:label property="previousTerminationDate" required="true">
              <cms:contentText key="PREV_TERM_DATE" code="participant.employment.history"/>
            </beacon:label>	
            <td class="content-field">		  		  	
              <html:text property="previousTerminationDate" styleId="previousTerminationDate" size="10" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);"/>
                
            </td>              
          </tr>                    		    
      </c:when>
      <c:otherwise>
        <html:hidden property="previousTerminationDate"/>
      </c:otherwise>
    </c:choose>
    
 	 --%>
     <%--BUTTON ROWS ... For Input--%>
         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <html:submit styleClass="content-buttonstyle" onclick="maintainPaxEmployment('create');">
               <cms:contentText key="SAVE_NEW_BUTTON" code="participant.employment.history"/>
             </html:submit>
             <html:cancel styleClass="content-buttonstyle">
               <cms:contentText code="system.button" key="CANCEL" />
			</html:cancel>             
           </td>
         </tr>
         <%--END BUTTON ROW--%>
      
        </table>
			
    </td>
  </tr>
</table>
</html:form>

<script type="text/javascript">
  Calendar.setup(
    {
      inputField  : "hireDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "hireDateTrigger"       // ID of the button
    }
  );
 

    Calendar.setup(
     {
      inputField  : "previousTerminationDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "previousTerminationDateTrigger"       // ID of the button
     }
    );
  
</script>

