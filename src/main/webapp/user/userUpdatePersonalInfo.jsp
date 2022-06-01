<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userMaintainUpdatePersonalInfo">
	<html:hidden property="method"/>
	<html:hidden property="version"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${userForm.id}"/>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_UPDATE_PERSONAL_INFO" code="user.user"/></span>
         <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>

        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="user.user"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        
        <table>
        
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
      		<tr class="form-blank-row">
			  <td></td>
			</tr>	
      		<tr class="form-row-spacer">				  
	            <beacon:label property="userType" required="true">
	              <cms:contentText key="USER_TYPE" code="user.user"/>
	            </beacon:label>	
	            <td class="content-field">
	            	<html:select property="userType" styleClass="content-field">
						<html:options collection="userTypeList" property="code" labelProperty="name"  />
					</html:select>
	            </td>
           </tr>
        
	        <tr class="form-blank-row">
			  <td></td>
			</tr>	
        <tr class="form-row-spacer">				  
            <beacon:label property="title" required="false">
              <cms:contentText key="PREFIX" code="user.user"/>
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
  			<cms:contentText key="FIRST_NAME" code="user.user"/>
		  </beacon:label>	
		  <td class="content-field">
			<html:text property="firstName" size="30" maxlength="40" styleClass="content-field"/>
		  </td>
		</tr>
		
			<tr class="form-blank-row">
			  <td></td>
			</tr>
				
		<tr class="form-row-spacer">				  
		  <beacon:label property="middleName" required="false">
  			<cms:contentText key="MIDDLE_NAME" code="user.user"/>
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
  			<cms:contentText key="LAST_NAME" code="user.user"/>
		  </beacon:label>	
		  <td class="content-field">
			<html:text property="lastName" size="30" maxlength="40" styleClass="content-field"/>
		  </td>
		</tr>
		
			<tr class="form-blank-row">
			  <td></td>
			</tr>
		
		 <tr class="form-row-spacer">				  
            <beacon:label property="suffix" required="false">
              <cms:contentText key="SUFFIX" code="user.user"/>
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
			
			<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
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
		<%--END BUTTON ROW--%>
				
		</table>
		</td>
	</tr>
</table>
</html:form>