<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="resendWelcomeEmail">
	<html:hidden property="method" value="resendWelcomeEmail" />
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${user.id}"/>
	</beacon:client-state>
	
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline">
        	<cms:contentText key="TITLE" code="participant.resend.welcome.email"/>
        </span>
        <br/><br/>
        
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="participant.resend.welcome.email"/>
        </span>
        <br/><br/>
     	
        <cms:errors/>
    	
    	<table>
          <tr class="form-row-spacer">				  
            <td colspan="2">
            	<cms:contentText key="HEADING" code="participant.resend.welcome.email"/>
			</td>
          </tr>

          <tr class="form-blank-row">
            <td colspan="2"></td>
          </tr>	        

          <tr class="form-row-spacer">				  
			<td class="content-field-label">
              <cms:contentText key="PAX_NAME" code="participant.resend.welcome.email"/>
            </td>	
            <td class="content-field">
              <c:out value="${user.nameLFMWithComma}" />
            </td>
          </tr>

          <tr class="form-blank-row">
            <td colspan="2"></td>
          </tr>	        

          <tr class="form-row-spacer">				  
			<td class="content-field-label">
              <cms:contentText key="PAX_ID" code="participant.resend.welcome.email"/>
            </td>	
            <td class="content-field">
              <c:out value="${user.userName}" />
            </td>
          </tr>

          <tr class="form-blank-row">
            <td colspan="2"></td>
          </tr>	        
          
			<tr class="form-buttonrow">
	           <td align="left" colspan="2">
               <beacon:authorize ifNotGranted="LOGIN_AS">
		            <html:submit styleClass="content-buttonstyle" onclick="setDispatch('resendWelcomeEmail')">
					  <cms:contentText code="system.button" key="SEND" />
					</html:submit>
               </beacon:authorize>
	
					<html:button property="cancelBtn" styleClass="login-buttonstyle" onclick="setActionDispatchAndSubmit('participantDisplay.do','display')">
					  <cms:contentText code="system.button" key="CANCEL" />
					</html:button>
	           </td>
	         </tr>				

        </table>
      </td>
     </tr>        
   </table>
</html:form>
