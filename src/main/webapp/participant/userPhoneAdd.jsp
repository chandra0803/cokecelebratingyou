<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userPhoneMaintainCreate">
	<html:hidden property="method" />
	<html:hidden property="fromPaxScreen"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userPhoneForm.userId}"/>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_ADD" code="participant.phone"/></span>
         <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="participant.phone"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
        
          <tr class="form-row-spacer">				  
            <beacon:label property="phoneType" required="true">
              <cms:contentText key="PHONE_TYPE" code="participant.phone"/>
            </beacon:label>	
            <td class="content-field">
            	<html:select property="phoneType" styleClass="content-field">
					<html:options collection="phoneTypes" property="code" labelProperty="name"  />
				</html:select>
            </td>
        </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="countryPhoneCode" >
			             	<cms:contentText key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE" code="participant.participant"/>
			            </beacon:label>	
            <td class="content-field" nowrap>
            <html:select property="countryPhoneCode" styleClass="content-field">
		<%-- Bug Fix 23605 start --%>
		<c:if test="${multiple}">
			<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
		</c:if>
		<%-- Bug Fix 23605 end --%>
			<html:options collection="countryList" property="countryCode" labelProperty="countryNameandPhoneCodeDisplay"  />
							
</html:select>       
            </td>
          </tr>
          
           <tr class="form-row-spacer">				  
              <beacon:label property="phoneNbr" >
			             	<cms:contentText key="PHONE_NUMBER" code="participant.participant"/>
			            </beacon:label>	
			            
            <td class="content-field" nowrap>            
	            <html:text property="phoneNbr" size="8" styleClass="content-field"/>       
            </td>
          </tr>
           <tr class="form-row-spacer">				  
          <beacon:label property="phoneExt" >
			             	<cms:contentText key="EXTENSION" code="participant.participant"/>
			            </beacon:label>	
			<td class="content-field" nowrap>      
	            <html:text property="phoneExt" size="3" styleClass="content-field"/>	            
            </td>
          </tr>
            <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
           

        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left"> 
            <beacon:authorize ifNotGranted="LOGIN_AS">      		
				<html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
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


		