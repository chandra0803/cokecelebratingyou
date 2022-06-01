<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form styleId="contentForm" action="participantMaintainUpdatePersonalInfo">
	<html:hidden property="method"/>
	<html:hidden property="version"/>
	<html:hidden property="active"/>
	<html:hidden property="originalPaxTermsAcceptFromDB"/>
	<html:hidden property="originalPaxStatusFromDB"/>
	<html:hidden property="ssnEditable"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${userForm.id}"/>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="UPDATE_PERSONAL_TITLE" code="participant.participant"/></span>
        
           <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="NOTE" code="participant.participant"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
	
		<table>
          <tr class="form-row-spacer">				  
            <beacon:label property="paxStatus" required="true">
              <cms:contentText key="PARTICIPANT_STATUS" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">              
				<html:select property="paxStatus" styleClass="content-field">
					<html:options collection="paxStatusList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>	        

 		  <tr class="form-row-spacer">				  
            <beacon:label property="suspensionStatus">
              <cms:contentText key="SUSPENSION_STATUS" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">              				
				<html:select property="suspensionStatus" styleClass="content-field">
					<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
					<html:options collection="suspensionStatusList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>	  			 
		
 		  <tr class="form-row-spacer">				  
            <beacon:label property="title">
              <cms:contentText key="TITLE" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 
				<html:select property="title" styleClass="content-field">
					<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
					<html:options collection="titleList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>	  			 


 		  <tr class="form-row-spacer">				  
            <beacon:label property="firstName" required="true">
              <cms:contentText key="FIRST_NAME" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 
				<html:text property="firstName" size="30" maxlength="40" styleClass="content-field"/>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>	  			 

		  <tr class="form-row-spacer">				  
            <beacon:label property="middleName">
              <cms:contentText key="MIDDLE_NAME" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 
				<html:text property="middleName" size="30" maxlength="40" styleClass="content-field"/>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>	  										


		  <tr class="form-row-spacer">				  
            <beacon:label property="lastName" required="true">
              <cms:contentText key="LAST_NAME" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 
				<html:text property="lastName" size="30" maxlength="40" styleClass="content-field"/>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>								

  		  <tr class="form-row-spacer">				  
            <beacon:label property="suffix">
            	<cms:contentText key="SUFFIX" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 				
				<html:select property="suffix" styleClass="content-field">
					<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="suffixList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>									
				<%-- Code Fix for bug:19064 the input type for SSN field is changed from text to password --%>
 		  <tr class="form-row-spacer">				  
            <beacon:label property="ssn">
            	<cms:contentText key="SSN" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 
            <c:choose>
            <c:when test="${userForm.ssnEditable == true}">
				<html:password property="ssn" size="30" styleClass="content-field"/>
			</c:when>
			<c:otherwise>
			<html:password property="ssn" size="30" disabled="true" styleClass="content-field"/>
			</c:otherwise>
			</c:choose>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>	

				
 		  <tr class="form-row-spacer">				  
            <beacon:label property="dateOfBirth">
            	<cms:contentText key="DATE_OF_BIRTH" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field">            		
				<html:text property="dateOfBirth" styleId="dateOfBirth" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                <img id="dateOfBirthTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key="DATE_OF_BIRTH" code="participant.participant"/>"/>
            </td>
          </tr>
       
          <tr class="form-blank-row">
            <td></td>
          </tr>							
									
 		  <tr class="form-row-spacer">				  
            <beacon:label property="gender">
            	<cms:contentText key="GENDER" code="participant.participant"/>
            </beacon:label>	
            <td class="content-field"> 								
				<html:select property="gender" styleClass="content-field">
					<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="genderList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
          
          <c:if test="${userForm.showSSOId}">
          <tr class="form-blank-row">
            <td></td>
          </tr>

		  <tr class="form-row-spacer">				  
            <beacon:label property="ssoId" required="true">
              <cms:contentText key="SSO_ID" code="participant.participant"/>  
            </beacon:label>	
            <td class="content-field"> 
				<html:text property="ssoId" size="30" maxlength="40" styleClass="content-field"/>
            </td>
          </tr>
          </c:if>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          
          <tr class="form-row-spacer">
                    <beacon:label property="welcomeEmailSent">
                       <cms:contentText key="WELCOME_EMAIL_RECIEVED" code="participant.participant"/>
                    </beacon:label> 
                   <td class="content-field">  
			         <html:select property="welcomeEmailSent">                          
                         <html:option value="true"><cms:contentText code="system.common.labels" key="YES" /></html:option>
                         <html:option value="false"><cms:contentText code="system.common.labels" key="NO" /></html:option>
                     </html:select>
			       </td>
		    </tr>
		  
		 <%-- Opt Out Awards Section - Start --%>  
		 <c:if test="${allowOptOutAwards }">   
	          <tr class="form-row-spacer">
	                    <beacon:label property="optOutAwards">
	                       <cms:contentText key="OPT_OUT_AWARDS" code="participant.participant"/>
	                    </beacon:label> 
	                   <td class="content-field">  
				         <html:select property="optOutAwards">                          
	                         <html:option value="true"><cms:contentText code="system.common.labels" key="YES" /></html:option>
	                         <html:option value="false"><cms:contentText code="system.common.labels" key="NO" /></html:option>
	                     </html:select>
				       </td>
			    </tr>
		    </c:if>	
		  <%-- Opt Out Awards Section - End --%>
		  
		<%-- Opt Out Of Program Section - Start --%>  
		 <c:if test="${allowOptOutOfProgram }">   
	          <tr class="form-row-spacer">
	                    <beacon:label property="optOutAwards">
	                       <cms:contentText key="OPT_OUT_PROGRAM" code="participant.participant"/>
	                    </beacon:label> 
	                   <td class="content-field">  
				         <html:select property="optOutOfProgram">                          
	                         <html:option value="true"><cms:contentText code="system.common.labels" key="YES" /></html:option>
	                         <html:option value="false"><cms:contentText code="system.common.labels" key="NO" /></html:option>
	                     </html:select>
				       </td>
			    </tr>
		    </c:if>	
		  <%-- Opt Out Of Program Section - End --%>	 
		  	    
       
          <c:choose>
			  <c:when test="${userForm.termsConditionsRequired}">
			  	   <c:if test="${userForm.userAllowedToAcceptForPax }">	
				     <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">  						
						<tr class="form-row-spacer">			  
				             <beacon:label property="paxTermsAccept" required="true">
				               <cms:contentText key="PARTICIPANT_TERMS_ACCEPT" code="participant.participant"/>
				              </beacon:label>
				             <td class="content-field">  
				               	<html:select property="paxTermsAccept" styleClass="content-field">
							  	<html:options collection="paxTermsAcceptList" property="code" labelProperty="name"  />
								</html:select>
				             </td>
				        </tr>
					  </beacon:authorize>
					</c:if>
					<c:if test="${!userForm.userAllowedToAcceptForPax }">	
						<tr class="form-row-spacer">			  
					    	<beacon:label property="paxTermsAccept" required="false">
					           <cms:contentText key="PARTICIPANT_TERMS_ACCEPT" code="participant.participant"/>
					        </beacon:label>
					    	<td class="content-field">  
					      		<c:out value="${userForm.paxTermsAcceptDesc}" />
					       	</td>
						</tr>	
					</c:if>
					<c:if test="${userForm.paxTermsAcceptDesc == 'accepted' or userForm.paxTermsAcceptDesc == 'declined'}">
						<tr class="form-row-spacer">
							<td></td><td></td>
							<td class="content-field">
								<cms:contentText key="BY" code="forum.library" />
								<c:out value="${userForm.termsAcceptedBy}" />							
								<c:out value="${userForm.termsAcceptedDate}" />
							</td>
						</tr>
					</c:if>
				</c:when>						
				<c:otherwise>
					<tr class="form-row-spacer">			  
			        	<beacon:label property="paxTermsAccept" required="false">
			                <cms:contentText key="PARTICIPANT_TERMS_ACCEPT" code="participant.participant"/>
			            </beacon:label>
			            <td class="content-field">  
					        <cms:contentText key="NOT_APPLICABLE" code="participant.participant"/>
					    </td>
					</tr>
				</c:otherwise>
			</c:choose>
	
          <tr class="form-blank-row">
            <td></td>
          </tr>	
							
		  <tr class="form-buttonrow">
	         <td></td>
	         <td></td>
	         <td align="left">
	         <beacon:authorize ifNotGranted="LOGIN_AS">
	             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('updatePersonalInfo')">
					<cms:contentText code="system.button" key="SAVE" />
				</html:submit>
             </beacon:authorize>
             
				<html:cancel styleClass="content-buttonstyle">
					<cms:contentText code="system.button" key="CANCEL" />
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
      inputField  : "dateOfBirth",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "dateOfBirthTrigger"       // ID of the button
    }
  );
</script>
