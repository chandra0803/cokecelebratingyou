<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userMaintainCreateInternal" focus="firstName">
<html:hidden property="method" value="create" />
	<beacon:client-state>
		<beacon:client-state-entry name="password2" value="${userForm.password2}"/>
		<beacon:client-state-entry name="confirmPassword2" value="${userForm.confirmPassword2}"/>
		<beacon:client-state-entry name="passwordSystemGenerated" value="${userForm.passwordSystemGenerated}"/>
	</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_ADD_BI_USER" code="user.user"/></span>
         
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="user.user"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
       
        <cms:errors/>

		<table width="100%" cellpadding="3" cellspacing="1">
		  <tr>
			<td width="49%" valign="top">
				<table cellpadding="3" cellspacing="1">							
					<%-- START Personal Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="PERSONAL_INFO_HEADER" code="user.user"/>
						</td>
					</tr>
					
					<tr class="form-row-spacer">				  
		              <beacon:label property="active" required="true">
		                <cms:contentText key="USER_STATUS" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		              	<html:select property="active" styleClass="content-field">
	        				<html:options collection="statusList" property="code" labelProperty="name"  />
	       				</html:select>
		              </td>
			        </tr>
					

					 <tr class="form-row-spacer">				  
		              <beacon:label property="title" required="false">
		                <cms:contentText key="PREFIX" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		              	<html:select property="title" styleClass="content-field">
							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>	
							<html:options collection="titleList" property="code" labelProperty="name"  />
						</html:select>
		              </td>
			        </tr>
						      

 					<tr class="form-row-spacer">				  
		              <beacon:label property="firstName" required="true">
		                <cms:contentText key="FIRST_NAME" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="firstName" size="30" styleClass="content-field"/>
		              </td>
			        </tr>
						      
			        
			        <tr class="form-row-spacer">				  
		              <beacon:label property="middleName" required="false">
		                <cms:contentText key="MIDDLE_NAME" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="middleName" size="30" styleClass="content-field"/>
		              </td>
			        </tr>
						  
			        
			         <tr class="form-row-spacer">				  
		              <beacon:label property="lastName" required="true">
		                <cms:contentText key="LAST_NAME" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="lastName" size="30" styleClass="content-field"/>
		              </td>
			        </tr>
						      

					<tr class="form-row-spacer">				  
		              <beacon:label property="suffix" required="false">
		                <cms:contentText key="SUFFIX" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		              	<html:select property="suffix" styleClass="content-field">
							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>	
							<html:options collection="suffixList" property="code" labelProperty="name"  />
						</html:select>
		              </td>
			        </tr>
						   

    				<tr class="form-row-spacer">				  
		              <beacon:label property="emailType" required="true">
		                <cms:contentText key="PRIMARY_EMAIL_TYPE" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		              	<html:select property="emailType" styleClass="content-field">
							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>	
							<html:options collection="emailTypeList" property="code" labelProperty="name"  />
						</html:select>
		              </td>
			        </tr>
						
			        
			        <tr class="form-row-spacer">				  
		              <beacon:label property="emailAddress" required="true">
		                <cms:contentText key="EMAIL_ADDRESS" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="emailAddress" size="30" styleClass="content-field"/>
		              </td>
			        </tr>
						
			        
	         		<tr class="form-row-spacer">				  
		              <beacon:label property="phoneType" required="false">
		                <cms:contentText key="PRIMARY_PHONE_TYPE" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		              	<html:select property="phoneType" styleClass="content-field">
							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>	
							<html:options collection="phoneTypeList" property="code" labelProperty="name"  />
						</html:select>
		              </td>
			        </tr>
						
			        
			         <tr class="form-row-spacer">				  
		              <beacon:label property="countryPhoneCode" >
			             	<cms:contentText key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE" code="participant.participant"/>
			            </beacon:label>
		              <td class="content-field">
		              <html:select
					property="countryPhoneCode" styleClass="content-field">
					<%-- Bug Fix 23605 start --%>
					<c:if test="${multiple}">
						<html:option value=''>
							<cms:contentText key="SELECT_ONE" code="system.general" />
						</html:option>
					</c:if>
					<%-- Bug Fix 23605 end --%>
					<c:forEach items="${countryList}" var="country">
					<option value="${country.countryCode}" >${country.countryNameandPhoneCodeDisplay}</option>
					</c:forEach>
					</html:select> 	
		              </td>
			        </tr>
			        
			       
			         <tr class="form-row-spacer">				  
		              <beacon:label property="phoneNumber" >
			             	<cms:contentText key="PHONE_NUMBER" code="participant.participant"/>
			            </beacon:label>		
		              <td class="content-field">		            
		                <html:text property="phoneNumber" size="8" styleClass="content-field"/>		            
		              </td>
			        </tr> 
			        
			         <tr class="form-row-spacer">				  
		            <beacon:label property="phoneExtension" >
			             	<cms:contentText key="EXTENSION" code="participant.participant"/>
			            </beacon:label>	
		              <td class="content-field">
		                 <html:text property="phoneExtension" size="3" maxlength="10" styleClass="content-field"/>		                  
		              </td>
			        </tr>
								       		<%-- END Personal Information --%>
				</table> <%-- End First Column Table --%>
			</td>
			<td width="2%" valign="top">&nbsp;</td>

			<td width="49%" valign="top">
				<table cellpadding="3" cellspacing="1">	
					<%-- START Web Site Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="WEB_SITE_INFO_HEADER" code="user.user"/>
						</td>
					</tr>
					
					<tr class="form-row-spacer">				  
			            <beacon:label property="userName" required="true">
			             	<cms:contentText key="LOGIN_ID" code="user.user"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:text property="userName" size="30" maxlength="40" styleClass="content-field"/>
				        </td>              
          			</tr>								
					<%-- START Roles Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="ROLE_HEADER" code="user.user"/>
						</td>
					</tr>	

					<tr class="form-row-spacer">				  
		              <beacon:label property="role" required="true">
		                <cms:contentText key="ROLE" code="user.user"/>
		              </beacon:label>	
		              <td class="content-field">
		              	<html:select property="role" styleClass="content-field">
	        				<html:options collection="allRoles" property="id" labelProperty="name"  />
	       				</html:select>
		              </td>
			        </tr>

	   			</table> <%-- end 2nd column table --%>
			</td>
		  </tr>
		</table>	
		
		<table width="100%" cellpadding="3" cellspacing="1" >
			<tr class="form-buttonrow">            
                <td align="center">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                  	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('createInternal')">
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
		